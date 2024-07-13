package dev.mim1q.derelict.entity.spider

import dev.mim1q.derelict.entity.boss.BigSpider
import dev.mim1q.derelict.entity.boss.BigSpiderAnimationProperties
import dev.mim1q.gimm1q.interpolation.AnimatedProperty
import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.world.World

class WebCasterEntity(entityType: EntityType<out HostileEntity>, world: World) : HostileEntity(entityType, world), BigSpider {
    companion object {
        val WEB_HELD: TrackedData<Boolean> = DataTracker.registerData(WebCasterEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
        val WEB_CASTING: TrackedData<Boolean> = DataTracker.registerData(WebCasterEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
    }

    override val bigSpiderAnimationProperties = BigSpiderAnimationProperties(this)

    override fun initDataTracker() {
        super.initDataTracker()
        dataTracker.startTracking(WEB_HELD, false)
        dataTracker.startTracking(WEB_CASTING, false)
    }

    override fun initGoals() {
        super.initGoals()

        goalSelector.add(0, SwimGoal(this))
        goalSelector.add(1, MeleeAttackGoal(this, 1.2, true))
        goalSelector.add(2, LookAtEntityGoal(this, PlayerEntity::class.java, 16.0f))
        goalSelector.add(5, WanderAroundFarGoal(this, 0.8))
        goalSelector.add(6, LookAroundGoal(this))

        targetSelector.add(2, ActiveTargetGoal(this, PlayerEntity::class.java, true))

    }

    private var targetTimer = 100
    private var webCooldown = 0
    private var webHeldTimer = 0

    val webHeldAnimation = AnimatedProperty(0f)
    val webSpinAnimation = AnimatedProperty(0f)


    override fun tick() {
        super.tick()

        if (world.isClient) {
            if (dataTracker[WEB_HELD]) {
                webHeldAnimation.transitionTo(1f, 5f)
            } else {
                webHeldAnimation.transitionTo(0f, 5f)
            }

            if (dataTracker[WEB_CASTING]) {
                webSpinAnimation.transitionTo(1f, 20f)
            } else {
                webSpinAnimation.transitionTo(0f, 20f)
            }
        } else {
            webCooldown--
            if (dataTracker[WEB_HELD]) {
                webHeldTimer--
            }

            if (target == null) {
                targetTimer = 60 + random.nextInt(60)
                dataTracker[WEB_HELD] = false
            } else if (webCooldown <= 0) {
                targetTimer--

                if (targetTimer < 0) {
                    dataTracker[WEB_HELD] = true

                    val checkPos = pos.add(rotationVector.multiply(2.0))
                    if ((target?.squaredDistanceTo(checkPos) ?: 100.0) < 4.0 && webHeldTimer < -40) {
//                        target?.addStatusEffect(
//                            StatusEffectInstance(
//                                ModStatusEffects.COBWEBBED,
//                                60,
//                                2,
//                                true,
//                                false,
//                                true
//                            )
//                        )
                        webCooldown = 200 + random.nextInt(200)
                        webHeldTimer = 60
                        dataTracker[WEB_HELD] = false
                    }
                }
            }
        }
    }
}