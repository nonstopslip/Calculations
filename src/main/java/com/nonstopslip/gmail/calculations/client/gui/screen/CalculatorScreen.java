package com.nonstopslip.gmail.calculations.client.gui.screen;

import com.nonstopslip.gmail.calculations.client.gui.Terminal;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;


public class CalculatorScreen extends Screen {

    private static CalculatorScreen instance;
    private Terminal terminal;

    private CalculatorScreen(Text title) {
        super(title);
    }


    public static CalculatorScreen getInstance() {
        if (instance == null) {
            instance = new CalculatorScreen(Text.translatable("screen.calculations.calculator"));
        }
        return instance;
    }


    @Override
    public void init() {
        this.terminal = this.terminal == null ? new Terminal(0, 0, 204, 20) : this.terminal;
        this.initWidgets();
    }

    private void initWidgets() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().margin(0, 1, 1, 0);
        GridWidget.Adder adder = gridWidget.createAdder(5);
        adder.add(this.terminal, 5);
        for (String s : BUTTON_SYMBOLS) {
            adder.add(this.createButton(s));
        }
        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, this.width, this.height, 0.5f, 0.5f);
        gridWidget.forEachChild(this::addDrawableChild);
    }


    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    public void buttonPressed(int id) {
        if (id >= "0".hashCode() && id <= "9".hashCode())
            this.terminal.insertDigit(id - "0".hashCode());

        else if (id == ADD_ID)
            this.terminal.setOperation(Terminal.ADD);
        else if (id == SUBTRACT_ID)
            this.terminal.setOperation(Terminal.SUBTRACT);
        else if (id == MULTIPLY_ID)
            this.terminal.setOperation(Terminal.MULTIPLY);
        else if (id == DIVIDE_ID)
            this.terminal.setOperation(Terminal.DIVIDE);
        else if (id == MODULUS_ID)
            this.terminal.setOperation(Terminal.MODULUS);

        else if (id == DELETE_ID)
            this.terminal.deleteLast();
        else if (id == CLEAR_ID)
            this.terminal.clear();
        else if (id == NEGATE_ID)
            this.terminal.negate();
        else if (id == PERIOD_ID)
            this.terminal.insertPeriod();
        else if (id == EQUALS_ID)
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

    private static final String[] BUTTON_SYMBOLS = {
            "7", "8", "9", "DEL", "C",
            "4", "5", "6", "×", "÷",
            "1", "2", "3", "+", "-",
            "+|-", "0", ",", "MOD", "="
    };
    
    private static final int DELETE_ID = BUTTON_SYMBOLS[3].hashCode();
    private static final int CLEAR_ID = BUTTON_SYMBOLS[4].hashCode();
    private static final int MULTIPLY_ID = BUTTON_SYMBOLS[8].hashCode();
    private static final int DIVIDE_ID = BUTTON_SYMBOLS[9].hashCode();
    private static final int ADD_ID = BUTTON_SYMBOLS[13].hashCode();
    private static final int SUBTRACT_ID = BUTTON_SYMBOLS[14].hashCode();
    private static final int NEGATE_ID = BUTTON_SYMBOLS[15].hashCode();
    private static final int PERIOD_ID = BUTTON_SYMBOLS[17].hashCode();
    private static final int MODULUS_ID = BUTTON_SYMBOLS[18].hashCode();
    private static final int EQUALS_ID = BUTTON_SYMBOLS[19].hashCode();
    
    
    private ButtonWidget createButton(String s) {
        return ButtonWidget.builder(Text.of(s), (button) -> {
            button.setFocused(false);
            this.buttonPressed(button.getMessage().getString().hashCode());
        }).size(40, 40).build();
    }

}
