import type { PayloadAction } from '@reduxjs/toolkit';
import { createSlice } from '@reduxjs/toolkit';
import jwtDecode from 'jwt-decode';

import { RootState } from '../../app/store';

export interface UserState {
  id?: string;
  role?: string;
  username?: string;
}

const accessToken = sessionStorage.getItem('access-token');
const decoded = accessToken ? (jwtDecode(accessToken) as UserState) : {};

const initialState: UserState = {
  id: decoded?.id,
  role: decoded?.role,
  username: decoded?.username,
};

export const counterSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    setUser: (state, { payload }: PayloadAction<UserState>) => {
      return payload;
    },
    clearUser: () => {
      return {};
    },
  },
});

export const { setUser, clearUser } = counterSlice.actions;

export const selectUser = (state: RootState) => state.user;

export default counterSlice.reducer;
