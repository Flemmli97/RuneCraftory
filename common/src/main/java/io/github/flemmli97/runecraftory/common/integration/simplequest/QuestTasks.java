package io.github.flemmli97.runecraftory.common.integration.simplequest;

import com.google.gson.JsonObject;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.simplequests.mixin.ItemPredicateAccessor;
import io.github.flemmli97.simplequests.quest.QuestEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;

import java.util.ArrayList;
import java.util.List;

public class QuestTasks {

    public static class ShippingEntry implements QuestEntry {

        public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "shipping");

        public final ItemPredicate predicate;
        public final int amount;
        public final MutableComponent description;

        public ShippingEntry(ItemPredicate predicate, int amount) {
            this(predicate, amount, "");
        }

        public ShippingEntry(ItemPredicate predicate, int amount, String description) {
            this.predicate = predicate;
            this.amount = amount;
            this.description = description.isEmpty() ? null : new TextComponent(description);
        }

        @Override
        public boolean submit(ServerPlayer player) {
            return false;
        }

        @Override
        public JsonObject serialize() {
            JsonObject obj = new JsonObject();
            obj.add("predicate", this.predicate.serializeToJson());
            if (this.description != null)
                obj.addProperty("description", this.description.getString());
            obj.addProperty("amount", this.amount);
            return obj;
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public MutableComponent translation(MinecraftServer server) {
            if (this.description != null)
                return this.description;
            List<MutableComponent> formattedItems = itemComponents(this.predicate);
            if (formattedItems.isEmpty())
                return new TranslatableComponent(this.getId().toString() + ".empty");
            if (formattedItems.size() == 1) {
                return new TranslatableComponent(this.getId().toString() + ".single", formattedItems.get(0).withStyle(ChatFormatting.AQUA), this.amount);
            }
            MutableComponent items = null;
            for (MutableComponent c : formattedItems) {
                if (items == null)
                    items = new TextComponent("[").append(c);
                else
                    items.append(new TextComponent(", ")).append(c);
            }
            items.append("]");
            return new TranslatableComponent(this.getId().toString() + ".multi", items.withStyle(ChatFormatting.AQUA), this.amount);
        }

        public static ShippingEntry fromJson(JsonObject obj) {
            return new ShippingEntry(ItemPredicate.fromJson(GsonHelper.getAsJsonObject(obj, "predicate")), obj.get("amount").getAsInt(),
                    GsonHelper.getAsString(obj, "description", ""));
        }

        public static List<MutableComponent> itemComponents(ItemPredicate predicate) {
            ItemPredicateAccessor acc = (ItemPredicateAccessor) predicate;
            List<MutableComponent> formattedItems = new ArrayList<>();
            if (acc.getItems() != null)
                acc.getItems().forEach(i -> formattedItems.add(new TranslatableComponent(i.getDescriptionId())));
            if (acc.getTag() != null)
                Registry.ITEM.getTag(acc.getTag()).ifPresent(n -> n.forEach(h -> formattedItems.add(new TranslatableComponent(h.value().getDescriptionId()))));
            return formattedItems;
        }
    }

    public record LevelEntry(int level) implements QuestEntry {

        public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "level");

        @Override
        public boolean submit(ServerPlayer player) {
            return Platform.INSTANCE.getPlayerData(player).map(d -> d.getPlayerLevel().getLevel() >= this.level).orElse(false);
        }

        @Override
        public JsonObject serialize() {
            JsonObject obj = new JsonObject();
            obj.addProperty("level", this.level);
            return obj;
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public MutableComponent translation(MinecraftServer server) {
            return new TranslatableComponent(this.getId().toString(), this.level);
        }

        public static LevelEntry fromJson(JsonObject obj) {
            return new LevelEntry(GsonHelper.getAsInt(obj, "level"));
        }
    }

    public record SkillLevelEntry(EnumSkills skill, int level) implements QuestEntry {

        public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "skill_level");

        @Override
        public boolean submit(ServerPlayer player) {
            return Platform.INSTANCE.getPlayerData(player).map(d -> d.getSkillLevel(this.skill).getLevel() >= this.level).orElse(false);
        }

        @Override
        public JsonObject serialize() {
            JsonObject obj = new JsonObject();
            obj.addProperty("skill", this.skill.name());
            obj.addProperty("level", this.level);
            return obj;
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public MutableComponent translation(MinecraftServer server) {
            return new TranslatableComponent(this.getId().toString(), this.skill, this.level);
        }

        public static SkillLevelEntry fromJson(JsonObject obj) {
            return new SkillLevelEntry(EnumSkills.valueOf(GsonHelper.getAsString(obj, "skill")), GsonHelper.getAsInt(obj, "level"));
        }
    }

    public record TamingEntry(EntityPredicate predicate, int amount, String description) implements QuestEntry {

        public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "taming");

        @Override
        public boolean submit(ServerPlayer player) {
            return false;
        }

        @Override
        public JsonObject serialize() {
            JsonObject obj = new JsonObject();
            obj.add("entity", this.predicate.serializeToJson());
            obj.addProperty("amount", this.amount);
            obj.addProperty("description", this.description);
            return obj;
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public MutableComponent translation(MinecraftServer server) {
            return new TranslatableComponent(this.getId().toString(), this.description);
        }

        public static TamingEntry fromJson(JsonObject obj) {
            return new TamingEntry(EntityPredicate.fromJson(GsonHelper.getAsJsonObject(obj, "entity", null)),
                    GsonHelper.getAsInt(obj, "amount", 1),
                    GsonHelper.getAsString(obj, "description"));
        }
    }
}
