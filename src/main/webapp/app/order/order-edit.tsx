import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useParams } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { OrderDTO } from 'app/order/order-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    totalAmount: yup.string().emptyToNull().numeric(10, 2).required(),
    status: yup.string().emptyToNull().max(255).required(),
    paymentMethod: yup.string().emptyToNull().max(100),
    transactionId: yup.string().emptyToNull().max(255),
    createdAt: yup.string().emptyToNull().offsetDateTime().required(),
    user: yup.number().integer().emptyToNull().required()
  });
}

export default function OrderEdit() {
  const { t } = useTranslation();
  useDocumentTitle(t('order.edit.headline'));

  const navigate = useNavigate();
  const [userValues, setUserValues] = useState<Map<number,string>>(new Map());
  const params = useParams();
  const currentId = +params.id!;

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareForm = async () => {
    try {
      const userValuesResponse = await axios.get('/api/orders/userValues');
      setUserValues(userValuesResponse.data);
      const data = (await axios.get('/api/orders/' + currentId)).data;
      useFormResult.reset(data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareForm();
  }, []);

  const updateOrder = async (data: OrderDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.put('/api/orders/' + currentId, data);
      navigate('/orders', {
            state: {
              msgSuccess: t('order.update.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('order.edit.headline')}</h1>
      <div>
        <Link to="/orders" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('order.edit.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(updateOrder)} noValidate>
      <InputRow useFormResult={useFormResult} object="order" field="id" disabled={true} type="number" />
      <InputRow useFormResult={useFormResult} object="order" field="totalAmount" required={true} />
      <InputRow useFormResult={useFormResult} object="order" field="status" required={true} />
      <InputRow useFormResult={useFormResult} object="order" field="paymentMethod" />
      <InputRow useFormResult={useFormResult} object="order" field="transactionId" />
      <InputRow useFormResult={useFormResult} object="order" field="createdAt" required={true} />
      <InputRow useFormResult={useFormResult} object="order" field="user" required={true} type="select" options={userValues} />
      <input type="submit" value={t('order.edit.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 cursor-pointer mt-6" />
    </form>
  </>);
}
