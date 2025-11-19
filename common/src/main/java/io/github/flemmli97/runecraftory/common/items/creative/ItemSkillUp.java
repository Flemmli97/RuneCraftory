package io.github.flemmli97.runecraftory.common.items.creative;

import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemSkillUp extends Item {

    public ItemSkillUp(Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
            for (Skills skill : Skills.values())
                data.increaseSkill(skill, skill.getProperties().xpAmountForNext(data.getSkillLevel(skill).getLevel()) - data.getSkillLevel(skill).getXp());
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}