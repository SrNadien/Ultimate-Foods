package nadiendev.ultimatefoods.events;

import nadiendev.ultimatefoods.items.ItemsAdds;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * Clic derecho con una Espada de Netherite sobre un Bloque de Netherite
 * → consume la espada y entrega la NETHERITE_DAGGER.
 */
@EventBusSubscriber(modid = nadiendev.ultimatefoods.UltimateFoodsCore.MOD_ID)
public class NetheriteDaggerInteraction {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        // Solo mano principal
        if (event.getHand() != InteractionHand.MAIN_HAND) return;

        Player player = event.getEntity();
        Level level = event.getLevel();
        ItemStack held = player.getItemInHand(InteractionHand.MAIN_HAND);

        // El jugador debe tener una espada de netherite en la mano
        if (!held.is(Items.NETHERITE_SWORD)) return;

        // El bloque sobre el que se hace clic debe ser un bloque de netherite
        if (!level.getBlockState(event.getPos()).is(Blocks.NETHERITE_BLOCK)) return;

        // Solo ejecutar en el servidor
        if (level.isClientSide) return;

        // Consumir la espada
        held.shrink(1);

        // Dar la daga al jugador
        ItemStack dagger = new ItemStack(ItemsAdds.NETHERITE_DAGGER.get());
        if (!player.getInventory().add(dagger)) {
            player.drop(dagger, false);
        }

        event.setCanceled(true);
    }
}