package io.github.flemmli97.runecraftory.common.blocks;

import com.mojang.authlib.GameProfile;
import io.github.flemmli97.runecraftory.common.blocks.tile.MonsterBarnBlockEntity;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BlockMonsterBarn extends BaseEntityBlock {

    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 3, 16);

    public BlockMonsterBarn(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (placer instanceof ServerPlayer player && level.getBlockEntity(pos) instanceof MonsterBarnBlockEntity barn)
            barn.setOwner(player.getUUID());
        super.setPlacedBy(level, pos, state, placer, stack);
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        UUID owner = null;
        if (player.isCreative() || (level.getBlockEntity(pos) instanceof MonsterBarnBlockEntity barn && player.getUUID().equals(owner = barn.getOwner())))
            return super.getDestroyProgress(state, player, level, pos);
        if (!player.level.isClientSide && owner != null) {
            UUID uuid = owner;
            Platform.INSTANCE.getPlayerData(player)
                    .ifPresent(d -> {
                        if (d.onBarnFailMine(pos))
                            player.sendMessage(new TranslatableComponent("runecraftory.barn.interact.not.owner", player.getServer()
                                    .getProfileCache().get(uuid).map(GameProfile::getName).orElse("UNKNOWN")).withStyle(ChatFormatting.DARK_RED), Util.NIL_UUID);
                    });
        }
        return 0;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (level.getBlockEntity(pos) instanceof MonsterBarnBlockEntity barn && level instanceof ServerLevel serverLevel)
            WorldHandler.get(serverLevel.getServer()).removeBarn(barn.getOwner(), GlobalPos.of(level.dimension(), pos));
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            UUID owner = null;
            if (level.getBlockEntity(pos) instanceof MonsterBarnBlockEntity barn && (player.isCreative() || player.getUUID().equals(owner = barn.getOwner()))) {
                if (barn.getBarnData() != null) {
                    String key = barn.getBarnData().hasRoof() ? "runecraftory.barn.interact.block.roofed" : "runecraftory.barn.interact.block";
                    player.sendMessage(new TranslatableComponent(key, barn.getBarnData().getCapacity(), barn.getBarnData().getCapacity() - barn.getBarnData().usedCapacity())
                            .withStyle(barn.getBarnData().getCapacity() > 0 ? ChatFormatting.GOLD : ChatFormatting.DARK_RED), Util.NIL_UUID);
                }
            } else if (owner != null) {
                UUID uuid = owner;
                Platform.INSTANCE.getPlayerData(player)
                        .ifPresent(d -> {
                            if (d.onBarnFailMine(pos))
                                player.sendMessage(new TranslatableComponent("runecraftory.barn.interact.not.owner", player.getServer()
                                        .getProfileCache().get(uuid).map(GameProfile::getName).orElse("UNKNOWN")).withStyle(ChatFormatting.DARK_RED), Util.NIL_UUID);
                        });
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MonsterBarnBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : BaseEntityBlock.createTickerHelper(blockEntityType, ModBlocks.monsterBarnBlockEntity.get(), MonsterBarnBlockEntity::tick);
    }
}
