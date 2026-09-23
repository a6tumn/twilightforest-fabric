package twilightforest.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import twilightforest.TFCommon
import twilightforest.client.listeners.LockedBiomeToastEventListeners
import twilightforest.client.renderer.TFWeatherRenderer

data class EnforceProgressionStatusPacket(val enforce: Boolean) : CustomPacketPayload {
	companion object {
		val TYPE: CustomPacketPayload.Type<EnforceProgressionStatusPacket> = CustomPacketPayload.Type(TFCommon.prefix("sync_progression_status"))
		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, EnforceProgressionStatusPacket> = CustomPacketPayload.codec(EnforceProgressionStatusPacket::write, ::EnforceProgressionStatusPacket)

		fun handle(
			message: EnforceProgressionStatusPacket,
			ctx: ClientPlayNetworking.Context
		) {
			val enforce = message.enforce
			TFWeatherRenderer.setProgressionEnforced(enforce)
			LockedBiomeToastEventListeners.setProgressionEnforced(enforce)
		}
	}

	constructor(buf: FriendlyByteBuf) : this(buf.readBoolean())

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

	fun write(buf: FriendlyByteBuf) {
		buf.writeBoolean(this.enforce)
	}
}