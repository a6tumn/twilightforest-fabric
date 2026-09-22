package twilightforest.tags

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.decoration.painting.PaintingVariant
import twilightforest.TFCommon.prefix

object TFPaintingVariantTags {
	val LICH_TOWER_PAINTINGS = create("tower_paintings")
	val LICH_BOSS_PAINTINGS = create("tower_boss_paintings")

	private fun create(tagName: String): TagKey<PaintingVariant> = TagKey.create(Registries.PAINTING_VARIANT, prefix(tagName))
}