import axios from 'axios';
import { PostRequest } from '../types/Post';

const API_BASE_URL = 'http://localhost:8080';

export const createPost = async (postRequest: PostRequest) => {
  const response = await axios.post(`${API_BASE_URL}/posts`, postRequest);
  return response.data;
};

export const getPlatforms = async (): Promise<string[]> => {
  const response = await axios.get(`${API_BASE_URL}/platforms`);
  return response.data;
};
