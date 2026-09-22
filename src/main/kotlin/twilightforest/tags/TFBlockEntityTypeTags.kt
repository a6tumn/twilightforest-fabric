package twilightforest.tags

import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.entity.BlockEntityType

object TFBlockEntityTypeTags {
	val RELOCATION_NOT_SUPPORTED = create("c", "relocation_not_supported")
	val IMMOVABLE = create("c", "immovable")

	private fun create(modid: String, tagName: String): TagKey<BlockEntityType<*>> = TagKey.create(Registries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(modid, tagName))
}