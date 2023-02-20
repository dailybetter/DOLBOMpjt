import Button from '@mui/material/Button';
import { Link } from 'react-router-dom';

export const SignupButton = () => {
  return (
    <Link to="/signup" style={{ textDecoration: 'none' }}>
      <Button sx={{ my: 2, color: 'white', display: 'block' }}>Sign up</Button>
    </Link>
  );
};
