package com.flemmli97.runecraftory.common.capability;

import com.flemmli97.runecraftory.api.datapack.FoodProperties;
import com.flemmli97.runecraftory.api.enums.EnumShop;
import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.common.config.GeneralConfig;
import com.flemmli97.runecraftory.common.config.values.SkillProperties;
import com.flemmli97.runecraftory.common.datapack.DataPackHandler;
import com.flemmli97.runecraftory.common.inventory.InventoryShippingBin;
import com.flemmli97.runecraftory.common.inventory.InventorySpells;
import com.flemmli97.runecraftory.common.lib.LibConstants;
import com.flemmli97.runecraftory.common.network.PacketHandler;
import com.flemmli97.runecraftory.common.network.S2CEquipmentUpdate;
import com.flemmli97.runecraftory.common.network.S2CFoodPkt;
import com.flemmli97.runecraftory.common.network.S2CLevelPkt;
import com.flemmli97.runecraftory.common.network.S2CMaxRunePoints;
import com.flemmli97.runecraftory.common.network.S2CMoney;
import com.flemmli97.runecraftory.common.network.S2CPlayerStats;
import com.flemmli97.runecraftory.common.network.S2CRunePoints;
import com.flemmli97.runecraftory.common.network.S2CSkillLevelPkt;
import com.flemmli97.runecraftory.common.registry.ModAttributes;
import com.flemmli97.runecraftory.common.utils.CustomDamage;
import com.flemmli97.runecraftory.common.utils.EntityUtils;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import com.flemmli97.runecraftory.common.utils.ItemUtils;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import com.flemmli97.runecraftory.common.utils.WorldUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class PlayerCapImpl implements IPlayerCap, ICapabilitySerializable<CompoundNBT> {

    private final LazyOptional<IPlayerCap> holder = LazyOptional.of(() -> this);

    //max runepoints possible: 2883
    private int money = GeneralConfig.startingMoney;
    private int runePointsMax = GeneralConfig.startingRP;
    private int runePoints = this.runePointsMax;
    private float str = GeneralConfig.startingStr;
    private float vit = GeneralConfig.startingVit;
    private float intel = GeneralConfig.startingIntel;
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
    private InventoryShippingBin shipping = new InventoryShippingBin();

    //private QuestMission quest;

    //Food buff
    private Item lastFood;
    private int rpFoodBuff;
    private Map<Attribute, Double> foodBuffs = new HashMap<>();
    private int foodDuration;

    //Weapon and ticker
    private int spellFlag;
    private int spellTicker;

    private int ticker = 0;
    private int offHandTick;
    private Hand prevHand = Hand.MAIN_HAND;
    private WeaponSwing weapon;
    private int swings, timeSinceLastSwing;

    //Gloves charge
    private boolean usingGloves;
    private int gloveTick;

    //Spear charge
    private int spearUse = 0;
    private int spearTicker = 0;

    public PlayerCapImpl() {
        for (EnumSkills skill : EnumSkills.values()) {
            this.skillMap.put(skill, new int[]{1, 0});
        }
    }

    @Override
    public float getHealth(PlayerEntity player) {
        return player.getHealth();
    }

    @Override
    public void setHealth(PlayerEntity player, float amount) {
        if (amount > this.getMaxHealth(player)) {
            amount = this.getMaxHealth(player);
        }
        player.setHealth(amount);
    }

    @Override
    public void regenHealth(PlayerEntity player, float amount) {
        this.setHealth(player, amount + this.getHealth(player));
    }

    @Override
    public float getMaxHealth(PlayerEntity player) {
        return (float) (player.getMaxHealth() + (this.foodBuffs.getOrDefault(Attributes.MAX_HEALTH, 0d)));
    }

    @Override
    public void setMaxHealth(PlayerEntity player, float amount) {
        ModifiableAttributeInstance health = player.getAttribute(Attributes.MAX_HEALTH);
        health.removeModifier(LibConstants.maxHealthModifier);
        health.applyPersistentModifier(new AttributeModifier(LibConstants.maxHealthModifier, "rf.hpModifier", amount - health.getBaseValue(), AttributeModifier.Operation.ADDITION));
    }

    @Override
    public int getRunePoints() {
        return this.runePoints;
    }

    @Override
    public int getMaxRunePoints() {
        return this.runePointsMax + this.rpFoodBuff;
    }

    @Override
    public boolean decreaseRunePoints(PlayerEntity player, int amount, boolean damage) {
        if (!player.isCreative()) {
            if (EntityUtils.isExhaust(player)) {
                amount *= 2;
            }
            if (this.runePoints >= amount)
                this.runePoints -= amount;
            else if (damage) {
                int diff = amount - this.runePoints;
                this.runePoints = 0;
                if (!player.world.isRemote) {
                    player.attackEntityFrom(CustomDamage.EXHAUST, (float) (diff * 2));
                    player.hurtResistantTime = 10;
                }
            } else
                return false;
            if (player instanceof ServerPlayerEntity)
                PacketHandler.sendToClient(new S2CRunePoints(this), (ServerPlayerEntity) player);
            return true;
        }
        return true;
    }

    @Override
    public void refreshRunePoints(PlayerEntity player, int amount) {
        this.runePoints = Math.min(this.getMaxRunePoints(), this.runePoints + amount);
        if (player instanceof ServerPlayerEntity)
            PacketHandler.sendToClient(new S2CRunePoints(this), (ServerPlayerEntity) player);
    }

    @Override
    public void setRunePoints(PlayerEntity player, int amount) {
        this.runePoints = amount;
        if (player instanceof ServerPlayerEntity)
            PacketHandler.sendToClient(new S2CRunePoints(this), (ServerPlayerEntity) player);
    }

    @Override
    public void setMaxRunePoints(PlayerEntity player, int amount) {
        this.runePointsMax = amount;
        if (player instanceof ServerPlayerEntity)
            PacketHandler.sendToClient(new S2CMaxRunePoints(this), (ServerPlayerEntity) player);
    }

    @Override
    public int getMoney() {
        return this.money;
    }

    @Override
    public boolean useMoney(PlayerEntity player, int amount) {
        if (this.money >= amount) {
            this.money -= amount;
            if (player instanceof ServerPlayerEntity) {
                PacketHandler.sendToClient(new S2CMoney(this), (ServerPlayerEntity) player);
            }
            return true;
        }
        return false;
    }

    @Override
    public void setMoney(PlayerEntity player, int amount) {
        this.money = amount;
        if (player instanceof ServerPlayerEntity) {
            PacketHandler.sendToClient(new S2CMoney(this), (ServerPlayerEntity) player);
        }
    }

    @Override
    public int[] getPlayerLevel() {
        return this.level;
    }

    @Override
    public void addXp(PlayerEntity player, int amount) {
        this.addXp(player, (int) (amount * GeneralConfig.xpMultiplier), false);
    }

    private void addXp(PlayerEntity player, int amount, boolean leveledUp) {
        //if (!player.capabilities.isCreativeMode)
        {
            int neededXP = LevelCalc.xpAmountForLevelUp(this.level[0]);
            int xpToNextLevel = neededXP - this.level[1];
            if (amount >= xpToNextLevel) {
                int diff = amount - xpToNextLevel;
                this.level[0] += 1;
                this.level[1] = 0;
                this.onLevelUp(player);
                player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.2f, 1.0f);
                this.addXp(player, diff, true);
            } else {
                this.level[1] += amount;
                if (player instanceof ServerPlayerEntity) {
                    PacketHandler.sendToClient(new S2CLevelPkt(this, leveledUp ? S2CLevelPkt.Type.LEVELUP : S2CLevelPkt.Type.SET), (ServerPlayerEntity) player);
                }
            }
        }
    }

    private void onLevelUp(PlayerEntity player) {
        this.setMaxHealth(player, this.getMaxHealth(player) + GeneralConfig.hpPerLevel);
        this.regenHealth(player, GeneralConfig.hpPerLevel);
        this.runePointsMax += GeneralConfig.rpPerLevel;
        this.runePoints = Math.min(this.runePoints + GeneralConfig.rpPerLevel, this.runePoints);
        this.str += GeneralConfig.strPerLevel;
        this.vit += GeneralConfig.vitPerLevel;
        this.intel += GeneralConfig.intPerLevel;
    }

    @Override
    public void setPlayerLevel(PlayerEntity player, int level, int xpAmount) {
        this.level[0] = level;
        this.level[1] = xpAmount;
        if (!player.world.isRemote && player instanceof ServerPlayerEntity) {
            PacketHandler.sendToClient(new S2CLevelPkt(this, S2CLevelPkt.Type.LEVELUP), (ServerPlayerEntity) player);
        }
    }

    @Override
    public float getStr() {
        return this.str;
    }

    @Override
    public void setStr(PlayerEntity player, float amount) {
        this.str = amount;
        if (player instanceof ServerPlayerEntity)
            PacketHandler.sendToClient(new S2CPlayerStats(this), (ServerPlayerEntity) player);
    }

    @Override
    public float getVit() {
        return this.vit;
    }

    @Override
    public void setVit(PlayerEntity player, float amount) {
        this.vit = amount;
        if (player instanceof ServerPlayerEntity)
            PacketHandler.sendToClient(new S2CPlayerStats(this), (ServerPlayerEntity) player);
    }

    @Override
    public float getIntel() {
        return this.intel;
    }

    @Override
    public void setIntel(PlayerEntity player, float amount) {
        this.intel = amount;
        if (player instanceof ServerPlayerEntity)
            PacketHandler.sendToClient(new S2CPlayerStats(this), (ServerPlayerEntity) player);
    }

    @Override
    public void updateEquipmentStats(PlayerEntity player, EquipmentSlotType slot) {
        ItemStack stack = player.getItemStackFromSlot(slot);
        switch (slot) {
            case CHEST:
                this.bodyBonus = ItemNBT.statIncrease(stack);
                if (player.world.isRemote) {
                    stack.getAttributeModifiers(slot).forEach((att, mod) ->
                            this.bodyBonus.merge(att, mod.getAmount(), (prev, v) -> prev += v));
                }
                break;
            case FEET:
                this.feetBonus = ItemNBT.statIncrease(stack);
                if (player.world.isRemote) {
                    stack.getAttributeModifiers(slot).forEach((att, mod) ->
                            this.feetBonus.merge(att, mod.getAmount(), (prev, v) -> prev += v));
                }
                break;
            case HEAD:
                this.headBonus = ItemNBT.statIncrease(stack);
                if (player.world.isRemote) {
                    stack.getAttributeModifiers(slot).forEach((att, mod) ->
                            this.headBonus.merge(att, mod.getAmount(), (prev, v) -> prev += v));
                }
                break;
            case LEGS:
                this.legsBonus = ItemNBT.statIncrease(stack);
                if (player.world.isRemote) {
                    stack.getAttributeModifiers(slot).forEach((att, mod) ->
                            this.legsBonus.merge(att, mod.getAmount(), (prev, v) -> prev += v));
                }
                break;
            case MAINHAND:
                this.mainHandBonus = ItemNBT.statIncrease(stack);
                float eff = this.shieldEfficiency;
                this.shieldEfficiency = ItemUtils.getShieldEfficiency(player);
                if (eff != this.shieldEfficiency && !this.offHandBonus.isEmpty())
                    this.updateEquipmentStats(player, EquipmentSlotType.OFFHAND);
                if (player.world.isRemote) {
                    stack.getAttributeModifiers(slot).forEach((att, mod) ->
                            this.mainHandBonus.merge(att, mod.getAmount(), (prev, v) -> prev += v));
                    this.mainHandBonus.merge(Attributes.ATTACK_DAMAGE, (double) EnchantmentHelper.getModifierForCreature(stack, CreatureAttribute.UNDEFINED), (prev, v) -> prev += v);
                }
                break;
            case OFFHAND:
                Map<Attribute, Double> inc = stack.getItem().isShield(stack, player) ? ItemNBT.statIncrease(stack) : new TreeMap<>(ModAttributes.sorted);
                inc.replaceAll((att, val) -> this.shieldEfficiency * val);
                this.offHandBonus = inc;
                if (player.world.isRemote) {
                    stack.getAttributeModifiers(slot).forEach((att, mod) ->
                            this.offHandBonus.merge(att, mod.getAmount(), (prev, v) -> prev += v));
                }
                break;
        }
        if (!player.world.isRemote && player instanceof ServerPlayerEntity) {
            PacketHandler.sendToClient(new S2CEquipmentUpdate(slot), (ServerPlayerEntity) player);
        }
    }

    @Override
    public double getAttributeValue(PlayerEntity player, Attribute att) {
        double val = Math.floor(this.headBonus.getOrDefault(att, 0d) +
                this.bodyBonus.getOrDefault(att, 0d) +
                this.legsBonus.getOrDefault(att, 0d) +
                this.feetBonus.getOrDefault(att, 0d) +
                this.mainHandBonus.getOrDefault(att, 0d) +
                this.offHandBonus.getOrDefault(att, 0d));
        if (att == Attributes.ATTACK_DAMAGE)
            val += this.getStr();
        if (att == ModAttributes.RF_MAGIC.get())
            val += this.getIntel();
        if (att == ModAttributes.RF_DEFENCE.get())
            val += this.getVit() * 0.5;
        if (att == ModAttributes.RF_MAGIC_DEFENCE.get())
            val += this.getVit() * 0.5;
        val += this.foodBuffs.getOrDefault(att, 0d);

        AttributeModifierManager atts = player.getAttributeManager();
        val += atts.hasAttributeInstance(att) ? atts.getAttributeValue(att) : 0;
        return val;
    }

    @Override
    public int[] getSkillLevel(EnumSkills skill) {
        return this.skillMap.get(skill);
    }

    @Override
    public void setSkillLevel(EnumSkills skill, PlayerEntity player, int level, int xp) {
        this.skillMap.get(skill)[0] = level;
        this.skillMap.get(skill)[1] = xp;
        if (!player.world.isRemote && player instanceof ServerPlayerEntity) {
            PacketHandler.sendToClient(new S2CSkillLevelPkt(this, skill, S2CSkillLevelPkt.Type.SET), (ServerPlayerEntity) player);
        }
    }

    @Override
    public void increaseSkill(EnumSkills skill, PlayerEntity player, int xp) {
        this.increaseSkill(skill, player, xp, false);
    }

    private void increaseSkill(EnumSkills skill, PlayerEntity player, int xp, boolean leveledUp) {
        //if (!player.capabilities.isCreativeMode)
        {
            int neededXP = LevelCalc.xpAmountForSkills(this.skillMap.get(skill)[0]);
            int xpToNextLevel = neededXP - this.skillMap.get(skill)[1];
            if (xp >= xpToNextLevel) {
                int diff = xp - xpToNextLevel;
                this.skillMap.get(skill)[0] += 1;
                this.skillMap.get(skill)[1] = 0;
                this.onSkillLevelUp(skill, player);
                player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.2f, 1.0f);
                this.increaseSkill(skill, player, diff, true);
            } else {
                this.skillMap.get(skill)[1] += xp;
                if (!player.world.isRemote && player instanceof ServerPlayerEntity) {
                    PacketHandler.sendToClient(new S2CSkillLevelPkt(this, skill, leveledUp ? S2CSkillLevelPkt.Type.LEVELUP : S2CSkillLevelPkt.Type.SET), (ServerPlayerEntity) player);
                }
            }
        }
    }

    private void onSkillLevelUp(EnumSkills skill, PlayerEntity player) {
        SkillProperties prop = GeneralConfig.skillProps.get(skill);
        this.setMaxHealth(player, this.getHealth(player) + prop.getHealthIncrease());
        this.regenHealth(player, prop.getHealthIncrease());
        this.runePointsMax += prop.getRPIncrease();
        this.runePoints += prop.getRPIncrease();
        this.str += prop.getStrIncrease();
        this.vit += prop.getVitIncrease();
        this.intel += prop.getIntelIncrease();
    }

    @Override
    public InventorySpells getInv() {
        return this.spells;
    }

    @Override
    public InventoryShippingBin getShippingInv() {
        return this.shipping;
    }

    /*@Override
    public QuestMission currentMission() {
        return this.quest;
    }

    @Override
    public boolean acceptMission(QuestMission quest) {
        if (this.quest == null && quest.questObjective()!=null) {
            this.quest = quest;
            return true;
        }
        return false;
    }*/

    @Override
    public boolean finishMission(PlayerEntity player) {
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


    @Override
    public void refreshShop(PlayerEntity player) {
       /* if (!player.world.isRemote) {
            Set<ExtendedItemStackWrapper> ignore = NPCShopItems.starterItems();
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
                for (float chance = 2.0f + list.size() * 0.002f; player.world.rand.nextFloat() < chance; chance -= 0.1f) {
                    pre.add(new ExtendedItemStackWrapper(list.get(player.world.rand.nextInt(list.size()))));
                }
                for (ExtendedItemStackWrapper wr : pre) {
                    ItemStack stack = wr.getStack();
                    shop.add(ItemNBT.getLeveledItem(stack, this.shippedItems.getOrDefault(stack.getItem().getRegistryName().toString(), 1)));
                }
                this.shopItems.put(profession, shop);
                PacketHandler.sendToClient(new PacketUpdateShopItems(profession, shop), (ServerPlayerEntity) player);
            }
        }*/
    }

    @Override
    public void setShop(PlayerEntity player, EnumShop shop, NonNullList<ItemStack> items) {
        if (player.world.isRemote)
            this.shopItems.put(shop, items);
    }

    @Override
    public NonNullList<ItemStack> getShop(EnumShop shop) {
        return this.shopItems.getOrDefault(shop, NonNullList.withSize(0, ItemStack.EMPTY));
    }

    @Override
    public void addShippingItem(PlayerEntity player, ItemStack item) {
        /*int level = player.world.isRemote || !NPCShopItems.leveledItems().contains(new ExtendedItemStackWrapper(item)) ? 1 : ItemNBT.itemLevel(item);
        boolean changed = this.shippedItems.compute(item.getItem().getRegistryName().toString(), (k, v) -> v == null ? level : Math.max(v, level)) != level;
        if (!player.world.isRemote && changed)
            PacketHandler.sendToClient(new PacketUpdateShippingItem(item, level), (ServerPlayerEntity) player);*/
    }

    @Override
    public RecipeKeeper getRecipeKeeper() {
        return this.keeper;
    }

    @Override
    public Item lastEatenFood() {
        return this.lastFood;
    }

    @Override
    public void applyFoodEffect(PlayerEntity player, ItemStack stack) {
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
        this.rpFoodBuff = this.runePointsMax * prop.getRpPercentIncrease() + prop.getRpIncrease();
        this.foodBuffs = gain;
        this.foodDuration = prop.duration();
        this.lastFood = stack.getItem();
        if (player instanceof ServerPlayerEntity) {
            PacketHandler.sendToClient(new S2CFoodPkt(stack), (ServerPlayerEntity) player);
        }
    }

    @Override
    public void removeFoodEffect(PlayerEntity player) {
        this.foodBuffs = Collections.emptyMap();
        this.foodDuration = -1;
        this.rpFoodBuff = 0;
        this.lastFood = null;
        if (!player.world.isRemote && player instanceof ServerPlayerEntity) {
            PacketHandler.sendToClient(new S2CFoodPkt(null), (ServerPlayerEntity) player);
        }
    }

    @Override
    public int rpFoodBuff() {
        return this.rpFoodBuff;
    }

    @Override
    public Map<Attribute, Double> foodEffects() {
        return this.foodBuffs;
    }

    @Override
    public int foodBuffDuration() {
        return this.foodDuration;
    }

    @Override
    public CompoundNBT foodBuffNBT() {
        CompoundNBT nbt = new CompoundNBT();
        if (this.lastFood != null)
            nbt.putString("LastFood", this.lastFood.getRegistryName().toString());
        CompoundNBT compound3 = new CompoundNBT();
        for (Map.Entry<Attribute, Double> entry : this.foodBuffs.entrySet()) {
            compound3.putDouble(entry.getKey().getRegistryName().toString(), entry.getValue());
        }
        nbt.put("FoodBuffs", compound3);
        nbt.putInt("FoodBuffRP", this.rpFoodBuff);
        nbt.putInt("FoodBuffDuration", this.foodDuration);
        return nbt;
    }

    @Override
    public void readFoodBuffFromNBT(CompoundNBT nbt) {
        if (nbt.contains("LastFood"))
            this.lastFood = ForgeRegistries.ITEMS.getValue(new ResourceLocation(nbt.getString("LastFood")));
        if (nbt.contains("FoodBuffs")) {
            CompoundNBT tag = nbt.getCompound("FoodBuffs");
            for (String s : tag.keySet()) {
                this.foodBuffs.put(ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(s)), tag.getDouble(s));
            }
        }
        this.rpFoodBuff = nbt.getInt("FoodBuffRP");
        this.foodDuration = nbt.getInt("FoodBuffDuration");
    }

    @Override
    public int spellFlag() {
        return this.spellFlag;
    }

    @Override
    public void setSpellFlag(int flag, int resetTime) {
        this.spellFlag = flag;
        this.spellTicker = resetTime;
    }

    @Override
    public int animationTick() {
        return this.ticker;
    }

    @Override
    public void startAnimation(int tick) {
        this.ticker = tick;
    }

    @Override
    public boolean startGlove(PlayerEntity player) {
        if (this.usingGloves) {
            return false;
        }
        this.usingGloves = true;
        this.gloveTick = 60;
        return true;
    }

    private void updateGlove(ServerPlayerEntity player) {
        --this.gloveTick;
        Vector3d look = player.getLookVec();
        Vector3d move = new Vector3d(look.x, 0.0, look.z).normalize().scale(0.4);
        player.setMotion(move.x, player.getMotion().y, move.z);
        for (LivingEntity e : player.world.getEntitiesWithinAABB(LivingEntity.class, player.getBoundingBox().grow(1.0))) {
            if (e != player) {
                float damagePhys = (float) this.getAttributeValue(player, Attributes.ATTACK_DAMAGE);
                this.decreaseRunePoints(player, 2, true);
                this.increaseSkill(EnumSkills.FIST, player, 5);
                /*if (!(e instanceof IEntityBase)) {
                    damagePhys = LevelCalc.scaleForVanilla(damagePhys);
                }*/
                //RFCalculations.playerDamage(player, e, CustomDamage.attack((EntityLivingBase) player, ItemNBT.getElement(player.getHeldItemMainhand()), CustomDamage.DamageType.NORMAL, CustomDamage.KnockBackType.VANILLA, 1.0f, 20), damagePhys, this, player.getHeldItemMainhand());
            }
        }
        if (this.gloveTick == 0) {
            this.usingGloves = false;
        }
    }

    @Override
    public void update(PlayerEntity player) {
        if (!player.world.isRemote) {
            if ((WorldUtils.canUpdateDaily(player.world) || Math.abs(player.world.getGameTime() / 24000 - this.lastUpdated / 24000) >= 1)) {
                this.getShippingInv().shipItems(player);
                this.refreshShop(player);
                this.lastUpdated = player.world.getGameTime();
            }
            if (--this.spellTicker == 0) {
                this.spellFlag = 0;
            }
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
        /*if (this.usingGloves) {
            this.updateGlove(player);
        }*/
        this.spearTicker = Math.max(--this.spearTicker, 0);
        this.offHandTick = Math.max(--this.offHandTick, 0);
    }

    @Override
    public boolean canUseSpear() {
        if (this.spearTicker > 0 && this.spearUse++ < 20) {
            return true;
        }
        this.spearUse = 0;
        this.spearTicker = 0;
        return false;
    }

    @Override
    public void startSpear() {
        this.spearTicker = 60;
    }

    @Override
    public int getSpearTick() {
        return this.spearTicker;
    }

    @Override
    public void disableOffHand() {
        this.offHandTick = 100;
    }

    @Override
    public boolean canUseOffHand() {
        return this.offHandTick == 0;
    }

    @Override
    public Hand getPrevSwung() {
        return this.prevHand;
    }

    @Override
    public void setPrevSwung(Hand hand) {
        this.prevHand = hand;
    }

    @Override
    public void startWeaponSwing(WeaponSwing swing, int delay) {
        if (this.weapon != swing) {
            this.swings = 0;
        }
        ++this.swings;
        this.timeSinceLastSwing = delay;
        this.weapon = swing;
    }

    @Override
    public boolean isAtUltimate() {
        return this.weapon.getMaxSwing() == this.swings;
    }


    @Override
    public void readFromNBT(CompoundNBT nbt, PlayerEntity player) {
        this.runePointsMax = nbt.getInt("MaxRunePoints");
        this.runePoints = nbt.getInt("RunePoints");
        if (nbt.contains("DeathHP") && player != null) {
            float f = nbt.getFloat("DeathHP");
            if (f > 0)
                this.setHealth(player, f);
        }
        this.money = nbt.getInt("Money");
        this.str = nbt.getFloat("Strength");
        this.vit = nbt.getFloat("Vitality");
        this.intel = nbt.getFloat("Intelligence");
        this.level = nbt.getIntArray("Level");
        CompoundNBT compound = nbt.getCompound("Skills");
        for (EnumSkills skill : EnumSkills.values()) {
            this.skillMap.put(skill, compound.getIntArray(skill.toString()));
        }
        if (nbt.contains("Inventory")) {
            CompoundNBT compound2 = nbt.getCompound("Inventory");
            this.spells.readFromNBT(compound2);
        }
        this.shipping.deserializeNBT(nbt.getCompound("Shipping"));
        CompoundNBT shipped = nbt.getCompound("ShippedItems");
        for (String key : shipped.keySet()) {
            this.shippedItems.put(new ResourceLocation(key), shipped.getInt(key));
        }
        CompoundNBT shops = nbt.getCompound("ShopItems");
        for (EnumShop shop : EnumShop.values()) {
            NonNullList<ItemStack> items = NonNullList.create();
            shops.getList(shop.toString(), Constants.NBT.TAG_COMPOUND).forEach(comp ->
                    items.add(ItemStack.read((CompoundNBT) comp)));
            this.shopItems.put(shop, items);
        }
        this.keeper.read(nbt.getCompound("Recipes"));
        this.lastUpdated = nbt.getLong("LastUpdated");
        /*if (nbt.contains("Quest")) {
            this.quest = new QuestMission(nbt.getCompoundTag("Quest"));
        }*/
        this.readFoodBuffFromNBT(nbt.getCompound("FoodData"));
    }

    @Override
    public CompoundNBT writeToNBT(CompoundNBT nbt, PlayerEntity player) {
        nbt.putInt("MaxRunePoints", this.runePointsMax);
        if (player == null) {
            nbt.putInt("RunePoints", this.runePoints);
        } else {
            nbt.putFloat("DeathHP", player.getMaxHealth() / 2.0f);
            nbt.putInt("RunePoints", (int) (this.runePointsMax * 0.3));
        }
        nbt.putInt("Money", this.money);
        nbt.putFloat("Strength", this.str);
        nbt.putFloat("Vitality", this.vit);
        nbt.putFloat("Intelligence", this.intel);
        nbt.putIntArray("Level", this.level);
        CompoundNBT compound = new CompoundNBT();
        for (EnumSkills skill : EnumSkills.values()) {
            compound.putIntArray(skill.toString(), this.skillMap.get(skill));
        }
        nbt.put("Skills", compound);
        CompoundNBT compound2 = new CompoundNBT();
        this.spells.writeToNBT(compound2);
        nbt.put("Inventory", compound2);
        nbt.put("Shipping", this.shipping.serializeNBT());
        CompoundNBT ship = new CompoundNBT();
        this.shippedItems.forEach((key, value) -> ship.putInt(key.toString(), value));
        nbt.put("ShippedItems", ship);
        CompoundNBT shop = new CompoundNBT();
        for (Map.Entry<EnumShop, NonNullList<ItemStack>> entry : this.shopItems.entrySet()) {
            ListNBT l = new ListNBT();
            for (ItemStack stack : entry.getValue())
                l.add(stack.serializeNBT());
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

    @Override
    public CompoundNBT resetNBT() {
        CompoundNBT nbt = new CompoundNBT();
        CompoundNBT spellNBT = new CompoundNBT();
        this.spells.writeToNBT(spellNBT);
        nbt.put("Inventory", spellNBT);
        return nbt;
    }

    @Override
    public CompoundNBT serializeNBT() {
        return this.writeToNBT(new CompoundNBT(), null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.readFromNBT(nbt, null);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        return CapabilityInsts.PlayerCap.orEmpty(cap, this.holder);
    }
}