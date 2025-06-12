package io.github.flemmli97.runecraftory.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.inventory.PlayerBoundCraftingContainer;
import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.utils.CraftingUtils;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SextupleRecipe implements Recipe<PlayerBoundCraftingContainer> {

    private final String group;
    private final ItemStack recipeOutput;
    private final NonNullList<Ingredient> recipeItems;
    private final int craftingLevel;
    private final int baseCost;

    public SextupleRecipe(String group, int level, int baseCost, ItemStack result, NonNullList<Ingredient> ingredients) {
        this.group = group;
        this.recipeOutput = result;
        this.recipeItems = ingredients;
        this.craftingLevel = level;
        this.baseCost = baseCost;
    }

    @Nullable
    public static MatchResult calculateMatches(SextupleRecipe recipe, NonNullList<ItemStack> inv) {
        if (inv.size() > 6)
            return null;
        for (ItemStack s : inv)
            if (!recipe.areItemsFitting(s))
                return null;
        NonNullList<ItemStack> list = NonNullList.create();
        NonNullList<ItemStack> bonus = NonNullList.create();
        for (Ingredient ingredient : recipe.getIngredients()) {
            for (ItemStack stack : inv) {
                if (ingredient.test(stack) && !list.contains(stack)) {
                    list.add(stack);
                    break;
                }
            }
        }
        if (list.size() != recipe.getIngredients().size())
            return null;
        for (ItemStack stack : inv) {
            if (!list.contains(stack))
                bonus.add(stack);
        }
        if (!recipe.requireExactMatch() && !bonus.isEmpty())
            return null;
        return new MatchResult(list, bonus);
    }

    @Nullable
    public static RecipeOutput getCraftingOutput(PlayerBoundCraftingContainer inv, RecipeHolder<? extends SextupleRecipe> holder) {
        SextupleRecipe recipe = holder.value();
        if (!recipe.matches(inv, inv.getPlayer().level()))
            return null;
        boolean unlocked = Platform.INSTANCE.getPlayerData(inv.getPlayer()).getRecipeKeeper().isUnlocked(holder);
        if (!unlocked && !GeneralConfig.recipeSystem.allowLocked)
            return null;
        return recipe.createOutput(inv, unlocked);
    }

    @Override
    public boolean matches(PlayerBoundCraftingContainer input, Level level) {
        if (input.getContainerSize() < 6)
            return false;
        NonNullList<ItemStack> stacks = NonNullList.create();
        for (int j = 0; j < 6; ++j) {
            ItemStack itemStack = input.getItem(j);
            if (!itemStack.isEmpty())
                stacks.add(itemStack);
        }
        MatchResult result = calculateMatches(this, stacks);
        return result != null;
    }

    @Override
    public ItemStack assemble(PlayerBoundCraftingContainer input, HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= this.recipeItems.size();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.getRecipeOutput();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    public RecipeOutput createOutput(PlayerBoundCraftingContainer inv, boolean unlocked) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        for (int j = 0; j < 6; ++j) {
            ItemStack itemStack = inv.getItem(j);
            if (!itemStack.isEmpty())
                stacks.add(itemStack);
        }
        MatchResult matches = calculateMatches(this, stacks);
        if (matches == null)
            return null;
        EnumCrafting type = EnumCrafting.FORGE;
        if (this.getType() == ModCrafting.ARMOR.get())
            type = EnumCrafting.ARMOR;
        if (this.getType() == ModCrafting.CHEMISTRY.get())
            type = EnumCrafting.CHEM;
        if (this.getType() == ModCrafting.COOKING.get())
            type = EnumCrafting.COOKING;
        ItemStack trueOutput = CraftingUtils.getCraftingOutput(this.getResultItem(inv.getPlayer().registryAccess()), inv, matches, type);
        return new RecipeOutput(trueOutput, unlocked ? trueOutput : new ItemStack(ModItems.UNKNOWN.get()), matches.bonusItems());
    }

    protected ItemStack getRecipeOutput() {
        return this.recipeOutput.copy();
    }

    public boolean areItemsFitting(ItemStack stack) {
        return true;
    }

    public boolean requireExactMatch() {
        return false;
    }

    public int getCraftingLevel() {
        return this.craftingLevel;
    }

    public int getAdditionalCost() {
        return this.baseCost;
    }

    @Override
    public String toString() {
        return String.format("Result: %s; Required Level: %d", this.recipeOutput, this.craftingLevel);
    }

    public record MatchResult(NonNullList<ItemStack> recipeMatches, NonNullList<ItemStack> bonusItems) {
    }

    public record RecipeOutput(ItemStack serverResult, ItemStack clientResult, NonNullList<ItemStack> bonusItems) {
    }

    public static class Serializer<T extends SextupleRecipe> implements RecipeSerializer<T> {

        private final MapCodec<T> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

        public Serializer(Factory<T> factory) {
            this.codec = mapCodec(factory);
            this.streamCodec = streamCodec(factory);
        }

        protected static <T extends SextupleRecipe> MapCodec<T> mapCodec(Factory<T> factory) {
            return RecordCodecBuilder.mapCodec(instance -> instance.group(
                            Codec.STRING.optionalFieldOf("group", "").forGetter(SextupleRecipe::getGroup),
                            ExtraCodecs.POSITIVE_INT.fieldOf("required_level").forGetter(SextupleRecipe::getCraftingLevel),
                            ExtraCodecs.POSITIVE_INT.fieldOf("crafting_cost").forGetter(SextupleRecipe::getAdditionalCost),
                            ItemStack.STRICT_CODEC.fieldOf("result").forGetter(SextupleRecipe::getRecipeOutput),
                            Ingredient.CODEC_NONEMPTY
                                    .listOf()
                                    .fieldOf("ingredients")
                                    .flatXmap(
                                            list -> {
                                                Ingredient[] ingredients = list.stream().filter(ingredient -> !ingredient.isEmpty()).toArray(Ingredient[]::new);
                                                if (ingredients.length == 0) {
                                                    return DataResult.error(() -> "No ingredients for shapeless recipe");
                                                } else {
                                                    return ingredients.length > 9
                                                            ? DataResult.error(() -> "Too many ingredients for shapeless recipe")
                                                            : DataResult.success(NonNullList.of(Ingredient.EMPTY, ingredients));
                                                }
                                            },
                                            DataResult::success
                                    )
                                    .forGetter(SextupleRecipe::getIngredients)
                    )
                    .apply(instance, factory::get));
        }

        protected static <T extends SextupleRecipe> StreamCodec<RegistryFriendlyByteBuf, T> streamCodec(Factory<T> factory) {
            return new StreamCodec<>() {
                @Override
                public T decode(RegistryFriendlyByteBuf buf) {
                    return factory.get(buf.readUtf(), buf.readInt(), buf.readInt(),
                            ItemStack.STREAM_CODEC.decode(buf), this.fromBuffer(buf));
                }

                private NonNullList<Ingredient> fromBuffer(RegistryFriendlyByteBuf buf) {
                    List<Ingredient> read = buf.readList(b -> Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
                    NonNullList<Ingredient> ingredients = NonNullList.withSize(read.size(), Ingredient.EMPTY);
                    for (int i = 0; i < read.size(); i++)
                        ingredients.set(i, read.get(i));
                    return ingredients;
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buf, T recipe) {
                    buf.writeUtf(recipe.getGroup());
                    buf.writeInt(recipe.getCraftingLevel());
                    buf.writeInt(recipe.getAdditionalCost());
                    ItemStack.STREAM_CODEC.encode(buf, recipe.getRecipeOutput());
                    buf.writeCollection(recipe.getIngredients(), (b, i) -> {
                        Ingredient.CONTENTS_STREAM_CODEC.encode(buf, i);
                    });
                }
            };
        }

        @Override
        public MapCodec<T> codec() {
            return this.codec;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
            return this.streamCodec;
        }

        public interface Factory<T extends SextupleRecipe> {
            T get(String group, int level, int cost, ItemStack result, NonNullList<Ingredient> ingredients);
        }
    }
}
