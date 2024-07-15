package io.github.flemmli97.runecraftory.client;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.gui.CraftingGui;
import io.github.flemmli97.runecraftory.client.gui.FarmlandInfo;
import io.github.flemmli97.runecraftory.client.gui.InfoScreen;
import io.github.flemmli97.runecraftory.client.gui.InfoSubScreen;
import io.github.flemmli97.runecraftory.client.gui.MaxChestScreen;
import io.github.flemmli97.runecraftory.client.gui.NPCShopGui;
import io.github.flemmli97.runecraftory.client.gui.OverlayGui;
import io.github.flemmli97.runecraftory.client.gui.SpellInvOverlayGui;
import io.github.flemmli97.runecraftory.client.gui.UpgradeGui;
import io.github.flemmli97.runecraftory.client.model.AnimatedPlayerModel;
import io.github.flemmli97.runecraftory.client.model.ModelGate;
import io.github.flemmli97.runecraftory.client.model.armor.PiyoSandals;
import io.github.flemmli97.runecraftory.client.model.armor.RingsArmorModel;
import io.github.flemmli97.runecraftory.client.model.misc.ModelButterfly;
import io.github.flemmli97.runecraftory.client.model.misc.ModelChest;
import io.github.flemmli97.runecraftory.client.model.misc.ModelEnergyOrb;
import io.github.flemmli97.runecraftory.client.model.misc.ModelMissile;
import io.github.flemmli97.runecraftory.client.model.misc.ModelPlate;
import io.github.flemmli97.runecraftory.client.model.misc.ModelSpikes;
import io.github.flemmli97.runecraftory.client.model.monster.ModelAmbrosia;
import io.github.flemmli97.runecraftory.client.model.monster.ModelAnt;
import io.github.flemmli97.runecraftory.client.model.monster.ModelBeetle;
import io.github.flemmli97.runecraftory.client.model.monster.ModelBigMuck;
import io.github.flemmli97.runecraftory.client.model.monster.ModelBuffaloo;
import io.github.flemmli97.runecraftory.client.model.monster.ModelBuffamoo;
import io.github.flemmli97.runecraftory.client.model.monster.ModelChimera;
import io.github.flemmli97.runecraftory.client.model.monster.ModelChipsqueek;
import io.github.flemmli97.runecraftory.client.model.monster.ModelCluckadoodle;
import io.github.flemmli97.runecraftory.client.model.monster.ModelDeadTree;
import io.github.flemmli97.runecraftory.client.model.monster.ModelDemon;
import io.github.flemmli97.runecraftory.client.model.monster.ModelDuck;
import io.github.flemmli97.runecraftory.client.model.monster.ModelFairy;
import io.github.flemmli97.runecraftory.client.model.monster.ModelFlowerLily;
import io.github.flemmli97.runecraftory.client.model.monster.ModelGhost;
import io.github.flemmli97.runecraftory.client.model.monster.ModelGoblin;
import io.github.flemmli97.runecraftory.client.model.monster.ModelGrimoire;
import io.github.flemmli97.runecraftory.client.model.monster.ModelHornet;
import io.github.flemmli97.runecraftory.client.model.monster.ModelLeafBall;
import io.github.flemmli97.runecraftory.client.model.monster.ModelMage;
import io.github.flemmli97.runecraftory.client.model.monster.ModelMarionetta;
import io.github.flemmli97.runecraftory.client.model.monster.ModelMimic;
import io.github.flemmli97.runecraftory.client.model.monster.ModelMino;
import io.github.flemmli97.runecraftory.client.model.monster.ModelMinotaur;
import io.github.flemmli97.runecraftory.client.model.monster.ModelNappie;
import io.github.flemmli97.runecraftory.client.model.monster.ModelOrc;
import io.github.flemmli97.runecraftory.client.model.monster.ModelPalmCat;
import io.github.flemmli97.runecraftory.client.model.monster.ModelPanther;
import io.github.flemmli97.runecraftory.client.model.monster.ModelPommePomme;
import io.github.flemmli97.runecraftory.client.model.monster.ModelRaccoon;
import io.github.flemmli97.runecraftory.client.model.monster.ModelRaccoonBerserk;
import io.github.flemmli97.runecraftory.client.model.monster.ModelRafflesia;
import io.github.flemmli97.runecraftory.client.model.monster.ModelSanoUno;
import io.github.flemmli97.runecraftory.client.model.monster.ModelSarcophagus;
import io.github.flemmli97.runecraftory.client.model.monster.ModelScorpion;
import io.github.flemmli97.runecraftory.client.model.monster.ModelSkelefang;
import io.github.flemmli97.runecraftory.client.model.monster.ModelSkyFish;
import io.github.flemmli97.runecraftory.client.model.monster.ModelSpider;
import io.github.flemmli97.runecraftory.client.model.monster.ModelThunderbolt;
import io.github.flemmli97.runecraftory.client.model.monster.ModelTortas;
import io.github.flemmli97.runecraftory.client.model.monster.ModelTroll;
import io.github.flemmli97.runecraftory.client.model.monster.ModelVeggieGhost;
import io.github.flemmli97.runecraftory.client.model.monster.ModelWeagle;
import io.github.flemmli97.runecraftory.client.model.monster.ModelWisp;
import io.github.flemmli97.runecraftory.client.model.monster.ModelWolf;
import io.github.flemmli97.runecraftory.client.model.monster.ModelWooly;
import io.github.flemmli97.runecraftory.client.model.monster.ModelWoolyWool;
import io.github.flemmli97.runecraftory.client.npc.NPCFeatureRenderers;
import io.github.flemmli97.runecraftory.client.npc.RenderNPC;
import io.github.flemmli97.runecraftory.client.particles.CirclingParticle;
import io.github.flemmli97.runecraftory.client.particles.LightningParticle;
import io.github.flemmli97.runecraftory.client.particles.MoveToGoalParticle;
import io.github.flemmli97.runecraftory.client.particles.RuneyParticle;
import io.github.flemmli97.runecraftory.client.particles.SinkingParticle;
import io.github.flemmli97.runecraftory.client.particles.SkelefangParticle;
import io.github.flemmli97.runecraftory.client.particles.TornadoParticle;
import io.github.flemmli97.runecraftory.client.particles.VortexParticle;
import io.github.flemmli97.runecraftory.client.render.RenderGate;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.client.render.RenderRuney;
import io.github.flemmli97.runecraftory.client.render.RenderTreasureChest;
import io.github.flemmli97.runecraftory.client.render.monster.RenderDeadTree;
import io.github.flemmli97.runecraftory.client.render.monster.RenderDuck;
import io.github.flemmli97.runecraftory.client.render.monster.RenderGhost;
import io.github.flemmli97.runecraftory.client.render.monster.RenderGoblin;
import io.github.flemmli97.runecraftory.client.render.monster.RenderOrc;
import io.github.flemmli97.runecraftory.client.render.monster.RenderRaccoon;
import io.github.flemmli97.runecraftory.client.render.monster.RenderRafflesia;
import io.github.flemmli97.runecraftory.client.render.monster.RenderSkelefang;
import io.github.flemmli97.runecraftory.client.render.monster.RenderSpider;
import io.github.flemmli97.runecraftory.client.render.monster.RenderVeggieGhost;
import io.github.flemmli97.runecraftory.client.render.monster.RenderWisp;
import io.github.flemmli97.runecraftory.client.render.monster.RenderWooly;
import io.github.flemmli97.runecraftory.client.render.monster.ScaledEntityRenderer;
import io.github.flemmli97.runecraftory.client.render.projectiles.CustomFishingHookRenderer;
import io.github.flemmli97.runecraftory.client.render.projectiles.EmptyRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderAppleProjectile;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderBigRaccoonLeaf;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderBoneNeedle;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderBullet;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderButterfly;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderCards;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderDarkBullet;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderDarkness;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderElementBall;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderElementalTrail;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderEnergyOrb;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderFireball;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderFurnitures;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderMarionettaTrap;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderMissile;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderMobArrow;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderPlate;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderPoisonNeedle;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderRockSpear;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderSingleFrameBeam;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderSmallRaccoonLeaf;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderSpiderWeb;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderSpikes;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderThrownItem;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderWaterLaser;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderWindBlade;
import io.github.flemmli97.runecraftory.client.tooltips.UpgradeTooltipComponent;
import io.github.flemmli97.runecraftory.common.blocks.BlockBrokenMineral;
import io.github.flemmli97.runecraftory.common.blocks.BlockCrafting;
import io.github.flemmli97.runecraftory.common.blocks.BlockCrop;
import io.github.flemmli97.runecraftory.common.blocks.BlockHerb;
import io.github.flemmli97.runecraftory.common.blocks.BlockMineral;
import io.github.flemmli97.runecraftory.common.blocks.BlockTreeSapling;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityStone;
import io.github.flemmli97.runecraftory.common.inventory.container.ShippingContainer;
import io.github.flemmli97.runecraftory.common.items.BigWeapon;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolFishingRod;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemDualBladeBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemGloveBase;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModContainer;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.tenshilib.client.model.RideableModel;
import io.github.flemmli97.tenshilib.client.particles.ColoredParticle;
import io.github.flemmli97.tenshilib.client.render.RenderProjectileItem;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.HeartParticle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import org.lwjgl.glfw.GLFW;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ClientRegister {

    private static final BlockColor CROP_COLOR = (blockState, blockAndTintGetter, blockPos, i) -> {
        if (blockState.getValue(BlockCrop.WILTED))
            return 0xdc680a;
        return -1;
    };

    public static void init() {
        ClientHandlers.OVERLAY = new OverlayGui(Minecraft.getInstance());
        ClientHandlers.SPELL_DISPLAY = new SpellInvOverlayGui(Minecraft.getInstance());
        ClientHandlers.FARM_DISPLAY = new FarmlandInfo(Minecraft.getInstance());
    }

    public static void registerKeyBinding(Consumer<KeyMapping> consumer) {
        consumer.accept(ClientHandlers.SPELL_1 = new TriggerKeyBind(RuneCraftory.MODID + ".key.spell_1", GLFW.GLFW_KEY_C, RuneCraftory.MODID + ".keycategory"));
        consumer.accept(ClientHandlers.SPELL_2 = new TriggerKeyBind(RuneCraftory.MODID + ".key.spell_2", GLFW.GLFW_KEY_V, RuneCraftory.MODID + ".keycategory"));
        consumer.accept(ClientHandlers.SPELL_3 = new TriggerKeyBind(RuneCraftory.MODID + ".key.spell_3", GLFW.GLFW_KEY_G, RuneCraftory.MODID + ".keycategory"));
        consumer.accept(ClientHandlers.SPELL_4 = new TriggerKeyBind(RuneCraftory.MODID + ".key.spell_4", GLFW.GLFW_KEY_B, RuneCraftory.MODID + ".keycategory"));
    }

    public static void setupRenderLayers(BiConsumer<Block, RenderType> consumer) {
        ModBlocks.BLOCKS.getEntries().forEach(reg -> {
            if (reg.get() instanceof BlockHerb || reg.get() instanceof BlockCrop || reg.get() instanceof BlockMineral || reg.get() instanceof BlockBrokenMineral)
                consumer.accept(reg.get(), RenderType.cutout());
            if (reg.get() instanceof BlockCrafting)
                consumer.accept(reg.get(), RenderType.cutout());
            if (reg == ModBlocks.MONSTER_BARN)
                consumer.accept(reg.get(), RenderType.cutout());
            if (reg.get() instanceof LeavesBlock)
                consumer.accept(reg.get(), RenderType.cutoutMipped());
            if (reg.get() instanceof BlockTreeSapling)
                consumer.accept(reg.get(), RenderType.cutout());
        });

        consumer.accept(ModBlocks.BOSS_SPAWNER.get(), RenderType.cutout());
    }

    public static void registerItemProps(ItemModelPropsRegister register) {
        ModItems.ITEMS.getEntries().forEach(reg -> {
            if (reg.get() instanceof ItemDualBladeBase || reg.get() instanceof BigWeapon)
                register.register(reg.get(), ItemModelProps.HELD_ID, ItemModelProps.HELD_MAIN_PROP);
            else if (reg.get() instanceof ItemGloveBase)
                register.register(reg.get(), ItemModelProps.GLOVE_HELD_ID, ItemModelProps.HELD_MAIN_GLOVE);
            else if (reg.get() instanceof ItemToolFishingRod)
                register.register(reg.get(), ItemModelProps.FISHING_ROD_ID, ItemModelProps.FISHING_RODS);
            else if (reg == ModItems.NPC_BABY)
                register.register(reg.get(), ItemModelProps.BABY_GENDER, ItemModelProps.BABY_GENDER_PROPS);
            else if (reg.get() instanceof ShieldItem)
                register.register(reg.get(), new ResourceLocation("blocking"), (itemStack, clientLevel, livingEntity, i) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0f : 0.0f);
        });
    }

    public static void registerBlockColors(BiConsumer<BlockColor, Block> cons) {
        ModBlocks.CROPS.forEach(reg -> cons.accept(CROP_COLOR, reg.get()));
        ModBlocks.FLOWERS.forEach(reg -> cons.accept(CROP_COLOR, reg.get()));
        BlockColor leaves = (blockState, blockAndTintGetter, blockPos, i) -> {
            if (blockAndTintGetter == null || blockPos == null) {
                return FoliageColor.getDefaultColor();
            }
            return BiomeColors.getAverageFoliageColor(blockAndTintGetter, blockPos);
        };
        cons.accept(leaves, ModBlocks.APPLE_LEAVES.get());
        cons.accept(leaves, ModBlocks.APPLE.get());
        cons.accept(leaves, ModBlocks.ORANGE_LEAVES.get());
        cons.accept(leaves, ModBlocks.ORANGE.get());
        cons.accept(leaves, ModBlocks.GRAPE_LEAVES.get());
        cons.accept(leaves, ModBlocks.GRAPE.get());
    }

    public static void registerScreen(MenuScreenRegister factory) {
        factory.register(ModContainer.CRAFTING_CONTAINER.get(), (CraftingGui::new));
        factory.register(ModContainer.UPGRADE_CONTAINER.get(), UpgradeGui::new);
        factory.register(ModContainer.INFO_CONTAINER.get(), InfoScreen::new);
        factory.register(ModContainer.INFO_SUB_CONTAINER.get(), InfoSubScreen::new);
        factory.register(ModContainer.SHIPPING_CONTAINER.get(), MaxChestScreen<ShippingContainer>::new);
        factory.register(ModContainer.SHOP_CONTAINER.get(), NPCShopGui::new);
    }

    public static <T extends Entity> void registerRenderers(EntityRendererRegister consumer) {
        consumer.register(ModEntities.GATE.get(), RenderGate::new);

        consumer.register(ModEntities.WOOLY.get(), RenderWooly::new);
        consumer.register(ModEntities.ORC.get(), ctx -> new RenderOrc<>(ctx, mobTexture(ModEntities.ORC.get())));
        consumer.register(ModEntities.ORC_ARCHER.get(), ctx -> new RenderOrc<>(ctx, mobTexture(ModEntities.ORC.get())));
        registerScaled(consumer, ModEntities.ANT.get(), ModelAnt::new, ModelAnt.LAYER_LOCATION, 0.7f, 0.6f);
        register(consumer, ModEntities.BEETLE.get(), ModelBeetle::new, ModelBeetle.LAYER_LOCATION);
        register(consumer, ModEntities.BIG_MUCK.get(), ModelBigMuck::new, ModelBigMuck.LAYER_LOCATION);
        register(consumer, ModEntities.BUFFAMOO.get(), ModelBuffamoo::new, ModelBuffamoo.LAYER_LOCATION, 0.9f);
        register(consumer, ModEntities.CHIPSQUEEK.get(), ModelChipsqueek::new, ModelChipsqueek.LAYER_LOCATION, 0.4f);
        register(consumer, ModEntities.CLUCKADOODLE.get(), ModelCluckadoodle::new, ModelCluckadoodle.LAYER_LOCATION, 0.35f);
        register(consumer, ModEntities.POMME_POMME.get(), ModelPommePomme::new, ModelPommePomme.LAYER_LOCATION);
        register(consumer, ModEntities.TORTAS.get(), ModelTortas::new, ModelTortas.LAYER_LOCATION, 0.8f);
        register(consumer, ModEntities.SKY_FISH.get(), ModelSkyFish::new, ModelSkyFish.LAYER_LOCATION);
        register(consumer, ModEntities.WEAGLE.get(), ModelWeagle::new, ModelWeagle.LAYER_LOCATION);
        consumer.register(ModEntities.GOBLIN.get(), RenderGoblin::new);
        consumer.register(ModEntities.GOBLIN_ARCHER.get(), RenderGoblin::new);
        consumer.register(ModEntities.DUCK.get(), ctx -> new RenderDuck<>(ctx, mobTexture(ModEntities.DUCK.get()), mobTexture(ModEntities.DUCK.get(), "_asleep")));
        registerScaled(consumer, ModEntities.FAIRY.get(), ModelFairy::new, ModelFairy.LAYER_LOCATION, 0.75f, 0.3f);
        consumer.register(ModEntities.GHOST.get(), ctx -> new RenderGhost<>(ctx, mobTexture(ModEntities.GHOST.get())));
        consumer.register(ModEntities.SPIRIT.get(), ctx -> new RenderWisp<>(ctx, mobTexture(ModEntities.SPIRIT.get())));
        consumer.register(ModEntities.GHOST_RAY.get(), ctx -> new RenderGhost<>(ctx, mobTexture(ModEntities.GHOST_RAY.get()), 1.4f));
        consumer.register(ModEntities.SPIDER.get(), RenderSpider::new);
        register(consumer, ModEntities.SHADOW_PANTHER.get(), ModelPanther::new, ModelPanther.LAYER_LOCATION, 0.85f);
        register(consumer, ModEntities.MONSTER_BOX.get(), ModelMimic::new, ModelMimic.LAYER_LOCATION, 0.5f);
        register(consumer, ModEntities.GOBBLE_BOX.get(), ModelMimic::new, ModelMimic.LAYER_LOCATION, 0.5f);
        registerScaled(consumer, ModEntities.KILLER_ANT.get(), ModelAnt::new, ModelAnt.LAYER_LOCATION, 1, 0.7f);
        consumer.register(ModEntities.HIGH_ORC.get(), ctx -> new RenderOrc<>(ctx, mobTexture(ModEntities.HIGH_ORC.get())));
        consumer.register(ModEntities.ORC_HUNTER.get(), ctx -> new RenderOrc<>(ctx, mobTexture(ModEntities.HIGH_ORC.get())));
        register(consumer, ModEntities.HORNET.get(), ModelHornet::new, ModelHornet.LAYER_LOCATION, 0.4f);
        register(consumer, ModEntities.SILVER_WOLF.get(), ModelWolf::new, ModelWolf.LAYER_LOCATION, 0.6f);
        register(consumer, ModEntities.LEAF_BALL.get(), ModelLeafBall::new, ModelLeafBall.LAYER_LOCATION);
        register(consumer, ModEntities.FURPY.get(), ModelChipsqueek::new, ModelChipsqueek.LAYER_LOCATION, 0.4f);
        register(consumer, ModEntities.PALM_CAT.get(), ModelPalmCat::new, ModelPalmCat.LAYER_LOCATION);
        register(consumer, ModEntities.MINO.get(), ModelMino::new, ModelMino.LAYER_LOCATION);
        register(consumer, ModEntities.TRICKY_MUCK.get(), ModelBigMuck::new, ModelBigMuck.LAYER_LOCATION);
        register(consumer, ModEntities.FLOWER_LILY.get(), ModelFlowerLily::new, ModelFlowerLily.LAYER_LOCATION);
        consumer.register(ModEntities.KING_WOOLY.get(), ctx -> new RenderWooly<>(ctx, 2.5f, 1.2f));
        register(consumer, ModEntities.BUFFALOO.get(), ModelBuffaloo::new, ModelBuffaloo.LAYER_LOCATION, 0.8f);
        consumer.register(ModEntities.GOBLIN_PIRATE.get(), ctx -> new RenderGoblin<>(ctx, mobTexture(ModEntities.GOBLIN_PIRATE.get())));
        consumer.register(ModEntities.GOBLIN_GANGSTER.get(), ctx -> new RenderGoblin<>(ctx, mobTexture(ModEntities.GOBLIN_GANGSTER.get())));
        consumer.register(ModEntities.IGNIS.get(), ctx -> new RenderWisp<>(ctx, mobTexture(ModEntities.IGNIS.get())));
        register(consumer, ModEntities.SCORPION.get(), ModelScorpion::new, ModelScorpion.LAYER_LOCATION, 0.6f);
        register(consumer, ModEntities.TROLL.get(), ModelTroll::new, ModelTroll.LAYER_LOCATION, 0.8f);
        register(consumer, ModEntities.FLOWER_LION.get(), ModelFlowerLily::new, ModelFlowerLily.LAYER_LOCATION);
        consumer.register(ModEntities.TOMATO_GHOST.get(), ctx -> new RenderVeggieGhost<>(ctx, mobTexture(ModEntities.TOMATO_GHOST.get())));
        consumer.register(ModEntities.GOBLIN_CAPTAIN.get(), ctx -> new RenderGoblin<>(ctx, mobTexture(ModEntities.GOBLIN_CAPTAIN.get())));
        consumer.register(ModEntities.GOBLIN_DON.get(), ctx -> new RenderGoblin<>(ctx, mobTexture(ModEntities.GOBLIN_DON.get())));
        register(consumer, ModEntities.MINERAL_SQUEEK.get(), ModelChipsqueek::new, ModelChipsqueek.LAYER_LOCATION, 0.4f);
        register(consumer, ModEntities.NAPPIE.get(), ModelNappie::new, ModelNappie.LAYER_LOCATION);
        register(consumer, ModEntities.MALM_TIGER.get(), ModelPalmCat::new, ModelPalmCat.LAYER_LOCATION);
        register(consumer, ModEntities.LITTLE_EMPEROR.get(), ModelMage::new, ModelMage.LAYER_LOCATION, 0.4f);
        registerScaled(consumer, ModEntities.DEMON.get(), ModelDemon::new, ModelDemon.LAYER_LOCATION, 0.85f);
        register(consumer, ModEntities.ARCH_DEMON.get(), ModelDemon::new, ModelDemon.LAYER_LOCATION);
        register(consumer, ModEntities.MINOTAUR.get(), ModelMinotaur::new, ModelMinotaur.LAYER_LOCATION, 0.95f);
        register(consumer, ModEntities.MINOTAUR_KING.get(), ModelMinotaur::new, ModelMinotaur.LAYER_LOCATION, 0.95f);

        register(consumer, ModEntities.AMBROSIA.get(), ModelAmbrosia::new, ModelAmbrosia.LAYER_LOCATION);
        register(consumer, ModEntities.THUNDERBOLT.get(), ModelThunderbolt::new, ModelThunderbolt.LAYER_LOCATION, 1.1f);
        register(consumer, ModEntities.MARIONETTA.get(), ModelMarionetta::new, ModelMarionetta.LAYER_LOCATION);
        consumer.register(ModEntities.DEAD_TREE.get(), ctx -> new RenderDeadTree<>(ctx, 2));
        register(consumer, ModEntities.CHIMERA.get(), ModelChimera::new, ModelChimera.LAYER_LOCATION, 0.8f);
        consumer.register(ModEntities.RACCOON.get(), RenderRaccoon::new);
        consumer.register(ModEntities.SKELEFANG.get(), RenderSkelefang::new);
        consumer.register(ModEntities.RAFFLESIA.get(), RenderRafflesia::new);
        registerScaled(consumer, ModEntities.GRIMOIRE.get(), ModelGrimoire::new, ModelGrimoire.LAYER_LOCATION, 1.5f, 1.6f);
        registerScaled(consumer, ModEntities.SANO.get(), ModelSanoUno::new, ModelSanoUno.LAYER_LOCATION, 2, 1.9f);
        registerScaled(consumer, ModEntities.UNO.get(), ModelSanoUno::new, ModelSanoUno.LAYER_LOCATION, 2, 1.9f);
        register(consumer, ModEntities.SARCOPHAGUS.get(), ModelSarcophagus::new, ModelSarcophagus.LAYER_LOCATION, 0);

        consumer.register(ModEntities.NPC.get(), RenderNPC::new);

        consumer.register(ModEntities.TREASURE_CHEST.get(), RenderTreasureChest::new);

        consumer.register(ModEntities.ARROW.get(), RenderMobArrow::new);
        consumer.register(ModEntities.SPORE.get(), EmptyRender::new);
        consumer.register(ModEntities.GUST.get(), EmptyRender::new);
        consumer.register(ModEntities.STONE.get(), ctx -> new RenderProjectileItem<>(ctx) {
            private final ItemStack stack = new ItemStack(ModItems.STONE_ROUND.get());

            @Override
            public ItemStack getRenderItemStack(EntityStone entity) {
                return this.stack;
            }

            @Override
            public Type getRenderType(EntityStone entity) {
                return Type.NORMAL;
            }
        });
        consumer.register(ModEntities.STATUS_BALL.get(), EmptyRender::new);
        consumer.register(ModEntities.POLLEN_PUFF.get(), EmptyRender::new);
        consumer.register(ModEntities.POLLEN.get(), EmptyRender::new);
        consumer.register(ModEntities.AMBROSIA_WAVE.get(), EmptyRender::new);
        consumer.register(ModEntities.BUTTERFLY.get(), RenderButterfly::new);
        consumer.register(ModEntities.LIGHTNING_ORB_BOLT.get(), EmptyRender::new);
        consumer.register(ModEntities.LIGHTNING_BEAM.get(), EmptyRender::new);
        consumer.register(ModEntities.ELEMENTAL_TRAIL.get(), RenderElementalTrail::new);
        consumer.register(ModEntities.SPIDER_WEB.get(), RenderSpiderWeb::new);
        consumer.register(ModEntities.DARK_BEAM.get(), ctx -> new RenderSingleFrameBeam<>(ctx, RenderSingleFrameBeam.DARK_BEAM));
        consumer.register(ModEntities.CARDS.get(), RenderCards::new);
        consumer.register(ModEntities.FURNITURE.get(), RenderFurnitures::new);
        consumer.register(ModEntities.TRAP_CHEST.get(), RenderMarionettaTrap::new);
        consumer.register(ModEntities.ELEMENTAL_BALL.get(), RenderElementBall::new);
        consumer.register(ModEntities.FIRE_BALL.get(), RenderFireball::new);
        consumer.register(ModEntities.EXPLOSION.get(), EmptyRender::new);
        consumer.register(ModEntities.ROCK_SPEAR.get(), RenderRockSpear::new);
        consumer.register(ModEntities.WIND_BLADE.get(), RenderWindBlade::new);
        consumer.register(ModEntities.WATER_LASER.get(), RenderWaterLaser::new);
        consumer.register(ModEntities.LIGHT_BALL.get(), EmptyRender::new);
        consumer.register(ModEntities.DARK_BALL.get(), EmptyRender::new);
        consumer.register(ModEntities.DARKNESS.get(), RenderDarkness::new);
        consumer.register(ModEntities.BIG_PLATE.get(), RenderPlate::new);
        consumer.register(ModEntities.DARK_BULLET.get(), RenderDarkBullet::new);
        consumer.register(ModEntities.POISON_NEEDLE.get(), RenderPoisonNeedle::new);
        consumer.register(ModEntities.SLEEP_AURA.get(), EmptyRender::new);
        consumer.register(ModEntities.CIRCLING_BULLET.get(), ctx -> new RenderBullet(ctx, new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/bullet.png")));
        consumer.register(ModEntities.THROWN_ITEM.get(), RenderThrownItem::new);
        consumer.register(ModEntities.APPLE.get(), RenderAppleProjectile::new);
        consumer.register(ModEntities.SLASH_RESIDUE.get(), EmptyRender::new);
        consumer.register(ModEntities.SMALL_RACCOON_LEAF.get(), RenderSmallRaccoonLeaf::new);
        consumer.register(ModEntities.BIG_RACCOON_LEAF.get(), RenderBigRaccoonLeaf::new);
        consumer.register(ModEntities.BONE_NEEDLE.get(), RenderBoneNeedle::new);
        consumer.register(ModEntities.ENERGY_ORB.get(), RenderEnergyOrb::new);
        consumer.register(ModEntities.HOMING_SPIKES.get(), RenderSpikes::new);
        consumer.register(ModEntities.POWER_WAVE.get(), EmptyRender::new);
        consumer.register(ModEntities.GUST_ROCK.get(), EmptyRender::new);
        consumer.register(ModEntities.TORNADO.get(), EmptyRender::new);
        consumer.register(ModEntities.LIGHT_BEAM.get(), ctx -> new RenderSingleFrameBeam<>(ctx, RenderSingleFrameBeam.LIGHT_BEAM));
        consumer.register(ModEntities.MISSILE.get(), RenderMissile::new);
        consumer.register(ModEntities.STARFALL.get(), EmptyRender::new);

        consumer.register(ModEntities.RUNEY.get(), RenderRuney::new);
        consumer.register(ModEntities.STAT_BONUS.get(), EmptyRender::new);

        consumer.register(ModEntities.SPORE_CIRCLE_SUMMONER.get(), EmptyRender::new);
        consumer.register(ModEntities.BUTTERFLY_SUMMONER.get(), EmptyRender::new);
        consumer.register(ModEntities.DARK_BULLET_SUMMONER.get(), EmptyRender::new);
        consumer.register(ModEntities.ELEMENTAL_BARRAGE_SUMMONER.get(), EmptyRender::new);
        consumer.register(ModEntities.ROOT_SPIKE_SUMMONER.get(), EmptyRender::new);
        consumer.register(ModEntities.RAFFLESIA_BREATH_SUMMONER.get(), EmptyRender::new);
        consumer.register(ModEntities.RAFFLESIA_CIRCLE_SUMMONER.get(), EmptyRender::new);
        consumer.register(ModEntities.BLAZE_BARRAGE.get(), EmptyRender::new);
        consumer.register(ModEntities.WIND_BLADE_BARRAGE_SUMMONER.get(), EmptyRender::new);
        consumer.register(ModEntities.FIRE_WALL_SUMMONER.get(), EmptyRender::new);
        consumer.register(ModEntities.ELEMENTAL_CIRCLE_SUMMONER.get(), EmptyRender::new);
        consumer.register(ModEntities.STARFALL_SUMMONER.get(), EmptyRender::new);

        consumer.register(ModEntities.FISHING_HOOK.get(), CustomFishingHookRenderer::new);
        consumer.register(ModEntities.SARCOPHAGUS_TELEPORTER.get(), EmptyRender::new);

        consumer.register(ModEntities.MULTIPART.get(), EmptyRender::new);
        consumer.register(ModEntities.RAFFLESIA_HORSETAIL.get(), EmptyRender::new);
        consumer.register(ModEntities.RAFFLESIA_FLOWER.get(), EmptyRender::new);
        consumer.register(ModEntities.RAFFLESIA_PITCHER.get(), EmptyRender::new);

        consumer.register(ModEntities.SANO_AND_UNO.get(), EmptyRender::new);

        NPCFeatureRenderers.init();
    }

    private static <T extends BaseMonster, M extends EntityModel<T> & RideableModel<T>> EntityRendererProvider<? super T> getMonsterRender(Function<ModelPart, M> model, ModelLayerLocation layerLocation, ResourceLocation texture, float shadow) {
        return manager -> new RenderMonster<>(manager, model.apply(manager.bakeLayer(layerLocation)), texture, shadow);
    }

    private static <T extends BaseMonster, M extends EntityModel<T> & RideableModel<T>> EntityRendererProvider<? super T> getMonsterRenderScaled(Function<ModelPart, M> model, ModelLayerLocation layerLocation, ResourceLocation texture, float shadow, float scale) {
        return manager -> new ScaledEntityRenderer<>(manager, model.apply(manager.bakeLayer(layerLocation)), texture, scale, shadow);
    }

    private static <T extends BaseMonster, M extends EntityModel<T> & RideableModel<T>> void register(EntityRendererRegister consumer, EntityType<T> reg, Function<ModelPart, M> model, ModelLayerLocation layerLocation) {
        register(consumer, reg, model, layerLocation, 0.5f);
    }

    private static <T extends BaseMonster, M extends EntityModel<T> & RideableModel<T>> void register(EntityRendererRegister consumer, EntityType<T> reg, Function<ModelPart, M> model, ModelLayerLocation layerLocation, float shadow) {
        consumer.register(reg, getMonsterRender(model, layerLocation, mobTexture(reg), shadow));
    }

    private static <T extends BaseMonster, M extends EntityModel<T> & RideableModel<T>> void registerScaled(EntityRendererRegister consumer, EntityType<T> reg, Function<ModelPart, M> model, ModelLayerLocation layerLocation, float scale) {
        registerScaled(consumer, reg, model, layerLocation, scale, 0.5f);
    }

    private static <T extends BaseMonster, M extends EntityModel<T> & RideableModel<T>> void registerScaled(EntityRendererRegister consumer, EntityType<T> reg, Function<ModelPart, M> model, ModelLayerLocation layerLocation, float scale, float shadow) {
        consumer.register(reg, getMonsterRenderScaled(model, layerLocation, mobTexture(reg), shadow, scale));
    }

    public static ResourceLocation mobTexture(EntityType<?> reg) {
        return new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/" + PlatformUtils.INSTANCE.entities().getIDFrom(reg).getPath() + ".png");
    }

    public static ResourceLocation mobTexture(EntityType<?> reg, String append) {
        return new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/" + PlatformUtils.INSTANCE.entities().getIDFrom(reg).getPath() + append + ".png");
    }

    public static void layerRegister(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> consumer) {
        consumer.accept(ModelGate.LAYER_LOCATION, ModelGate::createBodyLayer);
        consumer.accept(ModelWooly.LAYER_LOCATION, ModelWooly::createBodyLayer);
        consumer.accept(ModelOrc.LAYER_LOCATION, ModelOrc::createBodyLayer);
        consumer.accept(ModelAnt.LAYER_LOCATION, ModelAnt::createBodyLayer);
        consumer.accept(ModelBeetle.LAYER_LOCATION, ModelBeetle::createBodyLayer);
        consumer.accept(ModelBigMuck.LAYER_LOCATION, ModelBigMuck::createBodyLayer);
        consumer.accept(ModelBuffamoo.LAYER_LOCATION, ModelBuffamoo::createBodyLayer);
        consumer.accept(ModelChipsqueek.LAYER_LOCATION, ModelChipsqueek::createBodyLayer);
        consumer.accept(ModelCluckadoodle.LAYER_LOCATION, ModelCluckadoodle::createBodyLayer);
        consumer.accept(ModelPommePomme.LAYER_LOCATION, ModelPommePomme::createBodyLayer);
        consumer.accept(ModelTortas.LAYER_LOCATION, ModelTortas::createBodyLayer);
        consumer.accept(ModelSkyFish.LAYER_LOCATION, ModelSkyFish::createBodyLayer);
        consumer.accept(ModelGoblin.LAYER_LOCATION, ModelGoblin::createBodyLayer);
        consumer.accept(ModelWeagle.LAYER_LOCATION, ModelWeagle::createBodyLayer);
        consumer.accept(ModelDuck.LAYER_LOCATION, ModelDuck::createBodyLayer);
        consumer.accept(ModelFairy.LAYER_LOCATION, ModelFairy::createBodyLayer);
        consumer.accept(ModelGhost.LAYER_LOCATION, ModelGhost::createBodyLayer);
        consumer.accept(ModelWisp.LAYER_LOCATION, ModelWisp::createBodyLayer);
        consumer.accept(ModelSpider.LAYER_LOCATION, ModelSpider::createBodyLayer);
        consumer.accept(ModelPanther.LAYER_LOCATION, ModelPanther::createBodyLayer);
        consumer.accept(ModelHornet.LAYER_LOCATION, ModelHornet::createBodyLayer);
        consumer.accept(ModelWolf.LAYER_LOCATION, ModelWolf::createBodyLayer);
        consumer.accept(ModelLeafBall.LAYER_LOCATION, ModelLeafBall::createBodyLayer);
        consumer.accept(ModelMino.LAYER_LOCATION, ModelMino::createBodyLayer);
        consumer.accept(ModelFlowerLily.LAYER_LOCATION, ModelFlowerLily::createBodyLayer);
        consumer.accept(ModelScorpion.LAYER_LOCATION, ModelScorpion::createBodyLayer);
        consumer.accept(ModelPalmCat.LAYER_LOCATION, ModelPalmCat::createBodyLayer);
        consumer.accept(ModelTroll.LAYER_LOCATION, ModelTroll::createBodyLayer);
        consumer.accept(ModelVeggieGhost.LAYER_LOCATION, ModelVeggieGhost::createBodyLayer);
        consumer.accept(ModelNappie.LAYER_LOCATION, ModelNappie::createBodyLayer);
        consumer.accept(ModelMage.LAYER_LOCATION, ModelMage::createBodyLayer);
        consumer.accept(ModelDemon.LAYER_LOCATION, ModelDemon::createBodyLayer);
        consumer.accept(ModelMinotaur.LAYER_LOCATION, ModelMinotaur::createBodyLayer);

        consumer.accept(ModelChest.LAYER_LOCATION, ModelChest::createBodyLayer);

        consumer.accept(ModelAmbrosia.LAYER_LOCATION, ModelAmbrosia::createBodyLayer);
        consumer.accept(ModelThunderbolt.LAYER_LOCATION, ModelThunderbolt::createBodyLayer);
        consumer.accept(ModelMarionetta.LAYER_LOCATION, ModelMarionetta::createBodyLayer);
        consumer.accept(ModelDeadTree.LAYER_LOCATION, ModelDeadTree::createBodyLayer);
        consumer.accept(ModelChimera.LAYER_LOCATION, ModelChimera::createBodyLayer);
        consumer.accept(ModelRaccoon.LAYER_LOCATION, ModelRaccoon::createBodyLayer);
        consumer.accept(ModelRaccoonBerserk.LAYER_LOCATION, ModelRaccoonBerserk::createBodyLayer);
        consumer.accept(ModelSkelefang.LAYER_LOCATION, ModelSkelefang::createBodyLayer);
        consumer.accept(ModelRafflesia.LAYER_LOCATION, ModelRafflesia::createBodyLayer);
        consumer.accept(ModelGrimoire.LAYER_LOCATION, ModelGrimoire::createBodyLayer);
        consumer.accept(ModelSanoUno.LAYER_LOCATION, ModelSanoUno::createBodyLayer);
        consumer.accept(ModelSarcophagus.LAYER_LOCATION, ModelSarcophagus::createBodyLayer);

        consumer.accept(ModelButterfly.LAYER_LOCATION, ModelButterfly::createBodyLayer);
        consumer.accept(ModelWoolyWool.LAYER_LOCATION, ModelWoolyWool::createBodyLayer);

        consumer.accept(RenderFurnitures.LOC_CHAIR, RenderFurnitures::chairLayer);
        consumer.accept(RenderFurnitures.LOC_CHIPSQUEEK_PLUSH, RenderFurnitures::chipSqueekPlushLayer);
        consumer.accept(RenderFurnitures.LOC_WOOLY_PLUSH, RenderFurnitures::woolyPlushLayer);

        consumer.accept(ModelEnergyOrb.LAYER_LOCATION, () -> ModelEnergyOrb.createBodyLayer(CubeDeformation.NONE));
        consumer.accept(ModelEnergyOrb.LAYER_LOCATION_LAYER, () -> ModelEnergyOrb.createBodyLayer(new CubeDeformation(1.5f)));
        consumer.accept(ModelSpikes.LAYER_LOCATION, ModelSpikes::createBodyLayer);
        consumer.accept(ModelMissile.LAYER_LOCATION, ModelMissile::createBodyLayer);

        consumer.accept(AnimatedPlayerModel.LAYER_LOCATION, AnimatedPlayerModel::createBodyLayer);
        consumer.accept(PiyoSandals.LAYER_LOCATION, PiyoSandals::createBodyLayer);
        consumer.accept(RingsArmorModel.LAYER_LOCATION, RingsArmorModel::createBodyLayer);

        consumer.accept(ModelPlate.LAYER_LOCATION, ModelPlate::createBodyLayer);
    }

    public static <T extends ParticleOptions> void registerParticles(PartileRegister consumer) {
        consumer.register(ModParticles.SINKING_DUST.get(), SinkingParticle.Factory::new);
        consumer.register(ModParticles.LIGHT.get(), ColoredParticle.LightParticleFactory::new);
        consumer.register(ModParticles.SHORT_LIGHT.get(), ParticleFactories.ShortLightParticleFactory::new);
        consumer.register(ModParticles.CROSS.get(), ColoredParticle.LightParticleFactory::new);
        consumer.register(ModParticles.BLINK.get(), ColoredParticle.LightParticleFactory::new);
        consumer.register(ModParticles.SMOKE.get(), ColoredParticle.LightParticleFactory::new);
        consumer.register(ModParticles.STATIC_LIGHT.get(), ColoredParticle.NoGravityParticleFactory::new);
        consumer.register(ModParticles.CIRCLING_LIGHT.get(), CirclingParticle.CirclingFactoryBase::new);
        consumer.register(ModParticles.VORTEX.get(), VortexParticle.VortexFactoryBase::new);
        consumer.register(ModParticles.WIND.get(), ColoredParticle.NoGravityParticleFactory::new);
        consumer.register(ModParticles.SLEEP.get(), HeartParticle.Provider::new);
        consumer.register(ModParticles.POISON.get(), HeartParticle.Provider::new);
        consumer.register(ModParticles.PARALYSIS.get(), LightningParticle.Factory::new);
        consumer.register(ModParticles.LIGHTNING.get(), LightningParticle.Factory::new);
        consumer.register(ModParticles.TORNADO.get(), TornadoParticle.TornadoFactoryBase::new);

        consumer.register(ModParticles.RUNEY.get(), RuneyParticle.Provider::new);

        consumer.register(ModParticles.SKELEFANG_BONES.get(), SkelefangParticle.SkelefangParticleFactoryBase::new);
        consumer.register(ModParticles.DURATIONAL_PARTICLE.get(), MoveToGoalParticle.ParticleFactoryBase::new);
    }

    public static <T extends TooltipComponent> void registerTooltipComponentFactories(ToolTipComponentRegister register) {
        register.register(UpgradeTooltipComponent.UpgradeComponent.class, UpgradeTooltipComponent::new);
    }

    public interface EntityRendererRegister {
        <T extends Entity> void register(EntityType<? extends T> type, EntityRendererProvider<T> provider);
    }

    public interface PartileRegister {
        <T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> provider);
    }

    public interface ItemModelPropsRegister {
        void register(Item item, ResourceLocation res, ClampedItemPropertyFunction function);
    }

    public interface MenuScreenRegister {
        <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void register(MenuType<? extends M> type, ScreenConstructor<M, U> provider);
    }

    public interface ScreenConstructor<T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> {
        U create(T var1, Inventory var2, Component var3);
    }

    public interface ToolTipComponentRegister {
        <T extends TooltipComponent> void register(Class<T> clss, Function<? super T, ? extends ClientTooltipComponent> factory);
    }
}
