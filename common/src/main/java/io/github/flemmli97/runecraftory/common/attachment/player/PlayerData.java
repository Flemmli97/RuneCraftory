package io.github.flemmli97.runecraftory.common.attachment.player;

import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.config.values.SkillProperties;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.npc.EnumShop;
import io.github.flemmli97.runecraftory.common.inventory.InventoryShippingBin;
import io.github.flemmli97.runecraftory.common.inventory.InventoryShop;
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
import io.github.flemmli97.runecraftory.common.registry.ModCriteria;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.ItemUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
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
    /**
     * first number is level, second is the xp a.k.a. percent to next level
     */
    private LevelExpPair levelN = new LevelExpPair();
    //private final Map<EnumSkills, int[]> skillMap = new HashMap<>();
    private final EnumMap<EnumSkills, LevelExpPair> skillMapN = new EnumMap<>(EnumSkills.class);

    private final InventorySpells spells = new InventorySpells();

    private final DailyPlayerUpdater updater = new DailyPlayerUpdater(this);

    private final RecipeKeeper keeper = new RecipeKeeper();

    //private QuestMission quest;
    private final Map<ResourceLocation, Integer> shippedItems = new HashMap<>();
    private final Map<EnumShop, NonNullList<ItemStack>> shopItems = new HashMap<>();
    private final InventoryShippingBin shipping = new InventoryShippingBin();
    //Food buff
    private Item lastFood;
    private int rpFoodBuff;
    private Map<Attribute, Double> foodBuffs = new HashMap<>();
    private int foodDuration;

    private final PlayerWeaponHandler weaponHandler = new PlayerWeaponHandler();

    private final WalkingTracker walkingTracker = new WalkingTracker();

    public final EntitySelector entitySelector = new EntitySelector();

    public final TamedEntityTracker tamedEntity = new TamedEntityTracker();

    public PlayerData() {
        for (EnumSkills skill : EnumSkills.values()) {
            this.skillMapN.put(skill, new LevelExpPair());
        }
    }

    public void setMaxHealth(Player player, float amount, boolean asBaseHealth) {
        AttributeInstance health = player.getAttribute(Attributes.MAX_HEALTH);
        AttributeModifier modifier = health.getModifier(LibConstants.maxHealthModifier);
        double val = amount - (asBaseHealth ? health.getBaseValue() : health.getValue());
        if (modifier != null && !asBaseHealth)
            val += modifier.getAmount();
        health.removeModifier(LibConstants.maxHealthModifier);
        health.addPermanentModifier(new AttributeModifier(LibConstants.maxHealthModifier, "rf.hpModifier", val, AttributeModifier.Operation.ADDITION));
    }

    private void setFoodHealthBonus(Player player, double amount) {
        AttributeInstance health = player.getAttribute(Attributes.MAX_HEALTH);
        health.removeModifier(LibConstants.foodMaxHealthModifier);
        health.addPermanentModifier(new AttributeModifier(LibConstants.foodMaxHealthModifier, "rf.food.hpModifier", amount, AttributeModifier.Operation.ADDITION));
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
                    player.hurt(CustomDamage.EXHAUST, Math.min(player.getMaxHealth() * 0.25f, (float) (diff * 2)));
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
            ModCriteria.MONEY_TRIGGER.trigger(serverPlayer);
            Platform.INSTANCE.sendToClient(new S2CMoney(this), serverPlayer);
        }
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

    public LevelExpPair getPlayerLevel() {
        return this.levelN;
    }

    public void setPlayerLevel(Player player, int level, float xpAmount, boolean recalc) {
        this.levelN.setLevel(Mth.clamp(level, 1, GeneralConfig.maxLevel));
        this.levelN.setXp(Mth.clamp(xpAmount, 0, LevelCalc.xpAmountForLevelUp(level)));
        if (player instanceof ServerPlayer serverPlayer) {
            if (recalc) {
                this.recalculateStats(serverPlayer, true);
            } else
                Platform.INSTANCE.sendToClient(new S2CLevelPkt(this), serverPlayer);
        }
    }

    public void addXp(Player player, float amount) {
        if (this.levelN.getLevel() >= GeneralConfig.maxLevel)
            return;
        boolean levelUp = this.levelN.addXP(amount, GeneralConfig.maxLevel, LevelCalc::xpAmountForLevelUp, () -> this.onLevelUp(player));
        if (levelUp) {
            player.level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1, 0.5f);
        }
        if (player instanceof ServerPlayer serverPlayer) {
            if (levelUp)
                ModCriteria.LEVEL_TRIGGER.trigger(serverPlayer);
            Platform.INSTANCE.sendToClient(new S2CLevelPkt(this), serverPlayer);
        }
    }

    private void onLevelUp(Player player) {
        this.setMaxHealth(player, player.getMaxHealth() + GeneralConfig.hpPerLevel, false);
        player.heal(GeneralConfig.hpPerLevel);
        this.runePointsMax += GeneralConfig.rpPerLevel;
        this.runePoints = Math.min(this.runePoints + (int) GeneralConfig.rpPerLevel, this.runePoints);
        this.str += GeneralConfig.strPerLevel;
        this.vit += GeneralConfig.vitPerLevel;
        this.intel += GeneralConfig.intPerLevel;
    }

    public void recalculateStats(ServerPlayer player, boolean regen) {
        int lvl = this.levelN.getLevel() - 1;
        this.setMaxHealth(player, GeneralConfig.hpPerLevel * lvl + GeneralConfig.startingHealth + this.skillVal(SkillProperties::healthIncrease).intValue(), true);
        this.runePointsMax = GeneralConfig.rpPerLevel * lvl + GeneralConfig.startingRP + this.skillVal(SkillProperties::rpIncrease).intValue();
        if (regen) {
            player.setHealth(player.getMaxHealth());
            this.runePoints = (int) this.runePointsMax;
        }
        this.str = GeneralConfig.strPerLevel * lvl + GeneralConfig.startingStr + this.skillVal(SkillProperties::strIncrease).intValue();
        this.intel = GeneralConfig.intPerLevel * lvl + GeneralConfig.startingIntel + this.skillVal(SkillProperties::intelIncrease).intValue();
        this.vit = GeneralConfig.vitPerLevel * lvl + GeneralConfig.startingVit + this.skillVal(SkillProperties::vitIncrease).intValue();
        Platform.INSTANCE.sendToClient(new S2CLevelPkt(this), player);
    }

    private Double skillVal(Function<SkillProperties, Number> func) {
        return Arrays.stream(EnumSkills.values()).mapToDouble(s -> (this.skillMapN.get(s).getLevel() - 1) * func.apply(GeneralConfig.skillProps.get(s)).doubleValue()).sum();
    }

    public LevelExpPair getSkillLevel(EnumSkills skill) {
        return this.skillMapN.get(skill);
    }

    public void setSkillLevel(EnumSkills skill, Player player, int level, float xpAmount, boolean recalc) {
        this.skillMapN.get(skill).setLevel(Mth.clamp(level, 1, GeneralConfig.maxSkillLevel));
        this.skillMapN.get(skill).setXp(Mth.clamp(xpAmount, 0, LevelCalc.xpAmountForSkillLevelUp(skill, level)));
        if (player instanceof ServerPlayer serverPlayer) {
            if (recalc) {
                this.recalculateStats(serverPlayer, true);
                player.setHealth(player.getMaxHealth());
            }
            Platform.INSTANCE.sendToClient(new S2CSkillLevelPkt(this, skill), serverPlayer);
        }
    }

    public void increaseSkill(EnumSkills skill, Player player, float amount) {
        if (this.skillMapN.get(skill).getLevel() >= GeneralConfig.maxSkillLevel)
            return;
        boolean levelUp = this.skillMapN.get(skill).addXP(amount, GeneralConfig.maxSkillLevel, lvl -> LevelCalc.xpAmountForSkillLevelUp(skill, lvl), () -> this.onSkillLevelUp(skill, player));
        if (levelUp) {
            player.level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1, 0.5f);
        }
        if (player instanceof ServerPlayer serverPlayer) {
            if (levelUp)
                ModCriteria.SKILL_LEVEL_TRIGGER.trigger(serverPlayer, skill);
            Platform.INSTANCE.sendToClient(new S2CSkillLevelPkt(this, skill), serverPlayer);
        }
    }

    private void onSkillLevelUp(EnumSkills skill, Player player) {
        SkillProperties prop = GeneralConfig.skillProps.get(skill);
        this.setMaxHealth(player, player.getMaxHealth() + prop.healthIncrease(), false);
        player.heal(prop.healthIncrease());
        this.runePointsMax += prop.rpIncrease();
        this.runePoints += prop.rpIncrease();
        this.str += prop.strIncrease();
        this.vit += prop.vitIncrease();
        this.intel += prop.intelIncrease();
    }

    public void increaseStatBonus(Player player, ItemStatIncrease.Stat type) {
        switch (type) {
            case STR -> {
                this.strAdd += 1;
                if (player instanceof ServerPlayer serverPlayer)
                    Platform.INSTANCE.sendToClient(new S2CItemStatBoost(type, false), serverPlayer);
            }
            case INT -> {
                this.intAdd += 1;
                if (player instanceof ServerPlayer serverPlayer)
                    Platform.INSTANCE.sendToClient(new S2CItemStatBoost(type, false), serverPlayer);
            }
            case VIT -> {
                this.vitAdd += 1;
                if (player instanceof ServerPlayer serverPlayer)
                    Platform.INSTANCE.sendToClient(new S2CItemStatBoost(type, false), serverPlayer);
            }
            case HP -> {
                AttributeInstance health = player.getAttribute(Attributes.MAX_HEALTH);
                AttributeModifier modifier = health.getModifier(LibConstants.maxHealthItemIncrease);
                double val = modifier == null ? 0 : modifier.getAmount();
                health.removeModifier(LibConstants.maxHealthItemIncrease);
                health.addPermanentModifier(new AttributeModifier(LibConstants.maxHealthModifier, "rf.item.hpModifier", val + 10, AttributeModifier.Operation.ADDITION));
            }
        }
    }

    public void resetAllStatBoost(Player player, ItemStatIncrease.Stat type) {
        switch (type) {
            case STR -> {
                this.strAdd = 0;
                if (player instanceof ServerPlayer serverPlayer)
                    Platform.INSTANCE.sendToClient(new S2CItemStatBoost(type, true), serverPlayer);
                this.strAdd = 0;
            }
            case INT -> {
                this.intAdd = 0;
                if (player instanceof ServerPlayer serverPlayer)
                    Platform.INSTANCE.sendToClient(new S2CItemStatBoost(type, true), serverPlayer);
            }
            case VIT -> {
                this.vitAdd = 0;
                if (player instanceof ServerPlayer serverPlayer)
                    Platform.INSTANCE.sendToClient(new S2CItemStatBoost(type, true), serverPlayer);
            }
            case HP -> {
                AttributeInstance health = player.getAttribute(Attributes.MAX_HEALTH);
                health.removeModifier(LibConstants.maxHealthItemIncrease);
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
        if (player instanceof ServerPlayer serverPlayer) {
            this.setPlayerAttTo(serverPlayer, Attributes.MAX_HEALTH, this.equipmentBonus(Attributes.MAX_HEALTH));
            this.setPlayerAttTo(serverPlayer, Attributes.MOVEMENT_SPEED, this.equipmentBonus(Attributes.MOVEMENT_SPEED));
            Platform.INSTANCE.sendToClient(new S2CEquipmentUpdate(slot), serverPlayer);
        }
    }

    private void setPlayerAttTo(ServerPlayer player, Attribute att, double val) {
        AttributeInstance inst = player.getAttribute(att);
        if (inst != null) {
            inst.removeModifier(LibConstants.equipmentModifier);
            inst.addTransientModifier(new AttributeModifier(LibConstants.equipmentModifier, "rf.equipment.mod", val, AttributeModifier.Operation.ADDITION));
        }
    }

    private double equipmentBonus(Attribute att) {
        return this.headBonus.getOrDefault(att, 0d) +
                this.bodyBonus.getOrDefault(att, 0d) +
                this.legsBonus.getOrDefault(att, 0d) +
                this.feetBonus.getOrDefault(att, 0d) +
                this.mainHandBonus.getOrDefault(att, 0d) +
                this.offHandBonus.getOrDefault(att, 0d);
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
            for (EnumShop profession : EnumShop.values()) {
                Collection<ItemStack> datapack = DataPackHandler.get(profession);
                List<ItemStack> shopItems = new ArrayList<>();
                datapack.forEach(item -> {
                    if (this.shippedItems.containsKey(PlatformUtils.INSTANCE.items().getIDFrom(item.getItem())))
                        shopItems.add(item);
                });
                if (shopItems.isEmpty())
                    continue;
                NonNullList<ItemStack> shop = NonNullList.create();
                for (float chance = 1.5f + shopItems.size() * 0.002f; player.level.random.nextFloat() < chance; chance -= 0.1f) {
                    ItemStack stack = shopItems.remove(player.level.random.nextInt(shopItems.size()));
                    shop.add(stack);
                    if (shopItems.isEmpty() || (profession == EnumShop.RANDOM && shop.size() >= InventoryShop.shopSize))
                        break;
                }
                this.shopItems.put(profession, shop);
            }
        }
    }

    public NonNullList<ItemStack> getShop(EnumShop shop) {
        NonNullList<ItemStack> list = NonNullList.create();
        list.addAll(this.shopItems.getOrDefault(shop, NonNullList.withSize(0, ItemStack.EMPTY)));
        return list;
    }

    public void addShippingItem(Player player, ItemStack item) {
        int level = ItemNBT.itemLevel(item);
        boolean changed = this.shippedItems.compute(PlatformUtils.INSTANCE.items().getIDFrom(item.getItem()), (k, v) -> v == null ? level : Math.max(v, level)) != level;
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
            float percent = (float) (d * 0.01f);
            double mult = 0;
            if (att == Attributes.MAX_HEALTH)
                mult += player.getMaxHealth() * percent;
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
        if (this.foodBuffs.containsKey(Attributes.MAX_HEALTH))
            this.setFoodHealthBonus(player, this.foodBuffs.get(Attributes.MAX_HEALTH));
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
        this.setFoodHealthBonus(player, 0);
        if (player instanceof ServerPlayer serverPlayer) {
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

    public PlayerWeaponHandler getWeaponHandler() {
        return this.weaponHandler;
    }

    public void update(Player player) {
        this.weaponHandler.tick(this, player);
        if (player instanceof ServerPlayer serverPlayer) {
            this.updater.tick(serverPlayer);
            if (serverPlayer.tickCount % 10 == 0)
                this.walkingTracker.tickWalkingTracker(serverPlayer);
            ItemStack main = player.getMainHandItem();
            ItemStack off = player.getOffhandItem();
            if (main.is(ModItems.inspector.get()) || off.is(ModItems.inspector.get())) {
                if (this.entitySelector.poi != null) {
                    serverPlayer.getLevel().sendParticles(serverPlayer, ParticleTypes.FLAME, true,
                            this.entitySelector.poi.getX() + 0.5, this.entitySelector.poi.getY() + 1.5, this.entitySelector.poi.getZ() + 0.5,
                            1, 0, 0, 0, 0);
                    serverPlayer.getLevel().sendParticles(serverPlayer, ParticleTypes.FLAME, true,
                            this.entitySelector.poi.getX() + 0.5, this.entitySelector.poi.getY() + 1.5, this.entitySelector.poi.getZ() + 0.5,
                            3, 0, 0, 0, 0.01);
                }
            } else
                this.entitySelector.reset();
        }
        this.getInv().update(player);
        this.foodDuration = Math.max(--this.foodDuration, -1);
        if (this.foodDuration == 0) {
            this.removeFoodEffect(player);
        }
    }

    public DailyPlayerUpdater getDailyUpdater() {
        return this.updater;
    }

    public void readFromNBT(CompoundTag nbt, Player player) {
        this.starting = nbt.getBoolean("Starting");
        this.unlockedRecipes = nbt.getBoolean("UnlockedRecipes");
        this.runePointsMax = nbt.getFloat("MaxRunePoints");
        this.runePoints = nbt.getInt("RunePoints");
        if (nbt.contains("DeathHP") && player != null) {
            float f = nbt.getFloat("DeathHP");
            if (f > 0)
                player.setHealth(f);
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
        this.levelN.read(nbt.get("Level"));
        this.strAdd = nbt.getFloat("StrengthBonus");
        this.vitAdd = nbt.getFloat("VitalityBonus");
        this.intAdd = nbt.getFloat("IntelligenceBonus");
        CompoundTag skillCompound = nbt.getCompound("Skills");
        for (EnumSkills skill : EnumSkills.values()) {
            this.skillMapN.get(skill).read(skillCompound.get(skill.toString()));
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
        this.updater.read(nbt.getCompound("DailyUpdater"));
        /*if (nbt.contains("Quest")) {
            this.quest = new QuestMission(nbt.getCompoundTag("Quest"));
        }*/
        this.readFoodBuffFromNBT(nbt.getCompound("FoodData"));
        if (player instanceof ServerPlayer serverPlayer && serverPlayer.connection != null) {
            this.recalculateStats(serverPlayer, false);
        }
        this.walkingTracker.read(nbt.getCompound("WalkingTracker"));
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
        nbt.put("Level", this.levelN.save());
        nbt.putFloat("StrengthBonus", this.strAdd);
        nbt.putFloat("VitalityBonus", this.vitAdd);
        nbt.putFloat("IntelligenceBonus", this.intAdd);
        CompoundTag skillCompound = new CompoundTag();
        for (EnumSkills skill : EnumSkills.values()) {
            skillCompound.put(skill.toString(), this.skillMapN.get(skill).save());
        }
        nbt.put("Skills", skillCompound);
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
        nbt.put("DailyUpdater", this.updater.save());
        /*if (this.quest != null) {
            nbt.setTag("Quest", this.quest.writeToNBT(new NBTTagCompound()));
        }*/
        nbt.put("FoodData", this.foodBuffNBT());
        nbt.put("WalkingTracker", this.walkingTracker.save());
        nbt.put("TamedEntityTracker", this.tamedEntity.save());
        return nbt;
    }

    public void resetAll(ServerPlayer player) {
        PlayerData newData = new PlayerData();
        newData.spells.load(this.spells.save());
        newData.shipping.load(this.shipping.save());
        this.readFromNBT(newData.writeToNBT(new CompoundTag(), null), null);
        this.recalculateStats(player, false);
        this.starting = false;
        this.tamedEntity.reset();
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