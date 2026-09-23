package twilightforest.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3
import twilightforest.TFCommon
import twilightforest.item.LifedrainScepterItem

data class LifedrainParticlePacket(val entityID: Int, val victimPos: Vec3) : CustomPacketPayload {
	companion object {
		val TYPE: CustomPacketPayload.Type<LifedrainParticlePacket> = CustomPacketPayload.Type(TFCommon.prefix("lifedrain_particles"))
		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, LifedrainParticlePacket> = CustomPacketPayload.codec(LifedrainParticlePacket::write, ::LifedrainParticlePacket)

		fun handle(
			packet: LifedrainParticlePacket,
			ctx: ClientPlayNetworking.Context
		) {
			val entity = ctx.client().level!!.getEntity(packet.entityID)
			if (entity is LivingEntity) {
				LifedrainScepterItem.makeRedMagicTrail(entity.level(), entity, packet.victimPos)
			}
		}
	}

	constructor(buf: RegistryFriendlyByteBuf) : this(buf.readInt(), Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()))

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

	fun write(buf: RegistryFriendlyByteBuf) {
		buf.writeInt(this.entityID)
		buf.writeDouble(this.victimPos.x())
		buf.writeDouble(this.victimPos.y())
		buf.writeDouble(this.victimPos.z())
	}
}