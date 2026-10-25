package nadiendev.ultimatefoods.argentina.cacerola;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;

/**
 * Jade (opcional): debajo del nombre de la Cacerola Essen muestra el estado de la comida. El avance se
 * calcula en el servidor, porque el cliente solo se entera cuando empieza y cuando termina.
 */
@WailaPlugin
public class CacerolaJade implements IWailaPlugin {

    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "cacerola");
    private static final String CLAVE = "estado";

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(Servidor.INSTANCE, CacerolaBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(Cliente.INSTANCE, CacerolaBlock.class);
    }

    enum Servidor implements IServerDataProvider<BlockAccessor> {
        INSTANCE;

        @Override
        public void appendServerData(CompoundTag data, BlockAccessor accessor) {
            if (accessor.getBlockEntity() instanceof CacerolaBlockEntity pot) {
                data.putString(CLAVE, Component.Serializer.toJson(pot.statusMessage(accessor.getLevel()),
                        accessor.getLevel().registryAccess()));
            }
        }

        @Override
        public ResourceLocation getUid() {
            return UID;
        }
    }

    enum Cliente implements IBlockComponentProvider {
        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            CompoundTag data = accessor.getServerData();
            if (data.contains(CLAVE)) {
                Component estado = Component.Serializer.fromJson(data.getString(CLAVE), accessor.getLevel().registryAccess());
                if (estado != null) {
                    tooltip.add(estado);
                }
            }
        }

        @Override
        public ResourceLocation getUid() {
            return UID;
        }
    }
}
