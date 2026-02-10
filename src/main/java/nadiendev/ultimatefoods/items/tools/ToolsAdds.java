package nadiendev.ultimatefoods.items.tools;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.Unbreakable;
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
                            .component(DataComponents.UNBREAKABLE, new Unbreakable(true))   
            )
    );


     // ========== PICO DE NADIENITE ==========
    public static final DeferredHolder<Item, Item> NADIENITE_PICKAXE = TOOL_ITEMS.register(
            "nadienite_pickaxe",
            () -> new NadienitePickaxeItem(
                    NadieniteToolMaterial.NADIENITE,
                    new Item.Properties()
                            .stacksTo(1)
                            .fireResistant()
                            .rarity(Rarity.EPIC)  
                            .component(DataComponents.UNBREAKABLE, new Unbreakable(true))                 
            )
    );

    

      // ========== HACHA DE NADIENITE ==========
    public static final DeferredHolder<Item, Item> NADIENITE_AXE = TOOL_ITEMS.register(
            "nadienite_axe",
            () -> new NadieniteAxeItem(
                    NadieniteToolMaterial.NADIENITE,
                    new Item.Properties()
                            .stacksTo(1)
                            .fireResistant()
                            .rarity(Rarity.EPIC)  
                            .component(DataComponents.UNBREAKABLE, new Unbreakable(true))                          
            )
    );

        // ========== AZADA DE NADIENITE ==========
    public static final DeferredHolder<Item, Item> NADIENITE_HOE = TOOL_ITEMS.register(
            "nadienite_hoe",
            () -> new NadieniteHoeItem(
                    NadieniteToolMaterial.NADIENITE,
                    new Item.Properties()
                            .stacksTo(1)
                            .fireResistant()
                            .rarity(Rarity.EPIC)  
                            .component(DataComponents.UNBREAKABLE, new Unbreakable(true))                         
            )
    );

    // ========== PALA DE NADIENITE ==========
    public static final DeferredHolder<Item, Item> NADIENITE_SHOVEL = TOOL_ITEMS.register(
            "nadienite_shovel",
            () -> new NadieniteShovelItem(
                    NadieniteToolMaterial.NADIENITE,
                    new Item.Properties()
                            .stacksTo(1)
                            .fireResistant()
                            .rarity(Rarity.EPIC) 
                            .component(DataComponents.UNBREAKABLE, new Unbreakable(true))                             
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
                            .component(DataComponents.UNBREAKABLE, new Unbreakable(true))                             
            )
    );

    /**
     * Registra todas las herramientas en el event bus
     */
    public static void register(IEventBus eventBus) {
        TOOL_ITEMS.register(eventBus);
    }
}