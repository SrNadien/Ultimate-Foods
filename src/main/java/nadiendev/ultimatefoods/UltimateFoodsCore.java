package nadiendev.ultimatefoods;

import com.mojang.logging.LogUtils;
import nadiendev.ultimatefoods.datagen.DataGenerators;
import nadiendev.ultimatefoods.registry.CreativeTab;
import nadiendev.ultimatefoods.registry.EffectsAdds;
import nadiendev.ultimatefoods.registry.ItemsAdds;
import nadiendev.ultimatefoods.registry.SonidosReproducibles;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(UltimateFoodsCore.MOD_ID)
public class UltimateFoodsCore {
    public static final String MOD_ID = "ultimatefoods";
    public static final Logger LOGGER = LogUtils.getLogger();

    public UltimateFoodsCore(IEventBus modEventBus) {
        LOGGER.info("Inicializando Ultimate Foods Mod...");

        SonidosReproducibles.register(modEventBus);
        EffectsAdds.register(modEventBus);
        ItemsAdds.register(modEventBus);

        CreativeTab.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(DataGenerators::gatherClientData);
        modEventBus.addListener(DataGenerators::gatherServerData);

        LOGGER.info("Ultimate Foods Mod inicializado correctamente!");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Configuracion comun de Ultimate Foods");
    }
}
