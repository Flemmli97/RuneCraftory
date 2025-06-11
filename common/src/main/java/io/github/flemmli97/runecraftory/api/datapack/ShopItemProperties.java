package io.github.flemmli97.runecraftory.api.datapack;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.utils.CodecHelper;
import io.github.flemmli97.runecraftory.common.utils.MiscUtils;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public record ShopItemProperties(ItemStack stack, UnlockType unlockType,
                                 EntityPredicate predicate) {

    public static final Codec<TagKey<Item>> TAG_CODEC = Codec.STRING.flatXmap(
            r -> {
                if (r.startsWith("#"))
                    return DataResult.success(TagKey.create(Registries.ITEM, ResourceLocation.parse(r.substring(1))));
                return DataResult.error(() -> "Not a tag value" + r);
            },
            l -> DataResult.success("#" + l.location())
    );

    public static final Codec<MultiItemValue> MULTI_ITEM_VALUE_CODEC = Codec.either(TAG_CODEC, CodecHelper.listOrSingle(CodecHelper.ITEM_OR_STACK))
            .xmap(MultiItemValue::new, v -> v.tag != null ? Either.left(v.tag) : Either.right(v.items));

    public static final Codec<IntermediaryShopItem> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    MULTI_ITEM_VALUE_CODEC.fieldOf("item").forGetter(d -> d.items),
                    CodecUtils.stringEnumCodec(UnlockType.class, null).fieldOf("unlock_type").forGetter(d -> d.unlockType),
                    EntityPredicate.CODEC.optionalFieldOf("predicate").forGetter(d -> Optional.ofNullable(d.predicate == EntityPredicate.ANY ? null : d.predicate))
            ).apply(instance, (stack, type, adv) ->
                    new IntermediaryShopItem(stack, type, adv.orElse(EntityPredicate.ANY))));

    public static List<ShopItemProperties> from(IntermediaryShopItem item) {
        List<ShopItemProperties> list = new ArrayList<>();
        item.items().getContents().forEach(stack -> list.add(new ShopItemProperties(stack, item.unlockType, item.predicate)));
        return list;
    }

    /**
     * Used for the saved data instance
     */
    public record IntermediaryShopItem(MultiItemValue items, UnlockType unlockType,
                                       EntityPredicate predicate) {
        public IntermediaryShopItem(MultiItemValue items, UnlockType unlockType) {
            this(items, unlockType, EntityPredicate.ANY);
        }

        public boolean isTag() {
            return this.items.tag != null;
        }
    }

    public static class MultiItemValue {

        private final TagKey<Item> tag;
        private final List<ItemStack> items;

        private MultiItemValue(Either<TagKey<Item>, List<ItemStack>> either) {
            this.tag = either.left().orElse(null);
            this.items = either.right().orElse(null);
        }

        public MultiItemValue(TagKey<Item> tag) {
            this.tag = tag;
            this.items = null;
        }

        public MultiItemValue(List<ItemStack> list) {
            this.tag = null;
            this.items = list;
        }

        public static MultiItemValue of(Item... item) {
            return new MultiItemValue(Arrays.stream(item).map(ItemStack::new).toList());
        }

        public List<ItemStack> getContents() {
            if (this.tag != null) {
                return MiscUtils.expandTag(Registry.ITEM, this.tag, ItemStack::new);
            }
            return this.items;
        }
    }

    public enum UnlockType {
        DEFAULT,
        ALWAYS,
        NEEDS_SHIPPING
    }
}
