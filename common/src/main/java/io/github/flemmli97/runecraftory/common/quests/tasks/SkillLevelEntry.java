package io.github.flemmli97.runecraftory.common.quests.tasks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.simplequests_api.quest.entry.QuestEntry;
import io.github.flemmli97.simplequests_api.quest.entry.QuestEntryKey;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;

public record SkillLevelEntry(EnumSkills skill, int level) implements QuestEntry {

    public static final QuestEntryKey<SkillLevelEntry> ID = new QuestEntryKey<>(new ResourceLocation(RuneCraftory.MODID, "skill_level"));
    public static final Codec<SkillLevelEntry> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    CodecUtils.stringEnumCodec(EnumSkills.class, null).fieldOf("skill").forGetter(d -> d.skill),
                    ExtraCodecs.POSITIVE_INT.fieldOf("level").forGetter(d -> d.level)).apply(instance, SkillLevelEntry::new));

    @Override
    public boolean submit(ServerPlayer player) {
        return Platform.INSTANCE.getPlayerData(player).map(d -> d.getSkillLevel(this.skill).getLevel() >= this.level).orElse(false);
    }

    @Override
    public QuestEntryKey<?> getId() {
        return ID;
    }

    @Override
    public MutableComponent translation(ServerPlayer player) {
        return new TranslatableComponent(this.getId().toString(), this.skill, this.level);
    }
}
