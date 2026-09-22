package twilightforest.tags

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.damagesource.DamageType
import twilightforest.TFCommon.prefix

object TFDamageTypeTags {
	val BREAKS_LICH_SHIELDS = create("breaks_lich_shields")

	private fun create(name: String): TagKey<DamageType> = TagKey.create(Registries.DAMAGE_TYPE, prefix(name))
}