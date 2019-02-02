package com.flemmli97.runecraftory.api.items;

import java.util.Map;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.common.utils.MapWrapper;
import com.flemmli97.tenshilib.common.javahelper.ArrayUtils;
import com.google.common.collect.Maps;

import net.minecraft.entity.ai.attributes.IAttribute;

public class FoodProperties {//implements IJsonSerializable{
    
    private int hpRegen, rpRegen, hpRegenPercent, rpRegenPercent, duration;
    private Map<IAttribute, Integer> effects;
    private Map<IAttribute, Float> effectsPercent;
    private String[] potionApply;
    private String[] potionRemove;
    
    public FoodProperties(int hp, int rp, int percentHp, int percentRp, int duration,  Map<IAttribute, Integer> stats,  Map<IAttribute, Float> statsMulti, @Nullable String[] potionHeal, @Nullable String[] potionApply) {
        this.hpRegen = hp;
        this.rpRegen = rp;
        this.hpRegenPercent=percentHp;
        this.rpRegenPercent=percentRp;
        this.duration = duration;
        this.effects = MapWrapper.sort(stats, new ItemStatAttributes.Sort());
        this.effectsPercent = MapWrapper.sort(statsMulti, new ItemStatAttributes.Sort());
        this.potionRemove=potionHeal!=null?potionHeal:new String[0];
        this.potionApply=potionApply!=null?potionApply:new String[0];
    }
    
    /*public FoodProperties(JsonObject obj) {
        this.fromJson(obj);
    }*/
    
    public int getHPGain() {
        return this.hpRegen;
    }
    
    public int getRPRegen() {
        return this.rpRegen;
    }
    
    public int getHpPercentGain() {
        return this.hpRegenPercent;
    }
    
    public int getRpPercentRegen() {
        return this.rpRegenPercent;
    }
    
    public int duration() {
        return this.duration;
    }
    
    public Map<IAttribute, Integer> effects() {
        return Maps.newLinkedHashMap(this.effects);
    }
    
    public Map<IAttribute, Float> effectsMultiplier() {
        return Maps.newLinkedHashMap(this.effectsPercent);
    }
    
    public String[] potionHeals()
    {
    	return this.potionRemove;
    }
    
    public String[] potionApply()
    {
    	return this.potionApply;
    }
    
    @Override
    public String toString() {
        return "[HP:" + this.hpRegen + ",RP:" + this.rpRegen +",HP%:"+this.hpRegenPercent+",RP%:"+this.rpRegenPercent + ",Duration:" + this.duration + "]" +"{effects:["+ this.effects + "], potions:[" + ArrayUtils.arrayToString(this.potionRemove, null)+"]";
    }

	/*@Override
	public void fromJson(JsonElement json) {
		if(json.isJsonObject())
		{
			JsonObject obj = json.getAsJsonObject();
			this.hp=obj.get("hp").getAsInt();
			this.rp=obj.get("rp").getAsInt();
			this.hpPercent=obj.get("hpPercent").getAsInt();
			this.rpPercent=obj.get("rpPercent").getAsInt();
			this.duration=obj.get("duration").getAsInt();
			Map<IAttribute, Integer> stats=Maps.newHashMap();
			JsonObject statsObj = obj.getAsJsonObject("stats");
			statsObj.entrySet().forEach(entry->{
				stats.put(ItemUtils.getAttFromName(entry.getKey()), entry.getValue().getAsInt());
			});
			this.map=MapWrapper.sort(stats, new ItemStatAttributes.Sort());
			Map<IAttribute, Float> statMulti=Maps.newHashMap();
			JsonObject multiObj = obj.getAsJsonObject("statsMulti");
			multiObj.entrySet().forEach(entry->{
				statMulti.put(ItemUtils.getAttFromName(entry.getKey()), entry.getValue().getAsFloat());
			});
			this.mapMulti=MapWrapper.sort(statMulti, new ItemStatAttributes.Sort());
			JsonArray arr = obj.get("potions").getAsJsonArray();
			String[] potions = new String[arr.size()];
			for(int i = 0; i < potions.length; i++)
				potions[i]=arr.get(i).getAsString();
			this.potionApply=potions;
			JsonArray arr2 = obj.get("potionHeal").getAsJsonArray();
			String[] potionsHeal = new String[arr2.size()];
			for(int i = 0; i < potionsHeal.length; i++)
				potionsHeal[i]=arr2.get(i).getAsString();
			this.potionHeal=potionsHeal;
		}
	}

	@Override
	public JsonElement getSerializableElement() {
		JsonObject obj = new JsonObject();
		obj.add("hp", new JsonPrimitive(this.hp));
		obj.add("rp", new JsonPrimitive(this.hp));
		obj.add("hpPercent", new JsonPrimitive(this.hp));
		obj.add("rpPercent", new JsonPrimitive(this.hp));
		obj.add("duration", new JsonPrimitive(this.hp));
		JsonObject stat = new JsonObject();
		this.map.forEach((att,i)->{
			stat.add(att.getName(), new JsonPrimitive(i));
		});
		JsonObject statMulti = new JsonObject();
		this.mapMulti.forEach((att,f)->{
			statMulti.add(att.getName(), new JsonPrimitive(f));
		});
		JsonArray potionClear = new JsonArray();
		if(this.potionHeal!=null)
			for(String s : this.potionHeal)
				potionClear.add(s);
		JsonArray potion = new JsonArray();
		if(this.potionApply!=null)
			for(String s : this.potionApply)
				potionClear.add(s);
		obj.add("stats", stat);
		obj.add("statsMulti", statMulti);
		obj.add("potions", potion);
		obj.add("potionHeal", potionClear);
		return obj;
	}*/
}
