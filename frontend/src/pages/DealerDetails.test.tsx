import { describe, it, expect, vi, beforeEach } from 'vitest';
import { screen, waitFor } from '@testing-library/react';
import DealerDetails from './DealerDetails';
import { renderWithProviders } from '../test/utils';
import * as dealerController from '../api/endpoints/organization-management-controller/organization-management-controller';
import { Route, Routes } from 'react-router-dom';

vi.mock('../api/endpoints/organization-management-controller/organization-management-controller', async (importOriginal) => {
  const actual = await importOriginal() as any;
  return {
    ...actual,
    useGetDealer: vi.fn(),
    useListBranches: vi.fn(),
    useActivateBranch: vi.fn(),
    useDeactivateBranch: vi.fn(),
  };
});

describe('DealerDetails', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(dealerController.useActivateBranch).mockReturnValue({
      mutateAsync: vi.fn(),
    } as any);
    vi.mocked(dealerController.useDeactivateBranch).mockReturnValue({
      mutateAsync: vi.fn(),
    } as any);
  });

  it('renders dealer details and branches successfully', async () => {
    vi.mocked(dealerController.useGetDealer).mockReturnValue({
      data: { data: { id: '1', name: 'Dealer One', status: 'ACTIVE' } },
      isLoading: false,
      error: null,
    } as any);

    vi.mocked(dealerController.useListBranches).mockReturnValue({
      data: {
        data: [
          { id: '11', name: 'Branch One', status: 'ACTIVE' }
        ]
      },
      isLoading: false,
      error: null,
    } as any);

    renderWithProviders(
      <Routes>
        <Route path="/:id" element={<DealerDetails />} />
      </Routes>,
      { route: '/1' }
    );

    await waitFor(() => {
      expect(screen.getByText('Dealer One')).toBeInTheDocument();
      expect(screen.getByText('Branch One')).toBeInTheDocument();
    });
  });
});
