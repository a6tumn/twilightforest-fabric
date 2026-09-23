package twilightforest.network

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import twilightforest.TFCommon
import twilightforest.item.travellers_gear.TravellersArmorBeltItem

object SwapHotbarPacket : CustomPacketPayload {
	val TYPE: CustomPacketPayload.Type<SwapHotbarPacket> = CustomPacketPayload.Type(TFCommon.prefix("swap_hotbar"))
	val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SwapHotbarPacket> = StreamCodec.unit(this)

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

	fun handle(
		message: SwapHotbarPacket,
		ctx: ServerPlayNetworking.Context
	) {
		val player: Player = ctx.player()
		TravellersArmorBeltItem.travellersTrySwapHotbar(player)
		if (player is ServerPlayer) player.broadcastToPlayer(player)
	}
}