package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class EntityMineralSqueek extends EntityChipsqueek {

    public static final ResourceKey<LootTable> MINERAL_SQUEEK_HURT = ResourceKey.create(Registries.LOOT_TABLE, RuneCraftory.modRes("entities/mineral_squeek_hurt"));

    public EntityMineralSqueek(EntityType<? extends EntityChipsqueek> type, Level world) {
        super(type, world);
    }

    @Override
    protected void actuallyHurt(DamageSource source, float damageAmount) {
        damageAmount = Math.max(1, damageAmount);
        float health = this.getHealth();
        super.actuallyHurt(source, damageAmount);
        if (health - this.getHealth() >= 1) {
            LootTable lootTable = this.level().getServer().getLootTables().get(MINERAL_SQUEEK_HURT);
            LootContext.Builder builder = this.createLootContext(source.getEntity() instanceof Player, source);
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
