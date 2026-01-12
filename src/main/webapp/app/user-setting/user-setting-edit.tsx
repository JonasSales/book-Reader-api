import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useParams } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { UserSettingDTO } from 'app/user-setting/user-setting-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    theme: yup.string().emptyToNull().max(50),
    fontSize: yup.number().integer().emptyToNull(),
    fontFamily: yup.string().emptyToNull().max(100),
    user: yup.number().integer().emptyToNull().required()
  });
}

export default function UserSettingEdit() {
  const { t } = useTranslation();
  useDocumentTitle(t('userSetting.edit.headline'));

  const navigate = useNavigate();
  const [userValues, setUserValues] = useState<Map<number,string>>(new Map());
  const params = useParams();
  const currentId = +params.id!;

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareForm = async () => {
    try {
      const userValuesResponse = await axios.get('/api/userSettings/userValues');
      setUserValues(userValuesResponse.data);
      const data = (await axios.get('/api/userSettings/' + currentId)).data;
      useFormResult.reset(data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareForm();
  }, []);

  const updateUserSetting = async (data: UserSettingDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.put('/api/userSettings/' + currentId, data);
      navigate('/userSettings', {
            state: {
              msgSuccess: t('userSetting.update.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('userSetting.edit.headline')}</h1>
      <div>
        <Link to="/userSettings" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('userSetting.edit.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(updateUserSetting)} noValidate>
      <InputRow useFormResult={useFormResult} object="userSetting" field="id" disabled={true} type="number" />
      <InputRow useFormResult={useFormResult} object="userSetting" field="theme" />
      <InputRow useFormResult={useFormResult} object="userSetting" field="fontSize" type="number" />
      <InputRow useFormResult={useFormResult} object="userSetting" field="fontFamily" />
      <InputRow useFormResult={useFormResult} object="userSetting" field="user" required={true} type="select" options={userValues} />
      <input type="submit" value={t('userSetting.edit.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 cursor-pointer mt-6" />
    </form>
  </>);
}
