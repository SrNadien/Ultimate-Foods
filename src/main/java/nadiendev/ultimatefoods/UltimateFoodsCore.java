package nadiendev.ultimatefoods;

import com.mojang.logging.LogUtils;
import nadiendev.ultimatefoods.fisura.Fisura;
import nadiendev.ultimatefoods.config.ConfigCondition;
import nadiendev.ultimatefoods.config.ModConfigs;
import nadiendev.ultimatefoods.datagen.DataGenerators;

import nadiendev.ultimatefoods.registry.CreativeTab;
import nadiendev.ultimatefoods.registry.EffectsAdds;
import nadiendev.ultimatefoods.registry.ItemsAdds;
import nadiendev.ultimatefoods.registry.SonidosReproducibles;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(UltimateFoodsCore.MOD_ID)
public class UltimateFoodsCore {
    public static final String MOD_ID = "ultimatefoods";
    public static final Logger LOGGER = LogUtils.getLogger();

    public UltimateFoodsCore(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("Inicializando Ultimate Foods Mod...");

        modContainer.registerConfig(ModConfig.Type.COMMON, ModConfigs.SPEC);
        ConfigCondition.register(modEventBus);

        SonidosReproducibles.register(modEventBus);
        EffectsAdds.register(modEventBus);
        ItemsAdds.register(modEventBus);

        CreativeTab.register(modEventBus);
        Fisura.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(DataGenerators::gatherData);

        LOGGER.info("Ultimate Foods Mod inicializado correctamente!");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Configuracion comun de Ultimate Foods");
    }
}
