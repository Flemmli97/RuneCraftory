package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolFishingRod;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.common.utils.LootTableResources;
import io.github.flemmli97.runecraftory.mixinhelper.ExtendedFishingRodHookTrigger;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.entity.EntityProjectile;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * The vanilla fishing hook has too much hardcoded stuff in it
 */
public class EntityCustomFishingHook extends EntityProjectile {

    private static final EntityDataAccessor<Boolean> DATA_BITING = SynchedEntityData.defineId(EntityCustomFishingHook.class, EntityDataSerializers.BOOLEAN);

    private boolean inFishingSpot, inSand;
    private EnumElement element = EnumElement.NONE;

    private FluidState currentFluidState;

    private int nibble;
    private int timeUntilBite;
    private float fishAngle;
    private final int luck;
    private final int lureSpeedBonus;
    private final int nibbleBonus;
    private int difficultyBonus;

    private BooleanSupplier canAttack;
    private Runnable setOnCooldown;

    public EntityCustomFishingHook(EntityType<? extends EntityCustomFishingHook> entityType, Level level) {
        super(entityType, level);
        this.luck = 0;
        this.lureSpeedBonus = 0;
        this.nibbleBonus = 0;
    }

    public EntityCustomFishingHook(Level world, LivingEntity shooter, int speed, int luck, int charge) {
        super(ModEntities.FISHING_HOOK.get(), world, shooter);
        this.setPos(this.getX(), this.getY() + 0.1, this.getZ());
        this.shoot(shooter, Math.max(-90, shooter.getXRot() - 5), shooter.getYRot(), 0, 1.1f + Math.max(-0.3f, Mth.sin(-shooter.getXRot() * Mth.DEG_TO_RAD)), 0);
        this.lureSpeedBonus = speed;
        this.luck = luck;
        this.nibbleBonus = charge;
    }

    public void setElement(EnumElement element) {
        this.element = element;
    }

