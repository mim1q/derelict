package datagen.custom

import tada.lib.image.ImageAtlas
import java.nio.file.Path


object ImageAtlases {
  fun getMetalAtlas(atlasDirectory: Path, generatedDirectory: Path, metal: String) = ImageAtlas.create(
    atlasDirectory.resolve("${metal}_atlas.png").toFile(),
    generatedDirectory.resolve("assets/derelict/textures").toFile()
  ) {
    val blocks = listOf<(String) -> String>(
      { "block/$metal/$it${metal}_block" },
      { "block/$metal/${it}cut_${metal}" },
      { "block/$metal/$it${metal}_pillar_top" },
      { "block/$metal/$it${metal}_pillar" },
      { "block/$metal/$it${metal}_grate" },
      { "block/$metal/$it${metal}_chain" },
      { "block/$metal/$it${metal}_beam" },
      { "block/$metal/$it${metal}_chain_link_fence" },
      { "block/$metal/$it${metal}_ladder" },
      { "block/$metal/$it${metal}_patch" },
      { "item/$metal/$it${metal}_barbed_wire" },
      { "item/$metal/$it${metal}_chain" },
      { "item/$metal/$it${metal}_patch" },
      { "item/$metal/$it${metal}_sheet" },
    )
    val types = listOf("", "weathered_", "oxidized_")

    blocks.forEach { block ->
      types.forEach { type ->
        sprite(block(type))
      }
    }
  }
}