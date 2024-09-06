package dev.mim1q.derelict.world.feature

import dev.mim1q.derelict.block.cobweb.FancyCornerCobwebBlock.Companion.ROTATION
import dev.mim1q.derelict.block.cobweb.FancyCornerCobwebBlock.Companion.TYPE
import dev.mim1q.derelict.block.cobweb.FancyCornerCobwebBlock.Type.BOTTOM
import dev.mim1q.derelict.block.cobweb.FancyCornerCobwebBlock.Type.TOP
import dev.mim1q.derelict.init.ModBlocksAndItems
import dev.mim1q.derelict.init.ModBlocksAndItems.CORNER_COBWEB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3i
import net.minecraft.world.gen.feature.DefaultFeatureConfig
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.util.FeatureContext
import kotlin.math.abs
import kotlin.math.sign

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

class SpiderSilkParabola : Feature<DefaultFeatureConfig>(DefaultFeatureConfig.CODEC) {
    override fun generate(context: FeatureContext<DefaultFeatureConfig>): Boolean {
        val direction = POSSIBLE_DIRECTIONS.random()
        val pos = context.origin.subtract(direction.first.multiply(16)).mutableCopy()
        val world = context.world

        for (i in 0..SEARCH_RANGE) {
            if (
                world.getBlockState(pos).isAir
                && world.getBlockState(pos.up()).isSideSolidFullSquare(world, pos, Direction.DOWN)
            ) break
            if (i == SEARCH_RANGE) return false
            pos.move(0, 1, 0)
        }

        val heights = arrayOf(
            -2, -2, -1, -1, 0, 1, 1, 1, 2, 2, 3
        )

        val positions = arrayListOf<BlockPos>()

        outer@ for (i in 0..32) {
            if (i == 32) return false

            val height = heights.getOrElse(i) { heights.last() }
            for (unused in 0..<abs(height).coerceAtLeast(1)) {
                if (world.getBlockState(pos).isAir) {
                    positions.add(pos.toImmutable())
                } else {
                    break@outer
                }
                pos.move(0, sign(height.toDouble()).toInt(), 0)
            }
            pos.move(direction.first.x, 0, direction.first.z)
        }

        if (positions.size < 5) return false

        var lastPos = positions.first()
        val rot = direction.second
        val counterRot = (direction.second + 4) % 8
        for (position in positions) {
            world.placeIfPossible(position, ModBlocksAndItems.SPIDER_SILK.defaultState)
            if (position.y < lastPos.y) {
                world.placeIfPossible(lastPos.down(), CORNER_COBWEB.defaultState.with(ROTATION, rot).with(TYPE, TOP))
                world.placeIfPossible(position.up(), CORNER_COBWEB.defaultState.with(ROTATION, counterRot).with(TYPE, BOTTOM))
            } else if (position.y > lastPos.y) {
                world.placeIfPossible(position.down(), CORNER_COBWEB.defaultState.with(ROTATION, counterRot).with(TYPE, TOP))
                world.placeIfPossible(lastPos.up(), CORNER_COBWEB.defaultState.with(ROTATION, rot).with(TYPE, BOTTOM))
            }

            lastPos = position
        }

        return true
    }
}