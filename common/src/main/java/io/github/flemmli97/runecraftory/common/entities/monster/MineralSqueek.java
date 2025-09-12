package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class MineralSqueek extends Chipsqueek {

    public static final ResourceKey<LootTable> MINERAL_SQUEEK_HURT = ResourceKey.create(Registries.LOOT_TABLE, RuneCraftory.modRes("entities/mineral_squeek_hurt"));

    public MineralSqueek(EntityType<? extends Chipsqueek> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof Player player) {
            ItemStack main = player.getMainHandItem();
            if (!this.canBeDamagedBy(main))
                return false;
        }
        return super.hurt(source, amount);
    }

    @SuppressWarnings("deprecation")
    protected boolean canBeDamagedBy(ItemStack stack) {
        if (stack.isEmpty())
            return false;
        double[] val = {0};
        stack.forEachModifier(EquipmentSlot.MAINHAND, (att, mod) -> {
            if (att.is(Attributes.ATTACK_DAMAGE) && mod.operation() == AttributeModifier.Operation.ADD_VALUE) {
                val[0] += mod.amount();
            }
        });
        return val[0] > 1;
    }

    @Override
    protected void actuallyHurt(DamageSource source, float damageAmount) {
        damageAmount = Math.max(1, damageAmount);
        float health = this.getHealth();
        super.actuallyHurt(source, damageAmount);
        if (this.level() instanceof ServerLevel serverLevel && health - this.getHealth() >= 1) {
            LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(MINERAL_SQUEEK_HURT);
            LootParams.Builder builder = new LootParams.Builder(serverLevel)
                    .withParameter(LootContextParams.THIS_ENTITY, this)
                    .withParameter(LootContextParams.ORIGIN, this.position())
                    .withParameter(LootContextParams.DAMAGE_SOURCE, source)
                    .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, source.getEntity())
                    .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, source.getDirectEntity());
            if (source.getEntity() instanceof Player player) {
                builder = builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player).withLuck(player.getLuck());
            }
            LootParams lootParams = builder.create(LootContextParamSets.ENTITY);
            lootTable.getRandomItems(lootParams, this::spawnAtLocation);
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
