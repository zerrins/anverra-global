import { ExpoConfig, ConfigContext } from 'expo/config';

export default (_: ConfigContext): ExpoConfig => ({
  name: 'mobile',
  slug: 'mobile',
  version: '1.0.0',
  orientation: 'portrait',
  icon: './assets/icon.png',
  userInterfaceStyle: 'light',
  scheme: 'anverraglobal',
  ios: {
    supportsTablet: true,
    bundleIdentifier: 'com.anverraglobal.mobile'
  },
  android: {
    package: 'com.anverraglobal.mobile',
    adaptiveIcon: {
      backgroundColor: '#E6F4FE',
      foregroundImage: './assets/android-icon-foreground.png',
      backgroundImage: './assets/android-icon-background.png',
      monochromeImage: './assets/android-icon-monochrome.png'
    },
    predictiveBackGestureEnabled: false
  },
  web: {
    favicon: './assets/favicon.png'
  },
  plugins: [
    [
      'react-native-auth0',
      {
        domain: process.env.EXPO_PUBLIC_AUTH0_DOMAIN || 'example.auth0.com'
      }
    ],
    'expo-secure-store'
  ]
});
