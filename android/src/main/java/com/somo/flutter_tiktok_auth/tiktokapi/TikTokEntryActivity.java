package com.somo.flutter_tiktok_auth.tiktokapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.bytedance.sdk.open.tiktok.TikTokOpenApiFactory;
import com.bytedance.sdk.open.tiktok.api.TikTokOpenApi;
import com.bytedance.sdk.open.tiktok.authorize.model.Authorization;
import com.bytedance.sdk.open.tiktok.common.handler.IApiEventHandler;
import com.bytedance.sdk.open.tiktok.common.model.BaseReq;
import com.bytedance.sdk.open.tiktok.common.model.BaseResp;

import io.flutter.plugin.common.MethodChannel;

public class TikTokEntryActivity extends Activity implements IApiEventHandler {
    public static MethodChannel.Result result;

    TikTokOpenApi ttOpenApi;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ttOpenApi = TikTokOpenApiFactory.create(this);
        ttOpenApi.handleIntent(getIntent(),this); // receive and parse callback
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp instanceof Authorization.Response)  {
            Authorization.Response response = (Authorization.Response) resp;

            Log.d("SOMO authCode", "" + response.authCode);
            Log.d("SOMO errorMsg", "" + response.errorMsg);
            Log.d("SOMO errorCode", "" + response.errorCode);

            if (response.errorCode == 0) {
                TikTokEntryActivity.result.success(response.authCode);
            } else {
                TikTokEntryActivity.result.error("" + response.errorCode, response.errorMsg, null);
            }
        }
        this.finish();
    }

    @Override
    public void onErrorIntent(@Nullable Intent intent) {
        TikTokEntryActivity.result.error("-1", "Error", null);
        this.finish();
    }
}