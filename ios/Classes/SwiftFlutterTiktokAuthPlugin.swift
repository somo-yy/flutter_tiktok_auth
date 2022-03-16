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
      switch call.method {
      case "init":
          self.initSDK(result: result)
          break
      case "authorize":
          self.authorize(result: result)
          break
      default:
          result(FlutterMethodNotImplemented)
          return
      }
  }
    
    public func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [AnyHashable: Any]) -> Bool {
        var options = [UIApplication.LaunchOptionsKey: Any]()
        for (k, value) in launchOptions {
            let key = k as! UIApplication.LaunchOptionsKey
            options[key] = value
        }
        TikTokOpenSDKApplicationDelegate.sharedInstance().application(application, didFinishLaunchingWithOptions: options)
        return true
    }
    
    public func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {

        guard let sourceApplication = options[UIApplication.OpenURLOptionsKey.sourceApplication] as? String,
              let annotation = options[UIApplication.OpenURLOptionsKey.annotation] else {
            return false
        }

        if TikTokOpenSDKApplicationDelegate.sharedInstance().application(app, open: url, sourceApplication: sourceApplication, annotation: annotation) {
            return true
        }
        return false
    }

    public func application(_ application: UIApplication, open url: URL, sourceApplication: String, annotation: Any) -> Bool {
        if TikTokOpenSDKApplicationDelegate.sharedInstance().application(application, open: url, sourceApplication: sourceApplication, annotation: annotation) {
            return true
        }
        return false
    }

    public func application(_ application: UIApplication, handleOpen url: URL) -> Bool {
        if TikTokOpenSDKApplicationDelegate.sharedInstance().application(application, open: url, sourceApplication: nil, annotation: "") {
            return true
        }
        return false
    }
    
    private func initSDK(result: @escaping FlutterResult) {
        result(0)
    }
    
    private func authorize(result: @escaping FlutterResult) {
        /* STEP 1 */
        let scopes = ["user.info.basic,video.list"]
        let scopesSet = NSOrderedSet(array:scopes)
        let request = TikTokOpenSDKAuthRequest()
        request.permissions = scopesSet
        
        // let viewController: UIViewController = (UIApplication.shared.delegate?.window??.rootViewController)!;
        let viewController: UIViewController = (mainWindow?.rootViewController)!
        
        /* STEP 2 */
        request.send(viewController, completion: { resp -> Void in
            guard resp.errCode == TikTokOpenSDKErrorCode.success else {
                result(FlutterError(code: "\(resp.errCode)",
                                    message: resp.errString,
                                    details: nil))
                return
            }
            result(resp.code);
        })
    }
}
