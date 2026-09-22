package twilightforest.enchantment

import carminite.events.neoforge.BreakBlockEvent
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryCodecs
import net.minecraft.core.registries.Registries
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.enchantment.EnchantedItemInUse
import net.minecraft.world.item.enchantment.LevelBasedValue
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect
import net.minecraft.world.level.block.Block
import net.minecraft.world.phys.Vec3
import twilightforest.entity.projectile.ChainBlock
import twilightforest.init.TFDataAttachments
import java.util.*
import java.util.function.Function
import kotlin.math.roundToInt

data class SmashBlocksEffect(
	val maxSmash: LevelBasedValue,
	val radius: LevelBasedValue,
	val immuneBlocks: Optional<HolderSet<Block>>,
	val vulnerableBlocks: Optional<HolderSet<Block>>,
	val smashSound: Optional<Holder<SoundEvent>>
) : EnchantmentEntityEffect {
	companion object {
		val CODEC: MapCodec<SmashBlocksEffect> = RecordCodecBuilder.mapCodec(Function { instance: RecordCodecBuilder.Instance<SmashBlocksEffect> -> instance.group(
				LevelBasedValue.CODEC.fieldOf("max_smash").forGetter(SmashBlocksEffect::maxSmash),
				LevelBasedValue.CODEC.fieldOf("radius").forGetter(SmashBlocksEffect::radius),
				RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("immune_blocks").forGetter(SmashBlocksEffect::immuneBlocks),
				RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("vulnerable_blocks").forGetter(SmashBlocksEffect::vulnerableBlocks),
				SoundEvent.CODEC.optionalFieldOf("smash_sound").forGetter(SmashBlocksEffect::smashSound)
			).apply(instance) { maxSmash: LevelBasedValue, radius: LevelBasedValue, immuneBlocks: Optional<HolderSet<Block>>, vulnerableBlocks: Optional<HolderSet<Block>>, smashSound: Optional<Holder<SoundEvent>> -> SmashBlocksEffect(maxSmash, radius, immuneBlocks, vulnerableBlocks, smashSound) }
		})
	}

	override fun apply(level: ServerLevel, enchantmentLevel: Int, item: EnchantedItemInUse, entity: Entity, position: Vec3) {
		if (item.owner() is ServerPlayer) {
			val player = item.owner() as ServerPlayer
			var blocksSmashed = entity.getAttached(TFDataAttachments.SMASH_BLOCKS)!!.blocksSmashed
			val maxSmash = this.maxSmash.calculate(enchantmentLevel).roundToInt()
			if (blocksSmashed >= maxSmash) return
			val start = BlockPos.containing(position)
			val radius = this.radius.calculate(enchantmentLevel).roundToInt()

			for (pos in BlockPos.betweenClosed(start.offset(-radius, 0, -radius), start.offset(radius, 0, radius))) {
				if (blocksSmashed >= maxSmash) break
				val state = level.getBlockState(pos)
				if (!state.isAir) {
					if (this.immuneBlocks.isPresent && this.immuneBlocks.get().contains(state.typeHolder())) continue
					if (ChainBlock.canBreakBlockAt(level, pos, state, item.itemStack, player.gameMode.gameModeForPlayer.isBlockPlacingRestricted) && state.`carminite$canEntityDestroy`(level, pos, player)) {
						if (!BreakBlockEvent(level, pos, state, player).post().isCanceled) {
							level.destroyBlock(pos, false)
							if (!player.isCreative) state.block.playerDestroy(level, player, pos, state, level.getBlockEntity(pos), item.itemStack)
							if (this.smashSound.isPresent) {
								level.playSound(null, pos, this.smashSound.get().value(), SoundSource.BLOCKS, 1.0F, 1.0F)
							}
							blocksSmashed++
						}
					}
				}
			}

			entity.getAttached(TFDataAttachments.SMASH_BLOCKS)!!.blocksSmashed = blocksSmashed
		}
	}

	override fun codec(): MapCodec<out EnchantmentEntityEffect> = CODEC
}