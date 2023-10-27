package dev.mim1q.derelict.client.render.block

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.block.cobweb.FancyCobwebWithSpiderBlockEntity
import dev.mim1q.derelict.init.client.ModRender
import dev.mim1q.derelict.util.Easing
import dev.mim1q.derelict.util.extensions.drawBillboard
import dev.mim1q.derelict.util.extensions.radians
import net.minecraft.client.model.*
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory.Context
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.MathHelper
import org.joml.Quaternionf
import org.joml.Vector2f

fun MatrixStack.entry(setup: MatrixStack.() -> Unit) {
  push()
  setup()
  pop()
}

class FancyCobwebWithSpiderRenderer(context: Context) : BlockEntityRenderer<FancyCobwebWithSpiderBlockEntity> {
  private val model = SpiderModel(context.getLayerModelPart(ModRender.FANCY_COBWEB_SPIDER_LAYER))

  override fun render(
    entity: FancyCobwebWithSpiderBlockEntity,
    tickDelta: Float,
    matrices: MatrixStack,
    vertexConsumers: VertexConsumerProvider,
    light: Int,
    overlay: Int
  ) {
    val linearProgress = MathHelper.lerp(tickDelta.toDouble(), entity.lastLoweringProgress, entity.loweringProgress)
    val progress = Easing.easeInOutQuad(0F, 1F, linearProgress.toFloat())
    val yaw = MathHelper.lerp(tickDelta, entity.lastClientYaw, entity.clientYaw).radians()
    val vertices = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE))
    matrices.entry {
      scale(-1F, -1F, 1F)
      translate(-0.5, -0.5, 0.5)
      matrices.multiply(Quaternionf().rotationY(yaw))

      matrices.entry {
        val distance = progress * entity.distance.toFloat() * 0.8F
        model.renderString(matrices, vertices, light, distance)
      }

      matrices.entry {
        translate(0.0, progress * entity.distance, 0.0)
        model.render(matrices, vertices, light, overlay, 1F, 1F, 1F, 1F)
      }
    }
  }

  override fun rendersOutsideBoundingBox(blockEntity: FancyCobwebWithSpiderBlockEntity) = true

  companion object {
    val TEXTURE = Derelict.id("textures/entity/spiderling.png")
  }

  class SpiderModel(private val root: ModelPart) : Model(RenderLayer::getEntityCutout) {
    private val leftLegs = root.getChild("leftLegs").let {
      (0..3).map { i -> it.getChild("leftLeg$i") }
    }
    private val rightLegs = root.getChild("rightLegs").let {
      (0..3).map { i -> it.getChild("rightLeg$i") }
    }
    private val head = root.getChild("head")

    init {
      root.pivotZ = -20F
      root.pitch = 80F.radians()
      leftLegs.forEachIndexed { i, leg -> leg.yaw = (60F - 35F * i).radians(); leg.roll = 30F.radians()}
      rightLegs.forEachIndexed { i, leg -> leg.yaw = (-60F + 35F * i).radians(); leg.roll = (-30F).radians() }
      head.pitch = (-15F).radians()
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

    fun renderString(matrices: MatrixStack, vertices: VertexConsumer, light: Int, distance: Float) {
      matrices.entry {
        vertices.drawBillboard(
          matrices, light,
          Vector2f(-1/32F, 0F), Vector2f(1/32F, distance * 2.5F),
          fromUv = Vector2f(31/32F, 0F), toUv = Vector2f(1F, 1F),
        )
      }
    }

    companion object {
      fun getTexturedModelData(): TexturedModelData {
        val modelData = ModelData()
        val modelPartData = modelData.root
        val leftLegs = modelPartData.addChild("leftLegs", ModelPartBuilder.create(), ModelTransform.pivot(0.5F, 21.25F, -0.5F))
        leftLegs.addChild("leftLeg0", ModelPartBuilder.create().uv(0, 24).cuboid(-1.5F, -0.5F, -0.5F, 8.0F, 1.0F, 1.0F, Dilation(0.0F)), ModelTransform.pivot(1.0F, -0.75F, -2.0F))
        leftLegs.addChild("leftLeg1", ModelPartBuilder.create().uv(0, 28).cuboid(-1.5F, -0.5F, -0.5F, 8.0F, 1.0F, 1.0F, Dilation(0.0F)), ModelTransform.pivot(1.0F, -0.75F, -0.75F))
        leftLegs.addChild("leftLeg2", ModelPartBuilder.create().uv(0, 24).cuboid(-1.5F, -0.5F, -0.5F, 8.0F, 1.0F, 1.0F, Dilation(0.0F)), ModelTransform.pivot(1.0F, -0.75F, 0.5F))
        leftLegs.addChild("leftLeg3", ModelPartBuilder.create().uv(0, 28).cuboid(-1.5F, -0.5F, -0.5F, 8.0F, 1.0F, 1.0F, Dilation(0.0F)), ModelTransform.pivot(1.0F, -0.75F, 1.75F))

        val rightLegs = modelPartData.addChild("rightLegs", ModelPartBuilder.create(), ModelTransform.pivot(-0.5F, 21.25F, -0.5F))
        rightLegs.addChild("rightLeg0", ModelPartBuilder.create().uv(0, 30).mirrored().cuboid(-6.5F, -0.5F, -0.5F, 8.0F, 1.0F, 1.0F, Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-1.0F, -0.75F, -2.0F))
        rightLegs.addChild("rightLeg1", ModelPartBuilder.create().uv(0, 26).mirrored().cuboid(-6.5F, -0.5F, -0.5F, 8.0F, 1.0F, 1.0F, Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-1.0F, -0.75F, -0.75F))
        rightLegs.addChild("rightLeg2", ModelPartBuilder.create().uv(0, 30).mirrored().cuboid(-6.5F, -0.5F, -0.5F, 8.0F, 1.0F, 1.0F, Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-1.0F, -0.75F, 0.5F))
        rightLegs.addChild("rightLeg3", ModelPartBuilder.create().uv(0, 26).mirrored().cuboid(-6.5F, -0.5F, -0.5F, 8.0F, 1.0F, 1.0F, Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-1.0F, -0.75F, 1.75F))

        modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 9).cuboid(-2.0F, -1.5F, -4.0F, 4.0F, 3.0F, 4.0F, Dilation(0.0F))
          .uv(9, 20).cuboid(-1.5F, 1.5F, -4.0F, 3.0F, 1.0F, 0.0F, Dilation(0.0F)), ModelTransform.pivot(0.0F, 19.5F, -2.0F))

        modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(0, 0).cuboid(-2.5F, -7.0F, 1.0F, 5.0F, 4.0F, 5.0F, Dilation(0.0F))
          .uv(0, 18).cuboid(-1.5F, -5.0F, -2.0F, 3.0F, 2.0F, 3.0F, Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F))
        return TexturedModelData.of(modelData, 32, 32)
      }
    }
  }
}