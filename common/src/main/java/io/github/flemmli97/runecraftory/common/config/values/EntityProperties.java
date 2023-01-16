package io.github.flemmli97.runecraftory.common.config.values;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.github.flemmli97.runecraftory.common.lib.LibAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class EntityProperties {

    public static final EntityProperties defaultProp = new EntityProperties.Builder()
            .putAttributes(LibAttributes.MAX_HEALTH, 20)
            .putAttributes(LibAttributes.ATTACK_DAMAGE, 1)
            .xp(5).money(5).tamingChance(0.3f).build();

    private final List<String> confAttributes;
    private final List<String> confGains;
    private final int xp;
    private final int money;
    private final float taming;
    private final boolean ridable;
    private final boolean flying;
    private Map<Attribute, Double> baseValues;
    private Map<Attribute, Double> levelGains;
    private final int size;
    private final boolean needsRoof;

    private EntityProperties(List<String> baseValues, List<String> gains, int xp, int money, float tamingChance, boolean ridable, boolean flying, int size, boolean needsRoof) {
        this.confAttributes = baseValues;
        this.confGains = gains;
        this.xp = xp;
        this.money = money;
        this.taming = tamingChance;
        this.ridable = ridable;
        this.flying = flying;
        this.size = size;
        this.needsRoof = needsRoof;
    }

    public List<String> attString() {
        return Lists.newArrayList(this.confAttributes);
    }

    public Map<Attribute, Double> getBaseValues() {
        if (this.baseValues == null) {
            this.baseValues = new TreeMap<>(ModAttributes.SORTED);
            for (String s : this.confAttributes) {
                String[] sub = s.replace(" ", "").split("-");
                Attribute att = PlatformUtils.INSTANCE.attributes().getFromId(new ResourceLocation(sub[0]));
                if (att != null && PlatformUtils.INSTANCE.attributes().getIDFrom(att).toString().equals(sub[0]))
                    this.baseValues.put(att, Double.parseDouble(sub[1]));
            }
        }
        return ImmutableMap.copyOf(this.baseValues);
    }

    public List<String> gainString() {
        return Lists.newArrayList(this.confGains);
    }

    public Map<Attribute, Double> getAttributeGains() {
        if (this.levelGains == null) {
            this.levelGains = new TreeMap<>(ModAttributes.SORTED);
            for (String s : this.confGains) {
                String[] sub = s.replace(" ", "").split("-");
                Attribute att = PlatformUtils.INSTANCE.attributes().getFromId(new ResourceLocation(sub[0]));
                if (att != null && PlatformUtils.INSTANCE.attributes().getIDFrom(att).toString().equals(sub[0]))
                    this.levelGains.put(att, Double.parseDouble(sub[1]));
            }
        }
        return ImmutableMap.copyOf(this.levelGains);
    }

    public boolean ridable() {
        return this.ridable;
    }

    public boolean flying() {
        return this.flying;
    }

    public int getXp() {
        return this.xp;
    }

    public int getMoney() {
        return this.money;
    }

    public float tamingChance() {
        return this.taming;
    }

    public int getSize() {
        return this.size;
    }

    public boolean needsRoof() {
        return this.needsRoof;
    }

    public static class Builder {

        private final Set<String> baseValues = new LinkedHashSet<>();
        private final Set<String> gains = new LinkedHashSet<>();
        private int xp;
        private int money;
        private float taming;
        private boolean ridable;
        private boolean flying;
        private int size = 1;
        private boolean needsRoof = true;

        public Builder putAttributes(String att, double val) {
            this.baseValues.add(att + "-" + val);
            return this;
        }

        public Builder putAttributes(ResourceLocation att, double val) {
            return this.putAttributes(att.toString(), val);
        }

        public Builder putLevelGains(String att, double val) {
            this.gains.add(att + "-" + val);
            return this;
        }

        public Builder putLevelGains(ResourceLocation att, double val) {
            return this.putLevelGains(att.toString(), val);
        }

        public Builder xp(int xp) {
            this.xp = xp;
            return this;
        }

        public Builder money(int money) {
            this.money = money;
            return this;
        }

        public Builder tamingChance(float chance) {
            this.taming = chance;
            return this;
        }

        public Builder setRidable() {
            this.ridable = true;
            return this;
        }

        public Builder setFlying() {
            this.flying = true;
            return this;
        }

        public Builder setBarnOccupancy(int size) {
            this.size = Math.max(1, size);
            return this;
        }

        public Builder doesntNeedBarnRoof() {
            this.needsRoof = false;
            return this;
        }

        public EntityProperties build() {
            return new EntityProperties(Lists.newArrayList(this.baseValues), Lists.newArrayList(this.gains), this.xp, this.money, this.taming, this.ridable, this.flying, this.size, this.needsRoof);
        }
    }
}