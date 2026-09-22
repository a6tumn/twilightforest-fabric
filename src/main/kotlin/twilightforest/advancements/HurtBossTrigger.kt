package twilightforest.advancements

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.criterion.ContextAwarePredicate
import net.minecraft.advancements.criterion.EntityPredicate
import net.minecraft.advancements.criterion.SimpleCriterionTrigger
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.storage.loot.LootContext
import twilightforest.init.TFAdvancements
import java.util.*
import java.util.function.Function

class HurtBossTrigger : SimpleCriterionTrigger<HurtBossTrigger.TriggerInstance>() {
	override fun codec(): Codec<TriggerInstance> = TriggerInstance.CODEC

	fun trigger(player: ServerPlayer, hurt: Entity) {
		val entity = EntityPredicate.createContext(player, hurt)
		this.trigger(player) { instance: TriggerInstance -> instance.matches(entity) }
	}

	data class TriggerInstance(
		val playerPredicate: Optional<ContextAwarePredicate>,
		val hurt: Optional<ContextAwarePredicate>,
	) : SimpleInstance {
		override fun player(): Optional<ContextAwarePredicate> = playerPredicate

		fun matches(hurt: LootContext): Boolean = this.hurt.isEmpty || this.hurt.get().matches(hurt)

		companion object {
			val CODEC: Codec<TriggerInstance> = RecordCodecBuilder.create(Function { instance: RecordCodecBuilder.Instance<TriggerInstance> -> instance.group(
					EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
					EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("hurt_entity").forGetter(TriggerInstance::hurt)
				).apply(instance) { player: Optional<ContextAwarePredicate>, hurt: Optional<ContextAwarePredicate> -> TriggerInstance(player, hurt) }
			})

			fun hurtBoss(hurt: EntityPredicate.Builder): Criterion<TriggerInstance> =
				TFAdvancements.HURT_BOSS.createCriterion(TriggerInstance(Optional.empty<ContextAwarePredicate>(), Optional.of<ContextAwarePredicate>(EntityPredicate.wrap(hurt.build()))))
		}
	}
}