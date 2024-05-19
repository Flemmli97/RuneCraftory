package io.github.flemmli97.runecraftory.integration.simplequest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.simplequests.JsonCodecs;
import io.github.flemmli97.simplequests.api.QuestEntry;
import io.github.flemmli97.simplequests.mixin.ItemPredicateAccessor;
import io.github.flemmli97.simplequests.quest.types.QuestBase;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class QuestTasks {

    public static class ShippingEntry implements QuestEntry {

        public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "shipping");
        public static final Codec<ShippingEntry> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(JsonCodecs.ITEM_PREDICATE_CODEC.fieldOf("predicate").forGetter(d -> d.predicate),
                        ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(d -> d.amount),
                        Codec.STRING.optionalFieldOf("description").forGetter(d -> d.description == null ? Optional.empty() : Optional.of(d.description.getString()))
                ).apply(instance, (pred, amount, desc) -> new ShippingEntry(pred, amount, desc.orElse(""))));

        public final ItemPredicate predicate;
        public final int amount;
        public final MutableComponent description;

        public ShippingEntry(ItemPredicate predicate, int amount) {
            this(predicate, amount, "");
        }

        public ShippingEntry(ItemPredicate predicate, int amount, String description) {
            this.predicate = predicate;
            this.amount = amount;
            this.description = description.isEmpty() ? null : new TranslatableComponent(description);
        }

        @Override
        public boolean submit(ServerPlayer player) {
            return false;
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public MutableComponent translation(ServerPlayer player) {
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
        public static final Codec<LevelEntry> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(ExtraCodecs.POSITIVE_INT.fieldOf("level").forGetter(d -> d.level)).apply(instance, LevelEntry::new));

        @Override
        public boolean submit(ServerPlayer player) {
            return Platform.INSTANCE.getPlayerData(player).map(d -> d.getPlayerLevel().getLevel() >= this.level).orElse(false);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public MutableComponent translation(ServerPlayer player) {
            return new TranslatableComponent(this.getId().toString(), this.level);
        }
    }

    public record SkillLevelEntry(EnumSkills skill, int level) implements QuestEntry {

        public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "skill_level");
        public static final Codec<SkillLevelEntry> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(
                        CodecUtils.stringEnumCodec(EnumSkills.class, null).fieldOf("skill").forGetter(d -> d.skill),
                        ExtraCodecs.POSITIVE_INT.fieldOf("level").forGetter(d -> d.level)).apply(instance, SkillLevelEntry::new));

        @Override
        public boolean submit(ServerPlayer player) {
            return Platform.INSTANCE.getPlayerData(player).map(d -> d.getSkillLevel(this.skill).getLevel() >= this.level).orElse(false);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public MutableComponent translation(ServerPlayer player) {
            return new TranslatableComponent(this.getId().toString(), this.skill, this.level);
        }
    }

    public record TamingEntry(EntityPredicate predicate, int amount, String description) implements QuestEntry {

        public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "taming");
        public static final Codec<TamingEntry> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(JsonCodecs.ENTITY_PREDICATE_CODEC.optionalFieldOf("predicate").forGetter(d -> d.predicate == EntityPredicate.ANY ? Optional.empty() : Optional.of(d.predicate)),
                        ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(d -> d.amount),
                        Codec.STRING.optionalFieldOf("description").forGetter(d -> d.description.isEmpty() ? Optional.empty() : Optional.of(d.description))
                ).apply(instance, (pred, amount, desc) -> new TamingEntry(pred.orElse(EntityPredicate.ANY), amount, desc.orElse(""))));

        @Override
        public boolean submit(ServerPlayer player) {
            return false;
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public MutableComponent translation(ServerPlayer player) {
            return new TranslatableComponent(this.getId().toString(), this.description);
        }
    }

    public static class NPCTalk implements QuestEntry {

        public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "npc_talk");
        public static final Codec<NPCTalk> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(ResourceLocation.CODEC.optionalFieldOf("targetNPCId").forGetter(d -> Optional.ofNullable(d.targetNPCId)),
                                Codec.STRING.optionalFieldOf("targetNPC").forGetter(d -> d.targetNPC != null ? Optional.of(d.targetNPC.toString()) : Optional.empty()))
                        .apply(instance, (generic, target) -> new NPCTalk(generic.orElse(null), target.map(UUID::fromString).orElse(null))));

        private final ResourceLocation targetNPCId;

        public final UUID targetNPC;
        private EntityNPCBase npc;

        public NPCTalk(ResourceLocation generic) {
            this(generic, null);
        }

        protected NPCTalk(ResourceLocation generic, UUID targetNPC) {
            this.targetNPCId = generic;
            this.targetNPC = targetNPC;
        }

        @Override
        public boolean submit(ServerPlayer player) {
            return false;
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public MutableComponent translation(ServerPlayer player) {
            if (this.npc == null || !this.npc.isAlive()) {
                if (this.targetNPC == null)
                    this.npc = null;
                else
                    this.npc = EntityUtil.findFromUUID(EntityNPCBase.class, player.getLevel(), this.targetNPC);
            }
            if (this.npc != null)
                return new TranslatableComponent(this.getId().toString(), this.npc.getCustomName());
            return new TranslatableComponent(this.getId().toString());
        }

        @Override
        public QuestEntry resolve(ServerPlayer player, QuestBase quest) {
            if (quest instanceof NPCQuest npcQuest)
                return new NPCTalk(this.targetNPCId, npcQuest.getNpcUuid());
            return new NPCTalk(this.targetNPCId, this.targetNPC);
        }
    }
}
