package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class EntityMineralSqueek extends EntityChipsqueek {

    public static final ResourceLocation MINERAL_SQUEEK_HURT = new ResourceLocation(RuneCraftory.MODID, "entities/mineral_squeek_hurt");

    public EntityMineralSqueek(EntityType<? extends EntityChipsqueek> type, Level world) {
        super(type, world);
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        damageAmount = Math.max(1, damageAmount);
        float health = this.getHealth();
        super.actuallyHurt(damageSrc, damageAmount);
        if (health - this.getHealth() >= 1) {
            LootTable lootTable = this.level.getServer().getLootTables().get(MINERAL_SQUEEK_HURT);
            LootContext.Builder builder = this.createLootContext(damageSrc.getEntity() instanceof Player, damageSrc);
            lootTable.getRandomItems(builder.create(LootContextParamSets.ENTITY), this::spawnAtLocation);
        }
    }

    @Override
    protected float getDamageAfterArmorAbsorb(DamageSource damageSource, float damageAmount) {
        return damageAmount;
    }

    @Override
    protected float getDamageAfterMagicAbsorb(DamageSource damageSource, float damageAmount) {
        return damageAmount;
    }
}
