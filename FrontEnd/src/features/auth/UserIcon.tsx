import Avatar from '@mui/material/Avatar';
import IconButton from '@mui/material/IconButton';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import Tooltip from '@mui/material/Tooltip';
import Typography from '@mui/material/Typography';
import React from 'react';
import { NavigateFunction } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';

import { useAppDispatch } from '../../app/hooks';
import { AppDispatch } from '../../app/store';
import { clearUser } from './userSlice';

const settings = ['Profile', 'Account', 'Dashboard', 'Logout'] as const;

type Settings = (typeof settings)[number];

const createEventMapper: (
  dispatch: AppDispatch,
  navigate: NavigateFunction,
) => {
  [T in (typeof settings)[number]]?: () => void;
} = (dispatch, navigate) => ({
  Profile: () => {
    navigate('/profile');
  },
  Logout: () => {
    dispatch(clearUser());
    sessionStorage.removeItem('access-token');
    sessionStorage.removeItem('refresh-token');
  },
});

export const UserIcon = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const [anchorElUser, setAnchorElUser] = React.useState<null | HTMLElement>(null);

  const eventMapper = createEventMapper(dispatch, navigate);

  const handleOpenUserMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorElUser(event.currentTarget);
  };

  const handleCloseUserMenu = (setting: Settings) => {
    setAnchorElUser(null);
    const fn = eventMapper[setting];
    if (fn) fn();
  };

  return (
    <>
      <Tooltip title="Open settings">
        <IconButton onClick={handleOpenUserMenu} sx={{ p: 0 }}>
          <Avatar />
        </IconButton>
      </Tooltip>
      <Menu
        sx={{ mt: '45px' }}
        id="menu-appbar"
        anchorEl={anchorElUser}
        anchorOrigin={{
          vertical: 'top',
          horizontal: 'right',
        }}
        keepMounted
        transformOrigin={{
          vertical: 'top',
          horizontal: 'right',
        }}
        open={Boolean(anchorElUser)}
        onClose={handleCloseUserMenu}
      >
        {settings.map((setting) => (
          <MenuItem key={setting} onClick={() => handleCloseUserMenu(setting)}>
            <Typography textAlign="center">{setting}</Typography>
          </MenuItem>
        ))}
      </Menu>
    </>
  );
};
