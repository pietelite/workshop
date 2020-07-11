/*
 * MIT License
 *
 * Copyright (c) 2020 Pieter Svenson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.pietersvenson.workshop.command.common;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pietersvenson.workshop.features.Enablee;
import com.pietersvenson.workshop.util.Format;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

public class CommandTree {

  private CommandNode root;

  public CommandTree(@Nonnull CommandNode root) {
    this.root = Objects.requireNonNull(root);
  }

  public CommandNode root() {
    return root;
  }

  /**
   * Register all functionality of a command, noted in the plugin.yml.
   *
   * @param plugin the plugin under which this command is registered
   * @param root   the root CommandNode of the entire command tree,
   *               whose name is registered in the plugin.yml
   * @return the tree of all commands with the given root
   */
  public static CommandTree register(@Nonnull JavaPlugin plugin, @Nonnull CommandNode root) {
    CommandTree tree = new CommandTree(root);
    PluginCommand command = Objects.requireNonNull(plugin.getCommand(root.getPrimaryAlias()));
    command.setExecutor(root);
    command.setTabCompleter(root);
    root.getPermission().map(Permission::getName).ifPresent(command::setPermission);
    return tree;
  }

  public abstract static class CommandNode extends Enablee implements CommandExecutor, TabCompleter {

    public static final int ARG_MAX_LENGTH = 20;

    private final CommandNode parent;
    private final Permission permission;
    private final String description;
    private final List<String> aliases = Lists.newLinkedList();
    private final List<CommandNode> children = Lists.newLinkedList();
    private final Map<Parameter, String> parameters = Maps.newLinkedHashMap();

    /**
     * Simple constructor.
     *
     * @param parent       parent node, null if none
     * @param permission   permission allowing this command
     * @param description  describes the function of this command
     * @param primaryAlias the primary label used to call this command
     */
    public CommandNode(@Nullable CommandNode parent,
                       @Nullable Permission permission,
                       @Nonnull String description,
                       @Nonnull String primaryAlias) {
      this(parent, permission, description, primaryAlias, true, () -> true);

    }

    /**
     * Full constructor.
     *
     * @param parent       parent node, null if none
     * @param permission   permission allowing this command
     * @param description  describes the function of this command
     * @param primaryAlias the primary label used to call this command
     * @param addHelp      whether a help sub-command is generated
     */
    public CommandNode(@Nullable CommandNode parent,
                       @Nullable Permission permission,
                       @Nonnull String description,
                       @Nonnull String primaryAlias,
                       boolean addHelp,
                       Supplier<Boolean> enabler) {
      super(enabler);
      Objects.requireNonNull(description);
      Objects.requireNonNull(primaryAlias);
      this.parent = parent;
      this.permission = permission;
      this.description = description;
      this.aliases.add(primaryAlias);
      if (addHelp) {
        this.children.add(new HelpCommandNode(this));
      }
    }

    // Getters and Setters

    @Nullable
    public final CommandNode getParent() {
      return parent;
    }

    @Nonnull
    public final Optional<Permission> getPermission() {
      return Optional.ofNullable(permission);
    }

    @Nonnull
    public final String getDescription() {
      return description;
    }

    @Nonnull
    public final String getPrimaryAlias() {
      return aliases.get(0);
    }

    @Nonnull
    public final List<String> getAliases() {
      return aliases;
    }

    public final void addAliases(@Nonnull String... aliases) {
      this.aliases.addAll(Arrays.asList(aliases));
    }

    @Nonnull
    public final List<Parameter> getParameters() {
      return Lists.newLinkedList(parameters.keySet());
    }

    public final void addSubcommand(@Nonnull Parameter parameter, @Nonnull String description) {
      this.parameters.put(parameter, description);
    }

    @Nonnull
    public final String getFullCommand() {
      StringBuilder command = new StringBuilder(getPrimaryAlias());
      CommandNode cur = this;
      while (!cur.isRoot()) {
        command.insert(0, cur.parent.getPrimaryAlias() + " ");
        cur = cur.parent;
      }
      return command.toString();
    }

    public final void addChildren(CommandNode... nodes) {
      this.children.addAll(Arrays.asList(nodes));
    }

    @Nonnull
    public final List<CommandNode> getChildren() {
      return children;
    }

    public final boolean isRoot() {
      return parent == null;
    }

    public final void sendCommandError(CommandSender sender, String error) {
      sender.sendMessage(Format.error(error));
      sender.sendMessage(Format.error("Try: " + ChatColor.GRAY + "/" + getFullCommand() + " help"));
    }

    public final void sendCommandError(CommandSender sender, CommandError error) {
      sendCommandError(sender, error.getMessage());
    }

    @Override
    public final boolean onCommand(@Nonnull CommandSender sender,
                                   @Nonnull Command command,
                                   @Nonnull String label,
                                   @Nonnull String[] args) {
      if (!enabled()) {
        sender.sendMessage(Format.error("This command is not enabled"));
        return false;
      }

      // Adds support for quotations around space-delimited arguments
      String[] actualArgs;
      if (isRoot()) {
        actualArgs = Format.combineQuotedArguments(args);
        for (String arg : actualArgs) {
          if (arg.length() > ARG_MAX_LENGTH) {
            sender.sendMessage(Format.error("Arguments cannot exceed "
                + ARG_MAX_LENGTH
                + " characters!"));
            return false;
          }
        }
      } else {
        actualArgs = Arrays.copyOf(args, args.length);
      }

      // Main method
      if (this.permission != null && !sender.hasPermission(this.permission)) {
        sender.sendMessage(Format.error("You don't have permission to do this!"));
        return false;
      }
      if (actualArgs.length < 1) {
        return onWrappedCommand(sender, command, label, actualArgs);
      }
      for (CommandNode child : children) {
        for (String alias : child.aliases) {
          if (alias.equalsIgnoreCase(actualArgs[0])) {
            return child.onCommand(sender, command, child.getPrimaryAlias(), Arrays.copyOfRange(actualArgs, 1, actualArgs.length));
          }
        }
      }
      return onWrappedCommand(sender, command, label, actualArgs);
    }

    public abstract boolean onWrappedCommand(@Nonnull CommandSender sender,
                                             @Nonnull Command command,
                                             @Nonnull String label,
                                             @Nonnull String[] args);

    @Override
    public final List<String> onTabComplete(@Nonnull CommandSender sender,
                                            @Nonnull Command command,
                                            @Nonnull String label,
                                            @Nonnull String[] args) {
      List<String> cmds = Lists.newLinkedList();
      if (!enabled()) {
        return cmds;
      }
      if (this.permission != null && !sender.hasPermission(this.permission)) {
        return cmds; // empty
      }
      if (args.length == 0) {
        return cmds; // empty
      }
      for (CommandNode child : children) {
        for (int i = 0; i < child.aliases.size(); i++) {
          String alias = child.aliases.get(i);

          // If any alias matches from this child, then bump us up to its children
          if (alias.equalsIgnoreCase(args[0])) {
            return child.onTabComplete(sender,
                command,
                child.getPrimaryAlias(),
                Arrays.copyOfRange(args, 1, args.length));
          }

          // Only if we're on the last arg of the recursion and we're at the primary alias,
          // and we have permission to the command, add it
          if (args.length == 1 && i == 0) {
            if (child.permission == null || sender.hasPermission(child.permission)) {
              cmds.add(alias);
            }
          }
        }
      }

      for (Parameter param : parameters.keySet()) {
        cmds.addAll(param.nextAllowedInputs(sender, Arrays.copyOfRange(args, 0, args.length - 1)));
      }

      List<String> out = Lists.newLinkedList();
      StringUtil.copyPartialMatches(args[args.length - 1], cmds, out);
      Collections.sort(out);
      return out;
    }

  }

  private static final class HelpCommandNode extends CommandNode {

    public HelpCommandNode(@Nonnull CommandNode parent) {
      super(parent,
          null,
          "Get help for this command",
          "help",
          false,
          () -> true);
      Objects.requireNonNull(parent);
      addAliases("?");
    }

    @Override
    public boolean onWrappedCommand(@Nonnull CommandSender sender,
                                    @Nonnull Command command,
                                    @Nonnull String label,
                                    @Nonnull String[] args) {
      CommandNode parent = Objects.requireNonNull(this.getParent());
      sender.sendMessage(Format.success(
          "Command: "
              + ChatColor.GRAY
              + parent.getFullCommand()));
      for (CommandNode node : parent.getChildren()) {
        if (node.getPermission().map(sender::hasPermission).orElse(true)) {
          StringBuilder builder = new StringBuilder();
          builder.append(ChatColor.GRAY)
              .append("> ")
              .append(parent.getPrimaryAlias())
              .append(" ")
              .append(ChatColor.AQUA)
              .append(node.getPrimaryAlias());
          if (node.getChildren().size() > 1 || !node.getParameters().isEmpty()) {
            builder.append(" [ . . . ]");
          }
          builder.append(ChatColor.GRAY)
              .append(" > ")
              .append(ChatColor.WHITE)
              .append(node.getDescription());
          sender.sendMessage(builder.toString());
        }
      }
      for (Parameter parameter : parent.getParameters()) {
        if (parameter.getPermission().map(sender::hasPermission).orElse(true)) {
          parameter.getFullUsage(sender).ifPresent(usage -> {
            StringBuilder builder = new StringBuilder();
            builder.append(ChatColor.GRAY)
                .append("> ")
                .append(parent.getPrimaryAlias())
                .append(" ")
                .append(ChatColor.AQUA)
                .append(usage)
                .append(ChatColor.GRAY)
                .append(" > ")
                .append(ChatColor.WHITE)
                .append(parent.parameters.get(parameter));
            sender.sendMessage(builder.toString());
          });
        }
      }
      return true;

    }

  }

}

