import { describe, it, expect, vi, beforeEach } from 'vitest';
import { screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import CustomerDetails from './CustomerDetails';
import { renderWithProviders } from '../test/utils';
import { server } from '../test/setup';
import { http, HttpResponse } from 'msw';
import { Route, Routes } from 'react-router-dom';

describe('CustomerDetails', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const renderComponent = (id = 'cust-1') => {
    return renderWithProviders(
      <Routes>
        <Route path="/customers/:id" element={<CustomerDetails />} />
      </Routes>,
      { route: `/customers/${id}` }
    );
  };

  it('renders loading state initially', () => {
    renderComponent();
    expect(document.querySelector('.spinner')).toBeInTheDocument();
  });

  it('renders organization customer details successfully', async () => {
    renderComponent();
    
    await waitFor(() => {
      expect(screen.getByRole('heading', { name: 'Acme Corp' })).toBeInTheDocument();
    });
    
    expect(screen.getByText('ORGANIZATION')).toBeInTheDocument();
    expect(screen.getByText('contact@acme.com')).toBeInTheDocument();
    expect(screen.getByText('ACTIVE')).toBeInTheDocument();
    expect(screen.getByText('Business Information')).toBeInTheDocument();
    expect(screen.getByText('Business stuff')).toBeInTheDocument();
    expect(screen.queryByText('Individual Information')).not.toBeInTheDocument();
  });

  it('renders individual customer details successfully', async () => {
    server.use(
      http.get('http://localhost:8080/api/v1/customers/:id', ({ params }) => {
        return HttpResponse.json({
          id: params.id,
          name: 'John Doe',
          customerType: 'INDIVIDUAL',
          status: 'INACTIVE',
          contactInfo: 'john@doe.com',
          individualInfo: 'Individual stuff'
        });
      })
    );

    renderComponent('cust-2');
    
    await waitFor(() => {
      expect(screen.getByRole('heading', { name: 'John Doe' })).toBeInTheDocument();
    });
    
    expect(screen.getByText('INDIVIDUAL')).toBeInTheDocument();
    expect(screen.getByText('john@doe.com')).toBeInTheDocument();
    expect(screen.getByText('INACTIVE')).toBeInTheDocument();
    expect(screen.getByText('Individual Information')).toBeInTheDocument();
    expect(screen.getByText('Individual stuff')).toBeInTheDocument();
    expect(screen.queryByText('Business Information')).not.toBeInTheDocument();
  });

  it('renders error state on API failure', async () => {
    server.use(
      http.get('http://localhost:8080/api/v1/customers/:id', () => {
        return new HttpResponse(null, { status: 500 });
      })
    );

    renderComponent();
    
    await waitFor(() => {
      expect(screen.getByText(/An unexpected server error occurred/i)).toBeInTheDocument();
    });
  });

  it('handles activation lifecycle', async () => {
    const user = userEvent.setup();
    let activateCalled = false;
    
    server.use(
      http.get('http://localhost:8080/api/v1/customers/:id', ({ params }) => {
        return HttpResponse.json({
          id: params.id,
          name: 'John Doe',
          customerType: 'INDIVIDUAL',
          status: 'INACTIVE',
          contactInfo: 'john@doe.com',
        });
      }),
      http.post('http://localhost:8080/api/v1/customers/:id/lifecycle/activate', () => {
        activateCalled = true;
        return HttpResponse.json({}, { status: 200 });
      })
    );

    renderComponent();
    
    await waitFor(() => {
      expect(screen.getByRole('heading', { name: 'John Doe' })).toBeInTheDocument();
    });
    
    const activateBtn = screen.getByRole('button', { name: /Activate/i });
    await user.click(activateBtn);
    
    await waitFor(() => {
      expect(activateCalled).toBe(true);
    });
  });

  it('handles deactivation lifecycle', async () => {
    const user = userEvent.setup();
    let deactivateCalled = false;
    
    server.use(
      http.post('http://localhost:8080/api/v1/customers/:id/lifecycle/deactivate', () => {
        deactivateCalled = true;
        return HttpResponse.json({}, { status: 200 });
      })
    );

    renderComponent();
    
    await waitFor(() => {
      expect(screen.getByRole('heading', { name: 'Acme Corp' })).toBeInTheDocument();
    });
    
    const deactivateBtn = screen.getByRole('button', { name: /Deactivate/i });
    await user.click(deactivateBtn);
    
    await waitFor(() => {
      expect(deactivateCalled).toBe(true);
    });
  });

  it('opens edit modal', async () => {
    const user = userEvent.setup();
    renderComponent();
    
    await waitFor(() => {
      expect(screen.getByRole('heading', { name: 'Acme Corp' })).toBeInTheDocument();
    });
    
    const editBtn = screen.getByRole('button', { name: /Edit/i });
    await user.click(editBtn);
    
    await waitFor(() => {
      expect(screen.getByText('Edit Customer')).toBeInTheDocument();
    });
  });
});
