package com.somo.flutter_tiktok_auth;

import android.util.Log;

import androidx.annotation.NonNull;

import com.bytedance.sdk.open.tiktok.TikTokOpenApiFactory;
import com.bytedance.sdk.open.tiktok.TikTokOpenConfig;
import com.bytedance.sdk.open.tiktok.api.TikTokOpenApi;
import com.bytedance.sdk.open.tiktok.authorize.model.Authorization;
import com.somo.flutter_tiktok_auth.tiktokapi.TikTokEntryActivity;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;


/** FlutterTiktokAuthPlugin */
public class FlutterTiktokAuthPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
  private MethodChannel channel;
  private ActivityPluginBinding activityPluginBinding;
  private Result result;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_tiktok_auth");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    this.result = result;
    TikTokEntryActivity.result = result;

    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if (call.method.equals("init")) {
      init(call.argument("clientKey"));
    } else if (call.method.equals("authorize")) {
      Log.d("SOMO authCode AUTHORIZE", "authorize");
      authorize(call.argument("scope"), call.argument("state"));
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  private void attachToActivity(ActivityPluginBinding activityPluginBinding) {
    this.activityPluginBinding = activityPluginBinding;
  }

  private void disposeActivity() {
    activityPluginBinding = null;
  }

  @Override
  public void onAttachedToActivity(ActivityPluginBinding activityPluginBinding) {
    attachToActivity(activityPluginBinding);
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    disposeActivity();
  }

  @Override
  public void onReattachedToActivityForConfigChanges(ActivityPluginBinding activityPluginBinding) {
    attachToActivity(activityPluginBinding);
  }

  @Override
  public void onDetachedFromActivity() {
    disposeActivity();
  }

  private  void init(@NonNull String clientKey) {
    TikTokOpenConfig tiktokOpenConfig = new TikTokOpenConfig(clientKey);
    TikTokOpenApiFactory.init(tiktokOpenConfig);
    result.success(0);
  }

  private void authorize(String scope, String state) {
    // 1. Create TiktokOpenApi
    TikTokOpenApi tiktokOpenApi= TikTokOpenApiFactory.create(this.activityPluginBinding.getActivity());

    // 2. Create Authorization.Request instance
    Authorization.Request request = new Authorization.Request();
    request.scope = scope;
    request.state = state;
    request.callerLocalEntry = "com.somo.flutter_tiktok_auth.tiktokapi.TikTokEntryActivity";

    // 3. Start Authorization
    tiktokOpenApi.authorize(request);
  }
}
