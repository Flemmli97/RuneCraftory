package io.github.flemmli97.runecraftory.client.render;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.tenshilib.client.VertexUtils;
import io.github.flemmli97.tenshilib.client.shader.ShaderRegister;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;

import java.io.IOException;

public class RunecraftoryShaders extends RenderType {

    private static ShaderInstance GATE_SHADER_INSTANCE;
    private static final ShaderStateShard GATE_SHADER = new ShaderStateShard(() -> GATE_SHADER_INSTANCE);
    private static final VertexFormat POSITION_COLOR_2X_TEX_TIME = VertexFormat.builder().add("Position", VertexFormatElement.POSITION)
            .add("Color", VertexFormatElement.COLOR).add("UV0", VertexFormatElement.UV0)
            .add("UV1", VertexFormatElement.UV1)
            .add("Color2", VertexFormatElement.NORMAL)
            .add("Time", VertexUtils.SINGLE_FLOAT.get()).build();

    /**
     * The VertexFormat used is unconventional due to vanillas limitation.
     * {@link VertexFormatElement} is hard to extend and is limited to only 32 different ids.
     * Invasive changes there might cause potential issues so im leaving it alone...
     * <p>
     * POSITION_COLOR_2X_TEX uses the NORMAL element as second color and Offset as a gametime offset
     */
    public static final RenderType GATE_RENDER = create("runecraftory:gate", POSITION_COLOR_2X_TEX_TIME, VertexFormat.Mode.QUADS, 256, false, false, CompositeState.builder()
            .setShaderState(GATE_SHADER)
            .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
            .setOverlayState(OVERLAY).createCompositeState(false));

    public static void registerShader() {
        ShaderRegister.INSTANCE.register(RuneCraftory.MODID, register -> {
            try {
                register.register(RuneCraftory.modRes("gate"), POSITION_COLOR_2X_TEX_TIME,
                        shaderInstance -> RunecraftoryShaders.GATE_SHADER_INSTANCE = shaderInstance);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private RunecraftoryShaders(String string, VertexFormat vertexFormat, VertexFormat.Mode mode, int i, boolean bl, boolean bl2, Runnable runnable, Runnable runnable2) {
        super(string, vertexFormat, mode, i, bl, bl2, runnable, runnable2);
    }
}
