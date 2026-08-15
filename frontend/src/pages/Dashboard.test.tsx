
import { render, screen, waitFor } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import Dashboard from './Dashboard';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: false,
    },
  },
});

const renderWithProviders = (ui: React.ReactElement) => {
  return render(
    <QueryClientProvider client={queryClient}>
      {ui}
    </QueryClientProvider>
  );
};

describe('Dashboard', () => {
  it('displays loading state initially', () => {
    renderWithProviders(<Dashboard />);
    expect(screen.queryByRole('heading', { name: /Dashboard Overview/i })).not.toBeInTheDocument();
    // In our component, we don't render the header until loading is done
  });

  it('renders statistics after data is loaded', async () => {
    renderWithProviders(<Dashboard />);
    
    await waitFor(() => {
      expect(screen.getByText('150')).toBeInTheDocument(); // Total Policies
      expect(screen.getByText('120')).toBeInTheDocument(); // Active Policies
      expect(screen.getByText('$50,000')).toBeInTheDocument(); // Total Commission
    });
  });
});
