package twilightforest.advancements

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.criterion.ContextAwarePredicate
import net.minecraft.advancements.criterion.EntityPredicate
import net.minecraft.advancements.criterion.SimpleCriterionTrigger
import net.minecraft.server.level.ServerPlayer
import twilightforest.init.TFAdvancements
import java.util.*
import java.util.function.Function

class SimpleAdvancementTrigger : SimpleCriterionTrigger<SimpleAdvancementTrigger.TriggerInstance>() {
	override fun codec(): Codec<TriggerInstance> = TriggerInstance.CODEC

	fun trigger(player: ServerPlayer) = this.trigger(player) { true }

	data class TriggerInstance(val playerPredicate: Optional<ContextAwarePredicate>) : SimpleInstance {
		companion object {
			val CODEC: Codec<TriggerInstance> = RecordCodecBuilder.create(Function { instance: RecordCodecBuilder.Instance<TriggerInstance> ->
				instance.group(EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player))
					.apply(instance) { player: Optional<ContextAwarePredicate> -> TriggerInstance(player) }
			})

			fun makeTFPortal(): Criterion<TriggerInstance> = TFAdvancements.MADE_TF_PORTAL.createCriterion(TriggerInstance(Optional.empty<ContextAwarePredicate>()))
			fun eatHydraChop(): Criterion<TriggerInstance> = TFAdvancements.CONSUME_HYDRA_CHOP.createCriterion(TriggerInstance(Optional.empty<ContextAwarePredicate>()))
			fun completeQuestRam(): Criterion<TriggerInstance> = TFAdvancements.QUEST_RAM_COMPLETED.createCriterion(TriggerInstance(Optional.empty<ContextAwarePredicate>()))
			fun activateGhastTrap(): Criterion<TriggerInstance> = TFAdvancements.ACTIVATED_GHAST_TRAP.createCriterion(TriggerInstance(Optional.empty<ContextAwarePredicate>()))
			fun killAllPhantoms(): Criterion<TriggerInstance> = TFAdvancements.KILL_ALL_PHANTOMS.createCriterion(TriggerInstance(Optional.empty<ContextAwarePredicate>()))
			fun activatedPedestal(): Criterion<TriggerInstance> = TFAdvancements.PLACED_TROPHY_ON_PEDESTAL.createCriterion(TriggerInstance(Optional.empty<ContextAwarePredicate>()))
			fun brokenSword(): Criterion<TriggerInstance> = TFAdvancements.BROKE_GLASS_SWORD.createCriterion(TriggerInstance(Optional.empty<ContextAwarePredicate>()))
		}

		override fun player(): Optional<ContextAwarePredicate> = playerPredicate
	}
}