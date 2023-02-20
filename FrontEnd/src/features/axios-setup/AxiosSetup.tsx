import React from 'react';
import { useNavigate } from 'react-router-dom';

import { setAxiosConfig } from '../../api/instance';

export const AxiosSetup = () => {
  const navigate = useNavigate();

  React.useEffect(() => {
    setAxiosConfig(navigate);
  }, []);

  return <></>;
};
