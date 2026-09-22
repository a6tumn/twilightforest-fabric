package twilightforest.tags

import net.minecraft.tags.TagKey
import twilightforest.TFCommon.prefix
import twilightforest.init.TFRegistries
import twilightforest.util.woods.WoodPalette

object TFWoodPaletteTags {
	val WELL_SWIZZLE_MASK = create("well_swizzle_mask")
	val DRUID_HUT_SWIZZLE_MASK = create("druid_hut_swizzle_mask")
	val COMMON_PALETTES = create("common")
	val UNCOMMON_PALETTES = create("uncommon")
	val RARE_PALETTES = create("rare")
	val TREASURE_PALETTES = create("treasure")

	private fun create(name: String): TagKey<WoodPalette> = TagKey.create(TFRegistries.Keys.WOOD_PALETTES, prefix(name))
}