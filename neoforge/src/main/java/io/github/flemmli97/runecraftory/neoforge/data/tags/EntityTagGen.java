package io.github.flemmli97.runecraftory.neoforge.data.tags;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.tenshilib.TenshiLib;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class EntityTagGen extends IntrinsicHolderTagsProvider<EntityType<?>> {

    public static final TagKey<EntityType<?>> IM_HELD = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("improvedmobs", "default_blacklist_helditems"));
    public static final TagKey<EntityType<?>> IM_USE = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("improvedmobs", "default_blacklist_useitem"));
    public static final TagKey<EntityType<?>> IM_VILLAGERS = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("improvedmobs", "default_blacklist_villager"));
    public static final TagKey<EntityType<?>> IM_ARMOR = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("improvedmobs", "default_blacklist_armor"));

    public static final TagKey<EntityType<?>> MINECOLONIES = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("minecolonies", "mob_attack_blacklist"));

    @SuppressWarnings("deprecation")
    public EntityTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super(output, Registries.ENTITY_TYPE, lookupProvider, type -> type.builtInRegistryHolder().key(), RuneCraftory.MODID, fileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for (RegistryEntrySupplier<EntityType<?>, ?> type : RuneCraftoryEntities.getMonsters()) {
            this.tag(IM_HELD).add(type.get());
            this.tag(IM_USE).add(type.get());
            this.tag(IM_VILLAGERS).add(type.get());
            this.tag(IM_ARMOR).add(type.get());
            this.tag(MINECOLONIES).add(type.get());
            this.tag(RunecraftoryTags.EntityTypes.MONSTERS).add(type.get());
        }
        for (RegistryEntrySupplier<EntityType<?>, ?> sup : RuneCraftoryEntities.getBosses()) {
            this.tag(RunecraftoryTags.EntityTypes.BOSS_MONSTERS)
                    .add(sup.get());
        }
        this.tag(RunecraftoryTags.EntityTypes.BOSSES)
                .addTag(RunecraftoryTags.EntityTypes.BOSS_MONSTERS);

        this.tag(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
                .add(RuneCraftoryEntities.GATE.get())
                .add(RuneCraftoryEntities.SKY_FISH.get())
                .add(RuneCraftoryEntities.TORTAS.get());
        this.tag(EntityTypeTags.DISMOUNTS_UNDERWATER)
                .add(RuneCraftoryEntities.SKY_FISH.get())
                .add(RuneCraftoryEntities.TORTAS.get());
        this.tag(EntityTypeTags.AQUATIC)
                .add(RuneCraftoryEntities.SKY_FISH.get())
                .add(RuneCraftoryEntities.TORTAS.get());
        this.tag(EntityTypeTags.ARTHROPOD)
                .add(RuneCraftoryEntities.ANT.get())
                .add(RuneCraftoryEntities.KILLER_ANT.get())
                .add(RuneCraftoryEntities.BEETLE.get())
                .add(RuneCraftoryEntities.SPIDER.get())
                .add(RuneCraftoryEntities.HORNET.get())
                .add(RuneCraftoryEntities.SCORPION.get());
        this.tag(EntityTypeTags.UNDEAD)
                .add(RuneCraftoryEntities.GHOST.get())
                .add(RuneCraftoryEntities.GHOST_RAY.get())
                .add(RuneCraftoryEntities.SPIRIT.get())
                .add(RuneCraftoryEntities.IGNIS.get())
                .add(RuneCraftoryEntities.TOMATO_GHOST.get())
                .add(RuneCraftoryEntities.SKELEFANG.get());

        this.tag(RunecraftoryTags.EntityTypes.RAFFLESIA_SUMMONS)
                .add(RuneCraftoryEntities.HORNET.get())
                .add(RuneCraftoryEntities.ANT.get())
                .add(RuneCraftoryEntities.KILLER_ANT.get());

        this.tag(RunecraftoryTags.EntityTypes.TAMED_MONSTER_IGNORE)
                .add(EntityType.SNOW_GOLEM)
                .add(EntityType.IRON_GOLEM)
                .addOptional(ResourceLocation.fromNamespaceAndPath("advancedgolems", "golem"));

        this.tag(TenshiLib.MULTIPART_ENTITY)
                .add(RuneCraftoryEntities.MULTIPART.get());
    }

    @Override
    public String getName() {
        return "Entity Tags";
    }
}
