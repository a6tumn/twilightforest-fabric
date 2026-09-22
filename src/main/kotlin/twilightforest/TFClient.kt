package twilightforest

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.model.loading.v1.CustomUnbakedBlockStateModel
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry
import net.fabricmc.fabric.api.client.rendering.v1.*
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback.RegistrationHelper
import net.fabricmc.fabric.api.resource.v1.ResourceLoader
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.model.animal.pig.PigModel
import net.minecraft.client.model.animal.sheep.SheepFurModel
import net.minecraft.client.model.animal.wolf.AdultWolfModel
import net.minecraft.client.model.geom.LayerDefinitions
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.monster.silverfish.SilverfishModel
import net.minecraft.client.model.monster.slime.SlimeModel
import net.minecraft.client.model.monster.spider.SpiderModel
import net.minecraft.client.particle.FlameParticle
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.client.renderer.entity.*
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.client.renderer.entity.state.EntityRenderState
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import net.minecraft.client.renderer.item.ItemModels
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties
import net.minecraft.client.renderer.special.SpecialModelRenderers
import net.minecraft.client.resources.model.sprite.AtlasManager
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.PackType
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes
import twilightforest.client.*
import twilightforest.client.listeners.*
import twilightforest.client.model.TFModelLayers
import twilightforest.client.model.TFModelLoadingPlugin
import twilightforest.client.model.armor.*
import twilightforest.client.model.block.BrazierModel
import twilightforest.client.model.block.aurorablock.UnbakedNoiseVaryingBlockStateModel
import twilightforest.client.model.block.connected.UnbakedConnectedTextureModel
import twilightforest.client.model.block.forcefield.UnbakedForceFieldBlockStateModel
import twilightforest.client.model.block.giantblock.UnbakedGiantBlockStateModel
import twilightforest.client.model.block.patch.UnbakedPlantPatchBlockStateModel
import twilightforest.client.model.entity.*
import twilightforest.client.model.item.AnimatedItemModel
import twilightforest.client.model.item.TravellersGearItemModel
import twilightforest.client.model.item.TrollsteinnItemModel
import twilightforest.client.particle.*
import twilightforest.client.properties.*
import twilightforest.client.renderer.TFRenderPipelines
import twilightforest.client.renderer.TFSkyRenderer
import twilightforest.client.renderer.TFWeatherRenderer
import twilightforest.client.renderer.armor.TFArmorRenderer
import twilightforest.client.renderer.armor.TFSimpleArmorRenderer
import twilightforest.client.renderer.armor.TravellersArmorRenderer
import twilightforest.client.renderer.block.*
import twilightforest.client.renderer.entity.*
import twilightforest.client.renderer.entity.layers.IceLayer
import twilightforest.client.renderer.entity.layers.ShieldLayer
import twilightforest.client.renderer.gui.GuiBlockRenderer
import twilightforest.client.renderer.map.ConqueredMapIconRenderer
import twilightforest.client.renderer.map.MagicMapPlayerIconRenderer
import twilightforest.client.renderer.map.MapDecorationManager
import twilightforest.client.renderer.special.*
import twilightforest.client.renderer.tooltip.ItemDisplayTooltipComponent
import twilightforest.client.renderer.tooltip.PotionFlaskTooltipComponent
import twilightforest.client.renderer.tooltip.TravellersBeltTooltipComponent
import twilightforest.client.state.entity.TFGhastRenderState
import twilightforest.init.*
import twilightforest.item.PotionFlaskItem
import twilightforest.item.mapdata.MapDataManager
import twilightforest.item.travellers_gear.TravellersArmorBeltItem
import twilightforest.item.travellers_gear.TravellersGogglesItem
import twilightforest.network.*

object TFClient : ClientModInitializer {
	override fun onInitializeClient() {
		TFTintSources.init()

		ModelLoadingPlugin.register(TFModelLoadingPlugin())

		setupClientEvents()

		registerPackets()
		registerRenderPipelines()
		registerTooltips()
		registerBlockStateModels()
		registerItemModels()
		registerConditionalProperties()
		registerRangeProperties()
		registerSelectProperties()
		registerSpecialModelRenderers()
		registerAtlases()
		registerClientReloadListeners()
		registerScreens()
		registerEntityRenderers()
		registerBlockEntityRenderers()
		registerLayerDefinitions()
		registerParticleFactories()
		registerMapDecorators()
		registerMapDecorationTypes()
		registerRenderLayers()
		registerArmorRenderers()
		registerPictureInPictureRenderers()
	}

	private fun setupClientEvents() {
		TFSkyRenderer.init()
		TFWeatherRenderer.init()
		MapDataManager.init()
		MultipartRenderDispatcher.init()
		FoliageColorHandler.init()
		FogEventListeners.init()
		LockedBiomeToastEventListeners.init()
		CloudEventListeners.init()
		OverlayEventListeners.init()
		TravellersClientEventListeners.init()
		ClientGameEventListeners.init()
	}

