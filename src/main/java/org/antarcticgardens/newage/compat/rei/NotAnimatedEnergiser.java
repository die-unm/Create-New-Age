package org.antarcticgardens.newage.compat.rei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.rei.category.animations.AnimatedKinetics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.core.Direction;
import org.antarcticgardens.newage.NewAgeBlocks;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NotAnimatedEnergiser extends AnimatedKinetics {

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

    @Override
    public void render(@NotNull PoseStack graphics, int mouseX, int mouseY, float partialTick) {
        draw(graphics, getPos().getX(), getPos().getY());
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return new ArrayList<>();
    }
}
