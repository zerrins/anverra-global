import { describe, it, expect, vi, beforeEach } from 'vitest';
import { screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { CreateBranchModal } from './CreateBranchModal';
import { renderWithProviders } from '../test/utils';
import * as dealerController from '../api/endpoints/organization-management-controller/organization-management-controller';

vi.mock('../api/endpoints/organization-management-controller/organization-management-controller', async (importOriginal) => {
  const actual = await importOriginal() as any;
  return {
    ...actual,
    useCreateBranch: vi.fn(),
  };
});

describe('CreateBranchModal', () => {
  const mockCreate = vi.fn();
  const mockOnClose = vi.fn();
  const mockOnSuccess = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(dealerController.useCreateBranch).mockReturnValue({
      mutateAsync: mockCreate,
      isPending: false,
    } as any);
  });

  it('validates required fields', async () => {
    renderWithProviders(
      <CreateBranchModal dealerId="dealer-1" onClose={mockOnClose} onSuccess={mockOnSuccess} />
    );

    const user = userEvent.setup();
    await user.click(screen.getByRole('button', { name: /Create Branch/i }));

    await waitFor(() => {
      expect(screen.getByText('Name is required')).toBeInTheDocument();
    });
  });

  it('submits successfully', async () => {
    mockCreate.mockResolvedValue({});
    
    renderWithProviders(
      <CreateBranchModal dealerId="dealer-1" onClose={mockOnClose} onSuccess={mockOnSuccess} />
    );

    const user = userEvent.setup();
    await user.type(screen.getByLabelText(/Name/i), 'New Branch');
    await user.click(screen.getByRole('button', { name: /Create Branch/i }));

    await waitFor(() => {
      expect(mockCreate).toHaveBeenCalledWith({ data: { dealerId: 'dealer-1', name: 'New Branch' } });
      expect(mockOnSuccess).toHaveBeenCalled();
    });
  });
});
