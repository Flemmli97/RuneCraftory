package io.github.flemmli97.runecraftory.forge.integration.top;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.attachment.player.LevelExpPair;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.IBaseMob;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.common.world.BarnData;
import mcjty.theoneprobe.api.CompoundText;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.NumberFormat;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.UsernameCache;

public class EntityProbeProvider implements IProbeInfoEntityProvider {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "entity_provider");

    @Override
    public String getID() {
        return ID.toString();
    }

    @Override
    public void addProbeEntityInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level level, Entity entity, IProbeHitEntityData iProbeHitEntityData) {
        if (entity instanceof IBaseMob mob && (probeMode == ProbeMode.DEBUG
                || player.getMainHandItem().getItem() == ModItems.debug || player.isCreative()
                || (entity instanceof OwnableEntity ownable && player.getUUID().equals(ownable.getOwnerUUID())))) {
            LevelExpPair entityLevel = mob.level();
            probeInfo.progress((int) entityLevel.getXp(), LevelCalc.xpAmountForLevelUp(entityLevel.getLevel()),
                    probeInfo.defaultProgressStyle().width(150).height(12)
                            .color(0xff000000, 0xff0c8995, 0xff0c8995, 0xff8b8b8b)
                            .alignment(ElementAlignment.ALIGN_CENTER)
                            .prefix(new TranslatableComponent("runecraftory.tooltip.item.level", mob.level().getLevel()))
                            .numberFormat(NumberFormat.NONE));
        }
        if (entity instanceof BaseMonster monster) {
            if (monster.getOwnerUUID() != null) {
                String username = UsernameCache.getLastKnownUsername(monster.getOwnerUUID());
                if (username == null) {
                    probeInfo.text(CompoundText.create().warning(new TranslatableComponent("runecraftory.dependency.tooltips.owner.none")));
                } else {
                    probeInfo.text(CompoundText.create().style(TextStyleClass.HIGHLIGHTED).text(new TranslatableComponent("runecraftory.dependency.tooltips.owner", username)));
                }
                if (player.getUUID().equals(monster.getOwnerUUID())) {
                    withText(probeInfo, "runecraftory.dependency.tooltips.friendpoints", new TextComponent("" + monster.getFriendlyPoints().getLevel()), ChatFormatting.YELLOW);
                    BarnData barn = monster.getAssignedBarn();
                    if (barn != null) {
                        withText(probeInfo, "runecraftory.dependency.tooltips.barn", new TextComponent(String.format("[%s, %s, %s]", barn.pos.pos().getX(), barn.pos.pos().getY(), barn.pos.pos().getZ())), ChatFormatting.GREEN);
                    } else {
                        probeInfo.text(CompoundText.create().style(TextStyleClass.ERROR).text(new TranslatableComponent("runecraftory.dependency.tooltips.barn.no")));
                    }
                    withText(probeInfo, "runecraftory.dependency.tooltips.behaviour", new TextComponent("" + monster.behaviourState()), ChatFormatting.YELLOW);
                }
            }
        }
        if (entity instanceof EntityNPCBase npc) {
            if (npc.followEntity() != null) {
                withText(probeInfo, "runecraftory.dependency.tooltips.npc.follow", npc.followEntity().getDisplayName(), ChatFormatting.YELLOW);
            }
            withText(probeInfo, "runecraftory.dependency.tooltips.friendpoints", new TextComponent("" + npc.friendPoints(player)), ChatFormatting.YELLOW);
        }
    }

    private static void withText(IProbeInfo info, String main, Component other, ChatFormatting formatting) {
        info.text(CompoundText.create().info(new TranslatableComponent(main, new TextComponent("§" + formatting.getChar()).append(other))));
    }
}
