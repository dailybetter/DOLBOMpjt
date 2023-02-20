import { Container } from '@mui/material';
import Grid from '@mui/material/Grid';
import * as React from 'react';

import { Calendar } from '../features/calendar/Calendar';
import { SideBar2 } from '../features/sideBar/SideBar2';
export const CalendarPage = () => {
  return (
    <>
      <Grid container spacing={2}>
        <Grid item xs={2}>
          <SideBar2 />
        </Grid>
        <Grid item xs={10}>
          <Container fixed maxWidth="xl">
            <h1>CalendPage</h1>
            <Calendar />
          </Container>
        </Grid>
      </Grid>
    </>
  );
};
