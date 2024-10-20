package io.github.flemmli97.runecraftory.common.entities;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.attachment.player.LevelExpPair;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityTreasureChest;
import io.github.flemmli97.runecraftory.common.lib.LibConstants;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
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
import net.minecraft.tags.FluidTags;
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
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class GateEntity extends Mob implements IBaseMob {

    private static final Map<EnumElement, ResourceLocation> LOOT_RES = new HashMap<>();

    private static final EntityDataAccessor<String> ELEMENT_TYPE = SynchedEntityData.defineId(GateEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> ELEMENT = SynchedEntityData.defineId(GateEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MOB_LEVEL = SynchedEntityData.defineId(GateEntity.class, EntityDataSerializers.INT);
    private static final UUID ATTRIBUTE_LEVEL_MOD = UUID.fromString("EC84560E-5266-4DC3-A4E1-388b97DBC0CB");
    public int rotate, clientParticles;
    public boolean clientParticleFlag;
    private final List<EntityType<?>> spawnList = new ArrayList<>();
    private EnumElement type = EnumElement.NONE;
    private boolean initialSpawn = true;
    private final LevelExpPair expPair = new LevelExpPair();
    private boolean removeCauseEmptyList;
    private int maxNearby;
    private int spawnDelay;

    public GateEntity(EntityType<? extends GateEntity> type, Level level) {
        super(type, level);
        if (level.isClientSide) {
            this.rotate = ((level.random.nextInt(2) == 0) ? 1 : -1);
        }
        this.updateAttributes();
        this.setNoGravity(true);
        this.maxNearby = this.getRandom().nextInt(1 + MobConfig.maxNearby - MobConfig.minNearby) + MobConfig.minNearby;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(ModAttributes.DEFENCE.get()).add(ModAttributes.MAGIC_DEFENCE.get())
                .add(ModAttributes.RES_WATER.get()).add(ModAttributes.RES_EARTH.get())
                .add(ModAttributes.RES_WIND.get()).add(ModAttributes.RES_FIRE.get())
                .add(ModAttributes.RES_DARK.get()).add(ModAttributes.RES_LIGHT.get())
                .add(ModAttributes.RES_LOVE.get());
    }

    public static boolean gateSpawnRules(EntityType<? extends Mob> type, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, BlockState state, Random random) {
        BlockPos blockPos = pos.below();
        return spawnType == MobSpawnType.SPAWNER || level.getBlockState(blockPos).isValidSpawn(level, blockPos, type) || (level.getSeaLevel() - 5 > pos.getY() && state.getFluidState().is(FluidTags.WATER));
    }

    public static boolean canSpawnAt(EntityType<? extends GateEntity> type, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, Random random) {
        BlockState state = level.getBlockState(pos);
        return level.getDifficulty() != Difficulty.PEACEFUL && DataPackHandler.INSTANCE.gateSpawnsManager().hasSpawns(level, pos, state)
                && level.getLevel().getPoiManager().find(PoiType.MEETING.getPredicate(), p -> true, pos, MobConfig.bellRadius, PoiManager.Occupancy.ANY).isEmpty()
                && gateSpawnRules(type, level, reason, pos, state, random)
                && level.getEntitiesOfClass(GateEntity.class, new AABB(pos).inflate(MobConfig.minDist)).size() < MobConfig.maxGroup;
    }

    public static ResourceLocation getGateLootLocation(EnumElement element) {
        ResourceLocation def = ModEntities.GATE.get().getDefaultLootTable();
        return LOOT_RES.computeIfAbsent(element, e -> new ResourceLocation(def.getNamespace(),
                def.getPath() + "_" + e.name().toLowerCase(Locale.ROOT)));
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader level) {
        return level.isUnobstructed(this);
    }

    @Override
    public LevelExpPair level() {
        this.expPair.setLevel(this.entityData.get(MOB_LEVEL), l -> 0);
        return this.expPair;
    }

    @Override
    public int friendPoints(UUID uuid) {
        return -1;
    }

    @Override
    public void setLevel(int lvl) {
        this.entityData.set(MOB_LEVEL, Mth.clamp(lvl, 1, LibConstants.MAX_MONSTER_LEVEL));
    }

    @Override
    public int baseXP() {
        return MobConfig.gateXp;
    }

    @Override
    public int baseMoney() {
        return MobConfig.gateMoney;
    }

    @Override
    public boolean applyFoodEffect(ItemStack stack) {
        return false;
    }

    @Override
    public void removeFoodEffect() {
    }

    public EnumElement getElement() {
        return this.type;
    }

    public String elementName() {
        return this.entityData.get(ELEMENT_TYPE);
    }

    private void updateAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(MobConfig.gateHealth);
        this.getAttribute(ModAttributes.DEFENCE.get()).setBaseValue(MobConfig.gateDef);
        this.getAttribute(ModAttributes.MAGIC_DEFENCE.get()).setBaseValue(MobConfig.gateMDef);
        this.setHealth(this.getMaxHealth());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ELEMENT_TYPE, "none");
        this.entityData.define(MOB_LEVEL, LibConstants.BASE_LEVEL);
        this.entityData.define(ELEMENT, 0);
    }

    @Override
    public void tick() {
        if (Platform.INSTANCE.onLivingUpdate(this)) return;
        if (!this.level.isClientSide) {
            if (this.removeCauseEmptyList) {
                this.discard();
                return;
            }
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
        if (!this.level.isClientSide && --this.spawnDelay <= 0 && this.level.getDifficulty() != Difficulty.PEACEFUL) {
            this.spawnDelay = MobConfig.minSpawnDelay >= MobConfig.maxSpawnDelay ? MobConfig.minSpawnDelay : this.getRandom().nextInt(MobConfig.minSpawnDelay, MobConfig.maxSpawnDelay);
            if (this.spawnMobs(1)) {
                this.spawnDelay *= 0.4;
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("MobLevel", this.entityData.get(MOB_LEVEL));
        ListTag list = new ListTag();
        this.spawnList.forEach(type -> list.add(StringTag.valueOf(PlatformUtils.INSTANCE.entities().getIDFrom(type).toString())));
        compound.put("Spawns", list);
        compound.putString("Element", this.type.toString());
        compound.putBoolean("FirstSpawn", this.initialSpawn);
        compound.putInt("MaxNearby", this.maxNearby);
        compound.putInt("SpawnDelay", this.spawnDelay);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("MobLevel")) {
            this.entityData.set(MOB_LEVEL, compound.getInt("MobLevel"));
        }
        compound.getList("Spawns", Tag.TAG_STRING)
                .forEach(nbt -> this.spawnList.add(PlatformUtils.INSTANCE.entities().getFromId(new ResourceLocation(nbt.getAsString()))));
        if (compound.contains("Element")) {
            String el = compound.getString("Element");
            try {
                this.type = EnumElement.valueOf(el);
                this.entityData.set(ELEMENT_TYPE, this.type.getTranslation());
                this.entityData.set(ELEMENT, this.type.ordinal());
            } catch (IllegalArgumentException e) {
                RuneCraftory.LOGGER.error("Unable to set element type for gate entity {}", this);
            }
        }
        this.initialSpawn = compound.getBoolean("FirstSpawn");
        this.maxNearby = compound.getInt("MaxNearby");
        this.spawnDelay = compound.getInt("SpawnDelay");
    }

    @Override
    protected LootContext.Builder createLootContext(boolean attackedRecently, DamageSource src) {
        return super.createLootContext(attackedRecently, src);
    }

    @Override
    protected ResourceLocation getDefaultLootTable() {
        return getGateLootLocation(this.getElement());
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
        Holder<Biome> biome = level.getBiome(this.blockPosition());
        this.type = this.getType(level, biome);
        LevelCalc.GateLevelResult gateLevel = LevelCalc.levelFromPos(level.getLevel(), this.position());
        this.entityData.set(MOB_LEVEL, gateLevel.level());
        this.entityData.set(ELEMENT_TYPE, this.type.getTranslation());
        this.entityData.set(ELEMENT, this.type.ordinal());
        this.spawnList.addAll(DataPackHandler.INSTANCE.gateSpawnsManager().pickRandomMobs(level.getLevel(), this, biome, this.random,
                this.random.nextInt(3) + 1, this.blockPosition(), gateLevel.nearby()));
        this.setPos(this.getX(), this.getY() + 1, this.getZ());
        this.updateStatsToLevel();
        this.spawnMobs(Math.max(1, this.maxNearby - 2));
        this.spawnDelay = this.getRandom().nextInt(MobConfig.minSpawnDelay, MobConfig.maxSpawnDelay);
        //Cant check during spawn conditions since gate level isnt set there yet
        if (this.spawnList.isEmpty() && reason != MobSpawnType.SPAWN_EGG && reason != MobSpawnType.COMMAND)
            this.removeCauseEmptyList = true;
        return spawnData;
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    private boolean spawnMobs(int count) {
        if (!(this.level instanceof ServerLevel serverLevel) || serverLevel.getDifficulty() == Difficulty.PEACEFUL)
            return false;
        if (!this.spawnList.isEmpty()) {
            List<Entity> nearby = this.level.getEntities(this, this.getBoundingBox().inflate(18), entity ->
                    entity.getType() == ModEntities.TREASURE_CHEST.get() ||
                            entity.getType() == ModEntities.MONSTER_BOX.get() ||
                            entity.getType() == ModEntities.GOBBLE_BOX.get() ||
                            GateEntity.this.spawnList.contains(entity.getType()));
            if (nearby.size() <= this.maxNearby) {
                for (int amount = 0; amount < count; ++amount) {
                    double x = this.getX() + this.random.nextInt(9) - 4.0;
                    double y = this.getY() + this.random.nextInt(2) - 1.0;
                    double z = this.getZ() + this.random.nextInt(9) - 4.0;
                    int levelRand = LevelCalc.randomizedLevel(this.random, this.entityData.get(MOB_LEVEL));
                    EntityType<?> type = this.spawnList.get(this.random.nextInt(this.spawnList.size()));
                    if (this.initialSpawn) {
                        this.initialSpawn = false;
                        EntityType<?> chest = EntityUtils.trySpawnTreasureChest(this);
                        if (chest != null) {
                            type = chest;
                        }
                    }
                    Entity entity = type.create(this.level);
                    if (entity instanceof EntityTreasureChest chest) {
                        entity.absMoveTo(x, y, z, this.level.random.nextFloat() * 360.0f, 0.0f);
                        if (this.level.noCollision(chest)) {
                            EntityUtils.tieredTreasureChest(this, chest);
                            this.level.addFreshEntity(entity);
                        }
                    } else if (entity instanceof Mob mob) {
                        BlockPos pos = new BlockPos(x, y, z);
                        boolean notSolid;
                        BlockState state;
                        while ((notSolid = !(state = this.level.getBlockState(pos.below())).entityCanStandOnFace(this.level, pos, entity, Direction.UP) && !state.getFluidState().is(FluidTags.WATER)) && pos.distToCenterSqr(x, y, z) < 16)
                            pos = pos.below();
                        if (!notSolid) {
                            if (mob instanceof BaseMonster)
                                ((BaseMonster) mob).setLevel(levelRand);
                            entity.absMoveTo(x, y, z, this.level.random.nextFloat() * 360.0f, 0.0f);
                            if (Platform.INSTANCE.canEntitySpawnSpawner(mob, this.level, (float) entity.getX(), (float) entity.getY(), (float) entity.getZ(), null, MobSpawnType.SPAWNER) && this.level.noCollision(mob)) {
                                mob.finalizeSpawn(serverLevel, this.level.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.SPAWNER, null, null);
                                AttributeInstance follow = mob.getAttribute(Attributes.FOLLOW_RANGE);
                                mob.restrictTo(this.blockPosition(), (int) Math.max(18, follow != null ? follow.getValue() * 0.75 : 0));
                                this.level.addFreshEntity(entity);
                                mob.spawnAnim();
                            }
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected float getDamageAfterArmorAbsorb(DamageSource damageSrc, float damageAmount) {
        float reduce = 0.0f;
        if (!damageSrc.isBypassMagic() && !damageSrc.isBypassArmor()) {
            if (damageSrc.isMagic()) {
                reduce = (float) this.getAttribute(ModAttributes.MAGIC_DEFENCE.get()).getValue();
            } else {
                reduce = (float) this.getAttribute(ModAttributes.DEFENCE.get()).getValue();
            }
        }
        float min = reduce > damageAmount * 2 ? 0 : 0.5f;
        return super.getDamageAfterArmorAbsorb(damageSrc, Math.max(min, damageAmount - reduce));
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    protected void tickDeath() {
        if (this.deathTime == 5) {
            if (!this.level.isClientSide && this.getLastHurtByMob() != null)
                LevelCalc.addXP(this.getLastHurtByMob(), this.baseXP(), this.baseMoney(), this.level().getLevel());
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
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (this.level.isClientSide) {
            if (key.equals(MOB_LEVEL)) {
                this.updateStatsToLevel();
            }
            if (key.equals(ELEMENT)) {
                this.type = EnumElement.values()[this.entityData.get(ELEMENT)];
            }
        }
    }

    @Override
    public boolean isInWall() {
        return false;
    }

    @Override
    public void push(Entity entity) {
        if (entity instanceof Player)
            super.push(entity);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    public boolean canBeCollidedWith(Entity other) {
        return other instanceof Player;
    }

    private void updateStatsToLevel() {
        this.getAttribute(Attributes.MAX_HEALTH).removeModifier(ATTRIBUTE_LEVEL_MOD);
        this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(ATTRIBUTE_LEVEL_MOD, "rf.levelMod", (this.level().getLevel() - 1) * MobConfig.gateHealthGain, AttributeModifier.Operation.ADDITION));
        this.getAttribute(ModAttributes.DEFENCE.get()).removeModifier(ATTRIBUTE_LEVEL_MOD);
        this.getAttribute(ModAttributes.DEFENCE.get()).addPermanentModifier(new AttributeModifier(ATTRIBUTE_LEVEL_MOD, "rf.levelMod", (this.level().getLevel() - 1) * MobConfig.gateDefGain, AttributeModifier.Operation.ADDITION));
        this.getAttribute(ModAttributes.MAGIC_DEFENCE.get()).removeModifier(ATTRIBUTE_LEVEL_MOD);
        this.getAttribute(ModAttributes.MAGIC_DEFENCE.get()).addPermanentModifier(new AttributeModifier(ATTRIBUTE_LEVEL_MOD, "rf.levelMod", (this.level().getLevel() - 1) * MobConfig.gateMDefGain, AttributeModifier.Operation.ADDITION));
        this.setHealth(this.getMaxHealth());
    }

    private EnumElement getType(ServerLevelAccessor level, Holder<Biome> key) {
        EnumElement element = EnumElement.values()[this.getRandom().nextInt(EnumElement.values().length)];
        if (key.is(RunecraftoryTags.IS_PLAINS) && this.getRandom().nextFloat() < 0.5) {
            element = EnumElement.NONE;
        } else if (key.is(BiomeTags.IS_FOREST) && this.getRandom().nextFloat() < 0.5) {
            element = EnumElement.WIND;
        } else if (key.is(RunecraftoryTags.IS_HOT) && this.getRandom().nextFloat() < 0.5) {
            element = EnumElement.FIRE;
        } else if (key.is(BiomeTags.IS_MOUNTAIN) && this.getRandom().nextFloat() < 0.5) {
            element = EnumElement.WIND;
        } else if (key.is(BiomeTags.IS_OCEAN) && this.getRandom().nextFloat() < 0.5) {
            element = EnumElement.WATER;
        } else if (key.is(RunecraftoryTags.IS_SANDY) && this.getRandom().nextFloat() < 0.5) {
            element = EnumElement.EARTH;
        } else if (key.is(RunecraftoryTags.IS_MAGICAL)) {
            if (this.getRandom().nextFloat() < 0.4) {
                element = EnumElement.LIGHT;
            } else if (this.getRandom().nextFloat() < 0.2) {
                element = EnumElement.LOVE;
            }
        } else if (key.is(RunecraftoryTags.IS_SPOOKY) && this.getRandom().nextFloat() < 0.4) {
            element = EnumElement.DARK;
        }
        if (key.is(RunecraftoryTags.IS_END)) {
            if (this.getRandom().nextFloat() < 0.3) {
                element = EnumElement.DARK;
            } else if (this.getRandom().nextFloat() < 0.3) {
                element = EnumElement.LIGHT;
            }
        }
        return element;
    }

    @Override
    public boolean broadcastToPlayer(ServerPlayer player) {
        return !this.removeCauseEmptyList;
    }
}