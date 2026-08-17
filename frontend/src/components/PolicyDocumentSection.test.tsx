import { describe, it, expect, vi, beforeEach } from 'vitest';
import { screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { PolicyDocumentSection } from './PolicyDocumentSection';
import { renderWithProviders } from '../test/utils';
import * as policyDocController from '../api/endpoints/policy-document-controller/policy-document-controller';

vi.mock('../api/endpoints/policy-document-controller/policy-document-controller', async (importOriginal) => {
  const actual = await importOriginal() as any;
  return {
    ...actual,
    useGetDocument: vi.fn(),
    useDeleteDocument: vi.fn(),
    getGetDocumentQueryKey: vi.fn().mockReturnValue(['mock-key']),
  };
});

describe('PolicyDocumentSection', () => {
  const mockGetDocument = vi.fn();
  const mockDeleteDocument = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(policyDocController.useGetDocument).mockImplementation(mockGetDocument);
    vi.mocked(policyDocController.useDeleteDocument).mockReturnValue({
      mutateAsync: mockDeleteDocument,
    } as any);
  });

  it('renders empty state when document is not found (404)', async () => {
    mockGetDocument.mockReturnValue({
      data: undefined,
      isLoading: false,
      error: { status: 404 },
      refetch: vi.fn()
    });

    renderWithProviders(<PolicyDocumentSection policyId="1234" />);

    expect(screen.getByText('No document attached.')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Upload Document/i })).toBeInTheDocument();
  });

  it('renders document metadata and actions when populated', async () => {
    mockGetDocument.mockReturnValue({
      data: {
        data: {
          originalFilename: 'test-policy.pdf',
          contentType: 'application/pdf',
          sizeBytes: 2500000, // ~2.4 MB
          downloadUrl: 'https://fake-download.url'
        }
      },
      isLoading: false,
      error: null,
      refetch: vi.fn()
    });

    renderWithProviders(<PolicyDocumentSection policyId="1234" />);

    expect(screen.queryByText('No document attached.')).not.toBeInTheDocument();
    expect(screen.getByText('test-policy.pdf')).toBeInTheDocument();
    expect(screen.getByText(/application\/pdf/i)).toBeInTheDocument();
    expect(screen.getByText(/2.4 MB/i)).toBeInTheDocument();

    expect(screen.getByRole('button', { name: /Download/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Replace/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Remove/i })).toBeInTheDocument();
  });

  it('handles delete confirmation flow', async () => {
    const user = userEvent.setup();
    mockGetDocument.mockReturnValue({
      data: {
        data: {
          originalFilename: 'test-policy.pdf',
          contentType: 'application/pdf',
          sizeBytes: 2500000
        }
      },
      isLoading: false,
      error: null,
      refetch: vi.fn()
    });

    renderWithProviders(<PolicyDocumentSection policyId="1234" />);

    // Click remove
    await user.click(screen.getByRole('button', { name: /Remove/i }));

    // Cancel doesn't delete
    await user.click(screen.getByRole('button', { name: /Cancel/i }));
    expect(mockDeleteDocument).not.toHaveBeenCalled();

    // Click remove again
    await user.click(screen.getByRole('button', { name: /Remove/i }));

    // Confirm delete
    mockDeleteDocument.mockResolvedValue({});
    const removeButtons = screen.getAllByRole('button', { name: /Remove/i });
    await user.click(removeButtons[removeButtons.length - 1]);

    await waitFor(() => {
      expect(mockDeleteDocument).toHaveBeenCalledWith({ policyId: '1234' });
    });
  });

  it('triggers download when download button clicked', async () => {
    const user = userEvent.setup();
    const openSpy = vi.spyOn(window, 'open').mockImplementation(() => null);

    mockGetDocument.mockReturnValue({
      data: {
        data: {
          originalFilename: 'test-policy.pdf',
          contentType: 'application/pdf',
          sizeBytes: 2500000,
          downloadUrl: 'https://fake-download.url'
        }
      },
      isLoading: false,
      error: null,
      refetch: vi.fn()
    });

    renderWithProviders(<PolicyDocumentSection policyId="1234" />);

    await user.click(screen.getByRole('button', { name: /Download/i }));

    expect(openSpy).toHaveBeenCalledWith('https://fake-download.url', '_blank');
    openSpy.mockRestore();
  });
});
