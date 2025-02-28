package io.github.flemmli97.runecraftory.common.blocks.tile;

import io.github.flemmli97.runecraftory.api.datapack.EntityProperties;
import io.github.flemmli97.runecraftory.common.blocks.BlockBossSpawner;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.datapack.manager.StructureBossManager;
import io.github.flemmli97.runecraftory.common.entities.EnsembleMonsters;
import io.github.flemmli97.runecraftory.common.entities.utils.IBaseMob;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BossSpawnerBlockEntity extends BlockEntity {

    private int lastUpdateDay = -1;
    private int ticker;

    private ResourceLocation spawnListId;
    private StructureBossManager.BossSpawnList spawnList;
    private EntityType<?> nextSpawn;

    private BlockPos structurePos;
    private ResourceLocation structureID;
    private StructureStart structure;

    private final Random random = new Random();

    public BossSpawnerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.BOSS_SPAWNER_TILE.get(), blockPos, blockState);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, BossSpawnerBlockEntity blockEntity) {
        blockEntity.ticker++;
        if (blockEntity.ticker % 5 != 0)
            return;
        Vec3 pos = Vec3.atCenterOf(blockPos.above(2));
        List<ServerPlayer> nearby = LevelCalc.playersAround(level, pos, 20);
        if (!nearby.isEmpty() && blockEntity.nextSpawn != null) {
            EntityProperties prop = DataPackHandler.INSTANCE.monsterPropertiesManager().getPropertiesFor(blockEntity.nextSpawn);
            boolean canSpawn = false;
            if (prop.spawnerPredicate != EntityPredicate.ANY) {
                // Throw out all non matching players
                List<ServerPlayer> removed = new ArrayList<>();
                for (ServerPlayer player : nearby) {
                    if (!prop.spawnerPredicate.matches(player, player)) {
                        removed.add(player);
                        if (player.position().closerThan(pos, 16)) {
                            Vec3 opposite = player.position().subtract(pos).normalize();
                            player.fallDistance = 0;
                            player.setDeltaMovement(opposite);
                            player.sendMessage(new TranslatableComponent("runecraftory.misc.spawner.entry.deny").withStyle(ChatFormatting.DARK_PURPLE),
                                    ChatType.GAME_INFO, Util.NIL_UUID);
                            player.connection.send(new ClientboundSetEntityMotionPacket(player));
                        }
                    } else if (player.position().closerThan(pos, 10)) {
                        canSpawn = true;
                    }
                }
                nearby.removeAll(removed);
            } else {
                canSpawn = true;
            }
            boolean flag = blockEntity.lastUpdateDay != WorldUtils.day(level);
            if (canSpawn && flag) {
                blockEntity.spawnEntity(nearby, pos);
            }
        }
    }

    public StructureStart getStructure() {
        if (this.structureID != null && this.level instanceof ServerLevel serverLevel)
            this.structure = serverLevel.structureFeatureManager().getStructureAt(this.getBlockPos(), PlatformUtils.INSTANCE.registry(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY).getFromId(this.structureID));
        return this.structure;
    }

    public void spawnEntity(List<ServerPlayer> nearby, Vec3 pos) {
        if (!this.level.isClientSide && this.nextSpawn != null) {
            Entity e = this.nextSpawn.create(this.level);
            if (e != null) {
                this.lastUpdateDay = WorldUtils.day(this.level);
                if (e instanceof EnsembleMonsters ensemble) {
                    if (!ensemble.canSpawnerSpawn((ServerLevel) this.level, this.worldPosition, 32))
                        return;
                    ensemble.setLevel(LevelCalc.levelFromPos((ServerLevel) this.level, Vec3.atCenterOf(this.worldPosition), nearby));
                    ensemble.setRestrictRadius(13);
                    switch (this.getBlockState().getValue(BlockBossSpawner.FACING)) {
                        case SOUTH -> ensemble.withDirection(Rotation.CLOCKWISE_180);
                        case WEST -> ensemble.withDirection(Rotation.COUNTERCLOCKWISE_90);
                        case EAST -> ensemble.withDirection(Rotation.CLOCKWISE_90);
                        default -> ensemble.withDirection(Rotation.NONE);
                    }
                } else if (!this.noNearby())
                    return;
                if (e instanceof IBaseMob mob)
                    mob.setLevel(LevelCalc.levelFromPos((ServerLevel) this.level, Vec3.atCenterOf(this.worldPosition), nearby));
                e.moveTo(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 5, this.worldPosition.getZ() + 0.5, this.level.random.nextFloat() * 360.0F, 0.0F);
                if (e instanceof Mob mob) {
                    mob.restrictTo(this.worldPosition, 13);
                    mob.finalizeSpawn((ServerLevelAccessor) this.level, this.level.getCurrentDifficultyAt(e.blockPosition()), MobSpawnType.SPAWNER, null, null);
                }
                this.level.addFreshEntity(e);
                this.updateEntity();
            }
        }
    }

    public void setEntity(EntityType<?> entity) {
        this.nextSpawn = entity;
    }

    private void updateEntity() {
        if (this.spawnList != null)
            this.spawnList.getRandom(this.random).ifPresent(this::setEntity);
    }

    private boolean noNearby() {
        return this.level.getEntitiesOfClass(Mob.class, new AABB(this.worldPosition).inflate(32),
                e -> e.getType() == this.nextSpawn || (this.spawnList != null && this.spawnList.has(e.getType()))).isEmpty();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.lastUpdateDay = tag.getInt("LastUpdate");
        if (tag.contains("SpawnListId")) {
            this.spawnListId = new ResourceLocation(tag.getString("SpawnListId"));
            this.spawnList = DataPackHandler.INSTANCE.structureBossManager().getBoss(new ResourceLocation(tag.getString("SpawnListId")));
            this.updateEntity();
        }
        if (tag.contains("Entity")) {
            this.nextSpawn = Registry.ENTITY_TYPE.get(new ResourceLocation(tag.getString("Entity")));
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("LastUpdate", this.lastUpdateDay);
        if (this.spawnListId != null)
            tag.putString("SpawnListId", this.spawnListId.toString());
        if (this.nextSpawn != null)
            tag.putString("Entity", Registry.ENTITY_TYPE.getKey(this.nextSpawn).toString());
    }

    public static CompoundTag creatTagFor(ResourceLocation spawnListId) {
        CompoundTag tag = new CompoundTag();
        tag.putString("SpawnListId", spawnListId.toString());
        tag.putInt("LastUpdate", -1);
        return tag;
    }
}
