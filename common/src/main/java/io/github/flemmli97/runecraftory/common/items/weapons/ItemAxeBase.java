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
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.item.IAOEWeapon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
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
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ItemAxeBase extends AxeItem implements IItemUsable, IChargeable, IAOEWeapon {

    private static final Vec3[] particleDirection = generateParticleDir(2);

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
    public void onBlockBreak(ServerPlayer player) {
        Platform.INSTANCE.getPlayerData(player)
                .ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.LOGGING, 1));
    }

    @Override
    public float getRange(LivingEntity entity, ItemStack stack) {
        return (float) entity.getAttributeValue(ModAttributes.ATTACK_RANGE.get());
    }

    @Override
    public float getFOV(LivingEntity entity, ItemStack stack) {
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
                performRightClickActionPlayer(stack, player, this.getRange(entity, stack));
                return;
            }
            if (performRightClickAction(stack, entity, this.getRange(entity, stack))) {
                entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, entity.getSoundSource(), 1.0f, 1.0f);
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
                boolean success = performRightClickAction(stack, player, range);
                Platform.INSTANCE.sendToClient(new S2CScreenShake(4, 1), player);
                if (success) {
                    player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, player.getSoundSource(), 1.0f, 1.0f);
                    LevelCalc.levelSkill(player, data, EnumSkills.HAMMERAXE, 5);
                    LevelCalc.useRP(player, data, 10, true, false, true, EnumSkills.HAMMERAXE);
                }
            };
            data.getWeaponHandler().doWeaponAttack(player, PlayerWeaponHandler.WeaponUseState.HAMMERAXERIGHTCLICK, stack, run);
        });
    }

    public static boolean performRightClickAction(ItemStack stack, LivingEntity entity, float range) {
        List<Entity> list = getEntitiesIn(entity, range, null);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.DRAGON_FIREBALL_EXPLODE, entity.getSoundSource(), 1.0f, 0.4f);
        Vec3 pos = entity.position().add(0, -1, 0);
        BlockPos.MutableBlockPos mut = new BlockPos.MutableBlockPos();
        for (Vec3 dir : particleDirection) {
            Vec3 scaled = dir.scale(0.5);
            mut.set(Mth.floor(pos.x() + dir.x()), Mth.floor(pos.y()), Mth.floor(pos.z() + dir.z()));
            BlockState state = entity.level.getBlockState(mut);
            if (state.getRenderShape() != RenderShape.INVISIBLE)
                ((ServerLevel) entity.getLevel()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), entity.getX() + dir.x(), entity.getY() + 0.1, entity.getZ() + dir.z(), 0, (float) scaled.x(), 1.5f, (float) scaled.z(), 1);
        }
        if (!list.isEmpty()) {
            Supplier<CustomDamage.Builder> base = () -> new CustomDamage.Builder(entity).element(ItemNBT.getElement(stack)).knock(CustomDamage.KnockBackType.UP).knockAmount(0.7f).hurtResistant(10);
            boolean success = false;
            double damagePhys = CombatUtils.getAttributeValue(entity, Attributes.ATTACK_DAMAGE) * 1.1;
            for (Entity e : list) {
                if (CombatUtils.damage(entity, e, base.get(), false, false, damagePhys, stack))
                    success = true;
            }
            return success;
        }
        return false;
    }

    public static List<Entity> getEntitiesIn(LivingEntity entity, float reach, Predicate<Entity> pred) {
        return entity.level.getEntities(entity, entity.getBoundingBox().inflate(reach, 1, reach), (t) -> t != entity && (pred == null || pred.test(t)) && !t.isAlliedTo(entity) && t.isPickable() &&
                inReach(entity.position(), t, reach - 0.5f));
    }

    private static boolean inReach(Vec3 origin, Entity entity, float reach) {
        double dX = entity.getX() - origin.x;
        double dY = entity.getY() - origin.y();
        if (dY < -0.15 || dY > 1.15)
            return false;
        dY = Math.abs(dY) + 0.75;
        double dZ = entity.getZ() - origin.z;
        reach += entity.getBbWidth() * 0.5;
        return dX * dX + dY * dY + dZ * dZ <= reach * reach;
    }

    public static Consumer<AnimatedAction> movePlayer(Player player) {
        return a -> {
            if (a.getTick() <= 1) {
                player.setDeltaMovement(0, 0.35, 0);
                player.hasImpulse = true;
            }
        };
    }

    private static Vec3[] generateParticleDir(int range) {
        Vec3[] arr = new Vec3[(2 * range + 1) * (2 * range + 1) - 1];
        int i = 0;
        for (int x = -range; x <= range; x++)
            for (int z = -range; z <= range; z++) {
                if (x == 0 && z == 0)
                    continue;
                arr[i] = new Vec3(x, 0, z).normalize().scale(1.2);
                i++;
            }
        return arr;
    }
}
