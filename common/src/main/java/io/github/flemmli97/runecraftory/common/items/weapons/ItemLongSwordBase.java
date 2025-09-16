package io.github.flemmli97.runecraftory.common.items.weapons;

import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.lib.ItemTiers;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttackActions;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemComponentUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.item.ExtendedWeapon;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.function.Supplier;

public class ItemLongSwordBase extends SwordItem implements ExtendedWeapon {

    public ItemLongSwordBase(Item.Properties props) {
        super(ItemTiers.TIER, props);
    }

    public static void delayedRightClickAction(LivingEntity entity, ItemStack stack) {
        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, entity.getSoundSource(), 1.0f, 1.0f);
        if (entity instanceof ServerPlayer player)
            player.sweepAttack();
        if (performRightClickAction(stack, entity, 0) && entity instanceof ServerPlayer player) {
            LevelCalc.levelSkill(Platform.INSTANCE.getPlayerData(player), Skills.LONGSWORD, 7);
        }
    }

    public static boolean performRightClickAction(ItemStack stack, LivingEntity entity, double range) {
        Collection<LivingEntity> list = CombatUtils.EntityAttack.circleTargets(entity.getYRot() - 60, entity.getYRot() + 60, (float) range)
                .apply(entity, null);
        if (!list.isEmpty()) {
            Supplier<DynamicDamage.Builder> base = () -> new DynamicDamage.Builder(entity).element(ItemComponentUtils.getElement(stack)).knock(DynamicDamage.KnockBackType.UP, 1f).hurtResistant(10);
            boolean success = false;
            double damagePhys = CombatUtils.getAttributeValue(entity, Attributes.ATTACK_DAMAGE) * 1.2;
            for (LivingEntity e : list) {
                if (CombatUtils.damageWithFaintAndCrit(entity, e, base.get(), damagePhys, stack))
                    success = true;
            }
            return success;
        }
        return false;
    }

    @Override
    public void executeAttack(Player player, ItemStack stack) {
        Platform.INSTANCE.getPlayerData(player).getWeaponHandler().doWeaponAttack(RuneCraftoryAttackActions.LONG_SWORD.get(), stack);
    }

    @Override
    public boolean attackOnBlock(LivingEntity entity, ItemStack stack) {
        return true;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (entity instanceof ServerPlayer player) {
            int duration = stack.getUseDuration(entity) - remainingUseDuration;
            if (duration == ItemComponentUtils.getChargeTime(entity))
                EntityUtils.playSoundForPlayer(player, SoundEvents.NOTE_BLOCK_XYLOPHONE, 1, 1);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (hand == InteractionHand.OFF_HAND)
            return InteractionResultHolder.pass(itemstack);
        PlayerData data = Platform.INSTANCE.getPlayerData(player);
        boolean canCharge = (data.getSkillLevel(Skills.LONGSWORD).getLevel() >= 5 || player.isCreative()) && data.getWeaponHandler().canExecuteAction(RuneCraftoryAttackActions.LONGSWORD_USE.get());
        if (canCharge) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
        return InteractionResultHolder.pass(itemstack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!level.isClientSide && stack.getUseDuration(entity) - timeLeft - 1 >= ItemComponentUtils.getChargeTime(entity)) {
            if (entity instanceof ServerPlayer player) {
                Platform.INSTANCE.getPlayerData(player).getWeaponHandler().doWeaponAttack(RuneCraftoryAttackActions.LONGSWORD_USE.get(), stack);
                return;
            }
            if (performRightClickAction(stack, entity, 0)) {
                entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, entity.getSoundSource(), 1.0f, 1.0f);
            }
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }
}
