package dev.mim1q.derelict.client.render.entity

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.entity.SpiderlingEntity
import dev.mim1q.derelict.init.client.ModRender
import dev.mim1q.derelict.util.extensions.radians
import net.minecraft.client.model.*
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

class SpiderlingEntityRenderer(context: EntityRendererFactory.Context) :
    MobEntityRenderer<SpiderlingEntity, SpiderlingModel>(
        context, SpiderlingModel(context.getPart(ModRender.SPIDERLING_LAYER)), 0.3f
    ) {
    override fun getTexture(entity: SpiderlingEntity) = Derelict.id("textures/entity/spiderling.png")

}

class SpiderlingModel(
    private val root: ModelPart
) : EntityModel<SpiderlingEntity>(RenderLayer::getEntityCutout) {

    private val rightLegs = root.getChild("rightLegs")
    private val leftLegs = root.getChild("leftLegs")
    private val legs: Array<ModelPart> =
        Array(8) { i ->
            val child = if (i < 4) {
                "leftLeg"
            } else {
                "rightLeg"
            }
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
        root.render(matrices, vertices, light, overlay, red, green, blue, alpha)
    }

    override fun setAngles(
        entity: SpiderlingEntity,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        leftLegs.roll = (25f).radians()
        rightLegs.roll = (-25f).radians()
        val progress = limbAngle * 1.0f

        walkLeg(0, 60f, progress, 0f, 0.4f * limbDistance)
        walkLeg(1, 25f, progress, 90f, 0.4f * limbDistance)
        walkLeg(2, -15f, progress, 15f, 0.5f * limbDistance)
        walkLeg(3, -35f, progress, 105f, 0.6f * limbDistance)

        walkLeg(4, -60f, progress, 10f, 0.4f * limbDistance)
        walkLeg(5, -25f, progress, 100f, 0.4f * limbDistance)
        walkLeg(6, 15f, progress, 25f, 0.5f * limbDistance)
        walkLeg(7, 35f, progress, 115f, 0.6f * limbDistance)
    }

    private fun walkLeg(index: Int, defaultAngle: Float, progress: Float, offset: Float, multiplier: Float) {
        val leg = legs[index]
        leg.yaw = defaultAngle.radians() + sin((progress + offset.radians())) * multiplier
        leg.roll = max(0f, cos((progress + (offset - 35f).radians())) * multiplier)
        leg.roll *= if (index < 4) -1 else 1
    }
}

fun getSpiderlingTexturedModelData(): TexturedModelData {
    val modelData = ModelData()
    val modelPartData = modelData.root
    val leftLegs =
        modelPartData.addChild("leftLegs", ModelPartBuilder.create(), ModelTransform.pivot(0.5F, 21.25F, -0.5F))
    leftLegs.addChild(
        "leftLeg0",
        ModelPartBuilder.create().uv(0, 24).cuboid(-1.5F, -0.5F, -0.5F, 8.0F, 1.0F, 1.0F, Dilation(0.0F)),
        ModelTransform.pivot(1.0F, -0.75F, -2.0F)
    )
    leftLegs.addChild(
        "leftLeg1",
        ModelPartBuilder.create().uv(0, 28).cuboid(-1.5F, -0.5F, -0.5F, 8.0F, 1.0F, 1.0F, Dilation(0.0F)),
        ModelTransform.pivot(1.0F, -0.75F, -0.75F)
    )
    leftLegs.addChild(
        "leftLeg2",
        ModelPartBuilder.create().uv(0, 24).cuboid(-1.5F, -0.5F, -0.5F, 8.0F, 1.0F, 1.0F, Dilation(0.0F)),
        ModelTransform.pivot(1.0F, -0.75F, 0.5F)
    )
    leftLegs.addChild(
        "leftLeg3",
        ModelPartBuilder.create().uv(0, 28).cuboid(-1.5F, -0.5F, -0.5F, 8.0F, 1.0F, 1.0F, Dilation(0.0F)),
        ModelTransform.pivot(1.0F, -0.75F, 1.75F)
    )
    val rightLegs =
        modelPartData.addChild("rightLegs", ModelPartBuilder.create(), ModelTransform.pivot(-0.5F, 21.25F, -0.5F))
    rightLegs.addChild(
        "rightLeg0",
        ModelPartBuilder.create().uv(0, 30).mirrored().cuboid(-6.5F, -0.5F, -0.5F, 8.0F, 1.0F, 1.0F, Dilation(0.0F))
            .mirrored(false),
        ModelTransform.pivot(-1.0F, -0.75F, -2.0F)
    )
    rightLegs.addChild(
        "rightLeg1",
        ModelPartBuilder.create().uv(0, 26).mirrored().cuboid(-6.5F, -0.5F, -0.5F, 8.0F, 1.0F, 1.0F, Dilation(0.0F))
            .mirrored(false),
        ModelTransform.pivot(-1.0F, -0.75F, -0.75F)
    )
    rightLegs.addChild(
        "rightLeg2",
        ModelPartBuilder.create().uv(0, 30).mirrored().cuboid(-6.5F, -0.5F, -0.5F, 8.0F, 1.0F, 1.0F, Dilation(0.0F))
            .mirrored(false),
        ModelTransform.pivot(-1.0F, -0.75F, 0.5F)
    )
    rightLegs.addChild(
        "rightLeg3",
        ModelPartBuilder.create().uv(0, 26).mirrored().cuboid(-6.5F, -0.5F, -0.5F, 8.0F, 1.0F, 1.0F, Dilation(0.0F))
            .mirrored(false),
        ModelTransform.pivot(-1.0F, -0.75F, 1.75F)
    )
    modelPartData.addChild(
        "head",
        ModelPartBuilder.create().uv(0, 9).cuboid(-2.0F, -1.5F, -4.0F, 4.0F, 3.0F, 4.0F, Dilation(0.0F))
            .uv(9, 20).cuboid(-1.5F, 1.5F, -4.0F, 3.0F, 1.0F, 0.0F, Dilation(0.0F)),
        ModelTransform.pivot(0.0F, 19.5F, -2.0F)
    )
    modelPartData.addChild(
        "bb_main",
        ModelPartBuilder.create().uv(0, 0).cuboid(-2.5F, -7.0F, 1.0F, 5.0F, 4.0F, 5.0F, Dilation(0.0F))
            .uv(0, 18).cuboid(-1.5F, -5.0F, -2.0F, 3.0F, 2.0F, 3.0F, Dilation(0.0F)),
        ModelTransform.pivot(0.0F, 24.0F, 0.0F)
    )
    return TexturedModelData.of(modelData, 32, 32)
}