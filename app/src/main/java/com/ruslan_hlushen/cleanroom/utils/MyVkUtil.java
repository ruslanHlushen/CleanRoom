package com.ruslan_hlushen.cleanroom.utils;

import android.content.Context;
import android.widget.Toast;

import com.ruslan_hlushen.cleanroom.R;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Руслан on 15.01.2017.
 */
public class MyVkUtil {


    private VkSuccessListener vkSuccessListener;
    private VkErrorListener vkErrorListener;
    private Context context;
    private String userId;
    private Integer hostelId;


    public MyVkUtil(Context context,
                    String userId,
                    Integer hostelId,
                    VkSuccessListener vkSuccessListener,
                    VkErrorListener vkErrorListener) {

        this.context = context;
        this.userId = userId;
        this.hostelId = hostelId;
        this.vkSuccessListener = vkSuccessListener;
        this.vkErrorListener = vkErrorListener;
    }


    public void checkUserGroups() {

        final VKRequest request = VKApi.groups().get(VKParameters.from(VKApiConst.USER_ID, userId));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {

                try {
                    JSONObject jsonObjectResponse = response.json.getJSONObject("response");
                    JSONArray jsonArrayIDs = jsonObjectResponse.getJSONArray("items");
                    List<Integer> listInt = new JsonUtil<Integer>().jsonArrayToList(jsonArrayIDs, Integer.class.getName());

                    if (listInt.contains(hostelId)) {

                        getUserData();

                    } else {

                        vkErrorListener.onError();
                        Toast.makeText(context, context.getString(R.string.you_not_enter_to_oficial_group), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onError(VKError error) {

                vkErrorListener.onError();
                Toast.makeText(context, context.getString(R.string.registration_impossible), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void getUserData() {

        VKRequest request = VKApi.users().get();
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {

                VKList<VKApiUserFull> list = (VKList<VKApiUserFull>) response.parsedModel;
                VKApiUserFull userFull = list.get(0);

                vkSuccessListener.onSuccess(userFull.first_name, userFull.last_name, userId, hostelId);
            }


            @Override
            public void onError(VKError error) {

                vkErrorListener.onError();
                Toast.makeText(context, context.getString(R.string.connection_error), Toast.LENGTH_LONG).show();
            }
        });
    }


    public interface VkSuccessListener {

        public void onSuccess(String first_name, String last_name, String userId, Integer hostelId);
    }


    public interface VkErrorListener {

        public void onError();
    }
}
