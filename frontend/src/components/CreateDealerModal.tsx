import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useCreateDealer } from '../api/endpoints/organization-management-controller/organization-management-controller';
import { ApiErrorAlert } from './ApiErrorAlert';
import { X } from 'lucide-react';

const createDealerSchema = z.object({
  name: z.string().min(1, 'Name is required').max(100, 'Name must be 100 characters or less'),
});

type CreateDealerForm = z.infer<typeof createDealerSchema>;

interface CreateDealerModalProps {
  onClose: () => void;
  onSuccess: () => void;
}

export const CreateDealerModal = ({ onClose, onSuccess }: CreateDealerModalProps) => {
  const { register, handleSubmit, formState: { errors } } = useForm<CreateDealerForm>({
    resolver: zodResolver(createDealerSchema)
  });

  const { mutateAsync: createDealer, isPending, error } = useCreateDealer();

  const onSubmit = async (data: CreateDealerForm) => {
    try {
      await createDealer({ data });
      onSuccess();
    } catch {
      // Error handled by ApiErrorAlert
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content max-w-md">
        <div className="modal-header">
          <h2 className="modal-title">Create Dealer</h2>
          <button className="btn btn-secondary p-1" onClick={onClose}>
            <X size={20} />
          </button>
        </div>

        <div className="modal-body">
          <ApiErrorAlert error={error} />
          
          <form id="createDealerForm" onSubmit={handleSubmit(onSubmit)}>
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
            form="createDealerForm" 
            className="btn btn-primary"
            disabled={isPending}
          >
            {isPending ? 'Creating...' : 'Create Dealer'}
          </button>
        </div>
      </div>
    </div>
  );
};
