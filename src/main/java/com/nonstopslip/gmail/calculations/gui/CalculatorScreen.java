package com.nonstopslip.gmail.calculations.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;


public class CalculatorScreen extends Screen {

    public int widgetAreaWidth;
    public int widgetAreaHeight;
    public int widgetAreaOriginX;
    public int widgetAreaOriginY;
    public int widgetWidth;
    public int widgetHeight;

    private Terminal terminal;

    public CalculatorScreen(Text title) {
        super(title);
    }


    @Override
    public void init() {
        this.widgetAreaWidth = (int) (this.width * 0.5f);
        this.widgetAreaHeight = (int) (this.height * 0.9f);
        this.widgetAreaOriginX = (this.width - widgetAreaWidth) / 2;
        this.widgetAreaOriginY = (this.height - widgetAreaHeight) / 2;
        this.widgetWidth = widgetAreaWidth / 5;
        this.widgetHeight = widgetAreaHeight / 5;

        this.terminal = new Terminal(this.client.textRenderer,
                this.widgetAreaOriginX, this.widgetAreaOriginY,
                this.widgetAreaWidth, this.widgetHeight);
        this.setupButtons(widgetAreaOriginX, widgetAreaOriginY + widgetHeight);
        this.addDrawableChild(this.terminal);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        this.terminal.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }


    public void buttonPressed(GuiButton button) {
        if (button.id >= 0 && button.id <= 9)
            this.terminal.insertDigit(button.id);

        else if (button.id == ADD_ID)
            this.terminal.setOperation(Terminal.ADD);
        else if (button.id == SUBTRACT_ID)
            this.terminal.setOperation(Terminal.SUBTRACT);
        else if (button.id == MULTIPLY_ID)
            this.terminal.setOperation(Terminal.MULTIPLY);
        else if (button.id == DIVIDE_ID)
            this.terminal.setOperation(Terminal.DIVIDE);
        else if (button.id == MODULUS_ID)
            this.terminal.setOperation(Terminal.MODULUS);

        else if (button.id == DELETE_ID)
            this.terminal.deleteLast();
        else if (button.id == CLEAR_ID)
            this.terminal.clear();
        else if (button.id == NEGATE_ID)
            this.terminal.negate();
        else if (button.id == PERIOD_ID)
            this.terminal.insertPeriod();
        else if (button.id == EQUALS_ID)
            this.terminal.evaluate();
    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode >= GLFW.GLFW_KEY_0 && keyCode <= GLFW.GLFW_KEY_9)
            this.terminal.insertDigit(keyCode - GLFW.GLFW_KEY_0);
        else if (keyCode >= GLFW.GLFW_KEY_KP_0 && keyCode <= GLFW.GLFW_KEY_KP_9)
            this.terminal.insertDigit(keyCode - GLFW.GLFW_KEY_KP_0);

        else if (keyCode == GLFW.GLFW_KEY_KP_ADD)
            this.terminal.setOperation(Terminal.ADD);
        else if (keyCode == GLFW.GLFW_KEY_KP_SUBTRACT)
            this.terminal.setOperation(Terminal.SUBTRACT);
        else if (keyCode == GLFW.GLFW_KEY_KP_MULTIPLY)
            this.terminal.setOperation(Terminal.MULTIPLY);
        else if (keyCode == GLFW.GLFW_KEY_SLASH || keyCode == GLFW.GLFW_KEY_KP_DIVIDE)
            this.terminal.setOperation(Terminal.DIVIDE);
        else if (keyCode == GLFW.GLFW_KEY_M)
            this.terminal.setOperation(Terminal.MODULUS);

        else if (keyCode == GLFW.GLFW_KEY_MINUS)
            this.terminal.negate();
        else if (keyCode == GLFW.GLFW_KEY_PERIOD || keyCode == GLFW.GLFW_KEY_COMMA)
            this.terminal.insertPeriod();
        else if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER)
            this.terminal.evaluate();
        else if (keyCode == GLFW.GLFW_KEY_BACKSPACE)
            this.terminal.deleteLast();
        else if (keyCode == GLFW.GLFW_KEY_DELETE)
            this.terminal.clear();

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private static final String[][] buttonSymbols = {
            {"7", "8", "9", "DEL", "C"},
            {"4", "5", "6", "×", "÷"},
            {"1", "2", "3", "+", "-"},
            {"+|-", "0", ",", "MOD", "="}
    };
    
    private static final int DELETE_ID = buttonSymbols[0][3].hashCode();
    private static final int CLEAR_ID = buttonSymbols[0][4].hashCode();
    private static final int MULTIPLY_ID = buttonSymbols[1][3].hashCode();
    private static final int DIVIDE_ID = buttonSymbols[1][4].hashCode();
    private static final int ADD_ID = buttonSymbols[2][3].hashCode();
    private static final int SUBTRACT_ID = buttonSymbols[2][4].hashCode();
    private static final int NEGATE_ID = buttonSymbols[3][0].hashCode();
    private static final int PERIOD_ID = buttonSymbols[3][2].hashCode();
    private static final int MODULUS_ID = buttonSymbols[3][3].hashCode();
    private static final int EQUALS_ID = buttonSymbols[3][4].hashCode();    
    

    public void setupButtons(int originX, int originY) {
        for (int i = 0; i < buttonSymbols.length; i++) {
            for (int j = 0; j < buttonSymbols[0].length; j++) {
                GuiButton g = new GuiButton(originX + j * widgetWidth, originY + i * widgetHeight, widgetWidth, widgetHeight, buttonSymbols[i][j]);
                this.addDrawableChild(g);
            }

        }
    }
}
