package nadiendev.ultimatefoods.datagen.providers;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static nadiendev.ultimatefoods.datagen.providers.ModelosCubos.cubo;

/**
 * Modelos 3D de la carpeta custom: cajita feliz, doritos, monster, pancho y sandwichito.
 * Archivo generado a partir de los modelos que antes se escribian a mano.
 */
public class ModelosCustomComidas extends ModelProvider<BlockModelBuilder> {

    public ModelosCustomComidas(PackOutput output, ExistingFileHelper helper) {
        super(output, UltimateFoodsCore.MOD_ID, "custom", BlockModelBuilder::new, helper);
    }

    @Override
    public String getName() {
        return "Modelos custom: " + UltimateFoodsCore.MOD_ID;
    }

    @Override
    protected void registerModels() {
        cajita_feliz();
        doritos();
        monster();
        pancho();
        sandwichito();
    }

    private void cajita_feliz() {
        BlockModelBuilder b = getBuilder("custom/cajita_feliz");
        cubo(b, 6.0f, 0.0f, 5.0f, 10.0f, 6.0f, 6.0f).rot(0.0f, Direction.Axis.Y, 10.0f, 3.0f, 8.0f).cara(Direction.NORTH, "#0", 3.0f, 6.0f, 5.0f, 9.0f).cara(Direction.EAST, "#0", 9.0f, 7.0f, 9.5f, 10.0f).cara(Direction.SOUTH, "#0", 6.0f, 3.0f, 8.0f, 6.0f).cara(Direction.WEST, "#0", 8.0f, 9.0f, 8.5f, 12.0f).cara(Direction.UP, "#0", 11.0f, 2.5f, 9.0f, 2.0f).cara(Direction.DOWN, "#0", 11.0f, 2.5f, 9.0f, 3.0f).fin();
        cubo(b, 5.0f, 0.0f, 5.0f, 6.0f, 6.0f, 11.0f).rot(0.0f, Direction.Axis.Y, 5.5f, 2.5f, 8.0f).cara(Direction.NORTH, "#0", 8.5f, 9.0f, 9.0f, 12.0f).cara(Direction.EAST, "#0", 0.0f, 0.0f, 3.0f, 3.0f).cara(Direction.SOUTH, "#0", 9.5f, 7.0f, 10.0f, 10.0f).cara(Direction.WEST, "#0", 0.0f, 3.0f, 3.0f, 6.0f).cara(Direction.UP, "#0", 10.5f, 10.0f, 10.0f, 7.0f).cara(Direction.DOWN, "#0", 9.5f, 10.0f, 9.0f, 13.0f).fin();
        cubo(b, 10.0f, 7.5f, 7.5f, 11.0f, 9.25f, 8.5f).rot(0.0f, Direction.Axis.Y, 10.5f, 8.75f, 10.5f).cara(Direction.NORTH, "#0", 8.0f, 5.0f, 8.5f, 6.0f).cara(Direction.EAST, "#0", 8.5f, 5.0f, 9.0f, 6.0f).cara(Direction.SOUTH, "#0", 11.0f, 7.5f, 11.5f, 8.5f).cara(Direction.WEST, "#0", 11.0f, 8.5f, 11.5f, 9.5f).cara(Direction.UP, "#0", 12.5f, 1.0f, 12.0f, 0.5f).cara(Direction.DOWN, "#0", 12.5f, 1.0f, 12.0f, 1.5f).fin();
        cubo(b, 7.5f, 7.5f, 7.5f, 8.5f, 9.25f, 8.5f).rot(0.0f, Direction.Axis.Y, 8.0f, 8.75f, 10.5f).cara(Direction.NORTH, "#0", 11.0f, 9.5f, 11.5f, 10.5f).cara(Direction.EAST, "#0", 11.0f, 10.5f, 11.5f, 11.5f).cara(Direction.SOUTH, "#0", 1.0f, 11.5f, 1.5f, 12.5f).cara(Direction.WEST, "#0", 1.5f, 11.5f, 2.0f, 12.5f).cara(Direction.UP, "#0", 12.5f, 2.0f, 12.0f, 1.5f).cara(Direction.DOWN, "#0", 4.5f, 12.0f, 4.0f, 12.5f).fin();
        cubo(b, 5.0f, 7.5f, 7.5f, 6.0f, 9.25f, 8.5f).rot(0.0f, Direction.Axis.Y, 5.5f, 8.75f, 10.5f).cara(Direction.NORTH, "#0", 2.0f, 11.5f, 2.5f, 12.5f).cara(Direction.EAST, "#0", 2.5f, 11.5f, 3.0f, 12.5f).cara(Direction.SOUTH, "#0", 3.0f, 11.5f, 3.5f, 12.5f).cara(Direction.WEST, "#0", 3.5f, 11.5f, 4.0f, 12.5f).cara(Direction.UP, "#0", 5.0f, 12.5f, 4.5f, 12.0f).cara(Direction.DOWN, "#0", 5.5f, 12.0f, 5.0f, 12.5f).fin();
        cubo(b, 6.0f, 8.5f, 7.5f, 7.5f, 9.5f, 8.5f).rot(0.0f, Direction.Axis.Z, 6.25f, 9.0f, 8.0f).cara(Direction.NORTH, "#0", 11.5f, 8.5f, 12.25f, 9.0f).cara(Direction.EAST, "#0", 5.5f, 12.0f, 6.0f, 12.5f).cara(Direction.SOUTH, "#0", 11.5f, 9.0f, 12.25f, 9.5f).cara(Direction.WEST, "#0", 6.0f, 12.0f, 6.5f, 12.5f).cara(Direction.UP, "#0", 12.25f, 10.0f, 11.5f, 9.5f).cara(Direction.DOWN, "#0", 12.25f, 10.0f, 11.5f, 10.5f).fin();
        cubo(b, 8.5f, 8.5f, 7.5f, 10.0f, 9.5f, 8.5f).rot(0.0f, Direction.Axis.Y, 8.75f, 9.0f, 8.0f).cara(Direction.NORTH, "#0", 11.5f, 10.5f, 12.25f, 11.0f).cara(Direction.EAST, "#0", 6.5f, 12.0f, 7.0f, 12.5f).cara(Direction.SOUTH, "#0", 11.0f, 11.5f, 11.75f, 12.0f).cara(Direction.WEST, "#0", 7.0f, 12.0f, 7.5f, 12.5f).cara(Direction.UP, "#0", 12.25f, 11.5f, 11.5f, 11.0f).cara(Direction.DOWN, "#0", 12.75f, 0.0f, 12.0f, 0.5f).fin();
        cubo(b, 10.0f, 0.0f, 5.0f, 11.0f, 6.0f, 11.0f).rot(0.0f, Direction.Axis.Y, 10.5f, 2.5f, 8.0f).cara(Direction.NORTH, "#0", 9.5f, 10.0f, 10.0f, 13.0f).cara(Direction.EAST, "#0", 3.0f, 0.0f, 6.0f, 3.0f).cara(Direction.SOUTH, "#0", 10.0f, 10.0f, 10.5f, 13.0f).cara(Direction.WEST, "#0", 3.0f, 3.0f, 6.0f, 6.0f).cara(Direction.UP, "#0", 11.0f, 10.0f, 10.5f, 7.0f).cara(Direction.DOWN, "#0", 11.0f, 10.0f, 10.5f, 13.0f).fin();
        cubo(b, 6.0f, 0.0f, 10.0f, 10.0f, 6.0f, 11.0f).rot(0.0f, Direction.Axis.Y, 9.5f, 2.5f, 10.5f).cara(Direction.NORTH, "#0", 5.0f, 6.0f, 7.0f, 9.0f).cara(Direction.EAST, "#0", 0.0f, 11.0f, 0.5f, 14.0f).cara(Direction.SOUTH, "#0", 7.0f, 6.0f, 9.0f, 9.0f).cara(Direction.WEST, "#0", 0.5f, 11.0f, 1.0f, 14.0f).cara(Direction.UP, "#0", 3.0f, 11.5f, 1.0f, 11.0f).cara(Direction.DOWN, "#0", 13.0f, 2.0f, 11.0f, 2.5f).fin();
        cubo(b, 6.0f, 0.0f, 6.0f, 10.0f, 1.0f, 10.0f).rot(0.0f, Direction.Axis.Y, 8.0f, 0.0f, 7.0f).cara(Direction.NORTH, "#0", 11.0f, 2.5f, 13.0f, 3.0f).cara(Direction.EAST, "#0", 3.0f, 11.0f, 5.0f, 11.5f).cara(Direction.SOUTH, "#0", 11.0f, 3.0f, 13.0f, 3.5f).cara(Direction.WEST, "#0", 11.0f, 3.5f, 13.0f, 4.0f).cara(Direction.UP, "#0", 11.0f, 7.0f, 9.0f, 5.0f).cara(Direction.DOWN, "#0", 8.0f, 9.0f, 6.0f, 11.0f).fin();
        cubo(b, 5.0f, 6.0f, 5.0f, 11.0f, 6.25f, 11.0f).rot(0.0f, Direction.Axis.Y, 8.0f, 6.0f, 7.0f).cara(Direction.NORTH, "#0", 5.0f, 11.0f, 8.0f, 11.25f).cara(Direction.EAST, "#0", 11.0f, 5.0f, 14.0f, 5.25f).cara(Direction.SOUTH, "#0", 11.0f, 5.5f, 14.0f, 5.75f).cara(Direction.WEST, "#0", 11.0f, 6.0f, 14.0f, 6.25f).cara(Direction.UP, "#0", 3.0f, 9.0f, 0.0f, 6.0f).cara(Direction.DOWN, "#0", 9.0f, 0.0f, 6.0f, 3.0f).fin();
        cubo(b, 5.0f, 7.75f, 8.0f, 11.0f, 8.0f, 11.25f).rot(22.5f, Direction.Axis.X, 8.0f, 7.75f, 7.25f).cara(Direction.NORTH, "#0", 11.0f, 6.5f, 14.0f, 6.75f).cara(Direction.EAST, "#0", 11.5f, 7.5f, 13.25f, 7.75f).cara(Direction.SOUTH, "#0", 11.0f, 7.0f, 14.0f, 7.25f).cara(Direction.WEST, "#0", 11.5f, 8.0f, 13.25f, 8.25f).cara(Direction.UP, "#0", 12.0f, 1.75f, 9.0f, 0.0f).cara(Direction.DOWN, "#0", 6.0f, 9.0f, 3.0f, 10.75f).fin();
        cubo(b, 5.0f, 7.0f, 5.0f, 11.0f, 7.25f, 8.25f).rot(-22.5f, Direction.Axis.X, 8.0f, 7.0f, 7.0f).cara(Direction.NORTH, "#0", 11.0f, 4.0f, 14.0f, 4.25f).cara(Direction.EAST, "#0", 4.0f, 11.5f, 5.75f, 11.75f).cara(Direction.SOUTH, "#0", 11.0f, 4.5f, 14.0f, 4.75f).cara(Direction.WEST, "#0", 6.0f, 11.5f, 7.75f, 11.75f).cara(Direction.UP, "#0", 11.0f, 4.75f, 8.0f, 3.0f).cara(Direction.DOWN, "#0", 3.0f, 9.0f, 0.0f, 10.75f).fin();
        var tr = b.transforms();
        tr.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(76.0f, 0.0f, 1.0f).translation(-0.25f, -0.5f, 0.0f).scale(0.8f, 0.8f, 0.8f).end();
        tr.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(73.0f, 0.0f, 0.0f).scale(0.8f, 0.8f, 0.8f).end();
        tr.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(9.0f, 89.0f, 0.0f).translation(4.0f, 4.0f, 0.0f).end();
        tr.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(9.0f, 89.0f, 0.0f).translation(4.0f, 4.0f, 0.0f).end();
        tr.transform(ItemDisplayContext.GROUND).translation(0.0f, 4.25f, 0.0f).end();
        tr.transform(ItemDisplayContext.GUI).rotation(7.0f, -32.0f, -7.0f).translation(0.75f, 4.5f, 0.0f).scale(1.59f, 1.5f, 1.5f).end();
        tr.transform(ItemDisplayContext.HEAD).translation(0.0f, 14.5f, 0.0f).end();
        tr.transform(ItemDisplayContext.FIXED).translation(0.0f, 6.5f, -4.0f).scale(1.5f, 1.5f, 1.5f).end();
        tr.end();
    }

