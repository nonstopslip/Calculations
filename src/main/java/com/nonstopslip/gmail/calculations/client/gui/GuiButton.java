package com.nonstopslip.gmail.calculations.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;

public class GuiButton extends ButtonWidget {

    public int id;

    public GuiButton(int x, int y, int width, int height, String symbol) {
        super(x, y, width, height,
                Text.literal(symbol),
                button -> ((CalculatorScreen) MinecraftClient.getInstance().currentScreen).buttonPressed((GuiButton) button),
                textSupplier -> MutableText.of(TextContent.EMPTY));
        try {
            this.id = Integer.parseInt(symbol);
        } catch (Exception e) {
            this.id = symbol.hashCode();
        }
    }


    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.setFocused(false);
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
