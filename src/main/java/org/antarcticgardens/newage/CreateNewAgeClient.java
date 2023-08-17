package org.antarcticgardens.newage;

import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.antarcticgardens.newage.content.electricity.ElectricityPonder;
import org.antarcticgardens.newage.content.energiser.EnergiserPonder;
import org.antarcticgardens.newage.content.heat.HeatingPonder;
import org.antarcticgardens.newage.content.heat.heater.HeaterPonder;
import org.antarcticgardens.newage.content.reactor.ReactorPonder;

public class CreateNewAgeClient {


    public static void onInitializeClient(final FMLClientSetupEvent event) {

        // ponders
        PonderRegistrationHelper helper = new PonderRegistrationHelper(CreateNewAge.MOD_ID);
        helper.addStoryBoard(NewAgeBlocks.ENERGISER_T1, "energiser", EnergiserPonder::ponder);
        helper.addStoryBoard(NewAgeBlocks.ENERGISER_T2, "energiser", EnergiserPonder::ponder);
        helper.addStoryBoard(NewAgeBlocks.ENERGISER_T3, "energiser", EnergiserPonder::ponder);

        helper.addStoryBoard(NewAgeBlocks.HEAT_PIPE, "heating", HeatingPonder::ponder);
        helper.addStoryBoard(NewAgeBlocks.HEAT_PUMP, "heating", HeatingPonder::ponder);
        helper.addStoryBoard(NewAgeBlocks.BASIC_SOLAR_HEATING_PLATE, "heating", HeatingPonder::ponder);
        helper.addStoryBoard(NewAgeBlocks.ADVANCED_SOLAR_HEATING_PLATE, "heating", HeatingPonder::ponder);
        helper.addStoryBoard(NewAgeBlocks.STIRLING_ENGINE, "heating", HeatingPonder::ponder);

        helper.addStoryBoard(NewAgeBlocks.HEATER, "heater", HeaterPonder::ponder);

        helper.addStoryBoard(NewAgeBlocks.REACTOR_CASING, "reactor", ReactorPonder::ponder);
        helper.addStoryBoard(NewAgeBlocks.REACTOR_GLASS, "reactor", ReactorPonder::ponder);
        helper.addStoryBoard(NewAgeBlocks.REACTOR_FUEL_ACCEPTOR, "reactor", ReactorPonder::ponder);
        helper.addStoryBoard(NewAgeBlocks.REACTOR_ROD, "reactor", ReactorPonder::ponder);
        helper.addStoryBoard(NewAgeBlocks.REACTOR_HEAT_VENT, "reactor", ReactorPonder::ponder);
        helper.addStoryBoard(NewAgeItems.NUCLEAR_FUEL, "reactor", ReactorPonder::ponder);

        helper.addStoryBoard(NewAgeBlocks.ELECTRICAL_CONNECTOR, "wires", ElectricityPonder::ponder);
        helper.addStoryBoard(NewAgeItems.COPPER_WIRE, "wires", ElectricityPonder::ponder);
        helper.addStoryBoard(NewAgeItems.DIAMOND_WIRE, "wires", ElectricityPonder::ponder);
        helper.addStoryBoard(NewAgeItems.GOLDEN_WIRE, "wires", ElectricityPonder::ponder);
        helper.addStoryBoard(NewAgeItems.IRON_WIRE, "wires", ElectricityPonder::ponder);

        // ToolTip
        addToolTipModifier(NewAgeBlocks.ENERGISER_T1);
        addToolTipModifier(NewAgeBlocks.ENERGISER_T2);
        addToolTipModifier(NewAgeBlocks.ENERGISER_T3);
        addToolTipModifier(NewAgeBlocks.STIRLING_ENGINE);
        addToolTipModifier(NewAgeBlocks.GENERATOR_COIL);
    }

    public static void addToolTipModifier(BlockEntry<?> entry) {
        TooltipModifier.REGISTRY.register(entry.asItem(), KineticStats.create(entry.asItem()));

    }

}
