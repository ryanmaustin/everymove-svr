package org.everymove.svr.main.versus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Map.Entry;

import com.github.bhlangonijr.chesslib.Board;

import org.everymove.svr.main.repositories.GameRepository;
import org.everymove.svr.main.structs.Game;
import org.everymove.svr.main.structs.Player;
import org.everymove.svr.util.ChessUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Eventually, this will be hosted on a dedicated server.
 */
@Service
public class MatchmakingQueueService implements MatchmakingQueue
{
    private Queue<Player> inQueue;

    public static class Match
    {
        private final Game game;
        private boolean whiteReady = false;
        private boolean blackReady = false;

        public Match(Game game)
        {
            this.game = game;
        }

        public void playerReady(Player player)
        {
            if (player.equals(game.getBlack())) blackReady = true;
            if (player.equals(game.getWhite())) whiteReady = true;
        }

        public boolean isForPlayer(Player player)
        {
            return game.getBlack().equals(player) || game.getWhite().equals(player);
        }

        public boolean isStarted()
        {
            return this.blackReady && this.whiteReady;
        }
    }

    /**
     * Using a map to track when both players start the match.
     * Once both players start the match, the match is removed 
     * from this queue.
     */
    private Map<Game, Match> matches;

    private GameRepository gameRepository;

    @Autowired
    public MatchmakingQueueService(GameRepository gameRepository)
    {
        inQueue = new LinkedList<>();
        matches = new HashMap<>();
        this.gameRepository = gameRepository;
    }

    public void addPlayer(Player player)
    {
        if (inQueue.contains(player)) return;
        inQueue.add(player);

        if (inQueue.size() > 1)
        {
            Game game = new Game();
            game.setFen(new Board().getFen());

            Player one = inQueue.poll();
            Player two = inQueue.poll();

            if (ChessUtil.flipCoin())
            {
                game.setBlack(one);
                game.setWhite(two);
                game.setLastMoveMadeBy(one.getName());
            }
            else
            {
                game.setBlack(two);
                game.setWhite(one);
                game.setLastMoveMadeBy(two.getName());
            }
            this.gameRepository.saveGame(game);
            this.matches.put(game, new Match(game));
        }
    }

    /**
     * Returns a match for a player if a match exists
     */
    public Game getMatch(Player player)
    {
        Iterator<Entry<Game, Match>> iter = matches.entrySet().iterator();
        while (iter.hasNext())
        {
            Entry<Game, Match> entry = iter.next();
            Game game = entry.getKey();
            Match match = entry.getValue();

            if (match.isForPlayer(player))
            {
                match.playerReady(player);
                if (match.isStarted()) iter.remove();
                return game;
            }
        }
        return null; // no match at this time
    }
    
}
