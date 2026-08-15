import { describe, it, expect, vi, beforeEach } from 'vitest';
import { screen, render } from '@testing-library/react';
import { useAuth0 } from '@auth0/auth0-react';
import { ProtectedRoute } from '../components/ProtectedRoute';
import { MemoryRouter, Routes, Route } from 'react-router-dom';

vi.mock('@auth0/auth0-react');

describe('ProtectedRoute', () => {
  const mockLoginWithRedirect = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
  });

  const renderRoute = () => render(
    <MemoryRouter initialEntries={['/protected']}>
      <Routes>
        <Route element={<ProtectedRoute />}>
          <Route path="/protected" element={<div>Protected Content</div>} />
        </Route>
      </Routes>
    </MemoryRouter>
  );

  it('renders loading state when isLoading is true', () => {
    vi.mocked(useAuth0).mockReturnValue({
      isLoading: true,
      isAuthenticated: false,
      loginWithRedirect: mockLoginWithRedirect,
    } as any);

    renderRoute();
    // Loading spinner should be rendered but no text
    expect(screen.queryByText('Protected Content')).not.toBeInTheDocument();
  });

  it('redirects to login when unauthenticated', () => {
    vi.mocked(useAuth0).mockReturnValue({
      isLoading: false,
      isAuthenticated: false,
      loginWithRedirect: mockLoginWithRedirect,
    } as any);

    renderRoute();
    expect(mockLoginWithRedirect).toHaveBeenCalled();
    expect(screen.queryByText('Protected Content')).not.toBeInTheDocument();
  });

  it('renders children when authenticated', () => {
    vi.mocked(useAuth0).mockReturnValue({
      isLoading: false,
      isAuthenticated: true,
      loginWithRedirect: mockLoginWithRedirect,
    } as any);

    renderRoute();
    expect(screen.getByText('Protected Content')).toBeInTheDocument();
    expect(mockLoginWithRedirect).not.toHaveBeenCalled();
  });
});
