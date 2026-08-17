import { describe, it, expect, vi, beforeEach } from 'vitest';
import { screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { EditDealerModal } from './EditDealerModal';
import { renderWithProviders } from '../test/utils';
import * as dealerController from '../api/endpoints/organization-management-controller/organization-management-controller';

vi.mock('../api/endpoints/organization-management-controller/organization-management-controller', async (importOriginal) => {
  const actual = await importOriginal() as any;
  return {
    ...actual,
    useUpdateDealer: vi.fn(),
  };
});

describe('EditDealerModal', () => {
  const mockUpdate = vi.fn();
  const mockOnClose = vi.fn();
  const mockOnSuccess = vi.fn();
  
  const mockDealer: any = {
    id: '1',
    name: 'Existing Dealer',
    status: 'ACTIVE'
  };

  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(dealerController.useUpdateDealer).mockReturnValue({
      mutateAsync: mockUpdate,
      isPending: false,
    } as any);
  });

  it('renders existing values', () => {
    renderWithProviders(
      <EditDealerModal dealer={mockDealer} onClose={mockOnClose} onSuccess={mockOnSuccess} />
    );
    expect(screen.getByDisplayValue('Existing Dealer')).toBeInTheDocument();
  });

  it('submits successfully', async () => {
    mockUpdate.mockResolvedValue({});
    
    renderWithProviders(
      <EditDealerModal dealer={mockDealer} onClose={mockOnClose} onSuccess={mockOnSuccess} />
    );

    const user = userEvent.setup();
    await user.clear(screen.getByLabelText(/Name/i));
    await user.type(screen.getByLabelText(/Name/i), 'Updated Dealer');
    await user.click(screen.getByRole('button', { name: /Save Changes/i }));

    await waitFor(() => {
      expect(mockUpdate).toHaveBeenCalledWith({ id: '1', data: { name: 'Updated Dealer' } });
      expect(mockOnSuccess).toHaveBeenCalled();
    });
  });
});
