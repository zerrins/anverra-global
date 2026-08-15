
import { useGetPolicyStatistics, useGetCommissionStatistics } from '../api/endpoints/reporting-controller/reporting-controller';
import { ApiErrorAlert } from '../components/ApiErrorAlert';
import { BarChart3, TrendingUp, Users, Shield } from 'lucide-react';

const Dashboard = () => {
  const { data: policyStats, isLoading: isPolicyLoading, error: policyError } = useGetPolicyStatistics();
  const { data: commStats, isLoading: isCommLoading, error: commError } = useGetCommissionStatistics();

  if (isPolicyLoading || isCommLoading) {
    return (
      <div className="flex items-center justify-center py-12">
        <div className="spinner spinner-lg"></div>
      </div>
    );
  }

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Dashboard Overview</h1>
      </div>

      <ApiErrorAlert error={policyError || commError} />

      <div className="flex flex-col gap-6">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          <div className="card p-4 flex items-center gap-4">
            <div className="bg-blue-100 text-secondary p-3 rounded-full">
              <Shield size={24} />
            </div>
            <div>
              <div className="text-sm text-muted font-medium uppercase">Total Policies</div>
              <div className="text-2xl font-bold">{policyStats?.data?.totalPolicies || 0}</div>
            </div>
          </div>

          <div className="card p-4 flex items-center gap-4">
            <div className="bg-green-100 text-success p-3 rounded-full">
              <TrendingUp size={24} />
            </div>
            <div>
              <div className="text-sm text-muted font-medium uppercase">Active Policies</div>
              <div className="text-2xl font-bold">{policyStats?.data?.activeCount || 0}</div>
            </div>
          </div>

          <div className="card p-4 flex items-center gap-4">
            <div className="bg-yellow-100 text-warning p-3 rounded-full">
              <Users size={24} />
            </div>
            <div>
              <div className="text-sm text-muted font-medium uppercase">Total Commission</div>
              <div className="text-2xl font-bold">${commStats?.data?.totalCommissionAmount?.toLocaleString() || '0.00'}</div>
            </div>
          </div>

          <div className="card p-4 flex items-center gap-4">
            <div className="bg-purple-100 text-accent p-3 rounded-full">
              <BarChart3 size={24} />
            </div>
            <div>
              <div className="text-sm text-muted font-medium uppercase">Draft Policies</div>
              <div className="text-2xl font-bold">{policyStats?.data?.draftCount || 0}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
