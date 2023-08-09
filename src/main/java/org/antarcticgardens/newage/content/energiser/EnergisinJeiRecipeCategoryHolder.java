package org.antarcticgardens.newage.content.energiser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.antarcticgardens.newage.compat.NotAnimatedEnergiser;
import org.antarcticgardens.newage.tools.StringFormattingTool;

public class EnergisinJeiRecipeCategoryHolder {
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
            graphics.drawString(Minecraft.getInstance().font, Component.literal(StringFormattingTool.formatLong(((EnergisingRecipe) recipe.getAsAssemblyRecipe()).energyNeeded) + " ⚡"), 0, 20, 0x1166ff, false);
        }
    }; // TODO
}
