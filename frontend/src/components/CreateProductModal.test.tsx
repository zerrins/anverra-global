import { describe, it, expect, vi, beforeEach } from 'vitest';
import { screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { CreateProductModal } from './CreateProductModal';
import { renderWithProviders } from '../test/utils';
import * as productController from '../api/endpoints/product-controller/product-controller';

vi.mock('../api/endpoints/product-controller/product-controller', async (importOriginal) => {
  const actual = await importOriginal() as any;
  return {
    ...actual,
    useCreateProduct: vi.fn(),
  };
});

describe('CreateProductModal', () => {
  const onClose = vi.fn();
  const onSuccess = vi.fn();
  const mockMutateAsync = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(productController.useCreateProduct).mockReturnValue({
      mutateAsync: mockMutateAsync,
    } as any);
  });

  it('validates required fields', async () => {
    const user = userEvent.setup();
    renderWithProviders(<CreateProductModal onClose={onClose} onSuccess={onSuccess} />);

    await user.click(screen.getByRole('button', { name: /Create/i }));

    await waitFor(() => {
      expect(screen.getByText('Name is required')).toBeInTheDocument();
      expect(screen.getByText('Category is required')).toBeInTheDocument();
    });
    expect(mockMutateAsync).not.toHaveBeenCalled();
  });

  it('submits successfully', async () => {
    mockMutateAsync.mockResolvedValue({});
    const user = userEvent.setup();
    renderWithProviders(<CreateProductModal onClose={onClose} onSuccess={onSuccess} />);

    await user.type(screen.getByLabelText(/Name/i), 'New Product');
    await user.selectOptions(screen.getByLabelText(/Category/i), 'LIFE_INSURANCE');

    await user.click(screen.getByRole('button', { name: /Create/i }));

    await waitFor(() => {
      expect(mockMutateAsync).toHaveBeenCalledWith({
        data: { name: 'New Product', category: 'LIFE_INSURANCE' }
      });
      expect(onSuccess).toHaveBeenCalled();
    });
  });
});
