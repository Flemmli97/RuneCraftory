package io.github.flemmli97.runecraftory.common.items.consumables;

import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.crafting.SextupleRecipe;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ItemRecipeBread extends Item {

    private final EnumCrafting type;

    public ItemRecipeBread(EnumCrafting type, Properties props) {
        super(props);
        this.type = type;
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
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        if (living instanceof ServerPlayer player) {
            int amount = Math.max(1, ItemNBT.itemLevel(stack) / 3);
            Platform.INSTANCE.getPlayerData(player)
                    .ifPresent(data -> {
                        Collection<SextupleRecipe> unlocked = player.getServer().getRecipeManager().getAllRecipesFor(CraftingUtils.getType(this.type))
                                .stream().filter(r -> !data.getRecipeKeeper().isUnlocked(r) && (r.getCraftingLevel() - data.getSkillLevel(this.getSkill())[0]) <= 5)
                                .sorted(Comparator.comparingInt(SextupleRecipe::getCraftingLevel))
                                .limit(amount).collect(Collectors.toList());
                        data.getRecipeKeeper().unlockRecipes(player, unlocked);
                        if (unlocked.isEmpty())
                            player.sendMessage(new TranslatableComponent("recipe.eat.fail"), Util.NIL_UUID);
                    });
        }
        level.playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
        if (!(living instanceof Player player) || !(player.getAbilities().instabuild)) {
            stack.shrink(1);
        }
        return stack;
    }

    private EnumSkills getSkill() {
        return switch (this.type) {
            case FORGE -> EnumSkills.FORGING;
            case ARMOR -> EnumSkills.CRAFTING;
            case CHEM -> EnumSkills.CHEMISTRY;
            default -> EnumSkills.COOKING;
        };
    }
}