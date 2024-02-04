package io.github.flemmli97.runecraftory.forge.integration.top;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.blocks.BlockMonsterBarn;
import io.github.flemmli97.runecraftory.common.blocks.tile.MonsterBarnBlockEntity;
import io.github.flemmli97.runecraftory.common.world.BarnData;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockProvider implements IProbeInfoProvider {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "block_provider");

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, Player player, Level level, BlockState blockState, IProbeHitData iProbeHitData) {
        if (blockState.getBlock() instanceof BlockMonsterBarn) {
            BlockEntity entity = level.getBlockEntity(iProbeHitData.getPos());
            if (entity instanceof MonsterBarnBlockEntity barn) {
                BarnData data = barn.getBarnData();
                if (data != null) {
                    int size = data.getSize();
                    Component sizeText = size > 1 ? withTextColored(new TextComponent("" + size), ChatFormatting.GREEN)
                            : withTextColored(new TextComponent("" + size), ChatFormatting.DARK_RED);
                    iProbeInfo.text(new TranslatableComponent("runecraftory.dependency.tooltips.barn.1", withTextColored(new TranslatableComponent(data.hasRoof() ? "runecraftory.generic.yes" : "runecraftory.generic.no"), ChatFormatting.YELLOW),
                            sizeText));
                    iProbeInfo.text(new TranslatableComponent("runecraftory.dependency.tooltips.barn.2", data.usedCapacity(), data.getCapacity()));
                }
            }
        }
    }

    private static Component withTextColored(Component other, ChatFormatting formatting) {
        return new TextComponent("§" + formatting.getChar()).append(other);
    }
}
