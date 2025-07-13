package io.github.flemmli97.runecraftory.neoforge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.ShopItemProperties;
import io.github.flemmli97.runecraftory.api.datapack.provider.ShopItemProvider;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModNPCProfessions;
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
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.TURNIP_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.TURNIP_PINK_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.CABBAGE_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.PINK_MELON_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        //this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder()(), ModItems.hotHotSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder()(), ModItems.goldTurnipSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder()(), ModItems.goldPotatoSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder()(), ModItems.goldPumpkinSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder()(), ModItems.goldCabbageSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder()(), ModItems.bokChoySeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder()(), ModItems.leekSeeds.get(), UnlockType.DEFAULT);
        // this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder()(), ModItems.radishSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder()(), ModItems.greenPepperSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder()(), ModItems.spinachSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder()(), ModItems.yamSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder()(), ModItems.eggplantSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder()(), ModItems.pineappleSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder()(), ModItems.pumpkinSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder()(), ModItems.onionSeeds.get(), UnlockType.DEFAULT);
        //this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder()(), ModItems.cornSeeds.get(), UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.TOMATO_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.CUCUMBER_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        //this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder()(), ModItems.fodderSeeds.get(), UnlockType.DEFAULT);

        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.TURNIP.get());
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.TURNIP_PINK.get());
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.CABBAGE.get());
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.PINK_MELON.get());
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.PINEAPPLE.get());
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.STRAWBERRY.get());
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.GOLDEN_TURNIP.get());
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.GOLDEN_POTATO.get());
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.GOLDEN_PUMPKIN.get());
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.GOLDEN_CABBAGE.get());
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.HOT_HOT_FRUIT.get());
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.BOK_CHOY.get());
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.LEEK.get());
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.RADISH.get());
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.SPINACH.get());
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.GREEN_PEPPER.get());
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.YAM.get());
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.EGGPLANT.get());
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.TOMATO.get());
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.CORN.get());
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.CUCUMBER.get());
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.PUMPKIN.get());
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.ONION.get());

        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.RICE.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.CHOCOLATE.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.FLOUR.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.OIL.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.CURRY_POWDER.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.RICE_FLOUR.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.MIXED_HERBS.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.SWEET_POWDER.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.SOUR_DROP.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.HEAVY_SPICE.get(), ShopItemProperties.UnlockType.ALWAYS);

        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.MOB_STAFF.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.BRUSH.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder(), ModItems.GLASS.get(), ShopItemProperties.UnlockType.ALWAYS);

        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.TOYHERB_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.MOONDROP_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.PINK_CAT_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.CHARM_BLUE_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.LAMP_GRASS_SEEDS.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.CHERRY_GRASS_SEEDS.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.POM_POM_GRASS_SEEDS.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.AUTUMN_GRASS_SEEDS.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.NOEL_GRASS_SEEDS.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.FIREFLOWER_SEEDS.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.FOUR_LEAF_CLOVER_SEEDS.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.IRONLEAF_SEEDS.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.WHITE_CRYSTAL_SEEDS.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.RED_CRYSTAL_SEEDS.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.GREEN_CRYSTAL_SEEDS.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.BLUE_CRYSTAL_SEEDS.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.EMERY_FLOWER_SEEDS.get());

        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.TOYHERB.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.MOONDROP_FLOWER.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.PINK_CAT.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.CHARM_BLUE.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.LAMP_GRASS.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.CHERRY_GRASS.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.POM_POM_GRASS.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.AUTUMN_GRASS.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.NOEL_GRASS.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.FIREFLOWER.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.FOUR_LEAF_CLOVER.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.IRONLEAF.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.WHITE_CRYSTAL.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.RED_CRYSTAL.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.GREEN_CRYSTAL.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.BLUE_CRYSTAL.get());
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.EMERY_FLOWER.get());

        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.FORMULAR_A.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.FORMULAR_B.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.FORMULAR_C.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.MINIMIZER.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.GIANTIZER.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.GREENIFIER.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.GREENIFIER_PLUS.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCProfessions.FLORIST.asHolder(), ModItems.WETTABLE_POWDER.get(), ShopItemProperties.UnlockType.ALWAYS);

        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.HOE_SCRAP.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.HOE_IRON.get());
        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.HOE_SILVER.get());

        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.WATERING_CAN_SCRAP.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.WATERING_CAN_IRON.get());
        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.WATERING_CAN_SILVER.get());

        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.SICKLE_SCRAP.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.SICKLE_IRON.get());
        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.SICKLE_SILVER.get());

        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.HAMMER_SCRAP.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.HAMMER_IRON.get());
        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.HAMMER_SILVER.get());

        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.AXE_SCRAP.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.AXE_IRON.get());
        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.AXE_SILVER.get());

        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.FISHING_ROD_SCRAP.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.FISHING_ROD_IRON.get());
        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.FISHING_ROD_SILVER.get());

        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.BROAD_SWORD.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.CUTLASS.get());

        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.CLAYMORE.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.GREAT_SWORD.get());

        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.SPEAR.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.NEEDLE_SPEAR.get());

        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.BATTLE_AXE.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.POLE_AXE.get());

        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.BATTLE_HAMMER.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.WAR_HAMMER.get());

        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.SHORT_DAGGER.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.IRON_EDGE.get());

        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.LEATHER_GLOVE.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.GLOVES.get());

        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.ROD.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.BLACKSMITH.asHolder(), ModItems.AQUAMARINE_ROD.get());

        this.addItem(ModNPCProfessions.DOCTOR.asHolder(), ModItems.RECOVERY_POTION.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.DOCTOR.asHolder(), ModItems.HEALING_POTION.get());
        this.addItem(ModNPCProfessions.DOCTOR.asHolder(), ModItems.MYSTERY_POTION.get());
        this.addItem(ModNPCProfessions.DOCTOR.asHolder(), ModItems.MAGICAL_POTION.get());
        this.addItem(ModNPCProfessions.DOCTOR.asHolder(), ModItems.ROUNDOFF.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.DOCTOR.asHolder(), ModItems.PARA_GONE.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.DOCTOR.asHolder(), ModItems.COLD_MED.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.DOCTOR.asHolder(), ModItems.ANTIDOTE.get(), ShopItemProperties.UnlockType.DEFAULT);

        this.addItem(ModNPCProfessions.DOCTOR.asHolder(), ModItems.ELLI_LEAVES.get());
        this.addItem(ModNPCProfessions.DOCTOR.asHolder(), ModItems.WHITE_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.DOCTOR.asHolder(), ModItems.INDIGO_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.DOCTOR.asHolder(), ModItems.PURPLE_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.DOCTOR.asHolder(), ModItems.GREEN_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.DOCTOR.asHolder(), ModItems.BLUE_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.DOCTOR.asHolder(), ModItems.YELLOW_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.DOCTOR.asHolder(), ModItems.RED_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.DOCTOR.asHolder(), ModItems.ORANGE_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.DOCTOR.asHolder(), ModItems.BLACK_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.DOCTOR.asHolder(), ModItems.ANTIDOTE_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.DOCTOR.asHolder(), ModItems.MEDICINAL_HERB.get(), ShopItemProperties.UnlockType.DEFAULT);

        for (RegistryEntrySupplier<Item, ?> sup : ModItems.FOOD) {
            this.addItem(ModNPCProfessions.CHEF.asHolder(), sup.get(), ShopItemProperties.UnlockType.NEEDS_SHIPPING);
        }

        this.addItem(ModNPCProfessions.SPELL_MERCHANT.asHolder(), ModItems.FIRE_BALL_SMALL.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.SPELL_MERCHANT.asHolder(), ModItems.FIRE_BALL_BIG.get());
        this.addItem(ModNPCProfessions.SPELL_MERCHANT.asHolder(), ModItems.EXPLOSION.get());
        this.addItem(ModNPCProfessions.SPELL_MERCHANT.asHolder(), ModItems.WATER_LASER.get());
        this.addItem(ModNPCProfessions.SPELL_MERCHANT.asHolder(), ModItems.PARALLEL_LASER.get());
        this.addItem(ModNPCProfessions.SPELL_MERCHANT.asHolder(), ModItems.DELTA_LASER.get());
        this.addItem(ModNPCProfessions.SPELL_MERCHANT.asHolder(), ModItems.SCREW_ROCK.get());
        this.addItem(ModNPCProfessions.SPELL_MERCHANT.asHolder(), ModItems.EARTH_SPIKE.get());
        this.addItem(ModNPCProfessions.SPELL_MERCHANT.asHolder(), ModItems.AVENGER_ROCK.get());
        this.addItem(ModNPCProfessions.SPELL_MERCHANT.asHolder(), ModItems.SONIC_WIND.get());
        this.addItem(ModNPCProfessions.SPELL_MERCHANT.asHolder(), ModItems.DOUBLE_SONIC.get());
        this.addItem(ModNPCProfessions.SPELL_MERCHANT.asHolder(), ModItems.PENETRATE_SONIC.get());
        this.addItem(ModNPCProfessions.SPELL_MERCHANT.asHolder(), ModItems.LIGHT_BARRIER.get());
        this.addItem(ModNPCProfessions.SPELL_MERCHANT.asHolder(), ModItems.SHINE.get());
        this.addItem(ModNPCProfessions.SPELL_MERCHANT.asHolder(), ModItems.PRISM.get());
        this.addItem(ModNPCProfessions.SPELL_MERCHANT.asHolder(), ModItems.DARK_SNAKE.get());
        this.addItem(ModNPCProfessions.SPELL_MERCHANT.asHolder(), ModItems.DARK_BALL.get());
        this.addItem(ModNPCProfessions.SPELL_MERCHANT.asHolder(), ModItems.DARKNESS.get());
        this.addItem(ModNPCProfessions.SPELL_MERCHANT.asHolder(), ModItems.CURE.get());
        this.addItem(ModNPCProfessions.SPELL_MERCHANT.asHolder(), ModItems.CURE_ALL.get());
        this.addItem(ModNPCProfessions.SPELL_MERCHANT.asHolder(), ModItems.CURE_MASTER.get());
        this.addItem(ModNPCProfessions.SPELL_MERCHANT.asHolder(), ModItems.MEDI_POISON.get());
        this.addItem(ModNPCProfessions.SPELL_MERCHANT.asHolder(), ModItems.MEDI_PARA.get());
        this.addItem(ModNPCProfessions.SPELL_MERCHANT.asHolder(), ModItems.MEDI_SEAL.get());

        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.POWER_WAVE.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.DASH_SLASH.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.RUSH_ATTACK.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.ROUND_BREAK.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.MIND_THRUST.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.GUST.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.STORM.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.BLITZ.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.TWIN_ATTACK.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.RAIL_STRIKE.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.WIND_SLASH.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.FLASH_STRIKE.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.NAIVE_BLADE.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.STEEL_HEART.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.DELTA_STRIKE.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.HURRICANE.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.REAPER_SLASH.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.MILLION_STRIKE.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.AXEL_DISASTER.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.STARDUST_UPPER.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.TORNADO_SWING.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.GRAND_IMPACT.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.GIGA_SWING.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.UPPER_CUT.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.DOUBLE_KICK.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.STRAIGHT_PUNCH.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.NEKO_DAMASHI.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.RUSH_PUNCH.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.CYCLONE.get());
        this.addItem(ModNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), ModItems.RAPID_MOVE.get());
    }
}
