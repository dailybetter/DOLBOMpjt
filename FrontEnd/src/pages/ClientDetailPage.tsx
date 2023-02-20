import { Divider } from '@mui/material';
import Typography from '@mui/material/Typography';
import { Box } from '@mui/system';
import * as React from 'react';
import { useParams } from 'react-router-dom';

import { useAppSelector } from '../app/hooks';
import { BreadCrum } from '../features/clientManagement/BreadCrumb';
import { Dashboard } from '../features/clientManagement/Dashboard';
import { selectDashboard } from '../features/clientManagement/dashboardSlice';
import { SideBar2 } from '../features/sideBar/SideBar2';
// 내담자 정보
export const ClientDetailPage = () => {
  const currentState = useAppSelector(selectDashboard);
  const currentPath = currentState.path;
  console.log(currentPath);
  const { userName } = useParams();
  console.log(` 현재 mcI :${currentState.memberClientId}`);
  return (
    <>
      <Box sx={{ display: 'flex' }}>
        <SideBar2 />
        <Box sx={{ flexGrow: 1, pr: 5 }}>
          <Box
            sx={{
              display: 'flex',
              alignItems: 'flex-start',
              flexDirection: 'column',
              p: 1,
              mt: 10,
              ml: 0,
              bgcolor: 'background.paper',
              borderRadius: 1,
              border: 1,
              borderColor: 'grey.300',
            }}
          >
            <Box sx={{ display: 'flex' }}>
              <Typography variant="h2" component="div">
                {userName}
              </Typography>
            </Box>
            <Divider variant="middle" />
            <BreadCrum />
            <Dashboard />
          </Box>
        </Box>
      </Box>
    </>
  );
};
