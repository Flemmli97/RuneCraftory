package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttackActions;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.tenshilib.loader.registry.AttachmentRegister;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class NaiveBladeSpell extends WeaponSpell {

    public NaiveBladeSpell() {
        super(RuneCraftoryAttackActions.NAIVE_BLADE, RunecraftoryTags.Items.LONGSWORDS);
    }

    @Override
    public boolean canUse(ServerLevel level, LivingEntity entity, ItemStack stack) {
        if (entity instanceof Player player && RunecraftoryAttachments.PLAYER_DATA.get().get(player).getWeaponHandler().getCurrentAction() == this.useAction())
            return false;
        return super.canUse(level, entity, stack);
    }
}