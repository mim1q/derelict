package com.github.mim1q.derelict.client.render.entity.boss

import com.github.mim1q.derelict.entity.boss.ArachneEntity
import com.github.mim1q.derelict.util.extensions.radians
import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.MathHelper
import kotlin.math.acos
import kotlin.math.sin

class ArachneEntityModel(root: ModelPart) : EntityModel<ArachneEntity>(RenderLayer::getEntityCutout) {
  private val body = root.getChild("body")
  private val sternum = body.getChild("sternum")
  private val abdomen = sternum.getChild("abdomen")
  private val eggs = IntRange(0, 16).map { abdomen.getChild("eggs").getChild("egg$it") }
  private val head = body.getChild("head")

  private var additionalBodyHeight = 0.0F

  class ArachneLimbParts(
    private val parent: ArachneEntityModel,
    private val height: Float,
    number: Int,
    right: Boolean
  ) {
    private val prefix = if (right) "right" else "left"
    private val multiplier = if (right) -1 else 1
    private val joint: ModelPart = parent.body.getChild("${prefix}LimbJoint$number")
    private val limb: ModelPart = joint.getChild("${prefix}Limb$number")
    private val forelimb: ModelPart = limb.getChild("${prefix}Forelimb$number")

    fun setAngles(
      yawDegrees: Float, rollDegrees: Float, additionalRollDegrees: Float = 0.0F
    ) {
      joint.setAngles(0F, 0F, 0F)
      limb.setAngles(0F, 0F, 0F)
      forelimb.setAngles(0F, 0F, 0F)
      joint.yaw = yawDegrees.radians()
      limb.roll = -rollDegrees.radians() - additionalRollDegrees.radians()
      val jointToGround = (parent.additionalBodyHeight / 16F) + height + LIMB_LENGTH * sin(rollDegrees.radians())
      val forelimbAngle = acos(MathHelper.clamp(jointToGround / FORELIMB_LENGTH, -1.0F, 1.0F))
      forelimb.roll = MathHelper.HALF_PI + rollDegrees.radians() - forelimbAngle
      joint.yaw *= multiplier
      limb.roll *= multiplier
      forelimb.roll *= multiplier
    }
  }

  private val leftLegs = arrayOf(
    ArachneLimbParts(this, 13/16F, 0, false),
    ArachneLimbParts(this, 15/16F, 1, false),
    ArachneLimbParts(this, 17/16F, 2, false),
    ArachneLimbParts(this, 19/16F, 3, false),
  )
  private val rightLegs = arrayOf(
    ArachneLimbParts(this, 13/16F, 0, true),
    ArachneLimbParts(this, 15/16F, 1, true),
    ArachneLimbParts(this, 17/16F, 2, true),
    ArachneLimbParts(this, 19/16F, 3, true),
  )

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
    matrices.push()
    body.render(matrices, vertices, light, overlay, red, green, blue, alpha)
    matrices.pop()
  }

  override fun setAngles(
    entity: ArachneEntity,
    limbAngle: Float,
    limbDistance: Float,
    animationProgress: Float,
    headYaw: Float,
    headPitch: Float
  ) {
    additionalBodyHeight = sin(animationProgress * 0.1F) * 3F
    val additionalRoll = additionalBodyHeight * -2F
    body.pivotY = 14F - additionalBodyHeight
    leftLegs[0].setAngles(75.0F, 24.0F + additionalRoll)
    rightLegs[0].setAngles(75.0F, 24.0F + additionalRoll)
    leftLegs[1].setAngles(30.0F, 24.0F + additionalRoll)
    rightLegs[1].setAngles(30.0F, 24.0F + additionalRoll)
    leftLegs[2].setAngles(-15.0F, 24.0F + additionalRoll)
    rightLegs[2].setAngles(-15.0F, 24.0F + additionalRoll)
    leftLegs[3].setAngles(-45.0F, 15.0F + additionalRoll)
    rightLegs[3].setAngles(-45.0F, 15.0F + additionalRoll)
    eggs.forEachIndexed { index, egg ->
      val speed = 7F + sin(index * 100F) * 3F
      val scale = 1F + sin((animationProgress + index * speed) * 0.25F) * 0.1F
      egg.xScale = scale; egg.yScale = scale; egg.zScale = scale
    }
    abdomen.pitch = (sin(animationProgress * 0.1F - 0.5F) * 10F + 10F).radians()
  }

  companion object {
    const val LIMB_LENGTH = 22/16F
    const val FORELIMB_LENGTH = 30/16F
  }
}