package upce.abstrHeap;

import upce.abstrTree.AbstrLIFO;
import upce.abstrTree.LIFO;
import upce.abstrTreeEnum.eTypProhl;


import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class AbstrHeap<T extends Comparable<T>> implements AbstrHeapInterface<T> {

    private Prvek<T>[] pole;
    private int pocetPrvku;

    private Comparator<T> comparator;

    public AbstrHeap(Comparator<T> comparator) {
        this.comparator = comparator;
        pole = new Prvek[10];
        pocetPrvku = 0;
    }

    private class Prvek<T> {
        T data;

        public Prvek(T data) {
            this.data = data;
        }
    }

    public void setComparator(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    @Override
    public void vybuduj(T[] obj) {
        // Инициализируем массив pole новым размером, который на единицу больше размера obj.
        // Дополнительная единица необходима, так как индексация начинается с 1.
        pole = new Prvek[obj.length + 1];

        // Устанавливаем количество элементов в куче равным размеру переданного массива obj.
        pocetPrvku = obj.length;

        // Копируем элементы из массива obj в массив pole, начиная с индекса 1.
        for (int i = 0; i < pocetPrvku; i++) {
            pole[i + 1] = new Prvek<T>(obj[i]);
        }

        // Перестраиваем структуру pole в кучу, начиная с середины массива и двигаясь к корню.
        // Это необходимо для удовлетворения свойств кучи.
        for (int i = pocetPrvku / 2; i >= 1; i--) {
            // Для каждого узла, начиная с последнего родителя и до корня,
            // применяем метод heapify, чтобы упорядочить узлы в соответствии со свойствами кучи.
            heapify(i);
        }
    }

    @Override
    public void prebuduj() {
        // Начинаем с середины массива, так как листовые узлы уже удовлетворяют свойствам кучи.
        // Итерация в обратном порядке к корню кучи (индекс 1).
        for (int i = pocetPrvku / 2; i >= 1; i--) {
            // Вызываем метод heapify для текущего узла.
            // Этот метод убедится, что узел соответствует свойствам кучи,
            // и если нет, то выполнит необходимые обмены с дочерними узлами.
            heapify(i);
        }
    }

    private void heapify(int i) {
        // Определяем индексы левого и правого дочерних элементов текущего узла.
        int left = 2 * i;
        int right = 2 * i + 1;
        // Начинаем с предположения, что текущий узел (i) является узлом, который нужно "упорядочить".
        int largest = i;

        // Проверяем, больше ли левый дочерний элемент, чем текущий узел, используя компаратор.
        if (left <= pocetPrvku && comparator.compare(pole[left].data, pole[largest].data) > 0) {
            largest = left;
        }

        // Аналогичная проверка для правого дочернего элемента.
        if (right <= pocetPrvku && comparator.compare(pole[right].data, pole[largest].data) > 0) {
            largest = right;
        }

        // Если один из дочерних узлов больше, чем текущий,
        // меняем их местами и рекурсивно применяем heapify к измененному поддереву.
        if (largest != i) {
            Prvek<T> temp = pole[i];
            pole[i] = pole[largest];
            pole[largest] = temp;
            heapify(largest);
        }
    }



    @Override
    public void zrus() {
        pole = new Prvek[10];
        pocetPrvku = 0;
    }

    @Override
    public boolean jePrazdny() {
        return pocetPrvku == 0;
    }

    /*
    @Override
    public void vloz(T vloz) {
        if (jePrazdny()) {
            pole[1] = new Prvek<>(vloz);
            pocetPrvku++;
        } else {
            if (pocetPrvku + 1 >= pole.length) zvys();
            pole[pocetPrvku + 1] = new Prvek<>(vloz);
            pocetPrvku++;
            for (int i = pocetPrvku; i > 0; i = i / 2) {
                if (i / 2 > 0 && pole[i / 2].data.compareTo(pole[i].data) > 0) {
                    T pom = pole[i].data;
                    pole[i] = new Prvek<T>(pole[i / 2].data);
                    pole[i / 2] = new Prvek<T>(pom);
                } else break;
            }
        }
    }

    private void zvys() {
        Prvek<T>[] copy = new Prvek[pole.length * 2];
        System.arraycopy(pole, 1, copy, 1, pole.length - 1);
        pole = copy;
    }
     */

    @Override
    public void vloz(T vloz) {
        // Увеличиваем размер массива, если необходимо
        if (pocetPrvku + 1 >= pole.length) {
            zvys();
        }

        // Добавляем новый элемент в конец массива
        pole[++pocetPrvku] = new Prvek<>(vloz);

        // "Пузырьковый" подъем нового элемента
        int index = pocetPrvku;
        while (index > 1 && comparator.compare(pole[index].data, pole[index / 2].data) > 0) {
            // Меняем местами родительский и текущий узлы
            Prvek<T> temp = pole[index];
            pole[index] = pole[index / 2];
            pole[index / 2] = temp;

            // Перемещаемся вверх к корню кучи
            index /= 2;
        }
    }

    private void zvys() {
        Prvek<T>[] copy = new Prvek[pole.length * 2];
        System.arraycopy(pole, 1, copy, 1, pole.length - 1);
        pole = copy;
    }


    @Override
    public T zpristupniMax() {
        return pole[1].data;
    }

    @Override
    public T odeberMax() {
        if (jePrazdny()) {
            throw new NoSuchElementException("Kucha je prázdná");
        }

        T maxElement = pole[1].data;  // Сохраняем элемент с наивысшим приоритетом
        pole[1] = pole[pocetPrvku];  // Перемещаем последний элемент в корень
        pole[pocetPrvku] = null;     // Удаляем последний элемент
        pocetPrvku--;

        if (!jePrazdny()) {
            heapify(1);  // Перестраиваем кучу
        }

        return maxElement;
    }


    @Override
    public Iterator iterator(eTypProhl typ) {
        switch (typ) {
            case DO_SIRZKY -> {
                return iterator();
            }
            case DO_HLOUBKY -> {
                return iteratorHloubky();
            }
        }
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int i = 1;

            @Override
            public boolean hasNext() {
                return i <= pocetPrvku;  // Fixed condition to correctly check if there are more elements
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more elements in the heap");
                }
                return pole[i++].data;  // Post-increment i to get the current element and then increment
            }
        };
    }


    public Iterator<T> iteratorHloubky() {
        Stack<Integer> stack = new Stack<>();
        if (pocetPrvku > 0) {
            stack.push(1);
        }

        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return !stack.isEmpty();
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                int currentIndex = stack.pop();
                int leftChild = currentIndex * 2;
                int rightChild = currentIndex * 2 + 1;

                if (rightChild <= pocetPrvku) {
                    stack.push(rightChild);
                }
                if (leftChild <= pocetPrvku) {
                    stack.push(leftChild);
                }

                return pole[currentIndex].data;
            }
        };
    }

}
