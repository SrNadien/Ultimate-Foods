package nadiendev.ultimatefoods.argentina.comida.data;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.argentina.cacerola.CacerolaBlock;
import nadiendev.ultimatefoods.argentina.cacerola.CacerolaRegistry;
import nadiendev.ultimatefoods.argentina.comida.ComidaModule;
import nadiendev.ultimatefoods.argentina.comida.colocada.Comida;
import nadiendev.ultimatefoods.argentina.parrilla.ParrillaBlock;
import nadiendev.ultimatefoods.argentina.parrilla.ParrillaRegistry;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Modelos del modulo. Los items planos usan {@code item/generated}; el modelo 3D de la parrilla
 * lo entrega el agente de modelos en {@code models/block/argentina/parrilla.json}, asi que aca solo
 * se apunta a el (sin validarlo, el archivo no tiene por que existir todavia en este modulo).
 */
public final class ComidaModels {

    private static final ResourceLocation PARRILLA_BLOCK_MODEL =
            ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "block/argentina/parrilla");
    private static final ResourceLocation PARRILLA_ITEM_MODEL =
            ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "item/argentina/parrilla");

    private static final ResourceLocation CACEROLA_BLOCK_MODEL =
            ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "block/argentina/cacerola_essen");
    private static final ResourceLocation CACEROLA_ABIERTA_MODEL =
            ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "block/argentina/cacerola_essen_abierta");
    private static final ResourceLocation CACEROLA_ITEM_MODEL =
            ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "item/argentina/cacerola_essen");

    private ComidaModels() {
    }

    public static class Items extends ItemModelProvider {

        public Items(PackOutput output, ExistingFileHelper existingFileHelper) {
            super(output, UltimateFoodsCore.MOD_ID, existingFileHelper);
        }

        @Override
        public String getName() {
            return "Comida: Item Models";
        }

        @Override
        protected void registerModels() {
            ModelFile generated = new ModelFile.UncheckedModelFile(ResourceLocation.withDefaultNamespace("item/generated"));
            for (String id : ComidaModule.flatItemIds()) {
                // Las comidas colocables usan su modelo 3D de item: el del bloque pero sin el plato de abajo.
                Comida colocable = Comida.byId(id);
                if (colocable != null) {
                    getBuilder(id).parent(new ModelFile.UncheckedModelFile(colocable.itemModel()));
                    continue;
                }
                getBuilder(id)
                        .parent(generated)
                        .texture("layer0", ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "item/argentina/" + id));
            }
            // El item de inventario de la parrilla es el modelo 3D del agente C.
            getBuilder("parrilla").parent(new ModelFile.UncheckedModelFile(PARRILLA_ITEM_MODEL));
            getBuilder("cacerola_essen").parent(new ModelFile.UncheckedModelFile(CACEROLA_ITEM_MODEL));
        }
    }

    public static class Blocks extends BlockStateProvider {

        public Blocks(PackOutput output, ExistingFileHelper existingFileHelper) {
            super(output, UltimateFoodsCore.MOD_ID, existingFileHelper);
        }

        @Override
        public String getName() {
            return "Comida: Block States";
        }

        @Override
        protected void registerStatesAndModels() {
            ModelFile model = new ModelFile.UncheckedModelFile(PARRILLA_BLOCK_MODEL);
            // Un solo modelo para encendida y apagada (la luz y las particulas son lo que cambia);
            // solo se gira segun hacia donde mira.
            getVariantBuilder(ParrillaRegistry.PARRILLA.get()).forAllStates(state -> {
                Direction facing = state.getValue(ParrillaBlock.FACING);
                return ConfiguredModel.builder()
                        .modelFile(model)
                        .rotationY((int) (facing.toYRot() + 180.0F) % 360)
                        .build();
            });

            ModelFile cacerola = new ModelFile.UncheckedModelFile(CACEROLA_BLOCK_MODEL);
            ModelFile cacerolaAbierta = new ModelFile.UncheckedModelFile(CACEROLA_ABIERTA_MODEL);
            getVariantBuilder(CacerolaRegistry.CACEROLA_ESSEN.get()).forAllStates(state -> {
                Direction facing = state.getValue(CacerolaBlock.FACING);
                return ConfiguredModel.builder()
                        .modelFile(state.getValue(CacerolaBlock.ABIERTA) ? cacerolaAbierta : cacerola)
                        .rotationY((int) (facing.toYRot() + 180.0F) % 360)
                        .build();
            });
        }
    }
}
