package dev.mim1q.derelict.client.render.entity.spider

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.entity.spider.JumpingSpiderEntity
import dev.mim1q.derelict.init.client.ModRender
import dev.mim1q.derelict.util.extensions.radians
import dev.mim1q.derelict.util.extensions.setPartialAnglesDegrees
import net.minecraft.client.model.*
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper

class JumpingSpiderEntityRenderer(
    context: EntityRendererFactory.Context
) : MobEntityRenderer<JumpingSpiderEntity, JumpingSpiderEntityModel>(
    context,
    JumpingSpiderEntityModel(context.getPart(ModRender.JUMPING_SPIDER_LAYER)),
    0.5F
) {
    override fun getTexture(entity: JumpingSpiderEntity): Identifier = TEXTURE

    companion object {
        val TEXTURE = Derelict.id("textures/entity/spider/jumping_spider.png")
    }
}

class JumpingSpiderEntityModel(bone: ModelPart) : EntityModel<JumpingSpiderEntity>() {
    private val root = bone.getChild("root")
    private val leftLegs: ModelPart = root.getChild("left_legs")
    private val rightLegs: ModelPart = root.getChild("right_legs")
    private val head = root.getChild("head")

    private val allLegs: Array<ModelPart> =
        Array(8) { i ->
            val child = if (i < 4) "left_leg" else "right_leg"
            root.getChild("${child}s").getChild("$child${i % 4}")
        }

    override fun render(
        matrices: MatrixStack,
        vertices: VertexConsumer,
        light: Int,
        overlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        root.render(matrices, vertices, light, overlay)
    }

    override fun setAngles(
        entity: JumpingSpiderEntity,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        root.traverse().forEach(ModelPart::resetTransform)

        leftLegs.roll = (25f).radians()
        rightLegs.roll = (-25f).radians()

        walkSpiderLegs(allLegs, animationProgress, limbDistance)

        val jumpCharge = entity.jumpChargeAnimation.update(animationProgress)

        if (jumpCharge > MathHelper.EPSILON) {
            allLegs.forEachIndexed { index, it ->
                val multiplier = if (index < 4) 1 else -1
                val i = index % 4
                it.setPartialAnglesDegrees(0f, (5f - i * 5f) * multiplier, (60f - (10f * i)) * multiplier, jumpCharge)
            }

            root.pivotY += 4f * jumpCharge
            root.pivotZ += 8f * jumpCharge
        }

        head.pitch = headPitch.radians()
        head.yaw = headYaw.radians()
    }

    companion object {
        fun createTexturedModelData(): TexturedModelData = ModelData().let {
            it.root.apply {
                addChild("root", ModelPartBuilder.create().uv(30, 32).cuboid(-3F, -2F, 0F, 6F, 4F, 6F).uv(0, 0).cuboid(-5F, -3F, 6F, 10F, 6F, 10F).uv(0, 43).cuboid(0F, -5F, 7F, 0F, 2F, 10F), ModelTransform.pivot(0F, 15F, -3F)).apply {
                    addChild("body_r1", ModelPartBuilder.create().uv(0, 43).cuboid(0F, 0F, -5F, 0F, 1F, 10F), ModelTransform.of(-3.5F, -4F, 11F, 0F, -0.2618F, 0F))
                    addChild("body_r2", ModelPartBuilder.create().uv(0, 43).cuboid(0F, 0F, -5F, 0F, 1F, 10F), ModelTransform.of(3.5F, -4F, 11F, 0F, 0.2618F, 0F))
                    addChild("head", ModelPartBuilder.create().uv(0, 16).cuboid(-4F, -3F, -8F, 8F, 6F, 8F).uv(0, 42).cuboid(-3F, -4F, -8F, 0F, 1F, 8F).uv(0, 42).cuboid(0F, -5F, -9F, 0F, 2F, 8F).uv(0, 42).cuboid(3F, -4F, -8F, 0F, 1F, 8F).uv(0, 0).cuboid(0.5F, 3F, -7.5F, 3F, 4F, 2F).uv(0, 38).cuboid(-3.5F, 3F, -7.5F, 3F, 4F, 2F), ModelTransform.pivot(0F, -1F, 0F))
                    addChild("left_legs", ModelPartBuilder.create(), ModelTransform.pivot(4F, 0F, 2.5F)).apply {
                        addChild("left_leg0", ModelPartBuilder.create().uv(34, 28).cuboid(0F, -1F, -1F, 16F, 2F, 2F), ModelTransform.pivot(-1F, 0F, -2.5F))
                        addChild("left_leg1", ModelPartBuilder.create().uv(0, 34).cuboid(0F, -1F, -1F, 16F, 2F, 2F), ModelTransform.pivot(-1F, 0F, -0.5F))
                        addChild("left_leg2", ModelPartBuilder.create().uv(32, 24).cuboid(0F, -1F, -1F, 16F, 2F, 2F), ModelTransform.pivot(-1F, 0F, 1.5F))
                        addChild("left_leg3", ModelPartBuilder.create().uv(30, 4).cuboid(0F, -1F, -1F, 16F, 2F, 2F), ModelTransform.pivot(-1F, 0F, 3.5F))
                    }
                    addChild("right_legs", ModelPartBuilder.create(), ModelTransform.pivot(-4F, 0F, 2.5F)).apply {
                        addChild("right_leg0", ModelPartBuilder.create().uv(30, 0).cuboid(-16F, -1F, -1F, 16F, 2F, 2F), ModelTransform.pivot(1F, 0F, -2.5F))
                        addChild("right_leg1", ModelPartBuilder.create().uv(0, 30).cuboid(-16F, -1F, -1F, 16F, 2F, 2F), ModelTransform.pivot(1F, 0F, -0.5F))
                        addChild("right_leg2", ModelPartBuilder.create().uv(24, 20).cuboid(-16F, -1F, -1F, 16F, 2F, 2F), ModelTransform.pivot(1F, 0F, 1.5F))
                        addChild("right_leg3", ModelPartBuilder.create().uv(24, 16).cuboid(-16F, -1F, -1F, 16F, 2F, 2F), ModelTransform.pivot(1F, 0F, 3.5F))
                    }
                }
            }
            TexturedModelData.of(it, 128, 128)
        }
    }
}