import { describe, it, expect, vi, beforeEach } from 'vitest';
import { screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { EditCustomerModal } from './EditCustomerModal';
import { renderWithProviders } from '../test/utils';
import { server } from '../test/setup';
import { http, HttpResponse } from 'msw';

describe('EditCustomerModal', () => {
  const onClose = vi.fn();
  const onSuccess = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
  });

  const mockIndividual = {
    id: 'cust-1',
    name: 'John Doe',
    customerType: 'INDIVIDUAL',
    contactInfo: 'john@doe.com',
    addressInfo: '123 St',
    individualInfo: 'IND-INFO',
    dealerId: 'dealer-1',
    branchId: 'branch-1',
    agentId: 'agent-1'
  };

  const mockOrganization = {
    id: 'cust-2',
    name: 'Acme Corp',
    customerType: 'ORGANIZATION',
    contactInfo: 'contact@acme.com',
    addressInfo: '456 Ave',
    businessInfo: 'ORG-INFO',
    dealerId: 'dealer-1',
    branchId: 'branch-1',
    agentId: 'agent-1'
  };

  it('renders modal with existing individual values', () => {
    renderWithProviders(<EditCustomerModal customer={mockIndividual} onClose={onClose} onSuccess={onSuccess} />);
    
    expect(screen.getByText('Edit Customer')).toBeInTheDocument();
    
    expect((document.querySelector('input[name="name"]') as HTMLInputElement).value).toBe('John Doe');
    expect((document.querySelector('input[name="contactInfo"]') as HTMLInputElement).value).toBe('john@doe.com');
    expect((document.querySelector('input[name="addressInfo"]') as HTMLInputElement).value).toBe('123 St');
    expect((document.querySelector('input[name="individualInfo"]') as HTMLInputElement).value).toBe('IND-INFO');
    
    expect(document.querySelector('input[name="businessInfo"]')).not.toBeInTheDocument();
    expect(document.querySelector('select[name="customerType"]')).not.toBeInTheDocument();
    expect(document.querySelector('select[name="targetDealerId"]')).not.toBeInTheDocument();
  });

  it('renders modal with existing organization values', () => {
    renderWithProviders(<EditCustomerModal customer={mockOrganization} onClose={onClose} onSuccess={onSuccess} />);
    
    expect((document.querySelector('input[name="name"]') as HTMLInputElement).value).toBe('Acme Corp');
    expect((document.querySelector('input[name="businessInfo"]') as HTMLInputElement).value).toBe('ORG-INFO');
    
    expect(document.querySelector('input[name="individualInfo"]')).not.toBeInTheDocument();
  });

  it('shows validation errors if fields are cleared', async () => {
    const user = userEvent.setup();
    renderWithProviders(<EditCustomerModal customer={mockIndividual} onClose={onClose} onSuccess={onSuccess} />);
    
    const nameInput = document.querySelector('input[name="name"]') as HTMLInputElement;
    await user.clear(nameInput);
    
    const submitBtn = screen.getByRole('button', { name: /Save Changes/i });
    await user.click(submitBtn);
    
    await waitFor(() => {
      expect(screen.getByText('Name is required')).toBeInTheDocument();
    });
  });

  it('submits individual update successfully', async () => {
    const user = userEvent.setup();
    let capturedRequest: any;
    
    server.use(
      http.put('http://localhost:8080/api/v1/customers/:id', async ({ request }) => {
        capturedRequest = await request.json();
        return HttpResponse.json({ id: 'cust-1' }, { status: 200 });
      })
    );

    renderWithProviders(<EditCustomerModal customer={mockIndividual} onClose={onClose} onSuccess={onSuccess} />);
    
    const nameInput = document.querySelector('input[name="name"]') as HTMLInputElement;
    await user.clear(nameInput);
    await user.type(nameInput, 'John Updated');
    
    await user.click(screen.getByRole('button', { name: /Save Changes/i }));
    
    await waitFor(() => {
      expect(onSuccess).toHaveBeenCalled();
    });
    
    expect(capturedRequest).toEqual({
      name: 'John Updated',
      contactInfo: 'john@doe.com',
      addressInfo: '123 St',
      individualInfo: 'IND-INFO',
    });
  });

  it('submits organization update successfully', async () => {
    const user = userEvent.setup();
    let capturedRequest: any;
    
    server.use(
      http.put('http://localhost:8080/api/v1/customers/:id', async ({ request }) => {
        capturedRequest = await request.json();
        return HttpResponse.json({ id: 'cust-2' }, { status: 200 });
      })
    );

    renderWithProviders(<EditCustomerModal customer={mockOrganization} onClose={onClose} onSuccess={onSuccess} />);
    
    const businessInput = document.querySelector('input[name="businessInfo"]') as HTMLInputElement;
    await user.clear(businessInput);
    await user.type(businessInput, 'NEW-ORG-INFO');
    
    await user.click(screen.getByRole('button', { name: /Save Changes/i }));
    
    await waitFor(() => {
      expect(onSuccess).toHaveBeenCalled();
    });
    
    expect(capturedRequest).toEqual({
      name: 'Acme Corp',
      contactInfo: 'contact@acme.com',
      addressInfo: '456 Ave',
      businessInfo: 'NEW-ORG-INFO',
    });
  });
});
