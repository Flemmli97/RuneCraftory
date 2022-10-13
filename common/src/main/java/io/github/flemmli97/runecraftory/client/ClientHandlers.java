package io.github.flemmli97.runecraftory.client;

import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.client.gui.FarmlandInfo;
import io.github.flemmli97.runecraftory.client.gui.MonsterCompanionGui;
import io.github.flemmli97.runecraftory.client.gui.OverlayGui;
import io.github.flemmli97.runecraftory.client.gui.SpellInvOverlayGui;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.utils.CalendarImpl;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.RecipeToast;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Collection;

public class ClientHandlers {

    public static final CalendarImpl clientCalendar = new CalendarImpl();

    public static OverlayGui overlay;
    public static SpellInvOverlayGui spellDisplay;
    public static FarmlandInfo farmDisplay;

    public static TriggerKeyBind spell1;
    public static TriggerKeyBind spell2;
    public static TriggerKeyBind spell3;
    public static TriggerKeyBind spell4;

    public static boolean isRuneyWeather;

    private static CameraType pastType = CameraType.FIRST_PERSON;

    public static Player getPlayer() {
        return Minecraft.getInstance().player;
    }

    public static void updateClientCalendar(FriendlyByteBuf buffer) {
        EnumSeason prev = clientCalendar.currentSeason();
        clientCalendar.fromPacket(buffer);
        if (clientCalendar.currentSeason() != prev) {
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

    public static void setRuneyWeather(boolean runeyWeather) {
        isRuneyWeather = runeyWeather;
    }

    public static void grabMouse(LivingEntity entity) {
        if (entity == Minecraft.getInstance().player && Minecraft.getInstance().screen == null)
            Minecraft.getInstance().mouseHandler.grabMouse();
    }

    public static boolean disableMouse() {
        Minecraft mc = Minecraft.getInstance();
        return mc.player != null && EntityUtils.isDisabled(mc.player) && (mc.screen == null || mc.screen instanceof AbstractContainerScreen<?>);
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

    public static void openCompanionGui(int id) {
        Entity entity = Minecraft.getInstance().level.getEntity(id);
        if (entity instanceof BaseMonster monster) {
            if (!Minecraft.getInstance().player.getUUID().equals(monster.getOwnerUUID()))
                return;
            Minecraft.getInstance().setScreen(new MonsterCompanionGui(monster));
        }
    }
}
