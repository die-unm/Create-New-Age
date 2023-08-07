package org.antarcticgardens.newage;

import com.simibubi.create.content.fluids.tank.BoilerHeaters;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.antarcticgardens.newage.content.energiser.EnergisingRecipe;
import org.antarcticgardens.newage.tools.RecipeTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.antarcticgardens.newage.content.heat.heater.HeaterBlock.STRENGTH;

@Mod("create_new_age")
public class CreateNewAge {
    public static final Logger LOGGER = LoggerFactory.getLogger("create_new_age");

	public static final String MOD_ID = "create_new_age";

	public static final CreateRegistrate BASE_REGISTRATE = CreateRegistrate.create(MOD_ID);

	public static final RegistryObject<CreativeModeTab> tab = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID).register("create_new_age_tab",
			() -> CreativeModeTab.builder()
					.title(Component.translatable("item_group." + MOD_ID + ".tab"))
					.icon(() -> NewAgeBlocks.ENERGISER_T1.asStack())
					.build()
			);

	public static final CreateRegistrate REGISTRATE = BASE_REGISTRATE.useCreativeTab(tab);


	public static final ResourceKey<CreativeModeTab> CREATIVE_TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB,
			new ResourceLocation(MOD_ID, "tab"));

	public CreateNewAge() {
		var modBus = FMLJavaModLoadingContext.get().getModEventBus();
		LOGGER.info("Hello 1.20.1 Create!");

		try {
			Configurations.load();
		} catch (IOException e) {
			LOGGER.error("Failed to load config.", e);
		}

		REGISTRATE.registerEventListeners(modBus);

		NewAgeBlocks.load();
		NewAgeBlockEntityTypes.load();
		NewAgeItems.load();

		FMLJavaModLoadingContext.get().getModEventBus().addListener(CreateNewAgeClient::onInitializeClient);

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::generalSetup);

		try {
			EnergisingRecipe.type = RecipeTool.createIRecipeTypeInfo("energising", new ProcessingRecipeSerializer<>(EnergisingRecipe::new));
		} catch (Exception e) {
			LOGGER.error("Exceiption", e);
		}
	}

	private void generalSetup(final FMLCommonSetupEvent event) {

		event.enqueueWork(() -> BoilerHeaters.registerHeater(NewAgeBlocks.HEATER.get(), (level, pos, state) -> state.getValue(STRENGTH) - 1));
	}


}