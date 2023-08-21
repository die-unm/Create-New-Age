package org.antarcticgardens.newage;

import com.simibubi.create.content.contraptions.ContraptionMovementSetting;
import com.simibubi.create.content.fluids.tank.BoilerHeaters;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.levelgen.GenerationStep;
import org.antarcticgardens.newage.config.NewAgeConfig;
import org.antarcticgardens.newage.content.energiser.EnergisingRecipe;
import org.antarcticgardens.newage.tools.RecipeTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.antarcticgardens.newage.content.heat.heater.HeaterBlock.STRENGTH;

public class CreateNewAge implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("create_new_age");

	public static final String MOD_ID = "create_new_age";

	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);
	public static final CreateRegistrate REGISTRATE_UNTABBED = CreateRegistrate.create(MOD_ID);


	public static final CreativeModeTab CREATIVE_TAB = FabricItemGroupBuilder.build(new ResourceLocation(MOD_ID, "tab"), NewAgeBlocks.ENERGISER_T1::asStack);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello 1.20.1 Create!");

		NewAgeBlocks.load();
		NewAgeBlockEntityTypes.load();
		NewAgeItems.load();

		REGISTRATE.register();
		NewAgeConfig.getCommon();

		BoilerHeaters.registerHeater(NewAgeBlocks.HEATER.get(), (level, pos, state) -> state.getValue(STRENGTH) - 1);

		EnergisingRecipe.type = RecipeTool.createIRecipeTypeInfo("energising", new ProcessingRecipeSerializer<>(EnergisingRecipe::new));

		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, ResourceKey.create(Registry.FEATURE_REGISTRY.registry(), new ResourceLocation("create_new_age","ore_thorium")));
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, ResourceKey.create(Registry.FEATURE_REGISTRY.registry(), new ResourceLocation("create_new_age","magnetite")));

		ContraptionMovementSetting.register(NewAgeBlocks.ELECTRICAL_CONNECTOR.get(), () -> ContraptionMovementSetting.UNMOVABLE);

		var containerContainer = FabricLoader.getInstance().getModContainer("create_new_age");

		if (containerContainer.isEmpty()) {
			return;
		}

		ModContainer modContainer = containerContainer.get();
		ResourceManagerHelper.registerBuiltinResourcePack(new ResourceLocation(MOD_ID, "create_new_age_monkey_edition"), modContainer, "Create: New Age (Monkey Edition)", ResourcePackActivationType.NORMAL);
	}
}