package com.nonstopslip.gmail.minersmath.client.gui.screen;

import com.nonstopslip.gmail.minersmath.client.gui.Terminal;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;


@Environment(EnvType.CLIENT)
public class CalculatorScreen extends Screen {

    private static CalculatorScreen instance;
    private boolean keyHeld = false;
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
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
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
        adder.add(createNumberButton(7));
        adder.add(createNumberButton(8));
        adder.add(createNumberButton(9));
        adder.add(createButton("DEL", button -> this.terminal.deleteLast()));
        adder.add(createButton("C", button -> this.terminal.clear()));
        adder.add(createNumberButton(4));
        adder.add(createNumberButton(5));
        adder.add(createNumberButton(6));
        adder.add(createButton("+", button -> this.terminal.setOperation(Terminal.ADD)));
        adder.add(createButton("-", button -> this.terminal.setOperation(Terminal.SUBTRACT)));
        adder.add(createNumberButton(1));
        adder.add(createNumberButton(2));
        adder.add(createNumberButton(3));
        adder.add(createButton("×", button -> this.terminal.setOperation(Terminal.MULTIPLY)));
        adder.add(createButton("÷", button -> this.terminal.setOperation(Terminal.DIVIDE)));
        adder.add(createButton("MOD", button -> this.terminal.setOperation(Terminal.MODULUS)));
        adder.add(createButton("+|-", button -> this.terminal.negate()));
        adder.add(createNumberButton(0));
        adder.add(createButton(",", button -> this.terminal.insertPeriod()));
        adder.add(createButton("=", button -> this.terminal.evaluate()));

        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, this.width, this.height, 0.5f, 0.5f);
        gridWidget.forEachChild(this::addDrawableChild);
    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.keyHeld)
            return false;

        System.out.println(keyCode);
        this.keyHeld = true;

        switch (keyCode) {
            case GLFW.GLFW_KEY_0, GLFW.GLFW_KEY_1, GLFW.GLFW_KEY_2, GLFW.GLFW_KEY_3, GLFW.GLFW_KEY_4, GLFW.GLFW_KEY_5, GLFW.GLFW_KEY_6, GLFW.GLFW_KEY_7, GLFW.GLFW_KEY_8, GLFW.GLFW_KEY_9 ->
                    this.terminal.insertDigit(keyCode - GLFW.GLFW_KEY_0);
            case GLFW.GLFW_KEY_KP_0, GLFW.GLFW_KEY_KP_1, GLFW.GLFW_KEY_KP_2, GLFW.GLFW_KEY_KP_3, GLFW.GLFW_KEY_KP_4, GLFW.GLFW_KEY_KP_5, GLFW.GLFW_KEY_KP_6, GLFW.GLFW_KEY_KP_7, GLFW.GLFW_KEY_KP_8, GLFW.GLFW_KEY_KP_9 ->
                    this.terminal.insertDigit(keyCode - GLFW.GLFW_KEY_KP_0);

            case GLFW.GLFW_KEY_KP_ADD ->
                    this.terminal.setOperation(Terminal.ADD);
            case GLFW.GLFW_KEY_KP_SUBTRACT ->
                    this.terminal.setOperation(Terminal.SUBTRACT);
            case GLFW.GLFW_KEY_KP_MULTIPLY ->
                    this.terminal.setOperation(Terminal.MULTIPLY);
            case GLFW.GLFW_KEY_KP_DIVIDE ->
                    this.terminal.setOperation(Terminal.DIVIDE);
            case GLFW.GLFW_KEY_M ->
                    this.terminal.setOperation(Terminal.MODULUS);

            case GLFW.GLFW_KEY_MINUS ->
                    this.terminal.negate();
            case GLFW.GLFW_KEY_PERIOD, GLFW.GLFW_KEY_KP_DECIMAL ->
                    this.terminal.insertPeriod();
            case GLFW.GLFW_KEY_BACKSPACE ->
                    this.terminal.deleteLast();
            case GLFW.GLFW_KEY_DELETE ->
                    this.terminal.clear();
            case GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER ->
                    this.terminal.evaluate();
            default -> {
                return super.keyPressed(keyCode, scanCode, modifiers);
            }
        }
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        return true;
    }


    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        this.keyHeld = false;
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    private static ButtonWidget createButton(String s, ButtonWidget.PressAction pressAction) {
        return ButtonWidget.builder(Text.of(s), button -> {
            pressAction.onPress(button);
            button.setFocused(false);
        }).size(40, 40).build();
    }

    private static ButtonWidget createNumberButton(int n) {
        return createButton(String.valueOf(n), (button) -> {
            if (MinecraftClient.getInstance().currentScreen != null && MinecraftClient.getInstance().currentScreen instanceof CalculatorScreen) {
                ((CalculatorScreen) MinecraftClient.getInstance().currentScreen).terminal.insertDigit(n);
            }
        });
    }

}
