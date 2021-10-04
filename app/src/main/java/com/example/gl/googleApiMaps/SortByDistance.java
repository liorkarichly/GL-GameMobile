package com.example.gl.googleApiMaps;

import java.util.Comparator;

public class SortByDistance implements Comparator<StoresPlaces>
{

    private SortByName m_SortByName = new SortByName();

    @Override
    public int compare(StoresPlaces aDouble, StoresPlaces bDouble)
    {

        if (aDouble.getDistance() == bDouble.getDistance()){

            return m_SortByName.compare(aDouble, bDouble);
        }

        return Double.compare(aDouble.getDistance(),bDouble.getDistance());

    }

}

class SortByName implements Comparator<StoresPlaces>
{
    @Override
    public int compare(StoresPlaces aDouble, StoresPlaces bDouble)
    {


        return aDouble.getName().compareTo(bDouble.getName());

    }

}
