package twilightforest.advancements

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.criterion.ContextAwarePredicate
import net.minecraft.advancements.criterion.EntityPredicate
import net.minecraft.advancements.criterion.SimpleCriterionTrigger
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import twilightforest.init.TFAdvancements
import java.util.*
import java.util.function.Function

class KillBugTrigger : SimpleCriterionTrigger<KillBugTrigger.TriggerInstance>() {
	override fun codec(): Codec<TriggerInstance> = TriggerInstance.CODEC

	fun trigger(player: ServerPlayer, bug: BlockState) = this.trigger(player) { instance: TriggerInstance -> instance.matches(bug) }

	data class TriggerInstance(
		val playerPredicate: Optional<ContextAwarePredicate>,
		val bugType: Optional<Block>
	) : SimpleInstance {
		companion object {
			val CODEC: Codec<TriggerInstance> = RecordCodecBuilder.create(Function { instance: RecordCodecBuilder.Instance<TriggerInstance> -> instance.group(
					EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
					BuiltInRegistries.BLOCK.byNameCodec().optionalFieldOf("bug").forGetter(TriggerInstance::bugType)
				).apply(instance) { player: Optional<ContextAwarePredicate>, bugType: Optional<Block> -> TriggerInstance(player, bugType) }
			})

			fun killBug(bug: Block): Criterion<TriggerInstance> = TFAdvancements.KILL_BUG.createCriterion(TriggerInstance(Optional.empty<ContextAwarePredicate>(), Optional.of<Block>(bug)))
		}

		override fun player(): Optional<ContextAwarePredicate> = playerPredicate

		fun matches(bug: BlockState): Boolean = this.bugType.isEmpty || bug.`is`(this.bugType.get())
	}
}