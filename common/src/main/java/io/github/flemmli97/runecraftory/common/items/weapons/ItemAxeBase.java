package io.github.flemmli97.runecraftory.common.items.weapons;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.enums.EnumToolCharge;
import io.github.flemmli97.runecraftory.api.enums.EnumWeaponType;
import io.github.flemmli97.runecraftory.api.items.IChargeable;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerWeaponHandler;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.lib.ItemTiers;
import io.github.flemmli97.runecraftory.common.network.S2CScreenShake;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.item.IAOEWeapon;
import io.github.flemmli97.tenshilib.common.utils.CircleSector;
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
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ItemAxeBase extends AxeItem implements IItemUsable, IChargeable, IAOEWeapon {

    public ItemAxeBase(Item.Properties props) {
        super(ItemTiers.tier, 0, 0, props);
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
        return EnumWeaponType.HAXE;
    }

    @Override
    public int itemCoolDownTicks() {
        return GeneralConfig.weaponProps.get(this.getWeaponType()).cooldown();
    }

    @Override
    public void onEntityHit(ServerPlayer player, ItemStack stack) {
        Platform.INSTANCE.getPlayerData(player)
                .ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.HAMMERAXE, 1));
    }

    @Override
    public void onBlockBreak(ServerPlayer player) {
        Platform.INSTANCE.getPlayerData(player)
                .ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.LOGGING, 1));
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
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return ImmutableMultimap.of();
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
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (hand == InteractionHand.OFF_HAND)
            return InteractionResultHolder.pass(itemstack);
        if (player.isCreative() || Platform.INSTANCE.getPlayerData(player).map(cap -> cap.getSkillLevel(EnumSkills.HAMMERAXE).getLevel() >= 5).orElse(false)) {
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
                performRightClickActionPlayer(stack, player, this.getRange());
                return;
            }
            List<Entity> list = RayTraceUtils.getEntitiesIgnorePitch(entity, this.getRange(), 360, null);
            if (!list.isEmpty()) {
                CustomDamage src = new CustomDamage.Builder(entity).element(ItemNBT.getElement(stack)).knock(CustomDamage.KnockBackType.UP).knockAmount(0.7f).hurtResistant(10).get();
                boolean success = false;
                for (Entity e : list) {
                    float damagePhys = CombatUtils.getAttributeValueRaw(entity, Attributes.ATTACK_DAMAGE);
                    if (CombatUtils.damage(entity, e, src, damagePhys, stack))
                        success = true;
                }
                if (success) {
                    entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, entity.getSoundSource(), 1.0f, 1.0f);
                }
            }
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    public static void performRightClickActionPlayer(ItemStack stack, ServerPlayer player, float range) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
            Runnable run = () -> {
                List<Entity> list = getEntitiesIn(player, range, 360, null);
                Platform.INSTANCE.sendToClient(new S2CScreenShake(4, 1), player);
                if (!list.isEmpty()) {
                    CustomDamage src = new CustomDamage.Builder(player).element(ItemNBT.getElement(stack)).knock(CustomDamage.KnockBackType.UP).knockAmount(0.7f).hurtResistant(10).get();
                    boolean success = false;
                    for (Entity e : list) {
                        float damagePhys = CombatUtils.getAttributeValueRaw(player, Attributes.ATTACK_DAMAGE);
                        if (CombatUtils.damage(player, e, src, damagePhys, stack))
                            success = true;
                    }
                    if (success) {
                        player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, player.getSoundSource(), 1.0f, 1.0f);
                        LevelCalc.levelSkill(player, data, EnumSkills.HAMMERAXE, 3);
                        LevelCalc.useRP(player, data, 10, true, false, true, EnumSkills.HAMMERAXE);
                    }
                }
            };
            data.getWeaponHandler().doWeaponAttack(player, PlayerWeaponHandler.WeaponUseState.HAMMERAXERIGHTCLICK, stack, run);
        });
    }

    public static List<Entity> getEntitiesIn(LivingEntity entity, float reach, float aoe, Predicate<Entity> pred) {
        CircleSector circ = new CircleSector(entity.position().add(0.0D, 0.1D, 0.0D), Vec3.directionFromRotation(0.0F, entity.getViewYRot(1.0F)), reach, aoe, entity);
        return entity.level.getEntities(entity, entity.getBoundingBox().inflate(reach + 1.0F), (t) -> t != entity && (pred == null || pred.test(t)) && !t.isAlliedTo(entity) && t.isPickable() &&
                circ.intersects(t.level, t.getBoundingBox().inflate(0.15D, t.getY() - entity.getY() >= 0.9 ? 1.15 : (t.getBbHeight() <= 0.3D ? t.getBbHeight() : 0.15D), 0.15D)));
    }

    public static Consumer<AnimatedAction> movePlayer(Player player) {
        return a -> {
            if (a.getTick() <= 1) {
                player.setDeltaMovement(0, 0.35, 0);
                player.hasImpulse = true;
            }
        };
    }
}
