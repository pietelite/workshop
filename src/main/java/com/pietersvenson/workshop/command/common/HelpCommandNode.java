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

import com.pietersvenson.workshop.util.Format;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.Objects;

final class HelpCommandNode extends CommandNode {

  public HelpCommandNode(@Nonnull CommandNode parent) {
    super(parent,
        null,
        "Get help for this command",
        "help",
        false,
        null);
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
            .append("  ")
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
              .append("  ")
              .append(ChatColor.WHITE)
              .append(parent.getParameterDescription(parameter));
          sender.sendMessage(builder.toString());
        });
      }
    }
    return true;

  }

}
