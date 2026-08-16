import React, { useEffect } from 'react';
import { useForm, useWatch } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { useCreateCustomer } from '../api/endpoints/customer-controller/customer-controller';
import { useGetDealers, useGetBranches, useGetAgents } from '../api/endpoints/organization-hierarchy-controller/organization-hierarchy-controller';
import { ApiErrorAlert } from './ApiErrorAlert';
import { X } from 'lucide-react';
import { useRole } from '../auth/useRole';

const schema = z.object({
  customerType: z.enum(['INDIVIDUAL', 'ORGANIZATION'] as const),
  name: z.string().min(1, 'Name is required'),
  contactInfo: z.string().min(1, 'Contact Info is required'),
  addressInfo: z.string().min(1, 'Address Info is required'),
  individualInfo: z.string().optional(),
  businessInfo: z.string().optional(),
  targetDealerId: z.string().uuid('Invalid UUID').optional().or(z.literal('')),
  targetBranchId: z.string().uuid('Invalid UUID').optional().or(z.literal('')),
  targetAgentId: z.string().uuid('Invalid UUID').optional().or(z.literal('')),
}).superRefine((data, ctx) => {
  if (data.customerType === 'INDIVIDUAL' && !data.individualInfo) {
    ctx.addIssue({
      code: z.ZodIssueCode.custom,
      message: 'Individual Info is required for INDIVIDUAL',
      path: ['individualInfo']
    });
  }
  if (data.customerType === 'ORGANIZATION' && !data.businessInfo) {
    ctx.addIssue({
      code: z.ZodIssueCode.custom,
      message: 'Business Info is required for ORGANIZATION',
      path: ['businessInfo']
    });
  }
});

type FormData = z.infer<typeof schema>;

interface Props {
  onClose: () => void;
  onSuccess: () => void;
}

