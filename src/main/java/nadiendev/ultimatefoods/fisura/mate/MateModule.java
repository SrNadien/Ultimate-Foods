package nadiendev.ultimatefoods.fisura.mate;

import nadiendev.ultimatefoods.fisura.aldeano.MateVillagers;
import nadiendev.ultimatefoods.fisura.puesto.PuestoRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;

/**
 * Modulo del mate: arbol de yerba, yerbera, pava, mate, bombilla, el ritual de
 * cebar, los cuatro efectos, el aldeano y el mate puesto. Dueño: agente A (paquetes
 * fisura.mate, fisura.aldeano y fisura.puesto).
 *
 * <p>En 26.1 lo que se composta ya no se carga por codigo: va en el data map
 * {@code data/neoforge/data_maps/item/compostables.json} (hoja, brote, hojas, hoja seca y yuyos).
 * Los tradeos del yerbatero tambien son datos ({@code trade_set} y {@code villager_trade}).</p>
 */
public final class MateModule {

    private MateModule() {
    }

    public static void register(IEventBus modEventBus) {
        MateComponents.COMPONENTS.register(modEventBus);
        MateBlocks.BLOCKS.register(modEventBus);
        MateItems.ITEMS.register(modEventBus);
        MateBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        MateEffects.EFFECTS.register(modEventBus);
        MateVillagers.register(modEventBus);
        // El mate puesto es parte del modulo del mate (en 1.21.1 lo registraba Fisura).
        PuestoRegistry.register(modEventBus);
    }

    /** Orden de la pestaña: yerba y arbol, yerbera, mate y pava en sus estados, bombilla y yuyos. */
    public static void displayItems(CreativeModeTab.Output output) {
        output.accept(MateItems.YERBA_MATE_LOG.get());
        output.accept(MateItems.YERBA_MATE_PLANKS.get());
        output.accept(MateItems.YERBA_MATE_LEAVES.get());
        output.accept(MateItems.YERBA_MATE_SAPLING.get());
        output.accept(MateItems.YERBA_MATE_LEAF.get());
        output.accept(MateItems.YERBA_MATE_LEAF_DRIED.get());
        output.accept(MateItems.YERBA_MATE.get());
        output.accept(MateItems.YERBA_VERDEFLOR.get());

        output.accept(MateItems.YERBERA.get());
        output.accept(MateItems.AZUCARERA.get());

        output.accept(MateItems.MATE.get());
        output.accept(MateItems.MATE_CON_YERBA.get());
        output.accept(MateItems.MATE_AMARGO.get());
        output.accept(MateItems.MATE_DULCE.get());
        output.accept(MateItems.MATE_DE_LECHE.get());
        output.accept(MateItems.MATE_CON_YUYOS.get());
        output.accept(MateItems.MATE_LAVADO.get());

        output.accept(MateItems.PAVA.get());
        output.accept(MateItems.PAVA_CON_AGUA.get());
        output.accept(MateItems.PAVA_CALIENTE.get());

        output.accept(MateItems.BOMBILLA.get());
        output.accept(MateItems.YUYOS.get());
    }

    public static ItemStack tabIcon() {
        return new ItemStack(MateItems.MATE.get());
    }
}
