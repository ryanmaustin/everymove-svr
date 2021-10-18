package org.everymove.svr.main.engines;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.bhlangonijr.chesslib.Board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StockfishEngineTest 
{
    protected static final Logger logger = LoggerFactory.getLogger(StockfishEngineTest.class);
    private StockfishEngine engine;

    @BeforeEach
    public void init()
    {
        this.engine = new StockfishEngine();
    }

    @Test
    public void testMakeMoveFromStart()
    {
        Board board = new Board();
        String [] move = this.engine.getMove(board.getFen(), 100);
        logger.info("Stockfish moved: {} {}", move[0], move[1]);
        assertEquals(2, move[0].length());
        assertEquals(2, move[1].length());
    }
    

    @Test
    public void testMakeMoveFromEndgamePosition()
    {
        String [] move = this.engine.getMove("2Q5/B2pknpp/B1p2p2/4p3/2p1P2P/1PN2NP1/2P2P2/R2QK2R w KQ - 2 23", 1000);
        logger.info("Stockfish moved: {} {}", move[0], move[1]);
        assertEquals(2, move[0].length());
        assertEquals(2, move[1].length());
    }
}
