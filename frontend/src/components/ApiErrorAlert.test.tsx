import { describe, it, expect } from 'vitest';
import { screen, render } from '@testing-library/react';
import { ApiErrorAlert } from './ApiErrorAlert';

describe('ApiErrorAlert', () => {
  it('renders nothing when error is null', () => {
    const { container } = render(<ApiErrorAlert error={null} />);
    expect(container).toBeEmptyDOMElement();
  });

  it('handles standard RFC 7807 problem details', () => {
    const error = {
      status: 422,
      data: {
        title: 'Unprocessable Entity',
        detail: 'Invalid organization bounds',
        type: 'https://example.com/probs/invalid-bounds'
      }
    };
    render(<ApiErrorAlert error={error} />);
    expect(screen.getByText('Unprocessable Entity')).toBeInTheDocument();
    expect(screen.getByText('Invalid organization bounds')).toBeInTheDocument();
  });

  it('handles 401 gracefully', () => {
    const error = { status: 401, data: {} };
    render(<ApiErrorAlert error={error} />);
    expect(screen.getByText('Session Expired')).toBeInTheDocument();
    expect(screen.getByText(/Please log in again/i)).toBeInTheDocument();
  });

  it('handles 403 gracefully', () => {
    const error = { status: 403, data: {} };
    render(<ApiErrorAlert error={error} />);
    expect(screen.getByText('Access Denied')).toBeInTheDocument();
    expect(screen.getByText(/You do not have permission/i)).toBeInTheDocument();
  });

  it('handles plain string errors securely', () => {
    const error = { status: 500, data: 'Some stack trace...' };
    render(<ApiErrorAlert error={error} />);
    // Should fallback to generic because it is not an object with title
    expect(screen.getByText('Server Error')).toBeInTheDocument();
    expect(screen.getByText('Some stack trace...')).toBeInTheDocument();
  });
});
