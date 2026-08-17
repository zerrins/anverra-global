import { describe, it, expect, vi, beforeEach } from 'vitest';
import { screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { EditProductModal } from './EditProductModal';
import { renderWithProviders } from '../test/utils';
import * as productController from '../api/endpoints/product-controller/product-controller';
import type { ProductResponse } from '../api/model';

vi.mock('../api/endpoints/product-controller/product-controller', async (importOriginal) => {
  const actual = await importOriginal() as any;
  return {
    ...actual,
    useUpdateProduct: vi.fn(),
  };
});

describe('EditProductModal', () => {
  const onClose = vi.fn();
  const onSuccess = vi.fn();
  const mockMutateAsync = vi.fn();

  const mockProduct: ProductResponse = {
    id: 'prod-123',
    name: 'Existing Product',
    category: 'HEALTH_INSURANCE',
    status: 'ACTIVE',
    version: 1,
  };

  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(productController.useUpdateProduct).mockReturnValue({
      mutateAsync: mockMutateAsync,
    } as any);
  });

  it('prepopulates fields', () => {
    renderWithProviders(<EditProductModal product={mockProduct} onClose={onClose} onSuccess={onSuccess} />);
    expect(screen.getByLabelText(/Name/i)).toHaveValue('Existing Product');
    expect(screen.getByLabelText(/Category/i)).toHaveValue('HEALTH_INSURANCE');
  });

  it('submits successfully', async () => {
    mockMutateAsync.mockResolvedValue({});
    const user = userEvent.setup();
    renderWithProviders(<EditProductModal product={mockProduct} onClose={onClose} onSuccess={onSuccess} />);

    await user.clear(screen.getByLabelText(/Name/i));
    await user.type(screen.getByLabelText(/Name/i), 'Updated Product');
    await user.click(screen.getByRole('button', { name: /Save/i }));

    await waitFor(() => {
      expect(mockMutateAsync).toHaveBeenCalledWith({
        id: 'prod-123',
        data: { name: 'Updated Product', category: 'HEALTH_INSURANCE', version: 1 }
      });
      expect(onSuccess).toHaveBeenCalled();
    });
  });
});
