package twilightforest.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PlayerLookup
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import twilightforest.TFCommon
import twilightforest.init.TFDataAttachments
import java.util.*

data class GradualGlidePacket(val isGraduallyGliding: Boolean, val playerUUID: UUID) : CustomPacketPayload{
	companion object {
		val TYPE: CustomPacketPayload.Type<GradualGlidePacket> = CustomPacketPayload.Type(TFCommon.prefix("gradual_glide_packet"))
		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, GradualGlidePacket> = CustomPacketPayload.codec(GradualGlidePacket::write, ::GradualGlidePacket)

		fun handleServer(
			packet: GradualGlidePacket,
			ctx: ServerPlayNetworking.Context
		) {
			val player = ctx.player().level().getPlayerByUUID(packet.playerUUID) ?: return
			player.setAttached(TFDataAttachments.IS_GRADUALLY_GLIDING, packet.isGraduallyGliding)
			for (serverPlayer in PlayerLookup.tracking(player)) {
				ServerPlayNetworking.send(serverPlayer, GradualGlidePacket(packet.isGraduallyGliding, player.getUUID()))
			}
		}

		fun handleClient(
			packet: GradualGlidePacket,
			ctx: ClientPlayNetworking.Context
		) {
			val player = ctx.client().level!!.getPlayerByUUID(packet.playerUUID) ?: return
			player.setAttached(TFDataAttachments.IS_GRADUALLY_GLIDING, packet.isGraduallyGliding)
		}
	}

	constructor(buf: RegistryFriendlyByteBuf) : this(buf.readBoolean(), buf.readUUID())

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

	private fun write(registryFriendlyByteBuf: RegistryFriendlyByteBuf) {
		registryFriendlyByteBuf.writeBoolean(isGraduallyGliding)
		registryFriendlyByteBuf.writeUUID(playerUUID)
	}
}