import React, { createContext, useContext, useEffect, ReactNode } from 'react';
import { useAuth0 } from 'react-native-auth0';
import { useQueryClient } from '@tanstack/react-query';
import { setAccessTokenProvider, setOnUnauthorized } from './getAccessToken';

interface AuthContextType {
  isAuthenticated: boolean;
  isLoading: boolean;
  login: () => Promise<void>;
  logout: () => Promise<void>;
  error: Error | null;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const { authorize, clearSession, user, error, isLoading, getCredentials } = useAuth0();
  const queryClient = useQueryClient();
  
  // When Auth0 initializes, it populates user if credentials manager has a valid session.
  const isAuthenticated = !!user;

  useEffect(() => {
    if (error) {
      console.error('Auth0 error:', error);
    }
  }, [error]);

  const logout = React.useCallback(async () => {
    try {
      await clearSession();
    } catch (_e) {
      console.error('Logout failed:', _e);
    } finally {
      queryClient.clear();
    }
  }, [clearSession, queryClient]);

  useEffect(() => {
    setAccessTokenProvider(async () => {
      try {
        const credentials = await getCredentials();
        return credentials?.accessToken;
      } catch (_e) {
        return undefined;
      }
    });

    setOnUnauthorized(() => {
      // Called by API interceptor when 401 happens
      logout();
    });
  }, [getCredentials, logout]);

  const login = async () => {
    try {
      await authorize();
    } catch (_e) {
      console.error('Login failed:', _e);
    }
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, isLoading, login, logout, error }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
