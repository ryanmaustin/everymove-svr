package org.everymove.svr.main.structs;

import java.util.List;

/**
 * An Object corresponding to a Chess Move made based on unique Starting and Ending positions
 */
public class Move 
{

    private String startingPosition;

    private String endingPosition;

    private List<Comment> comments;

    public Move() 
    {
        // empty
    }

    public String getStartingPosition() 
    {
        return this.startingPosition;
    }

    public void setStartingPosition(String startingPosition) 
    {
        this.startingPosition = startingPosition;
    }

    public String getEndingPosition() 
    {
        return this.endingPosition;
    }

    public void setEndingPosition(String endingPosition) 
    {
        this.endingPosition = endingPosition;
    }

    public List<Comment> getComments() 
    {
        return this.comments;
    }

    public void setComments(List<Comment> comments) 
    {
        this.comments = comments;
    }


    public Move startingPosition(String startingPosition) 
    {
        setStartingPosition(startingPosition);
        return this;
    }

    public Move endingPosition(String endingPosition) 
    {
        setEndingPosition(endingPosition);
        return this;
    }

    public Move comments(List<Comment> comments) 
    {
        setComments(comments);
        return this;
    }

}
