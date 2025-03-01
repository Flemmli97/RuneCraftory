package io.github.flemmli97.runecraftory.api.datapack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonInstances {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

}
