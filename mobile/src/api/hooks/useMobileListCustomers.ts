import { useListCustomers } from '../generated/endpoints';

export interface MobileListCustomersParams {
  name?: string;
  customerType?: string;
  status?: string;
  page?: number;
  size?: number;
  sort?: string[];
}

/**
 * Wrapper to prevent Orval from serializing Pageable into `pageable=[object Object]`.
 * Spring Boot expects `page=0&size=20&sort=name,asc`.
 */
export const useMobileListCustomers = (params: MobileListCustomersParams, options?: any) => {
  // Cast to any so Orval's getListCustomersUrl appends `page` and `size` at the root
  return useListCustomers(params as any, options);
};
