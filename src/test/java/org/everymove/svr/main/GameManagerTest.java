package org.everymove.svr.main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.bhlangonijr.chesslib.Board;

import org.everymove.svr.main.engines.StockfishEngine;
import org.everymove.svr.main.repositories.InMemoryTestRepository;
import org.everymove.svr.main.structs.Color;
import org.everymove.svr.main.structs.Computer;
import org.everymove.svr.main.structs.Game;
import org.everymove.svr.main.structs.GameRequest;
import org.everymove.svr.main.structs.MoveRequest;
import org.everymove.svr.main.structs.Player;
import org.everymove.svr.main.structs.Tester;
import org.everymove.svr.main.versus.MatchmakingQueue;
import org.everymove.svr.main.versus.MatchmakingQueueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class GameManagerTest 
{

    private static final String PLAYER_ONE = "Player One";
    private static final String PLAYER_TWO = "Player Two";

    private GameManager gameManager;

    private InMemoryTestRepository repo;

    private MatchmakingQueue queue;

    @Mock
    private SimpMessagingTemplate messenger;

    @Mock
    private StockfishEngine engine;

    private Game game;
    
    @BeforeEach
    public void init()
    {
        MockitoAnnotations.openMocks(this);
        this.repo = spy(new InMemoryTestRepository());
        this.queue = spy(new MatchmakingQueueService(repo));
        this.gameManager = new GameManager(repo, repo, queue, messenger, engine);

        setupOneGame();
    }

    private void setupOneGame()
    {
        this.game = new Game();
        this.game.setFen(new Board().getFen());
        this.game.setBlack(new Tester(PLAYER_ONE));
        this.game.setWhite(new Tester(PLAYER_TWO));

        this.repo.saveGame(game);
    }

    private MoveRequest openingMove()
    {
        MoveRequest move = new MoveRequest();
            move.setGameId(game.getId());
            move.setFrom("e2");
            move.setTo("e4");
            move.setPlayerId(PLAYER_ONE);

        return move;
    }

    private MoveRequest rebuttalToOpeningMove()
    {
        MoveRequest move = new MoveRequest();
            move.setGameId(game.getId());
            move.setFrom("e7");
            move.setTo("e5");
            move.setPlayerId(PLAYER_TWO);

        return move;
    }

    @Test
    public void testMakeMove_savesGame()
    {
        gameManager.makeMove(openingMove());
        verify(repo, atLeastOnce()).saveGame(game);
    }

    @Test
    public void testMakeMove_sendsMoveToBothPlayers()
    {
        gameManager.makeMove(openingMove());
        verify(messenger, atLeastOnce()).convertAndSendToUser(eq(PLAYER_ONE), eq(GameManager.GAME_MOVE_DEST), any(MoveRequest.class));
        verify(messenger, atLeastOnce()).convertAndSendToUser(eq(PLAYER_TWO), eq(GameManager.GAME_MOVE_DEST), any(MoveRequest.class));
    }

    @Test
    public void testMakeMove_lastMoveByPlayerIsTracked()
    {
        gameManager.makeMove(openingMove());
        assertEquals(game.getLastMoveMadeBy(), PLAYER_ONE);
        gameManager.makeMove(rebuttalToOpeningMove());
        assertEquals(game.getLastMoveMadeBy(), PLAYER_TWO);
    }

    @Test
    public void testMakeMove_FenIsUpdated()
    {
        final String currentFen = this.game.getFen();
        gameManager.makeMove(openingMove());

        final String updatedFen = this.game.getFen();
        assertNotEquals(currentFen, updatedFen);
    }

    @Test
    public void testMakeMove_movesAreAddedToGame()
    {
        final MoveRequest opening = openingMove();
        final MoveRequest rebuttal = rebuttalToOpeningMove();
        gameManager.makeMove(opening);
        gameManager.makeMove(rebuttal);

        assertTrue(game.getMoves().contains(opening));
        assertTrue(game.getMoves().contains(rebuttal));
    }

    private GameRequest simulateGameRequestFromClient(Player player)
    {
        GameRequest request = new GameRequest();
            request.setAccepted(false);
            request.setChallengerPlayerId(player.getName());
            request.setChallengerPlaysAs(null);
            request.setRating(200);
        return request;
    }

    private Game simulateMatchmaking(Player player)
    {
        Game g = new Game();
        g.setBlack(player);
        g.setWhite(new Tester());
        doReturn(null, g).when(queue).getMatch(any());
        return g;
    }

    @Test
    public void testRequestNewGame_matchmakingRequired()
    {
        final String tester = "i am the tester";
        Tester challenger = new Tester(tester);
        GameRequest request = simulateGameRequestFromClient(challenger);
        Game g = simulateMatchmaking(challenger);
        final String gameId = "1234";
        g.setId(gameId);

        gameManager.requestNewGame(challenger, request);

        assertEquals(gameId, request.getGameId());
        assertEquals(g.getWhite().getName(), request.getOpponentPlayerId());
        assertTrue(request.isAccepted());
    }

    @Test
    public void testRequestGame_againstComputer_asWhite()
    {
        final String tester = "i am the tester";
        Tester challenger = new Tester(tester);
        GameRequest request = simulateGameRequestFromClient(challenger);
        request.setChallengerPlaysAs(Color.WHITE);
        request.setOpponentPlayerId("COMPUTER");

        gameManager.requestNewGame(challenger, request);

        assertTrue(repo.getGame(request.getGameId()).getBlack() instanceof Computer);
        assertTrue(request.isAccepted());
    }

    private Game simulateGameForComputer()
    {
        Game g = new Game();
            g.setFen(new Board().getFen());
            g.setWhite(new Computer(500));
            g.setBlack(new Tester());
        return g;
    }

    @Test
    public void testMakeEngineMove()
    {
        Game g = simulateGameForComputer();
        repo.saveGame(g);

        final String startingFen = g.getFen();
        final Computer computer = (Computer) (g.getWhite());

        // Simulate Stockfish opening move for white
        when(
            engine.getMove(startingFen, computer.getRating()))
        .thenReturn(
            new String [] { "e2", "e4", null }
        );

        gameManager.makeEngineMove(g);
        assertEquals(computer.getName(), g.getLastMoveMadeBy());
        assertEquals(1, g.getMoves().size());
    }
}
