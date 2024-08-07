package io.github.flemmli97.runecraftory.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.api.datapack.ConversationContext;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.client.gui.CraftingGui;
import io.github.flemmli97.runecraftory.client.gui.FarmlandInfo;
import io.github.flemmli97.runecraftory.client.gui.InfoScreen;
import io.github.flemmli97.runecraftory.client.gui.MonsterCompanionGui;
import io.github.flemmli97.runecraftory.client.gui.NPCCompanionGui;
import io.github.flemmli97.runecraftory.client.gui.NPCDialogueGui;
import io.github.flemmli97.runecraftory.client.gui.NPCGui;
import io.github.flemmli97.runecraftory.client.gui.NPCShopGui;
import io.github.flemmli97.runecraftory.client.gui.OverlayGui;
import io.github.flemmli97.runecraftory.client.gui.QuestGui;
import io.github.flemmli97.runecraftory.client.gui.SpawnEggScreen;
import io.github.flemmli97.runecraftory.client.gui.SpellInvOverlayGui;
import io.github.flemmli97.runecraftory.client.model.AnimatedPlayerModel;
import io.github.flemmli97.runecraftory.client.model.SittingModel;
import io.github.flemmli97.runecraftory.client.render.ScaledRenderer;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.entities.npc.job.ShopState;
import io.github.flemmli97.runecraftory.common.network.S2CTriggers;
import io.github.flemmli97.runecraftory.common.utils.CalendarImpl;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.world.family.SyncedFamilyData;
import io.github.flemmli97.runecraftory.integration.simplequest.ClientSideQuestDisplay;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.client.CameraType;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.toasts.RecipeToast;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ClientHandlers {

    public static final CalendarImpl CLIENT_CALENDAR = new CalendarImpl();

    public static OverlayGui OVERLAY;
    public static SpellInvOverlayGui SPELL_DISPLAY;
    public static FarmlandInfo FARM_DISPLAY;

    public static TriggerKeyBind SPELL_1;
    public static TriggerKeyBind SPELL_2;
    public static TriggerKeyBind SPELL_3;
    public static TriggerKeyBind SPELL_4;

    private static AnimatedPlayerModel<?> ANIMATED_PLAYER_MODEL;

    private static CameraType pastType = CameraType.FIRST_PERSON;

    public static Player getPlayer() {
        return Minecraft.getInstance().player;
    }

    public static void updateClientCalendar(FriendlyByteBuf buffer) {
        EnumSeason prev = CLIENT_CALENDAR.currentSeason();
        CLIENT_CALENDAR.fromPacket(buffer);
        if (CLIENT_CALENDAR.currentSeason() != prev) {
            Minecraft mc = Minecraft.getInstance();
            Level level = mc.level;
            if (mc.level != null) {
                int renderDist = mc.options.getEffectiveRenderDistance();
                for (int x = -renderDist; x <= renderDist; x++)
                    for (int y = level.getMinSection(); y < level.getSectionsCount(); y++)
                        for (int z = -renderDist; z <= renderDist; z++)
                            mc.levelRenderer.setSectionDirty(x, y, z);
            }
        }
    }

    public static void grabMouse(LivingEntity entity, boolean sleeping) {
        if (entity == Minecraft.getInstance().player && Minecraft.getInstance().screen == null) {
            Minecraft.getInstance().mouseHandler.grabMouse();
            if (sleeping)
                KeyMapping.releaseAll();
        }
    }

    public static boolean disableMouseMove() {
        Minecraft mc = Minecraft.getInstance();
        return mc.player != null && (EntityUtils.isDisabled(mc.player) || Platform.INSTANCE.getPlayerData(mc.player)
                .map(d -> d.getWeaponHandler().lockedLook()).orElse(false)) && (mc.screen == null || mc.screen instanceof AbstractContainerScreen<?>);
    }

    public static boolean disableMouseClick() {
        Minecraft mc = Minecraft.getInstance();
        return mc.player != null && EntityUtils.isDisabled(mc.player) && (mc.screen == null || mc.screen instanceof AbstractContainerScreen<?>);
    }

    public static boolean disableScrollMouse() {
        Minecraft mc = Minecraft.getInstance();
        return mc.player != null && (EntityUtils.isDisabled(mc.player) || Platform.INSTANCE.getPlayerData(mc.player).map(d -> d.getWeaponHandler().isItemSwapBlocked()).orElse(false)) && (mc.screen == null || mc.screen instanceof AbstractContainerScreen<?>);
    }

    public static boolean disableKeys(int key, int scanCode) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen instanceof ChatScreen)
            return false;
        return key != 256
                && !mc.options.keyInventory.matches(key, scanCode)
                && !mc.options.keyChat.matches(key, scanCode)
                && !mc.options.keyCommand.matches(key, scanCode) && EntityUtils.isDisabled(mc.player);
    }

    public static void recipeToast(Collection<ResourceLocation> recipes) {
        recipes.forEach(res -> Minecraft.getInstance().level.getRecipeManager().byKey(res).ifPresent(rec ->
                RecipeToast.addOrUpdate(Minecraft.getInstance().getToasts(), rec)));
    }

    public static void setToThirdPerson(boolean reset) {
        if (reset)
            Minecraft.getInstance().options.setCameraType(pastType);
        else {
            pastType = Minecraft.getInstance().options.getCameraType();
            Minecraft.getInstance().options.setCameraType(CameraType.THIRD_PERSON_BACK);
        }
    }

    public static void trySetPerspective(LivingEntity entity, boolean flag) {
        if (entity == Minecraft.getInstance().getCameraEntity())
            setToThirdPerson(!flag);
    }

    public static boolean orthorgraphicCam() {
        Entity entity = Minecraft.getInstance().getCameraEntity();
        if (entity instanceof LivingEntity living) {
            return Platform.INSTANCE.getEntityData(living).map(EntityData::isOrthoView).orElse(false);
        }
        return false;
    }

    public static void openCompanionGui(int id, boolean fullParty, boolean hasHome) {
        Entity entity = Minecraft.getInstance().level.getEntity(id);
        if (entity instanceof BaseMonster monster) {
            if (!Minecraft.getInstance().player.getUUID().equals(monster.getOwnerUUID()))
                return;
            Minecraft.getInstance().setScreen(new MonsterCompanionGui(monster, fullParty, hasHome));
        }
    }

    public static void openNPCChat(int id, ShopState isShopOpen, SyncedFamilyData family, int followState, Map<String, List<Component>> actions, ResourceLocation quest) {
        Entity entity = Minecraft.getInstance().level.getEntity(id);
        if (entity instanceof EntityNPCBase npc) {
            if (followState == 1)
                Minecraft.getInstance().setScreen(new NPCCompanionGui(npc, isShopOpen == ShopState.OPEN, quest));
            else
                Minecraft.getInstance().setScreen(new NPCGui<>(npc, isShopOpen, followState == 0, family, actions, quest));
        }
    }

    public static void drawCenteredScaledString(PoseStack stack, Font font, Component component, float x, float y, float scale, int color) {
        if (scale != 1) {
            stack.pushPose();
            stack.translate(x, y, 0);
            stack.scale(scale, scale, scale);
            stack.translate(-font.width(component) * 0.5, 0, 0);
            font.draw(stack, component, 0, 0, color);
            stack.popPose();
        } else {
            x -= font.width(component) * 0.5;
            font.draw(stack, component, x, y, color);
        }
    }

    public static void drawCenteredScaledString(PoseStack stack, Font font, String string, float x, float y, float scale, int color) {
        if (scale != 1) {
            stack.pushPose();
            stack.translate(x, y, 0);
            stack.scale(scale, scale, scale);
            stack.translate(-font.width(string) * 0.5, 0, 0);
            font.draw(stack, string, 0, 0, color);
            stack.popPose();
        } else {
            x -= font.width(string) * 0.5;
            font.draw(stack, string, x, y, color);
        }
    }

    public static void drawRightAlignedScaledString(PoseStack stack, Font font, Component string, float x, float y, float scale, int color) {
        if (scale != 1) {
            stack.pushPose();
            stack.translate(x, y, 0);
            stack.scale(scale, scale, scale);
            stack.translate(-font.width(string), 0, 0);
            font.draw(stack, string, 0, 0, color);
            stack.popPose();
        } else {
            x -= font.width(string);
            font.draw(stack, string, x, y, color);
        }
    }

    public static void drawRightAlignedScaledString(PoseStack stack, Font font, String string, float x, float y, float scale, int color) {
        if (scale != 1) {
            stack.pushPose();
            stack.translate(x, y, 0);
            stack.scale(scale, scale, scale);
            stack.translate(-font.width(string), 0, 0);
            font.draw(stack, string, 0, 0, color);
            stack.popPose();
        } else {
            x -= font.width(string);
            font.draw(stack, string, x, y, color);
        }
    }

    public static void handleShopRespone(Component txt) {
        if (Minecraft.getInstance().screen instanceof NPCShopGui shop) {
            if (txt != null)
                shop.drawBubble(txt);
            else
                shop.updateButtons();
        }
    }

    public static AnimatedPlayerModel<?> getAnimatedPlayerModel() {
        return ANIMATED_PLAYER_MODEL;
    }

    public static void initNonRendererModels(EntityRendererProvider.Context ctx) {
        ANIMATED_PLAYER_MODEL = new AnimatedPlayerModel<>(ctx.bakeLayer(AnimatedPlayerModel.LAYER_LOCATION));
        ArmorModels.initArmorModels(ctx);
    }

    public static void updateCurrentRecipeIndex(int index) {
        if (Minecraft.getInstance().screen instanceof CraftingGui gui)
            gui.setScrollValue(index);
    }

    public static void onAttributePkt() {
        if (Minecraft.getInstance().screen instanceof InfoScreen screen) {
            screen.onAttributePkt();
        }
    }

    public static void handleTriggers(S2CTriggers.Type type, BlockPos pos) {
        switch (type) {
            case FERTILIZER -> { //Makes the particles more visible. The one called at BoneMealItem checks for air blocks
                Level level = Minecraft.getInstance().level;
                double x = pos.getX() + 0.5D;
                double y = pos.getY() + 1.25D;
                double z = pos.getZ() + 0.5D;
                level.addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, 0.0D, 0.0D, 0.0D);
                Random random = level.getRandom();
                for (int i = 0; i < 15; ++i) {
                    double nX = x - 0.5 + random.nextDouble();
                    double nY = y - 0.35 + random.nextDouble() * 0.5;
                    double nZ = z - 0.5 + random.nextDouble();
                    if (!level.getBlockState((new BlockPos(nX, nY, nZ)).below()).isAir()) {
                        level.addParticle(ParticleTypes.HAPPY_VILLAGER, nX, nY, nZ,
                                random.nextGaussian() * 0.02D, random.nextGaussian() * 0.02D, random.nextGaussian() * 0.02D);
                    }
                }
            }
        }
    }

    public static void updateNPCDialogue(EntityNPCBase npc, ConversationContext convCtx, String conversationID, Component component, Map<String, Component> data, List<Component> actions) {
        if (Minecraft.getInstance().screen instanceof NPCDialogueGui<?> gui) {
            gui.updateConversation(Minecraft.getInstance(), convCtx, conversationID, component, data, actions);
        } else {
            NPCDialogueGui<EntityNPCBase> gui = new NPCDialogueGui<>(npc);
            gui.updateConversation(Minecraft.getInstance(), convCtx, conversationID, component, data, actions);
            Minecraft.getInstance().setScreen(gui);
        }
    }

    public static void openQuestGui(boolean hasActive, List<ClientSideQuestDisplay> quests) {
        Minecraft.getInstance().setScreen(new QuestGui(hasActive, quests));
    }

    public static void openSpawneggGui(InteractionHand hand) {
        Minecraft.getInstance().setScreen(new SpawnEggScreen(hand));
    }

    /**
     * Translates a rider to the current root position. Assumes the model is either a vanilla humanoid one or a custom SittingModel
     * In this version vanilla uses a mix of relative height and absolute value on entities for the passenger position which makes supporting all entities very hard.
     * This changes in future versions so for now only humanoid models supported
     * <p>
     * Additionally in vanilla the legs simply get rotated to indicate a sitting pose while the whole model still floats in the air
     */
    public static void translateRider(EntityRenderer<?> entityRenderer, Entity rider, EntityModel<?> model, PoseStack poseStack) {
        float scale = 1;
        if (entityRenderer instanceof ScaledRenderer scaledRender) {
            scale = scaledRender.getScale();
        }
        if (scale != 1)
            poseStack.scale(1 / scale, 1 / scale, 1 / scale);
        if (model instanceof SittingModel sittingModel)
            sittingModel.translateSittingPosition(poseStack);
        else {
            if (rider instanceof LivingEntity living && living.isBaby()) {
                poseStack.translate(0, 5 / 16d, 0);
            } else {
                poseStack.translate(0, 11 / 16d, 0);
            }
        }
        if (scale != 1)
            poseStack.scale(scale, scale, scale);

    }
}
