package nadiendev.ultimatefoods.registry;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, UltimateFoodsCore.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ULTIMATE_FOODS_TAB =
            CREATIVE_MODE_TABS.register("ultimate_foods_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.ultimatefoods"))
                    .icon(() -> new ItemStack(ItemsAdds.MONSTER.get()))
                    .displayItems((parameters, output) -> {
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
                        output.accept(ItemsAdds.RAW_HAMBURGUER_MEAT.get());
                        output.accept(ItemsAdds.COOKED_HAMBURGUER_MEAT.get());
                        output.accept(ItemsAdds.BURGER.get());
                    })
                    .build()
            );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
