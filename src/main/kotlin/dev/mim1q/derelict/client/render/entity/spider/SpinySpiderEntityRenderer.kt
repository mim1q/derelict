package dev.mim1q.derelict.client.render.entity.spider

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.entity.spider.SpinySpiderEntity
import dev.mim1q.derelict.init.client.ModRender
import dev.mim1q.derelict.util.extensions.radians
import net.minecraft.client.model.*
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper

class SpinySpiderEntityRenderer(
    context: EntityRendererFactory.Context
) : MobEntityRenderer<SpinySpiderEntity, SpinySpiderEntityModel>(
    context,
    SpinySpiderEntityModel(context.getPart(ModRender.SPINY_SPIDER_LAYER)),
    0.5f
) {
    override fun getTexture(entity: SpinySpiderEntity): Identifier = TEXTURE

    override fun scale(entity: SpinySpiderEntity, matrixStack: MatrixStack, f: Float) {
        var g = entity.getFuse(f)
        val h = 1.0f + MathHelper.sin(g * 100.0f) * g * 0.01f
        g = MathHelper.clamp(g, 0.0f, 1.0f)
        g *= g
        g *= g
        val i = (1.0f + g * 0.4f) * h
        val j = (1.0f + g * 0.1f) / h
        matrixStack.scale(i, j, i)
    }

    override fun getAnimationCounter(entity: SpinySpiderEntity, f: Float): Float {
        val g = entity.getFuse(f)
        return if ((g * 10.0f).toInt() % 2 == 0) 0.0f else MathHelper.clamp(g, 0.5f, 1.0f)
    }

    companion object {
        val TEXTURE = Derelict.id("textures/entity/spider/spiny_spider.png")
    }
}

class SpinySpiderEntityModel(part: ModelPart) : EntityModel<SpinySpiderEntity>(RenderLayer::getEntityCutout) {
    private val root = part.getChild("root")
    private val leftLegs: ModelPart = root.getChild("left_legs")
    private val rightLegs: ModelPart = root.getChild("right_legs")
    private val torso: ModelPart = root.getChild("torso")
    private val head: ModelPart = torso.getChild("head")

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
        entity: SpinySpiderEntity,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        root.traverse().forEach(ModelPart::resetTransform)

        root.pivotY -= 2f

        leftLegs.roll = (20f).radians()
        rightLegs.roll = (-20f).radians()

        walkSpiderLegs(allLegs, animationProgress, limbDistance)

        head.yaw = headYaw.radians()
        head.pitch = headPitch.radians()
    }

    companion object {
        fun createTexturedModelData(): TexturedModelData = ModelData().let {
            it.root.apply {
                addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0F, 23F, 1F)).apply {
                    addChild("left_legs", ModelPartBuilder.create(), ModelTransform.pivot(2F, -3F, -3F)).apply {
                        addChild("left_leg0", ModelPartBuilder.create().uv(28, 34).cuboid(0F, -1F, -1F, 12F, 2F, 2F), ModelTransform.pivot(0F, 0F, -1F))
                        addChild("left_leg1", ModelPartBuilder.create().uv(34, 26).cuboid(0F, -1F, -1F, 12F, 2F, 2F), ModelTransform.pivot(0F, 0F, 1F))
                        addChild("left_leg2", ModelPartBuilder.create().uv(34, 22).cuboid(0F, -1F, -1F, 12F, 2F, 2F), ModelTransform.pivot(0F, 0F, 1F))
                        addChild("left_leg3", ModelPartBuilder.create().uv(34, 18).cuboid(0F, -1F, -1F, 12F, 2F, 2F), ModelTransform.pivot(0F, 0F, 3F))
                    }
                    addChild("right_legs", ModelPartBuilder.create(), ModelTransform.pivot(-2F, -3F, -3F)).apply {
                        addChild("right_leg0", ModelPartBuilder.create().uv(0, 34).cuboid(-12F, -1F, -1F, 12F, 2F, 2F), ModelTransform.pivot(0F, 0F, -1F))
                        addChild("right_leg1", ModelPartBuilder.create().uv(28, 30).cuboid(-12F, -1F, -1F, 12F, 2F, 2F), ModelTransform.pivot(0F, 0F, 1F))
                        addChild("right_leg2", ModelPartBuilder.create().uv(30, 14).cuboid(-12F, -1F, -1F, 12F, 2F, 2F), ModelTransform.pivot(0F, 0F, 1F))
                        addChild("right_leg3", ModelPartBuilder.create().uv(0, 30).cuboid(-12F, -1F, -1F, 12F, 2F, 2F), ModelTransform.pivot(0F, 0F, 3F))
                    }
                    addChild("torso", ModelPartBuilder.create().uv(0, 0).cuboid(-7F, -6F, -3F, 14F, 3F, 11F).uv(0, 38).cuboid(-2F, -5F, -6F, 4F, 3F, 6F), ModelTransform.NONE).apply {
                        addChild("cube_r1", ModelPartBuilder.create().uv(0, 14).cuboid(-6F, 0F, 0F, 12F, 0F, 6F), ModelTransform.of(0F, -6F, 6F, 0.8727F, 0F, 0F))
                        addChild("cube_r2", ModelPartBuilder.create().uv(0, 20).cuboid(-6F, 0F, -4F, 6F, 0F, 10F), ModelTransform.of(-5F, -6F, 1F, 0F, 0F, 0.6981F))
                        addChild("cube_r3", ModelPartBuilder.create().uv(12, 20).cuboid(0F, 0F, -4F, 6F, 0F, 10F), ModelTransform.of(5F, -6F, 1F, 0F, 0F, -0.6981F))
                        addChild("head", ModelPartBuilder.create().uv(20, 38).cuboid(-3F, -2F, -4F, 6F, 4F, 4F), ModelTransform.pivot(0F, -4F, -6F))
                    }
                }
            }
            TexturedModelData.of(it, 64, 64)
        }
    }

}