package nadiendev.ultimatefoods.argentina.mate.datagen;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.argentina.mate.MateBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Estados de bloque. Ojo: el modelo de la yerbera lo hace a mano el agente C
 * ({@code block/argentina/yerbera}); aca solo se apunta a el.
 */
public class MateBlockStates extends BlockStateProvider {

    public MateBlockStates(PackOutput output, ExistingFileHelper helper) {
        super(output, UltimateFoodsCore.MOD_ID, helper);
    }

    private ResourceLocation tex(String name) {
        return modLoc("block/argentina/" + name);
    }

    @Override
    protected void registerStatesAndModels() {
        // Tronco: vertical y horizontal con la misma corteza y el corte de anillos arriba y abajo.
        ModelFile vertical = models().cubeColumn("block/argentina/yerba_mate_log", tex("yerba_mate_log"), tex("yerba_mate_log_top"));
        ModelFile horizontal = models().cubeColumnHorizontal("block/argentina/yerba_mate_log_horizontal",
                tex("yerba_mate_log"), tex("yerba_mate_log_top"));
        axisBlock(MateBlocks.YERBA_MATE_LOG.get(), vertical, horizontal);

        simpleBlock(MateBlocks.YERBA_MATE_LEAVES.get(),
                models().cubeAll("block/argentina/yerba_mate_leaves", tex("yerba_mate_leaves")).renderType("cutout_mipped"));

        simpleBlock(MateBlocks.YERBA_MATE_SAPLING.get(),
                models().cross("block/argentina/yerba_mate_sapling", tex("yerba_mate_sapling")).renderType("cutout"));

        horizontalBlock(MateBlocks.YERBERA.get(), new ModelFile.UncheckedModelFile(modLoc("block/argentina/yerbera")));
        horizontalBlock(MateBlocks.AZUCARERA.get(), new ModelFile.UncheckedModelFile(modLoc("block/argentina/azucarera")));
    }
}
