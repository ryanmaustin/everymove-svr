package org.everymove.svr.main.structs;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * A unique Chess Board Position that can be reached from any number of routes
 * 
 * Format:
 * 
 * wK-
 * 
 */
public class Position extends HashMap<String, String> 
{
    public static final String WHITE = "W";
    public static final String WHITE_KING = WHITE + "K";
    public static final String WHITE_ROOK = WHITE + "R";
    public static final String WHITE_KNIGHT = WHITE + "N";
    public static final String WHITE_BISHOP = WHITE + "B";
    public static final String WHITE_QUEEN = WHITE + "Q";
    public static final String WHITE_PAWN = WHITE + "P";

    public static final String BLACK = "B";
    public static final String BLACK_KING = BLACK + "K";
    public static final String BLACK_ROOK = BLACK + "R";
    public static final String BLACK_KNIGHT = BLACK + "N";
    public static final String BLACK_BISHOP = BLACK + "B";
    public static final String BLACK_QUEEN = BLACK + "Q";
    public static final String BLACK_PAWN = BLACK + "P";

    private String serialized;

    private Coordinates coordinates;

    public Position()
    {
        super();
    }

    public String getSerialized() 
    {
        return this.serialized;
    }

    public Coordinates getCoordinates() 
    {
        return this.coordinates;
    }

    public static Position fromCoordinates(Coordinates coordinates) 
    {
        Position position = new Position();
            position.coordinates = coordinates;
        
        StringBuilder sb = new StringBuilder();
        final String SINGLE_SPACE = " ";
        
        for (Coordinate coord : coordinates.getPieceCoordinates())
        {
            sb.append(coord.getPiece().toUpperCase() + coord.getX() + coord.getY() + SINGLE_SPACE);
        }
        position.serialized = sb.toString().stripTrailing().replace(SINGLE_SPACE, "-");

        return position;
    }

    public static Position fromSerialized(String serialized) 
    {
        Position position = new Position();

        Coordinates coords = new Coordinates();
        for (String key : serialized.split(Pattern.quote("-")))
        {
            if (key.length() != 4) throw new IllegalArgumentException(key + " must be exactly 4 Characters");

            coords.set( 
                Integer.parseInt(key.substring(2, 2)), 
                Integer.parseInt(key.substring(3, 3)),
                key.substring(0, 2)
            );
        }
        
        position.coordinates = coords;
        position.serialized = serialized;
        return position;
    }

}
