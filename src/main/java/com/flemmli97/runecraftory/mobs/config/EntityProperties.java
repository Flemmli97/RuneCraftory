package com.flemmli97.runecraftory.mobs.config;

import com.flemmli97.runecraftory.mobs.libs.LibAttributes;
import com.flemmli97.runecraftory.registry.ModAttributes;
import com.flemmli97.tenshilib.api.config.SimpleItemStackWrapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class EntityProperties {

    public static final EntityProperties defaultProp = new EntityProperties.Builder()
            .putAttributes(LibAttributes.GENERIC_MAX_HEALTH, 20)
            .putAttributes(LibAttributes.GENERIC_ATTACK_DAMAGE, 1)
            .xp(5).money(5).tamingChance(0.3f).build();

    private List<String> confAttributes;
    private Map<Attribute, Double> baseValues;
    private int xp;
    private int money;
    private float taming;
    private SimpleItemStackWrapper[] tamingItem;
    private final Map<SimpleItemStackWrapper, Integer> daily;
    private boolean ridable;
    private boolean flying;

    public EntityProperties(List<String> baseValues, int xp, int money, float tamingChance, SimpleItemStackWrapper[] tamingItem, Map<SimpleItemStackWrapper, Integer> dailyDrops, boolean ridable, boolean flying) {
        this.confAttributes = baseValues;
        this.xp = xp;
        this.money = money;
        this.taming = tamingChance;
        this.tamingItem = tamingItem;
        this.daily = dailyDrops;
        this.ridable = ridable;
        this.flying = flying;
    }

    public List<String> attString(){
        return this.confAttributes;
    }

    public Map<Attribute, Double> getBaseValues() {
        if(this.baseValues == null){
            this.baseValues = new TreeMap<>(ModAttributes.sorted);
            for (String s : this.confAttributes) {
                String[] sub = s.replace(" ", "").split("-");
                Attribute att = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(sub[0]));
                if (att != Attributes.GENERIC_LUCK)
                    this.baseValues.put(att, Double.parseDouble(sub[1]));
            }
        }
        return ImmutableMap.copyOf(this.baseValues);
    }

    public Map<SimpleItemStackWrapper, Integer> dailyDrops() {
        return this.daily;
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

    public SimpleItemStackWrapper[] getTamingItem() {
        return this.tamingItem;
    }

    public EntityProperties read(EntityPropertySpecs spec){
        try {
            this.baseValues = null;
            this.confAttributes = spec.baseValues.get();
            this.xp = spec.xp.get();
            this.money = spec.money.get();
            this.taming = spec.taming.get();
            List<String> tamings = spec.tamingItem.get();
            this.tamingItem = new SimpleItemStackWrapper[tamings.size()];
            for (int i = 0; i < this.tamingItem.length; i++)
                this.tamingItem[i] = new SimpleItemStackWrapper("", 0).readFromString(tamings.get(i));
            this.daily.clear();
            for (String s : spec.daily.get()) {
                String[] sub = s.replace(" ", "").split("-");
                SimpleItemStackWrapper stack = new SimpleItemStackWrapper("", 0).readFromString(sub[0]);
                this.daily.put(stack, Integer.parseInt(sub[1]));
            }
            this.ridable = spec.ridable.get();
            this.flying = spec.flying.get();
        }
        catch (Exception e){
            //throw new
        }
        return this;
    }
    /*@Override
    public EntityProperties config(Configuration config, String configCategory) {
        List<String> stats = Lists.newArrayList();
        this.baseValues.forEach((key, value) -> stats.add(key.getName() + ";" + value));
        List<String> drops = Lists.newArrayList();
        for (Entry<SimpleItemStackWrapper, Float> mapEntry : this.drops.entrySet()) {
            drops.add(mapEntry.getKey().toString() + ";" + mapEntry.getValue());
        }
        List<String> daily = Lists.newArrayList();
        for (Entry<SimpleItemStackWrapper, Integer> mapEntry : this.dailyDrops().entrySet()) {
            daily.add(mapEntry.getKey().toString() + ";" + mapEntry.getValue());
        }

        //Read from config
        this.baseValues.clear();
        for(String s : config.getStringList("Entity Stats", configCategory, stats.toArray(new String[0]), "Entity stats at level 5. Syntax is: \"attribute;value\""))
        {
            String[] sub = s.split(";");
            this.baseValues.put(ItemUtils.getAttFromName(sub[0]), Double.parseDouble(sub[1]));
        }
        this.drops.clear();
        for(String s : config.getStringList("Drops", configCategory, drops.toArray(new String[0]), "Syntax is \"item(,meta)(,amount);dropChance\""))
        {
            String[] sub = s.split(";");
            this.drops.put(new SimpleItemStackWrapper(sub[0]), Float.parseFloat(sub[1]));
        }
        this.xp = ConfigUtils.getIntConfig(config, "Base xp", configCategory, this.xp, 0, "Base xp when defeating this entity");
        this.money = ConfigUtils.getIntConfig(config, "Base money", configCategory, this.money, 0, "Base money when defeating this entity");
        this.taming = config.getFloat("Taming chance", configCategory, this.taming, 0.0f, 1.0f, "Base taming chance.");
        this.tamingItem = ArrayUtils.arrayConverter(config.getStringList("Taming Items", configCategory, ArrayUtils.arrayToStringArr(this.tamingItem), "Syntax is \"item(,meta)\""),
                SimpleItemStackWrapper::new,SimpleItemStackWrapper.class, false);
        this.daily.clear();
        for(String s : config.getStringList("Daily Products", configCategory, daily.toArray(new String[0]), "Syntax is \"item(,meta);hearts. Where hearts is the friendship minimum value for that drop\""))
        {
            String[] sub = s.split(";");
            this.daily.put(new SimpleItemStackWrapper(sub[0]), Integer.parseInt(sub[1]));
        }
        this.ridable = config.getBoolean("Ridable", configCategory, this.ridable, "If this entity is ridable");
        this.flying = config.getBoolean("Flying", configCategory, this.flying, "If this entity can fly");

        return this;
        //EntityStatMap.setDefaultStats((Class<? extends IEntityBase>) entry.getEntityClass(),
        //new EntityProperties(statMap, dropMap, baseXP, baseMoney, tamingChance,
        //		fromString.toArray(new ItemProperties[0]), dailyMap, ridable, canFly));
    }*/

    public static class Builder {

        private final List<String> baseValues = Lists.newArrayList();
        private int xp;
        private int money;
        private float taming;
        private final Set<SimpleItemStackWrapper> tamingItem = Sets.newHashSet();
        private final Map<SimpleItemStackWrapper, Integer> daily = Maps.newHashMap();
        private boolean ridable;
        private boolean flying;

        public Builder putAttributes(String att, double val){
            this.baseValues.add(att+" - " + val);
            return this;
        }

        public Builder putAttributes(ResourceLocation att, double val){
            this.baseValues.add(att+" - " + val);
            return this;
        }

        public Builder xp(int xp){
            this.xp = xp;
            return this;
        }

        public Builder money(int money){
            this.money = money;
            return this;
        }

        public Builder tamingChance(float chance){
            this.taming = chance;
            return this;
        }

        public Builder addTamingItem(SimpleItemStackWrapper itemstack){
            this.tamingItem.add(itemstack);
            return this;
        }

        public Builder addDaily(SimpleItemStackWrapper itemstack, int requiredHearts){
            this.daily.put(itemstack, requiredHearts);
            return this;
        }

        public Builder setRidable(){
            this.ridable = true;
            return this;
        }

        public Builder setFlying(){
            this.flying = true;
            return this;
        }

        public EntityProperties build(){
            return new EntityProperties(this.baseValues, this.xp, this.money, this.taming, this.tamingItem.toArray(new SimpleItemStackWrapper[0]), this.daily, this.ridable, this.flying);
        }
    }
}