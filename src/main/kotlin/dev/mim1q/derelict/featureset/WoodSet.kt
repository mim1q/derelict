package dev.mim1q.derelict.featureset

import net.fabricmc.fabric.api.`object`.builder.v1.block.type.BlockSetTypeBuilder
import net.fabricmc.fabric.api.`object`.builder.v1.block.type.WoodTypeBuilder
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry
import net.minecraft.block.*
import net.minecraft.item.Item
import net.minecraft.item.SignItem
import net.minecraft.util.Identifier

class WoodSet(
    id: Identifier,
    defaultItemSettings: Item.Settings = Item.Settings(),
) : FeatureSet(id, defaultItemSettings, AbstractBlock.Settings.copy(Blocks.OAK_WOOD)) {
    val blockSetType = BlockSetTypeBuilder.copyOf(BlockSetType.OAK).register(id)
    val woodType = WoodTypeBuilder.copyOf(WoodType.OAK).register(id, blockSetType)

    val planks = Block(defaultBlockSettings())
    val log = PillarBlock(defaultBlockSettings())
    val strippedLog = PillarBlock(defaultBlockSettings())
    val wood = PillarBlock(defaultBlockSettings())
    val strippedWood = PillarBlock(defaultBlockSettings())
    val stairs = StairsBlock(planks.defaultState, defaultBlockSettings())
    val slab = SlabBlock(defaultBlockSettings())
    val door = DoorBlock(blockSetType, defaultBlockSettings().nonOpaque())
    val trapdoor = TrapdoorBlock(blockSetType, defaultBlockSettings().nonOpaque())
    val fence = FenceBlock(defaultBlockSettings())
    val fenceGate = FenceGateBlock(woodType, defaultBlockSettings())
    val button = ButtonBlock(blockSetType, 20, defaultBlockSettings())
    val pressurePlate =
        PressurePlateBlock(blockSetType, defaultBlockSettings())
    val sign = SignBlock(woodType, defaultBlockSettings().noCollision())
    val wallSign = WallSignBlock(woodType, defaultBlockSettings().noCollision())

    override fun register(): WoodSet = this.apply {
        registerBlockWithItem("${name}_planks", planks)
        registerBlockWithItem("${name}_log", log)
        registerBlockWithItem("stripped_${name}_log", strippedLog)
        registerBlockWithItem("${name}_wood", wood)
        registerBlockWithItem("stripped_${name}_wood", strippedWood)
        registerBlockWithItem("${name}_stairs", stairs)
        registerBlockWithItem("${name}_slab", slab)
        registerBlockWithItem("${name}_door", door)
        registerBlockWithItem("${name}_trapdoor", trapdoor)
        registerBlockWithItem("${name}_fence", fence)
        registerBlockWithItem("${name}_fence_gate", fenceGate)
        registerBlockWithItem("${name}_button", button)
        registerBlockWithItem("${name}_pressure_plate", pressurePlate)
        registerBlock("${name}_sign", sign)
        registerBlock("${name}_wall_sign", wallSign)
        registerItem("${name}_sign", SignItem(defaultItemSettings.maxCount(16), sign, wallSign))
        StrippableBlockRegistry.register(log, strippedLog)
        StrippableBlockRegistry.register(wood, strippedWood)
    }
}