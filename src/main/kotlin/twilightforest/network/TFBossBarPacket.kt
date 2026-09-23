package twilightforest.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.Minecraft
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.network.protocol.game.ClientboundBossEventPacket
import net.minecraft.world.BossEvent
import twilightforest.TFCommon.prefix
import twilightforest.entity.boss.bar.ClientTFBossBar
import twilightforest.entity.boss.bar.ServerTFBossBar
import java.util.*

abstract class TFBossBarPacket : CustomPacketPayload {
	protected var id: UUID

	constructor(bossEvent: ServerTFBossBar) {
		this.id = bossEvent.id
	}

	constructor(buf: RegistryFriendlyByteBuf) {
		this.id = buf.readUUID()
	}

	open fun write(buf: RegistryFriendlyByteBuf) {
		buf.writeUUID(this.id)
	}

	class AddTFBossBarPacket : TFBossBarPacket {
		companion object {
			val TYPE: CustomPacketPayload.Type<AddTFBossBarPacket> = CustomPacketPayload.Type(prefix("add_tf_boss_bar"))
			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, AddTFBossBarPacket> = CustomPacketPayload.codec(AddTFBossBarPacket::write, ::AddTFBossBarPacket)

			fun handle(
				packet: AddTFBossBarPacket,
				ctx: ClientPlayNetworking.Context
			) {
				val minecraft = Minecraft.getInstance()
				minecraft.gui.bossOverlay.events[packet.id] = ClientTFBossBar(packet.id, packet.name, packet.progress, packet.color, packet.overlay, packet.darkenScreen, packet.playMusic, packet.createWorldFog)
			}
		}

		private var name: Component
		private var progress: Float
		private var color: Int
		private var overlay: BossEvent.BossBarOverlay
		private var darkenScreen: Boolean
		private var playMusic: Boolean
		private var createWorldFog: Boolean

		constructor(bossEvent: ServerTFBossBar) : super(bossEvent) {
			this.name = bossEvent.getName()
			this.progress = bossEvent.getProgress()
			this.color = bossEvent.barColor
			this.overlay = bossEvent.getOverlay()
			this.darkenScreen = bossEvent.shouldDarkenScreen()
			this.playMusic = bossEvent.shouldPlayBossMusic()
			this.createWorldFog = bossEvent.shouldCreateWorldFog()
		}

		constructor(buf: RegistryFriendlyByteBuf) : super(buf) {
			this.name = ComponentSerialization.STREAM_CODEC.decode(buf)
			this.progress = buf.readFloat()
			this.color = buf.readInt()
			this.overlay = buf.readEnum(BossEvent.BossBarOverlay::class.java)
			val i = buf.readUnsignedByte().toInt()
			this.darkenScreen = (i and 1) > 0
			this.playMusic = (i and 2) > 0
			this.createWorldFog = (i and 4) > 0
		}

		override fun write(buf: RegistryFriendlyByteBuf) {
			super.write(buf)
			ComponentSerialization.STREAM_CODEC.encode(buf, this.name)
			buf.writeFloat(this.progress)
			buf.writeInt(this.color)
			buf.writeEnum(this.overlay)
			buf.writeByte(ClientboundBossEventPacket.encodeProperties(this.darkenScreen, this.playMusic, this.createWorldFog))
		}

		override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
	}

	class UpdateTFBossBarStylePacket : TFBossBarPacket {
		companion object {
			val TYPE: CustomPacketPayload.Type<UpdateTFBossBarStylePacket> = CustomPacketPayload.Type(prefix("update_tf_boss_bar_style"))
			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, UpdateTFBossBarStylePacket> = CustomPacketPayload.codec(UpdateTFBossBarStylePacket::write, ::UpdateTFBossBarStylePacket)

			fun handle(
				packet: UpdateTFBossBarStylePacket,
				ctx: ClientPlayNetworking.Context
			) {
				val minecraft = Minecraft.getInstance()
				val bossEvent = minecraft.gui.bossOverlay.events[packet.id]
				if (bossEvent is ClientTFBossBar) {
					bossEvent.setBarColor(packet.color)
					bossEvent.setOverlay(packet.overlay)
					if (!packet.allowLerp) bossEvent.setSetTime(bossEvent.getSetTime() - 200L) // Boss bars lerp over 100 milliseconds, we sometimes don't want that
				}
			}
		}

		private var color: Int
		private var overlay: BossEvent.BossBarOverlay
		private var allowLerp: Boolean

		constructor(bossEvent: ServerTFBossBar, allowLerp: Boolean) : super(bossEvent) {
			this.color = bossEvent.barColor
			this.overlay = bossEvent.getOverlay()
			this.allowLerp = allowLerp
		}

		constructor(buf: RegistryFriendlyByteBuf) : super(buf) {
			this.color = buf.readInt()
			this.overlay = buf.readEnum(BossEvent.BossBarOverlay::class.java)
			this.allowLerp = buf.readBoolean()
		}

		override fun write(buf: RegistryFriendlyByteBuf) {
			super.write(buf)
			buf.writeInt(this.color)
			buf.writeEnum(this.overlay)
			buf.writeBoolean(this.allowLerp)
		}

		override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
	}
}