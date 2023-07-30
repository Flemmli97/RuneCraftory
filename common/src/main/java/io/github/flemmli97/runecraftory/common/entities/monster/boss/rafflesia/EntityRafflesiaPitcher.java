package io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia;

import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class EntityRafflesiaPitcher extends EntityRafflesiaPart {

    private static final Vec3 OFFSET = new Vec3(-1, 0, 0.2);

    private final AnimationHandler<EntityRafflesiaPitcher> animationHandler = new AnimationHandler<>(this, new AnimatedAction[]{EntityRafflesiaPart.PITCHER_ACTION});

    public EntityRafflesiaPitcher(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    public EntityRafflesiaPitcher(Level level, EntityRafflesia parent) {
        super(ModEntities.RAFFLESIA_PITCHER.get(), level, parent);
    }

    public static void rafflesiaSpawning(EntityRafflesiaPart part) {
        if (part.level.isClientSide)
            return;
        List<Mob> nearby = part.level.getEntities(EntityTypeTest.forClass(Mob.class), part.getBoundingBox().inflate(16), m -> {
            if (!m.getType().is(ModTags.RAFFLESIA_SUMMONS))
                return false;
            if (m instanceof OwnableEntity ownableEntity) {
                Player player = null;
                if (part.getOwner() != null) {
                    if (part.getOwner().getOwnerUUID() == null)
                        return true;
                    player = part.getOwner().getOwner();
                }
                if (player != null)
                    return player.getUUID().equals(ownableEntity.getOwnerUUID());
            }
            return true;
        });
        if (nearby.size() < 5) {
            int rand = 1 + (part.getRandom().nextBoolean() ? 1 : 0);
            BlockPos summonPos = part.blockPosition();
            for (int i = 0; i < rand; i++) {
                BlockPos pos = summonPos.offset(part.getRandom().nextInt(8) - 4, part.getRandom().nextInt(2), part.getRandom().nextInt(8) - 4);
                Optional<EntityType<?>> opt = Registry.ENTITY_TYPE.getTag(ModTags.RAFFLESIA_SUMMONS).flatMap(named -> named.getRandomElement(part.getRandom()).map(Holder::value));
                opt.ifPresent(type -> {
                    Entity e = type.create((ServerLevel) part.level, null, null, null, pos, MobSpawnType.MOB_SUMMONED, true, true);
                    if (e != null) {
                        if (e instanceof Mob mob)
                            mob.setTarget(part.getTarget());
                        part.level.addFreshEntity(e);
                    }
                });
            }
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.level.isClientSide) {
            this.getAnimationHandler().runIfNotNull(anim -> {
                if (anim.canAttack()) {
                    rafflesiaSpawning(this);
                }
            });
        }
    }

    @Override
    public Vec3 offset() {
        return OFFSET;
    }

    @Override
    public AnimatedAction attackAnim() {
        return EntityRafflesiaPart.PITCHER_ACTION;
    }

    @Override
    public AnimationHandler<?> getAnimationHandler() {
        return this.animationHandler;
    }
}