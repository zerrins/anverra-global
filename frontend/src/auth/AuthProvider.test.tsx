import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import { AuthProvider } from './AuthProvider';
import { useAuth0 } from '@auth0/auth0-react';
import { getToken } from './token';

vi.mock('@auth0/auth0-react', () => ({
  useAuth0: vi.fn(),
  Auth0Provider: ({ children }: any) => <>{children}</>,
}));

describe('AuthProvider', () => {
  beforeEach(() => {
    vi.stubEnv('VITE_AUTH0_DOMAIN', 'test-domain.auth0.com');
    vi.stubEnv('VITE_AUTH0_CLIENT_ID', 'test-client-id');
    vi.stubEnv('VITE_AUTH0_AUDIENCE', 'test-audience');
  });
  it('renders children when configuration is valid', () => {
    // Vite env is mocked, but let's assume we bypass it or we can just test the behavior
    // The component checks import.meta.env which we mocked to have valid values in vitest.config
    // Let's test the successful render case
    vi.mocked(useAuth0).mockReturnValue({
      getAccessTokenSilently: vi.fn().mockResolvedValue('test-token'),
      isLoading: false,
    } as any);

    render(
      <AuthProvider>
        <div>App Content</div>
      </AuthProvider>
    );

    expect(screen.getByText('App Content')).toBeInTheDocument();
  });

  it('sets the token getter correctly', async () => {
    const mockGetToken = vi.fn().mockResolvedValue('test-token-123');
    vi.mocked(useAuth0).mockReturnValue({
      getAccessTokenSilently: mockGetToken,
      isLoading: false,
    } as any);

    render(
      <AuthProvider>
        <div>App Content</div>
      </AuthProvider>
    );

    const token = await getToken();
    expect(token).toBe('test-token-123');
    expect(mockGetToken).toHaveBeenCalled();
  });
});
