package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.common.items.NPCSpawnEgg;
import io.github.flemmli97.runecraftory.common.items.RuneCraftoryEggItem;
import io.github.flemmli97.runecraftory.common.network.C2SSpawnEgg;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.ResourceLocationException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SpawnEggScreen extends Screen {

    private final Player player;
    protected LivingEntity entity;
    private final InteractionHand hand;
    private int leftPos, topPos;
    private final int sizeX = 200;
    private final int sizeY = 200;

    private EditBox levelEditor, npcIDEditor;
    private int level;
    private ResourceLocation npcID;

    public SpawnEggScreen(InteractionHand hand) {
        super(new TextComponent(""));
        this.hand = hand;
        this.player = Minecraft.getInstance().player;
    }

    @Override
    protected void init() {
        super.init();
        ItemStack stack = this.player.getItemInHand(this.hand);
        if (stack.getItem() instanceof RuneCraftoryEggItem egg) {
            EntityType<?> type = egg.getType(stack.getTag());
            Entity e = type.create(Minecraft.getInstance().level);
            if (!(e instanceof LivingEntity living)) {
                Minecraft.getInstance().setScreen(null);
                return;
            }
            EntityType.updateCustomEntityTag(Minecraft.getInstance().level, null, living, stack.getTag());
            this.entity = living;
        } else {
            Minecraft.getInstance().setScreen(null);
            return;
        }
        this.leftPos = this.width / 2 - (this.sizeX / 2);
        this.topPos = this.height / 2 - (this.sizeY / 2);
        this.buttons();
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTick) {
        this.fillGradient(stack, this.leftPos, this.topPos, this.leftPos + this.sizeX, this.topPos + this.sizeY, -1072689136, -804253680);
        int xPadding = 16;
        int yOff = 16;
        this.minecraft.font.draw(stack, new TranslatableComponent("runecraftory.gui.level"), this.leftPos + xPadding, this.topPos + yOff, 0xffffff);
        this.levelEditor.render(stack, mouseX, mouseY, partialTick);
        yOff += 16 + 20 + 60;
        if (this.npcIDEditor != null) {
            this.minecraft.font.draw(stack, new TranslatableComponent("runecraftory.gui.npc.id"), this.leftPos + xPadding, this.topPos + yOff, 0xffffff);
            this.npcIDEditor.render(stack, mouseX, mouseY, partialTick);
        }
        float scale = 1;
        if (this.entity.getBbWidth() > 1.2) {
            scale = 2f / this.entity.getBbWidth();
        }
        if (this.entity.getBbHeight() > 1.6) {
            scale = Math.min(scale, 2.4f / this.entity.getBbHeight());
        }
        InventoryScreen.renderEntityInInventory(this.leftPos + 150, this.topPos + 100, (int) (29 * scale), this.leftPos + 150 - mouseX, this.topPos + 65 - mouseY, this.entity);
        super.render(stack, mouseX, mouseY, partialTick);
    }

    protected void buttons() {
        int padding = 16;
        int yOff = padding + 12;
        this.levelEditor = new EditBox(this.minecraft.font, this.leftPos + padding, this.topPos + yOff, 48, 16, new TextComponent("")) {
            @Override
            public boolean charTyped(char codePoint, int modifiers) {
                if (Character.isDigit(codePoint))
                    return super.charTyped(codePoint, modifiers);
                return false;
            }
        };
        this.levelEditor.setResponder(s -> {
            try {
                this.level = Integer.parseInt(s);
            } catch (NumberFormatException ignored) {
            }
        });
        ItemStack stack = this.player.getItemInHand(this.hand);
        this.level = RuneCraftoryEggItem.getMobLevel(stack);
        this.levelEditor.setValue(this.level + "");
        this.addWidget(this.levelEditor);
        yOff += 16 + 20 + 60;
        if (stack.getItem() instanceof NPCSpawnEgg) {
            this.npcIDEditor = new EditBox(this.minecraft.font, this.leftPos + padding, this.topPos + yOff, this.sizeY - 32, 16, new TextComponent(""));
            this.npcID = NPCSpawnEgg.getNpcID(stack);
            if (this.npcID != null)
                this.npcIDEditor.setValue(this.npcID.toString());
            this.npcIDEditor.setResponder(s -> {
                try {
                    this.npcID = new ResourceLocation(s);
                } catch (ResourceLocationException ignored) {
                }
            });
            this.addWidget(this.npcIDEditor);
        }
        yOff += (16 + 8) * 2;
        this.addRenderableWidget(new Button(this.leftPos + this.sizeX / 2 - 50, this.topPos + yOff, 100, 20, new TranslatableComponent("runecraftory.gui.save"), b -> {
            Platform.INSTANCE.sendToServer(new C2SSpawnEgg(this.hand, this.level, this.npcID));
            this.minecraft.setScreen(null);
        }));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.levelEditor.keyPressed(keyCode, scanCode, modifiers) || this.levelEditor.canConsumeInput()) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
