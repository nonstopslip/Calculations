package com.nonstopslip.gmail.minersmath.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.function.BiFunction;

@Environment(EnvType.CLIENT)
public class Terminal extends TextWidget implements Drawable {

    //math operations
    public static final BiFunction<BigDecimal, BigDecimal, BigDecimal> ADD = BigDecimal::add;
    public static final BiFunction<BigDecimal, BigDecimal, BigDecimal> SUBTRACT = BigDecimal::subtract;
    public static final BiFunction<BigDecimal, BigDecimal, BigDecimal> MULTIPLY = BigDecimal::multiply;
    public static final BiFunction<BigDecimal, BigDecimal, BigDecimal> DIVIDE = (a, b) -> a.divide(b, new MathContext(10, RoundingMode.HALF_UP));
    public static final BiFunction<BigDecimal, BigDecimal, BigDecimal> MODULUS = BigDecimal::remainder;

    //dummy TextFieldWidget, only used for appearance
    private final TextFieldWidget textField;

    private String term = "0";
    private String prevTerm = "0";
    private boolean replaceTerm = false;
    private BiFunction<BigDecimal, BigDecimal, BigDecimal> operation = null;


    public Terminal(int x, int y, int width, int height) {
        super(x + 1, y, width - 2, height, Text.empty(), MinecraftClient.getInstance().textRenderer);
        this.alignRight();
        this.textField = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, x, y, width, height, Text.empty());
        this.textField.setEditable(false);
        this.textField.setFocusUnlocked(false);
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.textField.render(context, mouseX, mouseY, delta);
        this.setMessage(Text.of(this.term.replace(".", ",")));
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        this.textField.setX(x);
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        this.textField.setY(y);
    }

    public void deleteLast() {
        if (this.term.length() < (this.term.startsWith("-") ? 3 : 2))
            this.term = this.term.startsWith("-") ? "-0" : "0";
        else
            this.term = this.term.substring(0, term.length() - 1);
    }

    public void clear() {
        this.term = "0";
        this.prevTerm = "0";
        this.operation = null;
    }

    public void insertDigit(int n) {
        if (n < 0 || n > 9)
            throw new IllegalArgumentException("Argument n is not a digit");

        if (this.replaceTerm) {
            this.prevTerm = this.term;
            this.term = "0";
            this.replaceTerm = false;
        }
        this.term += (char)(n + (int)'0');

        if (this.term.startsWith("0") && !this.term.startsWith("0."))
            this.term = this.term.substring(1);
        else if (this.term.startsWith("-0"))
            this.term = '-' + this.term.substring(2);
    }


    public void setOperation(BiFunction<BigDecimal, BigDecimal, BigDecimal> operation) {
        if (this.operation != null)
            this.evaluate();
        this.operation = operation;
        this.replaceTerm = true;
    }

    public void negate() {
        if (this.operation != null)
            this.term = "-0";
        else if (this.term.startsWith("-"))
            this.term = this.term.substring(1);
        else
            this.term = "-" + this.term;
    }

    public void insertPeriod() {
        if (!this.term.contains("."))
            this.term += '.';
    }

    public void evaluate() {
        if (this.operation != null) {
            try {
                BigDecimal first = new BigDecimal(this.prevTerm);
                BigDecimal second = new BigDecimal(this.term);
                this.term = this.operation.apply(first, second).toString();
            } catch (Exception e) {
                this.term = "Error";
                this.replaceTerm = true;
            }
            this.prevTerm = "0";
            this.operation = null;
        }
    }

}
