package nadiendev.ultimatefoods.argentina.comida;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

/**
 * Comida o bebida que viene en un recipiente (plato hondo, botella): al terminar de consumirla
 * devuelve el recipiente vacio, como el guiso de hongos de vanilla.
 */
public class ContainerFoodItem extends Item {

    private final ItemLike container;
    private final UseAnim animation;

    public ContainerFoodItem(Properties properties, ItemLike container, UseAnim animation) {
        super(properties);
        this.container = container;
        this.animation = animation;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return animation;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (entity instanceof Player player && player.hasInfiniteMaterials()) {
            return result;
        }
        ItemStack leftover = new ItemStack(container);
        if (result.isEmpty()) {
            return leftover;
        }
        if (entity instanceof Player player && !player.getInventory().add(leftover)) {
            player.drop(leftover, false);
        }
        return result;
    }
}
