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
  http.get('http://localhost:8080/api/v1/customers', ({ request }) => {
    const url = new URL(request.url);
    const statusFilter = url.searchParams.get('status');

    let content = [
      {
        id: '123e4567-e89b-12d3-a456-426614174000',
        name: 'Acme Corp',
        customerType: 'ORGANIZATION',
        status: 'ACTIVE',
        contactInfo: 'contact@acme.com',
        businessInfo: 'Business stuff'
      },
      {
        id: '223e4567-e89b-12d3-a456-426614174000',
        name: 'John Doe',
        customerType: 'INDIVIDUAL',
        status: 'ACTIVE',
        contactInfo: 'john@doe.com',
        individualInfo: 'Individual stuff'
      },
      {
        id: '323e4567-e89b-12d3-a456-426614174000',
        name: 'Inactive Inc',
        customerType: 'ORGANIZATION',
        status: 'INACTIVE',
        contactInfo: 'inactive@inc.com',
        businessInfo: 'Closed'
      }
    ];

    if (statusFilter) {
      content = content.filter(c => c.status === statusFilter);
    }

    return HttpResponse.json({
      content,
      totalElements: content.length,
      totalPages: 1,
      number: 0,
      first: true,
      last: true,
    });
  }),
  http.get('http://localhost:8080/api/v1/customers/:id', ({ params }) => {
    return HttpResponse.json({
      id: params.id,
      name: 'Acme Corp',
      customerType: 'ORGANIZATION',
      status: 'ACTIVE',
      contactInfo: 'contact@acme.com',
      businessInfo: 'Business stuff'
    });
  }),
  http.get('http://localhost:8080/api/v1/hierarchy/dealers', () => {
    return HttpResponse.json([
      { id: '11111111-1111-1111-1111-111111111111', name: 'Dealer One' }
    ]);
  }),
  http.get('http://localhost:8080/api/v1/hierarchy/dealers/:dealerId/branches', () => {
    return HttpResponse.json([
      { id: '22222222-2222-2222-2222-222222222222', name: 'Branch One' }
    ]);
  }),
  http.get('http://localhost:8080/api/v1/hierarchy/branches/:branchId/agents', () => {
    return HttpResponse.json([
      { id: '33333333-3333-3333-3333-333333333333', name: 'Agent One' }
    ]);
  }),
  http.post('http://localhost:8080/api/v1/customers', () => {
    return HttpResponse.json({ id: 'new-cust-1', name: 'New Customer' }, { status: 201 });
  }),
  http.put('http://localhost:8080/api/v1/customers/:id', ({ params }) => {
    return HttpResponse.json({ id: params.id, name: 'Updated Customer' });
  }),
  http.post('http://localhost:8080/api/v1/customers/:id/lifecycle/activate', () => {
    return HttpResponse.json({}, { status: 200 });
  }),
  http.post('http://localhost:8080/api/v1/customers/:id/lifecycle/deactivate', () => {
    return HttpResponse.json({}, { status: 200 });
  }),
  http.get('http://localhost:8080/api/v1/insurers', ({ request }) => {
    const url = new URL(request.url);
    const statusFilter = url.searchParams.get('status');

    let content = [
      {
        id: '444e4567-e89b-12d3-a456-426614174000',
        name: 'Insurer One',
        status: 'ACTIVE',
        version: 1
      },
      {
        id: '555e4567-e89b-12d3-a456-426614174000',
        name: 'Insurer Two',
        status: 'ACTIVE',
        version: 1
      },
      {
        id: '666e4567-e89b-12d3-a456-426614174000',
        name: 'Inactive Insurer',
        status: 'INACTIVE',
        version: 1
      }
    ];

    if (statusFilter) {
      content = content.filter(c => c.status === statusFilter);
    }

    return HttpResponse.json({
      content,
      totalElements: content.length,
      totalPages: 1,
      number: 0,
      first: true,
      last: true,
    });
  }),
  http.get('http://localhost:8080/api/v1/insurers/:id', ({ params }) => {
    return HttpResponse.json({
      id: params.id,
      name: 'Insurer One',
      status: 'ACTIVE',
      version: 1
    });
  }),
  http.post('http://localhost:8080/api/v1/insurers', () => {
    return HttpResponse.json({ id: 'new-insurer', name: 'New Insurer', status: 'ACTIVE', version: 1 }, { status: 201 });
  }),
  http.put('http://localhost:8080/api/v1/insurers/:id', ({ params }) => {
    return HttpResponse.json({ id: params.id, name: 'Updated Insurer', status: 'ACTIVE', version: 2 });
  }),
  http.post('http://localhost:8080/api/v1/insurers/:id/lifecycle/activate', () => {
    return HttpResponse.json({}, { status: 200 });
  }),
  http.post('http://localhost:8080/api/v1/insurers/:id/lifecycle/deactivate', () => {
    return HttpResponse.json({}, { status: 200 });
  }),
];
