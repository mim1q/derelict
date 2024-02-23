package dev.mim1q.derelict.compat.emi

import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories
import dev.emi.emi.api.stack.EmiStack
import dev.mim1q.derelict.init.ModBlocksAndItems.AGING_STAFF
import dev.mim1q.derelict.init.ModBlocksAndItems.WAXING_STAFF
import dev.mim1q.derelict.item.getFullAgingMap
import net.minecraft.item.HoneycombItem
import net.minecraft.item.ItemConvertible

object EmiDerelictPlugin : EmiPlugin {
  private val AGING_STAFF_WORKSTATION: EmiStack = EmiStack.of(AGING_STAFF)
  private val WAXING_STAFF_WORKSTATION: EmiStack = EmiStack.of(WAXING_STAFF)

  override fun register(registry: EmiRegistry) {
    HoneycombItem.UNWAXED_TO_WAXED_BLOCKS.get()?.forEach {
      registry.addRecipe(
        EmiWorldInteractionRecipe.builder()
          .leftInput(it.key.emiStack())
          .rightInput(EmiStack.of(WAXING_STAFF), true)
          .output(it.value.emiStack())
          .build()
      )
    }

    getFullAgingMap().forEach {
      registry.addRecipe(
        EmiWorldInteractionRecipe.builder()
          .leftInput(it.key.emiStack())
          .rightInput(EmiStack.of(AGING_STAFF), true)
          .output(it.value.emiStack())
          .build()
      )
    }
  }

  private fun ItemConvertible.emiStack(): EmiStack = EmiStack.of(this)
}