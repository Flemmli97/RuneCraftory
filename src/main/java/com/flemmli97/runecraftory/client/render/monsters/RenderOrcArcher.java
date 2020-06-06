package com.flemmli97.runecraftory.client.render.monsters;

import com.flemmli97.runecraftory.client.models.monsters.ModelOrcArcher;
import com.flemmli97.runecraftory.client.render.RenderMobBase;
import com.flemmli97.runecraftory.common.entity.monster.EntityOrc;
import com.flemmli97.runecraftory.common.lib.LibReference;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

public class RenderOrcArcher<T extends EntityOrc> extends RenderMobBase<T>{

	private ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/monsters/orc.png");
	public RenderOrcArcher(RenderManager renderManager) {
		super(renderManager, new ModelOrcArcher());
		this.layerRenderers.add(new LayerItem<>(this, new ItemStack(Items.BOW), EnumHand.MAIN_HAND));
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return this.tex;
	}

}
