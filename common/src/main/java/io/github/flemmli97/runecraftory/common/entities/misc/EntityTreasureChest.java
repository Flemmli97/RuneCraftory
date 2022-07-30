package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.registry.ModTags;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.api.entity.IAnimated;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.ArrayList;
import java.util.List;

public class EntityTreasureChest extends Entity implements IAnimated {

    public static final int MaxTier = 3;

    private static final EntityDataAccessor<Integer> TIER = SynchedEntityData.defineId(EntityTreasureChest.class, EntityDataSerializers.INT);

    private static final AnimatedAction open = new AnimatedAction(12, 12, "open", "open", 1, false);

    private static final AnimatedAction[] anims = new AnimatedAction[]{open};

    private final AnimationHandler<EntityTreasureChest> animationHandler = new AnimationHandler<>(this, anims);

    protected Player lastHurtByPlayer;

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
        if (!this.isRemoved() && !this.level.isClientSide && anim != null && anim.getID().equals(open.getID()) && anim.canAttack()) {
            this.dropRandomItems();
            this.kill();
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
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
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("ChestTier", this.entityData.get(TIER));
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!this.level.isClientSide) {
            if (!this.getAnimationHandler().isCurrentAnim(open.getID())) {
                this.getAnimationHandler().setAnimation(open);
                this.playSound(SoundEvents.CHEST_OPEN, 0.7f, 1);
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
        this.entityData.set(TIER, Mth.clamp(tier, 0, MaxTier));
    }

    public int tier() {
        return this.entityData.get(TIER);
    }

    protected void dropRandomItems() {
        List<Item> items = new ArrayList<>();
        Registry.ITEM.getTag(this.lootTagFromTier()).map(n -> n.stream().map(Holder::value))
                .map(s -> items.addAll(s.toList()));
        if (!items.isEmpty()) {
            int rand = this.tier() < 2 ? 2 : 1;
            rand = Math.min(items.size(), rand + this.random.nextInt(this.tier() < 2 ? 3 : 2));
            for (int i = 0; i < rand; i++) {
                this.spawnAtLocation(items.get(this.random.nextInt(items.size())));
            }
        }
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

    protected TagKey<Item> lootTagFromTier() {
        return switch (this.tier()) {
            case 1 -> ModTags.chest_t2;
            case 2 -> ModTags.chest_t3;
            case 3 -> ModTags.chest_t4;
            default -> ModTags.chest_t1;
        };
    }

    @Override
    public AnimationHandler<?> getAnimationHandler() {
        return this.animationHandler;
    }
}
