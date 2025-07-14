package io.github.flemmli97.runecraftory.neoforge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.api.datapack.provider.ItemStatProvider;
import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryArmorEffects;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ItemStatGen extends ItemStatProvider {

    public ItemStatGen(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
        super(packOutput, RuneCraftory.MODID, provider);
    }

    @Override
    protected void add(HolderLookup.Provider provider) {
        //=====Vanilla stuff
        //Blocks
        this.addStat("stone", ItemTags.STONE_CRAFTING_MATERIALS, new ItemStat.Builder(5, 1, 1)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 0.2));
        this.addStat("sand", ItemTags.SAND, new ItemStat.Builder(5, 1, 1)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 0.2));
        this.addStat("gravel", Items.GRAVEL, new ItemStat.Builder(5, 1, 1)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0.2));
        this.addStat("logs", ItemTags.LOGS, new ItemStat.Builder(10, 2, 3)
                .addAttribute(Attributes.MAX_HEALTH, 3)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 2)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 0.25));
        this.addStat("wool", ItemTags.WOOL, new ItemStat.Builder(25, 3, 5)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 0.3)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0.3));
        this.addStat("obsidian", Items.OBSIDIAN, new ItemStat.Builder(100, 5, 7)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 5)
                .addAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.2)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 0.5));

        //Misc
        this.addStat("sapling", ItemTags.SAPLINGS, new ItemStat.Builder(0, 0, 5)
                .addAttribute(Attributes.MAX_HEALTH, 3)
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat("flowers", ItemTags.FLOWERS, new ItemStat.Builder(0, 0, 3)
                .addAttribute(Attributes.MAX_HEALTH, 2)
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat("candles", ItemTags.CANDLES, new ItemStat.Builder(0, 0, 1)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 0.5));

        this.addStat(Items.STICK, new ItemStat.Builder(11, 1, 1)
                .addAttribute(Attributes.ATTACK_DAMAGE, 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.2));
        this.addStat("arrows", ItemTags.ARROWS, new ItemStat.Builder(45, 2, 2)
                .addAttribute(Attributes.ATTACK_DAMAGE, 2)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setSpell(RuneCraftorySpells.ARROW.asHolder(), null, null));

        this.addStat("coals", ItemTags.COALS, new ItemStat.Builder(65, 5, 2)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0.2));
        this.addStat(Items.SNOWBALL, new ItemStat.Builder(15, 1, 5)
                .addAttribute(Attributes.ATTACK_DAMAGE, 1)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.2)
                .setSpell(RuneCraftorySpells.SNOWBALL.asHolder(), null, null));
        this.addStat(Items.STRING, new ItemStat.Builder(75, 7, 13)
                .addAttribute(Attributes.ATTACK_DAMAGE, 2)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.PARALYSIS.asHolder(), 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5)
                .setSpell(null, RuneCraftorySpells.WEB_SHOT.asHolder(), null));
        this.addStat(Items.FEATHER, new ItemStat.Builder(85, 7, 11)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 2)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 0.5)
                .setSpell(null, null, RuneCraftorySpells.GUST_SPELL.asHolder()));
        this.addStat(Items.ROTTEN_FLESH, new ItemStat.Builder(50, 5, 9)
                .addAttribute(Attributes.MAX_HEALTH, 5)
                .addMonsterStat(Attributes.MAX_HEALTH, 0.5));
        this.addStat(Items.BONE, new ItemStat.Builder(75, 5, 6)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 2)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 0.5));
        this.addStat(Items.GUNPOWDER, new ItemStat.Builder(100, 7, 21)
                .addAttribute(Attributes.ATTACK_DAMAGE, 1)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 5)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(Items.REDSTONE, new ItemStat.Builder(120, 7, 15)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 3)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0.5));
        this.addStat(Items.FLINT, new ItemStat.Builder(103, 9, 8)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(Items.QUARTZ, new ItemStat.Builder(263, 21, 14)
                .addAttribute(Attributes.ATTACK_DAMAGE, 7)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(Items.GLOWSTONE_DUST, new ItemStat.Builder(200, 17, 17)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 4)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 0.5));
        this.addStat(Items.LAPIS_LAZULI, new ItemStat.Builder(54, 4, 13)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 2)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 0.5));
        this.addStat(Items.LEATHER, new ItemStat.Builder(170, 14, 23)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 9)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 1));
        this.addStat(Items.CLAY_BALL, new ItemStat.Builder(100, 9, 19)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0.5));
        this.addStat(Items.BRICK, new ItemStat.Builder(132, 11, 20)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 1));
        this.addStat(Items.PAPER, new ItemStat.Builder(167, 13, 4)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 3)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 0.5));
        this.addStat(Items.BOOK, new ItemStat.Builder(500, 42, 26)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 12)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 0.5));
        this.addStat(Items.PRISMARINE_SHARD, new ItemStat.Builder(389, 34, 17)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 2.5)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 5)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0.5));
        this.addStat(Items.PRISMARINE_CRYSTALS, new ItemStat.Builder(523, 46, 25)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 8)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 0.5));
        this.addStat(Items.BLAZE_ROD, new ItemStat.Builder(350, 23, 25)
                .addAttribute(Attributes.ATTACK_DAMAGE, 12)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setSpell(RuneCraftorySpells.BLAZE_FIREBALLS.asHolder(), null, null));
        this.addStat(Items.ENDER_PEARL, new ItemStat.Builder(400, 28, 21)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.STUN_RESISTANCE.asHolder(), 5)
                .addMonsterStat(RuneCraftoryAttributes.STUN_RESISTANCE.asHolder(), 0.5));
        this.addStat(Items.SLIME_BALL, new ItemStat.Builder(375, 31, 18)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 12)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 0.5));
        this.addStat(Items.MAGMA_CREAM, new ItemStat.Builder(250, 21, 20)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 8)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 0.5));
        this.addStat(Items.GHAST_TEAR, new ItemStat.Builder(750, 49, 22)
                .addAttribute(Attributes.MAX_HEALTH, 15)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 3)
                .addMonsterStat(Attributes.MAX_HEALTH, 2)
                .setSpell(RuneCraftorySpells.GHAST_FIREBALL.asHolder(), null, null));
        this.addStat(Items.PHANTOM_MEMBRANE, new ItemStat.Builder(600, 55, 19)
                .addAttribute(Attributes.MOVEMENT_SPEED, 0.03)
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(Items.SUGAR, new ItemStat.Builder(25, 13, 18)
                .addAttribute(Attributes.MOVEMENT_SPEED, 0.01)
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(Items.TOTEM_OF_UNDYING, new ItemStat.Builder(3500, 110, 17)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 9)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 9)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1)
                .setSpell(null, RuneCraftorySpells.EVOKER_FANG.asHolder(), null));
        this.addStat(Items.DRAGON_BREATH, new ItemStat.Builder(2000, 150, 24)
                .addAttribute(Attributes.ATTACK_DAMAGE, 4)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 15)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 1)
                .setSpell(null, RuneCraftorySpells.DRAGON_FIREBALL.asHolder(), null));
        this.addStat(Items.SHULKER_SHELL, new ItemStat.Builder(700, 80, 29)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 17)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 12)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 1)
                .setSpell(RuneCraftorySpells.SHULKER_BULLET.asHolder(), null, null));
        this.addStat(Items.WITHER_SKELETON_SKULL, new ItemStat.Builder(5000, 170, 29)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 20)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 1)
                .setSpell(RuneCraftorySpells.WITHER_SKULL.asHolder(), null, null)
                .setElement(ItemElement.DARK));
        this.addStat(Items.NETHER_STAR, new ItemStat.Builder(20000, 600, 45)
                .addAttribute(Attributes.ATTACK_DAMAGE, 17)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 7)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 35)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 9)
                .addAttribute(RuneCraftoryAttributes.DRAIN.asHolder(), 5)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 3)
                .setSpell(RuneCraftorySpells.WITHER_SKULL.asHolder(), null, null)
                .setElement(ItemElement.DARK));

        //=======
        this.addStat(RuneCraftoryItems.ROUNDOFF.get(), new ItemStat.Builder(750, 50, 0)
                .addMonsterStat(Attributes.MAX_HEALTH, 3)
                .addMonsterStat(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.PARA_GONE.get(), new ItemStat.Builder(750, 50, 0)
                .addMonsterStat(Attributes.MAX_HEALTH, 3)
                .addMonsterStat(RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.COLD_MED.get(), new ItemStat.Builder(750, 50, 0)
                .addMonsterStat(Attributes.MAX_HEALTH, 3)
                .addMonsterStat(RuneCraftoryAttributes.COLD_RESISTANCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.ANTIDOTE.get(), new ItemStat.Builder(750, 50, 0)
                .addMonsterStat(Attributes.MAX_HEALTH, 3)
                .addMonsterStat(RuneCraftoryAttributes.POISON_RESISTANCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.RECOVERY_POTION.get(), new ItemStat.Builder(300, 25, 0)
                .addMonsterStat(Attributes.MAX_HEALTH, 3));
        this.addStat(RuneCraftoryItems.HEALING_POTION.get(), new ItemStat.Builder(500, 35, 0)
                .addMonsterStat(Attributes.MAX_HEALTH, 3));
        this.addStat(RuneCraftoryItems.MYSTERY_POTION.get(), new ItemStat.Builder(3000, 250, 0)
                .addMonsterStat(Attributes.MAX_HEALTH, 5));
        this.addStat(RuneCraftoryItems.MAGICAL_POTION.get(), new ItemStat.Builder(6000, 500, 0)
                .addMonsterStat(Attributes.MAX_HEALTH, 5));
        this.addStat(RuneCraftoryItems.INVINCIROID.get(), new ItemStat.Builder(25000, 1500, 0));
        this.addStat(RuneCraftoryItems.LOVE_POTION.get(), new ItemStat.Builder(50000, 2000, 0));
        this.addStat(RuneCraftoryItems.FORMUADE.get(), new ItemStat.Builder(20000, 700, 0));
        this.addStat(RuneCraftoryItems.OBJECT_X.get(), new ItemStat.Builder(6000, 500, 15));

        this.addStat(RuneCraftoryItems.BROAD_SWORD.get(), new ItemStat.Builder(100, 16, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 4)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(RuneCraftoryItems.STEEL_SWORD.get(), new ItemStat.Builder(1320, 54, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 8)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(RuneCraftoryItems.STEEL_SWORD_PLUS.get(), new ItemStat.Builder(2310, 99, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 15)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(RuneCraftoryItems.CUTLASS.get(), new ItemStat.Builder(4240, 210, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 23)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.AQUA_SWORD.get(), new ItemStat.Builder(6850, 357, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 37)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 9)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setElement(ItemElement.WATER));
        this.addStat(RuneCraftoryItems.INVISI_BLADE.get(), new ItemStat.Builder(9350, 571, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 49)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 6)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.DEFENDER.get(), new ItemStat.Builder(11830, 843, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 65)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 9)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.BURNING_SWORD.get(), new ItemStat.Builder(13440, 1290, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 79)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setElement(ItemElement.FIRE));
        this.addStat(RuneCraftoryItems.GORGEOUS_SWORD.get(), new ItemStat.Builder(16620, 1630, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 82)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.GAIA_SWORD.get(), new ItemStat.Builder(19260, 2120, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 95)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 7)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 17)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setElement(ItemElement.EARTH));
        this.addStat(RuneCraftoryItems.SNAKE_SWORD.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.LUCK_BLADE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.PLATINUM_SWORD.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.WIND_SWORD.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.CHAOS_BLADE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.SAKURA.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.SUNSPOT.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.DURENDAL.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.AERIAL_BLADE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.GRANTALE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.SMASH_BLADE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.ICIFIER.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.SOUL_EATER.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.RAVENTINE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.STAR_SABER.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.PLATINUM_SWORD_PLUS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.DRAGON_SLAYER.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.RUNE_BLADE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.GLADIUS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.RUNE_LEGEND.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.BACK_SCRATCHER.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.SPOON.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.VEGGIE_BLADE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.PLANT_SWORD.get(), new ItemStat.Builder(2000, 700, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5));

        this.addStat(RuneCraftoryItems.CLAYMORE.get(), new ItemStat.Builder(210, 17, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 6)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(RuneCraftoryItems.ZWEIHAENDER.get(), new ItemStat.Builder(1360, 58, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 11)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(RuneCraftoryItems.ZWEIHAENDER_PLUS.get(), new ItemStat.Builder(2170, 104, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 19)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.STUN.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(RuneCraftoryItems.GREAT_SWORD.get(), new ItemStat.Builder(3960, 231, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 29)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.SEA_CUTTER.get(), new ItemStat.Builder(7170, 404, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 42)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setElement(ItemElement.WATER));
        this.addStat(RuneCraftoryItems.CYCLONE_BLADE.get(), new ItemStat.Builder(10680, 623, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 55)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setElement(ItemElement.WIND));
        this.addStat(RuneCraftoryItems.POISON_BLADE.get(), new ItemStat.Builder(13450, 837, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 70)
                .addAttribute(RuneCraftoryAttributes.POISON.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.KATZBALGER.get(), new ItemStat.Builder(16920, 1030, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 87)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.EARTH_SHADE.get(), new ItemStat.Builder(19360, 1250, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 99)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setElement(ItemElement.EARTH));
        this.addStat(RuneCraftoryItems.BIG_KNIFE.get(), new ItemStat.Builder(22740, 1820, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 118)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.KATANA.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.FLAME_SABER.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.BIO_SMASHER.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.SNOW_CROWN.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.DANCING_DICER.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.FLAMBERGE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.FLAMBERGE_PLUS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.VOLCANON.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.PSYCHO.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.SHINE_BLADE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.GRAND_SMASHER.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.BELZEBUTH.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.OROCHI.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.PUNISHER.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.STEEL_SLICER.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.MOON_SHADOW.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.BLUE_EYED_BLADE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.BALMUNG.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.BRAVEHEART.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.FORCE_ELEMENT.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.HEAVENS_ASUNDER.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.CALIBURN.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.DEKASH.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.DAICONE.get(), new ItemStat.Builder(0, 0, 0));

        this.addStat(RuneCraftoryItems.SPEAR.get(), new ItemStat.Builder(190, 13, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 6)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(5.3))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(RuneCraftoryItems.WOOD_STAFF.get(), new ItemStat.Builder(1070, 56, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 8)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 6)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(5.3))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(RuneCraftoryItems.LANCE.get(), new ItemStat.Builder(1810, 101, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 13)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 1)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 6)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(5.3))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(RuneCraftoryItems.LANCE_PLUS.get(), new ItemStat.Builder(3460, 198, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 21)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 6)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(5.3))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(RuneCraftoryItems.NEEDLE_SPEAR.get(), new ItemStat.Builder(5770, 333, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 35)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 4)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 6)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(5.3))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.TRIDENT.get(), new ItemStat.Builder(9280, 543, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 50)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 8)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 6)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(5.3))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setElement(ItemElement.WATER));
        this.addStat(RuneCraftoryItems.WATER_SPEAR.get(), new ItemStat.Builder(14730, 934, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 68)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 14)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 6)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(5.3))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setElement(ItemElement.WATER));
        this.addStat(RuneCraftoryItems.HALBERD.get(), new ItemStat.Builder(18360, 1340, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 82)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 6)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(5.3))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.CORSESCA.get(), new ItemStat.Builder(20630, 1830, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 98)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 6)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(5.3))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.CORSESCA_PLUS.get(), new ItemStat.Builder(22130, 2010, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 109)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 6)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(5.3))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.POISON_SPEAR.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.FIVE_STAFF.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.HEAVY_LANCE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.FEATHER_LANCE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.ICEBERG.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.BLOOD_LANCE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.MAGICAL_LANCE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.FLARE_LANCE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.BRIONAC.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.POISON_QUEEN.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.MONK_STAFF.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.METUS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.SILENT_GRAVE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.OVERBREAK.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.BJOR.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.BELVAROSE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.GAE_BOLG.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.DRAGONS_FANG.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.GUNGNIR.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.LEGION.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.PITCHFORK.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.SAFETY_LANCE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.PINE_CLUB.get(), new ItemStat.Builder(0, 0, 0));

        this.addStat(RuneCraftoryItems.BATTLE_AXE.get(), new ItemStat.Builder(250, 19, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 9)
                .addAttribute(RuneCraftoryAttributes.CRITICAL.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(RuneCraftoryItems.BATTLE_SCYTHE.get(), new ItemStat.Builder(1430, 60, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 14)
                .addAttribute(RuneCraftoryAttributes.CRITICAL.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(RuneCraftoryItems.POLE_AXE.get(), new ItemStat.Builder(3250, 147, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 25)
                .addAttribute(RuneCraftoryAttributes.CRITICAL.asHolder(), 7)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(RuneCraftoryItems.POLE_AXE_PLUS.get(), new ItemStat.Builder(5430, 245, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 39)
                .addAttribute(RuneCraftoryAttributes.CRITICAL.asHolder(), 7)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.GREAT_AXE.get(), new ItemStat.Builder(8580, 417, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 54)
                .addAttribute(RuneCraftoryAttributes.CRITICAL.asHolder(), 11)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.TOMAHAWK.get(), new ItemStat.Builder(11360, 683, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 70)
                .addAttribute(RuneCraftoryAttributes.CRITICAL.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setElement(ItemElement.WIND));
        this.addStat(RuneCraftoryItems.BASILISK_FANG.get(), new ItemStat.Builder(14280, 1220, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 105)
                .addAttribute(RuneCraftoryAttributes.POISON.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.CRITICAL.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.ROCK_AXE.get(), new ItemStat.Builder(20280, 2420, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 128)
                .addAttribute(RuneCraftoryAttributes.CRITICAL.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setElement(ItemElement.EARTH));
        this.addStat(RuneCraftoryItems.DEMON_AXE.get(), new ItemStat.Builder(26240, 3180, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 140)
                .addAttribute(RuneCraftoryAttributes.CRITICAL.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setElement(ItemElement.DARK));
        this.addStat(RuneCraftoryItems.FROST_AXE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.CRESCENT_AXE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.CRESCENT_AXE_PLUS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.HEAT_AXE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.DOUBLE_EDGE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.ALLDALE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.DEVIL_FINGER.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.EXECUTIONER.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.SAINT_AXE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.AXE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.LOLLIPOP.get(), new ItemStat.Builder(0, 0, 0));

        this.addStat(RuneCraftoryItems.BATTLE_HAMMER.get(), new ItemStat.Builder(245, 18, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 10)
                .addAttribute(RuneCraftoryAttributes.STUN.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 20)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(RuneCraftoryItems.BAT.get(), new ItemStat.Builder(1240, 54, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 13)
                .addAttribute(RuneCraftoryAttributes.STUN.asHolder(), 6)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 20)
                .addAttribute(Attributes.ATTACK_KNOCKBACK, 1)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(RuneCraftoryItems.WAR_HAMMER.get(), new ItemStat.Builder(2960, 138, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 20)
                .addAttribute(RuneCraftoryAttributes.STUN.asHolder(), 6)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 20)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(RuneCraftoryItems.WAR_HAMMER_PLUS.get(), new ItemStat.Builder(6340, 265, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 31)
                .addAttribute(RuneCraftoryAttributes.STUN.asHolder(), 7)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 20)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.IRON_BAT.get(), new ItemStat.Builder(9350, 421, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 44)
                .addAttribute(RuneCraftoryAttributes.STUN.asHolder(), 9)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 20)
                .addAttribute(Attributes.ATTACK_KNOCKBACK, 1)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.GREAT_HAMMER.get(), new ItemStat.Builder(12740, 658, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 63)
                .addAttribute(RuneCraftoryAttributes.STUN.asHolder(), 11)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 20)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.ICE_HAMMER.get(), new ItemStat.Builder(15930, 910, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 88)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.STUN.asHolder(), 11)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 20)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setElement(ItemElement.WATER));
        this.addStat(RuneCraftoryItems.BONE_HAMMER.get(), new ItemStat.Builder(19370, 1240, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 109)
                .addAttribute(RuneCraftoryAttributes.STUN.asHolder(), 11)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 20)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setElement(ItemElement.DARK));
        this.addStat(RuneCraftoryItems.STRONG_STONE.get(), new ItemStat.Builder(22430, 1930, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 136)
                .addAttribute(RuneCraftoryAttributes.STUN.asHolder(), 11)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 30)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(4.5))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.FLAME_HAMMER.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.GIGANT_HAMMER.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.SKY_HAMMER.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.GRAVITON_HAMMER.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.SPIKED_HAMMER.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.CRYSTAL_HAMMER.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.SCHNABEL.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.GIGANT_HAMMER_PLUS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.KONGO.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.MJOLNIR.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.FATAL_CRUSH.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.SPLASH_STAR.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.HAMMER.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.TOY_HAMMER.get(), new ItemStat.Builder(0, 0, 0));

        this.addStat(RuneCraftoryItems.SHORT_DAGGER.get(), new ItemStat.Builder(230, 12, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 1)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0f))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(RuneCraftoryItems.STEEL_EDGE.get(), new ItemStat.Builder(950, 44, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 7)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0f))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(RuneCraftoryItems.FROST_EDGE.get(), new ItemStat.Builder(2610, 121, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 12)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 4)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0f))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5)
                .setElement(ItemElement.WATER));
        this.addStat(RuneCraftoryItems.IRON_EDGE.get(), new ItemStat.Builder(4910, 230, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 18)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 8)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0f))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.THIEF_KNIFE.get(), new ItemStat.Builder(7940, 384, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 28)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 11)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0f))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.WIND_EDGE.get(), new ItemStat.Builder(9600, 568, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 44)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 12)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 9)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0f))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setElement(ItemElement.WIND));
        this.addStat(RuneCraftoryItems.GORGEOUS_LX.get(), new ItemStat.Builder(13500, 836, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 56)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 16)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0f))
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.STEEL_KATANA.get(), new ItemStat.Builder(16400, 1320, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 71)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 21)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0f))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.TWIN_BLADE.get(), new ItemStat.Builder(19430, 1830, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 86)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 26)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0f))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.RAMPAGE.get(), new ItemStat.Builder(21640, 2760, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 97)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 31)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0f))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1.5f)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.SALAMANDER.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.PLATINUM_EDGE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.SONIC_DAGGER.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.CHAOS_EDGE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.DESERT_WIND.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.BROKEN_WALL.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.FORCE_DIVIDE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.HEART_FIRE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.ORCUS_SWORD.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.DEEP_BLIZZARD.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.DARK_INVITATION.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.PRIEST_SABER.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.EFREET.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.DRAGOON_CLAW.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.EMERALD_EDGE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.RUNE_EDGE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.EARNEST_EDGE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.TWIN_JUSTICE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.DOUBLE_SCRATCH.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.ACUTORIMASS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.TWIN_LEEKS.get(), new ItemStat.Builder(0, 0, 0));

        this.addStat(RuneCraftoryItems.LEATHER_GLOVE.get(), new ItemStat.Builder(190, 13, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0f))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(RuneCraftoryItems.BRASS_KNUCKLES.get(), new ItemStat.Builder(1580, 74, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 9)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 1)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0f))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(RuneCraftoryItems.KOTE.get(), new ItemStat.Builder(3170, 136, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 16)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0f))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.5));
        this.addStat(RuneCraftoryItems.GLOVES.get(), new ItemStat.Builder(5480, 238, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 22)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 4)
                .addAttribute(Attributes.MAX_HEALTH, 15)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0f))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.BEAR_CLAWS.get(), new ItemStat.Builder(8140, 394, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 34)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 9)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0f))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.FIST_EARTH.get(), new ItemStat.Builder(12640, 587, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 49)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 14)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0f))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setElement(ItemElement.EARTH));
        this.addStat(RuneCraftoryItems.FIST_FIRE.get(), new ItemStat.Builder(14720, 794, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 63)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 18)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 18)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0f))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setElement(ItemElement.FIRE));
        this.addStat(RuneCraftoryItems.FIST_WATER.get(), new ItemStat.Builder(16380, 931, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 79)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 21)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 20)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0f))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setElement(ItemElement.WATER));
        this.addStat(RuneCraftoryItems.DRAGON_CLAWS.get(), new ItemStat.Builder(19270, 1520, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 91)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 35)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0f))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.FIST_DARK.get(), new ItemStat.Builder(23930, 1960, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 101)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 24)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 24)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0f))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setElement(ItemElement.DARK));
        this.addStat(RuneCraftoryItems.FIST_WIND.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.FIST_LIGHT.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.CAT_PUNCH.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.ANIMAL_PUPPETS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.IRONLEAF_FISTS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.CAESTUS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.GOLEM_PUNCH.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.GOD_HAND.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.BAZAL_KATAR.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.FENRIR.get(), new ItemStat.Builder(0, 0, 0));

        this.addStat(RuneCraftoryItems.ROD.get(), new ItemStat.Builder(281, 32, 0)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 1.5f)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 0.5f)
                .setElement(ItemElement.FIRE)
                .setSpell(RuneCraftorySpells.FIREBALL.asHolder(), null, null));
        this.addStat(RuneCraftoryItems.AMETHYST_ROD.get(), new ItemStat.Builder(1550, 76, 0)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 13)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 0.5f)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 0.5)
                .setElement(ItemElement.EARTH));
        this.addStat(RuneCraftoryItems.AQUAMARINE_ROD.get(), new ItemStat.Builder(3430, 186, 0)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 17)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 0.5f)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 0.5)
                .setElement(ItemElement.WATER));
        this.addStat(RuneCraftoryItems.FRIENDLY_ROD.get(), new ItemStat.Builder(6670, 297, 0)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 28)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 0.5f)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 1)
                .setElement(ItemElement.LOVE));
        this.addStat(RuneCraftoryItems.LOVE_LOVE_ROD.get(), new ItemStat.Builder(8550, 436, 0)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 41)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 0.5f)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 1)
                .setElement(ItemElement.LOVE));
        this.addStat(RuneCraftoryItems.STAFF.get(), new ItemStat.Builder(11110, 599, 0)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 65)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 0.5f)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 1)
                .setElement(ItemElement.EARTH));
        this.addStat(RuneCraftoryItems.EMERALD_ROD.get(), new ItemStat.Builder(12430, 705, 0)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 77)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 0.5f)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 0.5)
                .setElement(ItemElement.WIND));
        this.addStat(RuneCraftoryItems.SILVER_STAFF.get(), new ItemStat.Builder(14600, 917, 0)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 98)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 0.5f)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 1)
                .setElement(ItemElement.DARK));
        this.addStat(RuneCraftoryItems.FLARE_STAFF.get(), new ItemStat.Builder(18000, 1330, 0)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 112)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 0.5f)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 1)
                .setElement(ItemElement.FIRE));
        this.addStat(RuneCraftoryItems.RUBY_ROD.get(), new ItemStat.Builder(21530, 1940, 0)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 127)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 0.5f)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 1)
                .setElement(ItemElement.FIRE));
        this.addStat(RuneCraftoryItems.SAPPHIRE_ROD.get(), new ItemStat.Builder(24620, 2350, 0)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 149)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3.0))
                .addAttribute(RuneCraftoryAttributes.ATTACK_WIDTH.asHolder(), 0.5f)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 1)
                .setElement(ItemElement.LIGHT));
        this.addStat(RuneCraftoryItems.EARTH_STAFF.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.LIGHTNING_WAND.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.ICE_STAFF.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.DIAMOND_ROD.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.WIZARDS_STAFF.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.MAGES_STAFF.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.SHOOTING_STAR_STAFF.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.HELL_BRANCH.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.CRIMSON_STAFF.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.BUBBLE_STAFF.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.GAIA_ROD.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.CYCLONE_ROD.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.STORM_WAND.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.RUNE_STAFF.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.MAGES_STAFF_PLUS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.MAGIC_BROOM.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.MAGIC_SHOT.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.HELL_CURSE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.ALGERNON.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.SORCERES_WAND.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.BASKET.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.GOLDEN_TURNIP_STAFF.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.SWEET_POTATO_STAFF.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.ELVISH_HARP.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.SYRINGE.get(), new ItemStat.Builder(0, 0, 0));

        this.addStat(RuneCraftoryItems.HOE_SCRAP.get(), new ItemStat.Builder(150, 34, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 1)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(RuneCraftoryItems.HOE_IRON.get(), new ItemStat.Builder(4500, 121, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 15)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(RuneCraftoryItems.HOE_SILVER.get(), new ItemStat.Builder(25000, 300, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 34)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(ItemElement.EARTH));
        this.addStat(RuneCraftoryItems.HOE_GOLD.get(), new ItemStat.Builder(0, 550, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 76)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(ItemElement.EARTH));
        this.addStat(RuneCraftoryItems.HOE_PLATINUM.get(), new ItemStat.Builder(0, 2000, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 111)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 45)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(ItemElement.EARTH));

        this.addStat(RuneCraftoryItems.WATERING_CAN_SCRAP.get(), new ItemStat.Builder(150, 45, 0)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 1)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(ItemElement.WATER));
        this.addStat(RuneCraftoryItems.WATERING_CAN_IRON.get(), new ItemStat.Builder(4500, 164, 0)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 7)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(ItemElement.WATER));
        this.addStat(RuneCraftoryItems.WATERING_CAN_SILVER.get(), new ItemStat.Builder(25000, 300, 0)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 19)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(ItemElement.WATER));
        this.addStat(RuneCraftoryItems.WATERING_CAN_GOLD.get(), new ItemStat.Builder(0, 550, 0)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 39)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(ItemElement.WATER));
        this.addStat(RuneCraftoryItems.WATERING_CAN_PLATINUM.get(), new ItemStat.Builder(0, 2000, 0)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 99)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(ItemElement.WATER));

        this.addStat(RuneCraftoryItems.SICKLE_SCRAP.get(), new ItemStat.Builder(150, 24, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 2)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(RuneCraftoryItems.SICKLE_IRON.get(), new ItemStat.Builder(4500, 118, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 19)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(RuneCraftoryItems.SICKLE_SILVER.get(), new ItemStat.Builder(25000, 300, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 36)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(RuneCraftoryItems.SICKLE_GOLD.get(), new ItemStat.Builder(0, 550, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 79)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(ItemElement.WIND));
        this.addStat(RuneCraftoryItems.SICKLE_PLATINUM.get(), new ItemStat.Builder(0, 2000, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 134)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 31)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1)
                .setElement(ItemElement.WIND));

        this.addStat(RuneCraftoryItems.AXE_SCRAP.get(), new ItemStat.Builder(150, 37, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addAttribute(RuneCraftoryAttributes.CRITICAL.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(RuneCraftoryItems.AXE_IRON.get(), new ItemStat.Builder(4500, 148, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 22)
                .addAttribute(RuneCraftoryAttributes.CRITICAL.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(RuneCraftoryItems.AXE_SILVER.get(), new ItemStat.Builder(25000, 300, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 39)
                .addAttribute(RuneCraftoryAttributes.CRITICAL.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(RuneCraftoryItems.AXE_GOLD.get(), new ItemStat.Builder(0, 550, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 83)
                .addAttribute(RuneCraftoryAttributes.CRITICAL.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(RuneCraftoryItems.AXE_PLATINUM.get(), new ItemStat.Builder(0, 2000, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 140)
                .addAttribute(RuneCraftoryAttributes.CRITICAL.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));

        this.addStat(RuneCraftoryItems.HAMMER_SCRAP.get(), new ItemStat.Builder(150, 39, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addAttribute(RuneCraftoryAttributes.CRITICAL.asHolder(), -5)
                .addAttribute(RuneCraftoryAttributes.STUN.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(RuneCraftoryItems.HAMMER_IRON.get(), new ItemStat.Builder(4500, 142, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 23)
                .addAttribute(RuneCraftoryAttributes.CRITICAL.asHolder(), -5)
                .addAttribute(RuneCraftoryAttributes.STUN.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(RuneCraftoryItems.HAMMER_SILVER.get(), new ItemStat.Builder(25000, 300, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 47)
                .addAttribute(RuneCraftoryAttributes.CRITICAL.asHolder(), -5)
                .addAttribute(RuneCraftoryAttributes.STUN.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(RuneCraftoryItems.HAMMER_GOLD.get(), new ItemStat.Builder(0, 550, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 85)
                .addAttribute(RuneCraftoryAttributes.CRITICAL.asHolder(), -5)
                .addAttribute(RuneCraftoryAttributes.STUN.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(RuneCraftoryItems.HAMMER_PLATINUM.get(), new ItemStat.Builder(0, 2000, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 145)
                .addAttribute(RuneCraftoryAttributes.CRITICAL.asHolder(), -7)
                .addAttribute(RuneCraftoryAttributes.STUN.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));

        this.addStat(RuneCraftoryItems.FISHING_ROD_SCRAP.get(), new ItemStat.Builder(150, 35, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 1)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(RuneCraftoryItems.FISHING_ROD_IRON.get(), new ItemStat.Builder(4500, 135, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 9)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 14)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(RuneCraftoryItems.FISHING_ROD_SILVER.get(), new ItemStat.Builder(25000, 300, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 26)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 27)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(RuneCraftoryItems.FISHING_ROD_GOLD.get(), new ItemStat.Builder(0, 550, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 66)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 72)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));
        this.addStat(RuneCraftoryItems.FISHING_ROD_PLATINUM.get(), new ItemStat.Builder(0, 2000, 0)
                .addAttribute(Attributes.ATTACK_DAMAGE, 89)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 98)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), attackRangeFor(3))
                .addMonsterStat(Attributes.MAX_HEALTH, 1));

        this.addStat(RuneCraftoryItems.MOB_STAFF.get(), new ItemStat.Builder(1500, 300, 0));
        this.addStat(RuneCraftoryItems.BRUSH.get(), new ItemStat.Builder(1500, 300, 0));
        this.addStat(RuneCraftoryItems.GLASS.get(), new ItemStat.Builder(2000, 400, 1));

        this.addStat(RuneCraftoryItems.LEVELISER.get(), new ItemStat.Builder(2000000, 3000, 0));
        this.addStat(RuneCraftoryItems.HEART_DRINK.get(), new ItemStat.Builder(1500000, 1500, 0));
        this.addStat(RuneCraftoryItems.VITAL_GUMMI.get(), new ItemStat.Builder(1500000, 1500, 0));
        this.addStat(RuneCraftoryItems.INTELLIGENCER.get(), new ItemStat.Builder(1500000, 1500, 0));
        this.addStat(RuneCraftoryItems.PROTEIN.get(), new ItemStat.Builder(1500000, 1500, 0));
        this.addStat(RuneCraftoryItems.FORMULAR_A.get(), new ItemStat.Builder(1000, 150, 0));
        this.addStat(RuneCraftoryItems.FORMULAR_B.get(), new ItemStat.Builder(2500, 250, 0));
        this.addStat(RuneCraftoryItems.FORMULAR_C.get(), new ItemStat.Builder(5000, 400, 0));
        this.addStat(RuneCraftoryItems.MINIMIZER.get(), new ItemStat.Builder(10000, 300, 0));
        this.addStat(RuneCraftoryItems.GIANTIZER.get(), new ItemStat.Builder(10000, 300, 0));
        this.addStat(RuneCraftoryItems.GREENIFIER.get(), new ItemStat.Builder(2000, 200, 0));
        this.addStat(RuneCraftoryItems.GREENIFIER_PLUS.get(), new ItemStat.Builder(10000, 500, 0));
        this.addStat(RuneCraftoryItems.WETTABLE_POWDER.get(), new ItemStat.Builder(1500, 150, 0));

        this.addStat(RuneCraftoryItems.CHEAP_BRACELET.get(), new ItemStat.Builder(120, 21, 0)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.BRONZE_BRACELET.get(), new ItemStat.Builder(850, 38, 0)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 4)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.SILVER_BRACELET.get(), new ItemStat.Builder(3000, 300, 0)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 10)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.GOLD_BRACELET.get(), new ItemStat.Builder(15000, 750, 0)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 25)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.PLATINUM_BRACELET.get(), new ItemStat.Builder(50000, 1000, 0)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 50)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1));
        this.addStat(RuneCraftoryItems.SILVER_RING.get(), new ItemStat.Builder(20000, 600, 0)
                .addAttribute(RuneCraftoryAttributes.LIGHT_RESISTANCE.asHolder(), 50)
                .addAttribute(RuneCraftoryAttributes.DARK_RESISTANCE.asHolder(), 50)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1));
        this.addStat(RuneCraftoryItems.GOLD_RING.get(), new ItemStat.Builder(0, 5000, 0)
                .addAttribute(RuneCraftoryAttributes.WIND_RESISTANCE.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.WATER_RESISTANCE.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), 15)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2));
        this.addStat(RuneCraftoryItems.PLATINUM_RING.get(), new ItemStat.Builder(0, 7500, 0)
                .addAttribute(RuneCraftoryAttributes.WIND_RESISTANCE.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.WATER_RESISTANCE.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.LIGHT_RESISTANCE.asHolder(), 25)
                .addAttribute(RuneCraftoryAttributes.DARK_RESISTANCE.asHolder(), 25)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2));
        this.addStat(RuneCraftoryItems.SHIELD_RING.get(), new ItemStat.Builder(30000, 1000, 0)
                .withArmorEffect(RuneCraftoryArmorEffects.SHIELD_RING.asHolder())
                .addAttribute(Attributes.MAX_HEALTH, 50)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 25)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 20));
        this.addStat(RuneCraftoryItems.CRITICAL_RING.get(), new ItemStat.Builder(25000, 0, 0)
                .addAttribute(RuneCraftoryAttributes.CRITICAL.asHolder(), 20)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 5));
        this.addStat(RuneCraftoryItems.SILENT_RING.get(), new ItemStat.Builder(15000, 750, 0)
                .addAttribute(RuneCraftoryAttributes.SEAL.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 100));
        this.addStat(RuneCraftoryItems.PARALYSIS_RING.get(), new ItemStat.Builder(15000, 750, 0)
                .addAttribute(RuneCraftoryAttributes.PARALYSIS.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder(), 100));
        this.addStat(RuneCraftoryItems.POISON_RING.get(), new ItemStat.Builder(15000, 750, 0)
                .addAttribute(RuneCraftoryAttributes.POISON.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.POISON_RESISTANCE.asHolder(), 100));
        this.addStat(RuneCraftoryItems.MAGIC_RING.get(), new ItemStat.Builder(50000, 1500, 0)
                .withArmorEffect(RuneCraftoryArmorEffects.MAGIC_RING.asHolder())
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 50));
        this.addStat(RuneCraftoryItems.THROWING_RING.get(), new ItemStat.Builder(10000, 500, 0)
                .withArmorEffect(RuneCraftoryArmorEffects.THROWING_RING.asHolder()));
        this.addStat(RuneCraftoryItems.STAY_UP_RING.get(), new ItemStat.Builder(8000, 500, 0)
                .addAttribute(RuneCraftoryAttributes.SLEEP.asHolder(), -20)
                .addAttribute(RuneCraftoryAttributes.FATIGUE_RESISTANCE.asHolder(), 25));
        this.addStat(RuneCraftoryItems.AQUAMARINE_RING.get(), new ItemStat.Builder(0, 0, 0)
                .addAttribute(RuneCraftoryAttributes.WATER_RESISTANCE.asHolder(), 50)
                .addMonsterStat(RuneCraftoryAttributes.WATER_RESISTANCE.asHolder(), 1)
                .setElement(ItemElement.WATER));
        this.addStat(RuneCraftoryItems.AMETHYST_RING.get(), new ItemStat.Builder(13000, 600, 0)
                .addAttribute(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 50)
                .addMonsterStat(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 1)
                .setElement(ItemElement.EARTH));
        this.addStat(RuneCraftoryItems.EMERALD_RING.get(), new ItemStat.Builder(13000, 600, 0)
                .addAttribute(RuneCraftoryAttributes.WIND_RESISTANCE.asHolder(), 50)
                .addMonsterStat(RuneCraftoryAttributes.WIND_RESISTANCE.asHolder(), 1)
                .setElement(ItemElement.WIND));
        this.addStat(RuneCraftoryItems.SAPPHIRE_RING.get(), new ItemStat.Builder(13000, 600, 0)
                .addAttribute(RuneCraftoryAttributes.LIGHT_RESISTANCE.asHolder(), 50)
                .addMonsterStat(RuneCraftoryAttributes.LIGHT_RESISTANCE.asHolder(), 1)
                .setElement(ItemElement.LIGHT));
        this.addStat(RuneCraftoryItems.RUBY_RING.get(), new ItemStat.Builder(13000, 600, 0)
                .addAttribute(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), 50)
                .addMonsterStat(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), 1)
                .setElement(ItemElement.FIRE));
        this.addStat(RuneCraftoryItems.CURSED_RING.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.DIAMOND_RING.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.AQUAMARINE_BROOCH.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.AMETHYST_BROOCH.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.EMERALD_BROOCH.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.SAPPHIRE_BROOCH.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.RUBY_BROOCH.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.DIAMOND_BROOCH.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.DOLPHIN_BROOCH.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.FIRE_RING.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.WIND_RING.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.WATER_RING.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.EARTH_RING.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.HAPPY_RING.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.SILVER_PENDANT.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.STAR_PENDANT.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.SUN_PENDANT.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.FIELD_PENDANT.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.DEW_PENDANT.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.EARTH_PENDANT.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.HEART_PENDANT.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.STRANGE_PENDANT.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.ANETTES_NECKLACE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.WORK_GLOVES.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.GLOVES_ACCESS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.POWER_GLOVES.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.EARRINGS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.WITCH_EARRINGS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.MAGIC_EARRINGS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.CHARM.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.HOLY_AMULET.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.ROSARY.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.TALISMAN.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.MAGIC_CHARM.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.LEATHER_BELT.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.LUCKY_STRIKE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.CHAMP_BELT.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.HAND_KNIT_SCARF.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.FLUFFY_SCARF.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.HEROS_PROOF.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.PROOF_OF_WISDOM.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.ART_OF_ATTACK.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.ART_OF_DEFENSE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.ART_OF_MAGIC.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.BADGE.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.COURAGE_BADGE.get(), new ItemStat.Builder(0, 0, 0));

        this.addStat(RuneCraftoryItems.SHIRT.get(), new ItemStat.Builder(120, 13, 0)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 2)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.VEST.get(), new ItemStat.Builder(1000, 30, 0)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 7)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.COTTON_CLOTH.get(), new ItemStat.Builder(4000, 190, 0)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 12)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 4)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.MAIL.get(), new ItemStat.Builder(7500, 350, 0)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 19)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 13)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 1));
        this.addStat(RuneCraftoryItems.CHAIN_MAIL.get(), new ItemStat.Builder(11000, 640, 0)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 26)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 20)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 1));
        this.addStat(RuneCraftoryItems.SCALE_VEST.get(), new ItemStat.Builder(14500, 1360, 0)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 39)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 32)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 1));
        this.addStat(RuneCraftoryItems.SPARKLING_SHIRT.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.WIND_CLOAK.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.PROTECTOR.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.PLATINUM_MAIL.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.LEMELLAR_VEST.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.MERCENARYS_CLOAK.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.WOOLY_SHIRT.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.ELVISH_CLOAK.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.DRAGON_CLOAK.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.POWER_PROTECTOR.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.RUNE_VEST.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.ROYAL_GARTER.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.FOUR_DRAGONS_VEST.get(), new ItemStat.Builder(0, 0, 0));

        this.addStat(RuneCraftoryItems.HEADBAND.get(), new ItemStat.Builder(50, 5, 0)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.BLUE_RIBBON.get(), new ItemStat.Builder(500, 35, 0)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 5)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.GREEN_RIBBON.get(), new ItemStat.Builder(500, 35, 0)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 5)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.PURPLE_RIBBON.get(), new ItemStat.Builder(500, 35, 0)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1)
                .addAttribute(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 5)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.SPECTACLES.get(), new ItemStat.Builder(1000, 100, 0)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.STRAW_HAT.get(), new ItemStat.Builder(1500, 140, 0)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 3)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.FANCY_HAT.get(), new ItemStat.Builder(2500, 210, 0)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 7)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 4)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.BRAND_GLASSES.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.CUTE_KNITTING.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.INTELLIGENT_GLASSES.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.FIREPROOF_HOOD.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.SILK_HAT.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.BLACK_RIBBON.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.LOLITA_HEADDRESS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.HEADDRESS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.YELLOW_RIBBON.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.CAT_EARS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.SILVER_HAIRPIN.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.RED_RIBBON.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.ORANGE_RIBBON.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.WHITE_RIBBON.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.FOUR_SEASONS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.FEATHERS_HAT.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.GOLD_HAIRPIN.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.INDIGO_RIBBON.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.CROWN.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.TURNIP_HEADGEAR.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.PUMPKIN_HEADGEAR.get(), new ItemStat.Builder(0, 0, 0));

        this.addStat(RuneCraftoryItems.LEATHER_BOOTS.get(), new ItemStat.Builder(75, 10, 0)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 2)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.FREE_FARMING_SHOES.get(), new ItemStat.Builder(450, 40, 0)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.FATIGUE_RESISTANCE.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.COLD_RESISTANCE.asHolder(), 3)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.PIYO_SANDALS.get(), new ItemStat.Builder(400, 35, 0)
                .withArmorEffect(RuneCraftoryArmorEffects.PIYO_SANDALS.asHolder())
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.LOVE_RESISTANCE.asHolder(), 1)
                .addAttribute(RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder(), 50)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 20)
                .addMonsterStat(RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.SECRET_SHOES.get(), new ItemStat.Builder(3600, 150, 0)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 8)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 1));
        this.addStat(RuneCraftoryItems.SILVER_BOOTS.get(), new ItemStat.Builder(5000, 310, 0)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 12)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 1));
        this.addStat(RuneCraftoryItems.HEAVY_BOOTS.get(), new ItemStat.Builder(9500, 680, 0)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 10)
                .addAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.5)
                .addAttribute(RuneCraftoryAttributes.FATIGUE_RESISTANCE.asHolder(), -20)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 1));
        this.addStat(RuneCraftoryItems.SNEAKING_BOOTS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.FAST_STEP_BOOTS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.GOLD_BOOTS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.BONE_BOOTS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.SNOW_BOOTS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.STRIDER_BOOTS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.STEP_IN_BOOTS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.FEATHER_BOOTS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.GHOST_BOOTS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.IRON_GETA.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.KNIGHT_BOOTS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.FAIRY_BOOTS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.WET_BOOTS.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.WATER_SHOES.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.ICE_SKATES.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.ROCKET_WING.get(), new ItemStat.Builder(0, 0, 0));

        this.addStat(Items.SHIELD, new ItemStat.Builder(50, 14, 0)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 2)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.SMALL_SHIELD.get(), new ItemStat.Builder(150, 23, 0)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 4)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.UMBRELLA.get(), new ItemStat.Builder(350, 60, 0)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 7)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.IRON_SHIELD.get(), new ItemStat.Builder(1000, 130, 0)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 7)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.MONKEY_PLUSH.get(), new ItemStat.Builder(2400, 0, 0)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.LOVE_RESISTANCE.asHolder(), 50)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0.5)
                .setElement(ItemElement.LOVE));
        this.addStat(RuneCraftoryItems.ROUND_SHIELD.get(), new ItemStat.Builder(5100, 0, 0)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 13)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 1));
        this.addStat(RuneCraftoryItems.TURTLE_SHIELD.get(), new ItemStat.Builder(8700, 0, 0)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 20)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 13)
                .addAttribute(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 15)
                .addAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.2)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 1));
        this.addStat(RuneCraftoryItems.CHAOS_SHIELD.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.BONE_SHIELD.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.MAGIC_SHIELD.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.HEAVY_SHIELD.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.PLATINUM_SHIELD.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.KITE_SHIELD.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.KNIGHT_SHIELD.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.ELEMENT_SHIELD.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.MAGICAL_SHIELD.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.PRISM_SHIELD.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.RUNE_SHIELD.get(), new ItemStat.Builder(0, 0, 0));
        this.addStat(RuneCraftoryItems.PLANT_SHIELD.get(), new ItemStat.Builder(2000, 700, 0)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 5));

        this.addStat(RuneCraftoryItems.SCRAP.get(), new ItemStat.Builder(13, 1, 1)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), -2)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), -1));
        this.addStat(RuneCraftoryItems.SCRAP_PLUS.get(), new ItemStat.Builder(0, 2, 1)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 2)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 0.2));
        this.addStat("iron", RunecraftoryTags.Items.IRON, new ItemStat.Builder(150, 2, 5)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 0.5));
        this.addStat("tin", RunecraftoryTags.Items.RAW_MATERIALS_TIN, new ItemStat.Builder(400, 14, 12)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 1.5)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 0.5));
        this.addStat("copper", RunecraftoryTags.Items.COPPER, new ItemStat.Builder(200, 9, 10)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 2)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 0.5));
        this.addStat("bronze", RunecraftoryTags.Items.DUSTS_BRONZE, new ItemStat.Builder(400, 14, 12)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 3)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 0.5));
        this.addStat("silver", RunecraftoryTags.Items.RAW_MATERIALS_SILVER, new ItemStat.Builder(1500, 27, 15)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 5)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 1));
        this.addStat("gold", RunecraftoryTags.Items.GOLD, new ItemStat.Builder(3500, 34, 18)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 7)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 5)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 2));
        this.addStat("platinum", RunecraftoryTags.Items.RAW_MATERIALS_PLATINUM, new ItemStat.Builder(5000, 111, 34)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 18)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 3));
        this.addStat("orichalcum", RunecraftoryTags.Items.ORICHALCUM, new ItemStat.Builder(20000, 750, 65)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 50)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 4));
        this.addStat("dragonic", RunecraftoryTags.Items.DRAGONIC, new ItemStat.Builder(0, 1000, 70)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 110)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 90)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 5));
        this.addStat(Items.NETHERITE_INGOT, new ItemStat.Builder(0, 200, 35)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 15)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 2));

        this.addStat("emerald", RunecraftoryTags.Items.GEMS_EMERALD, new ItemStat.Builder(2500, 5, 19)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 5)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0.5));
        this.addStat(Items.DIAMOND, new ItemStat.Builder(5000, 21, 23)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 10)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2));
        this.addStat("amethyst", RunecraftoryTags.Items.GEMS_AMETHYST, new ItemStat.Builder(3500, 18, 19)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 6)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 2));
        this.addStat("aquamarine", RunecraftoryTags.Items.GEMS_AQUAMARINE, new ItemStat.Builder(3500, 23, 19)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 6)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 3));
        this.addStat("ruby", RunecraftoryTags.Items.GEMS_RUBY, new ItemStat.Builder(4000, 37, 19)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 6)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 4));
        this.addStat("sapphire", RunecraftoryTags.Items.GEMS_SAPPHIRE, new ItemStat.Builder(3500, 24, 19)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 6)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 5));
        this.addStat(RuneCraftoryItems.CORE_GREEN.get(), new ItemStat.Builder(15000, 1050, 70)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 50)
                .addMonsterStat(RuneCraftoryAttributes.WIND_RESISTANCE.asHolder(), 6)
                .addMonsterStat(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), -3));
        this.addStat(RuneCraftoryItems.CORE_RED.get(), new ItemStat.Builder(15000, 1050, 70)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 50)
                .addMonsterStat(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), 6)
                .addMonsterStat(RuneCraftoryAttributes.WATER_RESISTANCE.asHolder(), -3));
        this.addStat(RuneCraftoryItems.CORE_BLUE.get(), new ItemStat.Builder(15000, 1050, 70)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 50)
                .addMonsterStat(RuneCraftoryAttributes.WATER_RESISTANCE.asHolder(), 6)
                .addMonsterStat(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), -3));
        this.addStat(RuneCraftoryItems.CORE_YELLOW.get(), new ItemStat.Builder(15000, 1050, 70)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 50)
                .addMonsterStat(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 6)
                .addMonsterStat(RuneCraftoryAttributes.WIND_RESISTANCE.asHolder(), -3));
        this.addStat(RuneCraftoryItems.CRYSTAL_SKULL.get(), new ItemStat.Builder(25000, 2300, 90)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 40)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 70)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 2)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 3));

        this.addStat(RuneCraftoryItems.CRYSTAL_WATER.get(), new ItemStat.Builder(2000, 150, 20)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 5)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 2)
                .setElement(ItemElement.WATER)
                .setSpell(RuneCraftorySpells.WATER_SWIPE.asHolder(), RuneCraftorySpells.WATER_SWIPE_140.asHolder(), RuneCraftorySpells.WATER_SWIPE_360.asHolder()));
        this.addStat(RuneCraftoryItems.CRYSTAL_EARTH.get(), new ItemStat.Builder(2000, 150, 20)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 5)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1)
                .setElement(ItemElement.EARTH)
                .setSpell(null, null, null)); // TODO
        this.addStat(RuneCraftoryItems.CRYSTAL_FIRE.get(), new ItemStat.Builder(2000, 150, 20)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 2)
                .setElement(ItemElement.FIRE)
                .setSpell(RuneCraftorySpells.DOUBLE_FIRE_BALL.asHolder(), RuneCraftorySpells.TRIPLE_FIRE_BALL.asHolder(), RuneCraftorySpells.QUAD_FIRE_BALL.asHolder()));
        this.addStat(RuneCraftoryItems.CRYSTAL_WIND.get(), new ItemStat.Builder(2000, 150, 20)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 5)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2)
                .setElement(ItemElement.WIND)
                .setSpell(RuneCraftorySpells.DOUBLE_SONIC.asHolder(), RuneCraftorySpells.QUADRUPLE_WIND_BLADE.asHolder(), RuneCraftorySpells.SEXTUPLE_WIND_BLADE.asHolder()));
        this.addStat(RuneCraftoryItems.CRYSTAL_LIGHT.get(), new ItemStat.Builder(2000, 150, 20)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 1.5)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1.5)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 0.5)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 0.5)
                .setElement(ItemElement.LIGHT)
                .setSpell(RuneCraftorySpells.EXPANDING_DOUBLE_LIGHT.asHolder(), RuneCraftorySpells.EXPANDING_QUAD_LIGHT.asHolder(), RuneCraftorySpells.EXPANDING_OCTO_LIGHT.asHolder()));
        this.addStat(RuneCraftoryItems.CRYSTAL_DARK.get(), new ItemStat.Builder(2000, 150, 20)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 1.5)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setElement(ItemElement.DARK)
                .setSpell(null, null, null)); // TODO
        this.addStat(RuneCraftoryItems.CRYSTAL_LOVE.get(), new ItemStat.Builder(2000, 150, 20)
                .addAttribute(RuneCraftoryAttributes.DRAIN.asHolder(), 3)
                .addMonsterStat(RuneCraftoryAttributes.DRAIN.asHolder(), 1)
                .setElement(ItemElement.LOVE));
        this.addStat(RuneCraftoryItems.CRYSTAL_SMALL.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.CRYSTAL_BIG.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.CRYSTAL_MAGIC.get(), new ItemStat.Builder(45, 400, 25)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 7)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 1));
        this.addStat(RuneCraftoryItems.CRYSTAL_RUNE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.CRYSTAL_ELECTRO.get(), 0, 0, 0);

        this.addStat(RuneCraftoryItems.STICK_THICK.get(), new ItemStat.Builder(1900, 200, 45)
                .addAttribute(Attributes.ATTACK_DAMAGE, 15)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.HORN_INSECT.get(), new ItemStat.Builder(130, 21, 8)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.HORN_RIGID.get(), new ItemStat.Builder(200, 44, 11)
                .addAttribute(Attributes.ATTACK_DAMAGE, 10)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.PLANT_STEM.get(), new ItemStat.Builder(300, 52, 17)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.HORN_BULL.get(), new ItemStat.Builder(450, 64, 26)
                .addAttribute(Attributes.ATTACK_DAMAGE, 8)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 2));
        this.addStat(RuneCraftoryItems.HORN_DEVIL.get(), new ItemStat.Builder(850, 91, 43)
                .addAttribute(Attributes.ATTACK_DAMAGE, 30)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 30)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 2));
        this.addStat(RuneCraftoryItems.MOVING_BRANCH.get(), new ItemStat.Builder(22000, 2300, 77)
                .addAttribute(Attributes.ATTACK_DAMAGE, -10)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 150)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 3)
                .setSpell(RuneCraftorySpells.ROOT_SPIKE.asHolder(), RuneCraftorySpells.APPLE_SHIELD.asHolder(), null));

        this.addStat(RuneCraftoryItems.GLUE.get(), new ItemStat.Builder(380, 41, 13)
                .addAttribute(Attributes.ATTACK_DAMAGE, 1)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 1)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 1));
        this.addStat(RuneCraftoryItems.DEVIL_BLOOD.get(), new ItemStat.Builder(730, 49, 27)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.DRAIN.asHolder(), 15)
                .addMonsterStat(RuneCraftoryAttributes.DRAIN.asHolder(), 1));
        this.addStat(RuneCraftoryItems.PARA_POISON.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.POISON_KING.get(), 0, 0, 0);

        this.addStat(RuneCraftoryItems.FEATHER_BLACK.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.FEATHER_THUNDER.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.FEATHER_YELLOW.get(), new ItemStat.Builder(500, 20, 15)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 13)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 2));
        this.addStat(RuneCraftoryItems.DRAGON_FIN.get(), 0, 0, 0);

        this.addStat(RuneCraftoryItems.TURTLE_SHELL.get(), new ItemStat.Builder(160, 30, 16)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.CRITICAL.asHolder(), -3)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 1));
        this.addStat(RuneCraftoryItems.FISH_FOSSIL.get(), new ItemStat.Builder(180, 30, 19)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 1));
        this.addStat(RuneCraftoryItems.SKULL.get(), new ItemStat.Builder(100, 1000, 35)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 9)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 4)
                .addAttribute(RuneCraftoryAttributes.COLD.asHolder(), 5)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1));
        this.addStat(RuneCraftoryItems.DRAGON_BONES.get(), new ItemStat.Builder(27000, 1500, 52)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 13)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 13)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 3)
                .setSpell(null, RuneCraftorySpells.BONE_NEEDLES.asHolder(), RuneCraftorySpells.ENERGY_ORB_SPELL.asHolder()));
        this.addStat(RuneCraftoryItems.TORTOISE_SHELL.get(), 0, 0, 0);

        this.addStat(RuneCraftoryItems.ROCK.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.STONE_ROUND.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.STONE_TINY.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.STONE_GOLEM.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.TABLET_GOLEM.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.STONE_SPIRIT.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.TABLET_TRUTH.get(), 0, 0, 0);

        this.addStat(RuneCraftoryItems.YARN.get(), new ItemStat.Builder(400, 75, 9)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 1));
        this.addStat(RuneCraftoryItems.OLD_BANDAGE.get(), new ItemStat.Builder(320, 24, 5)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 1));
        this.addStat(RuneCraftoryItems.AMBROSIAS_THORNS.get(), new ItemStat.Builder(7500, 500, 21)
                .addAttribute(Attributes.ATTACK_DAMAGE, 7)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 7)
                .addAttribute(RuneCraftoryAttributes.SLEEP.asHolder(), 10)
                .addMonsterStat(RuneCraftoryAttributes.SLEEP.asHolder(), 1)
                .setSpell(RuneCraftorySpells.POLLEN_PUFF.asHolder(), RuneCraftorySpells.WAVE.asHolder(), RuneCraftorySpells.BUTTERFLY.asHolder()));
        this.addStat(RuneCraftoryItems.THREAD_SPIDER.get(), new ItemStat.Builder(370, 28, 17)
                .addAttribute(Attributes.ATTACK_DAMAGE, 2)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.PARALYSIS.asHolder(), 5)
                .addMonsterStat(RuneCraftoryAttributes.PARALYSIS.asHolder(), 0.3));
        this.addStat(RuneCraftoryItems.PUPPETRY_STRINGS.get(), new ItemStat.Builder(30000, 1000, 37)
                .addAttribute(Attributes.ATTACK_DAMAGE, 6)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 9)
                .addAttribute(RuneCraftoryAttributes.SEAL.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.PARALYSIS.asHolder(), 5)
                .addMonsterStat(RuneCraftoryAttributes.SEAL.asHolder(), 0.5)
                .addMonsterStat(RuneCraftoryAttributes.PARALYSIS.asHolder(), 0.5)
                .setSpell(RuneCraftorySpells.DARK_BEAM.asHolder(), RuneCraftorySpells.PLATE.asHolder(), RuneCraftorySpells.DARK_BULLETS.asHolder()));
        this.addStat(RuneCraftoryItems.VINE.get(), new ItemStat.Builder(515, 58, 34)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.SEAL.asHolder(), 5)
                .addMonsterStat(RuneCraftoryAttributes.SEAL.asHolder(), 0.3));
        this.addStat(RuneCraftoryItems.TAIL_SCORPION.get(), new ItemStat.Builder(610, 62, 31)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5)
                .addAttribute(RuneCraftoryAttributes.POISON.asHolder(), 2)
                .addMonsterStat(RuneCraftoryAttributes.POISON.asHolder(), 0.3));
        this.addStat(RuneCraftoryItems.STRONG_VINE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.THREAD_PRETTY.get(), new ItemStat.Builder(386, 41, 24)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 15)
                .addAttribute(RuneCraftoryAttributes.PARALYSIS.asHolder(), 20)
                .addMonsterStat(RuneCraftoryAttributes.PARALYSIS.asHolder(), 2)
                .setSpell(null, RuneCraftorySpells.WEB_SHOT.asHolder(), null));
        this.addStat(RuneCraftoryItems.TAIL_CHIMERA.get(), new ItemStat.Builder(20000, 2600, 87)
                .addAttribute(Attributes.ATTACK_DAMAGE, 150)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), -10)
                .addAttribute(RuneCraftoryAttributes.POISON.asHolder(), 50)
                .addAttribute(RuneCraftoryAttributes.PARALYSIS.asHolder(), 50)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .addMonsterStat(RuneCraftoryAttributes.POISON.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.PARALYSIS.asHolder(), 1)
                .setSpell(null, RuneCraftorySpells.BUBBLE_BEAM.asHolder(), RuneCraftorySpells.FIREBALL_BARRAGE.asHolder()));

        this.addStat(RuneCraftoryItems.ARROW_HEAD.get(), new ItemStat.Builder(80, 10, 2)
                .addAttribute(Attributes.ATTACK_DAMAGE, 2)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .setSpell(null, RuneCraftorySpells.DOUBLE_ARROW.asHolder(), null));
        this.addStat(RuneCraftoryItems.BLADE_SHARD.get(), new ItemStat.Builder(139, 25, 9)
                .addAttribute(Attributes.ATTACK_DAMAGE, 3)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.BROKEN_HILT.get(), new ItemStat.Builder(550, 50, 22)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.POISON_RESISTANCE.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.SLEEP_RESISTANCE.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.FATIGUE_RESISTANCE.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.COLD_RESISTANCE.asHolder(), 5)
                .addMonsterStat(RuneCraftoryAttributes.POISON_RESISTANCE.asHolder(), 0.2)
                .addMonsterStat(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 0.2)
                .addMonsterStat(RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder(), 0.2)
                .addMonsterStat(RuneCraftoryAttributes.SLEEP_RESISTANCE.asHolder(), 0.2)
                .addMonsterStat(RuneCraftoryAttributes.FATIGUE_RESISTANCE.asHolder(), 0.2)
                .addMonsterStat(RuneCraftoryAttributes.COLD_RESISTANCE.asHolder(), 0.2));
        this.addStat(RuneCraftoryItems.BROKEN_BOX.get(), new ItemStat.Builder(1000, 200, 48)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 4)
                .addAttribute(RuneCraftoryAttributes.POISON_RESISTANCE.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.SLEEP_RESISTANCE.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.FATIGUE_RESISTANCE.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.COLD_RESISTANCE.asHolder(), 10)
                .addMonsterStat(RuneCraftoryAttributes.POISON_RESISTANCE.asHolder(), 0.5)
                .addMonsterStat(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 0.5)
                .addMonsterStat(RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder(), 0.5)
                .addMonsterStat(RuneCraftoryAttributes.SLEEP_RESISTANCE.asHolder(), 0.5)
                .addMonsterStat(RuneCraftoryAttributes.FATIGUE_RESISTANCE.asHolder(), 0.5)
                .addMonsterStat(RuneCraftoryAttributes.COLD_RESISTANCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.BLADE_GLISTENING.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GREAT_HAMMER_SHARD.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.HAMMER_PIECE.get(), new ItemStat.Builder(853, 106, 33)
                .addAttribute(Attributes.ATTACK_DAMAGE, 9)
                .addAttribute(RuneCraftoryAttributes.DIZZY.asHolder(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.SHOULDER_PIECE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.PIRATES_ARMOR.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SCREW_RUSTY.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SCREW_SHINY.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.ROCK_SHARD_LEFT.get(), new ItemStat.Builder(14000, 937, 88)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 75)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 30)
                .addAttribute(RuneCraftoryAttributes.POISON_RESISTANCE.asHolder(), 6)
                .addAttribute(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 6)
                .addAttribute(RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder(), 6)
                .addAttribute(RuneCraftoryAttributes.SLEEP_RESISTANCE.asHolder(), 6)
                .addAttribute(RuneCraftoryAttributes.FATIGUE_RESISTANCE.asHolder(), 6)
                .addAttribute(RuneCraftoryAttributes.COLD_RESISTANCE.asHolder(), 6)
                .addAttribute(RuneCraftoryAttributes.FAINT_RESISTANCE.asHolder(), 1)
                .addAttribute(RuneCraftoryAttributes.DRAIN_RESISTANCE.asHolder(), 1)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .addAttribute(RuneCraftoryAttributes.POISON_RESISTANCE.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.SLEEP_RESISTANCE.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.FATIGUE_RESISTANCE.asHolder(), 1)
                .addAttribute(RuneCraftoryAttributes.COLD_RESISTANCE.asHolder(), 1)
                .addAttribute(RuneCraftoryAttributes.FAINT_RESISTANCE.asHolder(), 1)
                .addAttribute(RuneCraftoryAttributes.DRAIN_RESISTANCE.asHolder(), 1));
        this.addStat(RuneCraftoryItems.ROCK_SHARD_RIGHT.get(), new ItemStat.Builder(14000, 937, 88)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 30)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 75)
                .addAttribute(RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 3)
                .addAttribute(Attributes.KNOCKBACK_RESISTANCE, 15)
                .addMonsterStat(RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 1)
                .addMonsterStat(Attributes.KNOCKBACK_RESISTANCE, 3));
        this.addStat(RuneCraftoryItems.MTGU_PLATE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.BROKEN_ICE_WALL.get(), 0, 0, 0);

        this.addStat(RuneCraftoryItems.FUR_SMALL.get(), new ItemStat.Builder(35, 7, 1)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 1)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1)
                .addAttribute(RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 1));
        this.addStat(RuneCraftoryItems.FUR_MEDIUM.get(), new ItemStat.Builder(1000, 100, 29)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder(), 10)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 1));
        this.addStat(RuneCraftoryItems.FUR_LARGE.get(), new ItemStat.Builder(3000, 500, 55)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder(), 25)
                .addAttribute(RuneCraftoryAttributes.SLEEP_RESISTANCE.asHolder(), 5)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 1));
        this.addStat(RuneCraftoryItems.FUR.get(), new ItemStat.Builder(130, 23, 7)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1)
                .addAttribute(RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder(), 3)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 1));
        this.addStat(RuneCraftoryItems.FURBALL.get(), new ItemStat.Builder(900, 120, 38)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 8)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 4)
                .addAttribute(RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.SLEEP_RESISTANCE.asHolder(), 3)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 2));
        this.addStat(RuneCraftoryItems.DOWN_YELLOW.get(), new ItemStat.Builder(300, 33, 21)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder(), 5)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 2));
        this.addStat(RuneCraftoryItems.FUR_QUALITY.get(), new ItemStat.Builder(650, 45, 36)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 4)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 3)
                .addAttribute(RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder(), 7)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 2));
        this.addStat(RuneCraftoryItems.DOWN_PENGUIN.get(), new ItemStat.Builder(1250, 129, 59)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 13)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 7)
                .addAttribute(RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder(), 13)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 3));
        this.addStat(RuneCraftoryItems.LIGHTNING_MANE.get(), new ItemStat.Builder(13000, 600, 31)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 8)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 6)
                .addAttribute(RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder(), 17)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder(), 1)
                .setSpell(RuneCraftorySpells.LASER3.asHolder(), RuneCraftorySpells.LASER5.asHolder(), RuneCraftorySpells.BIG_LIGHTNING.asHolder()));
        this.addStat(RuneCraftoryItems.FUR_RED_LION.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.FUR_BLUE_LION.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.CHEST_HAIR.get(), 0, 0, 0);

        this.addStat(RuneCraftoryItems.SPORE.get(), new ItemStat.Builder(110, 19, 9)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 2)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 1)
                .setSpell(null, RuneCraftorySpells.SPORE_CIRCLE_SPELL.asHolder(), null));
        this.addStat(RuneCraftoryItems.POWDER_POISON.get(), new ItemStat.Builder(550, 80, 21)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.POISON.asHolder(), 5)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.POISON.asHolder(), 0.5)
                .setSpell(null, RuneCraftorySpells.POISON_BALL.asHolder(), null));
        this.addStat(RuneCraftoryItems.SPORE_HOLY.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.FAIRY_DUST.get(), new ItemStat.Builder(300, 40, 19)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 5)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 1)
                .setSpell(null, RuneCraftorySpells.SLEEP_AURA.asHolder(), null));
        this.addStat(RuneCraftoryItems.FAIRY_ELIXIR.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.ROOT.get(), new ItemStat.Builder(770, 68, 25)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 8)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 1));
        this.addStat(RuneCraftoryItems.POWDER_MAGIC.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.POWDER_MYSTERIOUS.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.MAGIC.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.ASH_EARTH.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.ASH_FIRE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.ASH_WATER.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.TURNIPS_MIRACLE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.MELODY_BOTTLE.get(), 0, 0, 0);

        this.addStat(RuneCraftoryItems.CLOTH_CHEAP.get(), new ItemStat.Builder(80, 12, 4)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 1)
                .addAttribute(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 1));
        this.addStat(RuneCraftoryItems.CLOTH_QUALITY.get(), new ItemStat.Builder(800, 100, 18)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 1));
        this.addStat(RuneCraftoryItems.CLOTH_QUALITY_WORN.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.CLOTH_SILK.get(), new ItemStat.Builder(950, 130, 26)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 8)
                .addAttribute(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                .addMonsterStat(RuneCraftoryAttributes.DEFENCE.asHolder(), 1));
        this.addStat(RuneCraftoryItems.GHOST_HOOD.get(), new ItemStat.Builder(70, 650, 21)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 25)
                .addMonsterStat(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.GLOVE_GIANT.get(), new ItemStat.Builder(810, 76, 36)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 5)
                .addMonsterStat(RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.GLOVE_BLUE_GIANT.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.CARAPACE_INSECT.get(), new ItemStat.Builder(75, 11, 8)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.POISON_RESISTANCE.asHolder(), 15)
                .addMonsterStat(RuneCraftoryAttributes.POISON_RESISTANCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.CARAPACE_PRETTY.get(), new ItemStat.Builder(750, 85, 24)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 4)
                .addAttribute(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder(), 20)
                .addMonsterStat(RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder(), 0.5));
        this.addStat(RuneCraftoryItems.CLOTH_ANCIENT_ORC.get(), 0, 0, 0);

        this.addStat(RuneCraftoryItems.JAW_INSECT.get(), new ItemStat.Builder(100, 23, 14)
                .addAttribute(Attributes.ATTACK_DAMAGE, 2)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 0.3));
        this.addStat(RuneCraftoryItems.CLAW_PANTHER.get(), new ItemStat.Builder(450, 55, 28)
                .addAttribute(Attributes.ATTACK_DAMAGE, 4)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(RuneCraftoryItems.CLAW_MAGIC.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.FANG_WOLF.get(), new ItemStat.Builder(470, 60, 31)
                .addAttribute(Attributes.ATTACK_DAMAGE, 6)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 2));
        this.addStat(RuneCraftoryItems.FANG_GOLD_WOLF.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.CLAW_PALM.get(), new ItemStat.Builder(640, 74, 37)
                .addAttribute(Attributes.ATTACK_DAMAGE, 9)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 2));
        this.addStat(RuneCraftoryItems.CLAW_MALM.get(), new ItemStat.Builder(940, 83, 45)
                .addAttribute(Attributes.ATTACK_DAMAGE, 11)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 2));
        this.addStat(RuneCraftoryItems.GIANTS_NAIL.get(), new ItemStat.Builder(980, 103, 44)
                .addAttribute(Attributes.ATTACK_DAMAGE, 11)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 2));
        this.addStat(RuneCraftoryItems.CLAW_CHIMERA.get(), new ItemStat.Builder(18000, 1500, 50)
                .addAttribute(Attributes.ATTACK_DAMAGE, 15)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 2)
                .addAttribute(RuneCraftoryAttributes.POISON.asHolder(), 5)
                .addAttribute(RuneCraftoryAttributes.PARALYSIS.asHolder(), 5)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 2)
                .addMonsterStat(RuneCraftoryAttributes.POISON.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.PARALYSIS.asHolder(), 1)
                .setSpell(null, RuneCraftorySpells.SLASH.asHolder(), null));
        this.addStat(RuneCraftoryItems.TUSK_IVORY.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.TUSK_UNBROKEN_IVORY.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SCORPION_PINCER.get(), new ItemStat.Builder(1470, 138, 55)
                .addAttribute(Attributes.ATTACK_DAMAGE, 20)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 2));
        this.addStat(RuneCraftoryItems.DANGEROUS_SCISSORS.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.PROPELLOR_CHEAP.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.PROPELLOR_QUALITY.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.FANG_DRAGON.get(), new ItemStat.Builder(2300, 199, 68)
                .addAttribute(Attributes.ATTACK_DAMAGE, 40)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 2));
        this.addStat(RuneCraftoryItems.JAW_QUEEN.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.WIND_DRAGON_TOOTH.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GIANTS_NAIL_BIG.get(), 0, 0, 0);

        this.addStat(RuneCraftoryItems.SCALE_WET.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SCALE_GRIMOIRE.get(), new ItemStat.Builder(75000, 3000, 70)
                .addAttribute(Attributes.ATTACK_DAMAGE, 35)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 40)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 40)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 35)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 5)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 5));
        this.addStat(RuneCraftoryItems.SCALE_DRAGON.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SCALE_CRIMSON.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SCALE_BLUE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SCALE_GLITTER.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SCALE_LOVE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SCALE_BLACK.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SCALE_FIRE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SCALE_EARTH.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SCALE_LEGEND.get(), 0, 0, 0);

        this.addStat(RuneCraftoryItems.STEEL_DOUBLE.get(), new ItemStat.Builder(0, 200, 50));
        this.addStat(RuneCraftoryItems.STEEL_TEN.get(), new ItemStat.Builder(0, 2000, 95));
        this.addStat(RuneCraftoryItems.GLITTA_AUGITE.get(), new ItemStat.Builder(0, 1200, 0)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), 1));
        this.addStat(RuneCraftoryItems.INVIS_STONE.get(), new ItemStat.Builder(0, 750, 24));
        this.addStat(RuneCraftoryItems.LIGHT_ORE.get(), new ItemStat.Builder(0, 7500, 0));
        this.addStat(RuneCraftoryItems.RUNE_SPHERE_SHARD.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SHADE_STONE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.RACCOON_LEAF.get(), new ItemStat.Builder(25000, 2100, 60)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 35)
                .addAttribute(RuneCraftoryAttributes.ATTACK_RANGE.asHolder(), 1)
                .setSpell(RuneCraftorySpells.BIG_LEAF_SPELL.asHolder(), RuneCraftorySpells.SMALL_LEAF_SPELL_X5.asHolder(), null));
        this.addStat(RuneCraftoryItems.ICY_NOSE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.BIG_BIRDS_COMB.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.RAFFLESIA_PETAL.get(), new ItemStat.Builder(30000, 3400, 55)
                .addAttribute(Attributes.ATTACK_DAMAGE, 5)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 18)
                .addAttribute(RuneCraftoryAttributes.FATIGUE.asHolder(), 20)
                .addAttribute(RuneCraftoryAttributes.COLD.asHolder(), 15)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.FATIGUE.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.COLD.asHolder(), 1)
                .setSpell(RuneCraftorySpells.WIND_CIRCLE_X8.asHolder(), RuneCraftorySpells.RAFFLESIA_CIRCLE.asHolder(), RuneCraftorySpells.RAFFLESIA_POISON.asHolder()));
        this.addStat(RuneCraftoryItems.CURSED_DOLL.get(), new ItemStat.Builder(27000, 750, 39)
                .addAttribute(RuneCraftoryAttributes.DEFENCE.asHolder(), 4)
                .addAttribute(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 7)
                .addAttribute(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder(), 10)
                .addAttribute(RuneCraftoryAttributes.DRAIN_RESISTANCE.asHolder(), 15)
                .addMonsterStat(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder(), 1)
                .addMonsterStat(RuneCraftoryAttributes.DRAIN_RESISTANCE.asHolder(), 1)
                .setSpell(RuneCraftorySpells.CARD_THROW.asHolder(), RuneCraftorySpells.PLUSH_THROW.asHolder(), RuneCraftorySpells.FURNITURE.asHolder()));
        this.addStat(RuneCraftoryItems.WARRIORS_PROOF.get(), new ItemStat.Builder(3500, 330, 55)
                .addAttribute(Attributes.ATTACK_DAMAGE, 25)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 10)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 2)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 1));
        this.addStat(RuneCraftoryItems.PROOF_OF_RANK.get(), new ItemStat.Builder(3500, 330, 55)
                .addAttribute(Attributes.ATTACK_DAMAGE, 10)
                .addAttribute(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 25)
                .addMonsterStat(Attributes.ATTACK_DAMAGE, 1)
                .addMonsterStat(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 2));
        this.addStat(RuneCraftoryItems.THRONE_OF_EMPIRE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.WHITE_STONE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.RARE_CAN.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.CAN.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.BOOTS.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.LAWN.get(), 0, 0, 0);

        this.addStat(RuneCraftoryItems.FIRE_BALL_SMALL.get(), new ItemStat.Builder(2400, 120, 0));
        this.addStat(RuneCraftoryItems.FIRE_BALL_BIG.get(), new ItemStat.Builder(4800, 240, 0));
        this.addStat(RuneCraftoryItems.EXPLOSION.get(), new ItemStat.Builder(9600, 480, 0));
        this.addStat(RuneCraftoryItems.WATER_LASER.get(), new ItemStat.Builder(2400, 120, 0));
        this.addStat(RuneCraftoryItems.PARALLEL_LASER.get(), new ItemStat.Builder(4800, 240, 0));
        this.addStat(RuneCraftoryItems.DELTA_LASER.get(), new ItemStat.Builder(9600, 480, 0));
        this.addStat(RuneCraftoryItems.SCREW_ROCK.get(), new ItemStat.Builder(2400, 120, 0));
        this.addStat(RuneCraftoryItems.EARTH_SPIKE.get(), new ItemStat.Builder(4800, 240, 0));
        this.addStat(RuneCraftoryItems.AVENGER_ROCK.get(), new ItemStat.Builder(9600, 480, 0));
        this.addStat(RuneCraftoryItems.SONIC_WIND.get(), new ItemStat.Builder(2400, 120, 0));
        this.addStat(RuneCraftoryItems.DOUBLE_SONIC.get(), new ItemStat.Builder(4800, 240, 0));
        this.addStat(RuneCraftoryItems.PENETRATE_SONIC.get(), new ItemStat.Builder(9600, 480, 0));
        this.addStat(RuneCraftoryItems.LIGHT_BARRIER.get(), new ItemStat.Builder(2700, 135, 0));
        this.addStat(RuneCraftoryItems.SHINE.get(), new ItemStat.Builder(5400, 270, 0));
        this.addStat(RuneCraftoryItems.PRISM.get(), new ItemStat.Builder(10800, 540, 0));
        this.addStat(RuneCraftoryItems.DARK_SNAKE.get(), new ItemStat.Builder(2700, 135, 0));
        this.addStat(RuneCraftoryItems.DARK_BALL.get(), new ItemStat.Builder(5400, 270, 0));
        this.addStat(RuneCraftoryItems.DARKNESS.get(), new ItemStat.Builder(10800, 540, 0));
        this.addStat(RuneCraftoryItems.CURE.get(), new ItemStat.Builder(1500, 75, 0));
        this.addStat(RuneCraftoryItems.CURE_ALL.get(), new ItemStat.Builder(4500, 225, 0));
        this.addStat(RuneCraftoryItems.CURE_MASTER.get(), new ItemStat.Builder(12000, 600, 0));
        this.addStat(RuneCraftoryItems.MEDI_POISON.get(), new ItemStat.Builder(1500, 75, 0));
        this.addStat(RuneCraftoryItems.MEDI_PARA.get(), new ItemStat.Builder(3000, 150, 0));
        this.addStat(RuneCraftoryItems.MEDI_SEAL.get(), new ItemStat.Builder(5000, 250, 0));

        this.addStat(RuneCraftoryItems.GREETING.get(), new ItemStat.Builder(500, 25, 0));
        this.addStat(RuneCraftoryItems.POWER_WAVE.get(), new ItemStat.Builder(1000, 50, 0));
        this.addStat(RuneCraftoryItems.DASH_SLASH.get(), new ItemStat.Builder(2000, 100, 0));
        this.addStat(RuneCraftoryItems.RUSH_ATTACK.get(), new ItemStat.Builder(3500, 175, 0));
        this.addStat(RuneCraftoryItems.ROUND_BREAK.get(), new ItemStat.Builder(6500, 325, 0));
        this.addStat(RuneCraftoryItems.MIND_THRUST.get(), new ItemStat.Builder(2500, 125, 0));
        this.addStat(RuneCraftoryItems.GUST.get(), new ItemStat.Builder(3000, 150, 0));
        this.addStat(RuneCraftoryItems.STORM.get(), new ItemStat.Builder(4000, 200, 0));
        this.addStat(RuneCraftoryItems.BLITZ.get(), new ItemStat.Builder(10000, 500, 0));
        this.addStat(RuneCraftoryItems.TWIN_ATTACK.get(), new ItemStat.Builder(1500, 75, 0));
        this.addStat(RuneCraftoryItems.RAIL_STRIKE.get(), new ItemStat.Builder(4700, 235, 0));
        this.addStat(RuneCraftoryItems.WIND_SLASH.get(), new ItemStat.Builder(3500, 175, 0));
        this.addStat(RuneCraftoryItems.FLASH_STRIKE.get(), new ItemStat.Builder(10000, 500, 0));
        this.addStat(RuneCraftoryItems.NAIVE_BLADE.get(), new ItemStat.Builder(6000, 300, 0));
        this.addStat(RuneCraftoryItems.STEEL_HEART.get(), new ItemStat.Builder(4000, 200, 0));
        this.addStat(RuneCraftoryItems.DELTA_STRIKE.get(), new ItemStat.Builder(1500, 75, 0));
        this.addStat(RuneCraftoryItems.HURRICANE.get(), new ItemStat.Builder(2000, 200, 0));
        this.addStat(RuneCraftoryItems.REAPER_SLASH.get(), new ItemStat.Builder(2500, 125, 0));
        this.addStat(RuneCraftoryItems.MILLION_STRIKE.get(), new ItemStat.Builder(9000, 450, 0));
        this.addStat(RuneCraftoryItems.AXEL_DISASTER.get(), new ItemStat.Builder(5000, 250, 0));
        this.addStat(RuneCraftoryItems.STARDUST_UPPER.get(), new ItemStat.Builder(1800, 90, 0));
        this.addStat(RuneCraftoryItems.TORNADO_SWING.get(), new ItemStat.Builder(3500, 175, 0));
        this.addStat(RuneCraftoryItems.GRAND_IMPACT.get(), new ItemStat.Builder(6000, 300, 0));
        this.addStat(RuneCraftoryItems.GIGA_SWING.get(), new ItemStat.Builder(9500, 475, 0));
        this.addStat(RuneCraftoryItems.UPPER_CUT.get(), new ItemStat.Builder(2500, 125, 0));
        this.addStat(RuneCraftoryItems.DOUBLE_KICK.get(), new ItemStat.Builder(8000, 400, 0));
        this.addStat(RuneCraftoryItems.STRAIGHT_PUNCH.get(), new ItemStat.Builder(8500, 425, 0));
        this.addStat(RuneCraftoryItems.NEKO_DAMASHI.get(), new ItemStat.Builder(5500, 275, 0));
        this.addStat(RuneCraftoryItems.RUSH_PUNCH.get(), new ItemStat.Builder(7000, 350, 0));
        this.addStat(RuneCraftoryItems.CYCLONE.get(), new ItemStat.Builder(13000, 650, 0));
        this.addStat(RuneCraftoryItems.RAPID_MOVE.get(), new ItemStat.Builder(4000, 200, 0));
        this.addStat(RuneCraftoryItems.BONUS_CONCERTO.get(), new ItemStat.Builder(3000, 150, 0));
        this.addStat(RuneCraftoryItems.STRIKING_MARCH.get(), new ItemStat.Builder(8500, 425, 0));
        this.addStat(RuneCraftoryItems.IRON_WALTZ.get(), new ItemStat.Builder(8500, 425, 0));
        this.addStat(RuneCraftoryItems.TELEPORT.get(), new ItemStat.Builder(500, 10, 0));

        this.addStat(RuneCraftoryItems.WITHERED_GRASS.get(), 100, 1, 1);
        this.addStat(RuneCraftoryItems.WEEDS.get(), 30, 1, 1);
        this.addStat(RuneCraftoryItems.WHITE_GRASS.get(), 120, 5, 1);
        this.addStat(RuneCraftoryItems.INDIGO_GRASS.get(), 120, 5, 1);
        this.addStat(RuneCraftoryItems.PURPLE_GRASS.get(), 120, 5, 1);
        this.addStat(RuneCraftoryItems.GREEN_GRASS.get(), 120, 5, 1);
        this.addStat(RuneCraftoryItems.BLUE_GRASS.get(), 120, 5, 1);
        this.addStat(RuneCraftoryItems.YELLOW_GRASS.get(), 120, 5, 1);
        this.addStat(RuneCraftoryItems.ORANGE_GRASS.get(), 120, 5, 1);
        this.addStat(RuneCraftoryItems.RED_GRASS.get(), 120, 5, 1);
        this.addStat(RuneCraftoryItems.BLACK_GRASS.get(), 120, 5, 1);
        this.addStat(RuneCraftoryItems.ANTIDOTE_GRASS.get(), new ItemStat.Builder(120, 5, 1)
                .addAttribute(RuneCraftoryAttributes.POISON_RESISTANCE.asHolder(), 5));
        this.addStat(RuneCraftoryItems.MEDICINAL_HERB.get(), 150, 10, 1);
        this.addStat(RuneCraftoryItems.BAMBOO_SPROUT.get(), 100, 10, 1);
        this.addStat(RuneCraftoryItems.MUSHROOM.get(), 100, 10, 1);
        this.addStat(RuneCraftoryItems.MONARCH_MUSHROOM.get(), 300, 15, 1);
        this.addStat(RuneCraftoryItems.ELLI_LEAVES.get(), 250, 15, 1);

        this.addStat(RuneCraftoryItems.FORGING_BREAD.get(), 600, 100, 1);
        this.addStat(RuneCraftoryItems.COOKING_BREAD.get(), 600, 100, 1);
        this.addStat(RuneCraftoryItems.MEDICINE_BREAD.get(), 600, 100, 1);
        this.addStat(RuneCraftoryItems.ACCESSORY_BREAD.get(), 600, 100, 1);

        this.addStat(RuneCraftoryItems.RICE.get(), 100, 30, 0);
        this.addStat(RuneCraftoryItems.RICE_FLOUR.get(), 120, 35, 0);
        this.addStat(RuneCraftoryItems.FLOUR.get(), 90, 25, 0);
        this.addStat(RuneCraftoryItems.OIL.get(), 100, 30, 0);
        this.addStat(RuneCraftoryItems.CURRY_POWDER.get(), 130, 40, 0);
        this.addStat(RuneCraftoryItems.WINE.get(), 150, 50, 0);
        this.addStat(RuneCraftoryItems.CHOCOLATE.get(), 100, 30, 0);
        this.addStat(RuneCraftoryItems.EGG_S.get(), 200, 80, 0);
        this.addStat(RuneCraftoryItems.EGG_M.get(), 400, 100, 0);
        this.addStat(RuneCraftoryItems.EGG_L.get(), 600, 200, 0);
        this.addStat(RuneCraftoryItems.MILK_S.get(), 250, 100, 0);
        this.addStat(RuneCraftoryItems.MILK_M.get(), 500, 200, 0);
        this.addStat(RuneCraftoryItems.MILK_L.get(), 800, 350, 0);

        this.addStat(RuneCraftoryItems.ONIGIRI.get(), this.calcBuyOf(1.2, RuneCraftoryItems.RICE.get()), this.calcSellOf(1.2, RuneCraftoryItems.RICE.get()), 0);
        this.addStat(RuneCraftoryItems.CHEESE.get(), 300, 160, 0);
        this.addStat(RuneCraftoryItems.PICKLED_TURNIP.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.PICKLES.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.BAMBOO_RICE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SALMON_ONIGIRI.get(), 1000, 600, 0);
        this.addStat(RuneCraftoryItems.PICKLE_MIX.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SANDWICH.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.FRUIT_SANDWICH.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SALAD.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.RELAX_TEA_LEAVES.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.TURNIP_HEAVEN.get(), 0, 0, 0);

        this.addStat(RuneCraftoryItems.DUMPLINGS.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.FLAN.get(), 2000, 800, 0);
        this.addStat(RuneCraftoryItems.PUMPKIN_FLAN.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.STEAMED_BREAD.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.CHEESE_BREAD.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.POUND_CAKE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.CHOCOLATE_SPONGE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.CURRY_MANJU.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.CHINESE_MANJU.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.MEAT_DUMPLING.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.STEAMED_GYOZA.get(), 0, 0, 0);

        this.addStat(RuneCraftoryItems.MAYONNAISE.get(), 500, 0, 0);
        this.addStat(RuneCraftoryItems.BUTTER.get(), 900, 600, 0);
        this.addStat(RuneCraftoryItems.KETCHUP.get(), 800, 500, 0);
        this.addStat(RuneCraftoryItems.ICE_CREAM.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.APPLE_JUICE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.ORANGE_JUICE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GRAPE_JUICE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.STRAWBERRY_MILK.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.TOMATO_JUICE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.PINEAPPLE_JUICE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.FRUIT_JUICE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.FRUIT_SMOOTHIE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.VEGETABLE_JUICE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.VEGGIE_SMOOTHIE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.MIXED_JUICE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.MIXED_SMOOTHIE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.HOT_JUICE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.PRELUDE_TO_LOVE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GOLD_JUICE.get(), 0, 0, 0);

        this.addStat(RuneCraftoryItems.BAKED_ONIGIRI.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SWEET_POTATO.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.CORN_ON_THE_COB.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.BREAD.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.TOAST.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.RAISIN_BREAD.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.YAM_OF_THE_AGES.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.BUTTER_ROLL.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.JAM_ROLL.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.APPLE_PIE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.CAKE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.CHEESECAKE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.CHOCOLATE_CAKE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.COOKIE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.CHOCO_COOKIE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.DORIA.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SEAFOOD_DORIA.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.PIZZA.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SEAFOOD_PIZZA.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GRATIN.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SEAFOOD_GRATIN.get(), 0, 0, 0);

        this.addStat(RuneCraftoryItems.YOGURT.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.RICE_PORRIDGE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.MILK_PORRIDGE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.MARMALADE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.APPLE_JAM.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GRAPE_JAM.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.STRAWBERRY_JAM.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.HOT_MILK.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.HOT_CHOCOLATE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.BOILED_EGG.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.BOILED_SPINACH.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.BOILED_PUMPKIN.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.CHEESE_FONDUE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GRAPE_LIQUEUR.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GLAZED_YAM.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GRILLED_MISO.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.STEW.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.ROCKFISH_STEW.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.UNION_STEW.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.EGG_BOWL.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.TEMPURA_BOWL.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.CURRY_RICE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.UDON.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.TEMPURA_UDON.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.CURRY_UDON.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.BOILED_GYOZA.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.RELAX_TEA.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.ULTIMATE_CURRY.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.ROYAL_CURRY.get(), 0, 0, 0);

        this.addStat(RuneCraftoryItems.BAKED_APPLE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.FRIED_EGGS.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.POPCORN.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.FRENCH_FRIES.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.CORN_CEREAL.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.OMELET.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.OMELET_RICE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.FRIED_RICE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.FRIED_VEGGIES.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.FRENCH_TOAST.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.CROQUETTES.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.PANCAKES.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.DONUT.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.RISOTTO.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.MISO_EGGPLANT.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GYOZA.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.TEMPURA.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.CURRY_BREAD.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.CABBAGE_CAKES.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.DRY_CURRY.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.FRIED_UDON.get(), 0, 0, 0);

        this.addStat(RuneCraftoryItems.SALTED_CHAR.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SALTED_MASU_TROUT.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SALTED_CHERRY_SALMON.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SALTED_RAINBOW_TROUT.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SALTED_SALMON.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SALTED_TAIMEN.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SALTED_CHUB.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GRILLED_SQUID.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GRILLED_SUNSQUID.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GRILLED_LAMP_SQUID.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GRILLED_SAND_FLOUNDER.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GRILLED_SHRIMP.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GRILLED_LOBSTER.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GRILLED_BLOWFISH.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GRILLED_FALL_FLOUNDER.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GRILLED_TURBOT.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GRILLED_FLOUNDER.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SALTED_PIKE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GRILLED_NEEDLEFISH.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.DRIED_SARDINES.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.TUNA_TERIYAKI.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SALTED_POND_SMELT.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GRILLED_YELLOWTAIL.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GRILLED_MACKEREL.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GRILLED_SKIPJACK.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GRILLED_LOVER_SNAPPER.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GRILLED_GLITTER_SNAPPER.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GRILLED_GIRELLA.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GRILLED_SNAPPER.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GRILLED_GIBELIO.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GRILLED_CRUCIAN_CARP.get(), 0, 0, 0);

        this.addStat(RuneCraftoryItems.CHAR_SASHIMI.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.TROUT_SASHIMI.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.CHERRY_SASHIMI.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.RAINBOW_SASHIMI.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SALMON_SASHIMI.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.TAIMEN_SASHIMI.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SQUID_SASHIMI.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SUNSQUID_SASHIMI.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.LAMP_SQUID_SASHIMI.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SHRIMP_SASHIMI.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.LOBSTER_SASHIMI.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.BLOWFISH_SASHIMI.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.FALL_SASHIMI.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.TURBOT_SASHIMI.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.FLOUNDER_SASHIMI.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.PIKE_SASHIMI.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.NEEDLEFISH_SASHIMI.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SARDINE_SASHIMI.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.TUNA_SASHIMI.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.YELLOWTAIL_SASHIMI.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SKIPJACK_SASHIMI.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GIRELLA_SASHIMI.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.LOVER_SASHIMI.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GLITTER_SASHIMI.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SNAPPER_SASHIMI.get(), 0, 0, 0);

        this.addStat(RuneCraftoryItems.FAILED_DISH.get(), 100, 2, 1);
        this.addStat(RuneCraftoryItems.DISASTROUS_DISH.get(), 1500, 50, 1);
        this.addStat(RuneCraftoryItems.MIXED_HERBS.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SOUR_DROP.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.SWEET_POWDER.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.HEAVY_SPICE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.ORANGE.get(), 0, 0, 0);
        this.addStat(RuneCraftoryItems.GRAPES.get(), 500, 100, 1);
        this.addStat(RuneCraftoryItems.MEALY_APPLE.get(), 0, 0, 0);

        this.addStat(RuneCraftoryItems.TURNIP_SEEDS.get(), new ItemStat.Builder(100, 10, 0));
        this.addStat(RuneCraftoryItems.TURNIP_PINK_SEEDS.get(), new ItemStat.Builder(140, 14, 0));
        this.addStat(RuneCraftoryItems.CABBAGE_SEEDS.get(), new ItemStat.Builder(500, 50, 0));
        this.addStat(RuneCraftoryItems.PINK_MELON_SEEDS.get(), new ItemStat.Builder(1000, 100, 0));
        this.addStat(RuneCraftoryItems.HOT_HOT_SEEDS.get(), new ItemStat.Builder(750, 75, 0));
        this.addStat(RuneCraftoryItems.GOLD_TURNIP_SEEDS.get(), new ItemStat.Builder(5000, 500, 0));
        this.addStat(RuneCraftoryItems.GOLD_POTATO_SEEDS.get(), new ItemStat.Builder(3000, 300, 0));
        this.addStat(RuneCraftoryItems.GOLD_PUMPKIN_SEEDS.get(), new ItemStat.Builder(3500, 3500, 0));
        this.addStat(RuneCraftoryItems.GOLD_CABBAGE_SEEDS.get(), new ItemStat.Builder(2500, 250, 0));
        this.addStat(RuneCraftoryItems.BOK_CHOY_SEEDS.get(), new ItemStat.Builder(600, 60, 0));
        this.addStat(RuneCraftoryItems.LEEK_SEEDS.get(), new ItemStat.Builder(450, 45, 0));
        this.addStat(RuneCraftoryItems.RADISH_SEEDS.get(), new ItemStat.Builder(660, 66, 0));
        this.addStat(RuneCraftoryItems.GREEN_PEPPER_SEEDS.get(), new ItemStat.Builder(400, 40, 0));
        this.addStat(RuneCraftoryItems.SPINACH_SEEDS.get(), new ItemStat.Builder(120, 12, 0));
        this.addStat(RuneCraftoryItems.YAM_SEEDS.get(), new ItemStat.Builder(250, 25, 0));
        this.addStat(RuneCraftoryItems.EGGPLANT_SEEDS.get(), new ItemStat.Builder(700, 70, 0));
        this.addStat(RuneCraftoryItems.PINEAPPLE_SEEDS.get(), new ItemStat.Builder(1300, 130, 0));
        this.addStat(RuneCraftoryItems.PUMPKIN_SEEDS.get(), new ItemStat.Builder(800, 80, 0));
        this.addStat(RuneCraftoryItems.ONION_SEEDS.get(), new ItemStat.Builder(900, 90, 0));
        this.addStat(RuneCraftoryItems.CORN_SEEDS.get(), new ItemStat.Builder(830, 83, 0));
        this.addStat(RuneCraftoryItems.TOMATO_SEEDS.get(), new ItemStat.Builder(990, 99, 0));
        this.addStat(RuneCraftoryItems.STRAWBERRY_SEEDS.get(), new ItemStat.Builder(330, 33, 0));
        this.addStat(RuneCraftoryItems.CUCUMBER_SEEDS.get(), new ItemStat.Builder(230, 23, 0));
        this.addStat(RuneCraftoryItems.FODDER_SEEDS.get(), new ItemStat.Builder(50, 5, 0));
        this.addStat(RuneCraftoryItems.SWORD_SEEDS.get(), new ItemStat.Builder(1500, 500, 0));
        this.addStat(RuneCraftoryItems.SHIELD_SEEDS.get(), new ItemStat.Builder(1500, 500, 0));

        this.addStat(RuneCraftoryItems.FODDER.get(), new ItemStat.Builder(250, 35, 0));

        this.addStat(RuneCraftoryItems.TURNIP.get(), new ItemStat.Builder(230, 60, 1));
        this.addStat(RuneCraftoryItems.TURNIP_GIANT.get(), new ItemStat.Builder(0, 250, 20));
        this.addStat(RuneCraftoryItems.TURNIP_PINK.get(), new ItemStat.Builder(320, 130, 5));
        this.addStat(RuneCraftoryItems.TURNIP_PINK_GIANT.get(), new ItemStat.Builder(0, 460, 23));
        this.addStat(RuneCraftoryItems.CABBAGE.get(), new ItemStat.Builder(1200, 330, 14));
        this.addStat(RuneCraftoryItems.CABBAGE_GIANT.get(), new ItemStat.Builder(0, 780, 31));
        this.addStat(RuneCraftoryItems.PINK_MELON.get(), new ItemStat.Builder(3300, 660, 19));
        this.addStat(RuneCraftoryItems.PINK_MELON_GIANT.get(), new ItemStat.Builder(0, 1850, 29));
        this.addStat(RuneCraftoryItems.PINEAPPLE.get(), new ItemStat.Builder(7500, 2360, 53));
        this.addStat(RuneCraftoryItems.PINEAPPLE_GIANT.get(), new ItemStat.Builder(0, 6500, 76));
        this.addStat(RuneCraftoryItems.STRAWBERRY.get(), new ItemStat.Builder(1300, 370, 30));
        this.addStat(RuneCraftoryItems.STRAWBERRY_GIANT.get(), new ItemStat.Builder(0, 900, 45));
        this.addStat(RuneCraftoryItems.GOLDEN_TURNIP.get(), new ItemStat.Builder(50000, 15000, 88));
        this.addStat(RuneCraftoryItems.GOLDEN_TURNIP_GIANT.get(), new ItemStat.Builder(0, 45000, 95));
        this.addStat(RuneCraftoryItems.GOLDEN_POTATO.get(), new ItemStat.Builder(30000, 12500, 86));
        this.addStat(RuneCraftoryItems.GOLDEN_POTATO_GIANT.get(), new ItemStat.Builder(0, 30000, 90));
        this.addStat(RuneCraftoryItems.GOLDEN_PUMPKIN.get(), new ItemStat.Builder(25000, 10000, 87));
        this.addStat(RuneCraftoryItems.GOLDEN_PUMPKIN_GIANT.get(), new ItemStat.Builder(0, 23500, 93));
        this.addStat(RuneCraftoryItems.GOLDEN_CABBAGE.get(), new ItemStat.Builder(18500, 8000, 83));
        this.addStat(RuneCraftoryItems.GOLDEN_CABBAGE_GIANT.get(), new ItemStat.Builder(0, 20000, 89));
        this.addStat(RuneCraftoryItems.HOT_HOT_FRUIT.get(), new ItemStat.Builder(6000, 1000, 67));
        this.addStat(RuneCraftoryItems.HOT_HOT_FRUIT_GIANT.get(), new ItemStat.Builder(0, 2500, 79));
        this.addStat(RuneCraftoryItems.BOK_CHOY.get(), new ItemStat.Builder(1300, 440, 34));
        this.addStat(RuneCraftoryItems.BOK_CHOY_GIANT.get(), new ItemStat.Builder(0, 1100, 50));
        this.addStat(RuneCraftoryItems.LEEK.get(), new ItemStat.Builder(2300, 800, 23));
        this.addStat(RuneCraftoryItems.LEEK_GIANT.get(), new ItemStat.Builder(0, 1950, 37));
        this.addStat(RuneCraftoryItems.RADISH.get(), new ItemStat.Builder(3500, 1550, 40));
        this.addStat(RuneCraftoryItems.RADISH_GIANT.get(), new ItemStat.Builder(0, 4320, 49));
        this.addStat(RuneCraftoryItems.SPINACH.get(), new ItemStat.Builder(450, 120, 17));
        this.addStat(RuneCraftoryItems.SPINACH_GIANT.get(), new ItemStat.Builder(0, 350, 24));
        this.addStat(RuneCraftoryItems.GREEN_PEPPER.get(), new ItemStat.Builder(500, 210, 19));
        this.addStat(RuneCraftoryItems.GREEN_PEPPER_GIANT.get(), new ItemStat.Builder(0, 800, 28));
        this.addStat(RuneCraftoryItems.YAM.get(), new ItemStat.Builder(5500, 220, 20));
        this.addStat(RuneCraftoryItems.YAM_GIANT.get(), new ItemStat.Builder(0, 840, 28));
        this.addStat(RuneCraftoryItems.EGGPLANT.get(), new ItemStat.Builder(970, 310, 23));
        this.addStat(RuneCraftoryItems.EGGPLANT_GIANT.get(), new ItemStat.Builder(0, 850, 38));
        this.addStat(RuneCraftoryItems.TOMATO.get(), new ItemStat.Builder(800, 350, 25));
        this.addStat(RuneCraftoryItems.TOMATO_GIANT.get(), new ItemStat.Builder(0, 1100, 34));
        this.addStat(RuneCraftoryItems.CORN.get(), new ItemStat.Builder(2500, 1000, 37));
        this.addStat(RuneCraftoryItems.CORN_GIANT.get(), new ItemStat.Builder(0, 2800, 56));
        this.addStat(RuneCraftoryItems.CUCUMBER.get(), new ItemStat.Builder(350, 130, 12));
        this.addStat(RuneCraftoryItems.CUCUMBER_GIANT.get(), new ItemStat.Builder(0, 250, 27));
        //this.addStat(ModItems.pumpkin.get(), new ItemStat.MutableItemStat(0, 0, 0));
        //this.addStat(ModItems.pumpkinGiant.get(), new ItemStat.MutableItemStat(0, 0, 0));
        this.addStat(RuneCraftoryItems.ONION.get(), new ItemStat.Builder(1350, 450, 23));
        this.addStat(RuneCraftoryItems.ONION_GIANT.get(), new ItemStat.Builder(0, 1150, 45));

        this.addStat(Items.CARROT, new ItemStat.Builder(130, 100, 3));
        this.addStat(Items.POTATO, new ItemStat.Builder(130, 100, 3));
        this.addStat(RuneCraftoryItems.POTATO_GIANT.get(), new ItemStat.Builder(0, 450, 19));
        this.addStat(RuneCraftoryItems.CARROT_GIANT.get(), new ItemStat.Builder(0, 450, 19));

        this.addStat(RuneCraftoryItems.TOYHERB_SEEDS.get(), new ItemStat.Builder(80, 8, 0));
        this.addStat(RuneCraftoryItems.MOONDROP_SEEDS.get(), new ItemStat.Builder(190, 19, 0));
        this.addStat(RuneCraftoryItems.PINK_CAT_SEEDS.get(), new ItemStat.Builder(150, 15, 0));
        this.addStat(RuneCraftoryItems.CHARM_BLUE_SEEDS.get(), new ItemStat.Builder(130, 13, 0));
        this.addStat(RuneCraftoryItems.LAMP_GRASS_SEEDS.get(), new ItemStat.Builder(550, 55, 0));
        this.addStat(RuneCraftoryItems.CHERRY_GRASS_SEEDS.get(), new ItemStat.Builder(380, 38, 0));
        this.addStat(RuneCraftoryItems.POM_POM_GRASS_SEEDS.get(), new ItemStat.Builder(450, 45, 0));
        this.addStat(RuneCraftoryItems.AUTUMN_GRASS_SEEDS.get(), new ItemStat.Builder(900, 90, 0));
        this.addStat(RuneCraftoryItems.NOEL_GRASS_SEEDS.get(), new ItemStat.Builder(1700, 170, 0));
        this.addStat(RuneCraftoryItems.FIREFLOWER_SEEDS.get(), new ItemStat.Builder(2380, 238, 0));
        this.addStat(RuneCraftoryItems.FOUR_LEAF_CLOVER_SEEDS.get(), new ItemStat.Builder(770, 77, 0));
        this.addStat(RuneCraftoryItems.IRONLEAF_SEEDS.get(), new ItemStat.Builder(660, 66, 0));
        this.addStat(RuneCraftoryItems.WHITE_CRYSTAL_SEEDS.get(), new ItemStat.Builder(25000, 1500, 0));
        this.addStat(RuneCraftoryItems.RED_CRYSTAL_SEEDS.get(), new ItemStat.Builder(25000, 1500, 0));
        this.addStat(RuneCraftoryItems.GREEN_CRYSTAL_SEEDS.get(), new ItemStat.Builder(25000, 1500, 0));
        this.addStat(RuneCraftoryItems.BLUE_CRYSTAL_SEEDS.get(), new ItemStat.Builder(25000, 1500, 0));
        this.addStat(RuneCraftoryItems.EMERY_FLOWER_SEEDS.get(), new ItemStat.Builder(50000, 2000, 0));

        this.addStat(RuneCraftoryItems.TOYHERB.get(), new ItemStat.Builder(240, 110, 2));
        this.addStat(RuneCraftoryItems.TOYHERB_GIANT.get(), new ItemStat.Builder(0, 300, 12));
        this.addStat(RuneCraftoryItems.MOONDROP_FLOWER.get(), new ItemStat.Builder(380, 160, 5));
        this.addStat(RuneCraftoryItems.MOONDROP_FLOWER_GIANT.get(), new ItemStat.Builder(0, 360, 19));
        this.addStat(RuneCraftoryItems.PINK_CAT.get(), new ItemStat.Builder(450, 190, 6));
        this.addStat(RuneCraftoryItems.PINK_CAT_GIANT.get(), new ItemStat.Builder(0, 400, 17));
        this.addStat(RuneCraftoryItems.CHARM_BLUE.get(), new ItemStat.Builder(500, 210, 9));
        this.addStat(RuneCraftoryItems.CHARM_BLUE_GIANT.get(), new ItemStat.Builder(0, 450, 21));
        this.addStat(RuneCraftoryItems.LAMP_GRASS.get(), new ItemStat.Builder(1450, 660, 16));
        this.addStat(RuneCraftoryItems.LAMP_GRASS_GIANT.get(), new ItemStat.Builder(0, 1300, 34));
        this.addStat(RuneCraftoryItems.CHERRY_GRASS.get(), new ItemStat.Builder(750, 330, 14));
        this.addStat(RuneCraftoryItems.CHERRY_GRASS_GIANT.get(), new ItemStat.Builder(0, 800, 31));
        this.addStat(RuneCraftoryItems.POM_POM_GRASS.get(), new ItemStat.Builder(1500, 550, 19));
        this.addStat(RuneCraftoryItems.POM_POM_GRASS_GIANT.get(), new ItemStat.Builder(0, 1300, 36));
        this.addStat(RuneCraftoryItems.AUTUMN_GRASS.get(), new ItemStat.Builder(3800, 1250, 20));
        this.addStat(RuneCraftoryItems.AUTUMN_GRASS_GIANT.get(), new ItemStat.Builder(0, 2400, 39));
        this.addStat(RuneCraftoryItems.NOEL_GRASS.get(), new ItemStat.Builder(4500, 1550, 28));
        this.addStat(RuneCraftoryItems.NOEL_GRASS_GIANT.get(), new ItemStat.Builder(0, 3000, 42));
        this.addStat(RuneCraftoryItems.FIREFLOWER.get(), new ItemStat.Builder(4600, 1750, 26));
        this.addStat(RuneCraftoryItems.FIREFLOWER_GIANT.get(), new ItemStat.Builder(0, 2800, 48));
        this.addStat(RuneCraftoryItems.FOUR_LEAF_CLOVER.get(), new ItemStat.Builder(3330, 1000, 15));
        this.addStat(RuneCraftoryItems.FOUR_LEAF_CLOVER_GIANT.get(), new ItemStat.Builder(0, 2400, 29));
        this.addStat(RuneCraftoryItems.IRONLEAF.get(), new ItemStat.Builder(1500, 980, 23));
        this.addStat(RuneCraftoryItems.IRONLEAF_GIANT.get(), new ItemStat.Builder(0, 1600, 48));
        this.addStat(RuneCraftoryItems.WHITE_CRYSTAL.get(), new ItemStat.Builder(70000, 23000, 77));
        this.addStat(RuneCraftoryItems.WHITE_CRYSTAL_GIANT.get(), new ItemStat.Builder(0, 85000, 80));
        this.addStat(RuneCraftoryItems.RED_CRYSTAL.get(), new ItemStat.Builder(65000, 20000, 69));
        this.addStat(RuneCraftoryItems.RED_CRYSTAL_GIANT.get(), new ItemStat.Builder(0, 68000, 83));
        this.addStat(RuneCraftoryItems.GREEN_CRYSTAL.get(), new ItemStat.Builder(47500, 16600, 72));
        this.addStat(RuneCraftoryItems.GREEN_CRYSTAL_GIANT.get(), new ItemStat.Builder(0, 59000, 84));
        this.addStat(RuneCraftoryItems.BLUE_CRYSTAL.get(), new ItemStat.Builder(40000, 15000, 74));
        this.addStat(RuneCraftoryItems.BLUE_CRYSTAL_GIANT.get(), new ItemStat.Builder(0, 50000, 86));
        this.addStat(RuneCraftoryItems.EMERY_FLOWER.get(), new ItemStat.Builder(500000, 55000, 80));
        this.addStat(RuneCraftoryItems.EMERY_FLOWER_GIANT.get(), new ItemStat.Builder(0, 150000, 95));
    }

    private static double attackRangeFor(double range) {
        return range - RuneCraftoryAttributes.ATTACK_RANGE.get().getDefaultValue();
    }
}
