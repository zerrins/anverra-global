import { describe, it, expect, vi, beforeEach } from 'vitest';
import { screen, waitFor } from '@testing-library/react';
import DealerList from './DealerList';
import { renderWithProviders } from '../test/utils';
import * as dealerController from '../api/endpoints/organization-management-controller/organization-management-controller';

vi.mock('../api/endpoints/organization-management-controller/organization-management-controller', async (importOriginal) => {
  const actual = await importOriginal() as any;
  return {
    ...actual,
    useListDealers: vi.fn(),
    useActivateDealer: vi.fn(),
    useDeactivateDealer: vi.fn(),
  };
});

describe('DealerList', () => {
  const mockRefetch = vi.fn();
  const mockActivate = vi.fn();
  const mockDeactivate = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(dealerController.useActivateDealer).mockReturnValue({
      mutateAsync: mockActivate,
    } as any);
    vi.mocked(dealerController.useDeactivateDealer).mockReturnValue({
      mutateAsync: mockDeactivate,
    } as any);
  });

  it('renders dealers successfully', async () => {
    vi.mocked(dealerController.useListDealers).mockReturnValue({
      data: {
        data: [
          { id: '1', name: 'Dealer One', status: 'ACTIVE' },
          { id: '2', name: 'Dealer Two', status: 'INACTIVE' }
        ]
      },
      isLoading: false,
      error: null,
      refetch: mockRefetch,
    } as any);

    renderWithProviders(<DealerList />);

    await waitFor(() => {
      expect(screen.getByText('Dealer One')).toBeInTheDocument();
      expect(screen.getByText('Dealer Two')).toBeInTheDocument();
    });
  });

  it('handles empty state', async () => {
    vi.mocked(dealerController.useListDealers).mockReturnValue({
      data: { data: [] },
      isLoading: false,
      error: null,
      refetch: mockRefetch,
    } as any);

    renderWithProviders(<DealerList />);

    await waitFor(() => {
      expect(screen.getByText('No dealers found.')).toBeInTheDocument();
    });
  });
});
