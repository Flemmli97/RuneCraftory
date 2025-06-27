package io.github.flemmli97.runecraftory.client.gui.widgets;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.resources.ResourceLocation;

public class SpriteResources {

    public static final ResourceLocation ATTACK_ICON = RuneCraftory.modRes("icon/attack");
    public static final ResourceLocation DEFENCE_ICON = RuneCraftory.modRes("icon/defence");
    public static final ResourceLocation MAGIC_ATTACK_ICON = RuneCraftory.modRes("icon/magic_attack");
    public static final ResourceLocation MAGIC_DEFENCE_ICON = RuneCraftory.modRes("icon/magic_defence");
    public static final ResourceLocation FRIENDSHIP_ICON = RuneCraftory.modRes("icon/friendship");
    public static final ResourceLocation HEART_ICON = RuneCraftory.modRes("icon/heart");

    public static final ResourceLocation HEARTH_LETTER_ICON = RuneCraftory.modRes("icon/hearth_letter");
    public static final ResourceLocation ENGAGEMENT_RING_ICON = RuneCraftory.modRes("icon/engagement_ring");

    public static final ResourceLocation MONEY_ICON = RuneCraftory.modRes("icon/money");

    public static final WidgetSprites PAGE_BUTTON = new WidgetSprites(RuneCraftory.modRes("widget/page_button"),
            RuneCraftory.modRes("widget/page_button_highlighted"));
}
