import { describe, it, expect, vi, beforeEach } from 'vitest';
import { screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { ConfigureCommissionModal } from './ConfigureCommissionModal';
import { renderWithProviders } from '../test/utils';
import * as policyController from '../api/endpoints/policy-controller/policy-controller';

vi.mock('../api/endpoints/policy-controller/policy-controller', async (importOriginal) => {
  const actual = await importOriginal() as any;
  return {
    ...actual,
    useConfigureCommission: vi.fn(),
  };
});

describe('ConfigureCommissionModal Validation', () => {
  const onClose = vi.fn();
  const onSuccess = vi.fn();
  const mockMutateAsync = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(policyController.useConfigureCommission).mockReturnValue({
      mutateAsync: mockMutateAsync,
    } as any);
  });

  it('validates numeric constraints', async () => {
    const user = userEvent.setup();
    renderWithProviders(
      <ConfigureCommissionModal policyId="1234" onClose={onClose} onSuccess={onSuccess} />
    );

    // Default values are 0, so clear them to type invalid negative numbers
    const agentAInput = screen.getByLabelText(/Agent A Share/i);
    const agentBInput = screen.getByLabelText(/Agent B Share/i);
    
    await user.clear(agentAInput);
    await user.type(agentAInput, '-50');
    
    await user.clear(agentBInput);
    await user.type(agentBInput, '-100');
    
    await user.click(screen.getByRole('button', { name: /Save Configuration/i }));

    await waitFor(() => {
      expect(screen.getAllByText('Must be positive')).toHaveLength(2);
    });

    expect(mockMutateAsync).not.toHaveBeenCalled();
  });

  it('submits successfully when valid', async () => {
    mockMutateAsync.mockResolvedValue({});
    const user = userEvent.setup();
    renderWithProviders(
      <ConfigureCommissionModal policyId="1234" onClose={onClose} onSuccess={onSuccess} />
    );

    await user.type(screen.getByLabelText(/Total Commission Value/i), '1000');
    await user.type(screen.getByLabelText(/Agent A Share/i), '500');
    await user.type(screen.getByLabelText(/Agent B Share/i), '500');
    await user.click(screen.getByRole('button', { name: /Save Configuration/i }));

    await waitFor(() => {
      expect(mockMutateAsync).toHaveBeenCalledWith({
        policyId: '1234',
        data: {
          commissionType: 'STANDARD',
          totalCommissionValue: 1000,
          agentAShare: 500,
          agentBShare: 500,
        }
      });
      expect(onSuccess).toHaveBeenCalled();
    });
  });
});
