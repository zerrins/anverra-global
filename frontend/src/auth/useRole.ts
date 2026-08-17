import { useAuth0 } from '@auth0/auth0-react';

export const useRole = () => {
  const { user } = useAuth0();
  const roles = user?.['https://anverraglobal.com/roles'] as string[] | undefined;
  
  const isAdmin = roles?.includes('ROLE_ADMIN') ?? false;
  const isDealer = roles?.includes('ROLE_DEALER') ?? false;
  const isBranchAdmin = roles?.includes('ROLE_BRANCH_ADMIN') ?? false;
  
  const canManageOrganization = isAdmin || isDealer || isBranchAdmin;
  
  return { roles, isAdmin, isDealer, isBranchAdmin, canManageOrganization, user };
};
