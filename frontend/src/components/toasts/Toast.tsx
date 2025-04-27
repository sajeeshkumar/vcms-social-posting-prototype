import React, { useEffect } from 'react';
import styles from './Toast.module.css';

type ToastProps = {
  message: string;
  type: 'success' | 'error';
  onClose: () => void;
};

const Toast: React.FC<ToastProps> = ({ message, type, onClose }) => {
  useEffect(() => {
    const timer = setTimeout(onClose, 3000); // 3 seconds
    return () => clearTimeout(timer);
  }, [onClose]);

  return (
    <div className={`${styles.toast} ${styles[type]}`}>
      {message}
    </div>
  );
};

export default Toast;
