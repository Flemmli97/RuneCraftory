package io.github.flemmli97.runecraftory.common.attachment;

import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.api.registry.ArmorEffect;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.registry.ModArmorEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class ArmorEffectData {

    private final ArmorEffect[] effect = new ArmorEffect[3];
    // Cycle through index when crafting. Latest will overwrite the last added one
    private int appendingIndex;

    public void triggerEvent(ItemStack stack, Consumer<ArmorEffect> consumer) {
        if (this.effect[0] == null) {
            ArmorEffect defaultEffect = DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem()).map(ItemStat::getArmorEffect).orElse(null);
            if (defaultEffect != null && defaultEffect.canBeAppliedTo(stack))
                consumer.accept(defaultEffect);
        } else {
            for (ArmorEffect eff : this.effect) {
                if (eff != null)
                    consumer.accept(eff);
            }
        }
    }

    public void setArmorEffects(ArmorEffect first, ArmorEffect second, ArmorEffect third) {
        this.effect[0] = first;
        this.effect[1] = second;
        this.effect[2] = third;
    }

    public void addArmorEffects(ArmorEffect effect) {
        if (effect != null) {
            this.effect[this.appendingIndex] = effect;
            this.appendingIndex = (this.appendingIndex + 1) % 3;
        }
    }

    public void readFromNBT(CompoundTag nbt) {
        if (nbt.contains("FirstEffect"))
            this.effect[0] = ModArmorEffects.ARMOR_EFFECT_REGISTRY.get().getFromId(new ResourceLocation(nbt.getString("FirstEffect")));
        if (nbt.contains("SecondEffect"))
            this.effect[1] = ModArmorEffects.ARMOR_EFFECT_REGISTRY.get().getFromId(new ResourceLocation(nbt.getString("SecondEffect")));
        if (nbt.contains("ThirdEffect"))
            this.effect[2] = ModArmorEffects.ARMOR_EFFECT_REGISTRY.get().getFromId(new ResourceLocation(nbt.getString("ThirdEffect")));
        this.appendingIndex = nbt.getInt("Index");
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        if (this.effect[0] != null)
            nbt.putString("FirstEffect", this.effect[0].getRegistryName().toString());
        if (this.effect[1] != null)
            nbt.putString("SecondEffect", this.effect[1].getRegistryName().toString());
        if (this.effect[2] != null)
            nbt.putString("ThirdEffect", this.effect[2].getRegistryName().toString());
        nbt.putInt("Index", this.appendingIndex);
        return nbt;
    }
}
