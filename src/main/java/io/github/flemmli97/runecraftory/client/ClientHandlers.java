package io.github.flemmli97.runecraftory.client;

import io.github.flemmli97.runecraftory.client.gui.OverlayGui;
import io.github.flemmli97.runecraftory.client.gui.SpellInvOverlayGui;
import io.github.flemmli97.runecraftory.common.utils.CalendarImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.RecipeToast;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;

public class ClientHandlers {

    public static final CalendarImpl clientCalendar = new CalendarImpl();

    public static OverlayGui overlay;
    public static SpellInvOverlayGui spellDisplay;
    public static TriggerKeyBind spell1;
    public static TriggerKeyBind spell2;
    public static TriggerKeyBind spell3;
    public static TriggerKeyBind spell4;

    public static PlayerEntity getPlayer() {
        return Minecraft.getInstance().player;
    }

    public static void updateClientCalendar(PacketBuffer buffer) {
        clientCalendar.fromPacket(buffer);
    }

    public static void recipeToast(Collection<ResourceLocation> recipes) {
        recipes.forEach(res -> Minecraft.getInstance().world.getRecipeManager().getRecipe(res).ifPresent(rec ->
                RecipeToast.addOrUpdate(Minecraft.getInstance().getToastGui(), rec)));
    }
}
