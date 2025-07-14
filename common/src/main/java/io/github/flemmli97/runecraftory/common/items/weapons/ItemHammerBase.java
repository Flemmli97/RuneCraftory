package io.github.flemmli97.runecraftory.common.items.weapons;

import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.items.BigWeapon;
import io.github.flemmli97.runecraftory.common.lib.ItemTiers;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttackActions;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemComponentUtils;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.item.ExtendedWeapon;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ItemHammerBase extends PickaxeItem implements ExtendedWeapon, BigWeapon {

    public ItemHammerBase(Item.Properties props) {
        super(ItemTiers.TIER, props);
    }

    @Override
    public void executeAttack(Player player, ItemStack stack) {
        Platform.INSTANCE.getPlayerData(player).getWeaponHandler().doWeaponAttack(RuneCraftoryAttackActions.HAMMER_AXE.get(), stack);
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
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (hand == InteractionHand.OFF_HAND)
            return InteractionResultHolder.pass(itemstack);
        PlayerData data = Platform.INSTANCE.getPlayerData(player);
        boolean canCharge = (data.getSkillLevel(Skills.HAMMERAXE).getLevel() >= 5 || player.isCreative()) && data.getWeaponHandler().canExecuteAction(RuneCraftoryAttackActions.HAMMER_AXE_USE.get());
        if (canCharge) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
        return InteractionResultHolder.pass(itemstack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int timeLeft) {
        if (!world.isClientSide && stack.getUseDuration(entity) - timeLeft - 1 >= ItemComponentUtils.getChargeTime(entity)) {
            if (entity instanceof ServerPlayer player) {
                Platform.INSTANCE.getPlayerData(player).getWeaponHandler().doWeaponAttack(RuneCraftoryAttackActions.HAMMER_AXE_USE.get(), stack);
                return;
            }
            if (ItemAxeBase.performRightClickAction(stack, entity, this.getRange(entity, stack), 0.7f)) {
                entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, entity.getSoundSource(), 1.0f, 1.0f);
            }
        }
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level world, BlockPos pos, Player player) {
        return !player.isCreative();
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
