package dev.mim1q.derelict.client.render.armor

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.init.client.ModRender
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer
import net.minecraft.client.model.*
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.model.BipedEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack

class NetwalkersRenderer(ctx: EntityRendererFactory.Context) : ArmorRenderer {
    private val modelRoot = ctx.getPart(ModRender.NETWALKERS_LAYER)
    private val modelWaist = modelRoot.getChild("waist")
    private val modelLeftLeg = modelRoot.getChild("left_leg")
    private val modelRightLeg = modelRoot.getChild("right_leg")

    override fun render(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        stack: ItemStack,
        entity: LivingEntity,
        slot: EquipmentSlot,
        light: Int,
        contextModel: BipedEntityModel<LivingEntity>
    ) {
        modelWaist.copyTransform(contextModel.body)
        modelRightLeg.copyTransform(contextModel.rightLeg)
        modelLeftLeg.copyTransform(contextModel.leftLeg)

        modelRoot.render(
            matrices,
            vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE)),
            light,
            OverlayTexture.DEFAULT_UV
        )
    }
    companion object {
        val TEXTURE = Derelict.id("textures/armor/netwalkers.png")

        fun createTexturedModelData(): TexturedModelData = ModelData().let {
            it.root.apply {
                addChild("waist", ModelPartBuilder.create().uv(0, 22).cuboid(-4.5F, 7F, -2.5F, 9F, 5F, 5F, Dilation(0.1F)), ModelTransform.NONE)
                addChild("left_leg", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-2.5F, 0F, -2.48F, 5F, 12F, 5F).uv(44, 23).mirrored().cuboid(-2.5F, 4F, -2.48F, 5F, 4F, 5F, Dilation(0.01F)), ModelTransform.pivot(1.9F, 12F, 0F)).apply {
                    addChild("left_leg_r1", ModelPartBuilder.create().uv(30, 20).mirrored().cuboid(0F, -6F, 0F, 1F, 12F, 0F), ModelTransform.of(2.5F, 6F, 2.48F, 0F, -0.7854F, 0F))
                    addChild("left_leg_r2", ModelPartBuilder.create().uv(28, 20).mirrored().cuboid(0F, -6F, 0F, 1F, 12F, 0F), ModelTransform.of(2.5F, 6F, -2.48F, 0F, 0.7854F, 0F))
                }
                addChild("right_leg", ModelPartBuilder.create().uv(20, 0).cuboid(-2.5F, 0F, -2.5F, 5F, 12F, 5F).uv(42, 0).cuboid(-2.75F, -4F, -2.75F, 6F, 9F, 5F), ModelTransform.pivot(-1.9F, 12F, 0F)).apply {
                    addChild("right_leg_r1", ModelPartBuilder.create().uv(32, 20).cuboid(-1F, -6F, 0F, 1F, 12F, 0F), ModelTransform.of(-2.5F, 6F, 2.48F, 0F, 0.7854F, 0F))
                    addChild("right_leg_r2", ModelPartBuilder.create().uv(34, 20).cuboid(-1F, -6F, 0F, 1F, 12F, 0F), ModelTransform.of(-2.5F, 6F, -2.48F, 0F, -0.7854F, 0F))
                }
            }
            TexturedModelData.of(it, 64, 32)
        }
    }
}