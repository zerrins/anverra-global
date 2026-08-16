import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useListCustomers } from '../api/endpoints/customer-controller/customer-controller';
import { ApiErrorAlert } from '../components/ApiErrorAlert';
import { Plus, Eye } from 'lucide-react';
import { CreateCustomerModal } from '../components/CreateCustomerModal';

const CustomerList = () => {
  const navigate = useNavigate();
  const [page, setPage] = useState(0);
  const [nameFilter, setNameFilter] = useState('');
  const [typeFilter, setTypeFilter] = useState('');
  const [statusFilter, setStatusFilter] = useState('');
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);

  const { data, isLoading, error, refetch } = useListCustomers({
    name: nameFilter || undefined,
    customerType: typeFilter || undefined,
    status: statusFilter || undefined,
    pageable: {
      page,
      size: 10,
      sort: ['createdAt,desc'],
    }
  });

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Customers</h1>
        <button className="btn btn-primary" onClick={() => setIsCreateModalOpen(true)}>
          <Plus size={16} />
          Create Customer
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
          <label className="form-label text-xs">Type</label>
          <select 
            className="form-input" 
            value={typeFilter}
            onChange={e => { setTypeFilter(e.target.value); setPage(0); }}
          >
            <option value="">All Types</option>
            <option value="INDIVIDUAL">Individual</option>
            <option value="ORGANIZATION">Organization</option>
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
                <th>Type</th>
                <th>Status</th>
                <th>Contact</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {data?.data.content?.length === 0 ? (
                <tr>
                  <td colSpan={5} className="text-center text-muted italic">
                    No customers found.
                  </td>
                </tr>
              ) : (
                data?.data.content?.map((customer: any) => (
                  <tr key={customer.id}>
                    <td className="font-medium">{customer.name}</td>
                    <td>{customer.customerType}</td>
                    <td>
                      <span className={`badge ${
                        customer.status === 'ACTIVE' ? 'badge-success' : 'badge-neutral'
                      }`}>
                        {customer.status}
                      </span>
                    </td>
                    <td className="text-sm text-muted">
                      {customer.contactInfo?.substring(0, 30)}{customer.contactInfo && customer.contactInfo.length > 30 ? '...' : ''}
                    </td>
                    <td>
                      <button 
                        className="btn btn-secondary py-1 px-2 text-xs"
                        onClick={() => navigate(`/customers/${customer.id}`)}
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
        <CreateCustomerModal 
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

export default CustomerList;
