package io.github.flemmli97.runecraftory.common.items.weapons;

import io.github.flemmli97.runecraftory.api.registry.ArmorEffect;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.components.StaffData;
import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryArmorEffects;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttackActions;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryDataComponentTypes;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.item.ExtendedWeapon;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
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

public class ItemStaffBase extends Item implements ExtendedWeapon {

    public final ItemElement startElement;
    public final int amount;

    public ItemStaffBase(ItemElement startElement, int amount, Item.Properties props) {
        super(props);
        this.startElement = startElement;
        this.amount = Math.max(1, amount);
    }

    public int getStaffChargeTime(LivingEntity entity, ItemStack stack) {
        int time = stack.getOrDefault(RuneCraftoryDataComponentTypes.STAFF.get(), StaffData.DEFAULT)
                .chargeTime();
        if (ArmorEffect.hasArmorEffect(entity, RuneCraftoryArmorEffects.MAGIC_RING.asHolder()))
            time *= 0.75;
        return time;
    }

    public int chargeAmount(ItemStack stack) {
        return stack.getOrDefault(RuneCraftoryDataComponentTypes.STAFF.get(), StaffData.DEFAULT)
                .getChargeLevel();
    }

    @Override
    public void executeAttack(Player player, ItemStack stack) {
        Platform.INSTANCE.getPlayerData(player).getWeaponHandler().doWeaponAttack(RuneCraftoryAttackActions.STAFF.get(), stack);
    }

    @Override
    public boolean attackOnBlock(LivingEntity entity, ItemStack stack) {
        return true;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (livingEntity instanceof ServerPlayer player) {
            int duration = stack.getUseDuration(livingEntity) - remainingUseDuration;
            if (duration > 0 && duration / this.getStaffChargeTime(livingEntity, stack) <= this.chargeAmount(stack) && duration % this.getStaffChargeTime(livingEntity, stack) == 0)
                player.connection.send(new ClientboundSoundPacket(SoundEvents.NOTE_BLOCK_XYLOPHONE, player.getSoundSource(), player.getX(), player.getY(), player.getZ(), 1, 1, player.getRandom().nextLong()));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (hand == InteractionHand.OFF_HAND)
            return InteractionResultHolder.fail(stack);
        if (this.chargeAmount(stack) > 0) {
            if (!level.isClientSide) {
                if (this.getStaffChargeTime(player, stack) <= 0) {
                    int chargeLevel = Math.min(3, this.chargeAmount(stack));
                    Spell spell = stack.getOrDefault(RuneCraftoryDataComponentTypes.STAFF.get(), StaffData.DEFAULT)
                            .fromChargeLevel(stack, chargeLevel);
                    if (spell != null && player instanceof ServerPlayer serverPlayer) {
                        Platform.INSTANCE.getPlayerData(serverPlayer).getWeaponHandler().doWeaponAttack(RuneCraftoryAttackActions.STAFF_USE.get(), stack, spell);
                    }
                } else
                    player.startUsingItem(hand);
            }
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!level.isClientSide) {
            int tier = (stack.getUseDuration(entity) - timeLeft - 1) / this.getStaffChargeTime(entity, stack);
            int chargeLevel = Math.min(tier, this.chargeAmount(stack));
            Spell spell = stack.getOrDefault(RuneCraftoryDataComponentTypes.STAFF.get(), StaffData.DEFAULT)
                    .fromChargeLevel(stack, chargeLevel);
            if (spell != null) {
                if (entity instanceof ServerPlayer player) {
                    Platform.INSTANCE.getPlayerData(player).getWeaponHandler().doWeaponAttack(RuneCraftoryAttackActions.STAFF_USE.get(), stack, spell);
                    return;
                }
                spell.use((ServerLevel) level, entity, stack);
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
}

