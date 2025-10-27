package io.github.flemmli97.runecraftory.common.quests.tasks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.simplequests_api.SimpleQuestsAPI;
import io.github.flemmli97.simplequests_api.player.PlayerQuestData;
import io.github.flemmli97.simplequests_api.player.QuestProgress;
import io.github.flemmli97.simplequests_api.quest.QuestBase;
import io.github.flemmli97.simplequests_api.quest.entry.QuestEntryKey;
import io.github.flemmli97.simplequests_api.quest.entry.QuestTask;
import io.github.flemmli97.simplequests_api.quest.entry.ResolvedQuestTask;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import io.github.flemmli97.tenshilib.loader.registry.AttachmentRegister;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

import java.util.List;

public class SkillLevelTask implements QuestTask<SkillLevelTask.SkillLevelTaskResolved> {

    public static final QuestEntryKey<SkillLevelTask> ID = new QuestEntryKey<>(RuneCraftory.modRes("skill_level"));
    public static final MapCodec<SkillLevelTask> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(Codec.STRING.fieldOf("description").forGetter(d -> d.description),
                    ExtraCodecs.nonEmptyList(CodecUtils.stringEnumCodec(Skills.class, null).listOf()).fieldOf("skill").forGetter(d -> d.skills),
                    NumberProviders.CODEC.fieldOf("level").forGetter(d -> d.range)
            ).apply(instance, SkillLevelTask::new));

    private final String description;

    private final List<Skills> skills;
    private final NumberProvider range;

    public SkillLevelTask(String description, List<Skills> skills, NumberProvider range) {
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
            Skills skill = this.skills.getFirst();
            return Component.translatable(this.getId().toString(), skill, this.range.getInt(null));
        }
        return Component.translatable(this.description);
    }

    @Override
    public QuestEntryKey<?> getId() {
        return ID;
    }

    @Override
    public SkillLevelTaskResolved resolve(PlayerQuestData data, QuestProgress progress, QuestBase base) {
        LootContext ctx = SimpleQuestsAPI.createContext(data, base.id);
        Skills skills = this.skills.get(ctx.getRandom().nextInt(this.skills.size()));
        return new SkillLevelTaskResolved(skills, this.range.getInt(ctx));
    }

    public record SkillLevelTaskResolved(Skills skill, int level) implements ResolvedQuestTask {

        public static final MapCodec<SkillLevelTaskResolved> CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(CodecUtils.stringEnumCodec(Skills.class, null).fieldOf("skill").forGetter(d -> d.skill),
                        ExtraCodecs.POSITIVE_INT.fieldOf("level").forGetter(d -> d.level)
                ).apply(instance, SkillLevelTaskResolved::new));

        @Override
        public boolean submit(ServerPlayer player) {
            return RunecraftoryAttachments.PLAYER_DATA.get().get(player).getPlayerLevel().getLevel() >= this.level;
        }

        @Override
        public QuestEntryKey<SkillLevelTask> getId() {
            return ID;
        }

        @Override
        public MutableComponent translation(ServerPlayer player) {
            return Component.translatable(this.getId().toString(), this.skill, this.level);
        }
    }
}
