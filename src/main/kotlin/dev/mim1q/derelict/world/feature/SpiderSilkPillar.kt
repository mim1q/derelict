package dev.mim1q.derelict.world.feature

import dev.mim1q.derelict.block.cobweb.FancyCornerCobwebBlock
import dev.mim1q.derelict.init.ModBlocksAndItems
import net.minecraft.block.BlockState
import net.minecraft.block.SideShapeType
import net.minecraft.util.Util
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3i
import net.minecraft.util.math.random.Random
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.feature.DefaultFeatureConfig
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.util.FeatureContext
import java.util.*

private const val MIN_PILLAR_HEIGHT = 10
private const val MAX_PILLAR_HEIGHT = 64
private const val SEARCH_RANGE = 32

private val POSSIBLE_DIRECTIONS = arrayOf(
    Vec3i(1, 0, 0) to 6,
    Vec3i(1, 0, 1) to 7,
    Vec3i(0, 0, 1) to 0,
    Vec3i(-1, 0, 1) to 1,
    Vec3i(-1, 0, 0) to 2,
    Vec3i(-1, 0, -1) to 3,
    Vec3i(0, 0, -1) to 4,
    Vec3i(1, 0, -1) to 5,
)

class SpiderSilkPillar : Feature<DefaultFeatureConfig>(DefaultFeatureConfig.CODEC) {
    override fun generateIfValid(
        config: DefaultFeatureConfig,
        world: StructureWorldAccess,
        chunkGenerator: ChunkGenerator,
        random: Random,
        origin: BlockPos
    ): Boolean {
        val topPos = origin.mutableCopy()
        var found = false
        for (i in 0..<SEARCH_RANGE) {
            val aboveTopPos = topPos.up()
            if (world.getBlockState(topPos).isAir &&
                world.getBlockState(aboveTopPos).isSideSolid(world, aboveTopPos, Direction.DOWN, SideShapeType.FULL)
            ) {
                found = true
                break
            }
            topPos.setY(topPos.y + 1)
        }
        if (!found) return false

        val topOrigin = topPos.toImmutable()

        do {
            topPos.setY(topPos.y - 1)
            val height = topOrigin.y - topPos.y
            val belowTopPos = topPos.down()
            if (
                world.getBlockState(topPos).isAir
                && world.getBlockState(belowTopPos).isSideSolid(world, belowTopPos, Direction.UP, SideShapeType.FULL)
            ) {
                if (height < MIN_PILLAR_HEIGHT) return false
                return generate(FeatureContext(Optional.empty(), world, chunkGenerator, random, topOrigin, config))
            }
        } while (height < MAX_PILLAR_HEIGHT)

        return false
    }

    override fun generate(context: FeatureContext<DefaultFeatureConfig>): Boolean {
        var pos = context.origin
        while (context.world.getBlockState(pos).isAir) {
            setBlockState(context.world, pos, ModBlocksAndItems.SPIDER_SILK.defaultState)
            pos = pos.down()
        }

        var noTopConnection = true
        var noBottomConnection = true

        Util.copyShuffled(POSSIBLE_DIRECTIONS, context.random).forEach { direction ->
            if (context.random.nextFloat() < 0.2f || noTopConnection) {
                if (tryGenerateConnection(context.world, context.origin.down(5), direction.first, direction.second, 8, true))
                    noTopConnection = false
            }
            if (context.random.nextFloat() < 0.2f || noBottomConnection) {
                if (tryGenerateConnection(context.world, pos.up(5), direction.first, direction.second, 8, false))
                    noBottomConnection = false
            }
        }


        return true
    }

    private fun tryGenerateConnection(
        world: StructureWorldAccess,
        origin: BlockPos,
        sideOffset: Vec3i,
        rotation: Int,
        tries: Int,
        up: Boolean
    ): Boolean {
        val testPos = origin.mutableCopy()
        repeat(tries) {
            val pos = testPos.add(sideOffset.multiply(4))
            val otherPos = if (up) pos.up() else pos.down()

            if (world.getBlockState(pos).isAir
                && world.getBlockState(otherPos)
                    .isSideSolid(world, otherPos, if (up) Direction.DOWN else Direction.UP, SideShapeType.FULL)
            ) {
                generateConnection(world, pos.mutableCopy(), sideOffset, rotation, up)
                return true
            }
            testPos.move(0, if (up) 1 else -1, 0)
        }
        return false
    }

    private fun generateConnection(
        world: StructureWorldAccess,
        mutableOrigin: BlockPos.Mutable,
        sideOffset: Vec3i,
        rotation: Int,
        up: Boolean
    ) {
        val mainBlock = ModBlocksAndItems.SPIDER_SILK.defaultState
        val cornerBlock0 = ModBlocksAndItems.CORNER_COBWEB.defaultState
            .with(FancyCornerCobwebBlock.ROTATION, rotation)
            .with(FancyCornerCobwebBlock.TYPE, if (up) FancyCornerCobwebBlock.Type.BOTTOM else FancyCornerCobwebBlock.Type.TOP)
        val cornerBlock1 = ModBlocksAndItems.CORNER_COBWEB.defaultState
            .with(FancyCornerCobwebBlock.ROTATION, (rotation + 4) % 8)
            .with(FancyCornerCobwebBlock.TYPE, if (up) FancyCornerCobwebBlock.Type.TOP else FancyCornerCobwebBlock.Type.BOTTOM)
        val negativeOffset = sideOffset.multiply(-1)
        val verticalOffset = Vec3i(0, if (up) -1 else 1, 0)

        val predicate: (BlockState) -> Boolean = { it.isAir }

        setBlockStateIf(world, mutableOrigin, cornerBlock1, predicate)
        mutableOrigin.move(negativeOffset)
        setBlockStateIf(world, mutableOrigin, mainBlock, predicate)
        mutableOrigin.move(negativeOffset)
        setBlockStateIf(world, mutableOrigin, cornerBlock0, predicate)
        mutableOrigin.move(sideOffset)
        mutableOrigin.move(verticalOffset)

        setBlockStateIf(world, mutableOrigin, cornerBlock1, predicate)
        mutableOrigin.move(negativeOffset)
        setBlockStateIf(world, mutableOrigin, mainBlock, predicate)
        mutableOrigin.move(negativeOffset)
        setBlockStateIf(world, mutableOrigin, cornerBlock0, predicate)
        mutableOrigin.move(sideOffset)
        mutableOrigin.move(verticalOffset)

        setBlockStateIf(world, mutableOrigin, cornerBlock1, predicate)
        mutableOrigin.move(negativeOffset)
        setBlockStateIf(world, mutableOrigin, mainBlock, predicate)
        mutableOrigin.move(verticalOffset)
        setBlockStateIf(world, mutableOrigin, mainBlock, predicate)
        mutableOrigin.move(verticalOffset)
        setBlockStateIf(world, mutableOrigin, cornerBlock1, predicate)
    }
}