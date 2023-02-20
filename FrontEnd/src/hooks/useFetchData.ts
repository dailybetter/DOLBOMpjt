import { axiosService } from '../api/instance';
interface clientList {
  id: number;
  name: string;
  phone: string;
  email: string;
}
export const useFetchData = async () => {
  try {
    const res = await axiosService.get('/client/');

    return res.data.map((item: clientList) => item.name);
    // return res.data.map((item: any) => item.userName);
  } catch {
    console.log('dataFetchingerr');
  }
};