	private fun registerPackets() {
		ClientPlayNetworking.registerGlobalReceiver(AreaProtectionPacket.TYPE, AreaProtectionPacket::handle)
		ClientPlayNetworking.registerGlobalReceiver(CreateMovingCicadaSoundPacket.TYPE, CreateMovingCicadaSoundPacket::handle)
		ClientPlayNetworking.registerGlobalReceiver(EnforceProgressionStatusPacket.TYPE, EnforceProgressionStatusPacket::handle)
		ClientPlayNetworking.registerGlobalReceiver(MagicMapPacket.TYPE, MagicMapPacket::handle)
		ClientPlayNetworking.registerGlobalReceiver(MazeMapPacket.TYPE, MazeMapPacket::handle)
		ClientPlayNetworking.registerGlobalReceiver(MissingAdvancementToastPacket.TYPE, MissingAdvancementToastPacket::handle)
		ClientPlayNetworking.registerGlobalReceiver(MovePlayerPacket.TYPE, MovePlayerPacket::handle)
		ClientPlayNetworking.registerGlobalReceiver(ParticlePacket.TYPE, ParticlePacket::handle)
		ClientPlayNetworking.registerGlobalReceiver(GogglesZoomPacket.TYPE, GogglesZoomPacket::handleClient)
		ClientPlayNetworking.registerGlobalReceiver(GradualGlidePacket.TYPE, GradualGlidePacket::handleClient)
		ClientPlayNetworking.registerGlobalReceiver(SpawnCharmPacket.TYPE, SpawnCharmPacket::handle)
		ClientPlayNetworking.registerGlobalReceiver(SpawnFallenLeafFromPacket.TYPE, SpawnFallenLeafFromPacket::handle)
		ClientPlayNetworking.registerGlobalReceiver(StructureProtectionPacket.TYPE, StructureProtectionPacket::handle)
		ClientPlayNetworking.registerGlobalReceiver(SyncUncraftingTableConfigPacket.TYPE, SyncUncraftingTableConfigPacket::handle)
		ClientPlayNetworking.registerGlobalReceiver(UpdateTFMultipartPacket.TYPE, UpdateTFMultipartPacket::handle)
		ClientPlayNetworking.registerGlobalReceiver(UpdateThrownPacket.TYPE, UpdateThrownPacket::handle)
		ClientPlayNetworking.registerGlobalReceiver(LifedrainParticlePacket.TYPE, LifedrainParticlePacket::handle)
		ClientPlayNetworking.registerGlobalReceiver(UpdateDeathTimePacket.TYPE, UpdateDeathTimePacket::handle)
		ClientPlayNetworking.registerGlobalReceiver(TFBossBarPacket.AddTFBossBarPacket.TYPE, TFBossBarPacket.AddTFBossBarPacket::handle)
		ClientPlayNetworking.registerGlobalReceiver(TFBossBarPacket.UpdateTFBossBarStylePacket.TYPE, TFBossBarPacket.UpdateTFBossBarStylePacket::handle)
		ClientPlayNetworking.registerGlobalReceiver(SetMasonJarItemPacket.TYPE, SetMasonJarItemPacket::handle)
		ClientPlayNetworking.registerGlobalReceiver(SyncQuestsPacket.TYPE, SyncQuestsPacket::handle)
		ClientPlayNetworking.registerGlobalReceiver(TravellersWingsStatePacket.TYPE, TravellersWingsStatePacket::handle)
	}

	private fun registerRenderPipelines() {
		RenderPipelines.register(TFRenderPipelines.AURORA)
	}

	private fun registerTooltips() {
		ClientTooltipComponentCallback.EVENT.register { component ->
			when (component) {
				is PotionFlaskItem.Tooltip -> PotionFlaskTooltipComponent(component)
				is TravellersArmorBeltItem.Tooltip -> TravellersBeltTooltipComponent(component)
				is TravellersGogglesItem.Tooltip -> ItemDisplayTooltipComponent(component)
				else -> null
			}
		}
	}

	private fun registerBlockStateModels() {
		CustomUnbakedBlockStateModel.register(TFCommon.prefix("connected_texture_block"), UnbakedConnectedTextureModel.MAP_CODEC)
		CustomUnbakedBlockStateModel.register(TFCommon.prefix("force_field"), UnbakedForceFieldBlockStateModel.MAP_CODEC)
		CustomUnbakedBlockStateModel.register(TFCommon.prefix("giant_block"), UnbakedGiantBlockStateModel.MAP_CODEC)
		CustomUnbakedBlockStateModel.register(TFCommon.prefix("noise_varying"), UnbakedNoiseVaryingBlockStateModel.MAP_CODEC)
		CustomUnbakedBlockStateModel.register(TFCommon.prefix("plant_patch"), UnbakedPlantPatchBlockStateModel.MAP_CODEC)
	}

	private fun registerItemModels() {
		ItemModels.ID_MAPPER.put(TFCommon.prefix("travellers_gear"), TravellersGearItemModel.Unbaked.MAP_CODEC)
		ItemModels.ID_MAPPER.put(TFCommon.prefix("trollsteinn"), TrollsteinnItemModel.Unbaked.MAP_CODEC)
		ItemModels.ID_MAPPER.put(TFCommon.prefix("animated_item_model"), AnimatedItemModel.Unbaked.MAP_CODEC)
	}

	private fun registerConditionalProperties() {
		ConditionalItemModelProperties.ID_MAPPER.put(TFCommon.prefix("moonworm_queen_pulse"), MoonwormQueenPulse.TYPE)
		ConditionalItemModelProperties.ID_MAPPER.put(TFCommon.prefix("ore_meter_flash"), OreMeterFlash.TYPE)
	}

	private fun registerRangeProperties() {
		RangeSelectItemModelProperties.ID_MAPPER.put(TFCommon.prefix("potion_flask_dosage"), PotionFlaskDosage.TYPE)
		RangeSelectItemModelProperties.ID_MAPPER.put(TFCommon.prefix("potion_flask_damage"), PotionFlaskDamage.TYPE)
		RangeSelectItemModelProperties.ID_MAPPER.put(TFCommon.prefix("moon_dial_phase"), MoonDialPhaseProperty.MAP_CODEC)
	}

	private fun registerSelectProperties() {
		SelectItemModelProperties.ID_MAPPER.put(TFCommon.prefix("experiment_115_variant"), Experiment115Type.TYPE)
	}

