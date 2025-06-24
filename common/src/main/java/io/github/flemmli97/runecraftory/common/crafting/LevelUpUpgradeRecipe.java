package io.github.flemmli97.runecraftory.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public class LevelUpUpgradeRecipe implements Recipe<SingleRecipeInput> {

    protected final int level;
    protected final Ingredient base;
    protected final Ingredient upgradeMaterial;

    public LevelUpUpgradeRecipe(int level, Ingredient base, Ingredient upgradeMaterial) {
        this.level = level;
        this.base = base;
        this.upgradeMaterial = upgradeMaterial;
    }

    public static void build(RecipeOutput cons, int level, Ingredient base, Ingredient material, String string) {
        build(cons, level, base, material, ResourceLocation.parse(string));
    }

    public static void build(RecipeOutput cons, int level, Ingredient base, Ingredient material, ResourceLocation res) {
        cons.accept(ResourceLocation.fromNamespaceAndPath(res.getNamespace(), "level_upgrade/" + res.getPath()),
                new LevelUpUpgradeRecipe(level, base, material), null);
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        ItemStack stack = input.getItem(0);
        if (ItemNBT.itemLevel(stack) != this.level)
            return false;
        return true;
    }

    @Override
    public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registries) {
        ItemStack stack = input.getItem(0).copy();
        ItemNBT.getLeveledItem(stack, ItemNBT.itemLevel(stack) + 1);
        return stack;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModCrafting.LEVEL_UPGRADE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModCrafting.LEVEL_UP.get();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer implements RecipeSerializer<LevelUpUpgradeRecipe> {

        public static final MapCodec<LevelUpUpgradeRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(Codec.INT.fieldOf("level").forGetter(d -> d.level),
                        Ingredient.CODEC_NONEMPTY.fieldOf("base").forGetter(d -> d.base),
                        Ingredient.CODEC_NONEMPTY.fieldOf("base").forGetter(d -> d.base)
                ).apply(instance, LevelUpUpgradeRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, LevelUpUpgradeRecipe> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public LevelUpUpgradeRecipe decode(RegistryFriendlyByteBuf buf) {
                return new LevelUpUpgradeRecipe(buf.readInt(), Ingredient.CONTENTS_STREAM_CODEC.decode(buf), Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, LevelUpUpgradeRecipe recipe) {
                buf.writeInt(recipe.level);
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.base);
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.upgradeMaterial);
            }
        };

        @Override
        public MapCodec<LevelUpUpgradeRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, LevelUpUpgradeRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
