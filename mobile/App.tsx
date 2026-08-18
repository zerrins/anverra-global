import React from 'react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { Auth0Provider } from 'react-native-auth0';
import { RootNavigator } from './src/navigation/RootNavigator';
import { ErrorBoundary } from './src/components/ErrorBoundary';
import { ThemeProvider } from './src/theme/ThemeProvider';
import { AuthProvider } from './src/auth/AuthContext';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: (failureCount, error: any) => {
        if (error?.status === 401 || error?.status === 403) return false;
        return failureCount < 2;
      },
      refetchOnWindowFocus: false,
    },
  },
});

export default function App() {
  return (
    <ErrorBoundary>
      <ThemeProvider>
        <Auth0Provider
          domain={process.env.EXPO_PUBLIC_AUTH0_DOMAIN || 'example.auth0.com'}
          clientId={process.env.EXPO_PUBLIC_AUTH0_CLIENT_ID || 'dummy_client_id'}
        >
          <QueryClientProvider client={queryClient}>
            <AuthProvider>
              <RootNavigator />
            </AuthProvider>
          </QueryClientProvider>
        </Auth0Provider>
      </ThemeProvider>
    </ErrorBoundary>
  );
}
