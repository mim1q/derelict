package com.github.mim1q.derelict.util

import net.minecraft.util.function.BooleanBiFunction
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes


object ShapeUtil {
  fun rotate(from: Direction, to: Direction, shape: VoxelShape): VoxelShape {
    val buffer = arrayOf(shape, VoxelShapes.empty())
    val times: Int = (to.horizontal - from.horizontal + 4) % 4
    for (i in 0 until times) {
      buffer[0].forEachBox { minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double ->
        buffer[1] = VoxelShapes.combine(
          buffer[1],
          VoxelShapes.cuboid(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX),
          BooleanBiFunction.OR
        )
      }
      buffer[0] = buffer[1]
      buffer[1] = VoxelShapes.empty()
    }
    return buffer[0]
  }

  fun rotate(to: Direction, shape: VoxelShape): VoxelShape = rotate(Direction.NORTH, to, shape)
}