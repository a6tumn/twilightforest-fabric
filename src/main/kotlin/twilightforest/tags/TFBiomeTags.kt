package twilightforest.tags

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome
import twilightforest.TFCommon.prefix

object TFBiomeTags {
	val IS_TWILIGHT = create("in_twilight_forest")

	val VALID_QUEST_GROVE_BIOMES = create("valid_quest_grove_biomes")

	val VALID_HOLLOW_TREE_BIOMES = create("valid_hollow_tree_biomes")
	val VALID_HEDGE_MAZE_BIOMES = create("valid_hedge_maze_biomes")
	val VALID_HOLLOW_HILL_BIOMES = create("valid_hollow_hill_biomes")
	val VALID_CAMP_BIOMES = create("valid_camp_biomes")
	val VALID_MUSHROOM_TOWER_BIOMES = create("valid_mushroom_tower_biomes")

	val VALID_NAGA_COURTYARD_BIOMES = create("valid_naga_courtyard_biomes")
	val VALID_LICH_TOWER_BIOMES = create("valid_lich_tower_biomes")
	val VALID_LABYRINTH_BIOMES = create("valid_labyrinth_biomes")
	val VALID_HYDRA_LAIR_BIOMES = create("valid_hydra_lair_biomes")
	val VALID_KNIGHT_STRONGHOLD_BIOMES = create("valid_knight_stronghold_biomes")
	val VALID_DARK_TOWER_BIOMES = create("valid_dark_tower_biomes")
	val VALID_YETI_CAVE_BIOMES = create("valid_yeti_cave_biomes")
	val VALID_AURORA_PALACE_BIOMES = create("valid_aurora_palace_biomes")
	val VALID_TROLL_CAVE_BIOMES = create("valid_troll_cave_biomes")
	val VALID_GIANT_HOUSE_BIOMES = create("valid_giant_house_biomes")
	val VALID_FINAL_CASTLE_BIOMES = create("valid_final_castle_biomes")

	private fun create(tagName: String) : TagKey<Biome> = TagKey.create(Registries.BIOME, prefix(tagName))
}