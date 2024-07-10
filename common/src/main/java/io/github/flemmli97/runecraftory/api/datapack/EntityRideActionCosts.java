package io.github.flemmli97.runecraftory.api.datapack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EntityRideActionCosts {

    public static final Codec<EntityRideActionCosts> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    RideActionCost.CODEC.fieldOf("command_1").forGetter(d -> d.command1),
                    RideActionCost.CODEC.optionalFieldOf("command_2").forGetter(d -> Optional.ofNullable(d.command2)),
                    RideActionCost.CODEC.optionalFieldOf("command_3").forGetter(d -> Optional.ofNullable(d.command3)),
                    RideActionCost.CODEC.optionalFieldOf("command_4").forGetter(d -> Optional.ofNullable(d.command4))
            ).apply(instance, (c1, c2, c3, c4) -> new EntityRideActionCosts(c1, c2.orElse(null), c3.orElse(null), c4.orElse(null))));

    public static final EntityRideActionCosts DEFAULT = new EntityRideActionCosts.Builder(0.5f, true).build();

    private final RideActionCost command1;
    private final RideActionCost command2;
    private final RideActionCost command3;
    private final RideActionCost command4;

    private EntityRideActionCosts(RideActionCost command1, RideActionCost command2, RideActionCost command3, RideActionCost command4) {
        this.command1 = command1;
        this.command2 = command2;
        this.command3 = command3;
        this.command4 = command4;
    }

    public boolean canRun(int command, Entity entity, @Nullable Spell spell) {
        RideActionCost cost = this.getCost(command);
        return !(entity instanceof ServerPlayer player) || Platform.INSTANCE.getPlayerData(player)
                .map(data -> {
                    if (spell == null || !cost.multiplier) {
                        if (cost.multiplier)
                            return true;
                        if (!LevelCalc.useRP(player, data, cost.cost, false, false, false)) {
                            player.connection.send(
                                    new ClientboundSoundPacket(SoundEvents.VILLAGER_NO, SoundSource.PLAYERS, player.position().x, player.position().y, player.position().z, 1, 1));
                            return false;
                        }
                        return true;
                    }
                    return Spell.tryUseWithCost(player, ItemStack.EMPTY, spell, cost.cost);
                }).orElse(false);
    }

    private RideActionCost getCost(int command) {
        RideActionCost cost = switch (command) {
            case 3 -> this.command4;
            case 2 -> this.command3;
            case 1 -> this.command2;
            default -> this.command1;
        };
        if (cost == null)
            cost = this.command1;
        return cost;
    }

    record RideActionCost(float cost, boolean multiplier) {
        public static final Codec<RideActionCost> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(
                        Codec.FLOAT.fieldOf("cost").forGetter(d -> d.cost),
                        Codec.BOOL.fieldOf("multiplier").forGetter(d -> d.multiplier)
                ).apply(instance, RideActionCost::new));
    }

    public static class Builder {

        private final RideActionCost command1;
        private RideActionCost command2;
        private RideActionCost command3;
        private RideActionCost command4;

        public Builder() {
            this(0, false);
        }

        public Builder(float cost, boolean multiplier) {
            this.command1 = new RideActionCost(cost, multiplier);
        }

        public Builder secondCost(float cost, boolean multiplier) {
            this.command2 = new RideActionCost(cost, multiplier);
            return this;
        }

        public Builder thirdCost(float cost, boolean multiplier) {
            this.command3 = new RideActionCost(cost, multiplier);
            return this;
        }

        public Builder fourthCost(float cost, boolean multiplier) {
            this.command4 = new RideActionCost(cost, multiplier);
            return this;
        }

        public EntityRideActionCosts build() {
            return new EntityRideActionCosts(this.command1, this.command2, this.command3, this.command4);
        }
    }
}