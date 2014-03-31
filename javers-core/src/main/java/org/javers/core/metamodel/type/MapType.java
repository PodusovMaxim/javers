package org.javers.core.metamodel.type;

import org.javers.common.collections.EnumerableFunction;
import org.javers.common.collections.Lists;
import org.javers.common.exception.exceptions.JaversException;
import org.javers.common.validation.Validate;
import org.javers.core.metamodel.object.MapOwnerContext;
import org.javers.core.metamodel.object.SimpleOwnerContext;

import java.lang.reflect.Type;
import java.util.*;

import static org.javers.common.exception.exceptions.JaversExceptionCode.GENERIC_TYPE_NOT_PARAMETRIZED;

/**
 * Map where both keys and values
 * should be of {@link PrimitiveType} or {@link ValueType}.
 * <p/>
 *
 * Javers doesn't support complex maps with ValueObjects or Entities
 *
 * @author bartosz walacik
 */
public class MapType extends EnumerableType {
    private transient List<Class> elementTypes;

    public MapType(Type baseJavaType) {
        super(baseJavaType);

        if (getActualClassTypeArguments().size() == 2) {
            elementTypes = Lists.immutableListOf(getActualClassTypeArguments().get(0), getActualClassTypeArguments().get(1));
        } else {
            elementTypes = Collections.EMPTY_LIST;
        }
    }

    @Override
    public boolean isFullyParametrized() {
        return elementTypes.size() == 2;
    }

    @Override
    public Object map(Object sourceMap_, EnumerableFunction mapFunction, SimpleOwnerContext owner) {
        Validate.argumentsAreNotNull(sourceMap_, mapFunction);
        Map<Object, Object> sourceMap = (Map) sourceMap_;
        Map<Object, Object> targetMap = new HashMap(sourceMap.size());
        MapOwnerContext mapOwnerContext = new MapOwnerContext(owner);

        for (Map.Entry<?, ?> entry : sourceMap.entrySet()) {
            //key
            mapOwnerContext.switchToKey();
            Object mappedKey = mapFunction.apply(entry.getKey(), mapOwnerContext);

            //value
            mapOwnerContext.switchToValue(mappedKey.toString());
            Object mappedValue = mapFunction.apply(entry.getValue(), mapOwnerContext);

            targetMap.put(mappedKey, mappedValue);
        }

        return targetMap;
    }

    /**
     * If both Key and Value type arguments are actual Classes,
     * returns List with key Class and value Class.
     * Otherwise returns empty List
     */
    @Override
    public List<Class> getElementTypes() {
        return elementTypes;
    }

    /**
     * never returns null
     * @throws JaversException GENERIC_TYPE_NOT_PARAMETRIZED
     */
    public Class getKeyClass() {
        if (isFullyParametrized()) {
            return elementTypes.get(0);
        }
        throw new JaversException(GENERIC_TYPE_NOT_PARAMETRIZED,getBaseJavaType().toString());
    }

    /**
     * never returns null
     * @throws JaversException GENERIC_TYPE_NOT_PARAMETRIZED
     */
    public Class getValueClass() {
        if (isFullyParametrized()) {
            return elementTypes.get(1);
        }
        throw new JaversException(GENERIC_TYPE_NOT_PARAMETRIZED, getBaseJavaType().toString());
    }
}