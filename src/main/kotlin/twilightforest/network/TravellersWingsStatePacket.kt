package twilightforest.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import twilightforest.TFCommon
import twilightforest.components.entity.TravellersWingsAttachment
import twilightforest.components.entity.TravellersWingsAttachment.WingState
import twilightforest.init.TFDataAttachments

class TravellersWingsStatePacket : CustomPacketPayload {
	companion object {
		val TYPE: CustomPacketPayload.Type<TravellersWingsStatePacket> = CustomPacketPayload.Type(TFCommon.prefix("travellers_wings_state"))
		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, TravellersWingsStatePacket> = CustomPacketPayload.codec(TravellersWingsStatePacket::write, ::TravellersWingsStatePacket)

		fun handle(
			message: TravellersWingsStatePacket,
			ctx: ClientPlayNetworking.Context
		) {
			val player: Player = ctx.player()
			val entity = player.level().getEntity(message.entityId)
			if (entity is LivingEntity) {
				val attachment: TravellersWingsAttachment = entity.getAttachedOrCreate(TFDataAttachments.TRAVELLERS_WINGS)
				attachment.state = message.state
				attachment.sidestepLeft = message.sidestepLeft
				attachment.doubleJumpTimer = message.doubleJumpTimer
				attachment.sidestepTimer = message.sidestepTimer
			}
		}
	}

	private var entityId: Int
	private var state: WingState
	private var sidestepLeft: Boolean
	private var doubleJumpTimer: Int
	private var sidestepTimer: Int

	constructor(entityId: Int, state: WingState, sidestepLeft: Boolean, doubleJumpTimer: Int, sidestepTimer: Int) {
		this.entityId = entityId
		this.state = state
		this.sidestepLeft = sidestepLeft
		this.doubleJumpTimer = doubleJumpTimer
		this.sidestepTimer = sidestepTimer
	}

	constructor(buf: RegistryFriendlyByteBuf) {
		this.entityId = buf.readInt()
		this.state = buf.readEnum(WingState::class.java)
		this.sidestepLeft = buf.readBoolean()
		this.doubleJumpTimer = buf.readInt()
		this.sidestepTimer = buf.readInt()
	}

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

	fun write(buf: RegistryFriendlyByteBuf) {
		buf.writeInt(this.entityId)
		buf.writeEnum(this.state)
		buf.writeBoolean(this.sidestepLeft)
		buf.writeInt(this.doubleJumpTimer)
		buf.writeInt(this.sidestepTimer)
	}
}