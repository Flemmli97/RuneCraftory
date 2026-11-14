package io.github.flemmli97.runecraftory.common.entities.npc.text;

import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public interface TagProcessor {

    default String processTag(String tag, @Nullable String tagValue, String content) {
        return content;
    }

    default Style processStyle(String tag, @Nullable String tagValue, Style style) {
        return style;
    }

    class SimpleStyleProcessor implements TagProcessor {

        private final Function<Style, Style> processor;

        public SimpleStyleProcessor(Function<Style, Style> processor) {
            this.processor = processor;
        }

        @Override
        public Style processStyle(String tag, @Nullable String tagValue, Style style) {
            return this.processor.apply(style);
        }
    }
}
