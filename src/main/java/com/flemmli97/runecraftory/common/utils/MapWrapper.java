package com.flemmli97.runecraftory.common.utils;

import java.util.HashMap;

public class MapWrapper<T, V> extends HashMap<T, V>
{
	private static final long serialVersionUID = 1143326767918321316L;
    
    public MapWrapper<T, V> mapWrapperAdd(T t, V v) {
        this.put(t, v);
        return this;
    }
}
