package org.antarcticgardens.cna.content.motor;

import com.simibubi.create.content.kinetics.motor.CreativeMotorBlock;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

#if CNA_FABRIC
import io.github.fabricators_of_create.porting_lib.models.generators.ModelFile;
#else
import net.minecraftforge.client.model.generators.ModelFile;
#endif

public class MotorBlockStateGen extends SpecialBlockStateGen {
    private final String parentModelId;
    
    public MotorBlockStateGen(String parentModelId) {
        this.parentModelId = parentModelId;
    }
    
    @Override
    public int getXRotation(BlockState state) {
        return state.getValue(CreativeMotorBlock.FACING) == Direction.DOWN ? 180 : 0;
    }

    @Override
    public int getYRotation(BlockState state) {
        return state.getValue(CreativeMotorBlock.FACING)
                .getAxis()
                .isVertical() ? 0 : horizontalAngle(state.getValue(CreativeMotorBlock.FACING));
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        var horizontal = prov.models().withExistingParent("block/" + ctx.getName() + "/horizontal", prov.modLoc("block/" + parentModelId + "/horizontal"))
                .texture("tier", "block/" + ctx.getName());
        var vertical = prov.models().withExistingParent("block/" + ctx.getName() + "/vertical", prov.modLoc("block/" + parentModelId + "/vertical"))
                .texture("tier", "block/" + ctx.getName());

        return prov.models().getExistingFile(state.getValue(CreativeMotorBlock.FACING).getAxis().isVertical()
                ? vertical.getLocation() : horizontal.getLocation());
    }
}
