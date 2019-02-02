package com.flemmli97.runecraftory.common.core.handler.config;

import java.io.IOException;

import com.flemmli97.runecraftory.common.utils.ItemUtils;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import net.minecraft.entity.ai.attributes.IAttribute;

public class JsonAdapaters {

	public static class AttributeAdapter extends TypeAdapter<IAttribute>
	{
		@Override
		public void write(JsonWriter out, IAttribute value) throws IOException 
		{
			out.value(value.getName());
		}

		@Override
		public IAttribute read(JsonReader in) throws IOException 
		{			
			if (in.peek() == JsonToken.NULL) 
			{
		        in.nextNull();
		        return null;
			}
			return ItemUtils.getAttFromName(in.nextString());
		}
	}
}
