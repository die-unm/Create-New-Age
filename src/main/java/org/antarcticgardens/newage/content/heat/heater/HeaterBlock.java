package org.antarcticgardens.newage.content.heat.heater;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.antarcticgardens.newage.NewAgeBlockEntityTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.antarcticgardens.newage.content.heat.heatpipe.HeatPipeBlock.massPipe;

public class HeaterBlock extends Block implements EntityBlock {

    public static final IntegerProperty STRENGTH = IntegerProperty.create("heat_strength", 0, 3);
    public HeaterBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STRENGTH);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return NewAgeBlockEntityTypes.HEATER.create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        final int on = massPipe;
        massPipe++;
        if (massPipe >= 20) {
            massPipe = 0;
        }
        return (world, blockPos, blockState, sel) -> {
            if ((world.getGameTime() + on) % 20 != 0 || !(sel instanceof HeaterBlockEntity self) || self.getLevel() == null) return;
            if (self.heat > 8000) {
                self.getLevel().setBlock(self.getBlockPos(), Blocks.LAVA.defaultBlockState(), 3);
            } else if (self.heat > 200) {
                self.heat -= 200;
                level.setBlock(blockPos, state.setValue(STRENGTH, 3), 3);
            } else if (self.heat > 100) {
                self.heat -= 100;
                level.setBlock(blockPos, state.setValue(STRENGTH, 2), 3);
            } else if (self.heat > 50) {
                self.heat -= 50;
                level.setBlock(blockPos, state.setValue(STRENGTH, 1), 3);
            } else {
                level.setBlock(blockPos, state.setValue(STRENGTH, 0), 3);
            }
        };
    }

}
