package dev.mim1q.derelict.entity.boss

import dev.mim1q.derelict.entity.goal.TickingGoal
import dev.mim1q.derelict.entity.spider.control.ArachneBodyControl
import dev.mim1q.derelict.entity.spider.legs.SpiderLegController
import dev.mim1q.derelict.init.ModEntities
import dev.mim1q.derelict.tag.ModBlockTags
import dev.mim1q.derelict.tag.ModEntityTags
import dev.mim1q.derelict.util.entity.trackedEnum
import dev.mim1q.derelict.util.extensions.radians
import dev.mim1q.derelict.util.pickWeightedRandom
import dev.mim1q.derelict.util.wrapDegrees
import dev.mim1q.gimm1q.interpolation.AnimatedProperty
import dev.mim1q.gimm1q.interpolation.AnimatedProperty.EasingFunction
import dev.mim1q.gimm1q.interpolation.Easing
import dev.mim1q.gimm1q.screenshake.ScreenShakeUtils
import net.minecraft.block.BlockState
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import kotlin.math.cos
import kotlin.math.sin

class ArachneEntity(entityType: EntityType<ArachneEntity>, world: World) : HostileEntity(entityType, world) {
    init {
        ignoreCameraFrustum = true
    }

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

    val leftLegStomp = AnimatedProperty(0f, Easing::easeInOutCubic)
    val rightLegStomp = AnimatedProperty(0f, Easing::easeInOutCubic)

    private var screamingTicks = 0
    private var wasScreaming = false
    private var goalsSetup = false
    private var lastStage = 0

    private var currentAttack by trackedEnum(CURRENT_ATTACK, ArachneAttackType.entries)
    private var attackTicks = 0

    private var stompCooldown = 100
    private var spawnCooldown = 400

