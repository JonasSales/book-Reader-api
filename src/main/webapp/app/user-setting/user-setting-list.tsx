import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError } from 'app/common/utils';
import { UserSettingDTO } from 'app/user-setting/user-setting-model';
import axios from 'axios';
import useDocumentTitle from 'app/common/use-document-title';


export default function UserSettingList() {
  const { t } = useTranslation();
  useDocumentTitle(t('userSetting.list.headline'));

  const [userSettings, setUserSettings] = useState<UserSettingDTO[]>([]);
  const navigate = useNavigate();

  const getAllUserSettings = async () => {
    try {
      const response = await axios.get('/api/userSettings');
      setUserSettings(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/userSettings/' + id);
      navigate('/userSettings', {
            state: {
              msgInfo: t('userSetting.delete.success')
            }
          });
      getAllUserSettings();
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllUserSettings();
  }, []);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('userSetting.list.headline')}</h1>
      <div>
        <Link to="/userSettings/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('userSetting.list.createNew')}</Link>
      </div>
    </div>
    {!userSettings || userSettings.length === 0 ? (
    <div>{t('userSetting.list.empty')}</div>
    ) : (
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('userSetting.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('userSetting.theme.label')}</th>
            <th scope="col" className="text-left p-2">{t('userSetting.fontSize.label')}</th>
            <th scope="col" className="text-left p-2">{t('userSetting.fontFamily.label')}</th>
            <th scope="col" className="text-left p-2">{t('userSetting.user.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {userSettings.map((userSetting) => (
          <tr key={userSetting.id} className="odd:bg-gray-100">
            <td className="p-2">{userSetting.id}</td>
            <td className="p-2">{userSetting.theme}</td>
            <td className="p-2">{userSetting.fontSize}</td>
            <td className="p-2">{userSetting.fontFamily}</td>
            <td className="p-2">{userSetting.user}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/userSettings/edit/' + userSetting.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('userSetting.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(userSetting.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm cursor-pointer">{t('userSetting.list.delete')}</button>
              </div>
            </td>
          </tr>
          ))}
        </tbody>
      </table>
    </div>
    )}
  </>);
}
