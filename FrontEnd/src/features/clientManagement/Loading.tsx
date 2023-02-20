import Box from '@mui/material/Box';
import LinearProgress from '@mui/material/LinearProgress';
import Typography from '@mui/material/Typography';
export const Loading = () => {
  return (
    <Box
      sx={{
        p: 1,
        m: 1,
      }}
    >
      <Typography variant="h5" align="center" sx={{ mt: '10vh', mb: '3vh' }}>
        잠시만 기다려주세요!
      </Typography>
      <LinearProgress color="success" />
    </Box>
  );
};
