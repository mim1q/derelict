package dev.mim1q.derelict.entity.spider

import dev.mim1q.derelict.entity.spider.control.ArachneBodyControl
import dev.mim1q.derelict.entity.spider.legs.SpiderLegController
import dev.mim1q.derelict.init.ModStatusEffects
import dev.mim1q.derelict.tag.ModBlockTags
import dev.mim1q.derelict.util.wrapDegrees
import dev.mim1q.gimm1q.interpolation.AnimatedProperty
import dev.mim1q.gimm1q.interpolation.Easing
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.passive.PigEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import kotlin.math.pow

class WebCasterEntity(entityType: EntityType<WebCasterEntity>, world: World) : HostileEntity(entityType, world) {
    companion object {
        val WEB_HELD: TrackedData<Boolean> = DataTracker.registerData(WebCasterEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
    }

    val legController = SpiderLegController(
        this,
        24 / 16f,
        28 / 16f,
        //@formatter:off
        { Vec3d( 5 / 16.0, 0.6, 7 / 16.0) } to { Vec3d( 1.5, 0.0,  2.0) },
        { Vec3d( 5 / 16.0, 0.6, 5 / 16.0) } to { Vec3d( 2.0, 0.0,  1.5) },
        { Vec3d( 5 / 16.0, 0.6, 3 / 16.0) } to { if (dataTracker[WEB_HELD]) Vec3d(2.0, 0.0, 1.5) else Vec3d(2.0, 0.0, -1.0) },
        { Vec3d( 5 / 16.0, 0.6, 1 / 16.0) } to { Vec3d( 1.5, 0.0, -1.5) },

        { Vec3d(-5 / 16.0, 0.6, 7 / 16.0) } to { Vec3d(-1.5, 0.0,  2.0) },
        { Vec3d(-5 / 16.0, 0.6, 5 / 16.0) } to { Vec3d(-2.0, 0.0,  1.5) },
        { Vec3d(-5 / 16.0, 0.6, 3 / 16.0) } to { if (dataTracker[WEB_HELD]) Vec3d(-2.0, 0.0, 1.5) else Vec3d(-2.0, 0.0, -1.0) },
        { Vec3d(-5 / 16.0, 0.6, 1 / 16.0) } to { Vec3d(-1.5, 0.0, -1.5) },
        //@formatter:on
    )

    override fun initDataTracker() {
        super.initDataTracker()
        dataTracker.startTracking(WEB_HELD, false)
    }

    override fun initGoals() {
        goalSelector.add(0, SwimGoal(this))
        goalSelector.add(1, MeleeAttackGoal(this, 1.2, true))
        goalSelector.add(2, LookAtEntityGoal(this, PlayerEntity::class.java, 16.0f))
        goalSelector.add(5, WanderAroundFarGoal(this, 0.8))
        goalSelector.add(6, LookAroundGoal(this))

        targetSelector.add(2, ActiveTargetGoal(this, PlayerEntity::class.java, true))
        targetSelector.add(2, ActiveTargetGoal(this, PigEntity::class.java, true))

    }

    private var targetTimer = 100
    private var webCooldown = 100
    private var webHeldTimer = 40

    val webHeldAnimation = AnimatedProperty(0f)

    override fun tick() {
        if (world.isClient) {
            legController.tick()
        }

        super.tick()

        if (world.isClient) {
            handleAnimations()
        } else {
            tickWebLogic()
        }
    }

    private fun handleAnimations() {
        if (dataTracker[WEB_HELD]) {
            webHeldAnimation.transitionTo(1f, 15f, Easing::easeOutBack)
        } else {
            webHeldAnimation.transitionTo(0f, 5f, Easing::easeOutCubic)
        }
    }

    private fun tickWebLogic() {
        webCooldown--
        if (dataTracker[WEB_HELD]) {
            webHeldTimer--

            if (target == null) {
                targetTimer = 60 + random.nextInt(60)
                dataTracker[WEB_HELD] = false
                return
            }
        }

        if (webCooldown <= 0 && target != null) {
            targetTimer--
            if (targetTimer >= 0) return
            if (!dataTracker[WEB_HELD]) {

                dataTracker[WEB_HELD] = true
                playSound(
                    SoundEvents.BLOCK_WOOL_PLACE,
                    1.0f,
                    soundPitch
                )
                playSound(
                    SoundEvents.ENTITY_SPIDER_AMBIENT,
                    1.0f,
                    soundPitch
                )
            }
        }
    }

    override fun tryAttack(target: Entity): Boolean {
        val webHeld = dataTracker[WEB_HELD]
        if (webHeld && webHeldTimer > 0) return false

        if (webHeld && target is LivingEntity) {
            target.addStatusEffect(
                StatusEffectInstance(
                    ModStatusEffects.COBWEBBED,
                    60,
                    2,
                    true,
                    false,
                    true
                )
            )

            webCooldown = 200 + random.nextInt(200)
            webHeldTimer = 40
            dataTracker[WEB_HELD] = false

            playSound(
                SoundEvents.BLOCK_WOOL_PLACE,
                1.0f,
                soundPitch
            )

            return false
        }

        return super.tryAttack(target)
    }

    override fun getSoundPitch(): Float = 0.6f + random.nextFloat() * 0.25f
    override fun squaredAttackRange(target: LivingEntity): Double = (width + 1.0).pow(2) + target.width
    override fun setBodyYaw(bodyYaw: Float) = super.setBodyYaw(wrapDegrees(this.bodyYaw, bodyYaw, 10f))
    override fun createBodyControl() = ArachneBodyControl(this)
    override fun slowMovement(state: BlockState, multiplier: Vec3d) =
        if (!state.isIn(ModBlockTags.COBWEBS)) super.slowMovement(state, multiplier) else Unit
}