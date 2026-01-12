import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError } from 'app/common/utils';
import { BookDTO } from 'app/book/book-model';
import axios from 'axios';
import useDocumentTitle from 'app/common/use-document-title';


export default function BookList() {
  const { t } = useTranslation();
  useDocumentTitle(t('book.list.headline'));

  const [books, setBooks] = useState<BookDTO[]>([]);
  const navigate = useNavigate();

  const getAllBooks = async () => {
    try {
      const response = await axios.get('/api/books');
      setBooks(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/books/' + id);
      navigate('/books', {
            state: {
              msgInfo: t('book.delete.success')
            }
          });
      getAllBooks();
    } catch (error: any) {
      if (error?.response?.data?.code === 'REFERENCED') {
        const messageParts = error.response.data.message.split(',');
        navigate('/books', {
              state: {
                msgError: t(messageParts[0]!, { id: messageParts[1]! })
              }
            });
        return;
      }
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllBooks();
  }, []);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('book.list.headline')}</h1>
      <div>
        <Link to="/books/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('book.list.createNew')}</Link>
      </div>
    </div>
    {!books || books.length === 0 ? (
    <div>{t('book.list.empty')}</div>
    ) : (
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('book.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('book.title.label')}</th>
            <th scope="col" className="text-left p-2">{t('book.author.label')}</th>
            <th scope="col" className="text-left p-2">{t('book.fileName.label')}</th>
            <th scope="col" className="text-left p-2">{t('book.fileType.label')}</th>
            <th scope="col" className="text-left p-2">{t('book.isPremium.label')}</th>
            <th scope="col" className="text-left p-2">{t('book.uploadedAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('book.user.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {books.map((book) => (
          <tr key={book.id} className="odd:bg-gray-100">
            <td className="p-2">{book.id}</td>
            <td className="p-2">{book.title}</td>
            <td className="p-2">{book.author}</td>
            <td className="p-2">{book.fileName}</td>
            <td className="p-2">{book.fileType}</td>
            <td className="p-2">{book.isPremium?.toString()}</td>
            <td className="p-2">{book.uploadedAt}</td>
            <td className="p-2">{book.user}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/books/edit/' + book.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('book.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(book.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm cursor-pointer">{t('book.list.delete')}</button>
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
