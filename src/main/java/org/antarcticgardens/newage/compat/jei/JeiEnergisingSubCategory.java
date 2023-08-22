package org.antarcticgardens.newage.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import org.antarcticgardens.newage.NewAgeBlocks;
import org.antarcticgardens.newage.content.energiser.EnergisingRecipe;
import org.antarcticgardens.newage.tools.StringFormattingTool;

public class JeiEnergisingSubCategory extends SequencedAssemblySubCategory {
    AnimatedKinetics energiser;

    public JeiEnergisingSubCategory() {
        super(25);
        energiser = new AnimatedKinetics() {
            @Override
            public void draw(PoseStack matrixStack, int xOffset, int yOffset) {
                matrixStack.pushPose();
                matrixStack.translate(xOffset, yOffset, 200);
                matrixStack.mulPose(Vector3f.XP.rotationDegrees(-15.5f));
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(22.5f));

                blockElement(shaft(Direction.Axis.Z))
                        .rotateBlock(0, 0, getCurrentAngle())
                        .scale(20)
                        .render(matrixStack);

                blockElement(NewAgeBlocks.ENERGISER_T1.getDefaultState())
                        .scale(20)
                        .render(matrixStack);

                blockElement(AllBlocks.DEPOT.getDefaultState())
                        .scale(20)
                        .atLocal(0, 2, 0)
                        .render(matrixStack);

                matrixStack.popPose();
            }
        };
    }

    @Override
    public void draw(SequencedRecipe<?> recipe, PoseStack ms, double mouseX, double mouseY, int index) {
        energiser.offset = index;
        ms.pushPose();
        ms.translate(-7, 50, 0);
        ms.scale(.75f, .75f, .75f);
        energiser.draw(ms, getWidth() / 2, 0);
        ms.popPose();
        Minecraft.getInstance().font.draw(ms, Component.literal(StringFormattingTool.formatLong(((EnergisingRecipe)recipe.getAsAssemblyRecipe()).energyNeeded) + " ⚡"), 0, 20, 0x1166ff);
    }
}
