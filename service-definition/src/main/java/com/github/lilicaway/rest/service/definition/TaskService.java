package com.github.lilicaway.rest.service.definition;

import java.util.List;

import com.github.lilicaway.rest.service.definition.dto.Task;
import com.github.lilicaway.rest.service.definition.errors.NotFoundException;

public interface TaskService {
  List<Task> getAllTasks();
  
  Task getTask(String id) throws NotFoundException;
  String newTask(Task task);
  void updateTask(String id, Task task) throws NotFoundException;
  void deleteTask(String taskId) throws NotFoundException;
}
