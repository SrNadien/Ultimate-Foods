package nadiendev.ultimatefoods.datagen;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.blocks.BlocksAdds;
import nadiendev.ultimatefoods.blocks.NadieniteOreBlock;
import nadiendev.ultimatefoods.fluidos_xd.FluidsRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

public class BlockModelProvider extends BlockStateProvider {

    public BlockModelProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, UltimateFoodsCore.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // Bloques simples con cubeAll
        blockWithItem(BlocksAdds.NADIENITE_BLOCK);
        blockWithItem(BlocksAdds.STEEL_BLOCK);
        blockWithItem(BlocksAdds.SEXY_XD);
        blockWithItem(BlocksAdds.NETHER_STAR_BLOCK);
        
        // Minerales de Nadienite
        blockWithItem(NadieniteOreBlock.NADIENITE_ORE);
        blockWithItem(NadieniteOreBlock.DEEPSLATE_NADIENITE_ORE);

        // Fluidos
        fluidBlock(FluidsRegistry.NADIENITE_FLUID_BLOCK,
                ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "block/nadienite_fluid"),
                ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "block/nadienite_flow"));
    }

    /**
     * Método auxiliar para crear un bloque simple con cubeAll
     */
    private void blockWithItem(DeferredHolder<Block, Block> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }

    /**
     * Método auxiliar para bloques de fluido
     * Usa las texturas still/flow del fluido y no genera item model (los fluidos no tienen item propio)
     */
    private void fluidBlock(DeferredHolder<Block, LiquidBlock> deferredBlock,
                            ResourceLocation stillTexture,
                            ResourceLocation flowTexture) {
        ModelFile model = models().getBuilder(deferredBlock.getId().getPath())
                .texture("particle", stillTexture);
        simpleBlock(deferredBlock.get(), model);
    }

    /**
     * Método auxiliar para bloques con modelos personalizados
     * El modelo JSON debe estar en: assets/ultimatefoods/models/block/steel_block.json
     */
    private void customBlockWithItem(DeferredHolder<Block, Block> deferredBlock) {
        Block block = deferredBlock.get();
        String blockName = deferredBlock.getId().getPath();
        
        // Referencia al modelo personalizado que está en assets/models/block/
        ModelFile blockModel = models().getExistingFile(
            ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "block/" + blockName)
        );
        
        // Crear blockstate que usa el modelo personalizado
        simpleBlock(block, blockModel);
        
        // Crear item model que también usa el modelo del bloque
        simpleBlockItem(block, blockModel);
    }
}