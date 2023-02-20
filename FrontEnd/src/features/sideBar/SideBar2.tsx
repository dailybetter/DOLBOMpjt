import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import LogoutIcon from '@mui/icons-material/Logout';
import MenuIcon from '@mui/icons-material/Menu';
import PersonSearchIcon from '@mui/icons-material/PersonSearch';
import VideoCameraFrontIcon from '@mui/icons-material/VideoCameraFront';
import { Button, Modal } from '@mui/material';
import MuiAppBar, { AppBarProps as MuiAppBarProps } from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import CssBaseline from '@mui/material/CssBaseline';
import Divider from '@mui/material/Divider';
import MuiDrawer from '@mui/material/Drawer';
import IconButton from '@mui/material/IconButton';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import { CSSObject, styled, Theme, useTheme } from '@mui/material/styles';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import axios from 'axios';
import * as React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useLocation } from 'react-router-dom';

import { useAppDispatch, useAppSelector } from '../../app/hooks';
import { clearUser, selectUser } from '../auth/userSlice';

const drawerWidth = 240;

const style = {
  position: 'absolute' as const,
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 400,
  bgcolor: 'background.paper',
  border: '2px solid #000',
  boxShadow: 24,
  p: 4,
};

const openedMixin = (theme: Theme): CSSObject => ({
  width: drawerWidth,
  transition: theme.transitions.create('width', {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.enteringScreen,
  }),
  overflowX: 'hidden',
});

const closedMixin = (theme: Theme): CSSObject => ({
  transition: theme.transitions.create('width', {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.leavingScreen,
  }),
  overflowX: 'hidden',
  width: `calc(${theme.spacing(7)} + 1px)`,
  [theme.breakpoints.up('sm')]: {
    width: `calc(${theme.spacing(8)} + 1px)`,
  },
});

const DrawerHeader = styled('div')(({ theme }) => ({
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'flex-end',
  padding: theme.spacing(0, 1),
  ...theme.mixins.toolbar,
}));

interface AppBarProps extends MuiAppBarProps {
  open?: boolean;
}

