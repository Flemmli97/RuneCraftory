package io.github.flemmli97.runecraftory.common.items.consumables;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemDualBladeBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemGloveBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemHammerBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemLongSwordBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpearBase;
import io.github.flemmli97.runecraftory.common.recipes.CraftingType;
import io.github.flemmli97.runecraftory.common.recipes.ForgingRecipe;
import io.github.flemmli97.runecraftory.common.recipes.SextupleRecipe;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.runecraftory.common.utils.CraftingUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemComponentUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemRecipeBread extends Item {

    private final CraftingType type;

    public ItemRecipeBread(CraftingType type, Properties props) {
        super(props);
        this.type = type;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        if (living instanceof ServerPlayer player) {
            int amount = Math.max(1, ItemComponentUtils.itemLevel(stack) / 3);
            PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
            // Group equal recipes together. E.g. if an item has multiple variants of a recipe
            Map<Pair<Item, Integer>, List<RecipeHolder<SextupleRecipe>>> grouped = new HashMap<>();
            player.getServer().getRecipeManager().getAllRecipesFor(CraftingUtils.getType(this.type))
                    .stream().filter(r -> canUnlockRecipe(level, r, data, this.type.skill))
                    .forEach(r -> grouped.computeIfAbsent(Pair.of(r.value().getResultItem(level.registryAccess()).getItem(), r.value().getCraftingLevel()), k -> new ArrayList<>())
                            .add(r));
            Collection<RecipeHolder<SextupleRecipe>> unlocked = new ArrayList<>();
            grouped.entrySet().stream()
                    .sorted(Comparator.comparingInt(p -> p.getKey().getSecond()))
                    .limit(amount)
                    .forEach(r -> unlocked.addAll(r.getValue()));
            data.getRecipeKeeper().unlockRecipes(player, unlocked);
            if (unlocked.isEmpty())
                player.displayClientMessage(Component.translatable("runecraftory.misc.recipe.eat.fail"), false);
        }
        level.playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
        if (!(living instanceof Player player) || !(player.getAbilities().instabuild)) {
            stack.shrink(1);
        }
        return stack;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    private static boolean canUnlockRecipe(Level level, RecipeHolder<SextupleRecipe> r, PlayerData data, Skills skill) {
        SextupleRecipe recipe = r.value();
        if (recipe instanceof ForgingRecipe) {
            boolean weaponSkillCheck = true;
            Item res = recipe.getResultItem(level.registryAccess()).getItem();
            if (res instanceof ItemLongSwordBase) {
                weaponSkillCheck = (recipe.getCraftingLevel() - data.getSkillLevel(Skills.LONGSWORD).getLevel()) <= 5;
            } else if (res instanceof ItemDualBladeBase) {
                weaponSkillCheck = (recipe.getCraftingLevel() - data.getSkillLevel(Skills.DUAL).getLevel()) <= 5;
            } else if (res instanceof SwordItem) {
                weaponSkillCheck = (recipe.getCraftingLevel() - data.getSkillLevel(Skills.SHORTSWORD).getLevel()) <= 5;
            } else if (res instanceof ItemSpearBase) {
                weaponSkillCheck = (recipe.getCraftingLevel() - data.getSkillLevel(Skills.SPEAR).getLevel()) <= 5;
            } else if (res instanceof ItemHammerBase || res instanceof AxeItem) {
                weaponSkillCheck = (recipe.getCraftingLevel() - data.getSkillLevel(Skills.HAMMERAXE).getLevel()) <= 5;
            } else if (res instanceof ItemGloveBase) {
                weaponSkillCheck = (recipe.getCraftingLevel() - data.getSkillLevel(Skills.FIST).getLevel()) <= 5;
            }
            return weaponSkillCheck && !data.getRecipeKeeper().isUnlocked(r) && (recipe.getCraftingLevel() - data.getSkillLevel(skill).getLevel()) <= 5;
        }
        return !data.getRecipeKeeper().isUnlocked(r) && (recipe.getCraftingLevel() - data.getSkillLevel(skill).getLevel()) <= 5;
    }
}