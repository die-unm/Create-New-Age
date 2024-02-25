package org.antarcticgardens.cna.data.recipe;

import com.simibubi.create.foundation.utility.RegisteredObjects;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.antarcticgardens.cna.CreateNewAge;

@SuppressWarnings("unchecked")
public abstract class GeneratedRecipeBuilderBase<SELF> {
    protected final ItemLike result;

    protected String name = null;
    protected String suffix = "";
    protected RecipeCategory category = RecipeCategory.MISC;
    protected int amount = 1;
    protected CriterionTriggerInstance trigger;

    public GeneratedRecipeBuilderBase(ItemLike result) {
        this.result = result;
    }

    protected SELF suffix(String suffix) {
        this.suffix = suffix;
        return (SELF) this;
    }
    
    protected SELF name(String name) {
        this.name = name;
        return (SELF) this;
    }

    protected SELF category(RecipeCategory category) {
        this.category = category;
        return (SELF) this;
    }

    protected SELF unlockedBy(TagKey<Item> required) {
        trigger = RegistrateRecipeProvider.inventoryTrigger(ItemPredicate.Builder.item().of(required).build());
        return (SELF) this;
    }

    protected SELF unlockedBy(ItemLike... required) {
        trigger = RegistrateRecipeProvider.inventoryTrigger(ItemPredicate.Builder.item().of(required).build());
        return (SELF) this;
    }

    protected SELF amount(int amount) {
        this.amount = amount;
        return (SELF) this;
    }

    protected ResourceLocation createLocation() {
        return createLocation("");
    }

    protected ResourceLocation createLocation(String type) {
        String name = this.name == null ? RegisteredObjects.getKeyOrThrow(result.asItem()).getPath() : this.name;
        return new ResourceLocation(CreateNewAge.MOD_ID, type + (type.isEmpty() ? "" : "/") + name + suffix);
    }
}
