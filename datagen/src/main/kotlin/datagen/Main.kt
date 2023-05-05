package datagen

import tada.lib.generator.ResourceGenerator
import java.nio.file.Path

fun main(args: Array<String>) {
  println("Hello from datagen!")
  if (args.isEmpty()) throw IllegalArgumentException("Must provide an output directory")
  val generator = ResourceGenerator.create("derelict", Path.of(args[0])).apply {
    // Assets to generate

  }.generate()
}