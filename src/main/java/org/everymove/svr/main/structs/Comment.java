package org.everymove.svr.main.structs;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A Comment made for a Chess Move
 */
public class Comment 
{
    
    /**
     * Unique Comment ID
     */
    private String cid;

    /**
     * The text of the Comment in HTML
     */
    private String text;

    /**
     * The last updated time of this comment
     */
    private LocalDateTime time;

    /**
     * Whether or not this comment has been edited
     */
    private boolean edited;

    /**
     * The Username of the User who posted the comment
     */
    private String username;

    /**
     * + or - votes
     */
    private Integer votes;

    /**
     * Replies to this Comment
     */
    private List<Comment> replies;

    public Comment() 
    {
        // empty
    }


    public String getCid() 
    {
        return this.cid;
    }

    public void setCid(String cid) 
    {
        this.cid = cid;
    }

    public String getText() 
    {
        return this.text;
    }

    public void setText(String text) 
    {
        this.text = text;
    }

    public LocalDateTime getTime() 
    {
        return this.time;
    }

    public void setTime(LocalDateTime time) 
    {
        this.time = time;
    }

    public boolean isEdited() 
    {
        return this.edited;
    }

    public boolean getEdited() 
    {
        return this.edited;
    }

    public void setEdited(boolean edited) 
    {
        this.edited = edited;
    }

    public String getUsername() 
    {
        return this.username;
    }

    public void setUsername(String username) 
    {
        this.username = username;
    }

    public Integer getVotes() 
    {
        return this.votes;
    }

    public void setVotes(Integer votes) 
    {
        this.votes = votes;
    }

    public List<Comment> getReplies() 
    {
        return this.replies;
    }

    public void setReplies(List<Comment> replies) 
    {
        this.replies = replies;
    }


    public Comment cid(String cid) 
    {
        setCid(cid);
        return this;
    }

    public Comment text(String text) 
    {
        setText(text);
        return this;
    }

    public Comment time(LocalDateTime time) 
    {
        setTime(time);
        return this;
    }

    public Comment edited(boolean edited) 
    {
        setEdited(edited);
        return this;
    }

    public Comment username(String username) 
    {
        setUsername(username);
        return this;
    }

    public Comment votes(Integer votes) 
    {
        setVotes(votes);
        return this;
    }

    public Comment replies(List<Comment> replies) 
    {
        setReplies(replies);
        return this;
    }

}
