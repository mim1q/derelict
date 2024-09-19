package dev.mim1q.derelict.entity.boss

import dev.mim1q.derelict.entity.spider.control.ArachneBodyControl
import dev.mim1q.derelict.entity.spider.legs.SpiderLegController
import dev.mim1q.derelict.tag.ModBlockTags
import dev.mim1q.derelict.util.wrapDegrees
import dev.mim1q.gimm1q.interpolation.AnimatedProperty
import dev.mim1q.gimm1q.interpolation.AnimatedProperty.EasingFunction
import dev.mim1q.gimm1q.interpolation.Easing
import dev.mim1q.gimm1q.screenshake.ScreenShakeUtils
import net.minecraft.block.BlockState
import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class ArachneEntity(entityType: EntityType<ArachneEntity>, world: World) : HostileEntity(entityType, world) {
    val legController = SpiderLegController(
        this,
        { getScale() * 24 / 16f },
        { getScale() * 28 / 16f },
        //@formatter:off
        { ikVec( 0.6, 0.9, 1.0 ) } to { ikVec( 1.5, 0.0,  3.5) },
        { ikVec( 0.6, 1.0, 0.65) } to { ikVec( 2.5, 0.0,  1.5) },
        { ikVec( 0.6, 1.1, 0.3 ) } to { ikVec( 2.0, 0.0,  0.5) },
        { ikVec( 0.6, 1.2, 0.0 ) } to { ikVec( 1.5, 0.0, -1.0) },

        { ikVec(-0.6, 0.9, 1.0 ) } to { ikVec(-1.5, 0.0,  3.5) },
        { ikVec(-0.6, 1.0, 0.65) } to { ikVec(-2.5, 0.0,  1.5) },
        { ikVec(-0.6, 1.1, 0.3 ) } to { ikVec(-2.0, 0.0, -0.5) },
        { ikVec(-0.6, 1.2, 0.0 ) } to { ikVec(-1.5, 0.0, -1.0) },
        //@formatter:on
    )

    val legsRaisedAnimation = AnimatedProperty(0f, Easing::easeOutBack)
    val shakingAnimation = AnimatedProperty(0f, Easing::easeInOutCubic)

    private var screamingTicks = 0
    private var wasScreaming = false
    private var goalsSetup = false

    override fun initGoals() {
        if (age < 40 || goalsSetup) return

        goalsSetup = true

        goalSelector.add(3, LookAtEntityGoal(this, PlayerEntity::class.java, 24F))
        goalSelector.add(4, LookAroundGoal(this))
        goalSelector.add(2, WanderAroundGoal(this, 0.4))
        goalSelector.add(2, WanderAroundFarGoal(this, 0.4))
        goalSelector.add(0, MeleeAttackGoal(this, 0.6, true))

        targetSelector.add(0, ActiveTargetGoal(this, PlayerEntity::class.java, false))
    }

    override fun tick() {
        if (world.isClient) {
            legController.tick()
            handleAnimations()
        } else {
            if (age == 45) {
                initGoals()
            }

            processScreaming()
        }

        super.tick()
    }

    private fun processScreaming() {
        wasScreaming = screamingTicks > 0
        screamingTicks--

        if (age == 20) {
            screamingTicks = 40
        }
        dataTracker.set(SCREAMING, screamingTicks > 0)
        dataTracker.set(SHAKING, screamingTicks > 0)

        if (!wasScreaming && screamingTicks > 0) {
            ScreenShakeUtils.shakeAround(world as ServerWorld, pos, 1.2f, screamingTicks, 5.0, 30.0)
            world.playSound(null, this.blockPos, SoundEvents.ENTITY_SPIDER_AMBIENT, SoundCategory.HOSTILE, 1.3f, 0.3f)
        }
    }

    private fun handleAnimations() {
        legsRaisedAnimation.apply(SCREAMING, 15f, 40f, Easing::easeOutBack, Easing::easeInOutCubic)
        shakingAnimation.apply(SHAKING, 5f, 20f)
    }

    override fun setBodyYaw(bodyYaw: Float) = super.setBodyYaw(wrapDegrees(this.bodyYaw, bodyYaw, 10f))
    override fun createBodyControl() = ArachneBodyControl(this)
    override fun initDataTracker() {
        super.initDataTracker()

        dataTracker.startTracking(SCREAMING, false)
        dataTracker.startTracking(SHAKING, false)
    }

    private fun AnimatedProperty.apply(
        data: TrackedData<Boolean>,
        durationOn: Float,
        durationOff: Float,
        easingOn: EasingFunction = EasingFunction(Easing::easeInOutCubic),
        easingOff: EasingFunction = easingOn
    ) {
        if (dataTracker.get(data)) {
            transitionTo(1f, durationOn, easingOn)
        } else {
            transitionTo(0f, durationOff, easingOff)
        }
    }

    fun getScale() =
        1.5f - (health / maxHealth) * 0.5f

    private fun ikVec(x: Double, y: Double, z: Double): Vec3d {
        val scale = getScale()
        return Vec3d(x * scale, y * scale, z * scale)
    }

    override fun slowMovement(state: BlockState, multiplier: Vec3d) =
        if (!state.isIn(ModBlockTags.COBWEBS)) super.slowMovement(state, multiplier) else Unit

    companion object {
        val SCREAMING: TrackedData<Boolean> =
            DataTracker.registerData(ArachneEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)

        val SHAKING: TrackedData<Boolean> =
            DataTracker.registerData(ArachneEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)

        fun createArachneAttributes(): DefaultAttributeContainer.Builder = createHostileAttributes()
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.6)
            .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0)

    }
}