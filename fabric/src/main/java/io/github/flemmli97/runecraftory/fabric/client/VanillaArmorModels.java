package io.github.flemmli97.runecraftory.fabric.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EquipmentSlot;

public class VanillaArmorModels {

    private static HumanoidModel<?> inner;
    private static HumanoidModel<?> outer;

    public static void init(EntityRendererProvider.Context context) {
        inner = new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
        outer = new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR));
    }

    public static HumanoidModel<?> get(EquipmentSlot slot) {
        return slot == EquipmentSlot.LEGS ? inner : outer;
    }
}
