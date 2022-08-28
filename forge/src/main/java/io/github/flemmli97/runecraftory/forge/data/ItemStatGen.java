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

public class ItemStatGen extends ItemStatProvider {

    public ItemStatGen(DataGenerator gen) {
        super(gen, RuneCraftory.MODID);
    }

    @Override
    protected void add() {
        //Vanilla stuff
        this.addStat(Items.STICK, new ItemStat.MutableItemStat(15, 1, 2)
                .addAttribute(Attributes.ATTACK_DAMAGE, 1));
        this.addStat("arrows", ItemTags.ARROWS, new ItemStat.MutableItemStat(45, 3, 2)
                .addAttribute(Attributes.ATTACK_DAMAGE, 2)
                .setSpell(ModSpells.ARROW.get(), null, null));
        this.addStat("coals", ItemTags.COALS, new ItemStat.MutableItemStat(65, 5, 2)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 1));
        this.addStat(Items.SNOWBALL, new ItemStat.MutableItemStat(15, 1, 5)
                .addAttribute(Attributes.ATTACK_DAMAGE, 1)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 1)
                .setSpell(ModSpells.SNOWBALL.get(), null, null));
        this.addStat(Items.STRING, new ItemStat.MutableItemStat(76, 6, 13)
                .addAttribute(Attributes.ATTACK_DAMAGE, 2)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 2)
                .addAttribute(ModAttributes.RFPARA.get(), 3));
        this.addStat(Items.FEATHER, new ItemStat.MutableItemStat(79, 7, 11)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 2));
        this.addStat(Items.ROTTEN_FLESH, new ItemStat.MutableItemStat(50, 4, 9)
                .addAttribute(Attributes.MAX_HEALTH, 2));
        this.addStat(Items.BONE, new ItemStat.MutableItemStat(75, 5, 6)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 1));
        this.addStat(Items.GUNPOWDER, new ItemStat.MutableItemStat(100, 8, 21)
                .addAttribute(Attributes.ATTACK_DAMAGE, 1)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 5));
        this.addStat(Items.REDSTONE, new ItemStat.MutableItemStat(120, 7, 15)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 3));
        this.addStat(Items.FLINT, new ItemStat.MutableItemStat(103, 9, 8)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3));
        this.addStat(Items.QUARTZ, new ItemStat.MutableItemStat(263, 21, 16)
                .addAttribute(Attributes.ATTACK_DAMAGE, 4));
        this.addStat(Items.GLOWSTONE_DUST, new ItemStat.MutableItemStat(200, 17, 17)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 4));
        this.addStat(Items.LAPIS_LAZULI, new ItemStat.MutableItemStat(54, 4, 13)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 2));
        this.addStat(Items.LEATHER, new ItemStat.MutableItemStat(170, 14, 23)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 7));
        this.addStat(Items.CLAY_BALL, new ItemStat.MutableItemStat(100, 9, 19)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 2));
        this.addStat(Items.BRICK, new ItemStat.MutableItemStat(132, 11, 19)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 2)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 1));
        this.addStat(Items.PAPER, new ItemStat.MutableItemStat(167, 13, 4)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 2));
        this.addStat(Items.BOOK, new ItemStat.MutableItemStat(500, 42, 26)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 12));
        this.addStat(Items.PRISMARINE_SHARD, new ItemStat.MutableItemStat(389, 34, 17)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 1)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 3));
        this.addStat(Items.PRISMARINE_CRYSTALS, new ItemStat.MutableItemStat(523, 46, 25)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 8));
        this.addStat(Items.BLAZE_ROD, new ItemStat.MutableItemStat(350, 23, 23)
                .addAttribute(Attributes.ATTACK_DAMAGE, 7));
        this.addStat(Items.ENDER_PEARL, new ItemStat.MutableItemStat(400, 28, 21)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 3)
                .addAttribute(ModAttributes.RFRESSTUN.get(), 5));
        this.addStat(Items.SLIME_BALL, new ItemStat.MutableItemStat(375, 31, 18)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 4)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 9));
        this.addStat(Items.MAGMA_CREAM, new ItemStat.MutableItemStat(250, 21, 20)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 3)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 8));
        this.addStat(Items.GHAST_TEAR, new ItemStat.MutableItemStat(750, 49, 22)
                .addAttribute(Attributes.MAX_HEALTH, 7)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 2)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 3));
        this.addStat(Items.PHANTOM_MEMBRANE, new ItemStat.MutableItemStat(600, 55, 19)
                .addAttribute(Attributes.MOVEMENT_SPEED, 0.03));
        this.addStat(Items.SUGAR, new ItemStat.MutableItemStat(25, 13, 18)
                .addAttribute(Attributes.MOVEMENT_SPEED, 0.01));
        this.addStat(Items.TOTEM_OF_UNDYING, new ItemStat.MutableItemStat(3500, 110, 17)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 9)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 9)
                .setSpell(ModSpells.EVOKERFANG.get(), null, null));
        this.addStat(Items.WITHER_SKELETON_SKULL, new ItemStat.MutableItemStat(5000, 170, 23)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 15)
                .setSpell(ModSpells.WITHERSKULL.get(), null, null)
                .setElement(EnumElement.DARK));
        this.addStat(Items.NETHER_STAR, new ItemStat.MutableItemStat(20000, 600, 45)
                .addAttribute(Attributes.ATTACK_DAMAGE, 12)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 7)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 35)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 9)
                .addAttribute(ModAttributes.RFDRAIN.get(), 5)
                .setSpell(ModSpells.WITHERSKULL.get(), null, null)
                .setElement(EnumElement.DARK));

        //=======
        this.addStat(ModItems.roundoff.get(), new ItemStat.MutableItemStat(750, 50, 0));
        this.addStat(ModItems.paraGone.get(), new ItemStat.MutableItemStat(750, 50, 0));
        this.addStat(ModItems.coldMed.get(), new ItemStat.MutableItemStat(750, 50, 0));
        this.addStat(ModItems.antidote.get(), new ItemStat.MutableItemStat(750, 50, 0));
        this.addStat(ModItems.recoveryPotion.get(), new ItemStat.MutableItemStat(300, 25, 0));
        this.addStat(ModItems.healingPotion.get(), new ItemStat.MutableItemStat(500, 35, 0));
        this.addStat(ModItems.mysteryPotion.get(), new ItemStat.MutableItemStat(3000, 250, 0));
        this.addStat(ModItems.magicalPotion.get(), new ItemStat.MutableItemStat(6000, 500, 0));

        this.addStat(ModItems.broadSword.get(), new ItemStat.MutableItemStat(100, 16, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 4));
        this.addStat(ModItems.steelSword.get(), new ItemStat.MutableItemStat(1320, 54, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 6)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 1));
        this.addStat(ModItems.steelSwordPlus.get(), new ItemStat.MutableItemStat(2310, 99, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 8)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 2));
        this.addStat(ModItems.cutlass.get(), new ItemStat.MutableItemStat(5240, 210, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 12)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 4)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 1));
        this.addStat(ModItems.aquaSword.get(), new ItemStat.MutableItemStat(7850, 357, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 16)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 3)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.invisiBlade.get(), new ItemStat.MutableItemStat(12350, 571, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 19)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 4));

        this.addStat(ModItems.claymore.get(), new ItemStat.MutableItemStat(210, 17, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5.5));
        this.addStat(ModItems.zweihaender.get(), new ItemStat.MutableItemStat(1360, 58, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 7));
        this.addStat(ModItems.zweihaenderPlus.get(), new ItemStat.MutableItemStat(2170, 104, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 11)
                .addAttribute(ModAttributes.RFDIZ.get(), 5)
                .addAttribute(ModAttributes.RFSTUN.get(), 15));
        this.addStat(ModItems.greatSword.get(), new ItemStat.MutableItemStat(4960, 231, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 16)
                .addAttribute(ModAttributes.RFDIZ.get(), 9));
        this.addStat(ModItems.seaCutter.get(), new ItemStat.MutableItemStat(9170, 404, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 19)
                .addAttribute(ModAttributes.RFDIZ.get(), 11));
        this.addStat(ModItems.cycloneBlade.get(), new ItemStat.MutableItemStat(13680, 623, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 23)
                .addAttribute(ModAttributes.RFDIZ.get(), 13));

        this.addStat(ModItems.spear.get(), new ItemStat.MutableItemStat(190, 13, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5));
        this.addStat(ModItems.woodStaff.get(), new ItemStat.MutableItemStat(1270, 56, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 7));
        this.addStat(ModItems.lance.get(), new ItemStat.MutableItemStat(2310, 101, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 10)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 1));
        this.addStat(ModItems.lancePlus.get(), new ItemStat.MutableItemStat(4460, 198, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 14)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 2));
        this.addStat(ModItems.needleSpear.get(), new ItemStat.MutableItemStat(7770, 333, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 18)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 4));
        this.addStat(ModItems.trident.get(), new ItemStat.MutableItemStat(13280, 543, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 21)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 6));

        this.addStat(ModItems.battleAxe.get(), new ItemStat.MutableItemStat(250, 19, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 7)
                .addAttribute(ModAttributes.RFCRIT.get(), 5));
        this.addStat(ModItems.battleScythe.get(), new ItemStat.MutableItemStat(1430, 60, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 11)
                .addAttribute(ModAttributes.RFCRIT.get(), 15));
        this.addStat(ModItems.poleAxe.get(), new ItemStat.MutableItemStat(3250, 147, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 15)
                .addAttribute(ModAttributes.RFCRIT.get(), 7));
        this.addStat(ModItems.poleAxePlus.get(), new ItemStat.MutableItemStat(5430, 245, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 19)
                .addAttribute(ModAttributes.RFCRIT.get(), 7));
        this.addStat(ModItems.greatAxe.get(), new ItemStat.MutableItemStat(9580, 417, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 24)
                .addAttribute(ModAttributes.RFCRIT.get(), 11));
        this.addStat(ModItems.tomohawk.get(), new ItemStat.MutableItemStat(14360, 683, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 27)
                .addAttribute(ModAttributes.RFCRIT.get(), 15));

        this.addStat(ModItems.battleHammer.get(), new ItemStat.MutableItemStat(245, 18, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 10)
                .addAttribute(ModAttributes.RFSTUN.get(), 5));
        this.addStat(ModItems.bat.get(), new ItemStat.MutableItemStat(1240, 54, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 13)
                .addAttribute(ModAttributes.RFSTUN.get(), 6));
        this.addStat(ModItems.warHammer.get(), new ItemStat.MutableItemStat(2960, 138, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 18)
                .addAttribute(ModAttributes.RFSTUN.get(), 6));
        this.addStat(ModItems.warHammerPlus.get(), new ItemStat.MutableItemStat(6340, 265, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 15)
                .addAttribute(ModAttributes.RFSTUN.get(), 7));
        this.addStat(ModItems.ironBat.get(), new ItemStat.MutableItemStat(9350, 421, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 21)
                .addAttribute(ModAttributes.RFSTUN.get(), 9));
        this.addStat(ModItems.greatHammer.get(), new ItemStat.MutableItemStat(14740, 658, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 28)
                .addAttribute(ModAttributes.RFSTUN.get(), 11));

        this.addStat(ModItems.shortDagger.get(), new ItemStat.MutableItemStat(230, 12, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3));
        this.addStat(ModItems.steelEdge.get(), new ItemStat.MutableItemStat(950, 44, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5));
        this.addStat(ModItems.frostEdge.get(), new ItemStat.MutableItemStat(2610, 121, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 8)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 3)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.ironEdge.get(), new ItemStat.MutableItemStat(4910, 230, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 11));
        this.addStat(ModItems.thiefKnife.get(), new ItemStat.MutableItemStat(7940, 384, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 13));
        this.addStat(ModItems.windEdge.get(), new ItemStat.MutableItemStat(11600, 568, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 15)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 5)
                .setElement(EnumElement.WIND));

        this.addStat(ModItems.leatherGlove.get(), new ItemStat.MutableItemStat(190, 13, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5));
        this.addStat(ModItems.brassKnuckles.get(), new ItemStat.MutableItemStat(1580, 74, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 6)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 1));
        this.addStat(ModItems.kote.get(), new ItemStat.MutableItemStat(3170, 136, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 8)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 3));
        this.addStat(ModItems.gloves.get(), new ItemStat.MutableItemStat(5480, 238, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 11)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 4)
                .addAttribute(Attributes.MAX_HEALTH, 10));
        this.addStat(ModItems.bearClaws.get(), new ItemStat.MutableItemStat(8140, 394, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 14)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 9));
        this.addStat(ModItems.fistEarth.get(), new ItemStat.MutableItemStat(12640, 587, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 17)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 11)
                .setElement(EnumElement.EARTH));

        this.addStat(ModItems.rod.get(), new ItemStat.MutableItemStat(281, 32, 0)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 5)
                .addAttribute(ModAttributes.RFDIZ.get(), 15)
                .setElement(EnumElement.FIRE)
                .setSpell(ModSpells.FIREBALL.get(), null, null));
        this.addStat(ModItems.amethystRod.get(), new ItemStat.MutableItemStat(1550, 76, 0)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 9)
                .addAttribute(ModAttributes.RFDIZ.get(), 15)
                .setElement(EnumElement.EARTH));
        this.addStat(ModItems.aquamarineRod.get(), new ItemStat.MutableItemStat(3430, 186, 0)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 13)
                .addAttribute(ModAttributes.RFDIZ.get(), 15)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.friendlyRod.get(), new ItemStat.MutableItemStat(8670, 297, 0)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 17)
                .addAttribute(ModAttributes.RFDIZ.get(), 15)
                .setElement(EnumElement.LOVE));
        this.addStat(ModItems.loveLoveRod.get(), new ItemStat.MutableItemStat(10550, 436, 0)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 21)
                .addAttribute(ModAttributes.RFDIZ.get(), 15)
                .setElement(EnumElement.LOVE));
        this.addStat(ModItems.staff.get(), new ItemStat.MutableItemStat(14110, 599, 0)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 24)
                .addAttribute(ModAttributes.RFDIZ.get(), 15)
                .setElement(EnumElement.EARTH));

        this.addStat(ModItems.hoeScrap.get(), new ItemStat.MutableItemStat(150, 34, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.hoeIron.get(), new ItemStat.MutableItemStat(4500, 121, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 15)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 3));
        this.addStat(ModItems.hoeSilver.get(), new ItemStat.MutableItemStat(25000, 300, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 34)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 5)
                .setElement(EnumElement.EARTH));
        this.addStat(ModItems.hoeGold.get(), new ItemStat.MutableItemStat(0, 550, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 76)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 15)
                .setElement(EnumElement.EARTH));
        this.addStat(ModItems.hoePlatinum.get(), new ItemStat.MutableItemStat(0, 2000, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 111)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 45)
                .setElement(EnumElement.EARTH));

        this.addStat(ModItems.wateringCanScrap.get(), new ItemStat.MutableItemStat(150, 45, 0)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 1)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.wateringCanIron.get(), new ItemStat.MutableItemStat(4500, 164, 0)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 7)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.wateringCanSilver.get(), new ItemStat.MutableItemStat(25000, 300, 0)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 19)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.wateringCanGold.get(), new ItemStat.MutableItemStat(0, 550, 0)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 39)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.wateringCanPlatinum.get(), new ItemStat.MutableItemStat(0, 2000, 0)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 99)
                .setElement(EnumElement.WATER));

        this.addStat(ModItems.sickleScrap.get(), new ItemStat.MutableItemStat(150, 24, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 2));
        this.addStat(ModItems.sickleIron.get(), new ItemStat.MutableItemStat(4500, 118, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 19));
        this.addStat(ModItems.sickleSilver.get(), new ItemStat.MutableItemStat(25000, 300, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 36));
        this.addStat(ModItems.sickleGold.get(), new ItemStat.MutableItemStat(0, 550, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 79)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 5)
                .setElement(EnumElement.WIND));
        this.addStat(ModItems.sicklePlatinum.get(), new ItemStat.MutableItemStat(0, 2000, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 134)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 31)
                .setElement(EnumElement.WIND));

        this.addStat(ModItems.axeScrap.get(), new ItemStat.MutableItemStat(150, 37, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addAttribute(ModAttributes.RFCRIT.get(), 5));
        this.addStat(ModItems.axeIron.get(), new ItemStat.MutableItemStat(4500, 148, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 22)
                .addAttribute(ModAttributes.RFCRIT.get(), 5));
        this.addStat(ModItems.axeSilver.get(), new ItemStat.MutableItemStat(25000, 300, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 39)
                .addAttribute(ModAttributes.RFCRIT.get(), 5));
        this.addStat(ModItems.axeGold.get(), new ItemStat.MutableItemStat(0, 550, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 83)
                .addAttribute(ModAttributes.RFCRIT.get(), 5));
        this.addStat(ModItems.axePlatinum.get(), new ItemStat.MutableItemStat(0, 2000, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 140)
                .addAttribute(ModAttributes.RFCRIT.get(), 15));

        this.addStat(ModItems.hammerScrap.get(), new ItemStat.MutableItemStat(150, 39, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addAttribute(ModAttributes.RFCRIT.get(), -5)
                .addAttribute(ModAttributes.RFSTUN.get(), 10));
        this.addStat(ModItems.hammerIron.get(), new ItemStat.MutableItemStat(4500, 142, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 23)
                .addAttribute(ModAttributes.RFCRIT.get(), -5)
                .addAttribute(ModAttributes.RFSTUN.get(), 10));
        this.addStat(ModItems.hammerSilver.get(), new ItemStat.MutableItemStat(25000, 300, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 47)
                .addAttribute(ModAttributes.RFCRIT.get(), -5)
                .addAttribute(ModAttributes.RFSTUN.get(), 10));
        this.addStat(ModItems.hammerGold.get(), new ItemStat.MutableItemStat(0, 550, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 85)
                .addAttribute(ModAttributes.RFCRIT.get(), -5)
                .addAttribute(ModAttributes.RFSTUN.get(), 10));
        this.addStat(ModItems.hammerPlatinum.get(), new ItemStat.MutableItemStat(0, 2000, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 145)
                .addAttribute(ModAttributes.RFCRIT.get(), -7)
                .addAttribute(ModAttributes.RFSTUN.get(), 10));

        this.addStat(ModItems.fishingRodScrap.get(), new ItemStat.MutableItemStat(150, 35, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 1)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 5));
        this.addStat(ModItems.fishingRodIron.get(), new ItemStat.MutableItemStat(4500, 135, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 9)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 14));
        this.addStat(ModItems.fishingRodSilver.get(), new ItemStat.MutableItemStat(25000, 300, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 26)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 27));
        this.addStat(ModItems.fishingRodGold.get(), new ItemStat.MutableItemStat(0, 550, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 66)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 72));
        this.addStat(ModItems.fishingRodPlatinum.get(), new ItemStat.MutableItemStat(0, 2000, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 89)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 98));

        this.addStat(ModItems.cheapBracelet.get(), new ItemStat.MutableItemStat(120, 21, 0)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 1.5));
        this.addStat(ModItems.bronzeBracelet.get(), new ItemStat.MutableItemStat(850, 38, 0)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 2));

        this.addStat(Items.SHIELD, new ItemStat.MutableItemStat(200, 14, 0)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 2));
        this.addStat(ModItems.smallShield.get(), new ItemStat.MutableItemStat(600, 23, 0)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 4));

        this.addStat(ModItems.scrap.get(), new ItemStat.MutableItemStat(13, 1, 1));
        this.addStat(ModItems.scrapPlus.get(), new ItemStat.MutableItemStat(0, 2, 1)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 2));
        this.addStat("iron", ModTags.iron, new ItemStat.MutableItemStat(150, 2, 5)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 1));
        this.addStat("bronze", ModTags.bronze, new ItemStat.MutableItemStat(400, 14, 13)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 2.4));
        this.addStat("copper", ModTags.copper, new ItemStat.MutableItemStat(200, 9, 10)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 1.6));
        this.addStat("silver", ModTags.silver, new ItemStat.MutableItemStat(1500, 27, 15)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 5));
        this.addStat("gold", ModTags.gold, new ItemStat.MutableItemStat(3500, 34, 20)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 4)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 2));
        this.addStat("platinum", ModTags.platinum, new ItemStat.MutableItemStat(5000, 111, 34)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 25));
        this.addStat("orichalcum", ModTags.orichalcum, new ItemStat.MutableItemStat(20000, 750, 65)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 70));
        this.addStat("dragonic", ModTags.dragonic, new ItemStat.MutableItemStat(0, 1000, 70)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 130)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 90));
        this.addStat(Items.NETHERITE_INGOT, new ItemStat.MutableItemStat(0, 200, 40)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 20)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 20));

        this.addStat("emerald", ModTags.emerald, new ItemStat.MutableItemStat(2500, 5, 24)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 4)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 8));
        this.addStat(Items.DIAMOND, new ItemStat.MutableItemStat(5000, 21, 29)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 12)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 12));
        this.addStat("amethyst", ModTags.amethyst, new ItemStat.MutableItemStat(3500, 18, 24)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 4)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 1));
        this.addStat("aquamarine", ModTags.aquamarine, new ItemStat.MutableItemStat(3500, 23, 24)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 4)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 8));
        this.addStat("ruby", ModTags.ruby, new ItemStat.MutableItemStat(4000, 37, 24)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 4)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 8));
        this.addStat("sapphier", ModTags.sapphire, new ItemStat.MutableItemStat(3500, 24, 24)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 4)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 8));
        this.addStat(ModItems.coreGreen.get(), new ItemStat.MutableItemStat(15000, 1050, 24)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 4)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 8));
        this.addStat(ModItems.coreRed.get(), new ItemStat.MutableItemStat(15000, 1050, 24)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 4)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 8));
        this.addStat(ModItems.coreBlue.get(), new ItemStat.MutableItemStat(15000, 1050, 24)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 4)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 8));
        this.addStat(ModItems.coreYellow.get(), new ItemStat.MutableItemStat(15000, 1050, 24)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 4)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 8));
        this.addStat(ModItems.crystalSkull.get(), new ItemStat.MutableItemStat(25000, 2300, 24)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 4)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 8));
        this.addStat(ModItems.crystalWater.get(), new ItemStat.MutableItemStat(2000, 150, 20)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 5)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.crystalEarth.get(), new ItemStat.MutableItemStat(2000, 150, 20)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 5)
                .setElement(EnumElement.EARTH));
        this.addStat(ModItems.crystalFire.get(), new ItemStat.MutableItemStat(2000, 150, 20)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5)
                .setElement(EnumElement.FIRE));
        this.addStat(ModItems.crystalWind.get(), new ItemStat.MutableItemStat(2000, 150, 20)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 5)
                .setElement(EnumElement.WIND));
        this.addStat(ModItems.crystalLight.get(), new ItemStat.MutableItemStat(2000, 150, 20)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 1.5)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 3)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 1.5)
                .setElement(EnumElement.LIGHT));
        this.addStat(ModItems.crystalDark.get(), new ItemStat.MutableItemStat(2000, 150, 20)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 1.5)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 1.5)
                .setElement(EnumElement.DARK));
        this.addStat(ModItems.crystalLove.get(), new ItemStat.MutableItemStat(2000, 150, 20)
                .addAttribute(ModAttributes.RFDRAIN.get(), 3)
                .setElement(EnumElement.LOVE));
        this.addStat(ModItems.crystalSmall.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.crystalBig.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.crystalMagic.get(), new ItemStat.MutableItemStat(45, 400, 25)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 7)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 1));
        this.addStat(ModItems.crystalRune.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.crystalElectro.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.stickThick.get(), new ItemStat.MutableItemStat(1900, 200, 45)
                .addAttribute(Attributes.ATTACK_DAMAGE, 13));
        this.addStat(ModItems.hornInsect.get(), new ItemStat.MutableItemStat(130 , 21, 8)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3));
        this.addStat(ModItems.hornRigid.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.hornDevil.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.plantStem.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.hornBull.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.movingBranch.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.glue.get(), new ItemStat.MutableItemStat(380, 41, 13)
                .addAttribute(Attributes.ATTACK_DAMAGE, 1)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 1)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 1));
        this.addStat(ModItems.devilBlood.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.paraPoison.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.poisonKing.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.featherBlack.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.featherThunder.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.featherYellow.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.dragonFin.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.turtleShell.get(), new ItemStat.MutableItemStat(160, 30, 16)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 5)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 3)
                .addAttribute(ModAttributes.RFCRIT.get(), -3));
        this.addStat(ModItems.fishFossil.get(), new ItemStat.MutableItemStat(180 , 30, 19)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 5)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 1));
        this.addStat(ModItems.skull.get(), new ItemStat.MutableItemStat(100, 1000, 35)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 9)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 4)
                .addAttribute(ModAttributes.RFCOLD.get(), 5));
        this.addStat(ModItems.dragonBones.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.tortoiseShell.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.rock.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.stoneRound.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.stoneTiny.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.stoneGolem.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.tabletGolem.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.stoneSpirit.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.tabletTruth.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.yarn.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.oldBandage.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.ambrosiasThorns.get(), new ItemStat.MutableItemStat(7500, 350, 21)
                .addAttribute(Attributes.ATTACK_DAMAGE, 7)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 7)
                .addAttribute(ModAttributes.RFSLEEP.get(), 10));
        this.addStat(ModItems.threadSpider.get(), new ItemStat.MutableItemStat(370, 28, 17)
                .addAttribute(Attributes.ATTACK_DAMAGE, 2)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 2)
                .addAttribute(ModAttributes.RFPARA.get(), 5));
        this.addStat(ModItems.puppetryStrings.get(), new ItemStat.MutableItemStat(30000 , 1000, 37)
                .addAttribute(Attributes.ATTACK_DAMAGE, 6)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 9)
                .addAttribute(ModAttributes.RFSEAL.get(), 15)
                .addAttribute(ModAttributes.RFPARA.get(), 5));
        this.addStat(ModItems.vine.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.tailScorpion.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.strongVine.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.threadPretty.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.tailChimera.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.arrowHead.get(), new ItemStat.MutableItemStat(80, 10, 2)
                .addAttribute(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.bladeShard.get(), new ItemStat.MutableItemStat(139 , 25, 9)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3));
        this.addStat(ModItems.brokenHilt.get(), new ItemStat.MutableItemStat(550, 50, 22)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 2)
                .addAttribute(ModAttributes.RFRESPOISON.get(), 5)
                .addAttribute(ModAttributes.RFRESSEAL.get(), 5)
                .addAttribute(ModAttributes.RFRESPARA.get(), 5)
                .addAttribute(ModAttributes.RFRESSLEEP.get(), 5)
                .addAttribute(ModAttributes.RFRESFAT.get(), 5)
                .addAttribute(ModAttributes.RFRESCOLD.get(), 5));
        this.addStat(ModItems.brokenBox.get(), new ItemStat.MutableItemStat(1000 , 200, 48)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 4)
                .addAttribute(ModAttributes.RFRESPOISON.get(), 10)
                .addAttribute(ModAttributes.RFRESSEAL.get(), 10)
                .addAttribute(ModAttributes.RFRESPARA.get(), 10)
                .addAttribute(ModAttributes.RFRESSLEEP.get(), 10)
                .addAttribute(ModAttributes.RFRESFAT.get(), 10)
                .addAttribute(ModAttributes.RFRESCOLD.get(), 10));
        this.addStat(ModItems.bladeGlistening.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.greatHammerShard.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.hammerPiece.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.shoulderPiece.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.piratesArmor.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.screwRusty.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.screwShiny.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.rockShardLeft.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.rockShardRight.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.MTGUPlate.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.brokenIceWall.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.furSmall.get(), new ItemStat.MutableItemStat(35, 7, 1)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 1)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 1)
                .addAttribute(ModAttributes.RFRESDIZ.get(), 1));
        this.addStat(ModItems.furMedium.get(), new ItemStat.MutableItemStat(1000, 100, 29)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 5)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 3)
                .addAttribute(ModAttributes.RFRESDIZ.get(), 10));
        this.addStat(ModItems.furLarge.get(), new ItemStat.MutableItemStat(3000, 500, 55)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 10)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 5)
                .addAttribute(ModAttributes.RFRESDIZ.get(), 25)
                .addAttribute(ModAttributes.RFRESSLEEP.get(), 5));
        this.addStat(ModItems.fur.get(), new ItemStat.MutableItemStat(130, 23, 7)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 2)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 1)
                .addAttribute(ModAttributes.RFRESDIZ.get(), 3));
        this.addStat(ModItems.furball.get(), new ItemStat.MutableItemStat(900 , 120, 38)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 8)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 4)
                .addAttribute(ModAttributes.RFRESDIZ.get(), 10)
                .addAttribute(ModAttributes.RFRESSLEEP.get(), 3));
        this.addStat(ModItems.downYellow.get(), new ItemStat.MutableItemStat(300, 33, 21)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 3)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 2)
                .addAttribute(ModAttributes.RFRESDIZ.get(), 5));
        this.addStat(ModItems.furQuality.get(), new ItemStat.MutableItemStat(650, 45, 36)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 4)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 3)
                .addAttribute(ModAttributes.RFRESDIZ.get(), 7));
        this.addStat(ModItems.downPenguin.get(), new ItemStat.MutableItemStat(1250, 129, 59)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 13)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 7)
                .addAttribute(ModAttributes.RFRESDIZ.get(), 13));
            this.addStat(ModItems.lightningMane.get(), new ItemStat.MutableItemStat(13000, 600, 31)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 8)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 6)
                .addAttribute(ModAttributes.RFRESDIZ.get(), 17));
        this.addStat(ModItems.furRedLion.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.furBlueLion.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.chestHair.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.spore.get(), new ItemStat.MutableItemStat(110, 19, 9)
                .addAttribute(ModAttributes.RF_MAGIC.get(), 2));
        this.addStat(ModItems.powderPoison.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.sporeHoly.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.fairyDust.get(), new ItemStat.MutableItemStat(300, 40, 19)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 5));
        this.addStat(ModItems.fairyElixir.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.root.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.powderMagic.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.powderMysterious.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.magic.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.ashEarth.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.ashFire.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.ashWater.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.turnipsMiracle.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.melodyBottle.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.clothCheap.get(), new ItemStat.MutableItemStat(80, 12, 4)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 1)
                .addAttribute(ModAttributes.RFRESCRIT.get(), 1));
        this.addStat(ModItems.clothQuality.get(), new ItemStat.MutableItemStat(800, 100, 18)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 5)
                .addAttribute(ModAttributes.RFRESCRIT.get(), 5));
        this.addStat(ModItems.clothQualityWorn.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.clothSilk.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.ghostHood.get(), new ItemStat.MutableItemStat(70, 650, 21)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 2)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 2)
                .addAttribute(ModAttributes.RFRESSEAL.get(), 25));
        this.addStat(ModItems.gloveGiant.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.gloveBlueGiant.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.carapaceInsect.get(), new ItemStat.MutableItemStat(75 , 11, 8)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 1)
                .addAttribute(ModAttributes.RFRESPOISON.get(), 15));
        this.addStat(ModItems.carapacePretty.get(), new ItemStat.MutableItemStat(750, 85, 21)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 4)
                .addAttribute(ModAttributes.RFRESSEAL.get(), 5)
                .addAttribute(ModAttributes.RFRESPARA.get(), 20));
        this.addStat(ModItems.clothAncientOrc.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.jawInsect.get(), new ItemStat.MutableItemStat(100, 23, 14)
                .addAttribute(Attributes.ATTACK_DAMAGE, 2));
        this.addStat(ModItems.clawPanther.get(), new ItemStat.MutableItemStat(450, 55, 28)
                .addAttribute(Attributes.ATTACK_DAMAGE, 4));
        this.addStat(ModItems.clawMagic.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.fangWolf.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.fangGoldWolf.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.clawPalm.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.clawMalm.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.giantsNail.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.clawChimera.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.tuskIvory.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.tuskUnbrokenIvory.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.scorpionPincer.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.dangerousScissors.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.propellorCheap.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.propellorQuality.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.fangDragon.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.jawQueen.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.windDragonTooth.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.giantsNailBig.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.scaleWet.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.scaleGrimoire.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.scaleDragon.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.scaleCrimson.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.scaleBlue.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.scaleGlitter.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.scaleLove.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.scaleBlack.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.scaleFire.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.scaleEarth.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.scaleLegend.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.steelDouble.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.steelTen.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.glittaAugite.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.invisStone.get(), new ItemStat.MutableItemStat(0, 750, 24));
        this.addStat(ModItems.lightOre.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.runeSphereShard.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.shadeStone.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.racoonLeaf.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.icyNose.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.bigBirdsComb.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.rafflesiaPetal.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.cursedDoll.get(), new ItemStat.MutableItemStat(750, 27000, 39)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 4)
                .addAttribute(ModAttributes.RF_MAGIC_DEFENCE.get(), 7)
                .addAttribute(ModAttributes.RFRESSEAL.get(), 10)
                .addAttribute(ModAttributes.RFRESPARA.get(), 10)
                .addAttribute(ModAttributes.RFRESDRAIN.get(), 15));
        this.addStat(ModItems.warriorsProof.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.proofOfRank.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.throneOfEmpire.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.whiteStone.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.rareCan.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.can.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.boots.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.lawn.get(), new ItemStat.MutableItemStat(0, 0, 0));

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
        this.addStat(ModItems.antidoteGrass.get(), new ItemStat.MutableItemStat(120, 5, 1)
                .addAttribute(ModAttributes.RFRESPOISON.get(), 5));
        this.addStat(ModItems.medicinalHerb.get(), 150, 10, 1);
        this.addStat(ModItems.bambooSprout.get(), 100, 10, 1);
        this.addStat(ModItems.mushroom.get(), 100, 10, 1);
        this.addStat(ModItems.monarchMushroom.get(), 300, 15, 1);
        this.addStat(ModItems.elliLeaves.get(), 250, 15, 1);

        this.addStat(ModItems.forgingBread.get(), 600, 100, 1);
        this.addStat(ModItems.cookingBread.get(), 600, 100, 1);
        this.addStat(ModItems.chemistryBread.get(), 600, 100, 1);
        this.addStat(ModItems.armorBread.get(), 600, 100, 1);
        this.addStat(ModItems.eggS.get(), 1250, 250, 1);
        this.addStat(ModItems.eggM.get(), 1500, 300, 1);
        this.addStat(ModItems.eggL.get(), 1700, 400, 1);
        this.addStat(ModItems.milkS.get(), 1250, 250, 1);
        this.addStat(ModItems.milkM.get(), 1500, 300, 1);
        this.addStat(ModItems.milkL.get(), 1700, 400, 1);

        this.addStat(ModItems.onigiri.get(), 150, 50, 1);
        this.addStat(ModItems.pickledTurnip.get(), 300, 45, 1);
        this.addStat(ModItems.flan.get(), 2700, 800, 1);
        this.addStat(ModItems.hotMilk.get(), 800, 300, 1);
        this.addStat(ModItems.hotChocolate.get(), 1000, 200, 1);
        this.addStat(ModItems.bakedApple.get(), 1700, 200, 1);
        this.addStat(ModItems.friedVeggies.get(), 5500, 1300, 1);
        this.addStat(ModItems.failedDish.get(), 100, 2, 1);
        this.addStat(ModItems.disastrousDish.get(), 1500, 50, 1);

    }
}
