package dev.mim1q.derelict.world.feature

import com.mojang.serialization.Codec
import dev.mim1q.derelict.init.worldgen.ModFeatures
import net.minecraft.structure.StructurePlacementData
import net.minecraft.structure.StructureTemplate
import net.minecraft.structure.processor.StructureProcessor
import net.minecraft.structure.processor.StructureProcessorType
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldView

class LocalGravityProcessor : StructureProcessor() {
    companion object {
        val CODEC: Codec<LocalGravityProcessor> = Codec.unit(LocalGravityProcessor())
    }

    override fun process(
        world: WorldView,
        pos: BlockPos,
        pivot: BlockPos,
        originalBlockInfo: StructureTemplate.StructureBlockInfo,
        currentBlockInfo: StructureTemplate.StructureBlockInfo,
        data: StructurePlacementData
    ): StructureTemplate.StructureBlockInfo {
        val mutPos = currentBlockInfo.pos.mutableCopy()
        val diff = mutPos.y - pos.y

        for (i in 0..(10 + diff)) {
            if (i == 10) return originalBlockInfo

            if (world.getBlockState(mutPos).isAir) {
                mutPos.move(0, -1, 0)
            } else {
                mutPos.move(0, 1, 0)
                break
            }
        }

        return StructureTemplate.StructureBlockInfo(
            mutPos.add(0, diff, 0),
            originalBlockInfo.state,
            originalBlockInfo.nbt
        )
    }

    override fun getType(): StructureProcessorType<*> = ModFeatures.LOCAL_GRAVITY_PROCESSOR
}