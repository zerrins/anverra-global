import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { View, Text, StyleSheet, ActivityIndicator } from 'react-native';
import { useAuth } from '../auth/AuthContext';
import { useTheme } from '../theme/ThemeProvider';
import { LoginScreen } from '../screens/LoginScreen';
import { CustomersScreen } from '../screens/CustomersScreen';
import { CustomerDetailsScreen } from '../screens/CustomerDetailsScreen';
import { PoliciesScreen } from '../screens/PoliciesScreen';
import { PolicyDetailsScreen } from '../screens/PolicyDetailsScreen';

const PlaceholderScreen = ({ name }: { name: string }) => {
  const theme = useTheme();
  return (
    <View style={[styles.placeholderContainer, { backgroundColor: theme.colors.background }]}>
      <Text style={[styles.placeholderText, theme.typography.body, { color: theme.colors.textSecondary }]}>
        {name} (Temporary Foundation Screen)
      </Text>
    </View>
  );
};

const styles = StyleSheet.create({
  placeholderContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  placeholderText: {
    textAlign: 'center',
  }
});

const ProfileScreen = () => <PlaceholderScreen name="Profile" />;

export type RootStackParamList = {
  Login: undefined;
  MainTabs: undefined;
};

export type MainTabsParamList = {
  PoliciesStack: undefined;
  CustomersStack: undefined;
  Profile: undefined;
};

export type PoliciesStackParamList = {
  PoliciesList: undefined;
  PolicyDetails: { policyId: string };
};

export type CustomersStackParamList = {
  CustomersList: undefined;
  CustomerDetails: { customerId: string };
};

const Stack = createNativeStackNavigator<RootStackParamList>();
const Tab = createBottomTabNavigator<MainTabsParamList>();
const PoliciesStack = createNativeStackNavigator<PoliciesStackParamList>();
const CustomersStack = createNativeStackNavigator<CustomersStackParamList>();

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

function CustomersNavigator() {
  return (
    <CustomersStack.Navigator>
      <CustomersStack.Screen
        name="CustomersList"
        component={CustomersScreen}
        options={{ title: 'Customers' }}
      />
      <CustomersStack.Screen
        name="CustomerDetails"
        component={CustomerDetailsScreen}
        options={{ title: 'Customer Details' }}
      />
    </CustomersStack.Navigator>
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
        name="CustomersStack"
        component={CustomersNavigator}
        options={{ tabBarLabel: 'Customers' }}
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
  const theme = useTheme();

  if (isLoading) {
    return (
      <View style={[styles.placeholderContainer, { backgroundColor: theme.colors.background }]}>
        <ActivityIndicator size="large" color={theme.colors.primary} />
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
