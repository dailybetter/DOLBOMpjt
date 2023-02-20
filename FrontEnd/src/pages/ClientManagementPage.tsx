import { Container } from '@mui/material';
import Grid from '@mui/material/Grid';
import { Box } from '@mui/system';
import axios from 'axios';
import * as React from 'react';
import { useEffect, useState } from 'react';

import { axiosService } from '../api/instance';
import { ClientTable } from '../features/clientManagement/ClientTable';
import { SideBar2 } from '../features/sideBar/SideBar2';
interface ClientCardProps {
  id: number | string;
  clientId: number;
  name: string;
  phone: string;
  email: string;
}
export const ClientManagementPage = () => {
  const [data, setData] = useState([]);
  useEffect(() => {
    axiosService.get('/client/').then((res) => {
      console.log(res);
      setData(res.data);
    });
  }, []);
  return (
    <>
      <Box sx={{ display: 'flex' }}>
        <SideBar2 />
        <Box sx={{ flexGrow: 1, p: 3 }}>
          <ClientTable />
        </Box>
      </Box>
    </>
  );
};
