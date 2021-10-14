package org.everymove.svr.main.repositories;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.everymove.svr.main.structs.Game;
import org.everymove.svr.main.structs.Move;
import org.everymove.svr.main.structs.Player;
import org.springframework.stereotype.Repository;

@Repository
public class DynamoDBRepository implements MovesRepository, PlayerRepository, GameRepository
{
    private Set<Game> games;
    private List<Player> players;

    public DynamoDBRepository()
    {
        this.games = new HashSet<>();
        this.players = new ArrayList<>();
    }


    @Override
    public Move update(Move move) 
    {
        return null;
    }

    @Override
    public Move get(Move move) 
    {
        return null;
    }

    @Override
    public Player get(String id) 
    {
        for (Player player : this.players)
        {
            if (player.getName().equals(id)) return player;
        }
        return null;
    }

    @Override
    public Game getGame(String gameId) 
    {
        for (Game game : this.games)
        {
            if (game.getId().equals(gameId)) return game;
        }
        return null;
    }

    @Override
    public Game saveGame(Game game) 
    {
        if (this.games.contains(game))
        {
            this.games.remove(game);
        }
        this.games.add(game);
        return game;
    }

    @Override
    public void add(Player player) 
    {
        this.players.add(player);
    }
    
}
