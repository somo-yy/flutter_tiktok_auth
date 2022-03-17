package com.somo.flutter_tiktok_auth;

import android.app.Activity;

import com.bytedance.sdk.open.tiktok.TikTokOpenApiFactory;
import com.bytedance.sdk.open.tiktok.TikTokOpenConfig;
import com.bytedance.sdk.open.tiktok.api.TikTokOpenApi;
import com.bytedance.sdk.open.tiktok.authorize.model.Authorization;
import com.somo.flutter_tiktok_auth.tiktokapi.TikTokEntryActivity;

import java.util.HashMap;

import io.flutter.plugin.common.MethodChannel;

public class TikTokAuth {

  private TikTokOpenApi openApi;
  private static final String callerLocalEntry = "com.somo.flutter_tiktok_auth.tiktokapi.TikTokEntryActivity";

  TikTokAuth(Activity activity, String clientKey) {
    init(activity, clientKey);
  }

  private void init(Activity activity, String clientKey) {
    TikTokOpenConfig tiktokOpenConfig = new TikTokOpenConfig(clientKey);
    TikTokOpenApiFactory.init(tiktokOpenConfig);
    openApi = TikTokOpenApiFactory.create(activity);
  }

  public void authorize(MethodChannel.Result result, String scope, String state) {

    if (!TikTokEntryActivity.setPendingResult(result)) {
      return;
    }

    Authorization.Request request = new Authorization.Request();
    request.scope = scope;
    request.state = state;
    request.callerLocalEntry = callerLocalEntry;
    openApi.authorize(request);
  }

  public static HashMap<String, Object> authorizeResult(Authorization.Response resp) {
    return new HashMap<String, Object>() {
      {
        put("authCode", resp.authCode);
      }
    };
  }
}
