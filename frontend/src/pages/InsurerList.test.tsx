import { describe, it, expect, vi, beforeEach } from 'vitest';
import { screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import InsurerList from './InsurerList';
import { renderWithProviders } from '../test/utils';
import * as insurerController from '../api/endpoints/insurer-controller/insurer-controller';

vi.mock('../api/endpoints/insurer-controller/insurer-controller', async (importOriginal) => {
  const actual = await importOriginal() as any;
  return {
    ...actual,
    useListInsurers: vi.fn(),
    useActivateInsurer: vi.fn(),
    useDeactivateInsurer: vi.fn(),
  };
});

describe('InsurerList', () => {
  const mockRefetch = vi.fn();
  const mockActivate = vi.fn();
  const mockDeactivate = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(insurerController.useActivateInsurer).mockReturnValue({
      mutateAsync: mockActivate,
    } as any);
    vi.mocked(insurerController.useDeactivateInsurer).mockReturnValue({
      mutateAsync: mockDeactivate,
    } as any);
  });

  it('renders insurers successfully', async () => {
    vi.mocked(insurerController.useListInsurers).mockReturnValue({
      data: {
        data: {
          content: [
            { id: '1', name: 'Insurer One', status: 'ACTIVE', version: 1 },
            { id: '2', name: 'Insurer Two', status: 'INACTIVE', version: 1 }
          ],
          totalPages: 1,
          number: 0,
          first: true,
          last: true,
        }
      },
      isLoading: false,
      error: null,
      refetch: mockRefetch,
    } as any);

    renderWithProviders(<InsurerList />);

    await waitFor(() => {
      expect(screen.getByText('Insurer One')).toBeInTheDocument();
      expect(screen.getByText('Insurer Two')).toBeInTheDocument();
    });
  });

  it('handles empty state', async () => {
    vi.mocked(insurerController.useListInsurers).mockReturnValue({
      data: {
        data: {
          content: [],
          totalPages: 0,
          number: 0,
          first: true,
          last: true,
        }
      },
      isLoading: false,
      error: null,
      refetch: mockRefetch,
    } as any);

    renderWithProviders(<InsurerList />);

    await waitFor(() => {
      expect(screen.getByText('No insurers found.')).toBeInTheDocument();
    });
  });

  it('toggles insurer status', async () => {
    vi.mocked(insurerController.useListInsurers).mockReturnValue({
      data: {
        data: {
          content: [
            { id: '1', name: 'Insurer One', status: 'ACTIVE', version: 1 }
          ],
          totalPages: 1,
          number: 0,
          first: true,
          last: true,
        }
      },
      isLoading: false,
      error: null,
      refetch: mockRefetch,
    } as any);

    mockDeactivate.mockResolvedValue({});

    const user = userEvent.setup();
    renderWithProviders(<InsurerList />);

    const deactivateBtn = await screen.findByRole('button', { name: /Deactivate/i });
    await user.click(deactivateBtn);

    expect(mockDeactivate).toHaveBeenCalledWith({ id: '1' });
    await waitFor(() => {
      expect(mockRefetch).toHaveBeenCalled();
    });
  });
});
