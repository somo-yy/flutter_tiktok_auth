import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'dart:async';

import 'package:flutter_tiktok_auth/flutter_tiktok_auth.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String? _authCode;
  String? _errorMsg;

  final FlutterTiktokAuth _flutterTiktokAuth =
      FlutterTiktokAuth(clientKey: "awoubuqrr5wlagef");

  Future<void> authorize() async {
    String? authCode;
    String? errorMsg;

    try {
      authCode = await _flutterTiktokAuth.authorize(
              scope: "user.info.basic,video.list") ??
          '';
      errorMsg = null;
    } on PlatformException catch (error) {
      errorMsg = '${error.message}(${error.code})';
    }

    if (!mounted) return;

    setState(() {
      _authCode = authCode;
      _errorMsg = errorMsg;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: [
              Text('authCode: $_authCode\n'),
              Text('errorMsg: $_errorMsg\n'),
              ElevatedButton(
                  onPressed: () {
                    authorize();
                  },
                  child: const Text("Authorize"))
            ],
          ),
        ),
      ),
    );
  }
}
