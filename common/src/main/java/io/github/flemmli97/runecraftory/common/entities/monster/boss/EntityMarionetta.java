package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityCards;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityDarkBeam;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityFurniture;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityMarionettaTrap;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.MarionettaAttackGoal;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class EntityMarionetta extends BossMonster {

    public static final AnimatedAction melee = new AnimatedAction(10, 5, "melee");
    public static final AnimatedAction spin = new AnimatedAction(31, 6, "spin");
    public static final AnimatedAction card_attack = new AnimatedAction(13, 9, "card_attack");
    public static final AnimatedAction chest_attack = new AnimatedAction(24, 6, "chest_attack");
    public static final AnimatedAction chest_throw = new AnimatedAction(105, 7, "chest_throw");
    public static final AnimatedAction stuffed_animals = new AnimatedAction(15, 9, "stuffed_animals");
    public static final AnimatedAction dark_beam = new AnimatedAction(16, 6, "dark_beam");
    public static final AnimatedAction furniture = new AnimatedAction(24, 8, "furniture");
    public static final AnimatedAction defeat = new AnimatedAction(204, 150, "defeat", "defeat", 1, false);
    public static final AnimatedAction angry = new AnimatedAction(28, 0, "angry");
    private static final EntityDataAccessor<Boolean> CAUGHT = SynchedEntityData.defineId(EntityMarionetta.class, EntityDataSerializers.BOOLEAN);
    private static final AnimatedAction[] anims = new AnimatedAction[]{melee, spin, card_attack, chest_attack, chest_throw, stuffed_animals, dark_beam, furniture, defeat, angry};
    public final MarionettaAttackGoal<EntityMarionetta> attack = new MarionettaAttackGoal<>(this);
    private final List<LivingEntity> caughtEntities = new ArrayList<>();
    private final AnimationHandler<EntityMarionetta> animationHandler = new AnimationHandler<>(this, anims)
            .setAnimationChangeFunc(anim -> {
                if (this.entityData.get(CAUGHT)) {
                    if (!this.level.isClientSide) {
                        this.entityData.set(CAUGHT, false);
                        this.getAnimationHandler().setAnimation(chest_throw);
                    }
                    return true;
                }
                return false;
            });
    private double[] aiVarHelper;

    public EntityMarionetta(EntityType<? extends EntityMarionetta> type, Level world) {
        super(type, world);
        if (!world.isClientSide)
            this.goalSelector.addGoal(1, this.attack);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CAUGHT, false);
    }

    @Override
    public void setEnraged(boolean flag, boolean load) {
        super.setEnraged(flag, load);
        if (flag && !load)
            this.getAnimationHandler().setAnimation(angry);
    }

    public boolean caughtTarget() {
        return this.entityData.get(CAUGHT);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.29);
        super.applyAttributes();
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (anim.getID().equals(chest_throw.getID()))
            return false;
        if (anim.getID().equals(defeat.getID()) || anim.getID().equals(angry.getID()))
            return type == AnimationType.IDLE;
        if (type == AnimationType.GENERICATTACK)
            return this.isEnraged() || (!anim.getID().equals(dark_beam.getID()) && !anim.getID().equals(furniture.getID()));
        return false;
    }

    @Override
    public int animationCooldown(AnimatedAction anim) {
        int diffAdd = this.difficultyCooldown();
        return 30 + this.getRandom().nextInt(22) - (this.isEnraged() ? 20 : 0) + diffAdd;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.caughtEntities.contains(source.getEntity()))
            return false;
        return (!this.getAnimationHandler().hasAnimation() || !(this.getAnimationHandler().isCurrentAnim(chest_throw.getID(), angry.getID()))) && super.hurt(source, amount);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.getAnimationHandler().isCurrentAnim(angry.getID(), defeat.getID());
    }

    @Override
    public void push(double x, double y, double z) {
        if (this.getAnimationHandler().isCurrentAnim(angry.getID(), defeat.getID()))
            return;
        super.push(x, y, z);
    }

    @Override
    public AnimatedAction getDeathAnimation() {
        return defeat;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        LivingEntity target = this.getTarget();
        if (target != null) {
            if (!anim.getID().equals(spin.getID()))
                this.lookAt(target, 180.0f, 50.0f);
        }
        switch (anim.getID()) {
            case "melee" -> {
                if (target != null) {
                    this.getNavigation().moveTo(target, 1.0);
                }
                if (anim.canAttack()) {
                    this.mobAttack(anim, target, this::doHurtTarget);
                }
            }
            case "spin" -> {
                if (this.aiVarHelper == null)
                    return;
                this.setDeltaMovement(new Vec3(this.aiVarHelper[0], 0, this.aiVarHelper[2]));
                if (anim.getTick() >= anim.getAttackTime()) {
                    this.mobAttack(anim, null, e -> {
                        CustomDamage source = CombatUtils.build(this, e, new CustomDamage.Builder(this)).hurtResistant(8).get();
                        CombatUtils.mobAttack(this, e, source, CombatUtils.getAttributeValue(this, Attributes.ATTACK_DAMAGE, e));
                    });
                }
            }
            case "card_attack" -> {
                this.getNavigation().stop();
                if (anim.canAttack())
                    this.cardAttack();
            }
            case "chest_attack" -> {
                if (this.aiVarHelper == null)
                    return;
                this.setDeltaMovement(new Vec3(this.aiVarHelper[0], 0, this.aiVarHelper[2]));
                if (anim.getTick() >= anim.getAttackTime()) {
                    this.mobAttack(anim, null, e -> {
                        if (!this.caughtEntities.contains(e)) {
                            this.catchEntity(e);
                        }
                    });
                }
            }
            case "chest_throw" -> {
                this.getNavigation().stop();
                if (anim.canAttack()) {
                    Vec3 throwVec = new Vec3(this.getLookAngle().x(), 0, this.getLookAngle().z())
                            .normalize().scale(1.2).add(0, 0.85, 0);
                    EntityMarionettaTrap trap = new EntityMarionettaTrap(this.level, this);
                    this.caughtEntities.forEach(e -> {
                        e.addEffect(new MobEffectInstance(ModEffects.trueInvis.get(), 100, 1, true, false, false));
                        trap.addCaughtEntity(e);
                    });
                    trap.setDeltaMovement(throwVec);
                    this.level.addFreshEntity(trap);
                    this.caughtEntities.clear();
                }
            }
            case "stuffed_animals" -> {
                this.getNavigation().stop();
                if (anim.canAttack())
                    this.throwPlushies();
            }
            case "dark_beam" -> {
                this.getNavigation().stop();
                if (anim.canAttack() && !EntityUtils.sealed(this))
                    this.darkBeam();
            }
            case "furniture" -> {
                this.getNavigation().stop();
                if (anim.canAttack())
                    this.summonFurnitures();
            }
        }
    }

    @Override
    public AABB calculateAttackAABB(AnimatedAction anim, LivingEntity target) {
        if (anim.getID().equals(spin.getID())) {
            return this.getBoundingBox().inflate(1.6, 0, 1.6);
        }
        if (anim.getID().equals(chest_attack.getID())) {
            return this.getBoundingBox().inflate(1.2, 0, 1.2);
        }
        return super.calculateAttackAABB(anim, target);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2)
                this.getAnimationHandler().setAnimation(card_attack);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(spin);
            else
                this.getAnimationHandler().setAnimation(melee);
        }
    }

    @Override
    public float attackChance(AnimationType type) {
        return 1;
    }

    public void setAiVarHelper(double[] aiVarHelper) {
        this.aiVarHelper = aiVarHelper;
    }

    public void cardAttack() {
        if (!this.level.isClientSide) {
            for (Vector3f vec : RayTraceUtils.rotatedVecs(this.getLookAngle(), new Vec3(0, 1, 0), -80, 80, 10)) {
                EntityCards cards = new EntityCards(this.level, this, this.random.nextInt(8));
                cards.shoot(vec.x(), vec.y(), vec.z(), 1.5f, 0);
                this.level.addFreshEntity(cards);
            }
        }
    }

    public void throwPlushies() {
        if (!this.level.isClientSide) {
            int amount = this.random.nextInt(8) + 12;
            for (int i = 0; i < amount; ++i) {
                EntityFurniture furniture = new EntityFurniture(this.level, this, this.random.nextBoolean() ? EntityFurniture.Type.WOOLYPLUSH : EntityFurniture.Type.CHIPSQUEEKPLUSH);
                LivingEntity target = this.getTarget();
                if (target != null) {
                    Vec3 dir = target.position().subtract(this.position()).scale(0.45 + this.random.nextDouble() * 0.2);
                    furniture.shootAtPosition(this.getX() + dir.x(), target.getY() + 12, this.getZ() + dir.z(), 0.95f + this.random.nextFloat() * 0.2f, 9);
                } else
                    furniture.shoot(this, this.yHeadRot, this.getXRot(), -55, 0.95f + this.random.nextFloat() * 0.2f, 9);
                this.level.addFreshEntity(furniture);
            }
        }
    }

    public void darkBeam() {
        if (!this.level.isClientSide) {
            EntityDarkBeam beam = new EntityDarkBeam(this.level, this);
            LivingEntity target = this.getTarget();
            if (target != null)
                beam.setRotationTo(target.getX(), target.getY() + target.getBbHeight() * 0.5, target.getZ(), 0);
            this.level.addFreshEntity(beam);
        }
    }

    public void summonFurnitures() {
        if (!this.level.isClientSide) {
            int amount = this.random.nextInt(7) + 4;
            for (int i = 0; i < amount; ++i) {
                EntityFurniture.Type randType = EntityFurniture.Type.values()[this.random.nextInt(EntityFurniture.Type.values().length)];
                EntityFurniture furniture = new EntityFurniture(this.level, this, randType);
                furniture.setNoGravity(true);
                double xRand = this.getX() + (this.random.nextDouble() - 0.5) * 13;
                double yRand = this.getY() + (this.random.nextDouble()) * 2;
                double zRand = this.getZ() + (this.random.nextDouble() - 0.5) * 13;
                furniture.setPos(xRand, yRand, zRand);
                LivingEntity target = this.getTarget();
                if (target != null)
                    furniture.shootAtPosition(target.getX(), target.getY() + target.getBbHeight() * 0.5, target.getZ(), 0.1f, 1.2f);
                else
                    furniture.shoot(this, this.yHeadRot, this.getXRot(), 0, 0.09f, 1.2f);
                this.level.addFreshEntity(furniture);
            }
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
        this.caughtEntities.forEach(e -> {
            if (e.isAlive()) {
                if (e instanceof ServerPlayer player)
                    player.moveTo(this.getX(), this.getY() + this.getBbHeight() + 0.2, this.getZ());
                else
                    e.setPos(this.getX(), this.getY() + this.getBbHeight() + 0.2, this.getZ());
            }
        });
    }

    private void catchEntity(LivingEntity entity) {
        this.caughtEntities.add(entity);
        this.entityData.set(CAUGHT, true);
    }

    @Override
    public void push(Entity entityIn) {
        if (this.getAnimationHandler().isCurrentAnim(spin.getID(), chest_attack.getID()))
            return;
        super.push(entityIn);
    }

    @Override
    public AnimationHandler<EntityMarionetta> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.85D;
    }


}
