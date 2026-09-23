package twilightforest.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.Minecraft
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.item.ItemStackTemplate
import twilightforest.TFCommon
import twilightforest.client.MissingAdvancementToast

data class MissingAdvancementToastPacket(
	val title: Component,
	val icon: ItemStackTemplate
) : CustomPacketPayload {
	companion object {
		val TYPE: CustomPacketPayload.Type<MissingAdvancementToastPacket> = CustomPacketPayload.Type(TFCommon.prefix("missing_advancement_toast"))
		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, MissingAdvancementToastPacket> = StreamCodec.composite(
			ComponentSerialization.STREAM_CODEC, MissingAdvancementToastPacket::title,
			ItemStackTemplate.STREAM_CODEC, MissingAdvancementToastPacket::icon,
			::MissingAdvancementToastPacket
		)

		fun handle(
			packet: MissingAdvancementToastPacket,
			ctx: ClientPlayNetworking.Context
		) {
			Minecraft.getInstance().toastManager.addToast(MissingAdvancementToast(packet.title, packet.icon))
		}
	}

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}