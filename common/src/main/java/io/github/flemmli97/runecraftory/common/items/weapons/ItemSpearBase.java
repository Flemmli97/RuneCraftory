package io.github.flemmli97.runecraftory.common.items.weapons;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.enums.EnumToolCharge;
import io.github.flemmli97.runecraftory.api.enums.EnumWeaponType;
import io.github.flemmli97.runecraftory.api.items.IChargeable;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.item.IAOEWeapon;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class ItemSpearBase extends Item implements IItemUsable, IChargeable, IAOEWeapon {

    public ItemSpearBase(Item.Properties props) {
        super(props.stacksTo(1));
    }

    @Override
    public int getChargeTime(ItemStack stack) {
        return GeneralConfig.weaponProps.get(this.getWeaponType()).chargeTime();
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
        return EnumWeaponType.SPEAR;
    }

    @Override
    public int itemCoolDownTicks() {
        return GeneralConfig.weaponProps.get(this.getWeaponType()).cooldown();
    }

    @Override
    public void onEntityHit(ServerPlayer player, ItemStack stack) {
        Platform.INSTANCE.getPlayerData(player)
                .ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.SPEAR, 1));
    }

    @Override
    public void onBlockBreak(ServerPlayer player) {

    }

    @Override
    public float getRange() {
        return GeneralConfig.weaponProps.get(this.getWeaponType()).range();
    }

    @Override
    public float getFOV() {
        return GeneralConfig.weaponProps.get(this.getWeaponType()).aoe();
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
    public boolean canAttackBlock(BlockState state, Level world, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (player.isCreative() || Platform.INSTANCE.getPlayerData(player).map(cap -> cap.getSkillLevel(EnumSkills.SPEAR).getLevel() >= 5).orElse(false)) {
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
        if (entity instanceof ServerPlayer serverPlayer) {
            Platform.INSTANCE.getPlayerData(serverPlayer).ifPresent(data -> {
                int time = this.getUseDuration(stack) - timeLeft;
                if (data.getWeaponHandler().canStartSpear()) {
                    if (time >= this.getChargeTime(stack))
                        data.getWeaponHandler().startSpear();
                } else if (time >= 1) {
                    this.useSpear(serverPlayer, data);
                }
            });
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return ImmutableMultimap.of();
    }

    private void useSpear(ServerPlayer player, PlayerData data) {
        List<Entity> list = RayTraceUtils.getEntities(player, this.getRange(), 10);
        if (!list.isEmpty()) {
            LevelCalc.useRP(player, data, player.getRandom().nextInt(2) + 2, true, false, true, EnumSkills.SPEAR);
            data.getWeaponHandler().onUseSpear();
            LevelCalc.levelSkill(player, data, EnumSkills.SPEAR, 1);
            list.forEach(e -> CombatUtils.playerAttackWithItem(player, e, player.getMainHandItem(), 0.6f, false, false, false));
            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, player.getSoundSource(), 1.0f, 1.0f);
        }
    }
}
