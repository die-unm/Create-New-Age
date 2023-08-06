package org.antarcticgardens.newage.content.energiser;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.sequenced.IAssemblyRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.antarcticgardens.newage.NewAgeBlocks;
import org.antarcticgardens.newage.compat.NotAnimatedEnergiser;
import org.antarcticgardens.newage.tools.StringFormattingTool;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class EnergisingRecipe extends ProcessingRecipe<Container> implements IAssemblyRecipe {

    public static SequencedAssemblySubCategory subCategoryType = new SequencedAssemblySubCategory(52) {
        private NotAnimatedEnergiser energiser;

        {
            energiser = new NotAnimatedEnergiser();
        }

        @Override
        public void draw(SequencedRecipe<?> recipe, GuiGraphics graphics, double mouseX, double mouseY, int index) {
            PoseStack ms = graphics.pose();
            energiser.offset = index;
            ms.pushPose();
            ms.translate(-5, 50, 0);
            ms.scale(.6f, .6f, .6f);
            energiser.draw(graphics, getWidth() / 2, 0);
            ms.popPose();
            graphics.drawString(Minecraft.getInstance().font, Component.literal(StringFormattingTool.formatLong(((EnergisingRecipe)recipe.getAsAssemblyRecipe()).energyNeeded) + " ⚡"), 0, 20, 0x1166ff, false);
        }
    }; // TODO

    public static IRecipeTypeInfo type;

    public int energyNeeded;
    public EnergisingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(type, params);
    }

    @Override
    public void readAdditional(JsonObject json) {
        energyNeeded = json.get("energy_needed").getAsInt();
    }

    @Override
    public void readAdditional(FriendlyByteBuf buffer) {
        energyNeeded = buffer.readInt();
    }

    @Override
    public void writeAdditional(JsonObject json) {
        json.addProperty("energy_needed", energyNeeded);
    }

    @Override
    public void writeAdditional(FriendlyByteBuf buffer) {
        buffer.writeInt(energyNeeded);
    }

    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 4;
    }

    @Override
    public Component getDescriptionForAssembly() {
        return Component.translatable("recipe.assembly.energising");
    }

    @Override
    public void addRequiredMachines(Set<ItemLike> list) {
        list.add(NewAgeBlocks.ENERGISER_T1.get());
    }

    @Override
    public void addAssemblyIngredients(List<Ingredient> list) {}

    @Override
    public Supplier<Supplier<SequencedAssemblySubCategory>> getJEISubCategory() {
        return () -> () -> subCategoryType;
    }


    @Override
    public boolean matches(Container container, @NotNull Level level) {
        if (container.isEmpty())
            return false;
        return ingredients.get(0)
                .test(container.getItem(0));
    }

    public boolean test(ItemStack stack) {
        return ingredients.get(0)
                .test(stack);
    }



}
