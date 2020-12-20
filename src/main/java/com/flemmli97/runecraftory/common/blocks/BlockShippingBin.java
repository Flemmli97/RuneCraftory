package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.common.capability.IPlayerCap;
import com.flemmli97.runecraftory.common.capability.PlayerCapProvider;
import com.flemmli97.runecraftory.common.inventory.InventoryShippingBin;
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

    public BlockShippingBin(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public ActionResultType onUse(BlockState p_225533_1_, World p_225533_2_, BlockPos p_225533_3_, PlayerEntity p_225533_4_, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {

        if (p_225533_2_.isRemote)
            return ActionResultType.SUCCESS;
        InventoryShippingBin shippingInv = p_225533_4_.getCapability(PlayerCapProvider.PlayerCap).map(IPlayerCap::getShippingInv).orElse(null);
        if (shippingInv != null)
            p_225533_4_.openContainer(new SimpleNamedContainerProvider((p_226928_1_, p_226928_2_, p_226928_3_) -> ChestContainer.createGeneric9X6(p_226928_1_, p_226928_2_, new RecipeWrapper(shippingInv) {
                @Override
                public int getInventoryStackLimit() {
                    return 64;
                }
            }), name));
        return super.onUse(p_225533_1_, p_225533_2_, p_225533_3_, p_225533_4_, p_225533_5_, p_225533_6_);
    }
}
