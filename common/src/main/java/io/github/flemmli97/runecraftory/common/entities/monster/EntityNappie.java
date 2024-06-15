package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.HealingPredicateEntity;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

public class EntityNappie extends EntityPommePomme implements HealingPredicateEntity {

    public static final AnimatedAction HEAL = new AnimatedAction(0.72, 0.32, "cast");
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{HEAL, KICK, CHARGE_ATTACK, INTERACT, SLEEP};

    private final AnimationHandler<EntityNappie> animationHandler = new AnimationHandler<>(this, ANIMS);
    private final Predicate<LivingEntity> healingPredicate = e -> {
        if (this.getOwnerUUID() == null) {
            if (e instanceof OwnableEntity ownable && ownable.getOwnerUUID() != null)
                return false;
            return e instanceof Enemy;
        }
        if (e instanceof OwnableEntity ownable && this.getOwnerUUID().equals(ownable.getOwnerUUID()))
            return true;
        return this.getOwnerUUID().equals(e.getUUID());
    };

    public EntityNappie(EntityType<? extends EntityNappie> type, Level world) {
        super(type, world);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(HEAL)) {
            this.getNavigation().stop();
            if (anim.canAttack()) {
                ModSpells.CURE_ALL.get().use(this);
            }
        } else {
            super.handleAttack(anim);
        }
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (anim.is(HEAL))
            return this.getRandom().nextFloat() < 0.25f && type == AnimationType.MELEE;
        return super.isAnimOfType(anim, type);
    }

    @Override
    public AnimationHandler<EntityNappie> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        if (anim.is(HEAL))
            return 10;
        return super.maxAttackRange(anim);
    }

    @Override
    public Predicate<LivingEntity> healeableEntities() {
        return this.healingPredicate;
    }
}
