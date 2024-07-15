package dev.mim1q.derelict.client.render.entity.spider

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.entity.spider.WebCasterEntity
import dev.mim1q.derelict.init.client.ModRender
import dev.mim1q.derelict.util.extensions.radians
import dev.mim1q.derelict.util.extensions.setPartialAnglesDegrees
import net.minecraft.client.model.*
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import kotlin.math.min

class WebCasterEntityRenderer(
    ctx: EntityRendererFactory.Context
) : MobEntityRenderer<WebCasterEntity, WebCasterEntityModel>(
    ctx,
    WebCasterEntityModel(ctx.getPart(ModRender.WEB_CASTER_LAYER)),
    1.5f
) {
    override fun getTexture(entity: WebCasterEntity): Identifier = TEXTURE

    companion object {
        val TEXTURE = Derelict.id("textures/entity/spider/web_caster.png")
    }
}

class WebCasterEntityModel(part: ModelPart) : EntityModel<WebCasterEntity>(RenderLayer::getEntityCutout) {

    private val root = part.getChild("root")
    private val web = root.getChild("web")
    private val torso = root.getChild("torso")
    private val head = torso.getChild("head")
    private val back = torso.getChild("back")

    //@formatter:off
    private val leftLegs = arrayOf(
        BigSpiderLimb(torso,  75F, 15F, { 9 / 16F }, LIMB_LENGTH, FORELIMB_LENGTH, 0, false),
        BigSpiderLimb(torso,  30F, 25F, { 9 / 16F }, LIMB_LENGTH, FORELIMB_LENGTH, 1, false),
        BigSpiderLimb(torso, -15F, 20F, { 9 / 16F }, LIMB_LENGTH, FORELIMB_LENGTH, 2, false),
        BigSpiderLimb(torso, -50F, 5F,  { 9 / 16F }, LIMB_LENGTH, FORELIMB_LENGTH, 3, false),
    )
    private val rightLegs = arrayOf(
        BigSpiderLimb(torso,  75F, 15F, { 9 / 16F }, LIMB_LENGTH, FORELIMB_LENGTH, 0, true),
        BigSpiderLimb(torso,  30F, 25F, { 9 / 16F }, LIMB_LENGTH, FORELIMB_LENGTH, 1, true),
        BigSpiderLimb(torso, -15F, 20F, { 9 / 16F }, LIMB_LENGTH, FORELIMB_LENGTH, 2, true),
        BigSpiderLimb(torso, -50F, 5F,  { 9 / 16F }, LIMB_LENGTH, FORELIMB_LENGTH, 3, true),
    )
    //@formatter:on

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
        entity: WebCasterEntity,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        root.traverse().forEach(ModelPart::resetTransform)

        idleAnimation(leftLegs, animationProgress, 1f - limbDistance)
        idleAnimation(rightLegs, animationProgress, 1f - limbDistance)

        bigSpiderWalkAnimation(torso, back, leftLegs, rightLegs, limbAngle * 0.5f, min(limbDistance, 0.75f))

        head.yaw = headYaw.radians()
        head.pitch = headPitch.radians()

        val webHold = entity.webHeldAnimation.update(animationProgress)

        web.xScale = webHold
        web.yScale = webHold

        leftLegs[0].joint.setPartialAnglesDegrees(0f, 0f, 75f, webHold)
        leftLegs[0].limb.setPartialAnglesDegrees(0f, -40f, 0f, webHold)
        leftLegs[0].forelimb.setPartialAnglesDegrees(0f, 40f, 0f, webHold)

        rightLegs[0].joint.setPartialAnglesDegrees(0f, 0f, -75f, webHold)
        rightLegs[0].limb.setPartialAnglesDegrees(0f, 40f, 0f, webHold)
        rightLegs[0].forelimb.setPartialAnglesDegrees(0f, -40f, 0f, webHold)

        leftLegs[1].joint.setPartialAnglesDegrees(0f, 0f, 76f, webHold)
        leftLegs[1].limb.setPartialAnglesDegrees(0f, 15f, 0f, webHold)
        leftLegs[1].forelimb.setPartialAnglesDegrees(0f, -16f, 0f, webHold)

        rightLegs[1].joint.setPartialAnglesDegrees(0f, 0f, -76f, webHold)
        rightLegs[1].limb.setPartialAnglesDegrees(0f, -15f, 0f, webHold)
        rightLegs[1].forelimb.setPartialAnglesDegrees(0f, 16f, 0f, webHold)

