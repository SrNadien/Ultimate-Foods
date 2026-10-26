package nadiendev.ultimatefoods.fisura.aldeano;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.trading.TradeSet;

/**
 * Tradeos del yerbatero, por nivel. Solo usa items de este modulo (lo de comidas lo une
 * el coordinador despues).
 *
 * <p>En 26.1 ya no hay {@code VillagerTradesEvent}: los tradeos son datos. Cada nivel es un
 * trade set ({@code data/ultimatefoods/trade_set/yerbatero/level_N.json}, ofrece 2 al azar como
 * antes) que lista sus tradeos ({@code data/ultimatefoods/villager_trade/yerbatero/N/*.json}).
 * Todos con 0.05 de descuento por reputacion. La tabla, igual que en 1.21.1:</p>
 * <ul>
 * <li>Nivel 1, Novato: compra 24 hojas de yerba por 1 esmeralda (16 usos, 2 xp); vende mate por 3
 * (8, 1), bombilla por 2 (8, 1) y 4 yuyos por 1 (16, 1).</li>
 * <li>Nivel 2, Aprendiz: compra 16 hojas secas por 1 (16, 5); vende 6 de yerba por 2 (12, 5) y la
 * pava por 6 (6, 5).</li>
 * <li>Nivel 3, Oficial: compra 16 yuyos por 1 (12, 10); vende yerba Verdeflor por 4 (8, 10) y el
 * brote por 3 (6, 10).</li>
 * <li>Nivel 4, Experto: compra 12 de yerba por 2 (12, 15); vende la yerbera por 8 (4, 15) y la
 * azucarera por 6 (4, 15).</li>
 * <li>Nivel 5, Maestro: vende 3 Verdeflor por 9 (6, 30); compra 3 Verdeflor por 8 (6, 30); vende
 * 2 mates por 5 (6, 30).</li>
 * </ul>
 */
public final class MateTrades {

    public static final ResourceKey<TradeSet> NIVEL_1 = nivel(1);
    public static final ResourceKey<TradeSet> NIVEL_2 = nivel(2);
    public static final ResourceKey<TradeSet> NIVEL_3 = nivel(3);
    public static final ResourceKey<TradeSet> NIVEL_4 = nivel(4);
    public static final ResourceKey<TradeSet> NIVEL_5 = nivel(5);

    private MateTrades() {
    }

    private static ResourceKey<TradeSet> nivel(int nivel) {
        return ResourceKey.create(Registries.TRADE_SET,
                Identifier.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "yerbatero/level_" + nivel));
    }
}
