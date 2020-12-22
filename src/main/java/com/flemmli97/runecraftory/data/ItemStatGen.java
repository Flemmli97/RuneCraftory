package com.flemmli97.runecraftory.data;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.datapack.ItemStat;
import com.flemmli97.runecraftory.api.datapack.provider.ItemStatProvider;
import com.flemmli97.runecraftory.common.registry.ModAttributes;
import com.flemmli97.runecraftory.common.registry.ModItems;
import com.flemmli97.runecraftory.common.registry.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraftforge.common.Tags;

public class ItemStatGen extends ItemStatProvider {

    public ItemStatGen(DataGenerator gen) {
        super(gen, RuneCraftory.MODID);
    }

    @Override
    protected void add() {
        this.addStat(ModItems.broadSword.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 5));
        this.addStat(ModItems.steelSword.get(), new ItemStat.MutableItemStat(55, 15, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 9)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 2));
        this.addStat(ModItems.steelSwordPlus.get(), new ItemStat.MutableItemStat(100, 25, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 11)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 4));
        this.addStat(ModItems.cutlass.get(), new ItemStat.MutableItemStat(122, 30, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 16)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 3)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 1));

        this.addStat(ModItems.cheapBracelet.get(), new ItemStat.MutableItemStat(100, 25, 0)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 5));

        this.addStat("iron", Tags.Items.INGOTS_IRON, new ItemStat.MutableItemStat(1, 1, 5)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 1));
        this.addStat("bronze", ModTags.bronze, new ItemStat.MutableItemStat(25, 3, 10)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 3));
        this.addStat("silver", ModTags.silver, new ItemStat.MutableItemStat(105, 20, 15)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 6));
        this.addStat("gold", Tags.Items.INGOTS_GOLD, new ItemStat.MutableItemStat(222, 34, 20)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 15));
        this.addStat("platinum", ModTags.platinum, new ItemStat.MutableItemStat(643, 111, 30)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 25));
        this.addStat("orichalcum", ModTags.orichalcum, new ItemStat.MutableItemStat(1000, 150, 10)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 70));
        this.addStat("dragonic", ModTags.dragonic, new ItemStat.MutableItemStat(0, 400, 10)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 130)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 90));

        this.addStat("emerald", Tags.Items.GEMS_EMERALD, new ItemStat.MutableItemStat(1, 1, 20));

        this.addStat(ModItems.witheredGrass.get(), 100, 1, 1);
        this.addStat(ModItems.weeds.get(), 30, 1, 1);
        this.addStat(ModItems.whiteGrass.get(), 120, 5, 1);
        this.addStat(ModItems.indigoGrass.get(), 120, 5, 1);
        this.addStat(ModItems.purpleGrass.get(), 120, 5, 1);
        this.addStat(ModItems.greenGrass.get(), 120, 5, 1);
        this.addStat(ModItems.blueGrass.get(), 120, 5, 1);
        this.addStat(ModItems.yellowGrass.get(), 120, 5, 1);
        this.addStat(ModItems.orangeGrass.get(), 120, 5, 1);
        this.addStat(ModItems.redGrass.get(), 120, 5, 1);
        this.addStat(ModItems.blackGrass.get(), 120, 5, 1);
        this.addStat(ModItems.antidoteGrass.get(), 120, 5, 1);
        this.addStat(ModItems.medicinalHerb.get(), 150, 10, 1);

    }
}
