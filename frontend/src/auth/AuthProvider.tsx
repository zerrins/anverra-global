
import { useEffect } from 'react';
import type { ReactNode } from 'react';
import { Auth0Provider, useAuth0 } from '@auth0/auth0-react';
import { setTokenGetter, resetTokenState } from './token';

interface AuthProviderProps {
  children: ReactNode;
}

const domain = import.meta.env.VITE_AUTH0_DOMAIN;
const clientId = import.meta.env.VITE_AUTH0_CLIENT_ID;
const audience = import.meta.env.VITE_AUTH0_AUDIENCE;

const AuthTokenInjector = ({ children }: { children: ReactNode }) => {
  const { getAccessTokenSilently, isLoading } = useAuth0();

  useEffect(() => {
    if (!isLoading) {
      setTokenGetter(async () => {
        try {
          return await getAccessTokenSilently();
        } catch {
          return undefined;
        }
      });
    }
    return () => {
      resetTokenState();
    };
  }, [getAccessTokenSilently, isLoading]);

  return <>{children}</>;
};

export const AuthProvider = ({ children }: AuthProviderProps) => {
  if (!domain || !clientId) {
    return (
      <div className="flex items-center justify-center min-h-screen p-4">
        <div className="card p-6 max-w-md w-full">
          <h2 className="text-danger mb-2">Configuration Error</h2>
          <p className="text-muted text-sm">
            Auth0 Domain or Client ID is missing. Please check your .env file.
          </p>
        </div>
      </div>
    );
  }

  return (
    <Auth0Provider
      domain={domain}
      clientId={clientId}
      authorizationParams={{
        redirect_uri: window.location.origin,
        audience: audience || undefined,
      }}
      useRefreshTokens={true}
      cacheLocation="memory"
    >
      <AuthTokenInjector>{children}</AuthTokenInjector>
    </Auth0Provider>
  );
};
