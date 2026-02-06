package nadiendev.ultimatefoods.items.tools;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registro de todas las herramientas y armas de Nadienite
 */
public class ToolsAdds {
    
    public static final DeferredRegister<Item> TOOL_ITEMS =
            DeferredRegister.create(BuiltInRegistries.ITEM, UltimateFoodsCore.MOD_ID);

    // ========== ESPADA DE NADIENITE ==========
    /**
     * Espada de Nadienite
     * - 105 de daño
     * - Sin cooldown
     * - Irrompible
     */
    public static final DeferredHolder<Item, Item> NADIENITE_SWORD = TOOL_ITEMS.register(
            "nadienite_sword",
            () -> new NadieniteSwordItem(
                    NadieniteToolMaterial.NADIENITE,
                    new Item.Properties()
                            .stacksTo(1)
                            .fireResistant()
                            .rarity(Rarity.EPIC)
            )
    );

   
       //chancleta
       public static final DeferredHolder<Item, Item> CHANCLA = TOOL_ITEMS.register(
            "chancla",
            () -> new ChanclaItem(
                    NadieniteToolMaterial.NADIENITE,
                    new Item.Properties()
                            .stacksTo(1)
                            .fireResistant()
                            .rarity(Rarity.EPIC)
            )
    );

    /**
     * Registra todas las herramientas en el event bus
     */
    public static void register(IEventBus eventBus) {
        TOOL_ITEMS.register(eventBus);
    }
}