package org.everymove.svr.main.structs;

/**
 * A Transactional Move Object describing a move in a Chess Game
 */
public class MoveRequest 
{
    private String gameId;
    private String playerId;
    private String from;
    private String to;
    private String promotionChoice;

    public MoveRequest()
    {
        // empty
    }

    public String getGameId() 
    {
        return this.gameId;
    }

    public void setGameId(String gameId) 
    {
        this.gameId = gameId;
    }

    public String getPlayerId() 
    {
        return this.playerId;
    }

    public void setPlayerId(String playerId) 
    {
        this.playerId = playerId;
    }

    public String getFrom() 
    {
        return this.from;
    }

    public void setFrom(String from) 
    {
        this.from = from;
    }

    public String getTo() 
    {
        return this.to;
    }

    public void setTo(String to) 
    {
        this.to = to;
    }

    public String getPromotionChoice() 
    {
        return this.promotionChoice;
    }

    public void setPromotionChoice(String promotionChoice) 
    {
        this.promotionChoice = promotionChoice;
    }


    public MoveRequest gameId(String gameId) 
    {
        setGameId(gameId);
        return this;
    }

    public MoveRequest playerId(String playerId) 
    {
        setPlayerId(playerId);
        return this;
    }

    public MoveRequest from(String from) 
    {
        setFrom(from);
        return this;
    }

    public MoveRequest to(String to) 
    {
        setTo(to);
        return this;
    }

    public MoveRequest promotionChoice(String promotionChoice) 
    {
        setPromotionChoice(promotionChoice);
        return this;
    }
    
}
