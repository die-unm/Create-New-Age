package org.antarcticgardens.cna;

import com.simibubi.create.content.fluids.tank.BoilerHeaters;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.levelgen.GenerationStep;
import org.antarcticgardens.cna.config.CNAConfig;
import org.antarcticgardens.cna.content.electricity.generation.magnet.MagnetPlacementHelper;
import org.antarcticgardens.cna.content.energising.recipe.EnergisingRecipe;
import org.antarcticgardens.cna.content.heat.heater.HeaterBlock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateNewAge {
    public static final Logger LOGGER = LogManager.getLogger();
    
    public static final String MOD_ID = "create_new_age";
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);
    public static final ResourceKey<CreativeModeTab> CREATIVE_TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, 
            new ResourceLocation(MOD_ID, "tab"));

    private static CreateNewAge instance;
    
    private Platform platform;
    private int magnetPlacementHelperId;
    
    protected CreateNewAge() {
        instance = this;
    }
    
    protected void initialize(Platform platform) {
        this.platform = platform;
        
        LOGGER.info("Hello 1.20.1 Create!");

        platform.getRegistrar().beforeRegistration();
        
        CNABlocks.load();
        CNABlockEntityTypes.load();
        CNAItems.load();
        CNATags.load();
        CNAPartialModels.load();
        EnergisingRecipe.load();
        
        magnetPlacementHelperId = PlacementHelpers.register(new MagnetPlacementHelper());
        
        platform.getRegistrar().afterRegistration();
        CNAConfig.load();

        platform.commonSetup(() -> {
            BoilerHeaters.registerHeater(CNABlocks.HEATER.get(), ((level, pos, state) -> state.getValue(HeaterBlock.STRENGTH).ordinal() - 1));
        });

        // TODO: Ores
        
        // TODO: Monkey edition?
    }
    
    public Platform getPlatform() {
        return platform;
    }
    
    public int getMagnetPlacementHelperId() {
        return magnetPlacementHelperId;
    }
    
    public static CreateNewAge getInstance() {
        return instance;
    }
}
