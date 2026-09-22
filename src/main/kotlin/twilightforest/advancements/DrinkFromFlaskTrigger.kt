package twilightforest.advancements

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.criterion.ContextAwarePredicate
import net.minecraft.advancements.criterion.EntityPredicate
import net.minecraft.advancements.criterion.MinMaxBounds
import net.minecraft.advancements.criterion.SimpleCriterionTrigger
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.alchemy.Potion
import twilightforest.init.TFAdvancements
import twilightforest.util.HolderMatcher
import java.util.*

class DrinkFromFlaskTrigger : SimpleCriterionTrigger<DrinkFromFlaskTrigger.TriggerInstance>() {
	private val holderMatcher = HolderMatcher.INSTANCE

	override fun codec(): Codec<TriggerInstance> = TriggerInstance.DrinkFromFlaskTriggerInstanceFactory.CODEC

	fun trigger(player: ServerPlayer, doses: Int, seconds: Int, potion: Holder<Potion>) = this.trigger(player) { instance: TriggerInstance -> instance.matches(this, doses, seconds, potion) }

	data class TriggerInstance(
		val playerPredicate: Optional<ContextAwarePredicate>,
		val doses: MinMaxBounds.Ints,
		val seconds: MinMaxBounds.Ints,
		val potion: Holder<Potion>
	) : SimpleInstance {
		override fun player(): Optional<ContextAwarePredicate> = playerPredicate

		fun matches(
			parent: DrinkFromFlaskTrigger,
			doses: Int,
			seconds: Int,
			potion: Holder<Potion>
		): Boolean =
			this.doses.matches(doses) && this.seconds.matches(seconds) && parent.holderMatcher.match(this.potion, potion)

		object DrinkFromFlaskTriggerInstanceFactory {
			val CODEC: Codec<TriggerInstance> = RecordCodecBuilder.create { instance ->
				instance.group(
					EntityPredicate.ADVANCEMENT_CODEC
						.optionalFieldOf("player")
						.forGetter(TriggerInstance::playerPredicate),
					MinMaxBounds.Ints.CODEC
						.optionalFieldOf("doses", MinMaxBounds.Ints.between(0, 4))
						.forGetter(TriggerInstance::doses),
					MinMaxBounds.Ints.CODEC
						.optionalFieldOf("seconds", MinMaxBounds.Ints.exactly(8))
						.forGetter(TriggerInstance::seconds),
					BuiltInRegistries.POTION
						.holderByNameCodec()
						.fieldOf("potion")
						.forGetter(TriggerInstance::potion)
				).apply(instance, ::TriggerInstance)
			}

			fun drankPotion(
				doses: Int,
				seconds: MinMaxBounds.Ints,
				potion: Holder<Potion>
			): Criterion<TriggerInstance> =
				TFAdvancements.DRINK_FROM_FLASK.createCriterion(TriggerInstance(Optional.empty(), MinMaxBounds.Ints.exactly(doses), seconds, potion))

			fun drankPotion(
				doses: MinMaxBounds.Ints,
				seconds: MinMaxBounds.Ints,
				potion: Holder<Potion>
			): Criterion<TriggerInstance> =
				TFAdvancements.DRINK_FROM_FLASK.createCriterion(TriggerInstance(Optional.empty(), doses, seconds, potion))
		}
	}
}