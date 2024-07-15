package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.tenshilib.TenshiLib;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class EntityTagGen extends TagsProvider<EntityType<?>> {

    public static final TagKey<EntityType<?>> IM_HELD = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("improvedmobs", "default_blacklist_helditems"));
    public static final TagKey<EntityType<?>> IM_USE = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("improvedmobs", "default_blacklist_useitem"));
    public static final TagKey<EntityType<?>> IM_VILLAGERS = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("improvedmobs", "default_blacklist_villager"));
    public static final TagKey<EntityType<?>> IM_ARMOR = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("improvedmobs", "default_blacklist_armor"));

    public static final TagKey<EntityType<?>> MINECOLONIES = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("minecolonies", "mob_attack_blacklist"));

    @SuppressWarnings("deprecation")
    public EntityTagGen(DataGenerator arg, @Nullable ExistingFileHelper existingFileHelper) {
        super(arg, Registry.ENTITY_TYPE, RuneCraftory.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        for (RegistryEntrySupplier<EntityType<?>> type : ModEntities.getMonsters()) {
            this.tag(IM_HELD).add(type.get());
            this.tag(IM_USE).add(type.get());
            this.tag(IM_VILLAGERS).add(type.get());
            this.tag(IM_ARMOR).add(type.get());
            this.tag(MINECOLONIES).add(type.get());
            this.tag(RunecraftoryTags.MONSTERS).add(type.get());
        }
        this.tag(RunecraftoryTags.BOSS_MONSTERS)
                .add(ModEntities.AMBROSIA.get())
                .add(ModEntities.THUNDERBOLT.get())
                .add(ModEntities.MARIONETTA.get())
                .add(ModEntities.DEAD_TREE.get())
                .add(ModEntities.CHIMERA.get())
                .add(ModEntities.RACCOON.get())
                .add(ModEntities.SKELEFANG.get());
        TagKey<EntityType<?>> forgeBosses = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("forge", "bosses"));
        this.tag(forgeBosses)
                .addTag(RunecraftoryTags.BOSS_MONSTERS);
        this.tag(RunecraftoryTags.BOSSES)
                .addTag(forgeBosses);

        this.tag(RunecraftoryTags.RAFFLESIA_SUMMONS)
                .add(ModEntities.HORNET.get())
                .add(ModEntities.ANT.get())
                .add(ModEntities.KILLER_ANT.get());

        this.tag(RunecraftoryTags.FOREST_BOSSES)
                .add(ModEntities.AMBROSIA.get())
                .add(ModEntities.DEAD_TREE.get());
        this.tag(RunecraftoryTags.WATER_RUIN_BOSSES)
                .add(ModEntities.CHIMERA.get())
                .add(ModEntities.THUNDERBOLT.get());
        this.tag(RunecraftoryTags.THEATER_RUIN_BOSSES)
                .add(ModEntities.MARIONETTA.get());
        this.tag(RunecraftoryTags.PLAINS_ARENA_BOSSES)
                .add(ModEntities.RACCOON.get());
        this.tag(RunecraftoryTags.DESERT_ARENA_BOSSES)
                .add(ModEntities.SKELEFANG.get());
        this.tag(RunecraftoryTags.NETHER_ARENA_BOSSES)
                .add(ModEntities.RAFFLESIA.get());
        this.tag(RunecraftoryTags.WIND_SHRINE_BOSSES)
                .add(ModEntities.GRIMOIRE.get());
        this.tag(RunecraftoryTags.LEON_KARNAK_BOSSES)
                .add(ModEntities.SANO_AND_UNO.get());

        this.tag(RunecraftoryTags.TAMED_MONSTER_IGNORE)
                .add(EntityType.SNOW_GOLEM)
                .add(EntityType.IRON_GOLEM)
                .addOptional(new ResourceLocation("advancedgolems", "golem"));

        this.tag(TenshiLib.MULTIPART_ENTITY)
                .add(ModEntities.MULTIPART.get());
    }

    @Override
    public String getName() {
        return "Entity Tags";
    }
}
