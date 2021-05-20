package com.flemmli97.runecraftory.common.items.consumables;

import com.flemmli97.runecraftory.api.enums.EnumCrafting;
import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import com.flemmli97.runecraftory.common.utils.CraftingUtils;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.network.play.server.SRecipeBookPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class ItemRecipeBread extends Item {

    private final EnumCrafting type;

    public ItemRecipeBread(EnumCrafting type, Properties props) {
        super(props);
        this.type = type;
    }

    @Override
    public UseAction getUseAction(ItemStack p_77661_1_) {
        return UseAction.EAT;
    }

    @Override
    public int getUseDuration(ItemStack p_77626_1_) {
        return 32;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World p_77659_1_, PlayerEntity player, Hand hand) {
        player.setActiveHand(hand);
        return ActionResult.consume(player.getHeldItem(hand));
    }

    @Override
    public boolean hasEffect(ItemStack p_77636_1_) {
        return true;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity living) {
        if (living instanceof ServerPlayerEntity) {
            int amount = Math.max(1, ItemNBT.itemLevel(stack) / 3);
            ServerPlayerEntity player = (ServerPlayerEntity) living;
            AtomicBoolean success = new AtomicBoolean(false);
            Collection<SextupleRecipe> unlocked = player.getServer().getRecipeManager().listAllOfType(CraftingUtils.getType(this.type))
                    .stream().filter(r -> !player.getRecipeBook().isUnlocked(r) && r.getCraftingLevel() - player.getCapability(CapabilityInsts.PlayerCap).map(cap -> cap.getSkillLevel(this.getSkill())[0]).orElse(0) <= 5)
                    .sorted(Comparator.comparingInt(SextupleRecipe::getCraftingLevel))
                    .limit(amount).collect(Collectors.toList());
            unlocked.forEach(recipe -> {
                player.getRecipeBook().unlock(recipe);
                player.sendMessage(new TranslationTextComponent("recipe.eat.unlock", new TranslationTextComponent(recipe.getRecipeOutput().getTranslationKey())), Util.NIL_UUID);
                success.set(true);
            });
            player.connection.sendPacket(new SRecipeBookPacket(SRecipeBookPacket.State.ADD, unlocked.stream().map(SextupleRecipe::getId).collect(Collectors.toList()), Collections.emptyList(), player.getRecipeBook().getOptions()));
            if (!success.get())
                player.sendMessage(new TranslationTextComponent("recipe.eat.fail"), Util.NIL_UUID);
        }
        world.playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        if (!(living instanceof PlayerEntity) || !((PlayerEntity) living).abilities.isCreativeMode) {
            stack.shrink(1);
        }
        return stack;
    }

    private EnumSkills getSkill() {
        switch (this.type) {
            case FORGE:
                return EnumSkills.FORGING;
            case ARMOR:
                return EnumSkills.CRAFTING;
            case CHEM:
                return EnumSkills.CHEMISTRY;
            default:
                COOKING:
                return EnumSkills.COOKING;
        }
    }
}