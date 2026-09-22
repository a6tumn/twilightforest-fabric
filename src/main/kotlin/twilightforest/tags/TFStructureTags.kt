package twilightforest.tags

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.levelgen.structure.Structure
import twilightforest.TFCommon.prefix

object TFStructureTags {
	// Add structures to this tag to show on the Magic Map, detected by worldgen features avoiding landmarks and progression lock behavior
	val LANDMARK = create("landmark")

	private fun create(tagName: String): TagKey<Structure> = TagKey.create(Registries.STRUCTURE, prefix(tagName))
}