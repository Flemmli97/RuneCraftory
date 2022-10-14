package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.ChargeAttackGoal;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.ArrayList;
import java.util.List;

public class EntityWooly extends ChargingMonster {

    public static final AnimatedAction slap = new AnimatedAction(16, 7, "slap");
    public static final AnimatedAction kick = new AnimatedAction(20, 3, "kick");
    public static final AnimatedAction headbutt = new AnimatedAction(16, 7, "headbutt");
    public static final AnimatedAction interact = AnimatedAction.copyOf(headbutt, "interact");

    public static final AnimatedAction[] anims = new AnimatedAction[]{slap, kick, headbutt, interact};
    private static final EntityDataAccessor<Boolean> SHEARED = SynchedEntityData.defineId(EntityWooly.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SPAWNSHEARED = SynchedEntityData.defineId(EntityWooly.class, EntityDataSerializers.BOOLEAN);
    private final AnimationHandler<EntityWooly> animationHandler = new AnimationHandler<>(this, anims);
    public ChargeAttackGoal<EntityWooly> attack = new ChargeAttackGoal<>(this);
    private int shearTick;

    public EntityWooly(EntityType<? extends EntityWooly> type, Level level) {
        super(type, level);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SPAWNSHEARED, this.getRandom().nextFloat() < 0.1);
        this.entityData.define(SHEARED, this.entityData.get(SPAWNSHEARED));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.entityData.get(SPAWNSHEARED)) {
            this.shearTick = Math.max(--this.shearTick, 0);
            if (this.shearTick == 1) {
                this.setSheared(false);
            }
        }
    }

    @Override
    public float chargingLength() {
        return 3;
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2);
        super.applyAttributes();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Sheared", this.isSheared());
        compound.putBoolean("SpawnedSheared", this.entityData.get(SPAWNSHEARED));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSheared(compound.getBoolean("Sheared"));
        this.entityData.set(SPAWNSHEARED, compound.getBoolean("SpawnedSheared"));
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(Items.SHEARS)) {
            if (!this.level.isClientSide && !this.isSheared()) {
                this.shear(player, EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, itemStack));
                itemStack.hurtAndBreak(1, player, (playerx) -> playerx.broadcastBreakEvent(hand));
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.CONSUME;
            }
        } else {
            return super.mobInteract(player, hand);
        }
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.CHARGE)
            return anim.getID().equals(kick.getID());
        else if (type == AnimationType.MELEE)
            return anim.getID().equals(slap.getID()) || anim.getID().equals(headbutt.getID());
        return false;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 0.8;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2)
                this.getAnimationHandler().setAnimation(kick);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(headbutt);
            else
                this.getAnimationHandler().setAnimation(slap);
        }
    }

    @Override
    protected void tameEntity(Player owner) {
        super.tameEntity(owner);
        this.entityData.set(SPAWNSHEARED, false);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.SHEEP_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SHEEP_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.65f;
    }

    @Override
    public float getVoicePitch() {
        return 1.7f;
    }

    @Override
    public float attackChance(AnimationType type) {
        return this.entityData.get(SPAWNSHEARED) || this.isTamed() ? 0.8f : 0.03f;
    }

    @Override
    public AnimationHandler<EntityWooly> getAnimationHandler() {
        return this.animationHandler;
    }

    public boolean isSheared() {
        return this.entityData.get(SHEARED);
    }

    public void setSheared(boolean flag) {
        this.shearTick = 24000;
        this.entityData.set(SHEARED, flag);
    }

    public void shear(Player player, int fortune) {
        this.setSheared(true);
        List<ItemStack> ret = new ArrayList<>();
        //if(!this.isTamed()){
        //    ret.add((new ItemStack(ModItems.furSmall)));
        //}else {
        int i = 1 + this.random.nextInt(3);
        for (int j = 0; j < i; ++j) {
            ret.add(new ItemStack(Blocks.WHITE_WOOL, 1));
        }
        //}
        this.playSound(SoundEvents.SHEEP_SHEAR, 1.0f, 1.0f);
        this.gameEvent(GameEvent.SHEAR, player);

        for (ItemStack stack : ret) {
            ItemEntity itemEntity = this.spawnAtLocation(stack, 1);
            if (itemEntity != null) {
                itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add((this.random.nextFloat() - this.random.nextFloat()) * 0.1F, this.random.nextFloat() * 0.05F, (this.random.nextFloat() - this.random.nextFloat()) * 0.1F));
            }
        }
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(interact);
    }
}