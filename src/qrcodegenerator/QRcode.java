/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qrcodegenerator;

/**
 *
 * @author aapom
 */
public class QRcode {

    private final int version;
    private final ErrorLevel error_level;
    private final int masking;
    private final int[][] squares;
    private final int size;

    public QRcode(int version, ErrorLevel err, int mask) {
        if (version < 1 || version > 40) {
            version = 1;
        }
        this.version = version;
        error_level = err;
        if (mask < 0 || mask >= 8) {
            mask = 0;
        }
        masking = mask;
        size = 17 + 4 * this.version;
        squares = new int[size][size];
        fixed_squares();
    }

    private void fixed_squares() {
        figure1(0, 0);
        figure1(0, size - 7);
        figure1(size - 7, 0);
        figure2(size - 9, size - 9); // this works only to versions with one alignment
        figure3();
        figure4();
        squares[size - 8][8] = 1;

        mask_data();
    }

    private void figure1(int x, int y) {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (i < 0 || i >= size || j < 0 || j >= size) {
                    continue;
                }
                if (i == 0 || j == 0 || i == 6 || j == 6) {
                    squares[x + i][y + j] = 1;
                } else if (i >= 2 && i <= 4 && j >= 2 && j <= 4) {
                    squares[x + i][y + j] = 1;
                }
            }
        }
    }

    private void figure2(int x, int y) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i < 0 || i >= size || j < 0 || j >= size) {
                    continue;
                }
                if (i == 0 || j == 0 || i == 4 || j == 4) {
                    squares[x + i][y + j] = 1;
                } else if (i == 2 && j == 2) {
                    squares[x + i][y + j] = 1;
                }
            }
        }
    }

    private void figure3() {
        for (int i = 8; i < size - 8; i += 2) {
            squares[6][i] = 1;
            squares[i][6] = 1;
        }
    }

    private void figure4() {
        int error_mask = ((error_level.bits() << 3) + masking);
        int value = error_mask << 10;
        while (value >= (1 << 10)) {
            int mask = 0b10100110111 << 5;
            for (int i = 1 << 15; i > (1 << 10); i >>= 1) {
                if ((i & value) > 0) {
                    break;
                }
                mask >>= 1;
            }
            value ^= mask;
        }
        int almost_end = value + (error_mask << 10);
        int end = almost_end ^ 0b101010000010010;

        int j = 0;
        for (int i = 1 << 14; i > 0; i >>= 1) {
            if ((end & i) > 0) {
                if (j < 6) {
                    squares[8][j] = 1;
                    squares[size - 1 - j][8] = 1;
                } else if (j == 6) {
                    squares[8][7] = 1;
                    squares[size - 1 - j][8] = 1;
                } else if (j == 7) {
                    squares[8][8] = 1;
                    squares[8][size - 8] = 1;
                } else if (j == 8) {
                    squares[7][8] = 1;
                    squares[8][size - 7] = 1;
                } else {
                    squares[14 - j][8] = 1;
                    squares[8][size - 15 + j] = 1;
                }
            }
            j++;
        }
    }

    private boolean data_position(int i, int j) {

        if (i == 6) {
            return false;
        }
        if (j == 6) {
            return false;
        }
        if (i < 9 && j < 9) {
            return false;
        }
        if (i < 9 && j > size - 9) {
            return false;
        }
        if (i > size - 9 && j < 9) {
            return false;
        }
        if (i > size - 10 && j > size - 10
                && i < size - 4 && j < size - 4) {
            return false;
        }

        return true;
    }

    public void add_data(byte[] bytes) {
        clear();
        int a = 0;
        int n = 0;
        for (int x = size - 1; x >= 0; x -= 2) {
            if (x==6) {
                x=5;
            }
            for (int y = 0; y < size; y++) {
                int z=y;
                if (a % 2 == 0) {
                    z = size-1-y;
                }
                for (int k=0; k<2; k++) {
                    int j=x-k;
                    int i=z;
                    if (data_position(i,j)) {
                        int b = n/8;
                        int c = n%8;
                        if (b>=bytes.length) {
                            continue;
                        }
                        if ((bytes[b] & (1 << 7-c))==0) {
                            squares[i][j] = 1;
                        }
                        n++;
                    }
                }
            }
            a++;
        }
        mask_data();
    }

    private void clear() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (data_position(i, j)) {
                    squares[i][j] = 0;
                }
            }
        }
    }

    private void mask_data() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!data_position(i, j)) {
                    continue;
                }
                switch (masking) {
                    case 0:
                        if ((i + j) % 2 != 0) {
                            continue;
                        }
                        break;
                    case 1:
                        if (i % 2 != 0) {
                            continue;
                        }
                        break;
                    case 2:
                        if (j % 3 != 0) {
                            continue;
                        }
                        break;
                    case 3:
                        if ((i + j) % 3 != 0) {
                            continue;
                        }
                        break;
                    case 4:
                        if ((i / 2 + j / 3) % 2 != 0) {
                            continue;
                        }
                        break;
                    case 5:
                        if (((i * j) % 2) + ((i * j) % 3) != 0) {
                            continue;
                        }
                        break;
                    case 6:
                        if ((((i * j) % 2) + ((i * j) % 3)) % 2 != 0) {
                            continue;
                        }
                        break;
                    case 7:
                        if ((((i + j) % 2) + ((i * j) % 3)) % 2 != 0) {
                            continue;
                        }
                    default:
                        break;
                }

                if (squares[i][j] > 0) {
                    squares[i][j] = 0;
                } else {
                    squares[i][j] = 1;
                }
            }
        }
    }

    public String toString() {
        String ret = "";
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (squares[i][j] > 0) {
                    ret += "#";
                } else {
                    ret += " ";
                }
                // ret += ""+squares[i][j];
            }
            ret += "\n";
        }
        return ret;
    }

    public String toNumbers() {
        String ret = "";
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                ret += "" + (squares[i][j] % 10);
            }
            ret += "\n";
        }
        return ret;
    }
}
