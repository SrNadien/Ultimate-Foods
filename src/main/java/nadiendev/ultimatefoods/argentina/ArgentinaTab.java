package nadiendev.ultimatefoods.argentina;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.argentina.comida.ComidaModule;
import nadiendev.ultimatefoods.argentina.mate.MateModule;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Pestaña creativa propia, separada de la de Ultimate Foods. */
public final class ArgentinaTab {

    private static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, UltimateFoodsCore.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ARGENTINA_DELIGHT = TABS.register(
            "argentina_delight_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.ultimatefoods.argentina_delight"))
                    .icon(MateModule::tabIcon)
                    .displayItems((parameters, output) -> {
                        MateModule.displayItems(output);
                        ComidaModule.displayItems(output);
                    })
                    .build()
    );

    private ArgentinaTab() {
    }

    static void register(IEventBus modEventBus) {
        TABS.register(modEventBus);
    }
}
