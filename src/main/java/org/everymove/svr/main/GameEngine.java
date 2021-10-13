package org.everymove.svr.main;

import java.security.Principal;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;

import org.everymove.svr.main.repositories.GameRepository;
import org.everymove.svr.main.structs.Game;
import org.everymove.svr.main.structs.GameRequest;
import org.everymove.svr.main.structs.MoveRequest;
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

    private static final String GAME_MOVE_DEST = "games/move";
    private static final String GAME_REQUESTS_DEST = "games/requests";

    protected GameRepository games;
    protected SimpMessagingTemplate messenger;

    @Autowired
    public GameEngine(GameRepository games, SimpMessagingTemplate messenger)
    {
        this.games = games;
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
            tryToSaveGame(game);
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
            e.getLocalizedMessage()
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
            Square.fromValue(moveRequest.getTo().toUpperCase()), 
            Piece.fromValue(moveRequest.getPromotionChoice())
        );
        board.doMove(move, true);
    }

    protected void tryToUpdateClients(Game game, MoveRequest moveRequest)
    {
        String blackId = game.getBlack().getProfile().getPlayerId();
        String whiteId = game.getBlack().getProfile().getPlayerId();

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

    public void requestNewGame(Principal challenger, GameRequest gameRequest) 
    {
        // Send a Request to the Opponent
        sendGameRequestToPlayer(
            gameRequest.getOpponentPlayerId(), 
            gameRequest
        );
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
