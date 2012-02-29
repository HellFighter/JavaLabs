package ru.spbstu.telematics.lab2;
  /**
  * Саморасширяющаяся коллекция. Объекты хранятся в массиве отсортированном порядке.
  */
 public interface ISortedVector<T extends Comparable<T>> {
     /**
      * Добавляет объект в коллекцию
      * @param o
      */
     void add(T o);//Comparable o);
 
     /**
      * Удаляет объект из коллекции, находящийся на указанной позиции
      * @param index
      */
     void remove(int index);
 
     /**
      * Возвращает объект, находящийся на определенной позиции
      * @param index
      * @return
      */
     T get(int index);
 
     /**
      * Возвращает индекс объекта, если такой есть в векторе. Если такого нет, то -1.
      * @param o
      * @return
      */
     int indexOf(T o);
 }
  