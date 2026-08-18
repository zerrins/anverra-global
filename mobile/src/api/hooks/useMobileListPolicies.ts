import { useListPolicies } from '../generated/endpoints';

export interface MobileListPoliciesParams {
  page?: number;
  size?: number;
  sort?: string[];
}

/**
 * Wrapper to prevent Orval from serializing Pageable into `pageable=[object Object]`.
 * Spring Boot expects `page=0&size=20&sort=name,asc`.
 */
export const useMobileListPolicies = (params: MobileListPoliciesParams, options?: any) => {
  // Cast to any so Orval's getListPoliciesUrl appends `page` and `size` at the root
  return useListPolicies(params as any, options);
};
