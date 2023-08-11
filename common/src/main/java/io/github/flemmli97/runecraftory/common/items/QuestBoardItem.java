package io.github.flemmli97.runecraftory.common.items;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.common.blocks.BlockQuestboard;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class QuestBoardItem extends BlockItem implements MultiBlockItem {

    private final Supplier<List<Pair<BlockPos, BlockState>>> states = Suppliers.memoize(() -> BlockQuestboard.getPosMap(BlockPos.ZERO, ModBlocks.questBoard.get().defaultBlockState())
            .stream().map(p -> Pair.of(p.getSecond(), ModBlocks.questBoard.get().defaultBlockState().setValue(BlockQuestboard.PART, p.getFirst())))
            .toList());

    public QuestBoardItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(new TextComponent("WIP").withStyle(ChatFormatting.DARK_RED));
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    @Override
    public List<Pair<BlockPos, BlockState>> getBlocks() {
        return this.states.get();
    }
}
