package io.github.flemmli97.runecraftory.common.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModLootRegistries;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

import java.util.List;

public class LootingAndLuckLootFunction extends LootItemConditionalFunction {

    public static final MapCodec<LootingAndLuckLootFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> commonFields(instance)
                    .and(instance.group(NumberProviders.CODEC.fieldOf("base").forGetter(d -> d.baseChance),
                            NumberProviders.CODEC.fieldOf("looting_bonus").forGetter(d -> d.lootingBonus),
                            NumberProviders.CODEC.fieldOf("luck_bonus").forGetter(d -> d.luckBonus),
                            Codec.INT.fieldOf("limit").forGetter(d -> d.limit))
                    ).apply(instance, LootingAndLuckLootFunction::new)
    );

    private final NumberProvider baseChance;
    private final NumberProvider lootingBonus;
    private final NumberProvider luckBonus;
    private final int limit;

    private LootingAndLuckLootFunction(List<LootItemCondition> conditions, NumberProvider baseChance, NumberProvider luckBonus, NumberProvider lootingBonus, int limit) {
        super(conditions);
        this.baseChance = baseChance;
        this.luckBonus = luckBonus;
        this.lootingBonus = lootingBonus;
        this.limit = limit;
    }

    @Override
    public LootItemFunctionType getType() {
        return ModLootRegistries.LUCK_AND_LOOTING.get();
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext ctx) {
        Entity entity = ctx.getParamOrNull(LootContextParams.ATTACKING_ENTITY);
        float luck = ctx.getLuck();
        int looting = 0;
        List<LivingEntity> contributing = getContributingEntities(ctx);
        if (entity instanceof LivingEntity) {
            looting = Platform.INSTANCE.getLootingFromCtx(ctx);

        }
        for (LivingEntity other : contributing) {
            looting += Platform.INSTANCE.getLootingFromEntity(ctx.getParamOrNull(LootContextParams.THIS_ENTITY), other, ctx.getParamOrNull(LootContextParams.DAMAGE_SOURCE));
            if (other.getAttributes().hasAttribute(Attributes.LUCK))
                luck += (float) other.getAttributeValue(Attributes.LUCK);
        }
        float chance = (this.baseChance.getFloat(ctx) + this.luckBonus.getFloat(ctx) * luck) * (1 + this.lootingBonus.getFloat(ctx) * looting);
        if (chance >= 1) {
            int uniform = (int) chance * 2;
            float left = chance - uniform;
            int amount = ctx.getRandom().nextInt(uniform + 1);
            if (ctx.getRandom().nextFloat() < left) {
                amount++;
            }
            if (this.limit > 0)
                amount = Math.min(this.limit, amount);
            stack.setCount(amount);
            return stack;
        } else if (ctx.getRandom().nextFloat() < chance) {
            return stack;
        }
        return ItemStack.EMPTY;
    }

    public static List<LivingEntity> getContributingEntities(LootContext ctx) {
        Entity entity = ctx.getParamOrNull(LootContextParams.ATTACKING_ENTITY);
        if (entity instanceof Player player) {
            PlayerData data = Platform.INSTANCE.getPlayerData(player);
            return entity.level().getEntities(EntityTypeTest.forClass(LivingEntity.class), entity.getBoundingBox().inflate(64), data.party::isPartyMember);
        } else if (entity instanceof BaseMonster monster) {
            if (monster.getOwner() != null && Platform.INSTANCE.getPlayerData(monster.getOwner()).party.isPartyMember(monster))
                return List.of(monster);
        } else if (entity instanceof EntityNPCBase npc) {
            if (npc.followEntity() != null && Platform.INSTANCE.getPlayerData(npc.followEntity()).party.isPartyMember(npc))
                return List.of(npc);
        }
        return List.of();
    }

    public static class Builder extends LootItemConditionalFunction.Builder<LootingAndLuckLootFunction.Builder> {

        private final NumberProvider baseChance;
        private NumberProvider lootingBonus = ConstantValue.exactly(0);
        private NumberProvider luckBonus = ConstantValue.exactly(0);
        private int limit = 0;

        public Builder(NumberProvider baseChance) {
            this.baseChance = baseChance;
        }

        @Override
        protected LootingAndLuckLootFunction.Builder getThis() {
            return this;
        }

        public LootingAndLuckLootFunction.Builder withLuckBonus(NumberProvider luckBonus) {
            this.luckBonus = luckBonus;
            return this;
        }

        public LootingAndLuckLootFunction.Builder withLootingBonus(NumberProvider lootingBonus) {
            this.lootingBonus = lootingBonus;
            return this;
        }

        public LootingAndLuckLootFunction.Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        @Override
        public LootItemFunction build() {
            return new LootingAndLuckLootFunction(this.getConditions(), this.baseChance, this.luckBonus, this.lootingBonus, this.limit);
        }
    }
}
