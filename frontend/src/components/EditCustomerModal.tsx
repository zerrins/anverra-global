import React from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { useUpdateCustomer } from '../api/endpoints/customer-controller/customer-controller';
import { ApiErrorAlert } from './ApiErrorAlert';
import { X } from 'lucide-react';

const schema = z.object({
  name: z.string().min(1, 'Name is required'),
  contactInfo: z.string().min(1, 'Contact Info is required'),
  addressInfo: z.string().min(1, 'Address Info is required'),
  individualInfo: z.string().optional(),
  businessInfo: z.string().optional(),
});

type FormData = z.infer<typeof schema>;

interface Props {
  customer: any;
  onClose: () => void;
  onSuccess: () => void;
}

export const EditCustomerModal: React.FC<Props> = ({ customer, onClose, onSuccess }) => {
  const { register, handleSubmit, formState: { errors } } = useForm<FormData>({
    resolver: zodResolver(schema),
    defaultValues: {
      name: customer.name || '',
      contactInfo: customer.contactInfo || '',
      addressInfo: customer.addressInfo || '',
      individualInfo: customer.individualInfo || '',
      businessInfo: customer.businessInfo || '',
    }
  });

  const { mutateAsync: updateCustomer, isPending, error } = useUpdateCustomer();

  const onSubmit = async (data: FormData) => {
    try {
      await updateCustomer({ 
        id: customer.id, 
        data: {
          ...data,
          individualInfo: customer.customerType === 'INDIVIDUAL' ? data.individualInfo : undefined,
          businessInfo: customer.customerType === 'ORGANIZATION' ? data.businessInfo : undefined,
        }
      });
      onSuccess();
    } catch {
      // Handled by ApiErrorAlert
    }
  };

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4 overflow-y-auto">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-md flex flex-col my-8">
        <div className="flex items-center justify-between p-4 border-b border-border-light">
          <h2 className="text-lg font-bold">Edit Customer</h2>
          <button onClick={onClose} className="text-muted hover:text-primary">
            <X size={20} />
          </button>
        </div>
        
        <div className="p-4 overflow-y-auto max-h-[70vh]">
          <ApiErrorAlert error={error} />
          
          <form id="edit-customer-form" onSubmit={handleSubmit(onSubmit)} className="space-y-4">
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

            {customer.customerType === 'INDIVIDUAL' && (
              <div className="form-group">
                <label className="form-label">Individual Info</label>
                <input type="text" className="form-input" {...register('individualInfo')} />
                {errors.individualInfo && <div className="form-error">{errors.individualInfo.message}</div>}
              </div>
            )}

            {customer.customerType === 'ORGANIZATION' && (
              <div className="form-group">
                <label className="form-label">Business Info</label>
                <input type="text" className="form-input" {...register('businessInfo')} />
                {errors.businessInfo && <div className="form-error">{errors.businessInfo.message}</div>}
              </div>
            )}
          </form>
        </div>

        <div className="p-4 border-t border-border-light flex justify-end gap-2 bg-bg-main">
          <button type="button" className="btn btn-secondary" onClick={onClose} disabled={isPending}>
            Cancel
          </button>
          <button type="submit" form="edit-customer-form" className="btn btn-primary" disabled={isPending}>
            {isPending ? 'Saving...' : 'Save Changes'}
          </button>
        </div>
      </div>
    </div>
  );
};
