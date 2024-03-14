package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.attachment.player.LevelExpPair;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;

public class S2CCapSync implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_player_data_sync");
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
        this.spells = data.getInv().save();
        this.foodData = data.foodBuffNBT();
        this.recipes = data.getRecipeKeeper().unlockedRecipes();
    }

    public static S2CCapSync read(FriendlyByteBuf buf) {
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

    public static void handle(S2CCapSync pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
            data.setMoney(player, pkt.money);
            data.setRunePoints(player, pkt.runePoints);
            data.setMaxRunePoints(player, pkt.runePointsMax);
            data.setStr(player, pkt.str);
            data.setVit(player, pkt.vit);
            data.setIntel(player, pkt.intel);
            data.getPlayerLevel().from(pkt.level);
            pkt.skillMap.forEach((skill, val) -> data.getSkillLevel(skill).from(val));
            data.getInv().load(pkt.spells);
            data.readFoodBuffFromNBT(pkt.foodData);
            data.getRecipeKeeper().clientUpdate(pkt.recipes);
        });
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.money);
        buf.writeInt(this.runePoints);
        buf.writeFloat(this.runePointsMax);
        buf.writeFloat(this.str);
        buf.writeFloat(this.intel);
        buf.writeFloat(this.vit);
        this.level.toPacket(buf);
        buf.writeInt(EnumSkills.values().length);
        for (EnumSkills skill : EnumSkills.values()) {
            buf.writeEnum(skill);
            LevelExpPair xp = this.skillMap.getOrDefault(skill, new LevelExpPair());
            xp.toPacket(buf);
        }
        buf.writeNbt(this.spells);
        buf.writeNbt(this.foodData);
        buf.writeInt(this.recipes.size());
        this.recipes.forEach(buf::writeResourceLocation);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
