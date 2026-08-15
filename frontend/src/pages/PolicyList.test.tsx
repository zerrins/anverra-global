import { describe, it, expect } from 'vitest';
import { screen, waitFor } from '@testing-library/react';
import PolicyList from './PolicyList';
import { renderWithProviders } from '../test/utils';

describe('PolicyList', () => {
  it('renders a list of policies successfully via MSW', async () => {
    renderWithProviders(<PolicyList />);

    // MSW returns POL-1000 with ACTIVE status
    await waitFor(() => {
      expect(screen.getByText('POL-1000')).toBeInTheDocument();
      expect(screen.getByText('ACTIVE')).toBeInTheDocument();
    });
  });
});
