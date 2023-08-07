package org.antarcticgardens.newage.tools;

import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.antarcticgardens.newage.CreateNewAge;

import static org.antarcticgardens.newage.CreateNewAge.MOD_ID;

public class RecipeTool {

    private static final DeferredRegister<RecipeType<?>> register_type = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, MOD_ID);
    private static final DeferredRegister<RecipeSerializer<?>> register = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);
    public static <S extends RecipeSerializer<?>> IRecipeTypeInfo createIRecipeTypeInfo(String name, S serializer) {
        var rt = new RecipeType() {
            @Override
            public String toString() {
                return name;
            }
        };

        register.register(name, () -> serializer);

        register_type.register(name, () -> rt);

        return new IRecipeTypeInfo() {
            @Override
            public ResourceLocation getId() {
                return new ResourceLocation(CreateNewAge.MOD_ID, name);
            }

            @Override
            public <T extends RecipeSerializer<?>> T getSerializer() {
                return (T) serializer;
            }

            @Override
            public <T extends RecipeType<?>> T getType() {
                return (T) rt;
            }
        };
    }
}
