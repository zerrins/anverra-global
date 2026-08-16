import { describe, it, expect, vi, beforeEach } from 'vitest';
import { screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { EditInsurerModal } from './EditInsurerModal';
import { renderWithProviders } from '../test/utils';
import * as insurerController from '../api/endpoints/insurer-controller/insurer-controller';

vi.mock('../api/endpoints/insurer-controller/insurer-controller', async (importOriginal) => {
  const actual = await importOriginal() as any;
  return {
    ...actual,
    useUpdateInsurer: vi.fn(),
  };
});

describe('EditInsurerModal', () => {
  const onClose = vi.fn();
  const onSuccess = vi.fn();
  const mockMutateAsync = vi.fn();
  
  const mockInsurer = {
    id: '123',
    name: 'Existing Insurer',
    status: 'ACTIVE',
    version: 1
  };

  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(insurerController.useUpdateInsurer).mockReturnValue({
      mutateAsync: mockMutateAsync,
    } as any);
  });

  it('populates fields with initial data', () => {
    renderWithProviders(<EditInsurerModal insurer={mockInsurer} onClose={onClose} onSuccess={onSuccess} />);
    expect(screen.getByLabelText(/Name/i)).toHaveValue('Existing Insurer');
  });

  it('validates required fields', async () => {
    const user = userEvent.setup();
    renderWithProviders(<EditInsurerModal insurer={mockInsurer} onClose={onClose} onSuccess={onSuccess} />);

    await user.clear(screen.getByLabelText(/Name/i));
    await user.click(screen.getByRole('button', { name: /Save/i }));

    await waitFor(() => {
      expect(screen.getByText('Name is required')).toBeInTheDocument();
    });
    expect(mockMutateAsync).not.toHaveBeenCalled();
  });

  it('submits successfully', async () => {
    mockMutateAsync.mockResolvedValue({});
    const user = userEvent.setup();
    renderWithProviders(<EditInsurerModal insurer={mockInsurer} onClose={onClose} onSuccess={onSuccess} />);

    await user.clear(screen.getByLabelText(/Name/i));
    await user.type(screen.getByLabelText(/Name/i), 'Updated Insurer Name');
    await user.click(screen.getByRole('button', { name: /Save/i }));

    await waitFor(() => {
      expect(mockMutateAsync).toHaveBeenCalledWith({
        id: '123',
        data: { name: 'Updated Insurer Name', version: 1 }
      });
      expect(onSuccess).toHaveBeenCalled();
    });
  });
});
