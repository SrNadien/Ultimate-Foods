package nadiendev.ultimatefoods.tags;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.blocks.BlocksAdds;
import nadiendev.ultimatefoods.blocks.NadieniteOreBlock;
import nadiendev.ultimatefoods.items.ItemsAdds;
import nadiendev.ultimatefoods.items.armor.ArmorAdds;
import nadiendev.ultimatefoods.items.tools.ToolsAdds;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * ModItemTags
 * By NadienDev
 * Clase para definir y generar las etiquetas (tags) de ítems del mod
 * Facilita la organización de ítems en categorías como "lingotes de nadienite", "bloques de almacenamiento", "herramientas de nadienite", etc.
 */
public class ModItemTags {

    private ModItemTags() {
    }

    public static class Items {

        private Items() {
        }

        // Ingots Tags
        public static final TagKey<Item> INGOTS_NADIENITE = commonTag("ingots/nadienite");
        public static final TagKey<Item> INGOTS_STEEL = commonTag("ingots/steel");

        // Storage Blocks Tags
        public static final TagKey<Item> STORAGE_BLOCKS_NADIENITE = commonTag("storage_blocks/nadienite");
        public static final TagKey<Item> STORAGE_BLOCKS_STEEL = commonTag("storage_blocks/steel");

        // Ores Tags
        public static final TagKey<Item> ORES_NADIENITE = commonTag("ores/nadienite");

        private static TagKey<Item> commonTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
        }

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, name));
        }
    }

    // DATAGEN PROVIDER
    public static class Provider extends ItemTagsProvider {

        public Provider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                       CompletableFuture<TagLookup<Block>> blockTagsProvider,
                       @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, blockTagsProvider, UltimateFoodsCore.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            // ========== LINGOTES ==========
            
            // Tag para NADIENITE_INGOT
            this.tag(Items.INGOTS_NADIENITE)
                .add(ItemsAdds.NADIENITE_INGOT.get());
            
            // Tag para STEEL_INGOT
            this.tag(Items.INGOTS_STEEL)
                .add(ItemsAdds.STEEL_INGOT.get());

            // ========== BLOQUES DE ALMACENAMIENTO ==========

            // Tag para NADIENITE_BLOCK (como item)
            this.tag(Items.STORAGE_BLOCKS_NADIENITE)
                .add(BlocksAdds.NADIENITE_BLOCK.get().asItem());

            // Tag para STEEL_BLOCK (como item)
            this.tag(Items.STORAGE_BLOCKS_STEEL)
                .add(BlocksAdds.STEEL_BLOCK.get().asItem());

            // ========== MINERALES ==========

            // Tag para OREs de NADIENITE
            this.tag(Items.ORES_NADIENITE)
                .add(NadieniteOreBlock.NADIENITE_ORE.get().asItem())
                .add(NadieniteOreBlock.DEEPSLATE_NADIENITE_ORE.get().asItem());

            // ========== HERRAMIENTAS - TAGS DE MINECRAFT ==========

            // Tag: swords - Espadas
            this.tag(ItemTags.SWORDS)
                .add(ToolsAdds.NADIENITE_SWORD.get());



            // ========== ARMADURA - TAGS DE MINECRAFT ==========

            // Tag: head_armor - Cascos
            this.tag(ItemTags.HEAD_ARMOR)
                .add(ArmorAdds.NADIENITE_HELMET.get());

            // Tag: chest_armor - Pecheras
            this.tag(ItemTags.CHEST_ARMOR)
                .add(ArmorAdds.NADIENITE_CHESTPLATE.get());

            // Tag: leg_armor - Pantalones
            this.tag(ItemTags.LEG_ARMOR)
                .add(ArmorAdds.NADIENITE_LEGGINGS.get());

            // Tag: foot_armor - Botas
            this.tag(ItemTags.FOOT_ARMOR)
                .add(ArmorAdds.NADIENITE_BOOTS.get());

            // Tag: trimmable_armor - Armadura que puede recibir adornos
            this.tag(ItemTags.TRIMMABLE_ARMOR)
                .add(ArmorAdds.NADIENITE_HELMET.get())
                .add(ArmorAdds.NADIENITE_CHESTPLATE.get())
                .add(ArmorAdds.NADIENITE_LEGGINGS.get())
                .add(ArmorAdds.NADIENITE_BOOTS.get());
        }
    }
}