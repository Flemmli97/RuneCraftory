package io.github.flemmli97.runecraftory.common.items.weapons;

import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.enums.EnumWeaponType;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.common.items.BigWeapon;
import io.github.flemmli97.runecraftory.common.registry.ModAttackActions;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
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

public class ItemSpearBase extends Item implements IItemUsable, IAOEWeapon, BigWeapon {

    public ItemSpearBase(Item.Properties props) {
        super(props.stacksTo(1));
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
                    .ifPresent(d -> d.getWeaponHandler().doWeaponAttack(ModAttackActions.SPEAR.get(), stack));
            return false;
        }
        return true;
    }

    @Override
    public EnumWeaponType getWeaponType() {
        return EnumWeaponType.SPEAR;
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
    public boolean canAttackBlock(BlockState state, Level world, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (hand == InteractionHand.OFF_HAND)
            return InteractionResultHolder.pass(itemstack);
        if (player.isCreative() || Platform.INSTANCE.getPlayerData(player).map(cap -> cap.getSkillLevel(EnumSkills.SPEAR).getLevel() >= 5).orElse(false)) {
            if (player instanceof ServerPlayer serverPlayer) {
                if (Platform.INSTANCE.getPlayerData(player).map(data -> {
                    // Check if insta use is possible
                    if (data.getWeaponHandler().canExecuteAction(ModAttackActions.SPEAR_USE.get(), false)) {
                        data.getWeaponHandler().doWeaponAttack(ModAttackActions.SPEAR_USE.get(), itemstack);
                        return false;
                    }
                    return data.getWeaponHandler().getCurrentAction() == ModAttackActions.NONE.get();
                }).orElse(true)) {
                    player.startUsingItem(hand);
                }
            }
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
        if (entity instanceof ServerPlayer serverPlayer) {
            Platform.INSTANCE.getPlayerData(serverPlayer).ifPresent(data -> {
                int time = stack.getUseDuration() - timeLeft - 1;
                if (time >= ItemUtils.getChargeTime(entity) && data.getWeaponHandler().canExecuteAction(ModAttackActions.SPEAR_USE.get())) {
                    data.getWeaponHandler().doWeaponAttack(ModAttackActions.SPEAR_USE.get(), stack);
                }
            });
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    public void useSpear(ServerPlayer player, ItemStack stack, boolean finishing) {
        Collection<LivingEntity> list = CombatUtils.EntityAttack.obbTargets(IAOEWeapon.createOBB(player, stack, this.getRange(player, stack), 0.5))
                .apply(player, null);
        if (!list.isEmpty()) {
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.SPEAR, 2));
            list.forEach(e -> CombatUtils.playerAttackWithItem(player, e, player.getMainHandItem(), 0.6f, false, false));
        }
        if (finishing)
            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.PLAYER_ATTACK_SWOOSH.get(), player.getSoundSource(), 1.0f, (player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.2f + 1.5f);
        else
            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.PLAYER_ATTACK_SWOOSH_LIGHT.get(), player.getSoundSource(), 1.0f, (player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.2f + 1.0f);
    }
}
