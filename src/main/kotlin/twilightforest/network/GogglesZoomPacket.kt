package twilightforest.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PlayerLookup
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import twilightforest.TFCommon.prefix
import twilightforest.init.TFDataAttachments
import twilightforest.init.TFSounds
import twilightforest.init.custom.TravellersModifiersManager
import java.util.*

data class GogglesZoomPacket(
	val isUsingZoom: Boolean,
	val playerUUID: UUID
) : CustomPacketPayload {
	companion object {
		val TYPE: CustomPacketPayload.Type<GogglesZoomPacket> = CustomPacketPayload.Type(prefix("goggles_zoom_packet"))
		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, GogglesZoomPacket> = CustomPacketPayload.codec(GogglesZoomPacket::write, ::GogglesZoomPacket)

		fun handleServer(packet: GogglesZoomPacket, ctx: ServerPlayNetworking.Context) {
			val player = ctx.player().level().getPlayerByUUID(packet.playerUUID) ?: return
			val canChangeZoomState = TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.ZOOM_ABILITY)
			if (canChangeZoomState) {
				player.setAttached(TFDataAttachments.IS_USING_GOGGLES_ZOOM_MODIFIER, packet.isUsingZoom)
				player.playSound(if (packet.isUsingZoom) TFSounds.GOGGLES_ZOOM_IN.value() else TFSounds.GOGGLES_ZOOM_OUT.value())
				for (serverPlayer in PlayerLookup.tracking(player)) {
					ServerPlayNetworking.send(serverPlayer, GogglesZoomPacket(packet.isUsingZoom, player.getUUID()))
				}
			}
		}

		fun handleClient(packet: GogglesZoomPacket, ctx: ClientPlayNetworking.Context) {
			val player = ctx.client().level!!.getPlayerByUUID(packet.playerUUID) ?: return
			player.setAttached(TFDataAttachments.IS_USING_GOGGLES_ZOOM_MODIFIER, packet.isUsingZoom)
		}
	}

	constructor(buf: RegistryFriendlyByteBuf) : this(buf.readBoolean(), buf.readUUID())

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

	private fun write(registryFriendlyByteBuf: RegistryFriendlyByteBuf) {
		registryFriendlyByteBuf.writeBoolean(isUsingZoom)
		registryFriendlyByteBuf.writeUUID(playerUUID)
	}
}