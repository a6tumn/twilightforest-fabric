package twilightforest.enchantment

import com.mojang.serialization.MapCodec
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.enchantment.EnchantedItemInUse
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect
import net.minecraft.world.phys.Vec3
import twilightforest.init.TFItems
import twilightforest.inventory.InventoryUtil
import twilightforest.item.recipe.ScepterRepairRecipe

class RechargeScepterEffect : EnchantmentEntityEffect{
	companion object {
		@JvmField
		val CODEC: MapCodec<RechargeScepterEffect> = MapCodec.unit { RechargeScepterEffect() }

		@JvmStatic
		fun applyRecharge(level: ServerLevel, item: ItemStack, entity: Entity) {
			if (entity is Player && item.damageValue == item.maxDamage) {
				val recipes: List<ScepterRepairRecipe> = level.recipeAccess().recipes.values().stream().filter { holder -> holder.value() is ScepterRepairRecipe }.map { holder -> holder.value() }.map { value -> value as ScepterRepairRecipe }.toList()
				val slotsToConsume: MutableList<Int> = ArrayList()
				for (recipe in recipes) {
					if (item.`is`(recipe.scepter)) {
						val ingredientCopy = ArrayList(recipe.placementInfo().ingredients())
						scepterItemsCheck@ for (i in 0 until Inventory.INVENTORY_SIZE) {
							val stack = entity.inventory.getItem(i)
							if (stack.isEmpty) continue
							if (stack.`is`(TFItems.EXANIMATE_ESSENCE)) {
								stack.shrink(1)
								item.damageValue = 0
								return
							}
							for (ingredient in recipe.placementInfo().ingredients()) {
								if (ingredientCopy.contains(ingredient) && ingredient.test(stack)) {
									ingredientCopy.remove(ingredient)
									slotsToConsume.add(i)
									if (ingredientCopy.isEmpty()) break@scepterItemsCheck
								}
							}
						}

						if (slotsToConsume.size == recipe.placementInfo().ingredients().size) {
							for (slot in slotsToConsume) {
								val stack = entity.inventory.nonEquipmentItems[slot]
								stack.shrink(1)
								val remainder: ItemStackTemplate? = stack.craftingRemainder
								if (remainder != null) {
									InventoryUtil.giveItemToPlayer(entity, remainder.create())
								}
							}
							item.damageValue -= recipe.repairDurability
						}
					}
				}
			}
		}
	}

	override fun apply(serverLevel: ServerLevel, enchantmentLevel: Int, item: EnchantedItemInUse, entity: Entity, position: Vec3) = applyRecharge(serverLevel, item.itemStack, entity)

	override fun codec(): MapCodec<out EnchantmentEntityEffect> = CODEC
}