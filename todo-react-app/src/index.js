import React from 'react';
import { createRoot } from 'react-dom/client';
import './index.css';
//import reportWebVitals from './reportWebVitals';
import AppRouter from './AppRouter';

const container = document.getElementById('root');

const root = createRoot(container);

root.render( 
    <AppRouter tab="home" /> 
);

//reportWebVitals(); 애플리케이션의 성능 측정 데이터를 수집하는 데 사용하는 함수, 지금은 생략한다.