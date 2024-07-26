package dev.mim1q.derelict.entity.nonliving

import dev.mim1q.derelict.util.lerpAngleRadians
import dev.mim1q.gimm1q.interpolation.AnimatedProperty
import dev.mim1q.gimm1q.interpolation.Easing
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.MathHelper.HALF_PI
import net.minecraft.world.World
import org.joml.Math

class HangingCocoonEntity(type: EntityType<*>, world: World) : Entity(type, world) {
    init {
        intersectionChecked = true
    }

    val punchYaw = AnimatedProperty(0f) { start, end, delta ->
        lerpAngleRadians(start, end, Easing.easeOutQuad(0f, 1f, delta))
    }

    private var prevPunchAngle = 0f
    var punchTime = 0f
        private set
    val punchDistance = AnimatedProperty(0f)

    override fun tick() {
        super.tick()

        if (world.isClient) {
            punchTime++

            val punchAngle = dataTracker[PUNCH_ANGLE]
            punchYaw.transitionTo(punchAngle, 5f)

            if (prevPunchAngle != punchAngle) {
                punchDistance.transitionTo(1f, 10f, Easing::easeOutCubic)
                punchTime = 0f
            } else if (punchDistance.value == 1f) {
                punchDistance.transitionTo(0f, 80f, Easing::easeOutCubic)
            }

            prevPunchAngle = punchAngle
        }
    }

    override fun damage(source: DamageSource, amount: Float): Boolean {
        if (world.isClient) return false

        if (!dataTracker[BROKEN]) {
            dataTracker[BROKEN] = true
        } else {
//            discard()
        }

        val srcPos = source.position
        if (srcPos != null) {
            dataTracker[PUNCH_ANGLE] = Math.atan2(srcPos.z - z, srcPos.x - x).toFloat() - HALF_PI
        }

        return true
    }

    override fun initDataTracker() {
        dataTracker.startTracking(BROKEN, false)
        dataTracker.startTracking(PUNCH_ANGLE, 0f)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        dataTracker[BROKEN] = nbt.getBoolean("Broken")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        nbt.putBoolean("Broken", dataTracker[BROKEN])
    }

    override fun canHit(): Boolean = true

    companion object {
        val BROKEN: TrackedData<Boolean> =
            DataTracker.registerData(HangingCocoonEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
        val PUNCH_ANGLE: TrackedData<Float> =
            DataTracker.registerData(HangingCocoonEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
    }
}