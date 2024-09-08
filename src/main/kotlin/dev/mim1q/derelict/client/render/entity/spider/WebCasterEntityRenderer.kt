package dev.mim1q.derelict.client.render.entity.spider

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.entity.spider.WebCasterEntity
import dev.mim1q.derelict.entity.spider.legs.SpiderLegParts
import dev.mim1q.derelict.init.client.ModRender
import dev.mim1q.derelict.util.extensions.radians
import dev.mim1q.derelict.util.extensions.setPartialAnglesDegrees
import dev.mim1q.gimm1q.interpolation.Easing
import net.minecraft.client.model.*
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

class WebCasterEntityRenderer(
    ctx: EntityRendererFactory.Context
) : MobEntityRenderer<WebCasterEntity, WebCasterEntityModel>(
    ctx,
    WebCasterEntityModel(ctx.getPart(ModRender.WEB_CASTER_LAYER)),
    1.5f
) {
    init {
        addFeature(createEyesFeatureRenderer(this, TEXTURE))
    }

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

    private val legs = SpiderLegParts.createArray(torso)

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
        head.yaw = headYaw.radians()
        head.pitch = headPitch.radians()

        val webHold = entity.webHeldAnimation.update(animationProgress)

        web.xScale = webHold
        web.yScale = webHold

        legs[0].joint.setPartialAnglesDegrees(0f, 0f, 75f, webHold)
        legs[0].upper.setPartialAnglesDegrees(0f, -40f, 0f, webHold)
        legs[0].lower.setPartialAnglesDegrees(0f, 40f, 0f, webHold)

        legs[4].joint.setPartialAnglesDegrees(0f, 0f, -75f, webHold)
        legs[4].upper.setPartialAnglesDegrees(0f, 40f, 0f, webHold)
        legs[4].lower.setPartialAnglesDegrees(0f, -40f, 0f, webHold)

        legs[1].joint.setPartialAnglesDegrees(0f, 0f, 76f, webHold)
        legs[1].upper.setPartialAnglesDegrees(0f, 15f, 0f, webHold)
        legs[1].lower.setPartialAnglesDegrees(0f, -16f, 0f, webHold)

        legs[5].joint.setPartialAnglesDegrees(0f, 0f, -76f, webHold)
        legs[5].upper.setPartialAnglesDegrees(0f, -15f, 0f, webHold)
        legs[5].lower.setPartialAnglesDegrees(0f, 16f, 0f, webHold)

        web.pivotZ -= 20f * webHold
        web.pitch -= 10f.radians() * webHold
    }

    override fun animateModel(entity: WebCasterEntity, limbAngle: Float, limbDistance: Float, tickDelta: Float) {
        super.animateModel(entity, limbAngle, limbDistance, tickDelta)

        root.traverse().forEach(ModelPart::resetTransform)
        legs.forEachIndexed { index, it ->
            it.applyIk(entity.legController.getIk(index), Easing.lerp(entity.prevBodyYaw, entity.bodyYaw, tickDelta), tickDelta)
        }
    }

    companion object {
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