package org.everymove.svr.main;

import java.util.List;

import org.everymove.svr.main.structs.Comment;
import org.everymove.svr.main.structs.Move;
import org.everymove.svr.util.IllegalInput;
import org.everymove.svr.util.MoveValidation;
import org.everymove.svr.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MovesAPI 
{
    protected static final Logger logger = LoggerFactory.getLogger(MovesAPI.class);

    protected final Moves moves;

    @Autowired
    public MovesAPI(Moves moves) 
    {
        this.moves = moves;
    }

    @PutMapping
    @ResponseBody
    public Response moveComment(
        @RequestParam String startingPosition,
        @RequestParam String endingPosition,
        @RequestBody List<Comment> comments
    ) 
    {
        try
        {
            Move move = new Move()
                .startingPosition(startingPosition)
                .endingPosition(endingPosition)
                .comments(comments);

            MoveValidation.validate(move);   
            logger.info("Adding {} Comments for Move [{} -> {}]", comments.size(), startingPosition, endingPosition);
            return Response.ok(this.moves.update(move));
        }
        catch (Exception e)
        {
            return handleException(e);
        }
    } 

    @GetMapping
    @ResponseBody
    public Response getComments(
        @RequestParam String startingPosition,
        @RequestParam String endingPosition
    )
    {
        try
        {
            Move move = new Move()
                .startingPosition(startingPosition)
                .endingPosition(endingPosition);

            MoveValidation.validate(move);   

            move = this.moves.get(move);

            logger.info("Fetched {} Comments for Move [{} -> {}]", move.getComments().size(), startingPosition, endingPosition);
            return Response.ok(move.getComments());
        }
        catch (Exception e)
        {
            return handleException(e);
        }
    }

    public static Response handleException(Exception e)
    {
        if (e instanceof IllegalInput)
        {
            return Response.error(400, e.getLocalizedMessage());
        }
        else
        {
            return internalServerError();
        }
    }

    private static Response internalServerError()
    {
        return Response.error(500, "ISE");
    }
}
