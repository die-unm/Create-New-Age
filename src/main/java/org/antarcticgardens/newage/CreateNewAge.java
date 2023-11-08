package org.antarcticgardens.newage;

import com.simibubi.create.content.contraptions.ContraptionMovementSetting;
import com.simibubi.create.content.fluids.tank.BoilerHeaters;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.resource.PathPackResources;
import org.antarcticgardens.newage.config.NewAgeConfig;
import org.antarcticgardens.newage.content.energiser.EnergisingRecipe;
import org.antarcticgardens.newage.content.generation.magnets.MagnetPlacementHelper;
import org.antarcticgardens.newage.tools.RecipeTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

import static org.antarcticgardens.newage.content.heat.heater.HeaterBlock.STRENGTH;

@Mod("create_new_age")
public class CreateNewAge {
    public static final Logger LOGGER = LoggerFactory.getLogger("create_new_age");

	public static final String MOD_ID = "create_new_age";

	public static final CreateRegistrate BASE_REGISTRATE = CreateRegistrate.create(MOD_ID);


	public static final CreativeModeTab CREATIVE_TAB = new NewAgeCreativeTab("create_new_age_tab");

	public static final CreateRegistrate REGISTRATE = BASE_REGISTRATE.creativeModeTab(() -> CREATIVE_TAB);

	public static IRecipeTypeInfo ENERGISING_RECIPE_TYPE;
	
	private static int magnetPlacementHelperId;

	public CreateNewAge() {
		var modBus = FMLJavaModLoadingContext.get().getModEventBus();
		LOGGER.info("Hello 1.20.1 Create!");

		BASE_REGISTRATE.registerEventListeners(modBus);

		NewAgeBlocks.load();
		NewAgeBlockEntityTypes.load();
		NewAgeItems.load();

		magnetPlacementHelperId = PlacementHelpers.register(new MagnetPlacementHelper());
		
		NewAgeConfig.getCommon();

		FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientIniter::onInitializeClient);

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::generalSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerDatapack);

		RecipeTool.register_type.register(modBus);
		RecipeTool.register.register(modBus);

		try {
			ENERGISING_RECIPE_TYPE = RecipeTool.createIRecipeTypeInfo("energising", new ProcessingRecipeSerializer<>(EnergisingRecipe::new));
		} catch (Exception e) {
			LOGGER.error("Exception", e);
		}
	}

	public static int getMagnetPlacementHelperId() {
		return magnetPlacementHelperId;
	}
	
	public void registerDatapack(final AddPackFindersEvent event) {
		try {
			if (event.getPackType() == PackType.SERVER_DATA) {
				Path path = ModList.get().getModFileById(CreateNewAge.MOD_ID).getFile().findResource("resourcepacks/create_new_age_monkey_edition");
				PathPackResources resources = new PathPackResources(CreateNewAge.MOD_ID, path);
				PackMetadataSection section = resources.getMetadataSection(PackMetadataSection.SERIALIZER);

				String name = "create_new_age:create_new_age_monkey_edition";
				event.addRepositorySource((packConsumer, packConstructor) ->
						packConsumer.accept(packConstructor.create(
								name,
								Component.translatable("create_new_age.monkey_edition"),
								false,
								() -> resources,
								section,
								Pack.Position.TOP,
								PackSource.BUILT_IN,
								false
						))
				);
			}
		} catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

	private void generalSetup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			BoilerHeaters.registerHeater(NewAgeBlocks.HEATER.get(), (level, pos, state) -> state.getValue(STRENGTH).ordinal() - 1);
			ContraptionMovementSetting.register(NewAgeBlocks.ELECTRICAL_CONNECTOR.get(), () -> ContraptionMovementSetting.UNMOVABLE);
		});
	}
}