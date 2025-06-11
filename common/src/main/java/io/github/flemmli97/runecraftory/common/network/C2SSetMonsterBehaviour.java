package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModCriteria;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class C2SSetMonsterBehaviour implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<C2SSetMonsterBehaviour> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("c2s_monster_behaviour"));

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SSetMonsterBehaviour> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public C2SSetMonsterBehaviour decode(RegistryFriendlyByteBuf buf) {
            return new C2SSetMonsterBehaviour(buf.readInt(), buf.readEnum(Action.class));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, C2SSetMonsterBehaviour pkt) {
            buf.writeInt(pkt.id);
            buf.writeEnum(pkt.type);
        }
    };

    private final int id;
    private final Action type;

    public C2SSetMonsterBehaviour(int entityID, Action type) {
        this.id = entityID;
        this.type = type;
    }

    public static C2SSetMonsterBehaviour read(RegistryFriendlyByteBuf buf) {
        return new C2SSetMonsterBehaviour(buf.readInt(), buf.readEnum(Action.class));
    }

    public static void handle(C2SSetMonsterBehaviour pkt, ServerPlayer sender) {
        Entity entity = sender.level().getEntity(pkt.id);
        if (entity instanceof BaseMonster monster && sender.getUUID().equals(monster.getOwnerUUID())) {
            switch (pkt.type) {
                case HOME, FOLLOW, FOLLOW_DISTANCE, STAY, WANDER -> {
                    PlayerData data = Platform.INSTANCE.getPlayerData(sender);
                    if (pkt.type == Action.FOLLOW && !data.party.isPartyMember(entity) && data.party.isPartyFull()) {
                        sender.displayClientMessage(Component.translatable("runecraftory.monster.interact.party.full"), false);
                        return;
                    }
                    monster.setBehaviour(pkt.type.behaviour);
                    sender.displayClientMessage(Component.translatable(monster.behaviourState().interactKey, monster.getDisplayName()), false);
                }
                case FARM -> {
                    monster.setBehaviour(BaseMonster.Behaviour.FARM);
                    sender.displayClientMessage(Component.translatable(monster.behaviourState().interactKey, monster.getDisplayName()), false);
                    ModCriteria.COMMAND_FARMING.trigger(sender);
                }
                case CENTER, CENTER_FARM -> {
                    PlayerData data = Platform.INSTANCE.getPlayerData(sender);
                    data.entitySelector.selectedEntity = monster;
                    data.entitySelector.poi = monster.getRestrictCenter();
                    data.entitySelector.apply = (player, pos) -> {
                        monster.restrictToBasedOnBehaviour(pos, false);
                        data.entitySelector.poi = monster.getRestrictCenter();
                    };
                }
                case HARVESTINV -> {
                    PlayerData data = Platform.INSTANCE.getPlayerData(sender);
                    data.entitySelector.selectedEntity = monster;
                    data.entitySelector.poi = monster.getCropInventory();
                    data.entitySelector.apply = (player, pos) -> {
                        if (monster.isWithinRestriction(pos)) {
                            monster.setCropInventory(pos);
                            data.entitySelector.poi = monster.getCropInventory();
                            player.displayClientMessage(Component.translatable("runecraftory.behaviour.inventory.harvest"), false);
                        } else
                            player.displayClientMessage(Component.translatable("runecraftory.behaviour.inventory.harvest.invalid"), false);
                    };
                }
                case SEEDINV -> {
                    PlayerData data = Platform.INSTANCE.getPlayerData(sender);
                    data.entitySelector.selectedEntity = monster;
                    data.entitySelector.poi = monster.getSeedInventory();
                    data.entitySelector.apply = (player, pos) -> {
                        if (monster.isWithinRestriction(pos)) {
                            monster.setSeedInventory(pos);
                            data.entitySelector.poi = monster.getSeedInventory();
                            player.displayClientMessage(Component.translatable("runecraftory.behaviour.inventory.seed"), false);
                        } else
                            player.displayClientMessage(Component.translatable("runecraftory.behaviour.inventory.seed.invalid"), false);
                    };
                }
                case RIDE -> {
                    if (monster.behaviourState() == BaseMonster.Behaviour.FOLLOW || monster.behaviourState() == BaseMonster.Behaviour.FOLLOW_DISTANCE || monster.behaviourState() == BaseMonster.Behaviour.STAY)
                        monster.doStartRide(sender);
                }
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum Action {

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

        Action(String translation, BaseMonster.Behaviour behaviour) {
            this.translation = translation;
            this.behaviour = behaviour;
        }
    }
}
