
import { useGetPolicyStatistics, useGetCommissionStatistics } from '../api/endpoints/reporting-controller/reporting-controller';
import { ApiErrorAlert } from '../components/ApiErrorAlert';

const ReportingDashboard = () => {
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
        <h1 className="page-title">Reporting & Analytics</h1>
      </div>

      <ApiErrorAlert error={policyError || commError} />

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mt-6">
        <div className="card p-6">
          <h2 className="text-lg font-bold mb-4">Policy Status Breakdown</h2>
          {policyStats ? (
            <div className="space-y-4">
              <div className="flex justify-between items-center border-b border-border-light pb-2">
                <span className="text-muted">Total Policies</span>
                <span className="font-semibold">{policyStats.data.totalPolicies}</span>
              </div>
              <div className="flex justify-between items-center border-b border-border-light pb-2">
                <span className="text-muted">Draft</span>
                <span className="font-semibold">{policyStats.data.draftCount}</span>
              </div>
              <div className="flex justify-between items-center border-b border-border-light pb-2">
                <span className="text-muted">Active</span>
                <span className="font-semibold text-success">{policyStats.data.activeCount}</span>
              </div>
              <div className="flex justify-between items-center">
                <span className="text-muted">Inactive</span>
                <span className="font-semibold text-danger">{policyStats.data.inactiveCount}</span>
              </div>
            </div>
          ) : (
            <div className="text-muted text-sm italic">No policy data available</div>
          )}
        </div>

        <div className="card p-6">
          <h2 className="text-lg font-bold mb-4">Commission Breakdown</h2>
          {commStats ? (
            <div className="space-y-4">
              <div className="flex justify-between items-center border-b border-border-light pb-2">
                <span className="text-muted">Total Commission Amount</span>
                <span className="font-semibold">${commStats.data.totalCommissionAmount?.toLocaleString()}</span>
              </div>
              <div className="flex justify-between items-center border-b border-border-light pb-2">
                <span className="text-muted">Agent A Commissions</span>
                <span className="font-semibold">${commStats.data.agentACommissionAmount?.toLocaleString()}</span>
              </div>
              <div className="flex justify-between items-center border-b border-border-light pb-2">
                <span className="text-muted">Agent B Commissions</span>
                <span className="font-semibold">${commStats.data.agentBCommissionAmount?.toLocaleString()}</span>
              </div>
              <div className="flex justify-between items-center">
                <span className="text-muted">Policies with Commissions</span>
                <span className="font-semibold">{commStats.data.configuredCommissionCount}</span>
              </div>
            </div>
          ) : (
            <div className="text-muted text-sm italic">No commission data available</div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ReportingDashboard;