export const CreateCustomerModal: React.FC<Props> = ({ onClose, onSuccess }) => {
  const { isAdmin } = useRole();
  
  const { register, handleSubmit, control, setValue, formState: { errors } } = useForm<FormData>({
    resolver: zodResolver(schema),
    defaultValues: {
      customerType: 'INDIVIDUAL',
    }
  });

  const customerType = useWatch({ control, name: 'customerType' });
  const targetDealerId = useWatch({ control, name: 'targetDealerId' });
  const targetBranchId = useWatch({ control, name: 'targetBranchId' });

  // Reset downstream fields if upstream changes
  useEffect(() => {
    setValue('targetBranchId', '');
    setValue('targetAgentId', '');
  }, [targetDealerId, setValue]);

  useEffect(() => {
    setValue('targetAgentId', '');
  }, [targetBranchId, setValue]);

  const { data: dealersResponse } = useGetDealers({
    query: { enabled: isAdmin }
  });
  
  const { data: branchesResponse } = useGetBranches(targetDealerId as string, {
    query: { enabled: isAdmin && !!targetDealerId }
  });
  
  const { data: agentsResponse } = useGetAgents(targetBranchId as string, {
    query: { enabled: isAdmin && !!targetBranchId }
  });

  const { mutateAsync: createCustomer, isPending, error } = useCreateCustomer();

  const onSubmit = async (data: FormData) => {
    try {
      const payload = {
        ...data,
        targetDealerId: isAdmin && data.targetDealerId ? data.targetDealerId : undefined,
        targetBranchId: isAdmin && data.targetBranchId ? data.targetBranchId : undefined,
        targetAgentId: isAdmin && data.targetAgentId ? data.targetAgentId : undefined,
      };
      
      await createCustomer({ data: payload as any });
      onSuccess();
    } catch {
      // Handled by ApiErrorAlert
    }
  };

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4 overflow-y-auto">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-lg flex flex-col my-8">
        <div className="flex items-center justify-between p-4 border-b border-border-light">
          <h2 className="text-lg font-bold">Create New Customer</h2>
          <button onClick={onClose} className="text-muted hover:text-primary">
            <X size={20} />
          </button>
        </div>
        
        <div className="p-4 overflow-y-auto max-h-[70vh]">
          <ApiErrorAlert error={error} />
          
          <form id="create-customer-form" onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div className="form-group">
              <label className="form-label">Customer Type</label>
              <select className="form-input" {...register('customerType')}>
                <option value="INDIVIDUAL">Individual</option>
                <option value="ORGANIZATION">Organization</option>
              </select>
              {errors.customerType && <div className="form-error">{errors.customerType.message}</div>}
            </div>

            <div className="form-group">
              <label className="form-label">Name</label>
              <input type="text" className="form-input" {...register('name')} />
              {errors.name && <div className="form-error">{errors.name.message}</div>}
            </div>

            <div className="form-group">
              <label className="form-label">Contact Info</label>
              <input type="text" className="form-input" {...register('contactInfo')} />
              {errors.contactInfo && <div className="form-error">{errors.contactInfo.message}</div>}
            </div>

            <div className="form-group">
              <label className="form-label">Address Info</label>
              <input type="text" className="form-input" {...register('addressInfo')} />
              {errors.addressInfo && <div className="form-error">{errors.addressInfo.message}</div>}
            </div>

            {customerType === 'INDIVIDUAL' && (
              <div className="form-group">
                <label className="form-label">Individual Info</label>
                <input type="text" className="form-input" {...register('individualInfo')} />
                {errors.individualInfo && <div className="form-error">{errors.individualInfo.message}</div>}
              </div>
            )}

            {customerType === 'ORGANIZATION' && (
              <div className="form-group">
                <label className="form-label">Business Info</label>
                <input type="text" className="form-input" {...register('businessInfo')} />
                {errors.businessInfo && <div className="form-error">{errors.businessInfo.message}</div>}
              </div>
            )}

            {isAdmin && (
              <div className="mt-6 p-4 border rounded-md bg-gray-50 space-y-4">
                <h3 className="font-semibold text-sm mb-2">Global Admin Ownership Assignment</h3>
                
                <div className="form-group">
                  <label className="form-label">Dealer</label>
                  <select className="form-input" {...register('targetDealerId')}>
                    <option value="">-- Select Dealer --</option>
                    {dealersResponse?.data?.map((d: any) => (
                      <option key={d.id} value={d.id}>{d.name}</option>
                    ))}
                  </select>
                  {errors.targetDealerId && <div className="form-error">{errors.targetDealerId.message}</div>}
                </div>

                <div className="form-group">
                  <label className="form-label">Branch</label>
                  <select className="form-input" {...register('targetBranchId')} disabled={!targetDealerId}>
                    <option value="">-- Select Branch --</option>
                    {branchesResponse?.data?.map((b: any) => (
                      <option key={b.id} value={b.id}>{b.name}</option>
                    ))}
                  </select>
                  {errors.targetBranchId && <div className="form-error">{errors.targetBranchId.message}</div>}
                </div>

                <div className="form-group">
                  <label className="form-label">Agent</label>
                  <select className="form-input" {...register('targetAgentId')} disabled={!targetBranchId}>
                    <option value="">-- Select Agent --</option>
                    {agentsResponse?.data?.map((a: any) => (
                      <option key={a.id} value={a.id}>{a.name}</option>
                    ))}
                  </select>
                  {errors.targetAgentId && <div className="form-error">{errors.targetAgentId.message}</div>}
                </div>
              </div>
            )}
          </form>
        </div>

        <div className="p-4 border-t border-border-light flex justify-end gap-2 bg-bg-main">
          <button type="button" className="btn btn-secondary" onClick={onClose} disabled={isPending}>
            Cancel
          </button>
          <button type="submit" form="create-customer-form" className="btn btn-primary" disabled={isPending}>
            {isPending ? 'Creating...' : 'Create Customer'}
          </button>
        </div>
      </div>
    </div>
  );
};
