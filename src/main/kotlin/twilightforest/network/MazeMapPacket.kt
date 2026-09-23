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
import twilightforest.item.mapdata.TFMazeMapData
import java.util.stream.StreamSupport

// Rewraps vanilla ClientboundMapItemDataPacket to properly add our own data
data class MazeMapPacket(
	val inner: ClientboundMapItemDataPacket,
	val ore: Boolean,
	val yCenter: Int
) : CustomPacketPayload {
	companion object {
		val TYPE: CustomPacketPayload.Type<MazeMapPacket> = CustomPacketPayload.Type(TFCommon.prefix("maze_map"))
		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, MazeMapPacket> = StreamCodec.composite(
			ClientboundMapItemDataPacket.STREAM_CODEC, MazeMapPacket::inner,
			ByteBufCodecs.BOOL, MazeMapPacket::ore,
			ByteBufCodecs.INT, MazeMapPacket::yCenter,
			::MazeMapPacket
		)

		fun handle(
			message: MazeMapPacket,
			ctx: ClientPlayNetworking.Context
		) {
			val clientLevel = ctx.client().level
			val mapId = message.inner.mapId()
			var mapdata = MapDataManager.getClientMazeMapData(mapId)
			if (mapdata == null) {
				mapdata = TFMazeMapData(
					0, 0,
					message.inner.scale(),
					false,
					false,
					message.inner.locked(),
					clientLevel!!.dimension()
				)
				MapDataManager.saveClientMazeMapData(mapId, mapdata)
			}

			mapdata.ore = message.ore
			mapdata.yCenter = message.yCenter
			message.inner.applyToMap(mapdata)
			Minecraft.getInstance().mapTextureManager.update(mapId, mapdata)

			val saved = clientLevel!!.getMapData(message.inner.mapId())

			saved?.addClientSideDecorations(StreamSupport.stream(mapdata.getDecorations().spliterator(), false).toList())
		}
	}

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}