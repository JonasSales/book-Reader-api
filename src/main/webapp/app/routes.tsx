import React from 'react';
import { createBrowserRouter, RouterProvider } from 'react-router';
import App from "./app";
import Home from './home/home';
import UserList from './user/user-list';
import UserAdd from './user/user-add';
import UserEdit from './user/user-edit';
import RoleList from './role/role-list';
import RoleAdd from './role/role-add';
import RoleEdit from './role/role-edit';
import BookList from './book/book-list';
import BookAdd from './book/book-add';
import BookEdit from './book/book-edit';
import ReadingProgressList from './reading-progress/reading-progress-list';
import ReadingProgressAdd from './reading-progress/reading-progress-add';
import ReadingProgressEdit from './reading-progress/reading-progress-edit';
import OrderList from './order/order-list';
import OrderAdd from './order/order-add';
import OrderEdit from './order/order-edit';
import UserSettingList from './user-setting/user-setting-list';
import UserSettingAdd from './user-setting/user-setting-add';
import UserSettingEdit from './user-setting/user-setting-edit';
import Error from './error/error';


export default function AppRoutes() {
  const router = createBrowserRouter([
    {
      element: <App />,
      children: [
        { path: '', element: <Home /> },
        { path: 'users', element: <UserList /> },
        { path: 'users/add', element: <UserAdd /> },
        { path: 'users/edit/:id', element: <UserEdit /> },
        { path: 'roles', element: <RoleList /> },
        { path: 'roles/add', element: <RoleAdd /> },
        { path: 'roles/edit/:id', element: <RoleEdit /> },
        { path: 'books', element: <BookList /> },
        { path: 'books/add', element: <BookAdd /> },
        { path: 'books/edit/:id', element: <BookEdit /> },
        { path: 'readingProgresses', element: <ReadingProgressList /> },
        { path: 'readingProgresses/add', element: <ReadingProgressAdd /> },
        { path: 'readingProgresses/edit/:id', element: <ReadingProgressEdit /> },
        { path: 'orders', element: <OrderList /> },
        { path: 'orders/add', element: <OrderAdd /> },
        { path: 'orders/edit/:id', element: <OrderEdit /> },
        { path: 'userSettings', element: <UserSettingList /> },
        { path: 'userSettings/add', element: <UserSettingAdd /> },
        { path: 'userSettings/edit/:id', element: <UserSettingEdit /> },
        { path: 'error', element: <Error /> },
        { path: '*', element: <Error /> }
      ]
    }
  ]);

  return (
    <RouterProvider router={router} />
  );
}
