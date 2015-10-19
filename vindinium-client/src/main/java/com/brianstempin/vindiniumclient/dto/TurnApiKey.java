package com.brianstempin.vindiniumclient.dto;

import com.google.api.client.util.Key;

/**
 * Created by octavian on 19.10.15.
 */
public class TurnApiKey extends ApiKey {
    @Key
    private int turns;

    public TurnApiKey(String key, int turns) {
        super(key);
        this.turns = turns;
    }

    public int getTurns() { return turns; }
}
