package io.github.flemmli97.runecraftory.common.items.weapons;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.enums.EnumWeaponType;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.common.items.BigWeapon;
import io.github.flemmli97.runecraftory.common.lib.ItemTiers;
import io.github.flemmli97.runecraftory.common.network.S2CAttackDebug;
import io.github.flemmli97.runecraftory.common.network.S2CScreenShake;
import io.github.flemmli97.runecraftory.common.registry.ModAttackActions;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.ItemUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.item.IAOEWeapon;
import io.github.flemmli97.tenshilib.common.utils.OrientedBoundingBox;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
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
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ItemAxeBase extends AxeItem implements IItemUsable, IAOEWeapon, BigWeapon {

    private static final Vec3[] PARTICLE_DIRECTION = generateParticleDir(2);

    public ItemAxeBase(Item.Properties props) {
        super(ItemTiers.TIER, 0, 0, props);
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
                    .ifPresent(d -> d.getWeaponHandler().doWeaponAttack(ModAttackActions.HAMMER_AXE.get(), stack));
            return false;
        }
        return true;
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
    public float getWidth(LivingEntity entity, ItemStack stack) {
        return (float) entity.getAttributeValue(ModAttributes.ATTACK_WIDTH.get());
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
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (hand == InteractionHand.OFF_HAND)
            return InteractionResultHolder.pass(itemstack);
        boolean canCharge = Platform.INSTANCE.getPlayerData(player)
                .map(data -> (data.getSkillLevel(EnumSkills.HAMMERAXE).getLevel() >= 5 || player.isCreative()) && data.getWeaponHandler().canExecuteAction(ModAttackActions.HAMMER_AXE_USE.get())).orElse(false);
        if (canCharge) {
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
        if (!world.isClientSide && stack.getUseDuration() - timeLeft - 1 >= ItemUtils.getChargeTime(entity)) {
            if (entity instanceof ServerPlayer player) {
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.getWeaponHandler().doWeaponAttack(ModAttackActions.HAMMER_AXE_USE.get(), stack));
                return;
            }
            if (performRightClickAction(stack, entity, this.getRange(entity, stack), 0.7f)) {
                entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, entity.getSoundSource(), 1.0f, 1.0f);
            }
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    public static void delayedRightClickAction(LivingEntity entity, ItemStack stack) {
        double reach = CombatUtils.getRange(entity, 0);
        S2CScreenShake.sendAround(entity, 16, 4, 3);
        if (performRightClickAction(stack, entity, reach, 0.7f) && entity instanceof ServerPlayer player) {
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.HAMMERAXE, 5));
        }
    }

    public static boolean performRightClickAction(ItemStack stack, LivingEntity entity, double range, float knockback) {
        Collection<LivingEntity> list = getEntitiesIn(entity, range, null);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.DRAGON_FIREBALL_EXPLODE, entity.getSoundSource(), 1.0f, 0.4f);
        Vec3 pos = entity.position().add(0, -1, 0);
        BlockPos.MutableBlockPos mut = new BlockPos.MutableBlockPos();
        for (Vec3 dir : PARTICLE_DIRECTION) {
            Vec3 scaled = dir.scale(0.5);
            mut.set(Mth.floor(pos.x() + dir.x()), Mth.floor(pos.y()), Mth.floor(pos.z() + dir.z()));
            BlockState state = entity.level.getBlockState(mut);
            if (state.getRenderShape() != RenderShape.INVISIBLE)
                ((ServerLevel) entity.getLevel()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), entity.getX() + dir.x(), entity.getY() + 0.1, entity.getZ() + dir.z(), 0, (float) scaled.x(), 1.5f, (float) scaled.z(), 1);
        }
        if (!list.isEmpty()) {
            Supplier<CustomDamage.Builder> base = () -> new CustomDamage.Builder(entity).element(ItemNBT.getElement(stack))
                    .knock(CustomDamage.KnockBackType.UP).knockAmount(knockback).hurtResistant(5);
            boolean success = false;
            double damagePhys = CombatUtils.getAttributeValue(entity, Attributes.ATTACK_DAMAGE) * 1.1;
            for (Entity e : list) {
                if (CombatUtils.damageWithFaintAndCrit(entity, e, base.get(), damagePhys, stack))
                    success = true;
            }
            return success;
        }
        return false;
    }

    public static Collection<LivingEntity> getEntitiesIn(LivingEntity entity, double reach, Predicate<LivingEntity> pred) {
        double incHalf = Math.asin(0.5 / reach) * Mth.RAD_TO_DEG;
        float minYRot = 0;
        float maxYRot = 360;
        AABB aabb = new AABB(-0.5, -0.15, 0, 0.5, 1.15, reach);
        int rotationSteps = (int) ((maxYRot - minYRot) / (incHalf * 2)) + 2;
        float inc = (maxYRot - minYRot) / rotationSteps;
        Set<LivingEntity> entities = new HashSet<>();
        for (int steps = 0; steps <= rotationSteps; steps++) {
            float yRot = minYRot + inc * steps;
            OrientedBoundingBox obb = new OrientedBoundingBox(aabb, yRot, 0, entity.position());
            entities.addAll(RayTraceUtils.getEntitiesIn(entity, obb, true, EntityTypeTest.forClass(LivingEntity.class), pred));
            S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTACK, entity);
        }
        return entities;
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
