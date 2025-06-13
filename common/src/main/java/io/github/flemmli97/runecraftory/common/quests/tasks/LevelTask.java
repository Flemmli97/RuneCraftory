package io.github.flemmli97.runecraftory.common.quests.tasks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.simplequests_api.SimpleQuestsAPI;
import io.github.flemmli97.simplequests_api.player.PlayerQuestData;
import io.github.flemmli97.simplequests_api.player.QuestProgress;
import io.github.flemmli97.simplequests_api.quest.QuestBase;
import io.github.flemmli97.simplequests_api.quest.entry.QuestEntryKey;
import io.github.flemmli97.simplequests_api.quest.entry.QuestTask;
import io.github.flemmli97.simplequests_api.quest.entry.ResolvedQuestTask;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

public class LevelTask implements QuestTask<LevelTask.LevelTaskResolved> {

    public static final QuestEntryKey<LevelTask> ID = new QuestEntryKey<>(RuneCraftory.modRes("level"));
    public static final Codec<LevelTask> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(Codec.STRING.fieldOf("description").forGetter(d -> d.description),
                    NumberProviders.CODEC.fieldOf("level").forGetter(d -> d.range)
            ).apply(instance, LevelTask::new));

    private final String description;

    private final NumberProvider range;

    public LevelTask(String description, NumberProvider range) {
        this.description = description;
        this.range = range;
        if (this.description.isEmpty() && !(this.range instanceof ConstantValue))
            throw new IllegalStateException("Description is required");
    }

    @Override
    public MutableComponent translation(ServerPlayer player) {
        if (this.description.isEmpty() && this.range instanceof ConstantValue) {
            return Component.translatable(this.getId().toString(), this.range.getInt(null));
        }
        return Component.translatable(this.description);
    }

    @Override
    public QuestEntryKey<?> getId() {
        return ID;
    }

    @Override
    public LevelTaskResolved resolve(PlayerQuestData data, QuestProgress progress, QuestBase base) {
        LootContext ctx = SimpleQuestsAPI.createContext(data, base.id);
        return new LevelTaskResolved(this.range.getInt(ctx));
    }

    public record LevelTaskResolved(int level) implements ResolvedQuestTask {

        public static final Codec<LevelTaskResolved> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(ExtraCodecs.POSITIVE_INT.fieldOf("level").forGetter(d -> d.level)).apply(instance, LevelTaskResolved::new));

        @Override
        public boolean submit(ServerPlayer player) {
            return Platform.INSTANCE.getPlayerData(player).map(d -> d.getPlayerLevel().getLevel() >= this.level).orElse(false);
        }

        @Override
        public QuestEntryKey<LevelTask> getId() {
            return ID;
        }

        @Override
        public MutableComponent translation(ServerPlayer player) {
            return Component.translatable(this.getId().toString(), this.level);
        }
    }
}
