package ca.kodingkrafters.inc.vcms.backend.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PostRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-04-26T16:42:58.342157-04:00[America/Toronto]", comments = "Generator version: 7.12.0")
public class PostRequest {

  private String message;

  @Valid
  private List<String> mediaUrls = new ArrayList<>();

  @Valid
  private List<String> platforms = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable Date scheduleTime;

  public PostRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PostRequest(String message, List<String> platforms) {
    this.message = message;
    this.platforms = platforms;
  }

  public PostRequest message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  @NotNull 
  @Schema(name = "message", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public PostRequest mediaUrls(List<String> mediaUrls) {
    this.mediaUrls = mediaUrls;
    return this;
  }

  public PostRequest addMediaUrlsItem(String mediaUrlsItem) {
    if (this.mediaUrls == null) {
      this.mediaUrls = new ArrayList<>();
    }
    this.mediaUrls.add(mediaUrlsItem);
    return this;
  }

  /**
   * Get mediaUrls
   * @return mediaUrls
   */
  
  @Schema(name = "mediaUrls", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mediaUrls")
  public List<String> getMediaUrls() {
    return mediaUrls;
  }

  public void setMediaUrls(List<String> mediaUrls) {
    this.mediaUrls = mediaUrls;
  }

  public PostRequest platforms(List<String> platforms) {
    this.platforms = platforms;
    return this;
  }

  public PostRequest addPlatformsItem(String platformsItem) {
    if (this.platforms == null) {
      this.platforms = new ArrayList<>();
    }
    this.platforms.add(platformsItem);
    return this;
  }

  /**
   * Get platforms
   * @return platforms
   */
  @NotNull 
  @Schema(name = "platforms", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("platforms")
  public List<String> getPlatforms() {
    return platforms;
  }

  public void setPlatforms(List<String> platforms) {
    this.platforms = platforms;
  }

  public PostRequest scheduleTime(Date scheduleTime) {
    this.scheduleTime = scheduleTime;
    return this;
  }

  /**
   * Get scheduleTime
   * @return scheduleTime
   */
  @Valid 
  @Schema(name = "scheduleTime", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scheduleTime")
  public Date getScheduleTime() {
    return scheduleTime;
  }

  public void setScheduleTime(Date scheduleTime) {
    this.scheduleTime = scheduleTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostRequest postRequest = (PostRequest) o;
    return Objects.equals(this.message, postRequest.message) &&
        Objects.equals(this.mediaUrls, postRequest.mediaUrls) &&
        Objects.equals(this.platforms, postRequest.platforms) &&
        Objects.equals(this.scheduleTime, postRequest.scheduleTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message, mediaUrls, platforms, scheduleTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PostRequest {\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    mediaUrls: ").append(toIndentedString(mediaUrls)).append("\n");
    sb.append("    platforms: ").append(toIndentedString(platforms)).append("\n");
    sb.append("    scheduleTime: ").append(toIndentedString(scheduleTime)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

