package twilightforest.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.item.ItemStack
import twilightforest.TFCommon
import twilightforest.block.entity.MasonJarBlockEntity

data class SetMasonJarItemPacket(
	val pos: BlockPos,
	val empty: Boolean,
	val stack: ItemStack,
	val rotation: Int
) : CustomPacketPayload {
	companion object {
		val TYPE: CustomPacketPayload.Type<SetMasonJarItemPacket> = CustomPacketPayload.Type(TFCommon.prefix("set_mason_jar_item"))
		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SetMasonJarItemPacket> = CustomPacketPayload.codec(SetMasonJarItemPacket::write, SetMasonJarItemPacket::read)

		fun read(buf: RegistryFriendlyByteBuf): SetMasonJarItemPacket {
			val rotation = buf.readInt()
			val pos = buf.readBlockPos()
			val empty = buf.readBoolean()
			return SetMasonJarItemPacket(pos, empty, if (empty) ItemStack.EMPTY else ItemStack.STREAM_CODEC.decode(buf), rotation)
		}

		fun handle(
			packet: SetMasonJarItemPacket,
			ctx: ClientPlayNetworking.Context
		) {
			val clientLevel = ctx.client().level
			val blockEntity = clientLevel!!.getBlockEntity(packet.pos)
			if (blockEntity is MasonJarBlockEntity) {
				blockEntity.itemHandler.item = packet.stack
				blockEntity.setItemRotation(packet.rotation)
				blockEntity.setChanged()
			}
		}
	}

	constructor(pos: BlockPos, stack: ItemStack, rotation: Int) : this(pos, stack.isEmpty, stack, rotation)

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

	fun write(buf: RegistryFriendlyByteBuf) {
		buf.writeInt(this.rotation)
		buf.writeBlockPos(this.pos)
		buf.writeBoolean(this.empty)
		if (!this.empty) ItemStack.STREAM_CODEC.encode(buf, this.stack)
	}
}