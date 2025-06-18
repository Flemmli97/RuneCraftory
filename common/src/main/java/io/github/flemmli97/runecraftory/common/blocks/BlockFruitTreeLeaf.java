package io.github.flemmli97.runecraftory.common.blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Supplier;

public class BlockFruitTreeLeaf extends LeavesBlock {

    public static final MapCodec<BlockFruitTreeLeaf> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(
                    propertiesCodec(),
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("fruit").forGetter(d -> d.fruit.get())
            ).apply(inst, (prop, fruit) -> new BlockFruitTreeLeaf(prop, () -> fruit)));

    public static final BooleanProperty HAS_FRUIT = BooleanProperty.create("has_fruit");

    private final Supplier<Item> fruit;

    public BlockFruitTreeLeaf(Properties properties, Supplier<Item> fruit) {
        super(properties);
        this.fruit = fruit;
    }

    @Override
    public MapCodec<BlockFruitTreeLeaf> codec() {
        return CODEC;
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        return super.getDestroyProgress(state, player, level, pos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HAS_FRUIT);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide)
            return ItemInteractionResult.SUCCESS;
        if (state.getValue(HAS_FRUIT)) {
            Block.popResource(level, pos.below(), new ItemStack(this.fruit.get()));
            level.setBlock(pos, state.setValue(HAS_FRUIT, false), Block.UPDATE_ALL);
            return ItemInteractionResult.CONSUME;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}

