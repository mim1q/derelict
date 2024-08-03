package datagen.custom

import datagen.Constants
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
            { "block/$metal/$it${metal}_trapdoor" },
            { "block/$metal/$it${metal}_chain" },
            { "block/$metal/$it${metal}_beam" },
            { "block/$metal/$it${metal}_beam_top" },
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

    fun getColorsAtlas(atlasDirectory: Path, generatedDirectory: Path, name: String, folder: String, size: Int) =
        ImageAtlas.create(
            atlasDirectory.resolve("${name}_atlas.png").toFile(),
            generatedDirectory.resolve("assets/derelict/textures").toFile(),
            spriteWidth = size,
            spriteHeight = size,
        ) {
            listOf(
                "white", "light_gray", "gray", "black",
                "brown", "red", "orange", "yellow",
                "lime", "green", "cyan", "light_blue",
                "blue", "purple", "magenta", "pink"
            ).forEach {
                sprite("${folder}/${it}_$name")
            }
        }

    fun getEggsAtlas(atlasDirectory: Path, generatedDirectory: Path) = ImageAtlas.create(
        atlasDirectory.resolve("eggs_atlas.png").toFile(),
        generatedDirectory.resolve("assets/derelict/textures").toFile(),
        spriteWidth = 16,
        spriteHeight = 16
    ) {
        Constants.SPAWN_EGGS.forEach {
            sprite("item/${it}_spawn_egg")
        }
    }

    fun getStoneAtlas(atlasDirectory: Path, generatedDirectory: Path, name: String, count: Int) = ImageAtlas.create(
        atlasDirectory.resolve("$name.png").toFile(),
        generatedDirectory.resolve("assets/derelict/textures").toFile(),
        spriteWidth = 16,
        spriteHeight = 16
    ) {
        for (i in 0..<count) {
            sprite("block/${name}/${name}_$i")
        }
    }
}