package io.github.flemmli97.runecraftory.common.items;

import io.github.flemmli97.runecraftory.common.blocks.tile.BossSpawnerBlockEntity;
import io.github.flemmli97.runecraftory.common.entities.IBaseMob;
import io.github.flemmli97.runecraftory.common.lib.LibConstants;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
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

public class RuneCraftoryEggItem extends SpawnEgg {

    public RuneCraftoryEggItem(Supplier<? extends EntityType<?>> type, int primary, int secondary, Properties props) {
        super(type, primary, secondary, props);
    }

    @Override
    public boolean onEntitySpawned(Entity e, ItemStack stack, Player player) {
        if (e instanceof IBaseMob && stack.hasCustomHoverName()) {
            int level = LibConstants.BASE_LEVEL;
            try {
                level = Integer.parseInt(stack.getHoverName().getContents());
            } catch (NumberFormatException ignored) {
            }
            ((IBaseMob) e).setLevel(level);
        }
        //Temporary fix for Forge-Bug-#7730
        if (e.getBbWidth() > 0.7 && e.getBbWidth() < 1) {
            e.setPos(e.getX() + 0.05, e.getY(), e.getZ());
        }
        return super.onEntitySpawned(e, stack, player);
    }

    @Override
    public Component getEntityName(ItemStack stack) {
        Component comp = super.getEntityName(stack);
        if (comp != null) {
            try {
                Integer.parseInt(comp.getContents());
                return null;
            } catch (NumberFormatException e) {
                return comp;
            }
        }
        return null;
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
