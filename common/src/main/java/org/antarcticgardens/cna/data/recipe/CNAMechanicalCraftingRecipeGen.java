package org.antarcticgardens.cna.data.recipe;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import com.simibubi.create.foundation.data.recipe.MechanicalCraftingRecipeBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.ItemLike;
import org.antarcticgardens.cna.CNABlocks;
import org.antarcticgardens.cna.CNAItems;
import org.antarcticgardens.cna.CNATags;

import java.util.function.UnaryOperator;

#if CNA_FABRIC
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import io.github.fabricators_of_create.porting_lib.tags.Tags;
#else
import net.minecraftforge.common.Tags;
#endif

@SuppressWarnings("unused")
public class CNAMechanicalCraftingRecipeGen extends CreateRecipeProvider {
    GeneratedRecipe ADVANCED_MOTOR_EXTENSION = builder(CNABlocks.ADVANCED_MOTOR_EXTENSION)
            .amount(2)
            .mechanicalCrafting(b -> b
                    .key('D', CNAItems.OVERCHARGED_DIAMOND)
                    .key('M', CNABlocks.REINFORCED_MOTOR)
                    .key('C', CNAItems.COPPER_CIRCUIT)
                    .key('S', CNAItems.OVERCHARGED_IRON_SHEET)
                    .patternLine("SSSSS")
                    .patternLine("DCMCD")
                    .patternLine("SSSSS"));
    
    GeneratedRecipe REINFORCED_MOTOR = builder(CNABlocks.REINFORCED_MOTOR)
            .amount(2)
            .mechanicalCrafting(b -> b
                    .key('D', CNAItems.OVERCHARGED_DIAMOND)
                    .key('d', Tags.Items.GEMS_DIAMOND)
                    .key('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
                    .key('S', AllBlocks.SHAFT)
                    .key('C', AllBlocks.BRASS_CASING)
                    .key('P', CNATags.Common.PLATES_IRON)
                    .patternLine("dDPPd")
                    .patternLine("DCRSS")
                    .patternLine("dDPPd"));
    
    GeneratedRecipe REACTOR_ROD = builder(CNABlocks.REACTOR_ROD)
            .amount(2)
            .mechanicalCrafting(b -> b
                    .key('P', CNATags.Common.PLATES_GOLD)
                    .key('F', CNAItems.NUCLEAR_FUEL)
                    .key('C', CNABlocks.REACTOR_CASING)
                    .key('G', CNABlocks.REACTOR_GLASS)
                    .patternLine("CPPPC")
                    .patternLine(" GFG ")
                    .patternLine(" GFG ")
                    .patternLine("CPPPC"));
    
    // =================================================================================================================
    
    protected GeneratedRecipeBuilder builder(ItemLike result) {
        return new GeneratedRecipeBuilder(result);
    }

    protected class GeneratedRecipeBuilder extends GeneratedRecipeBuilderBase<GeneratedRecipeBuilder> {
        public GeneratedRecipeBuilder(ItemLike result) {
            super(result);
        }
        
        protected GeneratedRecipe mechanicalCrafting(UnaryOperator<MechanicalCraftingRecipeBuilder> operator) {
            return register(consumer -> operator.apply(MechanicalCraftingRecipeBuilder.shapedRecipe(result, amount))
                    .build(consumer, createLocation("mechanical_crafting")));
        }
    }
    
    @Override
    public String getName() {
        return "Create New Age Mechanical Crafting Recipes";
    }

    public CNAMechanicalCraftingRecipeGen(PackOutput output) {
        #if CNA_FABRIC
        super((FabricDataOutput) output);
        #else
        super(output);
        #endif
    }
}
