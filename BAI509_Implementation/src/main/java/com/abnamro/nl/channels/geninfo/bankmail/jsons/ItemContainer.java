package com.abnamro.nl.channels.geninfo.bankmail.jsons;

import java.util.List;

/**
 * Interface required because we store list of objects instead of the container object in a cache. We needs this
 * abstraction to reduce the duplicate code for each list we want to store in the cache.
 *
 * Note: if we would refactor and store the container objects in the cache, and return these from the cache instead of
 * the lists then this interface could be deleted.
 */
public interface ItemContainer<T> {
    List<T> getItems();
}
