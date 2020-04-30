package com.flemmli97.runecraftory.api.mappings;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flemmli97.runecraftory.api.entities.NPCData;
import com.flemmli97.runecraftory.api.entities.NPCData.Message;
import com.flemmli97.runecraftory.common.entity.npc.EntityNPCBase;
import com.flemmli97.runecraftory.common.lib.enums.EnumNPCPersonality;
import com.flemmli97.tenshilib.api.config.SimpleItemStackWrapper;
import com.google.common.collect.Maps;

public class NPCDataMaps {
    
    private Map<EnumNPCPersonality,List<Message>> greetings = Maps.newHashMap();
    private Map<EnumNPCPersonality,List<Message>> positiveMessage = Maps.newHashMap();
    private Map<EnumNPCPersonality,List<Message>> negativeMessage = Maps.newHashMap();
    private Map<EnumNPCPersonality,List<Message>> randomMessage = Maps.newHashMap();
     
    private Map<String,Set<SimpleItemStackWrapper>> favoriteItems = Maps.newHashMap();
    private Map<String,Set<SimpleItemStackWrapper>> likedItems = Maps.newHashMap();
    private Map<String,Set<SimpleItemStackWrapper>> hatedItems = Maps.newHashMap();
    
    public NPCData randomData(EntityNPCBase npc) {
        NPCData data = new NPCData();
        
        
        return data;
    }
}
