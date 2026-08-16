import { render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import CustomerList from './CustomerList';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: false,
    },
  },
});

describe('CustomerList', () => {
  it('renders customer data from API', async () => {
    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <CustomerList />
        </MemoryRouter>
      </QueryClientProvider>
    );

    expect(screen.getByRole('heading', { name: /Customers/i })).toBeInTheDocument();
    
    await waitFor(() => {
      expect(screen.getByText('Acme Corp')).toBeInTheDocument();
      expect(screen.getByText('John Doe')).toBeInTheDocument();
    });
  });
});
