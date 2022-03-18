import Flutter
import UIKit
import TikTokOpenSDK

public class SwiftFlutterTiktokAuthPlugin: NSObject, FlutterPlugin {
    public static func register(with registrar: FlutterPluginRegistrar) {
        TikTokOpenSDKApplicationDelegate.initialize()
        let channel = FlutterMethodChannel(name: "flutter_tiktok_auth", binaryMessenger: registrar.messenger())
        let instance = SwiftFlutterTiktokAuthPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
        registrar.addApplicationDelegate(instance)
    }

    private var mainWindow: UIWindow? {
        if let applicationWindow = UIApplication.shared.delegate?.window ?? nil {
            return applicationWindow
        }

        if #available(iOS 13.0, *) {
            if let scene = UIApplication.shared.connectedScenes.first(where: { $0.session.role == .windowApplication }),
               let sceneDelegate = scene.delegate as? UIWindowSceneDelegate,
               let window = sceneDelegate.window as? UIWindow  {
                return window
            }
        }

        return nil
    }

    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
      let args = call.arguments as? [String: Any]
      
      switch call.method {
      case "authorize":
          self.authorize(result: result, scope: args?["scope"] as! String, state: args?["state"] as! String)
          break
      default:
          result(FlutterMethodNotImplemented)
          return
      }
    }
    
    private func authorize(result: @escaping FlutterResult, scope: String, state: String) {
        let request = TikTokOpenSDKAuthRequest()
        request.permissions = NSOrderedSet(array:scope.components(separatedBy: ","))
        request.state = state
        
        let viewController: UIViewController = (mainWindow?.rootViewController)!
        
        print(">>>>>>>>>>>>> 1")
        
        request.send(viewController, completion: { resp -> Void in
            
            print(">>>>>>>>>>>>> 2: " + String(resp.errCode.rawValue) )
            
            if (resp.errCode == TikTokOpenSDKErrorCode.success) {
                print(">>>>>>>>>>>>> 3: " + (resp.code ?? "None") )
                result(["authCode": resp.code]);
            } else if ( resp.errCode == TikTokOpenSDKErrorCode.errorCodeUserCanceled ) {
                print(">>>>>>>>>>>>> 4: " + "Canceled" )
                result(FlutterError(code: "CANCELED",
                                    message: "Canceled",
                                    details: nil))
            } else {
                print(">>>>>>>>>>>>> 4: " + "Failed" + (resp.errString ?? "") )
                result(FlutterError(code: "FAILED",
                                    message: "\(resp.errCode):\(resp.errString ?? "")",
                                    details: nil))
            }
        })
    }
    
    public func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [AnyHashable: Any]) -> Bool {
        
        print(">>>>>>>>>>>>> 5" )
        var options = [UIApplication.LaunchOptionsKey: Any]()
        for (k, value) in launchOptions {
            let key = k as! UIApplication.LaunchOptionsKey
            options[key] = value
        }
        TikTokOpenSDKApplicationDelegate.sharedInstance().application(application, didFinishLaunchingWithOptions: options)
        return true
    }

    public func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {

        print(">>>>>>>>>>>>> 6.1 options")
        print(options)
        print("<<<<<<<<<<<<< 6.1 options")
        
        print(">>>>>>>>>>>>> 6.2 sourceApplication")
        print(UIApplication.OpenURLOptionsKey.sourceApplication)
        print("<<<<<<<<<<<<< 6.2 sourceApplication")
        
        print(">>>>>>>>>>>>> 6.3 annotation")
        print(UIApplication.OpenURLOptionsKey.annotation)
        print("<<<<<<<<<<<<< 6.3 annotation")
        
        guard let sourceApplication = options[UIApplication.OpenURLOptionsKey.sourceApplication] as? String,
              let annotation = options[UIApplication.OpenURLOptionsKey.annotation] else {
            print(">>>>>>>>>>>>> 7" )
            return false
        }
        
        print(">>>>>>>>>>>>> 8" )

        if TikTokOpenSDKApplicationDelegate.sharedInstance().application(app, open: url, sourceApplication: sourceApplication, annotation: annotation) {
            print(">>>>>>>>>>>>> 9" )
            return true
        }
        print(">>>>>>>>>>>>> 10" )
        return false
    }

    public func application(_ application: UIApplication, open url: URL, sourceApplication: String, annotation: Any) -> Bool {
        print(">>>>>>>>>>>>> 11" )
        if TikTokOpenSDKApplicationDelegate.sharedInstance().application(application, open: url, sourceApplication: sourceApplication, annotation: annotation) {
            print(">>>>>>>>>>>>> 12" )
            return true
        }
        print(">>>>>>>>>>>>> 13" )
        return false
    }

    public func application(_ application: UIApplication, handleOpen url: URL) -> Bool {
        print(">>>>>>>>>>>>> 14" )
        if TikTokOpenSDKApplicationDelegate.sharedInstance().application(application, open: url, sourceApplication: nil, annotation: "") {
            print(">>>>>>>>>>>>> 15" )
            return true
        }
        print(">>>>>>>>>>>>> 16" )
        return false
    }
}
