package dev.mim1q.derelict.compat.rei

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.init.ModBlocksAndItems.AGING_STAFF
import dev.mim1q.derelict.init.ModBlocksAndItems.WAXING_STAFF
import dev.mim1q.derelict.item.getFullAgingMap
import me.shedaniel.math.Point
import me.shedaniel.math.Rectangle
import me.shedaniel.rei.api.client.gui.widgets.Widget
import me.shedaniel.rei.api.client.gui.widgets.Widgets
import me.shedaniel.rei.api.client.plugins.REIClientPlugin
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry
import me.shedaniel.rei.api.client.registry.display.DisplayCategory
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.display.basic.BasicDisplay
import me.shedaniel.rei.api.common.entry.EntryIngredient
import me.shedaniel.rei.api.common.entry.EntryStack
import me.shedaniel.rei.api.common.util.EntryStacks
import me.shedaniel.rei.plugin.client.DefaultClientPlugin
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

object ReiDerelictPlugin : REIClientPlugin {
    val AGING_CATEGORY_ID: CategoryIdentifier<DefaultAgingDisplay> = CategoryIdentifier.of(Derelict.id("aging"))

    @Suppress("UnstableApiUsage")
    override fun registerCategories(registry: CategoryRegistry) {
        registry.add(AgingCategory)
        registry.addWorkstations(AGING_CATEGORY_ID, EntryStacks.of(AGING_STAFF))
        registry.addWorkstations(DefaultClientPlugin.WAXING, EntryStacks.of(WAXING_STAFF))
    }

    override fun registerDisplays(registry: DisplayRegistry) {
        getFullAgingMap().forEach {
            registry.add(DefaultAgingDisplay(it.key, it.value))
        }
    }

    class DefaultAgingDisplay private constructor(
        inputs: List<EntryIngredient>,
        outputs: List<EntryIngredient>
    ) : BasicDisplay(inputs, outputs) {
        constructor(input: ItemConvertible, output: ItemConvertible) : this(
            listOf(EntryIngredient.of(EntryStacks.of(input))),
            listOf(EntryIngredient.of(EntryStacks.of(output)))
        )

        override fun getCategoryIdentifier() = AGING_CATEGORY_ID
    }

    object AgingCategory : DisplayCategory<DefaultAgingDisplay> {
        override fun getCategoryIdentifier() = AGING_CATEGORY_ID

        override fun getIcon(): EntryStack<ItemStack> = EntryStacks.of(AGING_STAFF)

        override fun getTitle(): Text = Text.translatable("category.derelict.aging")

        override fun setupDisplay(display: DefaultAgingDisplay, bounds: Rectangle): List<Widget> {
            val startPoint = Point(bounds.centerX - 41, bounds.centerY - 13)
            return listOf(
                Widgets.createRecipeBase(bounds),
                Widgets.createArrow(Point(startPoint.x + 27, startPoint.y + 4)),
                Widgets.createResultSlotBackground(Point(startPoint.x + 61, startPoint.y + 5)),
                Widgets.createSlot(Point(startPoint.x + 4, startPoint.y + 5))
                    .entries(display.inputEntries[0])
                    .markInput(),
                Widgets.createSlot(Point(startPoint.x + 61, startPoint.y + 5))
                    .entries(display.outputEntries[0])
                    .disableBackground()
                    .markOutput()
            )
        }

        override fun getDisplayHeight() = 36
    }
}