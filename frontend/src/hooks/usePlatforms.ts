import { useEffect, useState } from 'react';
import api from '../services/api';

export const usePlatforms = () => {
  const [platforms, setPlatforms] = useState<string[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    api.get<string[]>('/platforms')
      .then(response => setPlatforms(response.data))
      .catch(error => setError(error.message))
      .finally(() => setLoading(false));
  }, []);

  return { platforms, loading, error };
};
