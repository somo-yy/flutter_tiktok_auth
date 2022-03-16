#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html.
# Run `pod lib lint flutter_tiktok_auth.podspec` to validate before publishing.
#
Pod::Spec.new do |s|
  s.name             = 'flutter_tiktok_auth'
  s.version          = '0.0.1'
  s.summary          = 'A new flutter plugin project.'
  s.description      = <<-DESC
A new flutter plugin project.
                       DESC
  s.homepage         = 'https://somo.me'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'SOMO' => 'yy@somo.me' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'
  s.dependency 'Flutter'

  s.dependency 'TikTokOpenSDK', '~> 5.0.0'
  s.platform = :ios, '9.3'

  s.static_framework = true

  # Flutter.framework does not contain a i386 slice.
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES', 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'i386' }
  s.swift_version = '5.0'
end
