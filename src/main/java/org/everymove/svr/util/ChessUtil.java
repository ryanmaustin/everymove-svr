package org.everymove.svr.util;

import java.util.Random;

import org.everymove.svr.main.structs.Color;
import org.everymove.svr.main.structs.Computer;
import org.everymove.svr.main.structs.Game;
import org.everymove.svr.main.structs.GameRequest;
import org.everymove.svr.main.structs.HumanPlayer;
import org.everymove.svr.main.structs.Player;

public class ChessUtil 
{
    private ChessUtil()
    {
        // hide
    }

    public static Color randomColor()
    {
        return new Random().nextInt(2) == 0 ?  Color.WHITE : Color.BLACK;
    }

    public static boolean flipCoin()
    {
        return new Random().nextInt(2) == 0;
    }

    public static String getWhitePlayerId(GameRequest gameRequest)
    {
        String challenger = gameRequest.getChallengerPlayerId();

        // If Challenger is White
        if (Color.WHITE.equals(gameRequest.getChallengerPlaysAs()))
        {
            return challenger;
        }
        return gameRequest.getOpponentPlayerId();
    }

    public static String getBlackPlayerId(GameRequest gameRequest)
    {
        String challenger = gameRequest.getChallengerPlayerId();

        // If Challenger is Black
        if (Color.BLACK.equals(gameRequest.getChallengerPlaysAs()))
        {
            return challenger;
        }
        return gameRequest.getOpponentPlayerId();
    }    

    public static Color getPlayerColor(Game game, Player player)
    {
        if (game.getBlack().equals(player)) return Color.BLACK;
        return Color.WHITE;
    }

    public static Player getOpponent(Game game, Player player)
    {
        if (game.getBlack().equals(player)) return game.getWhite();
        return game.getBlack();
    }

    public static String parsePromotionChoice(Color color, String piece)
    {
        if (piece == null) return null;
        if (piece.toUpperCase().startsWith("R"))
        {
            return color.toString() + "_ROOK";
        }
        else if (piece.toUpperCase().startsWith("N"))
        {
            return color.toString() + "_KNIGHT";
        }
        else if (piece.toUpperCase().startsWith("B"))
        {
            return color.toString() + "_BISHOP";
        }
        return color.toString() + "_QUEEN";
    }

    /**
     * Only use this method when there is one computer in a game
     */
    public static Player getComputer(Game game)
    {
        Player computer = game.getWhite();
        if (game.getBlack() instanceof Computer) return game.getBlack();
        return computer;
    }

    /**
     * Only use this method when there is one player in a game
     */
    public static Player getHuman(Game game)
    {
        Player computer = game.getWhite();
        if (game.getBlack() instanceof HumanPlayer) computer = game.getBlack();
        return computer;
    }
}
