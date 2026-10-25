package nadiendev.ultimatefoods.argentina.homenaje;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/** Datagen de La Copa. El modelo 3D lo pone Blockbench: aca solo se apunta a el. */
final class HomenajeDatos {

    private HomenajeDatos() {
    }

    private static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, path);
    }

    /** La copa mira al norte en el modelo; el blockstate la gira segun hacia donde mire el bloque. */
    static final class Estados extends BlockStateProvider {
        Estados(PackOutput output, ExistingFileHelper helper) {
            super(output, UltimateFoodsCore.MOD_ID, helper);
        }

        @Override
        protected void registerStatesAndModels() {
            horizontalBlock(HomenajeModule.LA_COPA.get(), new ModelFile.UncheckedModelFile(rl("block/argentina/la_copa")));
        }
    }

    /** `item/la_copa` hereda del modelo 3D del item (que trae su display). */
    static final class ModelosItem extends ItemModelProvider {
        ModelosItem(PackOutput output, ExistingFileHelper helper) {
            super(output, UltimateFoodsCore.MOD_ID, helper);
        }

        @Override
        protected void registerModels() {
            getBuilder("item/la_copa").parent(new ModelFile.UncheckedModelFile(rl("item/argentina/la_copa")));
        }
    }

    static LootTableProvider loot(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        return new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(CopaLoot::new, LootContextParamSets.BLOCK)), registries);
    }

    private static final class CopaLoot extends BlockLootSubProvider {
        CopaLoot(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
        }

        @Override
        protected void generate() {
            dropSelf(HomenajeModule.LA_COPA.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return List.of(HomenajeModule.LA_COPA.get());
        }
    }

    /** Seis lingotes de oro con forma de copa. */
    static final class Recetas extends RecipeProvider {
        Recetas(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected void buildRecipes(RecipeOutput out) {
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HomenajeModule.LA_COPA_ITEM.get())
                    .pattern("G G")
                    .pattern(" G ")
                    .pattern("GGG")
                    .define('G', Items.GOLD_INGOT)
                    .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
                    .save(out, rl("la_copa"));
        }
    }
}
