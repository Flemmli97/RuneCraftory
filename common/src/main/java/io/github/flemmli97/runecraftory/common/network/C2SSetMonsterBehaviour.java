package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModCriteria;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class C2SSetMonsterBehaviour implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "c2s_monster_behaviour");

    private final int id;
    private final Type type;

    public C2SSetMonsterBehaviour(int entityID, Type type) {
        this.id = entityID;
        this.type = type;
    }

    public static C2SSetMonsterBehaviour read(FriendlyByteBuf buf) {
        return new C2SSetMonsterBehaviour(buf.readInt(), buf.readEnum(Type.class));
    }

    public static void handle(C2SSetMonsterBehaviour pkt, ServerPlayer sender) {
        if (sender != null) {
            Entity entity = sender.level.getEntity(pkt.id);
            if (entity instanceof BaseMonster monster && sender.getUUID().equals(monster.getOwnerUUID())) {
                switch (pkt.type) {
                    case WANDER -> {
                        monster.setBehaviour(BaseMonster.Behaviour.WANDER);
                        sender.sendMessage(new TranslatableComponent(monster.behaviourState().interactKey), Util.NIL_UUID);
                    }
                    case FOLLOW -> {
                        monster.setBehaviour(BaseMonster.Behaviour.FOLLOW);
                        sender.sendMessage(new TranslatableComponent(monster.behaviourState().interactKey), Util.NIL_UUID);
                    }
                    case STAY -> {
                        monster.setBehaviour(BaseMonster.Behaviour.STAY);
                        sender.sendMessage(new TranslatableComponent(monster.behaviourState().interactKey), Util.NIL_UUID);
                    }
                    case FARM -> {
                        monster.setBehaviour(BaseMonster.Behaviour.FARM);
                        sender.sendMessage(new TranslatableComponent(monster.behaviourState().interactKey), Util.NIL_UUID);
                        ModCriteria.COMMAND_FARMING.trigger(sender);
                    }
                    case HOME -> Platform.INSTANCE.getPlayerData(sender)
                            .ifPresent(data -> {
                                data.entitySelector.selectedEntity = monster;
                                data.entitySelector.poi = monster.getRestrictCenter();
                                data.entitySelector.apply = (player, pos) -> {
                                    monster.restrictToBasedOnBehaviour(pos);
                                    data.entitySelector.poi = monster.getRestrictCenter();
                                    player.sendMessage(new TranslatableComponent("behaviour.home.position"), Util.NIL_UUID);
                                };
                            });
                    case HARVESTINV -> Platform.INSTANCE.getPlayerData(sender)
                            .ifPresent(data -> {
                                data.entitySelector.selectedEntity = monster;
                                data.entitySelector.poi = monster.getCropInventory();
                                data.entitySelector.apply = (player, pos) -> {
                                    if (monster.isWithinRestriction(pos)) {
                                        monster.setCropInventory(pos);
                                        data.entitySelector.poi = monster.getCropInventory();
                                        player.sendMessage(new TranslatableComponent("behaviour.inventory.harvest"), Util.NIL_UUID);
                                    } else
                                        player.sendMessage(new TranslatableComponent("behaviour.inventory.harvest.invalid"), Util.NIL_UUID);
                                };
                            });
                    case SEEDINV -> Platform.INSTANCE.getPlayerData(sender)
                            .ifPresent(data -> {
                                data.entitySelector.selectedEntity = monster;
                                data.entitySelector.poi = monster.getSeedInventory();
                                data.entitySelector.apply = (player, pos) -> {
                                    if (monster.isWithinRestriction(pos)) {
                                        monster.setSeedInventory(pos);
                                        data.entitySelector.poi = monster.getSeedInventory();
                                        player.sendMessage(new TranslatableComponent("behaviour.inventory.seed"), Util.NIL_UUID);
                                    } else
                                        player.sendMessage(new TranslatableComponent("behaviour.inventory.seed.invalid"), Util.NIL_UUID);
                                };
                            });
                }
            }
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.id);
        buf.writeEnum(this.type);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public enum Type {
        WANDER,
        FOLLOW,
        STAY,
        FARM,
        HOME,
        HARVESTINV,
        SEEDINV
    }
}
