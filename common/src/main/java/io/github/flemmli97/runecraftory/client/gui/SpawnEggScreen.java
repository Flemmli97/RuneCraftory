package io.github.flemmli97.runecraftory.client.gui;

import io.github.flemmli97.runecraftory.common.components.NPCSpawnData;
import io.github.flemmli97.runecraftory.common.entities.EnsembleMonsters;
import io.github.flemmli97.runecraftory.common.items.creative.NPCSpawnEgg;
import io.github.flemmli97.runecraftory.common.network.C2SSpawnEgg;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryDataComponentTypes;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.ResourceLocationException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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

    private final List<EditBox> editBoxes = new ArrayList<>();
    private EditBox levelEditor, npcIDEditor;
    private int level;
    private ResourceLocation npcID;

    public SpawnEggScreen(InteractionHand hand) {
        super(Component.literal(""));
        this.hand = hand;
        this.player = Minecraft.getInstance().player;
    }

    @Override
    protected void init() {
        super.init();
        ItemStack stack = this.player.getItemInHand(this.hand);
        EntityType<?> type;
        if (stack.getItem() instanceof SpawnEgg egg) {
            type = egg.getType(stack);
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
                    livingSub.refreshDimensions();
                    list.add(livingSub);
                }
            }
            this.entities = List.copyOf(list);
        } else if (e instanceof LivingEntity living) {
            living.refreshDimensions();
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
    protected void renderBlurredBackground(float partialTick) {
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.fillGradient(this.leftPos, this.topPos, this.leftPos + this.sizeX, this.topPos + this.sizeY, 0xc0101010, 0xc0101010);
        int padding = 16;
        int yOff = padding;
        graphics.drawString(this.minecraft.font, Component.translatable("runecraftory.gui.level"), this.leftPos + padding, this.topPos + yOff, 0xffffff);
        this.levelEditor.render(graphics, mouseX, mouseY, partialTick);
        yOff += 16 + 20 + 60;
        if (this.npcIDEditor != null) {
            graphics.drawString(this.minecraft.font, Component.translatable("runecraftory.gui.npc.id"), this.leftPos + padding, this.topPos + yOff, 0xffffff);
            this.npcIDEditor.render(graphics, mouseX, mouseY, partialTick);
        }
        int max = Math.min(4, this.entities.size());
        for (int i = 0; i < max; i++) {
            LivingEntity entity = this.entities.get(i);
            float scale = 1;
            float size = 80 * scale;
            int posX = this.sizeX - padding - 12;
            int posY = padding + 12;
            if (max > 1) {
                int idx = i;
                if (max % 2 == 1) {
                    idx -= 1;
                }
                int offset = (int) (Math.ceil((idx + 1.) / 2) * (idx % 2 == 0 ? -1 : 1));
                int abs = Math.max(0, Math.abs(offset) - 1);
                scale = Math.max(0.2f, 1 - abs * 0.3f);
                posX += offset * 30;
                posY -= abs * 15;
            }
            posX -= size;
            RenderUtils.renderScaledEntityGui(graphics, this.leftPos + posX, this.topPos + posY, size,
                    size, 30 * scale, 0, mouseX, mouseY, entity);
        }
    }

    protected void buttons() {
        this.editBoxes.clear();
        int padding = 16;
        int yOff = padding + 12;
        this.levelEditor = new EditBox(this.minecraft.font, this.leftPos + padding, this.topPos + yOff, 48, 16, Component.literal("")) {
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
        this.editBoxes.add(this.levelEditor);
        ItemStack stack = this.player.getItemInHand(this.hand);
        this.level = stack.getOrDefault(RuneCraftoryDataComponentTypes.SPAWN_EGG_LEVEL.get(), 1);
        this.levelEditor.setValue(this.level + "");
        this.addWidget(this.levelEditor);
        yOff += 16 + 20 + 60;
        if (stack.getItem() instanceof NPCSpawnEgg) {
            this.npcIDEditor = new EditBox(this.minecraft.font, this.leftPos + padding, this.topPos + yOff, this.sizeY - 32, 16, Component.literal(""));
            NPCSpawnData itemData = stack.getOrDefault(RuneCraftoryDataComponentTypes.NPC_SPAWN_DATA.get(), NPCSpawnData.DEFAULT);
            this.npcID = itemData.npcDataId().orElse(null);
            if (this.npcID != null)
                this.npcIDEditor.setValue(this.npcID.toString());
            this.npcIDEditor.setResponder(s -> {
                try {
                    this.npcID = s.isEmpty() ? null : ResourceLocation.parse(s);
                } catch (ResourceLocationException ignored) {
                }
            });
            this.addWidget(this.npcIDEditor);
            this.editBoxes.add(this.npcIDEditor);
        }
        yOff += (16 + 8) * 2;
        this.addRenderableWidget(Button.builder(Component.translatable("runecraftory.gui.save"), b -> {
            LoaderNetwork.INSTANCE.sendToServer(new C2SSpawnEgg(this.hand, this.level, this.npcID));
            this.minecraft.setScreen(null);
        }).bounds(this.leftPos + this.sizeX / 2 - 50, this.topPos + yOff, 100, 20).build());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.editBoxes.stream().noneMatch(EditBox::canConsumeInput) && this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean click = super.mouseClicked(mouseX, mouseY, button);
        if (!click)
            this.setFocused(null);
        return click;
    }
}
