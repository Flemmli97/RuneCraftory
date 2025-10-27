package io.github.flemmli97.runecraftory.api.registry;

import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.api.datapack.SpellProperties;
import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttackActions;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public abstract class Spell {

    public static boolean tryUseWithCost(LivingEntity entity, ItemStack stack, Spell spell) {
        return tryUseWithCost(entity, stack, spell, 1);
    }

    public static boolean tryUseWithCost(LivingEntity entity, ItemStack stack, Spell spell, float costMultiplier) {
        return tryUseWithCost(entity, stack, spell, costMultiplier, stack.getItem() instanceof ItemStaffBase);
    }

    public static boolean tryUseWithCost(LivingEntity entity, ItemStack stack, Spell spell, float costMultiplier, boolean hurt) {
        if (!(entity instanceof ServerPlayer player))
            return true;
        PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
        if (!LevelCalc.useRP(data, spell.properties().rpCost() * costMultiplier, hurt, spell.properties().percentageCost(), true, spell.costReductionSkills())) {
            if (!hurt)
                EntityUtils.playSoundForPlayer(player, RuneCraftorySounds.GENERIC_DENY.get(), 1, 1);
            return false;
        }
        return true;
    }

    public static void playSound(LivingEntity entity, SoundEvent sound, float volume, float pitch) {
        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), sound, entity.getSoundSource(), volume, pitch);
    }

    public void levelSkill(ServerPlayer player) {
        Map<Skills, Float> skillXp = this.properties().skillXP();
        if (!skillXp.isEmpty()) {
            PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
            skillXp.forEach((skill, xp) -> LevelCalc.levelSkill(data, Skills.DARK, xp));
        }
    }

    public Skills[] costReductionSkills() {
        return DataPackHandler.INSTANCE.spellPropertiesManager().getPropertiesFor(this).skills().toArray(Skills[]::new);
    }

    public SpellProperties properties() {
        return DataPackHandler.INSTANCE.spellPropertiesManager().getPropertiesFor(this);
    }

    public boolean use(LivingEntity entity) {
        if (entity.level() instanceof ServerLevel serverLevel)
            return this.use(serverLevel, entity, ItemStack.EMPTY);
        return false;
    }

    public boolean use(LivingEntity entity, boolean ignoreSeal) {
        if (entity.level() instanceof ServerLevel serverLevel)
            return this.use(serverLevel, entity, ItemStack.EMPTY, ignoreSeal);
        return false;
    }

    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack) {
        return this.use(level, entity, stack, false);
    }

    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, boolean ignoreSeal) {
        if (!ignoreSeal && EntityUtils.sealed(entity)) {
            if (entity instanceof ServerPlayer player) {
                EntityUtils.playSoundForPlayer(player, RuneCraftorySounds.GENERIC_DENY.get(), 1, 1);
            }
            return false;
        }
        return this.use(level, entity, stack, 1, 1, CombatUtils.getSpellLevelFromStack(stack));
    }

    public abstract boolean use(ServerLevel serverLevel, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int level);

    public AttackAction useAction() {
        return RuneCraftoryAttackActions.STAFF_USE.get();
    }

    public boolean canUse(ServerLevel serverLevel, LivingEntity entity, ItemStack stack) {
        return entity.getVehicle() == null || this.usableOnMounts();
    }

    public boolean delayedUse() {
        return true;
    }

    public boolean usableOnMounts() {
        return this.useAction().usableOnMounts(0);
    }

    @Override
    public String toString() {
        return RuneCraftorySpells.SPELLS.registry().getKey(this).toString();
    }
}
