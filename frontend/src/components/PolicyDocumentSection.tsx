import React, { useState } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import {
  useGetDocument,
  useDeleteDocument,
  getGetDocumentQueryKey
} from '../api/endpoints/policy-document-controller/policy-document-controller';
import { ApiErrorAlert } from './ApiErrorAlert';
import { PolicyDocumentUploadModal } from './PolicyDocumentUploadModal';
import { Download, RefreshCw, Trash2, FileText, Upload } from 'lucide-react';

interface Props {
  policyId: string;
}

export const PolicyDocumentSection: React.FC<Props> = ({ policyId }) => {
  const queryClient = useQueryClient();
  const [isUploadModalOpen, setIsUploadModalOpen] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);
  const [deleteConfirmationOpen, setDeleteConfirmationOpen] = useState(false);

  const { data: document, isLoading, error } = useGetDocument(policyId, {
    query: {
      retry: false, // Don't retry on 404
    }
  });

  const { mutateAsync: deleteDoc } = useDeleteDocument();

  const isNotFound = !!error && (error as any)?.status === 404;
  const displayError = isNotFound ? null : error;

  const handleDelete = async () => {
    try {
      setIsDeleting(true);
      await deleteDoc({ policyId });
      queryClient.invalidateQueries({ queryKey: getGetDocumentQueryKey(policyId) });
      setDeleteConfirmationOpen(false);
    } catch {
      // Error is caught by ApiErrorAlert or we can display it below
    } finally {
      setIsDeleting(false);
    }
  };

  const handleUploadSuccess = () => {
    setIsUploadModalOpen(false);
    queryClient.invalidateQueries({ queryKey: getGetDocumentQueryKey(policyId) });
  };

  const formatFileSize = (bytes?: number) => {
    if (bytes === undefined || bytes === null) return '';
    if (bytes < 1024) return bytes + ' B';
    else if (bytes < 1048576) return (bytes / 1024).toFixed(1) + ' KB';
    else return (bytes / 1048576).toFixed(1) + ' MB';
  };

  return (
    <div className="card p-6">
      <h2 className="text-lg font-bold mb-4">Policy Document</h2>

      <ApiErrorAlert error={displayError as any} />

      {isLoading && (
        <div className="flex justify-center p-4">
          <div className="spinner spinner-md"></div>
        </div>
      )}

      {!isLoading && isNotFound && (
        <div className="flex flex-col items-center justify-center p-6 border-2 border-dashed border-border-light rounded-lg bg-bg-main">
          <FileText size={32} className="text-muted mb-2" />
          <p className="text-muted font-medium mb-4">No document attached.</p>
          <button className="btn btn-primary" onClick={() => setIsUploadModalOpen(true)}>
            <Upload size={16} className="mr-2 inline" />
            Upload Document
          </button>
        </div>
      )}

      {!isLoading && !isNotFound && document?.data && (
        <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between p-4 border border-border-light rounded-lg bg-bg-main">
          <div className="flex items-center gap-3 mb-4 sm:mb-0">
            <div className="p-2 bg-primary/10 text-primary rounded-lg">
              <FileText size={24} />
            </div>
            <div>
              <div className="font-semibold">{document.data.originalFilename}</div>
              <div className="text-sm text-muted">
                {document.data.contentType} &bull; {formatFileSize(document.data.sizeBytes)}
              </div>
            </div>
          </div>

          <div className="flex items-center gap-2">
            <button
              className="btn btn-secondary py-1.5 px-3"
              onClick={() => {
                if (document.data?.downloadUrl) {
                  window.open(document.data.downloadUrl, '_blank');
                }
              }}
              title="Download Document"
            >
              <Download size={16} />
              <span className="sr-only">Download</span>
            </button>
            <button
              className="btn btn-secondary py-1.5 px-3"
              onClick={() => setIsUploadModalOpen(true)}
              title="Replace Document"
            >
              <RefreshCw size={16} />
              <span className="sr-only">Replace</span>
            </button>
            <button
              className="btn btn-danger py-1.5 px-3"
              onClick={() => setDeleteConfirmationOpen(true)}
              title="Remove Document"
            >
              <Trash2 size={16} />
              <span className="sr-only">Remove</span>
            </button>
          </div>
        </div>
      )}

      {isUploadModalOpen && (
        <PolicyDocumentUploadModal
          policyId={policyId}
          onClose={() => setIsUploadModalOpen(false)}
          onSuccess={handleUploadSuccess}
        />
      )}

      {deleteConfirmationOpen && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-lg shadow-xl w-full max-w-sm flex flex-col">
            <div className="p-4 border-b border-border-light">
              <h2 className="text-lg font-bold text-danger">Remove policy document?</h2>
            </div>
            <div className="p-4">
              <p>This will permanently remove the current document.</p>
            </div>
            <div className="p-4 border-t border-border-light flex justify-end gap-2 bg-bg-main">
              <button
                type="button"
                className="btn btn-secondary"
                onClick={() => setDeleteConfirmationOpen(false)}
                disabled={isDeleting}
              >
                Cancel
              </button>
              <button
                type="button"
                className="btn btn-danger"
                onClick={handleDelete}
                disabled={isDeleting}
              >
                {isDeleting ? 'Removing...' : 'Remove'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
