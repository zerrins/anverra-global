import { describe, it, expect, vi, beforeEach } from 'vitest';
import { screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { CreateInsurerModal } from './CreateInsurerModal';
import { renderWithProviders } from '../test/utils';
import * as insurerController from '../api/endpoints/insurer-controller/insurer-controller';

vi.mock('../api/endpoints/insurer-controller/insurer-controller', async (importOriginal) => {
  const actual = await importOriginal() as any;
  return {
    ...actual,
    useCreateInsurer: vi.fn(),
  };
});

describe('CreateInsurerModal', () => {
  const onClose = vi.fn();
  const onSuccess = vi.fn();
  const mockMutateAsync = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(insurerController.useCreateInsurer).mockReturnValue({
      mutateAsync: mockMutateAsync,
    } as any);
  });

  it('validates required fields', async () => {
    const user = userEvent.setup();
    renderWithProviders(<CreateInsurerModal onClose={onClose} onSuccess={onSuccess} />);

    await user.click(screen.getByRole('button', { name: /Create/i }));

    await waitFor(() => {
      expect(screen.getByText('Name is required')).toBeInTheDocument();
    });
    expect(mockMutateAsync).not.toHaveBeenCalled();
  });

  it('submits successfully', async () => {
    mockMutateAsync.mockResolvedValue({});
    const user = userEvent.setup();
    renderWithProviders(<CreateInsurerModal onClose={onClose} onSuccess={onSuccess} />);

    await user.type(screen.getByLabelText(/Name/i), 'New Insurer');
    await user.click(screen.getByRole('button', { name: /Create/i }));

    await waitFor(() => {
      expect(mockMutateAsync).toHaveBeenCalledWith({
        data: { name: 'New Insurer' }
      });
      expect(onSuccess).toHaveBeenCalled();
    });
  });
});
