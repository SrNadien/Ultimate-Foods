package nadiendev.ultimatefoods.fisura.aldeano;

import com.google.common.collect.ImmutableSet;
import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.fisura.mate.MateBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** El yerbatero: puesto de trabajo (la yerbera) y profesion de aldeano. */
public final class MateVillagers {

    private static final DeferredRegister<PoiType> POIS =
            DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, UltimateFoodsCore.MOD_ID);
    private static final DeferredRegister<VillagerProfession> PROFESSIONS =
            DeferredRegister.create(Registries.VILLAGER_PROFESSION, UltimateFoodsCore.MOD_ID);

    public static final DeferredHolder<PoiType, PoiType> YERBERA_POI = POIS.register("yerbera",
            () -> new PoiType(ImmutableSet.copyOf(MateBlocks.YERBERA.get().getStateDefinition().getPossibleStates()), 1, 1));

    public static final DeferredHolder<VillagerProfession, VillagerProfession> YERBATERO = PROFESSIONS.register("yerbatero",
            () -> new VillagerProfession(
                    "yerbatero",
                    holder -> holder.is(YERBERA_POI.getKey()),
                    holder -> holder.is(YERBERA_POI.getKey()),
                    ImmutableSet.of(),
                    ImmutableSet.of(),
                    SoundEvents.VILLAGER_WORK_FARMER));

    private MateVillagers() {
    }

    public static void register(IEventBus modEventBus) {
        POIS.register(modEventBus);
        PROFESSIONS.register(modEventBus);
    }
}
