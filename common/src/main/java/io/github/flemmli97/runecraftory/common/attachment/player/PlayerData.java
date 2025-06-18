package io.github.flemmli97.runecraftory.common.attachment.player;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.api.datapack.ShopItemProperties;
import io.github.flemmli97.runecraftory.api.datapack.SkillProperties;
import io.github.flemmli97.runecraftory.api.datapack.npc.NPCData;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.npc.job.NPCJob;
import io.github.flemmli97.runecraftory.common.inventory.InventoryShippingBin;
import io.github.flemmli97.runecraftory.common.inventory.InventoryShop;
import io.github.flemmli97.runecraftory.common.inventory.InventorySpells;
import io.github.flemmli97.runecraftory.common.items.tools.ItemStatIncrease;
import io.github.flemmli97.runecraftory.common.lib.LibConstants;
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
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import io.github.flemmli97.runecraftory.common.utils.DamageSourceUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PlayerData {

    private final Player player;

    public boolean starting, unlockedRecipes;
    //max runepoints possible: 2883
    private int money = GeneralConfig.startingMoney;
    private float runePointsMax = GeneralConfig.startingRp;
    private int runePoints = (int) this.runePointsMax;
    private float str = GeneralConfig.startingStr;
    private float vit = GeneralConfig.startingVit;
    private float intel = GeneralConfig.startingIntel;
    private float strAdd, vitAdd, intAdd;
    /**
     * first number is level, second is the xp a.k.a. percent to next level
     */
    private final LevelExpPair level = new LevelExpPair();
    private final EnumMap<EnumSkills, LevelExpPair> skillLevels = new EnumMap<>(EnumSkills.class);

    private final InventorySpells spells = new InventorySpells();

    private final DailyPlayerUpdater updater = new DailyPlayerUpdater(this);
    private int rpStillRegen;

    private final RecipeKeeper keeper = new RecipeKeeper();

    private final Map<Item, ShippedItemData> shippedItems = new HashMap<>();
    private final Map<NPCJob, NonNullList<ItemStack>> shopItems = new HashMap<>();
    private final InventoryShippingBin shipping = new InventoryShippingBin();
    //Food buff
    private Holder<Item> lastFoodBuff;
    private Map<Holder<Attribute>, Double> foodBuffs = new HashMap<>();
    private int foodDuration;

    private final PlayerWeaponHandler weaponHandler;

    private final WalkingTracker walkingTracker = new WalkingTracker();

    public final EntitySelector entitySelector = new EntitySelector();

    public final EntityStatsTracker entityStatsTracker = new EntityStatsTracker();

    public final Party party = new Party();

    private boolean touchedWater;

    private int craftingSeed;

    private int boughtBarns;

    private BlockPos blockBreakPosForMsg;
    private int breakTick;

    private int mobLevelIncrease;

    private NPCData.Gender gender;

    public PlayerData(Player player) {
        this.player = player;
        for (EnumSkills skill : EnumSkills.values()) {
            this.skillLevels.put(skill, new LevelExpPair());
        }
        this.weaponHandler = new PlayerWeaponHandler(player);
    }

    public Player player() {
        return this.player;
    }

    public void setMaxHealth(float amount, boolean asBaseHealth) {
        AttributeInstance health = this.player.getAttribute(Attributes.MAX_HEALTH);
        AttributeModifier modifier = health.getModifier(LibConstants.MAX_HEALTH_MODIFIER);
        double val = amount - (asBaseHealth ? health.getBaseValue() : health.getValue());
        if (modifier != null && !asBaseHealth)
            val += modifier.amount();
        health.removeModifier(LibConstants.MAX_HEALTH_MODIFIER);
        health.addPermanentModifier(new AttributeModifier(LibConstants.MAX_HEALTH_MODIFIER, val, AttributeModifier.Operation.ADD_VALUE));
    }

    private void setFoodHealthBonus(double amount) {
        AttributeInstance health = this.player.getAttribute(Attributes.MAX_HEALTH);
        health.removeModifier(LibConstants.FOOD_MODIFIER);
        health.addPermanentModifier(new AttributeModifier(LibConstants.FOOD_MODIFIER, amount, AttributeModifier.Operation.ADD_VALUE));
    }

    public int getRunePoints() {
        return this.runePoints;
    }

    public int getMaxRunePoints() {
        return (int) (this.runePointsMax + this.foodBuffs.getOrDefault(ModAttributes.MAX_RUNEPOINTS.get(), 0d));
    }

    public float getMaxRunePointsRaw() {
        return this.runePointsMax;
    }

    public boolean decreaseRunePoints(int amount, boolean damage) {
        if (!GeneralConfig.useRp && !this.player.level().isClientSide)
            return true;
        if (!this.player.isCreative()) {
            if (EntityUtils.isExhaust(this.player)) {
                amount *= 2;
            }
            if (this.runePoints >= amount)
                this.runePoints -= amount;
            else if (damage) {
                int diff = amount - this.runePoints;
                this.runePoints = 0;
                if (!this.player.level().isClientSide) {
                    int invul = this.player.invulnerableTime;
                    this.player.invulnerableTime = 10;
                    boolean res = this.player.hurt(DamageSourceUtils.exhaust(this.player.level()), Math.min(this.player.getMaxHealth() * 0.25f, (float) (diff * 2)));
                    if (res)
                        this.player.invulnerableTime = 10;
                    else
                        this.player.invulnerableTime = invul;
                }
            } else
                return false;
            this.rpStillRegen = 200;
            if (this.player instanceof ServerPlayer serverPlayer)
                LoaderNetwork.INSTANCE.sendToPlayer(new S2CRunePoints(this), serverPlayer);
            return true;
        }
        return true;
    }

    public void refreshRunePoints(int amount) {
        this.runePoints = Mth.clamp(this.runePoints + amount, 0, this.getMaxRunePoints());
        if (this.player instanceof ServerPlayer serverPlayer)
            LoaderNetwork.INSTANCE.sendToPlayer(new S2CRunePoints(this), serverPlayer);
    }

    public void setRunePoints(int amount) {
        this.runePoints = amount;
        if (this.player instanceof ServerPlayer serverPlayer)
            LoaderNetwork.INSTANCE.sendToPlayer(new S2CRunePoints(this), serverPlayer);
    }

    public void setMaxRunePoints(float amount) {
        this.runePointsMax = amount;
        if (this.player instanceof ServerPlayer serverPlayer)
            LoaderNetwork.INSTANCE.sendToPlayer(new S2CMaxRunePoints(this), serverPlayer);
    }

    public int getMoney() {
        return this.money;
    }

    public boolean useMoney(int amount) {
        if (this.money >= amount) {
            this.money -= amount;
            if (this.player instanceof ServerPlayer serverPlayer) {
                LoaderNetwork.INSTANCE.sendToPlayer(new S2CMoney(this), serverPlayer);
            }
            return true;
        }
        return false;
    }

    public void setMoney(int amount) {
        this.money = amount;
        if (this.player instanceof ServerPlayer serverPlayer) {
            ModCriteria.MONEY_TRIGGER.get().trigger(serverPlayer);
            LoaderNetwork.INSTANCE.sendToPlayer(new S2CMoney(this), serverPlayer);
        }
    }

    public float getStr() {
        return this.str;
    }

    public void setStr(float amount) {
        this.str = amount;
        if (this.player instanceof ServerPlayer serverPlayer)
            LoaderNetwork.INSTANCE.sendToPlayer(new S2CPlayerStats(this), serverPlayer);
    }

    public float getVit() {
        return this.vit;
    }

    public void setVit(float amount) {
        this.vit = amount;
        if (this.player instanceof ServerPlayer serverPlayer)
            LoaderNetwork.INSTANCE.sendToPlayer(new S2CPlayerStats(this), serverPlayer);
    }

    public float getIntel() {
        return this.intel;
    }

    public void setIntel(float amount) {
        this.intel = amount;
        if (this.player instanceof ServerPlayer serverPlayer)
            LoaderNetwork.INSTANCE.sendToPlayer(new S2CPlayerStats(this), serverPlayer);
    }

    public LevelExpPair getPlayerLevel() {
        return this.level;
    }

    public void setPlayerLevel(int level, float xpAmount, boolean recalc) {
        this.level.setLevel(Mth.clamp(level, 1, GeneralConfig.maxLevel), LevelCalc::xpAmountForLevelUp);
        this.level.setXp(Mth.clamp(xpAmount, 0, LevelCalc.xpAmountForLevelUp(level)));
        if (this.player instanceof ServerPlayer serverPlayer) {
            if (recalc) {
                this.recalculateStats(true);
            } else
                LoaderNetwork.INSTANCE.sendToPlayer(new S2CLevelPkt(this), serverPlayer);
        }
    }

    public void addXp(float amount) {
        if (this.level.getLevel() >= GeneralConfig.maxLevel)
            return;
        boolean levelUp = this.level.addXP(amount, GeneralConfig.maxLevel, LevelCalc::xpAmountForLevelUp, () -> this.onLevelUp(this.player));
        if (levelUp) {
            this.player.level().playSound(null, this.player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1, 0.5f);
        }
        if (this.player instanceof ServerPlayer serverPlayer) {
            if (levelUp)
                ModCriteria.LEVEL_TRIGGER.get().trigger(serverPlayer);
            LoaderNetwork.INSTANCE.sendToPlayer(new S2CLevelPkt(this), serverPlayer);
        }
    }

    private void onLevelUp(Player player) {
        float health = player.getMaxHealth();
        this.updateHealth();
        player.heal(player.getMaxHealth() - health);
        this.runePointsMax += GeneralConfig.rpPerLevel;
        this.runePoints = Math.min(this.runePoints + (int) GeneralConfig.rpPerLevel, this.runePoints);
        this.str += GeneralConfig.strPerLevel;
        this.vit += GeneralConfig.vitPerLevel;
        this.intel += GeneralConfig.intPerLevel;
    }

    public void recalculateStats(boolean regen) {
        if (!(this.player instanceof ServerPlayer serverPlayer))
            return;
        int lvl = this.level.getLevel() - 1;
        this.updateHealth();
        this.runePointsMax = GeneralConfig.rpPerLevel * lvl + GeneralConfig.startingRp + (int) this.skillValLevelFunc((skillLvl, prop) -> Math.min(100, skillLvl) * prop.rpIncrease());
        if (regen) {
            this.player.setHealth(this.player.getMaxHealth());
            this.runePoints = (int) this.runePointsMax;
        }
        this.str = GeneralConfig.strPerLevel * lvl + GeneralConfig.startingStr + (float) this.skillVal(SkillProperties::strIncrease);
        this.intel = GeneralConfig.intPerLevel * lvl + GeneralConfig.startingIntel + (float) this.skillVal(SkillProperties::intelIncrease);
        this.vit = GeneralConfig.vitPerLevel * lvl + GeneralConfig.startingVit + (float) this.skillVal(SkillProperties::vitIncrease);
        LoaderNetwork.INSTANCE.sendToPlayer(new S2CLevelPkt(this), serverPlayer);
    }

    private double skillVal(Function<SkillProperties, Number> func) {
        return this.skillLevels.entrySet().stream().mapToDouble(e -> (e.getValue().getLevel() - 1) * func.apply(DataPackHandler.INSTANCE.skillPropertiesManager().getPropertiesFor(e.getKey())).doubleValue()).sum();
    }

    private double skillValLevelFunc(BiFunction<Integer, SkillProperties, Number> func) {
        return this.skillLevels.entrySet().stream().mapToDouble(e -> func.apply(e.getValue().getLevel() - 1, DataPackHandler.INSTANCE.skillPropertiesManager().getPropertiesFor(e.getKey())).doubleValue()).sum();
    }

    public LevelExpPair getSkillLevel(EnumSkills skill) {
        return this.skillLevels.get(skill);
    }

    public void setSkillLevel(EnumSkills skill, int level, float xpAmount, boolean recalc) {
        this.skillLevels.get(skill).setLevel(this.player.level().isClientSide ? level : Mth.clamp(level, 1, DataPackHandler.INSTANCE.skillPropertiesManager().getPropertiesFor(skill).maxLevel()), l -> LevelCalc.xpAmountForSkillLevelUp(skill, l));
        this.skillLevels.get(skill).setXp(this.player.level().isClientSide ? xpAmount : Mth.clamp(xpAmount, 0, LevelCalc.xpAmountForSkillLevelUp(skill, level)));
        if (this.player instanceof ServerPlayer serverPlayer) {
            if (recalc) {
                this.recalculateStats(true);
                this.player.setHealth(this.player.getMaxHealth());
            }
            LoaderNetwork.INSTANCE.sendToPlayer(new S2CSkillLevelPkt(this, skill), serverPlayer);
        }
    }

    public void increaseSkill(EnumSkills skill, float amount) {
        if (this.skillLevels.get(skill).getLevel() >= DataPackHandler.INSTANCE.skillPropertiesManager().getPropertiesFor(skill).maxLevel())
            return;
        boolean levelUp = this.skillLevels.get(skill).addXP(amount, DataPackHandler.INSTANCE.skillPropertiesManager().getPropertiesFor(skill).maxLevel(), lvl -> LevelCalc.xpAmountForSkillLevelUp(skill, lvl), () -> this.onSkillLevelUp(skill, this.player));
        if (levelUp) {
            this.player.level().playSound(null, this.player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1, 0.5f);
        }
        if (this.player instanceof ServerPlayer serverPlayer) {
            if (levelUp)
                ModCriteria.SKILL_LEVEL_TRIGGER.get().trigger(serverPlayer, skill);
            LoaderNetwork.INSTANCE.sendToPlayer(new S2CSkillLevelPkt(this, skill), serverPlayer);
        }
    }

    private void onSkillLevelUp(EnumSkills skill, Player player) {
        SkillProperties prop = DataPackHandler.INSTANCE.skillPropertiesManager().getPropertiesFor(skill);
        int level = this.skillLevels.get(skill).getLevel();
        float health = player.getMaxHealth();
        this.updateHealth();
        player.heal(player.getMaxHealth() - health);
        if (level <= 100) {
            this.runePointsMax += prop.rpIncrease();
            this.runePoints += prop.rpIncrease();
        }
        this.str += prop.strIncrease();
        this.vit += prop.vitIncrease();
        this.intel += prop.intelIncrease();
    }

    private void updateHealth() {
        this.setMaxHealth(GeneralConfig.startingHealth
                + GeneralConfig.hpPerLevel * LevelCalc.getMultiplierInterval(this.level.getLevel(), 30, 30, 0.5f)
                + (float) this.skillValLevelFunc((skillLvl, prop) -> {
            int skillHealthMultiplier = 1 + (skillLvl / 25);
            return skillHealthMultiplier * prop.healthIncrease() * skillLvl;
        }), true);
    }

    public void increaseStatBonus(ItemStatIncrease.Stat type) {
        switch (type) {
            case STR -> {
                this.strAdd += 1;
                if (this.player instanceof ServerPlayer serverPlayer)
                    LoaderNetwork.INSTANCE.sendToPlayer(new S2CItemStatBoost(type, false), serverPlayer);
            }
            case INT -> {
                this.intAdd += 1;
                if (this.player instanceof ServerPlayer serverPlayer)
                    LoaderNetwork.INSTANCE.sendToPlayer(new S2CItemStatBoost(type, false), serverPlayer);
            }
            case VIT -> {
                this.vitAdd += 1;
                if (this.player instanceof ServerPlayer serverPlayer)
                    LoaderNetwork.INSTANCE.sendToPlayer(new S2CItemStatBoost(type, false), serverPlayer);
            }
            case HP -> {
                AttributeInstance health = this.player.getAttribute(Attributes.MAX_HEALTH);
                AttributeModifier modifier = health.getModifier(LibConstants.MAX_HEALTH_ITEM_INCREASE);
                double val = modifier == null ? 0 : modifier.amount();
                health.removeModifier(LibConstants.MAX_HEALTH_ITEM_INCREASE);
                health.addPermanentModifier(new AttributeModifier(LibConstants.MAX_HEALTH_ITEM_INCREASE, val + 10, AttributeModifier.Operation.ADD_VALUE));
            }
        }
    }

    public void resetAllStatBoost(ItemStatIncrease.Stat type) {
        switch (type) {
            case STR -> {
                this.strAdd = 0;
                if (this.player instanceof ServerPlayer serverPlayer)
                    LoaderNetwork.INSTANCE.sendToPlayer(new S2CItemStatBoost(type, true), serverPlayer);
            }
            case INT -> {
                this.intAdd = 0;
                if (this.player instanceof ServerPlayer serverPlayer)
                    LoaderNetwork.INSTANCE.sendToPlayer(new S2CItemStatBoost(type, true), serverPlayer);
            }
            case VIT -> {
                this.vitAdd = 0;
                if (this.player instanceof ServerPlayer serverPlayer)
                    LoaderNetwork.INSTANCE.sendToPlayer(new S2CItemStatBoost(type, true), serverPlayer);
            }
            case HP -> {
                AttributeInstance health = this.player.getAttribute(Attributes.MAX_HEALTH);
                health.removeModifier(LibConstants.MAX_HEALTH_ITEM_INCREASE);
            }
        }
    }

    public double getAttributeValue(Holder<Attribute> att) {
        double val = 0;
        float vit = this.getVit() + this.vitAdd;
        if (att == Attributes.ATTACK_DAMAGE) {
            val += this.getStr() + this.strAdd;
        }
        if (att == ModAttributes.MAGIC_ATTACK.get())
            val += this.getIntel() + this.intAdd;
        if (att == ModAttributes.DEFENCE.get())
            val += vit * 0.5;
        if (att == ModAttributes.MAGIC_DEFENCE.get())
            val += vit * 0.5;
        val += this.foodBuffs.getOrDefault(att, 0d);

        AttributeMap atts = this.player.getAttributes();
        val += atts.hasAttribute(att) ? atts.getValue(att) : 0;
        return val;
    }

    public InventorySpells getInv() {
        return this.spells;
    }

    public InventoryShippingBin getShippingInv() {
        return this.shipping;
    }

    public void refreshShop() {
        if (this.player instanceof ServerPlayer serverPlayer) {
            for (NPCJob profession : ModNPCJobs.JOBS.registry()) {
                Collection<ShopItemProperties> datapack = DataPackHandler.INSTANCE.shopItemsManager().get(profession);
                List<ItemStack> shopItems = new ArrayList<>();
                datapack.forEach(shopProps -> {
                    boolean canAdd = (shopProps.predicate().isEmpty() || shopProps.predicate().get().matches(serverPlayer, serverPlayer)) && switch (shopProps.unlockType()) {
                        case DEFAULT -> false;
                        case ALWAYS -> true;
                        case NEEDS_SHIPPING -> this.shippedItems.containsKey(shopProps.stack().getItem());
                    };
                    if (canAdd)
                        shopItems.add(shopProps.stack().copy());
                });
                NonNullList<ItemStack> shop = NonNullList.create();
                if (!shopItems.isEmpty()) {
                    for (float chance = 1.5f + shopItems.size() * 0.002f; this.player.level().random.nextFloat() < chance; chance -= 0.1f) {
                        ItemStack stack = shopItems.remove(this.player.level().random.nextInt(shopItems.size()));
                        shop.add(stack);
                        if (shopItems.isEmpty() || (profession == ModNPCJobs.RANDOM.get() && shop.size() >= InventoryShop.SHOP_SIZE))
                            break;
                    }
                }
                DataPackHandler.INSTANCE.shopItemsManager().getDefaultItems(profession).forEach(props -> shop.add(props.stack()));
                this.shopItems.put(profession, shop);
            }
        }
    }

    public NonNullList<ItemStack> getShop(NPCJob shop) {
        NonNullList<ItemStack> list = NonNullList.create();
        list.addAll(this.shopItems.getOrDefault(shop, NonNullList.withSize(0, ItemStack.EMPTY)));
        return list;
    }

    public void addShippingItem(ItemStack stack) {
        int level = ItemNBT.itemLevel(stack);
        this.shippedItems.compute(stack.getItem(), (k, v) -> v == null ?
                new ShippedItemData(stack.getCount(), level) : new ShippedItemData(v.amount + stack.getCount(), Math.max(v.maxLevel, level)));
    }

    public ShippedItemData shippedItemData(ItemStack stack) {
        return this.shippedItems.get(stack.getItem());
    }

    public int getShippedTypesAmount() {
        return this.shippedItems.size();
    }

    public RecipeKeeper getRecipeKeeper() {
        return this.keeper;
    }

    public Holder<Item> lastEatenFood() {
        return this.lastFoodBuff;
    }

    public void applyFoodEffect(ItemStack stack) {
        FoodProperties food = DataPackHandler.INSTANCE.foodManager().get(stack.getItem());
        if (food == null)
            return;
        Pair<Map<Holder<Attribute>, Double>, Map<Holder<Attribute>, Double>> foodStats = ItemNBT.foodStats(stack);
        Map<Holder<Attribute>, Double> gain = foodStats.getFirst();
        foodStats.getSecond().forEach((att, d) -> {
            float percent = (float) (d * 0.01f);
            double mult = 0;
            if (att == Attributes.MAX_HEALTH)
                mult += this.player.getMaxHealth() * percent;
            else if (att == ModAttributes.MAX_RUNEPOINTS.get())
                mult += this.runePointsMax * percent;
            else if (att == Attributes.ATTACK_DAMAGE)
                mult += this.str * percent;
            else if (att == ModAttributes.DEFENCE.get())
                mult += this.vit * 0.5 * percent;
            else if (att == ModAttributes.MAGIC_ATTACK.get())
                mult += this.intel * percent;
            else if (att == ModAttributes.DEFENCE.get())
                mult += this.vit * 0.5 * percent;
            mult += gain.getOrDefault(att, 0d);
            gain.put(att, mult);
        });
        if (!gain.isEmpty()) {
            this.removeFoodEffect(this.player);
            this.foodBuffs = gain;
            if (this.foodBuffs.containsKey(Attributes.MAX_HEALTH))
                this.setFoodHealthBonus(this.foodBuffs.get(Attributes.MAX_HEALTH));
            this.foodDuration = food.duration();
            this.lastFoodBuff = stack.getItemHolder();
        }
        if (this.player instanceof ServerPlayer serverPlayer) {
            LoaderNetwork.INSTANCE.sendToPlayer(new S2CFoodPkt(stack), serverPlayer);
        }
    }

    public void removeFoodEffect(Player player) {
        this.foodBuffs = Collections.emptyMap();
        this.foodDuration = -1;
        this.lastFoodBuff = null;
        this.setFoodHealthBonus(0);
        if (player instanceof ServerPlayer serverPlayer) {
            LoaderNetwork.INSTANCE.sendToPlayer(new S2CFoodPkt(null), serverPlayer);
        }
    }

    public Map<Holder<Attribute>, Double> foodEffects() {
        return this.foodBuffs;
    }

    public int foodBuffDuration() {
        return this.foodDuration;
    }

    public CompoundTag foodBuffNBT() {
        CompoundTag nbt = new CompoundTag();
        if (this.lastFoodBuff != null)
            nbt.put("LastFood", BuiltInRegistries.ITEM.holderByNameCodec().encodeStart(NbtOps.INSTANCE, this.lastFoodBuff).getOrThrow());

        CompoundTag compound3 = new CompoundTag();
        for (Map.Entry<Holder<Attribute>, Double> entry : this.foodBuffs.entrySet()) {
            compound3.putDouble(entry.getKey().unwrapKey().get().location().toString(), entry.getValue());
        }
        nbt.put("FoodBuffs", compound3);
        nbt.putInt("FoodBuffDuration", this.foodDuration);
        return nbt;
    }

    public void readFoodBuffFromNBT(CompoundTag nbt) {
        this.lastFoodBuff = nbt.contains("LastFood") ? BuiltInRegistries.ITEM.holderByNameCodec().parse(NbtOps.INSTANCE, nbt.get("LastFood")).getOrThrow() : null;
        this.foodBuffs.clear();
        CompoundTag tag = nbt.getCompound("FoodBuffs");
        for (String s : tag.getAllKeys()) {
            this.foodBuffs.put(BuiltInRegistries.ATTRIBUTE.getHolder(ResourceLocation.parse(s)).get(), tag.getDouble(s));
        }
        this.foodDuration = nbt.getInt("FoodBuffDuration");
    }

    public PlayerWeaponHandler getWeaponHandler() {
        return this.weaponHandler;
    }

    public void tick() {
        this.weaponHandler.tick();
        if (this.player instanceof ServerPlayer serverPlayer) {
            this.updater.tick(serverPlayer);
            if (serverPlayer.tickCount % 10 == 0) {
                if (this.walkingTracker.tickWalkingTracker(serverPlayer))
                    this.rpStillRegen = 200;
            }
            if (--this.rpStillRegen < 0 && serverPlayer.tickCount % 20 == 0) {
                this.refreshRunePoints(1);
            }
            if (--this.breakTick <= 0)
                this.blockBreakPosForMsg = null;
            ItemStack main = this.player.getMainHandItem();
            ItemStack off = this.player.getOffhandItem();
            if (main.is(ModItems.MOB_STAFF.get()) || off.is(ModItems.MOB_STAFF.get())) {
                if (this.entitySelector.poi != null) {
                    serverPlayer.serverLevel().sendParticles(serverPlayer, ParticleTypes.FLAME, true,
                            this.entitySelector.poi.getX() + 0.5, this.entitySelector.poi.getY() + 1.5, this.entitySelector.poi.getZ() + 0.5,
                            1, 0, 0, 0, 0);
                    serverPlayer.serverLevel().sendParticles(serverPlayer, ParticleTypes.FLAME, true,
                            this.entitySelector.poi.getX() + 0.5, this.entitySelector.poi.getY() + 1.5, this.entitySelector.poi.getZ() + 0.5,
                            3, 0, 0, 0, 0.01);
                }
            } else
                this.entitySelector.reset();

            if (serverPlayer.hasEffect(ModEffects.BATH.asHolder())) {
                if (!this.touchedWater && serverPlayer.isInWater())
                    this.touchedWater = true;
                else if (this.touchedWater && !serverPlayer.isInWater()) {
                    serverPlayer.removeEffect(ModEffects.BATH.asHolder());
                    this.touchedWater = false;
                }
            }
        }
        this.getInv().update(this.player);
        this.foodDuration = Math.max(--this.foodDuration, -1);
        if (this.foodDuration == 0) {
            this.removeFoodEffect(this.player);
        }
    }

    public DailyPlayerUpdater getDailyUpdater() {
        return this.updater;
    }

    public int getCraftingSeed(Player player) {
        if (this.craftingSeed == 0)
            this.craftingSeed = player.getRandom().nextInt();
        return this.craftingSeed;
    }

    public void onCrafted(Player player) {
        this.craftingSeed = player.getRandom().nextInt();
    }

    public int getBoughtBarns() {
        return this.boughtBarns;
    }

    public void onBuyBarn() {
        this.boughtBarns++;
    }

    public boolean onBarnFailMine(BlockPos pos) {
        boolean start = !pos.equals(this.blockBreakPosForMsg);
        this.blockBreakPosForMsg = pos;
        this.breakTick = 20;
        return start;
    }

    public void increaseMobFrom(BaseMonster monster) {
        if (!(this.player instanceof ServerPlayer serverPlayer))
            return;
        this.entityStatsTracker.killEntity(monster);
        this.increaseMobLevel(monster.getProp().levelIncreaseFromKill(this.entityStatsTracker.getKillCount(monster.getType()), serverPlayer));
    }

    public void increaseMobLevel(int increase) {
        this.mobLevelIncrease += increase;
    }

    public int getMobLevelIncrease() {
        return this.mobLevelIncrease;
    }

    public NPCData.Gender getGender() {
        return this.gender;
    }

    public void readFromNBT(CompoundTag nbt) {
//        this.starting = nbt.getBoolean("Starting");
//        this.runePointsMax = nbt.getFloat("MaxRunePoints");
//        this.runePoints = nbt.getInt("RunePoints");
//        this.money = nbt.getInt("Money");
//        this.str = nbt.getFloat("Strength");
//        this.vit = nbt.getFloat("Vitality");
//        this.intel = nbt.getFloat("Intelligence");
//        this.level.read(nbt.get("Level"));
//        this.strAdd = nbt.getFloat("StrengthBonus");
//        this.vitAdd = nbt.getFloat("VitalityBonus");
//        this.intAdd = nbt.getFloat("IntelligenceBonus");
//        CompoundTag skillCompound = nbt.getCompound("Skills");
//        for (EnumSkills skill : EnumSkills.values()) {
//            this.skillLevels.get(skill).read(skillCompound.get(skill.toString()));
//        }
//        this.spells.load(nbt.getCompound("Inventory"));
//        this.shipping.load(nbt.getCompound("Shipping"));
//        CompoundTag shipped = nbt.getCompound("ShippedItems");
//        shipped.getAllKeys().forEach(key -> {
//            CompoundTag d = shipped.getCompound(key);
//            this.shippedItems.put(Registry.ITEM.get(new ResourceLocation(key)), new ShippedItemData(d.getInt("Amount"), d.getInt("Level")));
//        });
//        CompoundTag shops = nbt.getCompound("ShopItems");
//        shops.getAllKeys().forEach(key -> {
//            NonNullList<ItemStack> items = NonNullList.create();
//            shops.getList(key, Tag.TAG_COMPOUND).forEach(comp ->
//                    items.add(ItemStack.of((CompoundTag) comp)));
//            this.shopItems.put(ModNPCJobs.getFromID(new ResourceLocation(key)), items);
//        });
//        this.keeper.read(nbt.getCompound("Recipes"));
//        this.updater.read(nbt.getCompound("DailyUpdater"));
//        /*if (nbt.contains("Quest")) {
//            this.quest = new QuestMission(nbt.getCompoundTag("Quest"));
//        }*/
//        this.readFoodBuffFromNBT(nbt.getCompound("FoodData"));
//        if (player instanceof ServerPlayer serverPlayer && serverPlayer.connection != null) {
//            this.recalculateStats(serverPlayer, false);
//        }
//        if (nbt.contains("RestoreHP") && player instanceof ServerPlayer serverPlayer) {
//            float f = nbt.getFloat("RestoreHP");
//            //Sheduling the health update in case other mods modify max health
//            serverPlayer.getServer().tell(new TickTask(1, () -> player.setHealth(f)));
//        }
//        this.walkingTracker.read(nbt.getCompound("WalkingTracker"));
//        this.party.load(nbt.getCompound("PartyTag"));
//        this.craftingSeed = nbt.getInt("CraftingSeed");
//        this.boughtBarns = nbt.getInt("BoughtBarns");
//        this.mobLevelIncrease = nbt.getInt("MobLevelIncrease");
    }

    public CompoundTag writeToNBTPlain(CompoundTag nbt) {
        return this.writeToNBT(nbt, false);
    }

    public CompoundTag writeToNBT(CompoundTag nbt, boolean wasDead) {
//        nbt.putBoolean("Starting", this.starting);
//        nbt.putFloat("MaxRunePoints", this.runePointsMax);
//        if (this.player == null) {
//            nbt.putInt("RunePoints", this.runePoints);
//        } else {
//            if (wasDead) {
//                nbt.putFloat("RestoreHP", this.player.getMaxHealth() * GeneralConfig.deathHpPercent);
//                nbt.putInt("RunePoints", (int) (this.runePointsMax * GeneralConfig.deathRpPercent));
//            } else {
//                nbt.putFloat("RestoreHP", this.player.getHealth());
//                nbt.putInt("RunePoints", this.runePoints);
//            }
//        }
//        nbt.putInt("Money", this.money);
//        nbt.putFloat("Strength", this.str);
//        nbt.putFloat("Vitality", this.vit);
//        nbt.putFloat("Intelligence", this.intel);
//        nbt.put("Level", this.level.save());
//        nbt.putFloat("StrengthBonus", this.strAdd);
//        nbt.putFloat("VitalityBonus", this.vitAdd);
//        nbt.putFloat("IntelligenceBonus", this.intAdd);
//        CompoundTag skillCompound = new CompoundTag();
//        for (EnumSkills skill : EnumSkills.values()) {
//            skillCompound.put(skill.toString(), this.skillLevels.get(skill).save());
//        }
//        nbt.put("Skills", skillCompound);
//        nbt.put("Inventory", this.spells.save());
//        nbt.put("Shipping", this.shipping.save());
//        CompoundTag ship = new CompoundTag();
//        this.shippedItems.forEach((key, value) -> {
//            CompoundTag d = new CompoundTag();
//            d.putInt("Amount", value.amount);
//            d.putInt("Level", value.maxLevel);
//            ship.put(key.toString(), d);
//        });
//        nbt.put("ShippedItems", ship);
//        CompoundTag shop = new CompoundTag();
//        for (Map.Entry<NPCJob, NonNullList<ItemStack>> entry : this.shopItems.entrySet()) {
//            ListTag l = new ListTag();
//            for (ItemStack stack : entry.getValue())
//                l.add(stack.save(new CompoundTag()));
//            shop.put(ModNPCJobs.getIDFrom(entry.getKey()).toString(), l);
//        }
//        nbt.put("ShopItems", shop);
//        nbt.put("Recipes", this.keeper.save());
//        nbt.put("DailyUpdater", this.updater.save());
//        /*if (this.quest != null) {
//            nbt.setTag("Quest", this.quest.writeToNBT(new NBTTagCompound()));
//        }*/
//        nbt.put("FoodData", this.foodBuffNBT());
//        nbt.put("WalkingTracker", this.walkingTracker.save());
//        nbt.put("TamedEntityTracker", this.entityStatsTracker.save());
//        nbt.put("PartyTag", this.party.save());
//        nbt.putInt("CraftingSeed", this.craftingSeed);
//        nbt.putInt("BoughtBarns", this.boughtBarns);
//        nbt.putInt("MobLevelIncrease", this.mobLevelIncrease);
        return nbt;
    }

    public void resetAll() {
        PlayerData newData = new PlayerData(this.player);
        newData.spells.load(this.spells.save(this.player.registryAccess()), this.player.registryAccess());
        newData.shipping.load(this.shipping.save(this.player.registryAccess()), this.player.registryAccess());
        this.readFromNBT(newData.writeToNBTPlain(new CompoundTag()));
        this.recalculateStats(false);
        this.refreshShop();
        this.starting = false;
        this.entityStatsTracker.reset();
        this.mobLevelIncrease = 0;
    }

    public record ShippedItemData(int amount, int maxLevel) {
    }
}