package io.github.flemmli97.runecraftory.common.advancements;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CropHarvestTrigger extends SimpleCriterionTrigger<CropHarvestTrigger.TriggerInstance> {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "crop_harvest");

    @Override
    protected CropHarvestTrigger.TriggerInstance createInstance(JsonObject json, EntityPredicate.Composite player, DeserializationContext context) {
        TagKey<Block> tag = null;
        if (json.has("tag")) {
            tag = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(GsonHelper.getAsString(json, "tag")));
        }
        StatePropertiesPredicate state = StatePropertiesPredicate.fromJson(json.get("state"));
        Block block;
        if (json.has("block")) {
            ResourceLocation resourceLocation = new ResourceLocation(GsonHelper.getAsString(json, "block"));
            block = Registry.BLOCK.getOptional(resourceLocation).orElseThrow(() -> new JsonSyntaxException("Unknown block type '" + resourceLocation + "'"));
            state.checkState(block.getStateDefinition(), string -> {
                throw new JsonSyntaxException("Block " + block + " has no property " + string);
            });
        } else {
            block = null;
        }
        return new CropHarvestTrigger.TriggerInstance(player, block, tag, state);
    }

    public void trigger(ServerPlayer player, BlockState state) {
        this.trigger(player, inst -> inst.matches(state));
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        @Nullable
        private final Block block;
        @Nullable
        private final TagKey<Block> tag;
        private final StatePropertiesPredicate state;

        public TriggerInstance(EntityPredicate.Composite composite, @Nullable Block block, @Nullable TagKey<Block> tag, StatePropertiesPredicate state) {
            super(ID, composite);
            this.block = block;
            this.tag = tag;
            this.state = state;
        }

        public static CropHarvestTrigger.TriggerInstance harvest(TagKey<Block> tag) {
            return new CropHarvestTrigger.TriggerInstance(EntityPredicate.Composite.ANY, null, tag, StatePropertiesPredicate.ANY);
        }

        public static CropHarvestTrigger.TriggerInstance harvest(TagKey<Block> tag, StatePropertiesPredicate.Builder builder) {
            return new CropHarvestTrigger.TriggerInstance(EntityPredicate.Composite.ANY, null, tag, builder.build());
        }

        public static CropHarvestTrigger.TriggerInstance harvest(Block block, StatePropertiesPredicate.Builder builder) {
            return new CropHarvestTrigger.TriggerInstance(EntityPredicate.Composite.ANY, block, null, builder.build());
        }

        /**
         * If the itemPredicate is defined get shipping amount for that item.
         * Else it will use the amount of shipped item types.
         */
        public boolean matches(BlockState state) {
            if (!(state.getBlock() instanceof CropBlock crop) || !crop.isMaxAge(state))
                return false;
            if (this.tag != null && !state.is(this.tag)) {
                return false;
            }
            if (this.block != null && !state.is(this.block)) {
                return false;
            }
            return this.state.matches(state);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject obj = super.serializeToJson(context);
            if (this.tag != null) {
                obj.addProperty("tag", this.tag.location().toString());
            }
            if (this.block != null) {
                obj.addProperty("block", Registry.BLOCK.getKey(this.block).toString());
            }
            obj.add("state", this.state.serializeToJson());
            return obj;
        }
    }
}
