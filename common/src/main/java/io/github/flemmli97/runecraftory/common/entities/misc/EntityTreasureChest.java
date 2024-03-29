package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.loot.LootCtxParameters;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.common.utils.LootTableResources;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class EntityTreasureChest extends Entity implements IAnimated {

    public static final int MAX_TIER = 4;

    private static final EntityDataAccessor<Integer> TIER = SynchedEntityData.defineId(EntityTreasureChest.class, EntityDataSerializers.INT);

    private static final AnimatedAction OPEN = AnimatedAction.builder(12, "open").marker(12).infinite().build();

    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{OPEN};

    private final AnimationHandler<EntityTreasureChest> animationHandler = new AnimationHandler<>(this, ANIMS);

    protected Player lastHurtByPlayer;

    private ResourceLocation chestLoot;

    private Runnable openChest;

    public EntityTreasureChest(EntityType<? extends EntityTreasureChest> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(TIER, 0);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        this.getAnimationHandler().tick();
        AnimatedAction anim = this.getAnimationHandler().getAnimation();
        if (!this.isRemoved() && !this.level.isClientSide && anim != null && anim.is(OPEN) && anim.canAttack()) {
            if (this.openChest != null)
                this.openChest.run();
            this.discard();
        }
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.04, 0.0));
        }
        this.move(MoverType.SELF, this.getDeltaMovement());
        double friction = 0.98;
        if (this.onGround) {
            friction = this.level.getBlockState(new BlockPos(this.getX(), this.getY() - 1.0, this.getZ())).getBlock().getFriction() * 0.98f;
        }
        this.setDeltaMovement(this.getDeltaMovement().multiply(friction, 0.98, friction));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.tier() == 4 && source != DamageSource.OUT_OF_WORLD)
            return false;
        if (source.getEntity() instanceof Player player)
            this.lastHurtByPlayer = player;
        if (!this.level.isClientSide) {
            this.discard();
            this.dropFromLootTable(source, true);
        }
        for (int i = 0; i < 20; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level.addParticle(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
        }
        return true;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.setTier(compound.getInt("ChestTier"));
        if (compound.contains("ChestLoot"))
            this.chestLoot = new ResourceLocation(compound.getString("ChestLoot"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("ChestTier", this.entityData.get(TIER));
        if (this.chestLoot != null)
            compound.putString("ChestLoot", this.chestLoot.toString());
    }

    public void setChestLoot(ResourceLocation loot) {
        this.chestLoot = loot;
        this.setTier(4);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer) {
            if (!this.getAnimationHandler().isCurrent(OPEN)) {
                this.getAnimationHandler().setAnimation(OPEN);
                this.playSound(SoundEvents.CHEST_OPEN, 0.7f, 1);
                this.openChest = () -> this.openChest(serverPlayer, serverPlayer.getItemInHand(hand));
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(serverPlayer, data, EnumSkills.SEARCHING, 20));
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    public ItemStack getPickResult() {
        return SpawnEgg.fromType(this.getType()).map(ItemStack::new).orElse(ItemStack.EMPTY);
    }

    public void setTier(int tier) {
        this.entityData.set(TIER, Mth.clamp(tier, 0, MAX_TIER));
    }

    public int tier() {
        return this.entityData.get(TIER);
    }

    protected void openChest(ServerPlayer player, ItemStack stack) {
        ResourceLocation resourceLocation = this.chestLoot != null ? this.chestLoot : switch (this.tier()) {
            case 1 -> LootTableResources.TIER_2_LOOT;
            case 2 -> LootTableResources.TIER_3_LOOT;
            case 3 -> LootTableResources.TIER_4_LOOT;
            default -> LootTableResources.TIER_1_LOOT;
        };
        LootContext.Builder builder = new LootContext.Builder((ServerLevel) this.level)
                .withRandom(this.random)
                .withLuck(player.getLuck())
                .withParameter(LootContextParams.THIS_ENTITY, this)
                .withParameter(LootContextParams.ORIGIN, this.position())
                .withParameter(LootCtxParameters.UUID_CONTEXT, player.getUUID())
                .withParameter(LootContextParams.TOOL, stack);
        LootTable lootTable = this.level.getServer().getLootTables().get(resourceLocation);
        lootTable.getRandomItems(builder.create(LootCtxParameters.MONSTER_INTERACTION), this::spawnAtLocation);
    }

    protected void dropFromLootTable(DamageSource damageSource, boolean attackedRecently) {
        ResourceLocation resourceLocation = this.getType().getDefaultLootTable();
        LootTable lootTable = this.level.getServer().getLootTables().get(resourceLocation);
        LootContext.Builder builder = this.createLootContext(attackedRecently, damageSource);
        lootTable.getRandomItems(builder.create(LootContextParamSets.ENTITY), this::spawnAtLocation);
    }

    protected LootContext.Builder createLootContext(boolean attackedRecently, DamageSource damageSource) {
        LootContext.Builder builder = new LootContext.Builder((ServerLevel) this.level).withRandom(this.random).withParameter(LootContextParams.THIS_ENTITY, this).withParameter(LootContextParams.ORIGIN, this.position()).withParameter(LootContextParams.DAMAGE_SOURCE, damageSource).withOptionalParameter(LootContextParams.KILLER_ENTITY, damageSource.getEntity()).withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, damageSource.getDirectEntity());
        if (attackedRecently && this.lastHurtByPlayer != null) {
            builder = builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, this.lastHurtByPlayer).withLuck(this.lastHurtByPlayer.getLuck());
        }
        return builder;
    }

    @Override
    public AnimationHandler<?> getAnimationHandler() {
        return this.animationHandler;
    }
}
