package nadiendev.ultimatefoods.argentina.parrilla;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** Dibuja la comida que se esta cocinando, apoyada sobre la reja de la parrilla. */
public class ParrillaRenderer implements BlockEntityRenderer<ParrillaBlockEntity> {

    private static final float ITEM_SCALE = 0.42F;
    /** Un pelin arriba de la reja para que no parpadee contra la textura. */
    private static final double LIFT = 0.02;

    public ParrillaRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ParrillaBlockEntity grill, float partialTick, PoseStack pose, MultiBufferSource buffer,
                       int packedLight, int packedOverlay) {
        Level level = grill.getLevel();
        if (level == null) {
            return;
        }
        Direction facing = grill.getBlockState().getValue(ParrillaBlock.FACING);
        // Adentro del bloque la luz es 0; se toma la del aire de arriba.
        int light = LevelRenderer.getLightColor(level, grill.getBlockPos().above());

        for (int slot = 0; slot < ParrillaBlockEntity.SLOTS; slot++) {
            ItemStack stack = grill.getItem(slot);
            if (stack.isEmpty()) {
                continue;
            }
            Vec3 center = ParrillaBlockEntity.slotCenter(slot, facing);
            pose.pushPose();
            pose.translate(center.x, ParrillaBlock.TOP_HEIGHT + LIFT, center.z);
            pose.mulPose(Axis.YP.rotationDegrees(-facing.toYRot() + 20.0F * (slot % 3 - 1)));
            pose.mulPose(Axis.XP.rotationDegrees(90.0F));
            pose.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, light,
                    OverlayTexture.NO_OVERLAY, pose, buffer, level, (int) grill.getBlockPos().asLong() + slot);
            pose.popPose();
        }
    }
}
