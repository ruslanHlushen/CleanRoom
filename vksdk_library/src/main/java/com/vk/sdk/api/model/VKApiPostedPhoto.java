//
//  Copyright (c) 2014 VK.com
//
//  Permission is hereby granted, free of charge, to any person obtaining a copy of
//  this software and associated documentation files (the "Software"), to deal in
//  the Software without restriction, including without limitation the rights to
//  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
//  the Software, and to permit persons to whom the Software is furnished to do so,
//  subject to the following conditions:
//
//  The above copyright notice and this permission notice shall be included in all
//  copies or substantial portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
//  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
//  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
//  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
//  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//

/**
 * PostdPhoto.java
 * vk-android-sdk
 * <p>
 * Created by Babichev Vitaly on 19.01.14.
 * Copyright (c) 2014 VK. All rights reserved.
 */
package com.vk.sdk.api.model;

import android.os.Parcel;

import org.json.JSONObject;

import static com.vk.sdk.api.model.VKAttachments.TYPE_POSTED_PHOTO;

/**
 * Subclass to directly uploaded wall photo.
 */
@SuppressWarnings("unused")
public class VKApiPostedPhoto extends VKApiPhoto {

    public static Creator<VKApiPostedPhoto> CREATOR = new Creator<VKApiPostedPhoto>() {
        public VKApiPostedPhoto createFromParcel(Parcel source) {

            return new VKApiPostedPhoto(source);
        }


        public VKApiPostedPhoto[] newArray(int size) {

            return new VKApiPostedPhoto[size];
        }
    };


    /**
     * Creates a PostedPhoto instance from Parcel.
     */
    public VKApiPostedPhoto(Parcel in) {

        super(in);
    }


    /**
     * Creates empty PostedPhoto instance.
     */
    public VKApiPostedPhoto() {

    }


    /**
     * Fills a PostedPhoto instance from JSONObject.
     */
    public VKApiPostedPhoto parse(JSONObject from) {

        super.parse(from);
        return this;
    }


    @Override
    public String getType() {

        return TYPE_POSTED_PHOTO;
    }
}
