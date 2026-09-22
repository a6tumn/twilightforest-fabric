package twilightforest.advancements

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.criterion.ContextAwarePredicate
import net.minecraft.advancements.criterion.EntityPredicate
import net.minecraft.advancements.criterion.ItemPredicate
import net.minecraft.advancements.criterion.SimpleCriterionTrigger
import net.minecraft.core.HolderGetter
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import twilightforest.init.TFAdvancements
import java.util.*
import java.util.function.Function

class UncraftItemTrigger : SimpleCriterionTrigger<UncraftItemTrigger.TriggerInstance>() {
	override fun codec(): Codec<TriggerInstance> = TriggerInstance.CODEC

	fun trigger(player: ServerPlayer, stack: ItemStack) = this.trigger(player) { instance: TriggerInstance -> instance.matches(stack) }

	data class TriggerInstance(
		val playerPredicate: Optional<ContextAwarePredicate>,
		val item: Optional<ItemPredicate>
	) : SimpleInstance {
		companion object {
			val CODEC: Codec<TriggerInstance> = RecordCodecBuilder.create(Function { instance: RecordCodecBuilder.Instance<TriggerInstance> -> instance.group(
					EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
					ItemPredicate.CODEC.optionalFieldOf("item").forGetter(TriggerInstance::item)
				).apply(instance) { player: Optional<ContextAwarePredicate>, item: Optional<ItemPredicate> -> TriggerInstance(player, item) }
			})

			fun uncraftedItem(predicate: ItemPredicate): Criterion<TriggerInstance> = TFAdvancements.UNCRAFT_ITEM.createCriterion(TriggerInstance(Optional.empty<ContextAwarePredicate>(), Optional.of<ItemPredicate>(predicate)))
			fun uncraftedItem(getter: HolderGetter<Item>, item: ItemLike): Criterion<TriggerInstance> = uncraftedItem(ItemPredicate.Builder.item().of(getter, item).build())
		}

		override fun player(): Optional<ContextAwarePredicate> = playerPredicate

		fun matches(item: ItemStack): Boolean = this.item.isEmpty || this.item.get().test(item)
	}
}