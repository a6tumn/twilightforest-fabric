package twilightforest.potions

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import twilightforest.TFCommon
import kotlin.math.min

class FrostedEffect : MobEffect(MobEffectCategory.HARMFUL, 0x56CBFD) {
	companion object {
		val MOVEMENT_SPEED_MODIFIER = TFCommon.prefix("frosted_slowdown")
		const val FROST_MULTIPLIER = -0.15
	}

	init {
		addAttributeModifier(Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED_MODIFIER, FROST_MULTIPLIER, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
	}

	override fun applyEffectTick(
		serverLevel: ServerLevel,
		mob: LivingEntity,
		amplification: Int
	): Boolean {
		mob.setIsInPowderSnow(true)
		if (amplification > 0 && mob.canFreeze()) {
			mob.ticksFrozen = min(mob.ticksRequiredToFreeze, mob.ticksFrozen + amplification)
		}
		return true
	}

	override fun shouldApplyEffectTickThisTick(tickCount: Int, amplification: Int): Boolean = true
}