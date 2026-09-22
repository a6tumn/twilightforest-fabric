package twilightforest.advancements.predicate

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.criterion.SingleComponentItemPredicate
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.core.component.predicates.DataComponentPredicate
import net.minecraft.world.item.component.DyedItemColor

data class ItemColorPredicate(val color: Int) : SingleComponentItemPredicate<DyedItemColor> {
	companion object {
		val CODEC: Codec<ItemColorPredicate> = RecordCodecBuilder.create { instance ->
			instance.group(
				Codec.INT.optionalFieldOf("color", -1)
					.forGetter(ItemColorPredicate::color)
			).apply(instance, ::ItemColorPredicate)
		}

		@JvmField
		val TYPE = DataComponentPredicate.ConcreteType(CODEC)

		fun anyColor(): ItemColorPredicate = ItemColorPredicate(-1)
		fun withColor(color: Int) : ItemColorPredicate = ItemColorPredicate(color)
	}

	override fun matches(value: DyedItemColor): Boolean = this.color == -1 || value.rgb() == this.color
	override fun componentType(): DataComponentType<DyedItemColor> = DataComponents.DYED_COLOR
}