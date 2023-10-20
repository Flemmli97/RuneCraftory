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
                    case HOME, FOLLOW, FOLLOW_DISTANCE, STAY, WANDER -> {
                        if (pkt.type == Type.FOLLOW && Platform.INSTANCE.getPlayerData(sender).map(d -> !d.party.isPartyMember(entity) && d.party.isPartyFull()).orElse(true)) {
                            sender.sendMessage(new TranslatableComponent("runecraftory.monster.interact.party.full"), Util.NIL_UUID);
                            return;
                        }
                        monster.setBehaviour(pkt.type.behaviour);
                        sender.sendMessage(new TranslatableComponent(monster.behaviourState().interactKey, monster.getDisplayName()), Util.NIL_UUID);
                    }
                    case FARM -> {
                        monster.setBehaviour(BaseMonster.Behaviour.FARM);
                        sender.sendMessage(new TranslatableComponent(monster.behaviourState().interactKey, monster.getDisplayName()), Util.NIL_UUID);
                        ModCriteria.COMMAND_FARMING.trigger(sender);
                    }
                    case CENTER, CENTER_FARM -> Platform.INSTANCE.getPlayerData(sender)
                            .ifPresent(data -> {
                                data.entitySelector.selectedEntity = monster;
                                data.entitySelector.poi = monster.getRestrictCenter();
                                data.entitySelector.apply = (player, pos) -> {
                                    monster.restrictToBasedOnBehaviour(pos, false);
                                    data.entitySelector.poi = monster.getRestrictCenter();
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
                                        player.sendMessage(new TranslatableComponent("runecraftory.behaviour.inventory.harvest"), Util.NIL_UUID);
                                    } else
                                        player.sendMessage(new TranslatableComponent("runecraftory.behaviour.inventory.harvest.invalid"), Util.NIL_UUID);
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
                                        player.sendMessage(new TranslatableComponent("runecraftory.behaviour.inventory.seed"), Util.NIL_UUID);
                                    } else
                                        player.sendMessage(new TranslatableComponent("runecraftory.behaviour.inventory.seed.invalid"), Util.NIL_UUID);
                                };
                            });
                    case RIDE -> {
                        if (monster.behaviourState() == BaseMonster.Behaviour.FOLLOW || monster.behaviourState() == BaseMonster.Behaviour.FOLLOW_DISTANCE || monster.behaviourState() == BaseMonster.Behaviour.STAY)
                            monster.doStartRide(sender);
                    }
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

        HOME("runecraftory.gui.companion.behaviour.home", BaseMonster.Behaviour.WANDER_HOME),
        FOLLOW("runecraftory.gui.companion.behaviour.follow", BaseMonster.Behaviour.FOLLOW),
        FOLLOW_DISTANCE("runecraftory.gui.companion.behaviour.follow_distance", BaseMonster.Behaviour.FOLLOW_DISTANCE),
        STAY("runecraftory.gui.companion.behaviour.stay", BaseMonster.Behaviour.STAY),
        WANDER("runecraftory.gui.companion.behaviour.wander", BaseMonster.Behaviour.WANDER),
        FARM("runecraftory.gui.companion.behaviour.farm", BaseMonster.Behaviour.FARM),
        HARVESTINV("runecraftory.gui.companion.behaviour.harvest", BaseMonster.Behaviour.FARM),
        SEEDINV("runecraftory.gui.companion.behaviour.seed", BaseMonster.Behaviour.FARM),
        RIDE("runecraftory.gui.companion.behaviour.ride", BaseMonster.Behaviour.FOLLOW),
        CENTER("runecraftory.gui.companion.behaviour.center", BaseMonster.Behaviour.WANDER),
        CENTER_FARM("runecraftory.gui.companion.behaviour.center", BaseMonster.Behaviour.FARM);

        public final String translation;
        public final BaseMonster.Behaviour behaviour;

        Type(String translation, BaseMonster.Behaviour behaviour) {
            this.translation = translation;
            this.behaviour = behaviour;
        }
    }
}
