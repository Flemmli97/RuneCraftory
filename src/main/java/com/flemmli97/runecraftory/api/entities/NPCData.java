package com.flemmli97.runecraftory.api.entities;

import java.util.List;

import com.flemmli97.runecraftory.api.items.ISpells;
import com.flemmli97.runecraftory.common.entity.npc.EntityNPCBase;
import com.flemmli97.runecraftory.common.lib.enums.EnumNPCPersonality;
import com.flemmli97.runecraftory.common.lib.enums.EnumWeaponType;
import com.flemmli97.tenshilib.api.config.SimpleItemStackWrapper;
import com.google.common.collect.Lists;

public class NPCData {

    private String id;
    private EnumNPCPersonality personality;
    private List<Message> greetings = Lists.newArrayList();
    private List<Message> positiveMessage = Lists.newArrayList();
    private List<Message> negativeMessage = Lists.newArrayList();
    private List<Message> randomMessage = Lists.newArrayList();

    private String favoriteItems;
    private String likedItems;
    private String hatedItems;
    
    private EnumWeaponType preferedWeapon = EnumWeaponType.FARM;
    private List<ISpells> spells = Lists.newArrayList();
    
    public NPCData() {
        
    }
    
    public static class Message{
        
        private final String msg;
        private final int minHeart, maxHeart;
        private final String condition;//weather: rain, storm, runey; time: morning, day, evening, night
        
        public Message(String msg, int min, int max, String condition) {
            this.msg=msg;
            this.minHeart=min;
            this.maxHeart=max;
            this.condition=condition;
        }
        
        public boolean matches(EntityNPCBase npc) {
            return false;
        }
        
        public String getMessage() {
            return this.msg;
        }
    }
}