	private fun registerSpecialModelRenderers() {
		SpecialModelRenderers.ID_MAPPER.put(TFCommon.prefix("candelabra"), CandelabraSpecialRenderer.Unbaked.MAP_CODEC)
		SpecialModelRenderers.ID_MAPPER.put(TFCommon.prefix("cicada"), CicadaSpecialRenderer.Unbaked.MAP_CODEC)
		SpecialModelRenderers.ID_MAPPER.put(TFCommon.prefix("firefly"), FireflySpecialRenderer.Unbaked.MAP_CODEC)
		SpecialModelRenderers.ID_MAPPER.put(TFCommon.prefix("keepsake_casket"), KeepsakeCasketSpecialRenderer.Unbaked.MAP_CODEC)
		SpecialModelRenderers.ID_MAPPER.put(TFCommon.prefix("knightmetal_shield"), KnightmetalShieldSpecialRenderer.Unbaked.MAP_CODEC)
		SpecialModelRenderers.ID_MAPPER.put(TFCommon.prefix("mason_jar"), MasonJarSpecialRenderer.Unbaked.MAP_CODEC)
		SpecialModelRenderers.ID_MAPPER.put(TFCommon.prefix("moonworm"), MoonwormSpecialRenderer.Unbaked.MAP_CODEC)
		SpecialModelRenderers.ID_MAPPER.put(TFCommon.prefix("mystic_crown"), MysticCrownSpecialRenderer.Unbaked.MAP_CODEC)
		SpecialModelRenderers.ID_MAPPER.put(TFCommon.prefix("skull_candle"), SkullCandleSpecialRenderer.Unbaked.MAP_CODEC)
		SpecialModelRenderers.ID_MAPPER.put(TFCommon.prefix("skull_chest"), SkullChestSpecialRenderer.Unbaked.MAP_CODEC)
		SpecialModelRenderers.ID_MAPPER.put(TFCommon.prefix("trophy"), TrophySpecialRenderer.Unbaked.MAP_CODEC)
	}

	private fun registerAtlases() {
		AtlasRegistry.register(AtlasManager.AtlasConfig(MagicPaintingAtlasInfo.ATLAS_LOCATION, MagicPaintingAtlasInfo.ATLAS_INFO_LOCATION, false))
	}

	private fun registerClientReloadListeners() {
		ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(TFCommon.prefix("texture_generator"), TextureGeneratorReloadListener.INSTANCE)
		ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(TFCommon.prefix("armor_cache"), TFArmorRenderer.ResourceReloadListener())
	}

	private fun registerScreens() {
		MenuScreens.register(TFMenuTypes.UNCRAFTING, ::UncraftingScreen)
	}

