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
        //=====Vanilla stuff
        //Blocks
        this.addStat("stone", ItemTags.STONE_CRAFTING_MATERIALS, new ItemStat.Builder(5, 1, 1)
                .addAttribute(ModAttributes.DEFENCE.get(), 1)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.2));
        this.addStat("sand", ItemTags.SAND, new ItemStat.Builder(5, 1, 1)
                .addAttribute(ModAttributes.DEFENCE.get(), 1)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.2));
        this.addStat("gravel", Items.GRAVEL, new ItemStat.Builder(5, 1, 1)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 1)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.2));
        this.addStat("logs", ItemTags.LOGS, new ItemStat.Builder(10, 2, 3)
                .addAttribute(Attributes.MAX_HEALTH, 3)
                .addAttribute(ModAttributes.DEFENCE.get(), 2)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.25));
        this.addStat("wool", ItemTags.WOOL, new ItemStat.Builder(25, 3, 5)
                .addAttribute(ModAttributes.DEFENCE.get(), 2)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 2)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.2)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.2));
        this.addStat("obsidian", Items.OBSIDIAN, new ItemStat.Builder(100, 5, 7)
                .addAttribute(ModAttributes.DEFENCE.get(), 5)
                .addAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.2)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));

        //Misc
        this.addStat("sapling", ItemTags.SAPLINGS, new ItemStat.Builder(0, 0, 5)
                .addAttribute(Attributes.MAX_HEALTH, 3)
                .addMonsterStat(Attributes.MAX_HEALTH, 0.15));
        this.addStat("flowers", ItemTags.FLOWERS, new ItemStat.Builder(0, 0, 3)
                .addAttribute(Attributes.MAX_HEALTH, 2)
                .addMonsterStat(Attributes.MAX_HEALTH, 0.1));
        this.addStat("candles", ItemTags.CANDLES, new ItemStat.Builder(0, 0, 1)
                .addAttribute(ModAttributes.MAGIC.get(), 1)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.1));

        this.addStat(Items.STICK, new ItemStat.Builder(11, 1, 1)
                .addAttribute(Attributes.ATTACK_DAMAGE, 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.2));
        this.addStat("arrows", ItemTags.ARROWS, new ItemStat.Builder(45, 2, 2)
                .addAttribute(Attributes.ATTACK_DAMAGE, 2)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.22)
                .setSpell(ModSpells.ARROW.get(), null, null));

        this.addStat("coals", ItemTags.COALS, new ItemStat.Builder(65, 5, 2)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 1)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.2));
        this.addStat(Items.SNOWBALL, new ItemStat.Builder(15, 1, 5)
                .addAttribute(Attributes.ATTACK_DAMAGE, 1)
                .addAttribute(ModAttributes.MAGIC.get(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.1)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.1)
                .setSpell(ModSpells.SNOWBALL.get(), null, null));
        this.addStat(Items.STRING, new ItemStat.Builder(75, 7, 13)
                .addAttribute(Attributes.ATTACK_DAMAGE, 2)
                .addAttribute(ModAttributes.MAGIC.get(), 2)
                .addAttribute(ModAttributes.RF_PARA.get(), 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.1)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.1)
                .addAttribute(ModAttributes.RF_PARA.get(), 0.25));
        this.addStat(Items.FEATHER, new ItemStat.Builder(85, 7, 11)
                .addAttribute(ModAttributes.MAGIC.get(), 2)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.3));
        this.addStat(Items.ROTTEN_FLESH, new ItemStat.Builder(50, 5, 9)
                .addAttribute(Attributes.MAX_HEALTH, 5)
                .addMonsterStat(Attributes.MAX_HEALTH, 0.5));
        this.addStat(Items.BONE, new ItemStat.Builder(75, 5, 6)
                .addAttribute(ModAttributes.DEFENCE.get(), 2)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.3));
        this.addStat(Items.GUNPOWDER, new ItemStat.Builder(100, 7, 21)
                .addAttribute(Attributes.ATTACK_DAMAGE, 1)
                .addAttribute(ModAttributes.MAGIC.get(), 5)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.3)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.7));
        this.addStat(Items.REDSTONE, new ItemStat.Builder(120, 7, 15)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 3)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.5));
        this.addStat(Items.FLINT, new ItemStat.Builder(103, 9, 8)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.3));
        this.addStat(Items.QUARTZ, new ItemStat.Builder(263, 21, 14)
                .addAttribute(Attributes.ATTACK_DAMAGE, 7)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(Items.GLOWSTONE_DUST, new ItemStat.Builder(200, 17, 17)
                .addAttribute(ModAttributes.MAGIC.get(), 4)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.3));
        this.addStat(Items.LAPIS_LAZULI, new ItemStat.Builder(54, 4, 13)
                .addAttribute(ModAttributes.MAGIC.get(), 2)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.1));
        this.addStat(Items.LEATHER, new ItemStat.Builder(170, 14, 23)
                .addAttribute(ModAttributes.DEFENCE.get(), 9)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 1));
        this.addStat(Items.CLAY_BALL, new ItemStat.Builder(100, 9, 19)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 2)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.1));
        this.addStat(Items.BRICK, new ItemStat.Builder(132, 11, 20)
                .addAttribute(ModAttributes.DEFENCE.get(), 5)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 2)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 1));
        this.addStat(Items.PAPER, new ItemStat.Builder(167, 13, 4)
                .addAttribute(ModAttributes.MAGIC.get(), 3)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.3));
        this.addStat(Items.BOOK, new ItemStat.Builder(500, 42, 26)
                .addAttribute(ModAttributes.MAGIC.get(), 12)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.5));
        this.addStat(Items.PRISMARINE_SHARD, new ItemStat.Builder(389, 34, 17)
                .addAttribute(ModAttributes.DEFENCE.get(), 2.5)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 5)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.5));
        this.addStat(Items.PRISMARINE_CRYSTALS, new ItemStat.Builder(523, 46, 25)
                .addAttribute(ModAttributes.MAGIC.get(), 8)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.5));
        this.addStat(Items.BLAZE_ROD, new ItemStat.Builder(350, 23, 25)
                .addAttribute(Attributes.ATTACK_DAMAGE, 12)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(Items.ENDER_PEARL, new ItemStat.Builder(400, 28, 21)
                .addAttribute(ModAttributes.MAGIC.get(), 3)
                .addAttribute(ModAttributes.RF_RES_STUN.get(), 5)
                .addMonsterStat(ModAttributes.RF_RES_STUN.get(), 0.5));
        this.addStat(Items.SLIME_BALL, new ItemStat.Builder(375, 31, 18)
                .addAttribute(ModAttributes.MAGIC.get(), 5)
                .addAttribute(ModAttributes.DEFENCE.get(), 12)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.6));
        this.addStat(Items.MAGMA_CREAM, new ItemStat.Builder(250, 21, 20)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addAttribute(ModAttributes.MAGIC.get(), 3)
                .addAttribute(ModAttributes.DEFENCE.get(), 8)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.4)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.6));
        this.addStat(Items.GHAST_TEAR, new ItemStat.Builder(750, 49, 22)
                .addAttribute(Attributes.MAX_HEALTH, 15)
                .addAttribute(ModAttributes.MAGIC.get(), 2)
                .addAttribute(ModAttributes.DEFENCE.get(), 3)
                .addMonsterStat(Attributes.MAX_HEALTH, 2));
        this.addStat(Items.PHANTOM_MEMBRANE, new ItemStat.Builder(600, 55, 19)
                .addAttribute(Attributes.MOVEMENT_SPEED, 0.03)
                .addMonsterStat(Attributes.MAX_HEALTH, 0.0008));
        this.addStat(Items.SUGAR, new ItemStat.Builder(25, 13, 18)
                .addAttribute(Attributes.MOVEMENT_SPEED, 0.01)
                .addMonsterStat(Attributes.MAX_HEALTH, 0.0005));
        this.addStat(Items.TOTEM_OF_UNDYING, new ItemStat.Builder(3500, 110, 17)
                .addAttribute(ModAttributes.DEFENCE.get(), 9)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 9)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.4)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.4)
                .setSpell(ModSpells.EVOKERFANG.get(), null, null));
        this.addStat(Items.WITHER_SKELETON_SKULL, new ItemStat.Builder(5000, 170, 29)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5)
                .addAttribute(ModAttributes.MAGIC.get(), 20)
                .addMonsterStat(ModAttributes.MAGIC.get(), 1)
                .setSpell(ModSpells.WITHERSKULL.get(), null, null)
                .setElement(EnumElement.DARK));
        this.addStat(Items.NETHER_STAR, new ItemStat.Builder(20000, 600, 45)
                .addAttribute(Attributes.ATTACK_DAMAGE, 17)
                .addAttribute(ModAttributes.DEFENCE.get(), 7)
                .addAttribute(ModAttributes.MAGIC.get(), 35)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 9)
                .addAttribute(ModAttributes.RF_DRAIN.get(), 5)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .addMonsterStat(ModAttributes.MAGIC.get(), 3)
                .setSpell(ModSpells.WITHERSKULL.get(), null, null)
                .setElement(EnumElement.DARK));

        //=======
        this.addStat(ModItems.roundoff.get(), new ItemStat.Builder(750, 50, 0)
                .addMonsterStat(Attributes.MAX_HEALTH, 5)
                .addMonsterStat(ModAttributes.RF_RES_SEAL.get(), 0.5));
        this.addStat(ModItems.paraGone.get(), new ItemStat.Builder(750, 50, 0)
                .addMonsterStat(Attributes.MAX_HEALTH, 5)
                .addMonsterStat(ModAttributes.RF_RES_PARA.get(), 0.5));
        this.addStat(ModItems.coldMed.get(), new ItemStat.Builder(750, 50, 0)
                .addMonsterStat(Attributes.MAX_HEALTH, 5)
                .addMonsterStat(ModAttributes.RF_RES_COLD.get(), 0.5));
        this.addStat(ModItems.antidote.get(), new ItemStat.Builder(750, 50, 0)
                .addMonsterStat(Attributes.MAX_HEALTH, 5)
                .addMonsterStat(ModAttributes.RF_RES_POISON.get(), 0.5));
        this.addStat(ModItems.recoveryPotion.get(), new ItemStat.Builder(300, 25, 0)
                .addMonsterStat(Attributes.MAX_HEALTH, 3));
        this.addStat(ModItems.healingPotion.get(), new ItemStat.Builder(500, 35, 0)
                .addMonsterStat(Attributes.MAX_HEALTH, 5));
        this.addStat(ModItems.mysteryPotion.get(), new ItemStat.Builder(3000, 250, 0)
                .addMonsterStat(Attributes.MAX_HEALTH, 6));
        this.addStat(ModItems.magicalPotion.get(), new ItemStat.Builder(6000, 500, 0)
                .addMonsterStat(Attributes.MAX_HEALTH, 7));
        this.addStat(ModItems.invinciroid.get(), new ItemStat.Builder(25000, 1500, 0));
        this.addStat(ModItems.lovePotion.get(), new ItemStat.Builder(50000, 2000, 0));
        this.addStat(ModItems.formuade.get(), new ItemStat.Builder(20000, 700, 0));
        this.addStat(ModItems.objectX.get(), new ItemStat.Builder(6000, 500, 15));

        this.addStat(ModItems.broadSword.get(), new ItemStat.Builder(100, 16, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 4)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(shortSwordBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(2.5))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.steelSword.get(), new ItemStat.Builder(1320, 54, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 8)
                .addAttribute(ModAttributes.RF_DIZ.get(), 5)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(shortSwordBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(2.5))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.steelSwordPlus.get(), new ItemStat.Builder(2310, 99, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 14)
                .addAttribute(ModAttributes.RF_DIZ.get(), 5)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(shortSwordBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(2.5))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.cutlass.get(), new ItemStat.Builder(5240, 210, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 21)
                .addAttribute(ModAttributes.RF_DIZ.get(), 5)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 2)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(shortSwordBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(2.5))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.aquaSword.get(), new ItemStat.Builder(7850, 357, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 34)
                .addAttribute(ModAttributes.MAGIC.get(), 9)
                .addAttribute(ModAttributes.RF_DIZ.get(), 5)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(shortSwordBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(2.5))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.invisiBlade.get(), new ItemStat.Builder(12350, 571, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 49)
                .addAttribute(ModAttributes.MAGIC.get(), 6)
                .addAttribute(ModAttributes.RF_DIZ.get(), 5)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(shortSwordBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(2.5))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));

        this.addStat(ModItems.claymore.get(), new ItemStat.Builder(210, 17, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 6)
                .addAttribute(ModAttributes.RF_DIZ.get(), 10)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(longSwordBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.zweihaender.get(), new ItemStat.Builder(1360, 58, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 12)
                .addAttribute(ModAttributes.RF_DIZ.get(), 10)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(longSwordBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.zweihaenderPlus.get(), new ItemStat.Builder(2170, 104, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 19)
                .addAttribute(ModAttributes.RF_DIZ.get(), 10)
                .addAttribute(ModAttributes.RF_STUN.get(), 15)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(longSwordBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.greatSword.get(), new ItemStat.Builder(4960, 231, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 27)
                .addAttribute(ModAttributes.RF_DIZ.get(), 10)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(longSwordBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.seaCutter.get(), new ItemStat.Builder(9170, 404, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 41)
                .addAttribute(ModAttributes.RF_DIZ.get(), 10)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(longSwordBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.cycloneBlade.get(), new ItemStat.Builder(13680, 623, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 53)
                .addAttribute(ModAttributes.RF_DIZ.get(), 10)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(longSwordBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));

        this.addStat(ModItems.spear.get(), new ItemStat.Builder(190, 13, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5)
                .addAttribute(ModAttributes.RF_DIZ.get(), 6)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(spearBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(5.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.woodStaff.get(), new ItemStat.Builder(1270, 56, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 8)
                .addAttribute(ModAttributes.RF_DIZ.get(), 6)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(spearBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(5.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.lance.get(), new ItemStat.Builder(2310, 101, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 12)
                .addAttribute(ModAttributes.DEFENCE.get(), 1)
                .addAttribute(ModAttributes.RF_DIZ.get(), 6)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(spearBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(5.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.lancePlus.get(), new ItemStat.Builder(4460, 198, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 20)
                .addAttribute(ModAttributes.DEFENCE.get(), 2)
                .addAttribute(ModAttributes.RF_DIZ.get(), 6)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(spearBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(5.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.needleSpear.get(), new ItemStat.Builder(7770, 333, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 35)
                .addAttribute(ModAttributes.DEFENCE.get(), 4)
                .addAttribute(ModAttributes.RF_DIZ.get(), 6)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(spearBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(5.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.trident.get(), new ItemStat.Builder(13280, 543, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 50)
                .addAttribute(ModAttributes.DEFENCE.get(), 6)
                .addAttribute(ModAttributes.RF_DIZ.get(), 6)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(spearBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(5.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));

        this.addStat(ModItems.battleAxe.get(), new ItemStat.Builder(250, 19, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 9)
                .addAttribute(ModAttributes.RF_CRIT.get(), 5)
                .addAttribute(ModAttributes.RF_DIZ.get(), 2)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(axeHammerBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.battleScythe.get(), new ItemStat.Builder(1430, 60, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 15)
                .addAttribute(ModAttributes.RF_CRIT.get(), 15)
                .addAttribute(ModAttributes.RF_DIZ.get(), 2)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(axeHammerBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.poleAxe.get(), new ItemStat.Builder(3250, 147, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 25)
                .addAttribute(ModAttributes.RF_CRIT.get(), 7)
                .addAttribute(ModAttributes.RF_DIZ.get(), 2)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(axeHammerBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.poleAxePlus.get(), new ItemStat.Builder(5430, 245, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 39)
                .addAttribute(ModAttributes.RF_CRIT.get(), 7)
                .addAttribute(ModAttributes.RF_DIZ.get(), 2)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(axeHammerBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.greatAxe.get(), new ItemStat.Builder(9580, 417, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 54)
                .addAttribute(ModAttributes.RF_CRIT.get(), 11)
                .addAttribute(ModAttributes.RF_DIZ.get(), 2)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(axeHammerBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.tomohawk.get(), new ItemStat.Builder(14360, 683, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 70)
                .addAttribute(ModAttributes.RF_CRIT.get(), 15)
                .addAttribute(ModAttributes.RF_DIZ.get(), 2)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(axeHammerBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));

        this.addStat(ModItems.battleHammer.get(), new ItemStat.Builder(245, 18, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 10)
                .addAttribute(ModAttributes.RF_STUN.get(), 5)
                .addAttribute(ModAttributes.RF_DIZ.get(), 20)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(axeHammerBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.bat.get(), new ItemStat.Builder(1240, 54, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 13)
                .addAttribute(ModAttributes.RF_STUN.get(), 6)
                .addAttribute(ModAttributes.RF_DIZ.get(), 20)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(axeHammerBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.warHammer.get(), new ItemStat.Builder(2960, 138, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 20)
                .addAttribute(ModAttributes.RF_STUN.get(), 6)
                .addAttribute(ModAttributes.RF_DIZ.get(), 20)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(axeHammerBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.warHammerPlus.get(), new ItemStat.Builder(6340, 265, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 30)
                .addAttribute(ModAttributes.RF_STUN.get(), 7)
                .addAttribute(ModAttributes.RF_DIZ.get(), 20)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(axeHammerBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.ironBat.get(), new ItemStat.Builder(9350, 421, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 44)
                .addAttribute(ModAttributes.RF_STUN.get(), 9)
                .addAttribute(ModAttributes.RF_DIZ.get(), 20)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(axeHammerBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.greatHammer.get(), new ItemStat.Builder(14740, 658, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 61)
                .addAttribute(ModAttributes.RF_STUN.get(), 11)
                .addAttribute(ModAttributes.RF_DIZ.get(), 20)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(axeHammerBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));

        this.addStat(ModItems.shortDagger.get(), new ItemStat.Builder(230, 12, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addAttribute(ModAttributes.RF_DIZ.get(), 3)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(dualBladeBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(2.5f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.steelEdge.get(), new ItemStat.Builder(950, 44, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 7)
                .addAttribute(ModAttributes.RF_DIZ.get(), 3)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(dualBladeBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(2.5f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.frostEdge.get(), new ItemStat.Builder(2610, 121, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 12)
                .addAttribute(ModAttributes.MAGIC.get(), 4)
                .addAttribute(ModAttributes.RF_DIZ.get(), 3)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(dualBladeBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(2.5f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.ironEdge.get(), new ItemStat.Builder(4910, 230, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 18)
                .addAttribute(ModAttributes.RF_DIZ.get(), 3)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(dualBladeBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(2.5f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.thiefKnife.get(), new ItemStat.Builder(7940, 384, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 27)
                .addAttribute(ModAttributes.RF_DIZ.get(), 3)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(dualBladeBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(2.5f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.windEdge.get(), new ItemStat.Builder(11600, 568, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 40)
                .addAttribute(ModAttributes.MAGIC.get(), 9)
                .addAttribute(ModAttributes.RF_DIZ.get(), 3)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(dualBladeBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(2.5f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setElement(EnumElement.WIND));

        this.addStat(ModItems.leatherGlove.get(), new ItemStat.Builder(190, 13, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5)
                .addAttribute(ModAttributes.RF_DIZ.get(), 3)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(gloveBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(2.5f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.brassKnuckles.get(), new ItemStat.Builder(1580, 74, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 9)
                .addAttribute(ModAttributes.DEFENCE.get(), 1)
                .addAttribute(ModAttributes.RF_DIZ.get(), 3)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(gloveBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(2.5f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.kote.get(), new ItemStat.Builder(3170, 136, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 16)
                .addAttribute(ModAttributes.DEFENCE.get(), 3)
                .addAttribute(ModAttributes.RF_DIZ.get(), 3)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(gloveBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(2.5f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.gloves.get(), new ItemStat.Builder(5480, 238, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 22)
                .addAttribute(ModAttributes.DEFENCE.get(), 4)
                .addAttribute(Attributes.MAX_HEALTH, 15)
                .addAttribute(ModAttributes.RF_DIZ.get(), 3)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(gloveBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(2.5f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.bearClaws.get(), new ItemStat.Builder(8140, 394, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 33)
                .addAttribute(ModAttributes.DEFENCE.get(), 9)
                .addAttribute(ModAttributes.RF_DIZ.get(), 3)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(gloveBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(2.5f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.fistEarth.get(), new ItemStat.Builder(12640, 587, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 48)
                .addAttribute(ModAttributes.MAGIC.get(), 10)
                .addAttribute(ModAttributes.DEFENCE.get(), 14)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 5)
                .addAttribute(ModAttributes.RF_DIZ.get(), 3)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(gloveBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(2.5f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setElement(EnumElement.EARTH));

        this.addStat(ModItems.rod.get(), new ItemStat.Builder(281, 32, 0)
                .addAttribute(ModAttributes.MAGIC.get(), 5)
                .addAttribute(ModAttributes.RF_DIZ.get(), 15)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(staffBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.5)
                .setElement(EnumElement.FIRE)
                .setSpell(ModSpells.FIREBALL.get(), null, null));
        this.addStat(ModItems.amethystRod.get(), new ItemStat.Builder(1550, 76, 0)
                .addAttribute(ModAttributes.MAGIC.get(), 13)
                .addAttribute(ModAttributes.RF_DIZ.get(), 15)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(staffBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.5)
                .setElement(EnumElement.EARTH));
        this.addStat(ModItems.aquamarineRod.get(), new ItemStat.Builder(3430, 186, 0)
                .addAttribute(ModAttributes.MAGIC.get(), 17)
                .addAttribute(ModAttributes.RF_DIZ.get(), 15)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(staffBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.5)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.friendlyRod.get(), new ItemStat.Builder(8670, 297, 0)
                .addAttribute(ModAttributes.MAGIC.get(), 28)
                .addAttribute(ModAttributes.RF_DIZ.get(), 15)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(staffBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(ModAttributes.MAGIC.get(), 1)
                .setElement(EnumElement.LOVE));
        this.addStat(ModItems.loveLoveRod.get(), new ItemStat.Builder(10550, 436, 0)
                .addAttribute(ModAttributes.MAGIC.get(), 41)
                .addAttribute(ModAttributes.RF_DIZ.get(), 15)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(staffBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(ModAttributes.MAGIC.get(), 1)
                .setElement(EnumElement.LOVE));
        this.addStat(ModItems.staff.get(), new ItemStat.Builder(14110, 599, 0)
                .addAttribute(ModAttributes.MAGIC.get(), 66)
                .addAttribute(ModAttributes.RF_DIZ.get(), 15)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(staffBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(ModAttributes.MAGIC.get(), 1)
                .setElement(EnumElement.EARTH));

        this.addStat(ModItems.hoeScrap.get(), new ItemStat.Builder(150, 34, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 1)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.hoeIron.get(), new ItemStat.Builder(4500, 121, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 15)
                .addAttribute(ModAttributes.MAGIC.get(), 3)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.hoeSilver.get(), new ItemStat.Builder(25000, 300, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 34)
                .addAttribute(ModAttributes.MAGIC.get(), 5)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(EnumElement.EARTH));
        this.addStat(ModItems.hoeGold.get(), new ItemStat.Builder(0, 550, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 76)
                .addAttribute(ModAttributes.MAGIC.get(), 15)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(EnumElement.EARTH));
        this.addStat(ModItems.hoePlatinum.get(), new ItemStat.Builder(0, 2000, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 111)
                .addAttribute(ModAttributes.MAGIC.get(), 45)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(EnumElement.EARTH));

        this.addStat(ModItems.wateringCanScrap.get(), new ItemStat.Builder(150, 45, 0)
                .addAttribute(ModAttributes.MAGIC.get(), 1)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.wateringCanIron.get(), new ItemStat.Builder(4500, 164, 0)
                .addAttribute(ModAttributes.MAGIC.get(), 7)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.wateringCanSilver.get(), new ItemStat.Builder(25000, 300, 0)
                .addAttribute(ModAttributes.MAGIC.get(), 19)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.wateringCanGold.get(), new ItemStat.Builder(0, 550, 0)
                .addAttribute(ModAttributes.MAGIC.get(), 39)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.wateringCanPlatinum.get(), new ItemStat.Builder(0, 2000, 0)
                .addAttribute(ModAttributes.MAGIC.get(), 99)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(EnumElement.WATER));

        this.addStat(ModItems.sickleScrap.get(), new ItemStat.Builder(150, 24, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 2)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.sickleIron.get(), new ItemStat.Builder(4500, 118, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 19)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.sickleSilver.get(), new ItemStat.Builder(25000, 300, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 36)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.sickleGold.get(), new ItemStat.Builder(0, 550, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 79)
                .addAttribute(ModAttributes.MAGIC.get(), 5)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(EnumElement.WIND));
        this.addStat(ModItems.sicklePlatinum.get(), new ItemStat.Builder(0, 2000, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 134)
                .addAttribute(ModAttributes.MAGIC.get(), 31)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(EnumElement.WIND));

        this.addStat(ModItems.axeScrap.get(), new ItemStat.Builder(150, 37, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addAttribute(ModAttributes.RF_CRIT.get(), 5)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.axeIron.get(), new ItemStat.Builder(4500, 148, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 22)
                .addAttribute(ModAttributes.RF_CRIT.get(), 5)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.axeSilver.get(), new ItemStat.Builder(25000, 300, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 39)
                .addAttribute(ModAttributes.RF_CRIT.get(), 5)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.axeGold.get(), new ItemStat.Builder(0, 550, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 83)
                .addAttribute(ModAttributes.RF_CRIT.get(), 5)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.axePlatinum.get(), new ItemStat.Builder(0, 2000, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 140)
                .addAttribute(ModAttributes.RF_CRIT.get(), 15)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));

        this.addStat(ModItems.hammerScrap.get(), new ItemStat.Builder(150, 39, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addAttribute(ModAttributes.RF_CRIT.get(), -5)
                .addAttribute(ModAttributes.RF_STUN.get(), 10)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.hammerIron.get(), new ItemStat.Builder(4500, 142, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 23)
                .addAttribute(ModAttributes.RF_CRIT.get(), -5)
                .addAttribute(ModAttributes.RF_STUN.get(), 10)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.hammerSilver.get(), new ItemStat.Builder(25000, 300, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 47)
                .addAttribute(ModAttributes.RF_CRIT.get(), -5)
                .addAttribute(ModAttributes.RF_STUN.get(), 10)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.hammerGold.get(), new ItemStat.Builder(0, 550, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 85)
                .addAttribute(ModAttributes.RF_CRIT.get(), -5)
                .addAttribute(ModAttributes.RF_STUN.get(), 10)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.hammerPlatinum.get(), new ItemStat.Builder(0, 2000, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 145)
                .addAttribute(ModAttributes.RF_CRIT.get(), -7)
                .addAttribute(ModAttributes.RF_STUN.get(), 10)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));

        this.addStat(ModItems.fishingRodScrap.get(), new ItemStat.Builder(150, 35, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 1)
                .addAttribute(ModAttributes.MAGIC.get(), 5)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.fishingRodIron.get(), new ItemStat.Builder(4500, 135, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 9)
                .addAttribute(ModAttributes.MAGIC.get(), 14)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.fishingRodSilver.get(), new ItemStat.Builder(25000, 300, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 26)
                .addAttribute(ModAttributes.MAGIC.get(), 27)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.fishingRodGold.get(), new ItemStat.Builder(0, 550, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 66)
                .addAttribute(ModAttributes.MAGIC.get(), 72)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.fishingRodPlatinum.get(), new ItemStat.Builder(0, 2000, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 89)
                .addAttribute(ModAttributes.MAGIC.get(), 98)
                .addAttribute(ModAttributes.ATTACK_SPEED.get(), attackSpeedFor(toolsBaseSpeed()))
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));

        this.addStat(ModItems.mobStaff.get(), new ItemStat.Builder(1500, 300, 0));
        this.addStat(ModItems.brush.get(), new ItemStat.Builder(1500, 300, 0));
        this.addStat(ModItems.glass.get(), new ItemStat.Builder(2000, 400, 1));

        this.addStat(ModItems.leveliser.get(), new ItemStat.Builder(2000000, 3000, 0));
        this.addStat(ModItems.heartDrink.get(), new ItemStat.Builder(1500000, 1500, 0));
        this.addStat(ModItems.vitalGummi.get(), new ItemStat.Builder(1500000, 1500, 0));
        this.addStat(ModItems.intelligencer.get(), new ItemStat.Builder(1500000, 1500, 0));
        this.addStat(ModItems.protein.get(), new ItemStat.Builder(1500000, 1500, 0));
        this.addStat(ModItems.formularA.get(), new ItemStat.Builder(1000, 150, 0));
        this.addStat(ModItems.formularB.get(), new ItemStat.Builder(2500, 250, 0));
        this.addStat(ModItems.formularC.get(), new ItemStat.Builder(5000, 400, 0));
        this.addStat(ModItems.minimizer.get(), new ItemStat.Builder(10000, 300, 0));
        this.addStat(ModItems.giantizer.get(), new ItemStat.Builder(10000, 300, 0));
        this.addStat(ModItems.greenifier.get(), new ItemStat.Builder(2000, 200, 0));
        this.addStat(ModItems.greenifierPlus.get(), new ItemStat.Builder(10000, 500, 0));
        this.addStat(ModItems.wettablePowder.get(), new ItemStat.Builder(1500, 150, 0));

        this.addStat(ModItems.cheapBracelet.get(), new ItemStat.Builder(120, 21, 0)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 1.5)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.5));
        this.addStat(ModItems.bronzeBracelet.get(), new ItemStat.Builder(850, 38, 0)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 4)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.5));
        this.addStat(ModItems.silverBracelet.get(), new ItemStat.Builder(3000, 300, 0)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 10)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.5));
        this.addStat(ModItems.goldBracelet.get(), new ItemStat.Builder(15000, 750, 0)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 25)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.5));
        this.addStat(ModItems.platinumBracelet.get(), new ItemStat.Builder(50000, 1000, 0)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 50)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.5));
        this.addStat(ModItems.silverRing.get(), new ItemStat.Builder(20000, 600, 0)
                .addAttribute(ModAttributes.RF_RES_LIGHT.get(), 50)
                .addAttribute(ModAttributes.RF_RES_DARK.get(), 50)
                .addMonsterStat(ModAttributes.RF_RES_LIGHT.get(), 1)
                .addMonsterStat(ModAttributes.RF_RES_DARK.get(), 1));
        this.addStat(ModItems.goldRing.get(), new ItemStat.Builder(0, 5000, 0)
                .addAttribute(ModAttributes.RF_RES_WIND.get(), 15)
                .addAttribute(ModAttributes.RF_RES_WATER.get(), 15)
                .addAttribute(ModAttributes.RF_RES_EARTH.get(), 15)
                .addAttribute(ModAttributes.RF_RES_FIRE.get(), 15)
                .addMonsterStat(ModAttributes.RF_RES_WIND.get(), 1)
                .addMonsterStat(ModAttributes.RF_RES_WATER.get(), 1)
                .addMonsterStat(ModAttributes.RF_RES_EARTH.get(), 1)
                .addMonsterStat(ModAttributes.RF_RES_FIRE.get(), 1));
        this.addStat(ModItems.platinumRing.get(), new ItemStat.Builder(0, 7500, 0)
                .addAttribute(ModAttributes.RF_RES_WIND.get(), 10)
                .addAttribute(ModAttributes.RF_RES_WATER.get(), 10)
                .addAttribute(ModAttributes.RF_RES_EARTH.get(), 10)
                .addAttribute(ModAttributes.RF_RES_FIRE.get(), 10)
                .addAttribute(ModAttributes.RF_RES_LIGHT.get(), 25)
                .addAttribute(ModAttributes.RF_RES_DARK.get(), 25)
                .addMonsterStat(ModAttributes.RF_RES_WIND.get(), 1)
                .addMonsterStat(ModAttributes.RF_RES_WATER.get(), 1)
                .addMonsterStat(ModAttributes.RF_RES_EARTH.get(), 1)
                .addMonsterStat(ModAttributes.RF_RES_FIRE.get(), 1));

        this.addStat(ModItems.shirt.get(), new ItemStat.Builder(120, 13, 0)
                .addAttribute(ModAttributes.DEFENCE.get(), 2)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat(ModItems.vest.get(), new ItemStat.Builder(1000, 30, 0)
                .addAttribute(ModAttributes.DEFENCE.get(), 7)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 2)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat(ModItems.cottonCloth.get(), new ItemStat.Builder(4000, 90, 0)
                .addAttribute(ModAttributes.DEFENCE.get(), 12)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 4)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));

        this.addStat(ModItems.headband.get(), new ItemStat.Builder(50, 5, 0)
                .addAttribute(ModAttributes.DEFENCE.get(), 1)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat(ModItems.blueRibbon.get(), new ItemStat.Builder(500, 35, 0)
                .addAttribute(ModAttributes.DEFENCE.get(), 3)
                .addAttribute(ModAttributes.RF_RES_SEAL.get(), 5)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat(ModItems.greenRibbon.get(), new ItemStat.Builder(500, 35, 0)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 3)
                .addAttribute(ModAttributes.RF_RES_SEAL.get(), 5)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat(ModItems.purpleRibbon.get(), new ItemStat.Builder(500, 35, 0)
                .addAttribute(ModAttributes.DEFENCE.get(), 2)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 1)
                .addAttribute(ModAttributes.RF_RES_SEAL.get(), 5)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));

        this.addStat(ModItems.leatherBoots.get(), new ItemStat.Builder(75, 10, 0)
                .addAttribute(ModAttributes.DEFENCE.get(), 2)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat(ModItems.freeFarmingShoes.get(), new ItemStat.Builder(450, 40, 0)
                .addAttribute(ModAttributes.DEFENCE.get(), 3)
                .addAttribute(ModAttributes.RF_RES_FAT.get(), 3)
                .addAttribute(ModAttributes.RF_RES_COLD.get(), 3)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat(ModItems.piyoSandals.get(), new ItemStat.Builder(400, 35, 0)
                .addAttribute(ModAttributes.DEFENCE.get(), 5)
                .addAttribute(ModAttributes.RF_RES_LOVE.get(), 1)
                .addAttribute(ModAttributes.RF_RES_DIZ.get(), 50)
                .addAttribute(ModAttributes.RF_DIZ.get(), 20)
                .addMonsterStat(ModAttributes.RF_RES_DIZ.get(), 0.7));

        this.addStat(Items.SHIELD, new ItemStat.Builder(200, 14, 0)
                .addAttribute(ModAttributes.DEFENCE.get(), 2)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat(ModItems.smallShield.get(), new ItemStat.Builder(600, 23, 0)
                .addAttribute(ModAttributes.DEFENCE.get(), 4)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat(ModItems.ironShield.get(), new ItemStat.Builder(1000, 50, 0)
                .addAttribute(ModAttributes.DEFENCE.get(), 7)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));

        this.addStat(ModItems.scrap.get(), new ItemStat.Builder(13, 1, 1)
                .addAttribute(ModAttributes.DEFENCE.get(), -2)
                .addMonsterStat(ModAttributes.DEFENCE.get(), -1));
        this.addStat(ModItems.scrapPlus.get(), new ItemStat.Builder(0, 2, 1)
                .addAttribute(ModAttributes.DEFENCE.get(), 2)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.2));
        this.addStat("iron", ModTags.IRON, new ItemStat.Builder(150, 2, 5)
                .addAttribute(ModAttributes.DEFENCE.get(), 1)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat("bronze", ModTags.BRONZE, new ItemStat.Builder(400, 14, 13)
                .addAttribute(ModAttributes.DEFENCE.get(), 2.4)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat("copper", ModTags.COPPER, new ItemStat.Builder(200, 9, 10)
                .addAttribute(ModAttributes.DEFENCE.get(), 1.6)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat("silver", ModTags.SILVER, new ItemStat.Builder(1500, 27, 15)
                .addAttribute(ModAttributes.DEFENCE.get(), 5)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.6));
        this.addStat("gold", ModTags.GOLD, new ItemStat.Builder(3500, 34, 20)
                .addAttribute(ModAttributes.DEFENCE.get(), 7)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 5)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.7));
        this.addStat("platinum", ModTags.PLATINUM, new ItemStat.Builder(5000, 111, 34)
                .addAttribute(ModAttributes.DEFENCE.get(), 30)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.8));
        this.addStat("orichalcum", ModTags.ORICHALCUM, new ItemStat.Builder(20000, 750, 65)
                .addAttribute(ModAttributes.DEFENCE.get(), 70)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 1));
        this.addStat("dragonic", ModTags.DRAGONIC, new ItemStat.Builder(0, 1000, 70)
                .addAttribute(ModAttributes.DEFENCE.get(), 130)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 90)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 1.5));
        this.addStat(Items.NETHERITE_INGOT, new ItemStat.Builder(0, 200, 40)
                .addAttribute(ModAttributes.DEFENCE.get(), 20)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 20)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.9));

        this.addStat("emerald", ModTags.EMERALDS, new ItemStat.Builder(2500, 5, 24)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 5)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.5));
        this.addStat(Items.DIAMOND, new ItemStat.Builder(5000, 21, 29)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 15)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 1));
        this.addStat("amethyst", ModTags.AMETHYSTS, new ItemStat.Builder(3500, 18, 24)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 7)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.75));
        this.addStat("aquamarine", ModTags.AQUAMARINES, new ItemStat.Builder(3500, 23, 24)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 7)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.5));
        this.addStat("ruby", ModTags.RUBIES, new ItemStat.Builder(4000, 37, 24)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 7)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.5));
        this.addStat("sapphire", ModTags.SAPPHIRES, new ItemStat.Builder(3500, 24, 24)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 7)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 7));
        this.addStat(ModItems.coreGreen.get(), new ItemStat.Builder(15000, 1050, 24)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 50)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 2));
        this.addStat(ModItems.coreRed.get(), new ItemStat.Builder(15000, 1050, 24)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 50)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 2));
        this.addStat(ModItems.coreBlue.get(), new ItemStat.Builder(15000, 1050, 24)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 50)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 2));
        this.addStat(ModItems.coreYellow.get(), new ItemStat.Builder(15000, 1050, 24)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 50)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 2));
        this.addStat(ModItems.crystalSkull.get(), new ItemStat.Builder(25000, 2300, 24)
                .addAttribute(ModAttributes.MAGIC.get(), 40)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 70)
                .addMonsterStat(ModAttributes.MAGIC.get(), 2)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 4));

        this.addStat(ModItems.crystalWater.get(), new ItemStat.Builder(2000, 150, 20)
                .addAttribute(ModAttributes.MAGIC.get(), 5)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.5)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.5)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.crystalEarth.get(), new ItemStat.Builder(2000, 150, 20)
                .addAttribute(ModAttributes.DEFENCE.get(), 5)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5)
                .setElement(EnumElement.EARTH));
        this.addStat(ModItems.crystalFire.get(), new ItemStat.Builder(2000, 150, 20)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5)
                .setElement(EnumElement.FIRE));
        this.addStat(ModItems.crystalWind.get(), new ItemStat.Builder(2000, 150, 20)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 5)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.5)
                .setElement(EnumElement.WIND));
        this.addStat(ModItems.crystalLight.get(), new ItemStat.Builder(2000, 150, 20)
                .addAttribute(ModAttributes.DEFENCE.get(), 1.5)
                .addAttribute(ModAttributes.MAGIC.get(), 3)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 1.5)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.5)
                .setElement(EnumElement.LIGHT));
        this.addStat(ModItems.crystalDark.get(), new ItemStat.Builder(2000, 150, 20)
                .addAttribute(ModAttributes.DEFENCE.get(), 1.5)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 1.5)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5)
                .setElement(EnumElement.DARK));
        this.addStat(ModItems.crystalLove.get(), new ItemStat.Builder(2000, 150, 20)
                .addAttribute(ModAttributes.RF_DRAIN.get(), 3)
                .addMonsterStat(ModAttributes.RF_DRAIN.get(), 0.5)
                .setElement(EnumElement.LOVE));
        this.addStat(ModItems.crystalSmall.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.crystalBig.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.crystalMagic.get(), new ItemStat.Builder(45, 400, 25)
                .addAttribute(ModAttributes.MAGIC.get(), 7)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 1));
        this.addStat(ModItems.crystalRune.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.crystalElectro.get(), new ItemStat.Builder(0, 0, 0));

        this.addStat(ModItems.stickThick.get(), new ItemStat.Builder(1900, 200, 45)
                .addAttribute(Attributes.ATTACK_DAMAGE, 15));
        this.addStat(ModItems.hornInsect.get(), new ItemStat.Builder(130, 21, 8)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.hornRigid.get(), new ItemStat.Builder(0, 0, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 10)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.plantStem.get(), new ItemStat.Builder(0, 0, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addAttribute(ModAttributes.MAGIC.get(), 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.4));
        this.addStat(ModItems.hornBull.get(), new ItemStat.Builder(0, 0, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 8)
                .addAttribute(ModAttributes.DEFENCE.get(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.7));
        this.addStat(ModItems.hornDevil.get(), new ItemStat.Builder(0, 0, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 30)
                .addAttribute(ModAttributes.MAGIC.get(), 30)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .addMonsterStat(ModAttributes.MAGIC.get(), 1));
        this.addStat(ModItems.movingBranch.get(), new ItemStat.Builder(0, 0, 0));

        this.addStat(ModItems.glue.get(), new ItemStat.Builder(380, 41, 13)
                .addAttribute(Attributes.ATTACK_DAMAGE, 1)
                .addAttribute(ModAttributes.DEFENCE.get(), 1)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.3)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.3));
        this.addStat(ModItems.devilBlood.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.paraPoison.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.poisonKing.get(), new ItemStat.Builder(0, 0, 0));

        this.addStat(ModItems.featherBlack.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.featherThunder.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.featherYellow.get(), new ItemStat.Builder(500, 20, 15)
                .addAttribute(ModAttributes.MAGIC.get(), 13)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.7));
        this.addStat(ModItems.dragonFin.get(), new ItemStat.Builder(0, 0, 0));

        this.addStat(ModItems.turtleShell.get(), new ItemStat.Builder(160, 30, 16)
                .addAttribute(ModAttributes.DEFENCE.get(), 5)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 3)
                .addAttribute(ModAttributes.RF_CRIT.get(), -3)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 1));
        this.addStat(ModItems.fishFossil.get(), new ItemStat.Builder(180, 30, 19)
                .addAttribute(ModAttributes.MAGIC.get(), 5)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 1)
                .addMonsterStat(ModAttributes.MAGIC.get(), 1));
        this.addStat(ModItems.skull.get(), new ItemStat.Builder(100, 1000, 35)
                .addAttribute(ModAttributes.MAGIC.get(), 9)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 4)
                .addAttribute(ModAttributes.RF_COLD.get(), 5)
                .addMonsterStat(ModAttributes.MAGIC.get(), 1)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.3));
        this.addStat(ModItems.dragonBones.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.tortoiseShell.get(), new ItemStat.Builder(0, 0, 0));

        this.addStat(ModItems.rock.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.stoneRound.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.stoneTiny.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.stoneGolem.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.tabletGolem.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.stoneSpirit.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.tabletTruth.get(), new ItemStat.Builder(0, 0, 0));

        this.addStat(ModItems.yarn.get(), new ItemStat.Builder(400, 75, 0)
                .addAttribute(ModAttributes.DEFENCE.get(), 3)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 1)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.3));
        this.addStat(ModItems.oldBandage.get(), new ItemStat.Builder(0, 0, 0)
                .addAttribute(ModAttributes.MAGIC.get(), 2)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 1)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.3));
        this.addStat(ModItems.ambrosiasThorns.get(), new ItemStat.Builder(7500, 350, 21)
                .addAttribute(Attributes.ATTACK_DAMAGE, 7)
                .addAttribute(ModAttributes.MAGIC.get(), 7)
                .addAttribute(ModAttributes.RF_SLEEP.get(), 10)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .addMonsterStat(ModAttributes.MAGIC.get(), 1)
                .addMonsterStat(ModAttributes.RF_SLEEP.get(), 0.4)
                .setSpell(ModSpells.POLLENPUFF.get(), ModSpells.WAVE.get(), ModSpells.BUTTERFLY.get()));
        this.addStat(ModItems.threadSpider.get(), new ItemStat.Builder(370, 28, 17)
                .addAttribute(Attributes.ATTACK_DAMAGE, 2)
                .addAttribute(ModAttributes.MAGIC.get(), 2)
                .addAttribute(ModAttributes.RF_PARA.get(), 5)
                .addMonsterStat(ModAttributes.RF_PARA.get(), 0.3));
        this.addStat(ModItems.puppetryStrings.get(), new ItemStat.Builder(30000, 1000, 37)
                .addAttribute(Attributes.ATTACK_DAMAGE, 6)
                .addAttribute(ModAttributes.MAGIC.get(), 9)
                .addAttribute(ModAttributes.RF_SEAL.get(), 15)
                .addAttribute(ModAttributes.RF_PARA.get(), 5)
                .addMonsterStat(ModAttributes.RF_SEAL.get(), 0.5)
                .addMonsterStat(ModAttributes.RF_PARA.get(), 0.3)
                .setSpell(ModSpells.DARKBEAM.get(), ModSpells.PLATE.get(), ModSpells.DARKBULLETS.get()));
        this.addStat(ModItems.vine.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.tailScorpion.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.strongVine.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.threadPretty.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.tailChimera.get(), new ItemStat.Builder(0, 0, 0));

        this.addStat(ModItems.arrowHead.get(), new ItemStat.Builder(80, 10, 2)
                .addAttribute(Attributes.ATTACK_DAMAGE, 2)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.2));
        this.addStat(ModItems.bladeShard.get(), new ItemStat.Builder(139, 25, 9)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.3));
        this.addStat(ModItems.brokenHilt.get(), new ItemStat.Builder(550, 50, 22)
                .addAttribute(ModAttributes.DEFENCE.get(), 2)
                .addAttribute(ModAttributes.RF_RES_POISON.get(), 5)
                .addAttribute(ModAttributes.RF_RES_SEAL.get(), 5)
                .addAttribute(ModAttributes.RF_RES_PARA.get(), 5)
                .addAttribute(ModAttributes.RF_RES_SLEEP.get(), 5)
                .addAttribute(ModAttributes.RF_RES_FAT.get(), 5)
                .addAttribute(ModAttributes.RF_RES_COLD.get(), 5)
                .addMonsterStat(ModAttributes.RF_RES_POISON.get(), 0.2)
                .addMonsterStat(ModAttributes.RF_RES_SEAL.get(), 0.2)
                .addMonsterStat(ModAttributes.RF_RES_PARA.get(), 0.2)
                .addMonsterStat(ModAttributes.RF_RES_SLEEP.get(), 0.2)
                .addMonsterStat(ModAttributes.RF_RES_FAT.get(), 0.2)
                .addMonsterStat(ModAttributes.RF_RES_COLD.get(), 0.2));
        this.addStat(ModItems.brokenBox.get(), new ItemStat.Builder(1000, 200, 48)
                .addAttribute(ModAttributes.DEFENCE.get(), 4)
                .addAttribute(ModAttributes.RF_RES_POISON.get(), 10)
                .addAttribute(ModAttributes.RF_RES_SEAL.get(), 10)
                .addAttribute(ModAttributes.RF_RES_PARA.get(), 10)
                .addAttribute(ModAttributes.RF_RES_SLEEP.get(), 10)
                .addAttribute(ModAttributes.RF_RES_FAT.get(), 10)
                .addAttribute(ModAttributes.RF_RES_COLD.get(), 10)
                .addMonsterStat(ModAttributes.RF_RES_POISON.get(), 0.5)
                .addMonsterStat(ModAttributes.RF_RES_SEAL.get(), 0.5)
                .addMonsterStat(ModAttributes.RF_RES_PARA.get(), 0.5)
                .addMonsterStat(ModAttributes.RF_RES_SLEEP.get(), 0.5)
                .addMonsterStat(ModAttributes.RF_RES_FAT.get(), 0.5)
                .addMonsterStat(ModAttributes.RF_RES_COLD.get(), 0.5));
        this.addStat(ModItems.bladeGlistening.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.greatHammerShard.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.hammerPiece.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.shoulderPiece.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.piratesArmor.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.screwRusty.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.screwShiny.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.rockShardLeft.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.rockShardRight.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.MTGUPlate.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.brokenIceWall.get(), new ItemStat.Builder(0, 0, 0));

        this.addStat(ModItems.furSmall.get(), new ItemStat.Builder(35, 7, 1)
                .addAttribute(ModAttributes.DEFENCE.get(), 1)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 1)
                .addAttribute(ModAttributes.RF_RES_DIZ.get(), 1)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.3));
        this.addStat(ModItems.furMedium.get(), new ItemStat.Builder(1000, 100, 29)
                .addAttribute(ModAttributes.DEFENCE.get(), 5)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 3)
                .addAttribute(ModAttributes.RF_RES_DIZ.get(), 10)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat(ModItems.furLarge.get(), new ItemStat.Builder(3000, 500, 55)
                .addAttribute(ModAttributes.DEFENCE.get(), 10)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 5)
                .addAttribute(ModAttributes.RF_RES_DIZ.get(), 25)
                .addAttribute(ModAttributes.RF_RES_SLEEP.get(), 5)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.7));
        this.addStat(ModItems.fur.get(), new ItemStat.Builder(130, 23, 7)
                .addAttribute(ModAttributes.DEFENCE.get(), 2)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 1)
                .addAttribute(ModAttributes.RF_RES_DIZ.get(), 3)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.3));
        this.addStat(ModItems.furball.get(), new ItemStat.Builder(900, 120, 38)
                .addAttribute(ModAttributes.DEFENCE.get(), 8)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 4)
                .addAttribute(ModAttributes.RF_RES_DIZ.get(), 10)
                .addAttribute(ModAttributes.RF_RES_SLEEP.get(), 3)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat(ModItems.downYellow.get(), new ItemStat.Builder(300, 33, 21)
                .addAttribute(ModAttributes.DEFENCE.get(), 3)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 2)
                .addAttribute(ModAttributes.RF_RES_DIZ.get(), 5)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat(ModItems.furQuality.get(), new ItemStat.Builder(650, 45, 36)
                .addAttribute(ModAttributes.DEFENCE.get(), 4)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 3)
                .addAttribute(ModAttributes.RF_RES_DIZ.get(), 7)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat(ModItems.downPenguin.get(), new ItemStat.Builder(1250, 129, 59)
                .addAttribute(ModAttributes.DEFENCE.get(), 13)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 7)
                .addAttribute(ModAttributes.RF_RES_DIZ.get(), 13)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.6));
        this.addStat(ModItems.lightningMane.get(), new ItemStat.Builder(13000, 600, 31)
                .addAttribute(ModAttributes.DEFENCE.get(), 8)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 6)
                .addAttribute(ModAttributes.RF_RES_DIZ.get(), 17)
                .addMonsterStat(ModAttributes.RF_RES_DIZ.get(), 1)
                .setSpell(ModSpells.LASER3.get(), ModSpells.LASER5.get(), ModSpells.BIGLIGHTNING.get()));
        this.addStat(ModItems.furRedLion.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.furBlueLion.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.chestHair.get(), new ItemStat.Builder(0, 0, 0));

        this.addStat(ModItems.spore.get(), new ItemStat.Builder(110, 19, 9)
                .addAttribute(ModAttributes.MAGIC.get(), 2)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.3));
        this.addStat(ModItems.powderPoison.get(), new ItemStat.Builder(550, 80, 21)
                .addAttribute(ModAttributes.MAGIC.get(), 2)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 2)
                .addAttribute(ModAttributes.RF_POISON.get(), 5)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.3)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.3)
                .addMonsterStat(ModAttributes.RF_POISON.get(), 0.5));
        this.addStat(ModItems.sporeHoly.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.fairyDust.get(), new ItemStat.Builder(300, 40, 19)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 5)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.33));
        this.addStat(ModItems.fairyElixir.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.root.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.powderMagic.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.powderMysterious.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.magic.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.ashEarth.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.ashFire.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.ashWater.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.turnipsMiracle.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.melodyBottle.get(), new ItemStat.Builder(0, 0, 0));

        this.addStat(ModItems.clothCheap.get(), new ItemStat.Builder(80, 12, 4)
                .addAttribute(ModAttributes.DEFENCE.get(), 1)
                .addAttribute(ModAttributes.RF_RES_CRIT.get(), 1)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.3));
        this.addStat(ModItems.clothQuality.get(), new ItemStat.Builder(800, 100, 18)
                .addAttribute(ModAttributes.DEFENCE.get(), 5)
                .addAttribute(ModAttributes.RF_RES_CRIT.get(), 5)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.3));
        this.addStat(ModItems.clothQualityWorn.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.clothSilk.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.ghostHood.get(), new ItemStat.Builder(70, 650, 21)
                .addAttribute(ModAttributes.DEFENCE.get(), 2)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 2)
                .addAttribute(ModAttributes.RF_RES_SEAL.get(), 25)
                .addMonsterStat(ModAttributes.RF_RES_SEAL.get(), 0.5));
        this.addStat(ModItems.gloveGiant.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.gloveBlueGiant.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.carapaceInsect.get(), new ItemStat.Builder(75, 11, 8)
                .addAttribute(ModAttributes.DEFENCE.get(), 2)
                .addAttribute(ModAttributes.RF_RES_POISON.get(), 15)
                .addMonsterStat(ModAttributes.RF_RES_POISON.get(), 0.5));
        this.addStat(ModItems.carapacePretty.get(), new ItemStat.Builder(750, 85, 24)
                .addAttribute(ModAttributes.DEFENCE.get(), 4)
                .addAttribute(ModAttributes.RF_RES_SEAL.get(), 5)
                .addAttribute(ModAttributes.RF_RES_PARA.get(), 20)
                .addMonsterStat(ModAttributes.RF_RES_PARA.get(), 0.5));
        this.addStat(ModItems.clothAncientOrc.get(), new ItemStat.Builder(0, 0, 0));

        this.addStat(ModItems.jawInsect.get(), new ItemStat.Builder(100, 23, 14)
                .addAttribute(Attributes.ATTACK_DAMAGE, 2)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.3));
        this.addStat(ModItems.clawPanther.get(), new ItemStat.Builder(450, 55, 28)
                .addAttribute(Attributes.ATTACK_DAMAGE, 4)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.clawMagic.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.fangWolf.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.fangGoldWolf.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.clawPalm.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.clawMalm.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.giantsNail.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.clawChimera.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.tuskIvory.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.tuskUnbrokenIvory.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.scorpionPincer.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.dangerousScissors.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.propellorCheap.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.propellorQuality.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.fangDragon.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.jawQueen.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.windDragonTooth.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.giantsNailBig.get(), new ItemStat.Builder(0, 0, 0));

        this.addStat(ModItems.scaleWet.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.scaleGrimoire.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.scaleDragon.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.scaleCrimson.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.scaleBlue.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.scaleGlitter.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.scaleLove.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.scaleBlack.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.scaleFire.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.scaleEarth.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.scaleLegend.get(), new ItemStat.Builder(0, 0, 0));

        this.addStat(ModItems.steelDouble.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.steelTen.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.glittaAugite.get(), new ItemStat.Builder(0, 0, 0)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), 1));
        this.addStat(ModItems.invisStone.get(), new ItemStat.Builder(0, 750, 24));
        this.addStat(ModItems.lightOre.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.runeSphereShard.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.shadeStone.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.racoonLeaf.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.icyNose.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.bigBirdsComb.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.rafflesiaPetal.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.cursedDoll.get(), new ItemStat.Builder(750, 27000, 39)
                .addAttribute(ModAttributes.DEFENCE.get(), 4)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 7)
                .addAttribute(ModAttributes.RF_RES_SEAL.get(), 10)
                .addAttribute(ModAttributes.RF_RES_PARA.get(), 10)
                .addAttribute(ModAttributes.RF_RES_DRAIN.get(), 15)
                .addMonsterStat(ModAttributes.RF_RES_SEAL.get(), 1)
                .addMonsterStat(ModAttributes.RF_RES_PARA.get(), 1)
                .addMonsterStat(ModAttributes.RF_RES_DRAIN.get(), 1)
                .setSpell(ModSpells.CARDTHROW.get(), ModSpells.PLUSHTHROW.get(), ModSpells.FURNITURE.get()));
        this.addStat(ModItems.warriorsProof.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.proofOfRank.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.throneOfEmpire.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.whiteStone.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.rareCan.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.can.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.boots.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(ModItems.lawn.get(), new ItemStat.Builder(0, 0, 0));

        this.addStat(ModItems.fireBallSmall.get(), new ItemStat.Builder(2400, 120, 0));
        this.addStat(ModItems.fireBallBig.get(), new ItemStat.Builder(4800, 240, 0));
        this.addStat(ModItems.explosion.get(), new ItemStat.Builder(9600, 480, 0));
        this.addStat(ModItems.waterLaser.get(), new ItemStat.Builder(2400, 120, 0));
        this.addStat(ModItems.parallelLaser.get(), new ItemStat.Builder(4800, 240, 0));
        this.addStat(ModItems.deltaLaser.get(), new ItemStat.Builder(9600, 480, 0));
        this.addStat(ModItems.screwRock.get(), new ItemStat.Builder(2400, 120, 0));
        this.addStat(ModItems.earthSpike.get(), new ItemStat.Builder(0, 240, 0));
        this.addStat(ModItems.avengerRock.get(), new ItemStat.Builder(9600, 480, 0));
        this.addStat(ModItems.sonicWind.get(), new ItemStat.Builder(2400, 120, 0));
        this.addStat(ModItems.doubleSonic.get(), new ItemStat.Builder(4800, 240, 0));
        this.addStat(ModItems.penetrateSonic.get(), new ItemStat.Builder(9600, 480, 0));
        this.addStat(ModItems.lightBarrier.get(), new ItemStat.Builder(2700, 135, 0));
        this.addStat(ModItems.shine.get(), new ItemStat.Builder(5400, 270, 0));
        this.addStat(ModItems.prism.get(), new ItemStat.Builder(10800, 540, 0));
        this.addStat(ModItems.darkSnake.get(), new ItemStat.Builder(2700, 135, 0));
        this.addStat(ModItems.darkBall.get(), new ItemStat.Builder(5400, 270, 0));
        this.addStat(ModItems.darkness.get(), new ItemStat.Builder(10800, 540, 0));
        this.addStat(ModItems.cure.get(), new ItemStat.Builder(1500, 75, 0));
        this.addStat(ModItems.cureAll.get(), new ItemStat.Builder(4500, 225, 0));
        this.addStat(ModItems.cureMaster.get(), new ItemStat.Builder(12000, 600, 0));
        this.addStat(ModItems.mediPoison.get(), new ItemStat.Builder(1500, 75, 0));
        this.addStat(ModItems.mediPara.get(), new ItemStat.Builder(3000, 150, 0));
        this.addStat(ModItems.mediSeal.get(), new ItemStat.Builder(5000, 250, 0));

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
        this.addStat(ModItems.antidoteGrass.get(), new ItemStat.Builder(120, 5, 1)
                .addAttribute(ModAttributes.RF_RES_POISON.get(), 5));
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

        this.addStat(ModItems.turnipSeeds.get(), new ItemStat.Builder(100, 10, 0));
        this.addStat(ModItems.turnipPinkSeeds.get(), new ItemStat.Builder(140, 14, 0));
        this.addStat(ModItems.cabbageSeeds.get(), new ItemStat.Builder(500, 50, 0));
        this.addStat(ModItems.pinkMelonSeeds.get(), new ItemStat.Builder(1000, 100, 0));
        this.addStat(ModItems.hotHotSeeds.get(), new ItemStat.Builder(750, 75, 0));
        this.addStat(ModItems.goldTurnipSeeds.get(), new ItemStat.Builder(5000, 500, 0));
        this.addStat(ModItems.goldPotatoSeeds.get(), new ItemStat.Builder(3000, 300, 0));
        this.addStat(ModItems.goldPumpkinSeeds.get(), new ItemStat.Builder(3500, 3500, 0));
        this.addStat(ModItems.goldCabbageSeeds.get(), new ItemStat.Builder(2500, 250, 0));
        this.addStat(ModItems.bokChoySeeds.get(), new ItemStat.Builder(600, 60, 0));
        this.addStat(ModItems.leekSeeds.get(), new ItemStat.Builder(450, 45, 0));
        this.addStat(ModItems.radishSeeds.get(), new ItemStat.Builder(660, 66, 0));
        this.addStat(ModItems.greenPepperSeeds.get(), new ItemStat.Builder(400, 40, 0));
        this.addStat(ModItems.spinachSeeds.get(), new ItemStat.Builder(120, 12, 0));
        this.addStat(ModItems.yamSeeds.get(), new ItemStat.Builder(250, 25, 0));
        this.addStat(ModItems.eggplantSeeds.get(), new ItemStat.Builder(700, 70, 0));
        this.addStat(ModItems.pineappleSeeds.get(), new ItemStat.Builder(1300, 130, 0));
        this.addStat(ModItems.pumpkinSeeds.get(), new ItemStat.Builder(800, 80, 0));
        this.addStat(ModItems.onionSeeds.get(), new ItemStat.Builder(900, 90, 0));
        this.addStat(ModItems.cornSeeds.get(), new ItemStat.Builder(830, 83, 0));
        this.addStat(ModItems.tomatoSeeds.get(), new ItemStat.Builder(990, 99, 0));
        this.addStat(ModItems.strawberrySeeds.get(), new ItemStat.Builder(330, 33, 0));
        this.addStat(ModItems.cucumberSeeds.get(), new ItemStat.Builder(230, 23, 0));
        this.addStat(ModItems.fodderSeeds.get(), new ItemStat.Builder(50, 5, 0));

        this.addStat(ModItems.fodder.get(), new ItemStat.Builder(250, 35, 0));

        this.addStat(ModItems.turnip.get(), new ItemStat.Builder(230, 60, 1));
        this.addStat(ModItems.turnipGiant.get(), new ItemStat.Builder(0, 250, 20));
        this.addStat(ModItems.turnipPink.get(), new ItemStat.Builder(320, 130, 5));
        this.addStat(ModItems.turnipPinkGiant.get(), new ItemStat.Builder(0, 460, 23));
        this.addStat(ModItems.cabbage.get(), new ItemStat.Builder(1200, 330, 14));
        this.addStat(ModItems.cabbageGiant.get(), new ItemStat.Builder(0, 780, 31));
        this.addStat(ModItems.pinkMelon.get(), new ItemStat.Builder(3300, 660, 19));
        this.addStat(ModItems.pinkMelonGiant.get(), new ItemStat.Builder(0, 1850, 29));
        this.addStat(ModItems.pineapple.get(), new ItemStat.Builder(7500, 2360, 53));
        this.addStat(ModItems.pineappleGiant.get(), new ItemStat.Builder(0, 6500, 76));
        this.addStat(ModItems.strawberry.get(), new ItemStat.Builder(1300, 370, 30));
        this.addStat(ModItems.strawberryGiant.get(), new ItemStat.Builder(0, 900, 45));
        this.addStat(ModItems.goldenTurnip.get(), new ItemStat.Builder(50000, 15000, 88));
        this.addStat(ModItems.goldenTurnipGiant.get(), new ItemStat.Builder(0, 45000, 95));
        this.addStat(ModItems.goldenPotato.get(), new ItemStat.Builder(30000, 12500, 86));
        this.addStat(ModItems.goldenPotatoGiant.get(), new ItemStat.Builder(0, 30000, 90));
        this.addStat(ModItems.goldenPumpkin.get(), new ItemStat.Builder(25000, 10000, 87));
        this.addStat(ModItems.goldenPumpkinGiant.get(), new ItemStat.Builder(0, 23500, 93));
        this.addStat(ModItems.goldenCabbage.get(), new ItemStat.Builder(18500, 8000, 83));
        this.addStat(ModItems.goldenCabbageGiant.get(), new ItemStat.Builder(0, 20000, 89));
        this.addStat(ModItems.hotHotFruit.get(), new ItemStat.Builder(6000, 1000, 67));
        this.addStat(ModItems.hotHotFruitGiant.get(), new ItemStat.Builder(0, 2500, 79));
        this.addStat(ModItems.bokChoy.get(), new ItemStat.Builder(1300, 440, 34));
        this.addStat(ModItems.bokChoyGiant.get(), new ItemStat.Builder(0, 1100, 50));
        this.addStat(ModItems.leek.get(), new ItemStat.Builder(2300, 800, 23));
        this.addStat(ModItems.leekGiant.get(), new ItemStat.Builder(0, 1950, 37));
        this.addStat(ModItems.radish.get(), new ItemStat.Builder(3500, 1550, 40));
        this.addStat(ModItems.radishGiant.get(), new ItemStat.Builder(0, 4320, 49));
        this.addStat(ModItems.spinach.get(), new ItemStat.Builder(450, 120, 17));
        this.addStat(ModItems.spinachGiant.get(), new ItemStat.Builder(0, 350, 24));
        this.addStat(ModItems.greenPepper.get(), new ItemStat.Builder(500, 210, 19));
        this.addStat(ModItems.greenPepperGiant.get(), new ItemStat.Builder(0, 800, 28));
        this.addStat(ModItems.yam.get(), new ItemStat.Builder(5500, 220, 20));
        this.addStat(ModItems.yamGiant.get(), new ItemStat.Builder(0, 840, 28));
        this.addStat(ModItems.eggplant.get(), new ItemStat.Builder(970, 310, 23));
        this.addStat(ModItems.eggplantGiant.get(), new ItemStat.Builder(0, 850, 38));
        this.addStat(ModItems.tomato.get(), new ItemStat.Builder(800, 350, 25));
        this.addStat(ModItems.tomatoGiant.get(), new ItemStat.Builder(0, 1100, 34));
        this.addStat(ModItems.corn.get(), new ItemStat.Builder(2500, 1000, 37));
        this.addStat(ModItems.cornGiant.get(), new ItemStat.Builder(0, 2800, 56));
        this.addStat(ModItems.cucumber.get(), new ItemStat.Builder(350, 130, 12));
        this.addStat(ModItems.cucumberGiant.get(), new ItemStat.Builder(0, 250, 27));
        //this.addStat(ModItems.pumpkin.get(), new ItemStat.MutableItemStat(0, 0, 0));
        //this.addStat(ModItems.pumpkinGiant.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.onion.get(), new ItemStat.Builder(1350, 450, 23));
        this.addStat(ModItems.onionGiant.get(), new ItemStat.Builder(0, 1150, 45));

        this.addStat(Items.CARROT, new ItemStat.Builder(130, 100, 3));
        this.addStat(Items.POTATO, new ItemStat.Builder(130, 100, 3));
        this.addStat(ModItems.potatoGiant.get(), new ItemStat.Builder(0, 450, 19));
        this.addStat(ModItems.carrotGiant.get(), new ItemStat.Builder(0, 450, 19));

        this.addStat(ModItems.toyherbSeeds.get(), new ItemStat.Builder(80, 8, 0));
        this.addStat(ModItems.moondropSeeds.get(), new ItemStat.Builder(190, 19, 0));
        this.addStat(ModItems.pinkCatSeeds.get(), new ItemStat.Builder(150, 15, 0));
        this.addStat(ModItems.charmBlueSeeds.get(), new ItemStat.Builder(130, 13, 0));
        this.addStat(ModItems.lampGrassSeeds.get(), new ItemStat.Builder(550, 55, 0));
        this.addStat(ModItems.cherryGrassSeeds.get(), new ItemStat.Builder(380, 38, 0));
        this.addStat(ModItems.pomPomGrassSeeds.get(), new ItemStat.Builder(450, 45, 0));
        this.addStat(ModItems.autumnGrassSeeds.get(), new ItemStat.Builder(900, 90, 0));
        this.addStat(ModItems.noelGrassSeeds.get(), new ItemStat.Builder(1700, 170, 0));
        this.addStat(ModItems.fireflowerSeeds.get(), new ItemStat.Builder(2380, 238, 0));
        this.addStat(ModItems.fourLeafCloverSeeds.get(), new ItemStat.Builder(770, 77, 0));
        this.addStat(ModItems.ironleafSeeds.get(), new ItemStat.Builder(660, 66, 0));
        this.addStat(ModItems.whiteCrystalSeeds.get(), new ItemStat.Builder(25000, 1500, 0));
        this.addStat(ModItems.redCrystalSeeds.get(), new ItemStat.Builder(25000, 1500, 0));
        this.addStat(ModItems.greenCrystalSeeds.get(), new ItemStat.Builder(25000, 1500, 0));
        this.addStat(ModItems.blueCrystalSeeds.get(), new ItemStat.Builder(25000, 1500, 0));
        this.addStat(ModItems.emeryFlowerSeeds.get(), new ItemStat.Builder(50000, 2000, 0));

        this.addStat(ModItems.toyherb.get(), new ItemStat.Builder(240, 110, 2));
        this.addStat(ModItems.toyherbGiant.get(), new ItemStat.Builder(0, 300, 12));
        this.addStat(ModItems.moondropFlower.get(), new ItemStat.Builder(380, 160, 5));
        this.addStat(ModItems.moondropFlowerGiant.get(), new ItemStat.Builder(0, 360, 19));
        this.addStat(ModItems.pinkCat.get(), new ItemStat.Builder(450, 190, 6));
        this.addStat(ModItems.pinkCatGiant.get(), new ItemStat.Builder(0, 400, 17));
        this.addStat(ModItems.charmBlue.get(), new ItemStat.Builder(500, 210, 9));
        this.addStat(ModItems.charmBlueGiant.get(), new ItemStat.Builder(0, 450, 21));
        this.addStat(ModItems.lampGrass.get(), new ItemStat.Builder(1450, 660, 16));
        this.addStat(ModItems.lampGrassGiant.get(), new ItemStat.Builder(0, 1300, 34));
        this.addStat(ModItems.cherryGrass.get(), new ItemStat.Builder(750, 330, 14));
        this.addStat(ModItems.cherryGrassGiant.get(), new ItemStat.Builder(0, 800, 31));
        this.addStat(ModItems.pomPomGrass.get(), new ItemStat.Builder(1500, 550, 19));
        this.addStat(ModItems.pomPomGrassGiant.get(), new ItemStat.Builder(0, 1300, 36));
        this.addStat(ModItems.autumnGrass.get(), new ItemStat.Builder(3800, 1250, 20));
        this.addStat(ModItems.autumnGrassGiant.get(), new ItemStat.Builder(0, 2400, 39));
        this.addStat(ModItems.noelGrass.get(), new ItemStat.Builder(4500, 1550, 28));
        this.addStat(ModItems.noelGrassGiant.get(), new ItemStat.Builder(0, 3000, 42));
        this.addStat(ModItems.fireflower.get(), new ItemStat.Builder(4600, 1750, 26));
        this.addStat(ModItems.fireflowerGiant.get(), new ItemStat.Builder(0, 2800, 48));
        this.addStat(ModItems.fourLeafClover.get(), new ItemStat.Builder(3330, 1000, 15));
        this.addStat(ModItems.fourLeafCloverGiant.get(), new ItemStat.Builder(0, 2400, 29));
        this.addStat(ModItems.ironleaf.get(), new ItemStat.Builder(1500, 980, 23));
        this.addStat(ModItems.ironleafGiant.get(), new ItemStat.Builder(0, 1600, 48));
        this.addStat(ModItems.whiteCrystal.get(), new ItemStat.Builder(70000, 23000, 77));
        this.addStat(ModItems.whiteCrystalGiant.get(), new ItemStat.Builder(0, 85000, 80));
        this.addStat(ModItems.redCrystal.get(), new ItemStat.Builder(65000, 20000, 69));
        this.addStat(ModItems.redCrystalGiant.get(), new ItemStat.Builder(0, 68000, 83));
        this.addStat(ModItems.greenCrystal.get(), new ItemStat.Builder(47500, 16600, 72));
        this.addStat(ModItems.greenCrystalGiant.get(), new ItemStat.Builder(0, 59000, 84));
        this.addStat(ModItems.blueCrystal.get(), new ItemStat.Builder(40000, 15000, 74));
        this.addStat(ModItems.blueCrystalGiant.get(), new ItemStat.Builder(0, 50000, 86));
        this.addStat(ModItems.emeryFlower.get(), new ItemStat.Builder(500000, 55000, 80));
        this.addStat(ModItems.emeryFlowerGiant.get(), new ItemStat.Builder(0, 150000, 95));

    }

    private static double attackSpeedFor(double delay) {
        return delay - 5;
    }

    private static double attackRangeFor(double range) {
        return range - 3;
    }

    private static double shortSwordBaseSpeed() {
        return 11;
    }

    private static double longSwordBaseSpeed() {
        return 17;
    }

    private static double spearBaseSpeed() {
        return 13;
    }

    private static double axeHammerBaseSpeed() {
        return 15;
    }

    private static double dualBladeBaseSpeed() {
        return 7;
    }

    private static double gloveBaseSpeed() {
        return 7;
    }

    private static double staffBaseSpeed() {
        return 15;
    }

    private static double toolsBaseSpeed() {
        return 20;
    }
}
