package dev.mim1q.derelict.entity.nonliving

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.util.lerpAngleRadians
import dev.mim1q.gimm1q.interpolation.AnimatedProperty
import dev.mim1q.gimm1q.interpolation.Easing
import net.minecraft.block.Blocks
import net.minecraft.block.FurnaceBlock
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.FallingBlockEntity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.item.SwordItem
import net.minecraft.loot.context.LootContextParameterSet
import net.minecraft.loot.context.LootContextParameters
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.BlockStateParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.MathHelper.HALF_PI
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.joml.Math
import kotlin.math.cos

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
            punchYaw.transitionTo(punchAngle, 30f, Easing::easeInOutQuad)

            if (prevPunchAngle != punchAngle) {
                punchDistance.transitionTo(1f, 5f, Easing::easeOutCubic)

                if (cos(punchTime * 0.2f) < 0) {
                    punchTime += 1f
                }
            } else if (punchDistance.value == 1f) {
                punchDistance.transitionTo(0f, 80f, Easing::easeOutCubic)
            }

            prevPunchAngle = punchAngle
        }
    }

    override fun damage(source: DamageSource, amount: Float): Boolean {
        if (world.isClient) return false

        val player = source.attacker as? PlayerEntity
        val world = world as? ServerWorld
        val cobwebState = Blocks.COBWEB.defaultState

        if (player != null && world != null) {
            if (shouldBreak(player)) {
                if (dataTracker[BROKEN]) {
                    dropLoot(true, source, player)
                    discard()
                } else {
                    dropLoot(false, source, player)
                    dataTracker[BROKEN] = true
                }
                playSound(SoundEvents.BLOCK_WOOL_BREAK, 1.0f, 0.8f)
                world.spawnParticles(
                    BlockStateParticleEffect(ParticleTypes.BLOCK, cobwebState),
                    x, y + 1, z, 20, 0.2, 0.5, 0.2, 1.0
                )
            } else {
                playSound(SoundEvents.BLOCK_WOOL_HIT, 1.0f, 0.8f)
                world.spawnParticles(
                    BlockStateParticleEffect(ParticleTypes.BLOCK, cobwebState),
                    x, y + 1, z, 2, 0.2, 0.5, 0.2, 1.0
                )
            }
        }

        val srcPos = source.position
        if (srcPos != null) {
            dataTracker[PUNCH_ANGLE] = Math.atan2(srcPos.z - z, srcPos.x - x).toFloat() - HALF_PI
        }

        return true
    }

    private fun dropLoot(broken: Boolean, source: DamageSource, player: PlayerEntity) {
        val lootTableId = Derelict.id("misc/hanging_cocoon_${if (broken) "break" else "damage"}")
        (world as? ServerWorld)?.let { serverWorld ->
            val lootTable = serverWorld.server.lootManager.getLootTable(lootTableId)
            lootTable.generateLoot(
                LootContextParameterSet(
                    serverWorld,
                    mapOf(
                        LootContextParameters.THIS_ENTITY to this,
                        LootContextParameters.KILLER_ENTITY to player,
                        LootContextParameters.DAMAGE_SOURCE to source
                    ),
                    mapOf(),
                    player.luck
                )
            ) {
                if (it.isOf(Items.SKELETON_SKULL)) {
                    FallingBlockEntity.spawnFromBlock(world, blockPos, Blocks.FURNACE.defaultState.with(FurnaceBlock.LIT, true))
                } else {
                    val item = ItemEntity(world, x, y, z, it)
                    item.velocity = Vec3d(item.velocity.x * 0.5, -0.1, item.velocity.z * 0.5)
                    item.setPosition(
                        item.pos.add(
                            0.75 * (random.nextDouble() - 0.5),
                            random.nextDouble(),
                            0.75 * (random.nextDouble() - 0.5)
                        )
                    )
                    world.spawnEntity(item)
                }
            }
        }
    }

    private fun shouldBreak(player: PlayerEntity): Boolean {
        if (player.mainHandStack.item is SwordItem || player.offHandStack.item is SwordItem) {
            return true
        }

        return random.nextFloat() < 0.2f
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