package datagen.custom

import tada.lib.presets.Preset
import tada.lib.tags.TagManager

object CustomTagPresets {
    fun unburnedWoodTags() = Preset {
        listOf<Pair<String, (String) -> String>>(
            "unburned_planks" to { "${it}_planks" },
            "unburned_logs" to { "${it}_log" },
            "unburned_stripped_logs" to { "stripped_${it}_log" },
            "unburned_wood" to { "${it}_wood" },
            "unburned_stripped_wood" to { "stripped_${it}_wood" },
            "unburned_wooden_stairs" to { "${it}_stairs" },
            "unburned_wooden_slabs" to { "${it}_slab" },
            "unburned_wooden_doors" to { "${it}_door" },
            "unburned_wooden_trapdoors" to { "${it}_trapdoor" },
            "unburned_wooden_fences" to { "${it}_fence" },
            "unburned_wooden_fences_gates" to { "${it}_fence_gate" },
            "unburned_wooden_pressure_plates" to { "${it}_pressure_plate" },
            "unburned_wooden_buttons" to { "${it}_button" },
            "unburned_wooden_signs" to { "${it}_sign" },
            "unburned_leaves" to { "${it}_leaves" },
            "unburned_cover_boards" to { "derelict:${it}_cover_board" },
            "unburned_double_cover_boards" to { "derelict:double_${it}_cover_boards" },
            "unburned_crossed_cover_boards" to { "derelict:crossed_${it}_cover_boards" },
        ).forEach {
            val vanillaWoods = listOf("oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry")
            TagManager.add("derelict:items/${it.first}", *vanillaWoods.map { type -> it.second(type) }.toTypedArray())
        }
    }
}