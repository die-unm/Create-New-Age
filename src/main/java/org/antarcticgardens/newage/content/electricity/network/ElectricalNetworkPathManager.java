package org.antarcticgardens.newage.content.electricity.network;

import org.antarcticgardens.newage.content.electricity.connector.ElectricalConnectorBlockEntity;

import java.util.*;

public class ElectricalNetworkPathManager {
    private final NetworkPathConductivityContext context = new NetworkPathConductivityContext();

    protected void addConnection(ElectricalConnectorBlockEntity node, ElectricalConnectorBlockEntity node1) {
        context.addConnection(node, node1);
    }

    protected NetworkPath findConductiblePath(ElectricalConnectorBlockEntity a, ElectricalConnectorBlockEntity b) {
        List<ElectricalConnectorBlockEntity> visited = new ArrayList<>();
        Queue<QueueElement> queue = new LinkedList<>();
        queue.add(new QueueElement(a, null));

        while (!queue.isEmpty()) {
            var element = queue.poll();

            if (element.connector.equals(b)) {
                NetworkPath path = unwrapConductiblePath(element);
                if (path != null && context.calculatePathConductivity(path) > 0)
                    return path;
            }

            for (ElectricalConnectorBlockEntity connector : element.connector.getConnectors().keySet()) {
                if (!visited.contains(connector)) {
                    visited.add(connector);
                    queue.add(new QueueElement(connector, element));
                }
            }
        }

        return null;
    }

    private NetworkPath unwrapConductiblePath(QueueElement element) {
        NetworkPath path = new NetworkPath();

        while (element != null) {
            if (path.getLength() != 0 && context.getConnectionConductivity(new NetworkPathKey<>(element.connector, path.getFirstNode())) <= 0)
                return null;

            path.addNodeToBeginning(element.connector);
            element = element.parent;
        }

        if (path.getLength() < 2)
            return null;

        return path;
    }

    protected NetworkPathConductivityContext getConductivityContext() {
        return context;
    }

    protected void tick() {
        context.updateConductivity();
    }

    private record QueueElement(ElectricalConnectorBlockEntity connector, QueueElement parent) { }
}
