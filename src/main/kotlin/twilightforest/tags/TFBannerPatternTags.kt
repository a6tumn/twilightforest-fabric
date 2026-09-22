package twilightforest.tags

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.entity.BannerPattern
import twilightforest.TFCommon.prefix

object TFBannerPatternTags {
	val NAGA_BANNER_PATTERN = create("pattern_item/naga")
	val LICH_BANNER_PATTERN = create("pattern_item/lich")
	val MINOSHROOM_BANNER_PATTERN = create("pattern_item/minoshroom")
	val HYDRA_BANNER_PATTERN = create("pattern_item/hydra")
	val KNIGHT_PHANTOM_BANNER_PATTERN = create("pattern_item/knight_phantom")
	val UR_GHAST_BANNER_PATTERN = create("pattern_item/ur_ghast")
	val ALPHA_YETI_BANNER_PATTERN = create("pattern_item/alpha_yeti")
	val SNOW_QUEEN_BANNER_PATTERN = create("pattern_item/snow_queen")
	val QUESTING_RAM_BANNER_PATTERN = create("pattern_item/questing_ram")

	private fun create(name: String) : TagKey<BannerPattern> = TagKey.create(Registries.BANNER_PATTERN, prefix(name))
}