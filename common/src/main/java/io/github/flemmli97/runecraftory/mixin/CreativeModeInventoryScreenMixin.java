package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.mixinhelper.CreativeScreenHandler;
import io.github.flemmli97.runecraftory.mixinhelper.CreativeScreenSubTab;
import io.github.flemmli97.tenshilib.client.gui.widget.list.SelectableListWidget;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CreativeModeInventoryScreen.class, priority = 999)
public abstract class CreativeModeInventoryScreenMixin extends EffectRenderingInventoryScreen<CreativeModeInventoryScreen.ItemPickerMenu> implements CreativeScreenSubTab {

    @Override
    @Shadow
    protected abstract void init();

    @Shadow
    protected abstract boolean hasPermissions(Player player);

    @Unique
    private FeatureFlagSet runecraftory$featureCache;
    @Unique
    private LocalPlayer runecraftory$playerCache;
    @Unique
    private SelectableListWidget runecraftory$subTabButtons;

    private CreativeModeInventoryScreenMixin(CreativeModeInventoryScreen.ItemPickerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void captureFeatures(LocalPlayer player, FeatureFlagSet enabledFeatures, boolean displayOperatorCreativeTab, CallbackInfo info) {
        this.runecraftory$featureCache = enabledFeatures;
        this.runecraftory$playerCache = player;
    }

    @Inject(method = "selectTab", at = @At("HEAD"))
    private void onSelect(CreativeModeTab tab, CallbackInfo info) {
        this.runecraftory$subTabButtons = CreativeScreenHandler.onSelectTab(tab, this.leftPos, this.topPos, this.runecraftory$subTabButtons,
                this::removeWidget, this::addRenderableWidget, () -> {
                    tab.buildContents(new CreativeModeTab.ItemDisplayParameters(this.runecraftory$featureCache,
                            this.hasPermissions(this.runecraftory$playerCache), this.runecraftory$playerCache.registryAccess()));
                    this.init();
                });
    }

    @Inject(method = "mouseScrolled", at = @At("HEAD"), cancellable = true)
    private void onScroll(double mouseX, double mouseY, double scrollX, double scrollY, CallbackInfoReturnable<Boolean> info) {
        if (this.runecraftory$subTabButtons != null && this.runecraftory$subTabButtons.isMouseOver(mouseX, mouseY)
                && this.runecraftory$subTabButtons.mouseScrolled(mouseX, mouseY, scrollX, scrollY)) {
            info.setReturnValue(true);
        }
    }

    @Inject(method = "mouseDragged", at = @At("HEAD"), cancellable = true)
    private void onDrag(double mouseX, double mouseY, int button, double dragX, double dragY, CallbackInfoReturnable<Boolean> info) {
        if (this.runecraftory$subTabButtons != null && this.runecraftory$subTabButtons.isMouseDragging()
                && this.runecraftory$subTabButtons.mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
            info.setReturnValue(true);
        }
    }

    @Override
    public SelectableListWidget runecraftory$subTabWidget() {
        return this.runecraftory$subTabButtons;
    }
}
