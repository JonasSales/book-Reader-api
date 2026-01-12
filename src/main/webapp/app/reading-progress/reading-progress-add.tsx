import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
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

export default function ReadingProgressAdd() {
  const { t } = useTranslation();
  useDocumentTitle(t('readingProgress.add.headline'));

  const navigate = useNavigate();
  const [userValues, setUserValues] = useState<Map<number,string>>(new Map());
  const [bookValues, setBookValues] = useState<Map<number,string>>(new Map());

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareRelations = async () => {
    try {
      const userValuesResponse = await axios.get('/api/readingProgresses/userValues');
      setUserValues(userValuesResponse.data);
      const bookValuesResponse = await axios.get('/api/readingProgresses/bookValues');
      setBookValues(bookValuesResponse.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareRelations();
  }, []);

  const createReadingProgress = async (data: ReadingProgressDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.post('/api/readingProgresses', data);
      navigate('/readingProgresses', {
            state: {
              msgSuccess: t('readingProgress.create.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('readingProgress.add.headline')}</h1>
      <div>
        <Link to="/readingProgresses" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('readingProgress.add.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(createReadingProgress)} noValidate>
      <InputRow useFormResult={useFormResult} object="readingProgress" field="currentPage" type="number" />
      <InputRow useFormResult={useFormResult} object="readingProgress" field="percentageCompleted" />
      <InputRow useFormResult={useFormResult} object="readingProgress" field="lastCfiRange" />
      <InputRow useFormResult={useFormResult} object="readingProgress" field="deviceType" />
      <InputRow useFormResult={useFormResult} object="readingProgress" field="lastReadAt" />
      <InputRow useFormResult={useFormResult} object="readingProgress" field="user" required={true} type="select" options={userValues} />
      <InputRow useFormResult={useFormResult} object="readingProgress" field="book" required={true} type="select" options={bookValues} />
      <input type="submit" value={t('readingProgress.add.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 cursor-pointer mt-6" />
    </form>
  </>);
}
