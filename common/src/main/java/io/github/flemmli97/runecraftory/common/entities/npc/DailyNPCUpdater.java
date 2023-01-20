package io.github.flemmli97.runecraftory.common.entities.npc;

import io.github.flemmli97.runecraftory.common.entities.DailyEntityUpdater;
import net.minecraft.nbt.CompoundTag;

public class DailyNPCUpdater extends DailyEntityUpdater<EntityNPCBase> {

    private int bread;

    public DailyNPCUpdater(EntityNPCBase entity) {
        super(entity);
        this.bread = entity.getRandom().nextInt(4) + 1;
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();
        this.bread = this.entity.getRandom().nextInt(4) + 1;
    }

    public int getBreadToBuy() {
        return this.bread;
    }

    public void onBuyBread() {
        this.bread--;
    }

    @Override
    public CompoundTag save() {
        CompoundTag tag = super.save();
        tag.putInt("Bread", this.bread);
        return tag;
    }

    @Override
    public void read(CompoundTag compound) {
        super.read(compound);
        this.bread = compound.getInt("Bread");
    }
}
