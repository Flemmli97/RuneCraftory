package io.github.flemmli97.runecraftory.mixin.compat;

import io.github.flemmli97.runecraftory.common.integration.simplequest.SimpleQuestData;
import io.github.flemmli97.simplequests.quest.types.QuestBase;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;

@Pseudo
@Mixin(targets = "io/github/flemmli97/simplequests/player/PlayerData")
public class SimpleQuestDataMixin implements SimpleQuestData {

    @Unique
    private Map<ResourceLocation, QuestBase> runecraftory_quest_board;

    @Override
    public Map<ResourceLocation, QuestBase> getQuestboardQuests() {
        return this.runecraftory_quest_board;
    }

    @Override
    public void setQuestboardQuests(Map<ResourceLocation, QuestBase> map) {
        this.runecraftory_quest_board = map;
    }
}
