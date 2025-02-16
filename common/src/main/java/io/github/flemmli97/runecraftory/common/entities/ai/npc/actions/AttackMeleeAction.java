package io.github.flemmli97.runecraftory.common.entities.ai.npc.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.NPCAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.NPCAttackGoal;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModNPCActions;
import io.github.flemmli97.runecraftory.common.utils.CodecHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.Optional;
import java.util.function.Supplier;

public class AttackMeleeAction implements NPCAction {

    public static final Codec<AttackMeleeAction> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(CodecHelper.NUMER_PROVIDER_CODEC.fieldOf("walk_time").forGetter(d -> d.walkTime),
                    NPCAction.optionalNumCooldown(d -> d.cooldown),
                    Codec.FLOAT.fieldOf("speed").forGetter(d -> d.speed)
            ).apply(instance, AttackMeleeAction::new));

    private final NumberProvider walkTime;
    private final NumberProvider cooldown;
    private final float speed;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private AttackMeleeAction(NumberProvider walkTime, Optional<NumberProvider> cooldown, float speed) {
        this(walkTime, cooldown.orElse(NPCAction.CONST_SEC), speed);
    }

    public AttackMeleeAction(NumberProvider walkTime) {
        this(walkTime, NPCAction.CONST_SEC, 1.2f);
    }

    public AttackMeleeAction(NumberProvider walkTime, float speed) {
        this(walkTime, NPCAction.CONST_SEC, speed);
    }

    public AttackMeleeAction(NumberProvider walkTime, NumberProvider cooldown, float speed) {
        this.walkTime = walkTime;
        this.cooldown = cooldown;
        this.speed = speed;
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
    public NPCAttackAction getAction(EntityNPCBase npc) {
        ItemStack hand = npc.getMainHandItem();
        if (hand.getItem() instanceof IItemUsable usabe) {
            AttackAction action = usabe.getWeaponType().getAction();
            if (action != null) {
                int amount = npc.getRandom().nextInt(action.combos().size()) + 1;
                return new NPCAttackAction(action, amount);
            }
        }
        return null;
    }

    @Override
    public boolean doAction(EntityNPCBase npc, NPCAttackGoal<?> goal, NPCAttackAction action) {
        goal.moveToEntityNearer(goal.getAttackTarget(), this.speed);
        npc.getLookControl().setLookAt(goal.getAttackTarget(), 30, 30);
        double minDist = npc.getMeleeAttackRangeSqr(goal.getAttackTarget());
        if (goal.getDistSqr() <= minDist) {
            if (action == null) {
                npc.swing(InteractionHand.MAIN_HAND);
                npc.npcAttack(npc::doHurtTarget);
            }
            npc.weaponHandler.setComboCount(npc.getRandom().nextInt(2));
            return true;
        }
        return false;
    }
}
