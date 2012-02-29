package ru.spbstu.telematics.lab2;
  /**
  * ����������������� ���������. ������� �������� � ������� ��������������� �������.
  */
 public interface ISortedVector<T extends Comparable<T>> {
     /**
      * ��������� ������ � ���������
      * @param o
      */
     void add(T o);//Comparable o);
 
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
     T get(int index);
 
     /**
      * ���������� ������ �������, ���� ����� ���� � �������. ���� ������ ���, �� -1.
      * @param o
      * @return
      */
     int indexOf(T o);
 }
  