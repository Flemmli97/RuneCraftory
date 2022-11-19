package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.mixinhelper.MixinUtils;
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
public abstract class PlayerMixin implements PrevEntityPosition {

    @Unique
    private double oldMPosX1, oldMPosX2;
    @Unique
    private double oldMPosY1, oldMPosY2;
    @Unique
    private double oldMPosZ1, oldMPosZ2;
    @Unique
    private boolean saveAt2;

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
        if (this.saveAt2) {
            this.oldMPosX2 = ((Player) (Object) this).getX();
            this.oldMPosY2 = ((Player) (Object) this).getY();
            this.oldMPosZ2 = ((Player) (Object) this).getZ();
            this.saveAt2 = false;
        } else {
            this.oldMPosX1 = ((Player) (Object) this).getX();
            this.oldMPosY1 = ((Player) (Object) this).getY();
            this.oldMPosZ1 = ((Player) (Object) this).getZ();
            this.saveAt2 = true;
        }
    }

    @Override
    public double getOldPlayerX() {
        return this.saveAt2 ? this.oldMPosX2 : this.oldMPosX1;
    }

    @Override
    public double getOldPlayerY() {
        return this.saveAt2 ? this.oldMPosY2 : this.oldMPosY1;
    }

    @Override
    public double getOldPlayerZ() {
        return this.saveAt2 ? this.oldMPosZ2 : this.oldMPosZ1;
    }
}
