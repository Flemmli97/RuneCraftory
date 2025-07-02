package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.MoveToAttackTarget;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;

public class EntityGoblinGangster extends EntityGoblin {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder(EntityGoblin.BUILDER, SLEEP);
    public static final String DOUBLE_STAB = BUILDER.add("double_stab", AnimationsBuilder.definition(0.88).marker("attack", 0.4, 0.72));
    public static final String DOUBLE_THROW = BUILDER.add("double_throw", AnimationsBuilder.definition(0.96).marker("attack", 0.4, 0.76));
    public static final String INTERACT = BUILDER.add("interact", DOUBLE_THROW);
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private final AnimationHandler<EntityGoblinGangster> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityGoblinGangster(EntityType<? extends EntityGoblin> type, Level level) {
        super(type, level);
    }

    public void quickAttack(Entity target) {
        DynamicDamage.Builder source = new DynamicDamage.Builder(this).noKnockback().hurtResistant(1);
        CombatUtils.mobAttack(this, target, source);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.CUTLASS_PROP.get()));
        this.setDropChance(EquipmentSlot.MAINHAND, 0);
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ModItems.CUTLASS_PROP.get()));
        this.setDropChance(EquipmentSlot.OFFHAND, 0);
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<BaseMonster>create()
                .start(DOUBLE_STAB).play(MonsterBehaviourUtils.requireInRangePlay())
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(new MoveToAttackTarget<>())
                .end(6)
                .start(DOUBLE_THROW).play(MonsterBehaviourUtils.cooldownedPlay())
                .end(3)
                .build();
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 1.5;
        double length = this.getBbWidth() * 2.15;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(DOUBLE_THROW)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                if (this.getTarget() != null && this.getSensing().hasLineOfSight(this.getTarget())) {
                    ModSpells.THROW_HAND_ITEM.get().use(this);
                }
                this.stopUsingItem();
            }
        } else if (anim.is(DOUBLE_STAB)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                this.mobAttack(anim, this.getTarget(), this::quickAttack);
            }
        }
    }

    @Override
    public AnimationHandler<EntityGoblinGangster> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 1 ? ModSpells.THROW_HAND_ITEM.get() : null))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(DOUBLE_THROW);
            else
                this.getAnimationHandler().setAnimation(DOUBLE_STAB);
        }
    }

    @Override
    public String getInteractAnimation() {
        return INTERACT;
    }
}
