package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.ShopItemProperties;
import io.github.flemmli97.runecraftory.api.datapack.provider.ShopItemProvider;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import net.minecraft.data.DataGenerator;

public class ShopItemGen extends ShopItemProvider {

    public ShopItemGen(DataGenerator gen) {
        super(gen, RuneCraftory.MODID);
    }

    @Override
    protected void add() {
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.TURNIP_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.TURNIP_PINK_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.CABBAGE_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.PINK_MELON_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.hotHotSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.goldTurnipSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.goldPotatoSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.goldPumpkinSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.goldCabbageSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.bokChoySeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.leekSeeds.get(), UnlockType.DEFAULT);
        // this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.radishSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.greenPepperSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.spinachSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.yamSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.eggplantSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.pineappleSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.pumpkinSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.onionSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.cornSeeds.get(), UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.TOMATO_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.CUCUMBER_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.fodderSeeds.get(), UnlockType.DEFAULT);

        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.TURNIP.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.TURNIP_PINK.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.CABBAGE.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.PINK_MELON.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.PINEAPPLE.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.STRAWBERRY.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.GOLDEN_TURNIP.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.GOLDEN_POTATO.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.GOLDEN_PUMPKIN.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.GOLDEN_CABBAGE.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.HOT_HOT_FRUIT.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.BOK_CHOY.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.LEEK.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.RADISH.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.SPINACH.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.GREEN_PEPPER.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.YAM.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.EGGPLANT.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.TOMATO.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.CORN.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.CUCUMBER.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.PUMPKIN.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.ONION.get());

        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.TOYHERB_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.MOONDROP_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.PINK_CAT_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.CHARM_BLUE_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.LAMP_GRASS_SEEDS.get(), ShopItemProperties.UnlockType.AFTER_UNLOCK);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.CHERRY_GRASS_SEEDS.get(), ShopItemProperties.UnlockType.AFTER_UNLOCK);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.POM_POM_GRASS_SEEDS.get(), ShopItemProperties.UnlockType.AFTER_UNLOCK);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.AUTUMN_GRASS_SEEDS.get(), ShopItemProperties.UnlockType.AFTER_UNLOCK);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.NOEL_GRASS_SEEDS.get(), ShopItemProperties.UnlockType.AFTER_UNLOCK);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.FIREFLOWER_SEEDS.get(), ShopItemProperties.UnlockType.AFTER_UNLOCK);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.FOUR_LEAF_CLOVER_SEEDS.get(), ShopItemProperties.UnlockType.AFTER_UNLOCK);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.IRONLEAF_SEEDS.get(), ShopItemProperties.UnlockType.AFTER_UNLOCK);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.WHITE_CRYSTAL_SEEDS.get(), ShopItemProperties.UnlockType.AFTER_UNLOCK);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.RED_CRYSTAL_SEEDS.get(), ShopItemProperties.UnlockType.AFTER_UNLOCK);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.GREEN_CRYSTAL_SEEDS.get(), ShopItemProperties.UnlockType.AFTER_UNLOCK);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.BLUE_CRYSTAL_SEEDS.get(), ShopItemProperties.UnlockType.AFTER_UNLOCK);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.EMERY_FLOWER_SEEDS.get(), ShopItemProperties.UnlockType.AFTER_UNLOCK);

        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.TOYHERB.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.MOONDROP_FLOWER.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.PINK_CAT.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.CHARM_BLUE.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.LAMP_GRASS.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.CHERRY_GRASS.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.POM_POM_GRASS.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.AUTUMN_GRASS.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.NOEL_GRASS.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.FIREFLOWER.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.FOUR_LEAF_CLOVER.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.IRONLEAF.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.WHITE_CRYSTAL.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.RED_CRYSTAL.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.GREEN_CRYSTAL.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.BLUE_CRYSTAL.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.EMERY_FLOWER.get());

        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.formularA.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.formularB.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.formularC.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.MINIMIZER.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.GIANTIZER.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.GREENIFIER.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.GREENIFIER_PLUS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.WETTABLE_POWDER.get(), ShopItemProperties.UnlockType.DEFAULT);

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.HOE_SCRAP.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.HOE_IRON.get());
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.HOE_SILVER.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.WATERING_CAN_SCRAP.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.WATERING_CAN_IRON.get());
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.WATERING_CAN_SILVER.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.SICKLE_SCRAP.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.SICKLE_IRON.get());
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.SICKLE_SILVER.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.HAMMER_SCRAP.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.HAMMER_IRON.get());
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.HAMMER_SILVER.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.AXE_SCRAP.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.AXE_IRON.get());
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.AXE_SILVER.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.FISHING_ROD_SCRAP.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.FISHING_ROD_IRON.get());
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.FISHING_ROD_SILVER.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.MOB_STAFF.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.BRUSH.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.GLASS.get(), ShopItemProperties.UnlockType.DEFAULT);

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.BROAD_SWORD.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.CUTLASS.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.CLAYMORE.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.GREAT_SWORD.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.SPEAR.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.NEEDLE_SPEAR.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.BATTLE_AXE.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.POLE_AXE.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.BATTLE_HAMMER.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.WAR_HAMMER.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.SHORT_DAGGER.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.IRON_EDGE.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.LEATHER_GLOVE.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.GLOVES.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.ROD.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.AQUAMARINE_ROD.get());

        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.RECOVERY_POTION.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.HEALING_POTION.get());
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.MYSTERY_POTION.get());
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.MAGICAL_POTION.get());
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.ROUNDOFF.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.PARA_GONE.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.COLD_MED.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.ANTIDOTE.get(), ShopItemProperties.UnlockType.DEFAULT);

        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.ELLI_LEAVES.get());
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.WHITE_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.INDIGO_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.PURPLE_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.GREEN_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.BLUE_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.YELLOW_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.RED_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.ORANGE_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.BLACK_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.ANTIDOTE_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.MEDICINAL_HERB.get(), ShopItemProperties.UnlockType.DEFAULT);

        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.FIRE_BALL_SMALL.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.FIRE_BALL_BIG.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.EXPLOSION.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.WATER_LASER.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.PARALLEL_LASER.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.DELTA_LASER.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.SCREW_ROCK.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.EARTH_SPIKE.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.AVENGER_ROCK.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.SONIC_WIND.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.DOUBLE_SONIC.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.PENETRATE_SONIC.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.LIGHT_BARRIER.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.SHINE.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.PRISM.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.DARK_SNAKE.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.DARK_BALL.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.DARKNESS.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.CURE.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.CURE_ALL.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.CURE_MASTER.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.MEDI_POISON.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.MEDI_PARA.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.MEDI_SEAL.get());

        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.POWER_WAVE.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.DASH_SLASH.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.RUSH_ATTACK.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.ROUND_BREAK.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.MIND_THRUST.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.GUST.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.STORM.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.BLITZ.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.TWIN_ATTACK.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.RAIL_STRIKE.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.WIND_SLASH.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.FLASH_STRIKE.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.NAIVE_BLADE.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.STEEL_HEART.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.DELTA_STRIKE.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.HURRICANE.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.REAPER_SLASH.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.MILLION_STRIKE.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.AXEL_DISASTER.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.STARDUST_UPPER.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.TORNADO_SWING.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.GRAND_IMPACT.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.GIGA_SWING.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.UPPER_CUT.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.DOUBLE_KICK.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.STRAIGHT_PUNCH.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.NEKO_DAMASHI.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.RUSH_PUNCH.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.CYCLONE.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.getSecond(), ModItems.RAPID_MOVE.get());
    }
}
