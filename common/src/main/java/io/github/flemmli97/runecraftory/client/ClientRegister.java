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
import io.github.flemmli97.runecraftory.client.model.monster.AmbrosiaModel;
import io.github.flemmli97.runecraftory.client.model.monster.AntModel;
import io.github.flemmli97.runecraftory.client.model.monster.BeetleModel;
import io.github.flemmli97.runecraftory.client.model.monster.BigMuckModel;
import io.github.flemmli97.runecraftory.client.model.monster.BuffalooModel;
import io.github.flemmli97.runecraftory.client.model.monster.BuffamooModel;
import io.github.flemmli97.runecraftory.client.model.monster.ChimeraModel;
import io.github.flemmli97.runecraftory.client.model.monster.ChipsqueekModel;
import io.github.flemmli97.runecraftory.client.model.monster.CluckadoodleModel;
import io.github.flemmli97.runecraftory.client.model.monster.DemonModel;
import io.github.flemmli97.runecraftory.client.model.monster.FairyModel;
import io.github.flemmli97.runecraftory.client.model.monster.FlowerLilyModel;
import io.github.flemmli97.runecraftory.client.model.monster.GrimoireModel;
import io.github.flemmli97.runecraftory.client.model.monster.HandonettaModel;
import io.github.flemmli97.runecraftory.client.model.monster.HornetModel;
import io.github.flemmli97.runecraftory.client.model.monster.LeafBallModel;
import io.github.flemmli97.runecraftory.client.model.monster.MageModel;
import io.github.flemmli97.runecraftory.client.model.monster.MarionettaModel;
import io.github.flemmli97.runecraftory.client.model.monster.MimicModel;
import io.github.flemmli97.runecraftory.client.model.monster.MinoModel;
import io.github.flemmli97.runecraftory.client.model.monster.MinotaurModel;
import io.github.flemmli97.runecraftory.client.model.monster.NappieModel;
import io.github.flemmli97.runecraftory.client.model.monster.PalmCatModel;
import io.github.flemmli97.runecraftory.client.model.monster.PantherModel;
import io.github.flemmli97.runecraftory.client.model.monster.PommePommeModel;
import io.github.flemmli97.runecraftory.client.model.monster.SanoUnoModel;
import io.github.flemmli97.runecraftory.client.model.monster.SarcophagusModel;
import io.github.flemmli97.runecraftory.client.model.monster.ScorpionModel;
import io.github.flemmli97.runecraftory.client.model.monster.SkyFishModel;
import io.github.flemmli97.runecraftory.client.model.monster.ThunderboltModel;
import io.github.flemmli97.runecraftory.client.model.monster.TortasModel;
import io.github.flemmli97.runecraftory.client.model.monster.TrollModel;
import io.github.flemmli97.runecraftory.client.model.monster.WeagleModel;
import io.github.flemmli97.runecraftory.client.model.monster.WolfModel;
import io.github.flemmli97.runecraftory.client.particles.BlockParticle;
import io.github.flemmli97.runecraftory.client.particles.CirclingParticle;
import io.github.flemmli97.runecraftory.client.particles.LightningParticle;
import io.github.flemmli97.runecraftory.client.particles.MoveToGoalParticle;
import io.github.flemmli97.runecraftory.client.particles.RuneyParticle;
import io.github.flemmli97.runecraftory.client.particles.SinkingParticle;
import io.github.flemmli97.runecraftory.client.particles.SkelefangParticle;
import io.github.flemmli97.runecraftory.client.particles.TornadoParticle;
import io.github.flemmli97.runecraftory.client.render.RenderGate;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.client.render.RenderRuney;
import io.github.flemmli97.runecraftory.client.render.RenderTreasureChest;
import io.github.flemmli97.runecraftory.client.render.monster.DeadTreeRender;
import io.github.flemmli97.runecraftory.client.render.monster.DuckRender;
import io.github.flemmli97.runecraftory.client.render.monster.GhostRender;
import io.github.flemmli97.runecraftory.client.render.monster.GoblinRender;
import io.github.flemmli97.runecraftory.client.render.monster.OrcRender;
import io.github.flemmli97.runecraftory.client.render.monster.RaccoonRender;
import io.github.flemmli97.runecraftory.client.render.monster.RafflesiaRender;
import io.github.flemmli97.runecraftory.client.render.monster.SkelefangRender;
import io.github.flemmli97.runecraftory.client.render.monster.SpiderRender;
import io.github.flemmli97.runecraftory.client.render.monster.VeggieGhostRender;
import io.github.flemmli97.runecraftory.client.render.monster.WispRender;
import io.github.flemmli97.runecraftory.client.render.monster.WoolyRender;
import io.github.flemmli97.runecraftory.client.render.npc.NPCFeatureRenderers;
import io.github.flemmli97.runecraftory.client.render.npc.NPCRender;
import io.github.flemmli97.runecraftory.client.render.npc.NPCTextureLayer;
import io.github.flemmli97.runecraftory.client.render.projectiles.AppleProjectileRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.BigRaccoonLeafRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.BoneNeedleRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.BulletRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.ButterflyRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.CardsRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.CustomFishingHookerRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.DarkBulletRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.DarknessRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.ElementBallRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.ElementalTrailRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.EmptyRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.EnergyOrbRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.FireballRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.FurnituresRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.MarionettaTrapRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.MissileRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.MobArrowRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.PlateRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.PoisonNeedleRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.RockSpearRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.SingleFrameBeamRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.SmallRaccoonLeafRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.SpiderWebRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.SpikesRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.ThrownItemRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.WaterLaserRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.WindBladeRender;
import io.github.flemmli97.runecraftory.client.tooltips.UpgradeTooltipComponent;
import io.github.flemmli97.runecraftory.common.blocks.BrokenMineralBlock;
import io.github.flemmli97.runecraftory.common.blocks.CraftingBlock;
import io.github.flemmli97.runecraftory.common.blocks.ExtendedCropBlock;
import io.github.flemmli97.runecraftory.common.blocks.HerbBlock;
import io.github.flemmli97.runecraftory.common.blocks.MineralBlock;
import io.github.flemmli97.runecraftory.common.blocks.TreeLeavesBlock;
import io.github.flemmli97.runecraftory.common.blocks.TreeSaplingBlock;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.misc.StoneEntity;
import io.github.flemmli97.runecraftory.common.inventory.container.ShippingContainer;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolFishingRod;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemGloveBase;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryBlocks;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryFluids;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryMenuTypes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryParticles;
import io.github.flemmli97.tenshilib.client.model.RideableModel;
import io.github.flemmli97.tenshilib.client.particles.ColoredParticle;
import io.github.flemmli97.tenshilib.client.render.ItemProjectileRenderer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.HeartParticle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.material.Fluid;
import org.lwjgl.glfw.GLFW;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ClientRegister {

    public static final int HOT_SPRING_BASE = 0xff40b5e1;
    public static final int HOT_SPRING_HUE = 0xff60c9f1;

    private static final BlockColor CROP_COLOR = (blockState, blockAndTintGetter, blockPos, i) -> {
        if (blockState.getValue(ExtendedCropBlock.WILTED))
            return 0xdc680a;
        return -1;
    };
    public static final ColorResolver HOT_SPRING_COLOR = (biome, d, e) -> waterBlend(biome.getWaterColor());

    private static int waterBlend(int source) {
        int r = blendChannel(source >> 16 & 255, ClientRegister.HOT_SPRING_HUE >> 16 & 255);
        int g = blendChannel(source >> 8 & 255, ClientRegister.HOT_SPRING_HUE >> 8 & 255);
        int b = blendChannel(source & 255, ClientRegister.HOT_SPRING_HUE & 255);
        return FastColor.ARGB32.color(r, g, b);
    }

    private static int blendChannel(int source, int overlay) {
        float sourcePercent = source / 255f;
        float overlayPercent = overlay / 255f;
        if (sourcePercent < 0.5) {
            return (int) Mth.clamp(2 * sourcePercent * overlayPercent * 255, 0, 255);
        }
        return (int) Mth.clamp((1 - 2 * (1 - sourcePercent) * (1 - overlayPercent)) * 255, 0, 255);
    }

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

    public static void setupBlockRenderLayers(BiConsumer<Block, RenderType> consumer) {
        RuneCraftoryBlocks.BLOCKS.getEntries().forEach(reg -> {
            if (reg.get() instanceof HerbBlock || reg.get() instanceof ExtendedCropBlock || reg.get() instanceof MineralBlock || reg.get() instanceof BrokenMineralBlock)
                consumer.accept(reg.get(), RenderType.cutout());
            if (reg.get() instanceof CraftingBlock)
                consumer.accept(reg.get(), RenderType.cutout());
            if (reg == RuneCraftoryBlocks.MONSTER_BARN)
                consumer.accept(reg.get(), RenderType.cutout());
            if (reg.get() instanceof LeavesBlock)
                consumer.accept(reg.get(), RenderType.cutoutMipped());
            if (reg.get() instanceof TreeSaplingBlock)
                consumer.accept(reg.get(), RenderType.cutout());
        });
        consumer.accept(RuneCraftoryBlocks.BOSS_SPAWNER.get(), RenderType.cutout());
    }

    public static void setupFluidRenderLayers(BiConsumer<Fluid, RenderType> consumer) {
        consumer.accept(RuneCraftoryFluids.FLOWING_HOT_SPRING_WATER.get(), RenderType.translucent());
        consumer.accept(RuneCraftoryFluids.HOT_SPRING_WATER.get(), RenderType.translucent());
    }

    public static void registerItemProps(ItemModelPropsRegister register) {
        RuneCraftoryItems.ITEMS.getEntries().forEach(reg -> {
            if (reg.get() instanceof ItemGloveBase)
                register.register(reg.get(), ItemModelProps.SLIM_PLAYER_ID, ItemModelProps.SLIM_PLAYER_PROPERTY);
            else if (reg.get() instanceof ItemToolFishingRod)
                register.register(reg.get(), ItemModelProps.FISHING_ROD_ID, ItemModelProps.FISHING_RODS);
            else if (reg == RuneCraftoryItems.NPC_BABY)
                register.register(reg.get(), ItemModelProps.BABY_GENDER, ItemModelProps.BABY_GENDER_PROPS);
            else if (reg.get() instanceof ShieldItem)
                register.register(reg.get(), ResourceLocation.withDefaultNamespace("blocking"), (itemStack, clientLevel, livingEntity, i) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0f : 0.0f);
        });
    }

    public static void registerBlockColors(BiConsumer<BlockColor, Block> cons) {
        RuneCraftoryBlocks.CROPS.forEach(reg -> cons.accept(CROP_COLOR, reg.get()));
        RuneCraftoryBlocks.FLOWERS.forEach(reg -> cons.accept(CROP_COLOR, reg.get()));
        BlockColor leaves = (blockState, blockAndTintGetter, blockPos, i) -> {
            if (blockState.hasProperty(TreeLeavesBlock.WILTED) && blockState.getValue(TreeLeavesBlock.WILTED))
                return 0xdc680a;
            if (blockAndTintGetter == null || blockPos == null) {
                return FoliageColor.getDefaultColor();
            }
            return BiomeColors.getAverageFoliageColor(blockAndTintGetter, blockPos);
        };
        cons.accept(leaves, RuneCraftoryBlocks.APPLE_LEAVES.get());
        cons.accept(leaves, RuneCraftoryBlocks.APPLE.get());
        cons.accept(leaves, RuneCraftoryBlocks.ORANGE_LEAVES.get());
        cons.accept(leaves, RuneCraftoryBlocks.ORANGE.get());
        cons.accept(leaves, RuneCraftoryBlocks.GRAPE_LEAVES.get());
        cons.accept(leaves, RuneCraftoryBlocks.GRAPE.get());
    }

    public static void registerScreen(MenuScreenRegister factory) {
        factory.register(RuneCraftoryMenuTypes.CRAFTING_CONTAINER.get(), (CraftingGui::new));
        factory.register(RuneCraftoryMenuTypes.UPGRADE_CONTAINER.get(), UpgradeGui::new);
        factory.register(RuneCraftoryMenuTypes.INFO_CONTAINER.get(), InfoScreen::new);
        factory.register(RuneCraftoryMenuTypes.INFO_SUB_CONTAINER.get(), InfoSubScreen::new);
        factory.register(RuneCraftoryMenuTypes.SHIPPING_CONTAINER.get(), MaxChestScreen<ShippingContainer>::new);
        factory.register(RuneCraftoryMenuTypes.SHOP_CONTAINER.get(), NPCShopGui::new);
    }

    public static void registerRenderers(EntityRendererRegister consumer) {
        consumer.register(RuneCraftoryEntities.GATE.get(), RenderGate::new);

        consumer.register(RuneCraftoryEntities.WOOLY.get(), WoolyRender::new);
        consumer.register(RuneCraftoryEntities.ORC.get(), ctx -> new OrcRender<>(ctx, mobTexture(RuneCraftoryEntities.ORC.get())));
        consumer.register(RuneCraftoryEntities.ORC_ARCHER.get(), ctx -> new OrcRender<>(ctx, mobTexture(RuneCraftoryEntities.ORC.get())));
        register(consumer, RuneCraftoryEntities.ANT.get(), AntModel::new, 0.8f);
        register(consumer, RuneCraftoryEntities.BEETLE.get(), BeetleModel::new, 0.6f);
        register(consumer, RuneCraftoryEntities.BIG_MUCK.get(), BigMuckModel::new, 0.6f);
        register(consumer, RuneCraftoryEntities.BUFFAMOO.get(), BuffamooModel::new, 0.8f);
        register(consumer, RuneCraftoryEntities.CHIPSQUEEK.get(), ChipsqueekModel::new, 0.4f);
        register(consumer, RuneCraftoryEntities.CLUCKADOODLE.get(), CluckadoodleModel::new, 0.35f);
        register(consumer, RuneCraftoryEntities.POMME_POMME.get(), PommePommeModel::new, 0.7f);
        register(consumer, RuneCraftoryEntities.TORTAS.get(), TortasModel::new, 0.8f);
        register(consumer, RuneCraftoryEntities.SKY_FISH.get(), SkyFishModel::new);
        register(consumer, RuneCraftoryEntities.WEAGLE.get(), WeagleModel::new);
        consumer.register(RuneCraftoryEntities.GOBLIN.get(), GoblinRender::new);
        consumer.register(RuneCraftoryEntities.GOBLIN_ARCHER.get(), GoblinRender::new);
        consumer.register(RuneCraftoryEntities.DUCK.get(), ctx -> new DuckRender<>(ctx, mobTexture(RuneCraftoryEntities.DUCK.get()), mobTexture(RuneCraftoryEntities.DUCK.get(), "_asleep")));
        register(consumer, RuneCraftoryEntities.FAIRY.get(), FairyModel::new, 0.3f);
        consumer.register(RuneCraftoryEntities.GHOST.get(), ctx -> new GhostRender<>(ctx, mobTexture(RuneCraftoryEntities.GHOST.get())));
        consumer.register(RuneCraftoryEntities.SPIRIT.get(), ctx -> new WispRender<>(ctx, mobTexture(RuneCraftoryEntities.SPIRIT.get())));
        consumer.register(RuneCraftoryEntities.GHOST_RAY.get(), ctx -> new GhostRender<>(ctx, mobTexture(RuneCraftoryEntities.GHOST_RAY.get())));
        consumer.register(RuneCraftoryEntities.SPIDER.get(), SpiderRender::new);
        register(consumer, RuneCraftoryEntities.SHADOW_PANTHER.get(), PantherModel::new, 0.85f);
        register(consumer, RuneCraftoryEntities.MONSTER_BOX.get(), MimicModel::new, 0.6f);
        register(consumer, RuneCraftoryEntities.GOBBLE_BOX.get(), MimicModel::new, 0.6f);
        register(consumer, RuneCraftoryEntities.KILLER_ANT.get(), AntModel::new, 0.8f);
        consumer.register(RuneCraftoryEntities.HIGH_ORC.get(), ctx -> new OrcRender<>(ctx, mobTexture(RuneCraftoryEntities.HIGH_ORC.get())));
        consumer.register(RuneCraftoryEntities.ORC_HUNTER.get(), ctx -> new OrcRender<>(ctx, mobTexture(RuneCraftoryEntities.HIGH_ORC.get())));
        register(consumer, RuneCraftoryEntities.HORNET.get(), HornetModel::new);
        register(consumer, RuneCraftoryEntities.SILVER_WOLF.get(), WolfModel::new, 0.7f);
        register(consumer, RuneCraftoryEntities.LEAF_BALL.get(), LeafBallModel::new);
        register(consumer, RuneCraftoryEntities.FURPY.get(), ChipsqueekModel::new, 0.4f);
        register(consumer, RuneCraftoryEntities.PALM_CAT.get(), PalmCatModel::new);
        register(consumer, RuneCraftoryEntities.MINO.get(), MinoModel::new, 0.7f);
        register(consumer, RuneCraftoryEntities.TRICKY_MUCK.get(), BigMuckModel::new, 0.6f);
        register(consumer, RuneCraftoryEntities.FLOWER_LILY.get(), FlowerLilyModel::new);
        consumer.register(RuneCraftoryEntities.KING_WOOLY.get(), WoolyRender::new);
        register(consumer, RuneCraftoryEntities.BUFFALOO.get(), BuffalooModel::new, 0.8f);
        consumer.register(RuneCraftoryEntities.GOBLIN_PIRATE.get(), ctx -> new GoblinRender<>(ctx, mobTexture(RuneCraftoryEntities.GOBLIN_PIRATE.get())));
        consumer.register(RuneCraftoryEntities.GOBLIN_GANGSTER.get(), ctx -> new GoblinRender<>(ctx, mobTexture(RuneCraftoryEntities.GOBLIN_GANGSTER.get())));
        consumer.register(RuneCraftoryEntities.IGNIS.get(), ctx -> new WispRender<>(ctx, mobTexture(RuneCraftoryEntities.IGNIS.get())));
        register(consumer, RuneCraftoryEntities.SCORPION.get(), ScorpionModel::new, 0.8f);
        register(consumer, RuneCraftoryEntities.TROLL.get(), TrollModel::new, 0.8f);
        register(consumer, RuneCraftoryEntities.FLOWER_LION.get(), FlowerLilyModel::new);
        consumer.register(RuneCraftoryEntities.TOMATO_GHOST.get(), ctx -> new VeggieGhostRender<>(ctx, mobTexture(RuneCraftoryEntities.TOMATO_GHOST.get())));
        consumer.register(RuneCraftoryEntities.GOBLIN_CAPTAIN.get(), ctx -> new GoblinRender<>(ctx, mobTexture(RuneCraftoryEntities.GOBLIN_CAPTAIN.get())));
        consumer.register(RuneCraftoryEntities.GOBLIN_DON.get(), ctx -> new GoblinRender<>(ctx, mobTexture(RuneCraftoryEntities.GOBLIN_DON.get())));
        register(consumer, RuneCraftoryEntities.MINERAL_SQUEEK.get(), ChipsqueekModel::new, 0.4f);
        register(consumer, RuneCraftoryEntities.NAPPIE.get(), NappieModel::new, 0.7f);
        register(consumer, RuneCraftoryEntities.MALM_TIGER.get(), PalmCatModel::new);
        register(consumer, RuneCraftoryEntities.LITTLE_EMPEROR.get(), MageModel::new, 0.4f);
        register(consumer, RuneCraftoryEntities.DEMON.get(), DemonModel::new);
        register(consumer, RuneCraftoryEntities.ARCH_DEMON.get(), DemonModel::new);
        register(consumer, RuneCraftoryEntities.MINOTAUR.get(), MinotaurModel::new, 0.95f);
        register(consumer, RuneCraftoryEntities.MINOTAUR_KING.get(), MinotaurModel::new, 0.95f);

        register(consumer, RuneCraftoryEntities.AMBROSIA.get(), AmbrosiaModel::new);
        register(consumer, RuneCraftoryEntities.THUNDERBOLT.get(), ThunderboltModel::new, 1.1f);
        register(consumer, RuneCraftoryEntities.MARIONETTA.get(), MarionettaModel::new);
        register(consumer, RuneCraftoryEntities.HANDONETTA.get(), HandonettaModel::new, 1);
        consumer.register(RuneCraftoryEntities.DEAD_TREE.get(), DeadTreeRender::new);
        register(consumer, RuneCraftoryEntities.CHIMERA.get(), ChimeraModel::new, 0.8f);
        consumer.register(RuneCraftoryEntities.RACCOON.get(), RaccoonRender::new);
        consumer.register(RuneCraftoryEntities.SKELEFANG.get(), SkelefangRender::new);
        consumer.register(RuneCraftoryEntities.RAFFLESIA.get(), RafflesiaRender::new);
        register(consumer, RuneCraftoryEntities.GRIMOIRE.get(), GrimoireModel::new, 1.3f);
        register(consumer, RuneCraftoryEntities.SANO.get(), SanoUnoModel::new, 1.5f);
        register(consumer, RuneCraftoryEntities.UNO.get(), SanoUnoModel::new, 1.5f);
        register(consumer, RuneCraftoryEntities.SARCOPHAGUS.get(), SarcophagusModel::new, 0);

        consumer.register(RuneCraftoryEntities.NPC.get(), NPCRender::new);

        consumer.register(RuneCraftoryEntities.TREASURE_CHEST.get(), RenderTreasureChest::new);

        consumer.register(RuneCraftoryEntities.ARROW.get(), MobArrowRender::new);
        consumer.register(RuneCraftoryEntities.SPORE.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.GUST.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.STONE.get(), ctx -> new ItemProjectileRenderer<>(ctx) {
            private final ItemStack stack = new ItemStack(RuneCraftoryItems.STONE_ROUND.get());

            @Override
            public Type getRenderType(StoneEntity entity) {
                return Type.NORMAL;
            }

            @Override
            public ItemStack getRenderItemStack(StoneEntity entity) {
                return this.stack;
            }
        });
        consumer.register(RuneCraftoryEntities.STATUS_BALL.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.POLLEN_PUFF.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.POLLEN.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.AMBROSIA_WAVE.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.BUTTERFLY.get(), ButterflyRender::new);
        consumer.register(RuneCraftoryEntities.LIGHTNING_ORB_BOLT.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.LIGHTNING_BEAM.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.ELEMENTAL_TRAIL.get(), ElementalTrailRender::new);
        consumer.register(RuneCraftoryEntities.SPIDER_WEB.get(), SpiderWebRender::new);
        consumer.register(RuneCraftoryEntities.DARK_BEAM.get(), ctx -> new SingleFrameBeamRender<>(ctx, SingleFrameBeamRender.DARK_BEAM));
        consumer.register(RuneCraftoryEntities.CARDS.get(), CardsRender::new);
        consumer.register(RuneCraftoryEntities.FURNITURE.get(), FurnituresRender::new);
        consumer.register(RuneCraftoryEntities.TRAP_CHEST.get(), MarionettaTrapRender::new);
        consumer.register(RuneCraftoryEntities.ELEMENTAL_BALL.get(), ElementBallRender::new);
        consumer.register(RuneCraftoryEntities.FIRE_BALL.get(), FireballRender::new);
        consumer.register(RuneCraftoryEntities.EXPLOSION.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.ROCK_SPEAR.get(), RockSpearRender::new);
        consumer.register(RuneCraftoryEntities.WIND_BLADE.get(), WindBladeRender::new);
        consumer.register(RuneCraftoryEntities.WATER_LASER.get(), WaterLaserRender::new);
        consumer.register(RuneCraftoryEntities.SWIPING_WATER_LASER.get(), WaterLaserRender::new);
        consumer.register(RuneCraftoryEntities.LIGHT_BALL.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.DARK_BALL.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.DARKNESS.get(), DarknessRender::new);
        consumer.register(RuneCraftoryEntities.BIG_PLATE.get(), PlateRender::new);
        consumer.register(RuneCraftoryEntities.DARK_BULLET.get(), DarkBulletRender::new);
        consumer.register(RuneCraftoryEntities.POISON_NEEDLE.get(), PoisonNeedleRender::new);
        consumer.register(RuneCraftoryEntities.SLEEP_AURA.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.CIRCLING_BULLET.get(), ctx -> new BulletRender(ctx, RuneCraftory.modRes("textures/entity/projectile/bullet.png")));
        consumer.register(RuneCraftoryEntities.THROWN_ITEM.get(), ThrownItemRender::new);
        consumer.register(RuneCraftoryEntities.APPLE.get(), AppleProjectileRender::new);
        consumer.register(RuneCraftoryEntities.SLASH_RESIDUE.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.SMALL_RACCOON_LEAF.get(), SmallRaccoonLeafRender::new);
        consumer.register(RuneCraftoryEntities.BIG_RACCOON_LEAF.get(), BigRaccoonLeafRender::new);
        consumer.register(RuneCraftoryEntities.BONE_NEEDLE.get(), BoneNeedleRender::new);
        consumer.register(RuneCraftoryEntities.ENERGY_ORB.get(), EnergyOrbRender::new);
        consumer.register(RuneCraftoryEntities.HOMING_SPIKES.get(), SpikesRender::new);
        consumer.register(RuneCraftoryEntities.POWER_WAVE.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.GUST_ROCK.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.TORNADO.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.LIGHT_BEAM.get(), ctx -> new SingleFrameBeamRender<>(ctx, SingleFrameBeamRender.LIGHT_BEAM));
        consumer.register(RuneCraftoryEntities.MISSILE.get(), MissileRender::new);
        consumer.register(RuneCraftoryEntities.STARFALL.get(), EmptyRender::new);

        consumer.register(RuneCraftoryEntities.RUNEY.get(), RenderRuney::new);
        consumer.register(RuneCraftoryEntities.STAT_BONUS.get(), EmptyRender::new);

        consumer.register(RuneCraftoryEntities.SPORE_CIRCLE_SUMMONER.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.BUTTERFLY_SUMMONER.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.DARK_BULLET_SUMMONER.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.ELEMENTAL_BARRAGE_SUMMONER.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.ROOT_SPIKE_SUMMONER.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.RAFFLESIA_BREATH_SUMMONER.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.RAFFLESIA_CIRCLE_SUMMONER.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.BLAZE_BARRAGE.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.WIND_BLADE_BARRAGE_SUMMONER.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.FIRE_WALL_SUMMONER.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.ELEMENTAL_CIRCLE_SUMMONER.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.STARFALL_SUMMONER.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.GROUND_SHAKE_PARTICLES.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.APPLE_RAIN_SUMMONER.get(), EmptyRender::new);

        consumer.register(RuneCraftoryEntities.FISHING_HOOK.get(), CustomFishingHookerRender::new);
        consumer.register(RuneCraftoryEntities.SARCOPHAGUS_TELEPORTER.get(), EmptyRender::new);

        consumer.register(RuneCraftoryEntities.MULTIPART.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.RAFFLESIA_HORSETAIL.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.RAFFLESIA_FLOWER.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.RAFFLESIA_PITCHER.get(), EmptyRender::new);
        consumer.register(RuneCraftoryEntities.HOE_TILLABLE_ITEM_ENTITY.get(), ItemEntityRenderer::new);

        consumer.register(RuneCraftoryEntities.SANO_AND_UNO.get(), EmptyRender::new);

        NPCFeatureRenderers.init();
    }

    private static <T extends BaseMonster, M extends EntityModel<T> & RideableModel<T>> EntityRendererProvider<? super T> getMonsterRender(Supplier<M> model, ResourceLocation texture, float shadow) {
        return manager -> new RenderMonster<>(manager, model.get(), texture, shadow);
    }

    private static <T extends BaseMonster, M extends EntityModel<T> & RideableModel<T>> void register(EntityRendererRegister consumer, EntityType<T> reg, Supplier<M> model) {
        register(consumer, reg, model, 0.5f);
    }

    private static <T extends BaseMonster, M extends EntityModel<T> & RideableModel<T>> void register(EntityRendererRegister consumer, EntityType<T> reg, Supplier<M> model, float shadow) {
        consumer.register(reg, getMonsterRender(model, mobTexture(reg), shadow));
    }

    public static ResourceLocation mobTexture(EntityType<?> reg) {
        return RuneCraftory.modRes("textures/entity/monsters/" + BuiltInRegistries.ENTITY_TYPE.getKey(reg).getPath() + ".png");
    }

    public static ResourceLocation mobTexture(EntityType<?> reg, String append) {
        return RuneCraftory.modRes("textures/entity/monsters/" + BuiltInRegistries.ENTITY_TYPE.getKey(reg).getPath() + append + ".png");
    }

    public static void layerRegister(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> consumer) {
        for (NPCTextureLayer.LayerType layerType : NPCTextureLayer.LayerType.values()) {
            if (layerType == NPCTextureLayer.LayerType.SKIN_LAYER || layerType.location == null)
                continue;
            consumer.accept(layerType.location, () -> LayerDefinition.create(PlayerModel.createMesh(new CubeDeformation(layerType.expand), false), 64, 64));
            consumer.accept(layerType.slimLocation, () -> LayerDefinition.create(PlayerModel.createMesh(new CubeDeformation(layerType.expand), true), 64, 64));
        }
    }

    public static <T extends ParticleOptions> void registerParticles(PartileRegister consumer) {
        consumer.register(RuneCraftoryParticles.SINKING_DUST.get(), SinkingParticle.Factory::new);
        consumer.register(RuneCraftoryParticles.LIGHT.get(), ColoredParticle.LightParticleFactory::new);
        consumer.register(RuneCraftoryParticles.SHORT_LIGHT.get(), ParticleFactories.ShortLightParticleFactory::new);
        consumer.register(RuneCraftoryParticles.CROSS.get(), ColoredParticle.LightParticleFactory::new);
        consumer.register(RuneCraftoryParticles.BLINK.get(), ColoredParticle.LightParticleFactory::new);
        consumer.register(RuneCraftoryParticles.SMOKE.get(), ColoredParticle.LightParticleFactory::new);
        consumer.register(RuneCraftoryParticles.STATIC_LIGHT.get(), ColoredParticle.NoGravityParticleFactory::new);
        consumer.register(RuneCraftoryParticles.CIRCLING_LIGHT.get(), CirclingParticle.CirclingFactoryBase::new);
        consumer.register(RuneCraftoryParticles.WIND.get(), ColoredParticle.NoGravityParticleFactory::new);
        consumer.register(RuneCraftoryParticles.SLEEP.get(), HeartParticle.Provider::new);
        consumer.register(RuneCraftoryParticles.POISON.get(), HeartParticle.Provider::new);
        consumer.register(RuneCraftoryParticles.PARALYSIS.get(), LightningParticle.Factory::new);
        consumer.register(RuneCraftoryParticles.LIGHTNING.get(), LightningParticle.Factory::new);
        consumer.register(RuneCraftoryParticles.TORNADO.get(), TornadoParticle.TornadoFactoryBase::new);
        consumer.register(RuneCraftoryParticles.BLOCK.get(), BlockParticle.Factory::new);

        consumer.register(RuneCraftoryParticles.RUNEY.get(), RuneyParticle.Provider::new);

        consumer.register(RuneCraftoryParticles.SKELEFANG_BONES.get(), SkelefangParticle.SkelefangParticleFactoryBase::new);
        consumer.register(RuneCraftoryParticles.DURATIONAL_PARTICLE.get(), MoveToGoalParticle.ParticleFactoryBase::new);
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
