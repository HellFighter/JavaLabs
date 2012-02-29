package ru.spbstu.telematics.lab2;
  /**
   * Саморасширяющаяся коллекция. Объекты хранятся в массиве отсортированном порядке.
   */
  public interface ISortedVector {
      /**
       * Добавляет объект в коллекцию
       * @param o
       */
     void add(Comparable o);
 
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
     Comparable get(int index);
 
     /**
      * Возвращает индекс объекта, если такой есть в векторе. Если такого нет, то -1.
      * @param o
      * @return
      */
     int indexOf(Comparable o);
 }
  