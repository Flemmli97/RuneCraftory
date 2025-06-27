package io.github.flemmli97.runecraftory.common.items.consumables;

import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemObjectX extends Item {

    public ItemObjectX(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (stack.has(DataComponents.FOOD) && !livingEntity.level().isClientSide) {
            return applyEffect(livingEntity, stack);
        }
        return stack;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    public static ItemStack applyEffect(LivingEntity livingEntity, ItemStack stack) {
        ItemStack eat = livingEntity.eat(livingEntity.level(), stack);
        List<Holder.Reference<MobEffect>> list = BuiltInRegistries.MOB_EFFECT.holders().filter(effect -> effect.value().getCategory() == MobEffectCategory.HARMFUL).toList();
        if (!list.isEmpty()) {
            int r = livingEntity.getRandom().nextInt(4) + 1;
            for (int i = 0; i < r; i++) {
                Holder.Reference<MobEffect> effect = list.get(livingEntity.getRandom().nextInt(list.size()));
                int amp = livingEntity.getRandom().nextInt(2);
                MobEffectInstance inst = livingEntity.getEffect(effect);
                if (inst != null)
                    amp += inst.getAmplifier();
                int duration = effect.value() == ModEffects.SLEEP.get() ? 80 : 600;
                livingEntity.addEffect(new MobEffectInstance(effect, duration, amp));
            }
        }
        return eat;
    }
}
