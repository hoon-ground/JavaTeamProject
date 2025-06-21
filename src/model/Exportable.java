package model;

// 인터페이스 구현 
// 객체 종류에 관계없이 다양한 객체를 동일한 방식으로 내보낼 수 있음.

public interface Exportable {
    String toExportString();
}