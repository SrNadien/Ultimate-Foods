package nadiendev.ultimatefoods.fisura.aldeano;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.fisura.mate.MateBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * El yerbatero: puesto de trabajo (la yerbera) y profesion de aldeano. En 26.1 los tradeos
 * de cada nivel son datos: la profesion apunta a los trade sets de {@link MateTrades}.
 */
public final class MateVillagers {

    private static final DeferredRegister<PoiType> POIS =
            DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, UltimateFoodsCore.MOD_ID);
    private static final DeferredRegister<VillagerProfession> PROFESSIONS =
            DeferredRegister.create(Registries.VILLAGER_PROFESSION, UltimateFoodsCore.MOD_ID);

    public static final DeferredHolder<PoiType, PoiType> YERBERA_POI = POIS.register("yerbera",
            () -> new PoiType(ImmutableSet.copyOf(MateBlocks.YERBERA.get().getStateDefinition().getPossibleStates()), 1, 1));

    public static final DeferredHolder<VillagerProfession, VillagerProfession> YERBATERO = PROFESSIONS.register("yerbatero",
            () -> new VillagerProfession(
                    // Misma clave de nombre que en 1.21.1, asi sirven los lang que ya hay.
                    Component.translatable("entity.minecraft.villager.ultimatefoods.yerbatero"),
                    holder -> holder.is(YERBERA_POI.getKey()),
                    holder -> holder.is(YERBERA_POI.getKey()),
                    ImmutableSet.of(),
                    ImmutableSet.of(),
                    SoundEvents.VILLAGER_WORK_FARMER,
                    Int2ObjectMap.ofEntries(
                            Int2ObjectMap.entry(1, MateTrades.NIVEL_1),
                            Int2ObjectMap.entry(2, MateTrades.NIVEL_2),
                            Int2ObjectMap.entry(3, MateTrades.NIVEL_3),
                            Int2ObjectMap.entry(4, MateTrades.NIVEL_4),
                            Int2ObjectMap.entry(5, MateTrades.NIVEL_5))));

    private MateVillagers() {
    }

    public static void register(IEventBus modEventBus) {
        POIS.register(modEventBus);
        PROFESSIONS.register(modEventBus);
    }
}
