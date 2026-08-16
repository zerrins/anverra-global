import { describe, it, expect, vi, beforeEach } from 'vitest';
import { screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { CreatePolicyModal } from './CreatePolicyModal';
import { renderWithProviders } from '../test/utils';
import * as policyController from '../api/endpoints/policy-controller/policy-controller';
import * as customerController from '../api/endpoints/customer-controller/customer-controller';
import * as insurerController from '../api/endpoints/insurer-controller/insurer-controller';

vi.mock('../api/endpoints/policy-controller/policy-controller', async (importOriginal) => {
  const actual = await importOriginal() as any;
  return {
    ...actual,
    useCreatePolicy: vi.fn(),
  };
});

vi.mock('../api/endpoints/customer-controller/customer-controller', async (importOriginal) => {
  const actual = await importOriginal() as any;
  return {
    ...actual,
    useListCustomers: vi.fn(),
  };
});

vi.mock('../api/endpoints/insurer-controller/insurer-controller', async (importOriginal) => {
  const actual = await importOriginal() as any;
  return {
    ...actual,
    useListInsurers: vi.fn(),
  };
});

describe('CreatePolicyModal Validation', () => {
  const onClose = vi.fn();
  const onSuccess = vi.fn();
  const mockMutateAsync = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(policyController.useCreatePolicy).mockReturnValue({
      mutateAsync: mockMutateAsync,
    } as any);
    
    vi.mocked(customerController.useListCustomers).mockReturnValue({
      data: {
        data: {
          content: [
            { id: '123e4567-e89b-12d3-a456-426614174000', name: 'John Doe', customerType: 'INDIVIDUAL' },
            { id: '223e4567-e89b-12d3-a456-426614174000', name: 'Acme Corp', customerType: 'ORGANIZATION' }
          ]
        }
      },
      isLoading: false,
      error: null
    } as any);

    vi.mocked(insurerController.useListInsurers).mockReturnValue({
      data: {
        data: {
          content: [
            { id: '333e4567-e89b-12d3-a456-426614174000', name: 'Insurer One', status: 'ACTIVE' }
          ]
        }
      },
      isLoading: false,
      error: null
    } as any);
  });

  it('validates UUID fields and numeric constraints', async () => {
    const user = userEvent.setup();
    renderWithProviders(<CreatePolicyModal onClose={onClose} onSuccess={onSuccess} />);

    // Submit empty form
    await user.click(screen.getByRole('button', { name: /Create Policy/i }));

    // Expect required messages or UUID errors
    await waitFor(() => {
      expect(screen.getByText('Customer is required')).toBeInTheDocument();
      // branchId is required, agentA/agentB are optional/valid uuid
      expect(screen.getAllByText('Must be a valid UUID')).toHaveLength(1); // branchId
    });

    // We can't really enter invalid UUIDs into the select if the options only have valid UUIDs,
    // so we just test that the 'required' message goes away if we select one.
    await user.selectOptions(screen.getByRole('combobox', { name: /Customer/i }), '123e4567-e89b-12d3-a456-426614174000');
    await user.type(screen.getByLabelText(/Agent A ID/i), 'invalid-uuid');
    await user.click(screen.getByRole('button', { name: /Create Policy/i }));

    await waitFor(() => {
      expect(screen.getAllByText('Must be a valid UUID')).toHaveLength(2); // branchId and agentAId
    });

    expect(mockMutateAsync).not.toHaveBeenCalled();
  });

  it('submits successfully when fields are valid, insurer is optional', async () => {
    mockMutateAsync.mockResolvedValue({});
    const user = userEvent.setup();
    renderWithProviders(<CreatePolicyModal onClose={onClose} onSuccess={onSuccess} />);

    await user.type(screen.getByLabelText(/Policy Number/i), 'POL-2000');
    await user.selectOptions(screen.getByRole('combobox', { name: /Customer/i }), '123e4567-e89b-12d3-a456-426614174000');
    // Not selecting insurer => it should be undefined in the payload since it's an empty string and we clean it up
    await user.type(screen.getByLabelText(/Agent A ID/i), '123e4567-e89b-12d3-a456-426614174001');
    await user.type(screen.getByLabelText(/Agent B ID/i), '123e4567-e89b-12d3-a456-426614174002');
    await user.type(screen.getByLabelText(/Branch ID/i), '123e4567-e89b-12d3-a456-426614174003');

    await user.click(screen.getByRole('button', { name: /Create Policy/i }));

    await waitFor(() => {
      expect(mockMutateAsync).toHaveBeenCalledWith({
        data: {
          policyNumber: 'POL-2000',
          customerId: '123e4567-e89b-12d3-a456-426614174000',
          agentAId: '123e4567-e89b-12d3-a456-426614174001',
          agentBId: '123e4567-e89b-12d3-a456-426614174002',
          branchId: '123e4567-e89b-12d3-a456-426614174003',
        }
      });
      expect(onSuccess).toHaveBeenCalled();
    });
  });

  it('submits successfully with an insurer selected', async () => {
    mockMutateAsync.mockResolvedValue({});
    const user = userEvent.setup();
    renderWithProviders(<CreatePolicyModal onClose={onClose} onSuccess={onSuccess} />);

    await user.type(screen.getByLabelText(/Policy Number/i), 'POL-2000');
    await user.selectOptions(screen.getByRole('combobox', { name: /Customer/i }), '123e4567-e89b-12d3-a456-426614174000');
    await user.selectOptions(screen.getByRole('combobox', { name: /Insurer/i }), '333e4567-e89b-12d3-a456-426614174000');
    await user.type(screen.getByLabelText(/Branch ID/i), '123e4567-e89b-12d3-a456-426614174003');

    await user.click(screen.getByRole('button', { name: /Create Policy/i }));

    await waitFor(() => {
      expect(mockMutateAsync).toHaveBeenCalledWith({
        data: {
          policyNumber: 'POL-2000',
          customerId: '123e4567-e89b-12d3-a456-426614174000',
          insurerId: '333e4567-e89b-12d3-a456-426614174000',
          branchId: '123e4567-e89b-12d3-a456-426614174003',
        }
      });
    });
  });
});
