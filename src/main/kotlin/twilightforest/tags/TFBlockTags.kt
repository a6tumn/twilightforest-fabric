package twilightforest.tags

import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import twilightforest.TFCommon.prefix

object TFBlockTags {
	val MAZESTONE = create("mazestone")
	val TOWERWOOD = create("towerwood")
	val CLOUDS = create("clouds")
	val DEADROCK = create("deadrock")
	val CASTLE_BLOCKS = create("castle_blocks")

	val TF_LOGS = create("logs")
	val TWILIGHT_OAK_LOGS = create("twilight_oak_logs")
	val CANOPY_LOGS = create("canopy_logs")
	val MANGROVE_LOGS = create("mangrove_logs")
	val DARKWOOD_LOGS = create("darkwood_logs")
	val TIME_LOGS = create("timewood_logs")
	val TRANSFORMATION_LOGS = create("transwood_logs")
	val MINING_LOGS = create("mining_logs")
	val SORTING_LOGS = create("sortwood_logs")

	val HOLLOW_LOGS = create("hollow_logs")
	val HOLLOW_LOGS_HORIZONTAL = create("hollow_logs_horizontal")
	val HOLLOW_LOGS_VERTICAL = create("hollow_logs_vertical")
	val HOLLOW_LOGS_CLIMBABLE = create("hollow_logs_climbable")

	val BANISTERS = create("banisters")
	val TF_CHESTS = create("chests")

	val PORTAL_EDGE = create("portal/edge")
	val PORTAL_POOL = create("portal/fluid")
	val PORTAL_DECO = create("portal/decoration")
	val GENERATED_PORTAL_DECO = create("portal/generated_decoration")

	val DARK_TOWER_ALLOWED_POTS = create("dark_tower_allowed_pots")
	val TROPHY_PEDESTAL_ACTIVATION_BLOCKS = create("trophy_pedestal_activation_blocks")
	val FIRE_JET_FUEL = create("fire_jet_fuel")
	val ICE_BOMB_REPLACEABLES = create("ice_bomb_replaceables")
	val MAZEBREAKER_ACCELERATED = create("mazebreaker_accelerated_mining")

	val COMMON_PROTECTIONS = create("common_protections")
	val ANNIHILATION_INCLUSIONS = create("annihilation_inclusions")
	val ANTIBUILDER_IGNORES = create("antibuilder_ignores")
	val CARMINITE_REACTOR_IMMUNE = create("carminite_reactor_immune")
	val CARMINITE_REACTOR_ORES = create("carminite_reactor_ores")
	val STRUCTURE_BANNED_INTERACTIONS = create("structure_banned_interactions")
	val PROGRESSION_ALLOW_BREAKING = create("progression_allow_breaking")
	val CANNOT_TROLL_CAVE_HOLLOW = create("cannot_troll_cave_hollow")

	val WORLDGEN_REPLACEABLES = create("worldgen_replaceables")
	val ROOT_TRACE_SKIP = create("tree_roots_skip")
	val SUPPORTS_STALAGMITES = create("supports_stalagmites")
	val CARVER_REPLACEABLES = create("carver_replaceables")
	val PLANTS_HANG_ON = create("plants_hang_on")
	val OREBERRY_BUSHES_SURVIVE = create("oreberry_bushes_survive")
	val TF_BERRY_BUSHES_SURVIVE = create("tf_berry_bushes_survive")
	val TF_BERRY_BUSHES_REPLACE = create("tf_berry_bushes_replace")
	val DARK_TOWER_BERRY_BUSHES_SURVIVE = create("dark_tower_berry_bushes_survive")
	val DARK_TOWER_BERRY_BUSHES_DIE = create("dark_tower_berry_bushes_die")
	val HUGE_MUSHGLOOM_PLACEABLE = create("huge_mushgloom_placeable")

	val ORE_MAGNET_SAFE_REPLACE_BLOCK = create("ore_magnet/ore_safe_replace_block")
	val ORE_MAGNET_IGNORE = create("ore_magnet/ignored_ores")

