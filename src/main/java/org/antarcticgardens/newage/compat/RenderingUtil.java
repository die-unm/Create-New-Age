package org.antarcticgardens.newage.compat;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public class RenderingUtil {
    public static void drawCenteredStringWithoutShadow(PoseStack stack, Font font, Component text, int x, int y, int color) {
        FormattedCharSequence formattedCharSequence = text.getVisualOrderText();
        font.draw(stack, formattedCharSequence, x - font.width(formattedCharSequence) / 2, y, color);
    }
}
