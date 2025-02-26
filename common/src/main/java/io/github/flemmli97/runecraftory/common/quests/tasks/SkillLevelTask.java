package io.github.flemmli97.runecraftory.common.quests.tasks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.utils.CodecHelper;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.simplequests_api.SimpleQuestsAPI;
import io.github.flemmli97.simplequests_api.player.PlayerQuestData;
import io.github.flemmli97.simplequests_api.player.QuestProgress;
import io.github.flemmli97.simplequests_api.quest.QuestBase;
import io.github.flemmli97.simplequests_api.quest.entry.QuestEntryKey;
import io.github.flemmli97.simplequests_api.quest.entry.QuestTask;
import io.github.flemmli97.simplequests_api.quest.entry.ResolvedQuestTask;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.List;

public class SkillLevelTask implements QuestTask<SkillLevelTask.SkillLevelTaskResolved> {

    public static final QuestEntryKey<SkillLevelTask> ID = new QuestEntryKey<>(new ResourceLocation(RuneCraftory.MODID, "skill_level"));
    public static final Codec<SkillLevelTask> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(Codec.STRING.fieldOf("description").forGetter(d -> d.description),
                    CodecHelper.nonEmptyList(CodecUtils.stringEnumCodec(EnumSkills.class, null), "Skill list can't be empty").fieldOf("skill").forGetter(d -> d.skills),
                    CodecHelper.NUMER_PROVIDER_CODEC.fieldOf("level").forGetter(d -> d.range)
            ).apply(instance, SkillLevelTask::new));

    private final String description;

    private final List<EnumSkills> skills;
    private final NumberProvider range;

    public SkillLevelTask(String description, List<EnumSkills> skills, NumberProvider range) {
        this.description = description;
        this.skills = skills;
        this.range = range;
        if (this.description.isEmpty() && !this.simple())
            throw new IllegalStateException("Description is required");
    }

    private boolean simple() {
        return this.skills.size() == 1 && this.range instanceof ConstantValue;
    }

    @Override
    public MutableComponent translation(ServerPlayer player) {
        if (this.description.isEmpty() && this.simple()) {
            EnumSkills skill = this.skills.get(0);
            return new TranslatableComponent(this.getId().toString(), skill, this.range.getInt(null));
        }
        return new TranslatableComponent(this.description);
    }

    @Override
    public QuestEntryKey<?> getId() {
        return ID;
    }

    @Override
    public SkillLevelTaskResolved resolve(PlayerQuestData data, QuestProgress progress, QuestBase base) {
        LootContext ctx = SimpleQuestsAPI.createContext(data, base.id);
        EnumSkills skills = this.skills.get(ctx.getRandom().nextInt(this.skills.size()));
        return new SkillLevelTaskResolved(skills, this.range.getInt(ctx));
    }

    public record SkillLevelTaskResolved(EnumSkills skill, int level) implements ResolvedQuestTask {

        public static final Codec<SkillLevelTaskResolved> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(CodecUtils.stringEnumCodec(EnumSkills.class, null).fieldOf("skill").forGetter(d -> d.skill),
                        ExtraCodecs.POSITIVE_INT.fieldOf("level").forGetter(d -> d.level)
                ).apply(instance, SkillLevelTaskResolved::new));

        @Override
        public boolean submit(ServerPlayer player) {
            return Platform.INSTANCE.getPlayerData(player).map(d -> d.getPlayerLevel().getLevel() >= this.level).orElse(false);
        }

        @Override
        public QuestEntryKey<SkillLevelTask> getId() {
            return ID;
        }

        @Override
        public MutableComponent translation(ServerPlayer player) {
            return new TranslatableComponent(this.getId().toString(), this.skill, this.level);
        }
    }
}
