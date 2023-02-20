import axios, { AxiosHeaders } from 'axios';
import type { NavigateFunction } from 'react-router-dom';

import { store } from '../app/store';
import { clearUser } from '../features/auth/userSlice';

axios.defaults.headers.common['Content-Type'] = 'application/json';

export const BASE_URL = 'https://i8c103.p.ssafy.io/api';
// export const BASE_URL = 'http://43.200.35.232:8080/api';

export const axiosService = axios.create({ baseURL: BASE_URL });
const accessToken = sessionStorage.getItem('access-token');
const refreshToken = sessionStorage.getItem('refresh-token');

if (accessToken) axiosService.defaults.headers.common['access-token'] = accessToken;
if (refreshToken) axiosService.defaults.headers.common['refresh-token'] = refreshToken;
type CustomHeaders = {
  'access-token': string;
  'refresh-token': string;
} & AxiosHeaders;

export const setAxiosConfig = (navigate: NavigateFunction) => {
  axiosService.interceptors.request.use(
    function (config) {
      const headers = config.headers as CustomHeaders;

      const accessToken = sessionStorage.getItem('access-token');
      const refreshToken = sessionStorage.getItem('refresh-token');

      if (accessToken) headers['access-token'] = accessToken;
      if (refreshToken) headers['refresh-token'] = refreshToken;

      return config;
    },
    function (error) {
      return Promise.reject(error);
    },
  );

  axiosService.interceptors.response.use(
    async function (res) {
      if (res.data.statusCodeValue === 401) {
        // UNAUTHORIZED
        // access-token 만료
        const instance = axios.create({ baseURL: BASE_URL });

        const accessToken = sessionStorage.getItem('access-token');
        const refreshToken = sessionStorage.getItem('refresh-token');

        instance.defaults.headers['access-token'] = accessToken;
        instance.defaults.headers['refresh-token'] = refreshToken;

        try {
          const result = await instance.get('users/token/refresh');
          const accessToken = result.headers['access-token'];
          if (accessToken) sessionStorage.setItem('access-token', accessToken);
          navigate(0); // refresh
        } catch (err) {
          // refresh token 만료
          alert('다시 로그인 해주세요');
          store.dispatch(clearUser());
          navigate('/login');
        }
      }
      return res;
    },
    function (err) {
      return Promise.reject(err);
    },
  );
};
