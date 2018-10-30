package com.tck.daole.okhttppackage.builder;

import com.tck.daole.okhttppackage.OkHttpUtils;
import com.tck.daole.okhttppackage.request.OtherRequest;
import com.tck.daole.okhttppackage.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends com.tck.daole.okhttppackage.builder.GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
