package twilightforest.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.level.levelgen.structure.BoundingBox
import twilightforest.TFCommon.prefix
import twilightforest.entity.ProtectionBox
import twilightforest.init.TFParticleType
import java.util.function.Consumer

class AreaProtectionPacket : CustomPacketPayload {
	companion object {
		val TYPE: CustomPacketPayload.Type<AreaProtectionPacket> = CustomPacketPayload.Type(prefix("add_protection_box"))
		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, AreaProtectionPacket> = CustomPacketPayload.codec(AreaProtectionPacket::write, ::AreaProtectionPacket)

		fun handle(
			message: AreaProtectionPacket,
			ctx: ClientPlayNetworking.Context
		) {
			val level = ctx.client().level!!
			message.sbb.forEach { box ->
				for (entity in level.entitiesForRendering()) {
					if (entity is ProtectionBox) {
						if (entity.lifeTime > 0 && entity.matches(box)) {
							entity.resetLifetime()
							return@forEach
						}
					}
				}

				level.addEntity(ProtectionBox(level, box))
			}

			for (i in 0..<20) {
				val vx = level.random.nextGaussian() * 0.02
				val vy = level.random.nextGaussian() * 0.02
				val vz = level.random.nextGaussian() * 0.02

				val x = message.pos.x + 0.5 + level.random.nextFloat() - level.random.nextFloat()
				val y = message.pos.y + 0.5 + level.random.nextFloat() - level.random.nextFloat()
				val z = message.pos.z + 0.5 + level.random.nextFloat() - level.random.nextFloat()

				level.addParticle(TFParticleType.PROTECTION, x, y, z, vx, vy, vz)
			}
		}
	}

	private var sbb: MutableList<BoundingBox>
	private var pos: BlockPos

	constructor(sbb: List<BoundingBox>, pos: BlockPos) {
		this.sbb = sbb.toMutableList()
		this.pos = pos
	}

	constructor(buf: FriendlyByteBuf) {
		this.sbb = ArrayList()
		val len = buf.readInt()
		for (i in 0..<len) {
			this.sbb.add(BoundingBox(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt()))
		}
		this.pos = buf.readBlockPos()
	}

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

	fun write(buf: RegistryFriendlyByteBuf) {
		buf.writeInt(this.sbb.size)
		this.sbb.forEach(Consumer { box: BoundingBox ->
			buf.writeInt(box.minX())
			buf.writeInt(box.minY())
			buf.writeInt(box.minZ())
			buf.writeInt(box.maxX())
			buf.writeInt(box.maxY())
			buf.writeInt(box.maxZ())
		})
		buf.writeBlockPos(this.pos)
	}
}