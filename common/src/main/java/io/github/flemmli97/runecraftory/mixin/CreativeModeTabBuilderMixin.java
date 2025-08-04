package io.github.flemmli97.runecraftory.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.flemmli97.runecraftory.common.creativetab.CreativeTabBuilderExtension;
import io.github.flemmli97.runecraftory.common.creativetab.SubTab;
import io.github.flemmli97.runecraftory.mixinhelper.CreativeTabExtension;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(CreativeModeTab.Builder.class)
public class CreativeModeTabBuilderMixin implements CreativeTabBuilderExtension {

    @Unique
    private final List<SubTab> runecraftory$subTabs = new ArrayList<>();

    @ModifyReturnValue(method = "build", at = @At("TAIL"))
    private CreativeModeTab onReturn(CreativeModeTab original) {
        if (!this.runecraftory$subTabs.isEmpty())
            ((CreativeTabExtension) original).runecraftory$setSubTabs(this.runecraftory$subTabs);
        return original;
    }

    @Override
    public void runecraftory$withSubTab(List<SubTab> subTabs) {
        this.runecraftory$subTabs.addAll(subTabs);
    }
}
