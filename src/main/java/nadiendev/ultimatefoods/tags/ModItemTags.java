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

        // Enchantment Tags - AutoSmelt (por separado para compatibilidad con datapacks)
        public static final TagKey<Item> AUTOSMELT_ENCHANTABLE_MINING   = tag("enchantable/autosmelt_mining");
        public static final TagKey<Item> AUTOSMELT_ENCHANTABLE_AXE      = tag("enchantable/autosmelt_axe");
        public static final TagKey<Item> AUTOSMELT_ENCHANTABLE_ALL      = tag("enchantable/autosmelt_all");
        public static final TagKey<Item> AUTOSMELT_ENCHANTABLE_AXES     = tag("enchantable/autosmelt_axes");
        public static final TagKey<Item> AUTOSMELT_ENCHANTABLE_SHOVELS  = tag("enchantable/autosmelt_shovels");
        public static final TagKey<Item> AUTOSMELT_ENCHANTABLE_PICKAXES = tag("enchantable/autosmelt_pickaxes");
        public static final TagKey<Item> AUTOSMELT_ENCHANTABLE          = tag("enchantable/autosmelt");

        // Enchantment Tags - XPBoost (por separado para compatibilidad con datapacks)
        public static final TagKey<Item> XPBOOST_ENCHANTABLE_SWORD      = tag("enchantable/xpboost_sword");
        public static final TagKey<Item> XPBOOST_ENCHANTABLE_SWORDS     = tag("enchantable/xpboost_swords");
        public static final TagKey<Item> XPBOOST_ENCHANTABLE_AXES       = tag("enchantable/xpboost_axes");
        public static final TagKey<Item> XPBOOST_ENCHANTABLE            = tag("enchantable/xpboost");

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

            // ========== ENCANTAMIENTOS ==========

            // AutoSmelt - cada tag de vanilla por separado para compatibilidad con datapacks
            this.tag(Items.AUTOSMELT_ENCHANTABLE_MINING)   .addTag(ItemTags.MINING_ENCHANTABLE);
            this.tag(Items.AUTOSMELT_ENCHANTABLE_AXE)      .addTag(ItemTags.AXES);
            // AUTOSMELT_ENCHANTABLE_ALL eliminado: ItemTags.ENCHANTABLE no existe en 1.21
            this.tag(Items.AUTOSMELT_ENCHANTABLE_AXES)     .addTag(ItemTags.AXES);
            this.tag(Items.AUTOSMELT_ENCHANTABLE_SHOVELS)  .addTag(ItemTags.SHOVELS);
            this.tag(Items.AUTOSMELT_ENCHANTABLE_PICKAXES) .addTag(ItemTags.PICKAXES);
            // Tag principal que agrupa todos los anteriores
            this.tag(Items.AUTOSMELT_ENCHANTABLE)
                .addTag(Items.AUTOSMELT_ENCHANTABLE_MINING)
                .addTag(Items.AUTOSMELT_ENCHANTABLE_AXE)
                                .addTag(Items.AUTOSMELT_ENCHANTABLE_AXES)
                .addTag(Items.AUTOSMELT_ENCHANTABLE_SHOVELS)
                .addTag(Items.AUTOSMELT_ENCHANTABLE_PICKAXES);

            // XPBoost - cada tag de vanilla por separado para compatibilidad con datapacks
            this.tag(Items.XPBOOST_ENCHANTABLE_SWORD)  .addTag(ItemTags.SWORD_ENCHANTABLE);
            this.tag(Items.XPBOOST_ENCHANTABLE_SWORDS) .addTag(ItemTags.SWORDS);
            this.tag(Items.XPBOOST_ENCHANTABLE_AXES)   .addTag(ItemTags.AXES);
            this.tag(Items.XPBOOST_ENCHANTABLE_AXES)   .add(ToolsAdds.NADIENITE_SWORD.get());
            this.tag(Items.XPBOOST_ENCHANTABLE_AXES)   .add(ToolsAdds.NADIENITE_AXE.get());
            // Tag principal que agrupa todos los anteriores
            this.tag(Items.XPBOOST_ENCHANTABLE)
                .addTag(Items.XPBOOST_ENCHANTABLE_SWORD)
                .addTag(Items.XPBOOST_ENCHANTABLE_SWORDS)
                .addTag(Items.XPBOOST_ENCHANTABLE_AXES);
            // Tag: pickaxes - Picos
            this.tag(ItemTags.PICKAXES)
                .add(ToolsAdds.NADIENITE_PICKAXE.get());

            // Tag: axes - Hachas
            this.tag(ItemTags.AXES)
                .add(ToolsAdds.NADIENITE_AXE.get());

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