
import { AlertCircle } from 'lucide-react';

interface ApiErrorProps {
  error: any;
}

export const ApiErrorAlert: React.FC<ApiErrorProps> = ({ error }) => {
  if (!error) return null;

  let title = 'An Error Occurred';
  let detail = error.message || 'Unknown error';

  if (error.status === 401) {
    title = 'Session Expired';
    detail = 'Please log in again.';
  } else if (error.status === 403) {
    title = 'Access Denied';
    detail = 'You do not have permission to perform this action.';
  } else if (error.status >= 500) {
    title = 'Server Error';
    detail = typeof error.data === 'string' ? error.data : 'An unexpected server error occurred.';
  } else if (error.data && error.data.title) {
    title = error.data.title;
    detail = error.data.detail || detail;
  }

  return (
    <div className="alert alert-danger flex items-start gap-3">
      <AlertCircle size={20} className="mt-0.5 shrink-0" />
      <div>
        <div className="alert-title">{title}</div>
        <div className="alert-desc">{detail}</div>
        {error.data && Array.isArray(error.data.errors) && (
          <ul className="list-disc pl-5 mt-2 text-sm">
            {error.data.errors.map((err: any, idx: number) => (
              <li key={idx}>
                {err.field ? <strong>{err.field}: </strong> : ''}
                {err.defaultMessage || err.message || JSON.stringify(err)}
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
};
