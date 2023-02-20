import Grid from '@mui/material/Grid';
import * as React from 'react';

export const UserProfilePage = () => {
  return (
    <>
      <Grid container spacing={2}>
        <Grid item xs={2}>
          <h1>User</h1>
        </Grid>
        <Grid item xs={10}>
          <h1>UserProfilePage</h1>
        </Grid>
      </Grid>
    </>
  );
};
