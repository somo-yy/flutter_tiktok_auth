package com.somo.flutter_tiktok_auth.tiktokapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bytedance.sdk.open.tiktok.TikTokOpenApiFactory;
import com.bytedance.sdk.open.tiktok.api.TikTokOpenApi;
import com.bytedance.sdk.open.tiktok.authorize.model.Authorization;
import com.bytedance.sdk.open.tiktok.common.handler.IApiEventHandler;
import com.bytedance.sdk.open.tiktok.common.model.BaseReq;
import com.bytedance.sdk.open.tiktok.common.model.BaseResp;
import com.somo.flutter_tiktok_auth.TikTokAuth;

import io.flutter.plugin.common.MethodChannel;

public class TikTokEntryActivity extends Activity implements IApiEventHandler {
    public static MethodChannel.Result pendingResult;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TikTokOpenApi openApi = TikTokOpenApiFactory.create(this);
        openApi.handleIntent(getIntent(),this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp instanceof Authorization.Response)  {
            Authorization.Response response = (Authorization.Response) resp;
            if (response.isSuccess()) {
                finishWithResult(TikTokAuth.authorizeResult(response));
            } else if (response.isCancel()) {
                finishWithError("CANCELED", "Canceled");
            } else {
                finishWithError("FAILED", response.errorCode + ":" + response.errorMsg);
            }
        } else {
            finishWithError("FAILED", "Unknown response");
        }
    }

    @Override
    public void onErrorIntent(@Nullable Intent intent) {
        finishWithError("FAILED", "Intent error");
    }

    public static boolean setPendingResult(MethodChannel.Result result) {
        if (pendingResult != null) {
            result.error("OPERATION_IN_PROGRESS", "Operation in progress", null);
            return false;
        }
        pendingResult = result;
        return true;
    }

    private void finishWithError(String errorCode, String errorMessage) {
        if (pendingResult != null) {
            pendingResult.error(errorCode, errorMessage, null);
            pendingResult = null;
        }
        this.finish();
    }

    private void finishWithResult(Object result) {
        if (pendingResult != null) {
            pendingResult.success(result);
            pendingResult = null;
        }
        this.finish();
    }
}