package io.github.flemmli97.runecraftory.common.items.creative;

import io.github.flemmli97.runecraftory.common.blocks.entity.BossSpawnerBlockEntity;
import io.github.flemmli97.runecraftory.common.entities.utils.IBaseMob;
import io.github.flemmli97.runecraftory.common.network.S2CSpawnEggScreen;
import io.github.flemmli97.runecraftory.common.registry.ModDataComponentTypes;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class RuneCraftoryEggItem extends SpawnEgg {

    public RuneCraftoryEggItem(Supplier<? extends EntityType<? extends Mob>> type, int primary, int secondary, Properties props) {
        super(new EntityTypeHolder<>(Mob.class, type), primary, secondary, props);
    }

    @Override
    public boolean onEntitySpawned(Entity e, ItemStack stack, Player player) {
        if (e instanceof IBaseMob mob) {
            mob.setXPLevel(stack.getOrDefault(ModDataComponentTypes.SPAWN_EGG_LEVEL.get(), 1));
        }
        //Temporary fix for Forge-Bug-#7730
        if (e.getBbWidth() > 0.7 && e.getBbWidth() < 1) {
            e.setPos(e.getX() + 0.05, e.getY(), e.getZ());
        }
        return super.onEntitySpawned(e, stack, player);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        InteractionResultHolder<ItemStack> res = super.use(world, player, hand);
        if (res.getResult() == InteractionResult.PASS) {
            if (player instanceof ServerPlayer serverPlayer)
                LoaderNetwork.INSTANCE.sendToPlayer(new S2CSpawnEggScreen(hand), serverPlayer);
            return InteractionResultHolder.consume(player.getItemInHand(hand));
        }
        return res;
    }

    @Override
    public InteractionResult onBlockUse(ItemStack stack, BlockPos pos, BlockState state, @Nullable BlockEntity tile) {
        if (tile instanceof BossSpawnerBlockEntity) {
            ((BossSpawnerBlockEntity) tile).setEntity(this.getType(stack));
            return InteractionResult.SUCCESS;
        }
        return super.onBlockUse(stack, pos, state, tile);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("runecraftory.tooltip.item.spawn").withStyle(ChatFormatting.GOLD));
        super.appendHoverText(stack, context, list, tooltipFlag);
    }
}
