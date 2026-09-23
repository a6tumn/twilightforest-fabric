package twilightforest.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.Minecraft
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket
import twilightforest.TFCommon
import twilightforest.item.mapdata.MapDataManager
import twilightforest.item.mapdata.TFMagicMapData
import java.util.stream.StreamSupport

// Rewraps vanilla ClientboundMapItemDataPacket to sync conquered status of structures
data class MagicMapPacket(
	val inner: ClientboundMapItemDataPacket,
	val conqueredStructures: MutableList<String>
) : CustomPacketPayload {
	companion object {
		val TYPE: CustomPacketPayload.Type<MagicMapPacket> = CustomPacketPayload.Type(TFCommon.prefix("magic_map"))
		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, MagicMapPacket> = StreamCodec.composite(
			ClientboundMapItemDataPacket.STREAM_CODEC,
			MagicMapPacket::inner,
			ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
			MagicMapPacket::conqueredStructures,
			::MagicMapPacket
		)

		fun handle(
			message: MagicMapPacket,
			ctx: ClientPlayNetworking.Context
		) {
			val clientLevel = ctx.client().level
			val mapId = message.inner.mapId()
			var mapdata = MapDataManager.getClientMagicMapData(mapId)
			if (mapdata == null) {
				mapdata = TFMagicMapData(0, 0, message.inner.scale(), false, false, message.inner.locked(), clientLevel!!.dimension())
				MapDataManager.saveClientMagicMapData(mapId, mapdata)
			}

			message.inner.applyToMap(mapdata)
			//TF: sync conquered structures for map
			mapdata.conqueredStructures.clear()
			mapdata.conqueredStructures.addAll(message.conqueredStructures)
			Minecraft.getInstance().mapTextureManager.update(mapId, mapdata)

			val saved = clientLevel!!.getMapData(message.inner.mapId())

			saved?.addClientSideDecorations(StreamSupport.stream(mapdata.getDecorations().spliterator(), false).toList())
		}
	}

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}