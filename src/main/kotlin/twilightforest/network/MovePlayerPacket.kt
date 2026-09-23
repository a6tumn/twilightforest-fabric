package twilightforest.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import twilightforest.TFCommon

data class MovePlayerPacket(
	val motionX: Double,
	val motionY: Double,
	val motionZ: Double
) : CustomPacketPayload {
	companion object {
		val TYPE: CustomPacketPayload.Type<MovePlayerPacket> = CustomPacketPayload.Type(TFCommon.prefix("move_player"))
		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, MovePlayerPacket> = CustomPacketPayload.codec(MovePlayerPacket::write, ::MovePlayerPacket)

		fun handle(
			message: MovePlayerPacket,
			ctx: ClientPlayNetworking.Context
		) {
			ctx.player().push(message.motionX, message.motionY, message.motionZ)
		}
	}

	constructor(buf: FriendlyByteBuf) : this(buf.readDouble(), buf.readDouble(), buf.readDouble())

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

	fun write(buf: FriendlyByteBuf) {
		buf.writeDouble(this.motionX)
		buf.writeDouble(this.motionY)
		buf.writeDouble(this.motionZ)
	}
}