package nadiendev.ultimatefoods.datagen.providers;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static nadiendev.ultimatefoods.datagen.providers.ModelosCubos.cubo;

/**
 * Modelos de item de las comidas originales (los planos y los que apuntan a un modelo custom).
 * Archivo generado a partir de los modelos que antes se escribian a mano.
 */
public class ModelosItemsComidas extends ItemModelProvider {

    public ModelosItemsComidas(PackOutput output, ExistingFileHelper helper) {
        super(output, UltimateFoodsCore.MOD_ID, helper);
    }

    @Override
    protected void registerModels() {
        baked_tortilla();
        cajita_feliz();
        chocolatada();
        doritos();
        monster();
        poop();
        super_chile();
        super_energy_drink();
        superpoop();
        taco();
        tortilla();
    }

    private void baked_tortilla() {
        ItemModelBuilder b = getBuilder("item/baked_tortilla");
        b.parent(new ModelFile.UncheckedModelFile(ResourceLocation.parse("item/generated")));
        b.texture("layer0", "ultimatefoods:item/baked_tortilla");
    }

    private void cajita_feliz() {
        ItemModelBuilder b = getBuilder("item/cajita_feliz");
        b.renderType("cutout");
        b.parent(new ModelFile.UncheckedModelFile(ResourceLocation.parse("ultimatefoods:custom/cajita_feliz")));
        b.texture("0", "ultimatefoods:block/cajita_feliz");
        b.texture("particle", "ultimatefoods:item/cajita_feliz");
    }

    private void chocolatada() {
        ItemModelBuilder b = getBuilder("item/chocolatada");
        b.parent(new ModelFile.UncheckedModelFile(ResourceLocation.parse("item/generated")));
        b.texture("layer0", "ultimatefoods:item/chocolate_milk");
    }

    private void doritos() {
        ItemModelBuilder b = getBuilder("item/doritos");
        b.renderType("cutout");
        b.parent(new ModelFile.UncheckedModelFile(ResourceLocation.parse("ultimatefoods:custom/doritos")));
        b.texture("particle", "ultimatefoods:item/doritos");
    }

    private void monster() {
        ItemModelBuilder b = getBuilder("item/monster");
        b.renderType("cutout");
        b.parent(new ModelFile.UncheckedModelFile(ResourceLocation.parse("ultimatefoods:custom/monster")));
        b.texture("particle", "ultimatefoods:item/monster");
    }

    private void poop() {
        ItemModelBuilder b = getBuilder("item/poop");
        b.parent(new ModelFile.UncheckedModelFile(ResourceLocation.parse("item/generated")));
        b.texture("layer0", "ultimatefoods:item/poop");
    }

    private void super_chile() {
        ItemModelBuilder b = getBuilder("item/super_chile");
        b.parent(new ModelFile.UncheckedModelFile(ResourceLocation.parse("item/generated")));
        b.texture("layer0", "ultimatefoods:item/chili_pepper");
    }

    private void super_energy_drink() {
        ItemModelBuilder b = getBuilder("item/super_energy_drink");
        b.parent(new ModelFile.UncheckedModelFile(ResourceLocation.parse("item/generated")));
        b.texture("layer0", "ultimatefoods:item/super_energy_drink");
    }

    private void superpoop() {
        ItemModelBuilder b = getBuilder("item/superpoop");
        b.parent(new ModelFile.UncheckedModelFile(ResourceLocation.parse("item/generated")));
        b.texture("layer0", "ultimatefoods:item/superpoop");
    }

    private void taco() {
        ItemModelBuilder b = getBuilder("item/taco");
        b.parent(new ModelFile.UncheckedModelFile(ResourceLocation.parse("item/generated")));
        b.texture("layer0", "ultimatefoods:item/taco");
    }

    private void tortilla() {
        ItemModelBuilder b = getBuilder("item/tortilla");
        b.parent(new ModelFile.UncheckedModelFile(ResourceLocation.parse("item/generated")));
        b.texture("layer0", "ultimatefoods:item/tortilla");
    }
}
