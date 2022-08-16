package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.api.datapack.provider.ItemStatProvider;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

public class ItemStatGen extends ItemStatProvider {

    public ItemStatGen(DataGenerator gen) {
        super(gen, RuneCraftory.MODID);
    }

    @Override
    protected void add() {
        this.addStat("arrows", ItemTags.ARROWS, new ItemStat.MutableItemStat(10, 1, 5)
                .addAttribute(Attributes.ATTACK_DAMAGE, 2)
                .setSpell(ModSpells.ARROW.get(), null, null));
        this.addStat("snowball", Items.SNOWBALL, new ItemStat.MutableItemStat(1, 0, 5)
                .addAttribute(Attributes.ATTACK_DAMAGE, 1)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 1)
                .setSpell(ModSpells.SNOWBALL.get(), null, null));
        this.addStat("totem", Items.TOTEM_OF_UNDYING, new ItemStat.MutableItemStat(2500, 100, 5)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 5)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 5)
                .setSpell(ModSpells.EVOKERFANG.get(), null, null));
        this.addStat("wither_skull", Items.WITHER_SKELETON_SKULL, new ItemStat.MutableItemStat(5000, 150, 5)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5)
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
                .addAttribute(Attributes.ATTACK_DAMAGE, 4));
        this.addStat(ModItems.steelSword.get(), new ItemStat.MutableItemStat(55, 15, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 1));
        this.addStat(ModItems.steelSwordPlus.get(), new ItemStat.MutableItemStat(100, 25, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 7)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 2));
        this.addStat(ModItems.cutlass.get(), new ItemStat.MutableItemStat(122, 30, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 9)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 3)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 1));
        this.addStat(ModItems.aquaSword.get(), new ItemStat.MutableItemStat(152, 45, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 11)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 3)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.invisiBlade.get(), new ItemStat.MutableItemStat(176, 55, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 15)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 4));

        this.addStat(ModItems.claymore.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5.5));
        this.addStat(ModItems.zweihaender.get(), new ItemStat.MutableItemStat(60, 15, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 7));
        this.addStat(ModItems.zweihaenderPlus.get(), new ItemStat.MutableItemStat(105, 34, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 10)
                .addAttribute(ModAttributes.RFDIZ.get(), 5)
                .addAttribute(ModAttributes.RFSTUN.get(), 15));
        this.addStat(ModItems.greatSword.get(), new ItemStat.MutableItemStat(145, 66, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 13)
                .addAttribute(ModAttributes.RFDIZ.get(), 9));
        this.addStat(ModItems.seaCutter.get(), new ItemStat.MutableItemStat(176, 73, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 16)
                .addAttribute(ModAttributes.RFDIZ.get(), 11));
        this.addStat(ModItems.cycloneBlade.get(), new ItemStat.MutableItemStat(197, 98, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 19)
                .addAttribute(ModAttributes.RFDIZ.get(), 13));

        this.addStat(ModItems.spear.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5));
        this.addStat(ModItems.woodStaff.get(), new ItemStat.MutableItemStat(45, 15, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 7));
        this.addStat(ModItems.lance.get(), new ItemStat.MutableItemStat(66, 26, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 9)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 1));
        this.addStat(ModItems.lancePlus.get(), new ItemStat.MutableItemStat(83, 34, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 12)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 2));
        this.addStat(ModItems.needleSpear.get(), new ItemStat.MutableItemStat(108, 47, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 14)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 4));
        this.addStat(ModItems.trident.get(), new ItemStat.MutableItemStat(134, 58, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 16)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 6));

        this.addStat(ModItems.battleAxe.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 7)
                .addAttribute(ModAttributes.RFCRIT.get(), 5));
        this.addStat(ModItems.battleScythe.get(), new ItemStat.MutableItemStat(29, 8, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 9)
                .addAttribute(ModAttributes.RFCRIT.get(), 15));
        this.addStat(ModItems.poleAxe.get(), new ItemStat.MutableItemStat(56, 21, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 12)
                .addAttribute(ModAttributes.RFCRIT.get(), 7));
        this.addStat(ModItems.poleAxePlus.get(), new ItemStat.MutableItemStat(87, 34, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 15)
                .addAttribute(ModAttributes.RFCRIT.get(), 7));
        this.addStat(ModItems.greatAxe.get(), new ItemStat.MutableItemStat(145, 68, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 18)
                .addAttribute(ModAttributes.RFCRIT.get(), 11));
        this.addStat(ModItems.tomohawk.get(), new ItemStat.MutableItemStat(211, 92, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 21)
                .addAttribute(ModAttributes.RFCRIT.get(), 15));

        this.addStat(ModItems.battleHammer.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 10)
                .addAttribute(ModAttributes.RFSTUN.get(), 5));
        this.addStat(ModItems.bat.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 11)
                .addAttribute(ModAttributes.RFSTUN.get(), 6));
        this.addStat(ModItems.warHammer.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 13)
                .addAttribute(ModAttributes.RFSTUN.get(), 6));
        this.addStat(ModItems.warHammerPlus.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 15)
                .addAttribute(ModAttributes.RFSTUN.get(), 7));
        this.addStat(ModItems.ironBat.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 17)
                .addAttribute(ModAttributes.RFSTUN.get(), 9));
        this.addStat(ModItems.greatHammer.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 19)
                .addAttribute(ModAttributes.RFSTUN.get(), 11));

        this.addStat(ModItems.shortDagger.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3));
        this.addStat(ModItems.steelEdge.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 4));
        this.addStat(ModItems.frostEdge.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 7)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 3)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.ironEdge.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 9));
        this.addStat(ModItems.thiefKnife.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 10));
        this.addStat(ModItems.windEdge.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 12)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 5)
                .setElement(EnumElement.WIND));

        this.addStat(ModItems.leatherGlove.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5));
        this.addStat(ModItems.brassKnuckles.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 6)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 1));
        this.addStat(ModItems.kote.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 8)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 3));
        this.addStat(ModItems.gloves.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 10)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 4)
                .addAttribute(Attributes.MAX_HEALTH, 10));
        this.addStat(ModItems.bearClaws.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 11)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 9));
        this.addStat(ModItems.fistEarth.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 13)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 11)
                .setElement(EnumElement.EARTH));

        this.addStat(ModItems.rod.get(), new ItemStat.MutableItemStat(111, 50, 0)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 5)
                .addAttribute(ModAttributes.RFDIZ.get(), 15)
                .setElement(EnumElement.FIRE)
                .setSpell(ModSpells.FIREBALL.get(), null, null));
        this.addStat(ModItems.amethystRod.get(), new ItemStat.MutableItemStat(155, 50, 0)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 8)
                .addAttribute(ModAttributes.RFDIZ.get(), 15)
                .setElement(EnumElement.EARTH));
        this.addStat(ModItems.aquamarineRod.get(), new ItemStat.MutableItemStat(243, 122, 0)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 11)
                .addAttribute(ModAttributes.RFDIZ.get(), 15)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.friendlyRod.get(), new ItemStat.MutableItemStat(367, 176, 0)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 15)
                .addAttribute(ModAttributes.RFDIZ.get(), 15)
                .setElement(EnumElement.LOVE));
        this.addStat(ModItems.loveLoveRod.get(), new ItemStat.MutableItemStat(455, 197, 0)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 18)
                .addAttribute(ModAttributes.RFDIZ.get(), 15)
                .setElement(EnumElement.LOVE));
        this.addStat(ModItems.staff.get(), new ItemStat.MutableItemStat(511, 237, 0)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 21)
                .addAttribute(ModAttributes.RFDIZ.get(), 15)
                .setElement(EnumElement.EARTH));

        this.addStat(ModItems.hoeScrap.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.hoeIron.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 15)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 3));
        this.addStat(ModItems.hoeSilver.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 34)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 5)
                .setElement(EnumElement.EARTH));
        this.addStat(ModItems.hoeGold.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 76)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 15)
                .setElement(EnumElement.EARTH));
        this.addStat(ModItems.hoePlatinum.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 111)
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
                .addAttribute(Attributes.ATTACK_DAMAGE, 2));
        this.addStat(ModItems.sickleIron.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 19));
        this.addStat(ModItems.sickleSilver.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 36));
        this.addStat(ModItems.sickleGold.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 79)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 5)
                .setElement(EnumElement.WIND));
        this.addStat(ModItems.sicklePlatinum.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 134)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 31)
                .setElement(EnumElement.WIND));

        this.addStat(ModItems.axeScrap.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addAttribute(ModAttributes.RFCRIT.get(), 5));
        this.addStat(ModItems.axeIron.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 22)
                .addAttribute(ModAttributes.RFCRIT.get(), 5));
        this.addStat(ModItems.axeSilver.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 39)
                .addAttribute(ModAttributes.RFCRIT.get(), 5));
        this.addStat(ModItems.axeGold.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 83)
                .addAttribute(ModAttributes.RFCRIT.get(), 5));
        this.addStat(ModItems.axePlatinum.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 140)
                .addAttribute(ModAttributes.RFCRIT.get(), 15));

        this.addStat(ModItems.hammerScrap.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addAttribute(ModAttributes.RFCRIT.get(), -5)
                .addAttribute(ModAttributes.RFSTUN.get(), 10));
        this.addStat(ModItems.hammerIron.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 23)
                .addAttribute(ModAttributes.RFCRIT.get(), -5)
                .addAttribute(ModAttributes.RFSTUN.get(), 10));
        this.addStat(ModItems.hammerSilver.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 47)
                .addAttribute(ModAttributes.RFCRIT.get(), -5)
                .addAttribute(ModAttributes.RFSTUN.get(), 10));
        this.addStat(ModItems.hammerGold.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 85)
                .addAttribute(ModAttributes.RFCRIT.get(), -5)
                .addAttribute(ModAttributes.RFSTUN.get(), 10));
        this.addStat(ModItems.hammerPlatinum.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 145)
                .addAttribute(ModAttributes.RFCRIT.get(), -7)
                .addAttribute(ModAttributes.RFSTUN.get(), 10));

        this.addStat(ModItems.fishingRodScrap.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 1)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 5));
        this.addStat(ModItems.fishingRodIron.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 9)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 14));
        this.addStat(ModItems.fishingRodSilver.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 26)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 27));
        this.addStat(ModItems.fishingRodGold.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 66)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 72));
        this.addStat(ModItems.fishingRodPlatinum.get(), new ItemStat.MutableItemStat(25, 5, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 89)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 98));

        this.addStat(ModItems.cheapBracelet.get(), new ItemStat.MutableItemStat(100, 13, 0)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 1.5));
        this.addStat(ModItems.bronzeBracelet.get(), new ItemStat.MutableItemStat(150, 21, 0)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 2));

        this.addStat(Items.SHIELD, new ItemStat.MutableItemStat(45, 3, 0)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 2));
        this.addStat(ModItems.smallShield.get(), new ItemStat.MutableItemStat(120, 18, 0)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 4));

        this.addStat("iron", Tags.Items.INGOTS_IRON, new ItemStat.MutableItemStat(33, 1, 5)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 1));
        this.addStat("bronze", ModTags.bronze, new ItemStat.MutableItemStat(25, 3, 10)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 2.4));
        this.addStat("silver", ModTags.silver, new ItemStat.MutableItemStat(105, 20, 15)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 5));
        this.addStat("gold", Tags.Items.INGOTS_GOLD, new ItemStat.MutableItemStat(222, 34, 20)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 4)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 2));
        this.addStat("platinum", ModTags.platinum, new ItemStat.MutableItemStat(643, 111, 30)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 25));
        this.addStat("orichalcum", ModTags.orichalcum, new ItemStat.MutableItemStat(1000, 150, 10)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 70));
        this.addStat("dragonic", ModTags.dragonic, new ItemStat.MutableItemStat(0, 400, 10)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 130)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 90));

        this.addStat("emerald", Tags.Items.GEMS_EMERALD, new ItemStat.MutableItemStat(45, 2, 20));

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
