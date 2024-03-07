package io.github.flemmli97.runecraftory.api.enums;

import com.google.common.base.Suppliers;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public enum EnumElement {

    NONE(ChatFormatting.GRAY, "runecraftory.element.none", 0xffffff, null),
    WATER(ChatFormatting.DARK_BLUE, "runecraftory.element.water", 0x101099, Suppliers.memoize(() -> new ItemStack(ModItems.CRYSTAL_WATER.get()))),
    EARTH(ChatFormatting.YELLOW, "runecraftory.element.earth", 0xe6e610, Suppliers.memoize(() -> new ItemStack(ModItems.CRYSTAL_EARTH.get()))),
    WIND(ChatFormatting.GREEN, "runecraftory.element.wind", 0x55ff55, Suppliers.memoize(() -> new ItemStack(ModItems.CRYSTAL_WIND.get()))),
    FIRE(ChatFormatting.DARK_RED, "runecraftory.element.fire", 0x991010, Suppliers.memoize(() -> new ItemStack(ModItems.CRYSTAL_FIRE.get()))),
    LIGHT(ChatFormatting.WHITE, "runecraftory.element.light", 0xffff60, Suppliers.memoize(() -> new ItemStack(ModItems.CRYSTAL_FIRE.get()))),
    DARK(ChatFormatting.DARK_PURPLE, "runecraftory.element.dark", 0x821082, Suppliers.memoize(() -> new ItemStack(ModItems.CRYSTAL_DARK.get()))),
    LOVE(ChatFormatting.RED, "runecraftory.element.love", 0xfc60fc, Suppliers.memoize(() -> new ItemStack(ModItems.CRYSTAL_LOVE.get())));

    private final String translation;
    private final ChatFormatting color;
    private final int particleColor;

    public final Supplier<ItemStack> icon;

    EnumElement(ChatFormatting color, String name, int particleColor, Supplier<ItemStack> icon) {
        this.color = color;
        this.translation = name;
        this.particleColor = particleColor;
        this.icon = icon;
    }

    public static EnumElement opposing(EnumElement element) {
        switch (element) {
            case DARK -> {
                return EnumElement.LIGHT;
            }
            case EARTH -> {
                return EnumElement.WIND;
            }
            case FIRE -> {
                return EnumElement.WATER;
            }
            case LIGHT -> {
                return EnumElement.DARK;
            }
            case NONE -> {
                return EnumElement.LOVE;
            }
            case WATER -> {
                return EnumElement.FIRE;
            }
            case WIND -> {
                return EnumElement.EARTH;
            }
            default -> {
                return EnumElement.NONE;
            }
        }
    }

    public String getTranslation() {
        return this.translation;
    }

    public ChatFormatting getColor() {
        return this.color;
    }

    public int getParticleColor() {
        return this.particleColor;
    }
}
