package twilightforest.tags

import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import twilightforest.TFCommon.prefix

object TFEntityTypeTags {
	val BOSSES = create("bosses")
	val LICH_POPPABLES = create("lich_poppables")
	val LIFEDRAIN_DROPS_NO_FLESH = create("lifedrain_drops_no_flesh")
	val RIDES_OBSTRUCT_SNATCHING = create("rides_obstruct_snatching")
	val DONT_KILL_BUGS = create("dont_kill_bugs")
	val SORTABLE_ENTITIES = create("sortable_entities")
	val MULTIPLAYER_INCLUSIVE_ENTITIES = create("multiplayer_inclusive_entities")
	val LICH_DEFLECTS_PHASE_2 = create("lich_deflects_phase_2")

	val AC_RESISTS_ACID = create("alexscaves", "resists_acid")
	val AC_RESISTS_MAGNETS = create("alexscaves", "resists_magnets")
	val AC_RESISTS_TREMORSAURUS_ROAR = create("alexscaves", "resists_tremorsaurus_roar")

	val AETHER_DEFLECTABLE_PROJECTILES = create("aether", "deflectable_projectiles")
	val AETHER_FIRE_MOB = create("aether", "fire_mob")
	val AETHER_PIGS = create("aether", "pigs")

	val AN_JAR_BLACKLIST = create("ars_nouveau", "jar_blacklist")
	val AN_JAR_RELEASE_BLACKLIST = create("ars_nouveau", "jar_release_blacklist")

	val IE_SHADER_BLACKLIST = create("immersiveengineering", "shaderbag/blacklist")

	private fun create(tagName: String) : TagKey<EntityType<*>> = TagKey.create(Registries.ENTITY_TYPE, prefix(tagName))
	private fun create(modid: String, tagName: String) : TagKey<EntityType<*>> = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(modid, tagName))
}