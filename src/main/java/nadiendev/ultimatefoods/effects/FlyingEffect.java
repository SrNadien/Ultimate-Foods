package nadiendev.ultimatefoods.effects;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.neoforged.neoforge.common.NeoForgeMod;

/**
 * Flying Effect
 * By NadienDev
 */
public class FlyingEffect extends MobEffect {

    public FlyingEffect() {
        super(MobEffectCategory.BENEFICIAL, ChatFormatting.AQUA.getColor());
        this.addAttributeModifier(
            NeoForgeMod.CREATIVE_FLIGHT, 
            ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "flying"), 
            1.0, 
            Operation.ADD_VALUE
        );
    }

    @Override
    public void addAttributeModifiers(AttributeMap pAttributeMap, int pAmplifier) {
        super.addAttributeModifiers(pAttributeMap, 0);
    }

}