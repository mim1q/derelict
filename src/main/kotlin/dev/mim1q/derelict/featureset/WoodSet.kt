package dev.mim1q.derelict.featureset

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.type.BlockSetTypeBuilder
import net.fabricmc.fabric.api.`object`.builder.v1.block.type.WoodTypeBuilder
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry
import net.minecraft.block.*
import net.minecraft.item.SignItem
import net.minecraft.util.Identifier

class WoodSet(
    id: Identifier,
    defaultItemSettings: FabricItemSettings = FabricItemSettings()
) : FeatureSet(id, defaultItemSettings, FabricBlockSettings.copyOf(Blocks.OAK_WOOD)) {
    val blockSetType = BlockSetTypeBuilder.copyOf(BlockSetType.OAK).register(id)
    val woodType = WoodTypeBuilder.copyOf(WoodType.OAK).register(id, blockSetType)

    val planks = Block(defaultBlockSettings())
    val log = PillarBlock(defaultBlockSettings())
    val strippedLog = PillarBlock(defaultBlockSettings())
    val wood = PillarBlock(defaultBlockSettings())
    val strippedWood = PillarBlock(defaultBlockSettings())
    val stairs = StairsBlock(planks.defaultState, defaultBlockSettings())
    val slab = SlabBlock(defaultBlockSettings())
    val door = DoorBlock(defaultBlockSettings().nonOpaque(), blockSetType)
    val trapdoor = TrapdoorBlock(defaultBlockSettings().nonOpaque(), blockSetType)
    val fence = FenceBlock(defaultBlockSettings())
    val fenceGate = FenceGateBlock(defaultBlockSettings(), woodType)
    val button = ButtonBlock(defaultBlockSettings(), blockSetType, 20, true)
    val pressurePlate =
        PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, defaultBlockSettings(), blockSetType)
    val sign = SignBlock(defaultBlockSettings().noCollision(), woodType)
    val wallSign = WallSignBlock(defaultBlockSettings().noCollision(), woodType)

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