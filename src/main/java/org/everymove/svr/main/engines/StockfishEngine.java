package org.everymove.svr.main.engines;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.andreinc.neatchess.client.UCI;
import net.andreinc.neatchess.client.model.Analysis;
import net.andreinc.neatchess.client.model.BestMove;
import net.andreinc.neatchess.client.model.Move;

@Component
public class StockfishEngine
{
    private static final Logger logger = LoggerFactory.getLogger(StockfishEngine.class);

    @Autowired
    public StockfishEngine()
    {
        // empty
    }

    /**
     * Indexes:
     * 0 - From Position
     * 1 - To Position
     * 2 - Promotion (null if no promotion)
     */
    public String [] getMove(String fen, int rating)
    {
        logger.info("Generating Move for Fen [{}]", fen);
        UCI uci = new UCI(120000L);
        uci.startStockfish();
        uci.setOption("MultiPV", "8");
        uci.setOption("UCI_LimitStrength", "true");
        uci.setOption("UCI_Elo", Integer.toString(rating));

        uci.uciNewGame();
        uci.positionFen(fen);

        try
        {
            return tryGettingBestMove(uci);
        }
        catch (Exception e)
        {
            return tryAgainUsingAnalysis(uci);
        }
    }

    private String [] tryGettingBestMove(UCI uci)
    {
        BestMove bestMove = uci.bestMove(9).getResultOrThrow();
        String promotion = bestMove.getCurrent().length() == 5 ? bestMove.getCurrent().substring(4, 5) : null;
        uci.close();
        return new String [] 
            { 
                bestMove.getCurrent().substring(0, 2), 
                bestMove.getCurrent().substring(2, 4),
                promotion
            };
    }

    private String [] tryAgainUsingAnalysis(UCI uci)
    {
        Analysis a = uci.analysis(18).getResultOrThrow();
        Move move = a.getBestMove();
        String promotion = move.getLan().length() == 5 ? move.getLan().substring(4, 5) : null;

        return new String [] 
            { 
                move.getLan().substring(0, 2), 
                move.getLan().substring(2, 4),
                promotion
            };
    }
    
    
}
