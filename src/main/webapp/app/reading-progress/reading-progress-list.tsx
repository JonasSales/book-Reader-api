import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError } from 'app/common/utils';
import { ReadingProgressDTO } from 'app/reading-progress/reading-progress-model';
import axios from 'axios';
import useDocumentTitle from 'app/common/use-document-title';


export default function ReadingProgressList() {
  const { t } = useTranslation();
  useDocumentTitle(t('readingProgress.list.headline'));

  const [readingProgresses, setReadingProgresses] = useState<ReadingProgressDTO[]>([]);
  const navigate = useNavigate();

  const getAllReadingProgresses = async () => {
    try {
      const response = await axios.get('/api/readingProgresses');
      setReadingProgresses(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/readingProgresses/' + id);
      navigate('/readingProgresses', {
            state: {
              msgInfo: t('readingProgress.delete.success')
            }
          });
      getAllReadingProgresses();
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllReadingProgresses();
  }, []);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('readingProgress.list.headline')}</h1>
      <div>
        <Link to="/readingProgresses/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('readingProgress.list.createNew')}</Link>
      </div>
    </div>
    {!readingProgresses || readingProgresses.length === 0 ? (
    <div>{t('readingProgress.list.empty')}</div>
    ) : (
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('readingProgress.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('readingProgress.currentPage.label')}</th>
            <th scope="col" className="text-left p-2">{t('readingProgress.percentageCompleted.label')}</th>
            <th scope="col" className="text-left p-2">{t('readingProgress.lastCfiRange.label')}</th>
            <th scope="col" className="text-left p-2">{t('readingProgress.deviceType.label')}</th>
            <th scope="col" className="text-left p-2">{t('readingProgress.lastReadAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('readingProgress.user.label')}</th>
            <th scope="col" className="text-left p-2">{t('readingProgress.book.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {readingProgresses.map((readingProgress) => (
          <tr key={readingProgress.id} className="odd:bg-gray-100">
            <td className="p-2">{readingProgress.id}</td>
            <td className="p-2">{readingProgress.currentPage}</td>
            <td className="p-2">{readingProgress.percentageCompleted}</td>
            <td className="p-2">{readingProgress.lastCfiRange}</td>
            <td className="p-2">{readingProgress.deviceType}</td>
            <td className="p-2">{readingProgress.lastReadAt}</td>
            <td className="p-2">{readingProgress.user}</td>
            <td className="p-2">{readingProgress.book}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/readingProgresses/edit/' + readingProgress.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('readingProgress.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(readingProgress.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm cursor-pointer">{t('readingProgress.list.delete')}</button>
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
