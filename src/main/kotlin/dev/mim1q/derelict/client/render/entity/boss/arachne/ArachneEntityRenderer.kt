package dev.mim1q.derelict.client.render.entity.boss.arachne

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.entity.boss.ArachneEntity
import dev.mim1q.derelict.init.client.ModRender
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.LivingEntityRenderer

class ArachneEntityRenderer(ctx: EntityRendererFactory.Context) :
    LivingEntityRenderer<ArachneEntity, ArachneEntityModel<ArachneEntity>>(
        ctx,
        ArachneEntityModel(ctx.getPart(ModRender.ARACHNE_LAYER)),
        1.5F
    ) {
    override fun getTexture(entity: ArachneEntity) = TEXTURE

    companion object {
        val TEXTURE = Derelict.id("textures/entity/boss/arachne.png")
    }
}