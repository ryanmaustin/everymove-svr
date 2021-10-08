package org.everymove.svr.main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import java.util.List;

import org.everymove.svr.main.structs.Move;
import org.everymove.svr.util.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MovesAPITest 
{

    private MovesAPI api;

    @Mock
    private Moves updater;

    @Before
    public void init()
    {
        MockitoAnnotations.openMocks(this);
        this.api = new MovesAPI(updater);
    }

    @Test
    public void testIllegalMovesReturn400()
    {
        Response response = api.moveComment(null, null, List.of());
        assertEquals(400, response.getBody().getStatusCode());
    }

    @Test
    public void testExceptionCaughtReturns500()
    {
        doThrow(NullPointerException.class).when(this.updater).update(any(Move.class));
        Response response = api.moveComment("start", "end", List.of());
        assertEquals(500, response.getBody().getStatusCode());
    }
}