	private fun registerEntityRenderers() {
		EntityRenderers.register(TFEntities.BOAR, ::BoarRenderer)
		EntityRenderers.register(TFEntities.BIGHORN_SHEEP, ::BighornRenderer)
		EntityRenderers.register(TFEntities.DEER, ::DeerRenderer)
		EntityRenderers.register(TFEntities.REDCAP, ::RedcapRenderer)
		EntityRenderers.register(TFEntities.SKELETON_DRUID, ::SkeletonDruidRenderer)
		EntityRenderers.register(TFEntities.HOSTILE_WOLF, ::HostileWolfRenderer)
		EntityRenderers.register(TFEntities.WRAITH, ::WraithRenderer)
		EntityRenderers.register(TFEntities.HYDRA, ::HydraRenderer)
		EntityRenderers.register(TFEntities.LICH, ::LichRenderer)
		EntityRenderers.register(TFEntities.PENGUIN) { m -> BirdRenderer(m, PenguinModel(m.bakeLayer(TFModelLayers.PENGUIN)), PenguinModel(m.bakeLayer(TFModelLayers.PENGUIN_BABY)), 0.375F, "penguin.png") }
		EntityRenderers.register(TFEntities.LICH_MINION, ::LichMinionRenderer)
		EntityRenderers.register(TFEntities.LOYAL_ZOMBIE, ::LoyalZombieRenderer)
		EntityRenderers.register(TFEntities.TINY_BIRD, ::TinyBirdRenderer)
		EntityRenderers.register(TFEntities.SQUIRREL, ::SquirrelRenderer)
		EntityRenderers.register(TFEntities.DWARF_RABBIT, ::BunnyRenderer)
		EntityRenderers.register(TFEntities.RAVEN) { m -> BirdRenderer(m, RavenModel(m.bakeLayer(TFModelLayers.RAVEN)), 0.3F, "raven.png") }
		EntityRenderers.register(TFEntities.QUEST_RAM, ::QuestRamRenderer)
		EntityRenderers.register(TFEntities.KOBOLD, ::KoboldRenderer)
		EntityRenderers.register(TFEntities.MOSQUITO_SWARM, ::MosquitoSwarmRenderer)
		EntityRenderers.register(TFEntities.DEATH_TOME, ::DeathTomeRenderer)
		EntityRenderers.register(TFEntities.MINOTAUR, ::MinotaurRenderer)
		EntityRenderers.register(TFEntities.MINOSHROOM, ::MinoshroomRenderer)
		EntityRenderers.register(TFEntities.FIRE_BEETLE, ::FireBeetleRenderer)
		EntityRenderers.register(TFEntities.SLIME_BEETLE, ::SlimeBeetleRenderer)
		EntityRenderers.register(TFEntities.PINCH_BEETLE, ::PinchBeetleRenderer)
		EntityRenderers.register(TFEntities.MIST_WOLF, ::MistWolfRenderer)
		EntityRenderers.register(TFEntities.CARMINITE_GHASTLING) { m -> TFGhastRenderer(m, TFGhastModel(m.bakeLayer(TFModelLayers.CARMINITE_GHASTLING)), 0.625F) }
		EntityRenderers.register(TFEntities.CARMINITE_GOLEM, ::CarminiteGolemRenderer)
		EntityRenderers.register(TFEntities.TOWERWOOD_BORER, ::TowerwoodBorerRenderer)
		EntityRenderers.register(TFEntities.CARMINITE_GHASTGUARD, ::CarminiteGhastRenderer)
		EntityRenderers.register(TFEntities.UR_GHAST, ::UrGhastRenderer)
		EntityRenderers.register(TFEntities.BLOCKCHAIN_GOBLIN, ::BlockChainGoblinRenderer)
		EntityRenderers.register(TFEntities.UPPER_GOBLIN_KNIGHT, ::UpperGoblinKnightRenderer)
		EntityRenderers.register(TFEntities.LOWER_GOBLIN_KNIGHT, ::LowerGoblinKnightRenderer)
		EntityRenderers.register(TFEntities.HELMET_CRAB, ::HelmetCrabRenderer)
		EntityRenderers.register(TFEntities.KNIGHT_PHANTOM, ::KnightPhantomRenderer)
		EntityRenderers.register(TFEntities.NAGA, ::NagaRenderer)
		EntityRenderers.register(TFEntities.SWARM_SPIDER) { m -> TFSpiderRenderer(m, 0.25F, "swarmspider.png", 0.5F) }
		EntityRenderers.register(TFEntities.KING_SPIDER) { m -> TFSpiderRenderer(m, 1.25F, "kingspider.png", 1.9F) }
		EntityRenderers.register(TFEntities.CARMINITE_BROODLING) { m -> TFSpiderRenderer(m, 0.6F, "towerbroodling.png", 0.7F) }
		EntityRenderers.register(TFEntities.HEDGE_SPIDER) { m -> TFSpiderRenderer(m, 0.8F, "hedgespider.png", 1.0F) }
		EntityRenderers.register(TFEntities.REDCAP_SAPPER, ::RedcapSapperRenderer)
		EntityRenderers.register(TFEntities.MAZE_SLIME, ::MazeSlimeRenderer)
		EntityRenderers.register(TFEntities.YETI, ::YetiRenderer)
		EntityRenderers.register(TFEntities.PROTECTION_BOX, ::ProtectionBoxRenderer)
		EntityRenderers.register(TFEntities.MAGIC_PAINTING, ::MagicPaintingRenderer)
		EntityRenderers.register(TFEntities.ALPHA_YETI, ::AlphaYetiRenderer)
		EntityRenderers.register(TFEntities.WINTER_WOLF, ::WinterWolfRenderer)
		EntityRenderers.register(TFEntities.SNOW_GUARDIAN, ::SnowGuardianRenderer)
		EntityRenderers.register(TFEntities.STABLE_ICE_CORE, ::StableIceCoreRenderer)
		EntityRenderers.register(TFEntities.UNSTABLE_ICE_CORE, ::UnstableIceCoreRenderer)
		EntityRenderers.register(TFEntities.SNOW_QUEEN, ::SnowQueenRenderer)
		EntityRenderers.register(TFEntities.TROLL, ::TrollRenderer)
		EntityRenderers.register(TFEntities.GIANT_MINER, ::TFGiantRenderer)
		EntityRenderers.register(TFEntities.ARMORED_GIANT, ::TFGiantRenderer)
		EntityRenderers.register(TFEntities.ICE_CRYSTAL, ::IceCrystalRenderer)
		EntityRenderers.register(TFEntities.CHAIN_BLOCK, ::BlockChainRenderer)
		EntityRenderers.register(TFEntities.CUBE_OF_ANNIHILATION, ::CubeOfAnnihilationRenderer)
		EntityRenderers.register(TFEntities.HARBINGER_CUBE, ::HarbingerCubeRenderer)
		EntityRenderers.register(TFEntities.ADHERENT, ::AdherentRenderer)
		EntityRenderers.register(TFEntities.ROVING_CUBE, ::RovingCubeRenderer)
		EntityRenderers.register(TFEntities.RISING_ZOMBIE, ::RisingZombieRenderer)
		EntityRenderers.register(TFEntities.PLATEAU_BOSS, ::NoopRenderer)
		EntityRenderers.register(TFEntities.NATURE_BOLT, ::ThrownItemRenderer)
		EntityRenderers.register(TFEntities.LICH_BOLT) { c -> CustomProjectileTextureRenderer(c, TFCommon.prefix("textures/particle/twilight_orb.png"), 1.0F, true, false) }
		EntityRenderers.register(TFEntities.WAND_BOLT) { c -> CustomProjectileTextureRenderer(c, TFCommon.prefix("textures/particle/twilight_orb.png"), 1.0F, true, false) }
		EntityRenderers.register(TFEntities.LICH_BOMB) { c -> CustomProjectileTextureRenderer(c, Identifier.withDefaultNamespace("textures/item/magma_cream.png"), 1.0F, true, true) }
		EntityRenderers.register(TFEntities.TOME_BOLT, ::ThrownItemRenderer)
		EntityRenderers.register(TFEntities.HYDRA_MORTAR, ::HydraMortarRenderer)
		EntityRenderers.register(TFEntities.SLIME_BLOB, ::ThrownItemRenderer)
		EntityRenderers.register(TFEntities.MOONWORM_SHOT, ::MoonwormShotRenderer)
		EntityRenderers.register(TFEntities.CHARM_EFFECT, ::ThrownItemRenderer)
		EntityRenderers.register(TFEntities.THROWN_WEP, ::ThrownWepRenderer)
		EntityRenderers.register(TFEntities.FALLING_ICE, ::FallingIceRenderer)
		EntityRenderers.register(TFEntities.THROWN_ICE, ::ThrownIceRenderer)
		EntityRenderers.register(TFEntities.THROWN_BLOCK, ::ThrownBlockRenderer)
		EntityRenderers.register(TFEntities.ICE_SNOWBALL, ::ThrownItemRenderer)
		EntityRenderers.register(TFEntities.SLIDER, ::SlideBlockRenderer)
		EntityRenderers.register(TFEntities.SEEKER_ARROW, ::DefaultArrowRenderer)
		EntityRenderers.register(TFEntities.ICE_ARROW, ::DefaultArrowRenderer)
	}

