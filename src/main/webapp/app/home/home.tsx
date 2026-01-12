import React from 'react';
import { Link } from 'react-router';
import { Trans, useTranslation } from 'react-i18next';
import useDocumentTitle from 'app/common/use-document-title';
import './home.css';


export default function Home() {
  const { t } = useTranslation();
  useDocumentTitle(t('home.index.headline'));

  return (<>
    <h1 className="grow text-3xl md:text-4xl font-medium mb-8">{t('home.index.headline')}</h1>
    <p className="mb-4"><Trans i18nKey="home.index.text" components={{ a: <a />, strong: <strong /> }} /></p>
    <p className="mb-12">
      <span>{t('home.index.swagger.text')}</span>
      <span> </span>
      <a href={process.env.API_PATH + '/swagger-ui.html'} target="_blank" className="underline">{t('home.index.swagger.link')}</a>.
    </p>
    <div className="md:w-2/5 mb-12">
      <h4 className="text-2xl font-medium mb-4">{t('home.index.exploreEntities')}</h4>
      <div className="flex flex-col border border-gray-300 rounded">
        <Link to="/users" className="w-full border-gray-300 hover:bg-gray-100 border-b rounded-t px-4 py-2">{t('user.list.headline')}</Link>
        <Link to="/roles" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('role.list.headline')}</Link>
        <Link to="/books" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('book.list.headline')}</Link>
        <Link to="/readingProgresses" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('readingProgress.list.headline')}</Link>
        <Link to="/orders" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('order.list.headline')}</Link>
        <Link to="/userSettings" className="w-full border-gray-300 hover:bg-gray-100 rounded-b px-4 py-2">{t('userSetting.list.headline')}</Link>
      </div>
    </div>
  </>);
}
