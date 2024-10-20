package io.github.flemmli97.runecraftory.common.items.weapons;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.enums.EnumToolCharge;
import io.github.flemmli97.runecraftory.api.enums.EnumWeaponType;
import io.github.flemmli97.runecraftory.api.items.IChargeable;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.attachment.StaffData;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.registry.ModAttackActions;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.platform.ExtendedItem;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.item.IExtendedWeapon;
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

public class ItemStaffBase extends Item implements IItemUsable, IChargeable, ExtendedItem, IExtendedWeapon {

    public final EnumElement startElement;
    public final int amount;

    public ItemStaffBase(EnumElement startElement, int amount, Item.Properties props) {
        super(props);
        this.startElement = startElement;
        this.amount = Math.max(1, amount);
    }

    @Override
    public int getChargeTime(ItemStack stack) {
        return Platform.INSTANCE.getStaffData(stack).map(cap ->
                cap.getTier1Spell(stack) != null ? cap.getTier1Spell(stack).coolDown() : cap.getTier2Spell(stack) != null ? cap.getTier1Spell(stack).coolDown() : cap.getTier3Spell(stack) != null ? cap.getTier3Spell(stack).coolDown() : 0).orElse(DataPackHandler.INSTANCE.weaponPropertiesManager().getPropertiesFor(this.getWeaponType()).chargeTime());
    }

    @Override
    public int chargeAmount(ItemStack stack) {
        return Platform.INSTANCE.getStaffData(stack).map(cap ->
                cap.getTier3Spell(stack) != null && ItemNBT.itemLevel(stack) >= 3 ? 3 : cap.getTier2Spell(stack) != null ? 2 : cap.getTier1Spell(stack) != null ? 1 : 0).orElse(0);
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
                    .ifPresent(d -> d.getWeaponHandler().doWeaponAttack(player, ModAttackActions.STAFF.get(), stack));
            return false;
        }
        return true;
    }

    @Override
    public EnumToolCharge chargeType(ItemStack stack) {
        return EnumToolCharge.CHARGEUPWEAPON;
    }

    @Override
    public EnumWeaponType getWeaponType() {
        return EnumWeaponType.STAFF;
    }

    @Override
    public void onBlockBreak(ServerPlayer player) {
    }

    @Override
    public float getRange(LivingEntity entity, ItemStack stack) {
        return (float) entity.getAttributeValue(ModAttributes.ATTACK_RANGE.get());
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (livingEntity instanceof ServerPlayer player) {
            int duration = stack.getUseDuration() - remainingUseDuration;
            if (duration > 0 && duration / this.getChargeTime(stack) <= this.chargeAmount(stack) && duration % this.getChargeTime(stack) == 0)
                player.connection.send(new ClientboundSoundPacket(SoundEvents.NOTE_BLOCK_XYLOPHONE, player.getSoundSource(), player.getX(), player.getY(), player.getZ(), 1, 1));
        }
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level world, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (hand == InteractionHand.OFF_HAND)
            return InteractionResultHolder.fail(stack);
        if (this.chargeAmount(stack) > 0) {
            if (!world.isClientSide) {
                if (this.getChargeTime(stack) <= 0) {
                    int level = Math.min(3, this.chargeAmount(stack));
                    Spell spell = this.getSpell(stack, level);
                    if (spell != null && player instanceof ServerPlayer serverPlayer) {
                        Platform.INSTANCE.getPlayerData(serverPlayer).ifPresent(data -> data.getWeaponHandler().doWeaponAttack(serverPlayer, ModAttackActions.STAFF_USE.get(), stack, spell, false));
                    }
                } else
                    player.startUsingItem(hand);
            }
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.pass(stack);
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
        if (!world.isClientSide) {
            int tier = (this.getUseDuration(stack) - timeLeft - 1) / this.getChargeTime(stack);
            int level = Math.min(tier, this.chargeAmount(stack));
            Spell spell = this.getSpell(stack, level);
            if (spell != null) {
                if (entity instanceof ServerPlayer player) {
                    Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.getWeaponHandler().doWeaponAttack(player, ModAttackActions.STAFF_USE.get(), stack, spell, false));
                    return;
                }
                spell.use((ServerLevel) world, entity, stack);
            }
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    public Spell getSpell(ItemStack stack, int level) {
        StaffData cap = Platform.INSTANCE.getStaffData(stack).orElseThrow(() -> new NullPointerException("Error getting capability for staff item"));
        return switch (level) {
            case 3 -> cap.getTier3Spell(stack);
            case 2 -> cap.getTier2Spell(stack);
            case 1 -> cap.getTier1Spell(stack);
            default -> null;
        };
    }

    public void castBaseSpell(ItemStack stack, LivingEntity entity) {
        if (entity.level instanceof ServerLevel serverLevel) {
            ModSpells.STAFF_CAST.get().use(serverLevel, entity, stack);
            entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, entity.getSoundSource(), 1.0f, 1.0f);
        }
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return false;
    }
}

