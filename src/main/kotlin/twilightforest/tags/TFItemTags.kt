package twilightforest.tags

import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import twilightforest.TFCommon.prefix

object TFItemTags {
	val TWILIGHT_OAK_LOGS = create("twilight_oak_logs")
	val CANOPY_LOGS = create("canopy_logs")
	val MANGROVE_LOGS = create("mangrove_logs")
	val DARKWOOD_LOGS = create("darkwood_logs")
	val TIME_LOGS = create("timewood_logs")
	val TRANSFORMATION_LOGS = create("transwood_logs")
	val MINING_LOGS = create("mining_logs")
	val SORTING_LOGS = create("sortwood_logs")
	val TWILIGHT_LOGS = create("logs")

	val BANISTERS = create("banisters")
	val DRYING_RACKS = create("drying_racks")

	val PAPER = makeCommonTag("paper")

	val TOWERWOOD = create("towerwood")

	val FIERY_VIAL = create("fiery_vial")

	val ARCTIC_FUR = create("arctic_fur")
	val CARMINITE_GEMS = makeCommonTag("gems/carminite")
	val FIERY_INGOTS = makeCommonTag("ingots/fiery")
	val IRONWOOD_INGOTS = makeCommonTag("ingots/ironwood")
	val KNIGHTMETAL_INGOTS = makeCommonTag("ingots/knightmetal")
	val MAZE_SLIME_BALLS = makeCommonTag("slime_balls/maze")
	val STEELEAF_INGOTS = makeCommonTag("ingots/steeleaf")
	val WROUGHT_IRON_INGOTS = makeCommonTag("ingots/wrought_iron")

	val STORAGE_BLOCKS_ARCTIC_FUR = makeCommonTag("storage_blocks/arctic_fur")
	val STORAGE_BLOCKS_CARMINITE = makeCommonTag("storage_blocks/carminite")
	val STORAGE_BLOCKS_FIERY = makeCommonTag("storage_blocks/fiery")
	val STORAGE_BLOCKS_IRONWOOD = makeCommonTag("storage_blocks/ironwood")
	val STORAGE_BLOCKS_KNIGHTMETAL = makeCommonTag("storage_blocks/knightmetal")
	val STORAGE_BLOCKS_MAZE_SLIME = makeCommonTag("storage_blocks/slime/maze")
	val STORAGE_BLOCKS_STEELEAF = makeCommonTag("storage_blocks/steeleaf")

	val RAW_MATERIALS_IRONWOOD = makeCommonTag("raw_materials/ironwood")
	val RAW_MATERIALS_KNIGHTMETAL = makeCommonTag("raw_materials/knightmetal")

	val PORTAL_ACTIVATOR = create("portal/activator")

	val WIP = create("wip")

	val KOBOLD_PACIFICATION_BREADS = create("kobold_pacification_breads")
	val BOAR_TEMPT_ITEMS = create("boar_tempt_items")
	val DEER_TEMPT_ITEMS = create("deer_tempt_items")
	val DWARF_RABBIT_TEMPT_ITEMS = create("dwarf_rabbit_tempt_items")
	val PENGUIN_TEMPT_ITEMS = create("penguin_tempt_items")
	val RAVEN_TEMPT_ITEMS = create("raven_tempt_items")
	val SQUIRREL_TEMPT_ITEMS = create("squirrel_tempt_items")
	val TINY_BIRD_TEMPT_ITEMS = create("tiny_bird_tempt_items")

	val BANNED_UNCRAFTING_INGREDIENTS = create("banned_uncrafting_ingredients")
	val BANNED_UNCRAFTABLES = create("banned_uncraftables")
	val UNCRAFTING_IGNORES_COST = create("uncrafting_ignores_cost")

	val KEPT_ON_DEATH = create("kept_on_death")
	val BLOCK_AND_CHAIN_ENCHANTABLE = create("enchantable/block_and_chain")

	val TRAVELLERS_BELT_BLACKLISTED = create("travellers_belt_blacklisted")
	val TRAVELLERS_AGILE_RANGER_WHITELISTED = create("travellers_agile_ranger_whitelisted")
	val TRAVELLERS_AGILE_RANGER_BLACKLISTED = create("travellers_agile_ranger_blacklisted")

	val REPAIRS_IRONWOOD_TOOLS = create("repairs_ironwood_tools")
	val REPAIRS_STEELEAF_TOOLS = create("repairs_steeleaf_tools")
	val REPAIRS_KNIGHTMETAL_TOOLS = create("repairs_knightmetal_tools")
	val REPAIRS_FIERY_TOOLS = create("repairs_fiery_tools")
	val REPAIRS_GIANT_TOOLS = create("repairs_giant_tools")
	val REPAIRS_ICE_TOOLS = create("repairs_ice_tools")
	val REPAIRS_GLASS_TOOLS = create("repairs_glass_tools")

	val REPAIRS_IRONWOOD_ARMOR = create("repairs_ironwood_armor")
	val REPAIRS_STEELEAF_ARMOR = create("repairs_steeleaf_armor")
	val REPAIRS_NAGA_ARMOR = create("repairs_naga_armor")
	val REPAIRS_FIERY_ARMOR = create("repairs_fiery_armor")
	val REPAIRS_KNIGHTMETAL_ARMOR = create("repairs_knightmetal_armor")
	val REPAIRS_PHANTOM_ARMOR = create("repairs_phantom_armor")
	val REPAIRS_ARCTIC_ARMOR = create("repairs_arctic_armor")
	val REPAIRS_YETI_ARMOR = create("repairs_yeti_armor")
	val REPAIRS_TRAVELLERS_GEAR = create("repairs_travellers_gear")

	val SCEPTERS = create("scepters")
	val IMMUNE_TO_THORNS = create("immune_to_thorns")

	val FOODS_JERKY = makeCommonTag("foods/jerky")
	val RENDER_LOWER_ON_DRYING_RACK = create("lower_on_drying_rack")
	val TROPHIES = create("trophies")
	val EMPERORS_CLOTH_APPLICABLE = create("emperors_cloth_applicable")

	val AC_FERNS = createFromNamespace("alexscaves", "ferns")
	val AC_FERROMAGNETIC_ITEMS = createFromNamespace("alexscaves", "ferromagnetic_items")
	val AC_RAW_MEATS = createFromNamespace("alexscaves", "raw_meats")

	val CURIOS_CHARM = createFromNamespace("curios", "charm")
	val CURIOS_HEAD = createFromNamespace("curios", "head")

	val CA_PLANTS = createFromNamespace("createaddition", "plants")
	val CA_PLANT_FOODS = createFromNamespace("createaddition", "plant_foods")

	val FA_MODIFIER_ETERNAL_INCOMPATIBLE = createFromNamespace("forbidden_arcanus", "modifier/eternal_incompatible")

	val FD_CABBAGE_ROLL_INGREDIENTS = createFromNamespace("farmersdelight", "cabbage_roll_ingredients")

	val RANDOMIUM_BLACKLIST = createFromNamespace("randomium", "blacklist")

	fun makeCommonTag(tagName: String) : TagKey<Item> = createFromNamespace("c", tagName)

	fun create(name: String) : TagKey<Item> = TagKey.create(Registries.ITEM, prefix(name))

	private fun createFromNamespace(modid: String, tagName: String) : TagKey<Item> = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(modid, tagName))
}