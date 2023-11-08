package org.antarcticgardens.newage.energy;

import com.mojang.datafixers.util.Pair;
import earth.terrarium.botarium.api.energy.EnergyBlock;
import earth.terrarium.botarium.api.energy.PlatformEnergyManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import static earth.terrarium.botarium.common.energy.util.EnergyHooks.safeGetBlockEnergyManager;

public class EnergyHooks {
    public static <T extends BlockEntity & EnergyBlock> void distributeEnergyNearby(T energyBlock, long amount) {
        BlockPos blockPos = energyBlock.getBlockPos();
        Level level = energyBlock.getLevel();
        if (level == null) return;
        Direction.stream()
                .map(direction -> Pair.of(direction, level.getBlockEntity(blockPos.relative(direction))))
                .filter(pair -> pair.getSecond() != null)
                .map(pair -> Pair.of(safeGetBlockEnergyManager(pair.getSecond(), pair.getFirst().getOpposite()), pair.getFirst()))
                .filter(pair -> pair.getFirst().isPresent())
                .forEach(pair -> {
                    PlatformEnergyManager externalEnergy = (PlatformEnergyManager) pair.getFirst().get();

                    // This weird construction is used because Botarium has duplicated PlatformEnergyManager 
                    // that causes the "unable to cast" crash
                    var energy = safeGetBlockEnergyManager(energyBlock, pair.getSecond()); 
                    if (energy.isPresent())
                        moveEnergy((PlatformEnergyManager) energy.get(), externalEnergy, 
                                amount == -1 ? energyBlock.getEnergyStorage().getStoredEnergy() : amount);
                });
    }

    public static long moveEnergy(PlatformEnergyManager from, PlatformEnergyManager to, long amount) {
        long extracted = from.extract(amount, true);
        long inserted = to.insert(extracted, false);
        return from.extract(inserted, false);
    }
}
