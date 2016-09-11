package com.github.lilicaway.rest.server.task;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.lilicaway.rest.service.definition.TaskService;
import com.github.lilicaway.rest.service.definition.dto.Task;
import com.github.lilicaway.rest.service.definition.errors.NotFoundException;

/**
 * This is a dummy implementation that just has everything in memory. A real implementation would store things in a
 * database or some other persistent storage.
 */
@RestController
@RequestMapping("/tasks")
public class TaskServiceImpl implements TaskService {
  /**
   * Our dummy storage system. Keyed by the Task id.
   */
  private final Map<String, Task> allTasks = new HashMap<>();
  private final AtomicInteger nextId = new AtomicInteger(0);

  {
    // We add some dummy tasks at construction time.
    {
      Task task = new Task();
      task.setName("this is task1");
      task.setLastUpdate(new Date());
      newTask(task);
    }
    {
      Task task = new Task();
      task.setName("this is task2");
      task.setLastUpdate(new Date());
      newTask(task);
    }
  }

  @Override
  @GetMapping(path = "")
  public List<Task> getAllTasks() {
    return new ArrayList<>(allTasks.values());
  }

  @Override
  @GetMapping(path = "/{taskId}")
  public Task getTask(@PathVariable String taskId) throws NotFoundException {
    Task taskFound = allTasks.get(taskId);
    if (taskFound == null) {
      throw new NotFoundException("Task with id '" + taskId + "' not found");
    }
    return taskFound;
  }

  /**
   * This is just a wrapper of {@link #newTask(Task)} that takes care of returning status 201 (Created) which is the
   * standard for Rest services.
   */
  @PostMapping(path = "")
  public ResponseEntity<String> newTaskAsEntity(@RequestBody Task task) throws URISyntaxException {
    String newTaskId = newTask(task);
    return ResponseEntity.created(new URI("/tasks/" + newTaskId)).body(newTaskId);
  }

  @Override
  public String newTask(Task task) {
    String id = Integer.toString(nextId.getAndIncrement());
    task.setId(id);
    allTasks.put(id, task);
    return id;
  }

  @Override
  @PutMapping(path = "/{taskId}")
  public void updateTask(@PathVariable String taskId, @RequestBody Task task) throws NotFoundException {
    if (allTasks.get(taskId) == null) {
      throw new NotFoundException("Task with id '" + taskId + "' not found");
    }
    allTasks.put(taskId, task);
  }

  @Override
  @DeleteMapping(path = "/{taskId}")
  public void deleteTask(@PathVariable String taskId) throws NotFoundException {
    Task removedTask = allTasks.remove(taskId);
    if (removedTask == null) {
      throw new NotFoundException("Task with id '" + taskId + "' not found");
    }
  }
}
