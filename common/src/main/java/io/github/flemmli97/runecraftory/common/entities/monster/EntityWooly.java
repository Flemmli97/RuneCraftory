package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.LeapingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.LeapingAttackGoal;
import io.github.flemmli97.runecraftory.common.loot.LootCtxParameters;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;

public class EntityWooly extends LeapingMonster {

    public static final ResourceLocation WOOLED_LOOT = new ResourceLocation(RuneCraftory.MODID, "entities/wooly/white");

    public static final AnimatedAction SLAP = new AnimatedAction(16, 7, "slap");
    public static final AnimatedAction KICK = new AnimatedAction(20, 3, "kick");
    public static final AnimatedAction HEADBUTT = new AnimatedAction(16, 7, "headbutt");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(HEADBUTT, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(2, "sleep").infinite().changeDelay(AnimationHandler.DEFAULT_ADJUST_TIME).build();
    public static final AnimatedAction[] ANIMS = new AnimatedAction[]{SLAP, KICK, HEADBUTT, INTERACT, SLEEP};
    private static final EntityDataAccessor<Boolean> SHEARED = SynchedEntityData.defineId(EntityWooly.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> SPAWNSHEARED = SynchedEntityData.defineId(EntityWooly.class, EntityDataSerializers.BOOLEAN);

    public LeapingAttackGoal<EntityWooly> attack = new LeapingAttackGoal<>(this);
    private final AnimationHandler<EntityWooly> animationHandler = new AnimationHandler<>(this, ANIMS);

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
    public float maxLeapDistance() {
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
        if (itemStack.is(ModTags.SHEARS)) {
            if (!this.level.isClientSide && !this.isSheared() && (!this.isTamed() || player.getUUID().equals(this.getOwnerUUID()))) {
                this.shear(player, itemStack);
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
        if (type == AnimationType.LEAP)
            return anim.getID().equals(KICK.getID());
        else if (type == AnimationType.MELEE)
            return anim.getID().equals(SLAP.getID()) || anim.getID().equals(HEADBUTT.getID());
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
                this.getAnimationHandler().setAnimation(KICK);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(HEADBUTT);
            else
                this.getAnimationHandler().setAnimation(SLAP);
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
        if (type == AnimationType.MELEE)
            return 0.8f;
        if (this.entityData.get(SPAWNSHEARED))
            return 0.8f;
        return this.isTamed() ? 0.7f : 0.03f;
    }

    @Override
    public int animationCooldown(@Nullable AnimatedAction anim) {
        return super.animationCooldown(anim) * 4;
    }

    @Override
    public AnimationHandler<EntityWooly> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void onDailyUpdate() {
        super.onDailyUpdate();
        if (!this.entityData.get(SPAWNSHEARED) || this.isTamed())
            this.setSheared(false);
    }

    @Override
    protected ResourceLocation getDefaultLootTable() {
        if (this.isSheared())
            return super.getDefaultLootTable();
        else
            return WOOLED_LOOT;
    }

    public boolean isSheared() {
        return this.entityData.get(SHEARED);
    }

    public void setSheared(boolean flag) {
        this.entityData.set(SHEARED, flag);
    }

    public void shear(Player player, ItemStack used) {
        LootTable lootTable = this.level.getServer().getLootTables().get(shearedLootTable(this.getDefaultLootTable()));
        lootTable.getRandomItems(this.dailyDropContext()
                .withParameter(LootCtxParameters.INTERACTING_PLAYER, player)
                .withParameter(LootContextParams.TOOL, used).create(LootContextParamSets.GIFT), this::spawnAtLocation);
        this.setSheared(true);
        this.playSound(SoundEvents.SHEEP_SHEAR, 1.0f, 1.0f);
        this.gameEvent(GameEvent.SHEAR, player);
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public AnimatedAction getSleepAnimation() {
        return SLEEP;
    }

    public static ResourceLocation shearedLootTable(ResourceLocation def) {
        return new ResourceLocation(def.getNamespace(), def.getPath() + "_sheared_drops");
    }
}