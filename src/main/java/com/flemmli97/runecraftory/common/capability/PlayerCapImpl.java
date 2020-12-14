/*package com.flemmli97.runecraftory.common.capability;

import com.flemmli97.runecraftory.api.enums.EnumShop;
import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.common.inventory.InventoryShippingBin;
import com.flemmli97.runecraftory.common.inventory.InventorySpells;
import com.flemmli97.runecraftory.common.registry.ModAttributes;
import com.flemmli97.runecraftory.common.utils.CustomDamage;
import com.flemmli97.runecraftory.common.utils.EntityUtils;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import com.flemmli97.runecraftory.lib.LibEntityConstants;
import com.flemmli97.runecraftory.network.PacketHandler;
import com.flemmli97.runecraftory.network.S2CMaxRunePoints;
import com.flemmli97.runecraftory.network.S2CMoney;
import com.flemmli97.runecraftory.network.S2CRunePoints;
import com.flemmli97.tenshilib.api.config.ExtendedItemStackWrapper;
import com.flemmli97.tenshilib.common.utils.ItemUtils;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.util.Constants;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlayerCapImpl implements IPlayerCap {

    //max runepoints possible: 2883
    private int money = 0;
    private int runePointsMax = 56;
    private int runePoints = this.runePointsMax;
    private float str = 5;
    private float vit = 4;
    private float intel = 5;
    private Map<Attribute, Integer> headBonus = Maps.newHashMap();
    private Map<Attribute, Integer> bodyBonus = Maps.newHashMap();
    private Map<Attribute, Integer> legsBonus = Maps.newHashMap();
    private Map<Attribute, Integer> feetBonus = Maps.newHashMap();
    private Map<Attribute, Integer> mainHandBonus = Maps.newHashMap();
    private Map<Attribute, Integer> offHandBonus = Maps.newHashMap();

    private Map<String, Integer> shippedItems = Maps.newHashMap();
    private Map<EnumShop, NonNullList<ItemStack>> shopItems = Maps.newHashMap();
    private long lastUpdated;

    /**
     * first number is level, second is the xp a.k.a. percent to next level
     */
    /*private int[] level = new int[]{1, 0};

    private Map<EnumSkills, int[]> skillMap = Maps.newHashMap();
    private InventorySpells spells = new InventorySpells();
    private InventoryShippingBin shipping = new InventoryShippingBin();

    //private QuestMission quest;

    //Food buff
    private int rpFoodBuff;
    private Map<Attribute, Integer> foodBuffs = Maps.newHashMap();
    private int foodDuration;

    //Weapon and ticker
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
        return player.getMaxHealth() + (this.foodBuffs.getOrDefault(Attributes.GENERIC_MAX_HEALTH, 0));
    }

    @Override
    public void setMaxHealth(PlayerEntity player, float amount) {
        ModifiableAttributeInstance health = player.getAttribute(Attributes.GENERIC_MAX_HEALTH);
        health.removeModifier(LibEntityConstants.maxHealthModifier);
        health.addPersistentModifier(new AttributeModifier(LibEntityConstants.maxHealthModifier, "rf.hpModifier", amount - health.getBaseValue(), AttributeModifier.Operation.ADDITION));
    }

    @Override
    public int getRunePoints() {
        return this.runePoints;
    }

    @Override
    public int getMaxRunePoints() {
        return this.runePointsMax + this.rpFoodBuff;
    }

    //TODO: boolean forced. so healing spells wont damage player when not enough rp
    @Override
    public boolean decreaseRunePoints(PlayerEntity player, int amount, boolean damage) {
        if (!player.isCreative()) {
            if (EntityUtils.isExhaust(player)) {
                amount *= 2;
            }
            if (this.runePoints >= amount)
                this.runePoints -= amount;
            else {
                int diff = amount - this.runePoints;
                this.runePoints = 0;
                if (!player.world.isRemote && damage)
                    player.attackEntityFrom(CustomDamage.EXHAUST, (float) (diff * 2));
            }
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
        //if (!player.capabilities.isCreativeMode)
        {
            int neededXP = LevelCalc.xpAmountForLevelUp(this.level[0]);
            int xpToNextLevel = neededXP - this.level[1];
            if (amount >= xpToNextLevel) {
                int diff = amount - xpToNextLevel;
                this.level[0] += 1;
                this.level[1] = 0;
                this.onLevelUp(player);
                player.world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.2f, 1.0f);
                this.addXp(player, diff);
            } else {
                this.level[1] += amount;
            }
            if (!player.world.isRemote && player instanceof ServerPlayerEntity) {
                PacketHandler.sendToClient(new PacketUpdateClient(this), (ServerPlayerEntity) player);
            }
        }
    }

    private void onLevelUp(PlayerEntity player) {
        this.setMaxHealth(player, this.getMaxHealth(player) + 10);
        this.regenHealth(player, 10);
        this.runePointsMax += 5;
        this.runePoints = Math.min(this.runePoints + 5, this.runePoints);
        this.str += 2.0f;
        this.vit += 2.0f;
        this.intel += 2.0f;
    }

    @Override
    public void setPlayerLevel(PlayerEntity player, int level, int xpAmount) {
        this.level[0] = level;
        this.level[1] = xpAmount;
        if (!player.world.isRemote && player instanceof ServerPlayerEntity) {
            PacketHandler.sendToClient(new PacketPlayerLevel(this), (ServerPlayerEntity) player);
        }
    }

    @Override
    public float getStr() {
        return this.str;
    }

    @Override
    public void setStr(PlayerEntity player, float amount) {
        this.str = amount;
        if (!player.world.isRemote && player instanceof ServerPlayerEntity) {
            PacketHandler.sendToClient(new PacketPlayerStats(this), (ServerPlayerEntity) player);
        }
    }

    @Override
    public float getVit() {
        return this.vit;
    }

    @Override
    public void setVit(PlayerEntity player, float amount) {
        this.vit = amount;
        if (!player.world.isRemote && player instanceof ServerPlayerEntity) {
            PacketHandler.sendToClient(new PacketPlayerStats(this), (ServerPlayerEntity) player);
        }
    }

    @Override
    public float getIntel() {
        return this.intel;
    }

    @Override
    public void setIntel(PlayerEntity player, float amount) {
        this.intel = amount;
        if (!player.world.isRemote && player instanceof ServerPlayerEntity) {
            PacketHandler.sendToClient(new PacketPlayerStats(this), (ServerPlayerEntity) player);
        }
    }

    @Override
    public void updateEquipmentStats(PlayerEntity player, EquipmentSlotType slot) {
        switch (slot) {
            case CHEST:
                this.bodyBonus = ItemNBT.statIncrease(player.getItemStackFromSlot(slot));
                break;
            case FEET:
                this.feetBonus = ItemNBT.statIncrease(player.getItemStackFromSlot(slot));
                break;
            case HEAD:
                this.headBonus = ItemNBT.statIncrease(player.getItemStackFromSlot(slot));
                break;
            case LEGS:
                this.legsBonus = ItemNBT.statIncrease(player.getItemStackFromSlot(slot));
                break;
            case MAINHAND:
                this.mainHandBonus = ItemNBT.statIncrease(player.getItemStackFromSlot(slot));
                break;
            case OFFHAND:
                this.offHandBonus = ItemNBT.statIncrease(player.getItemStackFromSlot(slot));
                break;
        }
        if (!player.world.isRemote && player instanceof ServerPlayerEntity) {
            PacketHandler.sendToClient(new PacketUpdateEquipmentStat(slot), (ServerPlayerEntity) player);
        }
    }

    @Override
    public int getAttributeValue(Attribute att) {
        int i = (int) Math.floor(this.headBonus.getOrDefault(att, 0) +
                this.bodyBonus.getOrDefault(att, 0) +
                this.legsBonus.getOrDefault(att, 0) +
                this.feetBonus.getOrDefault(att, 0) +
                this.mainHandBonus.getOrDefault(att, 0) +
                this.offHandBonus.getOrDefault(att, 0));
        if (att == Attributes.GENERIC_ATTACK_DAMAGE)
            i += this.getStr();
        if (att == ModAttributes.RF_MAGIC.get())
            i += this.getIntel();
        if (att == ModAttributes.RF_DEFENCE.get())
            i += this.getVit() * 0.5;
        if (att == ModAttributes.RF_MAGIC_DEFENCE.get())
            i += this.getVit() * 0.5;
        i += this.foodBuffs.getOrDefault(att, 0);
        return i;
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
            PacketHandler.sendToClient(new PacketSkills(this, skill), (ServerPlayerEntity) player);
        }
    }

    @Override
    public void increaseSkill(EnumSkills skill, PlayerEntity player, int xp) {
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
                this.increaseSkill(skill, player, diff);
            } else {
                this.skillMap.get(skill)[1] += xp;
            }
            if (!player.world.isRemote && player instanceof ServerPlayerEntity) {
                PacketHandler.sendToClient(new PacketUpdateClient(this), (ServerPlayerEntity) player);
            }
        }
    }

    private void onSkillLevelUp(EnumSkills skill, PlayerEntity player) {
        this.setMaxHealth(player, this.getHealth(player) + skill.getHealthIncrease());
        this.regenHealth(player, skill.getHealthIncrease());
        this.runePointsMax += skill.getRPIncrease();
        this.runePoints += skill.getRPIncrease();
        this.str += skill.getStrIncrease();
        this.vit += skill.getVitIncrease();
        this.intel += skill.getIntelIncrease();
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

    /*@Override
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
        /*return false;
    }


    @Override
    public void refreshShop(PlayerEntity player) {
        if (!player.world.isRemote) {
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
                Set<ExtendedItemStackWrapper> pre = Sets.newHashSet();
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
        }
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
        int level = player.world.isRemote || !NPCShopItems.leveledItems().contains(new ExtendedItemStackWrapper(item)) ? 1 : ItemNBT.itemLevel(item);
        boolean changed = this.shippedItems.compute(item.getItem().getRegistryName().toString(), (k, v) -> v == null ? level : Math.max(v, level)) != level;
        if (!player.world.isRemote && changed)
            PacketHandler.sendToClient(new PacketUpdateShippingItem(item, level), (ServerPlayerEntity) player);
    }

    @Override
    public void applyFoodEffect(PlayerEntity player, Map<Attribute, Integer> gain, Map<Attribute, Float> gainMulti, int duration) {
        this.removeFoodEffect(player);
        for (Attribute att : gainMulti.keySet()) {
            int i = 0;
            if (att == SharedMonsterAttributes.MAX_HEALTH)
                i += this.getMaxHealth(player) * gainMulti.get(att);
            else if (att == ItemStatAttributes.RPMAX)
                i += this.runePointsMax * gainMulti.get(att);
            else if (att == ItemStatAttributes.RFATTACK)
                i += this.str * gainMulti.get(att);
            else if (att == ItemStatAttributes.RFDEFENCE)
                i += this.vit * 0.5 * gainMulti.get(att);
            else if (att == ItemStatAttributes.RFMAGICATT)
                i += this.intel * gainMulti.get(att);
            else if (att == ItemStatAttributes.RFMAGICDEF)
                i += this.vit * 0.5 * gainMulti.get(att);
            i += gain.getOrDefault(att, 0);
            gain.put(att, i);
        }
        this.foodBuffs = gain;
        this.foodDuration = duration;
        if (!player.world.isRemote && player instanceof ServerPlayerEntity) {
            PacketHandler.sendToClient(new PacketFoodUpdate(this.foodBuffs, this.foodDuration), (ServerPlayerEntity) player);
        }
    }

    @Override
    public void removeFoodEffect(PlayerEntity player) {
        this.foodBuffs.clear();
        this.foodDuration = -1;
        if (!player.world.isRemote && player instanceof ServerPlayerEntity) {
            PacketHandler.sendToClient(new PacketFoodUpdate(this.foodBuffs, this.foodDuration), (ServerPlayerEntity) player);
        }
    }

    @Override
    public Map<Attribute, Integer> foodEffects() {
        return this.foodBuffs;
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

    private void updateGlove(PlayerEntity player) {
        --this.gloveTick;
        Vec3d look = player.getLookVec();
        Vec3d move = new Vec3d(look.x, 0.0, look.z).normalize().scale(0.4);
        player.motionX = move.x;
        player.motionZ = move.z;
        for (EntityLivingBase e : player.world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(1.0))) {
            if (e != player) {
                float damagePhys = this.getAttributeValue(ItemStatAttributes.RFATTACK);
                this.decreaseRunePoints(player, 2);
                this.increaseSkill(EnumSkills.FIST, player, 5);
                if (!(e instanceof IEntityBase)) {
                    damagePhys = LevelCalc.scaleForVanilla(damagePhys);
                }
                RFCalculations.playerDamage(player, e, CustomDamage.attack((EntityLivingBase) player, ItemNBT.getElement(player.getHeldItemMainhand()), CustomDamage.DamageType.NORMAL, CustomDamage.KnockBackType.VANILLA, 1.0f, 20), damagePhys, this, player.getHeldItemMainhand());
            }
        }
        if (this.gloveTick == 0) {
            this.usingGloves = false;
        }
    }

    @Override
    public void update(PlayerEntity player) {
        if (RFCalculations.canUpdateDaily(player.world) || Math.abs(player.world.getWorldTime() / 24000 - this.lastUpdated / 24000) >= 1) {
            this.getShippingInv().shipItems(player);
            this.refreshShop(player);
            this.lastUpdated = player.world.getWorldTime();
        }
        this.ticker = Math.max(--this.ticker, 0);
        this.foodDuration = Math.max(--this.foodDuration, -1);
        if (this.foodDuration == 0) {
            this.removeFoodEffect(player);
        }
        this.timeSinceLastSwing = Math.max(--this.timeSinceLastSwing, 0);
        if (this.timeSinceLastSwing == 0) {
            this.swings = 0;
        }
        if (this.usingGloves) {
            this.updateGlove(player);
        }
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
        NBTTagCompound compound = nbt.getCompoundTag("Skills");
        for (EnumSkills skill : EnumSkills.values()) {
            this.skillMap.put(skill, compound.getIntArray(skill.getIdentifier()));
        }
        if (nbt.hasKey("Inventory")) {
            NBTTagCompound compound2 = (NBTTagCompound) nbt.getTag("Inventory");
            this.spells.readFromNBT(compound2);
        }
        this.shipping.loadInventoryFromNBT(nbt.getTagList("Shipping", Constants.NBT.TAG_COMPOUND));
        nbt.getTagList("ShippedItems", Constants.NBT.TAG_STRING).forEach(s -> {
            String[] sub = ((NBTTagString) s).getString().split(";");
            this.shippedItems.put(sub[0], Integer.parseInt(sub[1]));
        });
        NBTTagCompound shops = nbt.getCompoundTag("ShopItems");
        for (EnumShop shop : EnumShop.values()) {
            NonNullList<ItemStack> items = NonNullList.create();
            shops.getTagList(shop.toString(), Constants.NBT.TAG_COMPOUND).forEach(comp ->
                    items.add(new ItemStack((NBTTagCompound) comp)));
            this.shopItems.put(shop, items);
        }
        this.lastUpdated = nbt.getLong("LastUpdated");
        if (nbt.hasKey("Quest")) {
            this.quest = new QuestMission(nbt.getCompoundTag("Quest"));
        }
        if (nbt.hasKey("FoodBuffs")) {
            NBTTagCompound tag = nbt.getCompoundTag("FoodBuffs");
            for (String s : tag.getKeySet()) {
                this.foodBuffs.put(ItemUtils.getAttFromName(s), tag.getInteger(s));
            }
        }
        this.foodDuration = nbt.getInteger("FoodBuffDuration");
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
            compound.putIntArray(skill.getIdentifier(), this.skillMap.get(skill));
        }
        nbt.setTag("Skills", compound);
        NBTTagCompound compound2 = new NBTTagCompound();
        this.spells.writeToNBT(compound2);
        nbt.setTag("Inventory", compound2);
        nbt.setTag("Shipping", this.shipping.saveInventoryToNBT());
        NBTTagList ship = new NBTTagList();
        this.shippedItems.forEach((key, value) -> ship.appendTag(new NBTTagString(key + ";" + value)));
        nbt.setTag("ShippedItems", ship);
        NBTTagCompound shop = new NBTTagCompound();
        for (Entry<EnumShop, NonNullList<ItemStack>> entry : this.shopItems.entrySet()) {
            NBTTagList l = new NBTTagList();
            for (ItemStack stack : entry.getValue())
                l.appendTag(stack.writeToNBT(new NBTTagCompound()));
            shop.setTag(entry.getKey().toString(), l);
        }
        nbt.setTag("ShopItems", shop);
        nbt.setLong("LastUpdated", this.lastUpdated);
        if (this.quest != null) {
            nbt.setTag("Quest", this.quest.writeToNBT(new NBTTagCompound()));
        }
        NBTTagCompound compound3 = new NBTTagCompound();
        for (Entry<Attribute, Integer> entry : this.foodBuffs.entrySet()) {
            compound3.setInteger(entry.getKey().getName(), entry.getValue());
        }
        nbt.setTag("FoodBuffs", compound3);
        nbt.setInteger("FoodBuffDuration", this.foodDuration);
        return nbt;
    }
}*/