package io.github.flemmli97.runecraftory.common.attachment;

import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.api.enums.EnumShop;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.config.values.SkillProperties;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.inventory.InventoryShippingBin;
import io.github.flemmli97.runecraftory.common.inventory.InventorySpells;
import io.github.flemmli97.runecraftory.common.items.tools.ItemStatIncrease;
import io.github.flemmli97.runecraftory.common.lib.LibConstants;
import io.github.flemmli97.runecraftory.common.network.S2CEquipmentUpdate;
import io.github.flemmli97.runecraftory.common.network.S2CFoodPkt;
import io.github.flemmli97.runecraftory.common.network.S2CItemStatBoost;
import io.github.flemmli97.runecraftory.common.network.S2CLevelPkt;
import io.github.flemmli97.runecraftory.common.network.S2CMaxRunePoints;
import io.github.flemmli97.runecraftory.common.network.S2CMoney;
import io.github.flemmli97.runecraftory.common.network.S2CPlayerStats;
import io.github.flemmli97.runecraftory.common.network.S2CRunePoints;
import io.github.flemmli97.runecraftory.common.network.S2CSkillLevelPkt;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.ItemUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public class PlayerData {

    public boolean starting, unlockedRecipes;
    //max runepoints possible: 2883
    private int money = GeneralConfig.startingMoney;
    private float runePointsMax = GeneralConfig.startingRP;
    private int runePoints = (int) this.runePointsMax;
    private float str = GeneralConfig.startingStr;
    private float vit = GeneralConfig.startingVit;
    private float intel = GeneralConfig.startingIntel;
    private float strAdd, vitAdd, intAdd;
    private Map<Attribute, Double> headBonus = new HashMap<>();
    private Map<Attribute, Double> bodyBonus = new HashMap<>();
    private Map<Attribute, Double> legsBonus = new HashMap<>();
    private Map<Attribute, Double> feetBonus = new HashMap<>();
    private Map<Attribute, Double> mainHandBonus = new HashMap<>();
    private Map<Attribute, Double> offHandBonus = new HashMap<>();
    private float shieldEfficiency = -1;
    private Map<ResourceLocation, Integer> shippedItems = new HashMap<>();
    private Map<EnumShop, NonNullList<ItemStack>> shopItems = new HashMap<>();
    private long lastUpdated;
    private RecipeKeeper keeper = new RecipeKeeper();
    /**
     * first number is level, second is the xp a.k.a. percent to next level
     */
    private int[] level = new int[]{1, 0};
    private Map<EnumSkills, int[]> skillMap = new HashMap<>();
    private InventorySpells spells = new InventorySpells();

    //private QuestMission quest;
    private InventoryShippingBin shipping = new InventoryShippingBin();
    //Food buff
    private Item lastFood;
    private int rpFoodBuff;
    private Map<Attribute, Double> foodBuffs = new HashMap<>();
    private int foodDuration;
    //Weapon and ticker
    private int fireballSpellFlag, bigFireballSpellFlag;
    private int spellTicker;
    private int ticker = 0;
    private WeaponSwing weapon;
    private int swings, timeSinceLastSwing;
    //Gloves charge
    private int gloveTick;
    private ItemStack glove = ItemStack.EMPTY;
    //Spear charge
    private int spearUseCounter = 0;
    private int spearTicker = 0;

    public PlayerData() {
        for (EnumSkills skill : EnumSkills.values()) {
            this.skillMap.put(skill, new int[]{1, 0});
        }
    }

    public float getHealth(Player player) {
        return player.getHealth();
    }

    public void setHealth(Player player, float amount) {
        if (amount > this.getMaxHealth(player)) {
            amount = this.getMaxHealth(player);
        }
        player.setHealth(amount);
        if (player.isDeadOrDying())
            player.die(CustomDamage.EXHAUST);
    }

    public void regenHealth(Player player, float amount) {
        this.setHealth(player, amount + this.getHealth(player));
    }

    public float getMaxHealth(Player player) {
        return (float) (player.getMaxHealth() + (this.foodBuffs.getOrDefault(Attributes.MAX_HEALTH, 0d)));
    }

    public void setMaxHealth(Player player, float amount) {
        AttributeInstance health = player.getAttribute(Attributes.MAX_HEALTH);
        health.removeModifier(LibConstants.maxHealthModifier);
        health.addPermanentModifier(new AttributeModifier(LibConstants.maxHealthModifier, "rf.hpModifier", amount - health.getBaseValue(), AttributeModifier.Operation.ADDITION));
    }

    public int getRunePoints() {
        return this.runePoints;
    }

    public int getMaxRunePoints() {
        return (int) (this.runePointsMax + this.rpFoodBuff);
    }

    public float getMaxRunePointsRaw() {
        return this.runePointsMax;
    }

    public boolean decreaseRunePoints(Player player, int amount, boolean damage) {
        if (!GeneralConfig.useRP && !player.level.isClientSide)
            return true;
        if (!player.isCreative()) {
            if (EntityUtils.isExhaust(player)) {
                amount *= 2;
            }
            if (this.runePoints >= amount)
                this.runePoints -= amount;
            else if (damage) {
                int diff = amount - this.runePoints;
                this.runePoints = 0;
                if (!player.level.isClientSide) {
                    player.hurt(CustomDamage.EXHAUST, (float) (diff * 2));
                    player.invulnerableTime = 10;
                }
            } else
                return false;
            if (player instanceof ServerPlayer serverPlayer)
                Platform.INSTANCE.sendToClient(new S2CRunePoints(this), serverPlayer);
            return true;
        }
        return true;
    }

    public void refreshRunePoints(Player player, int amount) {
        this.runePoints = Mth.clamp(this.runePoints + amount, 0, this.getMaxRunePoints());
        if (player instanceof ServerPlayer serverPlayer)
            Platform.INSTANCE.sendToClient(new S2CRunePoints(this), serverPlayer);
    }

    public void setRunePoints(Player player, int amount) {
        this.runePoints = amount;
        if (player instanceof ServerPlayer serverPlayer)
            Platform.INSTANCE.sendToClient(new S2CRunePoints(this), serverPlayer);
    }

    public void setMaxRunePoints(Player player, float amount) {
        this.runePointsMax = amount;
        if (player instanceof ServerPlayer serverPlayer)
            Platform.INSTANCE.sendToClient(new S2CMaxRunePoints(this), serverPlayer);
    }

    public int getMoney() {
        return this.money;
    }

    public boolean useMoney(Player player, int amount) {
        if (this.money >= amount) {
            this.money -= amount;
            if (player instanceof ServerPlayer serverPlayer) {
                Platform.INSTANCE.sendToClient(new S2CMoney(this), serverPlayer);
            }
            return true;
        }
        return false;
    }

    public void setMoney(Player player, int amount) {
        this.money = amount;
        if (player instanceof ServerPlayer serverPlayer) {
            Platform.INSTANCE.sendToClient(new S2CMoney(this), serverPlayer);
        }
    }

    public int[] getPlayerLevel() {
        return this.level;
    }

    public void addXp(Player player, int amount) {
        this.addXp(player, (int) (amount * GeneralConfig.xpMultiplier), false);
    }

    private void addXp(Player player, int amount, boolean leveledUp) {
        if (this.level[0] >= GeneralConfig.maxLevel)
            return;
        int neededXP = LevelCalc.xpAmountForLevelUp(this.level[0]);
        int xpToNextLevel = neededXP - this.level[1];
        if (amount >= xpToNextLevel) {
            int diff = amount - xpToNextLevel;
            this.level[0] += 1;
            this.level[1] = 0;
            this.onLevelUp(player);
            player.level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.2f, 1.0f);
            this.addXp(player, diff, true);
        } else {
            this.level[1] += amount;
            if (player instanceof ServerPlayer serverPlayer) {
                Platform.INSTANCE.sendToClient(new S2CLevelPkt(this), serverPlayer);
            }
        }
    }

    private void onLevelUp(Player player) {
        this.setMaxHealth(player, this.getMaxHealth(player) + GeneralConfig.hpPerLevel);
        this.regenHealth(player, GeneralConfig.hpPerLevel);
        this.runePointsMax += GeneralConfig.rpPerLevel;
        this.runePoints = Math.min(this.runePoints + (int) GeneralConfig.rpPerLevel, this.runePoints);
        this.str += GeneralConfig.strPerLevel;
        this.vit += GeneralConfig.vitPerLevel;
        this.intel += GeneralConfig.intPerLevel;
    }

    public void setPlayerLevel(Player player, int level, int xpAmount, boolean recalc) {
        this.level[0] = level;
        this.level[1] = xpAmount;
        if (player instanceof ServerPlayer serverPlayer) {
            if (recalc) {
                this.recalculateStats(serverPlayer, true);
            }
            Platform.INSTANCE.sendToClient(new S2CLevelPkt(this), serverPlayer);
        }
    }

    public void recalculateStats(ServerPlayer player, boolean regen) {
        int lvl = this.level[0] - 1;
        this.setMaxHealth(player, GeneralConfig.hpPerLevel * lvl + GeneralConfig.startingHealth + this.skillVal(SkillProperties::healthIncrease).intValue());
        this.runePointsMax = GeneralConfig.rpPerLevel * lvl + GeneralConfig.startingRP + this.skillVal(SkillProperties::rpIncrease).intValue();
        if (regen) {
            this.setHealth(player, this.getMaxHealth(player));
            this.runePoints = (int) this.runePointsMax;
        }
        this.str = GeneralConfig.strPerLevel * lvl + GeneralConfig.startingStr + this.skillVal(SkillProperties::strIncrease).intValue();
        this.intel = GeneralConfig.intPerLevel * lvl + GeneralConfig.startingIntel + this.skillVal(SkillProperties::intelIncrease).intValue();
        this.vit = GeneralConfig.vitPerLevel * lvl + GeneralConfig.startingVit + this.skillVal(SkillProperties::vitIncrease).intValue();
        Platform.INSTANCE.sendToClient(new S2CLevelPkt(this), player);
    }

    private Double skillVal(Function<SkillProperties, Number> func) {
        return Arrays.stream(EnumSkills.values()).mapToDouble(s -> (this.skillMap.get(s)[0] - 1) * func.apply(GeneralConfig.skillProps.get(s)).doubleValue()).sum();
    }

    public float getStr() {
        return this.str;
    }

    public void setStr(Player player, float amount) {
        this.str = amount;
        if (player instanceof ServerPlayer serverPlayer)
            Platform.INSTANCE.sendToClient(new S2CPlayerStats(this), serverPlayer);
    }

    public float getVit() {
        return this.vit;
    }

    public void setVit(Player player, float amount) {
        this.vit = amount;
        if (player instanceof ServerPlayer serverPlayer)
            Platform.INSTANCE.sendToClient(new S2CPlayerStats(this), serverPlayer);
    }

    public float getIntel() {
        return this.intel;
    }

    public void setIntel(Player player, float amount) {
        this.intel = amount;
        if (player instanceof ServerPlayer serverPlayer)
            Platform.INSTANCE.sendToClient(new S2CPlayerStats(this), serverPlayer);
    }

    public void consumeStatBoostItem(Player player, ItemStatIncrease.Stat type) {
        switch (type) {
            case STR -> {
                this.strAdd += 1;
                if (player instanceof ServerPlayer serverPlayer)
                    Platform.INSTANCE.sendToClient(new S2CItemStatBoost(type), serverPlayer);
            }
            case INT -> {
                this.intAdd += 1;
                if (player instanceof ServerPlayer serverPlayer)
                    Platform.INSTANCE.sendToClient(new S2CItemStatBoost(type), serverPlayer);
            }
            case VIT -> {
                this.vitAdd += 1;
                if (player instanceof ServerPlayer serverPlayer)
                    Platform.INSTANCE.sendToClient(new S2CItemStatBoost(type), serverPlayer);
            }
        }
    }

    public void resetAllStatBoost(Player player, ItemStatIncrease.Stat type) {
        switch (type) {
            case STR -> {
                this.strAdd = -10;
                if (player instanceof ServerPlayer serverPlayer)
                    Platform.INSTANCE.sendToClient(new S2CItemStatBoost(type), serverPlayer);
                this.strAdd = 0;
            }
            case INT -> {
                this.intAdd = -10;
                if (player instanceof ServerPlayer serverPlayer)
                    Platform.INSTANCE.sendToClient(new S2CItemStatBoost(type), serverPlayer);
                this.intAdd = 0;
            }
            case VIT -> {
                this.vitAdd = -10;
                if (player instanceof ServerPlayer serverPlayer)
                    Platform.INSTANCE.sendToClient(new S2CItemStatBoost(type), serverPlayer);
                this.vitAdd = 0;
            }
        }
    }

    public void updateEquipmentStats(Player player, EquipmentSlot slot) {
        ItemStack stack = player.getItemBySlot(slot);
        switch (slot) {
            case CHEST -> {
                this.bodyBonus = ItemNBT.statIncrease(stack);
                if (player.level.isClientSide) {
                    stack.getAttributeModifiers(slot).forEach((att, mod) ->
                            this.bodyBonus.merge(att, mod.getAmount(), (prev, v) -> prev += v));
                }
            }
            case FEET -> {
                this.feetBonus = ItemNBT.statIncrease(stack);
                if (player.level.isClientSide) {
                    stack.getAttributeModifiers(slot).forEach((att, mod) ->
                            this.feetBonus.merge(att, mod.getAmount(), (prev, v) -> prev += v));
                }
            }
            case HEAD -> {
                this.headBonus = ItemNBT.statIncrease(stack);
                if (player.level.isClientSide) {
                    stack.getAttributeModifiers(slot).forEach((att, mod) ->
                            this.headBonus.merge(att, mod.getAmount(), (prev, v) -> prev += v));
                }
            }
            case LEGS -> {
                this.legsBonus = ItemNBT.statIncrease(stack);
                if (player.level.isClientSide) {
                    stack.getAttributeModifiers(slot).forEach((att, mod) ->
                            this.legsBonus.merge(att, mod.getAmount(), (prev, v) -> prev += v));
                }
            }
            case MAINHAND -> {
                this.mainHandBonus = ItemNBT.statIncrease(stack);
                float eff = this.shieldEfficiency;
                this.shieldEfficiency = ItemUtils.getShieldEfficiency(player);
                if (eff != this.shieldEfficiency && !this.offHandBonus.isEmpty())
                    this.updateEquipmentStats(player, EquipmentSlot.OFFHAND);
                if (player.level.isClientSide) {
                    stack.getAttributeModifiers(slot).forEach((att, mod) ->
                            this.mainHandBonus.merge(att, mod.getAmount(), (prev, v) -> prev += v));
                    this.mainHandBonus.merge(Attributes.ATTACK_DAMAGE, (double) EnchantmentHelper.getDamageBonus(stack, MobType.UNDEFINED), (prev, v) -> prev += v);
                }
            }
            case OFFHAND -> {
                Map<Attribute, Double> inc = Platform.INSTANCE.isShield(stack, player) ? ItemNBT.statIncrease(stack) : new TreeMap<>(ModAttributes.sorted);
                inc.replaceAll((att, val) -> this.shieldEfficiency * val);
                this.offHandBonus = inc;
                if (player.level.isClientSide) {
                    stack.getAttributeModifiers(slot).forEach((att, mod) ->
                            this.offHandBonus.merge(att, mod.getAmount(), (prev, v) -> prev += v));
                }
            }
        }
        if (!player.level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            Platform.INSTANCE.sendToClient(new S2CEquipmentUpdate(slot), serverPlayer);
        }
    }

    public double getAttributeValue(Player player, Attribute att) {
        double val = Math.floor(this.headBonus.getOrDefault(att, 0d) +
                this.bodyBonus.getOrDefault(att, 0d) +
                this.legsBonus.getOrDefault(att, 0d) +
                this.feetBonus.getOrDefault(att, 0d) +
                this.mainHandBonus.getOrDefault(att, 0d) +
                this.offHandBonus.getOrDefault(att, 0d));
        float vit = this.getVit() + this.vitAdd;
        if (att == Attributes.ATTACK_DAMAGE)
            val += this.getStr() + this.strAdd;
        if (att == ModAttributes.RF_MAGIC.get())
            val += this.getIntel() + this.intAdd;
        if (att == ModAttributes.RF_DEFENCE.get())
            val += vit * 0.5;
        if (att == ModAttributes.RF_MAGIC_DEFENCE.get())
            val += vit * 0.5;
        val += this.foodBuffs.getOrDefault(att, 0d);

        AttributeMap atts = player.getAttributes();
        val += atts.hasAttribute(att) ? atts.getValue(att) : 0;
        return val;
    }

    public int[] getSkillLevel(EnumSkills skill) {
        return this.skillMap.get(skill);
    }

    public void setSkillLevel(EnumSkills skill, Player player, int level, int xp, boolean recalc) {
        this.skillMap.get(skill)[0] = level;
        this.skillMap.get(skill)[1] = xp;
        if (!player.level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            if (recalc) {
                this.recalculateStats(serverPlayer, true);
                this.setHealth(player, this.getMaxHealth(player));
            }
            Platform.INSTANCE.sendToClient(new S2CSkillLevelPkt(this, skill), serverPlayer);
        }
    }

    public void increaseSkill(EnumSkills skill, Player player, int xp) {
        this.increaseSkill(skill, player, xp, false);
    }

    private void increaseSkill(EnumSkills skill, Player player, int xp, boolean leveledUp) {
        if (this.skillMap.get(skill)[0] >= GeneralConfig.maxSkillLevel)
            return;
        int neededXP = LevelCalc.xpAmountForSkills(this.skillMap.get(skill)[0]);
        int xpToNextLevel = neededXP - this.skillMap.get(skill)[1];
        if (xp >= xpToNextLevel) {
            int diff = xp - xpToNextLevel;
            this.skillMap.get(skill)[0] += 1;
            this.skillMap.get(skill)[1] = 0;
            this.onSkillLevelUp(skill, player);
            player.level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.2f, 1.0f);
            this.increaseSkill(skill, player, diff, true);
        } else {
            this.skillMap.get(skill)[1] += xp;
            if (!player.level.isClientSide && player instanceof ServerPlayer serverPlayer) {
                Platform.INSTANCE.sendToClient(new S2CSkillLevelPkt(this, skill), serverPlayer);
            }
        }
    }

    private void onSkillLevelUp(EnumSkills skill, Player player) {
        SkillProperties prop = GeneralConfig.skillProps.get(skill);
        this.setMaxHealth(player, this.getMaxHealth(player) + prop.healthIncrease());
        this.regenHealth(player, prop.healthIncrease());
        this.runePointsMax += prop.rpIncrease();
        this.runePoints += prop.rpIncrease();
        this.str += prop.strIncrease();
        this.vit += prop.vitIncrease();
        this.intel += prop.intelIncrease();
    }

    public InventorySpells getInv() {
        return this.spells;
    }

    public InventoryShippingBin getShippingInv() {
        return this.shipping;
    }

    /*@Override
    public QuestMission currentMission() {
        return this.quest;
    }

    public boolean acceptMission(QuestMission quest) {
        if (this.quest == null && quest.questObjective()!=null) {
            this.quest = quest;
            return true;
        }
        return false;
    }*/

    public boolean finishMission(Player player) {
        /*if (this.quest != null && this.quest.questObjective().isFinished())
        {
            for (ItemStack stack : this.quest.questObjective().rewards()) {
                ItemUtils.spawnItemAtEntity(player, stack);
            }
            this.setMoney(player, this.getMoney() + this.quest.questObjective().moneyReward());
            this.quest = null;
            return true;
        }*/
        return false;
    }


    public void refreshShop(Player player) {
        if (!player.level.isClientSide) {
            /*Set<ExtendedItemStackWrapper> ignore = NPCShopItems.starterItems();
            for (EnumShop profession : EnumShop.values()) {
                List<ItemStack> list = NPCShopItems.getShopList(profession);
                list.removeIf((stack) ->
                {
                    ExtendedItemStackWrapper wr = new ExtendedItemStackWrapper(stack);
                    return !this.shippedItems.keySet().contains(wr.getItem().getRegistryName().toString()) && !ignore.contains(wr);
                });
                if (list.isEmpty())
                    continue;
                NonNullList<ItemStack> shop = NonNullList.create();
                Set<ExtendedItemStackWrapper> pre = new HashSet<>();
                for (float chance = 2.0f + list.size() * 0.002f; player.level.random.nextFloat() < chance; chance -= 0.1f) {
                    pre.add(new ExtendedItemStackWrapper(list.get(player.level.random.nextInt(list.size()))));
                }
                for (ExtendedItemStackWrapper wr : pre) {
                    ItemStack stack = wr.getStack();
                    shop.add(ItemNBT.getLeveledItem(stack, this.shippedItems.getOrDefault(stack.getItem().getRegistryName().toString(), 1)));
                }
                this.shopItems.put(profession, shop);
                 Platform.INSTANCE.sendToClient(new PacketUpdateShopItems(profession, shop), (ServerPlayer) player);
            }*/
        }
    }

    public void setShop(Player player, EnumShop shop, NonNullList<ItemStack> items) {
        if (player.level.isClientSide)
            this.shopItems.put(shop, items);
    }

    public NonNullList<ItemStack> getShop(EnumShop shop) {
        return this.shopItems.getOrDefault(shop, NonNullList.withSize(0, ItemStack.EMPTY));
    }

    public void addShippingItem(Player player, ItemStack item) {
        /*int level = player.world.isRemote || !NPCShopItems.leveledItems().contains(new ExtendedItemStackWrapper(item)) ? 1 : ItemNBT.itemLevel(item);
        boolean changed = this.shippedItems.compute(item.getItem().getRegistryName().toString(), (k, v) -> v == null ? level : Math.max(v, level)) != level;
        if (!player.world.isRemote && changed)
             Platform.INSTANCE.sendToClient(new PacketUpdateShippingItem(item, level), (ServerPlayer) player);*/
    }

    public RecipeKeeper getRecipeKeeper() {
        return this.keeper;
    }

    public Item lastEatenFood() {
        return this.lastFood;
    }

    public void applyFoodEffect(Player player, ItemStack stack) {
        this.removeFoodEffect(player);
        FoodProperties prop = DataPackHandler.getFoodStat(stack.getItem());
        Map<Attribute, Double> gain = prop.effects();
        prop.effectsMultiplier().forEach((att, d) -> {
            float percent = (float) (d > 0 ? 1 + d * 0.01f : 1 - d * 0.01f);
            double mult = 0;
            if (att == Attributes.MAX_HEALTH)
                mult += this.getMaxHealth(player) * percent;
            else if (att == Attributes.ATTACK_DAMAGE)
                mult += this.str * percent;
            else if (att == ModAttributes.RF_DEFENCE.get())
                mult += this.vit * 0.5 * percent;
            else if (att == ModAttributes.RF_MAGIC.get())
                mult += this.intel * percent;
            else if (att == ModAttributes.RF_DEFENCE.get())
                mult += this.vit * 0.5 * percent;
            mult += gain.getOrDefault(att, 0d);
            gain.put(att, mult);
        });
        this.rpFoodBuff = (int) this.runePointsMax * prop.getRpPercentIncrease() + prop.getRpIncrease();
        this.foodBuffs = gain;
        this.foodDuration = prop.duration();
        this.lastFood = stack.getItem();
        if (player instanceof ServerPlayer serverPlayer) {
            Platform.INSTANCE.sendToClient(new S2CFoodPkt(stack), serverPlayer);
        }
    }

    public void removeFoodEffect(Player player) {
        this.foodBuffs = Collections.emptyMap();
        this.foodDuration = -1;
        this.rpFoodBuff = 0;
        this.lastFood = null;
        if (!player.level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            Platform.INSTANCE.sendToClient(new S2CFoodPkt(null), serverPlayer);
        }
    }

    public int rpFoodBuff() {
        return this.rpFoodBuff;
    }

    public Map<Attribute, Double> foodEffects() {
        return this.foodBuffs;
    }

    public int foodBuffDuration() {
        return this.foodDuration;
    }

    public CompoundTag foodBuffNBT() {
        CompoundTag nbt = new CompoundTag();
        if (this.lastFood != null)
            nbt.putString("LastFood", PlatformUtils.INSTANCE.items().getIDFrom(this.lastFood).toString());
        CompoundTag compound3 = new CompoundTag();
        for (Map.Entry<Attribute, Double> entry : this.foodBuffs.entrySet()) {
            compound3.putDouble(PlatformUtils.INSTANCE.attributes().getIDFrom(entry.getKey()).toString(), entry.getValue());
        }
        nbt.put("FoodBuffs", compound3);
        nbt.putInt("FoodBuffRP", this.rpFoodBuff);
        nbt.putInt("FoodBuffDuration", this.foodDuration);
        return nbt;
    }

    public void readFoodBuffFromNBT(CompoundTag nbt) {
        if (nbt.contains("LastFood"))
            this.lastFood = PlatformUtils.INSTANCE.items().getFromId(new ResourceLocation(nbt.getString("LastFood")));
        if (nbt.contains("FoodBuffs")) {
            CompoundTag tag = nbt.getCompound("FoodBuffs");
            for (String s : tag.getAllKeys()) {
                this.foodBuffs.put(PlatformUtils.INSTANCE.attributes().getFromId(new ResourceLocation(s)), tag.getDouble(s));
            }
        }
        this.rpFoodBuff = nbt.getInt("FoodBuffRP");
        this.foodDuration = nbt.getInt("FoodBuffDuration");
    }

    public int fireballSpellFlag() {
        return this.fireballSpellFlag;
    }

    public void setFireballSpellFlag(int flag, int resetTime) {
        this.fireballSpellFlag = flag;
        this.spellTicker = resetTime;
    }

    public int bigFireballSpellFlag() {
        return this.bigFireballSpellFlag;
    }

    public void setBigFireballSpellFlag(int flag, int resetTime) {
        this.bigFireballSpellFlag = flag;
        this.spellTicker = resetTime;
    }

    public int animationTick() {
        return this.ticker;
    }

    public void startAnimation(int tick) {
        this.ticker = tick;
    }

    public void update(Player player) {
        if (!player.level.isClientSide) {
            if ((WorldUtils.canUpdateDaily(player.level) || Math.abs(player.level.getGameTime() / 24000 - this.lastUpdated / 24000) >= 1)) {
                this.getShippingInv().shipItems(player);
                this.refreshShop(player);
                this.lastUpdated = player.level.getGameTime();
            }
            if (--this.spellTicker == 0) {
                this.fireballSpellFlag = 0;
                this.bigFireballSpellFlag = 0;
            }
            this.updateGlove(player);
            --this.spearTicker;
        }
        this.getInv().update(player);
        this.ticker = Math.max(--this.ticker, 0);
        this.foodDuration = Math.max(--this.foodDuration, -1);
        if (this.foodDuration == 0) {
            this.removeFoodEffect(player);
        }
        this.timeSinceLastSwing = Math.max(--this.timeSinceLastSwing, 0);
        if (this.timeSinceLastSwing == 0) {
            this.swings = 0;
        }
    }

    public boolean canStartGlove() {
        return this.gloveTick <= 0;
    }

    public void startGlove(ItemStack stack) {
        this.gloveTick = 35;
        this.glove = stack;
    }

    private void updateGlove(Player player) {
        if (this.gloveTick <= 0)
            return;
        --this.gloveTick;
        Vec3 look = player.getLookAngle();
        Vec3 move = new Vec3(look.x, 0.0, look.z).normalize().scale(0.8).add(0, player.getDeltaMovement().y, 0);
        player.setDeltaMovement(move);
        player.hurtMarked = true;
        if (this.gloveTick % 4 == 0) {
            List<LivingEntity> list = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(1.0));
            if (!list.isEmpty())
                LevelCalc.useRP(player, this, 5, true, false, true, 1, EnumSkills.FIST);
            for (LivingEntity e : list) {
                if (e != player) {
                    this.increaseSkill(EnumSkills.FIST, player, 5);
                    CombatUtils.playerAttackWithItem(player, e, this.glove, 0.5f, false, false, false);
                }
            }
        }
    }

    public boolean canStartSpear() {
        return this.spearTicker <= 0 || this.spearUseCounter++ > 20;
    }

    public void startSpear() {
        this.spearTicker = 80;
        this.spearUseCounter = 0;
    }

    public void onUseSpear() {
        this.spearUseCounter++;
    }

    public void startWeaponSwing(WeaponSwing swing, int delay) {
        if (this.weapon != swing) {
            this.swings = 0;
        }
        ++this.swings;
        this.timeSinceLastSwing = delay;
        this.weapon = swing;
    }

    public boolean isAtUltimate() {
        return this.weapon.getMaxSwing() == this.swings;
    }

    public void readFromNBT(CompoundTag nbt, Player player) {
        this.starting = nbt.getBoolean("Starting");
        this.unlockedRecipes = nbt.getBoolean("UnlockedRecipes");
        this.runePointsMax = nbt.getFloat("MaxRunePoints");
        this.runePoints = nbt.getInt("RunePoints");
        if (nbt.contains("DeathHP") && player != null) {
            float f = nbt.getFloat("DeathHP");
            if (f > 0)
                this.setHealth(player, f);
        }
        if (nbt.contains("MaxHP") && player != null) {
            AttributeInstance health = player.getAttribute(Attributes.MAX_HEALTH);
            health.removeModifier(LibConstants.maxHealthModifier);
            health.addPermanentModifier(new AttributeModifier(LibConstants.maxHealthModifier, "rf.hpModifier", nbt.getDouble("MaxHP"), AttributeModifier.Operation.ADDITION));
        }
        this.money = nbt.getInt("Money");
        this.str = nbt.getFloat("Strength");
        this.vit = nbt.getFloat("Vitality");
        this.intel = nbt.getFloat("Intelligence");
        this.level = nbt.getIntArray("Level");
        this.strAdd = nbt.getFloat("StrengthBonus");
        this.vitAdd = nbt.getFloat("VitalityBonus");
        this.intAdd = nbt.getFloat("IntelligenceBonus");
        CompoundTag compound = nbt.getCompound("Skills");
        for (EnumSkills skill : EnumSkills.values()) {
            this.skillMap.put(skill, compound.getIntArray(skill.toString()));
        }
        this.spells.load(nbt.getCompound("Inventory"));
        this.shipping.load(nbt.getCompound("Shipping"));
        CompoundTag shipped = nbt.getCompound("ShippedItems");
        for (String key : shipped.getAllKeys()) {
            this.shippedItems.put(new ResourceLocation(key), shipped.getInt(key));
        }
        CompoundTag shops = nbt.getCompound("ShopItems");
        for (EnumShop shop : EnumShop.values()) {
            NonNullList<ItemStack> items = NonNullList.create();
            shops.getList(shop.toString(), Tag.TAG_COMPOUND).forEach(comp ->
                    items.add(ItemStack.of((CompoundTag) comp)));
            this.shopItems.put(shop, items);
        }
        this.keeper.read(nbt.getCompound("Recipes"));
        this.lastUpdated = nbt.getLong("LastUpdated");
        /*if (nbt.contains("Quest")) {
            this.quest = new QuestMission(nbt.getCompoundTag("Quest"));
        }*/
        this.readFoodBuffFromNBT(nbt.getCompound("FoodData"));
        if (player instanceof ServerPlayer serverPlayer && serverPlayer.connection != null) {
            this.recalculateStats(serverPlayer, false);
        }
    }

    public CompoundTag writeToNBT(CompoundTag nbt, Player player) {
        nbt.putBoolean("Starting", this.starting);
        nbt.putBoolean("UnlockedRecipes", this.unlockedRecipes);
        nbt.putFloat("MaxRunePoints", this.runePointsMax);
        if (player == null) {
            nbt.putInt("RunePoints", this.runePoints);
        } else if (!player.isAlive()) {
            AttributeModifier modifier = player.getAttribute(Attributes.MAX_HEALTH).getModifier(LibConstants.maxHealthModifier);
            if (modifier != null)
                nbt.putDouble("MaxHP", modifier.getAmount());
            nbt.putFloat("DeathHP", player.getMaxHealth() * GeneralConfig.deathHPPercent);
            nbt.putInt("RunePoints", (int) (this.runePointsMax * GeneralConfig.deathRPPercent));
        }
        nbt.putInt("Money", this.money);
        nbt.putFloat("Strength", this.str);
        nbt.putFloat("Vitality", this.vit);
        nbt.putFloat("Intelligence", this.intel);
        nbt.putIntArray("Level", this.level);
        nbt.putFloat("StrengthBonus", this.strAdd);
        nbt.putFloat("VitalityBonus", this.vitAdd);
        nbt.putFloat("IntelligenceBonus", this.intAdd);
        CompoundTag compound = new CompoundTag();
        for (EnumSkills skill : EnumSkills.values()) {
            compound.putIntArray(skill.toString(), this.skillMap.get(skill));
        }
        nbt.put("Skills", compound);
        nbt.put("Inventory", this.spells.save());
        nbt.put("Shipping", this.shipping.save());
        CompoundTag ship = new CompoundTag();
        this.shippedItems.forEach((key, value) -> ship.putInt(key.toString(), value));
        nbt.put("ShippedItems", ship);
        CompoundTag shop = new CompoundTag();
        for (Map.Entry<EnumShop, NonNullList<ItemStack>> entry : this.shopItems.entrySet()) {
            ListTag l = new ListTag();
            for (ItemStack stack : entry.getValue())
                l.add(stack.save(new CompoundTag()));
            shop.put(entry.getKey().toString(), l);
        }
        nbt.put("ShopItems", shop);
        nbt.put("Recipes", this.keeper.save());
        nbt.putLong("LastUpdated", this.lastUpdated);
        /*if (this.quest != null) {
            nbt.setTag("Quest", this.quest.writeToNBT(new NBTTagCompound()));
        }*/
        nbt.put("FoodData", this.foodBuffNBT());
        return nbt;
    }

    public CompoundTag resetNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.put("Inventory", this.spells.save());
        return nbt;
    }

    public enum WeaponSwing {
        SHORT(5),
        LONG(5),
        SPEAR(5),
        HAXE(5),
        DUAL(5),
        GLOVE(5);

        private final int swingAmount;

        WeaponSwing(int swingAmount) {
            this.swingAmount = swingAmount;
        }

        int getMaxSwing() {
            return this.swingAmount;
        }
    }
}