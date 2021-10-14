package org.everymove.svr.main.structs;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Game 
{
    /**
     * The Game ID
     */
    private String id;

    /**
     * The Player playing white
     */
    private Player white;

    /**
     * The Player playing black
     */
    private Player black;

    /**
     * List of successful Move Request made
     */
    private List<MoveRequest> moves;    

    /**
     * The Time that the Game started (Local to the Server)
     */
    private LocalDateTime startTime;

    /**
     * The FEN Position of the Board
     */
    private String fen;

    /**
     * The ID of the Player who made the last move
     */
    private String lastMoveMadeBy;

    public Game()
    {
        this.startTime = LocalDateTime.now();
        this.id = UUID.randomUUID().toString();
        this.moves = new ArrayList<>();
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

    public List<MoveRequest> getMoves() 
    {
        return this.moves;
    }

    public void setMoves(List<MoveRequest> moves) 
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

    public String getFen() 
    {
        return this.fen;
    }

    public void setFen(String fen) 
    {
        this.fen = fen;
    }

    public String getLastMoveMadeBy()
    {
        return this.lastMoveMadeBy;
    }
    
    public void setLastMoveMadeBy(String lastMoveMadeBy)
    {
        this.lastMoveMadeBy = lastMoveMadeBy;
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

    public Game moves(List<MoveRequest> moves) 
    {
        setMoves(moves);
        return this;
    }

    public Game startTime(LocalDateTime startTime) 
    {
        setStartTime(startTime);
        return this;
    }

    public Game fen(String fen) 
    {
        setFen(fen);
        return this;
    }

    public Game lastMoveMadeBy(String lastMoveMadeBy)
    {
        this.setLastMoveMadeBy(lastMoveMadeBy);
        return this;
    }

    public static Game fromExisting(Game game)
    {
        return new Game() 
            .startTime(game.startTime)
            .id(game.id)
            .black(game.black)
            .white(game.white)
            .moves(game.moves)
            .fen(game.fen);
    }

    @Override
    public int hashCode()
    {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Game)) return false;
        return this.id.equals(((Game) o).id);
    }
}
