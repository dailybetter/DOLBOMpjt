import { createSlice, PayloadAction } from '@reduxjs/toolkit';

import type { RootState } from '../../app/store';
interface FolderState {
  path: null | string;
  memberClientId: any;
  name: string;
  pathStack: string[];
  toggle: number;
  loading: boolean;
}

const initialState: FolderState = {
  name: 'root',
  path: '',
  memberClientId: 0,
  pathStack: ['root'],
  toggle: 1,
  loading: true,
};

const dashBoardSlice = createSlice({
  name: 'dashboard',
  initialState,
  reducers: {
    setMemberClientId: (state: any, action: PayloadAction<{ memberClientId: any }>) => {
      state.memberClientId = action.payload.memberClientId;
    },
    openAnotherFolder: (
      state: any,
      action: PayloadAction<{
        name?: string;
        memberClientId?: string;
        path?: string;
      }>,
    ) => {
      state.path = state.path + '/' + action.payload.path;
      if (action.payload.memberClientId) {
        state.memberClientId = action.payload.memberClientId;
      }
      state.name = action.payload.name;
    },
    appendPath: (state: any, action: PayloadAction<{ path: string }>) => {
      if (state.pathStack.length - 1) {
        state.path = state.path + '/' + action.payload.path;
      } else {
        state.path = state.path + action.payload.path;
      }
      state.pathStack = [...state.pathStack, action.payload.path];
    },
    popPath: (state) => {
      state.pathStack.pop();
      const exPath = state.path?.lastIndexOf('/');
      if (state.path) {
        state.path = state.path.substring(0, exPath);
      }
    },
    clearPath: (state) => {
      state.pathStack = [state.pathStack[0]];
      state.path = '';
    },
    updateToggle: (state) => {
      state.toggle += 1;
    },
    // setLoading: (state) => {
    //   state.loading = !state.loading;
    // },
  },
});

export const {
  openAnotherFolder,
  setMemberClientId,
  appendPath,
  popPath,
  clearPath,
  updateToggle,
} = dashBoardSlice.actions;
export const selectDashboard = (state: RootState) => state.dashboard;
export default dashBoardSlice.reducer;
