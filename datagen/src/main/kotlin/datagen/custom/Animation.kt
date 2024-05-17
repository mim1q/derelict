package datagen.custom

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import tada.lib.resources.MinecraftResource
import java.nio.file.Path

class Animation(
    private val frameTime: Int,
    private val interpolate: Boolean,
    private val frames: List<Int>
) : MinecraftResource() {
    override fun generate(): JsonObject {
        return JsonObject().apply {
            add("animation", JsonObject().apply {
                addProperty("frametime", frameTime)
                addProperty("interpolate", interpolate)
                add("frames", JsonArray().apply {
                    frames.forEach {
                        add(it)
                    }
                })
            })
        }
    }

    override fun getDefaultOutputDirectory(baseDir: Path, namespace: String): Path =
        baseDir.resolve("assets/${namespace}/textures/")
}