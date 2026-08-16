import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { 
  useGetCustomer, 
  useActivateCustomer, 
  useDeactivateCustomer
} from '../api/endpoints/customer-controller/customer-controller';
import { ApiErrorAlert } from '../components/ApiErrorAlert';
import { EditCustomerModal } from '../components/EditCustomerModal';
import { ArrowLeft, Play, Square, Edit } from 'lucide-react';

const CustomerDetails = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);

  const { data: customer, isLoading, error, refetch } = useGetCustomer(id!);
  
  const { mutateAsync: activate, isPending: isActivating } = useActivateCustomer();
  const { mutateAsync: deactivate, isPending: isDeactivating } = useDeactivateCustomer();

  const handleLifecycle = async (action: 'activate' | 'deactivate') => {
    try {
      if (action === 'activate') {
        await activate({ id: id! });
      } else if (action === 'deactivate') {
        await deactivate({ id: id! });
      }
      refetch();
    } catch {
      // Errors handled by ApiErrorAlert or local state if needed
    }
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center py-12">
        <div className="spinner spinner-lg"></div>
      </div>
    );
  }

  const custData = customer?.data;

  return (
    <div>
      <div className="mb-4">
        <button 
          onClick={() => navigate('/customers')} 
          className="text-muted hover:text-primary flex items-center gap-1 text-sm font-medium transition-colors"
        >
          <ArrowLeft size={16} /> Back to Customers
        </button>
      </div>

      <div className="page-header">
        <h1 className="page-title">{custData?.name}</h1>
        <div className="flex gap-2">
          {custData?.status === 'INACTIVE' && (
            <button 
              className="btn btn-success bg-success text-white hover:bg-opacity-90"
              onClick={() => handleLifecycle('activate')}
              disabled={isActivating}
            >
              <Play size={16} /> Activate
            </button>
          )}
          {custData?.status === 'ACTIVE' && (
            <button 
              className="btn btn-danger"
              onClick={() => handleLifecycle('deactivate')}
              disabled={isDeactivating}
            >
              <Square size={16} /> Deactivate
            </button>
          )}
          <button 
            className="btn btn-secondary"
            onClick={() => setIsEditModalOpen(true)}
          >
            <Edit size={16} /> Edit
          </button>
        </div>
      </div>

      <ApiErrorAlert error={error} />

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="card p-6">
          <h2 className="text-lg font-bold mb-4">Customer Information</h2>
          <div className="space-y-4">
            <div>
              <div className="text-sm text-muted">Customer Type</div>
              <div className="font-semibold">{custData?.customerType}</div>
            </div>
            <div>
              <div className="text-sm text-muted">Status</div>
              <span className={`badge ${
                custData?.status === 'ACTIVE' ? 'badge-success' : 'badge-neutral'
              }`}>
                {custData?.status}
              </span>
            </div>
            <div>
              <div className="text-sm text-muted">Contact Info</div>
              <div className="font-semibold">{custData?.contactInfo}</div>
            </div>
            <div>
              <div className="text-sm text-muted">Address Info</div>
              <div className="font-semibold">{custData?.addressInfo}</div>
            </div>
          </div>
        </div>

        <div className="card p-6">
          <h2 className="text-lg font-bold mb-4">
            {custData?.customerType === 'INDIVIDUAL' ? 'Individual Info' : 'Business Info'}
          </h2>
          <div className="space-y-4">
            {custData?.customerType === 'INDIVIDUAL' && (
              <div>
                <div className="text-sm text-muted">Individual Information</div>
                <div className="font-semibold">{custData?.individualInfo || 'N/A'}</div>
              </div>
            )}
            {custData?.customerType === 'ORGANIZATION' && (
              <div>
                <div className="text-sm text-muted">Business Information</div>
                <div className="font-semibold">{custData?.businessInfo || 'N/A'}</div>
              </div>
            )}
            <div>
              <div className="text-sm text-muted">Created At</div>
              <div className="text-sm">{custData?.createdAt ? new Date(custData.createdAt).toLocaleString() : 'N/A'}</div>
            </div>
            <div>
              <div className="text-sm text-muted">Updated At</div>
              <div className="text-sm">{custData?.updatedAt ? new Date(custData.updatedAt).toLocaleString() : 'N/A'}</div>
            </div>
          </div>
        </div>
      </div>

      {isEditModalOpen && custData && (
        <EditCustomerModal 
          customer={custData}
          onClose={() => setIsEditModalOpen(false)} 
          onSuccess={() => {
            setIsEditModalOpen(false);
            refetch();
          }} 
        />
      )}
    </div>
  );
};

export default CustomerDetails;
