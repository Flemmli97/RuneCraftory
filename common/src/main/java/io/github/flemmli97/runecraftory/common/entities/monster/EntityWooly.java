package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.LeapingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.lib.LootTableResources;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.loot.LootCtxParameters;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.MoveToAttackTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import org.jetbrains.annotations.Nullable;

public class EntityWooly extends LeapingMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String SLAP = BUILDER.add("slap", AnimationsBuilder.definition(0.76).marker("attack", 0.28, 0.56));
    public static final String KICK = BUILDER.add("kick", AnimationsBuilder.definition(1)
            .marker("attack_start", 0.24).marker("attack_end", 0.92));
    public static final String HEADBUTT = BUILDER.add("headbutt", AnimationsBuilder.definition(0.8).marker("attack", 0.44));
    public static final String INTERACT = BUILDER.add("interact", HEADBUTT);
    public static final String SLEEP = BUILDER.add("sleep", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();
    protected static final EntityDataAccessor<Boolean> SHEARED = SynchedEntityData.defineId(EntityWooly.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> SPAWNSHEARED = SynchedEntityData.defineId(EntityWooly.class, EntityDataSerializers.BOOLEAN);
    private final AnimationHandler<EntityWooly> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityWooly(EntityType<? extends EntityWooly> type, Level level) {
        super(type, level);
        boolean sheared = this.getRandom().nextFloat() < 0.05;
        this.entityData.set(SPAWNSHEARED, sheared);
        this.entityData.set(SHEARED, sheared);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.22);
        super.applyAttributes();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SPAWNSHEARED, false);
        builder.define(SHEARED, false);
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<BaseMonster>create()
                .start(SLAP).play(MonsterBehaviourUtils.requireInRangePlay())
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(new MoveToAttackTarget<>())
                .end(1)
                .start(HEADBUTT).play(MonsterBehaviourUtils.requireInRangePlay())
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(new MoveToAttackTarget<>())
                .end(1)
                .start(KICK).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepareOptional(new SetWalkTargetToAttackTarget<BaseMonster>()
                        .closeEnoughDist(MonsterBehaviourUtils.closeEnough(3)), new MoveToWalkTarget<>())
                .end(1)
                .start(KICK).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepareOptional(new SetWalkTargetAwayFromTarget<BaseMonster>().radius(5, 4), new MoveToWalkTarget<>())
                .end(1)
                .build()
                .startCondition(MonsterBehaviourUtils.chancedStart(this::attackChance));
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder()
                .add(2, new SetWalkTargetToAttackTarget<>(), new MoveToWalkTarget<>())
                .add(2, new SetRandomWalkTarget<>(), new MoveToWalkTarget<>())
                .add(5, new Idle<>()).build();
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
        if (itemStack.is(RunecraftoryTags.Items.SHEARS)) {
            if (!this.level().isClientSide && !this.isSheared() && (!this.isTamed() || player.getUUID().equals(this.getOwnerUUID()))) {
                this.shear(player, itemStack);
                itemStack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.CONSUME;
            }
        } else {
            return super.mobInteract(player, hand);
        }
    }

    @Override
    public int animationCooldown(@Nullable String anim) {
        int diffAdd = this.difficultyCooldown();
        if (anim == null)
            return this.getRandom().nextInt(20) + 30 + diffAdd;
        return this.getRandom().nextInt(30) + 30 + diffAdd;
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 1.4;
        double length = this.getBbWidth() * 1.9;
        if (anim.is(HEADBUTT) || anim.is(KICK)) {
            length *= 1.5;
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.vehicleDependentHeight() + 0.02, length);
    }

    @Override
    public AnimationHandler<EntityWooly> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    protected boolean isLeapingAnim(String anim) {
        return anim.equals(KICK);
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

    protected float attackChance() {
        return this.getEntityData().get(SPAWNSHEARED) || this.isTamed() ? 0.8f : 0;
    }

    public void shear(Player player, ItemStack used) {
        LootTable lootTable = this.level().getServer().reloadableRegistries().getLootTable(shearedLootTable(this.getDefaultLootTable()));
        lootTable.getRandomItems(this.dailyDropContext()
                .withOptionalParameter(LootCtxParameters.UUID_CONTEXT, player.getUUID())
                .withOptionalParameter(LootContextParams.TOOL, used).create(LootCtxParameters.MONSTER_INTERACTION), this::spawnAtLocation);
        this.setSheared(true);
        this.playSound(SoundEvents.SHEEP_SHEAR, 1.0f, 1.0f);
        this.gameEvent(GameEvent.SHEAR, player);
    }

    public static ResourceKey<LootTable> shearedLootTable(ResourceKey<LootTable> def) {
        return ResourceKey.create(Registries.LOOT_TABLE,
                ResourceLocation.fromNamespaceAndPath(def.location().getNamespace(), def.location().getPath() + "_sheared_drops"));
    }

    @Override
    protected ResourceKey<LootTable> getDefaultLootTable() {
        if (this.isSheared())
            return super.getDefaultLootTable();
        else
            return LootTableResources.WOOLY_WHITE;
    }

    public boolean isSheared() {
        return this.entityData.get(SHEARED);
    }

    public void setSheared(boolean flag) {
        this.entityData.set(SHEARED, flag);
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
    public void onDailyUpdate() {
        super.onDailyUpdate();
        if (!this.entityData.get(SPAWNSHEARED) || this.isTamed())
            this.setSheared(false);
    }

    @Override
    public String getInteractAnimation() {
        return INTERACT;
    }

    @Override
    public String getSleepAnimation() {
        return SLEEP;
    }
}