        leftLegs[2].joint.yaw += 90f.radians() * webHold
        rightLegs[2].joint.yaw -= 90f.radians() * webHold

        leftLegs[3].joint.yaw += 30f.radians() * webHold
        rightLegs[3].joint.yaw -= 30f.radians() * webHold

        web.pivotZ -= 20f * webHold
        web.pitch -= 10f.radians() * webHold
    }

    private fun idleAnimation(legs: Array<BigSpiderLimb>, animationProgress: Float, delta: Float) {
        legs[0].setAnglesFromDefaults()
        legs[1].setAnglesFromDefaults(yawDegrees = 25f)
        legs[2].setAnglesFromDefaults(yawDegrees = -45f)
        legs[3].setAnglesFromDefaults(yawDegrees = -25f, rollDegrees = 15f)
    }

    companion object {
        const val LIMB_LENGTH = 24 / 16F
        const val FORELIMB_LENGTH = 28 / 16F

        fun createTexturedModelData(): TexturedModelData = ModelData().let {
            it.root.apply {
                addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0F, 24F, 0F)).apply {
                    addChild("torso", ModelPartBuilder.create().uv(44, 52).cuboid(-4F, -3F, -4F, 8F, 6F, 8F), ModelTransform.pivot(0F, -8F, -4F)).apply {
                        addChild("head", ModelPartBuilder.create().uv(0, 54).cuboid(-3.5F, -2.5F, -8F, 7F, 5F, 8F).uv(0, 7).cuboid(-4.5F, -3F, -8.5F, 4F, 4F, 3F).uv(0, 0).cuboid(0.5F, -3F, -8.5F, 4F, 4F, 3F), ModelTransform.pivot(0F, -1.5F, -4F)).apply {
                            addChild("right_fang", ModelPartBuilder.create(), ModelTransform.pivot(-2F, 2.5F, -6F)).apply {
                                addChild("cube_r1", ModelPartBuilder.create().uv(60, 24).cuboid(-1F, -3.5F, -1.5F, 2F, 6F, 3F), ModelTransform.of(0F, 3.5F, 0F, 0F, 0.7854F, 0F))
                            }
                            addChild("left_fang", ModelPartBuilder.create(), ModelTransform.pivot(2F, 2.5F, -6F)).apply {
                                addChild("cube_r2", ModelPartBuilder.create().uv(30, 54).cuboid(-1F, -3.5F, -1.5F, 2F, 6F, 3F), ModelTransform.of(0F, 3.5F, 0F, 0F, -0.7854F, 0F))
                            }
                        }
                        addChild("back", ModelPartBuilder.create().uv(0, 0).cuboid(-3F, -3F, 0F, 6F, 6F, 20F), ModelTransform.pivot(0F, 0F, 4F))
                        addChild("left_leg_joint0", ModelPartBuilder.create(), ModelTransform.pivot(4F, 0F, -3F)).apply {
                            addChild("left_leg0", ModelPartBuilder.create().uv(52, 20).cuboid(0F, -1F, -1F, 24F, 2F, 2F).uv(0, 14).cuboid(23.5F, -1.5F, -1.5F, 3F, 3F, 3F), ModelTransform.NONE).apply {
                                addChild("left_leg_front0", ModelPartBuilder.create().uv(0, 34).cuboid(-1F, -1F, -1F, 28F, 2F, 2F), ModelTransform.pivot(25F, 0F, 0F))
                            }
                        }
                        addChild("left_leg_joint1", ModelPartBuilder.create(), ModelTransform.pivot(4F, 0F, -1F)).apply {
                            addChild("left_leg1", ModelPartBuilder.create().uv(50, 48).cuboid(0F, -1F, -1F, 24F, 2F, 2F).uv(0, 14).cuboid(23.5F, -1.5F, -1.5F, 3F, 3F, 3F), ModelTransform.NONE).apply {
                                addChild("left_leg_front1", ModelPartBuilder.create().uv(32, 16).cuboid(-1F, -1F, -1F, 28F, 2F, 2F), ModelTransform.pivot(25F, 0F, 0F))
                            }
                        }
                        addChild("left_leg_joint2", ModelPartBuilder.create(), ModelTransform.pivot(4F, 0F, 1F)).apply {
                            addChild("left_leg2", ModelPartBuilder.create().uv(50, 44).cuboid(0F, -1F, -1F, 24F, 2F, 2F).uv(0, 14).cuboid(23.5F, -1.5F, -1.5F, 3F, 3F, 3F), ModelTransform.NONE).apply {
                                addChild("left_leg_front2", ModelPartBuilder.create().uv(32, 12).cuboid(-1F, -1F, -1F, 28F, 2F, 2F), ModelTransform.pivot(25F, 0F, 0F))
                            }
                        }
                        addChild("left_leg_joint3", ModelPartBuilder.create(), ModelTransform.pivot(4F, 0F, 3F)).apply {
                            addChild("left_leg3", ModelPartBuilder.create().uv(50, 40).cuboid(0F, -1F, -1F, 24F, 2F, 2F).uv(0, 14).cuboid(23.5F, -1.5F, -1.5F, 3F, 3F, 3F), ModelTransform.NONE).apply {
                                addChild("left_leg_front3", ModelPartBuilder.create().uv(32, 8).cuboid(-1F, -1F, -1F, 28F, 2F, 2F), ModelTransform.pivot(25F, 0F, 0F))
                            }
                        }
                        addChild("right_leg_joint0", ModelPartBuilder.create(), ModelTransform.pivot(-4F, 0F, -3F)).apply {
                            addChild("right_leg0", ModelPartBuilder.create().uv(0, 50).cuboid(-24F, -1F, -1F, 24F, 2F, 2F).uv(0, 14).mirrored().cuboid(-26.5F, -1.5F, -1.5F, 3F, 3F, 3F), ModelTransform.NONE).apply {
                                addChild("right_leg_front0", ModelPartBuilder.create().uv(32, 4).cuboid(-27F, -1F, -1F, 28F, 2F, 2F), ModelTransform.pivot(-25F, 0F, 0F))
                            }
                        }
                        addChild("right_leg_joint1", ModelPartBuilder.create(), ModelTransform.pivot(-4F, 0F, -1F)).apply {
                            addChild("right_leg1", ModelPartBuilder.create().uv(0, 46).cuboid(-24F, -1F, -1F, 24F, 2F, 2F).uv(0, 14).mirrored().cuboid(-26.5F, -1.5F, -1.5F, 3F, 3F, 3F), ModelTransform.NONE).apply {
                                addChild("right_leg_front1", ModelPartBuilder.create().uv(32, 0).cuboid(-27F, -1F, -1F, 28F, 2F, 2F), ModelTransform.pivot(-25F, 0F, 0F))
                            }
                        }
                        addChild("right_leg_joint2", ModelPartBuilder.create(), ModelTransform.pivot(-4F, 0F, 1F)).apply {
                            addChild("right_leg2", ModelPartBuilder.create().uv(0, 42).cuboid(-24F, -1F, -1F, 24F, 2F, 2F).uv(0, 14).mirrored().cuboid(-26.5F, -1.5F, -1.5F, 3F, 3F, 3F), ModelTransform.NONE).apply {
                                addChild("right_leg_front2", ModelPartBuilder.create().uv(0, 30).cuboid(-27F, -1F, -1F, 28F, 2F, 2F), ModelTransform.pivot(-25F, 0F, 0F))
                            }
                        }
                        addChild("right_leg_joint3", ModelPartBuilder.create(), ModelTransform.pivot(-4F, 0F, 3F)).apply {
                            addChild("right_leg3", ModelPartBuilder.create().uv(0, 38).cuboid(-24F, -1F, -1F, 24F, 2F, 2F).uv(0, 14).mirrored().cuboid(-26.5F, -1.5F, -1.5F, 3F, 3F, 3F), ModelTransform.NONE).apply {
                                addChild("right_leg_front3", ModelPartBuilder.create().uv(0, 26).cuboid(-27F, -1F, -1F, 28F, 2F, 2F), ModelTransform.pivot(-25F, 0F, 0F))
                            }
                        }
                    }
                    addChild("web", ModelPartBuilder.create().uv(0, 104).cuboid(-16F, -12F, 0F, 32F, 24F, 0F), ModelTransform.pivot(0F, -13F, -30F))
                }
            }
            TexturedModelData.of(it, 128, 128)
        }
    }
}