package datagen

import tada.lib.generator.ResourceGenerator
import tada.lib.presets.common.TemplatePoolPresets
import tada.lib.resources.templatepool.TemplatePool

fun ResourceGenerator.spiderCavesTemplatePools() {
    val eggWeights = arrayOf(
        "field_0" to 1,
        "field_1" to 1,
        "field_2" to 1,
        "field_3" to 1,
        "field_4" to 1,
        "field_5" to 1,
        "field_6" to 1,
        "field_7" to 1,
        "small_pile_0" to 1,
        "small_pile_1" to 1,
        "small_pile_2" to 1,
        "small_pile_3" to 1,
        "medium_pile_0" to 1,
        "medium_pile_1" to 1,
        "medium_pile_2" to 1,
        "medium_pile_3" to 1,
        "large_pile_0" to 1,
        "large_pile_1" to 1,
        "large_pile_2" to 1,
    )

    add("spider_caves/spider_eggs", TemplatePool.create("derelict:spider_caves/spider_eggs") {
        eggWeights.forEach {
            single(it.second, "derelict:spider_caves/spider_eggs/${it.first}", "derelict:spider_eggs")
        }
    })

    add(TemplatePoolPresets.single("derelict:spider_caves/boss_arena"))
    add(TemplatePoolPresets.single("derelict:spider_caves/overworld_hint"))
}