/*
 * Copyright 2003-2023 The IdeaVim authors
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.txt file or at
 * https://opensource.org/licenses/MIT.
 */
package com.maddyhome.idea.vim.action.motion.leftright

import com.intellij.vim.annotations.CommandOrMotion
import com.intellij.vim.annotations.Mode
import com.maddyhome.idea.vim.api.ExecutionContext
import com.maddyhome.idea.vim.api.ImmutableVimCaret
import com.maddyhome.idea.vim.api.VimEditor
import com.maddyhome.idea.vim.api.injector
import com.maddyhome.idea.vim.api.options
import com.maddyhome.idea.vim.command.Argument
import com.maddyhome.idea.vim.command.MotionType
import com.maddyhome.idea.vim.command.OperatorArguments
import com.maddyhome.idea.vim.handler.Motion
import com.maddyhome.idea.vim.handler.MotionActionHandler

@CommandOrMotion(keys = ["<BS>", "<C-H>"], modes = [Mode.NORMAL, Mode.VISUAL, Mode.OP_PENDING])
class MotionBackspaceAction : MotionActionHandler.ForEachCaret() {
  override fun getOffset(
    editor: VimEditor,
    caret: ImmutableVimCaret,
    context: ExecutionContext,
    argument: Argument?,
    operatorArguments: OperatorArguments,
  ): Motion {
    val allowWrap = injector.options(editor).whichwrap.contains("b")
    return injector.motion.getHorizontalMotion(editor, caret, -operatorArguments.count1, allowPastEnd = false, allowWrap)
  }

  override val motionType: MotionType = MotionType.EXCLUSIVE
}

@CommandOrMotion(keys = ["<Space>"], modes = [Mode.NORMAL, Mode.VISUAL, Mode.OP_PENDING])
class MotionSpaceAction : MotionActionHandler.ForEachCaret() {
  override fun getOffset(
    editor: VimEditor,
    caret: ImmutableVimCaret,
    context: ExecutionContext,
    argument: Argument?,
    operatorArguments: OperatorArguments,
  ): Motion {
    val allowWrap = injector.options(editor).whichwrap.contains("s")
    return injector.motion.getHorizontalMotion(editor, caret, operatorArguments.count1, allowPastEnd = false, allowWrap)
  }

  override val motionType: MotionType = MotionType.EXCLUSIVE
}
