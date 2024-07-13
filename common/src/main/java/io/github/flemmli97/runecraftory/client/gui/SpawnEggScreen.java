package io.github.flemmli97.runecraftory.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.common.entities.EnsembleMonsters;
import io.github.flemmli97.runecraftory.common.items.creative.NPCSpawnEgg;
import io.github.flemmli97.runecraftory.common.items.creative.RuneCraftoryEggItem;
import io.github.flemmli97.runecraftory.common.network.C2SSpawnEgg;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import net.minecraft.ResourceLocationException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SpawnEggScreen extends Screen {

    private final Player player;
    protected List<LivingEntity> entities;
    private final InteractionHand hand;
    private int leftPos, topPos;
    private final int sizeX = 240;
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
        EntityType<?> type;
        if (stack.getItem() instanceof SpawnEgg egg) {
            type = egg.getType(stack.getTag());
        } else {
            Minecraft.getInstance().setScreen(null);
            return;
        }
        Entity e = type.create(Minecraft.getInstance().level);
        if (e instanceof EnsembleMonsters ensemble) {
            List<LivingEntity> list = new ArrayList<>();
            for (Supplier<? extends EntityType<?>> t : ensemble.entities()) {
                Entity sub = t.get().create(Minecraft.getInstance().level);
                if (sub instanceof LivingEntity livingSub) {
                    list.add(livingSub);
                }
            }
            this.entities = List.copyOf(list);
        } else if (e instanceof LivingEntity living) {
            this.entities = List.of(living);
        } else {
            Minecraft.getInstance().setScreen(null);
            return;
        }
        this.leftPos = this.width / 2 - (this.sizeX / 2);
        this.topPos = this.height / 2 - (this.sizeY / 2);
        this.buttons();
    }

    @Override
    public void tick() {
        super.tick();
        this.entities.forEach(entity -> entity.tickCount++);
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
        int max = Math.min(4, this.entities.size());
        double middle = (max - 1) / 2.;
        for (int i = 0; i < max; i++) {
            LivingEntity entity = this.entities.get(i);
            float scale = 1;
            if (entity.getBbWidth() > 1.2) {
                scale = 2f / entity.getBbWidth();
            }
            if (entity.getBbHeight() > 1.6) {
                scale = Math.min(scale, 2.4f / entity.getBbHeight());
            }
            int offset = Math.min(Math.abs(i - (int) middle), Math.abs(i - Mth.ceil(middle)));
            scale *= (1 - offset * 0.2);
            int posX = 160;
            int posY = 100;
            if (max > 1) {
                posX += (int) ((i - middle) * 90 * scale);
                posY -= offset * 15;
            }
            InventoryScreen.renderEntityInInventory(this.leftPos + posX, this.topPos + posY, (int) (29 * scale), this.leftPos + posX - mouseX, this.topPos + (posY - 35) - mouseY, entity);
        }
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
