import UploadFileIcon from '@mui/icons-material/UploadFile';
import { Button } from '@mui/material';
import IconButton from '@mui/material/IconButton';
import { useRef, useState } from 'react';

import { axiosService } from '../../api/instance';
import { useAppDispatch, useAppSelector } from '../../app/hooks';
import { selectDashboard, updateToggle } from './dashboardSlice';
type folderInfo = {
  update: () => void;
};
export const AddFileButton = ({ update }: folderInfo) => {
  const currentState = useAppSelector(selectDashboard);
  const fileInputRef = useRef<HTMLInputElement>(null);
  const accessToken = sessionStorage.getItem('access-token');
  const refreshToken = sessionStorage.getItem('refresh-token');
  const currentPath = currentState.path;
  const dispatch = useAppDispatch();
  const mcid = currentState.memberClientId;
  const handleFileChange = async (e: any) => {
    // Update chosen files
    e.preventDefault();
    const files = e.target.files[0];
    console.log(files);
    // setFilesToUpload(files);
    const formData = new FormData();
    const data = {
      member_client_id: mcid,
      path: currentPath,
    };
    formData.append(
      'dto',
      new Blob([JSON.stringify(data)], { type: 'application/json' }),
    );
    formData.append('file', files);

    await axiosService.post('/upload/', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
        'access-token': accessToken,
        'refresh-token': refreshToken,
      },
    });
    dispatch(updateToggle());
    update();
  };
  return (
    <>
      <IconButton component="label" color="primary" sx={{ mx: 1 }}>
        <UploadFileIcon />
        <input
          type="file"
          ref={fileInputRef}
          onChange={(e) => handleFileChange(e)}
          hidden
        />
      </IconButton>
    </>
  );
};
