import { useState } from 'react';
import { useListProducts, useActivateProduct, useDeactivateProduct } from '../api/endpoints/product-controller/product-controller';
import { ApiErrorAlert } from '../components/ApiErrorAlert';
import { Plus, Edit2, Play, Square } from 'lucide-react';
import { CreateProductModal } from '../components/CreateProductModal';
import { EditProductModal } from '../components/EditProductModal';
import { useRole } from '../auth/useRole';
import type { ProductResponse } from '../api/model';

const ProductList = () => {
  const { isAdmin } = useRole();
  const [page, setPage] = useState(0);
  const [nameFilter, setNameFilter] = useState('');
  const [categoryFilter, setCategoryFilter] = useState('');
  const [statusFilter, setStatusFilter] = useState('');

  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [editingProduct, setEditingProduct] = useState<ProductResponse | null>(null);

  const { data, isLoading, error, refetch } = useListProducts({
    name: nameFilter || undefined,
    category: categoryFilter || undefined,
    status: statusFilter || undefined,
    pageable: {
      page,
      size: 10,
      sort: ['createdAt,desc'],
    }
  });

  const { mutateAsync: activateProduct, isPending: isActivating } = useActivateProduct();
  const { mutateAsync: deactivateProduct, isPending: isDeactivating } = useDeactivateProduct();

  const handleToggleStatus = async (product: ProductResponse) => {
    try {
      if (product.status === 'ACTIVE') {
        await deactivateProduct({ id: product.id! });
      } else {
        await activateProduct({ id: product.id! });
      }
      refetch();
    } catch {
      // Handled by ApiErrorAlert or global error handling
    }
  };

  return (
    <div>
      <div className="page-header flex justify-between items-center">
        <h1 className="page-title">Products</h1>
        {isAdmin && (
          <button className="btn btn-primary flex items-center gap-2" onClick={() => setIsCreateModalOpen(true)}>
            <Plus size={16} />
            Create Product
          </button>
        )}
      </div>

      <div className="card p-4 mb-4 flex gap-4 flex-wrap">
        <div className="form-group mb-0 flex-1 min-w-[200px]">
          <label className="form-label text-xs">Name</label>
          <input
            type="text"
            className="form-input"
            placeholder="Search by name..."
            value={nameFilter}
            onChange={e => { setNameFilter(e.target.value); setPage(0); }}
          />
        </div>
        <div className="form-group mb-0 flex-1 min-w-[200px]">
          <label className="form-label text-xs">Category</label>
          <select
            className="form-input"
            value={categoryFilter}
            onChange={e => { setCategoryFilter(e.target.value); setPage(0); }}
          >
            <option value="">All Categories</option>
            <option value="LIFE_INSURANCE">Life Insurance</option>
            <option value="HEALTH_INSURANCE">Health Insurance</option>
            <option value="MOTOR_INSURANCE">Motor Insurance</option>
            <option value="TRAVEL_INSURANCE">Travel Insurance</option>
            <option value="PROPERTY_INSURANCE">Property Insurance</option>
            <option value="FIRE_INSURANCE">Fire Insurance</option>
            <option value="MARINE_INSURANCE">Marine Insurance</option>
            <option value="LIABILITY_INSURANCE">Liability Insurance</option>
            <option value="ENGINEERING_INSURANCE">Engineering Insurance</option>
            <option value="COMMERCIAL_INSURANCE">Commercial Insurance</option>
          </select>
        </div>
        <div className="form-group mb-0 w-48">
          <label className="form-label text-xs">Status</label>
          <select
            className="form-input"
            value={statusFilter}
            onChange={e => { setStatusFilter(e.target.value); setPage(0); }}
          >
            <option value="">All Statuses</option>
            <option value="ACTIVE">Active</option>
            <option value="INACTIVE">Inactive</option>
          </select>
        </div>
      </div>

      <ApiErrorAlert error={error} />

      <div className="card table-container">
        {isLoading ? (
          <div className="flex justify-center p-8">
            <div className="spinner spinner-md"></div>
          </div>
        ) : (
          <table className="w-full">
            <thead>
              <tr>
                <th>Name</th>
                <th>Category</th>
                <th>Status</th>
                {isAdmin && <th>Actions</th>}
              </tr>
            </thead>
            <tbody>
              {data?.data.content?.length === 0 ? (
                <tr>
                  <td colSpan={isAdmin ? 4 : 3} className="text-center text-muted italic">
                    No products found.
                  </td>
                </tr>
              ) : (
                data?.data.content?.map((product: ProductResponse) => (
                  <tr key={product.id}>
                    <td className="font-medium">{product.name}</td>
                    <td>{product.category?.replace(/_/g, ' ')}</td>
                    <td>
                      <span className={`badge ${
                        product.status === 'ACTIVE' ? 'badge-success' : 'badge-neutral'
                      }`}>
                        {product.status}
                      </span>
                    </td>
                    {isAdmin && (
                      <td>
                        <div className="flex gap-2">
                          <button
                            className="btn btn-secondary py-1 px-2 text-xs"
                            onClick={() => setEditingProduct(product)}
                          >
                            <Edit2 size={14} /> Edit
                          </button>
                          <button
                            className={`btn py-1 px-2 text-xs flex items-center gap-1 ${product.status === 'ACTIVE' ? 'btn-danger' : 'btn-success bg-success text-white hover:bg-opacity-90'}`}
                            onClick={() => handleToggleStatus(product)}
                            disabled={isActivating || isDeactivating}
                          >
                            {product.status === 'ACTIVE' ? <Square size={14} /> : <Play size={14} />}
                            {product.status === 'ACTIVE' ? 'Deactivate' : 'Activate'}
                          </button>
                        </div>
                      </td>
                    )}
                  </tr>
                ))
              )}
            </tbody>
          </table>
        )}
      </div>

      {data?.data && data.data.totalPages && data.data.totalPages > 1 && (
        <div className="flex justify-between items-center mt-4 p-2">
          <div className="text-sm text-muted">
            Showing page {data.data.number! + 1} of {data.data.totalPages}
          </div>
          <div className="flex gap-2">
            <button
              className="btn btn-secondary text-sm px-3"
              disabled={data.data.first}
              onClick={() => setPage(p => Math.max(0, p - 1))}
            >
              Previous
            </button>
            <button
              className="btn btn-secondary text-sm px-3"
              disabled={data.data.last}
              onClick={() => setPage(p => p + 1)}
            >
              Next
            </button>
          </div>
        </div>
      )}

      {isCreateModalOpen && (
        <CreateProductModal
          onClose={() => setIsCreateModalOpen(false)}
          onSuccess={() => {
            setIsCreateModalOpen(false);
            refetch();
          }}
        />
      )}

      {editingProduct && (
        <EditProductModal
          product={editingProduct}
          onClose={() => setEditingProduct(null)}
          onSuccess={() => {
            setEditingProduct(null);
            refetch();
          }}
        />
      )}
    </div>
  );
};

export default ProductList;
