package nadiendev.ultimatefoods.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record ModFood(FoodProperties properties, Consumable consumable) {

    public static final float NORMAL_EAT_SECONDS = 1.6F;
    public static final float FAST_EAT_SECONDS = 0.8F;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final FoodProperties.Builder food = new FoodProperties.Builder();
        private final List<ConsumeEffect> effects = new ArrayList<>();
        private float eatSeconds = NORMAL_EAT_SECONDS;

        public Builder nutrition(int nutrition) {
            this.food.nutrition(nutrition);
            return this;
        }

        public Builder saturationModifier(float saturation) {
            this.food.saturationModifier(saturation);
            return this;
        }

        public Builder alwaysEdible() {
            this.food.alwaysEdible();
            return this;
        }

        public Builder fast() {
            this.eatSeconds = FAST_EAT_SECONDS;
            return this;
        }

        public Builder effect(Supplier<MobEffectInstance> effect, float probability) {
            this.effects.add(new ApplyStatusEffectsConsumeEffect(effect.get(), probability));
            return this;
        }

        public ModFood build() {
            Consumable.Builder consumable = Consumable.builder().consumeSeconds(this.eatSeconds);
            for (ConsumeEffect effect : this.effects) {
                consumable.onConsume(effect);
            }
            return new ModFood(this.food.build(), consumable.build());
        }
    }
}
