package org.everymove.svr.main;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;

import org.everymove.svr.main.repositories.GameRepository;
import org.everymove.svr.main.repositories.PlayerRepository;
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
 * Game Engine performs the following functions:
 * - Initiates moves on behalf of players, ensuring that each move is legal
 * - Calls the Moves API to increment Hit Counts for every move
 * - Sends and Accepts new Game Requests on behalf of and for Players.
 * - Arbitrages the Stockfish Engine when the Player is challenging a computer
 */
@Service
public class GameEngine 
{
    protected static final Logger logger = LoggerFactory.getLogger(GameEngine.class);

    private static final String GAME_MOVE_DEST = "game/move";
    private static final String GAME_REQUESTS_DEST = "game/requests";

    protected GameRepository games;
    protected PlayerRepository players;
    protected PlayerQueue queue;
    protected SimpMessagingTemplate messenger;

    @Autowired
    public GameEngine(
        GameRepository games, 
        PlayerRepository players, 
        PlayerQueue queue,
        SimpMessagingTemplate messenger
    )
    {
        this.games = games;
        this.players = players;
        this.queue = queue;
        this.messenger = messenger;
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
            tryToUpdateClients(game, moveRequest);            
        }
        catch (Exception e) { this.handleMoveFailed(moveRequest, e); }
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

    protected void tryToUpdateClients(Game game, MoveRequest moveRequest)
    {
        String blackId = game.getBlack().getProfile().getPlayerId();
        String whiteId = game.getWhite().getProfile().getPlayerId();

        sendMoveRequestToPlayer(blackId, moveRequest);
        sendMoveRequestToPlayer(whiteId, moveRequest);
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

        // Look for an Opponent
        if (gameRequest.getOpponentPlayerId() == null)
        {
            waitForMatch(player, gameRequest);
        }
        else if (gameRequest.getOpponentPlayerId().equals("COMPUTER")) 
        {
            // TODO: Handle computer
        }

        resolveColor(gameRequest);
        handleNewGame(gameRequest);

        // Send a Request to the Opponent
        sendGameRequestToPlayer(
            gameRequest.getOpponentPlayerId(), 
            gameRequest
        );
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
        if (request.getChallengerPlaysAs() == null) request.setChallengerPlaysAs(ChessUtil.randomColor());
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
