package org.antarcticgardens.newage.content.electricity.network;

import earth.terrarium.botarium.common.energy.base.EnergyContainer;
import earth.terrarium.botarium.common.energy.base.EnergySnapshot;
import earth.terrarium.botarium.util.Updatable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.antarcticgardens.newage.content.electricity.connector.ElectricalConnectorBlockEntity;

public class NetworkEnergyContainer implements EnergyContainer, Updatable<BlockEntity> {
    private final ElectricalConnectorBlockEntity connector;
    private ElectricalNetwork network;

    public NetworkEnergyContainer(ElectricalConnectorBlockEntity connector, ElectricalNetwork network) {
        this.connector = connector;
        this.network = network;
    }
    
    public ElectricalNetwork getNetwork() {
        return network;
    }

    @Override
    public long insertEnergy(long maxAmount, boolean simulate) {
        return network.insert(connector, maxAmount, simulate);
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
        return new NetworkSnapshot(network);
    }

    @Override
    public void setEnergy(long energy) {

    }

    @Override
    public void clearContent() {

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
    public void update(BlockEntity be) {
        be.setChanged();
        be.getLevel().sendBlockUpdated(be.getBlockPos(), be.getBlockState(), be.getBlockState(), Block.UPDATE_ALL);
    }
}
