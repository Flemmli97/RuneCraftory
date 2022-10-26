package io.github.flemmli97.runecraftory.api.datapack;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.common.lib.LibAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.CodecHelper;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.ItemUtils;
import io.github.flemmli97.tenshilib.common.utils.MapUtils;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.SimpleRegistryWrapper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

public class ItemStat {

    public static final Codec<ItemStat> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    CodecHelper.enumCodec(EnumElement.class, EnumElement.NONE).orElse(EnumElement.NONE).fieldOf("element").forGetter(ItemStat::element),
                    CodecHelper.ofCustomRegistry(ModSpells.SPELLREGISTRY, ModSpells.SPELLREGISTRY_KEY).optionalFieldOf("tier1Spell").forGetter(s -> Optional.ofNullable(s.getTier1Spell())),
                    CodecHelper.ofCustomRegistry(ModSpells.SPELLREGISTRY, ModSpells.SPELLREGISTRY_KEY).optionalFieldOf("tier2Spell").forGetter(s -> Optional.ofNullable(s.getTier2Spell())),
                    CodecHelper.ofCustomRegistry(ModSpells.SPELLREGISTRY, ModSpells.SPELLREGISTRY_KEY).optionalFieldOf("tier3Spell").forGetter(s -> Optional.ofNullable(s.getTier3Spell())),

