import { describe, it, expect, vi, beforeEach } from 'vitest';
import { screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { CreatePolicyModal } from './CreatePolicyModal';
import { renderWithProviders } from '../test/utils';
import * as policyController from '../api/endpoints/policy-controller/policy-controller';

vi.mock('../api/endpoints/policy-controller/policy-controller', async (importOriginal) => {
  const actual = await importOriginal() as any;
  return {
    ...actual,
    useCreatePolicy: vi.fn(),
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
  });

  it('validates UUID fields and numeric constraints', async () => {
    const user = userEvent.setup();
    renderWithProviders(<CreatePolicyModal onClose={onClose} onSuccess={onSuccess} />);

    // Submit empty form
    await user.click(screen.getByRole('button', { name: /Create Policy/i }));

    // Expect required messages or UUID errors
    await waitFor(() => {
      expect(screen.getAllByText('Must be a valid UUID')).toHaveLength(4); // customer, agentA, agentB, branch
    });

    // Enter invalid UUIDs
    await user.type(screen.getByLabelText(/Customer ID/i), 'invalid-uuid');
    await user.click(screen.getByRole('button', { name: /Create Policy/i }));

    await waitFor(() => {
      expect(screen.getAllByText('Must be a valid UUID')).toHaveLength(4);
    });

    expect(mockMutateAsync).not.toHaveBeenCalled();
  });

  it('submits successfully when fields are valid', async () => {
    mockMutateAsync.mockResolvedValue({});
    const user = userEvent.setup();
    renderWithProviders(<CreatePolicyModal onClose={onClose} onSuccess={onSuccess} />);

    await user.type(screen.getByLabelText(/Policy Number/i), 'POL-2000');
    await user.type(screen.getByLabelText(/Customer ID/i), '123e4567-e89b-12d3-a456-426614174000');
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
});
