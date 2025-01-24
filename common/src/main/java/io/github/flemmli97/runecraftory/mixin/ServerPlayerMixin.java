package io.github.flemmli97.runecraftory.mixin;

import com.mojang.authlib.GameProfile;
import io.github.flemmli97.runecraftory.common.quests.QuestData;
import io.github.flemmli97.runecraftory.common.quests.QuestHandler;
import io.github.flemmli97.runecraftory.mixinhelper.QuestDataGet;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player implements QuestDataGet {

    @Unique
    private QuestData runecraftory_questData;

    private ServerPlayerMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void initData(CallbackInfo info) {
        this.runecraftory_questData = new QuestData((ServerPlayer) (Object) this);
    }

    @Inject(method = "restoreFrom", at = @At("RETURN"))
    private void copyOld(ServerPlayer oldPlayer, boolean alive, CallbackInfo info) {
        this.runecraftory_questData.clone(QuestHandler.getData(oldPlayer));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void save(CompoundTag compound, CallbackInfo info) {
        this.runecraftory_questData.load(compound.getCompound("RunecraftoryQuestData"));
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void onTick(CallbackInfo info) {
        this.runecraftory_questData.tick();
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void read(CompoundTag compound, CallbackInfo info) {
        compound.put("RunecraftoryQuestData", this.runecraftory_questData.save());
    }

    @Override
    public QuestData runecraftory$getQuestData() {
        return this.runecraftory_questData;
    }
}
