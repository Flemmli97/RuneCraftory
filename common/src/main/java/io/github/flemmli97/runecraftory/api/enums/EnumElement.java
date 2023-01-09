package io.github.flemmli97.runecraftory.api.enums;

import com.google.common.base.Suppliers;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public enum EnumElement {

    NONE(ChatFormatting.GRAY, "element_none", 0xffffff, null),
    WATER(ChatFormatting.DARK_BLUE, "element_water", 0x101099, Suppliers.memoize(() -> new ItemStack(ModItems.crystalWater.get()))),
    EARTH(ChatFormatting.YELLOW, "element_earth", 0xe6e610, Suppliers.memoize(() -> new ItemStack(ModItems.crystalEarth.get()))),
    WIND(ChatFormatting.GREEN, "element_wind", 0x55ff55, Suppliers.memoize(() -> new ItemStack(ModItems.crystalWind.get()))),
    FIRE(ChatFormatting.DARK_RED, "element_fire", 0x991010, Suppliers.memoize(() -> new ItemStack(ModItems.crystalFire.get()))),
    LIGHT(ChatFormatting.WHITE, "element_light", 0xffff60, Suppliers.memoize(() -> new ItemStack(ModItems.crystalFire.get()))),
    DARK(ChatFormatting.DARK_PURPLE, "element_dark", 0x821082, Suppliers.memoize(() -> new ItemStack(ModItems.crystalDark.get()))),
    LOVE(ChatFormatting.RED, "element_love", 0xfc60fc, Suppliers.memoize(() -> new ItemStack(ModItems.crystalLove.get())));

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
