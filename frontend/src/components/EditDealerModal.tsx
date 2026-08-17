import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useUpdateDealer } from '../api/endpoints/organization-management-controller/organization-management-controller';
import { ApiErrorAlert } from './ApiErrorAlert';
import { X } from 'lucide-react';
import type { DealerResponse } from '../api/model';

const editDealerSchema = z.object({
  name: z.string().min(1, 'Name is required').max(100, 'Name must be 100 characters or less'),
});

type EditDealerForm = z.infer<typeof editDealerSchema>;

interface EditDealerModalProps {
  dealer: DealerResponse;
  onClose: () => void;
  onSuccess: () => void;
}

export const EditDealerModal = ({ dealer, onClose, onSuccess }: EditDealerModalProps) => {
  const { register, handleSubmit, formState: { errors } } = useForm<EditDealerForm>({
    resolver: zodResolver(editDealerSchema),
    defaultValues: {
      name: dealer.name
    }
  });

  const { mutateAsync: updateDealer, isPending, error } = useUpdateDealer();

  const onSubmit = async (data: EditDealerForm) => {
    try {
      if (!dealer.id) return;
      await updateDealer({ id: dealer.id, data });
      onSuccess();
    } catch {
      // Error handled by ApiErrorAlert
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content max-w-md">
        <div className="modal-header">
          <h2 className="modal-title">Edit Dealer</h2>
          <button className="btn btn-secondary p-1" onClick={onClose}>
            <X size={20} />
          </button>
        </div>

        <div className="modal-body">
          <ApiErrorAlert error={error} />
          
          <form id="editDealerForm" onSubmit={handleSubmit(onSubmit)}>
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
            form="editDealerForm" 
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