	val ROOT_GROUND = makeCommonTag("ore_bearing_ground/root")
	val ROOT_ORES = makeCommonTag("ores_in_ground/root")

	val TIME_CORE_EXCLUDED = create("time_core_excluded")
	val MINING_CORE_EXCLUDED = create("mining_tree_excluded")
	val ORE_METER_TARGETABLE = create("ore_meter_targetable")

	val PENGUINS_SPAWNABLE_ON = create("penguins_spawnable_on")
	val GIANTS_SPAWNABLE_ON = create("giants_spawnable_on")
	val DRUID_PROJECTILE_REPLACEABLE = create("druid_projectile_replaceable")

	val STORAGE_BLOCKS_ARCTIC_FUR = makeCommonTag("storage_blocks/arctic_fur")
	val STORAGE_BLOCKS_CARMINITE = makeCommonTag("storage_blocks/carminite")
	val STORAGE_BLOCKS_FIERY = makeCommonTag("storage_blocks/fiery")
	val STORAGE_BLOCKS_IRONWOOD = makeCommonTag("storage_blocks/ironwood")
	val STORAGE_BLOCKS_KNIGHTMETAL = makeCommonTag("storage_blocks/knightmetal")
	val STORAGE_BLOCKS_MAZE_SLIME = makeCommonTag("storage_blocks/slime/maze")
	val STORAGE_BLOCKS_STEELEAF = makeCommonTag("storage_blocks/steeleaf")

	val INCORRECT_FOR_IRONWOOD_TOOL = create("incorrect_for_ironwood_tool")
	val INCORRECT_FOR_FIERY_TOOL = create("incorrect_for_fiery_tool")
	val INCORRECT_FOR_STEELEAF_TOOL = create("incorrect_for_steeleaf_tool")
	val INCORRECT_FOR_KNIGHTMETAL_TOOL = create("incorrect_for_knightmetal_tool")
	val INCORRECT_FOR_GIANT_TOOL = create("incorrect_for_giant_tool")
	val INCORRECT_FOR_ICE_TOOL = create("incorrect_for_ice_tool")
	val INCORRECT_FOR_GLASS_TOOL = create("incorrect_for_glass_tool")

	val MINEABLE_WITH_BLOCK_AND_CHAIN = create("mineable_with_block_and_chain")
	val BLOCK_AND_CHAIN_NEVER_BREAKS = create("block_and_chain_never_breaks")

	val SMALL_LAKES_DONT_REPLACE = create("small_lakes_dont_replace")
	val DRYING_RACKS = create("drying_racks")

	val AC_FERROMAGNETIC_BLOCKS = createFromNamespace("alexscaves", "ferromagnetic_blocks")
	val AC_GLOOMOTH_LIGHT_SOURCES = createFromNamespace("alexscaves", "gloomoth_light_sources")
	val AC_UNDERZEALOT_LIGHT_SOURCES = createFromNamespace("alexscaves", "underzealot_light_sources")

	val ARTIFACTS_CAMPSITE_CHESTS = createFromNamespace("artifacts", "campsite_chests")

	val CHEST_MOUNTED_STORAGE = createFromNamespace("create", "chest_mounted_storage")
	val PASSIVE_BOILER_HEATERS = createFromNamespace("create", "passive_boiler_heaters")
	val TREE_ATTACHMENTS = createFromNamespace("create", "tree_attachments")

	val FD_COMPOST_ACTIVATORS = createFromNamespace("farmersdelight", "compost_activators")
	val FD_HEAT_SOURCES = createFromNamespace("farmersdelight", "heat_sources")

	fun makeCommonTag(tagName: String) : TagKey<Block> = createFromNamespace("c", tagName)

	fun create(name: String) : TagKey<Block> = TagKey.create(Registries.BLOCK, prefix(name))

	private fun createFromNamespace(modid: String, tagName: String) : TagKey<Block> = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(modid, tagName))
}