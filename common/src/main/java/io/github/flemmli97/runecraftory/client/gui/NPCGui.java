package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.entities.npc.EnumShop;
import io.github.flemmli97.runecraftory.common.entities.npc.ShopState;
import io.github.flemmli97.runecraftory.common.network.C2SNPCInteraction;
import io.github.flemmli97.runecraftory.mixin.PoiTypeAccessor;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NPCGui<T extends EntityNPCBase> extends Screen {

    private static final ResourceLocation texturepath = new ResourceLocation(RuneCraftory.MODID, "textures/gui/view.png");

    private final int offSetX = 140;
    private final int offSetY = 50;
    protected int leftPos;
    protected int topPos;

    protected final T entity;
    private final ShopState isShopOpen;
    private final boolean canFollow;

    private List<FormattedCharSequence> components;

    public NPCGui(T entity, ShopState isShopOpen, boolean canFollow) {
        super(entity.getDisplayName());
        this.entity = entity;
        this.isShopOpen = isShopOpen;
        this.canFollow = canFollow;
    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = this.width - this.offSetX;
        this.topPos = this.offSetY;
        this.buttons();
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTick) {
        RenderSystem.setShaderTexture(0, texturepath);
        int posX = 25;
        int posY = 25;
        int texY = 150;
        if (this.entity.getShop() == EnumShop.NONE)
            texY = 40;
        if (this.entity.getShop() == EnumShop.RANDOM)
            texY = 60;
        this.blit(stack, posX, posY, 0, 0, 150, texY != 150 ? texY - 5 : texY);
        if (texY != 150)
            this.blit(stack, posX, posY + texY - 5, 0, 150 - 5, 150, 5);
        int txtOffX = posX + 5;
        int txtOffY = posY + 5;

        this.blit(stack, posX + 65, txtOffY + 13, 152, 2, 8, 8);

        int y = 0;
        ClientHandlers.drawCenteredScaledString(stack, this.font, this.entity.getName(), posX + 75, txtOffY, 1, 0);
        y += 1;
        this.font.draw(stack, "" + this.entity.friendPoints(this.minecraft.player), posX + 65 + 10, txtOffY + 13 * y, 0);
        y += 1;
        MutableComponent shopComp = null;
        if (this.entity.getShop() == EnumShop.GENERAL)
            shopComp = new TranslatableComponent("gui.npc.shop.owner", new TranslatableComponent(this.entity.getShop().translationKey));
        else if (this.entity.getShop() != EnumShop.NONE)
            shopComp = new TranslatableComponent(this.entity.getShop().translationKey);
        int shopY = txtOffY + 13 * y;
        int shopSizeY = -5;
        if (shopComp != null) {
            if (this.isShopOpen == ShopState.NOBED || this.isShopOpen == ShopState.NOWORKPLACE)
                shopComp.withStyle(ChatFormatting.DARK_RED);
            for (FormattedCharSequence comp : this.font.split(shopComp, 140)) {
                float xCenter = posX + 75 - this.minecraft.font.width(comp) * 0.5f;
                this.font.draw(stack, comp, xCenter, txtOffY + 13 * y, 0);
                y++;
                shopSizeY += 13;
            }
        }

        if (this.entity.getShop() != EnumShop.RANDOM && this.entity.getShop() != EnumShop.NONE) {
            for (Component comp : this.entity.getSchedule().viewSchedule()) {
                for (FormattedCharSequence formatted : this.font.split(comp, 140)) {
                    this.font.draw(stack, formatted, txtOffX, txtOffY + 13 * y, 0);
                    y++;
                }
            }
        }

        if (this.components != null && this.isHovering(txtOffX, shopY, 145, shopSizeY, mouseX, mouseY)) {
            this.renderTooltip(stack, this.components, mouseX, mouseY);
        }
        super.render(stack, mouseX, mouseY, partialTick);
    }

    protected void buttons() {
        int x = 2;
        int xSize = 90;
        int y = 0;
        this.addRenderableWidget(new Button(this.leftPos + x, this.topPos + y, xSize, 20, new TranslatableComponent("gui.npc.talk"), b -> {
            Platform.INSTANCE.sendToServer(new C2SNPCInteraction(this.entity.getId(), C2SNPCInteraction.Type.TALK));
            this.minecraft.setScreen(null);
        }));
        if (this.canFollow) {
            y += 30;
            this.addRenderableWidget(new Button(this.leftPos + x, this.topPos + y, xSize, 20, new TranslatableComponent("gui.npc.follow"), b -> {
                Platform.INSTANCE.sendToServer(new C2SNPCInteraction(this.entity.getId(), C2SNPCInteraction.Type.FOLLOW));
                this.minecraft.setScreen(null);
            }));
        }
        if (this.isShopOpen == ShopState.OPEN) {
            y += 30;
            this.addRenderableWidget(new Button(this.leftPos + x, this.topPos + y, xSize, 20, new TranslatableComponent("gui.npc.shop"), b -> {
                Platform.INSTANCE.sendToServer(new C2SNPCInteraction(this.entity.getId(), C2SNPCInteraction.Type.SHOP));
                this.minecraft.setScreen(null);
            }));
        }
        if (this.isShopOpen == ShopState.NOBED) {
            this.components = new ArrayList<>();
            this.components.addAll(this.font.split(new TranslatableComponent("gui.npc.bed.no"), 150));
        }
        if (this.isShopOpen == ShopState.NOWORKPLACE && this.entity.getShop().poiType.get() != null) {
            this.components = new ArrayList<>();
            this.components.addAll(this.font.split(new TranslatableComponent("gui.npc.workplace.no", this.formatShopPoi(this.entity.getShop().poiType.get())), 150));
        }
    }

    private Component formatShopPoi(PoiType poiType) {
        Set<BlockState> set = ((PoiTypeAccessor) poiType).matches();
        return set.stream().map(BlockBehaviour.BlockStateBase::getBlock)
                .distinct()
                .map(Block::getName)
                .reduce(new TextComponent(""), MutableComponent::append).withStyle(ChatFormatting.AQUA);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        if (this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
            this.onClose();
            return true;
        }
        return true;
    }

    protected boolean isHovering(int x, int y, int width, int height, double mouseX, double mouseY) {
        return mouseX >= x - 1
                && mouseX < (x + width + 1)
                && mouseY >= y - 1
                && mouseY < y + height + 1;
    }

    @Override
    public void onClose() {
        super.onClose();
        Platform.INSTANCE.sendToServer(new C2SNPCInteraction(this.entity.getId(), C2SNPCInteraction.Type.CLOSE));
    }
}