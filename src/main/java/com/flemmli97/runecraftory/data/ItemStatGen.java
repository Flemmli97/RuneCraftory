package com.flemmli97.runecraftory.data;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.datapack.ItemStat;
import com.flemmli97.runecraftory.api.datapack.provider.ItemStatProvider;
import com.flemmli97.runecraftory.api.enums.EnumElement;
import com.flemmli97.runecraftory.common.registry.ModAttributes;
import com.flemmli97.runecraftory.common.registry.ModItems;
import com.flemmli97.runecraftory.common.registry.ModSpells;
import com.flemmli97.runecraftory.common.registry.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;

public class ItemStatGen extends ItemStatProvider {

    public ItemStatGen(DataGenerator gen) {
        super(gen, RuneCraftory.MODID);
    }

    @Override
    protected void add() {
        this.addStat("arrows", ItemTags.ARROWS, new ItemStat.MutableItemStat(10, 1, 5)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 5)
                .setSpell(ModSpells.ARROW.get(), null, null));
        this.addStat("snowball", Items.SNOWBALL, new ItemStat.MutableItemStat(1, 0, 5)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 1)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 1)
                .setSpell(ModSpells.SNOWBALL.get(), null, null));
        this.addStat("totem", Items.TOTEM_OF_UNDYING, new ItemStat.MutableItemStat(2500, 100, 5)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 5)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 5)
                .setSpell(ModSpells.EVOKERFANG.get(), null, null));
        this.addStat("wither_skull", Items.WITHER_SKELETON_SKULL, new ItemStat.MutableItemStat(5000, 150, 5)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 5)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 15)
                .setSpell(ModSpells.WITHERSKULL.get(), null, null)
                .setElement(EnumElement.DARK));

        this.addStat(ModItems.roundoff.get(), new ItemStat.MutableItemStat(750, 50, 0));
        this.addStat(ModItems.paraGone.get(), new ItemStat.MutableItemStat(750, 50, 0));
        this.addStat(ModItems.coldMed.get(), new ItemStat.MutableItemStat(750, 50, 0));
        this.addStat(ModItems.antidote.get(), new ItemStat.MutableItemStat(750, 50, 0));
        this.addStat(ModItems.recoveryPotion.get(), new ItemStat.MutableItemStat(300, 25, 0));
        this.addStat(ModItems.healingPotion.get(), new ItemStat.MutableItemStat(500, 35, 0));
        this.addStat(ModItems.mysteryPotion.get(), new ItemStat.MutableItemStat(1000, 250, 0));
        this.addStat(ModItems.magicalPotion.get(), new ItemStat.MutableItemStat(3000, 500, 0));

        this.addStat(ModItems.broadSword.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 4));
        this.addStat(ModItems.steelSword.get(), new ItemStat.MutableItemStat(55, 15, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 5)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 1));
        this.addStat(ModItems.steelSwordPlus.get(), new ItemStat.MutableItemStat(100, 25, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 7)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 2));
        this.addStat(ModItems.cutlass.get(), new ItemStat.MutableItemStat(122, 30, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 10)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 3)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 1));

        this.addStat(ModItems.claymore.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 7));
        this.addStat(ModItems.zweihaender.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 8));
        this.addStat(ModItems.zweihaenderPlus.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 10)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 1));
        this.addStat(ModItems.greatSword.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 14)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 4));

        this.addStat(ModItems.spear.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 6));
        this.addStat(ModItems.woodStaff.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 7));
        this.addStat(ModItems.lance.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 8)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 1));
        this.addStat(ModItems.lancePlus.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 12)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 2));

        this.addStat(ModItems.battleAxe.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 9)
                .addAttribute(ModAttributes.RFCRIT.get(), 5));

        this.addStat(ModItems.poleAxe.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 12)
                .addAttribute(ModAttributes.RFCRIT.get(), 7));
        this.addStat(ModItems.poleAxePlus.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 15)
                .addAttribute(ModAttributes.RFCRIT.get(), 7));

        this.addStat(ModItems.battleHammer.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 10)
                .addAttribute(ModAttributes.RFSTUN.get(), 5));
        this.addStat(ModItems.bat.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 11)
                .addAttribute(ModAttributes.RFSTUN.get(), 6));
        this.addStat(ModItems.warHammer.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 15)
                .addAttribute(ModAttributes.RFSTUN.get(), 7));
        this.addStat(ModItems.warHammerPlus.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 18)
                .addAttribute(ModAttributes.RFSTUN.get(), 9));
        this.addStat(ModItems.ironBat.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 20)
                .addAttribute(ModAttributes.RFSTUN.get(), 11));

        this.addStat(ModItems.shortDagger.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 4));

        this.addStat(ModItems.leatherGlove.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 5));

        this.addStat(ModItems.rod.get(), new ItemStat.MutableItemStat(1000, 50, 0)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 5)
                .addAttribute(ModAttributes.RFDIZ.get(), 15)
                .setElement(EnumElement.FIRE)
                .setSpell(ModSpells.FIREBALL.get(), null, null));

        this.addStat(ModItems.hoeScrap.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 1));
        this.addStat(ModItems.hoeIron.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 15)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 3));
        this.addStat(ModItems.hoeSilver.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 34)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 5)
                .setElement(EnumElement.EARTH));
        this.addStat(ModItems.hoeGold.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 76)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 15)
                .setElement(EnumElement.EARTH));
        this.addStat(ModItems.hoePlatinum.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 111)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 45)
                .setElement(EnumElement.EARTH));

        this.addStat(ModItems.wateringCanScrap.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 1)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.wateringCanIron.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 7)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.wateringCanSilver.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 19)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.wateringCanGold.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 39)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.wateringCanPlatinum.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 99)
                .setElement(EnumElement.WATER));

        this.addStat(ModItems.sickleScrap.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 2));
        this.addStat(ModItems.sickleIron.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 19));
        this.addStat(ModItems.sickleSilver.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 36));
        this.addStat(ModItems.sickleGold.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 79)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 5)
                .setElement(EnumElement.WIND));
        this.addStat(ModItems.sicklePlatinum.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 134)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 31)
                .setElement(EnumElement.WIND));

        this.addStat(ModItems.axeScrap.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 3)
                .addAttribute(ModAttributes.RFCRIT.get(), 5));
        this.addStat(ModItems.axeIron.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 22)
                .addAttribute(ModAttributes.RFCRIT.get(), 5));
        this.addStat(ModItems.axeSilver.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 39)
                .addAttribute(ModAttributes.RFCRIT.get(), 5));
        this.addStat(ModItems.axeGold.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 83)
                .addAttribute(ModAttributes.RFCRIT.get(), 5));
        this.addStat(ModItems.axePlatinum.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 140)
                .addAttribute(ModAttributes.RFCRIT.get(), 15));

        this.addStat(ModItems.hammerScrap.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 3)
                .addAttribute(ModAttributes.RFCRIT.get(), -5)
                .addAttribute(ModAttributes.RFRESSTUN.get(), 10));
        this.addStat(ModItems.hammerIron.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 23)
                .addAttribute(ModAttributes.RFCRIT.get(), -5)
                .addAttribute(ModAttributes.RFRESSTUN.get(), 10));
        this.addStat(ModItems.hammerSilver.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 47)
                .addAttribute(ModAttributes.RFCRIT.get(), -5)
                .addAttribute(ModAttributes.RFRESSTUN.get(), 10));
        this.addStat(ModItems.hammerGold.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 85)
                .addAttribute(ModAttributes.RFCRIT.get(), -5)
                .addAttribute(ModAttributes.RFRESSTUN.get(), 10));
        this.addStat(ModItems.hammerPlatinum.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 145)
                .addAttribute(ModAttributes.RFCRIT.get(), -7)
                .addAttribute(ModAttributes.RFRESSTUN.get(), 10));

        this.addStat(ModItems.fishingRodScrap.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 1)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 5));
        this.addStat(ModItems.fishingRodIron.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 9)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 14));
        this.addStat(ModItems.fishingRodSilver.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 26)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 27));
        this.addStat(ModItems.fishingRodGold.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 66)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 72));
        this.addStat(ModItems.fishingRodPlatinum.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.GENERIC_ATTACK_DAMAGE, 89)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 98));

        this.addStat(ModItems.cheapBracelet.get(), new ItemStat.MutableItemStat(100, 25, 0)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 1.5));
        this.addStat(ModItems.bronzeBracelet.get(), new ItemStat.MutableItemStat(100, 25, 0)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 2));

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
