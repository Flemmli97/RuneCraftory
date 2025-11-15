package io.github.flemmli97.runecraftory.integration.jade;

import com.mojang.authlib.GameProfile;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.attachment.player.XpLevelHolder;
import io.github.flemmli97.runecraftory.common.blocks.MonsterBarnBlock;
import io.github.flemmli97.runecraftory.common.blocks.TreeBaseBlock;
import io.github.flemmli97.runecraftory.common.blocks.entity.MonsterBarnBlockEntity;
import io.github.flemmli97.runecraftory.common.blocks.entity.TreeBlockEntity;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.entities.utils.IBaseMob;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.runecraftory.common.world.data.BarnData;
import io.github.flemmli97.runecraftory.mixin.AttributeMapAccessor;
import io.github.flemmli97.tenshilib.common.entity.MultiPartEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.player.Player;
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
import snownee.jade.impl.ui.SimpleProgressStyle;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {

    private static final ResourceLocation ID = RuneCraftory.modRes("jade_entity_plugin");
    private static final ResourceLocation BARN_BLOCK_PLUGIN = RuneCraftory.modRes("jade_barn_block_plugin");
    private static final ResourceLocation TREE_BLOCK_PLUGIN = RuneCraftory.modRes("jade_tree_block_plugin");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(new IServerDataProvider<>() {
            @Override
            public void appendServerData(CompoundTag compoundTag, BlockAccessor accessor) {
                if (accessor.getBlockEntity() instanceof MonsterBarnBlockEntity barn) {
                    BarnData data = barn.getBarnData();
                    if (data != null) {
                        compoundTag.putBoolean("Roof", data.hasRoof());
                        compoundTag.putInt("RoofHeight", data.roofHeight());
                        compoundTag.putInt("Size", data.getSize());
                        compoundTag.putInt("Used", data.usedCapacity());
                        compoundTag.putInt("Capacity", data.getCapacity());
                    }
                }
            }

            @Override
            public ResourceLocation getUid() {
                return BARN_BLOCK_PLUGIN;
            }
        }, MonsterBarnBlockEntity.class);
        registration.registerBlockDataProvider(new IServerDataProvider<>() {
            @Override
            public void appendServerData(CompoundTag compoundTag, BlockAccessor accessor) {
                if (accessor.getBlockEntity() instanceof TreeBlockEntity tree) {
                    compoundTag.putInt("Health", tree.getHealth());
                }
            }

            @Override
            public ResourceLocation getUid() {
                return TREE_BLOCK_PLUGIN;
            }
        }, TreeBaseBlock.class);
        registration.registerEntityDataProvider(new IServerDataProvider<>() {
            @Override
            public void appendServerData(CompoundTag compoundTag, EntityAccessor accessor) {
                Entity entity = accessor.getEntity();
                Player player = accessor.getPlayer();
                if (entity instanceof IBaseMob mob && (player.getMainHandItem().getItem() == RuneCraftoryItems.DEBUG.get() || player.isCreative()
                        || (entity instanceof OwnableEntity ownable && player.getUUID().equals(ownable.getOwnerUUID())))) {
                    XpLevelHolder entityLevel = mob.xpLevel();
                    compoundTag.putFloat("RunecraftoryLevelPerc", entityLevel.getProgress());
                    compoundTag.putInt("RunecraftoryLevel", entityLevel.getLevel());
                }
                if (entity instanceof BaseMonster mob && player.getMainHandItem().getItem() == RuneCraftoryItems.DEBUG.get()) {
                    compoundTag.put("Attributes", mob.getAttributes().save());
                }
                if (entity instanceof BaseMonster monster) {
                    if (monster.getOwnerUUID() != null) {
                        String username = player.getServer().getProfileCache().get(monster.getOwnerUUID())
                                .map(GameProfile::getName).orElse(null);
                        if (username == null) {
                            compoundTag.putBoolean("HasUsername", false);
                        } else {
                            compoundTag.putBoolean("HasUsername", true);
                            compoundTag.putString("Username", username);
                        }
                        if (player.getUUID().equals(monster.getOwnerUUID())) {
                            compoundTag.putInt("FP", monster.friendPoints(player));
                            BarnData barn = monster.getAssignedBarn();
                            if (barn != null) {
                                compoundTag.putBoolean("HasBarn", true);
                                compoundTag.put("Barn", BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, barn.pos.pos()).getOrThrow());
                            } else {
                                compoundTag.putBoolean("HasBarn", false);
                            }
                            compoundTag.putString("Behaviour", monster.behaviourState().toString());
                        }
                    }
                }
                if (entity instanceof NPCEntity npc) {
                    if (npc.followEntity() != null) {
                        compoundTag.put("NPCFollow", ComponentSerialization.CODEC.encodeStart(NbtOps.INSTANCE, npc.followEntity().getDisplayName()).getOrThrow());
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
                    Component sizeText = size > 1 ? Component.literal("" + size).withStyle(ChatFormatting.GREEN)
                            : Component.literal("" + size).withStyle(ChatFormatting.DARK_RED);
                    if (!tag.getBoolean("Roof")) {
                        iTooltip.add(Component.translatable("runecraftory.dependency.tooltips.barn.1",
                                sizeText));
                    } else {
                        iTooltip.add(Component.translatable("runecraftory.dependency.tooltips.barn.1.alt", Component.translatable("" + tag.getInt("RoofHeight")).withStyle(ChatFormatting.YELLOW),
                                sizeText));
                    }
                    iTooltip.add(Component.translatable("runecraftory.dependency.tooltips.barn.2", tag.getInt("Used"), tag.getInt("Capacity")));
                }
            }

            @Override
            public ResourceLocation getUid() {
                return BARN_BLOCK_PLUGIN;
            }
        }, MonsterBarnBlock.class);
        registration.registerBlockComponent(new IBlockComponentProvider() {
            @Override
            public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
                CompoundTag tag = blockAccessor.getServerData();
                if (blockAccessor.getBlockEntity() instanceof TreeBlockEntity) {
                    iTooltip.add(Component.translatable("runecraftory.dependency.tooltips.tree", tag.getInt("Health")));
                }
            }

            @Override
            public ResourceLocation getUid() {
                return TREE_BLOCK_PLUGIN;
            }
        }, TreeBaseBlock.class);
        registration.addRayTraceCallback((hitResult, accessor, origin) -> {
            if (accessor instanceof EntityAccessor entityAccessor) {
                if (entityAccessor.getEntity() instanceof MultiPartEntity entity && BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).getNamespace().equals(RuneCraftory.MODID)) {
                    accessor = registration.entityAccessor().from(entityAccessor).entity(entity.getOwner()).build();
                    return accessor;
                }
            }
            return accessor;
        });
        registration.registerEntityComponent(new IEntityComponentProvider() {

            @SuppressWarnings("unchecked")
            @Override
            public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
                CompoundTag tag = entityAccessor.getServerData();
                if (tag.contains("RunecraftoryLevel")) {
                    float prog = tag.getFloat("RunecraftoryLevelPerc");
                    int lvl = tag.getInt("RunecraftoryLevel");
                    iTooltip.add(new ProgressElement(prog, Component.translatable("runecraftory.tooltip.item.level", lvl)
                            .withStyle(ChatFormatting.BOLD, ChatFormatting.YELLOW),
                            new SimpleProgressStyle().color(0xff3e9b1e, 0xff58af3b),
                            BoxStyle.getNestedBox(), true));
                }
                if (entityAccessor.getEntity() instanceof BaseMonster monster) {
                    if (monster.getOwnerUUID() != null) {
                        if (!tag.getBoolean("HasUsername")) {
                            iTooltip.add(Component.translatable("runecraftory.dependency.tooltips.owner.none").withStyle(ChatFormatting.YELLOW));
                        } else {
                            String username = tag.getString("Username");
                            iTooltip.add(Component.translatable("runecraftory.dependency.tooltips.owner", username).withStyle(ChatFormatting.GOLD));
                        }
                        if (entityAccessor.getPlayer().getUUID().equals(monster.getOwnerUUID())) {
                            withText(iTooltip, "runecraftory.dependency.tooltips.friendpoints", Component.literal("" + tag.getInt("FP")), ChatFormatting.YELLOW);
                            if (tag.getBoolean("HasBarn")) {
                                BlockPos pos = BlockPos.CODEC.parse(NbtOps.INSTANCE, tag.get("Barn")).getOrThrow();
                                withText(iTooltip, "runecraftory.dependency.tooltips.barn", Component.literal(String.format("[%s, %s, %s]", pos.getX(), pos.getY(), pos.getZ())), ChatFormatting.GREEN);
                            } else {
                                iTooltip.add(Component.translatable("runecraftory.dependency.tooltips.barn.no").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
                            }
                            withText(iTooltip, "runecraftory.dependency.tooltips.behaviour", Component.literal(tag.getString("Behaviour")), ChatFormatting.YELLOW);
                        }
                    }
                }
                if (tag.contains("Attributes") && entityAccessor.getEntity() instanceof BaseMonster m) {
                    AttributeMap map = new AttributeMap(DefaultAttributes.getSupplier((EntityType<? extends LivingEntity>) m.getType()));
                    map.load(tag.getList("Attributes", Tag.TAG_COMPOUND));
                    ((AttributeMapAccessor) map)
                            .getAttributes()
                            .values().stream().sorted((inst, inst2) -> RuneCraftoryAttributes.SORTED.compare(inst.getAttribute(), inst2.getAttribute()))
                            .forEach(inst -> iTooltip.add(Component.translatable("runecraftory.tooltip.item.attribute",
                                    Component.translatable(inst.getAttribute().value().getDescriptionId()), inst.getValue()).withStyle(ChatFormatting.GOLD)));
                }
                if (entityAccessor.getEntity() instanceof NPCEntity) {
                    if (tag.contains("NPCFollow")) {
                        withText(iTooltip, "runecraftory.dependency.tooltips.npc.follow", ComponentSerialization.CODEC.parse(NbtOps.INSTANCE, tag.get("NPCFollow")).getOrThrow(), ChatFormatting.YELLOW);
                    }
                    withText(iTooltip, "runecraftory.dependency.tooltips.friendpoints", Component.literal("" + tag.getInt("FP")), ChatFormatting.YELLOW);
                }
            }

            @Override
            public ResourceLocation getUid() {
                return ID;
            }
        }, Mob.class);
    }

    private static void withText(ITooltip tooltip, String key, Component other, ChatFormatting formatting, ChatFormatting... main) {
        tooltip.add(Component.translatable(key, other instanceof MutableComponent mut ? mut.withStyle(formatting) : other).withStyle(main));
    }
}
