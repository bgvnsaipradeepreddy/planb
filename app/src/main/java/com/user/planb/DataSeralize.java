package com.user.planb;

import com.user.planb.model.DataItems;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by user on 4/10/2018.
 */

public class DataSeralize implements Serializable {

    static ArrayList<DataItems> dataItems;
    public  static void  setComments(ArrayList<DataItems> comments){
        dataItems = comments;
    }
    public static ArrayList<DataItems> getComments(){
        return dataItems;
    }
}
