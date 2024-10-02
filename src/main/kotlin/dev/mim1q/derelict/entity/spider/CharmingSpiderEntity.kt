package dev.mim1q.derelict.entity.spider

import dev.mim1q.gimm1q.interpolation.AnimatedProperty
import dev.mim1q.gimm1q.interpolation.Easing
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityType
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.SpiderEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class CharmingSpiderEntity(
    entityType: EntityType<out SpiderEntity>,
    world: World
) : SpiderEntity(entityType, world) {
    companion object {
        fun createCharmingSpiderAttributes(): DefaultAttributeContainer.Builder = createSpiderAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0)
    }

    private var songSource: BlockPos? = null
    private var isSongPlaying: Boolean = false

    val tailDanceAnimation = AnimatedProperty(0f)
    val bodyDanceAnimation = AnimatedProperty(0f, Easing::easeOutBack)

    override fun initDataTracker() {
        super.initDataTracker()
    }

    override fun tick() {
        super.tick()

        if (this.songSource == null || !songSource!!.isWithinDistance(this.pos, 3.46) || !world.getBlockState(this.songSource).isOf(Blocks.JUKEBOX)) {
            this.isSongPlaying = false
            this.songSource = null
        }

        if (world.isClient) {
            if (isSongPlaying) {
                tailDanceAnimation.transitionTo(1f, 10f)

                when (age % 20) {
                    0 -> bodyDanceAnimation.transitionTo(1f, 10f)
                    10 -> bodyDanceAnimation.transitionTo(-1f, 10f)
                }
            } else {
                tailDanceAnimation.transitionTo(0f, 10f)
                bodyDanceAnimation.transitionTo(0f, 10f)
            }
        }
    }

    override fun setNearbySongPlaying(songPosition: BlockPos, playing: Boolean) {
        this.songSource = songPosition
        this.isSongPlaying = playing
    }
}