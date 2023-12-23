package io.github.flemmli97.runecraftory.api;

import io.github.flemmli97.runecraftory.api.action.AttackAction;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.registry.ModAttackActions;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.platform.registry.CustomRegistryEntry;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public abstract class Spell extends CustomRegistryEntry<Spell> {

    public static boolean tryUseWithCost(LivingEntity entity, ItemStack stack, Spell spell) {
        return tryUseWithCost(entity, stack, spell, 1);
    }

    public static boolean tryUseWithCost(LivingEntity entity, ItemStack stack, Spell spell, float costMultiplier) {
        return tryUseWithCost(entity, stack, spell, costMultiplier, stack.getItem() instanceof ItemStaffBase);
    }

    public static boolean tryUseWithCost(LivingEntity entity, ItemStack stack, Spell spell, float costMultiplier, boolean hurt) {
        return !(entity instanceof ServerPlayer player) || Platform.INSTANCE.getPlayerData(player)
                .map(data -> {
                    if (!LevelCalc.useRP(player, data, spell.rpCost() * costMultiplier, hurt, spell.percentageCost(), true, spell.costReductionSkills())) {
                        if (!hurt)
                            player.connection.send(
                                    new ClientboundSoundPacket(SoundEvents.VILLAGER_NO, SoundSource.PLAYERS, player.position().x, player.position().y, player.position().z, 1, 1));
                        return false;
                    }
                    return true;
                }).orElse(false);
    }

    public void update(Player player, ItemStack stack) {
    }

    public void levelSkill(ServerPlayer player) {
        Map<EnumSkills, Float> skillXp = DataPackHandler.INSTANCE.spellPropertiesManager().getPropertiesFor(this).skillXP;
        if (!skillXp.isEmpty()) {
            Platform.INSTANCE.getPlayerData(player).ifPresent(data ->
                    skillXp.forEach((skill, xp) -> LevelCalc.levelSkill(player, data, EnumSkills.DARK, xp)));
        }
    }

    public int coolDown() {
        return DataPackHandler.INSTANCE.spellPropertiesManager().getPropertiesFor(this).cooldown;
    }

    public int rpCost() {
        return DataPackHandler.INSTANCE.spellPropertiesManager().getPropertiesFor(this).rpCost;
    }

    public boolean percentageCost() {
        return DataPackHandler.INSTANCE.spellPropertiesManager().getPropertiesFor(this).percentage;
    }

    public EnumSkills[] costReductionSkills() {
        return DataPackHandler.INSTANCE.spellPropertiesManager().getPropertiesFor(this).skills.toArray(EnumSkills[]::new);
    }

    public boolean use(LivingEntity entity) {
        if (entity.level instanceof ServerLevel serverLevel)
            return this.use(serverLevel, entity, ItemStack.EMPTY);
        return false;
    }

    public boolean use(LivingEntity entity, boolean ignoreSeal) {
        if (entity.level instanceof ServerLevel serverLevel)
            return this.use(serverLevel, entity, ItemStack.EMPTY, ignoreSeal);
        return false;
    }

    public boolean use(ServerLevel world, LivingEntity entity, ItemStack stack) {
        return this.use(world, entity, stack, false);
    }

    public boolean use(ServerLevel world, LivingEntity entity, ItemStack stack, boolean ignoreSeal) {
        if (!ignoreSeal && EntityUtils.sealed(entity)) {
            if (entity instanceof ServerPlayer player) {
                player.connection.send(new ClientboundSoundPacket(SoundEvents.VILLAGER_NO, entity.getSoundSource(), entity.getX(), entity.getY(), entity.getZ(), 1, 0.7f));
            }
            return false;
        }
        return this.use(world, entity, stack, 1, 1, CombatUtils.getSpellLevelFromStack(stack));
    }

    public abstract boolean use(ServerLevel world, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int level);

    public AttackAction useAction() {
        return ModAttackActions.STAFF_USE.get();
    }

    public boolean canUse(ServerLevel world, LivingEntity entity, ItemStack stack) {
        return true;
    }

    @Override
    public String toString() {
        return this.getRegistryName().toString();
    }
}
