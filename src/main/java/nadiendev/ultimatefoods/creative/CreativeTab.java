package nadiendev.ultimatefoods.creative;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.avaritia.AvaritiaToolsAdds;
import nadiendev.ultimatefoods.blocks.BlocksAdds;
import nadiendev.ultimatefoods.blocks.NadieniteOreBlock;
import nadiendev.ultimatefoods.items.ItemsAdds;
import nadiendev.ultimatefoods.items.armor.ArmorAdds;
import nadiendev.ultimatefoods.items.tools.ToolsAdds;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * CreativeTab
 * By NadienDev
 */
public class CreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, UltimateFoodsCore.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ULTIMATE_FOODS_TAB =
            CREATIVE_MODE_TABS.register("ultimate_foods_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.ultimatefoods"))
                    .icon(() -> new ItemStack(ToolsAdds.CHANCLA.get()))
                    .displayItems((parameters, output) -> {
                        // ========== COMIDAS ==========
                        output.accept(ItemsAdds.MONSTER.get());
                        output.accept(ItemsAdds.CAJITA_FELIZ.get());
                        output.accept(ItemsAdds.SUPER_ENERGY_DRINK.get());
                        output.accept(ItemsAdds.DORITOS.get());
                        output.accept(ItemsAdds.POOP.get());
                        output.accept(ItemsAdds.SUPERPOOP.get());
                        output.accept(ItemsAdds.CHOCOLATADA.get());
                        output.accept(ItemsAdds.SUPER_CHILE.get());
                        output.accept(ItemsAdds.BAKED_TORTILLA.get());
                        output.accept(ItemsAdds.TORTILLA.get());
                        output.accept(ItemsAdds.TACO.get());

                        // Hamburguesa y sus componentes
                        output.accept(ItemsAdds.RAW_HAMBURGUER_MEAT.get());
                        output.accept(ItemsAdds.COOKED_HAMBURGUER_MEAT.get());
                        output.accept(ItemsAdds.BURGER.get());

                        // ========== UTILITIES ==========
                        output.accept(ItemsAdds.GALACTIC_STAR.get());
                        output.accept(ItemsAdds.PATRICIO.get());
                        output.accept(ItemsAdds.BOB_ESPONJA.get());
                        output.accept(ItemsAdds.SEMILLA.get());
                        output.accept(ItemsAdds.RADIOACTIVE_FUEL.get());
                        output.accept(ItemsAdds.PORTALMINI.get());
                        output.accept(ItemsAdds.RADIOACTIVE_ANTIMATTER.get());
                        output.accept(ItemsAdds.PLANOS.get());
                        output.accept(ItemsAdds.REACTORMINI.get());
                        output.accept(ItemsAdds.SUPER_SEED.get());
                        output.accept(ItemsAdds.GAMEBOY.get());
                        output.accept(ItemsAdds.GALACTIC_GLOBE.get());
                        output.accept(ItemsAdds.DRAGONSOUL.get());
                        output.accept(ItemsAdds.DOLL.get());
                        output.accept(ItemsAdds.CATALIZADOR.get());
                        output.accept(ItemsAdds.CALAVERA.get());
                        output.accept(ItemsAdds.AGUJERONEGRO.get());
                        output.accept(ItemsAdds.ABSOLUTE_REACTION_PLATING.get());
                        output.accept(ItemsAdds.BRUJULAMALDITA.get());
                        output.accept(ItemsAdds.UNIVERSE_SEARCH.get());
                        output.accept(ItemsAdds.TRANSMISOR.get());
                        output.accept(ItemsAdds.DISC_MCCACAO.get());
                        output.accept(ItemsAdds.DISC_AVENGERS.get());

                        // ========== MATERIALES ==========
                        output.accept(ItemsAdds.NADIENITE_INGOT.get());
                        output.accept(ItemsAdds.NADIENITE_NUGGET.get());
                        output.accept(ItemsAdds.STEEL_INGOT.get());

                        // ========== HERRAMIENTAS ESPECIALES ==========
                        output.accept(ItemsAdds.NETHERITE_DAGGER.get());

                        // ========== BLOQUES ==========
                        output.accept(BlocksAdds.STEEL_BLOCK.get());
                        output.accept(BlocksAdds.SEXY_XD.get());
                        output.accept(BlocksAdds.NADIENITE_BLOCK.get());
                        output.accept(BlocksAdds.NETHER_STAR_BLOCK.get());

                        // ========== MINERALES DE NADIENITE ==========
                        output.accept(NadieniteOreBlock.NADIENITE_ORE.get());
                        output.accept(NadieniteOreBlock.DEEPSLATE_NADIENITE_ORE.get());

                        // ========== HERRAMIENTAS DE NADIENITE ==========
                        output.accept(ToolsAdds.NADIENITE_SWORD.get());
                        output.accept(ToolsAdds.NADIENITE_AXE.get());
                        output.accept(ToolsAdds.NADIENITE_PICKAXE.get());
                        output.accept(ToolsAdds.NADIENITE_HOE.get());
                        output.accept(ToolsAdds.NADIENITE_SHOVEL.get());
                        output.accept(ToolsAdds.CHANCLA.get());

                        // ========== ARMADURA DE NADIENITE ==========
                        output.accept(ArmorAdds.NADIENITE_HELMET.get());
                        output.accept(ArmorAdds.NADIENITE_CHESTPLATE.get());
                        output.accept(ArmorAdds.NADIENITE_LEGGINGS.get());
                        output.accept(ArmorAdds.NADIENITE_BOOTS.get());

                        // ========== AVARITIA - INFINITY TIER ==========
                        if (ModList.get().isLoaded("avaritia")) {
                            output.accept(AvaritiaToolsAdds.INFINITY_1_SWORD.get());
                            output.accept(AvaritiaToolsAdds.INFINITY_1_PICKAXE.get());
                            output.accept(AvaritiaToolsAdds.INFINITY_1_HOE.get());
                            output.accept(AvaritiaToolsAdds.INFINITY_SWORD_BALANCED.get());
                        }

                        //fluid buckets
                        output.accept(ItemsAdds.NADIENITE_FLUID_BUCKET.get());
                    })
                    .build()
            );

    /**
     * Registra el DeferredRegister en el event bus
     * IMPORTANTE: Debe llamarse DESPUÉS de registrar los items
     */
    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}