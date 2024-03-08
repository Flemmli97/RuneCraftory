package io.github.flemmli97.runecraftory.common.blocks;

import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.loot.LootCtxParameters;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockHerb extends BushBlock {

    public static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);

    public static final IntegerProperty LEVEL = IntegerProperty.create("variant", 0, 10);

    private final Set<GroundTypes> types = EnumSet.noneOf(GroundTypes.class);

    public BlockHerb(BlockBehaviour.Properties props, GroundTypes... types) {
        super(props);
        this.types.addAll(Arrays.asList(types));
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return super.getDrops(state, builder.withParameter(LootCtxParameters.ITEM_LEVEL_CONTEXT, state.getValue(LEVEL)));
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        if (this.types.contains(GroundTypes.NETHER))
            if (state.is(BlockTags.BASE_STONE_NETHER) || state.is(BlockTags.NYLIUM))
                return true;
        if (this.types.contains(GroundTypes.END))
            if (state.is(RunecraftoryTags.ENDSTONES))
                return true;
        if (this.types.contains(GroundTypes.SANDY))
            if (state.is(BlockTags.SAND))
                return true;
        return state.is(BlockTags.DIRT) || state.is(RunecraftoryTags.FARMLAND) || super.mayPlaceOn(state, level, pos);
    }

    @Override
    public BlockBehaviour.OffsetType getOffsetType() {
        return BlockBehaviour.OffsetType.XZ;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 vector3d = state.getOffset(level, pos);
        return SHAPE.move(vector3d.x, vector3d.y, vector3d.z);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(LEVEL, ItemNBT.itemLevel(context.getItemInHand()));
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return ItemNBT.getLeveledItem(super.getCloneItemStack(level, pos, state), 1);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    public enum GroundTypes {
        NETHER,
        END,
        SANDY
    }
}