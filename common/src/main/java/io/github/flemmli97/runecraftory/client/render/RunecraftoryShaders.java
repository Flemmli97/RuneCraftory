package io.github.flemmli97.runecraftory.client.render;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.tenshilib.client.CustomRenderTypesHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.util.function.Consumer;

public class RunecraftoryShaders extends RenderType {

    private static ShaderInstance GATE_SHADER_INSTANCE;
    public static final ShaderStateShard GATE_SHADER = new ShaderStateShard(() -> GATE_SHADER_INSTANCE);
    public static final VertexFormat POSITION_COLOR_2X_TEX = new VertexFormat(ImmutableMap.<String, VertexFormatElement>builder().put("Position", DefaultVertexFormat.ELEMENT_POSITION)
            .put("Color", DefaultVertexFormat.ELEMENT_COLOR).put("Color2", DefaultVertexFormat.ELEMENT_COLOR).put("UV0", DefaultVertexFormat.ELEMENT_UV0)
            .put("UV1", DefaultVertexFormat.ELEMENT_UV1).build());

    public static final RenderType GATE_RENDER = CustomRenderTypesHelper.createType("runecraftory:gate", POSITION_COLOR_2X_TEX, VertexFormat.Mode.QUADS, 256, false, false, CompositeState.builder()
            .setShaderState(GATE_SHADER)
            .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
            .setOverlayState(OVERLAY).createCompositeState(false));

    public static void registerShader(ShaderRegister register) {
        try {
            register.register(new ResourceLocation(RuneCraftory.MODID, "gate"), POSITION_COLOR_2X_TEX,
                    shaderInstance -> RunecraftoryShaders.GATE_SHADER_INSTANCE = shaderInstance);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private RunecraftoryShaders(String string, VertexFormat vertexFormat, VertexFormat.Mode mode, int i, boolean bl, boolean bl2, Runnable runnable, Runnable runnable2) {
        super(string, vertexFormat, mode, i, bl, bl2, runnable, runnable2);
    }

    public interface ShaderRegister {

        void register(ResourceLocation id, VertexFormat vertexFormat, Consumer<ShaderInstance> onLoad) throws IOException;

    }
}
