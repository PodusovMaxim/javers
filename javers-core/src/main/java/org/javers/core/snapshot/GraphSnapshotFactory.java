package org.javers.core.snapshot;

import org.javers.common.collections.Multimap;
import org.javers.core.graph.ObjectGraphBuilder;
import org.javers.core.graph.ObjectNode;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.core.metamodel.object.GlobalCdoId;
import org.javers.core.metamodel.object.ValueObjectSetId;

/**
 * Decomposes given object graph into flat list of object Snapshots.
 * Resulting structure can be easily serialized and persisted.
 * <br/>
 * In fact, due to case of {@link ValueObjectSetId}, resulting structure
 * is a Multimap of GlobalCdoId -> Set&lt;CdoSnapshot&gt;
 *
 * @author bartosz walacik
 */
public class GraphSnapshotFactory {
    /**
     *
     * @param objectNode graph 'root', outcome from {@link ObjectGraphBuilder#buildGraph(Object)}
     */
    public Multimap<GlobalCdoId, CdoSnapshot> create(ObjectNode objectNode){
        return null;
    }
}
