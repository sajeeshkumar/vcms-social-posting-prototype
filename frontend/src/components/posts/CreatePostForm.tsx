import React, { useState, useEffect } from 'react';
import { createPost, getPlatforms } from '../../services/api';
import { PostRequest } from '../../types/Post';
import Toast from '../toasts/Toast';
import styles from './CreatePostForm.module.css';

const CreatePostForm = () => {
  const [message, setMessage] = useState('');
  const [mediaUrls, setMediaUrls] = useState<string[]>([]);
  const [platforms, setPlatforms] = useState<string[]>([]);
  const [availablePlatforms, setAvailablePlatforms] = useState<string[]>([]);
  const [scheduleTime, setScheduleTime] = useState('');

  const [toast, setToast] = useState<{message: string; type: 'success' | 'error'} | null>(null);

  useEffect(() => {
    getPlatforms().then(setAvailablePlatforms);
  }, []);

  const showToast = (message: string, type: 'success' | 'error') => {
    setToast({ message, type });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const post: PostRequest = {
      message,
      mediaUrls,
      platforms,
      scheduleTime: scheduleTime ? new Date(scheduleTime).toISOString() : undefined,
    };

    try {
      await createPost(post);
      showToast('Post scheduled/created successfully!', 'success');
      setMessage('');
      setMediaUrls([]);
      setPlatforms([]);
      setScheduleTime('');
    } catch (error) {
      console.error('Error creating post:', error);
      showToast('Failed to create post', 'error');
    }
  };

  const handleMediaUrlChange = (index: number, value: string) => {
    const updatedUrls = [...mediaUrls];
    updatedUrls[index] = value;
    setMediaUrls(updatedUrls);
  };

  const addMediaUrlField = () => {
    setMediaUrls([...mediaUrls, '']);
  };

  return (
    <div className={styles.container}>
      <form onSubmit={handleSubmit} className={styles.card}>
        <h2 className={styles.title}>Create a New Post</h2>

        {/* Form Fields Here */}
        {/* (same as before) */}

        <div>
          <label className={styles.label}>Message</label>
          <textarea
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            required
            className={styles.textarea}
            rows={4}
          />
        </div>

        <div>
          <label className={styles.label}>Media URLs</label>
          {mediaUrls.map((url, index) => (
            <input
              key={index}
              type="text"
              value={url}
              onChange={(e) => handleMediaUrlChange(index, e.target.value)}
              className={styles.input}
            />
          ))}
          <button
            type="button"
            onClick={addMediaUrlField}
            className={styles.addMediaButton}
          >
            + Add Media URL
          </button>
        </div>

        <div>
          <label className={styles.label}>Platforms</label>
          <div className={styles.checkboxGroup}>
            {availablePlatforms.map((platform) => (
              <label key={platform} className={styles.checkboxItem}>
                <input
                  type="checkbox"
                  value={platform}
                  checked={platforms.includes(platform)}
                  onChange={(e) => {
                    if (e.target.checked) {
                      setPlatforms([...platforms, platform]);
                    } else {
                      setPlatforms(platforms.filter(p => p !== platform));
                    }
                  }}
                />
                <span>{platform}</span>
              </label>
            ))}
          </div>
        </div>

        <div>
          <label className={styles.label}>Schedule Time (optional)</label>
          <input
            type="datetime-local"
            value={scheduleTime}
            onChange={(e) => setScheduleTime(e.target.value)}
            className={styles.datetime}
          />
        </div>

        <button
          type="submit"
          className={styles.button}
        >
          Submit
        </button>
      </form>

      {toast && (
        <Toast
          message={toast.message}
          type={toast.type}
          onClose={() => setToast(null)}
        />
      )}
    </div>
  );
};

export default CreatePostForm;
