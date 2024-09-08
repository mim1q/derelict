package dev.mim1q.derelict.world.feature

import dev.mim1q.derelict.block.cobweb.FancyCobwebBlock
import dev.mim1q.derelict.block.cobweb.FancyCornerCobwebBlock.Companion.ROTATION
import dev.mim1q.derelict.block.cobweb.FancyCornerCobwebBlock.Companion.TYPE
import dev.mim1q.derelict.block.cobweb.FancyCornerCobwebBlock.Type.BOTTOM
import dev.mim1q.derelict.block.cobweb.FancyCornerCobwebBlock.Type.TOP
import dev.mim1q.derelict.init.ModBlocksAndItems
import dev.mim1q.derelict.init.ModBlocksAndItems.CORNER_COBWEB
import dev.mim1q.derelict.init.ModBlocksAndItems.FANCY_CORNER_COBWEB
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.SideShapeType
import net.minecraft.util.Util
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3i
import net.minecraft.util.math.random.Random
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.WorldAccess
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.feature.DefaultFeatureConfig
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.util.FeatureContext
import kotlin.math.min
import kotlin.math.round

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
        val randomDir = POSSIBLE_DIRECTIONS.random()
        val randomSlope = random.nextDouble() * 0.3

        return SilkPlacer().generate(
            world, random,
            { it, i ->
                it.set(
                    topOrigin.x + round(i * randomSlope * randomDir.first.x).toInt(),
                    topOrigin.y - i,
                    topOrigin.z + round(i * randomSlope * randomDir.first.z).toInt(),
                )
            },
            randomDir.second
        )
    }

    override fun generate(context: FeatureContext<DefaultFeatureConfig>): Boolean {
        return true
    }
}

private class SilkPlacer {
    val positionStack = Array<BlockPos.Mutable>(64) { BlockPos.ORIGIN.mutableCopy() }

    fun generate(
        world: WorldAccess,
        random: Random,
        positionSetter: (BlockPos.Mutable, Int) -> Unit,
        rotation: Int
    ): Boolean {
        val height = setPositionsAndValidate(world, positionSetter) ?: return false

        var lastX = positionStack[0].x
        var lastZ = positionStack[0].z

        for (i in 0..<height) {
            val pos = positionStack[i]
            world.placeIfPossible(pos, ModBlocksAndItems.SPIDER_SILK.defaultState)
            if (pos.x != lastX || pos.z != lastZ) {
                world.placeIfPossible(
                    pos.up(), getRandomCornerWeb(random)
                        .with(ROTATION, (rotation + 4) % 8)
                        .with(TYPE, BOTTOM)
                )
                world.placeIfPossible(
                    BlockPos(lastX, pos.y, lastZ), getRandomCornerWeb(random)
                        .with(ROTATION, rotation)
                        .with(TYPE, TOP)
                )
                lastX = pos.x
                lastZ = pos.z
            }
        }

        var chance = 0.8

        Util.copyShuffled(POSSIBLE_DIRECTIONS, random).forEach {
            for (i in 3..min(8, height - 1)) {
                if (
                    random.nextDouble() < chance
                    && tryGenerateConnection(world, random, positionStack[i], true, it)
                ) {
                    chance -= 0.2
                    break
                }
            }
        }

        chance = 0.8

        Util.copyShuffled(POSSIBLE_DIRECTIONS, random).forEach {
            for (i in 3..min(8, height - 1)) {
                if (
                    random.nextDouble() < chance
                    && tryGenerateConnection(world, random, positionStack[height - i], false, it)
                ) {
                    chance -= 0.2
                    break
                }
            }
        }

        return true
    }

