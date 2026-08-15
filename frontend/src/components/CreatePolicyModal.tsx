
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { useCreatePolicy } from '../api/endpoints/policy-controller/policy-controller';
import { ApiErrorAlert } from './ApiErrorAlert';
import { X } from 'lucide-react';

const schema = z.object({
  policyNumber: z.string().min(1, 'Policy number is required'),
  customerId: z.string().uuid('Must be a valid UUID'),
  agentAId: z.string().uuid('Must be a valid UUID'),
  agentBId: z.string().uuid('Must be a valid UUID'),
  branchId: z.string().uuid('Must be a valid UUID'),
});

type FormData = z.infer<typeof schema>;

interface Props {
  onClose: () => void;
  onSuccess: () => void;
}

export const CreatePolicyModal: React.FC<Props> = ({ onClose, onSuccess }) => {
  const { register, handleSubmit, formState: { errors } } = useForm<FormData>({
    resolver: zodResolver(schema)
  });

  const { mutateAsync: createPolicy, isPending, error } = useCreatePolicy();

  const onSubmit = async (data: FormData) => {
    try {
      await createPolicy({ data });
      onSuccess();
    } catch {
      // Error handled by ApiErrorAlert
    }
  };

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-md flex flex-col max-h-[90vh]">
        <div className="flex items-center justify-between p-4 border-b border-border-light">
          <h2 className="text-lg font-bold">Create New Policy</h2>
          <button onClick={onClose} className="text-muted hover:text-primary">
            <X size={20} />
          </button>
        </div>
        
        <div className="p-4 overflow-y-auto">
          <ApiErrorAlert error={error} />
          
          <form id="create-policy-form" onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div className="form-group">
              <label htmlFor="policyNumber" className="form-label">Policy Number</label>
              <input id="policyNumber" type="text" className="form-input" {...register('policyNumber')} />
              {errors.policyNumber && <div className="form-error">{errors.policyNumber.message}</div>}
            </div>

            <div className="form-group">
              <label htmlFor="customerId" className="form-label">Customer ID (UUID)</label>
              <input id="customerId" type="text" className="form-input" {...register('customerId')} />
              {errors.customerId && <div className="form-error">{errors.customerId.message}</div>}
            </div>

            <div className="form-group">
              <label htmlFor="agentAId" className="form-label">Agent A ID (UUID)</label>
              <input id="agentAId" type="text" className="form-input" {...register('agentAId')} />
              {errors.agentAId && <div className="form-error">{errors.agentAId.message}</div>}
            </div>

            <div className="form-group">
              <label htmlFor="agentBId" className="form-label">Agent B ID (UUID)</label>
              <input id="agentBId" type="text" className="form-input" {...register('agentBId')} />
              {errors.agentBId && <div className="form-error">{errors.agentBId.message}</div>}
            </div>

            <div className="form-group">
              <label htmlFor="branchId" className="form-label">Branch ID (UUID)</label>
              <input id="branchId" type="text" className="form-input" {...register('branchId')} />
              {errors.branchId && <div className="form-error">{errors.branchId.message}</div>}
            </div>
          </form>
        </div>

        <div className="p-4 border-t border-border-light flex justify-end gap-2 bg-bg-main">
          <button type="button" className="btn btn-secondary" onClick={onClose} disabled={isPending}>
            Cancel
          </button>
          <button type="submit" form="create-policy-form" className="btn btn-primary" disabled={isPending}>
            {isPending ? 'Creating...' : 'Create Policy'}
          </button>
        </div>
      </div>
    </div>
  );
};
