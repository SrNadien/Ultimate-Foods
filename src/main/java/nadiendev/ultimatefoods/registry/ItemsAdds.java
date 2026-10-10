package nadiendev.ultimatefoods.registry;

import nadiendev.ultimatefoods.items.CustomFoodItem;
import nadiendev.ultimatefoods.items.PoopItem;
import nadiendev.ultimatefoods.items.SuperChileItem;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ItemsAdds {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(BuiltInRegistries.ITEM, UltimateFoodsCore.MOD_ID);

    public static final DeferredHolder<Item, Item> MONSTER = registerFoodItem(
            "monster",
            () -> new CustomFoodItem(
                    createFoodProperties(6, 0.6f)
                            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 1), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 400, 2), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 400, 2), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 400, 2), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.INVISIBILITY, 400, 2), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 400, 2), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.HEAL, 400, 2), 1.0f)
                            .build(),
                    SonidosReproducibles.MONSTER_EAT
            )
    );

    public static final DeferredHolder<Item, Item> CAJITA_FELIZ = registerFoodItem(
            "cajita_feliz",
            () -> new CustomFoodItem(
                    createFoodProperties(8, 0.8f)
                            .effect(() -> new MobEffectInstance(MobEffects.INVISIBILITY, 400, 1), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 800, 2), 1.0f)
                            .build(),
                    SonidosReproducibles.ERA_PAL_NENE
            )
    );

    public static final DeferredHolder<Item, Item> SUPER_ENERGY_DRINK = registerFoodItem(
            "super_energy_drink",
            () -> new CustomFoodItem(
                    createFoodProperties(4, 0.5f)
                            .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 3), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.JUMP, 300, 3), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.WATER_BREATHING, 300, 3), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 300, 3), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 3), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.INVISIBILITY, 300, 3), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 300, 2), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 300, 3), 1.0f)
                            .build(),
                    SonidosReproducibles.SUPER_ENERGY_DRINK
            )
    );

    public static final DeferredHolder<Item, Item> DORITOS = registerFoodItem(
            "doritos",
            () -> new CustomFoodItem(
                    createFoodProperties(3, 0.3f)
                            .fast()
                            .build(),
                    SonidosReproducibles.DORITOS_EAT
            )
    );

    public static final DeferredHolder<Item, Item> POOP = registerFoodItem(
        "poop",
        () -> new PoopItem(
                createFoodProperties(1, 0.1f)
                        .effect(() -> new MobEffectInstance(EffectsAdds.QUE_GUARRO, 6000, 0), 1.0f)
                        .build(),
                SonidosReproducibles.POOP_EAT,
                6000
        )
    );

    public static final DeferredHolder<Item, Item> SUPERPOOP = registerFoodItem(
        "superpoop",
        () -> new PoopItem(
                createFoodProperties(40, 40.0f)
                        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 12000, 10), 1.0f)
                        .effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 12000, 10), 1.0f)
                        .build(),
                SonidosReproducibles.POOP_EAT,
                12000
        )
    );

    public static final DeferredHolder<Item, Item> CHOCOLATADA = ITEMS.register(
        "chocolatada",
        () -> new Item(new Item.Properties()
                .stacksTo(64)
                .rarity(Rarity.COMMON)
                .food(new FoodProperties.Builder()
                        .nutrition(20)
                        .saturationModifier(8f)
                        .build()
                )
        )
    );

    public static final DeferredHolder<Item, Item> BAKED_TORTILLA = ITEMS.register(
        "baked_tortilla",
        () -> new Item(new Item.Properties()
                .stacksTo(64)
                .rarity(Rarity.COMMON)
                .food(new FoodProperties.Builder()
                        .nutrition(20)
                        .saturationModifier(8f)
                        .build()
                )
        )
    );

    public static final DeferredHolder<Item, Item> TORTILLA = ITEMS.register(
        "tortilla",
        () -> new Item(new Item.Properties()
                .stacksTo(64)
                .rarity(Rarity.COMMON)
                .food(new FoodProperties.Builder()
                        .nutrition(20)
                        .saturationModifier(8f)
                        .build()
                )
        )
    );

    public static final DeferredHolder<Item, Item> TACO = registerCustomFood(
         "taco",
              20,
              8f,
              64,
              Rarity.COMMON,
              createFoodProperties(20, 8f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 12000, 10), 1.0f)
            .build(),
            SonidosReproducibles.RICKROLL
    );

    public static final DeferredHolder<Item, Item> SUPER_CHILE = ITEMS.register(
         "super_chile",
             () -> new SuperChileItem(new Item.Properties()
            .stacksTo(64)
            .rarity(Rarity.COMMON)
            .food(new FoodProperties.Builder()
                    .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 12000, 10), 1.0f)
                    .nutrition(10)
                    .saturationModifier(3f)
                    .build()
              )
            )
         );

    public static final DeferredHolder<Item, Item> RAW_HAMBURGUER_MEAT = ITEMS.register(
        "raw_hamburger_meat",
        () -> new Item(new Item.Properties()
                .stacksTo(64)
                .rarity(Rarity.COMMON)
                .food(new FoodProperties.Builder()
                        .nutrition(2)
                        .saturationModifier(0.1f)
                        .build()
                )
        )
    );

    public static final DeferredHolder<Item, Item> COOKED_HAMBURGUER_MEAT = ITEMS.register(
        "cooked_hamburger_meat",
        () -> new Item(new Item.Properties()
                .stacksTo(64)
                .rarity(Rarity.COMMON)
                .food(new FoodProperties.Builder()
                        .nutrition(6)
                        .saturationModifier(0.8f)
                        .build()
                )
        )
    );

    public static final DeferredHolder<Item, Item> BURGER = ITEMS.register(
        "burger",
        () -> new Item(new Item.Properties()
                .stacksTo(64)
                .rarity(Rarity.EPIC)
                .food(new FoodProperties.Builder()
                        .nutrition(6)
                        .saturationModifier(1.2f)
                        .effect(() -> new MobEffectInstance(MobEffects.HEAL, 1, 5), 1.0f)
                        .build()
                )
        )
    );

    private static FoodProperties.Builder createFoodProperties(int nutrition, float saturation) {
        return new FoodProperties.Builder()
                .nutrition(nutrition)
                .saturationModifier(saturation);
    }

    private static DeferredHolder<Item, Item> registerFoodItem(String name, Supplier<Item> item) {
        return ITEMS.register(name, item);
    }

    public static DeferredHolder<Item, Item> registerSimpleFood(String name, int nutrition, float saturation) {
        return ITEMS.register(name, () -> new Item(new Item.Properties()
                .food(createFoodProperties(nutrition, saturation).build())));
    }

    public static DeferredHolder<Item, Item> registerCustomFood(String name, int nutrition, float saturation, int stackSize, Rarity rarity, FoodProperties foodProperties, DeferredHolder<SoundEvent, SoundEvent> sound) {
        return ITEMS.register(name, () -> new CustomFoodItem(foodProperties, sound));
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
