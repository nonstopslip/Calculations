package com.nonstopslip.gmail.calculations.gui;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.function.BiFunction;

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


    public Terminal(TextRenderer textRenderer, int x, int y, int width, int height) {
        super(x + 10, y, width - 20, height, Text.empty(), textRenderer);
        this.alignRight();
        this.textField = new TextFieldWidget(textRenderer, x, y, width, height, Text.empty());
        this.textField.setEditable(false);
        this.textField.setFocusUnlocked(false);
    }


    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.textField.render(matrices, mouseX, mouseY, delta);
        this.setMessage(Text.of(this.term.replace(".", ",")));
        super.render(matrices, mouseX, mouseY, delta);
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
        this.term += '.';
    }

    public void evaluate() {
        if (this.operation != null) {
            BigDecimal first = new BigDecimal(this.prevTerm);
            BigDecimal second = new BigDecimal(this.term);
            this.term = this.operation.apply(first, second).toString();
            this.prevTerm = "0";
            this.operation = null;
        }
    }

}
