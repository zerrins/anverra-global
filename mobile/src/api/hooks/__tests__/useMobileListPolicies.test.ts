import { describe, it, expect, jest, beforeEach } from '@jest/globals';
import { useMobileListPolicies } from '../useMobileListPolicies';
import { useListPolicies } from '../../generated/endpoints';

jest.mock('../../generated/endpoints', () => ({
  useListPolicies: jest.fn(),
}));

describe('useMobileListPolicies', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('passes flat pagination parameters to Orval generated hook', () => {
    const params = {
      page: 1,
      size: 50,
      sort: ['createdAt,desc'],
    };
    
    useMobileListPolicies(params);
    
    expect(useListPolicies).toHaveBeenCalledWith(
      params,
      undefined
    );
  });
});
