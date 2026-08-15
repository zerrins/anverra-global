
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useListPolicies } from '../api/endpoints/policy-controller/policy-controller';
import { ApiErrorAlert } from '../components/ApiErrorAlert';
import { Plus, Eye } from 'lucide-react';
import { CreatePolicyModal } from '../components/CreatePolicyModal';

const PolicyList = () => {
  const navigate = useNavigate();
  const [page, setPage] = useState(0);
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);

  const { data, isLoading, error, refetch } = useListPolicies({
    pageable: {
      page,
      size: 10,
      sort: ['createdAt,desc'],
    }
  });

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Policies</h1>
        <button className="btn btn-primary" onClick={() => setIsCreateModalOpen(true)}>
          <Plus size={16} />
          Create Policy
        </button>
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
                <th>Policy Number</th>
                <th>Status</th>
                <th>Premium</th>
                <th>Customer ID</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {data?.data.content?.length === 0 ? (
                <tr>
                  <td colSpan={5} className="text-center text-muted italic">
                    No policies found.
                  </td>
                </tr>
              ) : (
                data?.data.content?.map((policy: any) => (
                  <tr key={policy.policyId}>
                    <td className="font-medium">{policy.policyNumber}</td>
                    <td>
                      <span className={`badge ${
                        policy.status === 'ACTIVE' ? 'badge-success' :
                        policy.status === 'DRAFT' ? 'badge-warning' : 'badge-neutral'
                      }`}>
                        {policy.status}
                      </span>
                    </td>
                    <td>${policy.premium?.toLocaleString() || '0.00'}</td>
                    <td className="text-sm font-mono text-muted">
                      {policy.customerId?.substring(0, 8)}...
                    </td>
                    <td>
                      <button 
                        className="btn btn-secondary py-1 px-2 text-xs"
                        onClick={() => navigate(`/policies/${policy.policyId}`)}
                      >
                        <Eye size={14} /> View
                      </button>
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
        <CreatePolicyModal 
          onClose={() => setIsCreateModalOpen(false)} 
          onSuccess={() => {
            setIsCreateModalOpen(false);
            refetch();
          }} 
        />
      )}
    </div>
  );
};

export default PolicyList;
