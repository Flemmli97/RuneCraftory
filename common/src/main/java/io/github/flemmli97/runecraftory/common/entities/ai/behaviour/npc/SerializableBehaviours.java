package io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MoveToWalkTillClose;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.SetWalkTargetWithinDist;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.ThrowItemAt;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.DummyBehaviour;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.CustomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SerializableBehaviours {

    public static final SerializableBehaviour<Unit> IDLE = new SerializableBehaviour<>(MapCodec.unit(Unit.INSTANCE),
            data -> List.of(new Idle<NPCEntity>().runFor(e -> 1)));
    public static final SerializableBehaviour<WalkToData> WALK_TO = new SerializableBehaviour<>(WalkToData.CODEC,
            data -> List.of(new SetWalkTargetToAttackTarget<NPCEntity>()
                            .speedMod((e, t) -> data.speed()).closeEnoughDist((e, t) -> data.closeEnough()),
                    DummyBehaviour.opt(new MoveToWalkTillClose<>())));
    public static final SerializableBehaviour<WalkAwayData> WALK_AWAY = new SerializableBehaviour<>(WalkAwayData.CODEC,
            data -> List.of(new SetWalkTargetAwayFromTarget<NPCEntity>()
                            .speedMod((e, t) -> data.speed()).minDist(data.minDistance()).radius(data.radius()),
                    DummyBehaviour.opt(new MoveToWalkTillClose<>())));
    public static final SerializableBehaviour<KeepDistanceData> KEEP_DISTANCE = new SerializableBehaviour<>(KeepDistanceData.CODEC,
            data -> List.of(new SetWalkTargetWithinDist<NPCEntity>()
                            .speedMod((e, t) -> data.speed())
                            .min(data.min()).max(data.max()),
                    DummyBehaviour.opt(new MoveToWalkTillClose<>())));
    public static final SerializableBehaviour<RandomWalkData> RANDOM_WALK = new SerializableBehaviour<>(RandomWalkData.CODEC,
            data -> List.of(new SetRandomWalkTarget<NPCEntity>()
                            .speedModifier((e, t) -> data.speed()).setRadius(data.radius()),
                    DummyBehaviour.opt(new MoveToWalkTillClose<>())));
    public static final SerializableBehaviour<WalkToData> WALK_TO_FOLLOW = new SerializableBehaviour<>(WalkToData.CODEC,
            data -> List.of(new CustomBehaviour<>(e -> {
                        LivingEntity target = e.followEntity();
                        if (target != null) {
                            BrainUtils.setMemory(e, MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
                            BrainUtils.setMemory(e, MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker(target, false), data.speed(), data.closeEnough()));
                        }
                    }),
                    DummyBehaviour.opt(new MoveToWalkTillClose<>())));
    public static final SerializableBehaviour<Unit> LOOK_AT_FOLLOW = new SerializableBehaviour<>(MapCodec.unit(Unit.INSTANCE),
            data -> List.of(new CustomBehaviour<>(e -> {
                LivingEntity target = e.followEntity();
                if (target != null) {
                    BrainUtils.setForgettableMemory(e, MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true),
                            40 + e.getRandom().nextInt(20));
                }
            })));
    public static final SerializableBehaviour<Unit> ATTACK_WITH_WEAPON = new SerializableBehaviour<>(MapCodec.unit(Unit.INSTANCE),
            data -> List.of(new CustomBehaviour<>(e -> {
                LivingEntity target = BrainUtils.getTargetOfEntity(e);
                if (target != null) {
                    BrainUtils.setForgettableMemory(e, MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true),
                            40 + e.getRandom().nextInt(20));
                }
            }), new FirstApplicableBehaviour<>(
                    new SetWeaponBasedAttack<>(),
                    new CustomBehaviour<>(entity -> {
                        LivingEntity target = BrainUtils.getTargetOfEntity(entity);
                        if (target == null || !entity.getSensing().hasLineOfSight(target) || !entity.isWithinMeleeAttackRange(target))
                            return;
                        entity.swing(InteractionHand.MAIN_HAND);
                        entity.doHurtTarget(target);
                    })
            )));
    public static final SerializableBehaviour<SpellAttackData> ATTACK_WITH_SPELL = new SerializableBehaviour<>(SpellAttackData.CODEC,
            new SerializableBehaviour.BehaviourSequenceFactory<>() {
                @Override
                public List<ExtendedBehaviour<NPCEntity>> create(SpellAttackData data) {
                    return List.of(new CustomBehaviour<>(e -> {
                        LivingEntity target = BrainUtils.getTargetOfEntity(e);
                        if (target != null) {
                            BrainUtils.setForgettableMemory(e, MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true),
                                    40 + e.getRandom().nextInt(20));
                        }
                    }), new SetSpellAttack<>(data.spell(), entity -> data.amount().getInt(NPCAttackActions.createLootContext(entity))));
                }

                @Override
                public void addCondition(SpellAttackData data, Consumer<Predicate<NPCEntity>> predicate) {
                    if (!data.ignoreSeal())
                        predicate.accept(npc -> !EntityUtils.sealed(npc));
                }
            });
    public static final SerializableBehaviour<ItemThrowData> THROW_ITEM = new SerializableBehaviour<>(ItemThrowData.CODEC,
            data -> List.of(new CustomBehaviour<>(e -> {
                        LivingEntity target = data.throwAtTarget() ? BrainUtils.getTargetOfEntity(e) : e.followEntity();
                        if (target != null) {
                            BrainUtils.setForgettableMemory(e, MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true),
                                    40 + e.getRandom().nextInt(20));
                        }
                    }),
                    new ThrowItemAt<>(data.stacks())));

    public record WalkToData(float speed, int closeEnough) {

        public static final MapCodec<WalkToData> CODEC = RecordCodecBuilder.mapCodec(data ->
                data.group(Codec.FLOAT.fieldOf("walk_speed").forGetter(WalkToData::speed),
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("close_enough").forGetter(WalkToData::closeEnough)
                ).apply(data, WalkToData::new));
    }

    public record WalkAwayData(float speed, float minDistance, int radius) {

        public static final MapCodec<WalkAwayData> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(Codec.FLOAT.fieldOf("walk_speed").forGetter(WalkAwayData::speed),
                        Codec.FLOAT.fieldOf("min_distance").forGetter(WalkAwayData::minDistance),
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("radius").forGetter(WalkAwayData::radius)
                ).apply(inst, WalkAwayData::new));
    }

    public record KeepDistanceData(float speed, float min, float max) {

        public static final MapCodec<KeepDistanceData> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(Codec.FLOAT.fieldOf("walk_speed").forGetter(KeepDistanceData::speed),
                        Codec.FLOAT.fieldOf("min").forGetter(KeepDistanceData::min),
                        Codec.FLOAT.fieldOf("max").forGetter(KeepDistanceData::max)
                ).apply(inst, KeepDistanceData::new));
    }

    public record RandomWalkData(float speed, int radius) {

        public static final MapCodec<RandomWalkData> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(Codec.FLOAT.fieldOf("walk_speed").forGetter(RandomWalkData::speed),
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("radius").forGetter(RandomWalkData::radius)
                ).apply(inst, RandomWalkData::new));
    }

    public record SpellAttackData(Spell spell, boolean ignoreSeal, NumberProvider amount) {

        public static final MapCodec<SpellAttackData> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(RuneCraftorySpells.SPELLS.registry().byNameCodec().fieldOf("spell").forGetter(SpellAttackData::spell),
                        Codec.BOOL.fieldOf("ignore_seal").forGetter(SpellAttackData::ignoreSeal),
                        NumberProviders.CODEC.fieldOf("amount").forGetter(SpellAttackData::amount)
                ).apply(inst, SpellAttackData::new));

        public SpellAttackData(Spell spell) {
            this(spell, false, ConstantValue.exactly(1));
        }
    }

    public record ItemThrowData(List<ItemStack> stacks, boolean throwAtTarget) {

        public static final MapCodec<ItemThrowData> CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(CodecUtils.ITEM_STACK_CODEC.listOf().fieldOf("items").forGetter(ItemThrowData::stacks),
                        Codec.BOOL.fieldOf("throw_at_target").forGetter(ItemThrowData::throwAtTarget)
                ).apply(inst, ItemThrowData::new));
    }
}
