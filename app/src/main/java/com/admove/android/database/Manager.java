package com.admove.android.database;

import java.util.List;

/**
 * Interface for saving simple object in DB and get all from it.
 * <p/>
 * Created by Giorgi on 3/19/2016.
 */
public interface Manager<E> {

    void save(E object);

    List<E> getLast(int count);

    List<E> getAll();

}
