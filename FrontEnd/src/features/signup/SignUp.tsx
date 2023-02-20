import PersonAddIcon from '@mui/icons-material/PersonAdd';
import { DialogContent } from '@mui/material';
import Avatar from '@mui/material/Avatar';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Container from '@mui/material/Container';
import CssBaseline from '@mui/material/CssBaseline';
import Dialog from '@mui/material/Dialog';
import Grid from '@mui/material/Grid';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import * as React from 'react';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { axiosService } from '../../api/instance';
import { useAppSelector } from '../../app/hooks';
import IDinput from './IDinput';
import PwdInput from './PwdInput';
import { selectSignup } from './signupSlice';
const Signup = () => {
  const signupState = useAppSelector(selectSignup);
  const [nameValidate, setNameValidate] = useState(false);
  const [idValidate, setIdValidate] = useState(false);
  const [pwdValidate, setPwdValidate] = useState(false);
  const [open, setOpen] = React.useState(false);
  const [birth, setBirth] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [name, setName] = useState('');
  const [userId, setUserId] = useState('');
  const [userPassword, setUserPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [signUpResult, setSignUpResult] = useState(false);
  const [msg, setMsg] = useState(false);
  const navigate = useNavigate();
  const theme = createTheme();
  const birthInput = (e: any) => {
    const data: string = e.target.value;
    if (data.length === 4 && e.nativeEvent.data !== null) {
      e.target.value = `${data}-`;
    }
    if (data.length === 7 && e.nativeEvent.data !== null) {
      e.target.value = `${data}-`;
    }
    setBirth(e.target.value);
  };
  const handleClose = () => {
    setOpen(false);
    if (msg) {
      navigate('/login');
    }
  };
  const sendAxios = () => {
    setLoading(true);
    axiosService
      .post('/user', {
        email: userId,
        password: userPassword,
        name: name,
        content: '',
        birth,
        phone: phoneNumber,
      })
      .then(() => {
        setLoading(false);
        setSignUpResult(true);
        setMsg(true);
        setOpen(true);
      });
  };
  if (signUpResult) {
    return (
      <Dialog
        open={open}
        onClose={() => {
          handleClose();
          setSignUpResult(true);
        }}
      >
        <DialogContent>{msg ? '회원가입 성공' : '회원가입 실패'}</DialogContent>
      </Dialog>
    );
  }
  return (
    <div>
      <ThemeProvider theme={theme}>
        <Container component="main" maxWidth="xs">
          <CssBaseline />
          <Grid container sx={{ marginTop: 8 }}>
            {/* Header */}
            <Box
              sx={{
                marginTop: 8,
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
              }}
            >
              <h1>DOLBOM</h1>
              {/* Content */}
              <Grid>
                {/* birth */}
                <TextField
                  error={birth.length >= 30 ? true : false}
                  margin="dense"
                  id={'birth'}
                  label="생년월일 8자리를 입력하세요."
                  type="string"
                  helperText={birth.length >= 30 ? '30자 이하로 입력하세요.' : ''}
                  fullWidth
                  onChange={(e) => {
                    birthInput(e);
                  }}
                  inputProps={{ maxLength: 10 }}
                />
                {/* name */}
                <TextField
                  error={name.length >= 30 ? true : false}
                  margin="dense"
                  id={'name'}
                  label="이름 입력하세요.*"
                  type="text"
                  helperText={name.length >= 30 ? '30자 이하로 입력하세요.' : ''}
                  fullWidth
                  onChange={(e) => {
                    setName(e.target.value);
                    setNameValidate(!!e.target.value.length);
                  }}
                  inputProps={{ maxLength: 30 }}
                />
                {/* name */}
                <TextField
                  error={name.length >= 30 ? true : false}
                  margin="dense"
                  id={'phoneNumber'}
                  label="전화번호를 '-'없이 숫자만 입력해 주세요.*"
                  type="text"
                  helperText={name.length >= 12 ? '30자 이하로 입력하세요.' : ''}
                  fullWidth
                  onChange={(e) => {
                    setPhoneNumber(e.target.value);
                  }}
                  inputProps={{ maxLength: 12 }}
                />
                <IDinput setIdValidate={setIdValidate} setUserId={setUserId} />
                <PwdInput
                  setPwdValidate={setPwdValidate}
                  setUserPassword={setUserPassword}
                />
              </Grid>

              <Button
                disabled={!nameValidate || !idValidate || !pwdValidate}
                onClick={sendAxios}
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2 }}
              >
                회원 가입
              </Button>
            </Box>
          </Grid>
        </Container>
      </ThemeProvider>
    </div>
  );
};

export default Signup;
