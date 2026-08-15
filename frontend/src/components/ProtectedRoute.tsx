
import { useAuth0 } from '@auth0/auth0-react';
import { Outlet } from 'react-router-dom';

export const ProtectedRoute = () => {
  const { isAuthenticated, isLoading, loginWithRedirect } = useAuth0();

  if (isLoading) {
    return (
      <div className="flex items-center justify-center min-h-screen w-full">
        <div className="spinner spinner-lg"></div>
      </div>
    );
  }

  if (!isAuthenticated) {
    loginWithRedirect();
    return null; // Will redirect
  }

  return <Outlet />;
};
