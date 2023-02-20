import DeleteIcon from '@mui/icons-material/Delete';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import IconButton from '@mui/material/IconButton';
import { useState } from 'react';

import { axiosService } from '../../api/instance';
import { useAppDispatch, useAppSelector } from '../../app/hooks';
import { popPath, selectDashboard, updateToggle } from './dashboardSlice';
type folderInfo = {
  update: () => void;
};
export const DeleteButton = ({ update }: folderInfo) => {
  const [open, setOpen] = useState(false);
  const dispatch = useAppDispatch();
  const currentState = useAppSelector(selectDashboard);
  const currentPath = currentState.path;
  const mci = currentState.memberClientId;
  const Delete = () => {
    if (currentPath !== null) {
      dispatch(popPath());
    }
    axiosService
      .delete('/folder', { params: { id: mci, path: currentPath } })
      .then((res) => console.log(res));
    update();
    dispatch(updateToggle());
    handleClose();
  };
  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };
  return (
    <>
      <IconButton onClick={handleClickOpen} color="error" sx={{ mx: 1 }}>
        <DeleteIcon />
      </IconButton>
      <Dialog
        open={open}
        onClose={handleClose}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle id="alert-dialog-title">현재 폴더를 삭제하시겠습니까?</DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description">
            내부의 폴더, 파일이 모두 삭제 됩니다.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={Delete}>폴더 삭제</Button>
          <Button onClick={handleClose}>취소</Button>
        </DialogActions>
      </Dialog>
    </>
  );
};
