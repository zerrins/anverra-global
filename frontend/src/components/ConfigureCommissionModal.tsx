
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { useConfigureCommission } from '../api/endpoints/policy-controller/policy-controller';
import { ApiErrorAlert } from './ApiErrorAlert';
import { X } from 'lucide-react';

const schema = z.object({
  commissionType: z.string().min(1, 'Type is required'),
  totalCommissionValue: z.number().min(0, 'Must be positive'),
  agentAShare: z.number().min(0, 'Must be positive'),
  agentBShare: z.number().min(0, 'Must be positive'),
});

type FormData = z.infer<typeof schema>;

interface Props {
  policyId: string;
  onClose: () => void;
  onSuccess: () => void;
}

export const ConfigureCommissionModal: React.FC<Props> = ({ policyId, onClose, onSuccess }) => {
  const { register, handleSubmit, formState: { errors } } = useForm<FormData>({
    resolver: zodResolver(schema),
    defaultValues: {
      commissionType: 'STANDARD',
      totalCommissionValue: 0,
      agentAShare: 0,
      agentBShare: 0,
    }
  });

  const { mutateAsync: configure, isPending, error } = useConfigureCommission();

  const onSubmit = async (data: FormData) => {
    try {
      await configure({ policyId, data });
      onSuccess();
    } catch {
      // Error handled by ApiErrorAlert
    }
  };

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-md flex flex-col max-h-[90vh]">
        <div className="flex items-center justify-between p-4 border-b border-border-light">
          <h2 className="text-lg font-bold">Configure Commission</h2>
          <button onClick={onClose} className="text-muted hover:text-primary">
            <X size={20} />
          </button>
        </div>
        
        <div className="p-4 overflow-y-auto">
          <ApiErrorAlert error={error} />
          
          <form id="configure-commission-form" onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div className="form-group">
              <label className="form-label">Commission Type</label>
              <select className="form-select" {...register('commissionType')}>
                <option value="STANDARD">Standard</option>
                <option value="SPECIAL">Special</option>
              </select>
              {errors.commissionType && <div className="form-error">{errors.commissionType.message}</div>}
            </div>

            <div className="form-group">
              <label htmlFor="totalCommissionValue" className="form-label">Total Commission Value</label>
              <input id="totalCommissionValue" type="number" className="form-input" {...register('totalCommissionValue', { valueAsNumber: true })} />
              {errors.totalCommissionValue && <div className="form-error">{errors.totalCommissionValue.message}</div>}
            </div>

            <div className="form-group">
              <label htmlFor="agentAShare" className="form-label">Agent A Share</label>
              <input id="agentAShare" type="number" className="form-input" {...register('agentAShare', { valueAsNumber: true })} />
              {errors.agentAShare && <div className="form-error">{errors.agentAShare.message}</div>}
            </div>

            <div className="form-group">
              <label htmlFor="agentBShare" className="form-label">Agent B Share</label>
              <input id="agentBShare" type="number" className="form-input" {...register('agentBShare', { valueAsNumber: true })} />
              {errors.agentBShare && <div className="form-error">{errors.agentBShare.message}</div>}
            </div>
          </form>
        </div>

        <div className="p-4 border-t border-border-light flex justify-end gap-2 bg-bg-main">
          <button type="button" className="btn btn-secondary" onClick={onClose} disabled={isPending}>
            Cancel
          </button>
          <button type="submit" form="configure-commission-form" className="btn btn-primary" disabled={isPending}>
            {isPending ? 'Saving...' : 'Save Configuration'}
          </button>
        </div>
      </div>
    </div>
  );
};
