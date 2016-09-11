package com.github.lilicaway.rest.client;

import java.util.Date;

import com.github.lilicaway.rest.service.definition.dto.Task;
import com.github.lilicaway.rest.service.definition.errors.NotFoundException;

public class ClientApp {

  public static void main(String[] args) {
    TaskServiceClient client = new TaskServiceClient("http://localhost:8383");
    {
      Task task = client.getTask("1");
      System.out.println("task 1: " + task);
    }
    System.out.println("allTasks: " + client.getAllTasks());
    {
      Task task = new Task();
      task.setName("this is a new task");
      String newTaskId = client.newTask(task);
      System.out.println("Created task with id " + newTaskId);
      System.out.println("allTasks: " + client.getAllTasks());

      task = client.getTask(newTaskId);
      System.out.println("The new task is: " + task);

      // Now we update it
      task.setLastUpdate(new Date());
      task.setName("something else");
      client.updateTask(newTaskId, task);
      task = client.getTask(newTaskId);
      System.out.println("The updated task is: " + task);

      System.out.println("Before deleting allTasks: " + client.getAllTasks());
      client.deleteTask(newTaskId);
      System.out.println("After deleting  allTasks: " + client.getAllTasks());

      // Try to get it again after deleted
      try {
        task = client.getTask(newTaskId);
      } catch (NotFoundException e) {
        e.printStackTrace();
      }

      // delete non existent task
      try {
        client.deleteTask(newTaskId);
      } catch (NotFoundException e) {
        e.printStackTrace();
      }

      // update non existent task
      try {
        client.updateTask(newTaskId, task);
      } catch (NotFoundException e) {
        e.printStackTrace();
      }
    }
  }
}
