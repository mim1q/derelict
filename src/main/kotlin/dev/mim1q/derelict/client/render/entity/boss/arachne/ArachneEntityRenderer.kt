package dev.mim1q.derelict.client.render.entity.boss.arachne

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.client.render.entity.spider.createEyesFeatureRenderer
import dev.mim1q.derelict.entity.boss.ArachneEntity
import dev.mim1q.derelict.init.client.ModRender
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.util.math.MatrixStack

class ArachneEntityRenderer(ctx: EntityRendererFactory.Context) :
    MobEntityRenderer<ArachneEntity, ArachneEntityModel>(
        ctx,
        ArachneEntityModel(ctx.getPart(ModRender.ARACHNE_LAYER)),
        1.5F
    ) {
    init {
        addFeature(createEyesFeatureRenderer(this, TEXTURE))
    }

    override fun setupTransforms(
        entity: ArachneEntity,
        matrices: MatrixStack,
        animationProgress: Float,
        bodyYaw: Float,
        tickDelta: Float
    ) {
        super.setupTransforms(entity, matrices, animationProgress, bodyYaw, tickDelta)

        val scale = entity.getScale()
        matrices.scale(scale, scale, scale)
    }

    override fun getTexture(entity: ArachneEntity) = TEXTURE

    override fun getLyingAngle(entity: ArachneEntity): Float = 0f

    companion object {
        val TEXTURE = Derelict.id("textures/entity/boss/arachne.png")
    }
}