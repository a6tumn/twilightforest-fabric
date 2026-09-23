package twilightforest.network

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import twilightforest.TFCommon
import twilightforest.item.travellers_gear.TravellersGearLogic

data class PerformSidestepPacket(val isLeftStepSide: Boolean) : CustomPacketPayload {
	companion object {
		val TYPE: CustomPacketPayload.Type<PerformSidestepPacket> = CustomPacketPayload.Type(TFCommon.prefix("perform_sidestep_packet"))
		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, PerformSidestepPacket> = CustomPacketPayload.codec(PerformSidestepPacket::write, ::PerformSidestepPacket)

		fun handle(
			message: PerformSidestepPacket,
			ctx: ServerPlayNetworking.Context
		) {
			if (!TravellersGearLogic.tryPerformSidestep(ctx.player(), message.isLeftStepSide)) TravellersGearLogic.handleSidestepAbuse(ctx.player())
		}
	}

	constructor(buf: RegistryFriendlyByteBuf) : this(buf.readBoolean())

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

	private fun write(registryFriendlyByteBuf: RegistryFriendlyByteBuf) {
		registryFriendlyByteBuf.writeBoolean(isLeftStepSide)
	}
}