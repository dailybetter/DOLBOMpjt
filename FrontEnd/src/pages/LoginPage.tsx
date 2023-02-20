import * as React from 'react';

import { LoginForm } from '../features/auth/LoginForm';
import { Wave } from '../features/wave/Wave';
// import { MainAppBar } from '../features/main-app-bar/MainAppBar';

export const LoginPage = () => {
  return (
    <>
      {/* <MainAppBar /> */}
      <LoginForm />
      <Wave />
    </>
  );
};
