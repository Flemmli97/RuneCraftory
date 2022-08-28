package io.github.flemmli97.runecraftory.api.datapack;

import com.google.common.collect.Sets;
import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.common.lib.LibAttributes;
import io.github.flemmli97.runecraftory.common.lib.LibNBT;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.ItemUtils;
import io.github.flemmli97.tenshilib.common.utils.MapUtils;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.SimpleRegistryWrapper;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ItemStat {

    private static final Set<ResourceLocation> flatAttributes = Sets.newHashSet(
            LibAttributes.GENERIC_ATTACK_DAMAGE,
            LibAttributes.rf_defence,
            LibAttributes.rf_magic,
            LibAttributes.rf_magic_defence);
    private final Map<Attribute, Double> itemStats = new TreeMap<>(ModAttributes.sorted);
    private int buyPrice;
    private int sellPrice;
    private int upgradeDifficulty;
    private EnumElement element;
    private Spell tier1Spell;
    private Spell tier2Spell;
    private Spell tier3Spell;

    public static ItemStat fromPacket(FriendlyByteBuf buffer) {
        ItemStat stat = new ItemStat();
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
        return new LinkedHashMap<>(this.itemStats);
    }

    public Spell getTier1Spell() {
        return this.tier1Spell;
    }

    public Spell getTier2Spell() {
        return this.tier2Spell;
    }

    public Spell getTier3Spell() {
        return this.tier3Spell;
    }

    /**
     * Writes this ItemStat to a buffer for synchronization. Spell upgrades are not synched
     */
    public void toPacket(FriendlyByteBuf buffer) {
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
        CompoundTag tag = ItemNBT.getItemNBT(stack);
        if (stack.getItem() instanceof IItemUsable && tag != null) {
            try {
                EnumElement element = EnumElement.valueOf(tag.getString(LibNBT.Element));
                if (element != EnumElement.NONE) {
                    list.add(new TranslatableComponent(element.getTranslation()).withStyle(element.getColor()));
                }
            } catch (IllegalArgumentException ignored) {
            }
        }
        MutableComponent price = tag != null ? new TranslatableComponent("tooltip.item.level", tag.getInt(LibNBT.Level)) : null;
        if (ItemUtils.getBuyPrice(stack, this) > 0) {
            if (price == null)
                price = new TranslatableComponent("tooltip.item.buy", ItemUtils.getBuyPrice(stack));
            else
                price.append(" ").append(new TranslatableComponent("tooltip.item.buy", ItemUtils.getBuyPrice(stack))).append(" ");
        }
        if (price == null)
            price = new TranslatableComponent("tooltip.item.sell", ItemUtils.getSellPrice(stack));
        else
            price.append(" ").append(new TranslatableComponent("tooltip.item.sell", ItemUtils.getSellPrice(stack))).append(" ");
        list.add(price.withStyle(ChatFormatting.YELLOW));
        if (!ItemNBT.shouldHaveStats(stack))
            list.add(new TranslatableComponent("tooltip.item.difficulty", this.getDiff()).withStyle(ChatFormatting.YELLOW));
        if (showStat) {
            Map<Attribute, Double> stats = ItemNBT.statBonusRaw(stack);
            if (!stats.isEmpty()) {
                String prefix = ItemNBT.shouldHaveStats(stack) ? "tooltip.item.equipped" : "tooltip.item.upgrade";
                list.add(new TranslatableComponent(prefix).withStyle(ChatFormatting.GRAY));
            }
            for (Map.Entry<Attribute, Double> entry : stats.entrySet()) {
                MutableComponent comp = new TextComponent(" ").append(new TranslatableComponent(entry.getKey().getDescriptionId())).append(new TextComponent(": " + this.format(entry.getKey(), entry.getValue().intValue())));
                list.add(comp.withStyle(ChatFormatting.BLUE));
            }
        }
        return list;
    }

    private String format(Attribute att, int n) {
        boolean flat = flatAttributes.contains(PlatformUtils.INSTANCE.attributes().getIDFrom(att));
        int val = flat && n < 0 ? -n : n;
        return (val >= 0 ? "+" + val : "" + val) + (flat ? "" : "%");
    }

    @Override
    public String toString() {
        return "[Buy:" + this.buyPrice + ";Sell:" + this.sellPrice + ";UpgradeDifficulty:" + this.upgradeDifficulty + ";DefaultElement:" + this.element + "];{stats:[" + MapUtils.toString(this.itemStats, reg -> PlatformUtils.INSTANCE.attributes().getIDFrom(reg).toString(), Object::toString) + "]}";
    }

    /**
     * Used in serialization
     */
    public static class MutableItemStat {

        private final Map<Attribute, Double> itemStats = new TreeMap<>(ModAttributes.sorted);
        private int buyPrice;
        private int sellPrice;
        private int upgradeDifficulty;
        private EnumElement element = EnumElement.NONE;
        private Spell tier1Spell;
        private Spell tier2Spell;
        private Spell tier3Spell;

        public MutableItemStat(int buy, int sell, int upgrade) {
            this.buyPrice = buy;
            this.sellPrice = sell;
            this.upgradeDifficulty = upgrade;
        }

        public MutableItemStat setElement(EnumElement element) {
            this.element = element;
            return this;
        }

        public MutableItemStat addAttribute(Attribute att, double value) {
            this.itemStats.put(att, value);
            return this;
        }

        public MutableItemStat setSpell(Spell tier1, Spell tier2, Spell tier3) {
            this.tier1Spell = tier1;
            this.tier2Spell = tier2;
            this.tier3Spell = tier3;
            return this;
        }
    }
}