package io.github.flemmli97.runecraftory.common.items.consumables;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.crafting.ForgingRecipe;
import io.github.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemDualBladeBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemGloveBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemHammerBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemLongSwordBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpearBase;
import io.github.flemmli97.runecraftory.common.utils.CraftingUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
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
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemRecipeBread extends Item {

    private final EnumCrafting type;

    public ItemRecipeBread(EnumCrafting type, Properties props) {
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
            int amount = Math.max(1, ItemNBT.itemLevel(stack) / 3);
            Platform.INSTANCE.getPlayerData(player)
                    .ifPresent(data -> {
                        // Group equal recipes together. E.g. if an item has multiple variants of a recipe
                        Map<Pair<Item, Integer>, List<SextupleRecipe>> grouped = new HashMap<>();
                        player.getServer().getRecipeManager().getAllRecipesFor(CraftingUtils.getType(this.type))
                                .stream().filter(r -> canUnlockRecipe(r, data, this.getSkill()))
                                .forEach(r -> grouped.computeIfAbsent(Pair.of(r.getResultItem().getItem(), r.getCraftingLevel()), k -> new ArrayList<>())
                                        .add(r));
                        Collection<SextupleRecipe> unlocked = new ArrayList<>();
                        grouped.entrySet().stream()
                                .sorted(Comparator.comparingInt(p -> p.getKey().getSecond()))
                                .limit(amount)
                                .forEach(r -> unlocked.addAll(r.getValue()));
                        data.getRecipeKeeper().unlockRecipes(player, unlocked);
                        if (unlocked.isEmpty())
                            player.sendMessage(new TranslatableComponent("runecraftory.misc.recipe.eat.fail"), Util.NIL_UUID);
                    });
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
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    private EnumSkills getSkill() {
        return switch (this.type) {
            case FORGE -> EnumSkills.FORGING;
            case ARMOR -> EnumSkills.CRAFTING;
            case CHEM -> EnumSkills.CHEMISTRY;
            default -> EnumSkills.COOKING;
        };
    }

    private static boolean canUnlockRecipe(SextupleRecipe r, PlayerData data, EnumSkills skill) {
        if (r instanceof ForgingRecipe) {
            boolean weaponSkillCheck = true;
            Item res = r.getResultItem().getItem();
            if (res instanceof ItemLongSwordBase) {
                weaponSkillCheck = (r.getCraftingLevel() - data.getSkillLevel(EnumSkills.LONGSWORD).getLevel()) <= 5;
            } else if (res instanceof ItemDualBladeBase) {
                weaponSkillCheck = (r.getCraftingLevel() - data.getSkillLevel(EnumSkills.DUAL).getLevel()) <= 5;
            } else if (res instanceof SwordItem) {
                weaponSkillCheck = (r.getCraftingLevel() - data.getSkillLevel(EnumSkills.SHORTSWORD).getLevel()) <= 5;
            } else if (res instanceof ItemSpearBase) {
                weaponSkillCheck = (r.getCraftingLevel() - data.getSkillLevel(EnumSkills.SPEAR).getLevel()) <= 5;
            } else if (res instanceof ItemHammerBase || res instanceof AxeItem) {
                weaponSkillCheck = (r.getCraftingLevel() - data.getSkillLevel(EnumSkills.HAMMERAXE).getLevel()) <= 5;
            } else if (res instanceof ItemGloveBase) {
                weaponSkillCheck = (r.getCraftingLevel() - data.getSkillLevel(EnumSkills.FIST).getLevel()) <= 5;
            }
            return weaponSkillCheck && !data.getRecipeKeeper().isUnlocked(r) && (r.getCraftingLevel() - data.getSkillLevel(skill).getLevel()) <= 5;
        }
        return !data.getRecipeKeeper().isUnlocked(r) && (r.getCraftingLevel() - data.getSkillLevel(skill).getLevel()) <= 5;
    }
}