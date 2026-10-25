package nadiendev.ultimatefoods.fisura.mate.datagen;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Modelos de item del mate.
 * <ul>
 *   <li>Planos: {@code item/generated} con textura {@code item/fisura/<id>}.</li>
 *   <li>Cubos de bloque: apuntan al modelo del bloque.</li>
 *   <li>3D (agente C): un modelo minimo {@code item/<id>} cuyo padre es
 *       {@code item/fisura/<id>} (el modelo hecho a mano). No se valida que exista.</li>
 * </ul>
 */
public class MateItemModels extends ItemModelProvider {

    private static final String[] PLANOS = {
            "yerba_mate_leaf", "yerba_mate_leaf_dried", "yerba_mate", "yuyos"
    };

    /** Ids con modelo 3D hecho a mano (agente C). */
    private static final String[] TRES_D = {
            "yerba_verdeflor", "bombilla", "yerbera", "azucarera", "mate", "mate_con_yerba", "mate_amargo", "mate_dulce",
            "mate_de_leche", "mate_con_yuyos", "mate_lavado", "pava", "pava_con_agua", "pava_caliente"
    };

    public MateItemModels(PackOutput output, ExistingFileHelper helper) {
        super(output, UltimateFoodsCore.MOD_ID, helper);
    }

    @Override
    protected void registerModels() {
        ModelFile generated = new ModelFile.UncheckedModelFile(mcLoc("item/generated"));
        for (String id : PLANOS) {
            getBuilder(id).parent(generated).texture("layer0", modLoc("item/fisura/" + id));
        }

        getBuilder("yerba_mate_sapling").parent(generated).texture("layer0", modLoc("block/fisura/yerba_mate_sapling"));
        getBuilder("yerba_mate_planks").parent(new ModelFile.UncheckedModelFile(modLoc("block/fisura/yerba_mate_planks")));
        getBuilder("yerba_mate_log").parent(new ModelFile.UncheckedModelFile(modLoc("block/fisura/yerba_mate_log")));
        getBuilder("yerba_mate_leaves").parent(new ModelFile.UncheckedModelFile(modLoc("block/fisura/yerba_mate_leaves")));

        for (String id : TRES_D) {
            getBuilder(id).parent(new ModelFile.UncheckedModelFile(modLoc("item/fisura/" + id)));
        }
    }
}
