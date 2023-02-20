import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Container from '@mui/material/Container';
import CssBaseline from '@mui/material/CssBaseline';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import axios from 'axios';
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const theme = createTheme();

export const ClientCheckForm = () => {
  const navigate = useNavigate();

  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [clientSessionId, setClientSessionId] = useState('');
  const [conferenceId, setConferenceId] = useState('');

  // (27) 선택된 내담자들에 대한 정보를 post 한다. 일치여부 확인
  function sendToClientinfo() {
    const body = JSON.stringify({
      name: name,
      email: email,
      sessionId: clientSessionId,
      conferenceId: conferenceId,
    });
    axios
      .post('https://i8c103.p.ssafy.io/api/connections/conference/client', body, {
        headers: {
          'Content-Type': 'application/json',
          'access-token': sessionStorage.getItem('access-token'),
          'refresh-token': sessionStorage.getItem('refresh-token'),
        },
      })
      .then(function (res) {
        console.log(res.data + '고객정보 전달 성공!');
        console.log(clientSessionId);
        joinVideo();
      })
      .catch(function (err) {
        console.log(err + '고객정보 전달 실패!');
        alert('정보가 일치하지 않습니다. 다시 입력해주세요.');
      }); // 요청 응답값에 따라서, 입장 허가와 불허를 판단한다.
  }

  // 방을 만들기 위한 sessionId 설정
  sessionStorage.setItem('sessionId', clientSessionId);

  // videoPage로 넘어가기 위한 함수
  function joinVideo() {
    navigate(`/${clientSessionId}`);
  }

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
          <Typography component="h1" variant="h5">
            DOLBOM 서비스 입장
          </Typography>

          <Box component="form" noValidate sx={{ mt: 1 }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="name"
              label="이름"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="email"
              label="email"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="clientSessionId"
              label="코드"
              value={clientSessionId}
              onChange={(e) => setClientSessionId(e.target.value)}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="conferenceId"
              label="방번호"
              type="number"
              value={conferenceId}
              onChange={(e) => setConferenceId(e.target.value)}
            />
          </Box>

          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
            onClick={() => {
              sendToClientinfo();
            }}
          >
            세션 입장
          </Button>
        </Box>
      </Container>
    </ThemeProvider>
  );
};
