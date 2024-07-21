package dev.mim1q.derelict.entity.spider

import dev.mim1q.derelict.entity.spider.legs.SpiderLegController
import dev.mim1q.gimm1q.interpolation.AnimatedProperty
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.goal.LookAroundGoal
import net.minecraft.entity.ai.goal.LookAtEntityGoal
import net.minecraft.entity.ai.goal.WanderAroundFarGoal
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.DyeColor
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import kotlin.math.cos
import kotlin.math.sin

class DaddyLongLegsEntity(
    entityType: EntityType<DaddyLongLegsEntity>,
    world: World
) : PathAwareEntity(entityType, world) {
    private var songSource: BlockPos? = null
    private var isSongPlaying: Boolean = false

    val danceAnimation = AnimatedProperty(0f)
    private var danceOffsetVector: Vec3d = Vec3d.ZERO

    val sockColor: DyeColor?
        get() = dataTracker[SOCK_COLOR].let {
            if (it == -1) null else DyeColor.byId(it)
        }

    val legController = SpiderLegController(
        this,
        32 / 16f,
        32 / 16f,
        //@formatter:off
        { danceOffsetVector.add( 0.25, 60 / 16.0,  3 / 32.0 ) } to { Vec3d( 0.5, 0.0,  0.5) },
        { danceOffsetVector.add( 0.25, 60 / 16.0,  1 / 32.0 ) } to { Vec3d( 0.8, 0.0,  0.1) },
        { danceOffsetVector.add( 0.25, 60 / 16.0, -1 / 32.0 ) } to { Vec3d( 0.8, 0.0, -0.1) },
        { danceOffsetVector.add( 0.25, 60 / 16.0, -3 / 32.0 ) } to { Vec3d( 0.5, 0.0, -0.5) },

        { danceOffsetVector.add(-0.25, 60 / 16.0,  3 / 32.0 ) } to { Vec3d(-0.5, 0.0,  0.5) },
        { danceOffsetVector.add(-0.25, 60 / 16.0,  1 / 32.0 ) } to { Vec3d(-0.8, 0.0,  0.1) },
        { danceOffsetVector.add(-0.25, 60 / 16.0, -1 / 32.0 ) } to { Vec3d(-0.8, 0.0, -0.1) },
        { danceOffsetVector.add(-0.25, 60 / 16.0, -3 / 32.0 ) } to { Vec3d(-0.5, 0.0, -0.5) },
        //@formatter:on
    )

    override fun initGoals() {
        super.initGoals()

        goalSelector.add(4, WanderAroundFarGoal(this, 1.0))
        goalSelector.add(3, LookAtEntityGoal(this, PlayerEntity::class.java, 24F))
        goalSelector.add(3, LookAroundGoal(this))
    }

    override fun initDataTracker() {
        super.initDataTracker()

        dataTracker.startTracking(SOCK_COLOR, -1)
    }

    override fun tick() {
        super.tick()

        if (this.songSource == null || !songSource!!.isWithinDistance(this.pos, 3.46) || !world.getBlockState(this.songSource).isOf(Blocks.JUKEBOX)) {
            this.isSongPlaying = false
            this.songSource = null
        }

        if (world.isClient) {
            legController.tick()

            if (isSongPlaying) {
                danceAnimation.transitionTo(1f, 20f)
            } else {
                danceAnimation.transitionTo(0f, 20f)
            }
            danceOffsetVector = Vec3d(cos(age * 0.5), sin(age * 0.5) - 1.0, 0.0).multiply(danceAnimation.value.toDouble())
        }
    }

    override fun interactMob(player: PlayerEntity, hand: Hand): ActionResult {
        val stack = player.getStackInHand(hand)

        if (stack.isOf(Items.SHEARS) && sockColor != null) {
            if (!world.isClient) {
                playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1f, 1f)
                stack.damage(1, player) { it.sendToolBreakStatus(hand) }
                dataTracker.set(SOCK_COLOR, -1)
            }
            return ActionResult.SUCCESS
        }

        if (WOOL_TO_COLOR.any { it.key == stack.item }) {
            val newColor = WOOL_TO_COLOR[stack.item]
            if (newColor == sockColor) return ActionResult.PASS

            if (!world.isClient) {
                playSound(SoundEvents.BLOCK_WOOL_PLACE, 1f, 1f)
                stack.decrement(1)
                dataTracker.set(SOCK_COLOR, WOOL_TO_COLOR[stack.item]?.id ?: -1)
            }
            return ActionResult.SUCCESS
        }

        return super.interactMob(player, hand)
    }

    override fun setNearbySongPlaying(songPosition: BlockPos, playing: Boolean) {
        this.songSource = songPosition
        this.isSongPlaying = playing
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)

        if (dataTracker.get(SOCK_COLOR) != -1) nbt.putInt("SockColor", dataTracker.get(SOCK_COLOR))
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)

        if (nbt.contains("SockColor")) dataTracker.set(SOCK_COLOR, nbt.getInt("SockColor"))
    }

    companion object {
        private val WOOL_TO_COLOR = mapOf(
            //@formatter:off
            Blocks.WHITE_WOOL.asItem()      to DyeColor.WHITE,
            Blocks.LIGHT_GRAY_WOOL.asItem() to DyeColor.LIGHT_GRAY,
            Blocks.GRAY_WOOL.asItem()       to DyeColor.GRAY,
            Blocks.BLACK_WOOL.asItem()      to DyeColor.BLACK,
            Blocks.BROWN_WOOL.asItem()      to DyeColor.BROWN,
            Blocks.RED_WOOL.asItem()        to DyeColor.RED,
            Blocks.ORANGE_WOOL.asItem()     to DyeColor.ORANGE,
            Blocks.YELLOW_WOOL.asItem()     to DyeColor.YELLOW,
            Blocks.LIME_WOOL.asItem()       to DyeColor.LIME,
            Blocks.GREEN_WOOL.asItem()      to DyeColor.GREEN,
            Blocks.CYAN_WOOL.asItem()       to DyeColor.CYAN,
            Blocks.LIGHT_BLUE_WOOL.asItem() to DyeColor.LIGHT_BLUE,
            Blocks.BLUE_WOOL.asItem()       to DyeColor.BLUE,
            Blocks.PURPLE_WOOL.asItem()     to DyeColor.PURPLE,
            Blocks.MAGENTA_WOOL.asItem()    to DyeColor.MAGENTA,
            Blocks.PINK_WOOL.asItem()       to DyeColor.PINK
            //@formatter:on
        )

        val SOCK_COLOR: TrackedData<Int> = DataTracker.registerData(DaddyLongLegsEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
    }
}