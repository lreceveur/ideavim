/*
 * Copyright 2003-2023 The IdeaVim authors
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.txt file or at
 * https://opensource.org/licenses/MIT.
 */

package org.jetbrains.plugins.ideavim.action.change.insert

import com.maddyhome.idea.vim.state.mode.Mode
import org.jetbrains.plugins.ideavim.VimTestCaseBase
import org.junit.jupiter.api.Test

class InsertBeforeCursorActionTest : VimTestCaseBase() {
  @Test
  fun `test check caret shape`() {
    doTest("i", "123", "123", Mode.INSERT)
    assertCaretsVisualAttributes()
  }
}
