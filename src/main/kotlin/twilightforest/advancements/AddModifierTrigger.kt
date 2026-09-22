package twilightforest.advancements

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.criterion.ContextAwarePredicate
import net.minecraft.advancements.criterion.EntityPredicate
import net.minecraft.advancements.criterion.SimpleCriterionTrigger
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerPlayer
import twilightforest.init.TFAdvancements
import java.util.Optional

class AddModifierTrigger : SimpleCriterionTrigger<AddModifierTrigger.TriggerInstance>() {
	override fun codec(): Codec<TriggerInstance> = TriggerInstance.CODEC

	fun trigger(player: ServerPlayer, modifier: Identifier) = trigger(player) { instance -> instance.test(modifier) }

	data class TriggerInstance(
		val playerPredicate: Optional<ContextAwarePredicate>,
		val modifier: Optional<Identifier>
	) : SimpleInstance {
		companion object {
			val CODEC: Codec<TriggerInstance> = RecordCodecBuilder.create { instance ->
				instance.group(
					EntityPredicate.ADVANCEMENT_CODEC
						.optionalFieldOf("player")
						.forGetter(TriggerInstance::playerPredicate),
					Identifier.CODEC
						.optionalFieldOf("modifier")
						.forGetter(TriggerInstance::modifier)
				).apply(instance, ::TriggerInstance)
			}

			fun addedAnyModifier(): Criterion<TriggerInstance> = TFAdvancements.ADD_MODIFIER.createCriterion(TriggerInstance(Optional.empty(), Optional.empty()))
			fun addedModifier(modifier: Identifier): Criterion<TriggerInstance> = TFAdvancements.ADD_MODIFIER.createCriterion(TriggerInstance(Optional.empty(), Optional.of(modifier)))
		}

		override fun player(): Optional<ContextAwarePredicate> = playerPredicate

		fun test(modifier: Identifier): Boolean = this.modifier.isEmpty || this.modifier.get() == modifier
	}
}