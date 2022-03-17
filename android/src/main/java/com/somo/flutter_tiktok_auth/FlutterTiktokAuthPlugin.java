package com.somo.flutter_tiktok_auth;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;


/** FlutterTiktokAuthPlugin */
public class FlutterTiktokAuthPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {

  private static final String channelName = "flutter_tiktok_auth";
  private MethodChannel channel;
  private ActivityPluginBinding activityPluginBinding;
  private TikTokAuth ttAuth;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), channelName);
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {

    switch (call.method) {
      case "init":
        init(result, call.argument("clientKey"));
        break;
      case "authorize":
        authorize(result, call.argument("scope"), call.argument("state"));
        break;
      default:
        result.notImplemented();
    }
  }

  private void init(MethodChannel.Result result, @NonNull String clientKey) {
    ttAuth = new TikTokAuth(this.activityPluginBinding.getActivity(), clientKey);
    result.success(0);
  }

  private void authorize(MethodChannel.Result result, String scope, String state) {
    ttAuth.authorize(result, scope, state);
  }

  private void attachToActivity(ActivityPluginBinding activityPluginBinding) {
    this.activityPluginBinding = activityPluginBinding;
  }

  private void disposeActivity() {
    activityPluginBinding = null;
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
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
}
