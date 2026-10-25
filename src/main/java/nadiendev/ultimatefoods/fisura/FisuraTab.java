package nadiendev.ultimatefoods.fisura;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.fisura.comida.ComidaModule;
import nadiendev.ultimatefoods.fisura.homenaje.HomenajeModule;
import nadiendev.ultimatefoods.fisura.mate.MateModule;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Pestaña creativa propia, separada de la de Ultimate Foods. */
public final class FisuraTab {

    private static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, UltimateFoodsCore.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> FISURA = TABS.register(
            "fisura_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.ultimatefoods.fisura"))
                    .icon(MateModule::tabIcon)
                    .displayItems((parameters, output) -> {
                        MateModule.displayItems(output);
                        ComidaModule.displayItems(output);
                        HomenajeModule.displayItems(output);
                    })
                    .build()
    );

    private FisuraTab() {
    }

    static void register(IEventBus modEventBus) {
        TABS.register(modEventBus);
    }
}
