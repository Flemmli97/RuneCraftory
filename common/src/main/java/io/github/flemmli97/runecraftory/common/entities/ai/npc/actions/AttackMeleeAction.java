package io.github.flemmli97.runecraftory.common.entities.ai.npc.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.action.AttackActions;
import io.github.flemmli97.runecraftory.api.enums.EnumWeaponType;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.NPCAttackGoal;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModNPCActions;
import io.github.flemmli97.runecraftory.common.utils.CodecHelper;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.InteractionHand;
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
        int cooldown = 15;
        ItemStack hand = npc.getMainHandItem();
        if (hand.getItem() instanceof IItemUsable usabe && usabe.getWeaponType() != EnumWeaponType.FARM) {
            cooldown = 10;
        }
        return cooldown + this.cooldown.getInt(NPCAction.createLootContext(npc));
    }

    @Override
    public AnimatedAction getAction(EntityNPCBase npc) {
        ItemStack hand = npc.getMainHandItem();
        if (hand.getItem() instanceof IItemUsable usabe) {
            return switch (usabe.getWeaponType()) {
                case FARM -> null;
                case SHORTSWORD -> AttackActions.SHORT_SWORD.anim.apply(npc, npc.getRandom().nextInt(2));
                case LONGSWORD -> AttackActions.LONG_SWORD.anim.apply(npc, npc.getRandom().nextInt(2));
                case SPEAR -> AttackActions.SPEAR.anim.apply(npc, npc.getRandom().nextInt(2));
                case HAXE -> AttackActions.HAMMER_AXE.anim.apply(npc, npc.getRandom().nextInt(2));
                case DUAL -> AttackActions.DUAL_BLADES.anim.apply(npc, npc.getRandom().nextInt(2));
                case GLOVE -> AttackActions.GLOVES.anim.apply(npc, npc.getRandom().nextInt(2));
                case STAFF -> AttackActions.STAFF.anim.apply(npc, 0);
            };
        }
        return null;
    }

    @Override
    public boolean doAction(EntityNPCBase npc, NPCAttackGoal<?> goal, AnimatedAction action) {
        goal.moveToEntityNearer(goal.getAttackTarget(), 1.1f);
        npc.getLookControl().setLookAt(goal.getAttackTarget(), 30, 30);
        double minDist = npc.getAttributeValue(ModAttributes.ATTACK_RANGE.get()) - 0.3 + goal.getAttackTarget().getBbWidth() * 0.5;
        if (goal.getDist() <= minDist * minDist) {
            if (action == null) {
                npc.swing(InteractionHand.MAIN_HAND);
                npc.npcAttack(npc::doHurtTarget);
            }
            return true;
        }
        return false;
    }
}
