package org.everymove.svr.main;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;

import org.everymove.svr.main.engines.StockfishEngine;
import org.everymove.svr.main.repositories.GameRepository;
import org.everymove.svr.main.repositories.PlayerRepository;
import org.everymove.svr.main.structs.Color;
import org.everymove.svr.main.structs.Computer;
import org.everymove.svr.main.structs.Game;
import org.everymove.svr.main.structs.GameRequest;
import org.everymove.svr.main.structs.MoveRequest;
import org.everymove.svr.main.structs.Player;
import org.everymove.svr.util.ChessUtil;
import org.everymove.svr.util.MatchTimeout;
import org.everymove.svr.util.Sleeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * This Game Manager performs the following functions:
 * - Initiates moves on behalf of players, ensuring that each move is legal
 * - Calls the Moves API to increment Hit Counts for every move
 * - Sends and Accepts new Game Requests on behalf of and for Players.
 * - Arbitrages the Stockfish Engine when the Player is challenging a computer
 */
@Service
public class GameManager 
{
    protected static final Logger logger = LoggerFactory.getLogger(GameManager.class);

    private static final String GAME_MOVE_DEST = "game/move";
    private static final String GAME_REQUESTS_DEST = "game/requests";

    protected GameRepository games;
    protected PlayerRepository players;
    protected PlayerQueue queue;
    protected SimpMessagingTemplate messenger;
    protected StockfishEngine engine;

    @Autowired
    public GameManager(
        GameRepository games, 
        PlayerRepository players, 
        PlayerQueue queue,
        SimpMessagingTemplate messenger,
        StockfishEngine engine
    )
    {
        this.games = games;
        this.players = players;
        this.queue = queue;
        this.messenger = messenger;
        this.engine = engine;
    }

    public void makeMove(MoveRequest moveRequest)
    {
        try
        {
            Game game = tryToGetGame(moveRequest.getGameId());

            Board board = new Board();
            board.loadFromFen(game.getFen());

            tryToMove(board, moveRequest);
            game.lastMoveMadeBy(moveRequest.getPlayerId());
            game.getMoves().add(moveRequest);
            game.setFen(board.getFen());
            logger.info("Move Made for Game [{}] -- {} to {}", moveRequest.getGameId(), moveRequest.getFrom(), moveRequest.getTo());
            tryToSaveGame(game);
            sendMove(game, moveRequest);            
        }
        catch (Exception e) { this.handleMoveFailed(moveRequest, e); }
    }

    private boolean isNowComputersTurn(Game game)
    {
        String playerId = game.getLastMoveMadeBy();
        
        // If the next player is white and they are a computer, return true
        if (game.getBlack().getName().equals(playerId))
        {
            return game.getWhite() instanceof Computer;
        }
        else // next player is black and a computer
        {   
            return game.getBlack() instanceof Computer;
        }
    }

    private void handleMoveFailed(MoveRequest moveRequest, Exception e)
    {
        logger.error(
            "Unable to make move [{} -> {}] for Game [{}] because: {}", 
            moveRequest.getFrom(),
            moveRequest.getTo(),
            moveRequest.getGameId(),
            e.getLocalizedMessage(),
            e
        );
    }

    protected Game tryToGetGame(String gameId)
    {
        return this.games.getGame(gameId);
    }

    protected void tryToSaveGame(Game game)
    {
        this.games.saveGame(game);
    }

    protected void tryToMove(Board board, MoveRequest moveRequest)
    {
        Move move = new Move(
            Square.fromValue(moveRequest.getFrom().toUpperCase()), 
            Square.fromValue(moveRequest.getTo().toUpperCase())
        );
        if (moveRequest.getPromotionChoice() != null)
        {
            move = new Move(
                Square.fromValue(moveRequest.getFrom().toUpperCase()), 
                Square.fromValue(moveRequest.getTo().toUpperCase()), 
                Piece.fromValue(moveRequest.getPromotionChoice())
            );
        }
        board.doMove(move, true);
    }

    /**
     * Sends the given move to all players in a game. If the next
     * turn is a computer, request the computer to make a move.
     */
    protected void sendMove(Game game, MoveRequest moveRequest)
    {
        // Player vs Computer
        if (againstComputer(game))
        {
            Player player = ChessUtil.getHuman(game);
            sendMoveRequestToPlayer(player.getName(), moveRequest);

            if (isNowComputersTurn(game))
            {
                Sleeper.sleep(200);
                makeEngineMove(game);
            }
            return;
        }
        // Player vs Player
        String blackId = game.getBlack().getProfile().getPlayerId();
        String whiteId = game.getWhite().getProfile().getPlayerId();

        sendMoveRequestToPlayer(blackId, moveRequest);
        sendMoveRequestToPlayer(whiteId, moveRequest);
    }

    private boolean againstComputer(Game game)
    {
        if (game.getBlack() instanceof Computer) return true;
        if (game.getWhite() instanceof Computer) return true;
        return false;
    }

    protected void sendMoveRequestToPlayer(String playerId, MoveRequest moveRequest)
    {
        messenger.convertAndSendToUser(
            playerId, 
            GAME_MOVE_DEST, 
            moveRequest
        );
    }

