import './App.css';

import { BrowserRouter } from 'react-router-dom';

import { AxiosSetup } from './features/axios-setup/AxiosSetup';
import { HomePage } from './pages/HomePage';

function App() {
  return (
    <BrowserRouter>
      <AxiosSetup />
      <HomePage />
    </BrowserRouter>
  );
}

export default App;
