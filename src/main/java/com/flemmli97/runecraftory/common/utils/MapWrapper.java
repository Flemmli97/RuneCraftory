package com.flemmli97.runecraftory.common.utils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MapWrapper<T, V> extends HashMap<T, V>
{
	private static final long serialVersionUID = 1143326767918321316L;
    
    public MapWrapper<T, V> mapWrapperAdd(T t, V v) {
        this.put(t, v);
        return this;
    }
    
    public static <K,L> Map<K,L> sort(Map<K,L> old, Comparator<K> c)
    {
    	return old.entrySet().stream().sorted(Map.Entry.comparingByKey(c)).collect(Collectors.toMap(
          Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
