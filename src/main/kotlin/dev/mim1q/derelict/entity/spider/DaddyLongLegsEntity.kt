package dev.mim1q.derelict.entity.spider

import dev.mim1q.gimm1q.interpolation.AnimatedProperty
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.goal.LookAroundGoal
import net.minecraft.entity.ai.goal.LookAtEntityGoal
import net.minecraft.entity.ai.goal.WanderAroundFarGoal
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class DaddyLongLegsEntity(
    entityType: EntityType<DaddyLongLegsEntity>,
    world: World
) : PathAwareEntity(entityType, world) {
    private var songSource: BlockPos? = null
    private var isSongPlaying: Boolean = false

    val danceAnimation = AnimatedProperty(0f)

    override fun initGoals() {
        super.initGoals()

        goalSelector.add(4, WanderAroundFarGoal(this, 1.0))
        goalSelector.add(3, LookAtEntityGoal(this, PlayerEntity::class.java, 24F))
        goalSelector.add(3, LookAroundGoal(this))
    }

    override fun tick() {
        super.tick()

        if (this.songSource == null || !songSource!!.isWithinDistance(this.pos, 3.46) || !world.getBlockState(this.songSource).isOf(Blocks.JUKEBOX)) {
            this.isSongPlaying = false
            this.songSource = null
        }

        if (world.isClient) {
            if (isSongPlaying) {
                danceAnimation.transitionTo(1f, 20f)
            } else {
                danceAnimation.transitionTo(0f, 20f)
            }
        }
    }

    override fun setNearbySongPlaying(songPosition: BlockPos, playing: Boolean) {
        this.songSource = songPosition
        this.isSongPlaying = playing
    }
}