package com.flemmli97.runecraftory.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import net.minecraft.block.Block;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonGenerators {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static void cropJson(File assetsFolder, Iterable<Block> blocks){

        new File(assetsFolder+"/blockstates").mkdir();
        new File(assetsFolder+"/model").mkdir();
        try {
            for (Block block : blocks) {

                File jsonFile = new File(assetsFolder+"/blockstates", block.getRegistryName().getResourcePath() + ".json");
                if (!jsonFile.exists()) {
                    jsonFile.createNewFile();
                    FileWriter writer = new FileWriter(jsonFile);
                    JsonWriter json = GSON.newJsonWriter(writer);
                    json.beginObject();
                    json.name("variants");
                    json.beginObject();

                    json.name("status=0");
                    json.beginObject();
                    json.name("model");
                    json.value("runecraftory:crop_stage0");
                    json.endObject();

                    json.name("status=1");
                    json.beginObject();
                    json.name("model");
                    json.value(block.getRegistryName() + "_stage1");
                    json.endObject();

                    json.name("status=2");
                    json.beginObject();
                    json.name("model");
                    json.value(block.getRegistryName() + "_stage2");
                    json.endObject();

                    json.name("status=3");
                    json.beginObject();
                    json.name("model");
                    json.value(block.getRegistryName() + "_stage3");
                    json.endObject();

                    json.endObject();
                    json.endObject();
                    writer.close();
                }

                File jsonFile2 = new File(assetsFolder+"/model", block.getRegistryName().getResourcePath() + "_stage1.json");
                if (!jsonFile2.exists()) {
                    jsonFile2.createNewFile();
                    FileWriter writer = new FileWriter(jsonFile2);
                    JsonWriter json = GSON.newJsonWriter(writer);
                    json.beginObject();
                    json.name("parent");
                    json.value("block/crop");
                    json.name("textures");
                    json.beginObject();
                    json.name("crop");
                    json.value("runecraftory:blocks/"+ block.getRegistryName().getResourcePath() + "_stage1");
                    json.name("particle");
                    json.value("runecraftory:blocks/"+ block.getRegistryName().getResourcePath()+"_stage1");
                    json.endObject();
                    json.endObject();
                    writer.close();
                }

                File jsonFile3 = new File(assetsFolder+"/model", block.getRegistryName().getResourcePath() + "_stage2.json");
                if (!jsonFile3.exists()) {
                    jsonFile3.createNewFile();
                    FileWriter writer = new FileWriter(jsonFile3);
                    JsonWriter json = GSON.newJsonWriter(writer);
                    json.beginObject();
                    json.name("parent");
                    json.value("block/crop");
                    json.name("textures");
                    json.beginObject();
                    json.name("crop");
                    json.value("runecraftory:blocks/"+ block.getRegistryName().getResourcePath()+"_stage2");
                    json.name("particle");
                    json.value("runecraftory:blocks/"+ block.getRegistryName().getResourcePath()+"_stage2");
                    json.endObject();
                    json.endObject();
                    writer.close();
                }

                File jsonFile4 = new File(assetsFolder+"/model", block.getRegistryName().getResourcePath() + "_stage3.json");
                if (!jsonFile4.exists()) {
                    jsonFile4.createNewFile();
                    FileWriter writer = new FileWriter(jsonFile4);
                    JsonWriter json = GSON.newJsonWriter(writer);
                    json.beginObject();
                    json.name("parent");
                    json.value("block/crop");
                    json.name("textures");
                    json.beginObject();
                    json.name("crop");
                    json.value("runecraftory:blocks/"+ block.getRegistryName().getResourcePath()+"_stage3");
                    json.name("particle");
                    json.value("runecraftory:blocks/"+ block.getRegistryName().getResourcePath()+"_stage3");
                    json.endObject();
                    json.endObject();
                    writer.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void flowerJson(File assetsFolder, Iterable<Block> blocks){
        //new File(assetsFolder+"/blockstates").mkdir();
        //new File(assetsFolder+"/models").mkdir();
        try {
            for (Block block : blocks) {

                File jsonFile = new File(assetsFolder+"/blockstates", block.getRegistryName().getResourcePath() + ".json");
                if (!jsonFile.exists()) {
                    jsonFile.createNewFile();
                    FileWriter writer = new FileWriter(jsonFile);
                    JsonWriter json = GSON.newJsonWriter(writer);
                    json.beginObject();
                    json.name("variants");
                    json.beginObject();

                    json.name("status=0");
                    json.beginObject();
                    json.name("model");
                    json.value("runecraftory:flower_stage0");
                    json.endObject();

                    json.name("status=1");
                    json.beginObject();
                    json.name("model");
                    json.value(block.getRegistryName() + "_stage1");
                    json.endObject();

                    json.name("status=2");
                    json.beginObject();
                    json.name("model");
                    json.value(block.getRegistryName() + "_stage2");
                    json.endObject();

                    json.name("status=3");
                    json.beginObject();
                    json.name("model");
                    json.value(block.getRegistryName() + "_stage3");
                    json.endObject();

                    json.endObject();
                    json.endObject();
                    writer.close();
                }

                File jsonFile2 = new File(assetsFolder+"/model", block.getRegistryName().getResourcePath() + "_stage1.json");
                if (!jsonFile2.exists()) {
                    jsonFile2.createNewFile();
                    FileWriter writer = new FileWriter(jsonFile2);
                    JsonWriter json = GSON.newJsonWriter(writer);
                    json.beginObject();
                    json.name("parent");
                    json.value("block/cross");
                    json.name("textures");
                    json.beginObject();
                    json.name("cross");
                    json.value("runecraftory:blocks/"+ block.getRegistryName().getResourcePath() + "_stage1");
                    json.name("particle");
                    json.value("runecraftory:blocks/"+ block.getRegistryName().getResourcePath()+"_stage1");
                    json.endObject();
                    json.endObject();
                    writer.close();
                }

                File jsonFile3 = new File(assetsFolder+"/model", block.getRegistryName().getResourcePath() + "_stage2.json");
                if (!jsonFile3.exists()) {
                    jsonFile3.createNewFile();
                    FileWriter writer = new FileWriter(jsonFile3);
                    JsonWriter json = GSON.newJsonWriter(writer);
                    json.beginObject();
                    json.name("parent");
                    json.value("block/cross");
                    json.name("textures");
                    json.beginObject();
                    json.name("cross");
                    json.value("runecraftory:blocks/"+ block.getRegistryName().getResourcePath()+"_stage2");
                    json.name("particle");
                    json.value("runecraftory:blocks/"+ block.getRegistryName().getResourcePath()+"_stage2");
                    json.endObject();
                    json.endObject();
                    writer.close();
                }

                File jsonFile4 = new File(assetsFolder+"/model", block.getRegistryName().getResourcePath() + "_stage3.json");
                if (!jsonFile4.exists()) {
                    jsonFile4.createNewFile();
                    FileWriter writer = new FileWriter(jsonFile4);
                    JsonWriter json = GSON.newJsonWriter(writer);
                    json.beginObject();
                    json.name("parent");
                    json.value("block/cross");
                    json.name("textures");
                    json.beginObject();
                    json.name("cross");
                    json.value("runecraftory:items/crop_"+ block.getRegistryName().getResourcePath().replace("plant_", ""));
                    json.name("particle");
                    json.value("runecraftory:items/crop_"+ block.getRegistryName().getResourcePath().replace("plant_", ""));
                    json.endObject();
                    json.endObject();
                    writer.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
