import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_tiktok_auth/models/authorize_result.dart';
export "models/authorize_result.dart";

class FlutterTiktokAuth {
  static const MethodChannel _channel = MethodChannel('flutter_tiktok_auth');

  Future<AuthorizeResult> authorize(
      {required String scope, String state = ""}) async {
    try {
      final result = await _channel
          .invokeMethod('authorize', {"scope": scope, "state": state});
      return AuthorizeSucceeded(authCode: result["authCode"]);
    } on PlatformException catch (e) {
      switch (e.code) {
        case "CANCELED":
          return AuthorizeCanceled();
        case "OPERATION_IN_PROGRESS":
          return AuthorizePending();
        case "FAILED":
          return AuthorizeFailed(code: e.code, message: e.message);
        default:
          throw Exception("Unknown Error");
      }
    }
  }
}
