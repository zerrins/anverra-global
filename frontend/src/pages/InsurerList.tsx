import { useState } from 'react';
import { useListInsurers, useActivateInsurer, useDeactivateInsurer } from '../api/endpoints/insurer-controller/insurer-controller';
import { ApiErrorAlert } from '../components/ApiErrorAlert';
import { Plus, Edit2, Play, Square } from 'lucide-react';
import { CreateInsurerModal } from '../components/CreateInsurerModal.tsx';
import { EditInsurerModal } from '../components/EditInsurerModal.tsx';
import type { InsurerResponse } from '../api/model';

const InsurerList = () => {
  const [page, setPage] = useState(0);
  const [nameFilter, setNameFilter] = useState('');
  const [statusFilter, setStatusFilter] = useState('');
  
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [editingInsurer, setEditingInsurer] = useState<InsurerResponse | null>(null);

  const { data, isLoading, error, refetch } = useListInsurers({
    name: nameFilter || undefined,
    status: statusFilter || undefined,
    pageable: {
      page,
      size: 10,
      sort: ['createdAt,desc'],
    }
  });

  const { mutateAsync: activateInsurer, isPending: isActivating } = useActivateInsurer();
  const { mutateAsync: deactivateInsurer, isPending: isDeactivating } = useDeactivateInsurer();

  const handleToggleStatus = async (insurer: InsurerResponse) => {
    try {
      if (insurer.status === 'ACTIVE') {
        await deactivateInsurer({ id: insurer.id! });
      } else {
        await activateInsurer({ id: insurer.id! });
      }
      refetch();
    } catch {
      // Handled by ApiErrorAlert or global error handling if we wanted to
      // For now we'll just refetch to get the latest state or let it fail silently in UI other than perhaps an alert
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Insurers</h1>
        <button className="btn btn-primary" onClick={() => setIsCreateModalOpen(true)}>
          <Plus size={16} />
          Create Insurer
        </button>
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
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {data?.data.content?.length === 0 ? (
                <tr>
                  <td colSpan={3} className="text-center text-muted italic">
                    No insurers found.
                  </td>
                </tr>
              ) : (
                data?.data.content?.map((insurer: InsurerResponse) => (
                  <tr key={insurer.id}>
                    <td className="font-medium">{insurer.name}</td>
                    <td>
                      <span className={`badge ${
                        insurer.status === 'ACTIVE' ? 'badge-success' : 'badge-neutral'
                      }`}>
                        {insurer.status}
                      </span>
                    </td>
                    <td>
                      <div className="flex gap-2">
                        <button 
                          className="btn btn-secondary py-1 px-2 text-xs"
                          onClick={() => setEditingInsurer(insurer)}
                        >
                          <Edit2 size={14} /> Edit
                        </button>
                        <button 
                          className={`btn py-1 px-2 text-xs ${insurer.status === 'ACTIVE' ? 'btn-danger' : 'btn-success bg-success text-white hover:bg-opacity-90'}`}
                          onClick={() => handleToggleStatus(insurer)}
                          disabled={isActivating || isDeactivating}
                        >
                          {insurer.status === 'ACTIVE' ? <Square size={14} /> : <Play size={14} />} 
                          {insurer.status === 'ACTIVE' ? ' Deactivate' : ' Activate'}
                        </button>
                      </div>
                    </td>
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
        <CreateInsurerModal 
          onClose={() => setIsCreateModalOpen(false)} 
          onSuccess={() => {
            setIsCreateModalOpen(false);
            refetch();
          }} 
        />
      )}

      {editingInsurer && (
        <EditInsurerModal
          insurer={editingInsurer}
          onClose={() => setEditingInsurer(null)}
          onSuccess={() => {
            setEditingInsurer(null);
            refetch();
          }}
        />
      )}
    </div>
  );
};

export default InsurerList;
