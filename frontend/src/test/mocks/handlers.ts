import { http, HttpResponse } from 'msw';

export const handlers = [
  http.get('http://localhost:8080/api/v1/reporting/policies/statistics', () => {
    return HttpResponse.json({
      totalPolicies: 150,
      draftCount: 20,
      activeCount: 120,
      inactiveCount: 10,
    });
  }),
  http.get('http://localhost:8080/api/v1/reporting/commissions/statistics', () => {
    return HttpResponse.json({
      totalCommissionAmount: 50000,
      agentACommissionAmount: 25000,
      agentBCommissionAmount: 25000,
      configuredCommissionCount: 100,
    });
  }),
  http.get('http://localhost:8080/api/v1/policies', () => {
    return HttpResponse.json({
      content: [
        {
          policyId: '123e4567-e89b-12d3-a456-426614174000',
          policyNumber: 'POL-1000',
          status: 'ACTIVE',
          premium: 1000,
          customerId: '123e4567-e89b-12d3-a456-426614174001',
        }
      ],
      totalElements: 1,
      totalPages: 1,
      number: 0,
      first: true,
      last: true,
    });
  }),
];
