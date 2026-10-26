package nadiendev.ultimatefoods.fisura.cacerola;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;
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
 * <p>
 * El interruptor en la configuracion de Jade usa la clave de idioma
 * {@code config.jade.plugin_ultimatefoods.cacerola} (sale del UID de abajo).
 */
@WailaPlugin
public class CacerolaJade implements IWailaPlugin {

    private static final Identifier UID = Identifier.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "cacerola");
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
                data.put(CLAVE, accessor.encodeAsNbt(ComponentSerialization.STREAM_CODEC,
                        pot.statusMessage(accessor.getLevel())));
            }
        }

        @Override
        public Identifier getUid() {
            return UID;
        }
    }

    enum Cliente implements IBlockComponentProvider {
        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            Tag estado = accessor.getServerData().get(CLAVE);
            if (estado != null) {
                accessor.decodeFromNbt(ComponentSerialization.STREAM_CODEC, estado).ifPresent(tooltip::add);
            }
        }

        @Override
        public Identifier getUid() {
            return UID;
        }
    }
}
