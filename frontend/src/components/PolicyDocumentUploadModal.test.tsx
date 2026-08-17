import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import { screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { PolicyDocumentUploadModal } from './PolicyDocumentUploadModal';
import { renderWithProviders } from '../test/utils';
import * as policyDocController from '../api/endpoints/policy-document-controller/policy-document-controller';

vi.mock('../api/endpoints/policy-document-controller/policy-document-controller', async (importOriginal) => {
  const actual = await importOriginal() as any;
  return {
    ...actual,
    useGenerateUploadUrl: vi.fn(),
    useRegisterDocument: vi.fn(),
  };
});

describe('PolicyDocumentUploadModal', () => {
  const onClose = vi.fn();
  const onSuccess = vi.fn();
  const mockGenerateUploadUrl = vi.fn();
  const mockRegisterDocument = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(policyDocController.useGenerateUploadUrl).mockReturnValue({
      mutateAsync: mockGenerateUploadUrl,
    } as any);
    vi.mocked(policyDocController.useRegisterDocument).mockReturnValue({
      mutateAsync: mockRegisterDocument,
    } as any);
    globalThis.fetch = vi.fn() as any;
  });

  afterEach(() => {
    vi.restoreAllMocks();
  });

  it('validates file selection', async () => {
    renderWithProviders(
      <PolicyDocumentUploadModal policyId="1234" onClose={onClose} onSuccess={onSuccess} />
    );

    // Initial state, upload disabled
    const uploadBtn = screen.getByRole('button', { name: /Upload$/i });
    expect(uploadBtn).toBeDisabled();
  });

  it('completes the full upload sequence', async () => {
    const user = userEvent.setup();

    mockGenerateUploadUrl.mockResolvedValue({
      data: {
        uploadUrl: 'https://fake-r2.com/upload',
        storageKey: 'fake-key-123'
      }
    });

    (globalThis.fetch as any).mockResolvedValue({ ok: true });

    mockRegisterDocument.mockResolvedValue({ data: {} });

    renderWithProviders(
      <PolicyDocumentUploadModal policyId="1234" onClose={onClose} onSuccess={onSuccess} />
    );

    const file = new File(['hello'], 'hello.pdf', { type: 'application/pdf' });
    const input = screen.getByLabelText(/Select Document/i);

    await user.upload(input, file);

    const uploadBtn = screen.getByRole('button', { name: /Upload/i });
    expect(uploadBtn).not.toBeDisabled();

    await user.click(uploadBtn);

    await waitFor(() => {
      // Step 1: Generate URL
      expect(mockGenerateUploadUrl).toHaveBeenCalledWith({
        policyId: '1234',
        data: {
          originalFilename: 'hello.pdf',
          contentType: 'application/pdf'
        }
      });
      // Step 2: PUT request to R2
      expect(globalThis.fetch).toHaveBeenCalledWith('https://fake-r2.com/upload', expect.objectContaining({
        method: 'PUT',
        body: file,
        headers: { 'Content-Type': 'application/pdf' }
      }));
      // Step 3: Register
      expect(mockRegisterDocument).toHaveBeenCalledWith({
        policyId: '1234',
        data: {
          storageKey: 'fake-key-123',
          originalFilename: 'hello.pdf',
          contentType: 'application/pdf',
          sizeBytes: 5
        }
      });
      expect(onSuccess).toHaveBeenCalled();
    });
  });

  it('displays error if R2 direct upload fails', async () => {
    const user = userEvent.setup();

    mockGenerateUploadUrl.mockResolvedValue({
      data: {
        uploadUrl: 'https://fake-r2.com/upload',
        storageKey: 'fake-key-123'
      }
    });

    (globalThis.fetch as any).mockResolvedValue({ ok: false, status: 403 });

    renderWithProviders(
      <PolicyDocumentUploadModal policyId="1234" onClose={onClose} onSuccess={onSuccess} />
    );

    const file = new File(['hello'], 'hello.pdf', { type: 'application/pdf' });
    await user.upload(screen.getByLabelText(/Select Document/i), file);
    await user.click(screen.getByRole('button', { name: /Upload/i }));

    await waitFor(() => {
      expect(screen.getAllByText(/Failed to upload file to storage/i).length).toBeGreaterThan(0);
    });

    expect(mockRegisterDocument).not.toHaveBeenCalled();
    expect(onSuccess).not.toHaveBeenCalled();
  });

  it('displays error if registration fails', async () => {
    const user = userEvent.setup();

    mockGenerateUploadUrl.mockResolvedValue({
      data: {
        uploadUrl: 'https://fake-r2.com/upload',
        storageKey: 'fake-key-123'
      }
    });

    (globalThis.fetch as any).mockResolvedValue({ ok: true });

    mockRegisterDocument.mockRejectedValue(new Error('Backend error'));

    renderWithProviders(
      <PolicyDocumentUploadModal policyId="1234" onClose={onClose} onSuccess={onSuccess} />
    );

    const file = new File(['hello'], 'hello.pdf', { type: 'application/pdf' });
    await user.upload(screen.getByLabelText(/Select Document/i), file);
    await user.click(screen.getByRole('button', { name: /Upload/i }));

    await waitFor(() => {
      expect(screen.getAllByText('Backend error').length).toBeGreaterThan(0);
    });

    expect(onSuccess).not.toHaveBeenCalled();
  });
});