    fun tryGenerateConnection(
        world: WorldAccess,
        random: Random,
        pos: BlockPos,
        up: Boolean,
        dir: Pair<Vec3i, Int>
    ): Boolean {
        val sideOffset = dir.first
        val offset = if (up) Vec3i(sideOffset.x * 4, 4, sideOffset.z * 4) else Vec3i(sideOffset.x * 4, -4, sideOffset.z * 4)
        val checkPos = pos.add(offset)

        if (up) {
            if (
                world.getBlockState(checkPos).isReplaceable
                && world.getBlockState(checkPos.up()).isSideSolidFullSquare(world, checkPos, Direction.DOWN)
            ) {
                generateConnection(world, random, checkPos.mutableCopy(), sideOffset, dir.second, true)
                return true
            }
        } else {
            if (
                world.getBlockState(checkPos).isReplaceable
                && world.getBlockState(checkPos.down()).isSideSolidFullSquare(world, checkPos, Direction.UP)
            ) {
                generateConnection(world, random, checkPos.mutableCopy(), sideOffset, dir.second, false)
                return true
            }
        }

        return false
    }

    fun setPositionsAndValidate(world: WorldAccess, positionSetter: (BlockPos.Mutable, Int) -> Unit): Int? {
        for (i in 0..<MAX_PILLAR_HEIGHT) {
            positionSetter(positionStack[i], i)
            val currentPos = positionStack[i]
            if (
                world.getBlockState(currentPos).isReplaceable
                && world.getBlockState(currentPos.down()).isSideSolidFullSquare(world, currentPos, Direction.DOWN)
            ) {
                return if (i >= MIN_PILLAR_HEIGHT) {
                    i + 1
                } else null
            }
        }
        return null
    }

    private fun generateConnection(
        world: WorldAccess,
        random: Random,
        mutableOrigin: BlockPos.Mutable,
        sideOffset: Vec3i,
        rotation: Int,
        up: Boolean
    ) {
        val mainBlock = ModBlocksAndItems.SPIDER_SILK.defaultState
        val cornerBlock0 = {
            getRandomCornerWeb(random).with(ROTATION, rotation).with(TYPE, if (up) BOTTOM else TOP)
        }
        val cornerBlock1 = {
            getRandomCornerWeb(random).with(ROTATION, (rotation + 4) % 8).with(TYPE, if (up) TOP else BOTTOM)
        }
        val negativeOffset = sideOffset.multiply(-1)
        val verticalOffset = Vec3i(0, if (up) -1 else 1, 0)

        world.placeIfPossible(mutableOrigin, cornerBlock1())
        mutableOrigin.move(negativeOffset)
        world.placeIfPossible(mutableOrigin, mainBlock)
        mutableOrigin.move(negativeOffset)
        world.placeIfPossible(mutableOrigin, cornerBlock0())
        mutableOrigin.move(sideOffset)
        mutableOrigin.move(verticalOffset)

        world.placeIfPossible(mutableOrigin, cornerBlock1())
        mutableOrigin.move(negativeOffset)
        world.placeIfPossible(mutableOrigin, mainBlock)
        mutableOrigin.move(negativeOffset)
        world.placeIfPossible(mutableOrigin, cornerBlock0())
        mutableOrigin.move(sideOffset)
        mutableOrigin.move(verticalOffset)

        world.placeIfPossible(mutableOrigin, cornerBlock1())
        mutableOrigin.move(negativeOffset)
        world.placeIfPossible(mutableOrigin, mainBlock)
        mutableOrigin.move(verticalOffset)
        world.placeIfPossible(mutableOrigin, mainBlock)
        mutableOrigin.move(verticalOffset)
        world.placeIfPossible(mutableOrigin, cornerBlock1())
    }
}

fun WorldAccess.placeIfPossible(pos: BlockPos, state: BlockState): Boolean {
    val currentState = this.getBlockState(pos)
    val block = state.block
    if (currentState.isAir || block == ModBlocksAndItems.SPIDER_SILK || block is FancyCobwebBlock) {
        this.setBlockState(pos, state, Block.NOTIFY_LISTENERS)
        return true
    }

    return false
}

fun getRandomCornerWeb(random: Random): BlockState =
    if (random.nextDouble() < 0.02) FANCY_CORNER_COBWEB.defaultState else CORNER_COBWEB.defaultState