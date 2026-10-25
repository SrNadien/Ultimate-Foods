package nadiendev.ultimatefoods.argentina.mate;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.data.event.GatherDataEvent;

/**
 * Modulo del mate: arbol de yerba, yerbera, pava, mate, bombilla, el ritual de
 * cebar, los cuatro efectos y el aldeano. Dueño: agente A (paquetes
 * argentina.mate y argentina.aldeano).
 */
public final class MateModule {

    private MateModule() {
    }

    public static void register(IEventBus modEventBus) {
    }

    public static void displayItems(CreativeModeTab.Output output) {
    }

    public static ItemStack tabIcon() {
        return new ItemStack(Items.BOWL);
    }

    public static void gatherData(GatherDataEvent event) {
    }
}
