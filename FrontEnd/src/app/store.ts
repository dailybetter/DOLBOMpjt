import { configureStore } from '@reduxjs/toolkit';

import userSlice from '../features/auth/userSlice';
import dashboardSlice from '../features/clientManagement/dashboardSlice';
import signupSlice from '../features/signup/signupSlice';
export const store = configureStore({
  reducer: {
    user: userSlice,
    signup: signupSlice,
    dashboard: dashboardSlice,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
