package com.ruslan_hlushen.cleanroom.utils;

import com.ruslan_hlushen.cleanroom.models.Hostel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ruslan on 25.01.2017.
 */

public class GetDataUtil {

    public static List<String> getStringListFromHostelsList(List<Hostel> hostelsList) {

        List<String> stringList = new ArrayList<>();

        if (hostelsList != null) {

            for (int i = 0; i < hostelsList.size(); i++) {

                stringList.add(hostelsList.get(i).getHostelName());
            }
        }

        return stringList;
    }


    public static Integer getIdFromHostelsListByName(List<Hostel> hostelsList, String hostelName) {

        Integer id = null;

        if (hostelsList != null) {

            for (int i = 0; i < hostelsList.size(); i++) {

                if (hostelName.equals(hostelsList.get(i).getHostelName())) {

                    id = hostelsList.get(i).getHostelVkId();
                }
            }
        }

        return id;
    }
}
