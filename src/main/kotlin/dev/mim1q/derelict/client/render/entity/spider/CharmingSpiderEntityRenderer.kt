package dev.mim1q.derelict.client.render.entity.spider

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.entity.spider.CharmingSpiderEntity
import dev.mim1q.derelict.init.client.ModRender
import net.minecraft.client.model.*
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier


class CharmingSpiderEntityRenderer(
    ctx: EntityRendererFactory.Context
) : LivingEntityRenderer<CharmingSpiderEntity, CharmingSpiderEntityModel>(
    ctx,
    CharmingSpiderEntityModel(ctx.getPart(ModRender.CHARMING_SPIDER_LAYER)),
    0.5f
) {
    override fun getTexture(entity: CharmingSpiderEntity): Identifier = TEXTURE

    companion object {
        val TEXTURE = Derelict.id("textures/entity/spider/charming_spider.png")
    }
}

class CharmingSpiderEntityModel(
    private val root: ModelPart
) : EntityModel<CharmingSpiderEntity>(RenderLayer::getEntityCutout) {

    override fun render(
        matrices: MatrixStack,
        vertices: VertexConsumer,
        light: Int,
        overlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) = root.render(matrices, vertices, light, overlay)

    override fun setAngles(
        entity: CharmingSpiderEntity,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {

    }

    companion object {
        fun createTexturedModelData(): TexturedModelData = ModelData().let {
            it.root.apply {
                addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0F, 23F, 3F)).apply {
                    addChild("left_legs", ModelPartBuilder.create(), ModelTransform.pivot(2F, -3F, -3F)).apply {
                        addChild("left_leg0", ModelPartBuilder.create().uv(26, 26).cuboid(0F, -1F, -1F, 12F, 2F, 2F), ModelTransform.pivot(0F, 0F, -1F))
                        addChild("left_leg1", ModelPartBuilder.create().uv(26, 22).cuboid(0F, -1F, -1F, 12F, 2F, 2F), ModelTransform.pivot(0F, 0F, 1F))
                        addChild("left_leg2", ModelPartBuilder.create().uv(26, 18).cuboid(0F, -1F, -1F, 12F, 2F, 2F), ModelTransform.pivot(0F, 0F, 1F))
                        addChild("left_leg3", ModelPartBuilder.create().uv(26, 14).cuboid(0F, -1F, -1F, 12F, 2F, 2F), ModelTransform.pivot(0F, 0F, 3F))
                    }
                    addChild("right_legs", ModelPartBuilder.create(), ModelTransform.pivot(-2F, -3F, -3F)).apply {
                        addChild("right_leg0", ModelPartBuilder.create().uv(0, 24).cuboid(-12F, -1F, -1F, 12F, 2F, 2F), ModelTransform.pivot(0F, 0F, -1F))
                        addChild("right_leg1", ModelPartBuilder.create().uv(0, 20).cuboid(-12F, -1F, -1F, 12F, 2F, 2F), ModelTransform.pivot(0F, 0F, 1F))
                        addChild("right_leg2", ModelPartBuilder.create().uv(0, 16).cuboid(-12F, -1F, -1F, 12F, 2F, 2F), ModelTransform.pivot(0F, 0F, 1F))
                        addChild("right_leg3", ModelPartBuilder.create().uv(0, 12).cuboid(-12F, -1F, -1F, 12F, 2F, 2F), ModelTransform.pivot(0F, 0F, 3F))
                    }
                    addChild("torso", ModelPartBuilder.create().uv(0, 28).cuboid(-2F, -5F, -6F, 4F, 3F, 6F), ModelTransform.NONE).apply {
                        addChild("tail", ModelPartBuilder.create().uv(0, 0).cuboid(-8F, 0F, 0F, 16F, 0F, 12F), ModelTransform.pivot(0F, -5F, 0F))
                        addChild("head", ModelPartBuilder.create().uv(20, 30).cuboid(-3F, -2F, -4F, 6F, 4F, 4F).uv(0, 0).cuboid(-2.5F, 2F, -4F, 2F, 2F, 2F).uv(0, 0).cuboid(0.5F, 2F, -4F, 2F, 2F, 2F), ModelTransform.pivot(0F, -4F, -6F))
                    }
                }
            }
            TexturedModelData.of(it, 64, 64)
        }
    }
}