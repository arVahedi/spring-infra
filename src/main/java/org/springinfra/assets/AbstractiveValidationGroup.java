package org.springinfra.assets;

/**
 * An implementation of AbstractiveValidationGroup actually is a composite validation group which means nothing as an
 * independent group, it just consists of one or more other simple validation groups.
 * By calling {@link #getGroups(Object)} method, we can get all its effective validation groups for validating.
 * Obviously, this method provides a way for us can make decisions about effective validation groups at run-time. For instance:
 *
 * @see ValidationGroups
 */

@FunctionalInterface
public interface AbstractiveValidationGroup {

    Class<?>[] getGroups(Object object);
}
