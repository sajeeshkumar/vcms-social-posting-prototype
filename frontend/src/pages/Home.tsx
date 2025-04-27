import React from 'react';
import { usePlatforms } from '../hooks/usePlatforms';
import PlatformList from '../components/PlatformList';

const Home: React.FC = () => {
  const { platforms, loading, error } = usePlatforms();

  if (loading) return <p>Loading platforms...</p>;
  if (error) return <p>Error loading platforms: {error}</p>;

  return (
    <div className="home">
      <h1>Supported Platforms</h1>
      <PlatformList platforms={platforms} />
    </div>
  );
};

export default Home;