                    Codec.unboundedMap(Registry.ATTRIBUTE.byNameCodec(), Codec.DOUBLE).fieldOf("itemStats").forGetter(ItemStat::itemStats),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("buyPrice").forGetter(ItemStat::getBuy),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("sellPrice").forGetter(ItemStat::getSell),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("upgradeDifficulty").forGetter(ItemStat::getDiff)
            ).apply(instance, ((element, spell, spell2, spell3, atts, buy, sell, upgrade) ->
                    new ItemStat(buy, sell, upgrade, element, spell.orElse(null), spell2.orElse(null), spell3.orElse(null), atts))));

    private static final Set<ResourceLocation> flatAttributes = Sets.newHashSet(
            new ResourceLocation("generic.max_health"),
            LibAttributes.GENERIC_ATTACK_DAMAGE,
            LibAttributes.rf_defence,
            LibAttributes.rf_magic,
            LibAttributes.rf_magic_defence);

    private final Map<Attribute, Double> itemStats;
    private int buyPrice;
    private int sellPrice;
    private int upgradeDifficulty;
    private EnumElement element = EnumElement.NONE;
    private Spell tier1Spell;
    private Spell tier2Spell;
    private Spell tier3Spell;

    private transient Map<Attribute, Double> itemStatView;
    private transient ResourceLocation id;

    private ItemStat() {
        this.itemStats = new HashMap<>();
    }

    private ItemStat(int buyPrice, int sellPrice, int upgradeDifficulty, EnumElement element, Spell tier1Spell, Spell tier2Spell, Spell tier3Spell, Map<Attribute, Double> itemStats) {
        this.itemStats = itemStats;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.upgradeDifficulty = upgradeDifficulty;
        this.element = element;
        this.tier1Spell = tier1Spell;
        this.tier2Spell = tier2Spell;
        this.tier3Spell = tier3Spell;
    }

    public static ItemStat fromPacket(FriendlyByteBuf buffer) {
        ItemStat stat = new ItemStat();
        stat.id = buffer.readResourceLocation();
        stat.buyPrice = buffer.readInt();
        stat.sellPrice = buffer.readInt();
        stat.upgradeDifficulty = buffer.readInt();
        stat.element = buffer.readEnum(EnumElement.class);
        int size = buffer.readInt();
        for (int i = 0; i < size; i++)
            stat.itemStats.put(PlatformUtils.INSTANCE.attributes().getFromId(buffer.readResourceLocation()), buffer.readDouble());
        SimpleRegistryWrapper<Spell> spellRegistry = PlatformUtils.INSTANCE.registry(ModSpells.SPELLREGISTRY_KEY);
        if (buffer.readBoolean())
            stat.tier1Spell = spellRegistry.getFromId(buffer.readResourceLocation());
        if (buffer.readBoolean())
            stat.tier2Spell = spellRegistry.getFromId(buffer.readResourceLocation());
        if (buffer.readBoolean())
            stat.tier3Spell = spellRegistry.getFromId(buffer.readResourceLocation());
        return stat;
    }

    public void setID(ResourceLocation id) {
        if (this.id == null)
            this.id = id;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public int getBuy() {
        return this.buyPrice;
    }

    public int getSell() {
        return this.sellPrice;
    }

    public int getDiff() {
        return this.upgradeDifficulty;
    }

    public EnumElement element() {
        return this.element;
    }

    public Map<Attribute, Double> itemStats() {
        if (this.itemStatView == null) {
            this.itemStatView = new TreeMap<>(ModAttributes.sorted);
            this.itemStatView.putAll(this.itemStats);
        }
        return this.itemStatView;
    }

    @Nullable
    public Spell getTier1Spell() {
        return this.tier1Spell;
    }

    @Nullable
    public Spell getTier2Spell() {
        return this.tier2Spell;
    }

    @Nullable
    public Spell getTier3Spell() {
        return this.tier3Spell;
    }

    /**
     * Writes this ItemStat to a buffer for synchronization. Spell upgrades are not synched
     */
    public void toPacket(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(this.id);
        buffer.writeInt(this.buyPrice);
        buffer.writeInt(this.sellPrice);
        buffer.writeInt(this.upgradeDifficulty);
        buffer.writeEnum(this.element);
        buffer.writeInt(this.itemStats.size());
        this.itemStats.forEach((att, val) -> {
            buffer.writeResourceLocation(PlatformUtils.INSTANCE.attributes().getIDFrom(att));
            buffer.writeDouble(val);
        });
        buffer.writeBoolean(this.tier1Spell != null);
        if (this.tier1Spell != null)
            buffer.writeResourceLocation(this.tier1Spell.getRegistryName());
        buffer.writeBoolean(this.tier2Spell != null);
        if (this.tier2Spell != null)
            buffer.writeResourceLocation(this.tier2Spell.getRegistryName());
        buffer.writeBoolean(this.tier3Spell != null);
        if (this.tier3Spell != null)
            buffer.writeResourceLocation(this.tier3Spell.getRegistryName());
    }

    public List<Component> texts(ItemStack stack, boolean showStat) {
        List<Component> list = new ArrayList<>();
        if (stack.getItem() instanceof IItemUsable) {
            EnumElement element = ItemNBT.getElement(stack);
            if (element != EnumElement.NONE) {
                list.add(new TranslatableComponent(element.getTranslation()).withStyle(element.getColor()));
            }
        }
        MutableComponent price = ItemNBT.shouldHaveLevel(stack) ? new TranslatableComponent("tooltip.item.level", ItemNBT.itemLevel(stack)) : null;
        if (ItemUtils.getBuyPrice(stack, this) > 0) {
            if (price == null)
                price = new TranslatableComponent("tooltip.item.buy", ItemUtils.getBuyPrice(stack, this));
            else
                price.append(" ").append(new TranslatableComponent("tooltip.item.buy", ItemUtils.getBuyPrice(stack, this))).append(" ");
        }
        if (ItemUtils.getSellPrice(stack, this) > 0) {
            if (price == null)
                price = new TranslatableComponent("tooltip.item.sell", ItemUtils.getSellPrice(stack, this));
            else
                price.append(" ").append(new TranslatableComponent("tooltip.item.sell", ItemUtils.getSellPrice(stack, this)));
        }
        if (price != null)
            list.add(price.withStyle(ChatFormatting.YELLOW));
        boolean shouldHaveStats = ItemNBT.shouldHaveStats(stack);
        if (!shouldHaveStats && this.getDiff() > 0)
            list.add(new TranslatableComponent("tooltip.item.difficulty", this.getDiff()).withStyle(ChatFormatting.YELLOW));
        if (showStat) {
            Map<Attribute, Double> stats = ItemNBT.statIncrease(stack);
            if (!stats.isEmpty()) {
                String prefix = shouldHaveStats ? "tooltip.item.equipped" : "tooltip.item.upgrade";
                list.add(new TranslatableComponent(prefix).withStyle(ChatFormatting.GRAY));
            }
            for (Map.Entry<Attribute, Double> entry : stats.entrySet()) {
                MutableComponent comp = new TextComponent(" ").append(new TranslatableComponent(entry.getKey().getDescriptionId())).append(new TextComponent(": " + this.format(entry.getKey(), entry.getValue())));
                list.add(comp.withStyle(ChatFormatting.BLUE));
            }
        }
        return list;
    }

    private String format(Attribute att, double n) {
        if (att == Attributes.MOVEMENT_SPEED) {
            float f = ((int) (n * 100)) / 100f;
            return (f >= 0 ? "+" + f : "" + f);
        }
        boolean flat = flatAttributes.contains(PlatformUtils.INSTANCE.attributes().getIDFrom(att));
        int val = (int) (flat && n < 0 ? -n : n);
        return (val >= 0 ? "+" + val : "" + val) + (flat ? "" : "%");
    }

    @Override
    public String toString() {
        String s = "[Buy:" + this.buyPrice + ";Sell:" + this.sellPrice + ";UpgradeDifficulty:" + this.upgradeDifficulty + ";DefaultElement:" + this.element + "];{stats:[" + MapUtils.toString(this.itemStats, reg -> PlatformUtils.INSTANCE.attributes().getIDFrom(reg).toString(), Object::toString) + "]}";
        if (this.id != null)
            s = this.id + ":" + s;
        return s;
    }

    public static class Builder {

        private final Map<Attribute, Double> itemStats = new HashMap<>();
        private final int buyPrice;
        private final int sellPrice;
        private final int upgradeDifficulty;
        private EnumElement element = EnumElement.NONE;
        private Spell tier1Spell;
        private Spell tier2Spell;
        private Spell tier3Spell;

        public Builder(int buy, int sell, int upgrade) {
            this.buyPrice = buy;
            this.sellPrice = sell;
            this.upgradeDifficulty = upgrade;
        }

        public Builder setElement(EnumElement element) {
            this.element = element;
            return this;
        }

        public Builder addAttribute(Attribute att, double value) {
            this.itemStats.put(att, value);
            return this;
        }

        public Builder setSpell(Spell tier1, Spell tier2, Spell tier3) {
            this.tier1Spell = tier1;
            this.tier2Spell = tier2;
            this.tier3Spell = tier3;
            return this;
        }

        public ItemStat build() {
            return new ItemStat(this.buyPrice, this.sellPrice, this.upgradeDifficulty, this.element, this.tier1Spell, this.tier2Spell, this.tier3Spell, this.itemStats);
        }
    }
}