package io.github.flemmli97.runecraftory.common.items.weapons;

import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttackActions;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemComponentUtils;
import io.github.flemmli97.tenshilib.common.item.DualWeapon;
import io.github.flemmli97.tenshilib.common.item.ExtendedWeapon;
import io.github.flemmli97.tenshilib.loader.registry.AttachmentRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ItemGloveBase extends Item implements DualWeapon, ExtendedWeapon {

    public ItemGloveBase(Item.Properties props) {
        super(props);
    }

    @Override
    public void executeAttack(Player player, ItemStack stack) {
        RunecraftoryAttachments.PLAYER_DATA.get().get(player).getWeaponHandler().doWeaponAttack(RuneCraftoryAttackActions.GLOVES.get(), stack);
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
            return InteractionResultHolder.fail(itemstack);
        PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
        boolean canCharge = (data.getSkillLevel(Skills.FIST).getLevel() >= 5 || player.isCreative()) && data.getWeaponHandler().canExecuteAction(RuneCraftoryAttackActions.GLOVE_USE.get());
        if (canCharge) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
        return InteractionResultHolder.pass(itemstack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (entity instanceof ServerPlayer serverPlayer && stack.getUseDuration(entity) - timeLeft - 1 >= ItemComponentUtils.getChargeTime(entity)) {
            RunecraftoryAttachments.PLAYER_DATA.get().get(serverPlayer).getWeaponHandler().doWeaponAttack(RuneCraftoryAttackActions.GLOVE_USE.get(), stack);
        }
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
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

    @Override
    public ItemStack offHandStack(LivingEntity entity) {
        return RunecraftoryAttachments.ENTITY_DATA.get().get(entity).getGloveOffHand(entity.getMainHandItem());
    }
}
