import CreateNewFolderIcon from '@mui/icons-material/CreateNewFolder';
import { Button } from '@mui/material';
import Box from '@mui/material/Box';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import IconButton from '@mui/material/IconButton';
import TextField from '@mui/material/TextField';
import { useState } from 'react';

import { axiosService } from '../../api/instance';
import { useAppDispatch, useAppSelector } from '../../app/hooks';
import { selectDashboard, updateToggle } from './dashboardSlice';
type folderInfo = {
  update: () => void;
};
export const AddFolderButton = ({ update }: folderInfo) => {
  const dispatch = useAppDispatch();
  const [open, setOpen] = useState(false);
  const currentState = useAppSelector(selectDashboard);
  const currentPath = currentState.path;
  const currentMemberClientId = currentState.memberClientId;
  const [folderName, setFolderName] = useState('새폴더');
  const openModal = () => {
    setOpen(true);
  };
  const closeModal = () => {
    setOpen(false);
  };
  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    let path = currentPath;
    if (!path) {
      path = folderName;
    } else {
      path += '/' + folderName;
    }
    console.log(`현재 주소 : ${path}`);
    axiosService
      .post('/folder/', {
        member_client_id: currentMemberClientId,
        path,
      })
      .then((res) => {
        console.log(res);
        update();
        dispatch(updateToggle());
      })
      .catch((error) => console.log(error));

    setOpen(false);
    //폴더 생성 매서드
  };
  return (
    <>
      <IconButton onClick={openModal} color="primary" sx={{ mx: 1 }}>
        <CreateNewFolderIcon />
      </IconButton>
      <Dialog open={open} onClose={closeModal}>
        <DialogTitle>폴더 추가</DialogTitle>
        <Box
          component="form"
          onSubmit={handleSubmit}
          noValidate
          sx={{ m: 3, width: '50vh' }}
        >
          <TextField
            margin="dense"
            id="folderName"
            label="folderName"
            inputProps={{ maxLength: 12 }}
            type="text"
            // value={folderName}
            onChange={(e) => setFolderName(e.target.value)}
            fullWidth
            required
          />
          <Box sx={{ display: 'flex', flexDirection: 'row-reverse', mt: 2 }}>
            <Button onClick={closeModal} sx={{ ml: 2 }}>
              취소
            </Button>
            <Button type="submit" variant="contained">
              폴더 추가
            </Button>
          </Box>
        </Box>
      </Dialog>
    </>
  );
};