    private void doritos() {
        BlockModelBuilder b = getBuilder("custom/doritos");
        b.guiLight(BlockModel.GuiLight.FRONT);
        b.texture("1", "ultimatefoods:block/dorito1");
        b.texture("2", "ultimatefoods:block/dorito2");
        b.texture("particle", "ultimatefoods:item/doritos");
        cubo(b, 4.0f, 0.0f, 6.0f, 11.0f, 9.0f, 9.0f).rot(0.0f, Direction.Axis.Y, 3.0f, 0.0f, 6.0f).cara(Direction.NORTH, "#1", 0.0f, 0.0f, 3.5f, 4.5f).cara(Direction.EAST, "#1", 0.0f, 4.5f, 1.5f, 9.0f).cara(Direction.SOUTH, "#1", 3.5f, 0.0f, 7.0f, 4.5f).cara(Direction.WEST, "#1", 1.5f, 4.5f, 3.0f, 9.0f).cara(Direction.UP, "#1", 6.5f, 6.0f, 3.0f, 4.5f).cara(Direction.DOWN, "#1", 6.5f, 6.0f, 3.0f, 7.5f).fin();
        cubo(b, 4.0f, 9.0f, 6.5f, 11.0f, 10.0f, 8.5f).rot(0.0f, Direction.Axis.Y, 3.0f, 1.0f, 6.0f).cara(Direction.NORTH, "#1", 6.5f, 6.5f, 10.0f, 7.0f).cara(Direction.EAST, "#1", 1.5f, 9.0f, 2.5f, 9.5f).cara(Direction.SOUTH, "#1", 7.0f, 0.0f, 10.5f, 0.5f).cara(Direction.WEST, "#1", 2.5f, 9.0f, 3.5f, 9.5f).cara(Direction.UP, "#1", 10.0f, 5.5f, 6.5f, 4.5f).cara(Direction.DOWN, "#1", 10.0f, 5.5f, 6.5f, 6.5f).fin();
        cubo(b, 4.25f, 10.0f, 7.0f, 10.75f, 11.0f, 8.0f).rot(0.0f, Direction.Axis.Y, 3.0f, 1.0f, 6.0f).cara(Direction.NORTH, "#1", 7.0f, 0.5f, 10.25f, 1.0f).cara(Direction.EAST, "#1", 1.5f, 10.0f, 2.0f, 10.5f).cara(Direction.SOUTH, "#1", 7.0f, 1.0f, 10.25f, 1.5f).cara(Direction.WEST, "#1", 2.0f, 10.0f, 2.5f, 10.5f).cara(Direction.UP, "#1", 10.25f, 2.0f, 7.0f, 1.5f).cara(Direction.DOWN, "#1", 10.25f, 2.0f, 7.0f, 2.5f).fin();
        cubo(b, 3.3f, 9.0f, 6.5f, 4.3f, 11.0f, 8.5f).rot(0.0f, Direction.Axis.Y, 2.3f, 9.0f, 6.0f).cara(Direction.NORTH, "#1", 9.0f, 3.0f, 9.5f, 4.0f).cara(Direction.EAST, "#1", 4.5f, 7.5f, 5.5f, 8.5f).cara(Direction.SOUTH, "#1", 3.5f, 9.0f, 4.0f, 10.0f).cara(Direction.WEST, "#1", 5.5f, 7.5f, 6.5f, 8.5f).cara(Direction.UP, "#1", 4.5f, 10.0f, 4.0f, 9.0f).cara(Direction.DOWN, "#1", 5.5f, 9.0f, 5.0f, 10.0f).fin();
        cubo(b, 10.7f, 0.0f, 6.0f, 11.7f, 2.0f, 9.0f).rot(0.0f, Direction.Axis.Z, 9.7f, 0.0f, 6.0f).cara(Direction.NORTH, "#1", 5.5f, 9.0f, 6.0f, 10.0f).cara(Direction.EAST, "#1", 7.0f, 2.5f, 8.5f, 3.5f).cara(Direction.SOUTH, "#1", 6.0f, 9.0f, 6.5f, 10.0f).cara(Direction.WEST, "#1", 7.0f, 3.5f, 8.5f, 4.5f).cara(Direction.UP, "#1", 8.0f, 9.5f, 7.5f, 8.0f).cara(Direction.DOWN, "#1", 8.5f, 8.0f, 8.0f, 9.5f).fin();
        cubo(b, 11.2f, 0.0f, 6.0f, 12.2f, 1.0f, 9.0f).rot(0.0f, Direction.Axis.Z, 10.2f, -1.0f, 6.0f).cara(Direction.NORTH, "#1", 10.0f, 2.5f, 10.5f, 3.0f).cara(Direction.EAST, "#1", 8.5f, 2.5f, 10.0f, 3.0f).cara(Direction.SOUTH, "#1", 10.0f, 3.0f, 10.5f, 3.5f).cara(Direction.WEST, "#1", 3.0f, 8.5f, 4.5f, 9.0f).cara(Direction.UP, "#1", 9.0f, 4.5f, 8.5f, 3.0f).cara(Direction.DOWN, "#1", 5.0f, 8.5f, 4.5f, 10.0f).fin();
        cubo(b, 2.8f, 0.0f, 6.0f, 3.8f, 1.0f, 9.0f).rot(0.0f, Direction.Axis.Z, 1.8f, -1.0f, 6.0f).cara(Direction.NORTH, "#1", 3.5f, 10.0f, 4.0f, 10.5f).cara(Direction.EAST, "#1", 5.0f, 8.5f, 6.5f, 9.0f).cara(Direction.SOUTH, "#1", 10.0f, 3.5f, 10.5f, 4.0f).cara(Direction.WEST, "#1", 8.5f, 8.0f, 10.0f, 8.5f).cara(Direction.UP, "#1", 9.0f, 10.0f, 8.5f, 8.5f).cara(Direction.DOWN, "#1", 0.5f, 9.0f, 0.0f, 10.5f).fin();
        cubo(b, 3.3f, 0.0f, 6.0f, 4.3f, 2.0f, 9.0f).rot(0.0f, Direction.Axis.Z, 2.3f, 0.0f, 6.0f).cara(Direction.NORTH, "#1", 6.5f, 9.0f, 7.0f, 10.0f).cara(Direction.EAST, "#1", 6.5f, 7.0f, 8.0f, 8.0f).cara(Direction.SOUTH, "#1", 7.0f, 9.0f, 7.5f, 10.0f).cara(Direction.WEST, "#1", 3.0f, 7.5f, 4.5f, 8.5f).cara(Direction.UP, "#1", 1.0f, 10.5f, 0.5f, 9.0f).cara(Direction.DOWN, "#1", 1.5f, 9.0f, 1.0f, 10.5f).fin();
        cubo(b, 10.7f, 9.0f, 6.75f, 11.7f, 11.0f, 8.5f).rot(0.0f, Direction.Axis.Y, 9.7f, 9.0f, 6.0f).cara(Direction.NORTH, "#1", 9.0f, 7.0f, 9.5f, 8.0f).cara(Direction.EAST, "#1", 6.5f, 8.0f, 7.375f, 9.0f).cara(Direction.SOUTH, "#1", 9.0f, 8.5f, 9.5f, 9.5f).cara(Direction.WEST, "#1", 8.0f, 7.0f, 8.875f, 8.0f).cara(Direction.UP, "#2", 10.0f, 3.875f, 9.5f, 3.0f).cara(Direction.DOWN, "#1", 10.0f, 7.0f, 9.5f, 7.875f).fin();
        cubo(b, 11.2f, 10.0f, 6.75f, 12.2f, 11.0f, 8.5f).rot(0.0f, Direction.Axis.Y, 10.2f, 9.0f, 6.0f).cara(Direction.NORTH, "#1", 4.0f, 10.0f, 4.5f, 10.5f).cara(Direction.EAST, "#1", 7.5f, 9.5f, 8.375f, 10.0f).cara(Direction.SOUTH, "#1", 10.0f, 4.0f, 10.5f, 4.5f).cara(Direction.WEST, "#1", 9.5f, 8.5f, 10.375f, 9.0f).cara(Direction.UP, "#1", 9.5f, 10.375f, 9.0f, 9.5f).cara(Direction.DOWN, "#1", 10.0f, 9.0f, 9.5f, 9.875f).fin();
        cubo(b, 2.8f, 10.0f, 6.5f, 3.8f, 11.0f, 8.5f).rot(0.0f, Direction.Axis.Y, 1.8f, 9.0f, 6.0f).cara(Direction.NORTH, "#1", 4.5f, 10.0f, 5.0f, 10.5f).cara(Direction.EAST, "#1", 9.0f, 4.0f, 10.0f, 4.5f).cara(Direction.SOUTH, "#1", 10.0f, 4.5f, 10.5f, 5.0f).cara(Direction.WEST, "#1", 1.5f, 9.5f, 2.5f, 10.0f).cara(Direction.UP, "#1", 3.0f, 10.5f, 2.5f, 9.5f).cara(Direction.DOWN, "#1", 3.5f, 9.5f, 3.0f, 10.5f).fin();
        var tr = b.transforms();
        tr.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(63.08f, 44.0f, 4.07f).translation(-1.25f, 0.0f, 1.5f).scale(0.8f, 0.8f, 0.8f).end();
        tr.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(73.0f, 35.0f, -3.0f).translation(-1.25f, -0.25f, 1.75f).scale(0.8f, 0.8f, 0.8f).end();
        tr.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(12.0f, -91.0f, 3.0f).translation(5.25f, 3.0f, 0.0f).end();
        tr.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(4.0f, 116.0f, 0.0f).translation(5.25f, 3.5f, 0.0f).end();
        tr.transform(ItemDisplayContext.GROUND).translation(0.0f, 4.5f, 0.5f).end();
        tr.transform(ItemDisplayContext.GUI).translation(0.75f, 3.0f, 0.0f).scale(1.37f, 1.3f, 1.3f).end();
        tr.transform(ItemDisplayContext.HEAD).translation(0.5f, 4.5f, -5.75f).end();
        tr.transform(ItemDisplayContext.FIXED).translation(0.75f, 5.0f, -1.25f).scale(1.8f, 1.8f, 1.8f).end();
        tr.end();
    }

