package io.github.flemmli97.runecraftory.api.datapack;

import com.mojang.serialization.Codec;
import io.github.flemmli97.tenshilib.common.utils.math.parser.ExpValue;
import io.github.flemmli97.tenshilib.common.utils.math.parser.Expression;

public record ExpressionHolder(String expression, ExpValue value) {

    public static final Codec<ExpressionHolder> CODEC = Codec.STRING.xmap(ExpressionHolder::new, ExpressionHolder::expression);

    public ExpressionHolder(String expression) {
        this(expression, Expression.of(expression));
    }
}