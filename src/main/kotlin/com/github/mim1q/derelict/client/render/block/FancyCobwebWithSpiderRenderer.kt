package com.github.mim1q.derelict.client.render.block

import com.github.mim1q.derelict.Derelict
import com.github.mim1q.derelict.block.cobweb.FancyCobwebWithSpiderBlockEntity
import com.github.mim1q.derelict.init.client.ModRender
import com.github.mim1q.derelict.util.Easing
import net.minecraft.client.model.*
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory.Context
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3f

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
    val yaw = MathHelper.lerp(tickDelta, entity.lastClientYaw, entity.clientYaw)
    val vertices = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE))
    matrices.entry {
      scale(-1F, -1F, 1F)
      translate(-0.5, -1.5, 0.5)

      matrices.entry {
        translate(0.0, 1.0, 0.0)
        scale(1F, progress * entity.distance.toFloat(), 1F)
        model.renderString(matrices, vertices, light, overlay)
      }

      matrices.entry {
        translate(0.0, progress * entity.distance + 0.9F, 0.0)
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(yaw))
        matrices.scale(entity.scale, entity.scale, entity.scale)
        model.render(matrices, vertices, light, overlay, 1F, 1F, 1F, 1F)
      }
    }
  }

  override fun rendersOutsideBoundingBox(blockEntity: FancyCobwebWithSpiderBlockEntity) = true

  companion object {
    val TEXTURE = Derelict.id("textures/blockentity/spider.png")
  }

  class SpiderModel(root: ModelPart) : Model(RenderLayer::getEntityCutout) {
    private val spider: ModelPart = root.getChild("spider")
    private val string: ModelPart = root.getChild("string")

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
      spider.render(matrices, vertices, light, overlay)
    }

    fun renderString(matrices: MatrixStack, vertices: VertexConsumer, light: Int, overlay: Int) {
      string.render(matrices, vertices, light, overlay)
    }

    companion object {
      fun getTexturedModelData(): TexturedModelData {
        val modelData = ModelData()
        val modelPartData = modelData.root
        modelPartData.addChild(
          "spider",
          ModelPartBuilder.create().uv(0, 6).cuboid(-4.5f, -12.0f, 0.0f, 9.0f, 12.0f, 0.0f, Dilation(0.0f))
            .uv(0, 0).cuboid(-1.5f, -8.0f, -1.0f, 3.0f, 4.0f, 2.0f, Dilation(0.0f)),
          ModelTransform.pivot(0.0f, 8.5f, 0.0f)
        )
        modelPartData.addChild(
          "string",
          ModelPartBuilder.create().uv(30, 0).cuboid(-0.5f, -8.5f, 0.0f, 1.0f, 16.0f, 0.0f, Dilation(0.0f)),
          ModelTransform.of(0.0f, 8.5f, 0.0f, 0.0f, 0.78f, 0.0f)
        )
        return TexturedModelData.of(modelData, 32, 32)
      }
    }
  }
}