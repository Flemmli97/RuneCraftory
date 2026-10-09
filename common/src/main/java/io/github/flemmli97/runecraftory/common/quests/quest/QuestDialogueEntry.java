package io.github.flemmli97.runecraftory.common.quests.quest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.simplequests_api.quest.QuestHolder;

public record QuestDialogueEntry(QuestHolder quest, QuestResponses dialogues) {

    public static final Codec<QuestDialogueEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            QuestHolder.CODEC.fieldOf("quest").forGetter(d -> d.quest),
            QuestResponses.CODEC.fieldOf("dialogues").forGetter(d -> d.dialogues)
    ).apply(instance, QuestDialogueEntry::new));
    public static final Codec<QuestDialogueEntry> OPT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            QuestHolder.SIMPLE_CODEC.fieldOf("quest").forGetter(d -> d.quest),
            QuestResponses.CODEC.fieldOf("dialogues").forGetter(d -> d.dialogues)
    ).apply(instance, QuestDialogueEntry::new));

}
