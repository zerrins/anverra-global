import { describe, it, expect, vi, beforeEach } from 'vitest';
import { screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { CreateDealerModal } from './CreateDealerModal';
import { renderWithProviders } from '../test/utils';
import * as dealerController from '../api/endpoints/organization-management-controller/organization-management-controller';

vi.mock('../api/endpoints/organization-management-controller/organization-management-controller', async (importOriginal) => {
  const actual = await importOriginal() as any;
  return {
    ...actual,
    useCreateDealer: vi.fn(),
  };
});

describe('CreateDealerModal', () => {
  const mockCreate = vi.fn();
  const mockOnClose = vi.fn();
  const mockOnSuccess = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(dealerController.useCreateDealer).mockReturnValue({
      mutateAsync: mockCreate,
      isPending: false,
    } as any);
  });

  it('validates required fields', async () => {
    renderWithProviders(
      <CreateDealerModal onClose={mockOnClose} onSuccess={mockOnSuccess} />
    );

    const user = userEvent.setup();
    await user.click(screen.getByRole('button', { name: /Create Dealer/i }));

    await waitFor(() => {
      expect(screen.getByText('Name is required')).toBeInTheDocument();
    });
  });

  it('submits successfully', async () => {
    mockCreate.mockResolvedValue({});
    
    renderWithProviders(
      <CreateDealerModal onClose={mockOnClose} onSuccess={mockOnSuccess} />
    );

    const user = userEvent.setup();
    await user.type(screen.getByLabelText(/Name/i), 'New Dealer');
    await user.click(screen.getByRole('button', { name: /Create Dealer/i }));

    await waitFor(() => {
      expect(mockCreate).toHaveBeenCalledWith({ data: { name: 'New Dealer' } });
      expect(mockOnSuccess).toHaveBeenCalled();
    });
  });
});
