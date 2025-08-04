package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.creativetab.SubTab;
import io.github.flemmli97.runecraftory.common.creativetab.SubTabContextDisplayBuilder;
import io.github.flemmli97.runecraftory.mixinhelper.CreativeTabExtension;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(CreativeModeTab.class)
public class CreativeTabMixin implements CreativeTabExtension {

    @Shadow
    @Final
    private CreativeModeTab.DisplayItemsGenerator displayItemsGenerator;

    @Unique
    private List<SubTab> runecraftory$subTabs;

    @Inject(method = "hasAnyItems", at = @At("HEAD"), cancellable = true)
    private void checkDisplay(CallbackInfoReturnable<Boolean> info) {
        if (this.runecraftory$subTabs != null && !this.runecraftory$subTabs.isEmpty())
            info.setReturnValue(true);
    }

    @Inject(method = "buildContents", at = @At("HEAD"))
    public void injectContext(CreativeModeTab.ItemDisplayParameters parameters, CallbackInfo info) {
        if (this.runecraftory$subTabs != null && this.displayItemsGenerator instanceof SubTabContextDisplayBuilder ctx) {
            ctx.enabledTabs(this.runecraftory$subTabs.stream().filter(SubTab::selected).map(SubTab::id).collect(Collectors.toSet()));
        }
    }

    @Override
    public void runecraftory$setSubTabs(List<SubTab> tabs) {
        if (this.runecraftory$subTabs == null)
            this.runecraftory$subTabs = tabs;
    }

    @Override
    public List<SubTab> runecraftory$getSubTabs() {
        return this.runecraftory$subTabs;
    }
}
