package xcj.hs.entity;

import lombok.Data;

@Data
public class Point {

  private String pointId;
  private String pointDetail;
  private String chapter;
  private String isbn;
  private String grade;
  private String distribution;
  private String frequency;

  public Point() {}

  public Point(
      String pointId,
      String pointDetail,
      String chapter,
      String isbn,
      String grade,
      String distribution,
      String frequency) {
    this.pointId = pointId;
    this.pointDetail = pointDetail;
    this.chapter = chapter;
    this.isbn = isbn;
    this.grade = grade;
    this.distribution = distribution;
    this.frequency = frequency;
  }
}
