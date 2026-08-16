import React from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { useCreateInsurer } from '../api/endpoints/insurer-controller/insurer-controller';
import { ApiErrorAlert } from './ApiErrorAlert';
import { X } from 'lucide-react';

const schema = z.object({
  name: z.string().min(1, 'Name is required').max(100, 'Name must be 100 characters or less'),
});

type FormData = z.infer<typeof schema>;

interface Props {
  onClose: () => void;
  onSuccess: () => void;
}

export const CreateInsurerModal: React.FC<Props> = ({ onClose, onSuccess }) => {
  const { register, handleSubmit, formState: { errors } } = useForm<FormData>({
    resolver: zodResolver(schema)
  });

  const { mutateAsync: createInsurer, isPending, error } = useCreateInsurer();

  const onSubmit = async (data: FormData) => {
    try {
      await createInsurer({ data });
      onSuccess();
    } catch {
      // Error handled by ApiErrorAlert
    }
  };

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-md flex flex-col">
        <div className="flex items-center justify-between p-4 border-b border-border-light">
          <h2 className="text-lg font-bold">Create Insurer</h2>
          <button onClick={onClose} className="text-muted hover:text-primary">
            <X size={20} />
          </button>
        </div>
        
        <div className="p-4">
          <ApiErrorAlert error={error} />
          
          <form id="create-insurer-form" onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div className="form-group">
              <label htmlFor="name" className="form-label">Name</label>
              <input id="name" type="text" className="form-input" {...register('name')} />
              {errors.name && <div className="form-error">{errors.name.message}</div>}
            </div>
          </form>
        </div>

        <div className="p-4 border-t border-border-light flex justify-end gap-2 bg-bg-main">
          <button type="button" className="btn btn-secondary" onClick={onClose} disabled={isPending}>
            Cancel
          </button>
          <button type="submit" form="create-insurer-form" className="btn btn-primary" disabled={isPending}>
            {isPending ? 'Creating...' : 'Create'}
          </button>
        </div>
      </div>
    </div>
  );
};
