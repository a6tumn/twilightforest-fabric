package twilightforest.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.entity.LivingEntity
import twilightforest.TFCommon
import twilightforest.client.MovingCicadaSoundInstance

data class CreateMovingCicadaSoundPacket(val entityID: Int) : CustomPacketPayload {
	companion object {
		val TYPE: CustomPacketPayload.Type<CreateMovingCicadaSoundPacket> = CustomPacketPayload.Type(TFCommon.prefix("create_cicada_sound"))
		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, CreateMovingCicadaSoundPacket> = CustomPacketPayload.codec(CreateMovingCicadaSoundPacket::write, ::CreateMovingCicadaSoundPacket)

		fun handle(
			message: CreateMovingCicadaSoundPacket,
			ctx: ClientPlayNetworking.Context
		) {
			val entity = ctx.client().level!!.getEntity(message.entityID)
			if (entity is LivingEntity) {
				Minecraft.getInstance().soundManager.queueTickingSound(MovingCicadaSoundInstance(entity))
			}
		}
	}

	constructor(buf: RegistryFriendlyByteBuf) : this(buf.readInt())

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

	fun write(buf: FriendlyByteBuf) {
		buf.writeInt(this.entityID)
	}
}