package nadiendev.ultimatefoods.tags;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.blocks.BlocksAdds;
import nadiendev.ultimatefoods.blocks.NadieniteOreBlock;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * ModBlockTags
 * By NadienDev
 * Clase para definir y generar las etiquetas (tags) de bloques del mod
 * Facilita la organización de bloques en categorías como "necesita pico de nadienite", "bloques de almacenamiento", etc.
 */
public class ModBlockTags {

    private ModBlockTags() {
    }

    public static class Blocks {

        private Blocks() {
        }

        // Custom tag para herramientas de Nadienite
        public static final TagKey<Block> NEEDS_NADIENITE_TOOL = tag("needs_nadienite_tool");
        public static final TagKey<Block> INCORRECT_FOR_NADIENITE_TOOL = tag("incorrect_for_nadienite_tool");
        
        // Storage Blocks Tags
        public static final TagKey<Block> STORAGE_BLOCKS_NADIENITE = commonTag("storage_blocks/nadienite");
        public static final TagKey<Block> STORAGE_BLOCKS_STEEL = commonTag("storage_blocks/steel");

        private static TagKey<Block> commonTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
        }

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, name));
        }
    }

    // DATAGEN PROVIDER
    public static class Provider extends BlockTagsProvider {

        public Provider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, UltimateFoodsCore.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            
              // ============================================
             // NEEDS_NADIENITE_TOOL - Tag custom del mod
            // Bloques que pueden ser minados con pico de Nadienite
            // ============================================
            this.tag(Blocks.NEEDS_NADIENITE_TOOL)
                .addTag(Tags.Blocks.NEEDS_NETHERITE_TOOL)
                .add(BlocksAdds.NADIENITE_BLOCK.get())   // Incluye todos los bloques que necesitan netherite
                .add(NadieniteOreBlock.NADIENITE_ORE.get()) // Nadienite Ore necesita Nadienite Tool
                .add(NadieniteOreBlock.DEEPSLATE_NADIENITE_ORE.get());

            // ============================================
            // NEEDS_NETHERITE_TOOL
            // Bloques de Nadienite que necesitan pico de netherite (o superior)
            // ============================================
            this.tag(Tags.Blocks.NEEDS_NETHERITE_TOOL)
                .add(BlocksAdds.NADIENITE_BLOCK.get())
                .add(NadieniteOreBlock.NADIENITE_ORE.get())
                .add(NadieniteOreBlock.DEEPSLATE_NADIENITE_ORE.get());
             // ============================================
            // INCORRECT_FOR_NADIENITE_TOOL - Tag custom del mod
            // Bloques que no pueden ser minados con pico de Nadienite (aunque sean de Nadienite) 
            // =============================================
            this.tag(Blocks.INCORRECT_FOR_NADIENITE_TOOL);



            // ============================================
            // INCORRECT_FOR tiers inferiores
            // SOLO bloques de Nadienite - Steel y Sexy NO están aquí porque funcionan bien
            // ============================================
            this.tag(BlockTags.INCORRECT_FOR_WOODEN_TOOL)
                .add(NadieniteOreBlock.NADIENITE_ORE.get())
                .add(NadieniteOreBlock.DEEPSLATE_NADIENITE_ORE.get())
                .add(BlocksAdds.NADIENITE_BLOCK.get());
            
            
            this.tag(BlockTags.INCORRECT_FOR_STONE_TOOL)
                .add(NadieniteOreBlock.NADIENITE_ORE.get())
                .add(NadieniteOreBlock.DEEPSLATE_NADIENITE_ORE.get())
                .add(BlocksAdds.NADIENITE_BLOCK.get());
            
            
            this.tag(BlockTags.INCORRECT_FOR_IRON_TOOL)
                .add(NadieniteOreBlock.NADIENITE_ORE.get())
                .add(NadieniteOreBlock.DEEPSLATE_NADIENITE_ORE.get())
                .add(BlocksAdds.NADIENITE_BLOCK.get());
            
            
            this.tag(BlockTags.INCORRECT_FOR_GOLD_TOOL)
                .add(NadieniteOreBlock.NADIENITE_ORE.get())
                .add(NadieniteOreBlock.DEEPSLATE_NADIENITE_ORE.get())
                .add(BlocksAdds.NADIENITE_BLOCK.get());
            
            this.tag(BlockTags.INCORRECT_FOR_DIAMOND_TOOL)
                .add(NadieniteOreBlock.NADIENITE_ORE.get())
                .add(NadieniteOreBlock.DEEPSLATE_NADIENITE_ORE.get())
                .add(BlocksAdds.NADIENITE_BLOCK.get());
            
            // ============================================
            // TAGS DE MINEABLE
            // Todos los bloques se minan con pico (esto está bien)
            // ============================================
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(NadieniteOreBlock.NADIENITE_ORE.get())
                .add(NadieniteOreBlock.DEEPSLATE_NADIENITE_ORE.get())
                .add(BlocksAdds.NADIENITE_BLOCK.get())
                .add(BlocksAdds.STEEL_BLOCK.get())
                .add(BlocksAdds.SEXY_XD.get());
            
            // ============================================
            // BLOQUES DE ALMACENAMIENTO (Common tags)
            // ============================================
            this.tag(Blocks.STORAGE_BLOCKS_NADIENITE)
                .add(BlocksAdds.NADIENITE_BLOCK.get());
            
            this.tag(Blocks.STORAGE_BLOCKS_STEEL)
                .add(BlocksAdds.STEEL_BLOCK.get());
        }
        
    }
}