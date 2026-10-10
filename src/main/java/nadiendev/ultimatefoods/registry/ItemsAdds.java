package nadiendev.ultimatefoods.registry;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.items.CustomFoodItem;
import nadiendev.ultimatefoods.items.ModFood;
import nadiendev.ultimatefoods.items.PoopItem;
import nadiendev.ultimatefoods.items.SuperChileItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ItemsAdds {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(UltimateFoodsCore.MOD_ID);

    public static final DeferredHolder<Item, Item> MONSTER = registerFoodItem(
            "monster",
            () -> new CustomFoodItem(props(),
                    createFoodProperties(6, 0.6f)
                            .effect(() -> new MobEffectInstance(MobEffects.SPEED, 400, 1), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 400, 2), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 400, 2), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.NAUSEA, 400, 2), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.INVISIBILITY, 400, 2), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 400, 2), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.INSTANT_HEALTH, 400, 2), 1.0f)
                            .build(),
                    SonidosReproducibles.MONSTER_EAT
            )
    );

    public static final DeferredHolder<Item, Item> CAJITA_FELIZ = registerFoodItem(
            "cajita_feliz",
            () -> new CustomFoodItem(props(),
                    createFoodProperties(8, 0.8f)
                            .effect(() -> new MobEffectInstance(MobEffects.INVISIBILITY, 400, 1), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.SPEED, 800, 2), 1.0f)
                            .build(),
                    SonidosReproducibles.ERA_PAL_NENE
            )
    );

    public static final DeferredHolder<Item, Item> SUPER_ENERGY_DRINK = registerFoodItem(
            "super_energy_drink",
            () -> new CustomFoodItem(props(),
                    createFoodProperties(4, 0.5f)
                            .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 3), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.JUMP_BOOST, 300, 3), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.WATER_BREATHING, 300, 3), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.STRENGTH, 300, 3), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.SPEED, 300, 3), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.INVISIBILITY, 300, 3), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 300, 2), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.HASTE, 300, 3), 1.0f)
                            .build(),
                    SonidosReproducibles.SUPER_ENERGY_DRINK
            )
    );

    public static final DeferredHolder<Item, Item> DORITOS = registerFoodItem(
            "doritos",
            () -> new CustomFoodItem(props(),
                    createFoodProperties(3, 0.3f)
                            .fast()
                            .build(),
                    SonidosReproducibles.DORITOS_EAT
            )
    );

    public static final DeferredHolder<Item, Item> POOP = registerFoodItem(
        "poop",
        () -> new PoopItem(props(),
                createFoodProperties(1, 0.1f)
                        .effect(() -> new MobEffectInstance(EffectsAdds.QUE_GUARRO, 6000, 0), 1.0f)
                        .build(),
                SonidosReproducibles.POOP_EAT,
                6000
        )
    );

    public static final DeferredHolder<Item, Item> SUPERPOOP = registerFoodItem(
        "superpoop",
        () -> new PoopItem(props(),
                createFoodProperties(40, 40.0f)
                        .effect(() -> new MobEffectInstance(MobEffects.SPEED, 12000, 10), 1.0f)
                        .effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 12000, 10), 1.0f)
                        .build(),
                SonidosReproducibles.POOP_EAT,
                12000
        )
    );

    public static final DeferredHolder<Item, Item> CHOCOLATADA = reg(
        "chocolatada",
        () -> new Item(props()
                .stacksTo(64)
                .rarity(Rarity.COMMON)
                .food(new FoodProperties.Builder()
                        .nutrition(20)
                        .saturationModifier(8f)
                        .build()
                )
        )
    );

    public static final DeferredHolder<Item, Item> BAKED_TORTILLA = reg(
        "baked_tortilla",
        () -> new Item(props()
                .stacksTo(64)
                .rarity(Rarity.COMMON)
                .food(new FoodProperties.Builder()
                        .nutrition(20)
                        .saturationModifier(8f)
                        .build()
                )
        )
    );

    public static final DeferredHolder<Item, Item> TORTILLA = reg(
        "tortilla",
        () -> new Item(props()
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
              createFoodProperties(20, 8f)
            .effect(() -> new MobEffectInstance(MobEffects.SPEED, 12000, 10), 1.0f)
            .build(),
            SonidosReproducibles.RICKROLL
    );

    public static final DeferredHolder<Item, Item> SUPER_CHILE = reg(
         "super_chile",
             () -> new SuperChileItem(props()
            .stacksTo(64)
            .rarity(Rarity.COMMON)
            .food(ModFood.builder()
                    .effect(() -> new MobEffectInstance(MobEffects.NAUSEA, 12000, 10), 1.0f)
                    .nutrition(10)
                    .saturationModifier(3f)
                    .build().properties()
              )
            )
         );

    public static final DeferredHolder<Item, Item> RAW_HAMBURGUER_MEAT = reg(
        "raw_hamburger_meat",
        () -> new Item(props()
                .stacksTo(64)
                .rarity(Rarity.COMMON)
                .food(new FoodProperties.Builder()
                        .nutrition(2)
                        .saturationModifier(0.1f)
                        .build()
                )
        )
    );

    public static final DeferredHolder<Item, Item> COOKED_HAMBURGUER_MEAT = reg(
        "cooked_hamburger_meat",
        () -> new Item(props()
                .stacksTo(64)
                .rarity(Rarity.COMMON)
                .food(new FoodProperties.Builder()
                        .nutrition(6)
                        .saturationModifier(0.8f)
                        .build()
                )
        )
    );

    public static final DeferredHolder<Item, Item> BURGER = reg(
        "burger",
        () -> new Item(props()
                .stacksTo(64)
                .rarity(Rarity.EPIC)
                .food(ModFood.builder()
                        .nutrition(6)
                        .saturationModifier(1.2f)
                        .effect(() -> new MobEffectInstance(MobEffects.INSTANT_HEALTH, 1, 5), 1.0f)
                        .build().properties()
                )
        )
    );

    private static ModFood.Builder createFoodProperties(int nutrition, float saturation) {
        return ModFood.builder()
                .nutrition(nutrition)
                .saturationModifier(saturation);
    }

    private static final ThreadLocal<Item.Properties> CURRENT_PROPERTIES = new ThreadLocal<>();

    private static Item.Properties props() {
        Item.Properties properties = CURRENT_PROPERTIES.get();
        return properties != null ? properties : new Item.Properties();
    }

    private static <T extends Item> DeferredHolder<Item, T> reg(String name, Supplier<T> supplier) {
        return ITEMS.registerItem(name, properties -> {
            CURRENT_PROPERTIES.set(properties);
            try {
                return supplier.get();
            } finally {
                CURRENT_PROPERTIES.remove();
            }
        });
    }

    private static DeferredHolder<Item, Item> registerFoodItem(String name, Supplier<Item> item) {
        return reg(name, item);
    }

    private static DeferredHolder<Item, Item> registerCustomFood(String name, ModFood foodProperties, DeferredHolder<SoundEvent, SoundEvent> sound) {
        return reg(name, () -> new CustomFoodItem(props(), foodProperties, sound));
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
