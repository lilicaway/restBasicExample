package com.github.lilicaway.rest.service.definition.dto;

import java.util.Date;

public class Task {
  private String id;
  private String name;
  private Date lastUpdate;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Task [id=").append(id).append(", name=").append(name).append(", lastUpdate=").append(lastUpdate)
        .append("]");
    return builder.toString();
  }

}
