import { describe, it, expect, vi, beforeEach } from 'vitest';
import { screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { CreateCustomerModal } from './CreateCustomerModal';
import { renderWithProviders } from '../test/utils';
import * as roleModule from '../auth/useRole';
import { server } from '../test/setup';
import { http, HttpResponse } from 'msw';

vi.mock('../auth/useRole', () => ({
  useRole: vi.fn(),
}));

describe('CreateCustomerModal', () => {
  const onClose = vi.fn();
  const onSuccess = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (roleModule.useRole as any).mockReturnValue({ isAdmin: false, isAgent: true });
  });

  it('renders modal and default fields', () => {
    renderWithProviders(<CreateCustomerModal onClose={onClose} onSuccess={onSuccess} />);
    
    expect(screen.getByText('Create New Customer')).toBeInTheDocument();
    expect(document.querySelector('select[name="customerType"]')).toBeInTheDocument();
    expect(document.querySelector('input[name="name"]')).toBeInTheDocument();
    expect(document.querySelector('input[name="contactInfo"]')).toBeInTheDocument();
    expect(document.querySelector('input[name="addressInfo"]')).toBeInTheDocument();
    
    // Default is INDIVIDUAL
    expect(document.querySelector('input[name="individualInfo"]')).toBeInTheDocument();
    expect(document.querySelector('input[name="businessInfo"]')).not.toBeInTheDocument();
  });

  it('switches to ORGANIZATION and shows business info', async () => {
    const user = userEvent.setup();
    renderWithProviders(<CreateCustomerModal onClose={onClose} onSuccess={onSuccess} />);
    
    const typeSelect = document.querySelector('select[name="customerType"]') as HTMLSelectElement;
    await user.selectOptions(typeSelect, 'ORGANIZATION');
    
    expect(document.querySelector('input[name="individualInfo"]')).not.toBeInTheDocument();
    expect(document.querySelector('input[name="businessInfo"]')).toBeInTheDocument();
  });

  it('shows validation errors for empty required fields', async () => {
    const user = userEvent.setup();
    renderWithProviders(<CreateCustomerModal onClose={onClose} onSuccess={onSuccess} />);
    
    const submitBtn = screen.getByRole('button', { name: /Create Customer/i });
    await user.click(submitBtn);
    
    await waitFor(() => {
      expect(screen.getByText('Name is required')).toBeInTheDocument();
      expect(screen.getByText('Contact Info is required')).toBeInTheDocument();
      expect(screen.getByText('Address Info is required')).toBeInTheDocument();
      expect(screen.getByText('Individual Info is required for INDIVIDUAL')).toBeInTheDocument();
    });
  });

  it('submits valid INDIVIDUAL form successfully', async () => {
    const user = userEvent.setup();
    let capturedRequest: any;
    
    server.use(
      http.post('http://localhost:8080/api/v1/customers', async ({ request }) => {
        capturedRequest = await request.json();
        return HttpResponse.json({ id: 'new-1' }, { status: 201 });
      })
    );

    renderWithProviders(<CreateCustomerModal onClose={onClose} onSuccess={onSuccess} />);
    
    const nameInput = document.querySelector('input[name="name"]') as HTMLInputElement;
    const contactInfoInput = document.querySelector('input[name="contactInfo"]') as HTMLInputElement;
    const addressInfoInput = document.querySelector('input[name="addressInfo"]') as HTMLInputElement;
    const individualInfoInput = document.querySelector('input[name="individualInfo"]') as HTMLInputElement;
    
    await user.type(nameInput, 'Test IND');
    await user.type(contactInfoInput, 'test@ind.com');
    await user.type(addressInfoInput, '123 Ind St');
    await user.type(individualInfoInput, 'IND-INFO');
    
    await user.click(screen.getByRole('button', { name: /Create Customer/i }));
    
    await waitFor(() => {
      expect(onSuccess).toHaveBeenCalled();
    });
    
    expect(capturedRequest).toEqual({
      customerType: 'INDIVIDUAL',
      name: 'Test IND',
      contactInfo: 'test@ind.com',
      addressInfo: '123 Ind St',
      individualInfo: 'IND-INFO'
    });
  });

  it('submits valid ORGANIZATION form successfully', async () => {
    const user = userEvent.setup();
    let capturedRequest: any;
    
    server.use(
      http.post('http://localhost:8080/api/v1/customers', async ({ request }) => {
        capturedRequest = await request.json();
        return HttpResponse.json({ id: 'new-2' }, { status: 201 });
      })
    );

    renderWithProviders(<CreateCustomerModal onClose={onClose} onSuccess={onSuccess} />);
    
    const typeSelect = document.querySelector('select[name="customerType"]') as HTMLSelectElement;
    await user.selectOptions(typeSelect, 'ORGANIZATION');
    
    const nameInput = document.querySelector('input[name="name"]') as HTMLInputElement;
    const contactInfoInput = document.querySelector('input[name="contactInfo"]') as HTMLInputElement;
    const addressInfoInput = document.querySelector('input[name="addressInfo"]') as HTMLInputElement;
    const businessInfoInput = document.querySelector('input[name="businessInfo"]') as HTMLInputElement;
    
    await user.type(nameInput, 'Test ORG');
    await user.type(contactInfoInput, 'test@org.com');
    await user.type(addressInfoInput, '123 Org St');
    await user.type(businessInfoInput, 'ORG-INFO');
    
    await user.click(screen.getByRole('button', { name: /Create Customer/i }));
    
    await waitFor(() => {
      expect(onSuccess).toHaveBeenCalled();
    });
    
    expect(capturedRequest).toEqual({
      customerType: 'ORGANIZATION',
      name: 'Test ORG',
      contactInfo: 'test@org.com',
      addressInfo: '123 Org St',
      businessInfo: 'ORG-INFO',
      individualInfo: ''
    });
  });

  it('shows global admin selectors only for ADMIN', async () => {
    const user = userEvent.setup();
    (roleModule.useRole as any).mockReturnValue({ isAdmin: true, isAgent: false });
    
    renderWithProviders(<CreateCustomerModal onClose={onClose} onSuccess={onSuccess} />);
    
    expect(screen.getByText(/Global Admin Ownership Assignment/i)).toBeInTheDocument();
    
    // Wait for dealers to load
    await waitFor(() => {
      expect(screen.getByRole('option', { name: 'Dealer One' })).toBeInTheDocument();
    });
    
    // Dealer selector
    const dealerSelect = document.querySelector('select[name="targetDealerId"]') as HTMLSelectElement;
    await user.selectOptions(dealerSelect, '11111111-1111-1111-1111-111111111111');
    
    // Branches load
    await waitFor(() => {
      expect(screen.getByRole('option', { name: 'Branch One' })).toBeInTheDocument();
    });
    
    const branchSelect = document.querySelector('select[name="targetBranchId"]') as HTMLSelectElement;
    await user.selectOptions(branchSelect, '22222222-2222-2222-2222-222222222222');
    
    // Agents load
    await waitFor(() => {
      expect(screen.getByRole('option', { name: 'Agent One' })).toBeInTheDocument();
    });
    
    const agentSelect = document.querySelector('select[name="targetAgentId"]') as HTMLSelectElement;
    await user.selectOptions(agentSelect, '33333333-3333-3333-3333-333333333333');
  });

  it('submits ownership payload for ADMIN', async () => {
    const user = userEvent.setup();
    (roleModule.useRole as any).mockReturnValue({ isAdmin: true });
    
    let capturedRequest: any;
    server.use(
      http.post('http://localhost:8080/api/v1/customers', async ({ request }) => {
        capturedRequest = await request.json();
        return HttpResponse.json({ id: 'new-admin-1' }, { status: 201 });
      })
    );

    renderWithProviders(<CreateCustomerModal onClose={onClose} onSuccess={onSuccess} />);
    
    const nameInput = document.querySelector('input[name="name"]') as HTMLInputElement;
    const contactInfoInput = document.querySelector('input[name="contactInfo"]') as HTMLInputElement;
    const addressInfoInput = document.querySelector('input[name="addressInfo"]') as HTMLInputElement;
    const individualInfoInput = document.querySelector('input[name="individualInfo"]') as HTMLInputElement;
    
    await user.type(nameInput, 'Test ADMIN');
    await user.type(contactInfoInput, 'admin@test.com');
    await user.type(addressInfoInput, '123');
    await user.type(individualInfoInput, '123');
    
    await waitFor(() => {
      expect(screen.getByRole('option', { name: 'Dealer One' })).toBeInTheDocument();
    });
    
    const dealerSelect = document.querySelector('select[name="targetDealerId"]') as HTMLSelectElement;
    await user.selectOptions(dealerSelect, '11111111-1111-1111-1111-111111111111');
    
    await waitFor(() => {
      expect(screen.getByRole('option', { name: 'Branch One' })).toBeInTheDocument();
    });
    
    const branchSelect = document.querySelector('select[name="targetBranchId"]') as HTMLSelectElement;
    await user.selectOptions(branchSelect, '22222222-2222-2222-2222-222222222222');
    
    await waitFor(() => {
      expect(screen.getByRole('option', { name: 'Agent One' })).toBeInTheDocument();
    });
    
    const agentSelect = document.querySelector('select[name="targetAgentId"]') as HTMLSelectElement;
    await user.selectOptions(agentSelect, '33333333-3333-3333-3333-333333333333');
    
    await user.click(screen.getByRole('button', { name: /Create Customer/i }));
    
    await waitFor(() => {
      expect(onSuccess).toHaveBeenCalled();
    });
    
    expect(capturedRequest.targetDealerId).toBe('11111111-1111-1111-1111-111111111111');
    expect(capturedRequest.targetBranchId).toBe('22222222-2222-2222-2222-222222222222');
    expect(capturedRequest.targetAgentId).toBe('33333333-3333-3333-3333-333333333333');
  });
  
  it('changing Dealer clears Branch and Agent', async () => {
    const user = userEvent.setup();
    (roleModule.useRole as any).mockReturnValue({ isAdmin: true });
    
    renderWithProviders(<CreateCustomerModal onClose={onClose} onSuccess={onSuccess} />);
    
    await waitFor(() => {
      expect(screen.getByRole('option', { name: 'Dealer One' })).toBeInTheDocument();
    });
    
    const dealerSelect = document.querySelector('select[name="targetDealerId"]') as HTMLSelectElement;
    await user.selectOptions(dealerSelect, '11111111-1111-1111-1111-111111111111');
    
    await waitFor(() => {
      expect(screen.getByRole('option', { name: 'Branch One' })).toBeInTheDocument();
    });
    
    const branchSelect = document.querySelector('select[name="targetBranchId"]') as HTMLSelectElement;
    await user.selectOptions(branchSelect, '22222222-2222-2222-2222-222222222222');
    
    expect(branchSelect.value).toBe('22222222-2222-2222-2222-222222222222');
    
    // Clear dealer
    await user.selectOptions(dealerSelect, '');
    
    await waitFor(() => {
      expect(branchSelect.value).toBe('');
    });
  });
});
