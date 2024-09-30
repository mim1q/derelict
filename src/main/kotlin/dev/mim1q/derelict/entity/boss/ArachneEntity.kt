package dev.mim1q.derelict.entity.boss

import dev.mim1q.derelict.entity.goal.ReturnGoal
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
import dev.mim1q.gimm1q.interpolation.Easing
import dev.mim1q.gimm1q.screenshake.ScreenShakeUtils
import net.minecraft.block.BlockState
import net.minecraft.entity.*
import net.minecraft.entity.ai.goal.ActiveTargetGoal
import net.minecraft.entity.ai.goal.LookAroundGoal
import net.minecraft.entity.ai.goal.LookAtEntityGoal
import net.minecraft.entity.ai.goal.MeleeAttackGoal
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.boss.BossBar
import net.minecraft.entity.boss.ServerBossBar
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.thrown.SnowballEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.explosion.Explosion
import net.minecraft.world.explosion.ExplosionBehavior
import kotlin.math.cos
import kotlin.math.sin

class ArachneEntity(entityType: EntityType<ArachneEntity>, world: World) : HostileEntity(entityType, world) {
    init {
        ignoreCameraFrustum = true
    }

    var spawnPos: BlockPos? = null

    private val bossBar = ServerBossBar(name, BossBar.Color.RED, BossBar.Style.NOTCHED_12)

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

    val fangsAnimation = AnimatedProperty(0f, Easing::easeInOutCubic)

    private var screamingTicks = 0
    private var wasScreaming = false
    private var goalsSetup = false
    private var lastStage = 0

    private var currentAttack by trackedEnum(CURRENT_ATTACK, ArachneAttackType.entries)
    private var attackTicks = 0

    private var stompCooldown = 100
    private var spawnCooldown = 600
    private var rushCooldown = 400
    private var shootCooldown = 80

    override fun initGoals() {
        if (age < 40 || goalsSetup) return

        goalsSetup = true

        goalSelector.clear { true }

        goalSelector.add(4, LookAroundGoal(this))
        goalSelector.add(3, LookAtEntityGoal(this, PlayerEntity::class.java, 24F))
        goalSelector.add(2, MeleeAttackGoal(this, 0.6, true))

        goalSelector.add(1, createSmashAttack())
        goalSelector.add(1, createSpawnAttack())
        goalSelector.add(1, createRushAttack())
        goalSelector.add(1, createShootAttack())

        goalSelector.add(0, ReturnGoal(this, { !arePlayersNearby() }, { spawnPos }))

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
            bossBar.percent = health / maxHealth

            stompCooldown--
            spawnCooldown--
            rushCooldown--
            shootCooldown--

            if (lastStage != getStage()) {
                (world as? ServerWorld)?.spawnParticles(
                    ParticleTypes.EXPLOSION_EMITTER,
                    pos.x, pos.y, pos.z,
                    8, 2.0, 2.0, 2.0,
                    0.0
                )
                playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0f, 0.8f)
                world.getEntitiesByClass(LivingEntity::class.java, boundingBox.expand(8.0)) { it != this }.forEach {
                    it.takeKnockback(1.0, it.x - x, it.z - z)
                }
                lastStage = getStage()
            }

