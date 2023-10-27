package dev.mim1q.derelict.util.extensions

import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.util.math.MatrixStack
import org.joml.Matrix3f
import org.joml.Matrix4f
import org.joml.Vector2f

fun VertexConsumer.produceVertex(
  positionMatrix: Matrix4f,
  normalMatrix: Matrix3f,
  light: Int,
  x: Float,
  y: Float,
  z: Float,
  textureU: Float,
  textureV: Float,
  red: Int,
  green: Int,
  blue: Int,
  alpha: Int
) {
  vertex(positionMatrix, x, y, z)
    .color(red, green, blue, alpha)
    .texture(textureU, textureV)
    .overlay(OverlayTexture.DEFAULT_UV)
    .light(light)
    .normal(normalMatrix, 0F, -1F, 0F)
    .next()
}

fun VertexConsumer.drawBillboard(
  matrices: MatrixStack,
  light: Int,
  from: Vector2f,
  to: Vector2f,
  fromUv: Vector2f = Vector2f(0F, 0F),
  toUv: Vector2f = Vector2f(1F, 1F),
  red: Int = 255,
  green: Int = 255,
  blue: Int = 255,
  alpha: Int = 255,
  doubleSided: Boolean = true
) {
  val posMatrix = matrices.peek().positionMatrix
  val normalMatrix = matrices.peek().normalMatrix

  produceVertex(posMatrix, normalMatrix, light, from.x, from.y, 0.0f, fromUv.x, fromUv.y, red, green, blue, alpha)
  produceVertex(posMatrix, normalMatrix, light, to  .x, from.y, 0.0f, toUv  .x, fromUv.y, red, green, blue, alpha)
  produceVertex(posMatrix, normalMatrix, light, to  .x, to  .y, 0.0f, toUv  .x, toUv  .y, red, green, blue, alpha)
  produceVertex(posMatrix, normalMatrix, light, from.x, to  .y, 0.0f, fromUv.x, toUv  .y, red, green, blue, alpha)
  if (doubleSided) {
    produceVertex(posMatrix, normalMatrix, light, to  .x, from.y, 0.0f, toUv  .x, fromUv.y, red, green, blue, alpha)
    produceVertex(posMatrix, normalMatrix, light, from.x, from.y, 0.0f, fromUv.x, fromUv.y, red, green, blue, alpha)
    produceVertex(posMatrix, normalMatrix, light, from.x, to  .y, 0.0f, fromUv.x, toUv  .y, red, green, blue, alpha)
    produceVertex(posMatrix, normalMatrix, light, to  .x, to  .y, 0.0f, toUv  .x, toUv  .y, red, green, blue, alpha)
  }
}