	private fun registerBlockEntityRenderers() {
		BlockEntityRenderers.register(TFBlockEntities.FIREFLY, ::FireflyRenderer)
		BlockEntityRenderers.register(TFBlockEntities.CICADA, ::CicadaRenderer)
		BlockEntityRenderers.register(TFBlockEntities.MOONWORM, ::MoonwormRenderer)
		BlockEntityRenderers.register(TFBlockEntities.TROPHY, ::TrophyRenderer)
		BlockEntityRenderers.register(TFBlockEntities.TF_CHEST, ::TFChestRenderer)
		BlockEntityRenderers.register(TFBlockEntities.TF_TRAPPED_CHEST, ::TFChestRenderer)
		BlockEntityRenderers.register(TFBlockEntities.SKULL_CHEST, ::SkullChestRenderer)
		BlockEntityRenderers.register(TFBlockEntities.KEEPSAKE_CASKET, ::KeepsakeCasketRenderer)
		BlockEntityRenderers.register(TFBlockEntities.SKULL_CANDLE, ::SkullCandleRenderer)
		BlockEntityRenderers.register(TFBlockEntities.REACTOR_DEBRIS, ::ReactorDebrisRenderer)
		BlockEntityRenderers.register(TFBlockEntities.RED_THREAD, ::RedThreadRenderer)
		BlockEntityRenderers.register(TFBlockEntities.CANDELABRA, ::CandelabraRenderer)
		BlockEntityRenderers.register(TFBlockEntities.JAR, ::JarRenderer)
		BlockEntityRenderers.register(TFBlockEntities.MASON_JAR) { context -> JarRenderer.MasonJarRenderer(context) }
		BlockEntityRenderers.register(TFBlockEntities.OMINOUS_CANDLE, ::OminousCandleRenderer)
		BlockEntityRenderers.register(TFBlockEntities.SINISTER_SPAWNER, ::SinisterSpawnerRenderer)
		BlockEntityRenderers.register(TFBlockEntities.BRAZIER, ::BrazierRenderer)
		BlockEntityRenderers.register(TFBlockEntities.DRYING_RACK, ::DryingRackRenderer)
	}