    override fun initGoals() {
        if (age < 40 || goalsSetup) return

        goalsSetup = true

        goalSelector.clear { true }

        goalSelector.add(3, LookAtEntityGoal(this, PlayerEntity::class.java, 24F))
        goalSelector.add(4, LookAroundGoal(this))
        goalSelector.add(2, WanderAroundGoal(this, 0.4))
        goalSelector.add(2, WanderAroundFarGoal(this, 0.4))
        goalSelector.add(1, MeleeAttackGoal(this, 0.6, true))

        goalSelector.add(0, createSmashAttack())
        goalSelector.add(0, createSpawnAttack())

        if (targetSelector.goals.isEmpty()) {
            targetSelector.add(0, ActiveTargetGoal(this, PlayerEntity::class.java, false))
        }
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

            stompCooldown--
            spawnCooldown--

            if (lastStage != getStage()) {
                (world as? ServerWorld)?.spawnParticles(
                    ParticleTypes.EXPLOSION_EMITTER,
                    pos.x, pos.y, pos.z,
                    8, 2.0, 2.0, 2.0,
                    0.0
                )
                playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0f, 0.8f)
                lastStage = getStage()
            }
        }

        attackTicks++
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
        if (shouldShake()) {
            shakingAnimation.transitionTo(1f, 5f, Easing::easeInOutCubic)
        } else {
            shakingAnimation.transitionTo(0f, 20f, Easing::easeInOutCubic)
        }

        if (shouldRaiseLegs()) {
            legsRaisedAnimation.transitionTo(1f, 15f, Easing::easeOutBack)
        } else {
            legsRaisedAnimation.transitionTo(0f, 40f, Easing::easeInOutCubic)
        }

        if (currentAttack == ArachneAttackType.SMASH) {
            if (attackTicks > 10) {
                when (attackTicks % 20) {
                    1 -> leftLegStomp.transitionTo(1f, 8f, Easing::easeOutBounce)
                    9 -> leftLegStomp.transitionTo(0f, 20f, Easing::easeInOutCubic)

                    11 -> rightLegStomp.transitionTo(1f, 8f, Easing::easeOutBounce)
                    19 -> rightLegStomp.transitionTo(0f, 20f, Easing::easeInOutCubic)
                }
            }
        } else {
            leftLegStomp.transitionTo(0f, 10f, Easing::easeInOutCubic)
            rightLegStomp.transitionTo(0f, 10f, Easing::easeInOutCubic)
        }
    }

    private fun shouldShake() = when {
        dataTracker[SHAKING] -> true
        currentAttack == ArachneAttackType.SPAWN && attackTicks < 100 -> true
        else -> false
    }

    private fun shouldRaiseLegs() = when {
        dataTracker[SCREAMING] -> true
        currentAttack == ArachneAttackType.SMASH -> true
        else -> false
    }

    override fun setBodyYaw(bodyYaw: Float) = super.setBodyYaw(wrapDegrees(this.bodyYaw, bodyYaw, 10f))
    override fun createBodyControl() = ArachneBodyControl(this)
    override fun initDataTracker() {
        super.initDataTracker()

        dataTracker.startTracking(SCREAMING, false)
        dataTracker.startTracking(SHAKING, false)
        dataTracker.startTracking(CURRENT_ATTACK, 0)
    }

    override fun onTrackedDataSet(data: TrackedData<*>) {
        if (data == CURRENT_ATTACK) {
            attackTicks = 0
        }
        super.onTrackedDataSet(data)
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

    private fun getStage() = when (health / maxHealth) {
        in 0.0..0.25 -> 3
        in 0.25..0.5 -> 2
        in 0.5..0.75 -> 1
        else -> 0
    }

    fun getScale() =
        1.0f + 0.2f * getStage()

    private fun ikVec(x: Double, y: Double, z: Double): Vec3d {
        val scale = getScale()
        return Vec3d(x * scale, y * scale, z * scale)
    }

    override fun slowMovement(state: BlockState, multiplier: Vec3d) =
        if (!state.isIn(ModBlockTags.COBWEBS)) super.slowMovement(state, multiplier) else Unit

    private fun createSmashAttack() = createArachneAttack(
        ArachneAttackType.SMASH,
        100,
        tick@{ ticks ->
            val world = this@ArachneEntity.world as? ServerWorld ?: return@tick

            if (ticks > 10 && (ticks % 20 == 5 || ticks % 20 == 15)) {
                ScreenShakeUtils.shakeAround(world, pos, 1f, 20, 4.0, 16.0)
                val bodyYawRad = (bodyYaw + 90).radians()
                val scale = getScale()
                val pos = pos.add(3.0 * cos(bodyYawRad) * scale, 0.0, 3.0 * sin(bodyYawRad) * scale)

                world.spawnParticles(ParticleTypes.EXPLOSION, pos.x, pos.y + 0.5, pos.z, 2, 0.3, 0.3, 0.3, 0.0)
                playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.7f, 0.3f + random.nextFloat() * 0.2f)

                world.getOtherEntities(this, Box.of(pos, 4.0, 2.0, 4.0)).forEach {
                    if (it !is LivingEntity) return@forEach

                    it.damage(
                        damageSources.mobAttack(this),
                        getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE).toFloat() * 2.0f
                    )
                    it.takeKnockback(0.1, x - it.x, z - it.z)
                }
            }
        },
        { this.stompCooldown <= 0 && random.nextFloat() < 0.02f },
        { this.stompCooldown = 200 }
    )

    private fun createSpawnAttack() = createArachneAttack(
        ArachneAttackType.SPAWN,
        300,
        tick@{ ticks ->
            if (ticks <= 80 && ticks % 10 == 0) {
                val entityType = mapOf(
                    ModEntities.SPIDERLING to 80,
                    EntityType.SPIDER to 6,
                    ModEntities.JUMPING_SPIDER to 4,
                    EntityType.CAVE_SPIDER to 2,
                    ModEntities.SPINY_SPIDER to 2,
                    ModEntities.WEB_CASTER to 1,
                ).pickWeightedRandom(random) ?: return@tick

                val world = this@ArachneEntity.world as? ServerWorld ?: return@tick
                val entity = entityType.create(world) ?: return@tick

                entity.setPosition(pos.x + random.nextDouble() - 0.5, pos.y + 0.75, pos.z + random.nextDouble() - 0.5)
                if (target != null) {
                    entity.target = target
                }
                world.spawnEntity(entity)
            }
        },
        {
            spawnCooldown <= 0 && random.nextFloat() < 0.02f && world.getOtherEntities(
                this,
                Box.of(pos, 20.0, 20.0, 20.0)
            ) { it.type.isIn(ModEntityTags.SPAWNS_SPIDERLINGS_ON_DEATH) }.size <= 3
        },
        { spawnCooldown = 1200 }
    )

    private fun createArachneAttack(
        type: ArachneAttackType,
        duration: Int,
        onTick: (Int) -> Unit,
        startPredicate: () -> Boolean,
        cooldownSetter: () -> Unit = { },
    ) = object : TickingGoal(
        duration,
        onTick,
        {
            currentAttack = type
            this@ArachneEntity.navigation.stop()
        },
        {
            currentAttack = ArachneAttackType.NONE
            cooldownSetter()
        },
        { currentAttack == ArachneAttackType.NONE && startPredicate() },
    ) {
        init {
            controls.add(Control.MOVE)
        }
    }

    enum class ArachneAttackType {
        NONE,
        SMASH,
        SPAWN;
    }

    companion object {
        val SCREAMING: TrackedData<Boolean> =
            DataTracker.registerData(ArachneEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)

        val SHAKING: TrackedData<Boolean> =
            DataTracker.registerData(ArachneEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)

        val CURRENT_ATTACK: TrackedData<Int> =
            DataTracker.registerData(ArachneEntity::class.java, TrackedDataHandlerRegistry.INTEGER)

        fun createArachneAttributes(): DefaultAttributeContainer.Builder = createHostileAttributes()
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.6)
            .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0)
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0)
            .add(EntityAttributes.GENERIC_ATTACK_SPEED, 0.5)
    }
}