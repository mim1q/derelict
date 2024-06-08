package dev.mim1q.derelict.entity

import dev.mim1q.derelict.init.ModBlocksAndItems
import dev.mim1q.derelict.util.entity.nullableTracked
import dev.mim1q.derelict.util.entity.tracked
import dev.mim1q.gimm1q.interpolation.AnimatedProperty
import net.minecraft.entity.Bucketable
import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.*

class SpiderlingEntity(entityType: EntityType<out HostileEntity>, world: World) : HostileEntity(entityType, world), Bucketable {
    companion object {
        val ANCHOR_POSITION: TrackedData<Optional<BlockPos>> = DataTracker.registerData(SpiderlingEntity::class.java, TrackedDataHandlerRegistry.OPTIONAL_BLOCK_POS)
        val SPAWNED_FROM_BUCKET: TrackedData<Boolean> = DataTracker.registerData(SpiderlingEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)

        fun createSpiderlingAttributes(): DefaultAttributeContainer.Builder {
            return createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 2.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.33)
        }
    }

    init {
        ignoreCameraFrustum = true
    }

    var anchorPosition: BlockPos? by nullableTracked(ANCHOR_POSITION)
    val anchored = AnimatedProperty(0f)
    private var spawnedFromBucket by tracked(SPAWNED_FROM_BUCKET)

    override fun initDataTracker() {
        super.initDataTracker()
        dataTracker.startTracking(ANCHOR_POSITION, Optional.empty())
        dataTracker.startTracking(SPAWNED_FROM_BUCKET, false)
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

    override fun tick() {
        super.tick()
        if (world.isClient) {
            anchored.transitionTo(if (anchorPosition != null) 1f else 0f, 10f)
        } else {
            if (anchorPosition != null && !world.getBlockState(blockPos.down()).isAir) {
                setVelocity(0.0, 0.01, 0.0)
            }
        }

        if (anchorPosition != null) {
            val direction = Vec3d.ofCenter(anchorPosition!!).subtract(pos).normalize()
            if (direction.horizontalLength() > 0.05) {
                addVelocity(direction.x * 0.1, 0.0, direction.z * 0.1)
            }
            setNoGravity(true)
            if (velocity.y > 0) setVelocity(velocity.x, velocity.y * 0.99, velocity.z)
        } else {
            setNoGravity(false)
        }
    }

    override fun handleFallDamage(fallDistance: Float, damageMultiplier: Float, damageSource: DamageSource) = false

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

    override fun isFromBucket(): Boolean = spawnedFromBucket
    override fun setFromBucket(fromBucket: Boolean) {
        this.spawnedFromBucket = fromBucket
    }

    @Suppress("DEPRECATION")
    override fun copyDataToStack(stack: ItemStack) = Bucketable.copyDataToStack(this, stack)

    @Suppress("DEPRECATION")
    override fun copyDataFromNbt(nbt: NbtCompound) = Bucketable.copyDataFromNbt(this, nbt)
    override fun getBucketItem(): ItemStack = ModBlocksAndItems.SPIDERLING_IN_A_BUCKET.defaultStack
    override fun getBucketFillSound(): SoundEvent = SoundEvents.ENTITY_SPIDER_STEP
    override fun interactMob(player: PlayerEntity, hand: Hand): ActionResult {
        return Bucketable.tryBucket(player, hand, this).orElse(super.interactMob(player, hand))
    }

}

