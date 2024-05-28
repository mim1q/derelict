package dev.mim1q.derelict.entity

import dev.mim1q.derelict.util.entity.nullableTracked
import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*

class SpiderlingEntity(entityType: EntityType<out HostileEntity>, world: World) : HostileEntity(entityType, world) {
    var anchorPosition: BlockPos? by nullableTracked(ANCHOR_POSITION)

    override fun initDataTracker() {
        super.initDataTracker()
        dataTracker.startTracking(ANCHOR_POSITION, Optional.empty())
    }

    override fun initGoals() {
        super.initGoals()

        goalSelector.add(4, WanderAroundGoal(this, 1.0))
        goalSelector.add(5, WanderAroundFarGoal(this, 1.0))

        goalSelector.add(1, MeleeAttackGoal(this, 1.0, true))
        goalSelector.add(0, PounceAtTargetGoal(this, 0.3f))
        goalSelector.add(1, LookAtEntityGoal(this, PlayerEntity::class.java, 8.0f))

        targetSelector.add(0, ActiveTargetGoal(this, SheepEntity::class.java, true))
        targetSelector.add(0, ActiveTargetGoal(this, PlayerEntity::class.java, true))
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        val anchorPosition = anchorPosition
        if (anchorPosition != null) {
            nbt.putLong("AnchorPosition", anchorPosition.asLong())
        }
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        if (nbt.contains("AnchorPosition")) {
            anchorPosition = BlockPos.fromLong(nbt.getLong("AnchorPosition"))
        }
    }

    companion object {
        val ANCHOR_POSITION: TrackedData<Optional<BlockPos>> = DataTracker.registerData(SpiderlingEntity::class.java, TrackedDataHandlerRegistry.OPTIONAL_BLOCK_POS)

        fun createSpiderlingAttributes(): DefaultAttributeContainer.Builder {
            return createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 2.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.33)
        }
    }
}

