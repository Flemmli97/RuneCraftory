package io.github.flemmli97.runecraftory.common.entities;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityTreasureChest;
import io.github.flemmli97.runecraftory.common.lib.LibConstants;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.common.world.GateSpawning;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class GateEntity extends Mob implements IBaseMob {

    private static final EntityDataAccessor<String> elementType = SynchedEntityData.defineId(GateEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> element = SynchedEntityData.defineId(GateEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> mobLevel = SynchedEntityData.defineId(GateEntity.class, EntityDataSerializers.INT);
    private static final UUID attributeLevelMod = UUID.fromString("EC84560E-5266-4DC3-A4E1-388b97DBC0CB");
    public int rotate, clientParticles;
    public boolean clientParticleFlag;
    private List<EntityType<?>> spawnList = new ArrayList<>();
    private EnumElement type = EnumElement.NONE;
    private boolean initialSpawn = true;

    public GateEntity(EntityType<? extends GateEntity> type, Level level) {
        super(type, level);
        if (level.isClientSide) {
            this.rotate = ((level.random.nextInt(2) == 0) ? 1 : -1);
        }
        this.updateAttributes();
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(ModAttributes.RF_DEFENCE.get()).add(ModAttributes.RF_MAGIC_DEFENCE.get());
    }

    public static boolean canSpawnAt(EntityType<? extends GateEntity> type, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, Random random) {
        return level.getDifficulty() != Difficulty.PEACEFUL && GateSpawning.hasSpawns(level, pos) && checkMobSpawnRules(type, level, reason, pos, random) && level.getEntitiesOfClass(GateEntity.class, new AABB(pos).inflate(MobConfig.minDist)).size() < MobConfig.maxGroup;
    }

    @Override
    public int level() {
        return this.entityData.get(mobLevel);
    }

    @Override
    public void setLevel(int lvl) {
        this.entityData.set(mobLevel, Math.min(10000, lvl));
    }

    @Override
    public int baseXP() {
        return MobConfig.gateXP;
    }

    @Override
    public int baseMoney() {
        return MobConfig.gateMoney;
    }

    @Override
    public void applyFoodEffect(ItemStack stack) {
    }

    @Override
    public void removeFoodEffect() {
    }

    public EnumElement getElement() {
        return this.type;
    }

    public String elementName() {
        return this.entityData.get(elementType);
    }

    private void updateAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(MobConfig.gateHealth);
        this.getAttribute(ModAttributes.RF_DEFENCE.get()).setBaseValue(MobConfig.gateDef);
        this.getAttribute(ModAttributes.RF_MAGIC_DEFENCE.get()).setBaseValue(MobConfig.gateMDef);
        this.setHealth(this.getMaxHealth());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(elementType, "none");
        this.entityData.define(mobLevel, LibConstants.baseLevel);
        this.entityData.define(element, 0);
    }

    @Override
    public void tick() {
        if (Platform.INSTANCE.onLivingUpdate(this)) return;
        if (!this.level.isClientSide) {
            this.setSharedFlag(6, this.isCurrentlyGlowing());
        } else {
            this.clientParticles += 10;
            this.clientParticleFlag = true;
        }
        this.baseTick();
        if (this.lerpSteps > 0) {
            double d0 = this.getX() + (this.lerpX - this.getX()) / (double) this.lerpSteps;
            double d2 = this.getY() + (this.lerpY - this.getY()) / (double) this.lerpSteps;
            double d4 = this.getZ() + (this.lerpZ - this.getZ()) / (double) this.lerpSteps;
            double d6 = Mth.wrapDegrees(this.lerpYRot - (double) this.getYRot());
            this.setYRot((float) ((double) this.getYRot() + d6 / (double) this.lerpSteps));
            this.setXRot((float) ((double) this.getXRot() + (this.lerpXRot - (double) this.getXRot()) / (double) this.lerpSteps));
            --this.lerpSteps;
            this.setPos(d0, d2, d4);
            this.setRot(this.getYRot(), this.getXRot());
        } else if (!this.isEffectiveAi()) {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
        }
        if (this.random.nextInt(MobConfig.spawnChance) == 0 && this.level.getDifficulty() != Difficulty.PEACEFUL) {
            this.spawnMobs();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("MobLevel", this.entityData.get(mobLevel));
        ListTag list = new ListTag();
        this.spawnList.forEach(type -> list.add(StringTag.valueOf(PlatformUtils.INSTANCE.entities().getIDFrom(type).toString())));
        compound.put("Spawns", list);
        compound.putString("Element", this.type.toString());
        compound.putBoolean("FirstSpawn", this.initialSpawn);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("MobLevel")) {
            this.entityData.set(mobLevel, compound.getInt("MobLevel"));
        }
        compound.getList("Spawns", Tag.TAG_STRING)
                .forEach(nbt -> this.spawnList.add(PlatformUtils.INSTANCE.entities().getFromId(new ResourceLocation(nbt.getAsString()))));
        String el = compound.getString("Element");
        try {
            this.type = EnumElement.valueOf(el);
            this.entityData.set(elementType, this.type.getTranslation());
            this.entityData.set(element, this.type.ordinal());
        } catch (IllegalArgumentException e) {
            RuneCraftory.logger.error("Unable to set element type for gate entity {}", this);
        }
        this.initialSpawn = compound.getBoolean("FirstSpawn");
    }

    @Override
    protected LootContext.Builder createLootContext(boolean useLuck, DamageSource src) {
        return super.createLootContext(useLuck, src);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnReason) {
        return true;//this.world.getDifficulty() != Difficulty.PEACEFUL && super.canSpawn(world, reason) && this.world.getEntitiesWithinAABB(GateEntity.class, this.getBoundingBox().grow(48.0)).size() < 2;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return NonNullList.withSize(4, ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slotIn, ItemStack stack) {
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        Holder<Biome> biome = this.level.getBiome(this.blockPosition());
        this.spawnList.addAll(GateSpawning.pickRandomMobs(level.getLevel(), biome, this.random, this.random.nextInt(4) + 2, this.blockPosition()));
        this.type = this.getType(level, biome);
        this.entityData.set(mobLevel, LevelCalc.levelFromPos(this.level, this.blockPosition()));
        this.entityData.set(elementType, this.type.getTranslation());
        this.entityData.set(element, this.type.ordinal());
        this.setPos(this.getX(), this.getY() + 1, this.getZ());
        this.updateStatsToLevel();
        return spawnData;
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    private void spawnMobs() {
        if (!(this.level instanceof ServerLevel serverLevel) || serverLevel.getDifficulty() == Difficulty.PEACEFUL)
            return;
        if (!this.spawnList.isEmpty()) {
            int randAmount = this.random.nextInt(2) + 1;
            List<Entity> nearby = this.level.getEntities(this, this.getBoundingBox().inflate(18), entity ->
                    entity.getType() == ModEntities.treasureChest.get() ||
                            entity.getType() == ModEntities.monsterBox.get() ||
                            entity.getType() == ModEntities.gobbleBox.get() ||
                            GateEntity.this.spawnList.contains(entity.getType()));
            if (nearby.size() <= MobConfig.maxNearby) {
                for (int amount = 0; amount < randAmount; ++amount) {
                    double x = this.getX() + this.random.nextInt(9) - 4.0;
                    double y = this.getY() + this.random.nextInt(2) - 1.0;
                    double z = this.getZ() + this.random.nextInt(9) - 4.0;
                    int entityLevel = this.entityData.get(mobLevel);
                    int levelRand = Math.round(this.entityData.get(mobLevel) + (this.random.nextFloat() - 0.5f) * Math.round(entityLevel * 0.1f));
                    EntityType<?> type = this.spawnList.get(this.random.nextInt(this.spawnList.size()));
                    if (this.initialSpawn) {
                        this.initialSpawn = false;
                        if (this.getRandom().nextFloat() < 0.05) {
                            if (this.getRandom().nextFloat() < 0.4) {
                                if (this.getRandom().nextFloat() < 0.3)
                                    type = ModEntities.gobbleBox.get();
                                else
                                    type = ModEntities.monsterBox.get();
                            } else
                                type = ModEntities.treasureChest.get();
                        }
                    }
                    Entity entity = type.create(this.level);
                    if (entity instanceof EntityTreasureChest chest) {
                        entity.absMoveTo(x, y, z, this.level.random.nextFloat() * 360.0f, 0.0f);
                        if (this.level.noCollision(chest)) {
                            int rand = this.random.nextInt(100);
                            if (rand < 10)
                                chest.setTier(3);
                            else if (rand < 30)
                                chest.setTier(2);
                            else if (rand < 60)
                                chest.setTier(1);
                            this.level.addFreshEntity(entity);
                        }
                    } else if (entity instanceof Mob mob) {
                        BlockPos pos = new BlockPos(x, y, z);
                        boolean notSolid;
                        while ((notSolid = !this.level.getBlockState(pos.below()).entityCanStandOnFace(this.level, pos, entity, Direction.UP)) && pos.distToCenterSqr(x, y, z) < 16)
                            pos = pos.below();
                        if (!notSolid) {
                            if (mob instanceof BaseMonster)
                                ((BaseMonster) mob).setLevel(levelRand);
                            entity.absMoveTo(x, y, z, this.level.random.nextFloat() * 360.0f, 0.0f);
                            if (Platform.INSTANCE.canEntitySpawnSpawner(mob, this.level, (float) entity.getX(), (float) entity.getY(), (float) entity.getZ(), null, MobSpawnType.SPAWNER) && this.level.noCollision(mob)) {
                                mob.finalizeSpawn(serverLevel, this.level.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.SPAWNER, null, null);
                                AttributeInstance follow = mob.getAttribute(Attributes.FOLLOW_RANGE);
                                mob.restrictTo(this.blockPosition(), (int) Math.max(16, follow != null ? follow.getValue() * 0.75 : 0));
                                this.level.addFreshEntity(entity);
                                mob.spawnAnim();
                            }
                        }
                    }
                }
            }
        }
    }

    protected float reduceDamage(DamageSource damageSrc, float damageAmount) {
        float reduce = 0.0f;
        if (!damageSrc.isBypassMagic() && !damageSrc.isBypassArmor()) {
            if (damageSrc.isMagic()) {
                reduce = (float) this.getAttribute(ModAttributes.RF_MAGIC_DEFENCE.get()).getValue();
            } else {
                reduce = (float) this.getAttribute(ModAttributes.RF_DEFENCE.get()).getValue();
            }
        }
        float min = reduce > damageAmount * 2 ? 0 : 0.5f;
        return Math.max(min, damageAmount - reduce);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    protected void tickDeath() {
        if (this.deathTime == 5 && this.lastHurtByPlayer instanceof ServerPlayer) {
            Platform.INSTANCE.getPlayerData(this.lastHurtByPlayer).ifPresent(data -> {
                LevelCalc.addXP((ServerPlayer) this.lastHurtByPlayer, data, LevelCalc.gateXP(data, this));
                data.setMoney(this.lastHurtByPlayer, data.getMoney() + LevelCalc.getMoney(this.baseMoney(), this.level()));
            });
        }
        super.tickDeath();
    }

    @Override
    public void heal(float healAmount) {
    }

    @Override
    public void knockback(double strength, double x, double z) {
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.isInvulnerableTo(damageSrc)) {
            damageAmount = Platform.INSTANCE.onLivingHurt(this, damageSrc, damageAmount);
            if (damageAmount <= 0.0f) {
                return;
            }
            damageAmount = this.reduceDamage(damageSrc, damageAmount);
            if (damageAmount != 0.0f) {
                float f1 = this.getHealth();
                this.setHealth(f1 - damageAmount);
                this.getCombatTracker().recordDamage(damageSrc, f1, damageAmount);
            }
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (key.equals(mobLevel)) {
            this.updateStatsToLevel();
        }
        if (key.equals(element)) {
            this.type = EnumElement.values()[this.entityData.get(element)];
        }
        super.onSyncedDataUpdated(key);
    }

    @Override
    public boolean isInWall() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    private void updateStatsToLevel() {
        this.getAttribute(Attributes.MAX_HEALTH).removeModifier(attributeLevelMod);
        this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(attributeLevelMod, "rf.levelMod", (this.level() - 1) * MobConfig.gateHealthGain, AttributeModifier.Operation.ADDITION));
        this.getAttribute(ModAttributes.RF_DEFENCE.get()).removeModifier(attributeLevelMod);
        this.getAttribute(ModAttributes.RF_DEFENCE.get()).addPermanentModifier(new AttributeModifier(attributeLevelMod, "rf.levelMod", (this.level() - 1) * MobConfig.gateDefGain, AttributeModifier.Operation.ADDITION));
        this.getAttribute(ModAttributes.RF_MAGIC_DEFENCE.get()).removeModifier(attributeLevelMod);
        this.getAttribute(ModAttributes.RF_MAGIC_DEFENCE.get()).addPermanentModifier(new AttributeModifier(attributeLevelMod, "rf.levelMod", (this.level() - 1) * MobConfig.gateMDefGain, AttributeModifier.Operation.ADDITION));
        this.setHealth(this.getMaxHealth());
    }

    private EnumElement getType(ServerLevelAccessor level, Holder<Biome> key) {
        EnumElement element = EnumElement.values()[this.getRandom().nextInt(EnumElement.values().length)];
        if (key.is(ModTags.IS_PLAINS) && this.getRandom().nextFloat() < 0.5) {
            element = EnumElement.NONE;
        } else if (key.is(BiomeTags.IS_FOREST) && this.getRandom().nextFloat() < 0.5) {
            element = EnumElement.WIND;
        } else if (key.is(ModTags.IS_HOT) && this.getRandom().nextFloat() < 0.5) {
            element = EnumElement.FIRE;
        } else if (key.is(BiomeTags.IS_MOUNTAIN) && this.getRandom().nextFloat() < 0.5) {
            element = EnumElement.WIND;
        } else if (key.is(BiomeTags.IS_OCEAN) && this.getRandom().nextFloat() < 0.5) {
            element = EnumElement.WATER;
        } else if (key.is(ModTags.IS_SANDY) && this.getRandom().nextFloat() < 0.5) {
            element = EnumElement.EARTH;
        } else if (key.is(ModTags.IS_MAGICAL)) {
            if (this.getRandom().nextFloat() < 0.4) {
                element = EnumElement.LIGHT;
            } else if (this.getRandom().nextFloat() < 0.2) {
                element = EnumElement.LOVE;
            }
        } else if (key.is(ModTags.IS_SPOOKY) && this.getRandom().nextFloat() < 0.4) {
            element = EnumElement.DARK;
        }
        if (key.is(ModTags.IS_END)) {
            if (this.getRandom().nextFloat() < 0.3) {
                element = EnumElement.DARK;
            } else if (this.getRandom().nextFloat() < 0.3) {
                element = EnumElement.LIGHT;
            }
        }
        return element;
    }
}