package nadiendev.ultimatefoods.integration.jade;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

/**
 * JadeCompat
 * By NadienDev
 */
@WailaPlugin
public class JadeCompat implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
       
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
  
    }

    public ResourceLocation getUid() {
        return ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "jade_plugin");
    }
}