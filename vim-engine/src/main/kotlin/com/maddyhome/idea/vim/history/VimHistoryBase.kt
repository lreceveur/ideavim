/*
 * Copyright 2003-2023 The IdeaVim authors
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.txt file or at
 * https://opensource.org/licenses/MIT.
 */

package com.maddyhome.idea.vim.history

import com.maddyhome.idea.vim.diagnostic.VimLogger
import com.maddyhome.idea.vim.diagnostic.debug
import com.maddyhome.idea.vim.diagnostic.vimLogger

open class VimHistoryBase : VimHistory {
  val histories: MutableMap<VimHistory.Type, HistoryBlock> = mutableMapOf()

  @Deprecated("Please use fun addEntry(type: Type, text: String)")
  override fun addEntry(key: String, text: String) {
    val type = getTypeForString(key)
    addEntry(type, text)

  }

  override fun addEntry(type: VimHistory.Type, text: String) {
    val block = blocks(type)
    block.addEntry(text)
  }

  @Deprecated("Please use fun getEntries(type: Type, text: String)")
  override fun getEntries(key: String, first: Int, last: Int): List<HistoryEntry> {
    val type = getTypeForString(key)
    return getEntries(type, first, last)
  }

  override fun getEntries(type: VimHistory.Type, first: Int, last: Int): List<HistoryEntry> {
    var myFirst = first
    var myLast = last
    val block = blocks(type)

    val entries = block.getEntries()
    val res = ArrayList<HistoryEntry>()
    if (myFirst < 0) {
      myFirst = if (-myFirst > entries.size) {
        Integer.MAX_VALUE
      } else {
        val entry = entries[entries.size + myFirst]
        entry.number
      }
    }
    if (myLast < 0) {
      myLast = if (-myLast > entries.size) {
        Integer.MIN_VALUE
      } else {
        val entry = entries[entries.size + myLast]
        entry.number
      }
    } else if (myLast == 0) {
      myLast = Integer.MAX_VALUE
    }

    logger.debug { "first=$myFirst\nlast=$myLast" }

    for (entry in entries) {
      if (entry.number in myFirst..myLast) {
        res.add(entry)
      }
    }

    return res
  }

  private fun blocks(type: VimHistory.Type): HistoryBlock {
    return histories.getOrPut(type) { HistoryBlock() }
  }

  protected fun getTypeForString(key: String): VimHistory.Type {
    return when (key) {
      HistoryConstants.SEARCH -> VimHistory.Type.Search
      HistoryConstants.COMMAND -> VimHistory.Type.Command
      HistoryConstants.EXPRESSION -> VimHistory.Type.Expression
      HistoryConstants.INPUT -> VimHistory.Type.Input
      else -> VimHistory.Type.Custom(key)
    }
  }

  companion object {
    val logger: VimLogger = vimLogger<VimHistoryBase>()
  }
}
