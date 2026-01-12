import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { BookDTO } from 'app/book/book-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    title: yup.string().emptyToNull().max(255).required(),
    author: yup.string().emptyToNull().max(255).required(),
    description: yup.string().emptyToNull(),
    coverUrl: yup.string().emptyToNull(),
    fileUrl: yup.string().emptyToNull().required(),
    fileName: yup.string().emptyToNull().max(255).required(),
    fileType: yup.string().emptyToNull().max(255).required(),
    isPremium: yup.bool(),
    uploadedAt: yup.string().emptyToNull().offsetDateTime().required(),
    user: yup.number().integer().emptyToNull().required()
  });
}

export default function BookAdd() {
  const { t } = useTranslation();
  useDocumentTitle(t('book.add.headline'));

  const navigate = useNavigate();
  const [userValues, setUserValues] = useState<Map<number,string>>(new Map());

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareRelations = async () => {
    try {
      const userValuesResponse = await axios.get('/api/books/userValues');
      setUserValues(userValuesResponse.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareRelations();
  }, []);

  const createBook = async (data: BookDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.post('/api/books', data);
      navigate('/books', {
            state: {
              msgSuccess: t('book.create.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('book.add.headline')}</h1>
      <div>
        <Link to="/books" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('book.add.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(createBook)} noValidate>
      <InputRow useFormResult={useFormResult} object="book" field="title" required={true} />
      <InputRow useFormResult={useFormResult} object="book" field="author" required={true} />
      <InputRow useFormResult={useFormResult} object="book" field="description" type="textarea" />
      <InputRow useFormResult={useFormResult} object="book" field="coverUrl" type="textarea" />
      <InputRow useFormResult={useFormResult} object="book" field="fileUrl" required={true} type="textarea" />
      <InputRow useFormResult={useFormResult} object="book" field="fileName" required={true} />
      <InputRow useFormResult={useFormResult} object="book" field="fileType" required={true} />
      <InputRow useFormResult={useFormResult} object="book" field="isPremium" type="checkbox" />
      <InputRow useFormResult={useFormResult} object="book" field="uploadedAt" required={true} />
      <InputRow useFormResult={useFormResult} object="book" field="user" required={true} type="select" options={userValues} />
      <input type="submit" value={t('book.add.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 cursor-pointer mt-6" />
    </form>
  </>);
}
