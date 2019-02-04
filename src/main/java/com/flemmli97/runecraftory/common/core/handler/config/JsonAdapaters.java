package com.flemmli97.runecraftory.common.core.handler.config;

import java.io.IOException;
import java.lang.reflect.Method;

import com.flemmli97.runecraftory.common.utils.ItemUtils;
import com.flemmli97.tenshilib.common.javahelper.ReflectionUtils;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.util.ResourceLocation;

public class JsonAdapaters {

	public static class AttributeAdapter extends TypeAdapter<IAttribute>
	{
		@Override
		public void write(JsonWriter out, IAttribute value) throws IOException 
		{
			out.value(value.getName());
		}

		private static final Method jsonlocationString = ReflectionUtils.getMethod(JsonReader.class, "locationString");
		
		@Override
		public IAttribute read(JsonReader in) throws IOException 
		{			
			if (in.peek() == JsonToken.NULL) 
			{
		        in.nextNull();
		        return null;
			}
			String name = in.nextString();
			try
			{
				return ItemUtils.getAttFromName(name);
			}		
			catch(NullPointerException e)
			{
				throw new ConfigHandler.ConfigError(e.getMessage() + ReflectionUtils.invokeMethod(jsonlocationString, in));
			}
		}
	}
	
	public static class ResourceLocAdapter extends TypeAdapter<ResourceLocation>
	{
		@Override
		public void write(JsonWriter out, ResourceLocation value) throws IOException 
		{
			out.value(value.toString());
		}
		
		@Override
		public ResourceLocation read(JsonReader in) throws IOException 
		{			
			if (in.peek() == JsonToken.NULL) 
			{
		        in.nextNull();
		        return null;
			}
			return new ResourceLocation(in.nextString());
		}
	}
}
