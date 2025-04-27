export interface PostRequest {
    message: string;
    mediaUrls?: string[];
    platforms: string[];
    scheduleTime?: string; // ISO format
  }
  