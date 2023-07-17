package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.boss.MarionettaAttackGoal;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityMarionettaTrap;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
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
import java.util.function.BiConsumer;

public class EntityMarionetta extends BossMonster {

    public static final AnimatedAction MELEE = new AnimatedAction(10, 5, "melee");
    public static final AnimatedAction SPIN = new AnimatedAction(31, 6, "spin");
    public static final AnimatedAction CARD_ATTACK = new AnimatedAction(13, 9, "card_attack");
    public static final AnimatedAction CHEST_ATTACK = new AnimatedAction(24, 6, "chest_attack");
    public static final AnimatedAction CHEST_THROW = new AnimatedAction(105, 7, "chest_throw");
    public static final AnimatedAction STUFFED_ANIMALS = new AnimatedAction(15, 9, "stuffed_animals");
    public static final AnimatedAction DARK_BEAM = new AnimatedAction(16, 6, "dark_beam");
    public static final AnimatedAction FURNITURE = new AnimatedAction(24, 8, "furniture");
    public static final AnimatedAction DEFEAT = AnimatedAction.builder(204, "defeat").marker(150).infinite().build();
    public static final AnimatedAction ANGRY = new AnimatedAction(28, 0, "angry");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(MELEE, "interact");

    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{MELEE, SPIN, CARD_ATTACK, CHEST_ATTACK, CHEST_THROW, STUFFED_ANIMALS, DARK_BEAM, FURNITURE, DEFEAT, ANGRY, INTERACT};
    private static final ImmutableMap<String, BiConsumer<AnimatedAction, EntityMarionetta>> ATTACK_HANDLER = createAnimationHandler(b -> {
        b.put(MELEE, (anim, entity) -> {
            LivingEntity target = entity.getTarget();
            if (target != null) {
                entity.getNavigation().moveTo(target, 1.0);
            }
            if (anim.canAttack()) {
                entity.mobAttack(anim, target, entity::doHurtTarget);
            }
        });
        b.put(SPIN, (anim, entity) -> {
            if (entity.aiVarHelper == null)
                return;
            entity.setDeltaMovement(new Vec3(entity.aiVarHelper[0], 0, entity.aiVarHelper[2]));
            if (anim.getTick() >= anim.getAttackTime()) {
                entity.mobAttack(anim, null, e -> CombatUtils.mobAttack(entity, e, new CustomDamage.Builder(entity).hurtResistant(8), CombatUtils.getAttributeValue(entity, Attributes.ATTACK_DAMAGE)));
            }
        });
        b.put(CARD_ATTACK, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack())
                ModSpells.CARDTHROW.get().use(entity);
        });
        b.put(CHEST_ATTACK, (anim, entity) -> {
            if (entity.aiVarHelper == null)
                return;
            entity.setDeltaMovement(new Vec3(entity.aiVarHelper[0], 0, entity.aiVarHelper[2]));
            if (anim.getTick() >= anim.getAttackTime()) {
                entity.mobAttack(anim, null, e -> {
                    if (!entity.caughtEntities.contains(e)) {
                        entity.catchEntity(e);
                    }
                });
            }
        });
        b.put(CHEST_THROW, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack()) {
                Vec3 throwVec = new Vec3(entity.getLookAngle().x(), 0, entity.getLookAngle().z())
                        .normalize().scale(1.2).add(0, 0.85, 0);
                EntityMarionettaTrap trap = new EntityMarionettaTrap(entity.level, entity);
                entity.caughtEntities.forEach(e -> {
                    e.addEffect(new MobEffectInstance(ModEffects.TRUE_INVIS.get(), 100, 1, true, false, false));
                    trap.addCaughtEntity(e);
                });
                trap.setDeltaMovement(throwVec);
                entity.level.addFreshEntity(trap);
                entity.caughtEntities.clear();
            }
        });
        b.put(STUFFED_ANIMALS, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack())
                ModSpells.PLUSHTHROW.get().use(entity);
        });
        b.put(DARK_BEAM, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack() && !EntityUtils.sealed(entity))
                ModSpells.DARKBEAM.get().use(entity);
        });
        b.put(FURNITURE, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack() && !EntityUtils.sealed(entity))
                ModSpells.FURNITURE.get().use(entity);
        });
    });

    private static final EntityDataAccessor<Boolean> CAUGHT = SynchedEntityData.defineId(EntityMarionetta.class, EntityDataSerializers.BOOLEAN);

    public final MarionettaAttackGoal<EntityMarionetta> attack = new MarionettaAttackGoal<>(this);
    private final List<LivingEntity> caughtEntities = new ArrayList<>();
    private final AnimationHandler<EntityMarionetta> animationHandler = new AnimationHandler<>(this, ANIMS)
            .setAnimationChangeFunc(anim -> {
                if (this.entityData.get(CAUGHT)) {
                    if (!this.level.isClientSide) {
                        this.entityData.set(CAUGHT, false);
                        this.getAnimationHandler().setAnimation(CHEST_THROW);
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
            this.getAnimationHandler().setAnimation(ANGRY);
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
        if (anim.getID().equals(CHEST_THROW.getID()) || anim.getID().equals(DEFEAT.getID()) || anim.getID().equals(ANGRY.getID()) || anim.getID().equals(INTERACT.getID()))
            return false;
        if (type == AnimationType.GENERICATTACK)
            return this.isEnraged() || (!anim.getID().equals(DARK_BEAM.getID()) && !anim.getID().equals(FURNITURE.getID()));
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
        return (!this.getAnimationHandler().hasAnimation() || !(this.getAnimationHandler().isCurrent(CHEST_THROW, ANGRY))) && super.hurt(source, amount);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.getAnimationHandler().isCurrent(ANGRY, DEFEAT);
    }

    @Override
    public void push(double x, double y, double z) {
        if (this.getAnimationHandler().isCurrent(ANGRY, DEFEAT))
            return;
        super.push(x, y, z);
    }

    @Override
    public AnimatedAction getDeathAnimation() {
        return DEFEAT;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        LivingEntity target = this.getTarget();
        if (target != null) {
            if (!anim.getID().equals(SPIN.getID()))
                this.lookAt(target, 180.0f, 50.0f);
        }
        BiConsumer<AnimatedAction, EntityMarionetta> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public AABB calculateAttackAABB(AnimatedAction anim, LivingEntity target) {
        if (anim.getID().equals(SPIN.getID())) {
            return this.getBoundingBox().inflate(1.6, 0, 1.6);
        }
        if (anim.getID().equals(CHEST_ATTACK.getID())) {
            return this.getBoundingBox().inflate(1.2, 0, 1.2);
        }
        return super.calculateAttackAABB(anim, target);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2)
                this.getAnimationHandler().setAnimation(CARD_ATTACK);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(SPIN);
            else
                this.getAnimationHandler().setAnimation(MELEE);
        }
    }

    @Override
    public float attackChance(AnimationType type) {
        return 1;
    }

    public void setAiVarHelper(double[] aiVarHelper) {
        this.aiVarHelper = aiVarHelper;
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
        if (this.getAnimationHandler().isCurrent(SPIN, CHEST_ATTACK))
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

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }
}
