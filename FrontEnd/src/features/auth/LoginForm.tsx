import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Avatar from '@mui/material/Avatar';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Checkbox from '@mui/material/Checkbox';
import Container from '@mui/material/Container';
import CssBaseline from '@mui/material/CssBaseline';
import FormControlLabel from '@mui/material/FormControlLabel';
import Link from '@mui/material/Link';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import { useMutation } from '@tanstack/react-query';
import jwtDecode from 'jwt-decode';
import * as React from 'react';
import { useNavigate } from 'react-router-dom';

import { useAppDispatch } from '../../app/hooks';
import { login } from './authApi';
import { setUser } from './userSlice';
type UserData = {
  exp: number;
  id: string;
  role: string;
  sub: string;
  username: string;
};

const theme = createTheme();

export const LoginForm = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const { mutate } = useMutation(login, {
    onSuccess: (data: { 'access-token': string; 'refresh-token': string }) => {
      const accessToken = data['access-token'];
      const { id, role, username } = jwtDecode(accessToken) as UserData;
      dispatch(setUser({ id, role, username }));

      const refreshToken = data['refresh-token'];

      sessionStorage.setItem('access-token', 'Bearer ' + accessToken);
      sessionStorage.setItem('refresh-token', 'Bearer ' + refreshToken);
      navigate('/calendar');
    },
    onError: () => {
      alert('아이디 또는 비밀번호가 다릅니다.');
    },
  });

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const user = {
      id: data.get('id')?.toString() ?? '',
      pw: data.get('password')?.toString() ?? '',
    };
    mutate(user);
  };

  return (
    <ThemeProvider theme={theme}>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <h1>DOLBOM</h1>
          <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="id"
              label="ID"
              name="id"
              autoComplete="id"
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
              autoComplete="current-password"
            />
            <FormControlLabel
              control={<Checkbox value="remember" color="primary" />}
              label="Remember me"
            />
            <Button type="submit" fullWidth variant="contained" sx={{ mt: 3, mb: 2 }}>
              Sign In
            </Button>

            <Link href="/signup" variant="body2">
              {"Don't have an account? Sign Up"}
            </Link>
          </Box>
        </Box>
      </Container>
    </ThemeProvider>
  );
};
