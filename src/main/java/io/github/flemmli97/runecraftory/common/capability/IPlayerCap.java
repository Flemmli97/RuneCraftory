package io.github.flemmli97.runecraftory.common.capability;

import io.github.flemmli97.runecraftory.api.enums.EnumShop;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.inventory.InventoryShippingBin;
import io.github.flemmli97.runecraftory.common.inventory.InventorySpells;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;

import java.util.Map;

public interface IPlayerCap {

    float getHealth(PlayerEntity player);

    void setHealth(PlayerEntity player, float amount);

    void regenHealth(PlayerEntity player, float amount);

    float getMaxHealth(PlayerEntity player);

    void setMaxHealth(PlayerEntity player, float amount);

    int getRunePoints();

    int getMaxRunePoints();

    boolean decreaseRunePoints(PlayerEntity player, int amount, boolean damage);

    void refreshRunePoints(PlayerEntity player, int amount);

    void setRunePoints(PlayerEntity player, int amount);

    void setMaxRunePoints(PlayerEntity player, int amount);

    int getMoney();

    boolean useMoney(PlayerEntity player, int amount);

    void setMoney(PlayerEntity player, int amount);

    /**
     * int[0] is the player level, int[1] is the experience gained for the current level
     */
    int[] getPlayerLevel();

    /**
     * Level via {@link LevelCalc#addXP}
     */
    void addXp(PlayerEntity player, int amount);

    void setPlayerLevel(PlayerEntity player, int level, int xpAmount);

    //=====Player stats

    float getStr();

    void setStr(PlayerEntity player, float amount);

    float getVit();

    void setVit(PlayerEntity player, float amount);

    float getIntel();

    void setIntel(PlayerEntity player, float amount);

    Item lastEatenFood();

    void applyFoodEffect(PlayerEntity player, ItemStack food);

    void removeFoodEffect(PlayerEntity player);

    int rpFoodBuff();

    Map<Attribute, Double> foodEffects();

    int foodBuffDuration();

    CompoundNBT foodBuffNBT();

    void readFoodBuffFromNBT(CompoundNBT nbt);

    void updateEquipmentStats(PlayerEntity player, EquipmentSlotType slot);

    double getAttributeValue(PlayerEntity player, Attribute att);

    //=====Skills

    int[] getSkillLevel(EnumSkills skill);

    void setSkillLevel(EnumSkills skill, PlayerEntity player, int level, int xp);

    /**
     * Level via {@link LevelCalc#levelSkill}
     */
    void increaseSkill(EnumSkills skill, PlayerEntity player, int xp);

    //=====NBT

    /**
     * @param player Used during death
     */
    void readFromNBT(CompoundNBT nbt, PlayerEntity player);

    /**
     * @param player Used during death
     */
    CompoundNBT writeToNBT(CompoundNBT nbt, PlayerEntity player);

    CompoundNBT resetNBT();

    //=====Inventory

    InventorySpells getInv();

    InventoryShippingBin getShippingInv();

    //Quest
    //@Nullable
    //QuestMission currentMission();

    //boolean acceptMission(QuestMission quest);

    boolean finishMission(PlayerEntity player);

    //Shop and Shipping

    void refreshShop(PlayerEntity player);

    /**
     * Client side updating
     */
    void setShop(PlayerEntity player, EnumShop shop, NonNullList<ItemStack> items);

    NonNullList<ItemStack> getShop(EnumShop shop);

    void addShippingItem(PlayerEntity player, ItemStack item);

    RecipeKeeper getRecipeKeeper();

    //Weapon and ticker

    int spellFlag();

    void setSpellFlag(int flag, int resetTime);

    int animationTick();

    void startAnimation(int tick);

    void update(PlayerEntity player);

    boolean canUseSpear();

    void startSpear();

    int getSpearTick();

    void disableOffHand();

    boolean canUseOffHand();

    Hand getPrevSwung();

    void setPrevSwung(Hand hand);

    void startWeaponSwing(WeaponSwing swing, int delay);

    boolean isAtUltimate();

    boolean startGlove(PlayerEntity player);

    enum WeaponSwing {
        SHORT(5),
        LONG(5),
        SPEAR(5),
        HAXE(5),
        DUAL(5),
        GLOVE(5);

        private int swingAmount;

        WeaponSwing(int swingAmount) {
            this.swingAmount = swingAmount;
        }

        int getMaxSwing() {
            return this.swingAmount;
        }
    }
}