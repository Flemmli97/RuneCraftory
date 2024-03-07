package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolHammer;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ToolHammerUse extends AttackAction {

    private static final AttackChain CHAIN = new AttackChain(3, 15);

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        return AnimatedAction.builder(20 + 1, "hammer_axe_use").marker(12).speed(1.3f).build();
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (entity.getLevel() instanceof ServerLevel serverLevel && anim.canAttack() && stack.getItem() instanceof ItemToolHammer hammer) {
            ItemToolHammer.setDontUseRPFlagTemp(stack, true);
            int range = handler.getToolUseData().charge();
            BlockPos pos = entity.blockPosition();
            if (handler.getToolUseData().result() instanceof BlockHitResult hitResult && hitResult.getType() != HitResult.Type.MISS) {
                pos = hitResult.getBlockPos();
            }
            int amount = (int) BlockPos.betweenClosedStream(pos.offset(-range, -1, -range), pos.offset(range, 0, range))
                    .filter(p -> hammer.hammer(serverLevel, p.immutable(), stack, entity, true) != ItemToolHammer.HammerState.FAIL)
                    .count();
            if (amount > 0 && entity instanceof ServerPlayer player) {
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
                    LevelCalc.useRP(player, data, range * 15, true, false, true, EnumSkills.MINING);
                    LevelCalc.levelSkill(player, data, EnumSkills.MINING, (range + 1) * 10);
                });
            }
            ItemToolHammer.setDontUseRPFlagTemp(stack, false);
        }
    }

    @Override
    public boolean disableItemSwitch() {
        return false;
    }

    @Override
    public AttackChain attackChain(LivingEntity entity, int chain) {
        return CHAIN;
    }
}
