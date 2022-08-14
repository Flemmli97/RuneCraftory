package io.github.flemmli97.runecraftory.client;

import io.github.flemmli97.runecraftory.client.gui.OverlayGui;
import io.github.flemmli97.runecraftory.client.gui.SpellInvOverlayGui;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.utils.CalendarImpl;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.RecipeToast;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;

public class ClientHandlers {

    public static final CalendarImpl clientCalendar = new CalendarImpl();

    public static OverlayGui overlay;
    public static SpellInvOverlayGui spellDisplay;
    public static TriggerKeyBind spell1;
    public static TriggerKeyBind spell2;
    public static TriggerKeyBind spell3;
    public static TriggerKeyBind spell4;

    public static int shakeTick;

    private static CameraType pastType = CameraType.FIRST_PERSON;

    public static Player getPlayer() {
        return Minecraft.getInstance().player;
    }

    public static void updateClientCalendar(FriendlyByteBuf buffer) {
        clientCalendar.fromPacket(buffer);
    }

    public static void grabMouse() {
        Minecraft.getInstance().mouseHandler.grabMouse();
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
}
