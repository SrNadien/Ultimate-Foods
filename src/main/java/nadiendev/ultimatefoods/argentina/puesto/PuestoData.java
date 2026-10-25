package nadiendev.ultimatefoods.argentina.puesto;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.datagen.providers.NombreProvider;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

/** Datagen del mate puesto: una variante por mate o pava y por orientacion, con el modelo 3D del item. */
public final class PuestoData {

    private PuestoData() {
    }

    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        generator.addProvider(event.includeClient(),
                new NombreProvider("Mate puesto: estados de bloque", new Estados(output, helper)));
    }

    private static final class Estados extends BlockStateProvider {
        Estados(PackOutput output, ExistingFileHelper helper) {
            super(output, UltimateFoodsCore.MOD_ID, helper);
        }

        @Override
        protected void registerStatesAndModels() {
            getVariantBuilder(PuestoRegistry.MATE_PUESTO.get()).forAllStates(state -> {
                Puesto puesto = state.getValue(PuestoBlock.PUESTO);
                Direction facing = state.getValue(PuestoBlock.FACING);
                ModelFile modelo = new ModelFile.UncheckedModelFile(
                        ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "item/argentina/" + puesto.id()));
                return ConfiguredModel.builder()
                        .modelFile(modelo)
                        .rotationY(((int) facing.toYRot() + 180) % 360)
                        .build();
            });
        }
    }
}
