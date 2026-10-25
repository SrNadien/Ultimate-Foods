package nadiendev.ultimatefoods.fisura.aldeano;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.fisura.mate.MateItems;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.BasicItemListing;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import java.util.List;

/**
 * Tradeos del yerbatero, por nivel. Solo usa items de este modulo (lo de comidas lo une
 * el coordinador despues).
 */
@EventBusSubscriber(modid = UltimateFoodsCore.MOD_ID)
public final class MateTrades {

    private static final float MULT = 0.05F;

    private MateTrades() {
    }

    @SubscribeEvent
    public static void alRegistrarTradeos(VillagerTradesEvent event) {
        if (event.getType() != MateVillagers.YERBATERO.get()) {
            return;
        }
        var trades = event.getTrades();

        // Nivel 1: Novato
        List<VillagerTrades.ItemListing> n1 = trades.get(1);
        n1.add(compra(MateItems.YERBA_MATE_LEAF.get(), 24, 1, 16, 2));
        n1.add(venta(3, MateItems.MATE.get(), 1, 8, 1));
        n1.add(venta(2, MateItems.BOMBILLA.get(), 1, 8, 1));
        n1.add(venta(1, MateItems.YUYOS.get(), 4, 16, 1));

        // Nivel 2: Aprendiz
        List<VillagerTrades.ItemListing> n2 = trades.get(2);
        n2.add(compra(MateItems.YERBA_MATE_LEAF_DRIED.get(), 16, 1, 16, 5));
        n2.add(venta(2, MateItems.YERBA_MATE.get(), 6, 12, 5));
        n2.add(venta(6, MateItems.PAVA.get(), 1, 6, 5));

        // Nivel 3: Oficial
        List<VillagerTrades.ItemListing> n3 = trades.get(3);
        n3.add(compra(MateItems.YUYOS.get(), 16, 1, 12, 10));
        n3.add(venta(4, MateItems.YERBA_VERDEFLOR.get(), 1, 8, 10));
        n3.add(venta(3, MateItems.YERBA_MATE_SAPLING.get(), 1, 6, 10));

        // Nivel 4: Experto
        List<VillagerTrades.ItemListing> n4 = trades.get(4);
        n4.add(compra(MateItems.YERBA_MATE.get(), 12, 2, 12, 15));
        n4.add(venta(8, MateItems.YERBERA.get(), 1, 4, 15));
        n4.add(venta(6, MateItems.AZUCARERA.get(), 1, 4, 15));

        // Nivel 5: Maestro
        List<VillagerTrades.ItemListing> n5 = trades.get(5);
        n5.add(venta(9, MateItems.YERBA_VERDEFLOR.get(), 3, 6, 30));
        n5.add(compra(MateItems.YERBA_VERDEFLOR.get(), 3, 8, 6, 30));
        n5.add(venta(5, MateItems.MATE.get(), 2, 6, 30));
    }

    /** El aldeano vende: {@code esmeraldas} por {@code cantidad} del item. */
    private static VillagerTrades.ItemListing venta(int esmeraldas, ItemLike item, int cantidad, int usos, int xp) {
        return new BasicItemListing(esmeraldas, new ItemStack(item, cantidad), usos, xp, MULT);
    }

    /** El aldeano compra: {@code cantidad} del item a cambio de {@code esmeraldas}. */
    private static VillagerTrades.ItemListing compra(ItemLike item, int cantidad, int esmeraldas, int usos, int xp) {
        return new BasicItemListing(new ItemStack(item, cantidad), new ItemStack(Items.EMERALD, esmeraldas), usos, xp, MULT);
    }
}
