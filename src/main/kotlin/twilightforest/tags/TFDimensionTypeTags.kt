package twilightforest.tags

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.dimension.DimensionType
import twilightforest.TFCommon.prefix

object TFDimensionTypeTags {
	val ALLOWS_MAGIC_MAP_CHARTING = create("allows_magic_map_charting")
	val MOON_DIAL_INDETERMINATE = create("moon_dial_indeterminate")

	private fun create(tagName: String): TagKey<DimensionType> = TagKey.create(Registries.DIMENSION_TYPE, prefix(tagName))
}