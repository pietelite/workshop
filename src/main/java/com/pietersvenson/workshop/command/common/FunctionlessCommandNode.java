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

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class FunctionlessCommandNode extends CommandTree.CommandNode {

  public FunctionlessCommandNode(@Nullable CommandTree.CommandNode parent,
                                 @Nullable Permission permission,
                                 @Nonnull String description,
                                 @Nonnull String primaryAlias) {
    super(parent, permission, description, primaryAlias);
  }

  public FunctionlessCommandNode(@Nullable CommandTree.CommandNode parent,
                                 @Nullable Permission permission,
                                 @Nonnull String description,
                                 @Nonnull String primaryAlias,
                                 boolean addHelp,
                                 boolean active) {
    super(parent, permission, description, primaryAlias);
  }

  @Override
  public final boolean onWrappedCommand(@Nonnull CommandSender sender,
                                        @Nonnull Command command,
                                        @Nonnull String label,
                                        @Nonnull String[] args) {
    sendCommandError(sender, CommandError.FEW_ARGUMENTS);
    return false;
  }
}
