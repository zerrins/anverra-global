
import { ShieldAlert } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

export const AccessDenied = () => {
  const navigate = useNavigate();

  return (
    <div className="flex flex-col items-center justify-center min-h-screen p-4 bg-main text-center">
      <ShieldAlert className="text-danger mb-4" size={64} />
      <h1 className="text-2xl font-bold mb-2">Access Denied</h1>
      <p className="text-muted mb-6 max-w-md">
        You do not have the required permissions to view this page or perform this action.
      </p>
      <button className="btn btn-primary" onClick={() => navigate('/')}>
        Return to Dashboard
      </button>
    </div>
  );
};
