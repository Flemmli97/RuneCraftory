package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
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

    public static final TagKey<EntityType<?>> IMHeld = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("improvedmobs", "default_blacklist_helditems"));
    public static final TagKey<EntityType<?>> IMUse = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("improvedmobs", "default_blacklist_useitem"));
    public static final TagKey<EntityType<?>> IMVillagers = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("improvedmobs", "default_blacklist_villager"));

    public static final TagKey<EntityType<?>> minecolonies = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("minecolonies", "mob_attack_blacklist"));

    @SuppressWarnings("deprecation")
    public EntityTagGen(DataGenerator arg, @Nullable ExistingFileHelper existingFileHelper) {
        super(arg, Registry.ENTITY_TYPE, RuneCraftory.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        for (RegistryEntrySupplier<EntityType<?>> type : ModEntities.getMonsters()) {
            this.tag(IMHeld).add(type.get());
            this.tag(IMUse).add(type.get());
            this.tag(IMVillagers).add(type.get());
            this.tag(minecolonies).add(type.get());
            this.tag(ModTags.monsters).add(type.get());
        }
        this.tag(ModTags.bossMonsters)
                .add(ModEntities.ambrosia.get())
                .add(ModEntities.thunderbolt.get())
                .add(ModEntities.marionetta.get());
    }

    @Override
    public String getName() {
        return "Entity Tags";
    }
}
