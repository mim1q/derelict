package dev.mim1q.derelict.world.feature

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.mim1q.derelict.util.pickWeightedRandom
import net.minecraft.structure.StructurePlacementData
import net.minecraft.structure.StructureTemplate
import net.minecraft.util.BlockRotation
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.FeatureConfig
import net.minecraft.world.gen.feature.util.FeatureContext
import kotlin.jvm.optionals.getOrNull

class NbtFeature :
    Feature<NbtFeature.NbtFeatureConfig>(NbtFeatureConfig.CODEC) {
    override fun generate(context: FeatureContext<NbtFeatureConfig>): Boolean {
        val manager = context.world.toServerWorld().structureTemplateManager
        val chosenTemplateId = context.config.structures.pickWeightedRandom(context.random)
        val template = manager.getTemplate(chosenTemplateId).getOrNull() ?: return false

        val blocks = StructureTemplate.process(
            context.world,
            context.origin,
            context.origin,
            StructurePlacementData().setRotation(BlockRotation.random(context.random)),
            arrayListOf<StructureTemplate.StructureBlockInfo>().apply {
                template.blockInfoLists.forEach { addAll(it.all) }
            }
        )

        val yDeclines = hashMapOf<Pair<Int, Int>, Int?>()

        blocks.forEach {
            val yDecline = yDeclines.getOrPut(Pair(it.pos.x, it.pos.z)) {
                val mutPos = it.pos.mutableCopy()
                for (i in 0..10) {
                    if (context.world.getBlockState(mutPos).isAir) {
                        mutPos.move(0, -1, 0)
                    } else {
                        val downPos = mutPos.down()
                        if (context.world.getBlockState(downPos).isSideSolidFullSquare(context.world, downPos, Direction.UP)) {
                            return@getOrPut i - 1
                        }
                    }
                }
                return@getOrPut null
            }
            context.world.placeIfPossible(
                it.pos.down(yDecline ?: return@forEach), it.state
            )
        }

        return true
    }

    data class NbtFeatureConfig(
        val structures: Map<Identifier, Int>,
    ) : FeatureConfig {
        companion object {
            val CODEC: Codec<NbtFeatureConfig> =
                RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<NbtFeatureConfig> ->
                    instance.group(
                        Codec.unboundedMap(Identifier.CODEC, Codec.INT)
                            .fieldOf("structures")
                            .forGetter(NbtFeatureConfig::structures)
                    ).apply(instance, ::NbtFeatureConfig)
                }
        }
    }
}