package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.LeapingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.loot.LootCtxParameters;
import io.github.flemmli97.runecraftory.common.utils.LootTableResources;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.ActionUtils;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.DoNothingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveAwayRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EntityWooly extends LeapingMonster {

    private static final EntityDataAccessor<Boolean> SHEARED = SynchedEntityData.defineId(EntityWooly.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> SPAWNSHEARED = SynchedEntityData.defineId(EntityWooly.class, EntityDataSerializers.BOOLEAN);

    public static final AnimatedAction SLAP = new AnimatedAction(16, 7, "slap");
    public static final AnimatedAction KICK = new AnimatedAction(20, 3, "kick");
    public static final AnimatedAction HEADBUTT = new AnimatedAction(16, 7, "headbutt");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(HEADBUTT, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().build();
    public static final AnimatedAction[] ANIMS = new AnimatedAction[]{SLAP, KICK, HEADBUTT, INTERACT, SLEEP};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityWooly>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(SLAP, EntityWooly::attackChance), 2),
            WeightedEntry.wrap(new GoalAttackAction<EntityWooly>(KICK)
                    .cooldown(e -> e.animationCooldown(KICK))
                    .withCondition(ActionUtils.chanced(EntityWooly::attackChance))
                    .prepare(() -> new WrappedRunner<>(new MoveAwayRunner<>(1.5, 1, 4))), 1),
            WeightedEntry.wrap(new GoalAttackAction<EntityWooly>(KICK)
                    .cooldown(e -> e.animationCooldown(KICK))
                    .withCondition(ActionUtils.chanced(EntityWooly::attackChance))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetRunner<>(1, 3))), 1),
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(HEADBUTT, EntityWooly::attackChance), 2)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityWooly>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 1)), 1),
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(10, 5)), 2),
            WeightedEntry.wrap(new IdleAction<EntityWooly>(DoNothingRunner::new)
                    .duration(e -> e.getRandom().nextInt(10) + 15), 3)
    );

    public final AnimatedAttackGoal<EntityWooly> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityWooly> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityWooly(EntityType<? extends EntityWooly> type, Level level) {
        super(type, level);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SPAWNSHEARED, this.getRandom().nextFloat() < 0.05);
        this.entityData.define(SHEARED, this.entityData.get(SPAWNSHEARED));
    }

    @Override
    protected boolean isLeapingAnim(AnimatedAction anim) {
        return anim.is(KICK);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.22);
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
        if (itemStack.is(RunecraftoryTags.SHEARS)) {
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
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                return;
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
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.7f;
    }

    @Override
    public int animationCooldown(@Nullable AnimatedAction anim) {
        int diffAdd = this.difficultyCooldown();
        if (anim == null)
            return this.getRandom().nextInt(20) + 30 + diffAdd;
        return this.getRandom().nextInt(30) + 30 + diffAdd;
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
            return LootTableResources.WOOLED_WHITE_LOOT;
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
                .withOptionalParameter(LootCtxParameters.UUID_CONTEXT, player.getUUID())
                .withOptionalParameter(LootContextParams.TOOL, used).create(LootCtxParameters.MONSTER_INTERACTION), this::spawnAtLocation);
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

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        return new Vec3(0, 13.5 / 16d, -6 / 16d);
    }

    protected float attackChance() {
        return this.getEntityData().get(SPAWNSHEARED) || this.isTamed() ? 0.8f : 0;
    }

    public static ResourceLocation shearedLootTable(ResourceLocation def) {
        return new ResourceLocation(def.getNamespace(), def.getPath() + "_sheared_drops");
    }
}