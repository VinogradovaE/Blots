/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package copy;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Kathryn
 */
public class Play implements Serializable {
    private static final char EMPTY    = ' ';  // _ - пустая клетка
    private static final char BASE0    = 'X';  // X - база 0 игрока
    private static final char BASE1    = 'O';  // O - база 1 игрока
    private static final char BUG0     = 'x';  // x - клоп 0 игрока
    private static final char BUG1     = 'o';  // o - клоп 1 игрока
    private static final char BEAT_BY0 = '#';  // # - клоп 1 игрока, раздавленный 0 игроком
    private static final char BEAT_BY1 = '@';  // @ - клоп 0 игрока, раздавленный 1 игроком
    private static final char HELP     = '*';  // * - клетка, в которую можно сходить
    
    char[][] field;
    Boolean[][] marks;
    int size;        // Размер поля
    
    ArrayList<String> log = new ArrayList<>();
    
    public Play(int size) {
        this.size = size;
        this.field = new char[size][size];
        this.marks = new Boolean[size][size];
        for (int i = 0; i < this.size; i++)
            for (int j = 0; j < this.size; j++) {
                this.field[i][j] = EMPTY;
                this.marks[i][j] = false;
            }
        this.field[this.size-2][1] = BASE0;
        this.field[1][this.size - 2] = BASE1;
    }
    
    void setCon(int x, int y, int status) {
        if (!this.marks[x][y]) {
            this.marks[x][y] = true;
            for (int i = -1; i < 2; i++) 
                for (int j = -1; j < 2; j++) {
                    int k = Math.max(0, Math.min(this.size - 1, x+i));
                    int l = Math.max(0, Math.min(this.size - 1, y+j));
                    if (status == 0) {
                        if (this.field[k][l] == BASE0 || this.field[k][l] == BUG0 || this.field[k][l] == BEAT_BY0)
                            setCon(k, l, status);
                        else if (this.field[k][l] == EMPTY) {
                            this.field[k][l] = HELP;
                            this.marks[k][l] = true;
                        } else if (this.field[k][l] == BUG1)
                            this.marks[k][l] = true;
                    } else {
                        if (this.field[k][l] == BASE1 || this.field[k][l] == BUG1 || this.field[k][l] == BEAT_BY1)
                            setCon(k, l, status);
                        else if (this.field[k][l] == EMPTY) {
                            this.field[k][l] = HELP;
                            this.marks[k][l] = true;
                        } else if (this.field[k][l] == BUG0)
                            this.marks[k][l] = true;
                    }
                }
        }
    }
    
    public void setConnection(int status) {
        for (int i = 0; i < this.size; i++) 
            for (int j = 0; j < this.size; j++) {
                if (this.field[i][j] == HELP)
                    this.field[i][j] = EMPTY;
                this.marks[i][j] = false;
            }
        if (status == 0)
            setCon(this.size - 2, 1, status);
        else
            setCon(1, this.size - 2, status);
    }
    
    public Boolean checkInput(String s, int status) {
        Boolean flag = false;
        for (int k = 0; k < this.size; k++) 
            for (int l = 0; l < this.size; l++)
                if (this.marks[k][l] == true && this.field[k][l] != BASE0 && this.field[k][l] != BASE1)
                    flag = true;
        if (!flag)
            this.log.add("У вас не оталось ходов. Вы проиграли :C");
        String[] coords = s.split(",");
        int i, j;
        try {
            i = Integer.parseInt(coords[0]);
            j = Integer.parseInt(coords[1]);
        } catch(NumberFormatException ne) {
            this.log.add("Неверно введены координаты :(");
            return false;
        }
        if (i < 0 || i >= this.size || j < 0 || j >= this.size) {
            this.log.add("Координаты за пределами поля!");
            return false;
        }
        if (this.field[i][j] == BASE0 || this.field[i][j] == BASE1) {
            this.log.add("(" + i + ", " + j + "): На базу нельзя нападать");
            return false;
        }
        if (status == 0 && this.field[i][j] == BUG0 || status == 1 && this.field[i][j] == BUG1) {
            this.log.add("(" + i + ", " + j + "): Тут уже стоит ваш клоп");
            return false;
        }
        if (this.field[i][j] == BEAT_BY0 || this.field[i][j] == BEAT_BY1) {
            this.log.add("(" + i + ", " + j + "): Не тревожьте павших");
            return false;
        }
        if (this.marks[i][j] == false) {
            this.log.add("(" + i + ", " + j + "): Эта клетка не связана с вашей базой");
            return false;
        }
        return true;
    }
    
    public void setMark(int i, int j, int status) {
        switch (this.field[i][j]) {
            case BUG0: 
                if (status == 1) 
                    this.field[i][j] = BEAT_BY1;
                break;
            case BUG1: 
                if (status == 0) 
                    this.field[i][j] = BEAT_BY0;
                break;
            default:
                this.field[i][j] = (status == 0) ? BUG0 : BUG1;
                break;
        }
    }
    
    public void makeMove(String s, int status) {
        String[] ms = s.split("-");
        for (String m: ms) {
            String[] coords = m.split(",");
            int i = Integer.parseInt(coords[0]);
            int j = Integer.parseInt(coords[1]);
            setMark(i, j, status);
        }
    }
    
    public String getTextField() {
        String horisontal = "---+";
        String text_field = "   |";
        for (int i = 0; i < size; i++) {
            text_field += " " + i + " |";
            horisontal += "---+";
        }
        text_field += "\n";
        horisontal += "\n";
        text_field += horisontal;
        for (int i = 0; i < size; i++) {
            text_field += " " + i + " |";
            for (int j = 0; j < size; j++)
                text_field += " " + this.field[i][j] + " " + "|";
            text_field += "\n" + horisontal;
        }
        return text_field;
    }
    
    public String getLog(int status, int moves) {
        char st = (status == 0) ? BASE0 : BASE1;
        String log = "Игрок " + st + "\nОсталось ходов: " + moves + "\n\n";
        for (String s: this.log)
            log += s + "\n";
        return log;
    }
}
