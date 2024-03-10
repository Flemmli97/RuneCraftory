package io.github.flemmli97.runecraftory.common.items;

import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.tenshilib.platform.EventCalls;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BabySpawnEgg extends Item {

    public BabySpawnEgg(Properties props) {
        super(props);
    }

    public static ItemStack createBabyFrom(EntityNPCBase baby, Component playerName, UUID father, UUID mother) {
        CompoundTag tag = baby.saveWithoutId(new CompoundTag());
        tag.remove("Pos");
        tag.remove(Entity.UUID_TAG);
        tag.remove("Motion");
        tag.remove("Rotation");
        ItemStack stack = new ItemStack(ModItems.NPC_BABY.get());
        CompoundTag stackTag = stack.getOrCreateTag();
        stackTag.put(EntityType.ENTITY_TAG, tag);
        stackTag.putBoolean("Boy", baby.isMale());
        stackTag.putBoolean("NeedsName", !baby.hasDataName());
        stackTag.putUUID("Father", father);
        stackTag.putUUID("Mother", mother);
        stackTag.putString("PlayerName", Component.Serializer.toJson(playerName));
        return stack;
    }

    public static boolean isBoy(ItemStack stack) {
        return stack.hasTag() ? stack.getTag().getBoolean("Boy") : true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        tooltipComponents.add(isBoy(stack) ? new TranslatableComponent("runecraftory.tooltip.baby.boy").withStyle(ChatFormatting.BLUE)
                : new TranslatableComponent("runecraftory.tooltip.baby.girl").withStyle(ChatFormatting.RED));
        Component name = this.getPlayerName(stack);
        if (name != null)
            tooltipComponents.add(new TranslatableComponent("runecraftory.tooltip.baby.owner", name).withStyle(ChatFormatting.GOLD));
    }

    public Component getPlayerName(ItemStack stack) {
        CompoundTag compoundTag = stack.getTag();
        if (compoundTag != null && compoundTag.contains("PlayerName")) {
            try {
                MutableComponent component = Component.Serializer.fromJson(compoundTag.getString("PlayerName"));
                if (component != null) {
                    return component;
                }
            } catch (Exception exception) {
            }
        }
        return null;
    }

    public Component getEntityName(ItemStack stack) {
        return stack.hasCustomHoverName() ? stack.getHoverName() : null;
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level world = ctx.getLevel();
        if (!(world instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack stack = ctx.getItemInHand();
            BlockPos blockpos = ctx.getClickedPos();
            Direction direction = ctx.getClickedFace();
            BlockState blockstate = world.getBlockState(blockpos);
            BlockPos blockpos1 = blockstate.getCollisionShape(world, blockpos).isEmpty() ? blockpos : blockpos.relative(direction);
            Entity e = this.spawnEntity((ServerLevel) world, ctx.getPlayer(), stack, blockpos1, MobSpawnType.SPAWN_EGG, true, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP);
            if (e != null) {
                stack.shrink(1);
            }
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult raytraceresult = getPlayerPOVHitResult(world, player, ClipContext.Fluid.SOURCE_ONLY);
        if (raytraceresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(stack);
        } else if (!(world instanceof ServerLevel)) {
            return InteractionResultHolder.success(stack);
        } else {
            BlockPos blockpos = raytraceresult.getBlockPos();
            if (!(world.getBlockState(blockpos).getBlock() instanceof LiquidBlock)) {
                return InteractionResultHolder.pass(stack);
            } else if (world.mayInteract(player, blockpos) && player.mayUseItemAt(blockpos, raytraceresult.getDirection(), stack)) {
                Entity e = this.spawnEntity((ServerLevel) world, player, stack, blockpos, MobSpawnType.SPAWN_EGG, true, true, false);
                if (e != null) {
                    if (!player.isCreative())
                        stack.shrink(1);
                    player.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResultHolder.consume(stack);
                }
                return InteractionResultHolder.pass(stack);
            } else {
                return InteractionResultHolder.fail(stack);
            }
        }
    }

    private Entity spawnEntity(ServerLevel world, Player player, ItemStack stack, BlockPos pos, MobSpawnType reason, boolean forgeCheck, boolean updateLocation, boolean doCollisionOffset) {
        CompoundTag tag = stack.getTag();
        if (tag == null)
            return null;
        EntityType<?> type = ModEntities.NPC.get();
        Component customName = this.getEntityName(stack);
        if (customName == null && tag.getBoolean("NeedsName")) {
            if (player != null)
                player.sendMessage(new TranslatableComponent("runecraftory.npc.spawn.name.missing"), Util.NIL_UUID);
            return null;
        }
        Entity e = type.create(world, tag, null, player, pos, reason, updateLocation, doCollisionOffset);
        if (e instanceof EntityNPCBase npc) {
            if (forgeCheck && EventCalls.INSTANCE.specialSpawnCall((Mob) e, world, pos.getX(), pos.getY(), pos.getZ(), null, reason))
                return null;
            npc.tryUpdateName(customName);
            npc.getFamily().setFather(tag.getUUID("Father"));
            npc.getFamily().setMother(tag.getUUID("Mother"));
            world.addFreshEntityWithPassengers(e);
            return e;
        }
        return null;
    }
}
