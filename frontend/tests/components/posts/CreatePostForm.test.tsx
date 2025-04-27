import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { vi } from 'vitest';
import CreatePostForm from '../../../src/components/posts/CreatePostForm';
import axios from 'axios';

describe('CreatePostForm', () => {
  // Mock axios methods before each test
  beforeEach(() => {
    // Mock axios POST request for creating a post
    vi.spyOn(axios, 'post').mockResolvedValue({
      data: { message: 'Post created successfully' },
    });
    // Mock axios GET request for fetching platforms (if needed)
    vi.spyOn(axios, 'get').mockResolvedValue({
      data: ['Twitter', 'Instagram'],
    });

    render(<CreatePostForm />);
  });

  it('renders all the form fields correctly', () => {
    expect(screen.getByText('Create a New Post')).toBeInTheDocument();
    expect(screen.getByText('Message')).toBeInTheDocument();
    expect(screen.getByText('+ Add Media URL')).toBeInTheDocument();
    expect(screen.getByText('Platforms')).toBeInTheDocument();
    expect(screen.getByText('Schedule Time (optional)')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /submit/i })).toBeInTheDocument();
  });

  it('allows typing in the Message textarea', () => {
    const messageInput = screen.getByLabelText('Message') as HTMLTextAreaElement;
    fireEvent.change(messageInput, { target: { value: 'This is a test message' } });
    expect(messageInput.value).toBe('This is a test message');
  });

  it('submits the form and mocks the axios POST request', async () => {
    const messageInput = screen.getByLabelText('Message') as HTMLTextAreaElement;
    fireEvent.change(messageInput, { target: { value: 'Another test message' } });

    const submitButton = screen.getByRole('button', { name: /submit/i });
    fireEvent.click(submitButton);

    // Wait for the axios.post mock to be called
    await waitFor(() => expect(axios.post).toHaveBeenCalledTimes(1));

    // You can add more expectations if you want to verify that the toast appeared or other side effects
    expect(messageInput.value).toBe('');
  });

  it('fetches platforms correctly using the mocked axios GET request', async () => {
    // Wait for the axios.get mock to be called
    await waitFor(() => expect(axios.get).toHaveBeenCalledTimes(1));

    // You can test if the platforms are displayed (if applicable)
    expect(screen.getByText('Twitter')).toBeInTheDocument();
    expect(screen.getByText('Instagram')).toBeInTheDocument();
  });
});