    public void attackHandlingPlayer(BooleanSupplier canAttack, Runnable runnable) {
        this.canAttack = canAttack;
        this.setOnCooldown = runnable;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_BITING, false);
    }

    @Override
    public void tick() {
        if (this.shouldStopFishing()) {
            return;
        }
        BlockPos blockPos = this.blockPosition();
        BlockState state = this.level.getBlockState(blockPos);
        BlockState below = this.level.getBlockState(new BlockPos(this.position().add(0, -0.2, 0)));
        this.currentFluidState = this.level.getFluidState(blockPos);
        float fluid = 0.0f;
        this.inSand = false;
        if (this.currentFluidState.is(FluidTags.WATER)) {
            fluid = this.currentFluidState.getHeight(this.level, blockPos);
        } else if (state.isAir() && below.is(BlockTags.SAND)) {
            this.inFishingSpot = true;
            this.inSand = true;
        }
        if (!this.inFishingSpot && fluid > 0) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.3, 0.2, 0.3));
            this.inFishingSpot = true;
            return;
        }
        if (this.inFishingSpot) {
            Vec3 vec3 = this.getDeltaMovement();
            double d = this.getY() + vec3.y - blockPos.getY() - fluid;
            if (Math.abs(d) < 0.01) {
                d += Math.signum(d) * 0.1;
            }
            this.setDeltaMovement(vec3.x * 0.9, vec3.y - d * this.random.nextFloat() * 0.2, vec3.z * 0.9);
            boolean canFish = (fluid > 0 || this.inSand) && this.correctLocation(blockPos);
            if (canFish) {
                if (this.entityData.get(DATA_BITING)) {
                    this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.1 * this.random.nextFloat() * this.random.nextFloat(), 0.0));
                }
                if (!this.level.isClientSide) {
                    this.doFishing();
                }
            }
        }
        super.tick();
    }

    @Override
    public void moveEntity() {
        if (!this.currentFluidState.is(FluidTags.WATER)) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -this.getGravityVelocity(), 0.0));
        }
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.updateRotation();
        if (!this.inFishingSpot && (this.onGround || this.horizontalCollision)) {
            this.setDeltaMovement(Vec3.ZERO);
        }
        this.setDeltaMovement(this.getDeltaMovement().scale(this.motionReduction(this.isInWater())));
        this.reapplyPosition();
    }

    @Override
    protected float motionReduction(boolean inWater) {
        return 0.92f;
    }

    @Override
    public boolean isPiercing() {
        return true;
    }

    @Override
    public int maxPierceAmount() {
        return 5;
    }

    @Override
    protected boolean entityRayTraceHit(EntityHitResult entityHitResult) {
        if (this.canAttack != null && !this.canAttack.getAsBoolean()) {
            this.discard();
            return false;
        }
        boolean att = CombatUtils.damageWithFaintAndCrit(this.getOwner(), entityHitResult.getEntity(), new CustomDamage.Builder(this, this.getOwner()).noKnockback().element(this.element).hurtResistant(5), CombatUtils.getAttributeValue(this.getOwner(), Attributes.ATTACK_DAMAGE), null);
        if (att && this.setOnCooldown != null) {
            this.setOnCooldown.run();
            this.setOnCooldown = null;
            this.canAttack = null;
            if (this.getOwner() instanceof ServerPlayer player)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
                    LevelCalc.levelSkill(player, data, EnumSkills.FISHING, 10);
                    LevelCalc.levelSkill(player, data, EnumSkills.WATER, 1);
                });
        }
        return att;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        this.setDeltaMovement(this.getDeltaMovement().normalize().scale(blockHitResult.distanceTo(this)));
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        if (this.getOwner() instanceof LivingEntity living)
            Platform.INSTANCE.getEntityData(living).ifPresent(data -> data.fishingHook = null);
    }

    @Override
    public void onClientRemoval() {
        super.onClientRemoval();
        if (this.getOwner() instanceof LivingEntity living)
            Platform.INSTANCE.getEntityData(living).ifPresent(data -> data.fishingHook = null);
    }

    @Override
    public void onUpdateOwner() {
        super.onUpdateOwner();
        if (this.getOwner() instanceof LivingEntity living)
            Platform.INSTANCE.getEntityData(living).ifPresent(data -> data.fishingHook = this);
    }

    private boolean shouldStopFishing() {
        if (!(this.getOwner() instanceof LivingEntity entity))
            return true;
        ItemStack itemStack = entity.getMainHandItem();
        ItemStack itemStack2 = entity.getOffhandItem();
        boolean bl = itemStack.getItem() instanceof ItemToolFishingRod;
        boolean bl2 = itemStack2.getItem() instanceof ItemToolFishingRod;
        if (entity.isRemoved() || !entity.isAlive() || !bl && !bl2 || this.distanceToSqr(entity) > 1024.0) {
            this.discard();
            return true;
        }
        return false;
    }

    /**
     * Check for a 3x2 water area with air above
     */
    private boolean correctLocation(BlockPos blockPos) {
        for (int i = -1; i < 2; ++i) {
            int state = i != 1 ? 0 : 1;
            int yD = this.inSand ? i - 1 : i;
            boolean check = BlockPos.betweenClosedStream(blockPos.offset(-1, yD, -1), blockPos.offset(1, yD, 1))
                    .allMatch(p -> this.blockCheck(p, state) != LocationType.INVALID);
            if (!check)
                return false;
        }
        return true;
    }

    /**
     * @param state 0 = water only, 1 = air only, 2 = both
     */
    private LocationType blockCheck(BlockPos blockPos, int state) {
        BlockState blockState = this.level.getBlockState(blockPos);
        if (state != 0 && (blockState.isAir() || blockState.is(Blocks.LILY_PAD))) {
            return LocationType.AIR;
        }
        if (this.inSand) {
            if (state != 1 && (blockState.is(BlockTags.SAND)))
                return LocationType.MATCH;
            return LocationType.INVALID;
        }
        FluidState fluidState = blockState.getFluidState();
        if (state != 1 && (fluidState.is(FluidTags.WATER) && fluidState.isSource() && blockState.getCollisionShape(this.level, blockPos).isEmpty()))
            return LocationType.MATCH;
        return LocationType.INVALID;
    }

    protected void doFishing() {
        if (!(this.level instanceof ServerLevel serverLevel))
            return;
        if (this.nibble > 0) {
            --this.nibble;
            if (this.nibble <= 0) {
                this.timeUntilBite = 0;
                this.getEntityData().set(DATA_BITING, false);
            }
        } else if (this.timeUntilBite > 0) {
            --this.timeUntilBite;
            float splashChance = 0.15f;
            if (this.timeUntilBite < 20) {
                splashChance += (float) (20 - this.timeUntilBite) * 0.05f;
            } else if (this.timeUntilBite < 40) {
                splashChance += (float) (40 - this.timeUntilBite) * 0.02f;
            } else if (this.timeUntilBite < 60) {
                splashChance += (float) (60 - this.timeUntilBite) * 0.01f;
            }
            if (this.random.nextFloat() < splashChance) {
                float a = Mth.nextFloat(this.random, 0.0f, 360.0f) * ((float) Math.PI / 180);
                float c = Mth.nextFloat(this.random, 25.0f, 60.0f);
                double x = this.getX() + (Mth.sin(a) * c) * 0.1;
                double y = (float) Mth.floor(this.getY()) + 1.0f;
                double z = this.getZ() + (Mth.cos(a) * c) * 0.1;
                if (this.inSand) {
                    Vec3 belowPos = this.position().add(0, -0.2, 0);
                    BlockState below = this.level.getBlockState(new BlockPos(x, belowPos.y, z));
                    if (below.is(BlockTags.SAND))
                        serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, below), x, y - 0.9, z, 2 + this.random.nextInt(2), 0.1f, 0.0, 0.1f, 0.0);
                } else if (serverLevel.getBlockState(new BlockPos(x, y - 1.0, z)).is(Blocks.WATER)) {
                    serverLevel.sendParticles(ParticleTypes.SPLASH, x, y, z, 2 + this.random.nextInt(2), 0.1f, 0.0, 0.1f, 0.0);
                }
            }
            if (this.timeUntilBite > 0) {
                this.fishAngle += (this.random.nextGaussian() * 4);
                float angle = this.fishAngle * ((float) Math.PI / 180);
                float g = Mth.sin(angle);
                float h = Mth.cos(angle);
                double y = Mth.floor(this.getY()) + 1.0f;
                double z = this.getZ() + (h * this.timeUntilBite * 0.1f);
                double x = this.getX() + (g * this.timeUntilBite * 0.1f);
                if (this.inSand)
                    y -= 1;
                BlockState blockState = serverLevel.getBlockState(new BlockPos(x, y - 1, z));
                if ((this.inSand && blockState.is(Blocks.SAND)) || (!this.inSand && blockState.is(Blocks.WATER))) {
                    ParticleOptions bubble = ParticleTypes.BUBBLE;
                    ParticleOptions particle = ParticleTypes.BUBBLE;
                    if (this.inSand) {
                        bubble = new BlockParticleOption(ParticleTypes.BLOCK, blockState);
                        particle = bubble;
                        y += 0.1;
                    }
                    if (this.random.nextFloat() < 0.15f) {
                        serverLevel.sendParticles(bubble, x, y - 0.1f, z, 1, g, 0.1, h, 0.0);
                    }
                    float k = g * 0.04f;
                    float l = h * 0.04f;
                    serverLevel.sendParticles(particle, x, y, z, 0, l, 0.01, -k, 1.0);
                    serverLevel.sendParticles(particle, x, y, z, 0, -l, 0.01, k, 1.0);
                }
            } else {
                this.playSound(SoundEvents.FISHING_BOBBER_SPLASH, 0.25f, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.4f);
                double m = this.getY() + 0.5;
                ParticleOptions particle = ParticleTypes.BUBBLE;
                if (this.inSand) {
                    BlockState below = this.level.getBlockState(new BlockPos(this.position().add(0, -0.2, 0)));
                    particle = new BlockParticleOption(ParticleTypes.BLOCK, below);
                }
                serverLevel.sendParticles(particle, this.getX(), m, this.getZ(), (int) (1.0f + this.getBbWidth() * 20.0f), this.getBbWidth(), 0.0, this.getBbWidth(), 0.2f);
                if (!this.inSand)
                    serverLevel.sendParticles(ParticleTypes.FISHING, this.getX(), m, this.getZ(), (int) (1.0f + this.getBbWidth() * 20.0f), this.getBbWidth(), 0.0, this.getBbWidth(), 0.2f);
                serverLevel.sendParticles(ParticleTypes.NOTE, this.getX(), m, this.getZ(), 0, 0, 0, 0, 0);
                this.difficultyBonus = Mth.nextInt(this.random, 0, 3);
                this.nibble = Mth.nextInt(this.random, 12, 30 - this.difficultyBonus * 3) - this.difficultyBonus * 5;
                this.nibble += this.nibbleBonus * 3;
                this.nibble = Math.max(1, this.nibble);
                this.getEntityData().set(DATA_BITING, true);
            }
        } else {
            this.fishAngle = Mth.nextFloat(this.random, 0.0f, 360.0f);
            this.timeUntilBite = Mth.nextInt(this.random, Math.max(5, 100 - this.lureSpeedBonus * 15), Math.max(50, 600 - this.lureSpeedBonus * 75));
        }
    }

    public void retract(ItemStack stack) {
        Entity eOwner = this.getOwner();
        if (this.level.isClientSide || this.shouldStopFishing())
            return;
        if (!(eOwner instanceof ServerPlayer owner)) {
            this.discard();
            return;
        }
        if (this.nibble > 0) {
            //For now using vanilla loottables
            float luck = this.luck + owner.getLuck() + this.difficultyBonus * 0.5f
                    + Platform.INSTANCE.getPlayerData(owner).map(d -> d.getSkillLevel(EnumSkills.FISHING).getLevel()).orElse(0) * 0.02f;
            LootContext.Builder builder = new LootContext.Builder((ServerLevel) this.level)
                    .withParameter(LootContextParams.ORIGIN, this.position())
                    .withParameter(LootContextParams.TOOL, stack)
                    .withParameter(LootContextParams.THIS_ENTITY, this)
                    .withRandom(this.random)
                    .withLuck(luck);
            ResourceLocation loot = this.inSand ? LootTableResources.SAND_FISHING : LootTableResources.FISHING;
            LootTable lootTable = this.level.getServer().getLootTables().get(loot);
            List<ItemStack> list = lootTable.getRandomItems(builder.create(LootContextParamSets.FISHING));
            ((ExtendedFishingRodHookTrigger) CriteriaTriggers.FISHING_ROD_HOOKED).customTrigger(owner, stack, this, list);
            for (ItemStack itemStack2 : list) {
                ItemEntity itemEntity = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), itemStack2);
                double d = owner.getX() - this.getX();
                double e = owner.getY() - this.getY();
                double f = owner.getZ() - this.getZ();
                itemEntity.setDeltaMovement(d * 0.1, e * 0.1 + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08, f * 0.1);
                this.level.addFreshEntity(itemEntity);
                owner.level.addFreshEntity(new ExperienceOrb(owner.level, owner.getX(), owner.getY() + 0.5, owner.getZ() + 0.5, this.random.nextInt(6) + 1));
                if (itemStack2.is(ItemTags.FISHES))
                    owner.awardStat(Stats.FISH_CAUGHT, 1);
            }
            if (this.getOwner() instanceof ServerPlayer player)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
                    LevelCalc.useRP(player, data, 10 * (this.nibbleBonus + 1), true, 0, true, EnumSkills.FISHING);
                    LevelCalc.levelSkill(player, data, EnumSkills.FISHING, 25);
                    LevelCalc.levelSkill(player, data, EnumSkills.WATER, 5);
                });
        }

        this.discard();
    }

    enum LocationType {
        AIR,
        MATCH,
        INVALID
    }
}
