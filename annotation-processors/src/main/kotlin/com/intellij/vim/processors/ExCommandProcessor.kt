/*
 * Copyright 2003-2023 The IdeaVim authors
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.txt file or at
 * https://opensource.org/licenses/MIT.
 */

package com.intellij.vim.processors

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.intellij.vim.FileWriter
import com.intellij.vim.annotations.ExCommand

class ExCommandProcessor(private val environment: SymbolProcessorEnvironment): SymbolProcessor {
  companion object {
    const val comment = """# This file was automatically generated by [com.intellij.vim.processor.EXCommandProcessor].
# If you are going to change it, you are probably doing something wrong, as your changes will be overridden by the next `gradle kspKotlin` run.

"""
  }
  private val visitor = EXCommandVisitor()
  private val writer = FileWriter()
  private val commandToClass = mutableMapOf<String, String>()

  override fun process(resolver: Resolver): List<KSAnnotated> {
    resolver.getAllFiles().forEach { it.accept(visitor, Unit) }
    val filePath = environment.options["generated_directory"]!! + "/" + environment.options["ex_commands_file"]!!
    writer.writeFile(filePath, writer.getYAML(comment, commandToClass))
    return emptyList()
  }

  private inner class EXCommandVisitor : KSVisitorVoid() {
    @OptIn(KspExperimental::class)
    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
      val exCommandAnnotation = classDeclaration.getAnnotationsByType(ExCommand::class).firstOrNull() ?: return
      commandToClass[exCommandAnnotation.command] = classDeclaration.qualifiedName!!.asString()
    }

    override fun visitFile(file: KSFile, data: Unit) {
      file.declarations.forEach { it.accept(this, Unit) }
    }
  }
}