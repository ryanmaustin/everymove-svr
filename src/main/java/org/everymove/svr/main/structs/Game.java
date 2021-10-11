package org.everymove.svr.main.structs;

import java.time.LocalDateTime;
import java.util.List;

public class Game 
{
    private String id;
    private Player white;
    private Player black;
    private List<Move> moves;    
    private LocalDateTime startTime;

    public Game()
    {
        this.startTime = LocalDateTime.now();
    }

    public String getId() 
    {
        return this.id;
    }

    public void setId(String id) 
    {
        this.id = id;
    }

    public Player getWhite() 
    {
        return this.white;
    }

    public void setWhite(Player white) 
    {
        this.white = white;
    }

    public Player getBlack() 
    {
        return this.black;
    }

    public void setBlack(Player black) 
    {
        this.black = black;
    }

    public List<Move> getMoves() 
    {
        return this.moves;
    }

    public void setMoves(List<Move> moves) 
    {
        this.moves = moves;
    }

    public LocalDateTime getStartTime() 
    {
        return this.startTime;
    }

    public void setStartTime(LocalDateTime startTime) 
    {
        this.startTime = startTime;
    }

    public Game id(String id) 
    {
        setId(id);
        return this;
    }

    public Game white(Player white) 
    {
        setWhite(white);
        return this;
    }

    public Game black(Player black) 
    {
        setBlack(black);
        return this;
    }

    public Game moves(List<Move> moves) 
    {
        setMoves(moves);
        return this;
    }

    public Game startTime(LocalDateTime startTime) 
    {
        setStartTime(startTime);
        return this;
    }

    public static Game fromExisting(Game game)
    {
        return new Game() 
            .startTime(game.startTime)
            .id(game.id)
            .black(game.black)
            .white(game.white);
    }

}
