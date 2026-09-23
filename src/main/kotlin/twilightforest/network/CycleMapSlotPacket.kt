package twilightforest.network

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.entity.EquipmentSlot
import twilightforest.TFCommon
import twilightforest.components.item.ItemDisplayContents
import twilightforest.init.TFDataComponents
import twilightforest.init.TFSounds
import twilightforest.init.custom.TravellersModifiersManager

object CycleMapSlotPacket : CustomPacketPayload {
	val TYPE: CustomPacketPayload.Type<CycleMapSlotPacket> = CustomPacketPayload.Type(TFCommon.prefix("cycle_map_slot_packet"))
	val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, CycleMapSlotPacket> = StreamCodec.unit(this)

	fun handle(
		message: CycleMapSlotPacket,
		ctx: ServerPlayNetworking.Context
	) {
		val serverPlayer = ctx.player()
		val headStack = serverPlayer.getItemBySlot(EquipmentSlot.HEAD)
		val contents = headStack.get(TFDataComponents.ITEM_DISPLAY)
		if (contents == null || contents.isEmpty || !TravellersModifiersManager.isModifierActive(serverPlayer, TravellersModifiersManager.ITEM_DISPLAY_MODIFIER)) return

		val mutable = ItemDisplayContents.Mutable(contents)
		val oldIndex = mutable.chosenMapSlot()
		val newIndex = mutable.cycleChosenMapSlot()

		if (oldIndex != newIndex) {
			val updatedContents = mutable.toImmutable()
			headStack.set(TFDataComponents.ITEM_DISPLAY, updatedContents)
			serverPlayer.inventory.setChanged()
			serverPlayer.playSound(if (newIndex == -1) TFSounds.CYCLE_MAPS_EMPTY.value() else TFSounds.CYCLE_MAPS.value(), 1f, 1f)
		}
	}

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}