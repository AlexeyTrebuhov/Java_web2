package ru.geekbrains.lesson2;

import java.util.Random;
import java.util.Scanner;

public class Program {


    private static final  int WIN_COUNT = 4;
    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = 'O';
    private static final char DOT_EMPTY = '•';

    private static final Scanner SCANNER = new Scanner(System.in);

    private static char[][] field; // Двумерный массив хранит текущее состояние игрового поля

    private static final Random random = new Random();

    private static int fieldSizeX; // Размерность игрового поля
    private static int fieldSizeY; // Размерность игрового поля


    public static void main(String[] args) {
        while (true){
            initialize();
            printField();
            while (true){
                humanTurn();
                printField();
                if (gameCheck(DOT_HUMAN, "Вы победили!"))
                    break;
                aiTurn();
                printField();
                if (gameCheck(DOT_AI, "Компьютер победил!"))
                    break;
            }
            System.out.println("Желаете сыграть еще раз? (Y - да)");
            if (!SCANNER.next().equalsIgnoreCase("Y"))
                break;
        }
    }

    /**
     * Инициализация игрового поля
     */
    private static void initialize(){

        // Установим размерность игрового поля

        System.out.println("Установите ширину и высоту игрового поля (через пробел)");

        fieldSizeY = SCANNER.nextInt() ;
        fieldSizeX = SCANNER.nextInt() ;

        field = new char[fieldSizeX][fieldSizeY];

        // Пройдем по всем элементам массива
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                // Проинициализируем все элементы массива DOT_EMPTY (признак пустого поля)
                field[x][y] = DOT_EMPTY;
            }
        }
    }

    /**
     * Отрисовка игрового поля
     * //TODO: Поправить отрисовку игрового поля (сделал)
     */
    private static void printField(){
        // Печатаем верхнюю строку с номерами столбцов
        System.out.print("@");
        for (int i = 0; i < fieldSizeY * 2 + 1; i++){
            System.out.print((i % 2 == 0) ? " " : i / 2 + 1);
        }
        System.out.println();

        // отрисовка строк по горизонтали
        for (int i = 0; i < fieldSizeX; i++){
            System.out.print(i + 1 + " ");

            for (int j = 0; j <  fieldSizeY; j++)
                System.out.print(field[i][j] + " ");
            System.out.println();
        }

        // Отрисовка футора
        for (int i = 0; i < fieldSizeX * 2 + 2; i++){
            System.out.print("-");
        }
        System.out.println();

    }

    /**
     * Обработка хода игрока (человек)
     */
    private static void humanTurn(){
        int x, y;
        do
        {
            System.out.print("Введите координаты хода по оси X (от 0 до " + fieldSizeY + ") и по оси Y (от 0 до " + fieldSizeX + ") через пробел >>> ");
            y = SCANNER.nextInt() - 1;
            x = SCANNER.nextInt() - 1;
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = DOT_HUMAN;

    }

    /**
     * Проверка, ячейка является пустой
     * @param x
     * @param y
     * @return
     */
    static boolean isCellEmpty(int x, int y){
        return field[x][y] == DOT_EMPTY;
    }

    /**
     * Проверка корректности ввода
     * (координаты хода не должны превышать размерность массива, игрового поля)
     * @param x
     * @param y
     * @return
     */
    static boolean isCellValid(int x, int y){
        return x >= 0 &&  x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    /**
     * Ход компьютера
     */
    private static void aiTurn() {
        int x, y;
        int count = 0;

        // Проверка по всем горизонталям на два крестика рядом на одной горизонтали


        for (x = 0; x < fieldSizeX; x++) {
            for (y = 0; y < fieldSizeY; y++) {

                if (field[x][y] == DOT_HUMAN && field[x][y + 1] == DOT_HUMAN && field[x][y + 2] == DOT_EMPTY) {
                    field[x][y + 2] = DOT_AI;  //комп ставит ноль справа от двух крестиков, если они подряд по горизонтали
                    count = 1;

                } else {
                    if (field[x][y] == DOT_HUMAN && field[x][y + 1] == DOT_HUMAN && field[x][y - 1] == DOT_EMPTY) {
                        field[x][y - 1] = DOT_AI; //комп ставит ноль слева от двух крестиков, если они подряд по горизонтали
                        count = 1;

                    }
                }
            }
        }
        if (count == 0) {
            do {
                x = random.nextInt(fieldSizeX);
                y = random.nextInt(fieldSizeY);
            }
            while (!isCellEmpty(x, y));
            field[x][y] = DOT_AI;
        }
    }

    /**
     * Проверка победы
     * TODO: Переработать метод в домашнем задании
     * @param c
     * @return
     */
    static boolean checkWin(char c){

        int limiterX = fieldSizeX - WIN_COUNT + 1; // сдвиг на длину линии фишек по оси Х
        int limiterY = fieldSizeY - WIN_COUNT + 1;// сдвиг на длину линии фишек по оси У

        // Проверка по трем горизонталям (работает)
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < limiterY; y++) {
                if (field[x][y] == c && field[x][y+1] == c && field[x][y+2] == c && field[x][y+3] == c) return true;
            }
        }


        // Проверка по диагонали слева направо - сверху вниз (работает)
        for (int x = 0; x < limiterX; x++) {
            for (int y = 0; y < limiterY; y++) {
                if (field[x][y] == c && field[x+1][y+1] == c && field[x+2][y+2] == c && field[x+3][y+3] == c) return true;
            }
        }

        // Проверка по диагонали слева направо - снизу вверх (работает)
        for (int x = 0; x < limiterX; x++) {
            for (int y = WIN_COUNT-1; y < fieldSizeY; y++) {
               if (field[x][y] == c && field[x+1][y-1] == c && field[x+2][y-2] == c && field[x+3][y-3] == c) return true;
            }
        }


        // Проверка по трем вертикалям ( работает)
        for (int x = 0; x < limiterX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                if (field[x][y] == c && field[x+1][y] == c && field[x+2][y] == c && field[x+3][y] == c) return true;
            }
        }

        return false;
    }

    /**
     * Проверка на ничью
     * @return
     */
    static boolean checkDraw(){
        for (int x = 0; x < fieldSizeX; x++){
            for (int y = 0; y < fieldSizeY; y++)
                if (isCellEmpty(x, y)) return false;
        }
        return true;
    }

    /**
     * Метод проверки состояния игры
     * @param c
     * @param str
     * @return
     */
    static boolean gameCheck(char c, String str){
        if (checkWin(c)){
            System.out.println(str);
            return true;
        }
        if (checkDraw()){
            System.out.println("Ничья!");
            return true;
        }

        return false; // Игра продолжается
    }

}
