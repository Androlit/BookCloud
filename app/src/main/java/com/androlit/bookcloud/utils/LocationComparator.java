package com.androlit.bookcloud.utils;


import com.androlit.bookcloud.data.model.LocationBook;

import java.util.Comparator;

/**
 * Created by rubel on 8/5/2017.
 */

public class LocationComparator implements Comparator<LocationBook> {

    @Override
    public int compare(LocationBook loc1, LocationBook loc2) {
        if(loc1.getDistance() == loc2.getDistance()) return  0;
        return loc1.getDistance() < loc2.getDistance() ? -1 : 1;
    }
}
