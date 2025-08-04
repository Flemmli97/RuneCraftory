package io.github.flemmli97.runecraftory.common.creativetab;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SubTab {

    private final ResourceLocation id;
    private final Component title;

    @Nullable
    private ItemStack iconItemStack;
    private final Supplier<ItemStack> iconGenerator;
    private final ButtonInfo buttonInfo;

    private boolean selected;

    public SubTab(ResourceLocation id, Supplier<ItemStack> iconGenerator) {
        this(id, iconGenerator, ButtonInfo.DEFAULT);
    }

    public SubTab(ResourceLocation id, Supplier<ItemStack> iconGenerator, ButtonInfo buttonInfo) {
        this.id = id;
        this.title = Component.translatable("itemGroup." + id.getNamespace() + "." + id.getPath());
        this.iconGenerator = iconGenerator;
        this.buttonInfo = buttonInfo;
    }

    public ResourceLocation id() {
        return this.id;
    }

    public Component title() {
        return this.title;
    }

    public ItemStack icon() {
        if (this.iconItemStack == null) {
            this.iconItemStack = this.iconGenerator.get();
        }
        return this.iconItemStack;
    }

    public ButtonInfo buttonInfo() {
        return this.buttonInfo;
    }

    public void select(boolean select) {
        this.selected = select;
    }

    public boolean selected() {
        return this.selected;
    }

    public record ButtonInfo(ResourceLocation buttonTexture, ResourceLocation buttonHighlightTexture,
                             ResourceLocation buttonSelectedTexture,
                             int width, int height) {

        public static final ButtonInfo DEFAULT = new ButtonInfo(RuneCraftory.modRes("widget/sub_tab"),
                RuneCraftory.modRes("widget/sub_tab_highlighted"), RuneCraftory.modRes("widget/sub_tab_selected"), 36, 26);
    }
}
