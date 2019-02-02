package com.flemmli97.runecraftory.api.items;

import java.util.Map;

import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.flemmli97.runecraftory.common.utils.MapWrapper;
import com.google.common.collect.ImmutableMap;

import net.minecraft.entity.ai.attributes.IAttribute;

public class ItemStat //implements IJsonSerializable
{
    private int buyPrice;
    private int sellPrice;
    private int upgradeDifficulty;
    private EnumElement element;
    private Map<IAttribute, Integer> itemStats;
    
    public ItemStat(int buy, int sell, int upgradeDiff, EnumElement element, Map<IAttribute, Integer> stats) {
        this.buyPrice = buy;
        this.sellPrice = sell;
        this.upgradeDifficulty = upgradeDiff;
        this.element = element;
        this.itemStats = MapWrapper.sort(stats, new ItemStatAttributes.Sort());
    }
    
    /*public ItemStat(JsonObject obj)
    {
    	this.fromJson(obj);
    }*/
    
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
    
    public Map<IAttribute, Integer> itemStats() {
        return ImmutableMap.copyOf(this.itemStats);
    }
    
    @Override
    public String toString() {
        return "[Buy:" + this.buyPrice + ";Sell:" + this.sellPrice + ";UpgradeDifficulty:" + this.upgradeDifficulty + ";DefaultElement:" + this.element + "];{stats:["+this.itemStats+"]}";
    }

	/*@Override
	public void fromJson(JsonElement json) {
		if(json instanceof JsonObject)
		{
			JsonObject obj = (JsonObject) json;
			this.buyPrice=obj.get("buy").getAsInt();
			this.sellPrice=obj.get("sell").getAsInt();
			this.upgradeDifficulty=obj.get("upgradeDifficulty").getAsInt();
			this.element=EnumElement.fromName(obj.get("element").getAsString());
			
			Map<IAttribute, Integer> stats=Maps.newHashMap();
			JsonObject map = (JsonObject) obj.get("stats");
			map.entrySet().forEach(entry->{
				stats.put(ItemUtils.getAttFromName(entry.getKey()), entry.getValue().getAsInt());
			});
			this.itemStats=MapWrapper.sort(stats, new ItemStatAttributes.Sort());
		}
	}

	@Override
	public JsonElement getSerializableElement() {
		JsonObject obj = new JsonObject();
		obj.add("buy", new JsonPrimitive(this.buyPrice));
		obj.add("sell", new JsonPrimitive(this.sellPrice));
		obj.add("upgradeDifficulty", new JsonPrimitive(this.upgradeDifficulty));
		obj.add("element", new JsonPrimitive(this.element.getName()));
		JsonObject map = new JsonObject();
		this.itemStats.forEach((att,i)->{
			map.add(att.getName(), new JsonPrimitive(i));
		});
		obj.add("stats", map);
		return obj;
	}*/
}
