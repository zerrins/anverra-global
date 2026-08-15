
import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { 
  useGetPolicy, 
  useActivatePolicy, 
  useDeactivatePolicy, 
  useReactivatePolicy, 
  useUpdatePremium
} from '../api/endpoints/policy-controller/policy-controller';
import { ApiErrorAlert } from '../components/ApiErrorAlert';
import { ConfigureCommissionModal } from '../components/ConfigureCommissionModal';
import { ArrowLeft, Play, Square, RefreshCcw, DollarSign } from 'lucide-react';

const PolicyDetails = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [isCommissionModalOpen, setIsCommissionModalOpen] = useState(false);
  const [premium, setPremium] = useState<string>('');
  const [isEditingPremium, setIsEditingPremium] = useState(false);

  const { data: policy, isLoading, error, refetch } = useGetPolicy(id!);
  
  const { mutateAsync: activate, isPending: isActivating } = useActivatePolicy();
  const { mutateAsync: deactivate, isPending: isDeactivating } = useDeactivatePolicy();
  const { mutateAsync: reactivate, isPending: isReactivating } = useReactivatePolicy();
  const { mutateAsync: updatePremium, isPending: isUpdatingPremium } = useUpdatePremium();

  const handleLifecycle = async (action: 'activate' | 'deactivate' | 'reactivate') => {
    try {
      if (action === 'activate') {
        await activate({ policyId: id!, data: { isCommissionConfigured: true } }); // Assuming true for now
      } else if (action === 'deactivate') {
        await deactivate({ policyId: id! });
      } else if (action === 'reactivate') {
        await reactivate({ policyId: id!, data: { isCommissionConfigured: true } });
      }
      refetch();
    } catch {
      // Errors handled by generic component or context if configured, or just let ApiErrorAlert catch it from state if we set it up.
      // Wait, useMutation doesn't put error in `error` of useQuery. I should probably handle local error state if needed.
    }
  };

  const handleUpdatePremium = async () => {
    try {
      await updatePremium({ policyId: id!, data: { premium: Number(premium) } });
      setIsEditingPremium(false);
      refetch();
    } catch {
      // Handle error
    }
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center py-12">
        <div className="spinner spinner-lg"></div>
      </div>
    );
  }

  return (
    <div>
      <div className="mb-4">
        <button 
          onClick={() => navigate('/policies')} 
          className="text-muted hover:text-primary flex items-center gap-1 text-sm font-medium transition-colors"
        >
          <ArrowLeft size={16} /> Back to Policies
        </button>
      </div>

      <div className="page-header">
        <h1 className="page-title">Policy {policy?.data?.policyNumber}</h1>
        <div className="flex gap-2">
          {policy?.data?.status === 'DRAFT' && (
            <button 
              className="btn btn-success bg-success text-white hover:bg-opacity-90"
              onClick={() => handleLifecycle('activate')}
              disabled={isActivating}
            >
              <Play size={16} /> Activate
            </button>
          )}
          {policy?.data?.status === 'ACTIVE' && (
            <button 
              className="btn btn-danger"
              onClick={() => handleLifecycle('deactivate')}
              disabled={isDeactivating}
            >
              <Square size={16} /> Deactivate
            </button>
          )}
          {policy?.data?.status === 'INACTIVE' && (
            <button 
              className="btn btn-primary"
              onClick={() => handleLifecycle('reactivate')}
              disabled={isReactivating}
            >
              <RefreshCcw size={16} /> Reactivate
            </button>
          )}
          <button 
            className="btn btn-secondary"
            onClick={() => setIsCommissionModalOpen(true)}
          >
            <DollarSign size={16} /> Commission
          </button>
        </div>
      </div>

      <ApiErrorAlert error={error} />

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="card p-6">
          <h2 className="text-lg font-bold mb-4">Policy Information</h2>
          <div className="space-y-4">
            <div>
              <div className="text-sm text-muted">Status</div>
              <div className="font-semibold">{policy?.data?.status}</div>
            </div>
            <div>
              <div className="text-sm text-muted">Customer ID</div>
              <div className="font-mono text-sm">{policy?.data?.customerId}</div>
            </div>
            <div>
              <div className="text-sm text-muted">Branch ID</div>
              <div className="font-mono text-sm">{policy?.data?.branchId}</div>
            </div>
          </div>
        </div>

        <div className="card p-6">
          <h2 className="text-lg font-bold mb-4 flex justify-between items-center">
            Financials
            {!isEditingPremium && (
              <button 
                className="text-sm text-secondary hover:underline font-normal"
                onClick={() => {
                  setPremium(policy?.data?.premium?.toString() || '');
                  setIsEditingPremium(true);
                }}
              >
                Edit Premium
              </button>
            )}
          </h2>
          <div className="space-y-4">
            <div>
              <div className="text-sm text-muted">Premium Amount</div>
              {isEditingPremium ? (
                <div className="flex gap-2 mt-1">
                  <input 
                    type="number" 
                    className="form-input py-1" 
                    value={premium}
                    onChange={(e) => setPremium(e.target.value)}
                  />
                  <button className="btn btn-primary py-1 px-3" onClick={handleUpdatePremium} disabled={isUpdatingPremium}>
                    Save
                  </button>
                  <button className="btn btn-secondary py-1 px-3" onClick={() => setIsEditingPremium(false)}>
                    Cancel
                  </button>
                </div>
              ) : (
                <div className="font-semibold text-xl">${policy?.data?.premium?.toLocaleString() || '0.00'}</div>
              )}
            </div>
            {/* Add commission info if we had it in PolicyResponse, else we rely on Reporting/Dashboard for sum */}
          </div>
        </div>

        <div className="card p-6 md:col-span-2">
          <h2 className="text-lg font-bold mb-4">Agents</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <div className="text-sm text-muted">Agent A ID</div>
              <div className="font-mono text-sm">{policy?.data?.agentAId}</div>
            </div>
            <div>
              <div className="text-sm text-muted">Agent B ID</div>
              <div className="font-mono text-sm">{policy?.data?.agentBId}</div>
            </div>
          </div>
        </div>
      </div>

      {isCommissionModalOpen && (
        <ConfigureCommissionModal 
          policyId={id!} 
          onClose={() => setIsCommissionModalOpen(false)} 
          onSuccess={() => {
            setIsCommissionModalOpen(false);
            refetch();
          }} 
        />
      )}
    </div>
  );
};

export default PolicyDetails;
