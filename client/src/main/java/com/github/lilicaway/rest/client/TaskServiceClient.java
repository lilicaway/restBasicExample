package com.github.lilicaway.rest.client;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.github.lilicaway.rest.service.definition.TaskService;
import com.github.lilicaway.rest.service.definition.dto.Task;
import com.github.lilicaway.rest.service.definition.errors.NotFoundException;

public class TaskServiceClient implements TaskService {
  // This could be injected.
  private final RestTemplate restTemplate = new RestTemplate();

  private final String baseUrl;

  public TaskServiceClient(String baseUrl) {
    // Example: "http://localhost:8383"
    this.baseUrl = baseUrl;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Task> getAllTasks() {
    return restTemplate.getForObject("{baseUrl}/tasks", List.class, baseUrl);
  }

  @Override
  public Task getTask(String taskId) throws NotFoundException {
    try {
      return restTemplate.getForObject("{baseUrl}/tasks/{taskId}", Task.class, baseUrl, taskId);
    } catch (HttpClientErrorException e) {
      handleHttpClientErrorException(e, taskId);
      throw e;
    }
  }

  @Override
  public String newTask(Task task) {
    return restTemplate.postForObject("{baseUrl}/tasks", task, String.class, baseUrl);
  }

  @Override
  public void updateTask(String taskId, Task task) throws NotFoundException {
    try {
      restTemplate.put("{baseUrl}/tasks/{taskId}", task, baseUrl, taskId);
    } catch (HttpClientErrorException e) {
      handleHttpClientErrorException(e, taskId);
    }
  }

  @Override
  public void deleteTask(String taskId) throws NotFoundException {
    try {
      restTemplate.delete("{baseUrl}/tasks/{taskId}", baseUrl, taskId);
    } catch (HttpClientErrorException e) {
      handleHttpClientErrorException(e, taskId);
    }
  }

  private void handleHttpClientErrorException(HttpClientErrorException e, String taskId) {
    if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
      throw new NotFoundException("Task with id '" + taskId + "' was not found.");
    }
    throw e;
  }
}
