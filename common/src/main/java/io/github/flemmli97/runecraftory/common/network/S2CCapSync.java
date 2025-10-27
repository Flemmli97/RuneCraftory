package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.attachment.player.XpLevelHolder;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.tenshilib.loader.registry.AttachmentRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;

public class S2CCapSync implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CCapSync> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_player_data_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CCapSync> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CCapSync decode(RegistryFriendlyByteBuf buf) {
            return new S2CCapSync(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CCapSync pkt) {
            buf.writeInt(pkt.money);
            buf.writeInt(pkt.runePoints);
            pkt.level.toPacket(buf);
            buf.writeInt(Skills.values().length);
            for (Skills skill : Skills.values()) {
                buf.writeEnum(skill);
                XpLevelHolder xp = pkt.skillMap.getOrDefault(skill, new XpLevelHolder());
                xp.toPacket(buf);
            }
            buf.writeNbt(pkt.spells);
            ByteBufCodecs.optional(ByteBufCodecs.holderRegistry(Registries.ITEM))
                    .encode(buf, pkt.foodData.food());
            buf.writeInt(pkt.foodData.duration());
            buf.writeInt(pkt.recipes.size());
            pkt.recipes.forEach(buf::writeResourceLocation);
        }
    };

    private final EnumMap<Skills, XpLevelHolder> skillMap = new EnumMap<>(Skills.class);
    private final int money;
    private final int runePoints;
    private final XpLevelHolder level;
    private final CompoundTag spells;

    private final PlayerData.FoodData foodData;

    private final Collection<ResourceLocation> recipes;

    private S2CCapSync(RegistryFriendlyByteBuf buf) {
        this.money = buf.readInt();
        this.runePoints = buf.readInt();
        this.level = new XpLevelHolder(buf);
        int l = buf.readInt();
        for (int i = 0; i < l; i++) {
            Skills skill = buf.readEnum(Skills.class);
            this.skillMap.put(skill, new XpLevelHolder(buf));
        }
        this.spells = buf.readNbt();
        this.foodData = new PlayerData.FoodData(ByteBufCodecs.optional(ByteBufCodecs.holderRegistry(Registries.ITEM)).decode(buf), buf.readInt());
        this.recipes = new HashSet<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++)
            this.recipes.add(buf.readResourceLocation());
    }

    public S2CCapSync(PlayerData data) {
        this.money = data.getMoney();
        this.runePoints = data.getRunePoints();
        this.level = data.getPlayerLevel();
        for (Skills skill : Skills.values())
            this.skillMap.put(skill, data.getSkillLevel(skill));
        this.spells = data.getInv().save(data.player().registryAccess());
        this.foodData = data.foodBuff();
        this.recipes = data.getRecipeKeeper().unlockedRecipes();
    }

    public static void handle(S2CCapSync pkt, Player player) {
        PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
        data.setMoney(pkt.money);
        data.setRunePoints(pkt.runePoints);
        data.getPlayerLevel().from(pkt.level);
        pkt.skillMap.forEach((skill, val) -> data.getSkillLevel(skill).from(val));
        data.getInv().load(pkt.spells, player.registryAccess());
        data.updateFoodBuff(pkt.foodData);
        data.getRecipeKeeper().clientUpdate(pkt.recipes);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
