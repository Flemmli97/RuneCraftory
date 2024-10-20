package io.github.flemmli97.runecraftory.fabric.integration.jade;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.attachment.player.LevelExpPair;
import io.github.flemmli97.runecraftory.common.blocks.BlockMonsterBarn;
import io.github.flemmli97.runecraftory.common.blocks.tile.MonsterBarnBlockEntity;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.IBaseMob;
import io.github.flemmli97.runecraftory.common.entities.MultiPartEntity;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.world.BarnData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.impl.ui.ProgressElement;
import snownee.jade.impl.ui.ProgressStyle;
import snownee.jade.util.UsernameCache;

@SuppressWarnings("UnstableApiUsage")
@WailaPlugin
public class JadePlugin implements IWailaPlugin {

    private static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "jade_entity_plugin");
    private static final ResourceLocation IDBLOCK = new ResourceLocation(RuneCraftory.MODID, "jade_block_plugin");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(new IServerDataProvider<>() {
            @Override
            public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
                if (blockEntity instanceof MonsterBarnBlockEntity barn) {
                    BarnData data = barn.getBarnData();
                    if (data != null) {
                        compoundTag.putBoolean("Roof", data.hasRoof());
                        compoundTag.putInt("Size", data.getSize());
                        compoundTag.putInt("Used", data.usedCapacity());
                        compoundTag.putInt("Capacity", data.getCapacity());
                    }
                }
            }

            @Override
            public ResourceLocation getUid() {
                return IDBLOCK;
            }
        }, MonsterBarnBlockEntity.class);
        registration.registerEntityDataProvider(new IServerDataProvider<>() {
            @Override
            public void appendServerData(CompoundTag compoundTag, ServerPlayer player, Level level, Entity entity, boolean b) {
                if (entity instanceof IBaseMob mob && (player.getMainHandItem().getItem() == ModItems.DEBUG.get() || player.isCreative()
                        || (entity instanceof OwnableEntity ownable && player.getUUID().equals(ownable.getOwnerUUID())))) {
                    LevelExpPair entityLevel = mob.level();
                    compoundTag.putFloat("RunecraftoryLevelPerc", entityLevel.getProgress());
                    compoundTag.putInt("RunecraftoryLevel", entityLevel.getLevel());
                }
                if (entity instanceof BaseMonster monster) {
                    if (monster.getOwnerUUID() != null) {
                        String username = UsernameCache.getLastKnownUsername(monster.getOwnerUUID());
                        if (username == null) {
                            compoundTag.putBoolean("HasUsername", false);
                        } else {
                            compoundTag.putBoolean("HasUsername", true);
                            compoundTag.putString("Username", username);
                        }
                        if (player.getUUID().equals(monster.getOwnerUUID())) {
                            compoundTag.putInt("FP", monster.getFriendlyPoints().getLevel());
                            BarnData barn = monster.getAssignedBarn();
                            if (barn != null) {
                                compoundTag.putBoolean("HasBarn", true);
                                compoundTag.put("Barn", BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, barn.pos.pos()).getOrThrow(false, RuneCraftory.LOGGER::error));
                            } else {
                                compoundTag.putBoolean("HasBarn", false);
                            }
                            compoundTag.putString("Behaviour", monster.behaviourState().toString());
                        }
                    }
                }
                if (entity instanceof EntityNPCBase npc) {
                    if (npc.followEntity() != null) {
                        compoundTag.putString("NPCFollow", Component.Serializer.toJson(npc.followEntity().getDisplayName()));
                    }
                    compoundTag.putInt("FP", npc.friendPoints(player));
                }
            }

            @Override
            public ResourceLocation getUid() {
                return ID;
            }
        }, Mob.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new IBlockComponentProvider() {
            @Override
            public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
                CompoundTag tag = blockAccessor.getServerData();
                if (blockAccessor.getBlockEntity() instanceof MonsterBarnBlockEntity) {
                    int size = tag.getInt("Size");
                    Component sizeText = size > 1 ? new TextComponent("" + size).withStyle(ChatFormatting.GREEN)
                            : new TextComponent("" + size).withStyle(ChatFormatting.DARK_RED);
                    iTooltip.add(new TranslatableComponent("runecraftory.dependency.tooltips.barn.1", new TranslatableComponent(tag.getBoolean("Roof") ? "runecraftory.generic.yes" : "runecraftory.generic.no").withStyle(ChatFormatting.YELLOW),
                            sizeText));
                    iTooltip.add(new TranslatableComponent("runecraftory.dependency.tooltips.barn.2", tag.getInt("Used"), tag.getInt("Capacity")));
                }
            }

            @Override
            public ResourceLocation getUid() {
                return IDBLOCK;
            }
        }, BlockMonsterBarn.class);
        registration.addRayTraceCallback((hitResult, accessor, origin) -> {
            if (accessor instanceof EntityAccessor entityAccessor) {
                if (entityAccessor.getEntity() instanceof MultiPartEntity entity) {
                    accessor = registration.entityAccessor().from(entityAccessor).entity(entity.getOwner()).build();
                    return accessor;
                }
            }
            return accessor;
        });
        BoxStyle box = new BoxStyle();
        box.borderColor = 0xff000000;
        registration.registerEntityComponent(new IEntityComponentProvider() {
            @Override
            public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
                CompoundTag tag = entityAccessor.getServerData();
                if (tag.contains("RunecraftoryLevel")) {
                    float prog = tag.getFloat("RunecraftoryLevelPerc");
                    int lvl = tag.getInt("RunecraftoryLevel");
                    iTooltip.add(new ProgressElement(prog, new TranslatableComponent("runecraftory.tooltip.item.level", lvl),
                            new ProgressStyle().color(0xff0c8995, 0xff0c8995),
                            box, true));
                }
                if (entityAccessor.getEntity() instanceof BaseMonster monster) {
                    if (monster.getOwnerUUID() != null) {
                        if (!tag.getBoolean("HasUsername")) {
                            iTooltip.add(new TranslatableComponent("runecraftory.dependency.tooltips.owner.none").withStyle(ChatFormatting.YELLOW));
                        } else {
                            String username = tag.getString("Username");
                            iTooltip.add(new TranslatableComponent("runecraftory.dependency.tooltips.owner", username).withStyle(ChatFormatting.GOLD));
                        }
                        if (entityAccessor.getPlayer().getUUID().equals(monster.getOwnerUUID())) {
                            withText(iTooltip, "runecraftory.dependency.tooltips.friendpoints", new TextComponent("" + tag.getInt("FP")), ChatFormatting.YELLOW);
                            if (tag.getBoolean("HasBarn")) {
                                BlockPos pos = BlockPos.CODEC.parse(NbtOps.INSTANCE, tag.get("Barn")).getOrThrow(false, RuneCraftory.LOGGER::error);
                                withText(iTooltip, "runecraftory.dependency.tooltips.barn", new TextComponent(String.format("[%s, %s, %s]", pos.getX(), pos.getY(), pos.getZ())), ChatFormatting.GREEN);
                            } else {
                                iTooltip.add(new TranslatableComponent("runecraftory.dependency.tooltips.barn.no").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
                            }
                            withText(iTooltip, "runecraftory.dependency.tooltips.behaviour", new TextComponent(tag.getString("Behaviour")), ChatFormatting.YELLOW);
                        }
                    }
                }
                if (entityAccessor.getEntity() instanceof EntityNPCBase) {
                    if (tag.contains("NPCFollow")) {
                        withText(iTooltip, "runecraftory.dependency.tooltips.npc.follow", Component.Serializer.fromJson(tag.getString("NPCFollow")), ChatFormatting.YELLOW);
                    }
                    withText(iTooltip, "runecraftory.dependency.tooltips.friendpoints", new TextComponent("" + tag.getInt("FP")), ChatFormatting.YELLOW);
                }
            }

            @Override
            public ResourceLocation getUid() {
                return ID;
            }
        }, Mob.class);
    }

    private static void withText(ITooltip tooltip, String key, MutableComponent other, ChatFormatting formatting, ChatFormatting... main) {
        tooltip.add(new TranslatableComponent(key, other.withStyle(formatting)).withStyle(main));
    }
}
