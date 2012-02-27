package ru.spbstu.telematics.lab2;
  /**
   * ����������������� ���������. ������� �������� � ������� ��������������� �������.
   */
  public interface ISortedVector {
      /**
       * ��������� ������ � ���������
       * @param o
       */
     void add(Comparable o);
 
     /**
      * ������� ������ �� ���������, ����������� �� ��������� �������
      * @param index
      */
     void remove(int index);
 
     /**
      * ���������� ������, ����������� �� ������������ �������
      * @param index
      * @return
      */
     Comparable get(int index);
 
     /**
      * ���������� ������ �������, ���� ����� ���� � �������. ���� ������ ���, �� -1.
      * @param o
      * @return
      */
     int indexOf(Comparable o);
 }
  