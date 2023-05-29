package datagen.custom

import tada.lib.presets.Preset
import tada.lib.util.Id
import kotlin.random.Random

object AnimationPresets {
  fun flicker(id: String, seed: Long = 0) = Preset {
    val flickerFrames = listOf(
      listOf(1),
      listOf(1, 1),
      listOf(1, 0, 1),
      listOf(1, 1, 0, 1, 0, 1),
      listOf(1, 0, 1, 1, 0, 0, 1),
      listOf(1, 0, 1, 0, 1, 0),
      listOf(1, 0, 0, 1),
      listOf(1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 1)
    )
    val (_, name) = Id(id)
    val rng = Random(seed)
    val frames = mutableListOf<Int>()
    val count = rng.nextInt(8, 12)
    repeat(count) {
      frames.addAll(List(rng.nextInt(5, 40)) { 0 })
      frames.addAll(flickerFrames[rng.nextInt(flickerFrames.size)])
    }
    add("$name.png.mcmeta", Animation(2, true, frames))
  }

  fun copyBlockTexture(from: String, to: String) {
    val (fNs, fName) = Id(from)
    val (tNs, tName) = Id(to)
    FileCopyManager.addFileCopy("assets/$fNs/textures/block/$fName.png", "assets/$tNs/textures/block/$tName.png")
  }

  fun createIndexedBlockTextureCopies(from: String, count: Int) {
    val (ns, name) = Id(from)
    for (i in 0 until count) {
      copyBlockTexture(from, "$ns:$name/$i")
    }
  }
}