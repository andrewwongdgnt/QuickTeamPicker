package com.dgnt.pro.quickTeamPicker.util;

import com.dgnt.pro.quickTeamPicker.holder.IKeyable;
import com.dgnt.pro.quickTeamPicker.holder.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 10/7/2015.
 */
public class KeyableUtil {

    public static <K extends IKeyable> boolean addToList(List<K> list, K element) {
        for (IKeyable e : list) {
            if (e.getKey().equals(element.getKey()))
                return false;
        }
        list.add(element);
        return true;

    }

    public static <K extends IKeyable> boolean removeFromList(List<K> list, K element) {
        for (int i = list.size()-1; i >=0; i--) {
            if (list.get(i).equals(element.getKey())) {
                list.remove(i);
                return true;
            }
        }
        return false;
    }
}
