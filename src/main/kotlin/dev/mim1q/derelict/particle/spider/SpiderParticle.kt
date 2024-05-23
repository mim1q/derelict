package dev.mim1q.derelict.particle.spider

import dev.mim1q.derelict.client.render.block.entry
import dev.mim1q.derelict.util.extensions.radians
import net.minecraft.client.particle.*
import net.minecraft.client.render.Camera
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper.*
import net.minecraft.util.math.RotationAxis
import net.minecraft.util.math.Vec3d
import org.joml.Vector3f
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class SpiderParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    vx: Double,
    vz: Double,
    private val direction: Direction
) : SpriteBillboardParticle(world, x, y, z, 0.0, 0.0, 0.0) {
    init {
        maxAge = 400 + random.nextInt(40)
        alpha = 0.0F
        scale = random.nextFloat() * 0.05F + 0.1F
        velocityMultiplier = sqrt((vx * vx + vz * vz).toFloat())
    }

    private var rotation = atan2(vz, vx).toFloat()

    private val vx
        get() = sin(HALF_PI - rotation) * velocityMultiplier.toDouble()
    private val vz
        get() = cos(HALF_PI - rotation) * velocityMultiplier.toDouble()

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT

    override fun buildGeometry(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        val matrices = MatrixStack()
        matrices.entry {
            val x = lerp(tickDelta.toDouble(), prevPosX, x)
            val y = lerp(tickDelta.toDouble(), prevPosY, y)
            val z = lerp(tickDelta.toDouble(), prevPosZ, z)

            translate(x - camera.pos.x, y - camera.pos.y, z - camera.pos.z)
            val scale = scale / 8f
            scale(scale, scale, scale)
            rotateMatrices(matrices, direction, angle)
            translate(0.0, 0.0, -0.01)

            matrices.entry {
                translate(-1.5, 0.0, -0.2)
                drawBillboard(
                    vertexConsumer,
                    matrices,
                    0xF000F0,
                    3f,
                    8f
                )
            }

            val time = (age + tickDelta) * 0.5f

            val angle1 = abs(sin(time))
            val angle2 = abs(sin(time + 90f.radians()))
            val angle3 = abs(sin(time + 45f.radians()))
            val angle4 = abs(sin(time + 135f.radians()))

            drawLeg(matrices, vertexConsumer, -1.0, 5.0, 60f - angle1 * 50f)
            drawLeg(matrices, vertexConsumer, -1.0, 4.5, 100f - angle2 * 50f)
            drawLeg(matrices, vertexConsumer, -1.0, 3.5, 140f - angle3 * 40f)
            drawLeg(matrices, vertexConsumer, -1.25, 2.5, 180f - angle4 * 50f)

            drawLeg(matrices, vertexConsumer, 1.0, 5.0, -60f + angle4 * 50f)
            drawLeg(matrices, vertexConsumer, 1.0, 4.5, -100f + angle1 * 50f)
            drawLeg(matrices, vertexConsumer, 1.0, 3.5, -140f + angle2 * 40f)
            drawLeg(matrices, vertexConsumer, 1.25, 2.5, -180f + angle3 * 50f)

        }
    }

    private fun drawLeg(
        matrices: MatrixStack,
        vertexConsumer: VertexConsumer,
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
                0xF000F0,
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
                matrices.multiply(RotationAxis.POSITIVE_Z.rotation(angle + HALF_PI))
            }

            Direction.DOWN -> {
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F))
                matrices.multiply(RotationAxis.POSITIVE_Z.rotation(angle - HALF_PI))
            }

            else -> {
                matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(direction.asRotation()))
                matrices.multiply(RotationAxis.POSITIVE_Z.rotation(angle))
            }
        }


    override fun tick() {
        if (random.nextFloat() < 0.1) {
            rotation += (random.nextFloat() - 0.5F) * 2F
        }
        setAngle()
        setVelocity(vx, 0.0, vz)
        setBoundingBoxSpacing(0.01F, 0.01F)
        val offset = Vec3d(velocityX, velocityY, velocityZ).multiply(1.5).add(Vec3d.of(direction.vector).multiply(0.5))
        if (world.getBlockCollisions(null, boundingBox.offset(offset.x, offset.y, offset.z)).none()) {
            markDead()
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
            Direction.UP -> PI - rotation
            Direction.NORTH -> HALF_PI - rotation
            Direction.SOUTH -> rotation - HALF_PI
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
        val maxU = lerp(width / 16f, this.minU, maxU) + uOffset
        val maxV = lerp(height / 16f, this.minV, maxV)
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