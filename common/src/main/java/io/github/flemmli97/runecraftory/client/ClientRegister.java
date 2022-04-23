package io.github.flemmli97.runecraftory.client;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.gui.CraftingGui;
import io.github.flemmli97.runecraftory.client.gui.InfoScreen;
import io.github.flemmli97.runecraftory.client.gui.InfoSubScreen;
import io.github.flemmli97.runecraftory.client.gui.OverlayGui;
import io.github.flemmli97.runecraftory.client.gui.SpellInvOverlayGui;
import io.github.flemmli97.runecraftory.client.gui.UpgradeGui;
import io.github.flemmli97.runecraftory.client.model.ModelButterfly;
import io.github.flemmli97.runecraftory.client.model.ModelGate;
import io.github.flemmli97.runecraftory.client.model.monster.ModelAmbrosia;
import io.github.flemmli97.runecraftory.client.model.monster.ModelAnt;
import io.github.flemmli97.runecraftory.client.model.monster.ModelBeetle;
import io.github.flemmli97.runecraftory.client.model.monster.ModelBigMuck;
import io.github.flemmli97.runecraftory.client.model.monster.ModelBuffamoo;
import io.github.flemmli97.runecraftory.client.model.monster.ModelChipsqueek;
import io.github.flemmli97.runecraftory.client.model.monster.ModelCluckadoodle;
import io.github.flemmli97.runecraftory.client.model.monster.ModelGoblin;
import io.github.flemmli97.runecraftory.client.model.monster.ModelOrc;
import io.github.flemmli97.runecraftory.client.model.monster.ModelPommePomme;
import io.github.flemmli97.runecraftory.client.model.monster.ModelSkyFish;
import io.github.flemmli97.runecraftory.client.model.monster.ModelThunderbolt;
import io.github.flemmli97.runecraftory.client.model.monster.ModelTortas;
import io.github.flemmli97.runecraftory.client.model.monster.ModelWeagle;
import io.github.flemmli97.runecraftory.client.model.monster.ModelWooly;
import io.github.flemmli97.runecraftory.client.model.monster.ModelWoolyWool;
import io.github.flemmli97.runecraftory.client.particles.CirclingParticle;
import io.github.flemmli97.runecraftory.client.particles.SinkingParticle;
import io.github.flemmli97.runecraftory.client.particles.VortexParticle;
import io.github.flemmli97.runecraftory.client.render.RenderGate;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.client.render.monster.RenderAmbrosia;
import io.github.flemmli97.runecraftory.client.render.monster.RenderAnt;
import io.github.flemmli97.runecraftory.client.render.monster.RenderGoblin;
import io.github.flemmli97.runecraftory.client.render.monster.RenderOrcArcher;
import io.github.flemmli97.runecraftory.client.render.monster.RenderThunderbolt;
import io.github.flemmli97.runecraftory.client.render.monster.RenderWooly;
import io.github.flemmli97.runecraftory.client.render.projectiles.EmptyRender;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderButterfly;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderFireball;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderMobArrow;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderStaffBall;
import io.github.flemmli97.runecraftory.client.render.projectiles.RenderWaterLaser;
import io.github.flemmli97.runecraftory.common.blocks.BlockBrokenMineral;
import io.github.flemmli97.runecraftory.common.blocks.BlockCrafting;
import io.github.flemmli97.runecraftory.common.blocks.BlockCrop;
import io.github.flemmli97.runecraftory.common.blocks.BlockHerb;
import io.github.flemmli97.runecraftory.common.blocks.BlockMineral;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityStone;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemDualBladeBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemGloveBase;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModContainer;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.tenshilib.client.particles.ColoredParticle;
import io.github.flemmli97.tenshilib.client.render.RenderProjectileItem;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.HeartParticle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.lwjgl.glfw.GLFW;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ClientRegister {

    public static void init() {
        ClientHandlers.overlay = new OverlayGui(Minecraft.getInstance());
        ClientHandlers.spellDisplay = new SpellInvOverlayGui(Minecraft.getInstance());
    }

    public static void registerKeyBinding(Consumer<KeyMapping> consumer) {
        consumer.accept(ClientHandlers.spell1 = new TriggerKeyBind(RuneCraftory.MODID + ".key.spell_1", GLFW.GLFW_KEY_C, RuneCraftory.MODID + ".keycategory"));
        consumer.accept(ClientHandlers.spell2 = new TriggerKeyBind(RuneCraftory.MODID + ".key.spell_2", GLFW.GLFW_KEY_V, RuneCraftory.MODID + ".keycategory"));
        consumer.accept(ClientHandlers.spell3 = new TriggerKeyBind(RuneCraftory.MODID + ".key.spell_3", GLFW.GLFW_KEY_G, RuneCraftory.MODID + ".keycategory"));
        consumer.accept(ClientHandlers.spell4 = new TriggerKeyBind(RuneCraftory.MODID + ".key.spell_4", GLFW.GLFW_KEY_B, RuneCraftory.MODID + ".keycategory"));
    }

    public static void setupRenderLayers(BiConsumer<Block, RenderType> consumer) {
        ModBlocks.BLOCKS.getEntries().forEach(reg -> {
            if (reg.get() instanceof BlockHerb || reg.get() instanceof BlockCrop || reg.get() instanceof BlockMineral || reg.get() instanceof BlockBrokenMineral)
                consumer.accept(reg.get(), RenderType.cutout());
            if (reg.get() instanceof BlockCrafting)
                consumer.accept(reg.get(), RenderType.cutout());
        });

        consumer.accept(ModBlocks.bossSpawner.get(), RenderType.cutout());
    }

    public static void registerItemProps(ItemModelPropsRegister register) {
        ModItems.ITEMS.getEntries().forEach(reg -> {
            if (reg.get() instanceof ItemDualBladeBase)
                register.register(reg.get(), new ResourceLocation(RuneCraftory.MODID, "held"), ItemModelProps.heldMainProp);
            else if (reg.get() instanceof ItemGloveBase)
                register.register(reg.get(), new ResourceLocation(RuneCraftory.MODID, "glove_held"), ItemModelProps.heldMainGlove);
        });
    }

    public static void registerScreen(MenuScreenRegister factory) {
        factory.register(ModContainer.craftingContainer.get(), (CraftingGui::new));
        factory.register(ModContainer.upgradeContainer.get(), UpgradeGui::new);
        factory.register(ModContainer.infoContainer.get(), InfoScreen::new);
        factory.register(ModContainer.infoSubContainer.get(), InfoSubScreen::new);
    }

    public static <T extends Entity> void registerRenderers(EntityRendererRegister consumer) {
        consumer.register(ModEntities.gate.get(), RenderGate::new);

        consumer.register(ModEntities.wooly.get(), RenderWooly::new);
        register(consumer, ModEntities.orc.get(), ModelOrc::new, ModelOrc.LAYER_LOCATION);
        consumer.register(ModEntities.orcArcher.get(), RenderOrcArcher::new);
        consumer.register(ModEntities.ant.get(), RenderAnt::new);
        register(consumer, ModEntities.beetle.get(), ModelBeetle::new, ModelBeetle.LAYER_LOCATION);
        register(consumer, ModEntities.big_muck.get(), ModelBigMuck::new, ModelBigMuck.LAYER_LOCATION);
        register(consumer, ModEntities.buffamoo.get(), ModelBuffamoo::new, ModelBuffamoo.LAYER_LOCATION);
        register(consumer, ModEntities.chipsqueek.get(), ModelChipsqueek::new, ModelChipsqueek.LAYER_LOCATION);
        register(consumer, ModEntities.cluckadoodle.get(), ModelCluckadoodle::new, ModelCluckadoodle.LAYER_LOCATION);
        register(consumer, ModEntities.pomme_pomme.get(), ModelPommePomme::new, ModelPommePomme.LAYER_LOCATION);
        register(consumer, ModEntities.tortas.get(), ModelTortas::new, ModelTortas.LAYER_LOCATION);
        register(consumer, ModEntities.sky_fish.get(), ModelSkyFish::new, ModelSkyFish.LAYER_LOCATION);
        register(consumer, ModEntities.weagle.get(), ModelWeagle::new, ModelWeagle.LAYER_LOCATION);

        consumer.register(ModEntities.goblin.get(), RenderGoblin::new);
        consumer.register(ModEntities.goblinArcher.get(), RenderGoblin::new);

        consumer.register(ModEntities.ambrosia.get(), RenderAmbrosia::new);
        consumer.register(ModEntities.thunderbolt.get(), RenderThunderbolt::new);

        consumer.register(ModEntities.arrow.get(), RenderMobArrow::new);
        consumer.register(ModEntities.spore.get(), EmptyRender::new);
        consumer.register(ModEntities.gust.get(), EmptyRender::new);
        consumer.register(ModEntities.stone.get(), ctx -> new RenderProjectileItem<>(ctx) {
            private final ItemStack stack = new ItemStack(ModItems.stoneRound.get());

            @Override
            public ItemStack getRenderItemStack(EntityStone entity) {
                return this.stack;
            }

            @Override
            public Type getRenderType(EntityStone entity) {
                return Type.NORMAL;
            }
        });
        consumer.register(ModEntities.sleep_ball.get(), EmptyRender::new);
        consumer.register(ModEntities.pollen.get(), EmptyRender::new);
        consumer.register(ModEntities.ambrosia_wave.get(), EmptyRender::new);
        consumer.register(ModEntities.butterfly.get(), RenderButterfly::new);
        consumer.register(ModEntities.lightningOrbBolt.get(), EmptyRender::new);
        consumer.register(ModEntities.lightningBeam.get(), EmptyRender::new);

        consumer.register(ModEntities.staffThrown.get(), RenderStaffBall::new);
        consumer.register(ModEntities.fireBall.get(), RenderFireball::new);
        consumer.register(ModEntities.windBlade.get(), EmptyRender::new);
        consumer.register(ModEntities.waterLaser.get(), RenderWaterLaser::new);
    }

    private static <T extends BaseMonster, M extends EntityModel<T>> EntityRendererProvider<? super T> getMonsterRender(Function<ModelPart, M> model, ModelLayerLocation layerLocation, ResourceLocation texture) {
        return manager -> new RenderMonster<>(manager, model.apply(manager.bakeLayer(layerLocation)), texture);
    }

    private static <T extends BaseMonster, M extends EntityModel<T>> void register(EntityRendererRegister consumer, EntityType<T> reg, Function<ModelPart, M> model, ModelLayerLocation layerLocation) {
        consumer.register(reg, getMonsterRender(model, layerLocation, mobTexture(reg)));
    }

    public static <T extends BaseMonster> ResourceLocation mobTexture(EntityType<T> reg) {
        return new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/" + PlatformUtils.INSTANCE.entities().getIDFrom(reg).getPath() + ".png");
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

        consumer.accept(ModelAmbrosia.LAYER_LOCATION, ModelAmbrosia::createBodyLayer);
        consumer.accept(ModelThunderbolt.LAYER_LOCATION, ModelThunderbolt::createBodyLayer);

        consumer.accept(ModelButterfly.LAYER_LOCATION, ModelButterfly::createBodyLayer);
        consumer.accept(ModelWoolyWool.LAYER_LOCATION, ModelWoolyWool::createBodyLayer);
    }

    public static <T extends ParticleOptions> void registerParticles(PartileRegister consumer) {
        consumer.register(ModParticles.sinkingDust.get(), SinkingParticle.Factory::new);
        consumer.register(ModParticles.light.get(), ColoredParticle.LightParticleFactory::new);
        consumer.register(ModParticles.cross.get(), ColoredParticle.LightParticleFactory::new);
        consumer.register(ModParticles.blink.get(), ColoredParticle.LightParticleFactory::new);
        consumer.register(ModParticles.smoke.get(), ColoredParticle.LightParticleFactory::new);
        consumer.register(ModParticles.staticLight.get(), ColoredParticle.NoGravityParticleFactory::new);
        consumer.register(ModParticles.circlingLight.get(), CirclingParticle.CirclingFactoryBase::new);
        consumer.register(ModParticles.vortex.get(), VortexParticle.VortexFactoryBase::new);
        consumer.register(ModParticles.wind.get(), ColoredParticle.NoGravityParticleFactory::new);
        consumer.register(ModParticles.sleep.get(), HeartParticle.Provider::new);
        consumer.register(ModParticles.poison.get(), HeartParticle.Provider::new);
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
}
