package com.somo.flutter_tiktok_auth;

import android.content.Context;

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
  private static final String clientKeyResName = "tiktok_client_key";
  private MethodChannel channel;
  private ActivityPluginBinding activityPluginBinding;
  private TikTokAuth ttAuth;
  private Context context;

  public TikTokAuth getTTAuth() {
    if (ttAuth == null) {
      String clientKey = getResourceFromContext(context, clientKeyResName);
      ttAuth = new TikTokAuth(this.activityPluginBinding.getActivity(), clientKey);
    }
    return ttAuth;
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), channelName);
    channel.setMethodCallHandler(this);
    context = flutterPluginBinding.getApplicationContext();
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    switch (call.method) {
      case "authorize":
        authorize(result, call.argument("scope"), call.argument("state"));
        break;
      default:
        result.notImplemented();
    }
  }

  private void authorize(MethodChannel.Result result, String scope, String state) {
    getTTAuth().authorize(result, scope, state);
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

  private static String getResourceFromContext(@NonNull Context context, String resName) {
    final int stringRes = context.getResources().getIdentifier(resName, "string", context.getPackageName());
    if (stringRes == 0) {
      throw new IllegalArgumentException(String.format("The 'R.string.%s' value it's not defined in your project's resources file.", resName));
    }
    return context.getString(stringRes);
  }
}
