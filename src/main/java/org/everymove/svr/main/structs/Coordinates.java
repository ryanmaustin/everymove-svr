package org.everymove.svr.main.structs;

import java.util.ArrayList;
import java.util.List;

public class Coordinates
{
    private Coordinate [] [] coords;

    public Coordinates() 
    {
        this.coords = new Coordinate [8] [8];

        for (int x = 1; x <= 8; x++) {
            for (int y = 1; y <= 8; y++) {
                this.coords[x][y] = new Coordinate(x, y);
            }
        }
    }

    public Coordinate [] [] grid()
    {
        return this.coords;
    }
    
    /**
     * Returns a List of Coordinates that contain Pieces. The order of this list
     * is in natural grid order (left -> right, bottom -> top).
     */
    public List<Coordinate> getPieceCoordinates()
    {
        List<Coordinate> list = new ArrayList<>();
        for (int x = 1; x <= 8; x++) {
            for (int y = 1; y <= 8; y++) {
                final String piece = this.coords[x][y].getPiece();
                if (piece != null) {
                    list.add(this.coords[x][y]);
                }
            }
        }
        return list;
    }

    public void set(int x, int y, String piece) {

        Coordinate coord = new Coordinate(x, y);
        coord.setPiece(piece);
    }
}
