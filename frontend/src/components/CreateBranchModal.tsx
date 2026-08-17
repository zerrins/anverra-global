import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useCreateBranch } from '../api/endpoints/organization-management-controller/organization-management-controller';
import { ApiErrorAlert } from './ApiErrorAlert';
import { X } from 'lucide-react';

const createBranchSchema = z.object({
  name: z.string().min(1, 'Name is required').max(100, 'Name must be 100 characters or less'),
});

type CreateBranchForm = z.infer<typeof createBranchSchema>;

interface CreateBranchModalProps {
  dealerId: string;
  onClose: () => void;
  onSuccess: () => void;
}

export const CreateBranchModal = ({ dealerId, onClose, onSuccess }: CreateBranchModalProps) => {
  const { register, handleSubmit, formState: { errors } } = useForm<CreateBranchForm>({
    resolver: zodResolver(createBranchSchema)
  });

  const { mutateAsync: createBranch, isPending, error } = useCreateBranch();

  const onSubmit = async (data: CreateBranchForm) => {
    try {
      await createBranch({ data: { dealerId, name: data.name } });
      onSuccess();
    } catch {
      // Error handled by ApiErrorAlert
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content max-w-md">
        <div className="modal-header">
          <h2 className="modal-title">Create Branch</h2>
          <button className="btn btn-secondary p-1" onClick={onClose}>
            <X size={20} />
          </button>
        </div>

        <div className="modal-body">
          <ApiErrorAlert error={error} />
          
          <form id="createBranchForm" onSubmit={handleSubmit(onSubmit)}>
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
            form="createBranchForm" 
            className="btn btn-primary"
            disabled={isPending}
          >
            {isPending ? 'Creating...' : 'Create Branch'}
          </button>
        </div>
      </div>
    </div>
  );
};
