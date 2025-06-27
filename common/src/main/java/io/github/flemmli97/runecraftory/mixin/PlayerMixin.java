package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.mixinhelper.MixinUtils;
import io.github.flemmli97.runecraftory.mixinhelper.PlayerExtended;
import io.github.flemmli97.runecraftory.mixinhelper.PrevEntityPosition;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin implements PrevEntityPosition, PlayerExtended {

    @Unique
    private double runecraftory$oldMPosX1, runecraftory$oldMPosX2;
    @Unique
    private double runecraftory$oldMPosY1, runecraftory$oldMPosY2;
    @Unique
    private double runecraftory$oldMPosZ1, runecraftory$oldMPosZ2;
    @Unique
    private boolean runecraftory$saveAt2;

    @Inject(method = "updatePlayerPose", at = @At("HEAD"), cancellable = true)
    private void noPoseUpdate(CallbackInfo info) {
        if (MixinUtils.playerPose((Player) (Object) this))
            info.cancel();
    }

    @Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At(value = "TAIL"))
    private void itemDrop(CallbackInfoReturnable<ItemEntity> info) {
        MixinUtils.onPlayerThrowItem((Player) (Object) this, info.getReturnValue());
    }

    //Saving actually the last 2 positions of the player
    @Inject(method = "tick", at = @At("HEAD"))
    private void savePos(CallbackInfo info) {
        if (this.runecraftory$saveAt2) {
            this.runecraftory$oldMPosX2 = ((Player) (Object) this).getX();
            this.runecraftory$oldMPosY2 = ((Player) (Object) this).getY();
            this.runecraftory$oldMPosZ2 = ((Player) (Object) this).getZ();
            this.runecraftory$saveAt2 = false;
        } else {
            this.runecraftory$oldMPosX1 = ((Player) (Object) this).getX();
            this.runecraftory$oldMPosY1 = ((Player) (Object) this).getY();
            this.runecraftory$oldMPosZ1 = ((Player) (Object) this).getZ();
            this.runecraftory$saveAt2 = true;
        }
    }

    @Override
    public double runecraftory$getOldPlayerX() {
        return this.runecraftory$saveAt2 ? this.runecraftory$oldMPosX2 : this.runecraftory$oldMPosX1;
    }

    @Override
    public double runecraftory$getOldPlayerY() {
        return this.runecraftory$saveAt2 ? this.runecraftory$oldMPosY2 : this.runecraftory$oldMPosY1;
    }

    @Override
    public double runecraftory$getOldPlayerZ() {
        return this.runecraftory$saveAt2 ? this.runecraftory$oldMPosZ2 : this.runecraftory$oldMPosZ1;
    }
}
