package io.github.flemmli97.runecraftory.common.attachment.player;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DynamicOps;
import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.api.datapack.ShopItemProperties;
import io.github.flemmli97.runecraftory.api.datapack.SkillProperties;
import io.github.flemmli97.runecraftory.api.registry.NPCProfession;
import io.github.flemmli97.runecraftory.common.attachment.WeaponHandler;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.inventory.InventoryShippingBin;
import io.github.flemmli97.runecraftory.common.inventory.InventoryShop;
import io.github.flemmli97.runecraftory.common.inventory.InventorySpells;
import io.github.flemmli97.runecraftory.common.items.tools.ItemStatIncrease;
import io.github.flemmli97.runecraftory.common.lib.LibConstants;
import io.github.flemmli97.runecraftory.common.network.S2CFoodPkt;
import io.github.flemmli97.runecraftory.common.network.S2CLevelPkt;
import io.github.flemmli97.runecraftory.common.network.S2CMoney;
import io.github.flemmli97.runecraftory.common.network.S2CRunePoints;
import io.github.flemmli97.runecraftory.common.network.S2CSkillLevelPkt;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryCriteria;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEffects;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCProfessions;
import io.github.flemmli97.runecraftory.common.utils.DamageSourceUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemComponentUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.mixin.AttributeMapAccessor;
import io.github.flemmli97.tenshilib.common.attachment.SerializableAttachment;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class PlayerData implements SerializableAttachment<CompoundTag, PlayerData> {

    private final Player player;

    private boolean starting;

    private double runePoints;

    private final XpLevelHolder level = new XpLevelHolder();
    private final EnumMap<Skills, XpLevelHolder> skillLevels = new EnumMap<>(Skills.class);

    private int money = GeneralConfig.startingMoney;

    private final RecipeKeeper keeper = new RecipeKeeper();

    private final InventoryShippingBin shippingBin = new InventoryShippingBin();
    private final Map<Item, ShippedItemData> shippedItems = new HashMap<>();
    private final Map<NPCProfession, NonNullList<ItemStack>> shopItems = new HashMap<>();

    private final InventorySpells spells = new InventorySpells();

    private final DailyPlayerUpdater updater = new DailyPlayerUpdater(this);

    private Holder<Item> lastEaten;
    private int foodDuration;

    private final WalkingTracker walkingTracker = new WalkingTracker();
    public final EntityStatsTracker entityStatsTracker = new EntityStatsTracker();
    public final Party party = new Party();

    private int craftingSeed;
    private int boughtBarns;
    private int mobLevelIncrease;

    private final AnimationHandler<Player> animationHandler;
    private final WeaponHandler<Player> weaponHandler;
    public final EntitySelector entitySelector = new EntitySelector();
    private BlockPos blockBreakPosForMsg;
    private int breakTick;
    private int rpStillRegen;

    public PlayerData(Player player) {
        this.player = player;
        for (Skills skill : Skills.values()) {
            this.skillLevels.put(skill, new XpLevelHolder());
        }
        this.animationHandler = new PlayerAnimationHandler(player);
        this.weaponHandler = new WeaponHandler<>(player, () -> this.animationHandler);
    }

    public PlayerData(Player player, PlayerData other, boolean death) {
        this(player);
        CompoundTag tag = other.write(player.registryAccess());
        if (death) {
            tag.putInt("Money", (int) (other.getMoney() * 0.2));
            tag.putFloat("RestoreHP", this.player.getMaxHealth() * GeneralConfig.deathHpPercent);
            tag.putDouble("RunePoints", this.runePoints * GeneralConfig.deathRpPercent);
        }
        this.read(tag, player.registryAccess());
    }

    public Player player() {
        return this.player;
    }

    public void onJoin() {
        this.recalculateStats(false);
        // Update from the config values
        this.updateConfigAttributes();
        if (!this.starting) {
            this.starting = true;
            this.player.setHealth(this.player.getMaxHealth());
            this.setRunePoints((int) this.player.getAttributeValue(RuneCraftoryAttributes.MAX_RUNEPOINTS.asHolder()));
        }
    }

    private void setForVitality(ResourceLocation modifier, double value, AttributeUpdate update) {
        this.setAttributeValue(RuneCraftoryAttributes.DEFENCE.asHolder(), modifier, value * 0.5, update);
        this.setAttributeValue(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), modifier, value * 0.5, update);
    }

    private void setFoodBonus(Pair<Map<Holder<Attribute>, Double>, Map<Holder<Attribute>, Double>> bonus) {
        bonus.getFirst().forEach((att, val) ->
                this.setAttributeValue(att, LibConstants.FOOD_MODIFIER, val, AttributeUpdate.REPLACE));
        bonus.getSecond().forEach((att, val) -> {
            AttributeInstance instance = this.player.getAttribute(att);
            AttributeUpdate.REPLACE.modify(instance, LibConstants.FOOD_MODIFIER_MULTI, val, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        });
    }

    private void setAttributeValue(Holder<Attribute> attribute, ResourceLocation modifier, double value, AttributeUpdate update) {
        AttributeInstance instance = this.player.getAttribute(attribute);
        if (instance == null)
            return;
        update.modify(instance, modifier, value);
    }

    private void updateConfigAttributes() {
        this.setAttributeValue(Attributes.MAX_HEALTH, LibConstants.PLAYER_CONFIG_MODIFIER, GeneralConfig.startingHealth, AttributeUpdate.ABSOLUTE);
        this.setAttributeValue(RuneCraftoryAttributes.MAX_RUNEPOINTS.asHolder(), LibConstants.PLAYER_CONFIG_MODIFIER, GeneralConfig.startingRp, AttributeUpdate.ABSOLUTE);
        this.setAttributeValue(Attributes.ATTACK_DAMAGE, LibConstants.PLAYER_CONFIG_MODIFIER, GeneralConfig.startingStr, AttributeUpdate.ABSOLUTE);
        this.setForVitality(LibConstants.PLAYER_CONFIG_MODIFIER, GeneralConfig.startingVit, AttributeUpdate.ABSOLUTE);
        this.setAttributeValue(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), LibConstants.PLAYER_CONFIG_MODIFIER, GeneralConfig.startingIntel, AttributeUpdate.ABSOLUTE);
    }

    private void updateLevelAttributes() {
        float lvl = this.level.getLevel() - 1;
        this.setAttributeValue(Attributes.MAX_HEALTH, LibConstants.PLAYER_LEVEL_MODIFIER, GeneralConfig.hpPerLevel * (lvl + LevelCalc.getIntervalledMultiplier(this.level.getLevel(), 25, 30, 1)), AttributeUpdate.REPLACE);
        lvl += LevelCalc.getIntervalledMultiplier(this.level.getLevel(), 50, 30, 1);
        this.setAttributeValue(RuneCraftoryAttributes.MAX_RUNEPOINTS.asHolder(), LibConstants.PLAYER_LEVEL_MODIFIER, GeneralConfig.rpPerLevel * lvl, AttributeUpdate.REPLACE);
        this.setAttributeValue(Attributes.ATTACK_DAMAGE, LibConstants.PLAYER_LEVEL_MODIFIER, GeneralConfig.strPerLevel * lvl, AttributeUpdate.REPLACE);
        this.setForVitality(LibConstants.PLAYER_LEVEL_MODIFIER, GeneralConfig.vitPerLevel * lvl, AttributeUpdate.REPLACE);
        this.setAttributeValue(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), LibConstants.PLAYER_LEVEL_MODIFIER, GeneralConfig.intPerLevel * lvl, AttributeUpdate.REPLACE);
    }

    private void updateSkillLevelAttributes() {
        float adjust = LevelCalc.getIntervalledMultiplier(this.level.getLevel(), 50, 30, 0.5f);
        this.setAttributeValue(Attributes.MAX_HEALTH, LibConstants.PLAYER_SKILL_LEVEL_MODIFIER, this.skillVal(adjust, SkillProperties::healthIncrease), AttributeUpdate.REPLACE);
        adjust = LevelCalc.getIntervalledMultiplier(this.level.getLevel(), 150, 30, 0.5f);
        this.setAttributeValue(RuneCraftoryAttributes.MAX_RUNEPOINTS.asHolder(), LibConstants.PLAYER_SKILL_LEVEL_MODIFIER, this.skillVal(adjust, SkillProperties::rpIncrease), AttributeUpdate.REPLACE);
        this.setAttributeValue(Attributes.ATTACK_DAMAGE, LibConstants.PLAYER_SKILL_LEVEL_MODIFIER, this.skillVal(adjust, SkillProperties::strIncrease), AttributeUpdate.REPLACE);
        this.setForVitality(LibConstants.PLAYER_SKILL_LEVEL_MODIFIER, this.skillVal(adjust, SkillProperties::vitIncrease), AttributeUpdate.REPLACE);
        this.setAttributeValue(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), LibConstants.PLAYER_SKILL_LEVEL_MODIFIER, this.skillVal(adjust, SkillProperties::intelIncrease), AttributeUpdate.REPLACE);
    }

    private void clearAtributeModifier(Holder<Attribute> attribute, ResourceLocation modifier) {
        AttributeInstance instance = this.player.getAttribute(attribute);
        if (instance == null)
            return;
        instance.removeModifier(modifier);
    }

    private void clearAllAtributeModifier(ResourceLocation modifier) {
        ((AttributeMapAccessor) this.player.getAttributes())
                .getAttributes().forEach((att, inst) -> inst.removeModifier(modifier));
    }

    public int getRunePoints() {
        return (int) this.runePoints;
    }

    public int getMaxRunePoints() {
        return (int) this.player.getAttributeValue(RuneCraftoryAttributes.MAX_RUNEPOINTS.asHolder());
    }

    public boolean useRunePoints(int amount, boolean damage) {
        if (!GeneralConfig.useRp && !this.player.level().isClientSide)
            return true;
        if (!this.player.isCreative()) {
            if (this.player.hasEffect(RuneCraftoryEffects.FATIGUE.asHolder())) {
                amount *= 2;
            }
            if (this.runePoints >= amount)
                this.runePoints -= amount;
            else if (damage) {
                int diff = amount - this.getRunePoints();
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

    public void regenRunePoints(int amount) {
        if (amount <= 0)
            return;
        this.runePoints = Mth.clamp(this.runePoints + amount, 0, this.getMaxRunePoints());
        if (this.player instanceof ServerPlayer serverPlayer)
            LoaderNetwork.INSTANCE.sendToPlayer(new S2CRunePoints(this), serverPlayer);
    }

    public void setRunePoints(int amount) {
        this.runePoints = amount;
        if (this.player instanceof ServerPlayer serverPlayer)
            LoaderNetwork.INSTANCE.sendToPlayer(new S2CRunePoints(this), serverPlayer);
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

    public void giveMoney(int amount) {
        this.setMoney(this.getMoney() + amount);
    }

    public void setMoney(int amount) {
        this.money = amount;
        if (this.player instanceof ServerPlayer serverPlayer) {
            RuneCraftoryCriteria.MONEY_TRIGGER.get().trigger(serverPlayer);
            LoaderNetwork.INSTANCE.sendToPlayer(new S2CMoney(this), serverPlayer);
        }
    }

    public XpLevelHolder getPlayerLevel() {
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
        boolean levelUp = this.level.addXP(amount, GeneralConfig.maxLevel, LevelCalc::xpAmountForLevelUp, this::handleLevelStatUpdate);
        if (levelUp) {
            this.player.level().playSound(null, this.player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1, 0.5f);
        }
        if (this.player instanceof ServerPlayer serverPlayer) {
            if (levelUp)
                RuneCraftoryCriteria.LEVEL_TRIGGER.get().trigger(serverPlayer);
            LoaderNetwork.INSTANCE.sendToPlayer(new S2CLevelPkt(this), serverPlayer);
        }
    }

    private void handleLevelStatUpdate() {
        float health = this.player.getMaxHealth();
        int runePoints = this.getRunePoints();
        this.updateLevelAttributes();
        this.player.heal(this.player.getMaxHealth() - health);
        this.regenRunePoints(this.getMaxRunePoints() - runePoints);
    }

    public void recalculateStats(boolean regen) {
        if (!(this.player instanceof ServerPlayer serverPlayer))
            return;
        this.updateConfigAttributes();
        this.updateLevelAttributes();
        this.updateSkillLevelAttributes();
        if (regen) {
            this.player.setHealth(this.player.getMaxHealth());
            this.runePoints = this.getMaxRunePoints();
        }
        EntityUtils.sendAttributesTo(serverPlayer, serverPlayer);
        LoaderNetwork.INSTANCE.sendToPlayer(new S2CLevelPkt(this), serverPlayer);
    }

    private double skillVal(float adjust, Function<SkillProperties, Number> func) {
        return this.skillLevels.entrySet().stream().mapToDouble(e -> (e.getValue().getLevel() - 1 + adjust) * func.apply(DataPackHandler.INSTANCE.skillPropertiesManager().getPropertiesFor(e.getKey())).doubleValue()).sum();
    }

    public XpLevelHolder getSkillLevel(Skills skill) {
        return this.skillLevels.get(skill);
    }

    public void setSkillLevel(Skills skill, int level, float xpAmount, boolean recalc) {
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

    public void increaseSkill(Skills skill, float amount) {
        if (this.skillLevels.get(skill).getLevel() >= DataPackHandler.INSTANCE.skillPropertiesManager().getPropertiesFor(skill).maxLevel())
            return;
        boolean levelUp = this.skillLevels.get(skill).addXP(amount, DataPackHandler.INSTANCE.skillPropertiesManager().getPropertiesFor(skill).maxLevel(), lvl -> LevelCalc.xpAmountForSkillLevelUp(skill, lvl), this::onSkillLevelUp);
        if (levelUp) {
            this.player.level().playSound(null, this.player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1, 0.5f);
        }
        if (this.player instanceof ServerPlayer serverPlayer) {
            if (levelUp)
                RuneCraftoryCriteria.SKILL_LEVEL_TRIGGER.get().trigger(serverPlayer, skill);
            LoaderNetwork.INSTANCE.sendToPlayer(new S2CSkillLevelPkt(this, skill), serverPlayer);
        }
    }

    private void onSkillLevelUp() {
        float health = this.player.getMaxHealth();
        int runePoints = this.getRunePoints();
        this.updateSkillLevelAttributes();
        this.player.heal(this.player.getMaxHealth() - health);
        this.regenRunePoints(this.getMaxRunePoints() - runePoints);
    }

    public void increaseStatBonus(ItemStatIncrease.Stat type) {
        switch (type) {
            case STR -> this.setAttributeValue(Attributes.ATTACK_DAMAGE, LibConstants.PLAYER_STAT_BOOST_ITEM_INCREASE,
                    1, AttributeUpdate.ADD);
            case INT ->
                    this.setAttributeValue(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), LibConstants.PLAYER_STAT_BOOST_ITEM_INCREASE,
                            1, AttributeUpdate.ADD);
            case VIT -> this.setForVitality(LibConstants.PLAYER_STAT_BOOST_ITEM_INCREASE,
                    1, AttributeUpdate.ADD);
            case HP -> this.setAttributeValue(Attributes.MAX_HEALTH, LibConstants.PLAYER_STAT_BOOST_ITEM_INCREASE,
                    10, AttributeUpdate.ADD);
        }
    }

    public void resetAllStatBoost(ItemStatIncrease.Stat type) {
        switch (type) {
            case STR ->
                    this.clearAtributeModifier(Attributes.ATTACK_DAMAGE, LibConstants.PLAYER_STAT_BOOST_ITEM_INCREASE);
            case INT ->
                    this.clearAtributeModifier(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), LibConstants.PLAYER_STAT_BOOST_ITEM_INCREASE);
            case VIT -> {
                this.clearAtributeModifier(RuneCraftoryAttributes.DEFENCE.asHolder(), LibConstants.PLAYER_STAT_BOOST_ITEM_INCREASE);
                this.clearAtributeModifier(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), LibConstants.PLAYER_STAT_BOOST_ITEM_INCREASE);
            }
            case HP -> this.clearAtributeModifier(Attributes.MAX_HEALTH, LibConstants.PLAYER_STAT_BOOST_ITEM_INCREASE);
        }
    }

    public InventorySpells getInv() {
        return this.spells;
    }

    public InventoryShippingBin getShippingInv() {
        return this.shippingBin;
    }

    public void refreshShop() {
        if (this.player instanceof ServerPlayer serverPlayer) {
            for (NPCProfession profession : RuneCraftoryNPCProfessions.PROFESSIONS.registry()) {
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
                        if (shopItems.isEmpty() || (profession == RuneCraftoryNPCProfessions.TRAVELLING_MERCHANT.get() && shop.size() >= InventoryShop.SHOP_SIZE))
                            break;
                    }
                }
                DataPackHandler.INSTANCE.shopItemsManager().getDefaultItems(profession).forEach(props -> shop.add(props.stack()));
                this.shopItems.put(profession, shop);
            }
        }
    }

    public NonNullList<ItemStack> getShop(NPCProfession shop) {
        NonNullList<ItemStack> list = NonNullList.create();
        list.addAll(this.shopItems.getOrDefault(shop, NonNullList.withSize(0, ItemStack.EMPTY)));
        return list;
    }

    public void addShippingItem(ItemStack stack) {
        int level = ItemComponentUtils.itemLevel(stack);
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

    public void applyFoodEffect(ItemStack stack) {
        FoodProperties food = DataPackHandler.INSTANCE.foodManager().get(stack.getItem());
        if (food == null)
            return;
        Pair<Map<Holder<Attribute>, Double>, Map<Holder<Attribute>, Double>> foodStats = ItemComponentUtils.foodStats(stack);
        if (!foodStats.getFirst().isEmpty() || !foodStats.getSecond().isEmpty()) {
            this.removeFoodEffect(this.player);
            this.setFoodBonus(foodStats);
            this.foodDuration = food.duration();
            this.lastEaten = stack.getItemHolder();
        }
        if (this.player instanceof ServerPlayer serverPlayer) {
            LoaderNetwork.INSTANCE.sendToPlayer(new S2CFoodPkt(stack), serverPlayer);
        }
    }

    public void removeFoodEffect(Player player) {
        this.foodDuration = -1;
        this.lastEaten = null;
        this.clearAllAtributeModifier(LibConstants.FOOD_MODIFIER);
        this.clearAllAtributeModifier(LibConstants.FOOD_MODIFIER_MULTI);
        if (player instanceof ServerPlayer serverPlayer) {
            LoaderNetwork.INSTANCE.sendToPlayer(new S2CFoodPkt(null), serverPlayer);
        }
    }

    public FoodData foodBuff() {
        return new FoodData(Optional.ofNullable(this.lastEaten), this.foodDuration);
    }

    public void updateFoodBuff(FoodData data) {
        this.lastEaten = data.food().orElse(null);
        this.foodDuration = data.duration();
    }

    public WeaponHandler<Player> getWeaponHandler() {
        return this.weaponHandler;
    }

    public void tick() {
        this.animationHandler.tick();
        this.weaponHandler.tick();
        if (this.player instanceof ServerPlayer serverPlayer) {
            this.updater.tick(serverPlayer);
            if (serverPlayer.tickCount % 10 == 0) {
                if (this.walkingTracker.tickWalkingTracker(serverPlayer))
                    this.rpStillRegen = 200;
            }
            if (--this.rpStillRegen < 0 && serverPlayer.tickCount % 20 == 0) {
                this.regenRunePoints(1);
            }
            if (--this.breakTick <= 0)
                this.blockBreakPosForMsg = null;
            ItemStack main = this.player.getMainHandItem();
            ItemStack off = this.player.getOffhandItem();
            if (main.is(RuneCraftoryItems.MOB_STAFF.get()) || off.is(RuneCraftoryItems.MOB_STAFF.get())) {
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
        }
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

    public void resetAll() {
        PlayerData newData = new PlayerData(this.player);
        newData.spells.load(this.spells.save(this.player.registryAccess()), this.player.registryAccess());
        newData.shippingBin.load(this.shippingBin.save(this.player.registryAccess()), this.player.registryAccess());
        this.read(newData.write(this.player.registryAccess()), this.player.registryAccess());
        this.recalculateStats(false);
        this.refreshShop();
        this.starting = false;
        this.entityStatsTracker.reset();
        this.mobLevelIncrease = 0;
    }

    @Override
    public PlayerData read(CompoundTag tag, HolderLookup.Provider provider) {
        DynamicOps<Tag> ops = provider.createSerializationContext(NbtOps.INSTANCE);
        this.starting = tag.getBoolean("Starting");
        if (tag.contains("RestoreHP") && this.player instanceof ServerPlayer serverPlayer) {
            float f = tag.getFloat("RestoreHP");
            //Sheduling the health update in case other mods modify max health
            serverPlayer.getServer().tell(new TickTask(1, () -> this.player.setHealth(f)));
        }
        this.runePoints = tag.getDouble("RunePoints");
        this.level.read(tag.get("XpLevel"));
        CompoundTag skillCompound = tag.getCompound("Skills");
        for (Skills skill : Skills.values()) {
            this.skillLevels.get(skill).read(skillCompound.get(skill.toString()));
        }
        this.money = tag.getInt("Money");
        this.keeper.read(tag.getCompound("Recipes"));

        this.shippingBin.load(tag.getCompound("Shippingbin"), provider);
        ListTag ship = tag.getList("ShippedItems", Tag.TAG_COMPOUND);
        ship.forEach(t -> {
            CompoundTag data = (CompoundTag) t;
            this.shippedItems.put(BuiltInRegistries.ITEM.byNameCodec().parse(ops, data.get("Item")).getOrThrow(),
                    new ShippedItemData(data.getInt("Amount"), data.getInt("Level")));
        });
        ListTag shop = tag.getList("ShopItems", Tag.TAG_COMPOUND);
        shop.forEach(t -> {
            CompoundTag data = (CompoundTag) t;
            NonNullList<ItemStack> list = NonNullList.create();
            ListTag items = data.getList("Items", Tag.TAG_COMPOUND);
            items.forEach(lt -> ItemStack.parse(provider, lt).ifPresent(list::add));
            this.shopItems.put(RuneCraftoryNPCProfessions.PROFESSIONS.registry().byNameCodec().parse(ops, data.get("Shop")).getOrThrow(), list);
        });
        this.spells.load(tag.getCompound("Inventory"), provider);
        this.updater.read(tag.getCompound("DailyUpdater"));

        this.lastEaten = tag.contains("LastFood") ? BuiltInRegistries.ITEM.holderByNameCodec().parse(NbtOps.INSTANCE, tag.get("LastFood")).getOrThrow() : null;
        this.foodDuration = tag.getInt("FoodBuffDuration");

        this.walkingTracker.read(tag.getCompound("WalkingTracker"));
        this.entityStatsTracker.read(tag.getCompound("TamedEntityTracker"));
        this.party.load(tag.getCompound("PartyTag"));
        this.craftingSeed = tag.getInt("CraftingSeed");
        this.boughtBarns = tag.getInt("BoughtBarns");
        this.mobLevelIncrease = tag.getInt("MobLevelIncrease");
        return this;
    }

    @Override
    public CompoundTag write(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        DynamicOps<Tag> ops = provider.createSerializationContext(NbtOps.INSTANCE);
        tag.putBoolean("Starting", this.starting);
        tag.putDouble("RunePoints", this.runePoints);
        tag.put("XpLevel", this.level.save());
        CompoundTag skillCompound = new CompoundTag();
        for (Skills skill : Skills.values()) {
            skillCompound.put(skill.toString(), this.skillLevels.get(skill).save());
        }
        tag.put("Skills", skillCompound);
        tag.putInt("Money", this.money);
        tag.put("Recipes", this.keeper.save());

        tag.put("ShippingBin", this.shippingBin.save(provider));
        ListTag ship = new ListTag();
        this.shippedItems.forEach((key, value) -> {
            CompoundTag data = new CompoundTag();
            data.putInt("Amount", value.amount);
            data.putInt("Level", value.maxLevel);
            data.put("Item", BuiltInRegistries.ITEM.byNameCodec().encodeStart(ops, key).getOrThrow());
            ship.add(data);
        });
        tag.put("ShippedItems", ship);
        ListTag shop = new ListTag();
        for (Map.Entry<NPCProfession, NonNullList<ItemStack>> entry : this.shopItems.entrySet()) {
            CompoundTag data = new CompoundTag();
            data.put("Shop", RuneCraftoryNPCProfessions.PROFESSIONS.registry().byNameCodec().encodeStart(ops, entry.getKey()).getOrThrow());
            ListTag items = new ListTag();
            for (ItemStack stack : entry.getValue())
                items.add(stack.save(provider, new CompoundTag()));
            data.put("Items", items);
        }
        tag.put("ShopItems", shop);
        tag.put("Inventory", this.spells.save(provider));
        tag.put("DailyUpdater", this.updater.save());

        if (this.lastEaten != null)
            tag.put("LastFood", BuiltInRegistries.ITEM.holderByNameCodec().encodeStart(NbtOps.INSTANCE, this.lastEaten).getOrThrow());
        tag.putInt("FoodBuffDuration", this.foodDuration);

        tag.put("WalkingTracker", this.walkingTracker.save());
        tag.put("TamedEntityTracker", this.entityStatsTracker.save());
        tag.put("PartyTag", this.party.save());
        tag.putInt("CraftingSeed", this.craftingSeed);
        tag.putInt("BoughtBarns", this.boughtBarns);
        tag.putInt("MobLevelIncrease", this.mobLevelIncrease);
        return tag;
    }

    public record ShippedItemData(int amount, int maxLevel) {

    }

    public record FoodData(Optional<Holder<Item>> food, int duration) {

    }

    public enum AttributeUpdate {
        ABSOLUTE,
        REPLACE,
        ADD;

        public void modify(AttributeInstance instance, ResourceLocation modifier, double from) {
            this.modify(instance, modifier, from, AttributeModifier.Operation.ADD_VALUE);
        }

        public void modify(AttributeInstance instance, ResourceLocation modifier, double from, AttributeModifier.Operation operation) {
            double value = switch (this) {
                case ABSOLUTE -> from - instance.getBaseValue();
                case REPLACE -> from;
                case ADD -> {
                    AttributeModifier mod = instance.getModifier(modifier);
                    yield mod == null ? from : mod.amount() + from;
                }
            };
            instance.removeModifier(modifier);
            if (this == REPLACE && value <= 0)
                return;
            instance.addPermanentModifier(new AttributeModifier(modifier, value, operation));
        }
    }
}