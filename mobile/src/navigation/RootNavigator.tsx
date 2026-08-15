import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { useAuth } from '../auth/AuthContext';
import { LoginScreen } from '../screens/LoginScreen';
import { PoliciesScreen } from '../screens/PoliciesScreen';
import { PolicyDetailsScreen } from '../screens/PolicyDetailsScreen';
import { ProfileScreen } from '../screens/ProfileScreen';
import { ActivityIndicator, View } from 'react-native';

export type RootStackParamList = {
  Login: undefined;
  MainTabs: undefined;
};

export type MainTabsParamList = {
  PoliciesStack: undefined;
  Profile: undefined;
};

export type PoliciesStackParamList = {
  PoliciesList: undefined;
  PolicyDetails: { policyId: string };
};

const Stack = createNativeStackNavigator<RootStackParamList>();
const Tab = createBottomTabNavigator<MainTabsParamList>();
const PoliciesStack = createNativeStackNavigator<PoliciesStackParamList>();

function PoliciesNavigator() {
  return (
    <PoliciesStack.Navigator>
      <PoliciesStack.Screen 
        name="PoliciesList" 
        component={PoliciesScreen} 
        options={{ title: 'Policies' }} 
      />
      <PoliciesStack.Screen 
        name="PolicyDetails" 
        component={PolicyDetailsScreen} 
        options={{ title: 'Policy Details' }} 
      />
    </PoliciesStack.Navigator>
  );
}

function MainTabNavigator() {
  return (
    <Tab.Navigator screenOptions={{ headerShown: false }}>
      <Tab.Screen 
        name="PoliciesStack" 
        component={PoliciesNavigator} 
        options={{ tabBarLabel: 'Policies' }} 
      />
      <Tab.Screen 
        name="Profile" 
        component={ProfileScreen} 
        options={{ tabBarLabel: 'Profile', headerShown: true }} 
      />
    </Tab.Navigator>
  );
}

export function RootNavigator() {
  const { isAuthenticated, isLoading } = useAuth();

  if (isLoading) {
    return (
      <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
        <ActivityIndicator size="large" color="#0000ff" />
      </View>
    );
  }

  return (
    <NavigationContainer>
      <Stack.Navigator screenOptions={{ headerShown: false }}>
        {!isAuthenticated ? (
          <Stack.Screen name="Login" component={LoginScreen} />
        ) : (
          <Stack.Screen name="MainTabs" component={MainTabNavigator} />
        )}
      </Stack.Navigator>
    </NavigationContainer>
  );
}
