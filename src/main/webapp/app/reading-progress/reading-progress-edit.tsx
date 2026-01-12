import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useParams } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { ReadingProgressDTO } from 'app/reading-progress/reading-progress-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    currentPage: yup.number().integer().emptyToNull(),
    percentageCompleted: yup.number().emptyToNull(),
    lastCfiRange: yup.string().emptyToNull().max(255),
    deviceType: yup.string().emptyToNull().max(100),
    lastReadAt: yup.string().emptyToNull().offsetDateTime(),
    user: yup.number().integer().emptyToNull().required(),
    book: yup.number().integer().emptyToNull().required()
  });
}

export default function ReadingProgressEdit() {
  const { t } = useTranslation();
  useDocumentTitle(t('readingProgress.edit.headline'));

  const navigate = useNavigate();
  const [userValues, setUserValues] = useState<Map<number,string>>(new Map());
  const [bookValues, setBookValues] = useState<Map<number,string>>(new Map());
  const params = useParams();
  const currentId = +params.id!;

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareForm = async () => {
    try {
      const userValuesResponse = await axios.get('/api/readingProgresses/userValues');
      setUserValues(userValuesResponse.data);
      const bookValuesResponse = await axios.get('/api/readingProgresses/bookValues');
      setBookValues(bookValuesResponse.data);
      const data = (await axios.get('/api/readingProgresses/' + currentId)).data;
      useFormResult.reset(data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareForm();
  }, []);

  const updateReadingProgress = async (data: ReadingProgressDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.put('/api/readingProgresses/' + currentId, data);
      navigate('/readingProgresses', {
            state: {
              msgSuccess: t('readingProgress.update.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('readingProgress.edit.headline')}</h1>
      <div>
        <Link to="/readingProgresses" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('readingProgress.edit.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(updateReadingProgress)} noValidate>
      <InputRow useFormResult={useFormResult} object="readingProgress" field="id" disabled={true} type="number" />
      <InputRow useFormResult={useFormResult} object="readingProgress" field="currentPage" type="number" />
      <InputRow useFormResult={useFormResult} object="readingProgress" field="percentageCompleted" />
      <InputRow useFormResult={useFormResult} object="readingProgress" field="lastCfiRange" />
      <InputRow useFormResult={useFormResult} object="readingProgress" field="deviceType" />
      <InputRow useFormResult={useFormResult} object="readingProgress" field="lastReadAt" />
      <InputRow useFormResult={useFormResult} object="readingProgress" field="user" required={true} type="select" options={userValues} />
      <InputRow useFormResult={useFormResult} object="readingProgress" field="book" required={true} type="select" options={bookValues} />
      <input type="submit" value={t('readingProgress.edit.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 cursor-pointer mt-6" />
    </form>
  </>);
}
