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

package com.pietersvenson.workshop.features.classes.command;

import com.pietersvenson.workshop.command.common.CommandNode;
import com.pietersvenson.workshop.command.common.FunctionlessCommandNode;
import com.pietersvenson.workshop.features.classes.command.edit.CurriculumCommand;
import com.pietersvenson.workshop.features.classes.command.edit.NameCommand;
import com.pietersvenson.workshop.features.classes.command.edit.ParticipantsCommand;
import com.pietersvenson.workshop.features.classes.command.edit.ScheduleCommand;
import com.pietersvenson.workshop.permission.Permissions;

import javax.annotation.Nullable;

public class ClassroomEditCommand extends FunctionlessCommandNode {
  public ClassroomEditCommand(@Nullable CommandNode parent) {
    super(parent,
        Permissions.STAFF,
        "Edits the parameters of a class",
        "edit");
    addAliases("e");
    addChildren(new NameCommand(this),
        new CurriculumCommand(this),
        new ScheduleCommand(this),
        new ParticipantsCommand(this));
  }

}
