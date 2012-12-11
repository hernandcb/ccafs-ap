package org.cgiar.ccafs.ap.data.model;

import org.apache.commons.lang3.builder.ToStringBuilder;


public class ActivityPartner {

  private int id;
  private String name;
  private String email;


  public ActivityPartner() {
  }

  public ActivityPartner(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
