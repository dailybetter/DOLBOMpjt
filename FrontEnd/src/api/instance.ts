import axios, { AxiosHeaders } from 'axios';
import type { NavigateFunction } from 'react-router-dom';

import { store } from '../app/store';
import { clearUser } from '../features/auth/userSlice';

axios.defaults.headers.common['Content-Type'] = 'application/json';

export const BASE_URL = 'https://--------/api';

const axiosService = axios.create({ baseURL: BASE_URL });

type CustomHeaders = {
  'access-token'?: string;
  'refresh-token'?: string;
} & AxiosHeaders;

const setAuthHeaders = (headers: CustomHeaders) => {
  const accessToken = sessionStorage.getItem('access-token');
  const refreshToken = sessionStorage.getItem('refresh-token');

  if (accessToken) headers['access-token'] = accessToken;
  if (refreshToken) headers['refresh-token'] = refreshToken;

  return headers;
};

const getAccessToken = async () => {
  const instance = axios.create({ baseURL: BASE_URL });

  const headers: CustomHeaders = {};
  setAuthHeaders(headers);

  try {
    const result = await instance.get('users/token/refresh', { headers });
    const accessToken = result.headers['access-token'];
    if (accessToken) sessionStorage.setItem('access-token', accessToken);
    return accessToken;
  } catch (err) {
    throw err;
  }
};

export const setAxiosConfig = (navigate: NavigateFunction) => {
  axiosService.interceptors.request.use(
    function (config) {
      const headers = config.headers as CustomHeaders;

      setAuthHeaders(headers);

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
        try {
          const accessToken = await getAccessToken();
          if (accessToken) {
            const headers: CustomHeaders = {};
            setAuthHeaders(headers);
            config.headers = headers;
            return axiosService.request(res.config);
          }
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
