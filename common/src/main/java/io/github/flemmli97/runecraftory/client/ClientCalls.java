package io.github.flemmli97.runecraftory.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.api.enums.EnumWeather;
import io.github.flemmli97.runecraftory.client.gui.widgets.InfoButton;
import io.github.flemmli97.runecraftory.client.tooltips.UpgradeTooltipComponent;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.items.MultiBlockItem;
import io.github.flemmli97.runecraftory.common.items.tools.ItemFertilizer;
import io.github.flemmli97.runecraftory.common.network.C2SOpenInfo;
import io.github.flemmli97.runecraftory.common.network.C2SRideJump;
import io.github.flemmli97.runecraftory.common.network.C2SSpellKey;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.mixin.ContainerScreenAccessor;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.Input;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ClientCalls {

    public static void clientTick() {
        BossBarTracker.tickSounds();
        Player player = Minecraft.getInstance().player;
        if (player != null && Platform.INSTANCE.getPlayerData(player).getWeaponHandler().isItemSwapBlocked()) {
            Options options = Minecraft.getInstance().options;
            //Disable changing held item
            for (int i = 0; i < 9; ++i) {
                while (options.keyHotbarSlots[i].consumeClick()) ;
            }
            while (options.keySwapOffhand.consumeClick()) ;
        }
        if (Minecraft.getInstance().screen != null)
            return;
        switch (ClientHandlers.SPELL_1.onPress()) {
            case PRESSING -> LoaderNetwork.INSTANCE.sendToServer(new C2SSpellKey(0, false));
            case RELEASE -> LoaderNetwork.INSTANCE.sendToServer(new C2SSpellKey(0, true));
        }
        switch (ClientHandlers.SPELL_2.onPress()) {
            case PRESSING -> LoaderNetwork.INSTANCE.sendToServer(new C2SSpellKey(1, false));
            case RELEASE -> LoaderNetwork.INSTANCE.sendToServer(new C2SSpellKey(1, true));
        }
        switch (ClientHandlers.SPELL_3.onPress()) {
            case PRESSING -> LoaderNetwork.INSTANCE.sendToServer(new C2SSpellKey(2, false));
            case RELEASE -> LoaderNetwork.INSTANCE.sendToServer(new C2SSpellKey(2, true));
        }
        switch (ClientHandlers.SPELL_4.onPress()) {
            case PRESSING -> LoaderNetwork.INSTANCE.sendToServer(new C2SSpellKey(3, false));
            case RELEASE -> LoaderNetwork.INSTANCE.sendToServer(new C2SSpellKey(3, true));
        }
    }

    public static void initSkillTab(Screen screen, Consumer<AbstractWidget> cons) {
        if (ClientConfig.inventoryButton) {
            if (screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen) {
                int x = ((ContainerScreenAccessor) screen).getLeft();
                int y = ((ContainerScreenAccessor) screen).getTop();
                if (screen instanceof InventoryScreen) {
                    x += ClientConfig.inventoryOffsetX;
                    y += ClientConfig.inventoryOffsetY;
                } else {
                    x += ClientConfig.creativeInventoryOffsetX;
                    y += ClientConfig.creativeInventoryOffsetY;
                }
                cons.accept(new InfoButton(x, y, screen, b -> {
                    LoaderNetwork.INSTANCE.sendToServer(new C2SOpenInfo(C2SOpenInfo.Action.MAIN));
                }));
            }
        }
    }

    public static void handleInputUpdate(Player player, Input input) {
        if (EntityUtils.isDisabled(player)) {
            input.leftImpulse = 0;
            input.forwardImpulse = 0;
            input.up = false;
            input.down = false;
            input.left = false;
            input.right = false;
            input.jumping = false;
            input.shiftKeyDown = false;
        }
        float reduction = Platform.INSTANCE.getPlayerData(player).getWeaponHandler().movementReduction();
        if (reduction != 1) {
            input.leftImpulse *= reduction;
            input.forwardImpulse *= reduction;
            input.jumping = false;
            if (reduction == 0) {
                input.up = false;
                input.down = false;
                input.left = false;
                input.right = false;
                input.shiftKeyDown = false;
            }
        }
    }

    public static void renderScreenOverlays(GuiGraphics graphics, DeltaTracker tracker) {
        if (ClientHandlers.OVERLAY != null)
            ClientHandlers.OVERLAY.renderBar(graphics);
        if (ClientHandlers.SPELL_DISPLAY != null && ClientConfig.inventoryButton)
            ClientHandlers.SPELL_DISPLAY.render(graphics, tracker);
        if (ClientHandlers.FARM_DISPLAY != null)
            ClientHandlers.FARM_DISPLAY.render(graphics);
    }

    public static void tooltipEvent(ItemStack stack, List<Component> tooltip, TooltipFlag flag) {
        if (!stack.isEmpty()) {
            boolean showTooltip = true;
//            if (stack.hasTag()) {
//                CompoundTag tag = stack.getTag();
//                if (tag.contains("HideFlags", 99))
//                    showTooltip = (stack.getTag().getInt("HideFlags") & 0x20) == 0x0;
//                if (tag.getCompound(RuneCraftory.MODID).contains(LibNBT.CRAFTING_BONUS) && tooltip.get(0) instanceof MutableComponent mut)
//                    mut.withStyle(ChatFormatting.AQUA);
//            }
            if (showTooltip) {
                Pair<List<Component>, List<Component>> p = injectAdditionalTooltip(stack, flag);
                tooltip.addAll(1, p.getFirst());
                tooltip.addAll(p.getSecond());
            }
        }
    }

    private static Pair<List<Component>, List<Component>> injectAdditionalTooltip(ItemStack stack, TooltipFlag flag) {
        List<Component> tooltip = new ArrayList<>();
        boolean shift = Screen.hasShiftDown();
        List<Component> debug = new ArrayList<>();
        DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem()).ifPresent(stat -> {
            tooltip.addAll(stat.texts(stack, shift));
            if (flag.isAdvanced())
                debug.add(Component.translatable("runecraftory.tooltip.debug.stat", stat.getId().toString()).withStyle(ChatFormatting.GRAY));
        });
        CropProperties props = DataPackHandler.INSTANCE.cropManager().get(stack.getItem());
        if (props != null) {
            tooltip.addAll(props.texts());
            if (flag.isAdvanced())
                debug.add(Component.translatable("runecraftory.tooltip.debug.crop", props.getId().toString()).withStyle(ChatFormatting.GRAY));
        }
        if (shift) {
            FoodProperties food = DataPackHandler.INSTANCE.foodManager().get(stack.getItem());
            if (food != null) {
                tooltip.addAll(food.texts(stack));
                if (flag.isAdvanced())
                    debug.add(Component.translatable("runecraftory.tooltip.debug.food", food.getId().toString()).withStyle(ChatFormatting.GRAY));
            } else if (stack.has(DataComponents.FOOD)) {
                tooltip.add(Component.translatable("runecraftory.tooltip.item.eaten").withStyle(ChatFormatting.GRAY));
                MutableComponent comp = Component.literal(" ").append(Component.translatable(ModAttributes.RUNE_POINTS_GAIN.get().getDescriptionId())).append(Component.literal(": " + EntityUtils.getRPFromVanillaFood(stack)));
                tooltip.add(comp.withStyle(ChatFormatting.AQUA));
            }
        }
        return Pair.of(tooltip, debug);
    }

    public static void tooltipComponentEvent(ItemStack stack, Consumer<TooltipComponent> elements) {
        if (UpgradeTooltipComponent.shouldAdd(stack))
            elements.accept(new UpgradeTooltipComponent.UpgradeComponent(stack));
    }

    public static void worldRender(PoseStack stack) {
        if (GeneralConfig.debugAttack) {
            AttackAABBRender.INST.render(stack, Minecraft.getInstance().renderBuffers().crumblingBufferSource());
        }
        Minecraft minecraft = Minecraft.getInstance();
        MultiBlockItem item = null;
        ItemStack main = minecraft.player.getMainHandItem();
        ItemStack off = minecraft.player.getMainHandItem();
        ItemStack toUse = main;
        InteractionHand hand = InteractionHand.MAIN_HAND;
        if (off.getItem() instanceof MultiBlockItem multiBlockItem && !(main.getItem() instanceof BlockItem)) {
            item = multiBlockItem;
            toUse = off;
            hand = InteractionHand.OFF_HAND;
        }
        if (main.getItem() instanceof MultiBlockItem multiBlockItem) {
            item = multiBlockItem;
            toUse = main;
            hand = InteractionHand.OFF_HAND;
        }
        if (item == null)
            return;
        BlockHitResult hitResult = minecraft.hitResult instanceof BlockHitResult result && result.getType() != HitResult.Type.MISS ? result : null;
        if (hitResult != null) {
            BlockPlaceContext ctx = new BlockPlaceContext(minecraft.player, hand, toUse, hitResult);
            BlockPos pos = ctx.getClickedPos();
            Vec3 camPos = minecraft.gameRenderer.getMainCamera().getPosition();
            double x = camPos.x();
            double y = camPos.y();
            double z = camPos.z();
            MultiBufferSource.BufferSource buffer = minecraft.renderBuffers().bufferSource();
            VertexConsumer consumer = buffer.getBuffer(RenderType.lines());
            boolean invalid = false;
            List<Pair<BlockPos, VoxelShape>> list = new ArrayList<>();
            Rotation rot = EntityUtils.fromDirection(minecraft.player.getDirection());
            for (Pair<BlockPos, BlockState> p : item.getBlocks()) {
                BlockPos offset = p.getFirst().rotate(rot)
                        .offset(pos);
                BlockState current = minecraft.level.getBlockState(offset);
                if (!current.canBeReplaced(ctx)) {
                    invalid = true;
                    list.add(Pair.of(offset, Shapes.empty()));
                } else
                    list.add(Pair.of(offset, p.getSecond().rotate(rot).getShape(minecraft.level, offset).move(offset.getX(), offset.getY(), offset.getZ())));
            }
            float gb = invalid ? 0.3f : 1;
            for (Pair<BlockPos, VoxelShape> p : list) {
                if (p.getSecond() == Shapes.empty())
                    continue;
                renderBlockAt(stack, consumer, p.getSecond(), x, y, z, 1, gb, gb, 1, invalid);
            }
            buffer.endBatch(RenderType.lines());
        }
    }

    private static void renderBlockAt(PoseStack stack, VertexConsumer consumer, VoxelShape shape, double camX, double camY, double camZ, float r, float g, float b, float alpha, boolean invalid) {
        PoseStack.Pose pose = stack.last();
        shape.forAllEdges((k, l, m, n, o, p) -> {
            float dX = (float) (n - k);
            float dY = (float) (o - l);
            float dZ = (float) (p - m);
            float len = Mth.sqrt(dX * dX + dY * dY + dZ * dZ);
            consumer.addVertex(pose, (float) (k - camX), (float) (l - camY), (float) (m - camZ)).setColor(r, g, b, alpha).setNormal(pose, dX /= len, dY /= len, dZ /= len);
            consumer.addVertex(pose, (float) (n - camX), (float) (o - camY), (float) (p - camZ)).setColor(r, g, b, alpha).setNormal(pose, dX, dY, dZ);
        });
    }

    public static void tick(LivingEntity entity) {
        EntityData data = Platform.INSTANCE.getEntityData(entity);
        int mod = entity.tickCount % 20;
        if (mod == 0 && data.isSleeping()) {
            entity.level().addParticle(ModParticles.SLEEP.get(), entity.getX(), entity.getY() + entity.getBbHeight() + 0.5, entity.getZ(), 0, 0, 0);
        }
        if (mod == 5 && data.isPoisoned()) {
            entity.level().addParticle(ModParticles.POISON.get(), entity.getX(), entity.getY() + entity.getBbHeight() + 0.1, entity.getZ(), 0, 0, 0);
        }
        if (data.isParalysed()) {
            boolean bl2 = entity.isInvisible() ? entity.getRandom().nextInt(25) == 0 : entity.getRandom().nextInt(5) == 0;
            if (bl2) {
                entity.level().addParticle(ModParticles.PARALYSIS.get(), entity.getRandomX(0.5), entity.getRandomY(), entity.getRandomZ(0.5), 0.05, 0.05, 0.05);
            }
        }
        if (entity == Minecraft.getInstance().player) {
            ShakeHandler.shakeTick--;
            if (entity.getVehicle() instanceof BaseMonster && Minecraft.getInstance().player.input.jumping)
                LoaderNetwork.INSTANCE.sendToServer(C2SRideJump.INSTANCE);
        }
        if (entity == Minecraft.getInstance().cameraEntity) {
            if (ClientHandlers.CLIENT_CALENDAR.currentWeather() == EnumWeather.RUNEY) {
                int tries = Minecraft.getInstance().options.particles().get() != ParticleStatus.ALL ? 1 : 2;
                for (int i = 0; i < tries; i++)
                    entity.level().addParticle(ModParticles.RUNEY.get(),
                            entity.getX() + (entity.getRandom().nextDouble() - 0.5) * 24,
                            entity.getY() + (entity.getRandom().nextDouble() - 0.5) * 12,
                            entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * 24, 0, 0, 0);
            }
        }
    }

    public static boolean invis(LivingEntity entity) {
        return Platform.INSTANCE.getEntityData(entity).isInvis();
    }

    public static void renderShaking(Camera camera, float yaw, float pitch, float roll, float partialTicks,
                                     Consumer<Float> setYaw, Consumer<Float> setPitch, Consumer<Float> setRoll) {
        boolean stunned = Minecraft.getInstance().player.hasEffect(ModEffects.STUNNED.asHolder());
        if (stunned) {
            float pT = Minecraft.getInstance().player.tickCount * 10 - partialTicks;
            setYaw.accept(yaw + Mth.sin(pT) * 0.5f);
            setPitch.accept(pitch + Mth.sin(pT * 2) * 1);
        }
        int t = ShakeHandler.shakeTick;
        if (t <= 0)
            return;
        float strengthPitch = ShakeHandler.shakeStrength;
        float strengthRoll = ShakeHandler.shakeStrength;
        float pT = t * 24 - partialTicks;
        setPitch.accept(pitch + Mth.sin(pT * 2) * strengthPitch);
        setRoll.accept(roll + Mth.sin(pT) * strengthRoll);
    }

    public static void renderEntityShake(LivingEntity entity, PoseStack stack, float partialTicks) {
        boolean stunned = Platform.INSTANCE.getEntityData(entity).isStunned();
        if (!stunned)
            return;
        Vec3 dir = Vec3.directionFromRotation(0, entity.getViewYRot(partialTicks) + 90).scale(0.1);
        float pT = Minecraft.getInstance().player.tickCount * 10 - partialTicks;
        stack.translate(Mth.sin(pT) * dir.x(), 0, Mth.sin(pT) * dir.z());
    }

    public static boolean onBlockHighlightRender(Level level, PoseStack poseStack, VertexConsumer consumer, Entity entity, double camX, double camY, double camZ, BlockPos pos, BlockState state) {
        if (entity instanceof LivingEntity living && living.getMainHandItem().getItem() instanceof ItemFertilizer) {
            boolean targetingCrop = level.getBlockState(pos).getBlock() instanceof BushBlock;
            ItemFertilizer.getOtherForTargeted(entity.getDirection(), pos)
                    .forEach(p -> {
                        BlockState state1 = level.getBlockState(p);
                        if (targetingCrop && state1.isAir()) {
                            p = p.below();
                            state1 = level.getBlockState(p);
                        }
                        if (!state1.isAir() && level.getWorldBorder().isWithinBounds(p))
                            renderShape(poseStack, consumer, state1.getShape(level, p, CollisionContext.of(entity)), p.getX() - camX, p.getY() - camY, p.getZ() - camZ, 0.0F, 0.0F, 0.0F, 0.4F);
                    });
        }
        return false;
    }

    /**
     * From {@link LevelRenderer#renderShape}
     */
    private static void renderShape(PoseStack poseStack, VertexConsumer consumer, VoxelShape shape, double x, double y, double z, float red, float green, float blue, float alpha) {
        PoseStack.Pose pose = poseStack.last();
        shape.forAllEdges((k, l, m, n, o, p) -> {
            float q = (float) (n - k);
            float r = (float) (o - l);
            float s = (float) (p - m);
            float t = Mth.sqrt(q * q + r * r + s * s);
            q /= t;
            r /= t;
            s /= t;
            consumer.addVertex(pose, (float) (k + x), (float) (l + y), (float) (m + z)).setColor(red, green, blue, alpha).setNormal(pose, q, r, s);
            consumer.addVertex(pose, (float) (n + x), (float) (o + y), (float) (p + z)).setColor(red, green, blue, alpha).setNormal(pose, q, r, s);
        });
    }
}
