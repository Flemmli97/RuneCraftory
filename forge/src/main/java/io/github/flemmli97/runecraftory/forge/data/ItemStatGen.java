package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.api.datapack.provider.ItemStatProvider;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.ModArmorEffects;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
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
                .addAttribute(ModAttributes.PARA.get(), 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.1)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.1)
                .addAttribute(ModAttributes.PARA.get(), 0.25));
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
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setSpell(ModSpells.BLAZE_FIREBALLS.get(), null, null));
        this.addStat(Items.ENDER_PEARL, new ItemStat.Builder(400, 28, 21)
                .addAttribute(ModAttributes.MAGIC.get(), 3)
                .addAttribute(ModAttributes.RES_STUN.get(), 5)
                .addMonsterStat(ModAttributes.RES_STUN.get(), 0.5));
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
                .addMonsterStat(Attributes.MAX_HEALTH, 2)
                .setSpell(ModSpells.GHAST_FIREBALL.get(), null, null));
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
                .setSpell(ModSpells.EVOKER_FANG.get(), null, null));
        this.addStat(Items.DRAGON_BREATH, new ItemStat.Builder(2000, 150, 24)
                .addAttribute(Attributes.ATTACK_DAMAGE, 4)
                .addAttribute(ModAttributes.MAGIC.get(), 15)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.8)
                .setSpell(ModSpells.DRAGON_FIREBALL.get(), null, null));
        this.addStat(Items.SHULKER_SHELL, new ItemStat.Builder(700, 80, 29)
                .addAttribute(ModAttributes.DEFENCE.get(), 17)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 12)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 1)
                .setSpell(ModSpells.SHULKER_BULLET.get(), null, null));
        this.addStat(Items.WITHER_SKELETON_SKULL, new ItemStat.Builder(5000, 170, 29)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5)
                .addAttribute(ModAttributes.MAGIC.get(), 20)
                .addMonsterStat(ModAttributes.MAGIC.get(), 1)
                .setSpell(ModSpells.WITHER_SKULL.get(), null, null)
                .setElement(EnumElement.DARK));
        this.addStat(Items.NETHER_STAR, new ItemStat.Builder(20000, 600, 45)
                .addAttribute(Attributes.ATTACK_DAMAGE, 17)
                .addAttribute(ModAttributes.DEFENCE.get(), 7)
                .addAttribute(ModAttributes.MAGIC.get(), 35)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 9)
                .addAttribute(ModAttributes.DRAIN.get(), 5)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .addMonsterStat(ModAttributes.MAGIC.get(), 3)
                .setSpell(ModSpells.WITHER_SKULL.get(), null, null)
                .setElement(EnumElement.DARK));

        //=======
        this.addStat(ModItems.ROUNDOFF.get(), new ItemStat.Builder(750, 50, 0)
                .addMonsterStat(Attributes.MAX_HEALTH, 5)
                .addMonsterStat(ModAttributes.RES_SEAL.get(), 0.5));
        this.addStat(ModItems.PARA_GONE.get(), new ItemStat.Builder(750, 50, 0)
                .addMonsterStat(Attributes.MAX_HEALTH, 5)
                .addMonsterStat(ModAttributes.RES_PARA.get(), 0.5));
        this.addStat(ModItems.COLD_MED.get(), new ItemStat.Builder(750, 50, 0)
                .addMonsterStat(Attributes.MAX_HEALTH, 5)
                .addMonsterStat(ModAttributes.RES_COLD.get(), 0.5));
        this.addStat(ModItems.ANTIDOTE.get(), new ItemStat.Builder(750, 50, 0)
                .addMonsterStat(Attributes.MAX_HEALTH, 5)
                .addMonsterStat(ModAttributes.RES_POISON.get(), 0.5));
        this.addStat(ModItems.RECOVERY_POTION.get(), new ItemStat.Builder(300, 25, 0)
                .addMonsterStat(Attributes.MAX_HEALTH, 3));
        this.addStat(ModItems.HEALING_POTION.get(), new ItemStat.Builder(500, 35, 0)
                .addMonsterStat(Attributes.MAX_HEALTH, 5));
        this.addStat(ModItems.MYSTERY_POTION.get(), new ItemStat.Builder(3000, 250, 0)
                .addMonsterStat(Attributes.MAX_HEALTH, 6));
        this.addStat(ModItems.MAGICAL_POTION.get(), new ItemStat.Builder(6000, 500, 0)
                .addMonsterStat(Attributes.MAX_HEALTH, 7));
        this.addStat(ModItems.INVINCIROID.get(), new ItemStat.Builder(25000, 1500, 0));
        this.addStat(ModItems.LOVE_POTION.get(), new ItemStat.Builder(50000, 2000, 0));
        this.addStat(ModItems.FORMUADE.get(), new ItemStat.Builder(20000, 700, 0));
        this.addStat(ModItems.OBJECT_X.get(), new ItemStat.Builder(6000, 500, 15));

        this.addStat(ModItems.BROAD_SWORD.get(), new ItemStat.Builder(100, 16, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 4)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3.0))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.STEEL_SWORD.get(), new ItemStat.Builder(1320, 54, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 8)
                .addAttribute(ModAttributes.DIZZY.get(), 5)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3.0))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.STEEL_SWORD_PLUS.get(), new ItemStat.Builder(2310, 99, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 14)
                .addAttribute(ModAttributes.DIZZY.get(), 5)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3.0))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.CUTLASS.get(), new ItemStat.Builder(5240, 210, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 21)
                .addAttribute(ModAttributes.DIZZY.get(), 5)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 2)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3.0))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.AQUA_SWORD.get(), new ItemStat.Builder(7850, 357, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 34)
                .addAttribute(ModAttributes.MAGIC.get(), 9)
                .addAttribute(ModAttributes.DIZZY.get(), 5)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3.0))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.INVISI_BLADE.get(), new ItemStat.Builder(12350, 571, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 49)
                .addAttribute(ModAttributes.MAGIC.get(), 6)
                .addAttribute(ModAttributes.DIZZY.get(), 5)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3.0))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.PLANT_SWORD.get(), new ItemStat.Builder(2000, 700, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5));

        this.addStat(ModItems.CLAYMORE.get(), new ItemStat.Builder(210, 17, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 6)
                .addAttribute(ModAttributes.DIZZY.get(), 10)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.5))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.ZWEIHAENDER.get(), new ItemStat.Builder(1360, 58, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 12)
                .addAttribute(ModAttributes.DIZZY.get(), 10)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.5))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.ZWEIHAENDER_PLUS.get(), new ItemStat.Builder(2170, 104, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 19)
                .addAttribute(ModAttributes.DIZZY.get(), 10)
                .addAttribute(ModAttributes.STUN.get(), 15)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.5))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.GREAT_SWORD.get(), new ItemStat.Builder(4960, 231, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 27)
                .addAttribute(ModAttributes.DIZZY.get(), 10)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.5))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.SEA_CUTTER.get(), new ItemStat.Builder(9170, 404, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 41)
                .addAttribute(ModAttributes.DIZZY.get(), 10)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.5))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.CYCLONE_BLADE.get(), new ItemStat.Builder(13680, 623, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 53)
                .addAttribute(ModAttributes.DIZZY.get(), 10)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.5))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));

        this.addStat(ModItems.SPEAR.get(), new ItemStat.Builder(190, 13, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5)
                .addAttribute(ModAttributes.DIZZY.get(), 6)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(5.3))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.WOOD_STAFF.get(), new ItemStat.Builder(1270, 56, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 8)
                .addAttribute(ModAttributes.DIZZY.get(), 6)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(5.3))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.LANCE.get(), new ItemStat.Builder(2310, 101, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 12)
                .addAttribute(ModAttributes.DEFENCE.get(), 1)
                .addAttribute(ModAttributes.DIZZY.get(), 6)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(5.3))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.LANCE_PLUS.get(), new ItemStat.Builder(4460, 198, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 20)
                .addAttribute(ModAttributes.DEFENCE.get(), 2)
                .addAttribute(ModAttributes.DIZZY.get(), 6)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(5.3))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.NEEDLE_SPEAR.get(), new ItemStat.Builder(7770, 333, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 35)
                .addAttribute(ModAttributes.DEFENCE.get(), 4)
                .addAttribute(ModAttributes.DIZZY.get(), 6)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(5.3))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.TRIDENT.get(), new ItemStat.Builder(13280, 543, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 50)
                .addAttribute(ModAttributes.DEFENCE.get(), 6)
                .addAttribute(ModAttributes.DIZZY.get(), 6)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(5.3))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));

        this.addStat(ModItems.BATTLE_AXE.get(), new ItemStat.Builder(250, 19, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 9)
                .addAttribute(ModAttributes.CRIT.get(), 5)
                .addAttribute(ModAttributes.DIZZY.get(), 2)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.5))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.BATTLE_SCYTHE.get(), new ItemStat.Builder(1430, 60, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 15)
                .addAttribute(ModAttributes.CRIT.get(), 15)
                .addAttribute(ModAttributes.DIZZY.get(), 2)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.5))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.POLE_AXE.get(), new ItemStat.Builder(3250, 147, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 25)
                .addAttribute(ModAttributes.CRIT.get(), 7)
                .addAttribute(ModAttributes.DIZZY.get(), 2)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.5))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.POLE_AXE_PLUS.get(), new ItemStat.Builder(5430, 245, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 39)
                .addAttribute(ModAttributes.CRIT.get(), 7)
                .addAttribute(ModAttributes.DIZZY.get(), 2)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.5))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.GREAT_AXE.get(), new ItemStat.Builder(9580, 417, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 54)
                .addAttribute(ModAttributes.CRIT.get(), 11)
                .addAttribute(ModAttributes.DIZZY.get(), 2)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.5))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.TOMAHAWK.get(), new ItemStat.Builder(14360, 683, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 70)
                .addAttribute(ModAttributes.CRIT.get(), 15)
                .addAttribute(ModAttributes.DIZZY.get(), 2)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.5))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));

        this.addStat(ModItems.BATTLE_HAMMER.get(), new ItemStat.Builder(245, 18, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 10)
                .addAttribute(ModAttributes.STUN.get(), 5)
                .addAttribute(ModAttributes.DIZZY.get(), 20)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.5))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.BAT.get(), new ItemStat.Builder(1240, 54, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 13)
                .addAttribute(ModAttributes.STUN.get(), 6)
                .addAttribute(ModAttributes.DIZZY.get(), 20)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.5))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.WAR_HAMMER.get(), new ItemStat.Builder(2960, 138, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 20)
                .addAttribute(ModAttributes.STUN.get(), 6)
                .addAttribute(ModAttributes.DIZZY.get(), 20)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.5))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.WAR_HAMMER_PLUS.get(), new ItemStat.Builder(6340, 265, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 30)
                .addAttribute(ModAttributes.STUN.get(), 7)
                .addAttribute(ModAttributes.DIZZY.get(), 20)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.5))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.IRON_BAT.get(), new ItemStat.Builder(9350, 421, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 44)
                .addAttribute(ModAttributes.STUN.get(), 9)
                .addAttribute(ModAttributes.DIZZY.get(), 20)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.5))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.GREAT_HAMMER.get(), new ItemStat.Builder(14740, 658, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 61)
                .addAttribute(ModAttributes.STUN.get(), 11)
                .addAttribute(ModAttributes.DIZZY.get(), 20)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(4.5))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));

        this.addStat(ModItems.SHORT_DAGGER.get(), new ItemStat.Builder(230, 12, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addAttribute(ModAttributes.DIZZY.get(), 3)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.STEEL_EDGE.get(), new ItemStat.Builder(950, 44, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 7)
                .addAttribute(ModAttributes.DIZZY.get(), 3)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.FROST_EDGE.get(), new ItemStat.Builder(2610, 121, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 12)
                .addAttribute(ModAttributes.MAGIC.get(), 4)
                .addAttribute(ModAttributes.DIZZY.get(), 3)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.IRON_EDGE.get(), new ItemStat.Builder(4910, 230, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 18)
                .addAttribute(ModAttributes.DIZZY.get(), 3)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.THIEF_KNIFE.get(), new ItemStat.Builder(7940, 384, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 27)
                .addAttribute(ModAttributes.DIZZY.get(), 3)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.WIND_EDGE.get(), new ItemStat.Builder(11600, 568, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 40)
                .addAttribute(ModAttributes.MAGIC.get(), 9)
                .addAttribute(ModAttributes.DIZZY.get(), 3)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setElement(EnumElement.WIND));

        this.addStat(ModItems.LEATHER_GLOVE.get(), new ItemStat.Builder(190, 13, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5)
                .addAttribute(ModAttributes.DIZZY.get(), 3)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.BRASS_KNUCKLES.get(), new ItemStat.Builder(1580, 74, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 9)
                .addAttribute(ModAttributes.DEFENCE.get(), 1)
                .addAttribute(ModAttributes.DIZZY.get(), 3)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.KOTE.get(), new ItemStat.Builder(3170, 136, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 16)
                .addAttribute(ModAttributes.DEFENCE.get(), 3)
                .addAttribute(ModAttributes.DIZZY.get(), 3)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.GLOVES.get(), new ItemStat.Builder(5480, 238, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 22)
                .addAttribute(ModAttributes.DEFENCE.get(), 4)
                .addAttribute(Attributes.MAX_HEALTH, 15)
                .addAttribute(ModAttributes.DIZZY.get(), 3)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.BEAR_CLAWS.get(), new ItemStat.Builder(8140, 394, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 33)
                .addAttribute(ModAttributes.DEFENCE.get(), 9)
                .addAttribute(ModAttributes.DIZZY.get(), 3)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.FIST_EARTH.get(), new ItemStat.Builder(12640, 587, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 48)
                .addAttribute(ModAttributes.MAGIC.get(), 10)
                .addAttribute(ModAttributes.DEFENCE.get(), 14)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 5)
                .addAttribute(ModAttributes.DIZZY.get(), 3)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setElement(EnumElement.EARTH));

        this.addStat(ModItems.ROD.get(), new ItemStat.Builder(281, 32, 0)
                .addAttribute(ModAttributes.MAGIC.get(), 5)
                .addAttribute(ModAttributes.DIZZY.get(), 15)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3.0))
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.5)
                .setElement(EnumElement.FIRE)
                .setSpell(ModSpells.FIREBALL.get(), null, null));
        this.addStat(ModItems.AMETHYST_ROD.get(), new ItemStat.Builder(1550, 76, 0)
                .addAttribute(ModAttributes.MAGIC.get(), 13)
                .addAttribute(ModAttributes.DIZZY.get(), 15)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3.0))
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.5)
                .setElement(EnumElement.EARTH));
        this.addStat(ModItems.AQUAMARINE_ROD.get(), new ItemStat.Builder(3430, 186, 0)
                .addAttribute(ModAttributes.MAGIC.get(), 17)
                .addAttribute(ModAttributes.DIZZY.get(), 15)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3.0))
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.5)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.FRIENDLY_ROD.get(), new ItemStat.Builder(8670, 297, 0)
                .addAttribute(ModAttributes.MAGIC.get(), 28)
                .addAttribute(ModAttributes.DIZZY.get(), 15)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3.0))
                .addMonsterStat(ModAttributes.MAGIC.get(), 1)
                .setElement(EnumElement.LOVE));
        this.addStat(ModItems.LOVE_LOVE_ROD.get(), new ItemStat.Builder(10550, 436, 0)
                .addAttribute(ModAttributes.MAGIC.get(), 41)
                .addAttribute(ModAttributes.DIZZY.get(), 15)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3.0))
                .addMonsterStat(ModAttributes.MAGIC.get(), 1)
                .setElement(EnumElement.LOVE));
        this.addStat(ModItems.STAFF.get(), new ItemStat.Builder(14110, 599, 0)
                .addAttribute(ModAttributes.MAGIC.get(), 66)
                .addAttribute(ModAttributes.DIZZY.get(), 15)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3.0))
                .addMonsterStat(ModAttributes.MAGIC.get(), 1)
                .setElement(EnumElement.EARTH));

        this.addStat(ModItems.HOE_SCRAP.get(), new ItemStat.Builder(150, 34, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 1)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.HOE_IRON.get(), new ItemStat.Builder(4500, 121, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 15)
                .addAttribute(ModAttributes.MAGIC.get(), 3)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.HOE_SILVER.get(), new ItemStat.Builder(25000, 300, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 34)
                .addAttribute(ModAttributes.MAGIC.get(), 5)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(EnumElement.EARTH));
        this.addStat(ModItems.HOE_GOLD.get(), new ItemStat.Builder(0, 550, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 76)
                .addAttribute(ModAttributes.MAGIC.get(), 15)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(EnumElement.EARTH));
        this.addStat(ModItems.HOE_PLATINUM.get(), new ItemStat.Builder(0, 2000, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 111)
                .addAttribute(ModAttributes.MAGIC.get(), 45)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(EnumElement.EARTH));

        this.addStat(ModItems.WATERING_CAN_SCRAP.get(), new ItemStat.Builder(150, 45, 0)
                .addAttribute(ModAttributes.MAGIC.get(), 1)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.WATERING_CAN_IRON.get(), new ItemStat.Builder(4500, 164, 0)
                .addAttribute(ModAttributes.MAGIC.get(), 7)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.WATERING_CAN_SILVER.get(), new ItemStat.Builder(25000, 300, 0)
                .addAttribute(ModAttributes.MAGIC.get(), 19)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.WATERING_CAN_GOLD.get(), new ItemStat.Builder(0, 550, 0)
                .addAttribute(ModAttributes.MAGIC.get(), 39)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.WATERING_CAN_PLATINUM.get(), new ItemStat.Builder(0, 2000, 0)
                .addAttribute(ModAttributes.MAGIC.get(), 99)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(EnumElement.WATER));

        this.addStat(ModItems.SICKLE_SCRAP.get(), new ItemStat.Builder(150, 24, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 2)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.SICKLE_IRON.get(), new ItemStat.Builder(4500, 118, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 19)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.SICKLE_SILVER.get(), new ItemStat.Builder(25000, 300, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 36)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.SICKLE_GOLD.get(), new ItemStat.Builder(0, 550, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 79)
                .addAttribute(ModAttributes.MAGIC.get(), 5)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(EnumElement.WIND));
        this.addStat(ModItems.SICKLE_PLATINUM.get(), new ItemStat.Builder(0, 2000, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 134)
                .addAttribute(ModAttributes.MAGIC.get(), 31)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(EnumElement.WIND));

        this.addStat(ModItems.AXE_SCRAP.get(), new ItemStat.Builder(150, 37, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addAttribute(ModAttributes.CRIT.get(), 5)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.AXE_IRON.get(), new ItemStat.Builder(4500, 148, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 22)
                .addAttribute(ModAttributes.CRIT.get(), 5)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.AXE_SILVER.get(), new ItemStat.Builder(25000, 300, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 39)
                .addAttribute(ModAttributes.CRIT.get(), 5)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.AXE_GOLD.get(), new ItemStat.Builder(0, 550, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 83)
                .addAttribute(ModAttributes.CRIT.get(), 5)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.AXE_PLATINUM.get(), new ItemStat.Builder(0, 2000, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 140)
                .addAttribute(ModAttributes.CRIT.get(), 15)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));

        this.addStat(ModItems.HAMMER_SCRAP.get(), new ItemStat.Builder(150, 39, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addAttribute(ModAttributes.CRIT.get(), -5)
                .addAttribute(ModAttributes.STUN.get(), 10)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.HAMMER_IRON.get(), new ItemStat.Builder(4500, 142, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 23)
                .addAttribute(ModAttributes.CRIT.get(), -5)
                .addAttribute(ModAttributes.STUN.get(), 10)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.HAMMER_SILVER.get(), new ItemStat.Builder(25000, 300, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 47)
                .addAttribute(ModAttributes.CRIT.get(), -5)
                .addAttribute(ModAttributes.STUN.get(), 10)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.HAMMER_GOLD.get(), new ItemStat.Builder(0, 550, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 85)
                .addAttribute(ModAttributes.CRIT.get(), -5)
                .addAttribute(ModAttributes.STUN.get(), 10)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.HAMMER_PLATINUM.get(), new ItemStat.Builder(0, 2000, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 145)
                .addAttribute(ModAttributes.CRIT.get(), -7)
                .addAttribute(ModAttributes.STUN.get(), 10)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));

        this.addStat(ModItems.FISHING_ROD_SCRAP.get(), new ItemStat.Builder(150, 35, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 1)
                .addAttribute(ModAttributes.MAGIC.get(), 5)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.FISHING_ROD_IRON.get(), new ItemStat.Builder(4500, 135, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 9)
                .addAttribute(ModAttributes.MAGIC.get(), 14)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.FISHING_ROD_SILVER.get(), new ItemStat.Builder(25000, 300, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 26)
                .addAttribute(ModAttributes.MAGIC.get(), 27)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.FISHING_ROD_GOLD.get(), new ItemStat.Builder(0, 550, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 66)
                .addAttribute(ModAttributes.MAGIC.get(), 72)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(ModItems.FISHING_ROD_PLATINUM.get(), new ItemStat.Builder(0, 2000, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 89)
                .addAttribute(ModAttributes.MAGIC.get(), 98)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));

        this.addStat(ModItems.MOB_STAFF.get(), new ItemStat.Builder(1500, 300, 0));
        this.addStat(ModItems.BRUSH.get(), new ItemStat.Builder(1500, 300, 0));
        this.addStat(ModItems.GLASS.get(), new ItemStat.Builder(2000, 400, 1));

        this.addStat(ModItems.LEVELISER.get(), new ItemStat.Builder(2000000, 3000, 0));
        this.addStat(ModItems.HEART_DRINK.get(), new ItemStat.Builder(1500000, 1500, 0));
        this.addStat(ModItems.VITAL_GUMMI.get(), new ItemStat.Builder(1500000, 1500, 0));
        this.addStat(ModItems.INTELLIGENCER.get(), new ItemStat.Builder(1500000, 1500, 0));
        this.addStat(ModItems.PROTEIN.get(), new ItemStat.Builder(1500000, 1500, 0));
        this.addStat(ModItems.FORMULAR_A.get(), new ItemStat.Builder(1000, 150, 0));
        this.addStat(ModItems.FORMULAR_B.get(), new ItemStat.Builder(2500, 250, 0));
        this.addStat(ModItems.FORMULAR_C.get(), new ItemStat.Builder(5000, 400, 0));
        this.addStat(ModItems.MINIMIZER.get(), new ItemStat.Builder(10000, 300, 0));
        this.addStat(ModItems.GIANTIZER.get(), new ItemStat.Builder(10000, 300, 0));
        this.addStat(ModItems.GREENIFIER.get(), new ItemStat.Builder(2000, 200, 0));
        this.addStat(ModItems.GREENIFIER_PLUS.get(), new ItemStat.Builder(10000, 500, 0));
        this.addStat(ModItems.WETTABLE_POWDER.get(), new ItemStat.Builder(1500, 150, 0));

        this.addStat(ModItems.CHEAP_BRACELET.get(), new ItemStat.Builder(120, 21, 0)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 1.5)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.5));
        this.addStat(ModItems.BRONZE_BRACELET.get(), new ItemStat.Builder(850, 38, 0)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 4)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.5));
        this.addStat(ModItems.SILVER_BRACELET.get(), new ItemStat.Builder(3000, 300, 0)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 10)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.5));
        this.addStat(ModItems.GOLD_BRACELET.get(), new ItemStat.Builder(15000, 750, 0)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 25)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.5));
        this.addStat(ModItems.PLATINUM_BRACELET.get(), new ItemStat.Builder(50000, 1000, 0)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 50)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.5));
        this.addStat(ModItems.SILVER_RING.get(), new ItemStat.Builder(20000, 600, 0)
                .addAttribute(ModAttributes.RES_LIGHT.get(), 50)
                .addAttribute(ModAttributes.RES_DARK.get(), 50)
                .addMonsterStat(ModAttributes.RES_LIGHT.get(), 1)
                .addMonsterStat(ModAttributes.RES_DARK.get(), 1));
        this.addStat(ModItems.GOLD_RING.get(), new ItemStat.Builder(0, 5000, 0)
                .addAttribute(ModAttributes.RES_WIND.get(), 15)
                .addAttribute(ModAttributes.RES_WATER.get(), 15)
                .addAttribute(ModAttributes.RES_EARTH.get(), 15)
                .addAttribute(ModAttributes.RES_FIRE.get(), 15)
                .addMonsterStat(ModAttributes.RES_WIND.get(), 1)
                .addMonsterStat(ModAttributes.RES_WATER.get(), 1)
                .addMonsterStat(ModAttributes.RES_EARTH.get(), 1)
                .addMonsterStat(ModAttributes.RES_FIRE.get(), 1));
        this.addStat(ModItems.PLATINUM_RING.get(), new ItemStat.Builder(0, 7500, 0)
                .addAttribute(ModAttributes.RES_WIND.get(), 10)
                .addAttribute(ModAttributes.RES_WATER.get(), 10)
                .addAttribute(ModAttributes.RES_EARTH.get(), 10)
                .addAttribute(ModAttributes.RES_FIRE.get(), 10)
                .addAttribute(ModAttributes.RES_LIGHT.get(), 25)
                .addAttribute(ModAttributes.RES_DARK.get(), 25)
                .addMonsterStat(ModAttributes.RES_WIND.get(), 1)
                .addMonsterStat(ModAttributes.RES_WATER.get(), 1)
                .addMonsterStat(ModAttributes.RES_EARTH.get(), 1)
                .addMonsterStat(ModAttributes.RES_FIRE.get(), 1));

        this.addStat(ModItems.SHIRT.get(), new ItemStat.Builder(120, 13, 0)
                .addAttribute(ModAttributes.DEFENCE.get(), 2)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat(ModItems.VEST.get(), new ItemStat.Builder(1000, 30, 0)
                .addAttribute(ModAttributes.DEFENCE.get(), 7)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 2)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat(ModItems.COTTON_CLOTH.get(), new ItemStat.Builder(4000, 90, 0)
                .addAttribute(ModAttributes.DEFENCE.get(), 12)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 4)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));

        this.addStat(ModItems.HEADBAND.get(), new ItemStat.Builder(50, 5, 0)
                .addAttribute(ModAttributes.DEFENCE.get(), 1)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat(ModItems.BLUE_RIBBON.get(), new ItemStat.Builder(500, 35, 0)
                .addAttribute(ModAttributes.DEFENCE.get(), 3)
                .addAttribute(ModAttributes.RES_SEAL.get(), 5)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat(ModItems.GREEN_RIBBON.get(), new ItemStat.Builder(500, 35, 0)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 3)
                .addAttribute(ModAttributes.RES_SEAL.get(), 5)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat(ModItems.PURPLE_RIBBON.get(), new ItemStat.Builder(500, 35, 0)
                .addAttribute(ModAttributes.DEFENCE.get(), 2)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 1)
                .addAttribute(ModAttributes.RES_SEAL.get(), 5)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));

        this.addStat(ModItems.LEATHER_BOOTS.get(), new ItemStat.Builder(75, 10, 0)
                .addAttribute(ModAttributes.DEFENCE.get(), 2)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat(ModItems.FREE_FARMING_SHOES.get(), new ItemStat.Builder(450, 40, 0)
                .addAttribute(ModAttributes.DEFENCE.get(), 3)
                .addAttribute(ModAttributes.RES_FAT.get(), 3)
                .addAttribute(ModAttributes.RES_COLD.get(), 3)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat(ModItems.PIYO_SANDALS.get(), new ItemStat.Builder(400, 35, 0)
                .withArmorEffect(ModArmorEffects.PIYO_SANDALS.get())
                .addAttribute(ModAttributes.DEFENCE.get(), 5)
                .addAttribute(ModAttributes.RES_LOVE.get(), 1)
                .addAttribute(ModAttributes.RES_DIZZY.get(), 50)
                .addAttribute(ModAttributes.DIZZY.get(), 20)
                .addMonsterStat(ModAttributes.RES_DIZZY.get(), 0.7));

        this.addStat(Items.SHIELD, new ItemStat.Builder(200, 14, 0)
                .addAttribute(ModAttributes.DEFENCE.get(), 2)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat(ModItems.SMALL_SHIELD.get(), new ItemStat.Builder(600, 23, 0)
                .addAttribute(ModAttributes.DEFENCE.get(), 4)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat(ModItems.IRON_SHIELD.get(), new ItemStat.Builder(1000, 50, 0)
                .addAttribute(ModAttributes.DEFENCE.get(), 7)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat(ModItems.PLANT_SHIELD.get(), new ItemStat.Builder(2000, 700, 0)
                .addAttribute(ModAttributes.DEFENCE.get(), 5));

        this.addStat(ModItems.SCRAP.get(), new ItemStat.Builder(13, 1, 1)
                .addAttribute(ModAttributes.DEFENCE.get(), -2)
                .addMonsterStat(ModAttributes.DEFENCE.get(), -1));
        this.addStat(ModItems.SCRAP_PLUS.get(), new ItemStat.Builder(0, 2, 1)
                .addAttribute(ModAttributes.DEFENCE.get(), 2)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.2));
        this.addStat("iron", RunecraftoryTags.IRON, new ItemStat.Builder(150, 2, 5)
                .addAttribute(ModAttributes.DEFENCE.get(), 1)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat("bronze", RunecraftoryTags.BRONZE, new ItemStat.Builder(400, 14, 13)
                .addAttribute(ModAttributes.DEFENCE.get(), 2.4)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat("copper", RunecraftoryTags.COPPER, new ItemStat.Builder(200, 9, 10)
                .addAttribute(ModAttributes.DEFENCE.get(), 1.6)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat("silver", RunecraftoryTags.SILVER, new ItemStat.Builder(1500, 27, 15)
                .addAttribute(ModAttributes.DEFENCE.get(), 5)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.6));
        this.addStat("gold", RunecraftoryTags.GOLD, new ItemStat.Builder(3500, 34, 20)
                .addAttribute(ModAttributes.DEFENCE.get(), 7)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 5)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.7));
        this.addStat("platinum", RunecraftoryTags.PLATINUM, new ItemStat.Builder(5000, 111, 34)
                .addAttribute(ModAttributes.DEFENCE.get(), 30)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.8));
        this.addStat("orichalcum", RunecraftoryTags.ORICHALCUM, new ItemStat.Builder(20000, 750, 65)
                .addAttribute(ModAttributes.DEFENCE.get(), 70)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 1));
        this.addStat("dragonic", RunecraftoryTags.DRAGONIC, new ItemStat.Builder(0, 1000, 70)
                .addAttribute(ModAttributes.DEFENCE.get(), 130)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 90)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 1.5));
        this.addStat(Items.NETHERITE_INGOT, new ItemStat.Builder(0, 200, 40)
                .addAttribute(ModAttributes.DEFENCE.get(), 20)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 20)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.9));

        this.addStat("emerald", RunecraftoryTags.EMERALDS, new ItemStat.Builder(2500, 5, 24)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 5)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.5));
        this.addStat(Items.DIAMOND, new ItemStat.Builder(5000, 21, 29)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 15)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 1));
        this.addStat("amethyst", RunecraftoryTags.AMETHYSTS, new ItemStat.Builder(3500, 18, 24)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 7)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.75));
        this.addStat("aquamarine", RunecraftoryTags.AQUAMARINES, new ItemStat.Builder(3500, 23, 24)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 7)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.5));
        this.addStat("ruby", RunecraftoryTags.RUBIES, new ItemStat.Builder(4000, 37, 24)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 7)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.5));
        this.addStat("sapphire", RunecraftoryTags.SAPPHIRES, new ItemStat.Builder(3500, 24, 24)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 7)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 7));
        this.addStat(ModItems.CORE_GREEN.get(), new ItemStat.Builder(15000, 1050, 24)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 50)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 2));
        this.addStat(ModItems.CORE_RED.get(), new ItemStat.Builder(15000, 1050, 24)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 50)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 2));
        this.addStat(ModItems.CORE_BLUE.get(), new ItemStat.Builder(15000, 1050, 24)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 50)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 2));
        this.addStat(ModItems.CORE_YELLOW.get(), new ItemStat.Builder(15000, 1050, 24)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 50)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 2));
        this.addStat(ModItems.CRYSTAL_SKULL.get(), new ItemStat.Builder(25000, 2300, 24)
                .addAttribute(ModAttributes.MAGIC.get(), 40)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 70)
                .addMonsterStat(ModAttributes.MAGIC.get(), 2)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 4));

        this.addStat(ModItems.CRYSTAL_WATER.get(), new ItemStat.Builder(2000, 150, 20)
                .addAttribute(ModAttributes.MAGIC.get(), 5)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.5)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.5)
                .setElement(EnumElement.WATER));
        this.addStat(ModItems.CRYSTAL_EARTH.get(), new ItemStat.Builder(2000, 150, 20)
                .addAttribute(ModAttributes.DEFENCE.get(), 5)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5)
                .setElement(EnumElement.EARTH));
        this.addStat(ModItems.CRYSTAL_FIRE.get(), new ItemStat.Builder(2000, 150, 20)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5)
                .setElement(EnumElement.FIRE));
        this.addStat(ModItems.CRYSTAL_WIND.get(), new ItemStat.Builder(2000, 150, 20)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 5)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.5)
                .setElement(EnumElement.WIND));
        this.addStat(ModItems.CRYSTAL_LIGHT.get(), new ItemStat.Builder(2000, 150, 20)
                .addAttribute(ModAttributes.DEFENCE.get(), 1.5)
                .addAttribute(ModAttributes.MAGIC.get(), 3)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 1.5)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.5)
                .setElement(EnumElement.LIGHT));
        this.addStat(ModItems.CRYSTAL_DARK.get(), new ItemStat.Builder(2000, 150, 20)
                .addAttribute(ModAttributes.DEFENCE.get(), 1.5)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 1.5)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5)
                .setElement(EnumElement.DARK));
        this.addStat(ModItems.CRYSTAL_LOVE.get(), new ItemStat.Builder(2000, 150, 20)
                .addAttribute(ModAttributes.DRAIN.get(), 3)
                .addMonsterStat(ModAttributes.DRAIN.get(), 0.5)
                .setElement(EnumElement.LOVE));
        this.addStat(ModItems.CRYSTAL_SMALL.get(), 0, 0, 0);
        this.addStat(ModItems.CRYSTAL_BIG.get(), 0, 0, 0);
        this.addStat(ModItems.CRYSTAL_MAGIC.get(), new ItemStat.Builder(45, 400, 25)
                .addAttribute(ModAttributes.MAGIC.get(), 7)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 1));
        this.addStat(ModItems.CRYSTAL_RUNE.get(), 0, 0, 0);
        this.addStat(ModItems.CRYSTAL_ELECTRO.get(), 0, 0, 0);

        this.addStat(ModItems.STICK_THICK.get(), new ItemStat.Builder(1900, 200, 45)
                .addAttribute(Attributes.ATTACK_DAMAGE, 15));
        this.addStat(ModItems.HORN_INSECT.get(), new ItemStat.Builder(130, 21, 8)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.HORN_RIGID.get(), new ItemStat.Builder(200, 44, 11)
                .addAttribute(Attributes.ATTACK_DAMAGE, 10)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.PLANT_STEM.get(), new ItemStat.Builder(300, 52, 17)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addAttribute(ModAttributes.MAGIC.get(), 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.4));
        this.addStat(ModItems.HORN_BULL.get(), new ItemStat.Builder(450, 64, 26)
                .addAttribute(Attributes.ATTACK_DAMAGE, 8)
                .addAttribute(ModAttributes.DEFENCE.get(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.7));
        this.addStat(ModItems.HORN_DEVIL.get(), new ItemStat.Builder(850, 91, 43)
                .addAttribute(Attributes.ATTACK_DAMAGE, 30)
                .addAttribute(ModAttributes.MAGIC.get(), 30)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .addMonsterStat(ModAttributes.MAGIC.get(), 1));
        this.addStat(ModItems.MOVING_BRANCH.get(), new ItemStat.Builder(22000, 2300, 77)
                .addAttribute(Attributes.ATTACK_DAMAGE, -10)
                .addAttribute(ModAttributes.MAGIC.get(), 150)
                .setSpell(ModSpells.ROOT_SPIKE.get(), ModSpells.APPLE_SHIELD.get(), null));

        this.addStat(ModItems.GLUE.get(), new ItemStat.Builder(380, 41, 13)
                .addAttribute(Attributes.ATTACK_DAMAGE, 1)
                .addAttribute(ModAttributes.DEFENCE.get(), 1)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.3)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.3));
        this.addStat(ModItems.DEVIL_BLOOD.get(), 0, 0, 0);
        this.addStat(ModItems.PARA_POISON.get(), 0, 0, 0);
        this.addStat(ModItems.POISON_KING.get(), 0, 0, 0);

        this.addStat(ModItems.FEATHER_BLACK.get(), 0, 0, 0);
        this.addStat(ModItems.FEATHER_THUNDER.get(), 0, 0, 0);
        this.addStat(ModItems.FEATHER_YELLOW.get(), new ItemStat.Builder(500, 20, 15)
                .addAttribute(ModAttributes.MAGIC.get(), 13)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.7));
        this.addStat(ModItems.DRAGON_FIN.get(), 0, 0, 0);

        this.addStat(ModItems.TURTLE_SHELL.get(), new ItemStat.Builder(160, 30, 16)
                .addAttribute(ModAttributes.DEFENCE.get(), 5)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 3)
                .addAttribute(ModAttributes.CRIT.get(), -3)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 1));
        this.addStat(ModItems.FISH_FOSSIL.get(), new ItemStat.Builder(180, 30, 19)
                .addAttribute(ModAttributes.MAGIC.get(), 5)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 1)
                .addMonsterStat(ModAttributes.MAGIC.get(), 1));
        this.addStat(ModItems.SKULL.get(), new ItemStat.Builder(100, 1000, 35)
                .addAttribute(ModAttributes.MAGIC.get(), 9)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 4)
                .addAttribute(ModAttributes.COLD.get(), 5)
                .addMonsterStat(ModAttributes.MAGIC.get(), 1)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.3));
        this.addStat(ModItems.DRAGON_BONES.get(), new ItemStat.Builder(0, 0, 52)
                .addAttribute(ModAttributes.DEFENCE.get(), 13)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 13)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.5)
                .setSpell(null, ModSpells.BONE_NEEDLES.get(), ModSpells.ENERGY_ORB_SPELL.get()));
        this.addStat(ModItems.TORTOISE_SHELL.get(), 0, 0, 0);

        this.addStat(ModItems.ROCK.get(), 0, 0, 0);
        this.addStat(ModItems.STONE_ROUND.get(), 0, 0, 0);
        this.addStat(ModItems.STONE_TINY.get(), 0, 0, 0);
        this.addStat(ModItems.STONE_GOLEM.get(), 0, 0, 0);
        this.addStat(ModItems.TABLET_GOLEM.get(), 0, 0, 0);
        this.addStat(ModItems.STONE_SPIRIT.get(), 0, 0, 0);
        this.addStat(ModItems.TABLET_TRUTH.get(), 0, 0, 0);

        this.addStat(ModItems.YARN.get(), new ItemStat.Builder(400, 75, 0)
                .addAttribute(ModAttributes.DEFENCE.get(), 3)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 1)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.3));
        this.addStat(ModItems.OLD_BANDAGE.get(), new ItemStat.Builder(0, 0, 0)
                .addAttribute(ModAttributes.MAGIC.get(), 2)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 1)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.3));
        this.addStat(ModItems.AMBROSIAS_THORNS.get(), new ItemStat.Builder(7500, 350, 21)
                .addAttribute(Attributes.ATTACK_DAMAGE, 7)
                .addAttribute(ModAttributes.MAGIC.get(), 7)
                .addAttribute(ModAttributes.SLEEP.get(), 10)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .addMonsterStat(ModAttributes.MAGIC.get(), 1)
                .addMonsterStat(ModAttributes.SLEEP.get(), 0.4)
                .setSpell(ModSpells.POLLEN_PUFF.get(), ModSpells.WAVE.get(), ModSpells.BUTTERFLY.get()));
        this.addStat(ModItems.THREAD_SPIDER.get(), new ItemStat.Builder(370, 28, 17)
                .addAttribute(Attributes.ATTACK_DAMAGE, 2)
                .addAttribute(ModAttributes.MAGIC.get(), 2)
                .addAttribute(ModAttributes.PARA.get(), 5)
                .addMonsterStat(ModAttributes.PARA.get(), 0.3));
        this.addStat(ModItems.PUPPETRY_STRINGS.get(), new ItemStat.Builder(30000, 1000, 37)
                .addAttribute(Attributes.ATTACK_DAMAGE, 6)
                .addAttribute(ModAttributes.MAGIC.get(), 9)
                .addAttribute(ModAttributes.SEAL.get(), 15)
                .addAttribute(ModAttributes.PARA.get(), 5)
                .addMonsterStat(ModAttributes.SEAL.get(), 0.5)
                .addMonsterStat(ModAttributes.PARA.get(), 0.3)
                .setSpell(ModSpells.DARK_BEAM.get(), ModSpells.PLATE.get(), ModSpells.DARK_BULLETS.get()));
        this.addStat(ModItems.VINE.get(), new ItemStat.Builder(515, 58, 34)
                .addAttribute(ModAttributes.MAGIC.get(), 10)
                .addAttribute(ModAttributes.SEAL.get(), 5)
                .addMonsterStat(ModAttributes.SEAL.get(), 0.33));
        this.addStat(ModItems.TAIL_SCORPION.get(), new ItemStat.Builder(610, 62, 31)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5)
                .addAttribute(ModAttributes.POISON.get(), 2)
                .addMonsterStat(ModAttributes.POISON.get(), 0.35));
        this.addStat(ModItems.STRONG_VINE.get(), 0, 0, 0);
        this.addStat(ModItems.THREAD_PRETTY.get(), 0, 0, 0);
        this.addStat(ModItems.TAIL_CHIMERA.get(), new ItemStat.Builder(20000, 2600, 87)
                .addAttribute(Attributes.ATTACK_DAMAGE, 150)
                .addAttribute(ModAttributes.MAGIC.get(), -10)
                .addAttribute(ModAttributes.POISON.get(), 50)
                .addAttribute(ModAttributes.PARA.get(), 50)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .addMonsterStat(ModAttributes.POISON.get(), 1)
                .addMonsterStat(ModAttributes.PARA.get(), 1)
                .setSpell(null, ModSpells.BUBBLE_BEAM.get(), ModSpells.FIREBALL_BARRAGE.get()));

        this.addStat(ModItems.ARROW_HEAD.get(), new ItemStat.Builder(80, 10, 2)
                .addAttribute(Attributes.ATTACK_DAMAGE, 2)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.2));
        this.addStat(ModItems.BLADE_SHARD.get(), new ItemStat.Builder(139, 25, 9)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.3));
        this.addStat(ModItems.BROKEN_HILT.get(), new ItemStat.Builder(550, 50, 22)
                .addAttribute(ModAttributes.DEFENCE.get(), 2)
                .addAttribute(ModAttributes.RES_POISON.get(), 5)
                .addAttribute(ModAttributes.RES_SEAL.get(), 5)
                .addAttribute(ModAttributes.RES_PARA.get(), 5)
                .addAttribute(ModAttributes.RES_SLEEP.get(), 5)
                .addAttribute(ModAttributes.RES_FAT.get(), 5)
                .addAttribute(ModAttributes.RES_COLD.get(), 5)
                .addMonsterStat(ModAttributes.RES_POISON.get(), 0.2)
                .addMonsterStat(ModAttributes.RES_SEAL.get(), 0.2)
                .addMonsterStat(ModAttributes.RES_PARA.get(), 0.2)
                .addMonsterStat(ModAttributes.RES_SLEEP.get(), 0.2)
                .addMonsterStat(ModAttributes.RES_FAT.get(), 0.2)
                .addMonsterStat(ModAttributes.RES_COLD.get(), 0.2));
        this.addStat(ModItems.BROKEN_BOX.get(), new ItemStat.Builder(1000, 200, 48)
                .addAttribute(ModAttributes.DEFENCE.get(), 4)
                .addAttribute(ModAttributes.RES_POISON.get(), 10)
                .addAttribute(ModAttributes.RES_SEAL.get(), 10)
                .addAttribute(ModAttributes.RES_PARA.get(), 10)
                .addAttribute(ModAttributes.RES_SLEEP.get(), 10)
                .addAttribute(ModAttributes.RES_FAT.get(), 10)
                .addAttribute(ModAttributes.RES_COLD.get(), 10)
                .addMonsterStat(ModAttributes.RES_POISON.get(), 0.5)
                .addMonsterStat(ModAttributes.RES_SEAL.get(), 0.5)
                .addMonsterStat(ModAttributes.RES_PARA.get(), 0.5)
                .addMonsterStat(ModAttributes.RES_SLEEP.get(), 0.5)
                .addMonsterStat(ModAttributes.RES_FAT.get(), 0.5)
                .addMonsterStat(ModAttributes.RES_COLD.get(), 0.5));
        this.addStat(ModItems.BLADE_GLISTENING.get(), 0, 0, 0);
        this.addStat(ModItems.GREAT_HAMMER_SHARD.get(), 0, 0, 0);
        this.addStat(ModItems.HAMMER_PIECE.get(), 0, 0, 0);
        this.addStat(ModItems.SHOULDER_PIECE.get(), 0, 0, 0);
        this.addStat(ModItems.PIRATES_ARMOR.get(), 0, 0, 0);
        this.addStat(ModItems.SCREW_RUSTY.get(), 0, 0, 0);
        this.addStat(ModItems.SCREW_SHINY.get(), 0, 0, 0);
        this.addStat(ModItems.ROCK_SHARD_LEFT.get(), 0, 0, 0);
        this.addStat(ModItems.ROCK_SHARD_RIGHT.get(), 0, 0, 0);
        this.addStat(ModItems.MTGU_PLATE.get(), 0, 0, 0);
        this.addStat(ModItems.BROKEN_ICE_WALL.get(), 0, 0, 0);

        this.addStat(ModItems.FUR_SMALL.get(), new ItemStat.Builder(35, 7, 1)
                .addAttribute(ModAttributes.DEFENCE.get(), 1)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 1)
                .addAttribute(ModAttributes.RES_DIZZY.get(), 1)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.3));
        this.addStat(ModItems.FUR_MEDIUM.get(), new ItemStat.Builder(1000, 100, 29)
                .addAttribute(ModAttributes.DEFENCE.get(), 5)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 3)
                .addAttribute(ModAttributes.RES_DIZZY.get(), 10)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat(ModItems.FUR_LARGE.get(), new ItemStat.Builder(3000, 500, 55)
                .addAttribute(ModAttributes.DEFENCE.get(), 10)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 5)
                .addAttribute(ModAttributes.RES_DIZZY.get(), 25)
                .addAttribute(ModAttributes.RES_SLEEP.get(), 5)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.7));
        this.addStat(ModItems.FUR.get(), new ItemStat.Builder(130, 23, 7)
                .addAttribute(ModAttributes.DEFENCE.get(), 2)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 1)
                .addAttribute(ModAttributes.RES_DIZZY.get(), 3)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.3));
        this.addStat(ModItems.FURBALL.get(), new ItemStat.Builder(900, 120, 38)
                .addAttribute(ModAttributes.DEFENCE.get(), 8)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 4)
                .addAttribute(ModAttributes.RES_DIZZY.get(), 10)
                .addAttribute(ModAttributes.RES_SLEEP.get(), 3)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat(ModItems.DOWN_YELLOW.get(), new ItemStat.Builder(300, 33, 21)
                .addAttribute(ModAttributes.DEFENCE.get(), 3)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 2)
                .addAttribute(ModAttributes.RES_DIZZY.get(), 5)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat(ModItems.FUR_QUALITY.get(), new ItemStat.Builder(650, 45, 36)
                .addAttribute(ModAttributes.DEFENCE.get(), 4)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 3)
                .addAttribute(ModAttributes.RES_DIZZY.get(), 7)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.5));
        this.addStat(ModItems.DOWN_PENGUIN.get(), new ItemStat.Builder(1250, 129, 59)
                .addAttribute(ModAttributes.DEFENCE.get(), 13)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 7)
                .addAttribute(ModAttributes.RES_DIZZY.get(), 13)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.6));
        this.addStat(ModItems.LIGHTNING_MANE.get(), new ItemStat.Builder(13000, 600, 31)
                .addAttribute(ModAttributes.DEFENCE.get(), 8)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 6)
                .addAttribute(ModAttributes.RES_DIZZY.get(), 17)
                .addMonsterStat(ModAttributes.RES_DIZZY.get(), 1)
                .setSpell(ModSpells.LASER3.get(), ModSpells.LASER5.get(), ModSpells.BIG_LIGHTNING.get()));
        this.addStat(ModItems.FUR_RED_LION.get(), 0, 0, 0);
        this.addStat(ModItems.FUR_BLUE_LION.get(), 0, 0, 0);
        this.addStat(ModItems.CHEST_HAIR.get(), 0, 0, 0);

        this.addStat(ModItems.SPORE.get(), new ItemStat.Builder(110, 19, 9)
                .addAttribute(ModAttributes.MAGIC.get(), 2)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.3));
        this.addStat(ModItems.POWDER_POISON.get(), new ItemStat.Builder(550, 80, 21)
                .addAttribute(ModAttributes.MAGIC.get(), 2)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 2)
                .addAttribute(ModAttributes.POISON.get(), 5)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.3)
                .addMonsterStat(ModAttributes.MAGIC_DEFENCE.get(), 0.3)
                .addMonsterStat(ModAttributes.POISON.get(), 0.5));
        this.addStat(ModItems.SPORE_HOLY.get(), 0, 0, 0);
        this.addStat(ModItems.FAIRY_DUST.get(), new ItemStat.Builder(300, 40, 19)
                .addAttribute(ModAttributes.MAGIC.get(), 5)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.33));
        this.addStat(ModItems.FAIRY_ELIXIR.get(), 0, 0, 0);
        this.addStat(ModItems.ROOT.get(), new ItemStat.Builder(770, 68, 25)
                .addAttribute(ModAttributes.MAGIC.get(), 8)
                .addMonsterStat(ModAttributes.MAGIC.get(), 0.4));
        this.addStat(ModItems.POWDER_MAGIC.get(), 0, 0, 0);
        this.addStat(ModItems.POWDER_MYSTERIOUS.get(), 0, 0, 0);
        this.addStat(ModItems.MAGIC.get(), 0, 0, 0);
        this.addStat(ModItems.ASH_EARTH.get(), 0, 0, 0);
        this.addStat(ModItems.ASH_FIRE.get(), 0, 0, 0);
        this.addStat(ModItems.ASH_WATER.get(), 0, 0, 0);
        this.addStat(ModItems.TURNIPS_MIRACLE.get(), 0, 0, 0);
        this.addStat(ModItems.MELODY_BOTTLE.get(), 0, 0, 0);

        this.addStat(ModItems.CLOTH_CHEAP.get(), new ItemStat.Builder(80, 12, 4)
                .addAttribute(ModAttributes.DEFENCE.get(), 1)
                .addAttribute(ModAttributes.RES_CRIT.get(), 1)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.3));
        this.addStat(ModItems.CLOTH_QUALITY.get(), new ItemStat.Builder(800, 100, 18)
                .addAttribute(ModAttributes.DEFENCE.get(), 5)
                .addAttribute(ModAttributes.RES_CRIT.get(), 5)
                .addMonsterStat(ModAttributes.DEFENCE.get(), 0.3));
        this.addStat(ModItems.CLOTH_QUALITY_WORN.get(), 0, 0, 0);
        this.addStat(ModItems.CLOTH_SILK.get(), 0, 0, 0);
        this.addStat(ModItems.GHOST_HOOD.get(), new ItemStat.Builder(70, 650, 21)
                .addAttribute(ModAttributes.DEFENCE.get(), 2)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 2)
                .addAttribute(ModAttributes.RES_SEAL.get(), 25)
                .addMonsterStat(ModAttributes.RES_SEAL.get(), 0.5));
        this.addStat(ModItems.GLOVE_GIANT.get(), new ItemStat.Builder(810, 76, 36)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5)
                .addAttribute(ModAttributes.DEFENCE.get(), 10)
                .addAttribute(ModAttributes.RES_CRIT.get(), 5)
                .addMonsterStat(ModAttributes.RES_CRIT.get(), 0.5));
        this.addStat(ModItems.GLOVE_BLUE_GIANT.get(), 0, 0, 0);
        this.addStat(ModItems.CARAPACE_INSECT.get(), new ItemStat.Builder(75, 11, 8)
                .addAttribute(ModAttributes.DEFENCE.get(), 2)
                .addAttribute(ModAttributes.RES_POISON.get(), 15)
                .addMonsterStat(ModAttributes.RES_POISON.get(), 0.5));
        this.addStat(ModItems.CARAPACE_PRETTY.get(), new ItemStat.Builder(750, 85, 24)
                .addAttribute(ModAttributes.DEFENCE.get(), 4)
                .addAttribute(ModAttributes.RES_SEAL.get(), 5)
                .addAttribute(ModAttributes.RES_PARA.get(), 20)
                .addMonsterStat(ModAttributes.RES_PARA.get(), 0.5));
        this.addStat(ModItems.CLOTH_ANCIENT_ORC.get(), 0, 0, 0);

        this.addStat(ModItems.JAW_INSECT.get(), new ItemStat.Builder(100, 23, 14)
                .addAttribute(Attributes.ATTACK_DAMAGE, 2)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.3));
        this.addStat(ModItems.CLAW_PANTHER.get(), new ItemStat.Builder(450, 55, 28)
                .addAttribute(Attributes.ATTACK_DAMAGE, 4)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(ModItems.CLAW_MAGIC.get(), 0, 0, 0);
        this.addStat(ModItems.FANG_WOLF.get(), new ItemStat.Builder(470, 60, 31)
                .addAttribute(Attributes.ATTACK_DAMAGE, 6)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.6));
        this.addStat(ModItems.FANG_GOLD_WOLF.get(), 0, 0, 0);
        this.addStat(ModItems.CLAW_PALM.get(), new ItemStat.Builder(640, 74, 37)
                .addAttribute(Attributes.ATTACK_DAMAGE, 9)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.8));
        this.addStat(ModItems.CLAW_MALM.get(), 0, 0, 0);
        this.addStat(ModItems.GIANTS_NAIL.get(), new ItemStat.Builder(980, 103, 44)
                .addAttribute(Attributes.ATTACK_DAMAGE, 11)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.6));
        this.addStat(ModItems.CLAW_CHIMERA.get(), new ItemStat.Builder(18000, 1500, 50)
                .addAttribute(Attributes.ATTACK_DAMAGE, 15)
                .addAttribute(ModAttributes.MAGIC.get(), 2)
                .addAttribute(ModAttributes.POISON.get(), 5)
                .addAttribute(ModAttributes.PARA.get(), 5)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 2)
                .addMonsterStat(ModAttributes.POISON.get(), 1)
                .addMonsterStat(ModAttributes.PARA.get(), 1));
        this.addStat(ModItems.TUSK_IVORY.get(), 0, 0, 0);
        this.addStat(ModItems.TUSK_UNBROKEN_IVORY.get(), 0, 0, 0);
        this.addStat(ModItems.SCORPION_PINCER.get(), new ItemStat.Builder(1470, 138, 55)
                .addAttribute(Attributes.ATTACK_DAMAGE, 20)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.DANGEROUS_SCISSORS.get(), 0, 0, 0);
        this.addStat(ModItems.PROPELLOR_CHEAP.get(), 0, 0, 0);
        this.addStat(ModItems.PROPELLOR_QUALITY.get(), 0, 0, 0);
        this.addStat(ModItems.FANG_DRAGON.get(), 0, 0, 0);
        this.addStat(ModItems.JAW_QUEEN.get(), 0, 0, 0);
        this.addStat(ModItems.WIND_DRAGON_TOOTH.get(), 0, 0, 0);
        this.addStat(ModItems.GIANTS_NAIL_BIG.get(), 0, 0, 0);

        this.addStat(ModItems.SCALE_WET.get(), 0, 0, 0);
        this.addStat(ModItems.SCALE_GRIMOIRE.get(), 0, 0, 0);
        this.addStat(ModItems.SCALE_DRAGON.get(), 0, 0, 0);
        this.addStat(ModItems.SCALE_CRIMSON.get(), 0, 0, 0);
        this.addStat(ModItems.SCALE_BLUE.get(), 0, 0, 0);
        this.addStat(ModItems.SCALE_GLITTER.get(), 0, 0, 0);
        this.addStat(ModItems.SCALE_LOVE.get(), 0, 0, 0);
        this.addStat(ModItems.SCALE_BLACK.get(), 0, 0, 0);
        this.addStat(ModItems.SCALE_FIRE.get(), 0, 0, 0);
        this.addStat(ModItems.SCALE_EARTH.get(), 0, 0, 0);
        this.addStat(ModItems.SCALE_LEGEND.get(), 0, 0, 0);

        this.addStat(ModItems.STEEL_DOUBLE.get(), new ItemStat.Builder(0, 200, 50));
        this.addStat(ModItems.STEEL_TEN.get(), new ItemStat.Builder(0, 2000, 95));
        this.addStat(ModItems.GLITTA_AUGITE.get(), new ItemStat.Builder(0, 1200, 0)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), 1));
        this.addStat(ModItems.INVIS_STONE.get(), new ItemStat.Builder(0, 750, 24));
        this.addStat(ModItems.LIGHT_ORE.get(), new ItemStat.Builder(0, 7500, 0));
        this.addStat(ModItems.RUNE_SPHERE_SHARD.get(), 0, 0, 0);
        this.addStat(ModItems.SHADE_STONE.get(), 0, 0, 0);
        this.addStat(ModItems.RACCOON_LEAF.get(), new ItemStat.Builder(25000, 2100, 60)
                .addAttribute(ModAttributes.MAGIC.get(), 35)
                .addAttribute(ModAttributes.ATTACK_RANGE.get(), 1)
                .setSpell(ModSpells.BIG_LEAF_SPELL.get(), ModSpells.SMALL_LEAF_SPELL_X5.get(), null));
        this.addStat(ModItems.ICY_NOSE.get(), 0, 0, 0);
        this.addStat(ModItems.BIG_BIRDS_COMB.get(), 0, 0, 0);
        this.addStat(ModItems.RAFFLESIA_PETAL.get(), new ItemStat.Builder(30000, 3400, 55)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5)
                .addAttribute(ModAttributes.MAGIC.get(), 18)
                .addAttribute(ModAttributes.FATIGUE.get(), 20)
                .addAttribute(ModAttributes.COLD.get(), 15)
                .addMonsterStat(ModAttributes.MAGIC.get(), 1)
                .addMonsterStat(ModAttributes.FATIGUE.get(), 1)
                .addMonsterStat(ModAttributes.COLD.get(), 1)
                .setSpell(ModSpells.WIND_CIRCLE_X8.get(), ModSpells.RAFFLESIA_CIRCLE.get(), ModSpells.RAFFLESIA_POISON.get()));
        this.addStat(ModItems.CURSED_DOLL.get(), new ItemStat.Builder(750, 27000, 39)
                .addAttribute(ModAttributes.DEFENCE.get(), 4)
                .addAttribute(ModAttributes.MAGIC_DEFENCE.get(), 7)
                .addAttribute(ModAttributes.RES_SEAL.get(), 10)
                .addAttribute(ModAttributes.RES_PARA.get(), 10)
                .addAttribute(ModAttributes.RES_DRAIN.get(), 15)
                .addMonsterStat(ModAttributes.RES_SEAL.get(), 1)
                .addMonsterStat(ModAttributes.RES_PARA.get(), 1)
                .addMonsterStat(ModAttributes.RES_DRAIN.get(), 1)
                .setSpell(ModSpells.CARD_THROW.get(), ModSpells.PLUSH_THROW.get(), ModSpells.FURNITURE.get()));
        this.addStat(ModItems.WARRIORS_PROOF.get(), 0, 0, 0);
        this.addStat(ModItems.PROOF_OF_RANK.get(), 0, 0, 0);
        this.addStat(ModItems.THRONE_OF_EMPIRE.get(), 0, 0, 0);
        this.addStat(ModItems.WHITE_STONE.get(), 0, 0, 0);
        this.addStat(ModItems.RARE_CAN.get(), 0, 0, 0);
        this.addStat(ModItems.CAN.get(), 0, 0, 0);
        this.addStat(ModItems.BOOTS.get(), 0, 0, 0);
        this.addStat(ModItems.LAWN.get(), 0, 0, 0);

        this.addStat(ModItems.FIRE_BALL_SMALL.get(), new ItemStat.Builder(2400, 120, 0));
        this.addStat(ModItems.FIRE_BALL_BIG.get(), new ItemStat.Builder(4800, 240, 0));
        this.addStat(ModItems.EXPLOSION.get(), new ItemStat.Builder(9600, 480, 0));
        this.addStat(ModItems.WATER_LASER.get(), new ItemStat.Builder(2400, 120, 0));
        this.addStat(ModItems.PARALLEL_LASER.get(), new ItemStat.Builder(4800, 240, 0));
        this.addStat(ModItems.DELTA_LASER.get(), new ItemStat.Builder(9600, 480, 0));
        this.addStat(ModItems.SCREW_ROCK.get(), new ItemStat.Builder(2400, 120, 0));
        this.addStat(ModItems.EARTH_SPIKE.get(), new ItemStat.Builder(4800, 240, 0));
        this.addStat(ModItems.AVENGER_ROCK.get(), new ItemStat.Builder(9600, 480, 0));
        this.addStat(ModItems.SONIC_WIND.get(), new ItemStat.Builder(2400, 120, 0));
        this.addStat(ModItems.DOUBLE_SONIC.get(), new ItemStat.Builder(4800, 240, 0));
        this.addStat(ModItems.PENETRATE_SONIC.get(), new ItemStat.Builder(9600, 480, 0));
        this.addStat(ModItems.LIGHT_BARRIER.get(), new ItemStat.Builder(2700, 135, 0));
        this.addStat(ModItems.SHINE.get(), new ItemStat.Builder(5400, 270, 0));
        this.addStat(ModItems.PRISM.get(), new ItemStat.Builder(10800, 540, 0));
        this.addStat(ModItems.DARK_SNAKE.get(), new ItemStat.Builder(2700, 135, 0));
        this.addStat(ModItems.DARK_BALL.get(), new ItemStat.Builder(5400, 270, 0));
        this.addStat(ModItems.DARKNESS.get(), new ItemStat.Builder(10800, 540, 0));
        this.addStat(ModItems.CURE.get(), new ItemStat.Builder(1500, 75, 0));
        this.addStat(ModItems.CURE_ALL.get(), new ItemStat.Builder(4500, 225, 0));
        this.addStat(ModItems.CURE_MASTER.get(), new ItemStat.Builder(12000, 600, 0));
        this.addStat(ModItems.MEDI_POISON.get(), new ItemStat.Builder(1500, 75, 0));
        this.addStat(ModItems.MEDI_PARA.get(), new ItemStat.Builder(3000, 150, 0));
        this.addStat(ModItems.MEDI_SEAL.get(), new ItemStat.Builder(5000, 250, 0));

        this.addStat(ModItems.GREETING.get(), new ItemStat.Builder(500, 25, 0));
        this.addStat(ModItems.POWER_WAVE.get(), new ItemStat.Builder(1000, 50, 0));
        this.addStat(ModItems.DASH_SLASH.get(), new ItemStat.Builder(2000, 100, 0));
        this.addStat(ModItems.RUSH_ATTACK.get(), new ItemStat.Builder(3500, 175, 0));
        this.addStat(ModItems.ROUND_BREAK.get(), new ItemStat.Builder(6500, 325, 0));
        this.addStat(ModItems.MIND_THRUST.get(), new ItemStat.Builder(2500, 125, 0));
        this.addStat(ModItems.GUST.get(), new ItemStat.Builder(3000, 150, 0));
        this.addStat(ModItems.STORM.get(), new ItemStat.Builder(4000, 200, 0));
        this.addStat(ModItems.BLITZ.get(), new ItemStat.Builder(10000, 500, 0));
        this.addStat(ModItems.TWIN_ATTACK.get(), new ItemStat.Builder(1500, 75, 0));
        this.addStat(ModItems.RAIL_STRIKE.get(), new ItemStat.Builder(4700, 235, 0));
        this.addStat(ModItems.WIND_SLASH.get(), new ItemStat.Builder(3500, 175, 0));
        this.addStat(ModItems.FLASH_STRIKE.get(), new ItemStat.Builder(10000, 500, 0));
        this.addStat(ModItems.NAIVE_BLADE.get(), new ItemStat.Builder(6000, 300, 0));
        this.addStat(ModItems.STEEL_HEART.get(), new ItemStat.Builder(4000, 200, 0));
        this.addStat(ModItems.DELTA_STRIKE.get(), new ItemStat.Builder(1500, 75, 0));
        this.addStat(ModItems.HURRICANE.get(), new ItemStat.Builder(2000, 200, 0));
        this.addStat(ModItems.REAPER_SLASH.get(), new ItemStat.Builder(2500, 125, 0));
        this.addStat(ModItems.MILLION_STRIKE.get(), new ItemStat.Builder(9000, 450, 0));
        this.addStat(ModItems.AXEL_DISASTER.get(), new ItemStat.Builder(5000, 250, 0));
        this.addStat(ModItems.STARDUST_UPPER.get(), new ItemStat.Builder(1800, 90, 0));
        this.addStat(ModItems.TORNADO_SWING.get(), new ItemStat.Builder(3500, 175, 0));
        this.addStat(ModItems.GRAND_IMPACT.get(), new ItemStat.Builder(6000, 300, 0));
        this.addStat(ModItems.GIGA_SWING.get(), new ItemStat.Builder(9500, 475, 0));
        this.addStat(ModItems.UPPER_CUT.get(), new ItemStat.Builder(2500, 125, 0));
        this.addStat(ModItems.DOUBLE_KICK.get(), new ItemStat.Builder(8000, 400, 0));
        this.addStat(ModItems.STRAIGHT_PUNCH.get(), new ItemStat.Builder(8500, 425, 0));
        this.addStat(ModItems.NEKO_DAMASHI.get(), new ItemStat.Builder(5500, 275, 0));
        this.addStat(ModItems.RUSH_PUNCH.get(), new ItemStat.Builder(7000, 350, 0));
        this.addStat(ModItems.CYCLONE.get(), new ItemStat.Builder(13000, 650, 0));
        this.addStat(ModItems.RAPID_MOVE.get(), new ItemStat.Builder(4000, 200, 0));
        this.addStat(ModItems.BONUS_CONCERTO.get(), new ItemStat.Builder(3000, 150, 0));
        this.addStat(ModItems.STRIKING_MARCH.get(), new ItemStat.Builder(8500, 425, 0));
        this.addStat(ModItems.IRON_WALTZ.get(), new ItemStat.Builder(8500, 425, 0));
        this.addStat(ModItems.TELEPORT.get(), new ItemStat.Builder(500, 10, 0));

        this.addStat(ModItems.WITHERED_GRASS.get(), 100, 1, 1);
        this.addStat(ModItems.WEEDS.get(), 30, 1, 1);
        this.addStat(ModItems.WHITE_GRASS.get(), 120, 5, 1);
        this.addStat(ModItems.INDIGO_GRASS.get(), 120, 5, 1);
        this.addStat(ModItems.PURPLE_GRASS.get(), 120, 5, 1);
        this.addStat(ModItems.GREEN_GRASS.get(), 120, 5, 1);
        this.addStat(ModItems.BLUE_GRASS.get(), 120, 5, 1);
        this.addStat(ModItems.YELLOW_GRASS.get(), 120, 5, 1);
        this.addStat(ModItems.ORANGE_GRASS.get(), 120, 5, 1);
        this.addStat(ModItems.RED_GRASS.get(), 120, 5, 1);
        this.addStat(ModItems.BLACK_GRASS.get(), 120, 5, 1);
        this.addStat(ModItems.ANTIDOTE_GRASS.get(), new ItemStat.Builder(120, 5, 1)
                .addAttribute(ModAttributes.RES_POISON.get(), 5));
        this.addStat(ModItems.MEDICINAL_HERB.get(), 150, 10, 1);
        this.addStat(ModItems.BAMBOO_SPROUT.get(), 100, 10, 1);
        this.addStat(ModItems.MUSHROOM.get(), 100, 10, 1);
        this.addStat(ModItems.MONARCH_MUSHROOM.get(), 300, 15, 1);
        this.addStat(ModItems.ELLI_LEAVES.get(), 250, 15, 1);

        this.addStat(ModItems.FORGING_BREAD.get(), 600, 100, 1);
        this.addStat(ModItems.COOKING_BREAD.get(), 600, 100, 1);
        this.addStat(ModItems.CHEMISTRY_BREAD.get(), 600, 100, 1);
        this.addStat(ModItems.ARMOR_BREAD.get(), 600, 100, 1);

        this.addStat(ModItems.RICE_FLOUR.get(), 0, 0, 0);
        this.addStat(ModItems.CURRY_POWDER.get(), 0, 0, 0);
        this.addStat(ModItems.OIL.get(), 500, 100, 0);
        this.addStat(ModItems.FLOUR.get(), 0, 0, 0);
        this.addStat(ModItems.YOGURT.get(), 0, 0, 0);
        this.addStat(ModItems.CHEESE.get(), 0, 0, 0);
        this.addStat(ModItems.MAYONNAISE.get(), 0, 0, 0);
        this.addStat(ModItems.EGG_S.get(), 1250, 250, 1);
        this.addStat(ModItems.EGG_M.get(), 1500, 300, 1);
        this.addStat(ModItems.EGG_L.get(), 1700, 400, 1);
        this.addStat(ModItems.MILK_S.get(), 1250, 250, 1);
        this.addStat(ModItems.MILK_M.get(), 1500, 300, 1);
        this.addStat(ModItems.MILK_L.get(), 1700, 400, 1);
        this.addStat(ModItems.WINE.get(), 0, 0, 0);
        this.addStat(ModItems.CHOCOLATE.get(), 0, 0, 0);
        this.addStat(ModItems.RICE.get(), 0, 0, 0);

        this.addStat(ModItems.TURNIP_HEAVEN.get(), 0, 0, 0);
        this.addStat(ModItems.PICKLE_MIX.get(), 0, 0, 0);
        this.addStat(ModItems.SALMON_ONIGIRI.get(), 0, 0, 0);
        this.addStat(ModItems.BREAD.get(), 0, 0, 0);
        this.addStat(ModItems.ONIGIRI.get(), 150, 50, 0);
        this.addStat(ModItems.RELAX_TEA_LEAVES.get(), 0, 0, 0);
        this.addStat(ModItems.ICE_CREAM.get(), 0, 0, 0);
        this.addStat(ModItems.RAISIN_BREAD.get(), 0, 0, 0);
        this.addStat(ModItems.BAMBOO_RICE.get(), 0, 0, 0);
        this.addStat(ModItems.PICKLES.get(), 0, 0, 0);
        this.addStat(ModItems.PICKLED_TURNIP.get(), 300, 45, 0);
        this.addStat(ModItems.FRUIT_SANDWICH.get(), 0, 0, 0);
        this.addStat(ModItems.SANDWICH.get(), 0, 0, 0);
        this.addStat(ModItems.SALAD.get(), 0, 0, 0);

        this.addStat(ModItems.DUMPLINGS.get(), 0, 0, 0);
        this.addStat(ModItems.PUMPKIN_FLAN.get(), 0, 0, 0);
        this.addStat(ModItems.FLAN.get(), 2700, 800, 0);
        this.addStat(ModItems.CHOCOLATE_SPONGE.get(), 0, 0, 0);
        this.addStat(ModItems.POUND_CAKE.get(), 0, 0, 0);
        this.addStat(ModItems.STEAMED_GYOZA.get(), 0, 0, 0);
        this.addStat(ModItems.CURRY_MANJU.get(), 0, 0, 0);
        this.addStat(ModItems.CHINESE_MANJU.get(), 0, 0, 0);
        this.addStat(ModItems.MEAT_DUMPLING.get(), 0, 0, 0);
        this.addStat(ModItems.CHEESE_BREAD.get(), 0, 0, 0);
        this.addStat(ModItems.STEAMED_BREAD.get(), 0, 0, 0);

        this.addStat(ModItems.HOT_JUICE.get(), 0, 0, 0);
        this.addStat(ModItems.PRELUDETO_LOVE.get(), 0, 0, 0);
        this.addStat(ModItems.GOLD_JUICE.get(), 0, 0, 0);
        this.addStat(ModItems.BUTTER.get(), 0, 0, 0);
        this.addStat(ModItems.KETCHUP.get(), 0, 0, 0);
        this.addStat(ModItems.MIXED_SMOOTHIE.get(), 0, 0, 0);
        this.addStat(ModItems.MIXED_JUICE.get(), 0, 0, 0);
        this.addStat(ModItems.VEGGIE_SMOOTHIE.get(), 0, 0, 0);
        this.addStat(ModItems.VEGETABLE_JUICE.get(), 0, 0, 0);
        this.addStat(ModItems.FRUIT_SMOOTHIE.get(), 0, 0, 0);
        this.addStat(ModItems.FRUIT_JUICE.get(), 0, 0, 0);
        this.addStat(ModItems.STRAWBERRY_MILK.get(), 0, 0, 0);
        this.addStat(ModItems.APPLE_JUICE.get(), 0, 0, 0);
        this.addStat(ModItems.ORANGE_JUICE.get(), 0, 0, 0);
        this.addStat(ModItems.GRAPE_JUICE.get(), 1200, 130, 0);
        this.addStat(ModItems.TOMATO_JUICE.get(), 0, 0, 0);
        this.addStat(ModItems.PINEAPPLE_JUICE.get(), 0, 0, 0);

        this.addStat(ModItems.APPLE_PIE.get(), 0, 0, 0);
        this.addStat(ModItems.CHEESECAKE.get(), 0, 0, 0);
        this.addStat(ModItems.CHOCOLATE_CAKE.get(), 0, 0, 0);
        this.addStat(ModItems.CAKE.get(), 0, 0, 0);
        this.addStat(ModItems.CHOCO_COOKIE.get(), 0, 0, 0);
        this.addStat(ModItems.COOKIE.get(), 0, 0, 0);
        this.addStat(ModItems.YAMOFTHE_AGES.get(), 0, 0, 0);
        this.addStat(ModItems.SEAFOOD_GRATIN.get(), 0, 0, 0);
        this.addStat(ModItems.GRATIN.get(), 0, 0, 0);
        this.addStat(ModItems.SEAFOOD_DORIA.get(), 0, 0, 0);
        this.addStat(ModItems.DORIA.get(), 0, 0, 0);
        this.addStat(ModItems.SEAFOOD_PIZZA.get(), 0, 0, 0);
        this.addStat(ModItems.PIZZA.get(), 0, 0, 0);
        this.addStat(ModItems.BUTTER_ROLL.get(), 0, 0, 0);
        this.addStat(ModItems.JAM_ROLL.get(), 0, 0, 0);
        this.addStat(ModItems.TOAST.get(), 0, 0, 0);
        this.addStat(ModItems.SWEET_POTATO.get(), 0, 0, 0);
        this.addStat(ModItems.BAKED_ONIGIRI.get(), 0, 0, 0);
        this.addStat(ModItems.CORN_ON_THE_COB.get(), 0, 0, 0);

        this.addStat(ModItems.ROCKFISH_STEW.get(), 0, 0, 0);
        this.addStat(ModItems.UNION_STEW.get(), 0, 0, 0);
        this.addStat(ModItems.GRILLED_MISO.get(), 0, 0, 0);
        this.addStat(ModItems.RELAX_TEA.get(), 0, 0, 0);
        this.addStat(ModItems.ROYAL_CURRY.get(), 0, 0, 0);
        this.addStat(ModItems.ULTIMATE_CURRY.get(), 0, 0, 0);
        this.addStat(ModItems.CURRY_RICE.get(), 0, 0, 0);
        this.addStat(ModItems.EGG_BOWL.get(), 0, 0, 0);
        this.addStat(ModItems.TEMPURA_BOWL.get(), 0, 0, 0);
        this.addStat(ModItems.MILK_PORRIDGE.get(), 0, 0, 0);
        this.addStat(ModItems.RICE_PORRIDGE.get(), 0, 0, 0);
        this.addStat(ModItems.TEMPURA_UDON.get(), 30000, 3500, 0);
        this.addStat(ModItems.CURRY_UDON.get(), 40000, 5000, 0);
        this.addStat(ModItems.UDON.get(), 860, 140, 0);
        this.addStat(ModItems.CHEESE_FONDUE.get(), 0, 0, 0);
        this.addStat(ModItems.MARMALADE.get(), 0, 0, 0);
        this.addStat(ModItems.GRAPE_JAM.get(), 0, 0, 0);
        this.addStat(ModItems.APPLE_JAM.get(), 0, 0, 0);
        this.addStat(ModItems.STRAWBERRY_JAM.get(), 0, 0, 0);
        this.addStat(ModItems.BOILED_GYOZA.get(), 0, 0, 0);
        this.addStat(ModItems.GLAZED_YAM.get(), 0, 0, 0);
        this.addStat(ModItems.BOILED_EGG.get(), 0, 0, 0);
        this.addStat(ModItems.BOILED_SPINACH.get(), 0, 0, 0);
        this.addStat(ModItems.BOILED_PUMPKIN.get(), 0, 0, 0);
        this.addStat(ModItems.GRAPE_LIQUEUR.get(), 0, 0, 0);
        this.addStat(ModItems.HOT_MILK.get(), 800, 300, 0);
        this.addStat(ModItems.HOT_CHOCOLATE.get(), 1000, 200, 0);

        this.addStat(ModItems.GRILLED_SAND_FLOUNDER.get(), 0, 0, 0);
        this.addStat(ModItems.GRILLED_SHRIMP.get(), 0, 0, 0);
        this.addStat(ModItems.GRILLED_LOBSTER.get(), 0, 0, 0);
        this.addStat(ModItems.GRILLED_BLOWFISH.get(), 0, 0, 0);
        this.addStat(ModItems.GRILLED_LAMP_SQUID.get(), 0, 0, 0);
        this.addStat(ModItems.GRILLED_SUNSQUID.get(), 0, 0, 0);
        this.addStat(ModItems.GRILLED_SQUID.get(), 0, 0, 0);
        this.addStat(ModItems.GRILLED_FALL_FLOUNDER.get(), 0, 0, 0);
        this.addStat(ModItems.GRILLED_TURBOT.get(), 0, 0, 0);
        this.addStat(ModItems.GRILLED_FLOUNDER.get(), 0, 0, 0);
        this.addStat(ModItems.SALTED_PIKE.get(), 0, 0, 0);
        this.addStat(ModItems.GRILLED_NEEDLEFISH.get(), 0, 0, 0);
        this.addStat(ModItems.DRIED_SARDINES.get(), 0, 0, 0);
        this.addStat(ModItems.TUNA_TERIYAKI.get(), 0, 0, 0);
        this.addStat(ModItems.SALTED_POND_SMELT.get(), 0, 0, 0);
        this.addStat(ModItems.GRILLED_YELLOWTAIL.get(), 0, 0, 0);
        this.addStat(ModItems.GRILLED_MACKEREL.get(), 0, 0, 0);
        this.addStat(ModItems.GRILLED_SKIPJACK.get(), 0, 0, 0);
        this.addStat(ModItems.GRILLED_LOVER_SNAPPER.get(), 0, 0, 0);
        this.addStat(ModItems.GRILLED_GLITTER_SNAPPER.get(), 0, 0, 0);
        this.addStat(ModItems.GRILLED_GIRELLA.get(), 0, 0, 0);
        this.addStat(ModItems.GRILLED_SNAPPER.get(), 0, 0, 0);
        this.addStat(ModItems.GRILLED_GIBELIO.get(), 0, 0, 0);
        this.addStat(ModItems.GRILLED_CRUCIAN_CARP.get(), 0, 0, 0);
        this.addStat(ModItems.SALTED_TAIMEN.get(), 0, 0, 0);
        this.addStat(ModItems.SALTED_SALMON.get(), 0, 0, 0);
        this.addStat(ModItems.SALTED_CHUB.get(), 0, 0, 0);
        this.addStat(ModItems.SALTED_CHERRY_SALMON.get(), 0, 0, 0);
        this.addStat(ModItems.SALTED_RAINBOW_TROUT.get(), 0, 0, 0);
        this.addStat(ModItems.SALTED_CHAR.get(), 0, 0, 0);
        this.addStat(ModItems.SALTED_MASU_TROUT.get(), 0, 0, 0);
        this.addStat(ModItems.DRY_CURRY.get(), 0, 0, 0);
        this.addStat(ModItems.RISOTTO.get(), 0, 0, 0);
        this.addStat(ModItems.GYOZA.get(), 0, 0, 0);
        this.addStat(ModItems.PANCAKES.get(), 0, 0, 0);
        this.addStat(ModItems.TEMPURA.get(), 0, 0, 0);
        this.addStat(ModItems.FRIED_UDON.get(), 0, 0, 0);
        this.addStat(ModItems.DONUT.get(), 0, 0, 0);
        this.addStat(ModItems.FRENCH_TOAST.get(), 0, 0, 0);
        this.addStat(ModItems.CURRY_BREAD.get(), 0, 0, 0);
        this.addStat(ModItems.BAKED_APPLE.get(), 1700, 200, 0);
        this.addStat(ModItems.OMELET_RICE.get(), 0, 0, 0);
        this.addStat(ModItems.OMELET.get(), 0, 0, 0);
        this.addStat(ModItems.FRIED_EGGS.get(), 0, 0, 0);
        this.addStat(ModItems.MISO_EGGPLANT.get(), 0, 0, 0);
        this.addStat(ModItems.CORN_CEREAL.get(), 0, 0, 0);
        this.addStat(ModItems.POPCORN.get(), 0, 0, 0);
        this.addStat(ModItems.CROQUETTES.get(), 0, 0, 0);
        this.addStat(ModItems.FRENCH_FRIES.get(), 0, 0, 0);
        this.addStat(ModItems.CABBAGE_CAKES.get(), 0, 0, 0);
        this.addStat(ModItems.FRIED_RICE.get(), 0, 0, 0);
        this.addStat(ModItems.FRIED_VEGGIES.get(), 5500, 1300, 0);

        this.addStat(ModItems.SHRIMP_SASHIMI.get(), 0, 0, 0);
        this.addStat(ModItems.LOBSTER_SASHIMI.get(), 0, 0, 0);
        this.addStat(ModItems.BLOWFISH_SASHIMI.get(), 0, 0, 0);
        this.addStat(ModItems.LAMP_SQUID_SASHIMI.get(), 0, 0, 0);
        this.addStat(ModItems.SUNSQUID_SASHIMI.get(), 0, 0, 0);
        this.addStat(ModItems.SQUID_SASHIMI.get(), 0, 0, 0);
        this.addStat(ModItems.FALL_SASHIMI.get(), 0, 0, 0);
        this.addStat(ModItems.TURBOT_SASHIMI.get(), 0, 0, 0);
        this.addStat(ModItems.FLOUNDER_SASHIMI.get(), 0, 0, 0);
        this.addStat(ModItems.PIKE_SASHIMI.get(), 0, 0, 0);
        this.addStat(ModItems.NEEDLEFISH_SASHIMI.get(), 0, 0, 0);
        this.addStat(ModItems.SARDINE_SASHIMI.get(), 0, 0, 0);
        this.addStat(ModItems.TUNA_SASHIMI.get(), 0, 0, 0);
        this.addStat(ModItems.YELLOWTAIL_SASHIMI.get(), 0, 0, 0);
        this.addStat(ModItems.SKIPJACK_SASHIMI.get(), 0, 0, 0);
        this.addStat(ModItems.GIRELLA_SASHIMI.get(), 0, 0, 0);
        this.addStat(ModItems.LOVER_SASHIMI.get(), 0, 0, 0);
        this.addStat(ModItems.GLITTER_SASHIMI.get(), 0, 0, 0);
        this.addStat(ModItems.SNAPPER_SASHIMI.get(), 0, 0, 0);
        this.addStat(ModItems.TAIMEN_SASHIMI.get(), 0, 0, 0);
        this.addStat(ModItems.CHERRY_SASHIMI.get(), 0, 0, 0);
        this.addStat(ModItems.SALMON_SASHIMI.get(), 0, 0, 0);
        this.addStat(ModItems.RAINBOW_SASHIMI.get(), 0, 0, 0);
        this.addStat(ModItems.CHAR_SASHIMI.get(), 0, 0, 0);
        this.addStat(ModItems.TROUT_SASHIMI.get(), 0, 0, 0);

        this.addStat(ModItems.FAILED_DISH.get(), 100, 2, 1);
        this.addStat(ModItems.DISASTROUS_DISH.get(), 1500, 50, 1);
        this.addStat(ModItems.MIXED_HERBS.get(), 0, 0, 0);
        this.addStat(ModItems.SOUR_DROP.get(), 0, 0, 0);
        this.addStat(ModItems.SWEET_POWDER.get(), 0, 0, 0);
        this.addStat(ModItems.HEAVY_SPICE.get(), 0, 0, 0);
        this.addStat(ModItems.ORANGE.get(), 0, 0, 0);
        this.addStat(ModItems.GRAPES.get(), 500, 100, 1);
        this.addStat(ModItems.MEALY_APPLE.get(), 0, 0, 0);

        this.addStat(ModItems.TURNIP_SEEDS.get(), new ItemStat.Builder(100, 10, 0));
        this.addStat(ModItems.TURNIP_PINK_SEEDS.get(), new ItemStat.Builder(140, 14, 0));
        this.addStat(ModItems.CABBAGE_SEEDS.get(), new ItemStat.Builder(500, 50, 0));
        this.addStat(ModItems.PINK_MELON_SEEDS.get(), new ItemStat.Builder(1000, 100, 0));
        this.addStat(ModItems.HOT_HOT_SEEDS.get(), new ItemStat.Builder(750, 75, 0));
        this.addStat(ModItems.GOLD_TURNIP_SEEDS.get(), new ItemStat.Builder(5000, 500, 0));
        this.addStat(ModItems.GOLD_POTATO_SEEDS.get(), new ItemStat.Builder(3000, 300, 0));
        this.addStat(ModItems.GOLD_PUMPKIN_SEEDS.get(), new ItemStat.Builder(3500, 3500, 0));
        this.addStat(ModItems.GOLD_CABBAGE_SEEDS.get(), new ItemStat.Builder(2500, 250, 0));
        this.addStat(ModItems.BOK_CHOY_SEEDS.get(), new ItemStat.Builder(600, 60, 0));
        this.addStat(ModItems.LEEK_SEEDS.get(), new ItemStat.Builder(450, 45, 0));
        this.addStat(ModItems.RADISH_SEEDS.get(), new ItemStat.Builder(660, 66, 0));
        this.addStat(ModItems.GREEN_PEPPER_SEEDS.get(), new ItemStat.Builder(400, 40, 0));
        this.addStat(ModItems.SPINACH_SEEDS.get(), new ItemStat.Builder(120, 12, 0));
        this.addStat(ModItems.YAM_SEEDS.get(), new ItemStat.Builder(250, 25, 0));
        this.addStat(ModItems.EGGPLANT_SEEDS.get(), new ItemStat.Builder(700, 70, 0));
        this.addStat(ModItems.PINEAPPLE_SEEDS.get(), new ItemStat.Builder(1300, 130, 0));
        this.addStat(ModItems.PUMPKIN_SEEDS.get(), new ItemStat.Builder(800, 80, 0));
        this.addStat(ModItems.ONION_SEEDS.get(), new ItemStat.Builder(900, 90, 0));
        this.addStat(ModItems.CORN_SEEDS.get(), new ItemStat.Builder(830, 83, 0));
        this.addStat(ModItems.TOMATO_SEEDS.get(), new ItemStat.Builder(990, 99, 0));
        this.addStat(ModItems.STRAWBERRY_SEEDS.get(), new ItemStat.Builder(330, 33, 0));
        this.addStat(ModItems.CUCUMBER_SEEDS.get(), new ItemStat.Builder(230, 23, 0));
        this.addStat(ModItems.FODDER_SEEDS.get(), new ItemStat.Builder(50, 5, 0));
        this.addStat(ModItems.SWORD_SEEDS.get(), new ItemStat.Builder(1500, 500, 0));
        this.addStat(ModItems.SHIELD_SEEDS.get(), new ItemStat.Builder(1500, 500, 0));

        this.addStat(ModItems.FODDER.get(), new ItemStat.Builder(250, 35, 0));

        this.addStat(ModItems.TURNIP.get(), new ItemStat.Builder(230, 60, 1));
        this.addStat(ModItems.TURNIP_GIANT.get(), new ItemStat.Builder(0, 250, 20));
        this.addStat(ModItems.TURNIP_PINK.get(), new ItemStat.Builder(320, 130, 5));
        this.addStat(ModItems.TURNIP_PINK_GIANT.get(), new ItemStat.Builder(0, 460, 23));
        this.addStat(ModItems.CABBAGE.get(), new ItemStat.Builder(1200, 330, 14));
        this.addStat(ModItems.CABBAGE_GIANT.get(), new ItemStat.Builder(0, 780, 31));
        this.addStat(ModItems.PINK_MELON.get(), new ItemStat.Builder(3300, 660, 19));
        this.addStat(ModItems.PINK_MELON_GIANT.get(), new ItemStat.Builder(0, 1850, 29));
        this.addStat(ModItems.PINEAPPLE.get(), new ItemStat.Builder(7500, 2360, 53));
        this.addStat(ModItems.PINEAPPLE_GIANT.get(), new ItemStat.Builder(0, 6500, 76));
        this.addStat(ModItems.STRAWBERRY.get(), new ItemStat.Builder(1300, 370, 30));
        this.addStat(ModItems.STRAWBERRY_GIANT.get(), new ItemStat.Builder(0, 900, 45));
        this.addStat(ModItems.GOLDEN_TURNIP.get(), new ItemStat.Builder(50000, 15000, 88));
        this.addStat(ModItems.GOLDEN_TURNIP_GIANT.get(), new ItemStat.Builder(0, 45000, 95));
        this.addStat(ModItems.GOLDEN_POTATO.get(), new ItemStat.Builder(30000, 12500, 86));
        this.addStat(ModItems.GOLDEN_POTATO_GIANT.get(), new ItemStat.Builder(0, 30000, 90));
        this.addStat(ModItems.GOLDEN_PUMPKIN.get(), new ItemStat.Builder(25000, 10000, 87));
        this.addStat(ModItems.GOLDEN_PUMPKIN_GIANT.get(), new ItemStat.Builder(0, 23500, 93));
        this.addStat(ModItems.GOLDEN_CABBAGE.get(), new ItemStat.Builder(18500, 8000, 83));
        this.addStat(ModItems.GOLDEN_CABBAGE_GIANT.get(), new ItemStat.Builder(0, 20000, 89));
        this.addStat(ModItems.HOT_HOT_FRUIT.get(), new ItemStat.Builder(6000, 1000, 67));
        this.addStat(ModItems.HOT_HOT_FRUIT_GIANT.get(), new ItemStat.Builder(0, 2500, 79));
        this.addStat(ModItems.BOK_CHOY.get(), new ItemStat.Builder(1300, 440, 34));
        this.addStat(ModItems.BOK_CHOY_GIANT.get(), new ItemStat.Builder(0, 1100, 50));
        this.addStat(ModItems.LEEK.get(), new ItemStat.Builder(2300, 800, 23));
        this.addStat(ModItems.LEEK_GIANT.get(), new ItemStat.Builder(0, 1950, 37));
        this.addStat(ModItems.RADISH.get(), new ItemStat.Builder(3500, 1550, 40));
        this.addStat(ModItems.RADISH_GIANT.get(), new ItemStat.Builder(0, 4320, 49));
        this.addStat(ModItems.SPINACH.get(), new ItemStat.Builder(450, 120, 17));
        this.addStat(ModItems.SPINACH_GIANT.get(), new ItemStat.Builder(0, 350, 24));
        this.addStat(ModItems.GREEN_PEPPER.get(), new ItemStat.Builder(500, 210, 19));
        this.addStat(ModItems.GREEN_PEPPER_GIANT.get(), new ItemStat.Builder(0, 800, 28));
        this.addStat(ModItems.YAM.get(), new ItemStat.Builder(5500, 220, 20));
        this.addStat(ModItems.YAM_GIANT.get(), new ItemStat.Builder(0, 840, 28));
        this.addStat(ModItems.EGGPLANT.get(), new ItemStat.Builder(970, 310, 23));
        this.addStat(ModItems.EGGPLANT_GIANT.get(), new ItemStat.Builder(0, 850, 38));
        this.addStat(ModItems.TOMATO.get(), new ItemStat.Builder(800, 350, 25));
        this.addStat(ModItems.TOMATO_GIANT.get(), new ItemStat.Builder(0, 1100, 34));
        this.addStat(ModItems.CORN.get(), new ItemStat.Builder(2500, 1000, 37));
        this.addStat(ModItems.CORN_GIANT.get(), new ItemStat.Builder(0, 2800, 56));
        this.addStat(ModItems.CUCUMBER.get(), new ItemStat.Builder(350, 130, 12));
        this.addStat(ModItems.CUCUMBER_GIANT.get(), new ItemStat.Builder(0, 250, 27));
        //this.addStat(ModItems.pumpkin.get(), new ItemStat.MutableItemStat(0, 0, 0));
        //this.addStat(ModItems.pumpkinGiant.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(ModItems.ONION.get(), new ItemStat.Builder(1350, 450, 23));
        this.addStat(ModItems.ONION_GIANT.get(), new ItemStat.Builder(0, 1150, 45));

        this.addStat(Items.CARROT, new ItemStat.Builder(130, 100, 3));
        this.addStat(Items.POTATO, new ItemStat.Builder(130, 100, 3));
        this.addStat(ModItems.POTATO_GIANT.get(), new ItemStat.Builder(0, 450, 19));
        this.addStat(ModItems.CARROT_GIANT.get(), new ItemStat.Builder(0, 450, 19));

        this.addStat(ModItems.TOYHERB_SEEDS.get(), new ItemStat.Builder(80, 8, 0));
        this.addStat(ModItems.MOONDROP_SEEDS.get(), new ItemStat.Builder(190, 19, 0));
        this.addStat(ModItems.PINK_CAT_SEEDS.get(), new ItemStat.Builder(150, 15, 0));
        this.addStat(ModItems.CHARM_BLUE_SEEDS.get(), new ItemStat.Builder(130, 13, 0));
        this.addStat(ModItems.LAMP_GRASS_SEEDS.get(), new ItemStat.Builder(550, 55, 0));
        this.addStat(ModItems.CHERRY_GRASS_SEEDS.get(), new ItemStat.Builder(380, 38, 0));
        this.addStat(ModItems.POM_POM_GRASS_SEEDS.get(), new ItemStat.Builder(450, 45, 0));
        this.addStat(ModItems.AUTUMN_GRASS_SEEDS.get(), new ItemStat.Builder(900, 90, 0));
        this.addStat(ModItems.NOEL_GRASS_SEEDS.get(), new ItemStat.Builder(1700, 170, 0));
        this.addStat(ModItems.FIREFLOWER_SEEDS.get(), new ItemStat.Builder(2380, 238, 0));
        this.addStat(ModItems.FOUR_LEAF_CLOVER_SEEDS.get(), new ItemStat.Builder(770, 77, 0));
        this.addStat(ModItems.IRONLEAF_SEEDS.get(), new ItemStat.Builder(660, 66, 0));
        this.addStat(ModItems.WHITE_CRYSTAL_SEEDS.get(), new ItemStat.Builder(25000, 1500, 0));
        this.addStat(ModItems.RED_CRYSTAL_SEEDS.get(), new ItemStat.Builder(25000, 1500, 0));
        this.addStat(ModItems.GREEN_CRYSTAL_SEEDS.get(), new ItemStat.Builder(25000, 1500, 0));
        this.addStat(ModItems.BLUE_CRYSTAL_SEEDS.get(), new ItemStat.Builder(25000, 1500, 0));
        this.addStat(ModItems.EMERY_FLOWER_SEEDS.get(), new ItemStat.Builder(50000, 2000, 0));

        this.addStat(ModItems.TOYHERB.get(), new ItemStat.Builder(240, 110, 2));
        this.addStat(ModItems.TOYHERB_GIANT.get(), new ItemStat.Builder(0, 300, 12));
        this.addStat(ModItems.MOONDROP_FLOWER.get(), new ItemStat.Builder(380, 160, 5));
        this.addStat(ModItems.MOONDROP_FLOWER_GIANT.get(), new ItemStat.Builder(0, 360, 19));
        this.addStat(ModItems.PINK_CAT.get(), new ItemStat.Builder(450, 190, 6));
        this.addStat(ModItems.PINK_CAT_GIANT.get(), new ItemStat.Builder(0, 400, 17));
        this.addStat(ModItems.CHARM_BLUE.get(), new ItemStat.Builder(500, 210, 9));
        this.addStat(ModItems.CHARM_BLUE_GIANT.get(), new ItemStat.Builder(0, 450, 21));
        this.addStat(ModItems.LAMP_GRASS.get(), new ItemStat.Builder(1450, 660, 16));
        this.addStat(ModItems.LAMP_GRASS_GIANT.get(), new ItemStat.Builder(0, 1300, 34));
        this.addStat(ModItems.CHERRY_GRASS.get(), new ItemStat.Builder(750, 330, 14));
        this.addStat(ModItems.CHERRY_GRASS_GIANT.get(), new ItemStat.Builder(0, 800, 31));
        this.addStat(ModItems.POM_POM_GRASS.get(), new ItemStat.Builder(1500, 550, 19));
        this.addStat(ModItems.POM_POM_GRASS_GIANT.get(), new ItemStat.Builder(0, 1300, 36));
        this.addStat(ModItems.AUTUMN_GRASS.get(), new ItemStat.Builder(3800, 1250, 20));
        this.addStat(ModItems.AUTUMN_GRASS_GIANT.get(), new ItemStat.Builder(0, 2400, 39));
        this.addStat(ModItems.NOEL_GRASS.get(), new ItemStat.Builder(4500, 1550, 28));
        this.addStat(ModItems.NOEL_GRASS_GIANT.get(), new ItemStat.Builder(0, 3000, 42));
        this.addStat(ModItems.FIREFLOWER.get(), new ItemStat.Builder(4600, 1750, 26));
        this.addStat(ModItems.FIREFLOWER_GIANT.get(), new ItemStat.Builder(0, 2800, 48));
        this.addStat(ModItems.FOUR_LEAF_CLOVER.get(), new ItemStat.Builder(3330, 1000, 15));
        this.addStat(ModItems.FOUR_LEAF_CLOVER_GIANT.get(), new ItemStat.Builder(0, 2400, 29));
        this.addStat(ModItems.IRONLEAF.get(), new ItemStat.Builder(1500, 980, 23));
        this.addStat(ModItems.IRONLEAF_GIANT.get(), new ItemStat.Builder(0, 1600, 48));
        this.addStat(ModItems.WHITE_CRYSTAL.get(), new ItemStat.Builder(70000, 23000, 77));
        this.addStat(ModItems.WHITE_CRYSTAL_GIANT.get(), new ItemStat.Builder(0, 85000, 80));
        this.addStat(ModItems.RED_CRYSTAL.get(), new ItemStat.Builder(65000, 20000, 69));
        this.addStat(ModItems.RED_CRYSTAL_GIANT.get(), new ItemStat.Builder(0, 68000, 83));
        this.addStat(ModItems.GREEN_CRYSTAL.get(), new ItemStat.Builder(47500, 16600, 72));
        this.addStat(ModItems.GREEN_CRYSTAL_GIANT.get(), new ItemStat.Builder(0, 59000, 84));
        this.addStat(ModItems.BLUE_CRYSTAL.get(), new ItemStat.Builder(40000, 15000, 74));
        this.addStat(ModItems.BLUE_CRYSTAL_GIANT.get(), new ItemStat.Builder(0, 50000, 86));
        this.addStat(ModItems.EMERY_FLOWER.get(), new ItemStat.Builder(500000, 55000, 80));
        this.addStat(ModItems.EMERY_FLOWER_GIANT.get(), new ItemStat.Builder(0, 150000, 95));

    }

    private static double attackSpeedFor(double delay) {
        return delay - 5;
    }

    private static double attackRangeFor(double range) {
        return range - 3;
    }
}
