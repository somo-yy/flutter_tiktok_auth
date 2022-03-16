#import "FlutterTiktokAuthPlugin.h"
#if __has_include(<flutter_tiktok_auth/flutter_tiktok_auth-Swift.h>)
#import <flutter_tiktok_auth/flutter_tiktok_auth-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutter_tiktok_auth-Swift.h"
#endif

@implementation FlutterTiktokAuthPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterTiktokAuthPlugin registerWithRegistrar:registrar];
}
@end
