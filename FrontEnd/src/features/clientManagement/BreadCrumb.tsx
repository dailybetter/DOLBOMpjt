import NavigateNextIcon from '@mui/icons-material/NavigateNext';
import { Breadcrumbs, Typography } from '@mui/material';

import { useAppSelector } from '../../app/hooks';
import { selectDashboard } from './dashboardSlice';
export const BreadCrum = () => {
  const currentState = useAppSelector(selectDashboard);
  const pathStack = currentState.pathStack;
  const breadcrumb = pathStack.map((value, index) => (
    <Typography key={index} color="text.primary" variant="h4">
      {value}
    </Typography>
  ));
  return (
    <Breadcrumbs
      separator={<NavigateNextIcon fontSize="small" />}
      aria-label="breadcrumb"
    >
      {/* {pathStack.map((value, index) => (
        <Typography key={index} color="text.primary" variant="h4">
          {value}
        </Typography>
      ))} */}
      {breadcrumb}
    </Breadcrumbs>
  );
};