    private void monster() {
        BlockModelBuilder b = getBuilder("custom/monster");
        b.texture("0", "ultimatefoods:block/monster");
        b.texture("particle", "ultimatefoods:item/monster");
        cubo(b, 6.0f, 0.0f, 6.0f, 7.0f, 12.0f, 10.0f).rot(0.0f, Direction.Axis.Y, 6.5f, 6.0f, 7.5f).cara(Direction.NORTH, "#0", 6.0f, 3.0f, 6.25f, 6.0f).cara(Direction.EAST, "#0", 2.5f, 0.0f, 3.5f, 3.0f).cara(Direction.SOUTH, "#0", 3.5f, 6.0f, 3.75f, 9.0f).cara(Direction.WEST, "#0", 0.0f, 3.0f, 1.0f, 6.0f).cara(Direction.UP, "#0", 6.5f, 5.5f, 6.25f, 4.5f).cara(Direction.DOWN, "#0", 6.75f, 4.5f, 6.5f, 5.5f).fin();
        cubo(b, 7.0f, 0.0f, 10.0f, 10.0f, 12.0f, 10.25f).rot(0.0f, Direction.Axis.Y, 8.5f, 6.0f, 10.75f).cara(Direction.NORTH, "#0", 3.0f, 3.0f, 3.75f, 6.0f).cara(Direction.EAST, "#0", 6.5f, 0.0f, 6.625f, 3.0f).cara(Direction.SOUTH, "#0", 3.5f, 0.0f, 4.25f, 3.0f).cara(Direction.WEST, "#0", 6.75f, 0.0f, 6.875f, 3.0f).cara(Direction.UP, "#0", 7.75f, 4.375f, 7.0f, 4.25f).cara(Direction.DOWN, "#0", 7.75f, 4.5f, 7.0f, 4.625f).fin();
        cubo(b, 11.0f, 0.0f, 6.0f, 11.25f, 12.0f, 9.0f).rot(0.0f, Direction.Axis.Y, 10.5f, 6.0f, 7.5f).cara(Direction.NORTH, "#0", 6.75f, 4.5f, 6.875f, 7.5f).cara(Direction.EAST, "#0", 3.75f, 3.0f, 4.5f, 6.0f).cara(Direction.SOUTH, "#0", 5.25f, 6.75f, 5.375f, 9.75f).cara(Direction.WEST, "#0", 4.25f, 0.0f, 5.0f, 3.0f).cara(Direction.UP, "#0", 3.375f, 7.75f, 3.25f, 7.0f).cara(Direction.DOWN, "#0", 7.125f, 4.75f, 7.0f, 5.5f).fin();
        cubo(b, 5.75f, 0.0f, 6.0f, 6.0f, 12.0f, 9.0f).rot(0.0f, Direction.Axis.Y, 6.5f, 6.0f, 7.5f).cara(Direction.NORTH, "#0", 5.5f, 6.75f, 5.625f, 9.75f).cara(Direction.EAST, "#0", 4.5f, 3.0f, 5.25f, 6.0f).cara(Direction.SOUTH, "#0", 5.75f, 6.75f, 5.875f, 9.75f).cara(Direction.WEST, "#0", 5.0f, 0.0f, 5.75f, 3.0f).cara(Direction.UP, "#0", 7.125f, 6.25f, 7.0f, 5.5f).cara(Direction.DOWN, "#0", 7.125f, 6.25f, 7.0f, 7.0f).fin();
        cubo(b, 7.0f, 0.0f, 4.75f, 10.0f, 12.0f, 5.0f).rot(0.0f, Direction.Axis.Y, 8.5f, 6.0f, 5.5f).cara(Direction.NORTH, "#0", 5.25f, 3.0f, 6.0f, 6.0f).cara(Direction.EAST, "#0", 6.0f, 6.75f, 6.125f, 9.75f).cara(Direction.SOUTH, "#0", 5.75f, 0.0f, 6.5f, 3.0f).cara(Direction.WEST, "#0", 6.25f, 6.75f, 6.375f, 9.75f).cara(Direction.UP, "#0", 7.75f, 7.125f, 7.0f, 7.0f).cara(Direction.DOWN, "#0", 2.25f, 7.25f, 1.5f, 7.375f).fin();
        cubo(b, 7.0f, 0.0f, 9.0f, 11.0f, 12.0f, 10.0f).rot(0.0f, Direction.Axis.Y, 10.5f, 6.0f, 7.5f).cara(Direction.NORTH, "#0", 1.0f, 3.0f, 2.0f, 6.0f).cara(Direction.EAST, "#0", 3.75f, 6.0f, 4.0f, 9.0f).cara(Direction.SOUTH, "#0", 2.0f, 3.0f, 3.0f, 6.0f).cara(Direction.WEST, "#0", 4.0f, 6.0f, 4.25f, 9.0f).cara(Direction.UP, "#0", 8.0f, 0.75f, 7.0f, 0.5f).cara(Direction.DOWN, "#0", 8.0f, 0.75f, 7.0f, 1.0f).fin();
        cubo(b, 10.0f, 0.0f, 6.0f, 11.0f, 12.0f, 9.0f).rot(0.0f, Direction.Axis.Y, 10.5f, 6.0f, 7.5f).cara(Direction.NORTH, "#0", 4.25f, 6.0f, 4.5f, 9.0f).cara(Direction.EAST, "#0", 0.0f, 6.0f, 0.75f, 9.0f).cara(Direction.SOUTH, "#0", 4.5f, 6.0f, 4.75f, 9.0f).cara(Direction.WEST, "#0", 0.75f, 6.0f, 1.5f, 9.0f).cara(Direction.UP, "#0", 6.75f, 7.5f, 6.5f, 6.75f).cara(Direction.DOWN, "#0", 7.25f, 1.75f, 7.0f, 2.5f).fin();
        cubo(b, 6.0f, 0.0f, 5.0f, 11.0f, 12.0f, 6.0f).cara(Direction.NORTH, "#0", 0.0f, 0.0f, 1.25f, 3.0f).cara(Direction.EAST, "#0", 4.75f, 6.0f, 5.0f, 9.0f).cara(Direction.SOUTH, "#0", 1.25f, 0.0f, 2.5f, 3.0f).cara(Direction.WEST, "#0", 5.0f, 6.0f, 5.25f, 9.0f).cara(Direction.UP, "#0", 8.25f, 0.25f, 7.0f, 0.0f).cara(Direction.DOWN, "#0", 8.25f, 0.25f, 7.0f, 0.5f).fin();
        cubo(b, 6.6f, 11.5f, 5.5f, 10.4f, 12.5f, 9.4f).rot(0.0f, Direction.Axis.Y, -0.1f, -1.5f, -0.1f).cara(Direction.NORTH, "#0", 7.0f, 1.0f, 8.0f, 1.25f).cara(Direction.EAST, "#0", 7.0f, 1.25f, 8.0f, 1.5f).cara(Direction.SOUTH, "#0", 1.5f, 7.0f, 2.5f, 7.25f).cara(Direction.WEST, "#0", 7.0f, 1.5f, 8.0f, 1.75f).cara(Direction.UP, "#0", 2.5f, 7.0f, 1.5f, 6.0f).cara(Direction.DOWN, "#0", 3.5f, 6.0f, 2.5f, 7.0f).fin();
        cubo(b, 7.0f, 0.0f, 6.0f, 10.0f, 1.0f, 9.0f).rot(0.0f, Direction.Axis.Y, 0.0f, -13.0f, 0.0f).cara(Direction.NORTH, "#0", 2.5f, 7.0f, 3.25f, 7.25f).cara(Direction.EAST, "#0", 7.0f, 2.5f, 7.75f, 2.75f).cara(Direction.SOUTH, "#0", 7.0f, 2.75f, 7.75f, 3.0f).cara(Direction.WEST, "#0", 7.0f, 3.0f, 7.75f, 3.25f).cara(Direction.UP, "#0", 6.0f, 6.75f, 5.25f, 6.0f).cara(Direction.DOWN, "#0", 6.75f, 6.0f, 6.0f, 6.75f).fin();
        cubo(b, 7.0f, 11.8f, 6.0f, 10.0f, 12.8f, 9.0f).rot(0.0f, Direction.Axis.Y, 7.0f, 10.8f, 6.0f).cara(Direction.NORTH, "#0", 7.0f, 3.25f, 7.75f, 3.5f).cara(Direction.EAST, "#0", 7.0f, 3.5f, 7.75f, 3.75f).cara(Direction.SOUTH, "#0", 7.0f, 3.75f, 7.75f, 4.0f).cara(Direction.WEST, "#0", 7.0f, 4.0f, 7.75f, 4.25f).cara(Direction.UP, "#0", 7.0f, 3.75f, 6.25f, 3.0f).cara(Direction.DOWN, "#0", 7.0f, 3.75f, 6.25f, 4.5f).fin();
        cubo(b, 8.15f, 11.9f, 6.65f, 8.9f, 12.9f, 7.65f).rot(0.0f, Direction.Axis.Y, 7.9f, 11.9f, 5.65f).cara(Direction.NORTH, "#0", 6.25f, 5.5f, 6.5f, 5.75f).cara(Direction.EAST, "#0", 6.25f, 5.75f, 6.5f, 6.0f).cara(Direction.SOUTH, "#0", 6.5f, 5.5f, 6.75f, 5.75f).cara(Direction.WEST, "#0", 6.5f, 5.75f, 6.75f, 6.0f).cara(Direction.UP, "#0", 7.5f, 2.0f, 7.25f, 1.75f).cara(Direction.DOWN, "#0", 7.5f, 2.0f, 7.25f, 2.25f).fin();
        var tr = b.transforms();
        tr.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).translation(0.0f, -0.5f, 0.0f).scale(0.5f, 0.5f, 0.5f).end();
        tr.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).scale(0.5f, 0.5f, 0.5f).end();
        tr.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(-3.0f, 10.0f, 0.0f).translation(1.5f, 2.25f, 0.0f).scale(0.8f, 0.8f, 0.8f).end();
        tr.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0.0f, -180.0f, 0.0f).translation(0.0f, 2.25f, 0.0f).scale(0.8f, 0.8f, 0.8f).end();
        tr.transform(ItemDisplayContext.GROUND).translation(-0.75f, 1.5f, 0.0f).scale(0.7f, 0.7f, 0.7f).end();
        tr.transform(ItemDisplayContext.GUI).rotation(-175.0f, 87.58f, 180.0f).translation(0.5f, 1.5f, 0.0f).end();
        tr.transform(ItemDisplayContext.HEAD).rotation(180.0f, 85.5f, 180.0f).translation(0.0f, -10.75f, 6.25f).end();
        tr.transform(ItemDisplayContext.FIXED).rotation(0.0f, -85.0f, 0.0f).end();
        tr.end();
    }

    private void pancho() {
        BlockModelBuilder b = getBuilder("custom/pancho");
        cubo(b, 4.0f, 3.0f, 6.3f, 12.0f, 3.3f, 7.0f).rot(0.0f, Direction.Axis.Y, 10.0f, 3.0f, 6.0f).cara(Direction.NORTH, "#0", 7.0f, 7.5f, 11.0f, 7.625f).cara(Direction.EAST, "#0", 8.5f, 3.0f, 8.875f, 3.125f).cara(Direction.SOUTH, "#0", 0.0f, 8.0f, 4.0f, 8.125f).cara(Direction.WEST, "#0", 3.5f, 8.5f, 3.875f, 8.625f).cara(Direction.UP, "#0", 4.0f, 6.875f, 0.0f, 6.5f).cara(Direction.DOWN, "#0", 8.0f, 6.5f, 4.0f, 6.875f).fin();
        cubo(b, 4.0f, 3.0f, 8.0f, 12.0f, 3.3f, 8.7f).rot(0.0f, Direction.Axis.Y, 10.0f, 3.0f, 7.7f).cara(Direction.NORTH, "#0", 8.0f, 0.0f, 12.0f, 0.125f).cara(Direction.EAST, "#0", 8.5f, 3.5f, 8.875f, 3.625f).cara(Direction.SOUTH, "#0", 8.0f, 0.5f, 12.0f, 0.625f).cara(Direction.WEST, "#0", 5.5f, 8.5f, 5.875f, 8.625f).cara(Direction.UP, "#0", 4.0f, 7.375f, 0.0f, 7.0f).cara(Direction.DOWN, "#0", 8.0f, 7.0f, 4.0f, 7.375f).fin();
        cubo(b, 4.0f, 1.0f, 6.0f, 12.0f, 3.0f, 7.0f).rot(0.0f, Direction.Axis.Y, 10.0f, 1.0f, 6.0f).cara(Direction.NORTH, "#0", 0.0f, 2.0f, 4.0f, 3.0f).cara(Direction.EAST, "#0", 8.0f, 3.0f, 8.5f, 4.0f).cara(Direction.SOUTH, "#0", 0.0f, 3.0f, 4.0f, 4.0f).cara(Direction.WEST, "#0", 4.0f, 8.0f, 4.5f, 9.0f).cara(Direction.UP, "#0", 8.0f, 4.5f, 4.0f, 4.0f).cara(Direction.DOWN, "#0", 8.0f, 4.5f, 4.0f, 5.0f).fin();
        cubo(b, 3.75f, 1.6f, 6.0f, 4.0f, 2.6f, 9.0f).rot(0.0f, Direction.Axis.Y, 9.0f, 1.6f, 6.0f).cara(Direction.NORTH, "#0", 8.0f, 6.5f, 8.125f, 7.0f).cara(Direction.EAST, "#0", 0.0f, 7.5f, 1.5f, 8.0f).cara(Direction.SOUTH, "#0", 8.0f, 7.0f, 8.125f, 7.5f).cara(Direction.WEST, "#0", 1.5f, 7.5f, 3.0f, 8.0f).cara(Direction.UP, "#0", 7.125f, 9.5f, 7.0f, 8.0f).cara(Direction.DOWN, "#0", 7.625f, 8.0f, 7.5f, 9.5f).fin();
        cubo(b, 12.0f, 1.6f, 6.0f, 12.25f, 2.6f, 9.0f).rot(0.0f, Direction.Axis.Y, 17.25f, 1.6f, 6.0f).cara(Direction.NORTH, "#0", 0.5f, 8.5f, 0.625f, 9.0f).cara(Direction.EAST, "#0", 3.0f, 7.5f, 4.5f, 8.0f).cara(Direction.SOUTH, "#0", 1.0f, 8.5f, 1.125f, 9.0f).cara(Direction.WEST, "#0", 4.5f, 7.5f, 6.0f, 8.0f).cara(Direction.UP, "#0", 8.125f, 9.5f, 8.0f, 8.0f).cara(Direction.DOWN, "#0", 0.125f, 8.5f, 0.0f, 10.0f).fin();
        cubo(b, 4.0f, 1.6f, 5.8f, 12.0f, 2.6f, 6.0f).rot(0.0f, Direction.Axis.Y, 10.0f, 1.6f, 5.0f).cara(Direction.NORTH, "#0", 0.0f, 5.0f, 4.0f, 5.5f).cara(Direction.EAST, "#0", 1.5f, 8.5f, 1.625f, 9.0f).cara(Direction.SOUTH, "#0", 4.0f, 5.0f, 8.0f, 5.5f).cara(Direction.WEST, "#0", 2.0f, 8.5f, 2.125f, 9.0f).cara(Direction.UP, "#0", 12.0f, 4.125f, 8.0f, 4.0f).cara(Direction.DOWN, "#0", 12.0f, 4.5f, 8.0f, 4.625f).fin();
        cubo(b, 4.0f, 1.6f, 9.0f, 12.0f, 2.6f, 9.2f).rot(0.0f, Direction.Axis.Y, 10.0f, 1.6f, 8.2f).cara(Direction.NORTH, "#0", 0.0f, 5.5f, 4.0f, 6.0f).cara(Direction.EAST, "#0", 2.5f, 8.5f, 2.625f, 9.0f).cara(Direction.SOUTH, "#0", 4.0f, 5.5f, 8.0f, 6.0f).cara(Direction.WEST, "#0", 3.0f, 8.5f, 3.125f, 9.0f).cara(Direction.UP, "#0", 12.0f, 5.125f, 8.0f, 5.0f).cara(Direction.DOWN, "#0", 12.0f, 5.5f, 8.0f, 5.625f).fin();
        cubo(b, 4.0f, 0.75f, 7.0f, 12.0f, 2.0f, 8.0f).rot(0.0f, Direction.Axis.Y, 10.0f, 1.0f, 7.0f).cara(Direction.NORTH, "#0", 0.0f, 4.0f, 4.0f, 4.625f).cara(Direction.EAST, "#0", 4.5f, 8.0f, 5.0f, 8.625f).cara(Direction.SOUTH, "#0", 4.0f, 0.0f, 8.0f, 0.625f).cara(Direction.WEST, "#0", 5.0f, 8.0f, 5.5f, 8.625f).cara(Direction.UP, "#0", 4.0f, 6.5f, 0.0f, 6.0f).cara(Direction.DOWN, "#0", 8.0f, 6.0f, 4.0f, 6.5f).fin();
        cubo(b, 3.0f, 2.0f, 7.0f, 13.0f, 3.0f, 8.0f).rot(0.0f, Direction.Axis.Y, 10.0f, 2.0f, 7.0f).cara(Direction.NORTH, "#0", 4.0f, 1.0f, 9.0f, 1.5f).cara(Direction.EAST, "#0", 5.5f, 8.0f, 6.0f, 8.5f).cara(Direction.SOUTH, "#0", 4.0f, 1.5f, 9.0f, 2.0f).cara(Direction.WEST, "#0", 8.0f, 6.0f, 8.5f, 6.5f).cara(Direction.UP, "#0", 9.0f, 2.5f, 4.0f, 2.0f).cara(Direction.DOWN, "#0", 9.0f, 2.5f, 4.0f, 3.0f).fin();
        cubo(b, 4.0f, 1.0f, 8.0f, 12.0f, 3.0f, 9.0f).rot(0.0f, Direction.Axis.Y, 10.0f, 1.0f, 8.0f).cara(Direction.NORTH, "#0", 0.0f, 0.0f, 4.0f, 1.0f).cara(Direction.EAST, "#0", 6.0f, 7.5f, 6.5f, 8.5f).cara(Direction.SOUTH, "#0", 0.0f, 1.0f, 4.0f, 2.0f).cara(Direction.WEST, "#0", 6.5f, 7.5f, 7.0f, 8.5f).cara(Direction.UP, "#0", 8.0f, 3.5f, 4.0f, 3.0f).cara(Direction.DOWN, "#0", 8.0f, 3.5f, 4.0f, 4.0f).fin();
        var tr = b.transforms();
        tr.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(93.0f, 65.0f, 14.0f).translation(0.0f, 1.5f, 7.75f).end();
        tr.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(93.0f, 65.0f, 14.0f).translation(0.0f, 1.5f, 7.75f).end();
        tr.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0.0f, -18.0f, 0.0f).translation(5.25f, 5.0f, 0.0f).end();
        tr.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0.0f, -25.0f, 0.0f).translation(4.0f, 5.0f, 0.0f).end();
        tr.transform(ItemDisplayContext.GROUND).translation(0.0f, 6.0f, 0.0f).end();
        tr.transform(ItemDisplayContext.GUI).rotation(68.02f, -39.63f, -14.43f).translation(1.0f, 0.75f, 0.0f).end();
        tr.transform(ItemDisplayContext.HEAD).rotation(157.57f, 87.96f, -137.35f).translation(0.0f, 12.0f, -6.5f).end();
        tr.transform(ItemDisplayContext.FIXED).translation(0.0f, 4.5f, 0.0f).end();
        tr.end();
    }

    private void sandwichito() {
        BlockModelBuilder b = getBuilder("custom/sandwichito");
        cubo(b, 6.0f, 0.0f, 6.0f, 11.0f, 1.0f, 10.0f).rot(0.0f, Direction.Axis.Y, 8.5f, 1.0f, 8.0f).cara(Direction.NORTH, "#0", 0.0f, 8.0f, 5.0f, 9.0f).cara(Direction.EAST, "#0", 0.0f, 10.0f, 4.0f, 11.0f).cara(Direction.SOUTH, "#0", 5.0f, 8.0f, 10.0f, 9.0f).cara(Direction.WEST, "#0", 10.0f, 0.0f, 14.0f, 1.0f).cara(Direction.UP, "#0", 5.0f, 4.0f, 0.0f, 0.0f).cara(Direction.DOWN, "#0", 5.0f, 4.0f, 0.0f, 8.0f).fin();
        cubo(b, 6.0f, 1.0f, 6.0f, 11.0f, 2.0f, 10.0f).rot(0.0f, Direction.Axis.Y, 8.5f, 1.0f, 8.0f).cara(Direction.NORTH, "#0", 0.0f, 9.0f, 5.0f, 10.0f).cara(Direction.EAST, "#0", 10.0f, 1.0f, 14.0f, 2.0f).cara(Direction.SOUTH, "#0", 5.0f, 9.0f, 10.0f, 10.0f).cara(Direction.WEST, "#0", 10.0f, 2.0f, 14.0f, 3.0f).cara(Direction.UP, "#0", 10.0f, 4.0f, 5.0f, 0.0f).cara(Direction.DOWN, "#0", 10.0f, 4.0f, 5.0f, 8.0f).fin();
        var tr = b.transforms();
        tr.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(91.0f, 0.0f, 0.0f).translation(-0.5f, 1.5f, 7.75f).end();
        tr.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(91.0f, 0.0f, 0.0f).translation(0.75f, 1.5f, 8.25f).end();
        tr.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).translation(2.0f, 8.0f, 0.0f).end();
        tr.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).translation(2.0f, 8.0f, 0.0f).end();
        tr.transform(ItemDisplayContext.GROUND).translation(0.0f, 3.75f, 0.0f).end();
        tr.transform(ItemDisplayContext.GUI).rotation(12.0f, 45.0f, 0.0f).translation(-0.5f, 13.25f, 4.75f).scale(2.0f, 2.0f, 2.0f).end();
        tr.transform(ItemDisplayContext.HEAD).translation(0.0f, 14.0f, 0.0f).end();
        tr.transform(ItemDisplayContext.FIXED).translation(0.0f, 13.25f, 0.0f).scale(1.83f, 2.1f, 1.0f).end();
        tr.end();
    }
}
