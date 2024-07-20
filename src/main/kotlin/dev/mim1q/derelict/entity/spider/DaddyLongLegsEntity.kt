package dev.mim1q.derelict.entity.spider

import dev.mim1q.derelict.entity.spider.legs.SpiderLegController
import dev.mim1q.gimm1q.interpolation.AnimatedProperty
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.goal.LookAroundGoal
import net.minecraft.entity.ai.goal.LookAtEntityGoal
import net.minecraft.entity.ai.goal.WanderAroundFarGoal
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import kotlin.math.cos
import kotlin.math.sin

class DaddyLongLegsEntity(
    entityType: EntityType<DaddyLongLegsEntity>,
    world: World
) : PathAwareEntity(entityType, world) {
    private var songSource: BlockPos? = null
    private var isSongPlaying: Boolean = false

    val danceAnimation = AnimatedProperty(0f)
    var danceOffsetVector: Vec3d = Vec3d.ZERO


    val legController = SpiderLegController(
        this,
        32 / 16f,
        32 / 16f,
        //@formatter:off
        { danceOffsetVector.add( 0.25, 60 / 16.0,  3 / 32.0 ) } to { Vec3d( 0.5, 0.0,  0.5) },
        { danceOffsetVector.add( 0.25, 60 / 16.0,  1 / 32.0 ) } to { Vec3d( 0.8, 0.0,  0.1) },
        { danceOffsetVector.add( 0.25, 60 / 16.0, -1 / 32.0 ) } to { Vec3d( 0.8, 0.0, -0.1) },
        { danceOffsetVector.add( 0.25, 60 / 16.0, -3 / 32.0 ) } to { Vec3d( 0.5, 0.0, -0.5) },

        { danceOffsetVector.add(-0.25, 60 / 16.0,  3 / 32.0 ) } to { Vec3d(-0.5, 0.0,  0.5) },
        { danceOffsetVector.add(-0.25, 60 / 16.0,  1 / 32.0 ) } to { Vec3d(-0.8, 0.0,  0.1) },
        { danceOffsetVector.add(-0.25, 60 / 16.0, -1 / 32.0 ) } to { Vec3d(-0.8, 0.0, -0.1) },
        { danceOffsetVector.add(-0.25, 60 / 16.0, -3 / 32.0 ) } to { Vec3d(-0.5, 0.0, -0.5) },
        //@formatter:on
    )

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
            legController.tick()

            if (isSongPlaying) {
                danceAnimation.transitionTo(1f, 20f)
                danceOffsetVector = Vec3d(cos(age * 0.3), sin(age * 0.3), 0.0).multiply(danceAnimation.value.toDouble())
            } else {
                danceAnimation.transitionTo(0f, 20f)
                danceOffsetVector = Vec3d.ZERO
            }
        }
    }

    override fun setNearbySongPlaying(songPosition: BlockPos, playing: Boolean) {
        this.songSource = songPosition
        this.isSongPlaying = playing
    }
}