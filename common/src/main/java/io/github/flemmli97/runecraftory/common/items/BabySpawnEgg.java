package io.github.flemmli97.runecraftory.common.items;

import io.github.flemmli97.runecraftory.common.components.BabyData;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModDataComponentTypes;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
        stack.set(DataComponents.ENTITY_DATA, CustomData.of(tag));
        stack.set(ModDataComponentTypes.BABY_DATA.get(), new BabyData(baby.isMale(), baby.getDataName(), father, mother, Optional.of(playerName)));
        return stack;
    }

    public static boolean isBoy(ItemStack stack) {
        return stack.getOrDefault(ModDataComponentTypes.BABY_DATA.get(), BabyData.DEFAULT).male();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, list, tooltipFlag);
        list.add(isBoy(stack) ? Component.translatable("runecraftory.tooltip.baby.boy").withStyle(ChatFormatting.BLUE)
                : Component.translatable("runecraftory.tooltip.baby.girl").withStyle(ChatFormatting.RED));
        this.getPlayerName(stack).ifPresent(name -> list.add(Component.translatable("runecraftory.tooltip.baby.owner", name)
                .withStyle(ChatFormatting.GOLD)));
    }

    public Optional<Component> getPlayerName(ItemStack stack) {
        return stack.getOrDefault(ModDataComponentTypes.BABY_DATA.get(), BabyData.DEFAULT).player();
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

    private Entity spawnEntity(ServerLevel level, Player player, ItemStack stack, BlockPos pos, MobSpawnType spawnType, boolean forgeCheck, boolean updateLocation, boolean doCollisionOffset) {
        BabyData data = stack.get(ModDataComponentTypes.BABY_DATA.get());
        if (data == null)
            return null;
        EntityType<?> type = ModEntities.NPC.get();
        if (data.name().isEmpty()) {
            if (player != null)
                player.displayClientMessage(Component.translatable("runecraftory.npc.spawn.name.missing")
                        .withStyle(ChatFormatting.RED), false);
            return null;
        }
        Entity e = type.create(level, EntityType.createDefaultStackConfig(level, stack, player), pos, spawnType, updateLocation, doCollisionOffset);
        if (e instanceof EntityNPCBase npc) {
            if (forgeCheck && EventCalls.INSTANCE.specialSpawnCall((Mob) e, level, pos.getX(), pos.getY(), pos.getZ(), null, spawnType))
                return null;
            npc.tryUpdateName(Component.literal(data.name().get()));
            npc.getFamily().setFather(data.father());
            npc.getFamily().setMother(data.mother());
            level.addFreshEntityWithPassengers(e);
            return e;
        }
        return null;
    }
}