const AppBar = styled(MuiAppBar, {
  shouldForwardProp: (prop: string) => prop !== 'open',
})<AppBarProps>(({ theme, open }) => ({
  zIndex: theme.zIndex.drawer + 1,
  transition: theme.transitions.create(['width', 'margin'], {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.leavingScreen,
  }),
  ...(open && {
    marginLeft: drawerWidth,
    width: `calc(100% - ${drawerWidth}px)`,
    transition: theme.transitions.create(['width', 'margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  }),
}));

const Drawer = styled(MuiDrawer, {
  shouldForwardProp: (prop: string) => prop !== 'open',
})(({ theme, open }) => ({
  width: drawerWidth,
  flexShrink: 0,
  whiteSpace: 'nowrap',
  boxSizing: 'border-box',
  ...(open && {
    ...openedMixin(theme),
    '& .MuiDrawer-paper': openedMixin(theme),
  }),
  ...(!open && {
    ...closedMixin(theme),
    '& .MuiDrawer-paper': closedMixin(theme),
  }),
}));

export const SideBar2 = () => {
  const dispatch = useAppDispatch();
  const user = useAppSelector(selectUser);
  const navigate = useNavigate();

  const logOut = () => {
    dispatch(clearUser());
    sessionStorage.clear();

    navigate('/login');
  };
  const theme = useTheme();
  const [open, setOpen] = React.useState(false);

  const handleDrawerOpen = () => {
    setOpen(true);
  };

  const handleDrawerClose = () => {
    setOpen(false);
  };
  // const { pageTitle } = useLocation;
  const location = useLocation();
  const pageTitle = location.pathname.split('/');

  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  // user에 대한 고객 데이터 정보를 받는다.
  const [clientData, setClientData] = React.useState([]);

  // user가 selected한 client의 id를 담는다.
  const [selectedClientId, setSelectedClientId] = React.useState(0);

  // selected한 client에 대한 useState를 위해서 (e type 설정하기)
  const handleSelect = (e: { target: { value: any } }) => {
    setSelectedClientId(e.target.value);
  };

  const accessToken = sessionStorage.getItem('access-token');
  const refreshToken = sessionStorage.getItem('refresh-token');

  function moveToVideo() {
    console.log(`세션 아이디 : ${sessionStorage.getItem('sessionId')}`);
    navigate(`/${sessionStorage.getItem('sessionId')}`);
  }

  // (25) user에 대한 client를 받기 위한 axios 요청, header에 토큰을 보내고 back에서 user에 대한 client를 받는다.
  function getClientData() {
    axios
      .get('https://i8c103.p.ssafy.io/api/client', {
        headers: {
          'access-token': accessToken,
          'refresh-token': refreshToken,
        },
      })
      .then((response: { data: any }) => setClientData(response.data));
  }

  console.log(clientData); // client all data 확인
  console.log('selected: ' + selectedClientId); // selected 된 client의 id 확인

  // (26) 세션 생성을 누르고 back에 post 요청. sessionid랑 conf id 2개 받아서 storage에 저장
  function sendToSelected() {
    const body = JSON.stringify({ clientId: selectedClientId });
    axios
      .post('https://i8c103.p.ssafy.io/api/conference', body, {
        headers: {
          'Content-Type': 'application/json',
          'access-token': accessToken,
          'refresh-token': refreshToken,
        },
      })
      .then(function (res: { data: { sessionId: string; conferenceId: string } }) {
        sessionStorage.setItem('sessionId', res.data.sessionId), // 함수형으로 2개 전달
          sessionStorage.setItem('conferenceId', res.data.conferenceId),
          OpenVidu();
      });
  }

  // window.alert
  const useConfirm = (message: any, onConfirm: any, onCancel: any) => {
    if (!onConfirm || typeof onConfirm !== 'function') {
      return;
    }
    if (onCancel && typeof onCancel !== 'function') {
      return;
    }

    const confirmAction = () => {
      if (window.confirm(message)) {
        onConfirm();
      } else {
        onCancel();
      }
    };

    return confirmAction;
  };

  const cancelConfirm = () => console.log('취소했습니다.');
  interface itType {
    id: number;
    name: string;
    phone: string;
  }
  const confirmDelete: any = useConfirm(
    '상담실로 이동하시겠습니까?',
    moveToVideo,
    cancelConfirm,
  );

  // (29) 세션 생성 누를때, post 요청 한개 더 why? 하나는 storage에 id 저장, 하나는 userid 로 설정, 이거 무조건 sessionid.
  function OpenVidu() {
    const body = JSON.stringify({ customSessionId: sessionStorage.getItem('sessionId') });
    // axios
    //   .post('https://i8c103.p.ssafy.io/api/conference', body, {
    //     headers: {
    //       'Content-Type': 'application/json',
    //       'access-token': accessToken,
    //       'refresh-token': refreshToken,
    //     },
    //   })
    //   .then(function (response: any) {
    //     confirmDelete();
    //   });
    confirmDelete();
  }

  return (
    <Box sx={{ display: 'flex' }}>
      <CssBaseline />
      <AppBar position="fixed" open={open}>
        <Toolbar>
          <IconButton
            color="inherit"
            aria-label="open drawer"
            onClick={handleDrawerOpen}
            edge="start"
            sx={{
              marginRight: 5,
              ...(open && { display: 'none' }),
            }}
          >
            <MenuIcon />
          </IconButton>
          <Typography variant="h6" noWrap component="div">
            {pageTitle[1].toUpperCase()}
          </Typography>
        </Toolbar>
      </AppBar>
      <Drawer variant="permanent" open={open}>
        <DrawerHeader>
          <IconButton onClick={handleDrawerClose}>
            {theme.direction === 'rtl' ? <ChevronRightIcon /> : <ChevronLeftIcon />}
          </IconButton>
        </DrawerHeader>
        <Divider />
        <List>
          <Link to="/calendar" style={{ textDecoration: 'none', color: 'inherit' }}>
            <ListItem key="calendar" disablePadding>
              <ListItemButton
                sx={{
                  minHeight: 48,
                  justifyContent: open ? 'initial' : 'center',
                  px: 2.5,
                }}
              >
                <CalendarMonthIcon
                  sx={{
                    minWidth: 0,
                    mr: open ? 3 : 'auto',
                    justifyContent: 'center',
                  }}
                />
                <ListItemText primary="Calendar" sx={{ opacity: open ? 1 : 0 }} />
              </ListItemButton>
            </ListItem>
          </Link>

          <div style={{ textDecoration: 'none', color: 'inherit' }}>
            <ListItem key="video" disablePadding>
              <ListItemButton
                sx={{
                  minHeight: 48,
                  justifyContent: open ? 'initial' : 'center',
                  px: 2.5,
                }}
              >
                <div>
                  <VideoCameraFrontIcon
                    sx={{
                      minWidth: 0,
                      mr: open ? 3 : 'auto',
                      justifyContent: 'center',
                    }}
                    onClick={() => {
                      handleOpen(); // 모달 여는 함수
                      getClientData(); // 모달 열릴 때 axios로 counselor에 해당하는 client 호출
                    }}
                  />
                </div>
                <ListItemText primary="Video" sx={{ opacity: open ? 1 : 0 }} />
              </ListItemButton>
            </ListItem>
          </div>
          <Link
            to="/clientmanagement"
            style={{ textDecoration: 'none', color: 'inherit' }}
          >
            <ListItem key="ClientManagement" disablePadding>
              <ListItemButton
                sx={{
                  minHeight: 48,
                  justifyContent: open ? 'initial' : 'center',
                  px: 2.5,
                }}
              >
                <PersonSearchIcon
                  sx={{
                    minWidth: 0,
                    mr: open ? 3 : 'auto',
                    justifyContent: 'center',
                  }}
                />
                <ListItemText primary="ClientManagement" sx={{ opacity: open ? 1 : 0 }} />
              </ListItemButton>
            </ListItem>
          </Link>
        </List>
        <Divider />
        <List>
          <ListItem key="Logout" disablePadding>
            <ListItemButton
              onClick={logOut}
              sx={{
                minHeight: 48,
                justifyContent: open ? 'initial' : 'center',
                px: 2.5,
              }}
            >
              <LogoutIcon
                sx={{
                  minWidth: 0,
                  mr: open ? 3 : 'auto',
                  justifyContent: 'center',
                }}
              />
              <ListItemText primary="Logout" sx={{ opacity: open ? 1 : 0 }} />
            </ListItemButton>
          </ListItem>
        </List>
      </Drawer>
      <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
        <DrawerHeader />
      </Box>
    </Box>
  );
};
