package io.github.flemmli97.runecraftory.common.entities.ai.npc.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.enums.EnumWeaponType;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.NPCAttackGoal;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModAttackActions;
import io.github.flemmli97.runecraftory.common.registry.ModNPCActions;
import io.github.flemmli97.runecraftory.common.utils.CodecHelper;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.Optional;
import java.util.function.Supplier;

public class AttackMeleeAction implements NPCAction {

    public static final Codec<AttackMeleeAction> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(CodecHelper.NUMER_PROVIDER_CODEC.fieldOf("walkTime").forGetter(d -> d.walkTime),
                    NPCAction.optionalCooldown(d -> d.cooldown)
            ).apply(instance, AttackMeleeAction::new));

    private final NumberProvider walkTime;
    private final NumberProvider cooldown;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private AttackMeleeAction(NumberProvider walkTime, Optional<NumberProvider> cooldown) {
        this(walkTime, cooldown.orElse(NPCAction.CONST_ZERO));
    }

    public AttackMeleeAction(NumberProvider walkTime) {
        this(walkTime, NPCAction.CONST_ZERO);
    }

    public AttackMeleeAction(NumberProvider walkTime, NumberProvider cooldown) {
        this.walkTime = walkTime;
        this.cooldown = cooldown;
    }

    @Override
    public Supplier<NPCActionCodec> codec() {
        return ModNPCActions.MELEE_ATTACK;
    }

    @Override
    public int getDuration(EntityNPCBase npc) {
        return this.walkTime.getInt(NPCAction.createLootContext(npc));
    }

    @Override
    public int getCooldown(EntityNPCBase npc) {
        int cooldown = 20;
        ItemStack hand = npc.getMainHandItem();
        if (hand.getItem() instanceof IItemUsable usabe && usabe.getWeaponType() != EnumWeaponType.FARM) {
            cooldown = 12;
        }
        return cooldown + this.cooldown.getInt(NPCAction.createLootContext(npc));
    }

    @Override
    public AnimatedAction getAction(EntityNPCBase npc) {
        ItemStack hand = npc.getMainHandItem();
        if (hand.getItem() instanceof IItemUsable usabe) {
            return switch (usabe.getWeaponType()) {
                case FARM -> null;
                case SHORTSWORD -> ModAttackActions.SHORT_SWORD.get().getAnimation(npc, npc.getRandom().nextInt(2));
                case LONGSWORD -> ModAttackActions.LONG_SWORD.get().getAnimation(npc, npc.getRandom().nextInt(2));
                case SPEAR -> ModAttackActions.SPEAR.get().getAnimation(npc, npc.getRandom().nextInt(2));
                case HAXE -> ModAttackActions.HAMMER_AXE.get().getAnimation(npc, npc.getRandom().nextInt(2));
                case DUAL -> ModAttackActions.DUAL_BLADES.get().getAnimation(npc, npc.getRandom().nextInt(2));
                case GLOVE -> ModAttackActions.GLOVES.get().getAnimation(npc, npc.getRandom().nextInt(2));
                case STAFF -> ModAttackActions.STAFF.get().getAnimation(npc, 0);
            };
        }
        return null;
    }

    @Override
    public boolean doAction(EntityNPCBase npc, NPCAttackGoal<?> goal, AnimatedAction action) {
        goal.moveToEntityNearer(goal.getAttackTarget(), 1.1f);
        npc.getLookControl().setLookAt(goal.getAttackTarget(), 30, 30);
        double minDist = npc.getMeleeAttackRangeSqr(goal.getAttackTarget());
        if (goal.getDistSqr() <= minDist) {
            if (action == null) {
                npc.swing(InteractionHand.MAIN_HAND);
                npc.npcAttack(npc::doHurtTarget);
            }
            return true;
        }
        return false;
    }
}
