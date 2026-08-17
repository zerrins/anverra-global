import React, { useState } from 'react';
import { useGenerateUploadUrl, useRegisterDocument } from '../api/endpoints/policy-document-controller/policy-document-controller';
import { ApiErrorAlert } from './ApiErrorAlert';
import { X, UploadCloud } from 'lucide-react';

interface Props {
  policyId: string;
  onClose: () => void;
  onSuccess: () => void;
}

export const PolicyDocumentUploadModal: React.FC<Props> = ({ policyId, onClose, onSuccess }) => {
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [validationError, setValidationError] = useState<string | null>(null);
  const [uploadState, setUploadState] = useState<'IDLE' | 'PREPARING' | 'UPLOADING' | 'REGISTERING' | 'COMPLETED' | 'FAILED'>('IDLE');
  const [uploadError, setUploadError] = useState<any>(null);

  const { mutateAsync: generateUrl } = useGenerateUploadUrl();
  const { mutateAsync: registerDoc } = useRegisterDocument();

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      setSelectedFile(e.target.files[0]);
      setValidationError(null);
    }
  };

  const handleUpload = async () => {
    if (!selectedFile) {
      setValidationError('Please select a file to upload.');
      return;
    }
    if (selectedFile.size < 0) {
      setValidationError('File size must be non-negative.');
      return;
    }

    try {
      setUploadError(null);

      // Step 1: Generate Upload URL
      setUploadState('PREPARING');
      const genResponse = await generateUrl({
        policyId,
        data: {
          originalFilename: selectedFile.name,
          contentType: selectedFile.type || 'application/octet-stream'
        }
      });
      const { uploadUrl, storageKey } = genResponse.data;

      if (!uploadUrl || !storageKey) {
        throw new Error('Failed to obtain upload URL from server.');
      }

      // Step 2: Direct PUT to R2
      setUploadState('UPLOADING');
      const uploadResult = await fetch(uploadUrl, {
        method: 'PUT',
        body: selectedFile,
        headers: {
          'Content-Type': selectedFile.type || 'application/octet-stream'
        }
      });

      if (!uploadResult.ok) {
        throw new Error(`Failed to upload file to storage (Status ${uploadResult.status}).`);
      }

      // Step 3: Register Document
      setUploadState('REGISTERING');
      await registerDoc({
        policyId,
        data: {
          storageKey,
          originalFilename: selectedFile.name,
          contentType: selectedFile.type || 'application/octet-stream',
          sizeBytes: selectedFile.size
        }
      });

      setUploadState('COMPLETED');
      onSuccess();
    } catch (err: any) {
      setUploadError(err);
      setUploadState('FAILED');
    }
  };

  const isWorking = ['PREPARING', 'UPLOADING', 'REGISTERING'].includes(uploadState);

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-md flex flex-col max-h-[90vh]">
        <div className="flex items-center justify-between p-4 border-b border-border-light">
          <h2 className="text-lg font-bold">Upload Policy Document</h2>
          <button onClick={onClose} disabled={isWorking} className="text-muted hover:text-primary disabled:opacity-50">
            <X size={20} />
          </button>
        </div>

        <div className="p-6 overflow-y-auto space-y-4">
          <ApiErrorAlert error={uploadError} />
          {validationError && (
            <div className="alert alert-danger">{validationError}</div>
          )}

          {uploadError instanceof Error && !('status' in uploadError) && (
            <div className="alert alert-danger">
              <div className="alert-title">Upload Failed</div>
              <div className="alert-desc">{uploadError.message}</div>
            </div>
          )}

          <div className="form-group">
            <label className="form-label" htmlFor="documentFile">Select Document</label>
            <input
              id="documentFile"
              type="file"
              className="form-input"
              onChange={handleFileChange}
              disabled={isWorking}
            />
          </div>

          {isWorking && (
            <div className="flex items-center justify-center p-4 text-primary gap-2">
              <div className="spinner spinner-md"></div>
              <span className="font-medium">
                {uploadState === 'PREPARING' && 'Preparing upload...'}
                {uploadState === 'UPLOADING' && 'Uploading document...'}
                {uploadState === 'REGISTERING' && 'Registering document...'}
              </span>
            </div>
          )}
        </div>

        <div className="p-4 border-t border-border-light flex justify-end gap-2 bg-bg-main">
          <button type="button" className="btn btn-secondary" onClick={onClose} disabled={isWorking}>
            Cancel
          </button>
          <button
            type="button"
            className="btn btn-primary flex items-center gap-2"
            onClick={handleUpload}
            disabled={isWorking || !selectedFile}
          >
            <UploadCloud size={16} />
            {isWorking ? 'Uploading...' : 'Upload'}
          </button>
        </div>
      </div>
    </div>
  );
};
