import 'dart:async';

import 'package:flutter/services.dart';

class FlutterTiktokAuth {
  static const MethodChannel _channel = MethodChannel('flutter_tiktok_auth');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  final String clientKey;
  bool _hasInit = false;

  FlutterTiktokAuth({required this.clientKey});

  Future<void> _init() async {
    if (_hasInit) return;
    await _channel.invokeMethod('init', {'clientKey': clientKey});
    _hasInit = true;
  }

  Future<String?> authorize({required String scope, String state = ""}) async {
    await _init();
    return await _channel
        .invokeMethod('authorize', {"scope": scope, "state": state});
  }
}
