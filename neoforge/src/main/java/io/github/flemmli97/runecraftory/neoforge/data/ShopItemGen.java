package io.github.flemmli97.runecraftory.neoforge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.ShopItemProperties;
import io.github.flemmli97.runecraftory.api.datapack.provider.ShopItemProvider;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCProfessions;
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
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.TURNIP_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.TURNIP_PINK_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.CABBAGE_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.PINK_MELON_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
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
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.TOMATO_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.CUCUMBER_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        //this.addItem(ModNPCProfessions.GENERAL_STORE.asHolder()(), ModItems.fodderSeeds.get(), UnlockType.DEFAULT);

        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.TURNIP.get());
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.TURNIP_PINK.get());
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.CABBAGE.get());
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.PINK_MELON.get());
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.PINEAPPLE.get());
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.STRAWBERRY.get());
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.GOLDEN_TURNIP.get());
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.GOLDEN_POTATO.get());
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.GOLDEN_PUMPKIN.get());
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.GOLDEN_CABBAGE.get());
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.HOT_HOT_FRUIT.get());
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.NAPA_CABBAGE.get());
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.LEEK.get());
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.RADISH.get());
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.SPINACH.get());
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.GREEN_PEPPER.get());
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.YAM.get());
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.EGGPLANT.get());
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.TOMATO.get());
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.CORN.get());
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.CUCUMBER.get());
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.PUMPKIN.get());
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.ONION.get());

        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.RICE.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.CHOCOLATE.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.FLOUR.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.OIL.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.CURRY_POWDER.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.RICE_FLOUR.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.MIXED_HERBS.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.SWEET_POWDER.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.SOUR_DROP.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.HEAVY_SPICE.get(), ShopItemProperties.UnlockType.ALWAYS);

        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.MOB_STAFF.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.BRUSH.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(RuneCraftoryNPCProfessions.GENERAL_STORE.asHolder(), RuneCraftoryItems.GLASS.get(), ShopItemProperties.UnlockType.ALWAYS);

        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.TOYHERB_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.MOONDROP_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.PINK_CAT_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.CHARM_BLUE_SEEDS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.LAMP_GRASS_SEEDS.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.CHERRY_GRASS_SEEDS.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.POM_POM_GRASS_SEEDS.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.AUTUMN_GRASS_SEEDS.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.NOEL_GRASS_SEEDS.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.FIREFLOWER_SEEDS.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.FOUR_LEAF_CLOVER_SEEDS.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.IRONLEAF_SEEDS.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.WHITE_CRYSTAL_SEEDS.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.RED_CRYSTAL_SEEDS.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.GREEN_CRYSTAL_SEEDS.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.BLUE_CRYSTAL_SEEDS.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.EMERY_FLOWER_SEEDS.get());

        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.TOYHERB.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.MOONDROP_FLOWER.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.PINK_CAT.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.CHARM_BLUE.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.LAMP_GRASS.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.CHERRY_GRASS.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.POM_POM_GRASS.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.AUTUMN_GRASS.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.NOEL_GRASS.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.FIREFLOWER.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.FOUR_LEAF_CLOVER.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.IRONLEAF.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.WHITE_CRYSTAL.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.RED_CRYSTAL.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.GREEN_CRYSTAL.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.BLUE_CRYSTAL.get());
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.EMERY_FLOWER.get());

        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.FORMULAR_A.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.FORMULAR_B.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.FORMULAR_C.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.MINIMIZER.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.GIANTIZER.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.GREENIFIER.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.GREENIFIER_PLUS.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(RuneCraftoryNPCProfessions.FLORIST.asHolder(), RuneCraftoryItems.WETTABLE_POWDER.get(), ShopItemProperties.UnlockType.ALWAYS);

        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.HOE_SCRAP.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.HOE_IRON.get());
        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.HOE_SILVER.get());

        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.WATERING_CAN_SCRAP.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.WATERING_CAN_IRON.get());
        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.WATERING_CAN_SILVER.get());

        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.SICKLE_SCRAP.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.SICKLE_IRON.get());
        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.SICKLE_SILVER.get());

        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.HAMMER_SCRAP.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.HAMMER_IRON.get());
        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.HAMMER_SILVER.get());

        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.AXE_SCRAP.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.AXE_IRON.get());
        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.AXE_SILVER.get());

        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.FISHING_ROD_SCRAP.get(), ShopItemProperties.UnlockType.ALWAYS);
        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.FISHING_ROD_IRON.get());
        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.FISHING_ROD_SILVER.get());

        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.BROAD_SWORD.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.CUTLASS.get());

        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.CLAYMORE.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.GREAT_SWORD.get());

        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.SPEAR.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.NEEDLE_SPEAR.get());

        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.BATTLE_AXE.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.POLE_AXE.get());

        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.BATTLE_HAMMER.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.WAR_HAMMER.get());

        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.SHORT_DAGGER.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.IRON_EDGE.get());

        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.LEATHER_GLOVE.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.GLOVES.get());

        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.ROD.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.BLACKSMITH.asHolder(), RuneCraftoryItems.AQUAMARINE_ROD.get());

        this.addItem(RuneCraftoryNPCProfessions.DOCTOR.asHolder(), RuneCraftoryItems.RECOVERY_POTION.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.DOCTOR.asHolder(), RuneCraftoryItems.HEALING_POTION.get());
        this.addItem(RuneCraftoryNPCProfessions.DOCTOR.asHolder(), RuneCraftoryItems.MYSTERY_POTION.get());
        this.addItem(RuneCraftoryNPCProfessions.DOCTOR.asHolder(), RuneCraftoryItems.MAGICAL_POTION.get());
        this.addItem(RuneCraftoryNPCProfessions.DOCTOR.asHolder(), RuneCraftoryItems.ROUNDOFF.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.DOCTOR.asHolder(), RuneCraftoryItems.PARA_GONE.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.DOCTOR.asHolder(), RuneCraftoryItems.COLD_MED.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.DOCTOR.asHolder(), RuneCraftoryItems.ANTIDOTE.get(), ShopItemProperties.UnlockType.DEFAULT);

        this.addItem(RuneCraftoryNPCProfessions.DOCTOR.asHolder(), RuneCraftoryItems.ELLI_LEAVES.get());
        this.addItem(RuneCraftoryNPCProfessions.DOCTOR.asHolder(), RuneCraftoryItems.WHITE_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.DOCTOR.asHolder(), RuneCraftoryItems.INDIGO_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.DOCTOR.asHolder(), RuneCraftoryItems.PURPLE_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.DOCTOR.asHolder(), RuneCraftoryItems.GREEN_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.DOCTOR.asHolder(), RuneCraftoryItems.BLUE_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.DOCTOR.asHolder(), RuneCraftoryItems.YELLOW_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.DOCTOR.asHolder(), RuneCraftoryItems.RED_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.DOCTOR.asHolder(), RuneCraftoryItems.ORANGE_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.DOCTOR.asHolder(), RuneCraftoryItems.BLACK_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.DOCTOR.asHolder(), RuneCraftoryItems.ANTIDOTE_GRASS.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.DOCTOR.asHolder(), RuneCraftoryItems.MEDICINAL_HERB.get(), ShopItemProperties.UnlockType.DEFAULT);

        for (RegistryEntrySupplier<Item, ?> sup : RuneCraftoryItems.FOOD) {
            this.addItem(RuneCraftoryNPCProfessions.CHEF.asHolder(), sup.get(), ShopItemProperties.UnlockType.NEEDS_SHIPPING);
        }

        this.addItem(RuneCraftoryNPCProfessions.SPELL_MERCHANT.asHolder(), RuneCraftoryItems.FIRE_BALL_SMALL.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.SPELL_MERCHANT.asHolder(), RuneCraftoryItems.FIRE_BALL_BIG.get());
        this.addItem(RuneCraftoryNPCProfessions.SPELL_MERCHANT.asHolder(), RuneCraftoryItems.EXPLOSION.get());
        this.addItem(RuneCraftoryNPCProfessions.SPELL_MERCHANT.asHolder(), RuneCraftoryItems.WATER_LASER.get());
        this.addItem(RuneCraftoryNPCProfessions.SPELL_MERCHANT.asHolder(), RuneCraftoryItems.PARALLEL_LASER.get());
        this.addItem(RuneCraftoryNPCProfessions.SPELL_MERCHANT.asHolder(), RuneCraftoryItems.DELTA_LASER.get());
        this.addItem(RuneCraftoryNPCProfessions.SPELL_MERCHANT.asHolder(), RuneCraftoryItems.SCREW_ROCK.get());
        this.addItem(RuneCraftoryNPCProfessions.SPELL_MERCHANT.asHolder(), RuneCraftoryItems.EARTH_SPIKE.get());
        this.addItem(RuneCraftoryNPCProfessions.SPELL_MERCHANT.asHolder(), RuneCraftoryItems.AVENGER_ROCK.get());
        this.addItem(RuneCraftoryNPCProfessions.SPELL_MERCHANT.asHolder(), RuneCraftoryItems.SONIC_WIND.get());
        this.addItem(RuneCraftoryNPCProfessions.SPELL_MERCHANT.asHolder(), RuneCraftoryItems.DOUBLE_SONIC.get());
        this.addItem(RuneCraftoryNPCProfessions.SPELL_MERCHANT.asHolder(), RuneCraftoryItems.PENETRATE_SONIC.get());
        this.addItem(RuneCraftoryNPCProfessions.SPELL_MERCHANT.asHolder(), RuneCraftoryItems.LIGHT_BARRIER.get());
        this.addItem(RuneCraftoryNPCProfessions.SPELL_MERCHANT.asHolder(), RuneCraftoryItems.SHINE.get());
        this.addItem(RuneCraftoryNPCProfessions.SPELL_MERCHANT.asHolder(), RuneCraftoryItems.PRISM.get());
        this.addItem(RuneCraftoryNPCProfessions.SPELL_MERCHANT.asHolder(), RuneCraftoryItems.DARK_SNAKE.get());
        this.addItem(RuneCraftoryNPCProfessions.SPELL_MERCHANT.asHolder(), RuneCraftoryItems.DARK_BALL.get());
        this.addItem(RuneCraftoryNPCProfessions.SPELL_MERCHANT.asHolder(), RuneCraftoryItems.DARKNESS.get());
        this.addItem(RuneCraftoryNPCProfessions.SPELL_MERCHANT.asHolder(), RuneCraftoryItems.CURE.get());
        this.addItem(RuneCraftoryNPCProfessions.SPELL_MERCHANT.asHolder(), RuneCraftoryItems.CURE_ALL.get());
        this.addItem(RuneCraftoryNPCProfessions.SPELL_MERCHANT.asHolder(), RuneCraftoryItems.CURE_MASTER.get());
        this.addItem(RuneCraftoryNPCProfessions.SPELL_MERCHANT.asHolder(), RuneCraftoryItems.MEDI_POISON.get());
        this.addItem(RuneCraftoryNPCProfessions.SPELL_MERCHANT.asHolder(), RuneCraftoryItems.MEDI_PARA.get());
        this.addItem(RuneCraftoryNPCProfessions.SPELL_MERCHANT.asHolder(), RuneCraftoryItems.MEDI_SEAL.get());

        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.POWER_WAVE.get(), ShopItemProperties.UnlockType.DEFAULT);
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.DASH_SLASH.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.RUSH_ATTACK.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.ROUND_BREAK.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.MIND_THRUST.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.GUST.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.STORM.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.BLITZ.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.TWIN_ATTACK.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.RAIL_STRIKE.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.WIND_SLASH.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.FLASH_STRIKE.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.NAIVE_BLADE.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.STEEL_HEART.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.DELTA_STRIKE.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.HURRICANE.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.REAPER_SLASH.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.MILLION_STRIKE.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.AXEL_DISASTER.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.STARDUST_UPPER.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.TORNADO_SWING.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.GRAND_IMPACT.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.GIGA_SWING.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.UPPER_CUT.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.DOUBLE_KICK.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.STRAIGHT_PUNCH.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.NEKO_DAMASHI.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.RUSH_PUNCH.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.CYCLONE.get());
        this.addItem(RuneCraftoryNPCProfessions.RUNE_ABILITIES_MERCHANT.asHolder(), RuneCraftoryItems.RAPID_MOVE.get());
    }
}
