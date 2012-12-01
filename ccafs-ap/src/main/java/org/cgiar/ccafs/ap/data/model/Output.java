package org.cgiar.ccafs.ap.data.model;

import org.apache.commons.lang3.builder.ToStringBuilder;


public class Output {

  private int id;
  private Objective objective;
  private String code;
  private String description;

  public Output(int id) {
    this.id = id;
  }

  public Output(int id, Objective objective, String code, String description) {
    this.id = id;
    this.objective = objective;
    this.code = code;
    this.description = description;
  }

  public String getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }

  public int getId() {
    return id;
  }

  public Objective getObjective() {
    return objective;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setObjective(Objective objective) {
    this.objective = objective;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
