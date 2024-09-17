package io.github.flemmli97.runecraftory.client.render;

import com.mojang.blaze3d.vertex.BufferVertexConsumer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormatElement;

public class VertexHelper {

    public static final VertexFormatElement TIME = new VertexFormatElement(0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.GENERIC, 1);

    public static VertexConsumer time(VertexConsumer consumer, int time, float partialTicks) {
        if (!(consumer instanceof BufferVertexConsumer cons))
            return consumer;
        if (cons.currentElement().getUsage() != VertexFormatElement.Usage.GENERIC || cons.currentElement().getIndex() != 0) {
            return cons;
        }
        if (cons.currentElement().getType() != VertexFormatElement.Type.FLOAT || cons.currentElement().getCount() != 1) {
            throw new IllegalStateException();
        }
        cons.putFloat(0, ((float) (time % 24000) + partialTicks) / 24000.0f);
        cons.nextElement();
        return cons;
    }

}
