package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.AttackActionHandler;
import io.github.flemmli97.runecraftory.api.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.action.DataKey;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolHammer;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ToolHammerUse extends AttackAction {

    private final ComboContainer combo = ComboContainer.Builder.builder()
            .addCombo(handler -> handler.getAnimation().isPast("attack"), 4)
            .addCombo(handler -> handler.getAnimation().isPast("attack"), 4)
            .build();

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int comboIdx) {
        return PlayerModelAnimations.HAMME_AXE_USE.create(1.1f);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, AttackActionHandler handler, AnimatedAction anim) {
        if (entity.level() instanceof ServerLevel serverLevel && anim.isAt("attack") && stack.getItem() instanceof ItemToolHammer hammer) {
            ItemToolHammer.setDontUseRPFlagTemp(stack, true);
            int range = handler.get(DataKey.TOOL_DATA).charge();
            BlockPos pos = entity.blockPosition();
            if (handler.get(DataKey.TOOL_DATA).result() instanceof BlockHitResult hitResult && hitResult.getType() != HitResult.Type.MISS) {
                pos = hitResult.getBlockPos();
            }
            int amount = (int) BlockPos.betweenClosedStream(pos.offset(-range, -1, -range), pos.offset(range, 0, range))
                    .filter(p -> hammer.hammer(serverLevel, p.immutable(), stack, entity, true) != ItemToolHammer.HammerState.FAIL)
                    .count();
            if (amount > 0 && entity instanceof ServerPlayer player) {
                PlayerData data = Platform.INSTANCE.getPlayerData(player);
                LevelCalc.useRP(data, range * 15, true, 0, true, EnumSkills.MINING);
                LevelCalc.levelSkill(data, EnumSkills.MINING, (range + 1) * 10);
            }
            ItemToolHammer.setDontUseRPFlagTemp(stack, false);
        }
    }

    @Override
    public boolean disableItemSwitch() {
        return false;
    }

    @Override
    public ComboContainer combos() {
        return this.combo;
    }
}
