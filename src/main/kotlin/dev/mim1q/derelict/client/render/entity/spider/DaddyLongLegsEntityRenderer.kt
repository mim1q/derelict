package dev.mim1q.derelict.client.render.entity.spider

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.entity.spider.DaddyLongLegsEntity
import dev.mim1q.derelict.init.client.ModRender
import net.minecraft.client.model.*
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

class DaddyLongLegsEntityRenderer(
    context: EntityRendererFactory.Context,
) : MobEntityRenderer<DaddyLongLegsEntity, DaddyLongLegsEntityModel>(context, DaddyLongLegsEntityModel(context.getPart(ModRender.DADDY_LONG_LEGS_LAYER)), 0.5f) {
    override fun getTexture(entity: DaddyLongLegsEntity): Identifier = TEXTURE

    companion object {
        val TEXTURE = Derelict.id("textures/entity/spider/daddy_long_legs.png")
    }
}

class DaddyLongLegsEntityModel(part: ModelPart) : EntityModel<DaddyLongLegsEntity>() {
    private val root = part.getChild("root")
    private val body = root.getChild("body")

    override fun render(matrices: MatrixStack, vertices: VertexConsumer, light: Int, overlay: Int, red: Float, green: Float, blue: Float, alpha: Float) {
        root.render(matrices, vertices, light, overlay, red, green, blue, alpha)
    }

    override fun setAngles(entity: DaddyLongLegsEntity, limbAngle: Float, limbDistance: Float, animationProgress: Float, headYaw: Float, headPitch: Float) {
        root.traverse().forEach(ModelPart::resetTransform)
        root.pivotY += 2f
    }

    companion object {
        fun createTexturedModelData(): TexturedModelData = ModelData().let {
            it.root.apply {
                addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0F, 21F, 0F)).apply {
                    addChild("body", ModelPartBuilder.create().uv(0, 16).cuboid(-4F, -4F, -4F, 8F, 8F, 8F), ModelTransform.pivot(0F, -46F, 0F))
                    addChild("left_leg_joint0", ModelPartBuilder.create(), ModelTransform.pivot(3F, -44F, -1.5F)).apply {
                        addChild("left_leg0", ModelPartBuilder.create().uv(0, 15).cuboid(0F, -0.5F, 0F, 24F, 1F, 0F), ModelTransform.NONE).apply {
                            addChild("left_leg_front0", ModelPartBuilder.create().uv(0, 14).cuboid(0F, -0.5F, 0F, 24F, 1F, 0F), ModelTransform.pivot(24F, 0F, 0F))
                        }
                    }
                    addChild("left_leg_joint1", ModelPartBuilder.create(), ModelTransform.pivot(3F, -44F, -0.5F)).apply {
                        addChild("left_leg1", ModelPartBuilder.create().uv(0, 13).cuboid(0F, -0.5F, 0F, 24F, 1F, 0F), ModelTransform.NONE).apply {
                            addChild("left_leg_front1", ModelPartBuilder.create().uv(0, 12).cuboid(0F, -0.5F, 0F, 24F, 1F, 0F), ModelTransform.pivot(24F, 0F, 0F))
                        }
                    }
                    addChild("left_leg_joint2", ModelPartBuilder.create(), ModelTransform.pivot(3F, -44F, 0.5F)).apply {
                        addChild("left_leg2", ModelPartBuilder.create().uv(0, 11).cuboid(0F, -0.5F, 0F, 24F, 1F, 0F), ModelTransform.NONE).apply {
                            addChild("left_leg_front2", ModelPartBuilder.create().uv(0, 10).cuboid(0F, -0.5F, 0F, 24F, 1F, 0F), ModelTransform.pivot(24F, 0F, 0F))
                        }
                    }
                    addChild("left_leg_joint3", ModelPartBuilder.create(), ModelTransform.pivot(3F, -44F, 1.5F)).apply {
                        addChild("left_leg3", ModelPartBuilder.create().uv(0, 9).cuboid(0F, -0.5F, 0F, 24F, 1F, 0F), ModelTransform.NONE).apply {
                            addChild("left_leg_front3", ModelPartBuilder.create().uv(0, 8).cuboid(0F, -0.5F, 0F, 24F, 1F, 0F), ModelTransform.pivot(24F, 0F, 0F))
                        }
                    }
                    addChild("right_leg_joint0", ModelPartBuilder.create(), ModelTransform.pivot(-3F, -44F, -1.5F)).apply {
                        addChild("right_leg0", ModelPartBuilder.create().uv(0, 7).cuboid(-24F, -0.5F, 0F, 24F, 1F, 0F), ModelTransform.NONE).apply {
                            addChild("right_leg_front0", ModelPartBuilder.create().uv(0, 6).cuboid(-24F, -0.5F, 0F, 24F, 1F, 0F), ModelTransform.pivot(-24F, 0F, 0F))
                        }
                    }
                    addChild("right_leg_joint1", ModelPartBuilder.create(), ModelTransform.pivot(-3F, -44F, -0.5F)).apply {
                        addChild("right_leg1", ModelPartBuilder.create().uv(0, 5).cuboid(-24F, -0.5F, 0F, 24F, 1F, 0F), ModelTransform.NONE).apply {
                            addChild("right_leg_front1", ModelPartBuilder.create().uv(0, 4).cuboid(-24F, -0.5F, 0F, 24F, 1F, 0F), ModelTransform.pivot(-24F, 0F, 0F))
                        }
                    }
                    addChild("right_leg_joint2", ModelPartBuilder.create(), ModelTransform.pivot(-3F, -44F, 0.5F)).apply {
                        addChild("right_leg2", ModelPartBuilder.create().uv(0, 3).cuboid(-24F, -0.5F, 0F, 24F, 1F, 0F), ModelTransform.NONE).apply {
                            addChild("right_leg_front2", ModelPartBuilder.create().uv(0, 2).cuboid(-24F, -0.5F, 0F, 24F, 1F, 0F), ModelTransform.pivot(-24F, 0F, 0F))
                        }
                    }
                    addChild("right_leg_joint3", ModelPartBuilder.create(), ModelTransform.pivot(-3F, -44F, 1.5F)).apply {
                        addChild("right_leg3", ModelPartBuilder.create().uv(0, 1).cuboid(-24F, -0.5F, 0F, 24F, 1F, 0F), ModelTransform.NONE).apply {
                            addChild("right_leg_front3", ModelPartBuilder.create().uv(0, 0).cuboid(-24F, -0.5F, 0F, 24F, 1F, 0F), ModelTransform.pivot(-24F, 0F, 0F))
                        }
                    }
                }
            }
            TexturedModelData.of(it, 64, 64)
        }
    }
}