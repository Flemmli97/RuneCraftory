package com.flemmli97.runecraftory.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class InvWrapperPlayer extends RecipeWrapper {

    private final PlayerEntity player;

    private InvWrapperPlayer(IItemHandlerModifiable inv, PlayerEntity player){
        super(inv);
        this.player = player;
    }

    public PlayerEntity getPlayer(){
        return this.player;
    }

    public static InvWrapperPlayer create(IItemHandlerModifiable inv, PlayerEntity player){
        return new InvWrapperPlayer(inv, player);
    }
}
