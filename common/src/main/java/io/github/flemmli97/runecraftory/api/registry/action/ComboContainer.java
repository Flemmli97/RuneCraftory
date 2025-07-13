package io.github.flemmli97.runecraftory.api.registry.action;

import com.google.common.base.Predicates;
import io.github.flemmli97.runecraftory.common.attachment.AttackActionHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ComboContainer {

    public static Predicate<AttackActionHandler> past(String marker) {
        return handler -> handler.getAnimation() == null || handler.isCurrentAnimationDone() || handler.getAnimation().isPast(marker);
    }

    private final List<ComboHandler> handlers;

    public ComboContainer(List<ComboHandler> handlers) {
        this.handlers = handlers;
    }

    @Nullable
    public ComboHandler get(int comboIdx) {
        if (comboIdx < 0 || comboIdx >= this.handlers.size())
            return null;
        return this.handlers.get(comboIdx);
    }

    public int size() {
        return this.handlers.size();
    }

    public record ComboHandler(Predicate<AttackActionHandler> canExecute, Predicate<AttackActionHandler> canAdvance,
                               ComboGetter advanceTo, int resetTime) {

        public static class Builder {

            private Predicate<AttackActionHandler> canExecute = Predicates.alwaysTrue();
            private final Predicate<AttackActionHandler> canAdvance;
            private Function<Integer, ComboGetter> advanceTo = i -> h -> i;
            private int resetTime;

            public Builder(Predicate<AttackActionHandler> canAdvance) {
                this.canAdvance = canAdvance;
            }

            public Builder withCheck(Predicate<AttackActionHandler> canAdvance) {
                this.canExecute = canAdvance;
                return this;
            }

            public Builder buffer(int resetTime) {
                this.resetTime = resetTime;
                return this;
            }

            public Builder advanceTo(Function<Integer, ComboGetter> advanceTo) {
                this.advanceTo = advanceTo;
                return this;
            }

            public ComboHandler build(int nextIndex) {
                return new ComboHandler(this.canExecute, this.canAdvance, this.advanceTo.apply(nextIndex), this.resetTime);
            }
        }
    }

    public interface ComboGetter {

        int get(AttackActionHandler handler);
    }

    public static class Builder {

        private final List<ComboHandler> handlers = new ArrayList<>();

        public static Builder builder() {
            return new Builder();
        }

        public Builder addCombo(Predicate<AttackActionHandler> canAdvance) {
            int idx = this.handlers.size() + 1;
            this.handlers.add(new ComboHandler(Predicates.alwaysTrue(), canAdvance, h -> idx, 0));
            return this;
        }

        public Builder addCombo(Predicate<AttackActionHandler> canAdvance, int resetTime) {
            int idx = this.handlers.size() + 1;
            this.handlers.add(new ComboHandler(Predicates.alwaysTrue(), canAdvance, h -> idx, resetTime));
            return this;
        }

        public Builder addCombo(ComboHandler.Builder builder) {
            this.handlers.add(builder.build(this.handlers.size() + 1));
            return this;
        }

        public ComboContainer build() {
            return new ComboContainer(List.copyOf(this.handlers));
        }
    }
}
