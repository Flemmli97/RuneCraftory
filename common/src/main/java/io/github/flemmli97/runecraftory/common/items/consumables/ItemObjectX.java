package io.github.flemmli97.runecraftory.common.items.consumables;

import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemObjectX extends Item {

    public ItemObjectX(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (this.isEdible()) {
            ItemStack eat = livingEntity.eat(level, stack);
            List<MobEffect> list = PlatformUtils.INSTANCE.effects().values()
                    .stream().filter(effect -> !effect.isBeneficial()).toList();
            if (!list.isEmpty()) {
                int r = livingEntity.getRandom().nextInt(5) + 1;
                for (int i = 0; i < r; i++) {
                    MobEffect effect = list.get(livingEntity.getRandom().nextInt(list.size()));
                    int amp = livingEntity.getRandom().nextInt(2);
                    MobEffectInstance inst = livingEntity.getEffect(effect);
                    if (inst != null)
                        amp += inst.getAmplifier();
                    livingEntity.addEffect(new MobEffectInstance(effect, 600, amp));
                }
            }
            return eat;
        }
        return stack;
    }
}
