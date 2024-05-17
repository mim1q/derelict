package dev.mim1q.derelict.block.flickering

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.config.DerelictConfigModel.FlickeringLightsSetting
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.state.property.EnumProperty
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

interface FlickeringBlock {
    fun scheduleFlicker(world: World, state: BlockState, pos: BlockPos, random: Random, block: Block) {
        if (world.isClient) return
        if (Derelict.CONFIG.flickeringLights() == FlickeringLightsSetting.FAST) {
            world.setBlockState(pos, state.with(LIGHT_STATE, LightState.FLICKERING))
            return
        }
        val (lightState, delay) = when (Derelict.CONFIG.flickeringLights()) {
            FlickeringLightsSetting.FANCY -> nextFancyFlicker(random, state[LIGHT_STATE])
            else -> if (state[LIGHT_STATE] == LightState.HALF_ON) nextFabulousFlicker(random) else LightState.HALF_ON to 1 + random.nextInt(
                1
            )
        }

        world.setBlockState(pos, state.with(LIGHT_STATE, lightState))
        world.scheduleBlockTick(pos, block, delay)
    }

    fun nextFancyFlicker(random: Random, currentState: LightState): Pair<LightState, Int> {
        return when (currentState) {
            LightState.OFF -> LightState.ON to if (random.nextFloat() < 0.75) (random.nextInt(3) + 1) else (random.nextInt(
                100
            ) + 20)

            else -> LightState.OFF to random.nextInt(3) + 1
        }
    }

    fun nextFabulousFlicker(random: Random): Pair<LightState, Int> {
        if (random.nextFloat() < 0.5) {
            if (random.nextFloat() < 0.5) {
                return LightState.ON to random.nextInt(4) + 2
            }
            return LightState.ON to random.nextInt(100) + 20
        }
        return LightState.OFF to random.nextInt(6) + 1
    }

    companion object {
        fun getLuminance(state: BlockState): Int {
            return when (state[LIGHT_STATE]) {
                LightState.HALF_ON -> 5
                LightState.ON -> 10
                else -> 0
            }
        }

        val LIGHT_STATE: EnumProperty<LightState> = EnumProperty.of("light_state", LightState::class.java)

        enum class LightState(private val id: String) : StringIdentifiable {
            FLICKERING("flickering"),
            OFF("off"),
            HALF_ON("half_on"),
            ON("on");

            override fun asString() = id
        }
    }
}