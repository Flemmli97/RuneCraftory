package io.github.flemmli97.runecraftory.common.items;

import com.google.common.base.Suppliers;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public enum ItemElement {

    NONE(ChatFormatting.GRAY, "none", 0xffffff, null),
    WATER(ChatFormatting.DARK_BLUE, "water", 0x101099, Suppliers.memoize(() -> new ItemStack(ModItems.CRYSTAL_WATER.get()))),
    EARTH(ChatFormatting.YELLOW, "earth", 0xe6e610, Suppliers.memoize(() -> new ItemStack(ModItems.CRYSTAL_EARTH.get()))),
    WIND(ChatFormatting.GREEN, "wind", 0x55ff55, Suppliers.memoize(() -> new ItemStack(ModItems.CRYSTAL_WIND.get()))),
    FIRE(ChatFormatting.DARK_RED, "fire", 0x991010, Suppliers.memoize(() -> new ItemStack(ModItems.CRYSTAL_FIRE.get()))),
    LIGHT(ChatFormatting.WHITE, "light", 0xffff60, Suppliers.memoize(() -> new ItemStack(ModItems.CRYSTAL_LIGHT.get()))),
    DARK(ChatFormatting.DARK_PURPLE, "dark", 0x821082, Suppliers.memoize(() -> new ItemStack(ModItems.CRYSTAL_DARK.get()))),
    LOVE(ChatFormatting.RED, "love", 0xfc60fc, Suppliers.memoize(() -> new ItemStack(ModItems.CRYSTAL_LOVE.get())));

    public static final String PREFIX = "runecraftory.element.";
    public final Supplier<ItemStack> icon;
    private final String translation;
    private final ChatFormatting color;
    private final int particleColor;

    ItemElement(ChatFormatting color, String key, int particleColor, Supplier<ItemStack> icon) {
        this.color = color;
        this.translation = PREFIX + key;
        this.particleColor = particleColor;
        this.icon = icon;
    }

    public static ItemElement opposing(ItemElement element) {
        switch (element) {
            case DARK -> {
                return ItemElement.LIGHT;
            }
            case EARTH -> {
                return ItemElement.WIND;
            }
            case FIRE -> {
                return ItemElement.WATER;
            }
            case LIGHT -> {
                return ItemElement.DARK;
            }
            case NONE -> {
                return ItemElement.LOVE;
            }
            case WATER -> {
                return ItemElement.FIRE;
            }
            case WIND -> {
                return ItemElement.EARTH;
            }
            default -> {
                return ItemElement.NONE;
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
