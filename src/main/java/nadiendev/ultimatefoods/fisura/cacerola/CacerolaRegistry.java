package nadiendev.ultimatefoods.fisura.cacerola;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/** Registro de la Cacerola Essen: bloque, item, block entity y tipo de receta propio. */
public final class CacerolaRegistry {

    /** Bloques que calientan la cacerola si estan debajo (los que tienen LIT tienen que estar prendidos). */
    public static final TagKey<Block> HEAT_SOURCES =
            TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "cacerola_heat_sources"));

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(UltimateFoodsCore.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(UltimateFoodsCore.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, UltimateFoodsCore.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, UltimateFoodsCore.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, UltimateFoodsCore.MOD_ID);

    public static final DeferredBlock<CacerolaBlock> CACEROLA_ESSEN = BLOCKS.registerBlock(
            "cacerola_essen",
            CacerolaBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .strength(2.0F, 4.0F)
                    .sound(SoundType.LANTERN)
                    .requiresCorrectToolForDrops()
                    .noOcclusion());

    public static final DeferredItem<BlockItem> CACEROLA_ESSEN_ITEM = ITEMS.registerSimpleBlockItem(CACEROLA_ESSEN);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CacerolaBlockEntity>> CACEROLA_BE =
            BLOCK_ENTITIES.register("cacerola_essen", () ->
                    BlockEntityType.Builder.of(CacerolaBlockEntity::new, CACEROLA_ESSEN.get()).build(null));

    public static final DeferredHolder<RecipeType<?>, RecipeType<CacerolaRecipe>> CACEROLA_TYPE =
            RECIPE_TYPES.register("cacerola", () -> RecipeType.simple(
                    ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "cacerola")));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CacerolaRecipe>> CACEROLA_SERIALIZER =
            RECIPE_SERIALIZERS.register("cacerola", CacerolaRecipe.Serializer::new);

    private CacerolaRegistry() {
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        RECIPE_TYPES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
    }
}
