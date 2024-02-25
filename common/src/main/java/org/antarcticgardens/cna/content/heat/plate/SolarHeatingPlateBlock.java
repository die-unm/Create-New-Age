package org.antarcticgardens.cna.content.heat.plate;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.utility.Lang;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.antarcticgardens.cna.CNABlockEntityTypes;
import org.antarcticgardens.cna.config.CNAConfig;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static org.antarcticgardens.cna.content.heat.pipe.HeatPipeBlock.massPipe;

public class SolarHeatingPlateBlock extends Block implements EntityBlock, IWrenchable {
    private final BlockEntityEntry<?> entry;
    private final int strength;

    public SolarHeatingPlateBlock(Properties properties, BlockEntityEntry<?> entry, int strength) {
        super(properties.strength(4.0f));
        this.entry = entry;
        this.strength = strength;
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return entry.create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        final int on = massPipe;
        massPipe++;
        if (massPipe >= 20) {
            massPipe = 0;
        }
        return (level1, blockPos, blockState, blockEntity) -> {
            if ((level1.getGameTime() + on) % 20 != 0 || !(blockEntity instanceof SolarHeatingPlateBlockEntity ent)) return;
            ent.tick(blockPos, level1, blockState);
        };
    }

    public static SolarHeatingPlateBlock createAdvanced(Properties properties) {
        return  new SolarHeatingPlateBlock(properties, CNABlockEntityTypes.ADVANCED_SOLAR_HEATING_PLATE, 60);
    }

    public static SolarHeatingPlateBlock createBasic(Properties properties) {
        return  new SolarHeatingPlateBlock(properties, CNABlockEntityTypes.BASIC_SOLAR_HEATING_PLATE, 20);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Lang.translate("tooltip.create_new_age.generates").style(ChatFormatting.GRAY)
                .component());
        tooltip.add(Lang.text(" ").translate("tooltip.create_new_age.temperature.ps", strength * CNAConfig.getCommon().solarPanelHeatMultiplier.get()).style(ChatFormatting.AQUA).component());
    }
}
