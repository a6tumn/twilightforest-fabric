package twilightforest.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.Minecraft
import net.minecraft.core.particles.ItemParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import twilightforest.TFCommon
import twilightforest.config.TFConfig
import twilightforest.entity.CharmEffect
import twilightforest.init.TFEntities

data class SpawnCharmPacket(
	val charm: ItemStack,
	val event: ResourceKey<SoundEvent>
) : CustomPacketPayload {
	companion object {
		val TYPE: CustomPacketPayload.Type<SpawnCharmPacket> = CustomPacketPayload.Type(TFCommon.prefix("spawn_charm"))
		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SpawnCharmPacket> = CustomPacketPayload.codec(SpawnCharmPacket::write, ::SpawnCharmPacket)

		fun handle(
			packet: SpawnCharmPacket,
			ctx: ClientPlayNetworking.Context
		) {
			val player: Player = ctx.player()
			val level = ctx.client().level
			val camera = Minecraft.getInstance().cameraEntity
			if (TFConfig.spawnCharmAnimationAsTotem) {
				Minecraft.getInstance().gameRenderer.displayItemActivation(packet.charm)
				//prefer the camera pos over the player as the player position isnt quite synced to the client yet
				Minecraft.getInstance().particleEngine.createTrackingEmitter(camera ?: player, ItemParticleOption(ParticleTypes.ITEM, packet.charm.item), 20)
			} else {
				val effect = CharmEffect(TFEntities.CHARM_EFFECT, player.level(), player, packet.charm)
				effect.offset = Math.PI.toFloat()
				level!!.addEntity(effect)
			}
			val event: SoundEvent? = BuiltInRegistries.SOUND_EVENT.get(packet.event).map { it.value() }.orElse(null)
			if (camera != null && event != null) {
				level!!.playLocalSound(camera.x, camera.y, camera.z, event, player.soundSource, 1.5f, 1.0f, false)
			}
		}
	}

	constructor(buf: RegistryFriendlyByteBuf) : this(ItemStack.STREAM_CODEC.decode(buf), buf.readResourceKey(Registries.SOUND_EVENT))

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

	fun write(buf: RegistryFriendlyByteBuf) {
		ItemStack.STREAM_CODEC.encode(buf, this.charm)
		buf.writeResourceKey(this.event)
	}
}