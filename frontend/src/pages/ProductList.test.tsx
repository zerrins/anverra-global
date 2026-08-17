import { describe, it, expect, vi, beforeEach } from 'vitest';
import { screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import ProductList from './ProductList';
import { renderWithProviders } from '../test/utils';
import * as productController from '../api/endpoints/product-controller/product-controller';
import * as useRole from '../auth/useRole';

vi.mock('../api/endpoints/product-controller/product-controller', async (importOriginal) => {
  const actual = await importOriginal() as any;
  return {
    ...actual,
    useListProducts: vi.fn(),
    useActivateProduct: vi.fn(),
    useDeactivateProduct: vi.fn(),
  };
});

vi.mock('../auth/useRole', () => ({
  useRole: vi.fn(),
}));

describe('ProductList', () => {
  const mockRefetch = vi.fn();
  const mockActivate = vi.fn();
  const mockDeactivate = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(productController.useActivateProduct).mockReturnValue({
      mutateAsync: mockActivate,
    } as any);
    vi.mocked(productController.useDeactivateProduct).mockReturnValue({
      mutateAsync: mockDeactivate,
    } as any);
    vi.mocked(useRole.useRole).mockReturnValue({ isAdmin: true } as any);
  });

  it('renders products successfully', async () => {
    vi.mocked(productController.useListProducts).mockReturnValue({
      data: {
        data: {
          content: [
            { id: '1', name: 'Product One', category: 'LIFE_INSURANCE', status: 'ACTIVE', version: 1 },
            { id: '2', name: 'Product Two', category: 'HEALTH_INSURANCE', status: 'INACTIVE', version: 1 }
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

    renderWithProviders(<ProductList />);

    await waitFor(() => {
      expect(screen.getByText('Product One')).toBeInTheDocument();
      expect(screen.getByText('Product Two')).toBeInTheDocument();
    });
  });

  it('handles empty state', async () => {
    vi.mocked(productController.useListProducts).mockReturnValue({
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

    renderWithProviders(<ProductList />);

    await waitFor(() => {
      expect(screen.getByText('No products found.')).toBeInTheDocument();
    });
  });

  it('toggles product status for admin', async () => {
    vi.mocked(productController.useListProducts).mockReturnValue({
      data: {
        data: {
          content: [
            { id: '1', name: 'Product One', category: 'LIFE_INSURANCE', status: 'ACTIVE', version: 1 }
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
    renderWithProviders(<ProductList />);

    const deactivateBtn = await screen.findByRole('button', { name: /Deactivate/i });
    await user.click(deactivateBtn);

    expect(mockDeactivate).toHaveBeenCalledWith({ id: '1' });
    await waitFor(() => {
      expect(mockRefetch).toHaveBeenCalled();
    });
  });

  it('hides admin actions for non-admin users', async () => {
    vi.mocked(useRole.useRole).mockReturnValue({ isAdmin: false } as any);
    vi.mocked(productController.useListProducts).mockReturnValue({
      data: {
        data: {
          content: [
            { id: '1', name: 'Product One', category: 'LIFE_INSURANCE', status: 'ACTIVE', version: 1 }
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

    renderWithProviders(<ProductList />);

    await waitFor(() => {
      expect(screen.queryByRole('button', { name: /Deactivate/i })).not.toBeInTheDocument();
      expect(screen.queryByRole('button', { name: /Edit/i })).not.toBeInTheDocument();
      expect(screen.queryByRole('button', { name: /Create Product/i })).not.toBeInTheDocument();
    });
  });
});
