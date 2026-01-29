package org.springinfra.assets;

/**
 * This interface should be implemented by all enums that want to be able to be persisted in the database.
 * Usually, enums are persisted either 2 common ways:
 * 1. Persisting based on their names (which obviously is not normalized)
 * 2. Persisting based on their order (which can cause an issue if code is formatted and the order of enums is changed)
 * <p>
 * Instead of the above ways, this interface makes sure that each enum that wants to be persisted to the database has an identity code,
 * and would be used as value for inserting in the database. In this way, it provides a safe way for persisting enums which is completely safe against
 * any format or renaming operation.
 */
public interface PersistableEnum<E extends Enum<E> & PersistableEnum<E>> {

    Integer getCode();

}
