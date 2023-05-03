package com.nonstopslip.gmail.minersmath.client;

import com.nonstopslip.gmail.minersmath.client.gui.screen.CalculatorScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class MinersMath implements ClientModInitializer {

    private static final KeyBinding OPEN_CALCULATOR;

    @Override
    public void onInitializeClient() {

    }


    static {
        OPEN_CALCULATOR = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.calculations.open_calculator",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Y,
                "category.calculations.calculations"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (OPEN_CALCULATOR.wasPressed()) {
                MinecraftClient.getInstance().setScreen(CalculatorScreen.getInstance());
            }
        });
    }

}
