package dev.mim1q.derelict.block.cobweb

import dev.mim1q.derelict.block.cobweb.FancyCobwebBlock.Companion.shouldBeSolid
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

class SpiderSilkBlock(settings: Settings) : Block(settings) {
    @Suppress("OVERRIDE_DEPRECATION")
    override fun onEntityCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity) {
        entity.slowMovement(state, Vec3d(0.75, 0.5, 0.75))
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun isSideInvisible(state: BlockState, stateFrom: BlockState, direction: Direction): Boolean =
        stateFrom.block == this

    @Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
    override fun getCollisionShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape =
        if (shouldBeSolid(pos, context)) VoxelShapes.fullCube() else super.getCollisionShape(state, world, pos, context)
}