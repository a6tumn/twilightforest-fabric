package twilightforest.enchantment

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.EntityTypeTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.enchantment.EnchantedItemInUse
import net.minecraft.world.item.enchantment.LevelBasedValue
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect
import net.minecraft.world.phys.Vec3
import twilightforest.init.TFMobEffects
import java.util.function.Function
import kotlin.math.max
import kotlin.math.roundToInt

data class ApplyFrostedEffect(
	val duration: LevelBasedValue,
	val amplifier: LevelBasedValue
) : EnchantmentEntityEffect {
	companion object {
		@JvmField
		val CODEC: MapCodec<ApplyFrostedEffect> = RecordCodecBuilder.mapCodec(Function { instance: RecordCodecBuilder.Instance<ApplyFrostedEffect> -> instance.group(
				LevelBasedValue.CODEC.fieldOf("duration").forGetter(ApplyFrostedEffect::duration),
				LevelBasedValue.CODEC.fieldOf("amplifier").forGetter(ApplyFrostedEffect::amplifier)
			).apply(instance) { duration: LevelBasedValue, amplifier: LevelBasedValue -> ApplyFrostedEffect(duration, amplifier) }
		})

		@JvmStatic
		fun doChillAuraEffect(victim: LivingEntity, duration: Int, amplifier: Int, shouldHit: Boolean) {
			if (shouldHit && !victim.`is`(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)) {
				if (!victim.getItemBySlot(EquipmentSlot.HEAD).`is`(ItemTags.FREEZE_IMMUNE_WEARABLES)
					&& !victim.getItemBySlot(EquipmentSlot.CHEST).`is`(ItemTags.FREEZE_IMMUNE_WEARABLES)
					&& !victim.getItemBySlot(EquipmentSlot.LEGS).`is`(ItemTags.FREEZE_IMMUNE_WEARABLES)
					&& !victim.getItemBySlot(EquipmentSlot.FEET).`is`(ItemTags.FREEZE_IMMUNE_WEARABLES)) {
					if (victim !is Player || !victim.isCreative) {
						victim.addEffect(MobEffectInstance(TFMobEffects.FROSTY, duration, amplifier))
					}
				}
			}
		}
	}

	override fun apply(serverLevel: ServerLevel, enchantmentLevel: Int, item: EnchantedItemInUse, entity: Entity, position: Vec3) {
		if (entity is LivingEntity) {
			val duration = this.duration.calculate(enchantmentLevel).roundToInt()
			val amplifier = max(0, this.amplifier.calculate(enchantmentLevel).roundToInt())
			doChillAuraEffect(entity, duration, amplifier, true)
		}
	}

	override fun codec(): MapCodec<out EnchantmentEntityEffect> = CODEC
}