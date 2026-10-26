package nadiendev.ultimatefoods.fisura.parrilla;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Dibuja la comida que se esta cocinando, apoyada sobre la reja de la parrilla. En 26.1 los
 * renderers trabajan en dos pasos: {@link #extractRenderState} copia del block entity lo que hace
 * falta (hacia donde mira, la luz y cada item ya resuelto a su modelo) y {@link #submit} lo dibuja.
 */
public class ParrillaRenderer implements BlockEntityRenderer<ParrillaBlockEntity, ParrillaRenderer.Estado> {

    private static final float ITEM_SCALE = 0.42F;
    /** Un pelin arriba de la reja para que no parpadee contra la textura. */
    private static final double LIFT = 0.02;

    private final ItemModelResolver itemModelResolver;

    public ParrillaRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public Estado createRenderState() {
        return new Estado();
    }

    @Override
    public void extractRenderState(ParrillaBlockEntity grill, Estado state, float partialTicks, Vec3 cameraPosition,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(grill, state, partialTicks, cameraPosition, breakProgress);
        Level level = grill.getLevel();
        state.facing = grill.getBlockState().getValue(ParrillaBlock.FACING);
        // Adentro del bloque la luz es 0; se toma la del aire de arriba.
        if (level != null) {
            state.lightCoords = LevelRenderer.getLightCoords(level, grill.getBlockPos().above());
        }
        int seed = (int) grill.getBlockPos().asLong();
        state.items = new ArrayList<>(ParrillaBlockEntity.SLOTS);
        for (int slot = 0; slot < ParrillaBlockEntity.SLOTS; slot++) {
            ItemStackRenderState itemState = new ItemStackRenderState();
            itemModelResolver.updateForTopItem(itemState, grill.getItem(slot), ItemDisplayContext.FIXED, level, null, seed + slot);
            state.items.add(itemState);
        }
    }

    @Override
    public void submit(Estado state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
        Direction facing = state.facing;
        for (int slot = 0; slot < state.items.size(); slot++) {
            ItemStackRenderState itemState = state.items.get(slot);
            if (itemState.isEmpty()) {
                continue;
            }
            Vec3 center = ParrillaBlockEntity.slotCenter(slot, facing);
            pose.pushPose();
            pose.translate(center.x, ParrillaBlock.TOP_HEIGHT + LIFT, center.z);
            pose.mulPose(Axis.YP.rotationDegrees(-facing.toYRot() + 20.0F * (slot % 3 - 1)));
            pose.mulPose(Axis.XP.rotationDegrees(90.0F));
            pose.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
            itemState.submit(pose, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            pose.popPose();
        }
    }

    /** Lo que el renderer necesita de la parrilla en cada cuadro. */
    public static class Estado extends BlockEntityRenderState {
        public Direction facing = Direction.NORTH;
        public List<ItemStackRenderState> items = List.of();
    }
}
