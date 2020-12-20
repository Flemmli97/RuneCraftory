package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.common.capability.IPlayerCap;
import com.flemmli97.runecraftory.common.capability.PlayerCapProvider;
import com.flemmli97.runecraftory.common.inventory.InventoryShippingBin;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class BlockShippingBin extends Block {

    private static final ITextComponent name = new TranslationTextComponent("container.shipping_bin");

    public BlockShippingBin(AbstractBlock.Properties props) {
        super(props);
    }

    @Override
    public ActionResultType onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (world.isRemote)
            return ActionResultType.SUCCESS;
        InventoryShippingBin shippingInv = player.getCapability(PlayerCapProvider.PlayerCap).map(IPlayerCap::getShippingInv).orElse(null);
        if (shippingInv != null) {
            player.openContainer(new SimpleNamedContainerProvider((p_226928_1_, p_226928_2_, p_226928_3_) -> ChestContainer.createGeneric9X6(p_226928_1_, p_226928_2_, new RecipeWrapper(shippingInv) {
                @Override
                public int getInventoryStackLimit() {
                    return 64;
                }
            }), name));
            return ActionResultType.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, rayTraceResult);
    }
}
