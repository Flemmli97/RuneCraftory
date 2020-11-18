package com.flemmli97.runecraftory.mobs.client.render;

import com.flemmli97.runecraftory.mobs.entity.BaseMonster;
import com.flemmli97.runecraftory.mobs.entity.BossMonster;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;

public abstract class RenderMonster<T extends BaseMonster, M extends EntityModel<T>> extends MobRenderer<T, M> {

    public RenderMonster(EntityRendererManager manager, M model) {
        super(manager, model, 0.5f);
    }

}
