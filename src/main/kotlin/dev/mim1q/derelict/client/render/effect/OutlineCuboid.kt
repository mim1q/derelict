package dev.mim1q.derelict.client.render.effect

import dev.mim1q.derelict.interfaces.CuboidAccessor
import net.minecraft.client.model.ModelPart
import net.minecraft.util.math.Direction

class OutlineCuboid(
    u: Int, v: Int,
    x: Float, y: Float, z: Float,
    sizeX: Float, sizeY: Float, sizeZ: Float,
    extraX: Float, extraY: Float, extraZ: Float,
    mirror: Boolean,
    textureWidth: Float, textureHeight: Float,
    set: MutableSet<Direction>,
) : ModelPart.Cuboid(0, 0, x, y, z, sizeX, sizeY, sizeZ, extraX, extraY, extraZ, mirror, 64f, 64f, set), CuboidAccessor {
    override fun `derelict$hasOutlineCuboid`(): Boolean = false
}