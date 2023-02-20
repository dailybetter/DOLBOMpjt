import { createSlice } from '@reduxjs/toolkit';

import type { RootState } from '../../app/store';

// 재지정이 가능 한 type = interface
interface SignupState {
  isSignupClick: boolean;
}

const initialState: SignupState = {
  isSignupClick: false,
};

const signupSlice = createSlice({
  name: 'signup',
  initialState,
  reducers: {
    openModal: (state: any) => {
      state.isSignupClick = true;
    },
  },
});

export const { openModal } = signupSlice.actions;
export const selectSignup = (state: RootState) => state.signup;
export default signupSlice.reducer;
