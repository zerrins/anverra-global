import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ActivityIndicator } from 'react-native';
import { useAuth } from '../auth/AuthContext';

export function LoginScreen() {
  const { login, isLoading, error } = useAuth();

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Anverra Global</Text>
      <Text style={styles.subtitle}>Sign in to manage your policies</Text>
      
      {error && (
        <Text style={styles.errorText}>
          Authentication failed. Please try again.
        </Text>
      )}

      <TouchableOpacity 
        style={styles.button} 
        onPress={login} 
        disabled={isLoading}
        accessibilityRole="button"
        accessibilityLabel="Sign in"
      >
        {isLoading ? (
          <ActivityIndicator color="#FFFFFF" />
        ) : (
          <Text style={styles.buttonText}>Sign In</Text>
        )}
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 24,
    backgroundColor: '#FFFFFF',
  },
  title: {
    fontSize: 28,
    fontWeight: 'bold',
    marginBottom: 8,
    color: '#111827',
  },
  subtitle: {
    fontSize: 16,
    color: '#6B7280',
    marginBottom: 48,
    textAlign: 'center',
  },
  errorText: {
    color: '#DC2626',
    marginBottom: 24,
    textAlign: 'center',
  },
  button: {
    backgroundColor: '#2563EB',
    paddingVertical: 14,
    paddingHorizontal: 32,
    borderRadius: 8,
    width: '100%',
    alignItems: 'center',
  },
  buttonText: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: '600',
  },
});
