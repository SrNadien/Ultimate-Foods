package nadiendev.ultimatefoods.items.armor;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Clase para registrar todas las piezas de armadura del mod
 * Actualmente incluye: Armadura de Nadienite (set completo)
 */
public class ArmorAdds {
    public static final DeferredRegister<Item> ARMOR_ITEMS =
            DeferredRegister.create(BuiltInRegistries.ITEM, UltimateFoodsCore.MOD_ID);

    // ========== ARMADURA NADIENITE ==========

    /**
     * CASCO DE NADIENITE
     * Efectos individuales:
     * - Visión Nocturna
     * - Respiración Acuática IV
     */
    public static final DeferredHolder<Item, Item> NADIENITE_HELMET = ARMOR_ITEMS.register(
            "nadienite_helmet",
            () -> new NadieniteArmorItem(
                    NadieniteArmorMaterial.NADIENITE,
                    ArmorItem.Type.HELMET,
                    new Item.Properties()
                            .stacksTo(1)
                            .fireResistant()
                            .rarity(Rarity.EPIC)
            )
    );

    /**
     * PECHERA DE NADIENITE
     * Efectos individuales:
     * - Vuelo (modo creativo)
     * - Regeneración VI
     */
    public static final DeferredHolder<Item, Item> NADIENITE_CHESTPLATE = ARMOR_ITEMS.register(
            "nadienite_chestplate",
            () -> new NadieniteArmorItem(
                    NadieniteArmorMaterial.NADIENITE,
                    ArmorItem.Type.CHESTPLATE,
                    new Item.Properties()
                            .stacksTo(1)
                            .fireResistant()
                            .rarity(Rarity.EPIC)
            )
    );

    /**
     * PANTALONES DE NADIENITE
     * Efectos individuales:
     * - Invisibilidad
     * - Health Boost VI (+12 corazones)
     */
    public static final DeferredHolder<Item, Item> NADIENITE_LEGGINGS = ARMOR_ITEMS.register(
            "nadienite_leggings",
            () -> new NadieniteArmorItem(
                    NadieniteArmorMaterial.NADIENITE,
                    ArmorItem.Type.LEGGINGS,
                    new Item.Properties()
                            .stacksTo(1)
                            .fireResistant()
                            .rarity(Rarity.EPIC)
            )
    );

    /**
     * BOTAS DE NADIENITE
     * Efectos individuales:
     * - Velocidad VI
     * - Salto VI
     */
    public static final DeferredHolder<Item, Item> NADIENITE_BOOTS = ARMOR_ITEMS.register(
            "nadienite_boots",
            () -> new NadieniteArmorItem(
                    NadieniteArmorMaterial.NADIENITE,
                    ArmorItem.Type.BOOTS,
                    new Item.Properties()
                            .stacksTo(1)
                            .fireResistant()
                            .rarity(Rarity.EPIC)
            )
    );

    /**
     * Registra todos los items de armadura en el event bus
     */
    public static void register(IEventBus eventBus) {
        ARMOR_ITEMS.register(eventBus);
    }
}