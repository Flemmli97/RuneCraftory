package io.github.flemmli97.runecraftory.common.items.weapons;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.enums.EnumWeaponType;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.common.items.BigWeapon;
import io.github.flemmli97.runecraftory.common.lib.ItemTiers;
import io.github.flemmli97.runecraftory.common.registry.ModAttackActions;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.ItemUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.function.Supplier;

public class ItemLongSwordBase extends SwordItem implements IItemUsable, IAOEWeapon, BigWeapon {

    public ItemLongSwordBase(Item.Properties props) {
        super(ItemTiers.TIER, 0, 0, props);
    }

    @Override
    public boolean resetAttackStrength(LivingEntity entity, ItemStack stack) {
        return false;
    }

    @Override
    public boolean swingWeapon(LivingEntity entity, ItemStack stack) {
        return false;
    }

    @Override
    public boolean onServerSwing(LivingEntity entity, ItemStack stack) {
        if (entity instanceof Player player) {
            Platform.INSTANCE.getPlayerData(player)
                    .ifPresent(d -> d.getWeaponHandler().doWeaponAttack(ModAttackActions.LONG_SWORD.get(), stack));
            return false;
        }
        return true;
    }

    @Override
    public EnumWeaponType getWeaponType() {
        return EnumWeaponType.LONGSWORD;
    }

    @Override
    public void onBlockBreak(ServerPlayer player) {

    }

    @Override
    public float getRange(LivingEntity entity, ItemStack stack) {
        return (float) entity.getAttributeValue(ModAttributes.ATTACK_RANGE.get());
    }

    @Override
    public float getWidth(LivingEntity entity, ItemStack stack) {
        return (float) entity.getAttributeValue(ModAttributes.ATTACK_WIDTH.get());
    }

    @Override
    public boolean doSweepingAttack() {
        return false;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (entity instanceof ServerPlayer player) {
            int duration = stack.getUseDuration() - remainingUseDuration;
            if (duration == ItemUtils.getChargeTime(entity))
                player.connection.send(new ClientboundSoundPacket(SoundEvents.NOTE_BLOCK_XYLOPHONE, player.getSoundSource(), player.getX(), player.getY(), player.getZ(), 1, 1));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (hand == InteractionHand.OFF_HAND)
            return InteractionResultHolder.pass(itemstack);
        boolean canCharge = Platform.INSTANCE.getPlayerData(player)
                .map(data -> (data.getSkillLevel(EnumSkills.LONGSWORD).getLevel() >= 5 || player.isCreative()) && data.getWeaponHandler().canExecuteAction(ModAttackActions.LONGSWORD_USE.get())).orElse(false);
        if (canCharge) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
        return InteractionResultHolder.pass(itemstack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int timeLeft) {
        if (!world.isClientSide && stack.getUseDuration() - timeLeft - 1 >= ItemUtils.getChargeTime(entity)) {
            if (entity instanceof ServerPlayer player) {
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.getWeaponHandler().doWeaponAttack(ModAttackActions.LONGSWORD_USE.get(), stack));
                return;
            }
            if (performRightClickAction(stack, entity, 0)) {
                entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, entity.getSoundSource(), 1.0f, 1.0f);
            }
        }
    }

    public static void delayedRightClickAction(LivingEntity entity, ItemStack stack) {
        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, entity.getSoundSource(), 1.0f, 1.0f);
        if (entity instanceof ServerPlayer player)
            player.sweepAttack();
        if (performRightClickAction(stack, entity, 0) && entity instanceof ServerPlayer player) {
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.LONGSWORD, 7));
        }
    }

    public static boolean performRightClickAction(ItemStack stack, LivingEntity entity, double range) {
        Collection<LivingEntity> list = CombatUtils.EntityAttack.circleTargets(entity.getYRot() - 60, entity.getYRot() + 60, (float) range)
                .apply(entity, null);
        if (!list.isEmpty()) {
            Supplier<CustomDamage.Builder> base = () -> new CustomDamage.Builder(entity).element(ItemNBT.getElement(stack)).knock(CustomDamage.KnockBackType.UP).knockAmount(1f).hurtResistant(10);
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
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return ImmutableMultimap.of();
    }
}
