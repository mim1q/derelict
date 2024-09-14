package dev.mim1q.derelict.entity.spider

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.world.World

class ArachneEggEntity(type: EntityType<*>, world: World) : Entity(type, world) {
    override fun initDataTracker() {
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
    }

    override fun isInvulnerableTo(damageSource: DamageSource): Boolean {
        if (damageSource.isOf(DamageTypes.GENERIC_KILL)) {
            val playerSource = damageSource.source as? PlayerEntity ?: return false
            return playerSource.distanceTo(this) <= 10f
        }
        return true
    }
}