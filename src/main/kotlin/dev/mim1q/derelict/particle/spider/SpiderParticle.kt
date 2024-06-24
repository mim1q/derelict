package dev.mim1q.derelict.particle.spider

import dev.mim1q.derelict.util.extensions.radians
import dev.mim1q.derelict.util.render.entry
import net.minecraft.client.particle.*
import net.minecraft.client.render.Camera
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.RotationAxis
import net.minecraft.util.math.Vec3d
import org.joml.Vector3f
import kotlin.math.abs

class SpiderParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    vx: Double,
    vz: Double,
    private val direction: Direction
) : SpriteBillboardParticle(world, x, y, z, 0.0, 0.0, 0.0) {
    companion object {
        val matrices = MatrixStack()
    }



    init {
        maxAge = 40 + random.nextInt(40)
        alpha = 0.0F
        scale = random.nextFloat() * 0.05F + 0.1F
        velocityMultiplier = MathHelper.sqrt((vx * vx + vz * vz).toFloat())
    }

    private var rotation = MathHelper.atan2(vz, vx).toFloat()

    private val vx
        get() = MathHelper.sin(MathHelper.HALF_PI - rotation) * velocityMultiplier.toDouble()
    private val vz
        get() = MathHelper.cos(MathHelper.HALF_PI - rotation) * velocityMultiplier.toDouble()

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT

    override fun buildGeometry(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        matrices.entry {
            val x = MathHelper.lerp(tickDelta.toDouble(), prevPosX, x)
            val y = MathHelper.lerp(tickDelta.toDouble(), prevPosY, y)
            val z = MathHelper.lerp(tickDelta.toDouble(), prevPosZ, z)

            val light = getBrightness(0f)
            translate(x - camera.pos.x, y - camera.pos.y, z - camera.pos.z)
            val scale = scale / 8f
            scale(scale, scale, scale)
            rotateMatrices(matrices, direction, MathHelper.lerp(tickDelta, prevAngle, angle))
            translate(0.0, 0.0, -0.01)

            matrices.entry {
                translate(-1.5, 0.0, -0.2)
                repeat(3) {
                    drawBillboard(
                        vertexConsumer,
                        matrices,
                        light,
                        3f,
                        8f
                    )
                    translate(0.0, 0.0, -0.3)
                }
            }

            val time = (age + tickDelta) * 0.67f

            val angle1 = MathHelper.abs(MathHelper.sin(time))
            val angle2 = MathHelper.abs(MathHelper.sin(time + 90f.radians()))
            val angle3 = MathHelper.abs(MathHelper.sin(time + 45f.radians()))
            val angle4 = MathHelper.abs(MathHelper.sin(time + 135f.radians()))

            drawLeg(matrices, vertexConsumer, light, -1.0, 5.0, 60f - angle1 * 50f)
            drawLeg(matrices, vertexConsumer, light, -0.5, 4.5, 100f - angle2 * 50f)
            drawLeg(matrices, vertexConsumer, light, -0.5, 3.5, 140f - angle3 * 40f)
            drawLeg(matrices, vertexConsumer, light, -1.25, 2.5, 180f - angle4 * 50f)

            drawLeg(matrices, vertexConsumer, light, 1.0, 5.0, -60f + angle4 * 50f)
            drawLeg(matrices, vertexConsumer, light, 0.5, 4.5, -100f + angle1 * 50f)
            drawLeg(matrices, vertexConsumer, light, 0.5, 3.5, -140f + angle2 * 40f)
            drawLeg(matrices, vertexConsumer, light, 1.25, 2.5, -180f + angle3 * 50f)

        }
    }

    private fun drawLeg(
        matrices: MatrixStack,
        vertexConsumer: VertexConsumer,
        light: Int,
        xOffset: Double,
        zOffset: Double,
        angle: Float
    ) {
        matrices.entry {
            translate(xOffset, zOffset, 0.005)
            multiply(RotationAxis.POSITIVE_Z.rotationDegrees(angle))
            translate(-0.5, 0.0, 0.0)
            drawBillboard(
                vertexConsumer,
                matrices,
                light,
                1f,
                6f,
                4f / 16f,
            )
        }
    }

    private fun rotateMatrices(matrices: MatrixStack, direction: Direction, angle: Float) =
        when (direction) {
            Direction.UP -> {
                matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(90.0F))
                matrices.multiply(RotationAxis.POSITIVE_Z.rotation(angle + MathHelper.HALF_PI))
            }

            Direction.DOWN -> {
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F))
                matrices.multiply(RotationAxis.POSITIVE_Z.rotation(angle - MathHelper.HALF_PI))
            }

            else -> {
                matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(direction.asRotation()))
                matrices.multiply(RotationAxis.POSITIVE_Z.rotation(angle))
            }
        }


    override fun tick() {
        rotation += (random.nextFloat() - 0.5F) * if (random.nextFloat() < 0.1f) 1.0f else 0.2f
        setAngle()
        setVelocity(vx, 0.0, vz)
        setBoundingBoxSpacing(0.01F, 0.01F)

        val offset = Vec3d(velocityX, velocityY, velocityZ).multiply(1.5).add(Vec3d.of(direction.vector).multiply(0.5))
        val xVel = abs(x - prevPosX) < MathHelper.EPSILON
        val yVel = abs(y - prevPosY) < MathHelper.EPSILON
        val zVel = abs(z - prevPosZ) < MathHelper.EPSILON

        if (
            (age > 2 && (xVel xor yVel xor zVel xor true))
            || world.getBlockCollisions(null, boundingBox.offset(offset.x, offset.y, offset.z)).none()
        ) {
            markDead()
            return
        }

        super.tick()

        if (age <= 2) {
            alpha = (age / 2.0F)
        }
        val remaining = maxAge - age
        if (remaining <= 5) {
            alpha = (remaining / 5.0F)
        }
    }

    override fun setVelocity(x: Double, y: Double, z: Double) {
        when (direction) {
            Direction.DOWN, Direction.UP -> super.setVelocity(x, 0.0, z)
            Direction.NORTH, Direction.SOUTH -> super.setVelocity(x, z, 0.0)
            Direction.EAST, Direction.WEST -> super.setVelocity(0.0, x, z)
        }
    }

    private fun setAngle() {
        prevAngle = angle
        angle = when (direction) {
            Direction.DOWN -> rotation
            Direction.UP -> MathHelper.PI - rotation
            Direction.NORTH -> MathHelper.HALF_PI - rotation
            Direction.SOUTH -> rotation - MathHelper.HALF_PI
            Direction.EAST -> rotation
            Direction.WEST -> -rotation
        }
    }

    class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<SpiderParticleEffect> {
        override fun createParticle(
            parameters: SpiderParticleEffect,
            world: ClientWorld,
            x: Double,
            y: Double,
            z: Double,
            velocityX: Double,
            velocityY: Double,
            velocityZ: Double
        ): Particle {
            val particle = SpiderParticle(world, x, y, z, velocityX, velocityZ, parameters.direction)
            particle.setSprite(spriteProvider)
            return particle
        }
    }

    private fun drawBillboard(
        vertexConsumer: VertexConsumer,
        matrices: MatrixStack,
        light: Int,
        width: Float = 1f,
        height: Float = 1f,
        u: Float = 0f,
    ) {
        val vertices = arrayOf(
            Vector3f(0f, 0f, 0.0f),
            Vector3f(0f, height, 0.0f),
            Vector3f(width, height, 0.0f),
            Vector3f(width, 0f, 0.0f)
        )

        val uOffset = (maxU - minU) * u
        val maxU = MathHelper.lerp(width / 16f, this.minU, maxU) + uOffset
        val maxV = MathHelper.lerp(height / 16f, this.minV, maxV)
        val minU = minU + uOffset
        val minV = minV

        val posMatrix = matrices.peek().positionMatrix

        vertexConsumer
            .vertex(posMatrix, vertices[0].x, vertices[0].y, vertices[0].z)
            .texture(maxU, maxV)
            .color(this.red, this.green, this.blue, this.alpha)
            .light(light)
            .next()
        vertexConsumer
            .vertex(posMatrix, vertices[1].x, vertices[1].y, vertices[1].z)
            .texture(maxU, minV)
            .color(this.red, this.green, this.blue, this.alpha)
            .light(light)
            .next()
        vertexConsumer
            .vertex(posMatrix, vertices[2].x, vertices[2].y, vertices[2].z)
            .texture(minU, minV)
            .color(this.red, this.green, this.blue, this.alpha)
            .light(light)
            .next()
        vertexConsumer
            .vertex(posMatrix, vertices[3].x, vertices[3].y, vertices[3].z)
            .texture(minU, maxV)
            .color(this.red, this.green, this.blue, this.alpha)
            .light(light)
            .next()
    }
}