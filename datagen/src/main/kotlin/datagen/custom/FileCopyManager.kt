package datagen.custom

import java.io.File
import java.nio.file.Path

object FileCopyManager {
  private var sourceRoot: Path? = null
  private var destinationRoot: Path? = null
  private val fileCopies = mutableListOf<Pair<String, String>>()

  fun setupRoot(sourceRoot: Path, destinationRoot: Path) {
    this.sourceRoot = sourceRoot
    this.destinationRoot = destinationRoot
  }

  fun addFileCopy(source: String, destination: String) {
    fileCopies.add(source to destination)
  }

  fun copyFiles() {
    if (sourceRoot == null || destinationRoot == null) throw IllegalStateException("Root is null")
    fileCopies.forEach { (source, destination) ->
      val sourceFile = File(sourceRoot!!.toFile(), source)
      val destinationFile = File(destinationRoot!!.toFile(), destination)
      sourceFile.copyTo(destinationFile, overwrite = true)
    }
  }
}