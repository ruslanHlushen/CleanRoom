package com.ruslan_hlushen.cleanroom.utils;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Руслан on 14.01.2017.
 */
public class JsonUtil<T> {

    public ArrayList<T> jsonArrayToList(JSONArray jArray, String className) {

        ArrayList<T> listData = new ArrayList<>();

        if (jArray != null) {

            for (int i = 0; i < jArray.length(); i++) {

                try {

                    listData.add(jsonToObject(jArray.get(i), className));

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        return listData;
    }


    public T jsonToObject(Object jsonObject, String className) throws ClassNotFoundException {

        T t = null;
        Class<T> classT = (Class<T>) Class.forName(className);

        if (jsonObject != null) {

            t = new Gson().fromJson(jsonObject.toString(), classT);
        }

        return t;
    }
}
