import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ActivityIndicator } from 'react-native';
import { useAuth } from '../auth/AuthContext';
import { useTheme, Theme } from '../theme/ThemeProvider';

export function LoginScreen() {
  const { login, isLoading, error } = useAuth();
  const theme = useTheme();
  const styles = createStyles(theme);

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
          <ActivityIndicator color={theme.colors.background} />
        ) : (
          <Text style={styles.buttonText}>Sign In</Text>
        )}
      </TouchableOpacity>
    </View>
  );
}

const createStyles = (theme: Theme) => StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: theme.spacing.xl,
    backgroundColor: theme.colors.background,
  },
  title: {
    ...theme.typography.h1,
    marginBottom: theme.spacing.sm,
    color: theme.colors.text,
  },
  subtitle: {
    ...theme.typography.body,
    color: theme.colors.textSecondary,
    marginBottom: theme.spacing.xl,
    textAlign: 'center',
  },
  errorText: {
    ...theme.typography.body,
    color: theme.colors.error,
    marginBottom: theme.spacing.lg,
    textAlign: 'center',
  },
  button: {
    backgroundColor: theme.colors.primary,
    paddingVertical: theme.spacing.md,
    paddingHorizontal: theme.spacing.xl,
    borderRadius: theme.borderRadius.md,
    width: '100%',
    alignItems: 'center',
  },
  buttonText: {
    ...theme.typography.body,
    color: theme.colors.background,
    fontWeight: '600',
  },
});
