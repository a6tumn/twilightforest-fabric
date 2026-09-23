package twilightforest.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.phys.Vec3
import twilightforest.TFCommon

class ParticlePacket : CustomPacketPayload {
	companion object {
		val TYPE: CustomPacketPayload.Type<ParticlePacket> = CustomPacketPayload.Type(TFCommon.prefix("particle_queue"))
		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ParticlePacket> = CustomPacketPayload.codec(ParticlePacket::write, ::ParticlePacket)

		fun handle(
			message: ParticlePacket,
			ctx: ClientPlayNetworking.Context
		) {
			for ((particleOptions, overrideLimiter, alwaysShow, x, y, z, x2, y2, z2) in message.queuedParticles) {
				ctx.client().level!!.addParticle(particleOptions, overrideLimiter, alwaysShow, x, y, z, x2, y2, z2)
			}
		}
	}

	private val queuedParticles: MutableList<QueuedParticle> = ArrayList()

	constructor()

	constructor(buf: RegistryFriendlyByteBuf) {
		val size = buf.readInt()
		for (i in 0..<size) {
			val type = BuiltInRegistries.PARTICLE_TYPE.byId(buf.readInt()) ?: break // Fail silently and end execution entirely. Due to Type serialization we now have completely unknown data in the pipeline without any way to safely read it all
			this.queuedParticles.add(QueuedParticle(ParticleTypes.STREAM_CODEC.decode(buf), buf.readBoolean(), buf.readBoolean(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble()))
		}
	}

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

	fun write(buf: RegistryFriendlyByteBuf) {
		buf.writeInt(this.queuedParticles.size)
		for ((particleOptions, overrideLimiter, alwaysShow, x, y, z, x2, y2, z2) in this.queuedParticles) {
			val d = BuiltInRegistries.PARTICLE_TYPE.getId(particleOptions.type)
			buf.writeInt(d)
			ParticleTypes.STREAM_CODEC.encode(buf, particleOptions)
			buf.writeBoolean(overrideLimiter)
			buf.writeBoolean(alwaysShow)
			buf.writeDouble(x)
			buf.writeDouble(y)
			buf.writeDouble(z)
			buf.writeDouble(x2)
			buf.writeDouble(y2)
			buf.writeDouble(z2)
		}
	}

	fun queueParticle(
		particleOptions: ParticleOptions,
		overrideLimiter: Boolean,
		alwaysShow: Boolean,
		x: Double,
		y: Double,
		z: Double,
		x2: Double,
		y2: Double,
		z2: Double
	) {
		this.queuedParticles.add(QueuedParticle(particleOptions, overrideLimiter, alwaysShow, x, y, z, x2, y2, z2))
	}

	fun queueParticle(
		particleOptions: ParticleOptions,
		overrideLimiter: Boolean,
		alwaysShow: Boolean,
		xyz: Vec3,
		xyz2: Vec3
	) {
		this.queuedParticles.add(QueuedParticle(particleOptions, overrideLimiter, alwaysShow, xyz.x, xyz.y, xyz.z, xyz2.x, xyz2.y, xyz2.z))
	}

	private data class QueuedParticle(
		val particleOptions: ParticleOptions,
		val overrideLimiter: Boolean,
		val alwaysShow: Boolean,
		val x: Double,
		val y: Double,
		val z: Double,
		val x2: Double,
		val y2: Double,
		val z2: Double
	)
}