            if (!arePlayersNearby()) {
                if (age % 20 == 0) {
                    heal(0.5f)
                }
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

        if (shouldRaiseFangs()) {
            fangsAnimation.transitionTo(1f, 15f, Easing::easeOutBack)
        } else {
            fangsAnimation.transitionTo(0f, 15f, Easing::easeInOutCubic)
        }

        if (currentAttack == ArachneAttackType.SMASH) {
            if (attackTicks > 70) {
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

    private fun shouldRaiseFangs() = when {
        currentAttack == ArachneAttackType.SHOOT -> true
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

    private fun getStage() = when (health / maxHealth) {
        in 0.0..0.25 -> 3
        in 0.25..0.5 -> 2
        in 0.5..0.75 -> 1
        else -> 0
    }

    fun getScale() = 1.0f + 0.05f * getStage()

    override fun getBoundingBox(pose: EntityPose): Box =
        super.getBoundingBox(pose).let {
            val scale = (getScale() - 1) * 0.5
            it.expand(it.xLength * scale, it.yLength * scale, it.zLength * scale)
        }

    private fun ikVec(x: Double, y: Double, z: Double): Vec3d {
        val scale = getScale()
        return Vec3d(x * scale, y * scale, z * scale)
    }

    override fun onStartedTrackingBy(player: ServerPlayerEntity) {
        super.onStartedTrackingBy(player)
        bossBar.addPlayer(player)
    }

    override fun onStoppedTrackingBy(player: ServerPlayerEntity) {
        super.onStoppedTrackingBy(player)
        bossBar.removePlayer(player)
    }

    override fun slowMovement(state: BlockState, multiplier: Vec3d) =
        if (!state.isIn(ModBlockTags.COBWEBS)) super.slowMovement(state, multiplier) else Unit

    private fun setGlobalCooldown() {
        spawnCooldown = spawnCooldown.coerceAtLeast(20)
        stompCooldown = stompCooldown.coerceAtLeast(20)
        rushCooldown = rushCooldown.coerceAtLeast(20)
    }

    private fun createSmashAttack() = createArachneAttack(
        ArachneAttackType.SMASH,
        160,
        tick@{ ticks ->
            val world = this@ArachneEntity.world as? ServerWorld ?: return@tick

            if (ticks > 70 && (ticks % 20 == 5 || ticks % 20 == 15)) {
                val target = target
                if (target != null) {
                    navigation.startMovingTo(target, 0.5)
                }

                ScreenShakeUtils.shakeAround(world, pos, 1f, 20, 4.0, 16.0)
                val bodyYawRad = (bodyYaw + 90).radians()
                val scale = getScale()
                val pos = pos.add(3.0 * cos(bodyYawRad) * scale, 0.0, 3.0 * sin(bodyYawRad) * scale)

                world.spawnParticles(ParticleTypes.EXPLOSION, pos.x, pos.y + 0.5, pos.z, 2, 0.3, 0.3, 0.3, 0.0)
                playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.7f, 0.3f + random.nextFloat() * 0.2f)

                world.getOtherEntities(this, Box.of(pos, 5.0, 4.0, 5.0)).forEach {
                    if (it !is LivingEntity) return@forEach

                    it.damage(
                        damageSources.mobAttack(this),
                        getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE).toFloat() * 1.5f
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
            spawnCooldown <= 0 && random.nextFloat() < 0.01f && world.getOtherEntities(
                this,
                Box.of(pos, 20.0, 20.0, 20.0)
            ) { it.type.isIn(ModEntityTags.SPAWNS_SPIDERLINGS_ON_DEATH) }.size <= 3
        },
        { spawnCooldown = 1200 }
    )

    private fun createRushAttack() = createArachneAttack(
        ArachneAttackType.RUSH,
        50,
        tick@{ ticks ->
            (world as? ServerWorld)?.spawnParticles(
                ParticleTypes.LARGE_SMOKE,
                pos.x, pos.y + 0.5, pos.z,
                3, 0.5, 0.2, 0.5, 0.01
            )

            if (ticks < 30) {
                return@tick
            }
            if (ticks == 30) {
                addVelocity(0.0, 0.5, 0.0)
            }

            val bodyYawRad = (bodyYaw + 90).radians()
            move(MovementType.SELF, Vec3d(cos(bodyYawRad) * 0.7, 0.0, sin(bodyYawRad) * 0.7))
        },
        { rushCooldown <= 0 && random.nextFloat() < 0.01f },
        {
            world.createExplosion(
                this,
                null,
                object : ExplosionBehavior() {
                    override fun canDestroyBlock(
                        explosion: Explosion,
                        world: BlockView,
                        pos: BlockPos,
                        state: BlockState,
                        power: Float
                    ): Boolean = false
                },
                pos.add(0.0, 0.5, 0.0),
                1.5f,
                false,
                World.ExplosionSourceType.MOB
            )
            rushCooldown = 200
            setGlobalCooldown()
        },
        { ticks -> !(ticks > 40 && (prevX == x && prevZ == z)) }
    )

    private fun createShootAttack() = createArachneAttack(
        ArachneAttackType.SHOOT,
        40,
        tick@{ ticks ->
            val target = target
            if (ticks == 30 && target != null) {
                val projectile = SnowballEntity(EntityType.SNOWBALL, world)
                val diff = target.pos.add(0.0, 1.5, 0.0).subtract(pos)
                projectile.velocity = diff.normalize().multiply(0.5)
                projectile.setPosition(pos.add(0.0, 0.5, 0.0).add(rotationVector))
                projectile.owner = this@ArachneEntity
            }
        },
        { shootCooldown <= 0 && random.nextFloat() < 0.1f && (target != null && target!!.distanceTo(this) > 6.0) },
        { shootCooldown = 100 }
    )

    private fun createArachneAttack(
        type: ArachneAttackType,
        duration: Int,
        onTick: (ticks: Int) -> Unit,
        startPredicate: () -> Boolean,
        cooldownSetter: () -> Unit = { },
        shouldContinue: (ticks: Int) -> Boolean = { true },
    ) = object : TickingGoal(
        duration,
        onTick,
        {
            currentAttack = type
            this@ArachneEntity.navigation.stop()
        },
        {
            currentAttack = ArachneAttackType.NONE
            setGlobalCooldown()
            cooldownSetter()
        },
        { currentAttack == ArachneAttackType.NONE && startPredicate() },
        shouldContinue
    ) {
        init {
            controls.add(Control.MOVE)
        }
    }

    override fun tryAttack(target: Entity): Boolean {
        val result = super.tryAttack(target)
        if (result && target is PlayerEntity && random.nextFloat() < 0.1f) {
            target.startRiding(this, true)
        }

        return result
    }

    override fun isInvulnerableTo(damageSource: DamageSource): Boolean =
        dataTracker[SCREAMING] || !arePlayersNearby() || super.isInvulnerableTo(damageSource)

    override fun updatePassengerPosition(passenger: Entity, positionUpdater: PositionUpdater) {
        if (this.hasPassenger(passenger)) {
            val y = this.y + 0.3 + passenger.heightOffset
            val bodyYawRad = (bodyYaw + 90).radians()
            val dx = cos(bodyYawRad) * 2.0 * getScale()
            val dz = sin(bodyYawRad) * 2.0 * getScale()
            positionUpdater.accept(passenger, this.x + dx, y, this.z + dz)
        }
    }

    private fun arePlayersNearby(): Boolean {
        val pos = Vec3d.ofBottomCenter(spawnPos ?: blockPos)

        return world.getClosestPlayer(pos.x, pos.y, pos.z, 16.0, false) != null
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)

        if (nbt.contains("spawn_pos")) spawnPos = BlockPos.fromLong(nbt.getLong("spawn_pos"))
        if (nbt.contains("stomp_cooldown")) stompCooldown = nbt.getInt("stomp_cooldown")
        if (nbt.contains("rush_cooldown")) rushCooldown = nbt.getInt("rush_cooldown")
        if (nbt.contains("shoot_cooldown")) shootCooldown = nbt.getInt("shoot_cooldown")
        if (nbt.contains("spawn_cooldown")) spawnCooldown = nbt.getInt("spawn_cooldown")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)

        if (spawnPos != null) nbt.putLong("spawn_pos", spawnPos!!.asLong())
        nbt.putInt("stomp_cooldown", stompCooldown)
        nbt.putInt("rush_cooldown", rushCooldown)
        nbt.putInt("shoot_cooldown", shootCooldown)
        nbt.putInt("spawn_cooldown", spawnCooldown)
    }

    enum class ArachneAttackType {
        NONE,
        SMASH,
        SPAWN,
        RUSH,
        SHOOT;
    }

    companion object {
        val SCREAMING: TrackedData<Boolean> =
            DataTracker.registerData(ArachneEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)

        val SHAKING: TrackedData<Boolean> =
            DataTracker.registerData(ArachneEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)

        val CURRENT_ATTACK: TrackedData<Int> =
            DataTracker.registerData(ArachneEntity::class.java, TrackedDataHandlerRegistry.INTEGER)

        fun createArachneAttributes(): DefaultAttributeContainer.Builder = createHostileAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 200.0)
            .add(EntityAttributes.GENERIC_ARMOR, 4.0)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.45)
            .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0)
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0)
            .add(EntityAttributes.GENERIC_ATTACK_SPEED, 0.5)
    }
}