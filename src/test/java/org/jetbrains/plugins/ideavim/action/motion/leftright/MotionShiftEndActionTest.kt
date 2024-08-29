/*
 * Copyright 2003-2023 The IdeaVim authors
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.txt file or at
 * https://opensource.org/licenses/MIT.
 */

@file:Suppress("RemoveCurlyBracesFromTemplate")

package org.jetbrains.plugins.ideavim.action.motion.leftright

import com.maddyhome.idea.vim.api.injector
import com.maddyhome.idea.vim.options.OptionConstants
import com.maddyhome.idea.vim.state.mode.Mode
import com.maddyhome.idea.vim.state.mode.SelectionType
import org.jetbrains.plugins.ideavim.SkipNeovimReason
import org.jetbrains.plugins.ideavim.TestOptionConstants
import org.jetbrains.plugins.ideavim.TestWithoutNeovim
import org.jetbrains.plugins.ideavim.VimTestCaseBase
import org.jetbrains.plugins.ideavim.impl.OptionTest
import org.jetbrains.plugins.ideavim.impl.TraceOptions
import org.jetbrains.plugins.ideavim.impl.VimOption

@TraceOptions(TestOptionConstants.keymodel, TestOptionConstants.selectmode)
class MotionShiftEndActionTest : VimTestCaseBase() {
  @TestWithoutNeovim(SkipNeovimReason.OPTION)
  @OptionTest(
    VimOption(TestOptionConstants.keymodel, doesntAffectTest = true),
    VimOption(TestOptionConstants.selectmode, doesntAffectTest = true),
  )
  fun `test simple end`() {
    val keys = listOf("<S-End>")
    val before = """
            A Discovery

            I found it in a ${c}legendary land
            all rocks and lavender and tufted grass,
            where it was settled on some sodden sand
            hard by the torrent of a mountain pass.
    """.trimIndent()
    val after = """
            A Discovery

            I found it in a legendary lan${c}d
            all rocks and lavender and tufted grass,
            where it was settled on some sodden sand
            hard by the torrent of a mountain pass.
    """.trimIndent()
    doTest(keys, before, after, Mode.NORMAL())
  }

  @TestWithoutNeovim(SkipNeovimReason.OPTION)
  @OptionTest(
    VimOption(TestOptionConstants.keymodel, limitedValues = [OptionConstants.keymodel_startsel]),
    VimOption(TestOptionConstants.selectmode, limitedValues = [""]),
  )
  fun `test start visual`() {
    val keys = listOf("<S-End>")
    val before = """
            A Discovery

            I found it in a ${c}legendary land
            all rocks and lavender and tufted grass,
            where it was settled on some sodden sand
            hard by the torrent of a mountain pass.
    """.trimIndent()
    val after = """
            A Discovery

            I found it in a ${s}legendary land${c}${se}
            all rocks and lavender and tufted grass,
            where it was settled on some sodden sand
            hard by the torrent of a mountain pass.
    """.trimIndent()
    doTest(keys, before, after, Mode.VISUAL(SelectionType.CHARACTER_WISE))
  }

  @TestWithoutNeovim(SkipNeovimReason.OPTION)
  @OptionTest(
    VimOption(TestOptionConstants.keymodel, limitedValues = [OptionConstants.keymodel_startsel]),
    VimOption(TestOptionConstants.selectmode, limitedValues = [OptionConstants.selectmode_key]),
  )
  fun `test start select`() {
    val keys = listOf("<S-End>")
    val before = """
            A Discovery

            I found it in a ${c}legendary land
            all rocks and lavender and tufted grass,
            where it was settled on some sodden sand
            hard by the torrent of a mountain pass.
    """.trimIndent()
    val after = """
            A Discovery

            I found it in a ${s}legendary land${c}${se}
            all rocks and lavender and tufted grass,
            where it was settled on some sodden sand
            hard by the torrent of a mountain pass.
    """.trimIndent()
    doTest(keys, before, after, Mode.SELECT(SelectionType.CHARACTER_WISE))
  }

  @OptionTest(
    VimOption(TestOptionConstants.keymodel, limitedValues = [""]),
    VimOption(TestOptionConstants.selectmode, limitedValues = [""]),
  )
  @TestWithoutNeovim(SkipNeovimReason.OPTION)
  fun `test continue visual`() {
    val before = """
            A Discovery

            I found it in a ${c}legendary land
            all rocks and lavender and tufted grass,
            where it was settled on some sodden sand
            hard by the torrent of a mountain pass.
    """.trimIndent()
    val after = """
            A Discovery

            ${s}I found it in a legendary land${c}${se}
            all rocks and lavender and tufted grass,
            where it was settled on some sodden sand
            hard by the torrent of a mountain pass.
    """.trimIndent()
    configureByText(before)
    typeText(injector.parser.parseKeys("<S-End>"))
    assertState(Mode.NORMAL())
    typeText(injector.parser.parseKeys("0v" + "<S-End>"))
    assertState(after)
    assertState(Mode.VISUAL(SelectionType.CHARACTER_WISE))
  }

  @OptionTest(
    VimOption(TestOptionConstants.keymodel, limitedValues = [""]),
    VimOption(TestOptionConstants.selectmode, limitedValues = [""]),
  )
  @TestWithoutNeovim(SkipNeovimReason.OPTION)
  fun `test continue select`() {
    val before = """
            A Discovery

            I found it in a ${c}legendary land
            all rocks and lavender and tufted grass,
            where it was settled on some sodden sand
            hard by the torrent of a mountain pass.
    """.trimIndent()
    val after = """
            A Discovery

            ${s}I found it in a legendary land${c}${se}
            all rocks and lavender and tufted grass,
            where it was settled on some sodden sand
            hard by the torrent of a mountain pass.
    """.trimIndent()
    configureByText(before)
    typeText(injector.parser.parseKeys("<S-End>"))
    assertState(Mode.NORMAL())
    typeText(injector.parser.parseKeys("0gh" + "<S-End>"))
    assertState(after)
    assertState(Mode.SELECT(SelectionType.CHARACTER_WISE))
  }
}
