package dev.mim1q.derelict.item.armor

import dev.mim1q.derelict.init.ModBlocksAndItems
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.recipe.Ingredient
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import java.util.function.Supplier

enum class ModArmorMaterials(
    private val id: String,
    private val durabilityMultiplier: Int,
    private val protectionAmounts: IntArray,
    private val enchantability: Int,
    private val equipSound: SoundEvent,
    private val toughness: Float,
    private val knockbackResistance: Float,
    private val repairIngredientSupplier: Supplier<Ingredient>
) : ArmorMaterial {

    SPIDER(
        "derelict_spider",
        20, intArrayOf(3, 7, 5, 3), 10,
        SoundEvents.ENTITY_SPIDER_STEP,
        0.0f, 0.0f,
        { Ingredient.ofItems(ModBlocksAndItems.SPIDER_MOLT) }
    );

    override fun getDurability(type: ArmorItem.Type): Int = BASE_DURABILITY[type.ordinal] * this.durabilityMultiplier
    override fun getProtection(type: ArmorItem.Type): Int = protectionAmounts[type.ordinal]
    override fun getEnchantability(): Int = this.enchantability
    override fun getEquipSound(): SoundEvent = this.equipSound
    override fun getRepairIngredient(): Ingredient = repairIngredientSupplier.get()
    override fun getName(): String = this.id
    override fun getToughness(): Float = this.toughness
    override fun getKnockbackResistance(): Float = this.knockbackResistance

    companion object {
        private val BASE_DURABILITY = intArrayOf(13, 15, 16, 11)
    }
}
