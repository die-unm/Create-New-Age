package org.antarcticgardens.newage.content.electricity.network;

import earth.terrarium.botarium.api.energy.EnergyBlock;
import earth.terrarium.botarium.api.energy.EnergySnapshot;
import earth.terrarium.botarium.api.energy.SimpleEnergySnapshot;
import earth.terrarium.botarium.api.energy.StatefulEnergyContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.antarcticgardens.newage.content.electricity.connector.ElectricalConnectorBlockEntity;

public class NetworkEnergyContainer implements StatefulEnergyContainer<BlockEntity> {
    private final ElectricalConnectorBlockEntity connector;
    private ElectricalNetwork network;

    public NetworkEnergyContainer(ElectricalConnectorBlockEntity connector, ElectricalNetwork network) {
        this.connector = connector;
        this.network = network;
    }

    @Override
    public long insertEnergy(long maxAmount, boolean simulate) {
        if (connector.getLevel() != null) {
            BlockEntity entity = connector.getLevel().getBlockEntity(connector.getSupportingBlockPos());

            if (entity instanceof EnergyBlock energyBlock) {
                long canExtract = energyBlock.getEnergyStorage().extractEnergy(Long.MAX_VALUE, true);
                long inserted = network.insert(connector, canExtract, false);
                energyBlock.getEnergyStorage().extractEnergy(inserted, false);
            }
        }

        return 0;
    }

    @Override
    public long extractEnergy(long maxAmount, boolean simulate) {
        return 0;
    }

    @Override
    public long getStoredEnergy() {
        return 0;
    }

    @Override
    public long getMaxCapacity() {
        return Long.MAX_VALUE;
    }

    @Override
    public long maxInsert() {
        return Long.MAX_VALUE;
    }

    @Override
    public long maxExtract() {
        return 0;
    }

    @Override
    public boolean allowsInsertion() {
        return true;
    }

    @Override
    public boolean allowsExtraction() {
        return false;
    }

    @Override
    public EnergySnapshot createSnapshot() {
        return new SimpleEnergySnapshot(this);
    }

    @Override
    public void setEnergy(long energy) {

    }

    @Override
    public void deserialize(CompoundTag nbt) {

    }

    @Override
    public CompoundTag serialize(CompoundTag nbt) {
        return new CompoundTag();
    }

    public void update(ElectricalNetwork network) {
        this.network = network;
    }

    @Override
    public void update(BlockEntity updatable) {

    }
}
