import { useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { useGetDealer, useListBranches, useActivateBranch, useDeactivateBranch } from '../api/endpoints/organization-management-controller/organization-management-controller';
import { ApiErrorAlert } from '../components/ApiErrorAlert';
import { ArrowLeft, Plus, Edit2, Play, Square } from 'lucide-react';
import { CreateBranchModal } from '../components/CreateBranchModal';
import { EditBranchModal } from '../components/EditBranchModal';
import type { BranchResponse } from '../api/model';
import { useRole } from '../auth/useRole';

const DealerDetails = () => {
  const { id } = useParams<{ id: string }>();
  const { canManageOrganization } = useRole();
  
  const { data: dealer, isLoading: isLoadingDealer, error: dealerError } = useGetDealer(id as string, { query: { enabled: !!id } });
  
  const { data: branches, isLoading: isLoadingBranches, error: branchesError, refetch: refetchBranches } = useListBranches({ dealerId: id as string }, { query: { enabled: !!id } });

  const [isCreateBranchModalOpen, setIsCreateBranchModalOpen] = useState(false);
  const [editingBranch, setEditingBranch] = useState<BranchResponse | null>(null);

  const { mutateAsync: activateBranch, isPending: isActivating } = useActivateBranch();
  const { mutateAsync: deactivateBranch, isPending: isDeactivating } = useDeactivateBranch();

  const handleToggleStatus = async (branch: BranchResponse) => {
    try {
      if (branch.status === 'ACTIVE') {
        await deactivateBranch({ id: branch.id! });
      } else {
        await activateBranch({ id: branch.id! });
      }
      refetchBranches();
    } catch {
      // Error handled by ApiErrorAlert
    }
  };

  if (isLoadingDealer) {
    return (
      <div className="flex justify-center p-8">
        <div className="spinner spinner-md"></div>
      </div>
    );
  }

  if (dealerError) {
    return <ApiErrorAlert error={dealerError} />;
  }

  if (!dealer?.data) {
    return <div className="p-4">Dealer not found</div>;
  }

  const dealerData = dealer.data;

  return (
    <div>
      <div className="mb-4">
        <Link to="/dealers" className="text-secondary hover:underline flex items-center gap-1 text-sm">
          <ArrowLeft size={16} />
          Back to Dealers
        </Link>
      </div>

      <div className="page-header">
        <div>
          <h1 className="page-title">{dealerData.name}</h1>
          <span className={`badge mt-2 ${dealerData.status === 'ACTIVE' ? 'badge-success' : 'badge-neutral'}`}>
            {dealerData.status}
          </span>
        </div>
      </div>

      <div className="card mt-8">
        <div className="card-header flex justify-between items-center">
          <h2 className="text-xl font-semibold">Branches</h2>
          {canManageOrganization && (
            <button className="btn btn-primary" onClick={() => setIsCreateBranchModalOpen(true)}>
              <Plus size={16} />
              Create Branch
            </button>
          )}
        </div>
        
        <ApiErrorAlert error={branchesError} />

        <div className="p-4">
          {isLoadingBranches ? (
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
                {!branches?.data || branches.data.length === 0 ? (
                  <tr>
                    <td colSpan={3} className="text-center text-muted italic p-4">
                      No branches found for this dealer.
                    </td>
                  </tr>
                ) : (
                  branches.data.map((branch: BranchResponse) => (
                    <tr key={branch.id}>
                      <td className="font-medium">{branch.name}</td>
                      <td>
                        <span className={`badge ${
                          branch.status === 'ACTIVE' ? 'badge-success' : 'badge-neutral'
                        }`}>
                          {branch.status}
                        </span>
                      </td>
                      <td>
                        <div className="flex gap-2">
                          {canManageOrganization && (
                            <>
                              <button 
                                className="btn btn-secondary py-1 px-2 text-xs"
                                onClick={() => setEditingBranch(branch)}
                              >
                                <Edit2 size={14} /> Edit
                              </button>
                              <button 
                                className={`btn py-1 px-2 text-xs ${branch.status === 'ACTIVE' ? 'btn-danger' : 'btn-success bg-success text-white hover:bg-opacity-90'}`}
                                onClick={() => handleToggleStatus(branch)}
                                disabled={isActivating || isDeactivating}
                              >
                                {branch.status === 'ACTIVE' ? <Square size={14} /> : <Play size={14} />} 
                                {branch.status === 'ACTIVE' ? ' Deactivate' : ' Activate'}
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
      </div>

      {isCreateBranchModalOpen && (
        <CreateBranchModal 
          dealerId={id as string}
          onClose={() => setIsCreateBranchModalOpen(false)} 
          onSuccess={() => {
            setIsCreateBranchModalOpen(false);
            refetchBranches();
          }} 
        />
      )}

      {editingBranch && (
        <EditBranchModal
          branch={editingBranch}
          onClose={() => setEditingBranch(null)}
          onSuccess={() => {
            setEditingBranch(null);
            refetchBranches();
          }}
        />
      )}
    </div>
  );
};

export default DealerDetails;
