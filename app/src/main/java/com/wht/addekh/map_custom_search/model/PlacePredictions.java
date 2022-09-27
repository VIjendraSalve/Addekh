package com.wht.addekh.map_custom_search.model;

import java.util.ArrayList;

/**
 * Created by Kyra on 1/11/2016.
 */
public class PlacePredictions {

    public static ArrayList<PlaceAutoComplete> predictions=new ArrayList<>();

    public ArrayList<PlaceAutoComplete> getPlaces() {
        return predictions;
    }

    public void setPlaces(ArrayList<PlaceAutoComplete> places) {
        this.predictions = places;
    }


}
