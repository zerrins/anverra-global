import { useState } from 'react';
import { useListDealers, useActivateDealer, useDeactivateDealer } from '../api/endpoints/organization-management-controller/organization-management-controller';
import { ApiErrorAlert } from '../components/ApiErrorAlert';
import { Plus, Edit2, Play, Square, Eye } from 'lucide-react';
import { CreateDealerModal } from '../components/CreateDealerModal';
import { EditDealerModal } from '../components/EditDealerModal';
import type { DealerResponse } from '../api/model';
import { useRole } from '../auth/useRole';
import { Link } from 'react-router-dom';

const DealerList = () => {
  const { isAdmin } = useRole();
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [editingDealer, setEditingDealer] = useState<DealerResponse | null>(null);

  const { data, isLoading, error, refetch } = useListDealers();

  const { mutateAsync: activateDealer, isPending: isActivating } = useActivateDealer();
  const { mutateAsync: deactivateDealer, isPending: isDeactivating } = useDeactivateDealer();

  const handleToggleStatus = async (dealer: DealerResponse) => {
    try {
      if (dealer.status === 'ACTIVE') {
        await deactivateDealer({ id: dealer.id! });
      } else {
        await activateDealer({ id: dealer.id! });
      }
      refetch();
    } catch {
      // Handled by ApiErrorAlert or global error handler
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Dealers</h1>
        {isAdmin && (
          <button className="btn btn-primary" onClick={() => setIsCreateModalOpen(true)}>
            <Plus size={16} />
            Create Dealer
          </button>
        )}
      </div>

      <ApiErrorAlert error={error} />

      <div className="card table-container mt-4">
        {isLoading ? (
          <div className="flex justify-center p-8">
            <div className="spinner spinner-md"></div>
          </div>
        ) : (
          <table className="w-full">
            <thead>
              <tr>
                <th>Name</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {!data?.data || data.data.length === 0 ? (
                <tr>
                  <td colSpan={3} className="text-center text-muted italic p-4">
                    No dealers found.
                  </td>
                </tr>
              ) : (
                data.data.map((dealer: DealerResponse) => (
                  <tr key={dealer.id}>
                    <td className="font-medium">{dealer.name}</td>
                    <td>
                      <span className={`badge ${
                        dealer.status === 'ACTIVE' ? 'badge-success' : 'badge-neutral'
                      }`}>
                        {dealer.status}
                      </span>
                    </td>
                    <td>
                      <div className="flex gap-2">
                        <Link 
                          to={`/dealers/${dealer.id}`}
                          className="btn btn-secondary py-1 px-2 text-xs"
                        >
                          <Eye size={14} /> View
                        </Link>
                        {isAdmin && (
                          <>
                            <button 
                              className="btn btn-secondary py-1 px-2 text-xs"
                              onClick={() => setEditingDealer(dealer)}
                            >
                              <Edit2 size={14} /> Edit
                            </button>
                            <button 
                              className={`btn py-1 px-2 text-xs ${dealer.status === 'ACTIVE' ? 'btn-danger' : 'btn-success bg-success text-white hover:bg-opacity-90'}`}
                              onClick={() => handleToggleStatus(dealer)}
                              disabled={isActivating || isDeactivating}
                            >
                              {dealer.status === 'ACTIVE' ? <Square size={14} /> : <Play size={14} />} 
                              {dealer.status === 'ACTIVE' ? ' Deactivate' : ' Activate'}
                            </button>
                          </>
                        )}
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        )}
      </div>

      {isCreateModalOpen && (
        <CreateDealerModal 
          onClose={() => setIsCreateModalOpen(false)} 
          onSuccess={() => {
            setIsCreateModalOpen(false);
            refetch();
          }} 
        />
      )}

      {editingDealer && (
        <EditDealerModal
          dealer={editingDealer}
          onClose={() => setEditingDealer(null)}
          onSuccess={() => {
            setEditingDealer(null);
            refetch();
          }}
        />
      )}
    </div>
  );
};

export default DealerList;
