package io.github.flemmli97.runecraftory.common.items.weapons;

import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttackActions;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemComponentUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.tenshilib.common.item.AOEWeapon;
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
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collection;

public class ItemSpearBase extends Item implements ExtendedWeapon {

    public ItemSpearBase(Item.Properties props) {
        super(props);
    }

    @Override
    public void executeAttack(Player player, ItemStack stack) {
        RunecraftoryAttachments.PLAYER_DATA.get().get(player).getWeaponHandler().executeAttack(RuneCraftoryAttackActions.SPEAR.get(), stack);
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
        PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
        if (player.isCreative() || data.getSkillLevel(Skills.SPEAR).getLevel() >= 5) {
            if (player instanceof ServerPlayer) {
                if (data.getWeaponHandler().canExecuteAttack(RuneCraftoryAttackActions.SPEAR_USE.get(), false)) {
                    data.getWeaponHandler().executeAttack(RuneCraftoryAttackActions.SPEAR_USE.get(), itemstack);
                } else {
                    if (data.getWeaponHandler().getCurrentAction() == RuneCraftoryAttackActions.NONE.get()) {
                        player.startUsingItem(hand);
                    }
                }
            }
            return InteractionResultHolder.consume(itemstack);
        }
        return InteractionResultHolder.pass(itemstack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (entity instanceof ServerPlayer serverPlayer) {
            PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(serverPlayer);
            int time = stack.getUseDuration(entity) - timeLeft - 1;
            if (time >= ItemComponentUtils.getChargeTime(entity) && data.getWeaponHandler().canExecuteAttack(RuneCraftoryAttackActions.SPEAR_USE.get())) {
                data.getWeaponHandler().executeAttack(RuneCraftoryAttackActions.SPEAR_USE.get(), stack);
            }
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

    public void useSpear(ServerPlayer player, ItemStack stack, boolean finishing) {
        Collection<LivingEntity> list = CombatUtils.EntityAttack.obbTargets(AOEWeapon.createOBB(player, this.getRange(player, stack), 0.5, 0.5))
                .apply(player, null);
        if (!list.isEmpty()) {
            LevelCalc.levelSkill(RunecraftoryAttachments.PLAYER_DATA.get().get(player), Skills.SPEAR, 2);
            list.forEach(e -> CombatUtils.attackWithItem(player, e, player.getMainHandItem(), 0.6f, false, false));
        }
        if (finishing)
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), RuneCraftorySounds.PLAYER_ATTACK_SWOOSH.get(), player.getSoundSource(), 1.0f, (player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.2f + 1.5f);
        else
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), RuneCraftorySounds.PLAYER_ATTACK_SWOOSH_LIGHT.get(), player.getSoundSource(), 1.0f, (player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.2f + 1.0f);
    }
}
