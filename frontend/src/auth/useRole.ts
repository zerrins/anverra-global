import { useAuth0 } from '@auth0/auth0-react';

export const useRole = () => {
  const { user } = useAuth0();
  const roles = user?.['https://anverraglobal.com/roles'] as string[] | undefined;
  
  const isAdmin = roles?.includes('ROLE_ADMIN') ?? false;
  
  return { roles, isAdmin };
};
