package io.github.flemmli97.runecraftory.common.entities.ai.npc.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.NPCAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.NPCAttackGoal;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModAttackActions;
import io.github.flemmli97.runecraftory.common.registry.ModNPCActions;
import io.github.flemmli97.runecraftory.common.utils.JsonCodecHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.Optional;
import java.util.function.Supplier;

public class AttackMeleeAction implements NPCAction {

    public static final Codec<AttackMeleeAction> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(JsonCodecHelper.NUMER_PROVIDER_CODEC.fieldOf("walkTime").forGetter(d -> d.walkTime),
                    NPCAction.optionalCooldown(d -> d.cooldown)
            ).apply(instance, AttackMeleeAction::new));

    private final NumberProvider walkTime;
    private final NumberProvider cooldown;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private AttackMeleeAction(NumberProvider walkTime, Optional<NumberProvider> cooldown) {
        this(walkTime, cooldown.orElse(NPCAction.CONST_SEC));
    }

    public AttackMeleeAction(NumberProvider walkTime) {
        this(walkTime, NPCAction.CONST_SEC);
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
        return this.cooldown.getInt(NPCAction.createLootContext(npc));
    }

    @Override
    public AttackAction getAction(EntityNPCBase npc) {
        ItemStack hand = npc.getMainHandItem();
        if (hand.getItem() instanceof IItemUsable usabe) {
            return switch (usabe.getWeaponType()) {
                case FARM -> null;
                case SHORTSWORD -> ModAttackActions.SHORT_SWORD.get();
                case LONGSWORD -> ModAttackActions.LONG_SWORD.get();
                case SPEAR -> ModAttackActions.SPEAR.get();
                case HAXE -> ModAttackActions.HAMMER_AXE.get();
                case DUAL -> ModAttackActions.DUAL_BLADES.get();
                case GLOVE -> ModAttackActions.GLOVES.get();
                case STAFF -> ModAttackActions.STAFF.get();
            };
        }
        return null;
    }

    @Override
    public boolean doAction(EntityNPCBase npc, NPCAttackGoal<?> goal, AttackAction action) {
        goal.moveToEntityNearer(goal.getAttackTarget(), 1.1f);
        npc.getLookControl().setLookAt(goal.getAttackTarget(), 30, 30);
        double minDist = npc.getMeleeAttackRangeSqr(goal.getAttackTarget());
        if (goal.getDistSqr() <= minDist) {
            if (action == null) {
                npc.swing(InteractionHand.MAIN_HAND);
                npc.npcAttack(npc::doHurtTarget);
            }
            npc.weaponHandler.setChainCount(npc.getRandom().nextInt(2));
            return true;
        }
        return false;
    }
}