	private fun registerLayerDefinitions() {
		ModelLayerRegistry.registerModelLayer(TFModelLayers.ARCTIC_ARMOR_INNER) { LayerDefinition.create(ArcticArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.ARCTIC_ARMOR_OUTER) { LayerDefinition.create(ArcticArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.FIERY_ARMOR_INNER) { LayerDefinition.create(FieryArmorModel.createMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0.0F), 64, 32) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.FIERY_ARMOR_OUTER) { LayerDefinition.create(FieryArmorModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0.0F), 64, 32) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TRAVELLERS_ARMOR_HELMET) { LayerDefinition.create(TravellersGearModels.addGogglePieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES) { LayerDefinition.create(TravellersGearModels.addGlovePieces(CubeDeformation(0.295F), false), 64, 32) } // TODO: reduce to 0.25F (+ dx?) without z-fighting in the player's inventory view
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES_SLIM) { LayerDefinition.create(TravellersGearModels.addGlovePieces(CubeDeformation(0.295F), true), 64, 32) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TRAVELLERS_ARMOR_LEGGINGS) { TravellersWingsModel.createLayer(0.25F) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TRAVELLERS_ARMOR_BOOTS) { LayerDefinition.create(TravellersGearModels.addBootPieces(CubeDeformation(0.5F)), 64, 32) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHTMETAL_ARMOR_INNER) { LayerDefinition.create(KnightmetalArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHTMETAL_ARMOR_OUTER) { LayerDefinition.create(KnightmetalArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.PHANTOM_ARMOR_INNER) { LayerDefinition.create(PhantomArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.PHANTOM_ARMOR_OUTER) { LayerDefinition.create(PhantomArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.YETI_ARMOR_INNER) { LayerDefinition.create(YetiArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.YETI_ARMOR_OUTER) { LayerDefinition.create(YetiArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.ALPHA_YETI_TROPHY, AlphaYetiModel::createTrophy)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA_TROPHY, HydraHeadModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHT_PHANTOM_TROPHY, KnightPhantomModel::createTrophy)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.LICH_TROPHY, LichModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MINOSHROOM_TROPHY, MinoshroomModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.NAGA_TROPHY, NagaModel<EntityRenderState>::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.QUEST_RAM_TROPHY, QuestRamModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.SNOW_QUEEN_TROPHY, SnowQueenModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.UR_GHAST_TROPHY, UrGhastModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.ADHERENT, AdherentModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.ALPHA_YETI, AlphaYetiModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.ARMORED_GIANT) { LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 32) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.BIGHORN_SHEEP, BighornModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.BIGHORN_SHEEP_BABY) { BighornModel.create().apply(BighornModel.BABY_TRANSFORMER) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.BIGHORN_SHEEP_BABY_WOOL) { SheepFurModel.createFurLayer().apply(BighornModel.BABY_TRANSFORMER) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.BLOCKCHAIN_GOBLIN, BlockChainGoblinModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.BOAR, BoarModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.BOAR_BABY) { BoarModel.create().apply(PigModel.BABY_TRANSFORMER) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.BUNNY, BunnyModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.BUNNY_BABY) { BunnyModel.create().apply(BunnyModel.BABY_TRANSFORMER) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.CARMINITE_BROODLING, SpiderModel::createSpiderBodyLayer)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.CARMINITE_GOLEM, CarminiteGolemModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.CARMINITE_GHASTGUARD, TFGhastModel<TFGhastRenderState>::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.CARMINITE_GHASTLING, TFGhastModel<TFGhastRenderState>::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.CHAIN, ChainModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.CUBE_OF_ANNIHILATION, CubeOfAnnihilationModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.DEATH_TOME, DeathTomeModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.DEER, DeerModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.DEER_BABY) { DeerModel.create().apply(DeerModel.BABY_TRANSFORMER) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.FIRE_BEETLE, FireBeetleModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.GIANT_MINER) { LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 32) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.HARBINGER_CUBE, HarbingerCubeModel<LivingEntityRenderState>::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.HEDGE_SPIDER, SpiderModel::createSpiderBodyLayer)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.HELMET_CRAB, HelmetCrabModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.HOSTILE_WOLF) { LayerDefinition.create(AdultWolfModel.createBodyLayer(CubeDeformation.NONE), 64, 32) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA_HEAD, HydraHeadModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA, HydraModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA_MORTAR, HydraMortarModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA_NECK, HydraNeckModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.ICE_CRYSTAL, IceCrystalModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.KING_SPIDER, SpiderModel::createSpiderBodyLayer)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHT_PHANTOM, KnightPhantomModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.KOBOLD, KoboldModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.LICH_MINION) { LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.LICH, LichModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.LOWER_GOBLIN_KNIGHT, LowerGoblinKnightModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.LOYAL_ZOMBIE) { LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MAZE_SLIME, SlimeModel::createInnerBodyLayer)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MAZE_SLIME_OUTER, SlimeModel::createOuterBodyLayer)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MINOSHROOM, MinoshroomModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MINOTAUR, MinotaurModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MIST_WOLF) { LayerDefinition.create(AdultWolfModel.createBodyLayer(CubeDeformation.NONE), 64, 32) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MOSQUITO_SWARM, MosquitoSwarmModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.NAGA, NagaModel<EntityRenderState>::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.NAGA_BODY, NagaModel<EntityRenderState>::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.NOOP) { LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 0, 0) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.PENGUIN, PenguinModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.PENGUIN_BABY) { PenguinModel.create().apply(PenguinModel.BABY_TRANSFORMER) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.PINCH_BEETLE, PinchBeetleModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.PROTECTION_BOX) { LayerDefinition.create(ProtectionBoxModel.createMesh(), 16, 16) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.QUEST_RAM, QuestRamModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.RAVEN, RavenModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.REDCAP, RedcapModel::create)
		val redcapArmor: ArmorModelSet<LayerDefinition> = RedcapModel.createArmorLayerSet()
		ModelLayerRegistry.registerModelLayer(TFModelLayers.REDCAP_ARMOR.head(), redcapArmor::head)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.REDCAP_ARMOR.chest(), redcapArmor::chest)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.REDCAP_ARMOR.legs(), redcapArmor::legs)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.REDCAP_ARMOR.feet(), redcapArmor::feet)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.RISING_ZOMBIE) { LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.ROVING_CUBE, CubeOfAnnihilationModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.SKELETON_DRUID, SkeletonDruidModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.SKELETON_DRUID_BABY) { SkeletonDruidModel.create().apply(HumanoidModel.BABY_TRANSFORMER) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.SLIME_BEETLE, SlimeBeetleModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.SLIME_BEETLE_TAIL, SlimeBeetleModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.SNOW_QUEEN, SnowQueenModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.CHAIN_BLOCK, SpikeBlockModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.SQUIRREL, SquirrelModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.STABLE_ICE_CORE, StableIceCoreModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.SWARM_SPIDER, SpiderModel::createSpiderBodyLayer)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TINY_BIRD, TinyBirdModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TOWERWOOD_BORER, SilverfishModel::createBodyLayer)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TROLL, TrollModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.UNSTABLE_ICE_CORE, UnstableIceCoreModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.UPPER_GOBLIN_KNIGHT, UpperGoblinKnightModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.UR_GHAST, UrGhastModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.WINTER_WOLF) { LayerDefinition.create(AdultWolfModel.createBodyLayer(CubeDeformation.NONE), 64, 32) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.WRAITH, WraithModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.YETI, YetiModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.CICADA, CicadaModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.FIREFLY, FireflyModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.KEEPSAKE_CASKET) { KeepsakeCasketModel.create(true) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.SKULL_CHEST) { KeepsakeCasketModel.create(false) }
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MOONWORM, MoonwormModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.BRAZIER, BrazierModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.RED_THREAD, RedThreadModel::create)
		ModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHTMETAL_SHIELD, KnightmetalShieldModel::create)
	}

	private fun registerParticleFactories() {
		ParticleProviderRegistry.getInstance().register(TFParticleType.LARGE_FLAME, LargeFlameParticle::Factory)
		ParticleProviderRegistry.getInstance().register(TFParticleType.LEAF_RUNE, LeafRuneParticle::Factory)
		ParticleProviderRegistry.getInstance().register(TFParticleType.BOSS_TEAR, GhastTearParticle.Factory())
		ParticleProviderRegistry.getInstance().register(TFParticleType.GHAST_TRAP, GhastTrapParticle::Factory)
		ParticleProviderRegistry.getInstance().register(TFParticleType.PROTECTION, ProtectionParticle::Factory) //probably not a good idea, but worth a shot
		ParticleProviderRegistry.getInstance().register(TFParticleType.SNOW, SnowParticle::Factory)
		ParticleProviderRegistry.getInstance().register(TFParticleType.SNOW_GUARDIAN, SnowGuardianParticle::Factory)
		ParticleProviderRegistry.getInstance().register(TFParticleType.SNOW_WARNING, SnowWarningParticle::SimpleFactory)
		ParticleProviderRegistry.getInstance().register(TFParticleType.EXTENDED_SNOW_WARNING, SnowWarningParticle::ExtendedFactory)
		ParticleProviderRegistry.getInstance().register(TFParticleType.ICE_BEAM, IceBeamParticle::Factory)
		ParticleProviderRegistry.getInstance().register(TFParticleType.ANNIHILATE, AnnihilateParticle::Factory)
		ParticleProviderRegistry.getInstance().register(TFParticleType.PERFECT_DODGE, PerfectDodgeParticle::Provider)
		ParticleProviderRegistry.getInstance().register(TFParticleType.DOUBLE_JUMP, DoubleJumpParticle::Provider)
		ParticleProviderRegistry.getInstance().register(TFParticleType.HUGE_SMOKE, SmokeScaleParticle::Factory)
		ParticleProviderRegistry.getInstance().register(TFParticleType.FIREFLY, FireflyParticle::StationaryProvider)
		ParticleProviderRegistry.getInstance().register(TFParticleType.WANDERING_FIREFLY, FireflyParticle::WanderingProvider)
		ParticleProviderRegistry.getInstance().register(TFParticleType.PARTICLE_SPAWNER_FIREFLY, FireflyParticle::ParticleSpawnerProvider)
		ParticleProviderRegistry.getInstance().register(TFParticleType.FALLEN_LEAF, LeafParticle::Factory)
		ParticleProviderRegistry.getInstance().register(TFParticleType.DIM_FLAME, FlameParticle::SmallFlameProvider)
		ParticleProviderRegistry.getInstance().register(TFParticleType.OMINOUS_FLAME, FlameParticle::SmallFlameProvider)
		ParticleProviderRegistry.getInstance().register(TFParticleType.SORTING_PARTICLE, SortingParticle::Factory)
		ParticleProviderRegistry.getInstance().register(TFParticleType.TRANSFORMATION_PARTICLE, TransformationParticle::Factory)
		ParticleProviderRegistry.getInstance().register(TFParticleType.LOG_CORE_PARTICLE, LogCoreParticle::Factory)
		ParticleProviderRegistry.getInstance().register(TFParticleType.CLOUD_PUFF, CloudPuffParticle::Factory)
		ParticleProviderRegistry.getInstance().register(TFParticleType.DRYING_RACK, DryingRackParticle::Provider)
		ParticleProviderRegistry.getInstance().register(TFParticleType.MAGIC_EFFECT, MagicEffectParticle::Factory)
		ParticleProviderRegistry.getInstance().register(TFParticleType.ANGRY_LICH, AngryLichParticle::Factory)
		ParticleProviderRegistry.getInstance().register(TFParticleType.TWILIGHT_ORB) { sprite -> CustomTextureParticle.Factory(sprite, true) }
		ParticleProviderRegistry.getInstance().register(TFParticleType.SHIELD_BREAK, CustomTextureParticle::ShieldBreak)
	}

	private fun registerMapDecorators() {
		MapDecorationManager.addRenderer(MapDecorationTypes.PLAYER.value(), MagicMapPlayerIconRenderer())
		MapDecorationManager.addRenderer(TFMapDecorations.QUEST_GROVE, ConqueredMapIconRenderer())
		MapDecorationManager.addRenderer(TFMapDecorations.NAGA_COURTYARD, ConqueredMapIconRenderer())
		MapDecorationManager.addRenderer(TFMapDecorations.LICH_TOWER, ConqueredMapIconRenderer())
		MapDecorationManager.addRenderer(TFMapDecorations.LABYRINTH, ConqueredMapIconRenderer())
		MapDecorationManager.addRenderer(TFMapDecorations.HYDRA_LAIR, ConqueredMapIconRenderer())
		MapDecorationManager.addRenderer(TFMapDecorations.KNIGHT_STRONGHOLD, ConqueredMapIconRenderer())
		MapDecorationManager.addRenderer(TFMapDecorations.DARK_TOWER, ConqueredMapIconRenderer())
		MapDecorationManager.addRenderer(TFMapDecorations.YETI_LAIR, ConqueredMapIconRenderer())
		MapDecorationManager.addRenderer(TFMapDecorations.AURORA_PALACE, ConqueredMapIconRenderer())
		MapDecorationManager.addRenderer(TFMapDecorations.FINAL_CASTLE, ConqueredMapIconRenderer())
	}

	private fun registerMapDecorationTypes() {
		MapDecorationManager.addMap(MagicMapPlayerIconRenderer())
		val conquered = ConqueredMapIconRenderer()
		MapDecorationManager.addDecoration(BuiltInRegistries.MAP_DECORATION_TYPE.getResourceKey(TFMapDecorations.QUEST_GROVE).orElseThrow(), conquered)
		MapDecorationManager.addDecoration(BuiltInRegistries.MAP_DECORATION_TYPE.getResourceKey(TFMapDecorations.NAGA_COURTYARD).orElseThrow(), conquered)
		MapDecorationManager.addDecoration(BuiltInRegistries.MAP_DECORATION_TYPE.getResourceKey(TFMapDecorations.LICH_TOWER).orElseThrow(), conquered)
		MapDecorationManager.addDecoration(BuiltInRegistries.MAP_DECORATION_TYPE.getResourceKey(TFMapDecorations.LABYRINTH).orElseThrow(), conquered)
		MapDecorationManager.addDecoration(BuiltInRegistries.MAP_DECORATION_TYPE.getResourceKey(TFMapDecorations.HYDRA_LAIR).orElseThrow(), conquered)
		MapDecorationManager.addDecoration(BuiltInRegistries.MAP_DECORATION_TYPE.getResourceKey(TFMapDecorations.KNIGHT_STRONGHOLD).orElseThrow(), conquered)
		MapDecorationManager.addDecoration(BuiltInRegistries.MAP_DECORATION_TYPE.getResourceKey(TFMapDecorations.DARK_TOWER).orElseThrow(), conquered)
		MapDecorationManager.addDecoration(BuiltInRegistries.MAP_DECORATION_TYPE.getResourceKey(TFMapDecorations.YETI_LAIR).orElseThrow(), conquered)
		MapDecorationManager.addDecoration(BuiltInRegistries.MAP_DECORATION_TYPE.getResourceKey(TFMapDecorations.AURORA_PALACE).orElseThrow(), conquered)
		MapDecorationManager.addDecoration(BuiltInRegistries.MAP_DECORATION_TYPE.getResourceKey(TFMapDecorations.FINAL_CASTLE).orElseThrow(), conquered)
	}

	private var bakedMultiPartRenderers = false
	private fun registerRenderLayers() {
		LivingEntityRenderLayerRegistrationCallback.EVENT.register(LivingEntityRenderLayerRegistrationCallback { _: EntityType<out LivingEntity>, renderer: LivingEntityRenderer<*, *, *>, registrationHelper: RegistrationHelper, context: EntityRendererProvider.Context ->
			if (!bakedMultiPartRenderers) {
				BakedMultiPartRenderers.bakeMultiPartRenderers(context)
				bakedMultiPartRenderers = true
			}
			attachLivingRenderLayers(renderer, registrationHelper)
		})
	}

	@Suppress("UNCHECKED_CAST")
	private fun attachLivingRenderLayers(
		renderer: LivingEntityRenderer<*, *, *>,
		registrationHelper: RegistrationHelper
	) {
		registrationHelper.register(ShieldLayer(renderer) as RenderLayer<EntityRenderState, EntityModel<EntityRenderState>>)
		registrationHelper.register(IceLayer(renderer) as RenderLayer<EntityRenderState, EntityModel<EntityRenderState>>)
	}

	private fun registerArmorRenderers() {
		ArmorRenderer.register(
			{ context: EntityRendererProvider.Context -> TFSimpleArmorRenderer(context, { part: ModelPart -> FieryArmorModel(part) }, TFModelLayers.ARCTIC_ARMOR_INNER, TFModelLayers.ARCTIC_ARMOR_OUTER) },
			TFItems.ARCTIC_HELMET, TFItems.ARCTIC_CHESTPLATE, TFItems.ARCTIC_LEGGINGS, TFItems.ARCTIC_BOOTS
		)
		ArmorRenderer.register(
			{ context: EntityRendererProvider.Context -> TFSimpleArmorRenderer(context, { part: ModelPart -> FieryArmorModel(part) }, TFModelLayers.FIERY_ARMOR_INNER, TFModelLayers.FIERY_ARMOR_OUTER) },
			TFItems.FIERY_HELMET, TFItems.FIERY_CHESTPLATE, TFItems.FIERY_LEGGINGS, TFItems.FIERY_BOOTS
		)
		ArmorRenderer.register(
			{ context: EntityRendererProvider.Context -> TravellersArmorRenderer(context) },
			TFItems.TRAVELLERS_GOGGLES, TFItems.TRAVELLERS_VEST, TFItems.TRAVELLERS_GLOVES, TFItems.TRAVELLERS_WINGS, TFItems.TRAVELLERS_BELT, TFItems.TRAVELLERS_BOOTS
		)
		ArmorRenderer.register(
			{ context: EntityRendererProvider.Context -> TFSimpleArmorRenderer(context, { root: ModelPart -> TFArmorModel(root) }, TFModelLayers.KNIGHTMETAL_ARMOR_INNER, TFModelLayers.KNIGHTMETAL_ARMOR_OUTER) },
			TFItems.KNIGHTMETAL_HELMET, TFItems.KNIGHTMETAL_CHESTPLATE, TFItems.KNIGHTMETAL_LEGGINGS, TFItems.KNIGHTMETAL_BOOTS
		)
		ArmorRenderer.register(
			{ context: EntityRendererProvider.Context -> TFSimpleArmorRenderer(context, { root: ModelPart -> TFArmorModel(root) }, TFModelLayers.PHANTOM_ARMOR_INNER, TFModelLayers.PHANTOM_ARMOR_OUTER) },
			TFItems.PHANTOM_HELMET, TFItems.PHANTOM_CHESTPLATE
		)
		ArmorRenderer.register(
			{ context: EntityRendererProvider.Context -> TFSimpleArmorRenderer(context, { part: ModelPart -> YetiArmorModel(part) }, TFModelLayers.YETI_ARMOR_INNER, TFModelLayers.YETI_ARMOR_OUTER) },
			TFItems.YETI_HELMET, TFItems.YETI_CHESTPLATE, TFItems.YETI_LEGGINGS, TFItems.YETI_BOOTS
		)
	}

	private fun registerPictureInPictureRenderers() {
		PictureInPictureRendererRegistry.register { ctx: PictureInPictureRendererRegistry.Context -> GuiBlockRenderer(ctx.bufferSource()) }
	}
}