import { axiosService } from '../../api/instance';

export const login = async ({ id, pw }: { id: string; pw: string }) => {
  const { data } = await axiosService.post('/users/login', {
    email: id,
    password: pw,
  });
  return data;
};

export const getUserInfo = async () => {
  const { data } = await axiosService.get('/users/user/mypage');
  return data;
};
