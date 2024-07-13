package io.github.flemmli97.runecraftory.common.items.creative;

import io.github.flemmli97.runecraftory.common.blocks.tile.BossSpawnerBlockEntity;
import io.github.flemmli97.runecraftory.common.entities.EnsembleMonsters;
import io.github.flemmli97.runecraftory.common.network.S2CSpawnEggScreen;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class EnsembleEggItem extends SpawnEgg {

    public EnsembleEggItem(Supplier<? extends EntityType<?>> type, int primary, int secondary, Properties props) {
        super(type, primary, secondary, props);
    }

    @Override
    public boolean onEntitySpawned(Entity e, ItemStack stack, Player player) {
        if (e instanceof EnsembleMonsters mob) {
            mob.setLevel(RuneCraftoryEggItem.getMobLevel(stack));
        }
        return super.onEntitySpawned(e, stack, player);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        InteractionResultHolder<ItemStack> res = super.use(world, player, hand);
        if (res.getResult() == InteractionResult.PASS) {
            if (player instanceof ServerPlayer serverPlayer)
                Platform.INSTANCE.sendToClient(new S2CSpawnEggScreen(hand), serverPlayer);
            return InteractionResultHolder.consume(player.getItemInHand(hand));
        }
        return res;
    }

    @Override
    public InteractionResult onBlockUse(ItemStack stack, BlockPos pos, BlockState state, @Nullable BlockEntity tile) {
        if (tile instanceof BossSpawnerBlockEntity) {
            ((BossSpawnerBlockEntity) tile).setEntity(PlatformUtils.INSTANCE.entities().getIDFrom(this.getType(stack.getTag())));
            return InteractionResult.SUCCESS;
        }
        return super.onBlockUse(stack, pos, state, tile);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(new TranslatableComponent("runecraftory.tooltip.item.spawn").withStyle(ChatFormatting.GOLD));
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }
}
