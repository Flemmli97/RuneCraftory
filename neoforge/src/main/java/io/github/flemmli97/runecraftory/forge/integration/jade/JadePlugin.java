//package io.github.flemmli97.runecraftory.forge.integration.jade;
//
//import io.github.flemmli97.runecraftory.RuneCraftory;
//import io.github.flemmli97.runecraftory.common.attachment.player.LevelExpPair;
//import io.github.flemmli97.runecraftory.common.blocks.BlockMonsterBarn;
//import io.github.flemmli97.runecraftory.common.blocks.entity.MonsterBarnBlockEntity;
//import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
//import io.github.flemmli97.runecraftory.common.entities.MultiPartEntity;
//import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
//import io.github.flemmli97.runecraftory.common.entities.utils.IBaseMob;
//import io.github.flemmli97.runecraftory.common.registry.ModItems;
//import io.github.flemmli97.runecraftory.common.world.BarnData;
//import mcp.mobius.waila.api.Accessor;
//import mcp.mobius.waila.api.EntityAccessor;
//import mcp.mobius.waila.api.ITooltip;
//import mcp.mobius.waila.api.IWailaClientRegistration;
//import mcp.mobius.waila.api.IWailaCommonRegistration;
//import mcp.mobius.waila.api.IWailaPlugin;
//import mcp.mobius.waila.api.TooltipPosition;
//import mcp.mobius.waila.api.WailaPlugin;
//import mcp.mobius.waila.api.event.WailaRayTraceEvent;
//import mcp.mobius.waila.impl.ui.BorderStyle;
//import mcp.mobius.waila.impl.ui.ProgressElement;
//import mcp.mobius.waila.impl.ui.ProgressStyle;
//import net.minecraft.ChatFormatting;
//import net.minecraft.core.BlockPos;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.NbtOps;
//import net.minecraft.network.chat.Component;
//import net.minecraft.network.chat.MutableComponent;
//import net.minecraft.world.entity.Mob;
//import net.minecraft.world.entity.OwnableEntity;
//import net.neoforged.neoforge.common.UsernameCache;
//
//
//@SuppressWarnings("UnstableApiUsage")
//@WailaPlugin
//public class JadePlugin implements IWailaPlugin {
//
//    private static IWailaClientRegistration client;
//
//    @Override
//    public void register(IWailaCommonRegistration registration) {
//        registration.registerBlockDataProvider(((compoundTag, serverPlayer, level, blockEntity, b) -> {
//            if (blockEntity instanceof MonsterBarnBlockEntity barn) {
//                BarnData data = barn.getBarnData();
//                if (data != null) {
//                    compoundTag.putBoolean("Roof", data.hasRoof());
//                    compoundTag.putInt("RoofHeight", data.roofHeight());
//                    compoundTag.putInt("Size", data.getSize());
//                    compoundTag.putInt("Used", data.usedCapacity());
//                    compoundTag.putInt("Capacity", data.getCapacity());
//                }
//            }
//        }), MonsterBarnBlockEntity.class);
//        registration.registerEntityDataProvider((compoundTag, player, level, entity, b) -> {
//            if (entity instanceof IBaseMob mob && (player.getMainHandItem().getItem() == ModItems.DEBUG.get() || player.isCreative()
//                    || (entity instanceof OwnableEntity ownable && player.getUUID().equals(ownable.getOwnerUUID())))) {
//                LevelExpPair entityLevel = mob.xpLevel();
//                compoundTag.putFloat("RunecraftoryLevelPerc", entityLevel.getProgress());
//                compoundTag.putInt("RunecraftoryLevel", entityLevel.getLevel());
//            }
//            if (entity instanceof BaseMonster monster) {
//                if (monster.getOwnerUUID() != null) {
//                    String username = UsernameCache.getLastKnownUsername(monster.getOwnerUUID());
//                    if (username == null) {
//                        compoundTag.putBoolean("HasUsername", false);
//                    } else {
//                        compoundTag.putBoolean("HasUsername", true);
//                        compoundTag.putString("Username", username);
//                    }
//                    if (player.getUUID().equals(monster.getOwnerUUID())) {
//                        compoundTag.putInt("FP", monster.getFriendlyPoints().getLevel());
//                        BarnData barn = monster.getAssignedBarn();
//                        if (barn != null) {
//                            compoundTag.putBoolean("HasBarn", true);
//                            compoundTag.put("Barn", BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, barn.pos.pos()).getOrThrow(false, RuneCraftory.LOGGER::error));
//                        } else {
//                            compoundTag.putBoolean("HasBarn", false);
//                        }
//                        compoundTag.putString("Behaviour", monster.behaviourState().toString());
//                    }
//                }
//            }
//            if (entity instanceof EntityNPCBase npc) {
//                if (npc.followEntity() != null) {
//                    compoundTag.putString("NPCFollow", Component.Serializer.toJson(npc.followEntity().getDisplayName()));
//                }
//                compoundTag.putInt("FP", npc.friendPoints(player));
//            }
//        }, Mob.class);
//    }
//
//    public static void multipartHandler(WailaRayTraceEvent event) {
//        Accessor<?> accessor = event.getAccessor();
//        if (accessor instanceof EntityAccessor entityAccessor) {
//            if (entityAccessor.getEntity() instanceof MultiPartEntity entity) {
//                accessor = client.createEntityAccessor(
//                        entity.getOwner(),
//                        accessor.getLevel(),
//                        accessor.getPlayer(),
//                        accessor.getServerData(),
//                        entityAccessor.getHitResult(),
//                        accessor.isServerConnected()
//                );
//                event.setAccessor(accessor);
//            }
//        }
//    }
//
//    @Override
//    public void registerClient(IWailaClientRegistration registration) {
//        client = registration;
//        registration.registerComponentProvider((iTooltip, blockAccessor, iPluginConfig) -> {
//            CompoundTag tag = blockAccessor.getServerData();
//            if (blockAccessor.getBlockEntity() instanceof MonsterBarnBlockEntity) {
//                int size = tag.getInt("Size");
//                Component sizeText = size > 1 ? Component.literal("" + size).withStyle(ChatFormatting.GREEN)
//                        : Component.literal("" + size).withStyle(ChatFormatting.DARK_RED);
//                if (!tag.getBoolean("Roof")) {
//                    iTooltip.add(Component.translatable("runecraftory.dependency.tooltips.barn.1",
//                            sizeText));
//                } else {
//                    iTooltip.add(Component.translatable("runecraftory.dependency.tooltips.barn.1.alt", Component.translatable("" + tag.getInt("RoofHeight")).withStyle(ChatFormatting.YELLOW),
//                            sizeText));
//                }
//                iTooltip.add(Component.translatable("runecraftory.dependency.tooltips.barn.2", tag.getInt("Used"), tag.getInt("Capacity")));
//            }
//        }, TooltipPosition.BODY, BlockMonsterBarn.class);
//        registration.registerComponentProvider((iTooltip, entityAccessor, iPluginConfig) -> {
//            CompoundTag tag = entityAccessor.getServerData();
//            if (tag.contains("RunecraftoryLevel")) {
//                float prog = tag.getFloat("RunecraftoryLevelPerc");
//                int lvl = tag.getInt("RunecraftoryLevel");
//                iTooltip.add(new ProgressElement(prog, Component.translatable("runecraftory.tooltip.item.level", lvl),
//                        (ProgressStyle) new ProgressStyle().color(0xff0c8995, 0xff0c8995),
//                        (BorderStyle) new BorderStyle().color(0xff000000)));
//            }
//            if (entityAccessor.getEntity() instanceof BaseMonster monster) {
//                if (monster.getOwnerUUID() != null) {
//                    if (!tag.getBoolean("HasUsername")) {
//                        iTooltip.add(Component.translatable("runecraftory.dependency.tooltips.owner.none").withStyle(ChatFormatting.YELLOW));
//                    } else {
//                        String username = tag.getString("Username");
//                        iTooltip.add(Component.translatable("runecraftory.dependency.tooltips.owner", username).withStyle(ChatFormatting.GOLD));
//                    }
//                    if (entityAccessor.getPlayer().getUUID().equals(monster.getOwnerUUID())) {
//                        withText(iTooltip, "runecraftory.dependency.tooltips.friendpoints", Component.literal("" + tag.getInt("FP")), ChatFormatting.YELLOW);
//                        if (tag.getBoolean("HasBarn")) {
//                            BlockPos pos = BlockPos.CODEC.parse(NbtOps.INSTANCE, tag.get("Barn")).getOrThrow(false, RuneCraftory.LOGGER::error);
//                            withText(iTooltip, "runecraftory.dependency.tooltips.barn", Component.literal(String.format("[%s, %s, %s]", pos.getX(), pos.getY(), pos.getZ())), ChatFormatting.GREEN);
//                        } else {
//                            iTooltip.add(Component.translatable("runecraftory.dependency.tooltips.barn.no").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
//                        }
//                        withText(iTooltip, "runecraftory.dependency.tooltips.behaviour", Component.literal(tag.getString("Behaviour")), ChatFormatting.YELLOW);
//                    }
//                }
//            }
//            if (entityAccessor.getEntity() instanceof EntityNPCBase) {
//                if (tag.contains("NPCFollow")) {
//                    withText(iTooltip, "runecraftory.dependency.tooltips.npc.follow", Component.Serializer.fromJson(tag.getString("NPCFollow")), ChatFormatting.YELLOW);
//                }
//                withText(iTooltip, "runecraftory.dependency.tooltips.friendpoints", Component.literal("" + tag.getInt("FP")), ChatFormatting.YELLOW);
//            }
//        }, TooltipPosition.BODY, Mob.class);
//    }
//
//    private static void withText(ITooltip tooltip, String key, MutableComponent other, ChatFormatting formatting, ChatFormatting... main) {
//        tooltip.add(Component.translatable(key, other.withStyle(formatting)).withStyle(main));
//    }
//}
