import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useUpdateBranch } from '../api/endpoints/organization-management-controller/organization-management-controller';
import { ApiErrorAlert } from './ApiErrorAlert';
import { X } from 'lucide-react';
import type { BranchResponse } from '../api/model';

const editBranchSchema = z.object({
  name: z.string().min(1, 'Name is required').max(100, 'Name must be 100 characters or less'),
});

type EditBranchForm = z.infer<typeof editBranchSchema>;

interface EditBranchModalProps {
  branch: BranchResponse;
  onClose: () => void;
  onSuccess: () => void;
}

export const EditBranchModal = ({ branch, onClose, onSuccess }: EditBranchModalProps) => {
  const { register, handleSubmit, formState: { errors } } = useForm<EditBranchForm>({
    resolver: zodResolver(editBranchSchema),
    defaultValues: {
      name: branch.name
    }
  });

  const { mutateAsync: updateBranch, isPending, error } = useUpdateBranch();

  const onSubmit = async (data: EditBranchForm) => {
    try {
      if (!branch.id) return;
      await updateBranch({ id: branch.id, data: { name: data.name } });
      onSuccess();
    } catch {
      // Error handled by ApiErrorAlert
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content max-w-md">
        <div className="modal-header">
          <h2 className="modal-title">Edit Branch</h2>
          <button className="btn btn-secondary p-1" onClick={onClose}>
            <X size={20} />
          </button>
        </div>

        <div className="modal-body">
          <ApiErrorAlert error={error} />
          
          <form id="editBranchForm" onSubmit={handleSubmit(onSubmit)}>
            <div className="form-group">
              <label className="form-label" htmlFor="name">Name</label>
              <input 
                id="name"
                className={`form-input ${errors.name ? 'border-red-500' : ''}`}
                {...register('name')} 
              />
              {errors.name && <span className="form-error">{errors.name.message}</span>}
            </div>
          </form>
        </div>

        <div className="modal-footer">
          <button className="btn btn-secondary" onClick={onClose} disabled={isPending}>
            Cancel
          </button>
          <button 
            type="submit" 
            form="editBranchForm" 
            className="btn btn-primary"
            disabled={isPending}
          >
            {isPending ? 'Saving...' : 'Save Changes'}
          </button>
        </div>
      </div>
    </div>
  );
};
