import Button from '@mui/material/Button';
import { Link } from 'react-router-dom';

export const LoginButton = () => {
  return (
    <Link to="/login" style={{ textDecoration: 'none' }}>
      <Button sx={{ my: 2, color: 'white', display: 'block' }}>log in</Button>
    </Link>
  );
};
