package io.github.flemmli97.runecraftory.common.items.weapons;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.enums.EnumToolCharge;
import io.github.flemmli97.runecraftory.api.enums.EnumWeaponType;
import io.github.flemmli97.runecraftory.api.items.IChargeable;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerWeaponHandler;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.lib.ItemTiers;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.item.IAOEWeapon;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
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

import java.util.List;
import java.util.function.Supplier;

public class ItemLongSwordBase extends SwordItem implements IItemUsable, IChargeable, IAOEWeapon {

    public ItemLongSwordBase(Item.Properties props) {
        super(ItemTiers.tier, 0, 0, props);
    }

    @Override
    public int getChargeTime(ItemStack stack) {
        return DataPackHandler.SERVER_PACK.weaponPropertiesManager().getPropertiesFor(this.getWeaponType()).chargeTime();
    }

    @Override
    public int chargeAmount(ItemStack stack) {
        return 1;
    }

    @Override
    public EnumToolCharge chargeType(ItemStack stack) {
        return EnumToolCharge.CHARGEUPWEAPON;
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
    public float getFOV(LivingEntity entity, ItemStack stack) {
        return DataPackHandler.SERVER_PACK.weaponPropertiesManager().getPropertiesFor(this.getWeaponType()).aoe();
    }

    @Override
    public boolean doSweepingAttack() {
        return false;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        int duration = stack.getUseDuration() - remainingUseDuration;
        if (duration == this.getChargeTime(stack))
            livingEntity.playSound(SoundEvents.NOTE_BLOCK_XYLOPHONE, 1, 1);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (player.isCreative() || Platform.INSTANCE.getPlayerData(player).map(cap -> cap.getSkillLevel(EnumSkills.LONGSWORD).getLevel() >= 5).orElse(false)) {
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
        if (!world.isClientSide && this.getUseDuration(stack) - timeLeft >= this.getChargeTime(stack)) {
            if (entity instanceof ServerPlayer player) {
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
                    Runnable run = () -> {
                        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, entity.getSoundSource(), 1.0f, 1.0f);
                        player.sweepAttack();
                        if (performRightClickAction(stack, entity, this.getRange(entity, stack))) {
                            LevelCalc.levelSkill(player, data, EnumSkills.LONGSWORD, 7);
                        }
                    };
                    data.getWeaponHandler().doWeaponAttack(player, PlayerWeaponHandler.WeaponUseState.LONGSWORDRIGHTCLICK, stack, run);
                });
                return;
            }
            if (performRightClickAction(stack, entity, this.getRange(entity, stack))) {
                entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, entity.getSoundSource(), 1.0f, 1.0f);
            }
        }
    }

    public static boolean performRightClickAction(ItemStack stack, LivingEntity entity, float range) {
        List<Entity> list = RayTraceUtils.getEntities(entity, range, 200);
        if (!list.isEmpty()) {
            Supplier<CustomDamage.Builder> base = () -> new CustomDamage.Builder(entity).element(ItemNBT.getElement(stack)).knock(CustomDamage.KnockBackType.UP).knockAmount(1f).hurtResistant(10);
            boolean success = false;
            double damagePhys = CombatUtils.getAttributeValue(entity, Attributes.ATTACK_DAMAGE) * 1.2;
            for (Entity e : list) {
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
