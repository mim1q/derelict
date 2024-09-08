package dev.mim1q.derelict.client.render.entity.spider

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.entity.SpiderlingEntity
import dev.mim1q.derelict.init.client.ModRender
import dev.mim1q.derelict.util.extensions.drawBillboard
import dev.mim1q.derelict.util.extensions.radians
import dev.mim1q.derelict.util.render.entry
import dev.mim1q.gimm1q.interpolation.Easing
import net.minecraft.client.model.*
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.RotationAxis
import org.joml.Vector2f
import kotlin.math.atan2
import kotlin.math.sqrt


class SpiderlingEntityRenderer(context: EntityRendererFactory.Context) :
    MobEntityRenderer<SpiderlingEntity, SpiderlingModel>(
        context, SpiderlingModel(context.getPart(ModRender.SPIDERLING_LAYER)), 0.3f
    ) {

    init {
        addFeature(createEyesFeatureRenderer(this, TEXTURE))
    }

    companion object {
        val TEXTURE = Derelict.id("textures/entity/spider/spiderling.png")
    }

    override fun getTexture(entity: SpiderlingEntity) = TEXTURE

    override fun render(mob: SpiderlingEntity, f: Float, tickDelta: Float, matrixStack: MatrixStack, vertexConsumerProvider: VertexConsumerProvider, light: Int) {
        super.render(mob, f, tickDelta, matrixStack, vertexConsumerProvider, light)

        if (mob.anchorPosition == null) return

        val yOffset = 0.65

        val vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(getTexture(mob)))
        val anchor = mob.anchorPosition!!
        val x = anchor.x + 0.5 - Easing.lerp(mob.prevX.toFloat(), mob.x.toFloat(), tickDelta)
        val y = anchor.y.toDouble() - Easing.lerp(mob.prevY.toFloat(), mob.y.toFloat(), tickDelta) - yOffset
        val z = anchor.z + 0.5 - Easing.lerp(mob.prevZ.toFloat(), mob.z.toFloat(), tickDelta)

        matrixStack.entry {
            val distance = sqrt(x * x + y * y + z * z).toFloat()
            val roll = atan2(y, sqrt(x * x + z * z)).toFloat()
            val yaw = atan2(x, z).toFloat() - MathHelper.HALF_PI
            translate(0.0, yOffset, 0.0)
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(yaw))
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotation(roll))
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90f))
            scale(distance, 1f, 1f)

            vertexConsumer.drawBillboard(
                matrixStack,
                light,
                Vector2f(0.0f, -1 / 32f),
                Vector2f(1.0f, 1 / 32f),
                Vector2f(16 / 32f, 0f),
                Vector2f(1f, 1 / 32f),
                255,
                255,
                255,
                255,
                true
            )
        }
    }

    override fun getLyingAngle(entity: SpiderlingEntity) = if (entity.anchorPosition != null) 15f else 180f
}

class SpiderlingModel(
    private val root: ModelPart
) : EntityModel<SpiderlingEntity>(RenderLayer::getEntityCutout) {

    private val main = root.getChild("")
    private val rightLegs = main.getChild("rightLegs")
    private val leftLegs = main.getChild("leftLegs")
    private val neck = main.getChild("neck")
    private val head = neck.getChild("head")

    private val legs: Array<ModelPart> =
        Array(8) { i ->
            val child = if (i < 4) {
                "leftLeg"
            } else {
                "rightLeg"
            }
            main.getChild("${child}s").getChild("$child${i % 4}")
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
        root.pitch = 0f
        main.pitch = entity.anchored.update(animationProgress) * 90f.radians()

        leftLegs.roll = (25f).radians()
        rightLegs.roll = (-25f).radians()
        val progress = limbAngle * 1.0f

        walkSpiderLegs(legs, progress, limbDistance)

        neck.yaw = 0f
        head.yaw = 0f
        head.pitch = 0f

        neck.pitch = headPitch.radians() - entity.anchored.value * 75f.radians()

        if (entity.anchorPosition == null) {
            head.pitch = headPitch.radians()
            neck.pitch = 0f
        }

        head.yaw = headYaw.radians()
    }
}

