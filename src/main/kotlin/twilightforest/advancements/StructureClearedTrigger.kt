package twilightforest.advancements

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.criterion.ContextAwarePredicate
import net.minecraft.advancements.criterion.EntityPredicate
import net.minecraft.advancements.criterion.SimpleCriterionTrigger
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.levelgen.structure.Structure
import twilightforest.init.TFAdvancements
import java.util.*
import java.util.function.Function

class StructureClearedTrigger : SimpleCriterionTrigger<StructureClearedTrigger.TriggerInstance>() {
	override fun codec(): Codec<TriggerInstance> = TriggerInstance.CODEC

	fun trigger(player: ServerPlayer, structure: ResourceKey<Structure>) = this.trigger(player) { instance: TriggerInstance -> instance.test(structure) }

	data class TriggerInstance(
		val playerPredicate: Optional<ContextAwarePredicate>,
		val structure: ResourceKey<Structure>
	) : SimpleInstance {
		companion object {
			val CODEC: Codec<TriggerInstance> = RecordCodecBuilder.create(Function { instance: RecordCodecBuilder.Instance<TriggerInstance> -> instance.group(
					EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
					ResourceKey.codec(Registries.STRUCTURE).fieldOf("structure").forGetter(TriggerInstance::structure)
				).apply(instance) { player: Optional<ContextAwarePredicate>, structure: ResourceKey<Structure> -> TriggerInstance(player, structure) }
			})

			fun clearedStructure(structure: ResourceKey<Structure>): Criterion<TriggerInstance> = TFAdvancements.STRUCTURE_CLEARED.createCriterion(TriggerInstance(Optional.empty<ContextAwarePredicate>(), structure))
		}

		override fun player(): Optional<ContextAwarePredicate> = playerPredicate

		fun test(structure: ResourceKey<Structure>): Boolean = this.structure == structure
	}
}