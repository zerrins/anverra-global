import { describe, it, expect, jest, beforeEach } from '@jest/globals';
import { useMobileListCustomers } from '../useMobileListCustomers';
import { useListCustomers } from '../../generated/endpoints';

jest.mock('../../generated/endpoints', () => ({
  useListCustomers: jest.fn(),
}));

describe('useMobileListCustomers', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('passes flat search and pagination parameters to Orval generated hook', () => {
    const params = {
      name: 'John Doe',
      page: 2,
      size: 10,
    };
    
    useMobileListCustomers(params);
    
    expect(useListCustomers).toHaveBeenCalledWith(
      params,
      undefined
    );
  });
});
