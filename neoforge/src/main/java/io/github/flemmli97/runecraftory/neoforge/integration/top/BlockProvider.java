package io.github.flemmli97.runecraftory.neoforge.integration.top;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.blocks.MonsterBarnBlock;
import io.github.flemmli97.runecraftory.common.blocks.TreeBaseBlock;
import io.github.flemmli97.runecraftory.common.blocks.entity.MonsterBarnBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.entity.TreeBlockEntity;
import io.github.flemmli97.runecraftory.common.world.data.BarnData;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockProvider implements IProbeInfoProvider {

    public static final ResourceLocation ID = RuneCraftory.modRes("block_provider");

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, Player player, Level level, BlockState blockState, IProbeHitData iProbeHitData) {
        if (blockState.getBlock() instanceof MonsterBarnBlock) {
            BlockEntity entity = level.getBlockEntity(iProbeHitData.getPos());
            if (entity instanceof MonsterBarnBlockEntity barn) {
                BarnData data = barn.getBarnData();
                if (data != null) {
                    int size = data.getSize();
                    Component sizeText = size > 1 ? withTextColored(Component.literal("" + size), ChatFormatting.GREEN)
                            : withTextColored(Component.literal("" + size), ChatFormatting.DARK_RED);
                    if (!data.hasRoof()) {
                        iProbeInfo.text(Component.translatable("runecraftory.dependency.tooltips.barn.1",
                                sizeText));
                    } else {
                        iProbeInfo.text(Component.translatable("runecraftory.dependency.tooltips.barn.1.alt", withTextColored(Component.translatable("" + data.roofHeight()), ChatFormatting.YELLOW),
                                sizeText));
                    }
                    iProbeInfo.text(Component.translatable("runecraftory.dependency.tooltips.barn.2", data.usedCapacity(), data.getCapacity()));
                }
            }
        }
        if (blockState.getBlock() instanceof TreeBaseBlock) {
            BlockEntity entity = level.getBlockEntity(iProbeHitData.getPos());
            if (entity instanceof TreeBlockEntity tree) {
                iProbeInfo.text(Component.translatable("runecraftory.dependency.tooltips.tree", tree.getHealth()));
            }
        }
    }

    private static Component withTextColored(Component other, ChatFormatting formatting) {
        return Component.literal("§" + formatting.getChar()).append(other);
    }
}
