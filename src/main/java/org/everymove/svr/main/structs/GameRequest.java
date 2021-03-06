package org.everymove.svr.main.structs;

public class GameRequest 
{
    private String challengerPlayerId;
    private Color challengerPlaysAs;
    private String opponentPlayerId;
    private int clockInSeconds;
    private int incrementInSeconds;
    private boolean accepted = false;
    private String gameId;
    private int rating = 1000;

    public GameRequest() 
    {
        // empty
    }

    public String getChallengerPlayerId() 
    {
        return this.challengerPlayerId;
    }

    public void setChallengerPlayerId(String challengerPlayerId) 
    {
        this.challengerPlayerId = challengerPlayerId;
    }

    public Color getChallengerPlaysAs() 
    {
        return this.challengerPlaysAs;
    }

    public void setChallengerPlaysAs(Color challengerPlaysAs) 
    {
        this.challengerPlaysAs = challengerPlaysAs;
    }

    public String getOpponentPlayerId() 
    {
        return this.opponentPlayerId;
    }

    public void setOpponentPlayerId(String opponentPlayerId) 
    {
        this.opponentPlayerId = opponentPlayerId;
    }

    public int getClockInSeconds() 
    {
        return this.clockInSeconds;
    }

    public void setClockInSeconds(int clockInSeconds) 
    {
        this.clockInSeconds = clockInSeconds;
    }

    public int getIncrementInSeconds() 
    {
        return this.incrementInSeconds;
    }

    public void setIncrementInSeconds(int incrementInSeconds) 
    {
        this.incrementInSeconds = incrementInSeconds;
    }

    public void setRating(Integer rating)
    {
        if (rating != null) this.rating = rating;
    }

    public int getRating()
    {
        return this.rating;
    }

    public boolean isAccepted() 
    {
        return this.accepted;
    }

    public boolean getAccepted() 
    {
        return this.accepted;
    }

    public void setAccepted(boolean accepted) 
    {
        this.accepted = accepted;
    }

    public String getGameId()
    {
        return this.gameId;
    }

    public void setGameId(String gameId)
    {
        this.gameId = gameId;
    }

}