    /**
     * Requests a New Game. 
     * 
     * If the Game Request does not specify an opponent,
     * the Challenger will be placed in the Game Queue and will be matched with 
     * an opponent that is also looking to play.
     * 
     * If the Game Request does not specify a color, then one will be
     * chosen for them.
     */
    public void requestNewGame(Principal challenger, GameRequest gameRequest) 
    {   
        Player player = (Player) challenger;

        
        // Enter match making. This thread will block until a match is found
        if (gameRequest.getOpponentPlayerId() == null)
        {
            waitForMatch(player, gameRequest);
        }
        else if (gameRequest.getOpponentPlayerId().equals("COMPUTER")) 
        {
            playAgainstComputer(player, gameRequest);
            return;
        }

        resolveColor(gameRequest);
        handleNewGame(gameRequest);

        // Send a Request to the Opponent
        sendGameRequestToPlayer(
            gameRequest.getOpponentPlayerId(), 
            gameRequest
        );
    }

    /**
     * Starts a New Game for the Player against a Computer
     */
    private void playAgainstComputer(Player player, GameRequest gameRequest)
    {
        Color playsAs = ChessUtil.randomColor();
        Game game = new Game().againstComputer(player, playsAs, gameRequest.getRating());
        game.setFen(new Board().getFen());

        this.games.saveGame(game);

        updateGameRequest(game, gameRequest, player);
        sendGameRequestToPlayer(player.getName(), gameRequest);
        Sleeper.sleep(500L);

        if (playsAs.equals(Color.BLACK)) // make engine move for white
        {
            makeEngineMove(game); // move white
        }
    }

    private void makeEngineMove(Game game)
    {
        Player computer = ChessUtil.getComputer(game);
        if (!(computer instanceof Computer)) throw new IllegalStateException(computer.getName() + " is not a computer!");
        String [] move = this.engine.getMove(game.getFen(), ((Computer) computer).getRating());
        Color color = ChessUtil.getPlayerColor(game, computer);

        MoveRequest moveRequest = new MoveRequest();
            moveRequest.setFrom(move[0]);
            moveRequest.setTo(move[1]);
            moveRequest.setPromotionChoice(ChessUtil.parsePromotionChoice(color, move[2]));
            moveRequest.setGameId(game.getId());
            moveRequest.setPlayerId(computer.getName());

        makeMove(moveRequest);
    }

    private void handleNewGame(GameRequest gameRequest)
    {
        // If the game is accepted, then this becomes a new Game
        if (gameRequest.getGameId() == null && gameRequest.isAccepted())
        {
            Game newGame = new Game();
            
            Player black = getPlayer(ChessUtil.getBlackPlayerId(gameRequest));
            newGame.setBlack(black);
            newGame.setWhite(getPlayer(ChessUtil.getWhitePlayerId(gameRequest)));

            newGame.fen(new Board().getFen());
            newGame.setLastMoveMadeBy(black.getProfile().getPlayerId());
            newGame.setMoves(new ArrayList<>());
            gameRequest.setGameId(newGame.getId());

            logger.info("New Game Started: {} to {}", gameRequest.getChallengerPlayerId(), gameRequest.getOpponentPlayerId());
            this.games.saveGame(newGame);
        }
        else
        {
            logger.info("New Game Requested: {} to {}", gameRequest.getChallengerPlayerId(), gameRequest.getOpponentPlayerId());
        }
    }

    private Player getPlayer(String playerId)
    {
        logger.debug("Getting Player [{}]...", playerId);
        return this.players.get(playerId);
    }

    /**
     * Chooses a random color for the challenger if none is specified
     */
    private static void resolveColor(GameRequest request)
    {
        if (request.getChallengerPlaysAs() == null) 
            request.setChallengerPlaysAs(ChessUtil.randomColor().toString());
    }

    private void waitForMatch(Player player, GameRequest request)
    {
        queue.addPlayer(player);
        LocalDateTime timeout = LocalDateTime.now().plusMinutes(1L);
        while (timeout.isAfter(LocalDateTime.now()))
        {
            Game match = this.queue.getMatch(player);
            if (match != null)
            {
                updateGameRequest(match, request, player);
                return;
            }

            Sleeper.sleep(333L);
            logger.info("Player [{}] is still waiting for an opponent...", player.getName());
        }
        throw new MatchTimeout(player.getName());
    }

    /**
     * Updates the Game Request so that when it goes to the opponent,
     * they know who they are facing and what color they are playing as.
     * 
     * Also, sets the Game ID and accepted to true.
     */
    private void updateGameRequest(Game game, GameRequest request, Player player)
    {
        request.setOpponentPlayerId(ChessUtil.getOpponent(game, player).getName());
        request.setChallengerPlaysAs(ChessUtil.getPlayerColor(game, player).toString());
        request.setGameId(game.getId());

        // Set accepted to true because a match means that both players
        // want to play
        request.setAccepted(true);
    }

    public void acceptChallenge(Principal opponent, GameRequest gameRequest) 
    {
        if (opponent.getName().equals(gameRequest.getOpponentPlayerId()))
        {
            gameRequest.setAccepted(true);

            // When a Challenge is accepted, both the Challenger and the Opponent
            // should be notified that the game has started
            sendGameRequestToPlayer(gameRequest.getChallengerPlayerId(), gameRequest);
            sendGameRequestToPlayer(gameRequest.getOpponentPlayerId(), gameRequest);
        }
    }
    
    protected void sendGameRequestToPlayer(String playerId, GameRequest gameRequest)
    {
        messenger.convertAndSendToUser(
            playerId, 
            GAME_REQUESTS_DEST, 
            gameRequest
        );
    }
}
