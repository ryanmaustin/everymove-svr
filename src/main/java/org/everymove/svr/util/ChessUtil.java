package org.everymove.svr.util;

import java.util.Random;

import org.everymove.svr.main.structs.Color;
import org.everymove.svr.main.structs.Game;
import org.everymove.svr.main.structs.GameRequest;
import org.everymove.svr.main.structs.Player;

public class ChessUtil 
{
    private ChessUtil()
    {
        // hide
    }

    public static String randomColor()
    {
        return new Random().nextInt(2) == 0 ? "WHITE" : "BLACK";
    }

    public static boolean flipCoin()
    {
        return new Random().nextInt(2) == 0;
    }

    public static String getWhitePlayerId(GameRequest gameRequest)
    {
        String challenger = gameRequest.getChallengerPlayerId();

        // If Challenger is White
        if (Color.valueOf(gameRequest.getChallengerPlaysAs().toUpperCase()).equals(Color.WHITE))
        {
            return challenger;
        }
        return gameRequest.getOpponentPlayerId();
    }

    public static String getBlackPlayerId(GameRequest gameRequest)
    {
        String challenger = gameRequest.getChallengerPlayerId();

        // If Challenger is Black
        if (Color.valueOf(gameRequest.getChallengerPlaysAs().toUpperCase()).equals(Color.BLACK))
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
}
