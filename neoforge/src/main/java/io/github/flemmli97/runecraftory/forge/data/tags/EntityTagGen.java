package io.github.flemmli97.runecraftory.forge.data.tags;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
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

import java.util.concurrent.CompletableFuture;

public class EntityTagGen extends IntrinsicHolderTagsProvider<EntityType<?>> {

    public static final TagKey<EntityType<?>> IM_HELD = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("improvedmobs", "default_blacklist_helditems"));
    public static final TagKey<EntityType<?>> IM_USE = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("improvedmobs", "default_blacklist_useitem"));
    public static final TagKey<EntityType<?>> IM_VILLAGERS = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("improvedmobs", "default_blacklist_villager"));
    public static final TagKey<EntityType<?>> IM_ARMOR = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("improvedmobs", "default_blacklist_armor"));

    public static final TagKey<EntityType<?>> MINECOLONIES = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("minecolonies", "mob_attack_blacklist"));

    @SuppressWarnings("deprecation")
    public EntityTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.ENTITY_TYPE, lookupProvider, type -> type.builtInRegistryHolder().key(), RuneCraftory.MODID, null);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for (RegistryEntrySupplier<EntityType<?>, ?> type : ModEntities.getMonsters()) {
            this.tag(IM_HELD).add(type.get());
            this.tag(IM_USE).add(type.get());
            this.tag(IM_VILLAGERS).add(type.get());
            this.tag(IM_ARMOR).add(type.get());
            this.tag(MINECOLONIES).add(type.get());
            this.tag(RunecraftoryTags.EntityTypes.MONSTERS).add(type.get());
        }
        for (RegistryEntrySupplier<EntityType<?>, ?> sup : ModEntities.getBosses()) {
            this.tag(RunecraftoryTags.EntityTypes.BOSS_MONSTERS)
                    .add(sup.get());
        }
        this.tag(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
                .add(ModEntities.GATE.get())
                .add(ModEntities.SKY_FISH.get());
        this.tag(EntityTypeTags.AQUATIC)
                .add(ModEntities.SKY_FISH.get());
        this.tag(EntityTypeTags.ARTHROPOD)
                .add(ModEntities.ANT.get());

        this.tag(RunecraftoryTags.EntityTypes.RAFFLESIA_SUMMONS)
                .add(ModEntities.HORNET.get())
                .add(ModEntities.ANT.get())
                .add(ModEntities.KILLER_ANT.get());

        this.tag(RunecraftoryTags.EntityTypes.TAMED_MONSTER_IGNORE)
                .add(EntityType.SNOW_GOLEM)
                .add(EntityType.IRON_GOLEM)
                .addOptional(ResourceLocation.fromNamespaceAndPath("advancedgolems", "golem"));

        this.tag(TenshiLib.MULTIPART_ENTITY)
                .add(ModEntities.MULTIPART.get());
    }

    @Override
    public String getName() {
        return "Entity Tags";
    }
}
