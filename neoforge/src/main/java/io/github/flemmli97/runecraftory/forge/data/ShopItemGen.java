package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.ShopItemProperties;
import io.github.flemmli97.runecraftory.api.datapack.provider.ShopItemProvider;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class ShopItemGen extends ShopItemProvider {

    public ShopItemGen(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
        super(packOutput, RuneCraftory.MODID, provider);
    }

    @Override
    protected void add(HolderLookup.Provider provider) {
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.TURNIP_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.TURNIP_PINK_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.CABBAGE_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.PINK_MELON_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.asHolder()(), ModItems.hotHotSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.asHolder()(), ModItems.goldTurnipSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.asHolder()(), ModItems.goldPotatoSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.asHolder()(), ModItems.goldPumpkinSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.asHolder()(), ModItems.goldCabbageSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.asHolder()(), ModItems.bokChoySeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.asHolder()(), ModItems.leekSeeds.get(), UnlockType.DEFAULT);
        // this.addItem(ModNPCJobs.GENERAL.asHolder()(), ModItems.radishSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.asHolder()(), ModItems.greenPepperSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.asHolder()(), ModItems.spinachSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.asHolder()(), ModItems.yamSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.asHolder()(), ModItems.eggplantSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.asHolder()(), ModItems.pineappleSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.asHolder()(), ModItems.pumpkinSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.asHolder()(), ModItems.onionSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.asHolder()(), ModItems.cornSeeds.get(), UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.TOMATO_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.CUCUMBER_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        //this.addItem(ModNPCJobs.GENERAL.asHolder()(), ModItems.fodderSeeds.get(), UnlockType.DEFAULT);

        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.TURNIP.get());
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.TURNIP_PINK.get());
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.CABBAGE.get());
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.PINK_MELON.get());
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.PINEAPPLE.get());
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.STRAWBERRY.get());
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.GOLDEN_TURNIP.get());
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.GOLDEN_POTATO.get());
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.GOLDEN_PUMPKIN.get());
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.GOLDEN_CABBAGE.get());
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.HOT_HOT_FRUIT.get());
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.BOK_CHOY.get());
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.LEEK.get());
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.RADISH.get());
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.SPINACH.get());
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.GREEN_PEPPER.get());
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.YAM.get());
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.EGGPLANT.get());
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.TOMATO.get());
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.CORN.get());
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.CUCUMBER.get());
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.PUMPKIN.get());
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.ONION.get());

        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.RICE.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.CHOCOLATE.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.FLOUR.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.OIL.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.CURRY_POWDER.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.RICE_FLOUR.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.MIXED_HERBS.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.SWEET_POWDER.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.SOUR_DROP.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.HEAVY_SPICE.get(), ShopItemProperties.UnlockType.ALWAYS);

        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.MOB_STAFF.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.BRUSH.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCJobs.GENERAL.asHolder(), ModItems.GLASS.get(), ShopItemProperties.UnlockType.ALWAYS);

        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.TOYHERB_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.MOONDROP_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.PINK_CAT_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.CHARM_BLUE_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.LAMP_GRASS_SEEDS.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.CHERRY_GRASS_SEEDS.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.POM_POM_GRASS_SEEDS.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.AUTUMN_GRASS_SEEDS.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.NOEL_GRASS_SEEDS.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.FIREFLOWER_SEEDS.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.FOUR_LEAF_CLOVER_SEEDS.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.IRONLEAF_SEEDS.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.WHITE_CRYSTAL_SEEDS.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.RED_CRYSTAL_SEEDS.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.GREEN_CRYSTAL_SEEDS.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.BLUE_CRYSTAL_SEEDS.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.EMERY_FLOWER_SEEDS.get());

        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.TOYHERB.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.MOONDROP_FLOWER.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.PINK_CAT.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.CHARM_BLUE.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.LAMP_GRASS.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.CHERRY_GRASS.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.POM_POM_GRASS.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.AUTUMN_GRASS.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.NOEL_GRASS.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.FIREFLOWER.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.FOUR_LEAF_CLOVER.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.IRONLEAF.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.WHITE_CRYSTAL.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.RED_CRYSTAL.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.GREEN_CRYSTAL.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.BLUE_CRYSTAL.get());
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.EMERY_FLOWER.get());

        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.FORMULAR_A.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.FORMULAR_B.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.FORMULAR_C.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.MINIMIZER.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.GIANTIZER.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.GREENIFIER.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.GREENIFIER_PLUS.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCJobs.FLOWER.asHolder(), ModItems.WETTABLE_POWDER.get(), ShopItemProperties.UnlockType.ALWAYS);

        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.HOE_SCRAP.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.HOE_IRON.get());
        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.HOE_SILVER.get());

        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.WATERING_CAN_SCRAP.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.WATERING_CAN_IRON.get());
        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.WATERING_CAN_SILVER.get());

        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.SICKLE_SCRAP.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.SICKLE_IRON.get());
        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.SICKLE_SILVER.get());

        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.HAMMER_SCRAP.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.HAMMER_IRON.get());
        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.HAMMER_SILVER.get());

        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.AXE_SCRAP.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.AXE_IRON.get());
        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.AXE_SILVER.get());

        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.FISHING_ROD_SCRAP.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.FISHING_ROD_IRON.get());
        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.FISHING_ROD_SILVER.get());

        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.BROAD_SWORD.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.CUTLASS.get());

        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.CLAYMORE.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.GREAT_SWORD.get());

        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.SPEAR.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.NEEDLE_SPEAR.get());

        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.BATTLE_AXE.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.POLE_AXE.get());

        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.BATTLE_HAMMER.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.WAR_HAMMER.get());

        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.SHORT_DAGGER.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.IRON_EDGE.get());

        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.LEATHER_GLOVE.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.GLOVES.get());

        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.ROD.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.SMITH.asHolder(), ModItems.AQUAMARINE_ROD.get());

        this.addItem(ModNPCJobs.DOCTOR.asHolder(), ModItems.RECOVERY_POTION.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.asHolder(), ModItems.HEALING_POTION.get());
        this.addItem(ModNPCJobs.DOCTOR.asHolder(), ModItems.MYSTERY_POTION.get());
        this.addItem(ModNPCJobs.DOCTOR.asHolder(), ModItems.MAGICAL_POTION.get());
        this.addItem(ModNPCJobs.DOCTOR.asHolder(), ModItems.ROUNDOFF.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.asHolder(), ModItems.PARA_GONE.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.asHolder(), ModItems.COLD_MED.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.asHolder(), ModItems.ANTIDOTE.get(), ShopItemProperties.UnlockType.DEFAULT);

        this.addItem(ModNPCJobs.DOCTOR.asHolder(), ModItems.ELLI_LEAVES.get());
        this.addItem(ModNPCJobs.DOCTOR.asHolder(), ModItems.WHITE_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.asHolder(), ModItems.INDIGO_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.asHolder(), ModItems.PURPLE_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.asHolder(), ModItems.GREEN_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.asHolder(), ModItems.BLUE_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.asHolder(), ModItems.YELLOW_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.asHolder(), ModItems.RED_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.asHolder(), ModItems.ORANGE_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.asHolder(), ModItems.BLACK_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.asHolder(), ModItems.ANTIDOTE_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.DOCTOR.asHolder(), ModItems.MEDICINAL_HERB.get(), ShopItemProperties.UnlockType.DEFAULT);

        for (RegistryEntrySupplier<Item, ?> sup : ModItems.FOOD) {
            this.addItem(ModNPCJobs.COOK.asHolder(), sup.get(), ShopItemProperties.UnlockType.NEEDS_SHIPPING);
        }

        this.addItem(ModNPCJobs.MAGIC.asHolder(), ModItems.FIRE_BALL_SMALL.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.MAGIC.asHolder(), ModItems.FIRE_BALL_BIG.get());
        this.addItem(ModNPCJobs.MAGIC.asHolder(), ModItems.EXPLOSION.get());
        this.addItem(ModNPCJobs.MAGIC.asHolder(), ModItems.WATER_LASER.get());
        this.addItem(ModNPCJobs.MAGIC.asHolder(), ModItems.PARALLEL_LASER.get());
        this.addItem(ModNPCJobs.MAGIC.asHolder(), ModItems.DELTA_LASER.get());
        this.addItem(ModNPCJobs.MAGIC.asHolder(), ModItems.SCREW_ROCK.get());
        this.addItem(ModNPCJobs.MAGIC.asHolder(), ModItems.EARTH_SPIKE.get());
        this.addItem(ModNPCJobs.MAGIC.asHolder(), ModItems.AVENGER_ROCK.get());
        this.addItem(ModNPCJobs.MAGIC.asHolder(), ModItems.SONIC_WIND.get());
        this.addItem(ModNPCJobs.MAGIC.asHolder(), ModItems.DOUBLE_SONIC.get());
        this.addItem(ModNPCJobs.MAGIC.asHolder(), ModItems.PENETRATE_SONIC.get());
        this.addItem(ModNPCJobs.MAGIC.asHolder(), ModItems.LIGHT_BARRIER.get());
        this.addItem(ModNPCJobs.MAGIC.asHolder(), ModItems.SHINE.get());
        this.addItem(ModNPCJobs.MAGIC.asHolder(), ModItems.PRISM.get());
        this.addItem(ModNPCJobs.MAGIC.asHolder(), ModItems.DARK_SNAKE.get());
        this.addItem(ModNPCJobs.MAGIC.asHolder(), ModItems.DARK_BALL.get());
        this.addItem(ModNPCJobs.MAGIC.asHolder(), ModItems.DARKNESS.get());
        this.addItem(ModNPCJobs.MAGIC.asHolder(), ModItems.CURE.get());
        this.addItem(ModNPCJobs.MAGIC.asHolder(), ModItems.CURE_ALL.get());
        this.addItem(ModNPCJobs.MAGIC.asHolder(), ModItems.CURE_MASTER.get());
        this.addItem(ModNPCJobs.MAGIC.asHolder(), ModItems.MEDI_POISON.get());
        this.addItem(ModNPCJobs.MAGIC.asHolder(), ModItems.MEDI_PARA.get());
        this.addItem(ModNPCJobs.MAGIC.asHolder(), ModItems.MEDI_SEAL.get());

        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.POWER_WAVE.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.DASH_SLASH.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.RUSH_ATTACK.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.ROUND_BREAK.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.MIND_THRUST.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.GUST.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.STORM.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.BLITZ.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.TWIN_ATTACK.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.RAIL_STRIKE.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.WIND_SLASH.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.FLASH_STRIKE.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.NAIVE_BLADE.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.STEEL_HEART.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.DELTA_STRIKE.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.HURRICANE.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.REAPER_SLASH.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.MILLION_STRIKE.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.AXEL_DISASTER.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.STARDUST_UPPER.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.TORNADO_SWING.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.GRAND_IMPACT.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.GIGA_SWING.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.UPPER_CUT.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.DOUBLE_KICK.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.STRAIGHT_PUNCH.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.NEKO_DAMASHI.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.RUSH_PUNCH.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.CYCLONE.get());
        this.addItem(ModNPCJobs.RUNE_SKILLS.asHolder(), ModItems.RAPID_MOVE.get());
    }
}
