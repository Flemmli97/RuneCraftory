package io.github.flemmli97.runecraftory.common.entities.ai.behaviour;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.common.entities.misc.ThrownItemEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryMemoryTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class ThrowItemAt<E extends LivingEntity> extends ExtendedBehaviour<E> {

    private static final MemoryTest MEMORIES = MemoryTest.builder(1)
            .hasMemories(RuneCraftoryMemoryTypes.ITEM_THROW_TARGET.get());

    private final List<ItemStack> items;

    public ThrowItemAt(List<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected void start(E entity) {
        entity.swing(InteractionHand.MAIN_HAND);
        ItemStack stack = this.items.get(entity.getRandom().nextInt(this.items.size())).copy();
        ThrownItemEntity thrownItem = new ThrownItemEntity(entity.level(), entity);
        thrownItem.setItem(stack);
        thrownItem.setActAsFood(true);
        Entity target = BrainUtils.getMemory(entity, RuneCraftoryMemoryTypes.ITEM_THROW_TARGET.get());
        thrownItem.shootAtEntity(target, 0.6f, 0);
        thrownItem.level().addFreshEntity(thrownItem);
    }
}
