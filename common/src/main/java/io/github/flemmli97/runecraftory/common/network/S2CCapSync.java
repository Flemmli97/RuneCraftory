package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.attachment.player.LevelExpPair;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
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
            S2CCapSync pkt = new S2CCapSync();
            pkt.money = buf.readInt();
            pkt.runePoints = buf.readInt();
            pkt.runePointsMax = buf.readFloat();
            pkt.str = buf.readFloat();
            pkt.intel = buf.readFloat();
            pkt.vit = buf.readFloat();
            pkt.level.fromPacket(buf);
            int l = buf.readInt();
            for (int i = 0; i < l; i++) {
                EnumSkills skill = buf.readEnum(EnumSkills.class);
                pkt.skillMap.put(skill, new LevelExpPair(buf));
            }
            pkt.spells = buf.readNbt();
            pkt.foodData = buf.readNbt();
            pkt.recipes = new HashSet<>();
            int size = buf.readInt();
            for (int i = 0; i < size; i++)
                pkt.recipes.add(buf.readResourceLocation());
            return pkt;
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CCapSync pkt) {
            buf.writeInt(pkt.money);
            buf.writeInt(pkt.runePoints);
            buf.writeFloat(pkt.runePointsMax);
            buf.writeFloat(pkt.str);
            buf.writeFloat(pkt.intel);
            buf.writeFloat(pkt.vit);
            pkt.level.toPacket(buf);
            buf.writeInt(EnumSkills.values().length);
            for (EnumSkills skill : EnumSkills.values()) {
                buf.writeEnum(skill);
                LevelExpPair xp = pkt.skillMap.getOrDefault(skill, new LevelExpPair());
                xp.toPacket(buf);
            }
            buf.writeNbt(pkt.spells);
            buf.writeNbt(pkt.foodData);
            buf.writeInt(pkt.recipes.size());
            pkt.recipes.forEach(buf::writeResourceLocation);
        }
    };

    private final EnumMap<EnumSkills, LevelExpPair> skillMap = new EnumMap<>(EnumSkills.class);
    private int money;
    private int runePoints;
    private float runePointsMax;
    private float str;
    private float vit;
    private float intel;
    private LevelExpPair level = new LevelExpPair();
    private CompoundTag spells;

    private CompoundTag foodData;

    private Collection<ResourceLocation> recipes;

    private S2CCapSync() {
    }

    public S2CCapSync(PlayerData data) {
        this.money = data.getMoney();
        this.runePoints = data.getRunePoints();
        this.runePointsMax = data.getMaxRunePointsRaw();
        this.str = data.getStr();
        this.intel = data.getIntel();
        this.vit = data.getVit();
        this.level = data.getPlayerLevel();
        for (EnumSkills skill : EnumSkills.values())
            this.skillMap.put(skill, data.getSkillLevel(skill));
        this.spells = data.getInv().save(data.player().registryAccess());
        this.foodData = data.foodBuffNBT();
        this.recipes = data.getRecipeKeeper().unlockedRecipes();
    }

    public static void handle(S2CCapSync pkt, Player player) {
        PlayerData data = Platform.INSTANCE.getPlayerData(player);
        data.setMoney(pkt.money);
        data.setRunePoints(pkt.runePoints);
        data.setMaxRunePoints(pkt.runePointsMax);
        data.setStr(pkt.str);
        data.setVit(pkt.vit);
        data.setIntel(pkt.intel);
        data.getPlayerLevel().from(pkt.level);
        pkt.skillMap.forEach((skill, val) -> data.getSkillLevel(skill).from(val));
        data.getInv().load(pkt.spells, player.registryAccess());
        data.readFoodBuffFromNBT(pkt.foodData);
        data.getRecipeKeeper().clientUpdate(pkt.recipes);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
