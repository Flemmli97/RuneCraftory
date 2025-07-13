package io.github.flemmli97.runecraftory.integration.sereneseasons;

import io.github.flemmli97.runecraftory.api.calendar.Season;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.SeasonHelper;

public class SeasonsAccess {

    @Nullable
    public static SeasonData getDate(Level level) {
        if (!GeneralConfig.SERENE_SEASONS.get())
            return null;
        ISeasonState helper = SeasonHelper.getSeasonState(level);
        int seasonDays = helper.getSeasonDuration() / helper.getDayDuration();
        return new SeasonData(helper.getDay() + 1,
                helper.getCycleDuration() / helper.getDayDuration(),
                (helper.getDay() % seasonDays) + 1,
                switch (helper.getSeason()) {
                    case SPRING -> Season.SPRING;
                    case SUMMER -> Season.SUMMER;
                    case AUTUMN -> Season.AUTUMN;
                    case WINTER -> Season.WINTER;
                });
    }

    public record SeasonData(int dayOfYear, int daysPerYear, int dayOfSeason, Season season) {

    }
}
