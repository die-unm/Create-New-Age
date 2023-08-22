package org.antarcticgardens.newage;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.api.Updatable;
import earth.terrarium.botarium.api.energy.InsertOnlyEnergyContainer;
import earth.terrarium.botarium.api.energy.StatefulEnergyContainer;
import earth.terrarium.botarium.common.energy.base.EnergyContainer;
import earth.terrarium.botarium.common.energy.base.EnergySnapshot;
import earth.terrarium.botarium.common.energy.impl.SimpleEnergySnapshot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SimpleInsertOnlyMutableContainer extends InsertOnlyEnergyContainer {
    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    private long capacity;
    private long energy;

    public SimpleInsertOnlyMutableContainer(Updatable entity, long maxCapacity) {
        super(entity, maxCapacity);
        this.capacity = maxCapacity;
    }

    @Override
    public long insertEnergy(long maxAmount, boolean simulate) {
        return internalInsert(maxAmount, simulate);
    }

    @Override
    public long extractEnergy(long maxAmount, boolean simulate) {
        return 0;
    }

    @Override
    public long internalInsert(long maxAmount, boolean simulate) {
        long inserted = Mth.clamp(maxAmount, 0, getMaxCapacity() - getStoredEnergy());
        if (simulate) return inserted;
        this.setEnergy(this.energy + inserted);
        return inserted;
    }

    @Override
    public long internalExtract(long maxAmount, boolean simulate) {
        long extracted = Mth.clamp(maxAmount, 0, getStoredEnergy());
        if (simulate) return extracted;
        this.setEnergy(this.energy - extracted);
        return extracted;
    }

    @Override
    public void setEnergy(long energy) {
        this.energy = energy;
    }

    @Override
    public long getStoredEnergy() {
        return energy;
    }

    @Override
    public long getMaxCapacity() {
        return capacity;
    }

    @Override
    public long maxInsert() {
        return 1024;
    }

    @Override
    public long maxExtract() {
        return 1024;
    }

    @Override
    public CompoundTag serialize(CompoundTag root) {
        CompoundTag tag = root.getCompound(Botarium.BOTARIUM_DATA);
        tag.putLong("Energy", this.energy);
        root.put(Botarium.BOTARIUM_DATA, tag);
        return root;
    }

    @Override
    public void deserialize(CompoundTag root) {
        CompoundTag tag = root.getCompound(Botarium.BOTARIUM_DATA);
        this.energy = tag.getLong("Energy");
    }

    @Override
    public boolean allowsInsertion() {
        return true;
    }

    @Override
    public boolean allowsExtraction() {
        return false;
    }
}
