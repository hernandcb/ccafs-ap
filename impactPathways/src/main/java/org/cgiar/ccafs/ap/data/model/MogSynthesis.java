package org.cgiar.ccafs.ap.data.model;
// Generated Mar 3, 2016 1:58:15 PM by Hibernate Tools 4.3.1.Final


/**
 * MogSynthesis generated by hbm2java
 */
public class MogSynthesis implements java.io.Serializable {


  private Integer id;
  private int programId;
  private int mogId;
  private int year;
  private String synthesisReport;
  private String synthesisGender;

  public MogSynthesis() {
  }

  public MogSynthesis(int programId, int mogId, int year, String synthesisReport, String synthesisGender) {
    this.programId = programId;
    this.mogId = mogId;
    this.year = year;
    this.synthesisReport = synthesisReport;
    this.synthesisGender = synthesisGender;
  }

  public Integer getId() {
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public int getProgramId() {
    return this.programId;
  }

  public void setProgramId(int programId) {
    this.programId = programId;
  }

  public int getMogId() {
    return this.mogId;
  }

  public void setMogId(int mogId) {
    this.mogId = mogId;
  }

  public int getYear() {
    return this.year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public String getSynthesisReport() {
    return this.synthesisReport;
  }

  public void setSynthesisReport(String synthesisReport) {
    this.synthesisReport = synthesisReport;
  }

  public String getSynthesisGender() {
    return this.synthesisGender;
  }

  public void setSynthesisGender(String synthesisGender) {
    this.synthesisGender = synthesisGender;
  }


}

