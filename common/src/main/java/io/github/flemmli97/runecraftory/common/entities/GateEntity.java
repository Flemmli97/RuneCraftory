package io.github.flemmli97.runecraftory.common.entities;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.lib.LibConstants;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
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

    private List<EntityType<?>> spawnList = new ArrayList<>();
    private EnumElement type = EnumElement.NONE;
    public int rotate, clientParticles;
    public boolean clientParticleFlag;
    private static final EntityDataAccessor<String> elementType = SynchedEntityData.defineId(GateEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> element = SynchedEntityData.defineId(GateEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> mobLevel = SynchedEntityData.defineId(GateEntity.class, EntityDataSerializers.INT);
    private static final UUID attributeLevelMod = UUID.fromString("EC84560E-5266-4DC3-A4E1-388b97DBC0CB");

    public GateEntity(EntityType<? extends GateEntity> type, Level level) {
        super(type, level);
        if (level.isClientSide) {
            this.rotate = ((level.random.nextInt(2) == 0) ? 1 : -1);
        }
        this.updateAttributes();
        this.setNoGravity(true);
    }

    @Override
    public int level() {
        return this.entityData.get(mobLevel);
    }

    @Override
    public void setLevel(int lvl) {
        this.entityData.set(mobLevel, Math.min(10000, lvl));
    }

    public EnumElement getElement() {
        return this.type;
    }

    public String elementName() {
        return this.entityData.get(elementType);
    }

    @Override
    public int baseXP() {
        return MobConfig.gateXP;
    }

    @Override
    public int baseMoney() {
        return MobConfig.gateMoney;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(ModAttributes.RF_DEFENCE.get()).add(ModAttributes.RF_MAGIC_DEFENCE.get());
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
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("MobLevel", this.entityData.get(mobLevel));
        ListTag list = new ListTag();
        this.spawnList.forEach(type -> list.add(StringTag.valueOf(PlatformUtils.INSTANCE.entities().getIDFrom(type).toString())));
        compound.put("Spawns", list);
        compound.putString("Element", this.type.toString());
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
    }

    @Override
    public void setItemSlot(EquipmentSlot slotIn, ItemStack stack) {
    }

    @Override
    public void heal(float healAmount) {
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
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
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

    private void spawnMobs() {
        if (!(this.level instanceof ServerLevel serverLevel) || serverLevel.getDifficulty() == Difficulty.PEACEFUL)
            return;
        if (!this.spawnList.isEmpty()) {
            int randAmount = this.random.nextInt(2) + 1;
            List<Entity> nearby = this.level.getEntities(this, this.getBoundingBox().inflate(18), entity -> GateEntity.this.spawnList.contains(entity.getType()));
            if (nearby.size() <= MobConfig.maxNearby) {
                for (int amount = 0; amount < randAmount; ++amount) {
                    double x = this.getX() + this.random.nextInt(9) - 4.0;
                    double y = this.getY() + this.random.nextInt(2) - 1.0;
                    double z = this.getZ() + this.random.nextInt(9) - 4.0;
                    int entityLevel = this.entityData.get(mobLevel);
                    int levelRand = Math.round(this.entityData.get(mobLevel) + (this.random.nextFloat() - 0.5f) * Math.round(entityLevel * 0.1f));
                    EntityType<?> type = this.spawnList.get(this.random.nextInt(this.spawnList.size()));
                    Entity entity = type.create(this.level);
                    if (entity instanceof Mob mob) {
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

    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnReason) {
        return true;//this.world.getDifficulty() != Difficulty.PEACEFUL && super.canSpawn(world, reason) && this.world.getEntitiesWithinAABB(GateEntity.class, this.getBoundingBox().grow(48.0)).size() < 2;
    }

    public static boolean canSpawnAt(EntityType<? extends GateEntity> type, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, Random random) {
        return level.getDifficulty() != Difficulty.PEACEFUL && GateSpawning.hasSpawns(level, pos) && checkMobSpawnRules(type, level, reason, pos, random) && level.getEntitiesOfClass(GateEntity.class, new AABB(pos).inflate(MobConfig.minDist)).size() < MobConfig.maxGroup;
    }

    @Override
    protected void tickDeath() {
        /*if (this.deathTime == 5 && this.attackingPlayer != null) {
            IPlayer cap = this.attackingPlayer.getCapability(PlayerCapProvider.PlayerCap, null);
            cap.addXp(this.attackingPlayer, LevelCalc.xpFromLevel(this.baseXP(), this.level()));
            cap.setMoney(this.attackingPlayer, cap.getMoney() + LevelCalc.xpFromLevel(this.baseMoney(), this.level()));
        }*/
        super.tickDeath();
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
    public void knockback(double strength, double x, double z) {
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
    protected LootContext.Builder createLootContext(boolean useLuck, DamageSource src) {
        return super.createLootContext(useLuck, src);
    }

    /*@Override
    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
        int max = 0;
        float luck = 0.0f;
        if (source.getTrueSource() instanceof EntityPlayer) {
            luck = RFCalculations.getAttributeValue((EntityPlayer) source.getTrueSource(), ItemStatAttributes.RFRLUCK, null, null);
        }
        while (luck >= 1.0f) {
            luck -= 0.8;
            --max;
        }
        float dropChance = 0.25F;
        ItemStack drop = ItemStack.EMPTY;
        int maxDrop = 2;
        switch (this.type) {
            case DARK:
                maxDrop = 2;
                drop = new ItemStack(ModItems.crystalDark);
                dropChance = 0.1F;
                break;
            case EARTH:
                maxDrop = 2;
                drop = new ItemStack(ModItems.crystalEarth);
                dropChance = 0.25F;
                break;
            case FIRE:
                maxDrop = 2;
                drop = new ItemStack(ModItems.crystalFire);
                dropChance = 0.25F;
                break;
            case LIGHT:
                maxDrop = 2;
                drop = new ItemStack(ModItems.crystalLight);
                dropChance = 0.1F;
                break;
            case LOVE:
                maxDrop = 1;
                drop = new ItemStack(ModItems.crystalLove);
                dropChance = 0.05F;
                break;
            case WATER:
                maxDrop = 2;
                drop = new ItemStack(ModItems.crystalWater);
                dropChance = 0.3F;
                break;
            case WIND:
                maxDrop = 2;
                drop = new ItemStack(ModItems.crystalWind);
                dropChance = 0.3F;
                break;
            case NONE:
                break;
        }
        while (max < maxDrop)
        {
            if (this.rand.nextFloat() < dropChance + luck)
            {
                ItemUtils.spawnItemAtEntity(this, drop);
            }
            ++max;
        }
    }*/

    @Override
    public boolean isInWall() {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
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

    private void updateStatsToLevel() {
        this.getAttribute(Attributes.MAX_HEALTH).removeModifier(attributeLevelMod);
        this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(attributeLevelMod, "rf.levelMod", (this.level() - 1) * MobConfig.gateHealthGain, AttributeModifier.Operation.ADDITION));
        this.getAttribute(ModAttributes.RF_DEFENCE.get()).removeModifier(attributeLevelMod);
        this.getAttribute(ModAttributes.RF_DEFENCE.get()).addPermanentModifier(new AttributeModifier(attributeLevelMod, "rf.levelMod", (this.level() - 1) * MobConfig.gateDefGain, AttributeModifier.Operation.ADDITION));
        this.getAttribute(ModAttributes.RF_MAGIC_DEFENCE.get()).removeModifier(attributeLevelMod);
        this.getAttribute(ModAttributes.RF_MAGIC_DEFENCE.get()).addPermanentModifier(new AttributeModifier(attributeLevelMod, "rf.levelMod", (this.level() - 1) * MobConfig.gateMDefGain, AttributeModifier.Operation.ADDITION));
        this.setHealth(this.getMaxHealth());
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

    @Override
    public void applyFoodEffect(ItemStack stack) {
    }

    @Override
    public void removeFoodEffect() {
    }
}