package io.github.flemmli97.runecraftory.common.entities.ai.npc.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.NPCAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.NPCAttackGoal;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityThrownItem;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModNPCActions;
import io.github.flemmli97.runecraftory.common.utils.JsonCodecHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class FoodThrowAction implements NPCAction {

    public static final Codec<FoodThrowAction> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(ItemStack.CODEC.listOf().fieldOf("items").forGetter(d -> d.items),
                    JsonCodecHelper.NUMER_PROVIDER_CODEC.fieldOf("walkTime").forGetter(d -> d.walkTime),
                    NPCAction.optionalCooldown(d -> d.cooldown)
            ).apply(instance, FoodThrowAction::new));

    private final List<ItemStack> items;
    private final NumberProvider walkTime;
    private final NumberProvider cooldown;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private FoodThrowAction(List<ItemStack> items, NumberProvider walkTime, Optional<NumberProvider> cooldown) {
        this(items, walkTime, cooldown.orElse(NPCAction.CONST_ZERO));
    }

    public FoodThrowAction(List<ItemStack> items, NumberProvider walkTime) {
        this(items, walkTime, NPCAction.CONST_ZERO);
    }

    public FoodThrowAction(List<ItemStack> items, NumberProvider walkTime, NumberProvider cooldown) {
        this.items = items;
        this.walkTime = walkTime;
        this.cooldown = cooldown;
    }

    @Override
    public Supplier<NPCActionCodec> codec() {
        return ModNPCActions.FOOD_THROW_ACTION;
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
        return null;
    }

    @Override
    public boolean doAction(EntityNPCBase npc, NPCAttackGoal<?> goal, AttackAction action) {
        if (npc.followEntity() == null || this.items.isEmpty())
            return true;
        goal.moveToEntity(npc.followEntity(), 1, 2);
        npc.getLookControl().setLookAt(npc.followEntity(), 30, 30);
        if (npc.distanceToSqr(npc.followEntity()) <= 30) {
            npc.swing(InteractionHand.MAIN_HAND);
            ItemStack stack = this.items.get(npc.getRandom().nextInt(this.items.size())).copy();
            EntityThrownItem entity = new EntityThrownItem(npc.level, npc);
            entity.setItem(stack);
            entity.setActAsFood(true);
            entity.shootAtEntity(npc.followEntity(), 0.6f, 0);
            npc.level.addFreshEntity(entity);
            npc.getNavigation().stop();
            return true;
        }
        return false;
    }
}
