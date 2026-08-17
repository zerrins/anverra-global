import { describe, it, expect, vi, beforeEach } from 'vitest';
import { screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { EditBranchModal } from './EditBranchModal';
import { renderWithProviders } from '../test/utils';
import * as dealerController from '../api/endpoints/organization-management-controller/organization-management-controller';

vi.mock('../api/endpoints/organization-management-controller/organization-management-controller', async (importOriginal) => {
  const actual = await importOriginal() as any;
  return {
    ...actual,
    useUpdateBranch: vi.fn(),
  };
});

describe('EditBranchModal', () => {
  const mockUpdate = vi.fn();
  const mockOnClose = vi.fn();
  const mockOnSuccess = vi.fn();
  
  const mockBranch: any = {
    id: '11',
    name: 'Existing Branch',
    status: 'ACTIVE',
    dealerId: '1'
  };

  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(dealerController.useUpdateBranch).mockReturnValue({
      mutateAsync: mockUpdate,
      isPending: false,
    } as any);
  });

  it('renders existing values', () => {
    renderWithProviders(
      <EditBranchModal branch={mockBranch} onClose={mockOnClose} onSuccess={mockOnSuccess} />
    );
    expect(screen.getByDisplayValue('Existing Branch')).toBeInTheDocument();
  });

  it('submits successfully', async () => {
    mockUpdate.mockResolvedValue({});
    
    renderWithProviders(
      <EditBranchModal branch={mockBranch} onClose={mockOnClose} onSuccess={mockOnSuccess} />
    );

    const user = userEvent.setup();
    await user.clear(screen.getByLabelText(/Name/i));
    await user.type(screen.getByLabelText(/Name/i), 'Updated Branch');
    await user.click(screen.getByRole('button', { name: /Save Changes/i }));

    await waitFor(() => {
      expect(mockUpdate).toHaveBeenCalledWith({ id: '11', data: { name: 'Updated Branch' } });
      expect(mockOnSuccess).toHaveBeenCalled();
    });
  });
});