fun getSpiderlingTexturedModelData(): TexturedModelData {
    val modelData = ModelData()
    val modelPartData = modelData.root
    val main = modelPartData.addChild(
        "", ModelPartBuilder.create().uv(0, 0).cuboid(-2.5f, -2.0f, 1.0f, 5.0f, 4.0f, 5.0f, Dilation(0.0f))
            .uv(0, 18).cuboid(-1.5f, 0.0f, -2.0f, 3.0f, 2.0f, 3.0f, Dilation(0.0f)), ModelTransform.pivot(0.0f, 19.0f, 0.0f)
    )

    val leftLegs = main.addChild("leftLegs", ModelPartBuilder.create(), ModelTransform.pivot(0.5f, 2.25f, -0.5f))
    leftLegs.addChild("leftLeg0", ModelPartBuilder.create().uv(0, 24).cuboid(-1.5f, -0.5f, -0.5f, 8.0f, 1.0f, 1.0f, Dilation(0.0f)), ModelTransform.pivot(1.0f, -0.75f, -2.0f))
    leftLegs.addChild("leftLeg1", ModelPartBuilder.create().uv(0, 28).cuboid(-1.5f, -0.5f, -0.5f, 8.0f, 1.0f, 1.0f, Dilation(0.0f)), ModelTransform.pivot(1.0f, -0.75f, -0.75f))
    leftLegs.addChild("leftLeg2", ModelPartBuilder.create().uv(0, 24).cuboid(-1.5f, -0.5f, -0.5f, 8.0f, 1.0f, 1.0f, Dilation(0.0f)), ModelTransform.pivot(1.0f, -0.75f, 0.5f))
    leftLegs.addChild("leftLeg3", ModelPartBuilder.create().uv(0, 28).cuboid(-1.5f, -0.5f, -0.5f, 8.0f, 1.0f, 1.0f, Dilation(0.0f)), ModelTransform.pivot(1.0f, -0.75f, 1.75f))

    val rightLegs = main.addChild("rightLegs", ModelPartBuilder.create(), ModelTransform.pivot(-0.5f, 2.25f, -0.5f))
    rightLegs.addChild("rightLeg0", ModelPartBuilder.create().uv(0, 30).mirrored().cuboid(-6.5f, -0.5f, -0.5f, 8.0f, 1.0f, 1.0f, Dilation(0.0f)).mirrored(false), ModelTransform.pivot(-1.0f, -0.75f, -2.0f))
    rightLegs.addChild("rightLeg1", ModelPartBuilder.create().uv(0, 26).mirrored().cuboid(-6.5f, -0.5f, -0.5f, 8.0f, 1.0f, 1.0f, Dilation(0.0f)).mirrored(false), ModelTransform.pivot(-1.0f, -0.75f, -0.75f))
    rightLegs.addChild("rightLeg2", ModelPartBuilder.create().uv(0, 30).mirrored().cuboid(-6.5f, -0.5f, -0.5f, 8.0f, 1.0f, 1.0f, Dilation(0.0f)).mirrored(false), ModelTransform.pivot(-1.0f, -0.75f, 0.5f))
    rightLegs.addChild("rightLeg3", ModelPartBuilder.create().uv(0, 26).mirrored().cuboid(-6.5f, -0.5f, -0.5f, 8.0f, 1.0f, 1.0f, Dilation(0.0f)).mirrored(false), ModelTransform.pivot(-1.0f, -0.75f, 1.75f))

    val neck = main.addChild("neck", ModelPartBuilder.create(), ModelTransform.pivot(0.0f, 0.5f, -2.0f))
    neck.addChild(
        "head", ModelPartBuilder.create().uv(0, 9).cuboid(-2.0f, -1.5f, -4.0f, 4.0f, 3.0f, 4.0f, Dilation(0.0f))
            .uv(9, 20).cuboid(-1.5f, 1.5f, -4.0f, 3.0f, 1.0f, 0.0f, Dilation(0.0f)), ModelTransform.pivot(0.0f, 0.0f, 0.0f)
    )
    return TexturedModelData.of(modelData, 32, 32)
}