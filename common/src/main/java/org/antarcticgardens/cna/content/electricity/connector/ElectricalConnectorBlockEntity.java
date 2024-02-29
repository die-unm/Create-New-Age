package org.antarcticgardens.cna.content.electricity.connector;

import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import org.antarcticgardens.cna.CNABlockEntityTypes;
import org.antarcticgardens.cna.content.electricity.network.ElectricalNetwork;
import org.antarcticgardens.cna.content.electricity.network.NetworkEnergyStorage;
import org.antarcticgardens.cna.content.electricity.wire.WireType;
import org.antarcticgardens.esl.energy.EnergyStorage;

import java.util.*;

public class ElectricalConnectorBlockEntity extends BlockEntity implements IHaveGoggleInformation {
    private final Map<ElectricalConnectorBlockEntity, WireType> connectors = new HashMap<>();
    private final Map<BlockPos, WireType> connectorPositions = new HashMap<>();

    private ElectricalNetwork network;
    private final NetworkEnergyStorage storage;

    private boolean connectionsInitialized = false;
    boolean needsInstanceUpdate = true;

    public ElectricalConnectorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        storage = new NetworkEnergyStorage(this, null);
        EnergyStorage.registerForBlockEntity((blockEntity, direction) -> blockEntity.storage, CNABlockEntityTypes.ELECTRICAL_CONNECTOR.get());
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        ListTag list = new ListTag();

        for (Map.Entry<BlockPos, WireType> e : connectorPositions.entrySet()) {
            CompoundTag compound = new CompoundTag();
            compound.put("position", NBTHelper.writeVec3i(e.getKey()));
            compound.put("wire", StringTag.valueOf(e.getValue().name()));

            list.add(compound);
        }

        nbt.put("connections", list);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        ListTag list = nbt.getList("connections", Tag.TAG_COMPOUND);
        connectorPositions.clear();

        for (Tag listTag : list.toArray(new Tag[0])) {
            if (listTag instanceof CompoundTag ct && ct.contains("position") && ct.contains("wire")) {
                BlockPos pos = new BlockPos(NBTHelper.readVec3i((ListTag) ct.get("position")));
                WireType wire = WireType.valueOf(ct.getString("wire"));

                connectorPositions.put(pos, wire);
            }
        }

        needsInstanceUpdate = true;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    public Map<BlockPos, WireType> getConnectorPositions() {
        return Collections.unmodifiableMap(connectorPositions);
    }

    public BlockPos getSupportingBlockPos() {
        return getBlockPos().relative(getBlockState().getValue(BlockStateProperties.FACING).getOpposite());
    }

    protected void serverTick() {
        if (network == null) 
            setNetwork(new ElectricalNetwork(this));
        
        if (!connectionsInitialized) {
            updateConnections();
            connectionsInitialized = true;
        }
    }

    #if !CNA_FABRIC
    @Override
    public AABB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }
    #endif

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        Lang.translate("tooltip.create_new_age.connector_info")
                .style(ChatFormatting.WHITE).forGoggles(tooltip);
        
        Lang.translate("tooltip.create_new_age.mode")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
        
        ElectricalConnectorMode mode = getBlockState().getValue(ElectricalConnectorBlock.MODE);
        Lang.translate("tooltip.create_new_age.connector_mode." + mode.getSerializedName())
                .style(ChatFormatting.AQUA)
                .forGoggles(tooltip, 1);
        
        return true;
    }

    protected void neighborChanged() {
        if (network != null) {
            network.updateConsumersAndSources();
        }
    }

    private void updateConnections() {
        for (Map.Entry<BlockPos, WireType> e : connectorPositions.entrySet()) {
            if (getLevel().getBlockEntity(e.getKey()) instanceof ElectricalConnectorBlockEntity connector)
                connect(connector, e.getValue());
        }

        needsInstanceUpdate = true;
    }

    protected void remove(Level level) {
        if (!level.isClientSide())
            network.destroy();

        for (Map.Entry<ElectricalConnectorBlockEntity, WireType> e : connectors.entrySet()) {
            e.getKey().disconnect(this);
            e.getKey().updateConnections();

            e.getKey().setChanged();

            if (level instanceof ServerLevel serverLevel) {
                serverLevel.getChunkSource().blockChanged(e.getKey().getBlockPos());

                Containers.dropContents(level, getBlockPos(), NonNullList.of(ItemStack.EMPTY, e.getValue().getDroppedItem()));
            }
        }
    }

    public void connect(ElectricalConnectorBlockEntity entity, WireType wireType) {
        if (!connectors.containsKey(entity)) 
            connectors.put(entity, wireType);

        entity.connectWithoutNetworking(this, wireType);

        if (!connectorPositions.containsKey(entity.getBlockPos()))
            connectorPositions.put(entity.getBlockPos(), wireType);

        entity.setChanged();
        setChanged();

        if (level instanceof ServerLevel serverLevel) {
            network.addNode(entity);
            
            serverLevel.getChunkSource().blockChanged(entity.getBlockPos());
            serverLevel.getChunkSource().blockChanged(getBlockPos());
        }
    }
    
    private void connectWithoutNetworking(ElectricalConnectorBlockEntity entity, WireType wireType) {
        if (!connectors.containsKey(entity)) 
            connectors.put(entity, wireType);

        if (!connectorPositions.containsKey(entity.getBlockPos()))
            connectorPositions.put(entity.getBlockPos(), wireType);
    }

    public boolean isConnected(BlockPos pos) {
        return connectorPositions.containsKey(pos);
    }

    public void disconnect(ElectricalConnectorBlockEntity entity) {
        connectors.remove(entity);
        connectorPositions.remove(entity.getBlockPos());
    }

    public Map<ElectricalConnectorBlockEntity, WireType> getConnectedConnectors() {
        return Collections.unmodifiableMap(connectors);
    }

    public void setNetwork(ElectricalNetwork network) {
        this.network = network;
        storage.setNetwork(network);
    }

    public ElectricalNetwork getNetwork() {
        return network;
    }
}
