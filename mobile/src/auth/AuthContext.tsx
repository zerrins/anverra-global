import React, { createContext, useContext, useEffect, ReactNode } from 'react';
import { useAuth0 } from 'react-native-auth0';
import { setAccessTokenProvider } from './getAccessToken';

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
  const isAuthenticated = !!user;

  useEffect(() => {
    if (error) {
      console.error('Auth0 error:', error);
    }
  }, [error]);

  useEffect(() => {
    setAccessTokenProvider(async () => {
      try {
        // This will automatically handle token refresh if expired!
        const credentials = await getCredentials();
        return credentials?.accessToken;
      } catch (_e) {
        return undefined;
      }
    });
  }, [getCredentials]);

  const login = async () => {
    try {
      await authorize();
    } catch (_e) {
      console.error('Login failed:', _e);
    }
  };

  const logout = async () => {
    try {
      await clearSession();
    } catch (_e) {
      console.error('Logout failed:', _e);
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
